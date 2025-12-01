define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/requirementsTypeService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/activityService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'
    ],
    function (module) {
        module.controller('AddWorkflowController', AddWorkflowController);

        function AddWorkflowController($scope, $rootScope, $stateParams, $translate, $timeout, CommonService, ItemService, MfrService, MfrPartsService, SpecificationsService, RequirementsTypeService, ProjectService, ActivityService) {
            var vm = this;

            vm.workflows = [];
            var item = $scope.data.selectedItem;
            vm.type = $scope.data.selectedType;
            var objectType = $scope.data.selectedItemType;

            var parsed = angular.element("<div></div>");
            vm.selectWorkflow = parsed.html($translate.instant("SELECT")).html();
            var workflowAttached = parsed.html($translate.instant("WORKFLOW_ATTACHED_MSG")).html();
            var workflowChangeNoMsg = parsed.html($translate.instant("WORKFLOW_CHANGE_NO_MSG")).html();

            vm.addWf = {
                workflowDefinition: null
            };

            function addWorkflow() {
                if (vm.addWf.workflowDefinition != null) {
                    if (vm.type == "ITEMS") {
                        ItemService.attachWorkflow(item.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }, function (error) {
                                  $rootScope.showErrorMessage(error.message);
                                  $rootScope.hideBusyIndicator();
                             }
                        )
                    }
                    if (vm.type == "SPECIFICATIONS") {
                        SpecificationsService.attachWorkflow(item.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                    if (vm.type == "REQUIREMENTS") {
                        RequirementsTypeService.attachWorkflow(item.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                    if (vm.type == "MANUFACTURERS") {
                        MfrService.attachWorkflow(item.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                    if (vm.type == "MANUFACTURER PARTS") {
                        MfrPartsService.attachWorkflow(item.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                    if (vm.type == "PROJECTS") {
                        ProjectService.attachWorkflow(item.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                    if (vm.type == "PROJECT ACTIVITIES") {
                        ActivityService.attachWorkflow(item.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                } else if (vm.addWf.workflowDefinition == null) {
                    $rootScope.showWarningMessage(workflowChangeNoMsg)
                }

            }

            function loadWorkflows() {
                if (vm.type == "ITEMS") {
                    ItemService.getWorkflows(objectType.id, vm.type).then(
                        function (data) {
                            vm.wfs = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                }
                if (vm.type == "SPECIFICATIONS") {
                    SpecificationsService.getWorkflows(objectType.id, 'REQUIREMENTS').then(
                        function (data) {
                            vm.wfs = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                }
                if (vm.type == "REQUIREMENTS") {
                    RequirementsTypeService.getWorkflows(objectType.id, vm.type).then(
                        function (data) {
                            vm.wfs = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                }
                if (vm.type == "MANUFACTURERS") {
                    MfrService.getWorkflows(objectType.id, vm.type).then(
                        function (data) {
                            vm.wfs = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                }
                if (vm.type == "MANUFACTURER PARTS") {
                    MfrPartsService.getWorkflows(objectType.id, vm.type).then(
                        function (data) {
                            vm.wfs = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                }
                if (vm.type == "PROJECTS") {
                    ProjectService.getWorkflows(vm.type).then(
                        function (data) {
                            vm.wfs = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                }
                if (vm.type == "PROJECT ACTIVITIES") {
                    ProjectService.getWorkflows(vm.type).then(
                        function (data) {
                            vm.wfs = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                }
            }

            (function () {
                //if ($application.homeLoaded == true) {
                $rootScope.$on('app.workflow.add', addWorkflow);
                loadWorkflows();
                //}
            })();
        }
    }
);