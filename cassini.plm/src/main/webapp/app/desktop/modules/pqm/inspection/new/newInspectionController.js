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
        'app/shared/services/core/inspectionService',
        'app/shared/services/core/inspectionPlanService',
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/mfrPartsService',
        'app/desktop/modules/mfr/mfrparts/directive/manufacturerPartDirective',
        'app/shared/services/core/classificationService'
    ],
    function (module) {

        module.controller('NewInspectionController', NewInspectionController);

        function NewInspectionController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, ItemService,
                                         AutonumberService, InspectionPlanService, ObjectTypeAttributeService, ProjectService,
                                         LoginService, QualityTypeService, MfrPartsService, ClassificationService) {

            var vm = this;

            var parsed = angular.element("<div></div>");

            $scope.selectInspectionPlanTitle = parsed.html($translate.instant("SELECT_INSPECTION_PLAN")).html();
            $scope.selectProductTitle = parsed.html($translate.instant("SELECT_PRODUCT_TITLE")).html();
            $scope.selectPartTitle = parsed.html($translate.instant("SELECT_MATERIAL_TITLE")).html();
            var selectInspectionPlanTitle = parsed.html($translate.instant("SELECT_INSPECTION_PLAN")).html();
            var noInspectionPlans = parsed.html($translate.instant("NO_INSPECTION_PLANS")).html();
            $scope.selectAssignedToTitle = parsed.html($translate.instant("SELECT_ASSIGNED_TO")).html();
            $scope.selectWorkflowTitle = parsed.html($translate.instant("SELECT_WORKFLOW")).html();
            var inspectionCreated = parsed.html($translate.instant("INSPECTION_CREATED")).html();
            var selectInspectionPlan = parsed.html($translate.instant("PLAN_SELECT_VALIDATION")).html();
            var selectProduct = parsed.html($translate.instant("PRODUCT_SELECT_VALIDATION")).html();
            var selectManufacturerPartType = parsed.html($translate.instant("SELECT_MFR_PART_TYPE")).html();
            var selectMaterial = parsed.html($translate.instant("SELECT_MATERIAL")).html();
            var enterInspectionPlanNumber = parsed.html($translate.instant("ENTER_INSPECTION_NUMBER")).html();
            var assignedToValid = parsed.html($translate.instant("ASSIGNED_TO_SELECT_VALID")).html();
            var workflowSelectValid = parsed.html($translate.instant("WORKFLOW_SELECT_VALID")).html();
            var attributeRequired = $translate.instant("ATTRIBUTE_REQUIRED");
            $scope.selectedInspectionType = $scope.data.inspectionType;
            $scope.actionType = $scope.data.actionType;
            vm.onSelectProduct = onSelectProduct;
            vm.onSelectPart = onSelectPart;
            vm.onSelectInspectionPlan = onSelectInspectionPlan;

            vm.newInspection = {
                id: null,
                item: null,
                material: null,
                inspectionNumber: null,
                inspectionPlan: null,
                assignedTo: null,
                workflow: null,
                status: "NONE",
                deviationSummary: null,
                notes: null
            };

            vm.onSelectPartType = onSelectPartType;
            function onSelectPartType(partType) {
                vm.selectedPartType = partType;
                $rootScope.showBusyIndicator($("#rightSidePanel"));
                if(partType != null) {
                    MfrPartsService.getManufacturerPartsByType(partType.id).then(
                        function (data) {
                            vm.materials = data;
                            vm.newInspection.material = null;
                            vm.newInspection.inspectionPlan = null;
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
                else {
                    $rootScope.hideBusyIndicator();
                }
            }

            $scope.selectInspectionType = selectInspectionType;
            function selectInspectionType(value) {
                $scope.selectedInspectionType = value;
                vm.newInspection = {
                    id: null,
                    item: null,
                    material: null,
                    inspectionNumber: null,
                    inspectionPlan: null,
                    assignedTo: null,
                    workflow: null,
                    status: "NONE",
                    deviationSummary: null,
                    notes: null
                };
                $scope.$evalAsync();
            }

            function onSelectProduct(product) {
                vm.selectedProduct = product;
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                InspectionPlanService.getReleasedInspectionPlanByProduct(product.latestRevision).then(
                    function (data) {
                        vm.inspectionPlans = data;
                        vm.newInspection.inspectionPlan = null;
                        if (vm.inspectionPlans.length > 0) {
                            $scope.selectInspectionPlanTitle = selectInspectionPlanTitle;
                        } else {
                            $scope.selectInspectionPlanTitle = noInspectionPlans;
                        }
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function onSelectPart(part) {
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                InspectionPlanService.getReleasedInspectionPlanByPart(part.id).then(
                    function (data) {
                        vm.inspectionPlans = data;
                        if (vm.inspectionPlans.length > 0) {
                            $scope.selectInspectionPlanTitle = selectInspectionPlanTitle;
                        } else {
                            $scope.selectInspectionPlanTitle = noInspectionPlans;
                        }
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            function onSelectInspectionPlan(inspectionPlan) {
                if (inspectionPlan != null && inspectionPlan != undefined) {
                    vm.selectedInspectionPlan = inspectionPlan;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.selectedInspectionPlan != null) {
                    var source = vm.selectedInspectionPlan.planType.inspectionNumberSource;
                    AutonumberService.getNextNumber(source.id).then(
                        function (data) {
                            vm.newInspection.inspectionNumber = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function create() {
                if (validate()) {
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
                    var qualityAttributeDto = {
                        objectAttributes: vm.qualityProperties
                    }
                    if ($scope.selectedInspectionType == "ITEMINSPECTION") {
                        qualityAttributeDto.itemInspection = vm.newInspection
                    } else {
                        qualityAttributeDto.materialInspection = vm.newInspection
                    }
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    QualityTypeService.createQualityObject($scope.selectedInspectionType, qualityAttributeDto).then(
                        function (data) {

                            if ($scope.selectedInspectionType == "ITEMINSPECTION") {
                                vm.newInspection = data.itemInspection;
                            } else {
                                vm.newInspection = data.materialInspection;
                            }
                            saveCustomAttributes().then(
                                function (atts) {
                                    $scope.callback(vm.newInspection);
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.hideSidePanel();
                                    $rootScope.showSuccessMessage(inspectionCreated);
                                    vm.newInspection = {
                                        id: null,
                                        item: null,
                                        inspectionNumber: null,
                                        inspectionPlan: null,
                                        assignedTo: null,
                                        workflow: null,
                                        deviationSummary: null,
                                        notes: null
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
                if ($scope.selectedInspectionType == "ITEMINSPECTION" && (vm.newInspection.item == null || vm.newInspection.item == "" || vm.newInspection.item == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(selectProduct);
                } else if ($scope.selectedInspectionType == "MATERIALINSPECTION" && (vm.selectedPartType == null || vm.selectedPartType == "" || vm.selectedPartType == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(selectManufacturerPartType);
                } else if ($scope.selectedInspectionType == "MATERIALINSPECTION" && (vm.newInspection.material == null || vm.newInspection.material == "" || vm.newInspection.material == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(selectMaterial);
                } else if (vm.newInspection.inspectionPlan == null || vm.newInspection.inspectionPlan == "" || vm.newInspection.inspectionPlan == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectInspectionPlan);
                } else if (vm.newInspection.inspectionNumber == null || vm.newInspection.inspectionNumber == "" || vm.newInspection.inspectionNumber == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(enterInspectionPlanNumber);
                } else if (vm.newInspection.assignedTo == null || vm.newInspection.assignedTo == "" || vm.newInspection.assignedTo == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(assignedToValid);
                } else if (vm.newInspection.workflow == null || vm.newInspection.workflow == "" || vm.newInspection.workflow == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(workflowSelectValid);
                } else if ((vm.qualityProperties.length > 0) && !validateCustomAttributes()) {
                    valid = false;
                }

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

            function saveCustomAttributes() {
                var defered = $q.defer();
                if (vm.inspectionImageAttributes.length > 0 || vm.inspectionAttachmentAttributes.length > 0) {
                    angular.forEach(vm.inspectionImageAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeImageValue(vm.newInspection.objectType, vm.newInspection.id, imgAtt.id.attributeDef, vm.inspectionImages.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    )

                    angular.forEach(vm.inspectionAttachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues(vm.newInspection.objectType, vm.newInspection.id, imgAtt.id.attributeDef, vm.inspectionAttachments.get(imgAtt.id.attributeDef)).then(
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

            function loadInspectionCustomProperties() {
                vm.qualityProperties = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("QUALITY").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newInspection.id,
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

            function loadWorkflows() {
                var promise = null;
                if ($scope.selectedInspectionType == "ITEMINSPECTION") {
                    promise = ProjectService.getWorkflows("ITEM INSPECTIONS");
                } else {
                    promise = ProjectService.getWorkflows("MATERIAL INSPECTIONS");
                }
                if (promise != null) {
                    promise.then(
                        function (data) {
                            vm.workflows = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
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

            function loadProductItems() {
                ItemService.getNormalAndConfigurableItemsByItemClass("PRODUCT").then(
                    function (data) {
                        vm.productItems = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                loadProductItems();
                loadInspectionCustomProperties();
                loadWorkflows();
                loadPersons();
                $rootScope.$on('app.inspections.new', create);
            })();
        }
    }
)
;