define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/directives/workflow/workflowDirective'
    ],
    function (module) {
        module.controller('NcrWorkflowController', NcrWorkflowController);

        function NcrWorkflowController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal, httpFactory, CommonService,
                                       InspectionPlanService, WorkflowService, NcrService, PreferenceService, QualityWorkflowService) {

            var vm = this;

            (function () {
                $scope.$on('app.ncr.tabActivated', function (event, args) {
                    if (args.tabId == 'details.workflow') {
                        $scope.$broadcast('app.object.workflow', {});
                    }
                });
            })();
        }
    }
)
;



