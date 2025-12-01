define(
    [
        'app/desktop/modules/classification/classification.module',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/classificationService',
        'app/desktop/modules/classification/workflowDirective/itemClassificationDirective',
        'app/desktop/modules/classification/workflowDirective/itemClassificationController',
        'app/desktop/modules/classification/workflowDirective/workflowChangeDirective',
        'app/desktop/modules/classification/workflowDirective/workflowManufacturerDirective',
        'app/desktop/modules/directives/mesWorkflowTypeDirective',
        'app/desktop/modules/classification/workflowDirective/workflowManufacturerPartDirective',
        'app/desktop/modules/classification/workflowDirective/qualityWorkflowDirective',
        'app/desktop/modules/classification/workflowDirective/requirementWorkflowDirectiveController',
        'app/desktop/modules/classification/workflowDirective/mbomWorkTypeDirective',
        'app/desktop/modules/directives/customObjectTypeDirective',
        'app/desktop/modules/directives/mroObjectTypeDirective',
        'app/desktop/modules/classification/workflowDirective/mroWorkTypeDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/desktop/modules/directives/pmObjectTypeDirective',
        'app/shared/services/core/workflowService'
    ],
    function (module) {
        module.controller('WorkflowTypeBasicController', WorkflowTypeBasicController);

        function WorkflowTypeBasicController($scope, $rootScope, $timeout, $window, $state, $stateParams, $cookies, ItemTypeService,
                                             ClassificationService, AutonumberService, CommonService, DialogService, LovService, $translate, WorkflowService) {

            var parsed = angular.element("<div></div>");
            var vm = this;
            vm.valid = true;
            vm.error = "";
            vm.workflowType = null;
            vm.autoNumbers = [];
            vm.selectedTypes = ['ITEMS', 'CHANGES', 'QUALITY', 'INSPECTIONS', 'MANUFACTURERS', 'MANUFACTURER PARTS', 'PROGRAM', 'PROJECTS', 'PROJECT TASKS', 'PROJECT ACTIVITIES', 'PROJECT TEMPLATE', 'TASK TEMPLATE', 'PROGRAM TEMPLATE', 'MANUFACTURING', 'MAINTENANCE&REPAIR', 'REQUIREMENT DOCUMENTS', 'REQUIREMENT', 'CUSTOM OBJECTS'];
            vm.onSave = onSave;
            var nodeId = null;
            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            var workflowTypeSuccessMessage = $translate.instant("MFR_PART_TYPE_SUCCESS_MESSAGE");
            var worklfowTypeUpdateMessage = $translate.instant("WRF_TYPE_UPDATE_MESSAGE");
            var workflowTypeValidation = $translate.instant("WRF_TYPE_NAME_VALIDATION");
            var workflowTypeAssignableValidation = $translate.instant("WRF_TYPE_ASSIGNABLE_VALIDATION");
            var Duplicate = $translate.instant("DUPLICATE_MESSAGE");
            var mfrTypeValidation = $translate.instant("MANUFACTURER_TYPE_VALIDATE");
            var mfrPartTypeValidation = $translate.instant("MFR_PART_TYPE_VALIDATION");
            var itemTypeValidation = $translate.instant("ITEM_TYPE_EMPTY");
            var changeValidation = $translate.instant("CHANGE_TYPE_EMPTY");
            var specValidation = $translate.instant("SPECIFICATION_TYPE_EMPTY");
            var reqValidation = $translate.instant("REQUIREMENT_TYPE_EMPTY");
            var typeValidation = $translate.instant("ITEM_TYPE_VALIDATION");
            var customTypeValidation = $translate.instant("CUSTOM_TYPE_VALIDATION");
            var typeAssignableValidation = $translate.instant("WRF_TYPE_TYPE_VALIDATION");

            function onSave() {
                if (vm.workflowType != null && validate()) {
                    var id = $rootScope.selectedClassificationType.id;
                    $rootScope.selectedClassificationType.id = null;
                    ClassificationService.updateType('WORKFLOWTYPE', vm.workflowType).then(
                        function (data) {
                            if (data != null) {
                                $rootScope.selectedClassificationType = data;
                                $rootScope.showSuccessMessage(data.name + " : " + worklfowTypeUpdateMessage);
                                $rootScope.$broadcast("app.classification.update", {
                                    nodeId: nodeId,
                                    nodeName: vm.workflowType.name
                                });
                            }
                            else if (data == null) {
                                $rootScope.selectedClassificationType.id = id;
                                $rootScope.showErrorMessage(vm.workflowType.name + " : " + Duplicate);
                            }
                        },
                        function (error) {
                            $rootScope.selectedClassificationType.id = id;
                            $rootScope.showErrorMessage(error.message);
                        }
                    )

                }
            }

            function validate() {
                vm.valid = true;

                $rootScope.closeNotification();

                if (vm.workflowType.name == null || vm.workflowType.name == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage(workflowTypeValidation);
                } else if (vm.workflowType.assignable == null || vm.workflowType.assignable == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage(workflowTypeAssignableValidation);
                } else if (vm.workflowType.assignable == "ITEMS" || vm.workflowType.assignable == "CHANGES" || vm.workflowType.assignable == "MANUFACTURER PARTS" || vm.workflowType.assignable == "MANUFACTURING"
                    || vm.workflowType.assignable == "MANUFACTURERS" || vm.workflowType.assignable == "SPECIFICATIONS" || vm.workflowType.assignable == "REQUIREMENTS"
                    || vm.workflowType.assignable == "QUALITY" || vm.workflowType.assignable == "MAINTENANCE&REPAIR" || vm.workflowType.assignable == "CUSTOM OBJECTS") {
                    if (vm.workflowType.itemType == null) {
                        vm.valid = false;
                        if (vm.workflowType.assignable == "ITEMS") {
                            $rootScope.showErrorMessage(itemTypeValidation);
                        }
                        else if (vm.workflowType.assignable == "CHANGES") {
                            $rootScope.showErrorMessage(changeValidation);
                        }
                        else if (vm.workflowType.assignable == "MANUFACTURERS") {
                            $rootScope.showErrorMessage(mfrTypeValidation);
                        }
                        else if (vm.workflowType.assignable == "MANUFACTURER PARTS") {
                            $rootScope.showErrorMessage(mfrPartTypeValidation);
                        }
                        else if (vm.workflowType.assignable == "SPECIFICATIONS") {
                            $rootScope.showErrorMessage(specValidation);
                        }
                        else if (vm.workflowType.assignable == "REQUIREMENTS") {
                            $rootScope.showErrorMessage(reqValidation);
                        }
                        else if (vm.workflowType.assignable == "QUALITY") {
                            $rootScope.showErrorMessage(typeValidation);
                        }
                        else if (vm.workflowType.assignable == "MAINTENANCE&REPAIR") {
                            $rootScope.showErrorMessage(typeValidation);
                        }
                        else if (vm.workflowType.assignable == "MANUFACTURING") {
                            $rootScope.showErrorMessage(typeValidation);
                        }
                        else if (vm.workflowType.assignable == "CUSTOM OBJECTS") {
                            $rootScope.showErrorMessage(customTypeValidation);
                        }
                    }
                }
                return vm.valid;
            }

            $rootScope.workflowTypeSelected = workflowTypeSelected;

            function workflowTypeSelected() {
                if ($rootScope.selectedWfType != null) {
                    vm.workflowType = $rootScope.selectedWfType;
                    nodeId = $rootScope.selectedWfTypeNodeId;
                    if (vm.workflowType != null && vm.workflowType != undefined) {
                        if (vm.workflowType.id == undefined || vm.workflowType.id == null) {
                            vm.attributesShow = false;
                            //$scope.$apply();
                        } else {
                            vm.attributesShow = true;
                            //$scope.$apply();
                        }
                        vm.workflowType.attributes = [];
                        loadAutoNumbers();
                        loadAssignedWorkflow(vm.workflowType);
                    } else {
                        if (!$scope.$$phase) $scope.$apply();
                    }
                }

            }

            function loadLifeCycles() {
                ItemTypeService.getLifeCycles().then(
                    function (data) {
                        vm.lifecycles = data;
                        loadWorkflowTypeWorkflows();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            vm.loadWfType = loadWfType;
            function loadWfType() {
                if (vm.workflowType.assignedType != null && vm.workflowType.assignedType != "" && vm.workflowType.assignedType != undefined) {
                    ClassificationService.getAssignedObjectType($rootScope.selectedWfType.id, vm.workflowType.assignedType, vm.workflowType.assignable).then(
                        function (data) {
                            vm.workflowType.itemType = data;
                            $timeout(function () {
                                $scope.$off('app.workflowType.selected', workflowTypeSelected);
                            }, 200)
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadAssignedWorkflow(workflow) {
                vm.assignedWorkflows = [];
                WorkflowService.getUsedWorkflows(workflow.id).then(
                    function (data) {
                        vm.usedWorkflows = data;
                        loadWfType();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadWorkflowTypeWorkflows() {
                if (vm.workflowType.id != null) {
                    WorkflowService.getWorkflowsByType(vm.workflowType.id, pageable).then(
                        function (data) {
                            vm.workflowTypeWorkflows = data.content;
                            if (vm.workflowTypeWorkflows.length > 0) {
                                vm.workflowTypeWorkflowsExist = true;
                            } else {
                                vm.workflowTypeWorkflowsExist = false;
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadAutoNumbers() {
                AutonumberService.getAutonumbers().then(
                    function (data) {
                        vm.autoNumbers = data;
                        angular.forEach(data, function (item) {
                            if (vm.workflowType.itemNumberSource == null &&
                                item.name == "Default Workflow Number Source") {
                                vm.workflowType.itemNumberSource = item;
                            }
                        });
                        $timeout(function () {
                            loadLifeCycles();
                        }, 500);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.onSelectType = onSelectType;

            function onSelectType(itemType) {
                vm.workflowType.assignedType = null;
                vm.workflowType.itemType = null;
                if (itemType != null && itemType != undefined) {
                    vm.workflowType.itemType = itemType;
                    vm.workflowType.assignedType = vm.workflowType.itemType.id;
                }
            }


            vm.changeAssignable = changeAssignable;
            function changeAssignable() {
                vm.workflowType.itemType = null;

            }


            (function () {
                $scope.$on('app.workflowType.tabactivated', function (event, data) {
                    $rootScope.workflowTypeSelected();
                    //$scope.$on('app.workflowType.selected', workflowTypeSelected);
                    $scope.$on('app.workflowType.save', onSave)
                })


            })();
        }
    });