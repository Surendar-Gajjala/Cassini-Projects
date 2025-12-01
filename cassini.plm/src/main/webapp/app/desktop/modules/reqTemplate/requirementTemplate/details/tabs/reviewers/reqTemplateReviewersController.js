define(
    [
        'app/desktop/modules/req/reqdocs/requirement/requirement.module',
        'app/shared/services/core/requirementService',
        'app/shared/services/core/reqDocTemplateService'
    ],
    function (module) {
        module.controller('ReqTemplateReviewersController', ReqTemplateReviewersController);

        function ReqTemplateReviewersController($scope, $rootScope, $timeout, $state, $stateParams, RequirementService, ReqDocTemplateService, CommonService, $q, $translate, $application) {
            var vm = this;
            vm.reqId = $stateParams.requirementId;


            $rootScope.loadRequirementTemplateReviwers = loadRequirementTemplateReviwers;
            function loadRequirementTemplateReviwers() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                ReqDocTemplateService.getRequirementTemplateReviewers(vm.reqId).then(
                    function (data) {
                        vm.reqReviewers = data;
                        CommonService.getPersonReferences(vm.reqReviewers, 'reviewer');
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                $scope.$on('app.req.template.tabActivated', function (event, data) {
                    if (data.tabId == 'details.reviewers') {
                        loadRequirementTemplateReviwers();
                    }
                });
            })();
        }
    }
);

