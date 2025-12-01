define(
    [
        'app/desktop/modules/req/reqdocs/requirement/requirement.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('ReqFilesController', ReqFilesController);

        function ReqFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            vm.reqId = $stateParams.requirementId;

            (function () {
                $scope.$on('app.req.requirement.tabActivated', function (event, data) {
                    if (data.tabId == 'details.files') {
                        $timeout(function () {
                            $scope.$broadcast('app.objectFile.tabActivated', {});
                        }, 500);
                    }
                });
            })();
        }
    }
);

