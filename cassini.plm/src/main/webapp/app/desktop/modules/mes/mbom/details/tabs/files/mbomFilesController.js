define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('MBOMFilesController', MBOMFilesController);

        function MBOMFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            vm.mbomId = $stateParams.mbomId;

            (function () {
                $scope.$on('app.mbom.tabActivated', function (event, data) {
                    if (data.tabId == 'details.files') {
                        $('.tab-content .tab-pane').css("overflow", "auto");
                        $timeout(function () {
                            $scope.$broadcast('app.objectFile.tabActivated', {});
                        }, 500);
                    }
                });
            })();
        }
    }
);

