define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('SparePartFilesController', SparePartFilesController);

        function SparePartFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            vm.sparePartId = $stateParams.sparePartId;

            (function () {
                $scope.$on('app.sparePart.tabActivated', function (event, data) {
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

