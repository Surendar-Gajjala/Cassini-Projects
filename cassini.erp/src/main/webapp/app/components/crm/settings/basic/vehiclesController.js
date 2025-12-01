define(['app/app.modules',
        'app/components/crm/vehicle/vehicleFactory'
    ],
    function ($app) {
        $app.controller('VehiclesController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'vehicleFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                        vehicleFactory) {

                    $scope.vehicles = [];
                    $scope.newVehicle = {
                        editMode: true,
                        showValues: false,
                        number: "",
                        description: ""
                    };
                    $scope.loading = true;
                    $scope.showDetails = false;

                    $scope.toggleDetails = function() {
                        $scope.showDetails = !$scope.showDetails;
                    };

                    $scope.acceptChanges = function (vehicle) {
                        vehicle.editMode = false;

                        $timeout(function() {
                            vehicle.showValues = true;
                        }, 500);

                        $scope.saveVehicle(vehicle);
                    };

                    $scope.showEditMode = function (vehicle) {
                        vehicle.editMode = true;
                        vehicle.showValues = false;
                    };

                    $scope.hideEditMode = function (vehicle) {
                        if(vehicle.number == "") {
                            var index = $scope.vehicles.indexOf(vehicle);
                            if(index != -1) {
                                $scope.vehicles.splice(index, 1);
                            }
                        }
                        else {
                            vehicle.editMode = false;

                            $timeout(function() {
                                vehicle.showValues = true;
                            }, 500);
                        }
                    };

                    $scope.addVehicle = function() {
                        var t = angular.copy($scope.newVehicle);
                        $scope.vehicles.push(t);
                    };

                    $scope.saveVehicle = function(vehicle) {
                        vehicle.number = vehicle.newNumber;
                        vehicle.description = vehicle.newDescription;
                        vehicleFactory.createVehicle(vehicle).then(
                            function(data) {
                                vehicle = data;
                            },
                            function(error) {
                                console.error(error);
                            }
                        );
                    };

                    function loadVehicles() {
                        vehicleFactory.getVehicles().then(
                            function(data) {
                                angular.forEach(data, function(vehicle) {
                                    vehicle.editMode = false;
                                    vehicle.showValues = true;
                                    vehicle.newNumber = vehicle.number;
                                    vehicle.newDescription = vehicle.description;
                                });
                                $scope.vehicles = data;
                                $scope.loading = false;
                            }
                        );
                    }

                    (function() {
                        loadVehicles();
                    })();

                }
            ]
        );
    }
);