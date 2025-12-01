define(['app/app.modules',
        'app/components/crm/vehicle/vehicleFactory'
    ],
    function ($app) {
        $app.controller('VehicleSelectionController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'vehicleFactory','$modalInstance', 'selectedVehicle',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                            vehicleFactory, $modalInstance, selectedVehicle) {
                    $scope.loading = true;
                    $scope.vehicles = [];
                    $scope.selectedVehicle = selectedVehicle;

                    $scope.selectVehicle = function(vehicle) {
                        $scope.selectedVehicle = vehicle;
                    };

                    $scope.ok = function() {
                        $modalInstance.close($scope.selectedVehicle);
                    };

                    $scope.cancel = function() {
                        $modalInstance.dismiss('cancel');
                    };
                    $scope.isVehicleSelected = function() {
                        angular.forEach($scope.vehicles, function(vehicle) {
                            if(vehicle.selected == true) {
                                $scope.selectedVehicle = vehicle;
                            }
                        });
                    };

                    $scope.loadVehicles = function () {
                        vehicleFactory.getVehicles().then (
                            function(data) {
                                $scope.loading = false;
                                $scope.vehicles = data;

                                if($scope.selectedVehicle != null) {
                                    angular.forEach(data, function(vehicle) {
                                        if(vehicle.id == $scope.selectedVehicle.id) {
                                            vehicle.selected = true;
                                        }
                                    });
                                }
                            }
                        );
                    };

                    (function() {
                        $scope.loadVehicles();
                    })();


                }
            ]
        );
    }
);