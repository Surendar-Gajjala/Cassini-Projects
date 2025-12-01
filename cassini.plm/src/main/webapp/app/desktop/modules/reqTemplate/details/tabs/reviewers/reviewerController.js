define(
    [
        'app/desktop/modules/req/req.module',
        'app/shared/services/core/reqDocTemplateService'

    ],
    function (module) {
        module.controller('ReqDocTemplateReviewerController', ReqDocTemplateReviewerController);

        function ReqDocTemplateReviewerController($scope, $rootScope, $timeout, $state, $stateParams, CommonService, $q, $translate, $application, ReqDocTemplateService) {

            var vm = this;
            vm.reqDocId = $stateParams.reqDocId;


            $rootScope.loadDocumentTemplateReviewers = loadDocumentTemplateReviewers;
            function loadDocumentTemplateReviewers() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                ReqDocTemplateService.getDocTemplateReviewers(vm.reqDocId).then(
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
                $scope.$on('app.req.documentTemplate.tabActivated', function (event, data) {
                    if (data.tabId == 'details.reviewers') {
                        loadDocumentTemplateReviewers();
                    }
                });
            })();
        }
    }
);
