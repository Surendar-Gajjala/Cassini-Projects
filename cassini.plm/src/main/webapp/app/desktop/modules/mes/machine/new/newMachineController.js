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
        'app/desktop/modules/directives/mroObjectTypeDirective',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/machineService',
        'app/shared/services/core/workCenterService',
        'app/desktop/directives/mes-mfr-data/mesMfrDataDirectiveController',
        'app/desktop/directives/mes-asset/mesAssetDirectiveController',
        'app/shared/services/core/assetService',
        'app/shared/services/core/meterService'

    ],
    function (module) {

        module.controller('NewMachineController', NewMachineController);

        function NewMachineController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, MachineService, LoginService,
                                      ObjectTypeAttributeService, AutonumberService, MESObjectTypeService, AttributeAttachmentService, WorkCenterService,
                                      AssetService, MeterService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            vm.selectedMachineType = null;
            vm.attributes = [];
            vm.validattributes = [];

            var itemTypeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var numberValidation = parsed.html($translate.instant("NUMBER_CANNOT_BE_EMPTY")).html();
            var itemNameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var workCenterValidation = parsed.html($translate.instant("WORKCENTER_VALIDATION")).html();
            var MachineManagerValidation = parsed.html($translate.instant("MACHINE_MANAGER_VALIDATION")).html();
            var pleaseEnter = parsed.html($translate.instant("PLEASE_ENTER")).html();
            vm.selectWorkCenterTitle = parsed.html($translate.instant("SELECT_WORKCENTER")).html();


            vm.newMachine = {
                id: null,
                type: null,
                number: null,
                name: null,
                workCenter: null,
                description: null,
                requiresMaintenance: true,
                image: null,
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
            function onSelectType(machineType) {
                if (machineType != null && machineType != undefined) {
                    vm.selectedMachineType = machineType;
                    vm.newMachine.type = machineType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.selectedMachineType != null) {
                    var source = vm.selectedMachineType.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newMachine.number = data;
                            loadMachineTypeAttributes();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            function createNewMachine() {
                create().then(function () {
                    vm.newMachine = {
                        id: null,
                        type: null,
                        number: null,
                        name: null,
                        description: null,
                        requiresMaintenance: true,
                        image: null,
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
                if (vm.newMachine.type == null || vm.newMachine.type == undefined ||
                    vm.newMachine.type == "") {
                    $rootScope.showWarningMessage(itemTypeValidation);
                    valid = false;
                }
                else if (vm.newMachine.number == null || vm.newMachine.number == undefined ||
                    vm.newMachine.number == "") {
                    $rootScope.showWarningMessage(numberValidation);
                    valid = false;
                }
                else if (vm.newMachine.workCenter == null || vm.newMachine.workCenter == undefined ||
                    vm.newMachine.workCenter == "") {
                    $rootScope.showWarningMessage(workCenterValidation);
                    valid = false;
                }
                else if (vm.newMachine.name == null || vm.newMachine.name == undefined ||
                    vm.newMachine.name == "") {
                    $rootScope.showWarningMessage(itemNameValidation);
                    valid = false;
                } else if (vm.newMachine.requiresMaintenance && (vm.newAsset.type == null || vm.newAsset.type == "" || vm.newAsset.type == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(assetTypeValidation);
                } else if (vm.newMachine.requiresMaintenance && (vm.newAsset.number == null || vm.newAsset.number == "" || vm.newAsset.number == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(assetNumberValidation);
                }
                else if (vm.newMachine.requiresMaintenance && (vm.newAsset.name == null || vm.newAsset.name == "" || vm.newAsset.name == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(assetNameValidation);
                }
                else if (vm.newMachine.requiresMaintenance && vm.newAsset.metered && vm.newAsset.meters.length <= 0) {
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

            var insertedSuccefully = parsed.html($translate.instant("MACHINE_ADDED_SUCCESS")).html();

            function create() {
                var dfd = $q.defer();
                vm.validattributes = [];
                if (validate() && validateRequiredAttributes()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.newAsset.resourceType = vm.newMachine.type.objectType;
                    /*vm.newAsset.name = vm.newMachine.name;
                     vm.newAsset.description = vm.newMachine.description;*/
                    vm.newMachine.asset = vm.newAsset;
                    MachineService.createMachine(vm.newMachine).then(
                        function (data) {
                            document.getElementById("imageId").value = "";
                            if (vm.newMachine.imageFile != null) {
                                MachineService.uploadImage(data.id, vm.newMachine.imageFile).then(
                                    function (data) {
                                    }
                                );
                            }
                            vm.newMachine = data;
                            saveAttributes().then(
                                function (att) {
                                    vm.newMachine = {
                                        id: null,
                                        type: null,
                                        number: null,
                                        name: null,
                                        description: null,
                                        requiresMaintenance: true
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
                                    vm.selectedMachineType = null;
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


            function addMachinePropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'MACHINETYPE', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
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
                        attribute.id.objectId = vm.newMachine.id;

                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'BOOLEAN' && attribute.booleanValue != true) {
                            attribute.booleanValue = false;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length != null) {
                            attribute.attachmentValues = addMachinePropertyAttachment(attribute)
                        }
                    });
                    $timeout(function () {
                        MachineService.saveMachineAttributes(vm.newMachine.id, vm.attributes).then(
                            function (data) {

                                if (vm.imageAttributes.length > 0) {
                                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                                        MachineService.uploadImageAttribute(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
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


            function loadMachineTypeAttributes() {
                vm.attributes = [];
                MESObjectTypeService.getMesObjectAttributesWithHierarchy(vm.newMachine.type.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newMachine.id,
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
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadWorkCenters() {
                WorkCenterService.getWorkCenters().then(
                    function (data) {
                        vm.workCenters = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.onSelectAssetType = onSelectAssetType;
            function onSelectAssetType(objectType) {
                if (objectType != null && objectType != undefined) {
                    vm.newAsset.type = objectType;
                    vm.assetType = objectType;
                    generateAssetAutoNumber();
                }
            }

            function generateAssetAutoNumber() {
                if (vm.newAsset.type != null && vm.newAsset.type.autoNumberSource != null) {
                    var source = vm.newAsset.type.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newAsset.number = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            vm.meterIds = [];
            vm.meters = [];
            vm.getMeters = getMeters;
            function getMeters() {
                vm.meters = [];
                vm.meterIds = [];
                if (vm.newAsset.metered == true) {
                    MeterService.getMeters().then(
                        function (data) {
                            vm.meters = data;

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
                else {
                    vm.newAsset.meters = [];
                }
            }

            (function () {
                $rootScope.hideBusyIndicator();
                loadWorkCenters();
                loadPersons();
                $rootScope.$on('app.machine.new', createNewMachine);

            })();
        }
    }
);