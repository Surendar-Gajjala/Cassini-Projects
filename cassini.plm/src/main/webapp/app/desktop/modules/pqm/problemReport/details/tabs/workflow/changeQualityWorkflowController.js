define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'
    ],
    function (module) {
        module.controller('ChangeQualityWorkflowController', ChangeQualityWorkflowController);

        function ChangeQualityWorkflowController($scope, $rootScope, $stateParams, $translate, $timeout, CommonService, ProjectService, QualityWorkflowService, QualityTypeService) {
            var vm = this;

            vm.workflows = [];
            var item = $scope.data.selectedItem;
            var objectType = $scope.data.selectedItemType;

            var parsed = angular.element("<div></div>");
            vm.selectWorkflow = parsed.html($translate.instant("SELECT")).html();
            var workflowChangeMsg = parsed.html($translate.instant("WORKFLOW_CHANGE_MSG")).html();
            var workflowChangeNoMsg = parsed.html($translate.instant("WORKFLOW_CHANGE_NO_MSG")).html();

            vm.changeWf = {
                workflowDefinition: null
            };


            function changeQualityWorkflow() {
                if (vm.changeWf.workflowDefinition != null) {
                    QualityWorkflowService.deleteObjectWorkflow(item.id).then(
                        function () {
                            QualityWorkflowService.attachObjectWorkflow(item.id, vm.changeWf.workflowDefinition.id, item.objectType).then(
                                function (data) {
                                    $rootScope.hideSidePanel();
                                    $rootScope.showSuccessMessage(workflowChangeMsg);
                                    $scope.callback(data);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        })
                } else if (vm.changeWf.workflowDefinition == null) {
                    $rootScope.showWarningMessage(workflowChangeNoMsg);
                }
            }

            function loadWorkflows() {
                if (objectType == "ITEM INSPECTIONS" || objectType == "MATERIAL INSPECTIONS") {
                    ProjectService.getWorkflows(objectType).then(
                        function (data) {
                            vm.wfs = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    QualityTypeService.getQualityTypeWorkflows(objectType.id, 'QUALITY').then(
                        function (data) {
                            vm.wfs = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            (function () {
                //if ($application.homeLoaded == true) {
                $rootScope.$on('app.workflow.change.quality', changeQualityWorkflow);
                loadWorkflows();
                //}
            })();
        }
    }
);