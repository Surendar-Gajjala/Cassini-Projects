define([
        'app/desktop/modules/mfr/mfrparts/mfrparts.module',
        'app/desktop/modules/directives/files/changeFilesDirectiveController'

    ],
    function (module) {
        module.controller('MfrPartInspectionReportsController', MfrPartInspectionReportsController);

        function MfrPartInspectionReportsController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies) {

            var vm = this;
            vm.mfrPartId = $stateParams.manufacturePartId;

            (function () {
                $scope.$on('app.mfrPart.tabactivated', function (event, data) {
                    if (data.tabId == 'details.inspectionReports') {
                        $timeout(function () {
                            $scope.$broadcast('app.changeFile.tabActivated', {});
                        }, 500);
                    }
                });
            })();

        }
    }
);