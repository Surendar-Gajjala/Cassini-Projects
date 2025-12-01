define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'

    ], function (module) {
        module.controller('PPAPChecklistController', PPAPChecklistController);

        function PPAPChecklistController($scope, $rootScope, $sce, $timeout, $state, $application, $translate, $stateParams, $cookies, $window, ItemTypeService, CommonService) {

            var vm = this;

            vm.ppapId = $stateParams.ppapId;

            (function () {
                $scope.$on('app.ppap.tabActivated', function (event, data) {
                    if (data.tabId == 'details.checklist') {
                        $timeout(function () {
                            $scope.$broadcast('app.objectFile.tabActivated', {});
                        }, 800);
                    }
                })
            })();

        }
    });