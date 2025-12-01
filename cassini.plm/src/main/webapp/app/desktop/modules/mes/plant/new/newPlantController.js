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
        'app/shared/services/core/plantService',
        'app/desktop/directives/mes-asset/mesAssetDirectiveController'
    ],
    function (module) {

        module.controller('NewPlantController', NewPlantController);

        function NewPlantController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, LoginService,
                                    ObjectTypeAttributeService, AutonumberService, MESObjectTypeService, PlantService, AttributeAttachmentService) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            vm.persons = [];
            vm.selectedPlantType = null;
            vm.attributes = [];
            vm.validattributes = [];
            var itemTypeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var itemNumberValidation = parsed.html($translate.instant("ITEM_NUMBER_VALIDATION")).html();
            var itemNameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var plantManagerValidation = parsed.html($translate.instant("PLANT_MANAGER_VALIDATION")).html();
            var pleaseEnter = parsed.html($translate.instant("PLEASE_ENTER")).html();
            var successMsg = parsed.html($translate.instant("PLANT_CREATED_SUCCESS_MESSAGE")).html();
            vm.newPlant = {
                id: null,
                type: null,
                number: null,
                name: null,
                description: null,
                address: null,
                city: null,
                country: null,
                postalCode: null,
                phoneNumber: null,
                mobileNumber: null,
                faxNumber: null,
                email: null,
                plantPerson: null,
                notes: null,
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
            vm.autoNumber = autoNumber;
            vm.onSelectType = onSelectType;
            function onSelectType(plantType) {
                if (plantType != null && plantType != undefined) {
                    vm.selectedPlantType = plantType;
                    vm.newPlant.type = plantType;
                    autoNumber();
                }
            }


            function autoNumber() {
                if (vm.selectedPlantType != null) {
                    var source = vm.selectedPlantType.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newPlant.number = data;
                            loadPlantTypeAttributes();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            /*
             * Create Plant
             * */

            function createNewPlant() {
                create().then(function () {
                    vm.newPlant = {
                        id: null,
                        type: null,
                        number: null,
                        name: null,
                        description: null,
                        address: null,
                        city: null,
                        country: null,
                        postalCode: null,
                        phoneNumber: null,
                        mobileNumber: null,
                        faxNumber: null,
                        email: null,
                        plantPerson: null,
                        notes: null,
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
                    $scope.callback();
                    $rootScope.hideBusyIndicator();
                })


            }

            var enterValidEmail = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();

            function validateEmail(email) {
                var valid = true;
                var atpos = email.indexOf("@");
                var dotpos = email.lastIndexOf(".");
                if (email != null && email != undefined && email != "") {
                    if (atpos < 1 || ( dotpos - atpos < 2 )) {
                        valid = false;
                    }
                }
                return valid
            }

            /*
             * Validate Plant Object
             * */
            var assetTypeValidation = parsed.html($translate.instant("ASSET_TYPE_VALIDATION")).html();
            var assetNumberValidation = parsed.html($translate.instant("ASSET_NUMBER_VALIDATION")).html();
            var metersValidation = parsed.html($translate.instant("ASSET_METER_VALIDATION")).html();
            var assetNameValidation = parsed.html($translate.instant("ASSET_NAME_VALIDATION")).html();

            function validate() {
                var valid = true;
                if (vm.newPlant.type == null || vm.newPlant.type == undefined ||
                    vm.newPlant.type == "") {
                    $rootScope.showWarningMessage(itemTypeValidation);
                    valid = false;
                }
                else if (vm.newPlant.number == null || vm.newPlant.number == undefined ||
                    vm.newPlant.number == "") {
                    $rootScope.showWarningMessage(itemNumberValidation);
                    valid = false;
                }
                else if (vm.newPlant.name == null || vm.newPlant.name == undefined ||
                    vm.newPlant.name == "") {
                    $rootScope.showWarningMessage(itemNameValidation);
                    valid = false;
                }
                else if (vm.newPlant.email != null && !validateEmail(vm.newPlant.email)) {
                    valid = false;
                    $rootScope.showWarningMessage(enterValidEmail);
                }

                else if (vm.newPlant.plantPerson == null || vm.newPlant.plantPerson == undefined ||
                    vm.newPlant.plantPerson == "") {
                    $rootScope.showWarningMessage(plantManagerValidation);
                    valid = false;
                } else if (vm.newPlant.requiresMaintenance && (vm.newAsset.type == null || vm.newAsset.type == "" || vm.newAsset.type == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(assetTypeValidation);
                } else if (vm.newPlant.requiresMaintenance && (vm.newAsset.number == null || vm.newAsset.number == "" || vm.newAsset.number == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(assetNumberValidation);
                }

                else if (vm.newPlant.requiresMaintenance && (vm.newAsset.name == null || vm.newAsset.name == "" || vm.newAsset.name == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(assetNameValidation);
                }

                else if (vm.newPlant.requiresMaintenance && vm.newAsset.metered && vm.newAsset.meters.length <= 0) {
                    $rootScope.showWarningMessage(metersValidation);
                    valid = false;
                }
                else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                    valid = false;
                }

                return valid;
            }

            /*
             * Validate Attributes
             * */
            function validateRequiredAttributes() {
                var valid = true;
                if (vm.attributes.length > 0) {
                    angular.forEach(vm.attributes, function (attribute) {
                        if (valid) {
                            if (attribute.attributeDef.required == true && !$rootScope.checkAttribute(attribute)) {
                                valid = false;
                                $rootScope.showWarningMessage(pleaseEnter + " " + attribute.attributeDef.name);
                            }
                        }
                    })
                }
                return valid;
            }

            /*
             * Create Plant
             *
             * */


            function create() {
                var dfd = $q.defer();
                vm.validattributes = [];
                if (validate() && validateRequiredAttributes()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.newAsset.resourceType = vm.newPlant.type.objectType;
                    /* vm.newAsset.name = vm.newPlant.name;
                     vm.newAsset.description = vm.newPlant.description;*/
                    vm.newPlant.asset = vm.newAsset;
                    PlantService.createPlant(vm.newPlant).then(
                        function (data) {
                            vm.newPlant = data;
                            saveAttributes().then(
                                function (att) {
                                    vm.newPlant = {
                                        id: null,
                                        type: null,
                                        number: null,
                                        name: null,
                                        description: null,
                                        address: null,
                                        city: null,
                                        country: null,
                                        postalCode: null,
                                        phoneNumber: null,
                                        mobileNumber: null,
                                        faxNumber: null,
                                        email: null,
                                        plantPerson: null,
                                        notes: null,
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
                                    vm.selectedPlantType = null;
                                    vm.attributes = [];
                                    $scope.callback();
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showSuccessMessage(successMsg);
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


            function addPlantPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'PLANT', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }
                    )
                })
                return propertyAttachmentIds;
            }

            /*
             * Save Plant Attributes
             * */
            function saveAttributes() {
                var defered = $q.defer();
                vm.imageAttributes = [];
                var images = new Hashtable();
                if (vm.attributes.length > 0) {
                    angular.forEach(vm.attributes, function (attribute) {
                        attribute.id.objectId = vm.newPlant.id;

                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'BOOLEAN' && attribute.booleanValue != true) {
                            attribute.booleanValue = false;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length != null) {
                            attribute.attachmentValues = addPlantPropertyAttachment(attribute)
                        }
                    });
                    $timeout(function () {
                        PlantService.savePlantAttributes(vm.attributes).then(
                            function (data) {

                                if (vm.imageAttributes.length > 0) {
                                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                                        PlantService.uploadImageAttribute(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
                                            function (data) {
                                                defered.resolve();
                                                $rootScope.hideBusyIndicator();
                                            }
                                        )
                                    })
                                } else {
                                    defered.resolve();
                                }

                            }, function () {
                                $rootScope.hideBusyIndicator();
                                defered.reject();
                            })
                    }, 2000)
                } else {
                    defered.resolve();
                }
                return defered.promise;
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


            function loadPlantTypeAttributes() {
                vm.attributes = [];
                MESObjectTypeService.getMesObjectAttributesWithHierarchy(vm.newPlant.type.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newPlant.id,
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
                loadPersons();
                $rootScope.$on('app.plant.new', createNewPlant);
                //}
            })();
        }
    }
);