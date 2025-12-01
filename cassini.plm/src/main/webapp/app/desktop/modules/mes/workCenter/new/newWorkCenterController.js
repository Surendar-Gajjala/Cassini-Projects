define(
    [
        'app/desktop/modules/mes/mes.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/desktop/modules/directives/mesObjectTypeDirective',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/workCenterService',
        'app/shared/services/core/plantService',
        'app/shared/services/core/plantService',
        'app/shared/services/core/assemblyLineService',
        'app/shared/services/core/qualityTypeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/desktop/directives/mes-asset/mesAssetDirectiveController',
        'app/shared/services/core/classificationService'
    ],
    function (module) {

        module.controller('NewWorkCenterController', NewWorkCenterController);

        function NewWorkCenterController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, ClassificationService,
                                         MESObjectTypeService, WorkCenterService, PlantService, AutonumberService, AssemblyLineService, QualityTypeService) {

            var vm = this;

            var parsed = angular.element("<div></div>");

            vm.selectPlantTitle = parsed.html($translate.instant("SELECT_PLANT")).html();
            vm.selectAssemblyLineTitle = parsed.html($translate.instant("SELECT_ASSEMBLY_LINE")).html();
            var plantValidation = parsed.html($translate.instant("P_SELECT_PLANT")).html();
            var assemblyLineValidation = parsed.html($translate.instant("AL_SELECT_ASSEMBLY_LINE")).html();
            var typeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var nameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var numberValidation = parsed.html($translate.instant("PLEASE_ENTER_NUMBER")).html();
            var workCenterCreated = parsed.html($translate.instant("WORKCENTER_CREATED")).html();
            var attributeRequired = $translate.instant("ATTRIBUTE_REQUIRED");
            vm.workCenter = {
                id: null,
                name: null,
                number: null,
                type: null,
                description: null,
                plant: null,
                assemblyLine: null,
                active: true,
                location: null,
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
            vm.onSelectType = onSelectType;
            vm.autoNumber = autoNumber;
            function onSelectType(type) {
                if (type != null && type != undefined) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.workCenter.type = type;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.workCenter.type != null && vm.workCenter.type != undefined && vm.workCenter.type.autoNumberSource != null) {
                    AutonumberService.getNextNumberByName(vm.workCenter.type.autoNumberSource.name).then(
                        function (data) {
                            vm.workCenter.number = data;
                            loadAttributeDefs();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadAttributeDefs() {
                vm.attributes = [];
                MESObjectTypeService.getMesObjectAttributesWithHierarchy(vm.workCenter.type.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.workCenter.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                stringValue: null,
                                mlistValue: [],
                                newListValue: null,
                                listValueEditMode: false,
                                timestampValue: null,
                                booleanValue: false,
                                dateValue: null,
                                timeValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                doubleValue: null,
                                measurementUnit: null,
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
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            if (attribute.validations != null && attribute.validations != "") {
                                attribute.newValidations = JSON.parse(attribute.validations);
                            }

                            if (attribute.dataType == "RICHTEXT") {
                                $timeout(function () {
                                    $('.note-current-fontname').text('Arial');
                                }, 1000);

                            }
                            vm.attributes.push(att);
                        });
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function create() {
                if (validate()) {
                    vm.imageAttributes = [];
                    vm.attachmentAttributes = [];
                    vm.images = new Hashtable();
                    vm.attachments = new Hashtable();
                    angular.forEach(vm.attributes, function (attribute) {
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            vm.images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            vm.attachments.put(attribute.id.attributeDef, attribute.attachmentValues);
                            vm.attachmentAttributes.push(attribute);
                            attribute.attachmentValues = [];
                        }
                    });
                    vm.workCenter.mesObjectAttributes = vm.attributes;
                    vm.newAsset.resourceType = vm.workCenter.type.objectType;
                    /* vm.newAsset.name = vm.workCenter.name;
                     vm.newAsset.description = vm.workCenter.description;*/
                    vm.workCenter.asset = vm.newAsset;
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    WorkCenterService.createWorkCenter(vm.workCenter).then(
                        function (data) {
                            vm.workCenter = data;
                            saveAttributes().then(
                                function (attributes) {
                                    $scope.callback(data);
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.hideSidePanel();
                                    $rootScope.showSuccessMessage(workCenterCreated);
                                }
                            )

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            var assetTypeValidation = parsed.html($translate.instant("ASSET_TYPE_VALIDATION")).html();
            var assetNumberValidation = parsed.html($translate.instant("ASSET_NUMBER_VALIDATION")).html();
            var metersValidation = parsed.html($translate.instant("ASSET_METER_VALIDATION")).html();
            var assetNameValidation = parsed.html($translate.instant("ASSET_NAME_VALIDATION")).html();

            function validate() {
                var valid = true;

                if (vm.workCenter.plant == null || vm.workCenter.plant == "") {
                    valid = false;
                    $rootScope.showWarningMessage(plantValidation);
                } else if (vm.workCenter.type == null || vm.workCenter.type == "" || vm.workCenter.type == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(typeValidation);
                } else if (vm.workCenter.number == null || vm.workCenter.number == "" || vm.workCenter.number == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(numberValidation);
                } else if (vm.workCenter.name == null || vm.workCenter.name == "" || vm.workCenter.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                } else if (vm.workCenter.requiresMaintenance && (vm.newAsset.type == null || vm.newAsset.type == "" || vm.newAsset.type == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(assetTypeValidation);
                } else if (vm.workCenter.requiresMaintenance && (vm.newAsset.number == null || vm.newAsset.number == "" || vm.newAsset.number == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(assetNumberValidation);
                }
                else if (vm.workCenter.requiresMaintenance && (vm.newAsset.name == null || vm.newAsset.name == "" || vm.newAsset.name == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(assetNameValidation);
                }

                else if (vm.workCenter.requiresMaintenance && vm.newAsset.metered && vm.newAsset.meters.length <= 0) {
                    $rootScope.showWarningMessage(metersValidation);
                    valid = false;
                } else if (vm.attributes.length > 0 && !validateAttributes()) {
                    valid = false;
                }

                return valid;
            }

            function validateAttributes() {
                var valid = true;
                angular.forEach(vm.attributes, function (attribute) {
                    if (valid) {
                        if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                            attribute.attributeDef.dataType != 'TIMESTAMP') {
                            if (!$rootScope.checkAttribute(attribute)) {
                                valid = false;
                                $rootScope.showWarningMessage(attribute.attributeDef.name + ":" + attributeRequired);
                            }
                        }
                    }
                });
                return valid;
            }

            function saveAttributes() {
                var defered = $q.defer();
                if (vm.imageAttributes.length > 0 || vm.attachmentAttributes.length > 0) {
                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeImageValue("MESOBJECTTYPE", vm.workCenter.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }
                            )
                        }
                    )

                    angular.forEach(vm.attachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues("MESOBJECTTYPE", vm.workCenter.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }
                            )
                        }
                    )
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function loadPlants() {
                PlantService.getPlants().then(
                    function (data) {
                        vm.plants = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadAssemblyLines() {
                AssemblyLineService.getAssemblyLines().then(
                    function (data) {
                        vm.assemblyLines = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadPlants();
                loadAssemblyLines();
                $rootScope.$on('app.workCenter.new', create);
            })();
        }
    }
);