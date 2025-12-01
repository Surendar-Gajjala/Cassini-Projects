define(
    [
        'app/desktop/modules/mro/mro.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/desktop/modules/directives/mesObjectTypeDirective',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/sparePartsService'

    ],
    function (module) {

        module.controller('NewSparePartController', NewSparePartController);

        function NewSparePartController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, SparePartService, LoginService,
                                        ObjectTypeAttributeService, AutonumberService, MESObjectTypeService, AttributeAttachmentService) {

            var vm = this;

            var parsed = angular.element("<div></div>");


            vm.selectedSparePartType = null;
            vm.attributes = [];
            vm.validattributes = [];

            var itemTypeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var itemNumberValidation = parsed.html($translate.instant("ITEM_NUMBER_VALIDATION")).html();
            var itemNameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var pleaseEnter = parsed.html($translate.instant("PLEASE_ENTER")).html();

            vm.newSparePart = {
                id: null,
                type: null,
                number: null,
                name: null,
                description: null
            };


            vm.autoNumber = autoNumber;
            vm.onSelectType = onSelectType;
            vm.type = null;
            function onSelectType(objectType) {
                if (objectType != null && objectType != undefined) {
                    vm.newSparePart.type = objectType;
                    vm.type = objectType;
                    autoNumber();
                }
            }


            function autoNumber() {
                if (vm.type != null) {
                    var source = vm.type.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newSparePart.number = data;
                            loadSparePartTypeAttributes();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            function createNewSparePart() {
                create().then(function () {
                    vm.newSparePart = {
                        id: null,
                        type: null,
                        number: null,
                        name: null,
                        description: null
                    };
                    vm.type = null;
                    $scope.callback();
                    $rootScope.hideBusyIndicator();
                })

            }


            function validate() {
                var valid = true;
                if (vm.newSparePart.type == null || vm.newSparePart.type == undefined ||
                    vm.newSparePart.type == "") {
                    $rootScope.showWarningMessage(itemTypeValidation);
                    valid = false;
                }
                else if (vm.newSparePart.number == null || vm.newSparePart.number == undefined ||
                    vm.newSparePart.number == "") {
                    $rootScope.showWarningMessage(itemNumberValidation);
                    valid = false;
                }
                else if (vm.newSparePart.name == null || vm.newSparePart.name == undefined ||
                    vm.newSparePart.name == "") {
                    $rootScope.showWarningMessage(itemNameValidation);
                    valid = false;
                }

                else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                    valid = false;
                }

                return valid;
            }

            function validateRequiredAttributes() {
                var valid = true;
                if (vm.attributes.length > 0) {
                    angular.forEach(vm.attributes, function (attribute) {
                        if (valid) {
                            if ($rootScope.checkAttribute(attribute)) {
                                valid = true;
                            } else {
                                valid = false;
                                $rootScope.showWarningMessage("Please enter" + " " + attribute.attributeDef.name);
                            }
                        }
                    })
                }
                return valid;
            }

            var insertedSuccefully = parsed.html($translate.instant("SPAREPART_CREATED_SUCCESS")).html();

            function create() {
                var dfd = $q.defer();
                vm.validattributes = [];
                if (validate() && validateRequiredAttributes()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    SparePartService.createSparePart(vm.newSparePart).then(
                        function (data) {
                            vm.newSparePart = data;
                            saveAttributes().then(
                                function (att) {
                                    vm.newSparePart = {
                                        id: null,
                                        type: null,
                                        number: null,
                                        name: null,
                                        description: null
                                    };
                                    vm.type = null;
                                    vm.selectedSparePartType = null;
                                    vm.attributes = [];
                                    $scope.callback();
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showSuccessMessage(insertedSuccefully);
                                }
                            )
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }

                return dfd.promise;
            }


            function addSparePartPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'MROSPAREPART', attachmentFile).then(
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
                        attribute.id.objectId = vm.newSparePart.id;

                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'BOOLEAN' && attribute.booleanValue != true) {
                            attribute.booleanValue = false;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length != null) {
                            attribute.attachmentValues = addSparePartPropertyAttachment(attribute)
                        }
                    });
                    $timeout(function () {
                        SparePartService.saveSparePartAttributes(vm.attributes).then(
                            function (data) {

                                if (vm.imageAttributes.length > 0) {
                                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                                        SparePartService.uploadImageAttribute(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
                                            function (data) {

                                                defered.resolve();
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


            function loadSparePartTypeAttributes() {
                vm.attributes = [];
                SparePartService.getObjectAttributesWithHierarchy(vm.newSparePart.type.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newSparePart.id,
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
            }


            (function () {
                $rootScope.hideBusyIndicator();
                $rootScope.$on('app.sparePart.new', createNewSparePart);
                //}
            })();
        }
    }
);