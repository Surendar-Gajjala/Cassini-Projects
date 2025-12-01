define(['app/desktop/modules/mfr/mfrparts/mfrparts.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/desktop/modules/classification/directive/classificationTreeController',
        'app/shared/services/core/mfrPartsService',
        'app/desktop/modules/mfr/mfrparts/directive/manufacturerPartDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('NewmfrpartController', NewmfrpartController);

        function NewmfrpartController($scope, $q, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                      MfrPartsService, ItemTypeService, ObjectAttributeService, AttributeAttachmentService, AttachmentService) {

            var vm = this;
            vm.valid = true;

            vm.createPart = createPart;
            vm.close = close;
            vm.onSelectType = onSelectType;
            vm.anotherItem = anotherItem;
            var mfrId = $stateParams.manufacturerId;
            vm.attributes = [];
            vm.statuses = ['ACTIVE'];
            vm.objectAttributes = [];
            vm.reqObjectAttributes = [];
            vm.validattributes = [];
            vm.validObjectAttributes = [];
            vm.newManufacturepart = {
                id: null,
                manufacturer: $scope.data.mfrId,
                mfrPartType: null,
                partName: null,
                description: null,
                partNumber: null,
                status: 'ACTIVE',
                lifeCycle: null,
                workflowDefinition: null,
                workflowDefId: null,
                thumbnail: null,
                serialized: true,
                imageFile: null
            };

            vm.error = {
                has: false
            }

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

            function loadMfrPartAttributeDefs() {
                vm.objectAttributes = [];
                vm.reqObjectAttributes = [];
                ItemTypeService.getAllTypeAttributes("MANUFACTURERPART").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                                var att = {
                                    id: {
                                        objectId: vm.newManufacturepart.id,
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
                                    refValue: null,
                                    timestampValue: null,
                                    ref: null,
                                    imageValue: null,
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
                                if (attribute.dataType == "RICHTEXT") {
                                    $timeout(function () {
                                        $('.note-current-fontname').text('Arial');
                                    }, 1000);

                                }
                                if (attribute.required == false) {
                                    vm.objectAttributes.push(att);
                                } else {
                                    vm.reqObjectAttributes.push(att);
                                }
                            }
                        )
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function anotherItem() {
                create().then(function () {
                    $scope.callback();
                    $rootScope.hideBusyIndicator();
                    vm.newManufacturepart = {
                        id: null,
                        manufacturer: $scope.data.mfrId,
                        mfrPartType: null,
                        partName: null,
                        description: null,
                        partNumber: null,
                        status: 'ACTIVE',
                        workflowDefinition: null,
                        workflowDefId: null,
                        thumbnail: null,
                        imageFile: null,
                        serialized: true
                    };
                }, function (error) {
                    $rootScope.hideBusyIndicator();
                })
            }

            function createPart() {
                create().then(function () {
                    $rootScope.hideSidePanel();
                    $scope.callback();
                }, function (error) {
                    $rootScope.hideBusyIndicator();
                })

            }

            function create() {
                vm.validattributes = [];
                vm.validObjectAttributes = [];
                var dfd = $q.defer();
                if (validate()) {
                    vm.newPartName = [];
                    MfrPartsService.getPartByMfrAndNumberAndType(mfrId, vm.newManufacturepart.partNumber, vm.newManufacturepart.mfrPartType.id).then(
                        function (data) {
                            vm.newPartName = data;
                            if (vm.newPartName != "") {
                                $rootScope.showWarningMessage(mfrPartNumberExist);
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
                                            $rootScope.showWarningMessage(attribute.attributeDef.name + ":" + "Attribute is Required");
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
                                            $rootScope.showWarningMessage(attribute.attributeDef.name + ":" + "Attribute is Required");
                                        }
                                    } else {
                                        vm.validObjectAttributes.push(attribute);
                                    }
                                });
                                if (vm.attributes.length == vm.validattributes.length &&
                                    vm.reqObjectAttributes.length == vm.validObjectAttributes.length) {
                                    if (vm.newManufacturepart.workflowDefinition != null) {
                                        vm.newManufacturepart.workflowDefId = vm.newManufacturepart.workflowDefinition.id;
                                    }
                                    MfrPartsService.createManufacturepart(vm.newManufacturepart, mfrId).then(
                                        function (data) {
                                            vm.creating = false;
                                            document.getElementById("fileId").value = "";
                                            if (vm.newManufacturepart.imageFile != null) {
                                                MfrPartsService.uploadMfrPartImage(data.id, vm.newManufacturepart.imageFile).then(
                                                    function (data) {
                                                    }, function (error) {
                                                        $rootScope.showErrorMessage(error.message);
                                                    }
                                                );
                                            }
                                            vm.newManufacturepart = data;
                                            saveObjectAttributes().then(
                                                function (data) {
                                                    saveAttributes().then(
                                                        function (data) {
                                                            vm.attributes = [];
                                                            loadMfrPartAttributeDefs();
                                                            dfd.resolve();
                                                            vm.newManufacturepart = null;
                                                            $rootScope.showSuccessMessage(mfrPartNewMessage);
                                                        }
                                                    )
                                                }, function (error) {
                                                    $rootScope.showErrorMessage(error.message);
                                                }
                                            );
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
                    )
                }
                return dfd.promise;
            }

            function saveObjectAttributes() {
                var defered = $q.defer();
                var objectAttrs = vm.objectAttributes.concat(vm.reqObjectAttributes);
                var propertyImages = new Hashtable();
                vm.propertyImageAttributes = [];
                if (objectAttrs.length > 0) {
                    angular.forEach(objectAttrs, function (attribute) {
                        attribute.id.objectId = vm.newManufacturepart.id;
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            propertyImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            attribute.attachmentValues = addMfrPartPropertyAttachment(attribute);
                        }
                    });
                    $timeout(function () {
                        ObjectAttributeService.saveItemObjectAttributes(vm.newManufacturepart.id, objectAttrs).then(
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
                            },
                            function (error) {
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

            function addMfrPartPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'MANUFACTURERPART', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                })

                return propertyAttachmentIds;

            }

            function saveAttributes() {
                var defered = $q.defer();
                vm.imageAttributes = [];
                var images = new Hashtable();
                if (vm.attributes.length > 0) {
                    angular.forEach(vm.attributes, function (attribute) {
                        attribute.id.objectId = vm.newManufacturepart.id;

                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        /* if (attribute.timeValue != null) {
                         attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");
                         }*/
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length != null) {
                            attribute.attachmentValues = addAttachment(attribute)
                        }
                    });
                    $timeout(function () {
                        MfrPartsService.saveMfrAttributes(vm.newManufacturepart.id, vm.attributes).then(
                            function (data) {
                                if (vm.imageAttributes.length > 0) {
                                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                                        MfrPartsService.updateMfrPartImageValue(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
                                            function (data) {
                                                defered.resolve();
                                                vm.newManufacturepart = {
                                                    manufacturer: $scope.data.mfrId,
                                                    mfrPartType: null,
                                                    description: null,
                                                    partNumber: null,
                                                    status: 'ACTIVE'
                                                };
                                                vm.attributes = [];
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
                            }
                        )
                    }, 2000)
                } else {
                    defered.resolve();
                }

                return defered.promise;
            }

            function addAttachment(attribute) {
                var attachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'MANUFACTURERPART', attachmentFile).then(
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
                if (vm.newManufacturepart.mfrPartType == null || vm.newManufacturepart.mfrPartType == undefined
                    || vm.newManufacturepart.mfrPartType == "") {
                    $rootScope.showWarningMessage(mfrPartTypeValidate);
                    valid = false;
                } else if (vm.newManufacturepart.partNumber == null || vm.newManufacturepart.partNumber == undefined
                    || vm.newManufacturepart.partNumber == "") {
                    $rootScope.showWarningMessage(mfrPartDesValidate);
                    valid = false;
                } else if (vm.newManufacturepart.partName == null || vm.newManufacturepart.partName == undefined
                    || vm.newManufacturepart.partName == "") {
                    $rootScope.showWarningMessage(mfrPartNameValidate);
                    valid = false;
                } else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                    valid = false;
                }
                return valid;
            }

            var parsed = angular.element("<div></div>");
            var mfrPartNewMessage = $translate.instant("MANUFACTURER_PART_CREATED_MESSAGE");
            var mfrPartTypeValidate = $translate.instant("MANUFACTURER_PART_TYPE_VALIDATE");
            var mfrPartNameValidate = $translate.instant("MANUFACTURER_PART_NAME_VALIDATE");
            var mfrPartDesValidate = $translate.instant("MANUFACTURER_PART_NUMBER_VALIDATE");
            var mfrPartDescriptionValidate = $translate.instant("MANUFACTURER_PART_DESCRIPTION_VALIDATE");
            var mfrPartNumberExist = $translate.instant("MANUFACTURER_PART_ALREADY_EXIST");
            $scope.selectWorkflow = parsed.html($translate.instant("SELECT_WORKFLOW")).html();

            function onSelectType(mfrPartType) {
                vm.attributes = [];
                if (mfrPartType != null && mfrPartType != undefined) {
                    vm.newManufacturepart.mfrPartType = mfrPartType;
                    MfrPartsService.getMfrPartAttributesWithHierarchy(vm.newManufacturepart.mfrPartType.id).then(
                        function (data) {
                            angular.forEach(data, function (attribute) {
                                var att = {
                                    id: {
                                        objectId: vm.newManufacturepart.id,
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
                                    refValue: null,
                                    timestampValue: null,
                                    ref: null,
                                    attachmentValues: [],
                                    imageValue: null
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
                    )
                    loadWorkflows();
                }
            }

            function loadWorkflows() {
                vm.workflows = [];
                MfrPartsService.getWorkflows(vm.newManufacturepart.mfrPartType.id, 'MANUFACTURER PARTS').then(
                    function (data) {
                        vm.workflows = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadMfrPartAttributeDefs();
                $scope.$on('app.mfr.mfrparts.new', anotherItem);
            })();

        }

    });