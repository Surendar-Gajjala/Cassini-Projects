define(
    [
        'app/desktop/desktop.app'
    ],

    function (module) {
        module.directive('mesMfrData', ['$rootScope', '$compile', '$timeout', '$application', function ($rootScope, $compile, $timeout, $application) {

            return {
                templateUrl: 'app/desktop/directives/mes-mfr-data/mesMfrDataDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    updateType: '=',
                    hasPermission: '=',
                    manufacturerData: '=',
                    updateManufacturer: '='
                },
                link: function ($scope, elem, attr) {
                    $scope.editMfrDate = false;
                    $scope.mfrDate = $scope.manufacturerData.mfrDate;
                    $scope.updateData = updateData;
                    $scope.changeDate = changeDate;
                    $scope.cancelDate = cancelDate;

                    function changeDate() {
                        $scope.editMfrDate = true;
                        $scope.mfrDate = $scope.manufacturerData.mfrDate;
                    }

                    function cancelDate() {
                        $scope.editMfrDate = false;
                        $scope.manufacturerData.mfrDate = $scope.mfrDate;
                    }

                    function updateData() {
                        $scope.updateManufacturer();
                    }
                }
            };
        }]);
    }
);
