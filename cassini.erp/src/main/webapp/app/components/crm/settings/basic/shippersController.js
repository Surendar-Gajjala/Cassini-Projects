define(['app/app.modules',
        'app/components/common/commonFactory',
        'app/components/crm/order/shipment/shipmentFactory'],
    function($app) {
        $app.controller('ShippersController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'commonFactory', 'shipmentFactory',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies,
                         commonFactory, shipmentFactory) {

                    $scope.loading = false;
                    $scope.shippers = [];
                    $scope.emptyShipper = {
                        id: null,
                        name: null,
                        address: {
                            addressType: commonFactory.getAddressTypeByName("Office"),
                            addressText: null,
                            city: null,
                            state: null,
                            pincode: null,
                            country: commonFactory.getCountryAndStatesMapByCountry("India").country
                        }
                    };
                    $scope.mode = "new";
                    $scope.states = commonFactory.getCountryAndStatesMapByCountry("India").states;

                    $scope.showContents = false;
                    $scope.showForm = false;

                    $scope.toggleDetails = function() {
                        $scope.showContents = !$scope.showContents;
                    };

                    $scope.processShipper = function() {
                        if($scope.mode == 'new') {
                            $scope.createShipper();
                        }
                        else {
                            $scope.saveShipper();
                        }
                    };

                    $scope.closeForm = function() {
                        $scope.showForm = false;
                    };

                    $scope.createNewShipper = function() {
                        $scope.mode = "new";
                        $scope.showForm = true;
                        $scope.shipper = angular.copy($scope.emptyShipper);
                    };

                    $scope.createShipper = function() {
                        $scope.shipper.name = $scope.shipper.newName;
                        $scope.shipper.officePhone = $scope.shipper.newOfficePhone;
                        $scope.shipper.officeFax = $scope.shipper.newOfficeFax;
                        $scope.shipper.officeEmail = $scope.shipper.newOfficeEmail;
                        $scope.shipper.address.addressText = $scope.shipper.address.newAddressText;
                        $scope.shipper.address.city = $scope.shipper.address.newCity;
                        $scope.shipper.address.state = $scope.shipper.address.newState;
                        $scope.shipper.address.pincode = $scope.shipper.address.newPincode;

                        shipmentFactory.createShipper($scope.shipper).then(
                            function(data) {
                                $rootScope.showSuccessMessage("New shipper created successfully!");
                                $scope.showForm = false;
                                $scope.shippers.push(data);
                            }
                        )
                    };

                    $scope.updateShipper = function(shipper) {
                        $scope.mode = "update";
                        $scope.shipper = shipper;
                        $scope.showForm = true;
                    };

                    $scope.removeItem=function($index){
                        $scope.shippers.splice($index,1);
                    }

                    $scope.saveShipper = function() {
                        $scope.shipper.name = $scope.shipper.newName;
                        $scope.shipper.officePhone = $scope.shipper.newOfficePhone;
                        $scope.shipper.officeFax = $scope.shipper.newOfficeFax;
                        $scope.shipper.officeEmail = $scope.shipper.newOfficeEmail;
                        $scope.shipper.address.addressText = $scope.shipper.address.newAddressText;
                        $scope.shipper.address.city = $scope.shipper.address.newCity;
                        $scope.shipper.address.state = $scope.shipper.address.newState;
                        $scope.shipper.address.pincode = $scope.shipper.address.newPincode;

                        shipmentFactory.updateShipper($scope.shipper).then(
                            function(data) {
                                $rootScope.showSuccessMessage("Shipper information updated successfully!");
                                $scope.showForm = false;
                            }
                        );
                    };

                    function loadShippers() {
                        shipmentFactory.getShippers().then(
                            function(data) {
                                angular.forEach(data, function(shipper) {
                                    shipper.newName = shipper.name;
                                    shipper.newOfficePhone = shipper.officePhone;
                                    shipper.newOfficeFax = shipper.officeFax;
                                    shipper.newOfficeEmail = shipper.officeEmail;
                                    shipper.address.newAddressText = shipper.address.addressText;
                                    shipper.address.newCity = shipper.address.city;
                                    shipper.address.newState = shipper.address.state;
                                    shipper.address.newPincode = shipper.address.pincode;
                                });
                                $scope.shippers = data;
                            }
                        );
                    }

                    (function(){
                        loadShippers();
                    })();
                }
            ]
        );
    }
);