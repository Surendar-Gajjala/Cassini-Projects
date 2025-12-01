define(
    [
        'app/desktop/modules/req/reqdocs/requirement/requirement.module',
        'app/shared/services/core/reqDocumentService'
    ],
    function (module) {
        module.controller('ReqDocumentReviewersController', ReqDocumentReviewersController);

        function ReqDocumentReviewersController($scope, $rootScope, $timeout, $state, $stateParams, ReqDocumentService, CommonService, $q, $translate, $application) {
            var vm = this;
            vm.reqDocId = $stateParams.reqId;

            $rootScope.loadDocumentReviewers = loadDocumentReviewers;
            function loadDocumentReviewers() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                ReqDocumentService.getDocumentReviewersAndApprovers(vm.reqDocId).then(
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
                $scope.$on('app.req.document.tabActivated', function (event, data) {
                    if (data.tabId == 'details.reviewers') {
                        loadDocumentReviewers();
                    }
                });
            })();
        }
    }
);

