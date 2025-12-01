define(
    [
        'app/desktop/modules/mfr/mfr.module',
        'moment',
        'moment-timezone-with-data',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/lifecycleService',
        'app/desktop/modules/mfr/directive/manufacturerDirective',
        'app/shared/services/core/itemTypeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],

    function (module) {
        module.controller('NewMfrController', NewMfrController);

        function NewMfrController($scope, $q, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                  MfrService, ItemTypeService, LifecycleService, ObjectAttributeService, AttachmentService, AttributeAttachmentService) {

            var parse = angular.element("<div></div>");
            var vm = this;
            vm.valid = true;

            vm.create = create;
            vm.onSelectType = onSelectType;
            vm.Manufacturers = [];
            vm.objectAttributes = [];
            vm.reqObjectAttributes = [];
            vm.attributes = [];
            vm.newManufacture = {
                id: null,
                mfrType: null,
                name: null,
                description: null,
                phoneNumber: null,
                contactPerson: null,
                lifeCycle: null,
                workflowDefinition: null,
                workflowDefId: null
            };

            vm.choices = [
                {
                    name: "True",
                    value: true
                },
                {
                    name: "False",
                    value: false
                }
            ];

            function loadMfrAttributeDefs() {
                vm.objectAttributes = [];
                vm.reqObjectAttributes = [];
                ItemTypeService.getAllTypeAttributes("MANUFACTURER").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newManufacture.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                listValue: null,
                                mlistValue: [],
                                newListValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                integerValue: null,
                                stringValue: null,
                                doubleValue: null,
                                imageValue: null,
                                refValue: null,
                                timestampValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.dataType == "TEXT") {
                                att.stringValue = attribute.defaultTextValue;
                            }
                            if (attribute.dataType == "LIST" && !attribute.listMultiple && attribute.defaultListValue != null) {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.dataType == "LIST" && attribute.listMultiple && attribute.defaultListValue != null) {
                                att.mlistValue.push(attribute.defaultListValue);
                            }
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss")
                            }
                            if (attribute.required == false) {
                                vm.objectAttributes.push(att);
                            } else {
                                vm.reqObjectAttributes.push(att);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    })
            }

            function onSelectType(mfrType) {
                vm.attributes = [];
                if (mfrType != null && mfrType != undefined) {
                    vm.newManufacture.mfrType = mfrType;
                    MfrService.getMfrAttributesWithHierarchy(vm.newManufacture.mfrType.id).then(
                        function (data) {
                            angular.forEach(data, function (attribute) {
                                var att = {
                                    id: {
                                        objectId: vm.newManufacture.id,
                                        attributeDef: attribute.id

                                    },
                                    attributeDef: attribute,
                                    listValue: null,
                                    mlistValue: [],
                                    newListValue: null,
                                    listValueEditMode: false,
                                    booleanValue: false,
                                    integerValue: null,
                                    stringValue: null,
                                    doubleValue: null,
                                    imageValue: null,
                                    refValue: null,
                                    timestampValue: null,
                                    ref: null,
                                    attachmentValues: []
                                };
                                if (attribute.lov != null) {
                                    att.lovValues = attribute.lov.values;
                                }
                                if (attribute.dataType == "TEXT") {
                                    att.stringValue = attribute.defaultTextValue;
                                }
                                if (attribute.dataType == "LIST" && !attribute.listMultiple && attribute.defaultListValue != null) {
                                    att.listValue = attribute.defaultListValue;
                                }
                                if (attribute.dataType == "LIST" && attribute.listMultiple && attribute.defaultListValue != null) {
                                    att.mlistValue.push(attribute.defaultListValue);
                                }
                                if (attribute.dataType == "TIMESTAMP") {
                                    att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss")
                                }
                                if (attribute.validations != null && attribute.validations != "") {
                                    attribute.newValidations = JSON.parse(attribute.validations);
                                }
                                vm.attributes.push(att);
                            });
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                    loadWorkflows();
                }
            }

            function create() {
                var dfd = $q.defer();
                vm.validattributes = [];
                vm.validObjectAttributes = [];
                vm.newMfrName = [];
                if (validate()) {
                    MfrService.getByName(vm.newManufacture.name).then(
                        function (data) {
                            vm.newMfrName = data;
                            if (vm.newMfrName != "") {
                                $rootScope.showErrorMessage(mfrNameAlreadyExist);
                            } else {
                                $rootScope.showBusyIndicator($('#rightSidePanel'));
                                vm.creating = true;
                                angular.forEach(vm.attributes, function (attribute) {
                                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                                        if ($rootScope.checkAttribute(attribute)) {
                                            vm.validattributes.push(attribute);
                                        }
                                        else {
                                            $rootScope.showErrorMessage(attribute.attributeDef.name + ":" + "Attribute is Required");
                                        }
                                    } else {
                                        vm.validattributes.push(attribute);
                                    }
                                });
                                angular.forEach(vm.reqObjectAttributes, function (attribute) {
                                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                                        if ($rootScope.checkAttribute(attribute)) {
                                            vm.validObjectAttributes.push(attribute);
                                        }
                                        else {
                                            $rootScope.showErrorMessage(attribute.attributeDef.name + ":" + "Attribute is Required");
                                        }
                                    } else {
                                        vm.validObjectAttributes.push(attribute);
                                    }
                                });
                                if (vm.attributes.length == vm.validattributes.length &&
                                    vm.reqObjectAttributes.length == vm.validObjectAttributes.length) {
                                    if (vm.newManufacture.workflowDefinition != null) {
                                        vm.newManufacture.workflowDefId = vm.newManufacture.workflowDefinition.id;
                                    }
                                    MfrService.createManufacture(vm.newManufacture).then(
                                        function (data) {
                                            vm.newManufacture = data;
                                            saveObjectAttributes().then(
                                                function (object) {
                                                    saveAttributes().then(
                                                        function (att) {
                                                            $rootScope.showSuccessMessage(mfrNewItem);
                                                            $scope.callback(data);
                                                            vm.attributes = [];
                                                            dfd.resolve();
                                                            $rootScope.hideBusyIndicator();
                                                        }
                                                    )
                                                }
                                            )
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            dfd.reject();
                                        }
                                    )
                                } else {
                                    $rootScope.hideBusyIndicator();
                                }
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
                return dfd.promise;
            }

            var mfrNewItem = parse.html($translate.instant("MANUFACTURER_CREATED_MESSAGE")).html();
            var mfrTypeValidate = parse.html($translate.instant("MANUFACTURER_TYPE_VALIDATE")).html();
            var mfrNameValidate = parse.html($translate.instant("MANUFACTURER_NAME_VALIDATE")).html();
            var mfrDesValidate = parse.html($translate.instant("MANUFACTURER_DESCRIPTION_VALIDATE")).html();
            var mfrPhoneNumberValidate = parse.html($translate.instant("MANUFACTURER_PHONE_NUMBER_VALIDATE")).html();
            var mfrPersonValidate = parse.html($translate.instant("MANUFACTURER_CONTACT_PERSON_VALIDATE")).html();
            var mfrNameAlreadyExist = parse.html($translate.instant("MANUFACTURER_NAME_EXIST")).html();
            var validPhoneNumber = parse.html($translate.instant("ENTER_VALID_PHONE_NUMBER")).html();
            $scope.selectWorkflow = parse.html($translate.instant("SELECT_WORKFLOW")).html();

            function saveAttributes() {
                var defered = $q.defer();
                vm.imageAttributes = [];
                var images = new Hashtable();
                if (vm.attributes.length > 0) {
                    angular.forEach(vm.attributes, function (attribute) {
                        attribute.id.objectId = vm.newManufacture.id;

                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        /*if (attribute.timeValue != null) {
                         attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");
                         }*/
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length != null) {
                            attribute.attachmentValues = addAttachment(attribute)
                        }
                    });
                    $timeout(function () {
                        MfrService.saveMfrAttributes(vm.newManufacture.id, vm.attributes).then(
                            function (data) {

                                if (vm.imageAttributes.length > 0) {
                                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                                        MfrService.updateMfrImageValue(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
                                            function (data) {
                                                vm.newManufacture = {
                                                    mfrType: null,
                                                    name: null,
                                                    description: null,
                                                    phoneNumber: null,
                                                    contactPerson: null
                                                };
                                                defered.resolve();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        )
                                    })
                                } else {
                                    defered.resolve();
                                }
                            }, function () {
                                defered.reject();
                            })
                    }, 2000)
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function saveObjectAttributes() {
                var defered = $q.defer();
                var propertyImages = new Hashtable();
                vm.propertyImageAttributes = [];
                var objectAttrs = vm.objectAttributes.concat(vm.reqObjectAttributes);
                if (objectAttrs.length > 0) {
                    angular.forEach(objectAttrs, function (attribute) {
                        attribute.id.objectId = vm.newManufacture.id;

                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            propertyImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        /* if (attribute.timeValue != null) {
                         attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");
                         }*/
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            attribute.attachmentValues = addMfrPropertyAttachment(attribute);
                        }
                    });
                    $timeout(function () {
                        ObjectAttributeService.saveItemObjectAttributes(vm.newManufacture.id, objectAttrs).then(
                            function (data) {
                                if (vm.propertyImageAttributes.length > 0) {
                                    angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                        ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, propertyImages.get(propImgAtt.id.attributeDef)).then(
                                            function (data) {
                                                defered.resolve();
                                            }
                                        )
                                    })
                                } else {
                                    defered.resolve();
                                }
                                defered.resolve();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                defered.reject();
                            }
                        )
                    }, 2000);
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function anotherItem() {
                create().then(function () {
                    vm.newManufacture = {
                        id: null,
                        mfrType: null,
                        name: null,
                        description: null,
                        phoneNumber: null,
                        contactPerson: null,
                        workflowDefinition: null,
                        workflowDefId: null
                    };
                    $scope.callback();
                    $rootScope.hideBusyIndicator();
                })
            }

            function addMfrPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'MANUFACTURER', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                })
                return propertyAttachmentIds;
            }

            function addAttachment(attribute) {
                var attachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'MANUFACTURER', attachmentFile).then(
                        function (data) {
                            attachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        })
                })
                return attachmentIds;
            }

            function validate() {
                var valid = true;
                if (vm.newManufacture.mfrType == null || vm.newManufacture.mfrType == undefined
                    || vm.newManufacture.mfrType == "") {
                    $rootScope.showErrorMessage(mfrTypeValidate);
                    valid = false;
                }
                else if (vm.newManufacture.name == null || vm.newManufacture.name == undefined
                    || vm.newManufacture.name == "") {
                    $rootScope.showErrorMessage(mfrNameValidate);
                    valid = false;
                } else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                    valid = false;
                }
                return valid;
            }

            function loadWorkflows() {
                MfrService.getWorkflows(vm.newManufacture.mfrType.id, 'MANUFACTURERS').then(
                    function (data) {
                        vm.workflows = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            (function () {
                //if ($application.homeLoaded == true) {
                loadMfrAttributeDefs();
                $rootScope.$on('app.mfrs.new', anotherItem);
                //}
            })();
        }
    }
)
;