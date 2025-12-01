define(
    [
        'app/desktop/modules/mes/mes.module',
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
        'app/shared/services/core/equipmentService',
        'app/desktop/directives/mes-mfr-data/mesMfrDataDirectiveController',
        'app/desktop/directives/mes-asset/mesAssetDirectiveController',
    ],
    function (module) {

        module.controller('NewEquipmentController', NewEquipmentController);

        function NewEquipmentController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, EquipmentService, LoginService,
                                        ObjectTypeAttributeService, AutonumberService, MESObjectTypeService, AttributeAttachmentService) {

            var vm = this;

            var parsed = angular.element("<div></div>");


            vm.selectedEquipmentType = null;
            vm.attributes = [];
            vm.validattributes = [];

            var itemTypeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var itemNumberValidation = parsed.html($translate.instant("ITEM_NUMBER_VALIDATION")).html();
            var itemNameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var EquipmentManagerValidation = parsed.html($translate.instant("EQUIPMENT_MANAGER_VALIDATION")).html();
            var pleaseEnter = parsed.html($translate.instant("PLEASE_ENTER")).html();

            vm.newEquipment = {
                id: null,
                type: null,
                number: null,
                name: null,
                description: null,
                requiresMaintenance: true,
                imageFile: null,
                manufacturerData: {
                    object: null,
                    mfrName: null,
                    mfrDescription: null,
                    mfrModelNumber: null,
                    mfrPartNumber: null,
                    mfrSerialNumber: null,
                    mfrLotNumber: null,
                    mfrDate: null
                }
            };
            vm.newAsset = {
                id: null,
                type: null,
                name: null,
                number: null,
                description: null,
                metered: false,
                resource: null,
                resourceType: "",
                meters: []
            };

            vm.autoNumber = autoNumber;
            vm.onSelectType = onSelectType;
            function onSelectType(equipmentType) {
                if (equipmentType != null && equipmentType != undefined) {
                    vm.selectedEquipmentType = equipmentType;
                    vm.newEquipment.type = equipmentType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.selectedEquipmentType != null) {
                    var source = vm.selectedEquipmentType.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newEquipment.number = data;
                            loadEquipmentTypeAttributes();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }


            function createNewEquipment() {
                create().then(function () {
                    vm.newEquipment = {
                        id: null,
                        type: null,
                        number: null,
                        name: null,
                        description: null,
                        requiresMaintenance: true,
                        imageFile: null,
                        manufacturerData: {
                            object: null,
                            mfrName: null,
                            mfrDescription: null,
                            mfrModelNumber: null,
                            mfrPartNumber: null,
                            mfrSerialNumber: null,
                            mfrLotNumber: null,
                            mfrDate: null
                        }
                    };
                    vm.newAsset = {
                        id: null,
                        type: null,
                        name: null,
                        number: null,
                        description: null,
                        metered: false,
                        resource: null,
                        resourceType: "",
                        meters: []
                    };
                    $scope.callback();
                    $rootScope.hideBusyIndicator();
                })

            }

            var assetTypeValidation = parsed.html($translate.instant("ASSET_TYPE_VALIDATION")).html();
            var assetNumberValidation = parsed.html($translate.instant("ASSET_NUMBER_VALIDATION")).html();
            var metersValidation = parsed.html($translate.instant("ASSET_METER_VALIDATION")).html();
            var assetNameValidation = parsed.html($translate.instant("ASSET_NAME_VALIDATION")).html();

            function validate() {
                var valid = true;
                if (vm.newEquipment.type == null || vm.newEquipment.type == undefined ||
                    vm.newEquipment.type == "") {
                    $rootScope.showWarningMessage(itemTypeValidation);
                    valid = false;
                }
                else if (vm.newEquipment.number == null || vm.newEquipment.number == undefined ||
                    vm.newEquipment.number == "") {
                    $rootScope.showWarningMessage(itemNumberValidation);
                    valid = false;
                }
                else if (vm.newEquipment.name == null || vm.newEquipment.name == undefined ||
                    vm.newEquipment.name == "") {
                    $rootScope.showWarningMessage(itemNameValidation);
                    valid = false;
                } else if (vm.newEquipment.requiresMaintenance && (vm.newAsset.type == null || vm.newAsset.type == "" || vm.newAsset.type == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(assetTypeValidation);
                } else if (vm.newEquipment.requiresMaintenance && (vm.newAsset.number == null || vm.newAsset.number == "" || vm.newAsset.number == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(assetNumberValidation);
                }
                else if (vm.newEquipment.requiresMaintenance && (vm.newAsset.name == null || vm.newAsset.name == "" || vm.newAsset.name == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(assetNameValidation);
                }

                else if (vm.newEquipment.requiresMaintenance && vm.newAsset.metered && vm.newAsset.meters.length <= 0) {
                    $rootScope.showWarningMessage(metersValidation);
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

            var insertedSuccefully = parsed.html($translate.instant("EQUIPMENT_ADDED_SUCCESS")).html();

            function create() {
                var dfd = $q.defer();
                vm.validattributes = [];
                if (validate() && validateRequiredAttributes()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.newAsset.resourceType = vm.newEquipment.type.objectType;
                    /*vm.newAsset.name = vm.newEquipment.name;
                     vm.newAsset.description = vm.newEquipment.description;*/
                    vm.newEquipment.asset = vm.newAsset;
                    EquipmentService.createEquipment(vm.newEquipment).then(
                        function (data) {
                            document.getElementById("imageId").value = "";
                            if (vm.newEquipment.imageFile != null) {
                                EquipmentService.uploadImage(data.id, vm.newEquipment.imageFile).then(
                                    function (data) {
                                    }
                                );
                            }
                            vm.newEquipment = data;
                            saveAttributes().then(
                                function (att) {
                                    vm.newEquipment = {
                                        id: null,
                                        type: null,
                                        number: null,
                                        name: null,
                                        description: null,
                                        requiresMaintenance: true,
                                        imageFile: null,
                                        manufacturerData: {
                                            object: null,
                                            mfrName: null,
                                            mfrDescription: null,
                                            mfrModelNumber: null,
                                            mfrPartNumber: null,
                                            mfrSerialNumber: null,
                                            mfrLotNumber: null,
                                            mfrDate: null
                                        }
                                    };
                                    vm.newAsset = {
                                        id: null,
                                        type: null,
                                        name: null,
                                        number: null,
                                        description: null,
                                        metered: false,
                                        resource: null,
                                        resourceType: "",
                                        meters: []
                                    };
                                    vm.selectedEquipmentType = null;
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


            function addEquipmentPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'EQUIPMENTTYPE', attachmentFile).then(
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
                        attribute.id.objectId = vm.newEquipment.id;

                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'BOOLEAN' && attribute.booleanValue != true) {
                            attribute.booleanValue = false;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length != null) {
                            attribute.attachmentValues = addEquipmentPropertyAttachment(attribute)
                        }
                    });
                    $timeout(function () {
                        EquipmentService.saveEquipmentAttributes(vm.newEquipment.id, vm.attributes).then(
                            function (data) {

                                if (vm.imageAttributes.length > 0) {
                                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                                        EquipmentService.uploadImageAttribute(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
                                            function (data) {

                                                defered.resolve();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
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


            function loadEquipmentTypeAttributes() {
                vm.attributes = [];
                MESObjectTypeService.getMesObjectAttributesWithHierarchy(vm.newEquipment.type.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newEquipment.id,
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
                    }
                );
            }

            function loadPersons() {
                vm.persons = [];
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && login.external == false) {
                                vm.persons.push(login.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                $rootScope.hideBusyIndicator();
                loadPersons();
                $rootScope.$on('app.equipment.new', createNewEquipment);
                //}
            })();
        }
    }
);