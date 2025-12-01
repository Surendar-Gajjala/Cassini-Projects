define(
    [
        'app/desktop/modules/mes/mes.module',
        'moment',
        'moment-timezone-with-data',
        'app/desktop/modules/directives/mesObjectTypeDirective',
        'app/shared/services/core/assemblyLineService',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/classificationService',
        'app/shared/services/core/plantService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/measurementService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController'
    ],
    function (module) {

        module.controller('NewAssemblyLineController', NewAssemblyLineController);

        function NewAssemblyLineController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, CommonService, ClassificationService,
                                           AutonumberService, AssemblyLineService, LoginService, PlantService, QualityTypeService, ObjectTypeAttributeService, MESObjectTypeService, MeasurementService) {

            var vm = this;


            var parsed = angular.element("<div></div>");

            var attributeRequired = parsed.html($translate.instant("ATTRIBUTE_REQUIRED")).html();
            var assemblyLineNumberValidation = parsed.html($translate.instant("NUMBER_CAN_NOT_EMPTY")).html();
            var assemblyLineTypeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var assemblyLineNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var assemblyLineCreatedMsg = parsed.html($translate.instant("ASSEMBLYLINE_CREATED_MSG")).html();
            vm.selectPlantTitle = parsed.html($translate.instant("SELECT_PLANT")).html();
            var plantValidation = parsed.html($translate.instant("P_SELECT_PLANT")).html();
            $scope.select = parsed.html($translate.instant("SELECT")).html();

            vm.onSelectType = onSelectType;
            vm.newAssemblyLine = {
                id: null,
                plant: null,
                type: null,
                number: null,
                description: null
            };


            function onSelectType(objectType) {
                if (objectType != null && objectType != undefined) {
                    vm.newAssemblyLine.type = objectType;
                    vm.type = objectType;
                    autoNumber();
                    loadAttributeDefs();
                }
            }

            function autoNumber() {
                if (vm.newAssemblyLine.type != null && vm.newAssemblyLine.type.autoNumberSource != null) {
                    var source = vm.newAssemblyLine.type.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newAssemblyLine.number = data;
                        }
                    )
                }
            }

            function create() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.newAssemblyLine.type = vm.type;
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
                    vm.newAssemblyLine.mesObjectAttributes = vm.attributes;
                    AssemblyLineService.createAssemblyLine(vm.newAssemblyLine).then(
                        function (data) {
                            vm.newAssemblyLine = data;
                            saveAttributes().then(
                                function (attributes) {
                                    vm.attributes = [];
                                    $scope.callback(vm.newAssemblyLine);
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showSuccessMessage(assemblyLineCreatedMsg);
                                    vm.type = null;
                                    vm.newAssemblyLine = {
                                        id: null,
                                        type: null,
                                        number: null,
                                        description: null
                                    };
                                }
                            )

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.newAssemblyLine.plant == null || vm.newAssemblyLine.plant == undefined ||
                    vm.newAssemblyLine.plant == "") {
                    valid = false;
                    $rootScope.showErrorMessage(plantValidation);
                } else if (vm.newAssemblyLine.type == null || vm.newAssemblyLine.type == undefined ||
                    vm.newAssemblyLine.type == "") {
                    $rootScope.showErrorMessage(assemblyLineTypeValidation);
                    valid = false;
                } else if (vm.newAssemblyLine.number == null || vm.newAssemblyLine.number == undefined ||
                    vm.newAssemblyLine.number == "") {
                    $rootScope.showErrorMessage(assemblyLineNumberValidation);
                    valid = false;
                } else if (vm.newAssemblyLine.name == null || vm.newAssemblyLine.name == undefined ||
                    vm.newAssemblyLine.name == "") {
                    $rootScope.showErrorMessage(assemblyLineNameValidation);
                    valid = false;
                } else if (vm.attributes.length > 0 && !validateAttributes()) {
                    valid = false;
                } else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
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
                            ClassificationService.updateAttributeImageValue("MESOBJECTTYPE", vm.newAssemblyLine.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }
                                , function (error) {
                                    defered.resolve();
                                }
                            )
                        }
                    );
                    angular.forEach(vm.attachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues("MESOBJECTTYPE", vm.newAssemblyLine.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }
                                , function (error) {
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

            function loadAttributeDefs() {
                vm.attributes = [];
                MESObjectTypeService.getMesObjectAttributesWithHierarchy(vm.type.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.type.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                mlistValue: [],
                                newListValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                refValue: null,
                                timestampValue: null,
                                ref: null,
                                imageValue: null,
                                attachmentValues: []
                            };
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
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
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
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

            (function () {
                loadPlants();
                $rootScope.$on('app.assemblyLine.new', create);
            })();
        }
    }
)
;