define(
    [
        'app/desktop/modules/item/item.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/desktop/modules/pqm/directives/qualityTypeDirective',
        'app/desktop/modules/pqm/directives/alternateQualityTypeDirective',
        'app/shared/services/core/inspectionPlanService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/classificationService'
    ],
    function (module) {

        module.controller('NewInspectionPlanController', NewInspectionPlanController);

        function NewInspectionPlanController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce,
                                             AutonumberService, InspectionPlanService, ObjectTypeAttributeService, QualityTypeService,
                                             ItemService, MfrPartsService, ClassificationService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var attributeRequired = $translate.instant("ATTRIBUTE_REQUIRED");
            $scope.selectProductTitle = parsed.html($translate.instant("SELECT_PRODUCT_TITLE")).html();
            $scope.selectMaterialTitle = parsed.html($translate.instant("SELECT_MATERIAL_TITLE")).html();
            $scope.selectWorkflowTitle = parsed.html($translate.instant("SELECT_WORKFLOW_TITLE")).html();
            var selectProduct = parsed.html($translate.instant("SELECT_PRODUCT")).html();
            var selectMaterial = parsed.html($translate.instant("SELECT_MATERIAL")).html();
            var pleaseSelectPlanType = parsed.html($translate.instant("PLEASE_SELECT_PLAN_TYPE")).html();
            var pleaseEnterNumber = parsed.html($translate.instant("PLEASE_ENTER_NUMBER")).html();
            var pleaseEnterName = parsed.html($translate.instant("PLEASE_ENTER_NAME")).html();
            var selectWorkflowValid = parsed.html($translate.instant("WORKFLOW_SELECT_VALID")).html();
            var planCreated = parsed.html($translate.instant("INSPECTION_PLAN_CREATED")).html();
            $scope.selectWorkflow = parsed.html($translate.instant("SELECT_WORKFLOW")).html();

            $scope.selectedInspectionPlanType = $scope.data.inspectionPlanType;
            $scope.actionType = $scope.data.actionType;
            vm.onSelectType = onSelectType;
            vm.newInspectionPlan = {
                id: null,
                number: null,
                name: null,
                description: null,
                workflow: null,
                product: null,
                material: null,
                planType: null
            };

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.newInspectionPlan.planType = itemType;
                    autoNumber();
                }
            }

            $scope.selectInspectionPlanType = selectInspectionPlanType;
            function selectInspectionPlanType(value) {
                $scope.selectedInspectionPlanType = value;
                vm.newInspectionPlan = {
                    id: null,
                    number: null,
                    name: null,
                    description: null,
                    workflow: null,
                    product: null,
                    material: null,
                    planType: null
                };
                $scope.$evalAsync();
            }

            function loadProductItems() {
                if (vm.newInspectionPlan.planType.productType == null || vm.newInspectionPlan.planType.productType == "") {
                    $rootScope.showWarningMessage("Please set product type for inspection plan");
                } else {
                    ItemService.getNormalAndConfigurableItemsByItemType(vm.newInspectionPlan.planType.productType.id).then(
                        function (data) {
                            vm.productItems = data;
                            ItemService.getLatestRevisionReferences(vm.productItems, 'latestRevision');
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadMaterials() {
                if (vm.newInspectionPlan.planType.partType != null && vm.newInspectionPlan.planType.partType != "") {
                    MfrPartsService.getManufacturerPartsByType(vm.newInspectionPlan.planType.partType.id).then(
                        function (data) {
                            vm.materials = data;
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function autoNumber() {
                if (vm.newInspectionPlan.planType != null && vm.newInspectionPlan.planType.numberSource != null) {
                    var source = vm.newInspectionPlan.planType.numberSource;
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newInspectionPlan.number = data;
                            loadAttributeDefs();
                            loadWorkflows();
                            if ($scope.selectedInspectionPlanType == 'PRODUCTINSPECTIONPLAN') {
                                loadProductItems();
                            } else {
                                loadMaterials();
                            }
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadWorkflows() {
                QualityTypeService.getQualityTypeWorkflows(vm.newInspectionPlan.planType.id, 'QUALITY').then(
                    function (data) {
                        vm.workflows = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadAttributeDefs() {
                vm.attributes = [];
                ClassificationService.getObjectTypeAttributesWithHierarchy(vm.newInspectionPlan.planType.qualityType, vm.newInspectionPlan.planType.id, 0).then(
                    function (data) {
                        vm.qualityTypeAttributes = data.qualityTypeAttributes;
                        angular.forEach(vm.qualityTypeAttributes, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newInspectionPlan.planType.id,
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
                            if (attribute.validations != null && attribute.validations != "") {
                                attribute.newValidations = JSON.parse(attribute.validations);
                            }
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            vm.attributes.push(att);

                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function create() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
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
                    vm.inspectionImageAttributes = [];
                    vm.inspectionAttachmentAttributes = [];
                    vm.inspectionImages = new Hashtable();
                    vm.inspectionAttachments = new Hashtable();

                    angular.forEach(vm.qualityProperties, function (attribute) {
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            vm.inspectionImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.inspectionImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            vm.inspectionAttachments.put(attribute.id.attributeDef, attribute.attachmentValues);
                            vm.inspectionAttachmentAttributes.push(attribute);
                            attribute.attachmentValues = [];
                        }
                    });
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    var qualityAttributeDto = {
                        inspectionPlanAttributes: vm.attributes,
                        objectAttributes: vm.qualityProperties
                    }
                    if ($scope.selectedInspectionPlanType == "PRODUCTINSPECTIONPLAN") {
                        qualityAttributeDto.productInspectionPlan = vm.newInspectionPlan;
                    } else {
                        qualityAttributeDto.materialInspectionPlan = vm.newInspectionPlan;
                    }
                    QualityTypeService.createQualityObject($scope.selectedInspectionPlanType, qualityAttributeDto).then(
                        function (data) {
                            if ($scope.selectedInspectionPlanType == "PRODUCTINSPECTIONPLAN") {
                                vm.newInspectionPlan = data.productInspectionPlan;
                            } else {
                                vm.newInspectionPlan = data.materialInspectionPlan;
                            }
                            saveCustomAttributes().then(
                                function (atts) {
                                    saveAttributes().then(
                                        function (attributes) {
                                            $scope.callback(vm.newInspectionPlan);
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.hideSidePanel();
                                            $rootScope.showSuccessMessage(planCreated);
                                            vm.newInspectionPlan = {
                                                id: null,
                                                number: null,
                                                name: null,
                                                description: null,
                                                workflow: null,
                                                product: null,
                                                material: null,
                                                planType: null
                                            };
                                        }
                                    )
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
                if (vm.newInspectionPlan.planType == null || vm.newInspectionPlan.planType == "" || vm.newInspectionPlan.planType == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseSelectPlanType);
                } else if (vm.newInspectionPlan.number == null || vm.newInspectionPlan.number == "" || vm.newInspectionPlan.number == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterNumber);
                } else if ($scope.selectedInspectionPlanType == "PRODUCTINSPECTIONPLAN" && (vm.newInspectionPlan.product == null || vm.newInspectionPlan.product == "" || vm.newInspectionPlan.product == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(selectProduct);
                } else if ($scope.selectedInspectionPlanType == "MATERIALINSPECTIONPLAN" && (vm.newInspectionPlan.material == null || vm.newInspectionPlan.material == "" || vm.newInspectionPlan.material == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(selectMaterial);
                } else if (vm.newInspectionPlan.name == null || vm.newInspectionPlan.name == "" || vm.newInspectionPlan.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterName);
                } else if (vm.newInspectionPlan.workflow == null || vm.newInspectionPlan.workflow == "" || vm.newInspectionPlan.workflow == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectWorkflowValid);
                } else if (vm.attributes.length > 0 && !validateAttributes()) {
                    valid = false;
                } else if ((vm.qualityProperties.length > 0) && !validateCustomAttributes()) {
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

            function validateCustomAttributes() {
                var valid = true;
                angular.forEach(vm.qualityProperties, function (attribute) {
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
                            ClassificationService.updateAttributeImageValue(vm.newInspectionPlan.planType.qualityType, vm.newInspectionPlan.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    )

                    angular.forEach(vm.attachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues(vm.newInspectionPlan.planType.qualityType, vm.newInspectionPlan.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
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

            function saveCustomAttributes() {
                var defered = $q.defer();
                if (vm.inspectionImageAttributes.length > 0 || vm.inspectionAttachmentAttributes.length > 0) {
                    angular.forEach(vm.inspectionImageAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeImageValue(vm.newInspectionPlan.objectType, vm.newInspectionPlan.id, imgAtt.id.attributeDef, vm.inspectionImages.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    )

                    angular.forEach(vm.inspectionAttachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues(vm.newInspectionPlan.objectType, vm.newInspectionPlan.id, imgAtt.id.attributeDef, vm.inspectionAttachments.get(imgAtt.id.attributeDef)).then(
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

            function loadInspectionPlanCustomProperties() {
                vm.qualityProperties = [];
                vm.inspectionPlanRevisionProperties = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("QUALITY").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newInspectionPlan.id,
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
                            vm.qualityProperties.push(att);
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                loadInspectionPlanCustomProperties();
                $rootScope.$on('app.inspectionPlans.new', create);
            })();
        }
    }
)
;