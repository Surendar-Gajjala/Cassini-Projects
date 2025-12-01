define(
    [
        'app/desktop/modules/req/req.module',
        'app/desktop/modules/directives/timelineDirective',
        'app/shared/services/core/reqDocumentService'

    ],
    function (module) {
        module.controller('ReqDocumentTimeLineController', ReqDocumentTimeLineController);

        function ReqDocumentTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
            $translate, ReqDocumentService) {
            var vm = this;
            vm.reqDocId = $stateParams.reqId;
            $scope.loading = true;

            var parsed = angular.element("<div></div>");

            function loadReqDocRevisions() {
                ReqDocumentService.getReqDocRevisionIds(vm.reqDocId).then(
                    function (data) {
                        vm.reqDocRevisionIds = data;
                        $timeout(function () {
                            $scope.$broadcast('app.object.timeline', {});
                        }, 500);
                    }
                )
            }

            (function () {

                $scope.$on('app.req.document.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timelineHistory') {
                        loadReqDocRevisions();
                    }
                });

            })();
        }
    }
);