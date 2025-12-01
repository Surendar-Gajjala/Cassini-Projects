define(['app/app.modules', 'app/components/crm/customer/customerFactory'],
    function($app) {
        $app.controller('CustomerTypesController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'customerFactory',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies, customerFactory) {

                    $scope.customerTypes = [];
                    $scope.newCustomerType = {
                        editMode: true,
                        showValues: false,
                        name: "",
                        description: ""
                    };
                    $scope.loading = true;

                    $scope.showDetails = false;
                    $scope.toggleDetails = function() {
                        $scope.showDetails = !$scope.showDetails;
                    };

                    $scope.acceptChanges = function (customerType) {
                        customerType.editMode = false;

                        $timeout(function() {
                            customerType.showValues = true;
                        }, 500);

                        $scope.saveCustomerType(customerType);
                    };

                    $scope.showEditMode = function (customerType) {
                        customerType.editMode = true;
                        customerType.showValues = false;
                    };
                    $scope.hideEditMode = function (customerType) {
                        if(customerType.name == "") {
                            var index = $scope.customerTypes.indexOf(customerType);
                            if(index != -1) {
                                $scope.customerTypes.splice(index, 1);
                            }
                        }
                        else {
                            customerType.editMode = false;

                            $timeout(function () {
                                customerType.showValues = true;
                            }, 500);
                        }
                    };

                    $scope.addCustomerType = function() {
                        var t = angular.copy($scope.newCustomerType);
                        $scope.customerTypes.push(t);
                    };

                    $scope.saveCustomerType = function(customerType) {
                        customerType.name = customerType.newName;
                        customerType.description = customerType.newDescription;
                        customerFactory.saveCustomerType(customerType).then(
                            function(data) {
                                customerType = data;
                            },
                            function(error) {
                                console.error(error);
                            }
                        );
                    };

                    $scope.loadCustomerTypes = function() {
                        $scope.loading = true;
                        customerFactory.getCustomerTypes().then(
                            function(data) {
                                angular.forEach(data, function(type) {
                                    type.editMode = false;
                                    type.showValues = true;
                                    type.newName = type.name;
                                    type.newDescription = type.description;
                                });
                                $scope.customerTypes = data;
                                $scope.loading = false;
                            },
                            function(error) {
                                console.error(error);
                            }
                        );
                    };

                    $scope.loadCustomerTypes();
                }
            ]
        );
    }
);