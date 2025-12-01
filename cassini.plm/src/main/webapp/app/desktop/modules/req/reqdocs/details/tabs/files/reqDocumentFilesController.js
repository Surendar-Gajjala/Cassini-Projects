define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('ReqDocumentFilesController', ReqDocumentFilesController);

        function ReqDocumentFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            vm.reqDocId = $stateParams.reqId;

            (function () {
                $scope.$on('app.req.document.tabActivated', function (event, data) {
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

