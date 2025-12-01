define(
    [
        'app/desktop/modules/req/reqdocs/requirement/requirement.module',
        'app/shared/services/core/requirementService'
    ],
    function (module) {
        module.controller('ReqReviewersController', ReqReviewersController);

        function ReqReviewersController($scope, $rootScope, $timeout, $state, $stateParams, RequirementService, CommonService, $q, $translate, $application) {
            var vm = this;
            vm.reqId = $stateParams.requirementId;


            $rootScope.loadReviwers = loadReviwers;
            function loadReviwers() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                RequirementService.getReviewersAndApprovers(vm.reqId).then(
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
                $scope.$on('app.req.requirement.tabActivated', function (event, data) {
                    if (data.tabId == 'details.reviewers') {
                        loadReviwers();
                    }
                });
            })();
        }
    }
);

