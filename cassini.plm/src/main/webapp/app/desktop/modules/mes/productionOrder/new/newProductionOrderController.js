define(
    [
        'app/desktop/modules/mes/mes.module',
        'moment',
        'moment-timezone-with-data',
        'app/desktop/modules/directives/mesObjectTypeDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/productionOrderService',
        'app/shared/services/core/bopService',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/classificationService',
        'app/shared/services/core/plantService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'
    ],
    function (module) {

        module.controller('NewProductionOrderController', NewProductionOrderController);

        function NewProductionOrderController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, ClassificationService,
                                              AutonumberService, PlantService, BOPService, ProductionOrderService, ObjectTypeAttributeService, MESObjectTypeService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var typeValidation = parsed.html($translate.instant("TYPE_VALIDATION")).html();
            var numberValidation = parsed.html($translate.instant("PLEASE_ENTER_NUMBER")).html();
            var nameValidation = parsed.html($translate.instant("PLEASE_ENTER_NAME")).html();
            var poCreatedMsg = parsed.html($translate.instant("PO_CREATED_MESSAGE")).html();
            vm.selectPlantTitle = parsed.html($translate.instant("SELECT_PLANT")).html();
            var plantValidation = parsed.html($translate.instant("P_SELECT_PLANT")).html();
            var plannedStartDateValidation = parsed.html($translate.instant("PLANNED_START_DATE_VALIDATION")).html();
            var plannedFinishDateValidation = parsed.html($translate.instant("PLANNED_FINISH_DATE_VALIDATION")).html();
            var finishDateValidation = parsed.html($translate.instant("FINISH_DATE_VALIDATION")).html();

            vm.productionOrder = {
                id: null,
                type: null,
                number: null,
                name: null,
                plant: null,
                description: null,
                plannedStartDate: null,
                plannedFinishDate: null,
                workflow: null
            };

            vm.onSelectType = onSelectType;
            function onSelectType(objectType) {
                if (objectType != null && objectType != undefined) {
                    vm.productionOrder.type = objectType;
                    vm.type = objectType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.productionOrder.type != null && vm.productionOrder.type.autoNumberSource != null) {
                    var source = vm.productionOrder.type.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.productionOrder.number = data;
                            loadProductionOrderTypeAttributes();
                            loadWorkflows();
                        }
                    )
                }
            }

            function loadWorkflows() {
                BOPService.getMESTypeWorkflows(vm.productionOrder.type.id, 'MANUFACTURING').then(
                    function (data) {
                        vm.workflows = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadProductionOrderTypeAttributes() {
                vm.attributes = [];
                MESObjectTypeService.getMesObjectAttributesWithHierarchy(vm.productionOrder.type.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.productionOrder.id,
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
                            if (attribute.dataType == "LIST") {
                                att.listValue = attribute.defaultListValue;
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
                    vm.customImageAttributes = [];
                    vm.customAttachmentAttributes = [];
                    vm.customImages = new Hashtable();
                    vm.customAttachments = new Hashtable();
                    angular.forEach(vm.productionOrderRequiredProperties, function (reqatt) {
                        vm.productionOrderProperties.push(reqatt);
                    })
                    angular.forEach(vm.productionOrderProperties, function (attribute) {
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            vm.customImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.customImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            vm.customAttachments.put(attribute.id.attributeDef, attribute.attachmentValues);
                            vm.customAttachmentAttributes.push(attribute);
                            attribute.attachmentValues = [];
                        }
                    });
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    ProductionOrderService.createProductionOrder(vm.productionOrder).then(
                        function (data) {
                            vm.productionOrder = data;
                            saveCustomAttributes().then(
                                saveAttributes().then(
                                    function (attributes) {
                                        $scope.callback(vm.productionOrder);
                                        $rootScope.hideBusyIndicator();
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(poCreatedMsg);
                                        vm.productionOrder = {
                                            id: null,
                                            type: null,
                                            number: null,
                                            name: null,
                                            description: null,
                                            plannedStartDate: null,
                                            plannedFinishDate: null,
                                            workflow: null,
                                        };
                                    }
                                )
                            )
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
            }

            function validate() {
                var valid = true;
                if (vm.productionOrder.type == null || vm.productionOrder.type == "" || vm.productionOrder.type == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(typeValidation);
                } else if (vm.productionOrder.number == null || vm.productionOrder.number == "" || vm.productionOrder.number == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(numberValidation);
                } else if (vm.productionOrder.name == null || vm.productionOrder.name == "" || vm.productionOrder.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                } else if (vm.productionOrder.plant == null || vm.productionOrder.plant == undefined || vm.productionOrder.plant == "") {
                    valid = false;
                    $rootScope.showWarningMessage(plantValidation);
                } else if (vm.productionOrder.plannedFinishDate != null && (vm.productionOrder.plannedStartDate == null || vm.productionOrder.plannedStartDate == undefined || vm.productionOrder.plannedStartDate == "")) {
                    valid = false;
                    $rootScope.showWarningMessage(plannedStartDateValidation);
                } else if (vm.productionOrder.plannedStartDate != null && vm.productionOrder.plannedFinishDate != null && vm.productionOrder.plannedStartDate != "" && vm.productionOrder.plannedFinishDate != "") {
                    var plannedFinishDate = moment(vm.productionOrder.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                    var plannedStartDate = moment(vm.productionOrder.plannedStartDate, $rootScope.applicationDateSelectFormat);
                    var val = plannedFinishDate.isSame(plannedStartDate) || plannedFinishDate.isAfter(plannedStartDate);
                    if (!val) {
                        valid = false;
                        $rootScope.showWarningMessage(finishDateValidation);
                    }
                }

                return valid;
            }

            function saveCustomAttributes() {
                var defered = $q.defer();
                if (vm.customImageAttributes.length > 0 || vm.customAttachmentAttributes.length > 0) {
                    angular.forEach(vm.customImageAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeImageValue("PRODUCTIONORDER", vm.productionOrder.id, imgAtt.id.attributeDef, vm.customImages.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    )

                    angular.forEach(vm.customAttachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues("PRODUCTIONORDER", vm.productionOrder.id, imgAtt.id.attributeDef, vm.customAttachments.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    )
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function saveAttributes() {
                var defered = $q.defer();
                angular.forEach(vm.attributes, function (attribute) {
                    attribute.id.objectId = vm.productionOrder.id;
                });
                BOPService.saveBOPAttributes(vm.attributes).then(
                    function (data) {
                        if (vm.imageAttributes.length > 0 || vm.attachmentAttributes.length > 0) {
                            angular.forEach(vm.imageAttributes, function (imgAtt) {
                                    ClassificationService.updateAttributeImageValue("MESOBJECTTYPE", vm.productionOrder.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                                        function (data) {
                                            defered.resolve();
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                            )

                            angular.forEach(vm.attachmentAttributes, function (imgAtt) {
                                    ClassificationService.updateAttributeAttachmentValues("MESOBJECTTYPE", vm.productionOrder.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
                                        function (data) {
                                            defered.resolve();
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                            )
                        } else {
                            defered.resolve();
                        }
                    }
                )
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

            function loadBOPCustomProperties() {
                vm.productionOrderProperties = [];
                vm.productionOrderRequiredProperties = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("PRODUCTIONORDER").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.productionOrder.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                stringValue: null,
                                mlistValue: [],
                                newListValue: null,
                                timeValue: null,
                                timestampValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
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
                            if (attribute.required == false) {
                                vm.productionOrderProperties.push(att);
                            } else {
                                vm.productionOrderRequiredProperties.push(att);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                loadBOPCustomProperties();
                loadPlants();
                $rootScope.$on('app.productionOrder.new', create);
            })();
        }
    }
);