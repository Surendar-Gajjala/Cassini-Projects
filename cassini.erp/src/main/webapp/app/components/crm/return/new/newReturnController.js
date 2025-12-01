define(['app/app.modules',
        'app/components/crm/customer/customerFactory',
        'app/components/crm/return/returnFactory',
        'app/components/prod/product/select/productSelectionController',
        'app/components/crm/customer/selectCustomerDialogController'
    ],
    function($app) {
        $app.controller('NewReturnController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams', '$cookies',
                'customerFactory', 'returnFactory', '$modal',

                function($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies,
                         customerFactory, returnFactory, $modal) {

                    $rootScope.iconClass = "fa flaticon-refresh2";
                    $rootScope.viewTitle = "New Return";

                    $scope.customerReturn = null;
                    $scope.returnCreated = false;
                    $scope.selectText = "Select";
                    $scope.createAnother = false;
                    $scope.returnItems = [];
                    $scope.selectedCustomer = null;
                    $scope.reason = "";

                    $scope.selectProducts = function() {
                        //$state.go('app.prod.products');

                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/prod/product/select/productSelectionDialog.jsp',
                            controller: 'ProductSelectionController',
                            size: 'lg',
                            resolve: {
                                selectType: function () {
                                    return "check";
                                }
                            }
                        });

                        modalInstance.result.then(
                            function (selectedProducts) {
                                angular.forEach(selectedProducts, function(prod) {
                                    var item = {
                                        product: prod,
                                        quantity: 0
                                    };
                                    $scope.returnItems.push(item);
                                });
                            },
                            function () {

                            }
                        );
                    };

                    $scope.showReturns = function() {
                        $state.go('app.crm.returns');
                    };

                    $scope.clearReturn = function() {
                        $app.returnItems.items = [];
                    };


                    $scope.createReturn = function() {
                        initCustomerReturn();

                        if(validateReturn() == true) {
                            $rootScope.closeNotification();
                            returnFactory.createReturn($scope.customerReturn).then(
                                function (data) {
                                    $scope.customerReturn = data;
                                    $scope.returnCreated = true;
                                    var msg = "Return created successfully!";
                                    $app.returnCart = null;
                                    $scope.hideSubmitButton = true;
                                    $scope.createAnother = true;
                                    $rootScope.showSuccessMessage(msg);
                                    $scope.createAnother = true;

                                   var source = $stateParams.source;
                                    if(source != null && source != undefined) {
                                        if(source == 'customer.details') {
                                            $state.go('app.crm.customer', {customerId: $rootScope.lastSelectedCustomer.id})
                                        }
                                    }
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                    };

                    function validateReturn() {
                        var valid = true;

                        if($scope.customerReturn.customer == null) {
                            $rootScope.showErrorMessage("Select a customer");
                            valid = false;
                        }
                        else if($scope.reason == null || $scope.reason == "") {
                            $rootScope.showErrorMessage("Reason for return cannot be empty");
                            valid = false;
                        }
                        else if($scope.customerReturn.details.length == 0) {
                            $rootScope.showErrorMessage("There are no items in the return");
                            valid = false;
                        }
                        else {
                            var validQuantity = true;
                            angular.forEach($scope.returnItems, function (item) {
                                if (item.quantity == undefined ||
                                    isNaN(item.quantity) ||
                                    item.quantity < 0) {
                                    validQuantity = false;
                                }
                                else if (!isNaN(item.quantity)) {
                                    var s = "" + item.quantity;
                                    if (s.indexOf('.') != -1) {
                                        validQuantity = false;
                                    }
                                    else if(item.quantity == 0) {
                                        validQuantity = false;
                                    }
                                }
                            });

                            if(validQuantity == false) {
                                $rootScope.showErrorMessage("Quantity has to be a positive whole number greater than zero");
                            }

                            valid = validQuantity;
                        }

                        return valid;
                    }

                    $scope.createAnotherReturn = function() {
                        $scope.customerReturn = null;
                        $scope.returnCreated = false;
                        $scope.selectText = "Select";
                        $scope.createAnother = false;
                        $scope.returnItems = [];
                        $scope.selectedCustomer = null;
                        $scope.reason = "";
                    };

                    $scope.removeItem = function(index) {
                        $scope.returnItems.splice(index, 1);
                    };

                    $scope.showCustomerSelectionDialog = function () {
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/crm/customer/selectCustomerDialog.jsp',
                            controller: 'SelectCustomerDialogController',
                            size: 'lg',
                            resolve: {
                                salesRepFilter: function () {
                                    return null;
                                }
                            }
                        });

                        modalInstance.result.then(
                            function (selectedCustomer) {
                                $scope.selectedCustomer = selectedCustomer;
                                $scope.selectText = "Change";
                            }
                        );
                    };

                    function initCustomerReturn() {
                        var items = [];

                        angular.forEach($scope.returnItems, function(item) {
                            var row = {};
                            row.product = item.product;
                            row.quantity = item.quantity;

                            items.push(row);
                        });

                        if($scope.customerReturn == null) {
                            $scope.customerReturn = {};
                        }
                        if($stateParams.source == 'customer.details') {
                            $scope.selectedCustomer = $rootScope.lastSelectedCustomer;
                        }
                        else {
                            $scope.customerReturn.customer = $scope.selectedCustomer;
                        }
                        $scope.customerReturn.details = items;
                        $scope.customerReturn.reason = $scope.reason;
                    }

                    (function() {
                        if($stateParams.source == 'customer.details') {
                            if($scope.customerReturn == null) {
                                $scope.customerReturn = {};
                            }
                            $scope.customerReturn.customer = $rootScope.lastSelectedCustomer;
                            $scope.selectedCustomer = $rootScope.lastSelectedCustomer;
                        }
                    })();
                }
            ]
        );
    }
);