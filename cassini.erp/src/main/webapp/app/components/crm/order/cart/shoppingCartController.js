define(['app/app.modules',
        'app/components/common/commonFactory',
        'app/components/crm/customer/customerFactory',
        'app/components/crm/order/orderFactory',
        'app/components/crm/customer/selectCustomerDialogController',
        'app/components/prod/product/productFactory',
        'app/shared/directives/commonDirectives'
    ],
    function ($app) {
        $app.controller('ShoppingCartController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'commonFactory', 'customerFactory', 'orderFactory', 'productFactory', '$modal',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          commonFactory, customerFactory, orderFactory, productFactory, $modal) {

                    $rootScope.iconClass = "fa fa-shopping-cart";
                    $rootScope.viewTiatle = "Shopping Cart";

                    $scope.states = commonFactory.getCountryAndStatesMapByCountry("India").states;

                    $rootScope.orderUpdated = false;
                    $scope.quickOrderForm = false;
                    $rootScope.shoppingCart = $app.shoppingCart;
                    $scope.customerOrder = null;
                    $rootScope.orderCreated = false;
                    $scope.selectText = $scope.shoppingCart.customer == null ? "Select" : "Change";

                    $rootScope.creatingOrder = false;

                    var address = {
                        addressType: commonFactory.getAddressTypeByName("Billing"),
                        addressText: null,
                        city: null,
                        state: null,
                        country: commonFactory.getCountryAndStatesMapByCountry("India").country,
                        pincode: null
                    };

                    if ($scope.shoppingCart.shipTo == undefined) {
                        $scope.shoppingCart.shipTo = null;
                    }

                    if ($scope.shoppingCart.billingAddress == null || $scope.shoppingCart.billingAddress == undefined) {
                        if ($scope.shoppingCart.customer != null && $scope.shoppingCart.customer.customerAddresses.length > 0) {
                            $scope.shoppingCart.billingAddress = $scope.shoppingCart.customer.customerAddresses[0];
                        }
                        else {
                            $scope.shoppingCart.billingAddress = angular.copy(address);
                        }
                        $scope.shoppingCart.shippingSameAsBilling = true;
                    }

                    if ($scope.shoppingCart.shippingAddress == null || $scope.shoppingCart.shippingAddress == undefined) {
                        if ($scope.shoppingCart.customer != null && $scope.shoppingCart.customer.customerAddresses.length > 0) {
                            $scope.shoppingCart.shippingAddress = $scope.shoppingCart.customer.customerAddresses[0];
                        }
                        else {
                            $scope.shoppingCart.shippingAddress = angular.copy(address);
                        }
                        $scope.shoppingCart.shippingAddress.addressType = commonFactory.getAddressTypeByName("Shipping");
                    }

                    if ($scope.shoppingCart.poNumber == undefined) {
                        $scope.shoppingCart.poNumber = null;
                    }
                    if ($scope.shoppingCart.notes == undefined) {
                        $scope.shoppingCart.notes = null;
                    }

                    if ($scope.shoppingCart.contactPhone == undefined) {
                        $scope.shoppingCart.contactPhone = null;
                    }

                    $scope.$on('$viewContentLoaded', function () {
                        $rootScope.setToolbarTemplate('shopping-cart-tb')
                    });

                    $rootScope.newCustomer = function () {
                        $state.go('app.crm.newcustomer', {source: 'shopping.cart'});
                    };

                    $rootScope.createAnother = false;

                    function updateCartAddress() {
                        if ($scope.shoppingCart.customer != null && $scope.shoppingCart.customer != undefined) {
                            var addressSet = false;
                            var addresses = $scope.shoppingCart.customer.customerAddresses;
                            if (addresses != null && addresses != undefined && addresses.length > 0) {
                                angular.forEach(addresses, function (address) {
                                    if (address.addressType == "Shipping") {
                                        $scope.shoppingCart.shippingAddress = address;
                                        addressSet = true;
                                    }
                                    else if (address.addressType == "Billing") {
                                        $scope.shoppingCart.billingAddress = address;
                                    }
                                    else if (address.addressType == "Office") {
                                        $scope.shoppingCart.billingAddress = address;
                                        addressSet = true;
                                    }
                                });
                            }

                            if (addressSet == true && $scope.shoppingCart.shippingAddress == null) {
                                $scope.shoppingCart.shippingAddress = $scope.shoppingCart.billingAddress;
                            }
                        }
                    }

                    $scope.getProductBySku = function (item) {
                        if (item.product.sku != null && item.product.sku != undefined && item.product.sku != "") {

                            if (isItemInCart(item) == true) {
                                item.error = true;
                                item.errorMessage = "Item already in the list";
                            }
                            else {
                                productFactory.getProductBySku(item.product.sku).then(
                                    function (data) {
                                        if (data != "") {
                                            item.product.id = data.id;
                                            item.product.name = data.name;
                                            item.unitPrice = data.unitPrice;
                                            item.disabled = false;
                                            item.error = false;
                                        }
                                        else {
                                            item.error = true;
                                            item.errorMessage = "Item doesn't exist";
                                        }
                                    }
                                )
                            }
                        }
                        else {

                        }
                    };

                    $scope.addNewRowToOrderForm = function (row) {
                        var index = $scope.shoppingCart.items.indexOf(row);
                        if (index == $scope.shoppingCart.items.length - 1 || row == null) {
                            var item = {
                                product: {
                                    sku: null,
                                    name: null
                                },
                                unitPrice: null,
                                quantity: null,
                                serialNumber: $scope.shoppingCart.items.length + 1
                            };
                            $scope.shoppingCart.items.push(item);
                        }
                    };


                    $scope.showQuickOrderForm = function () {
                        $rootScope.closeNotification();

                        $scope.quickOrderForm = !$scope.quickOrderForm;
                        if ($scope.quickOrderForm == true) {
                            $scope.addNewRowToOrderForm(null);
                        }
                        else {
                            var items = [];
                            var proceed = true;
                            angular.forEach($scope.shoppingCart.items, function (item) {
                                if (item.product.sku != null) {
                                    if (item.product.name != null && item.product.name != "") {
                                        items.push(item)
                                    }
                                    else {
                                        proceed = false;
                                    }
                                }
                            });
                            if (proceed == true) {
                                $scope.shoppingCart.items = items;
                            }
                            else {
                                $rootScope.showErrorMessage("One of the items in the order does not exist");
                                $scope.quickOrderForm = !$scope.quickOrderForm;
                            }

                        }
                    };

                    function isItemInCart(row) {
                        var yes = false;
                        angular.forEach($scope.shoppingCart.items, function (item) {
                            if (item != row && item.product.sku == row.product.sku) {
                                yes = true;
                            }
                        });
                        return yes;
                    }


                    $scope.toggleShippingSame = function () {
                        if ($scope.shoppingCart.shippingSameAsBilling == true) {
                            $scope.shoppingCart.shippingAddress = angular.copy($scope.shoppingCart.billingAddress);
                        }
                        else {
                            $scope.shoppingCart.shippingAddress = angular.copy(address);
                        }

                        $scope.shoppingCart.shippingAddress.addressType = commonFactory.getAddressTypeByName("Shipping");
                    };

                    function validateOrderInfo() {
                        var valid = true;
                        /*
                        var deliveryDate = moment($scope.customerOrder.deliveryDate, 'DD/MM/YYYY');
                        var today = moment(new Date());
                        var todayStr = today.format('DD/MM/YYYY');
                        today = moment(todayStr, 'DD/MM/YYYY');
                        var val = deliveryDate.isSameOrAfter(today);
                        */

                        if ($scope.customerOrder.customer == null) {
                            $rootScope.showErrorMessage("Select a customer");
                            valid = false;
                        }/*
                        else if ($scope.customerOrder.deliveryDate != null &&
                            $scope.customerOrder.deliveryDate != undefined &&
                            val == false) {
                            $rootScope.showErrorMessage("Delivery date cannot be before today's date");
                            valid = false;
                        }*/
                        else if ($scope.customerOrder.billingAddress.addressText == null ||
                            $scope.customerOrder.billingAddress.addressText == "") {
                            $rootScope.showErrorMessage("Specify billing address");
                            valid = false;
                        }
                        else if ($scope.customerOrder.billingAddress.city == null ||
                            $scope.customerOrder.billingAddress.city == "") {
                            $rootScope.showErrorMessage("Specify city for billing address");
                            valid = false;
                        }
                        else if ($scope.customerOrder.billingAddress.state == null) {
                            $rootScope.showErrorMessage("Specify state for billing address");
                            valid = false;
                        }
                        else if ($scope.customerOrder.details.length == 0) {
                            $rootScope.showErrorMessage("There are no items in the shopping cart");
                            valid = false;
                        }
                        else if ($scope.customerOrder.details.length == 0) {
                            $rootScope.showErrorMessage("There are no items in the shopping cart");
                            valid = false;
                        }
                        else {
                            var validQuantity = true;
                            angular.forEach($scope.customerOrder.details, function (item) {
                                if (item.quantity == undefined ||
                                    isNaN(item.quantity) ||
                                    item.quantity <= 0) {
                                    validQuantity = false;
                                }
                                else if (!isNaN(item.quantity)) {
                                    var s = "" + item.quantity;
                                    if (s.indexOf('.') != -1) {
                                        validQuantity = false;
                                    }
                                }
                            });

                            if (validQuantity == false) {
                                $rootScope.showErrorMessage("Quantity has to be a positive whole number");
                            }

                            valid = validQuantity;
                        }

                        return valid;
                    }

                    $scope.buyProducts = function () {
                        $state.go('app.prod.products', {mode: 'buy'});
                    };

                    $rootScope.showOrders = function () {
                        $state.go('app.crm.orders.all');
                    };

                    $scope.clearCart = function () {
                        $app.shoppingCart.items = [];
                    };

                    $rootScope.cancelShoppingCart = function () {
                        $app.shoppingCart = null;
                        window.history.back();
                    };

                    $rootScope.createAnotherOrder = function () {
                        if ($app.shoppingCart == null) {
                            hideNotification();

                            $scope.createAnother = false;
                            $scope.customerOrder = null;
                            $rootScope.orderCreated = false;
                            $scope.hideSubmitButton = false;

                            $app.shoppingCart = {
                                items: []
                            };

                            $scope.shoppingCart = $app.shoppingCart;
                        }
                    };

                    $rootScope.performCreateOrder = function () {
                        initCustomerOrder();

                        if (validateOrderInfo()) {
                            $rootScope.creatingOrder = true;

                            orderFactory.createOrder($scope.customerOrder).then(
                                function (data) {
                                    $scope.customerOrder = data;
                                    $rootScope.orderCreated = true;
                                    var msg = "Order {0} created successfully!".format(data.orderNumber);
                                    $app.shoppingCart = null;
                                    $scope.hideSubmitButton = true;
                                    $scope.createAnother = true;
                                    $scope.shoppingCart.items = data.details;

                                    $rootScope.$broadcast('app.updateNotification');
                                    $rootScope.showSuccessMessage(msg);

                                    $rootScope.creatingOrder = false;
                                },
                                function (error) {
                                    console.error(error);
                                    $rootScope.creatingOrder = false;
                                }
                            );
                            if ($scope.quickOrderForm == true) {
                                $scope.showQuickOrderForm();
                            }
                        }
                    };

                    $scope.getSubTotal = function () {
                        if ($scope.shoppingCart != null) {
                            var subTotal = 0;
                            angular.forEach($scope.shoppingCart.items, function (item) {
                                subTotal = subTotal + (item.quantity * item.unitPrice);
                            });

                            return subTotal;
                        }
                        else {
                            return 0;
                        }
                    };

                    $scope.getVat = function () {
                        var sub = $scope.getSubTotal();
                        return Math.round(sub * 0.145);
                    };

                    $scope.getTotal = function () {
                        return $scope.getSubTotal() + $scope.getVat();
                    };

                    $scope.removeItem = function (item) {

                        if (item.rowId == null || item.rowId == undefined) {
                            $rootScope.showSuccessMessage("Item has been removed");
                            var index = $rootScope.shoppingCart.items.indexOf(item);
                            $rootScope.shoppingCart.items.splice(index, 1);

                        } else {
                            orderFactory.deleteOrderItem(item.rowId).then(
                                function (data) {
                                    $rootScope.showSuccessMessage("Item has been removed");
                                    var index = $rootScope.shoppingCart.items.indexOf(item);
                                    $rootScope.shoppingCart.items.splice(index, 1);
                                    $scope.customerOrder.orderTotal -= (item.unitPrice * item.quantity);
                                    $rootScope.orderUpdated = true;
                                }
                            )
                        }
                    };

                    $scope.updateOrder = function () {
                        $scope.customerOrder.details = $rootScope.shoppingCart.items;
                        orderFactory.updateOrder($scope.customerOrder).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Order updated successfully!");
                                //$scope.editMode = false;
                            },
                            function (error) {

                            }
                        )
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
                                $scope.shoppingCart.customer = selectedCustomer;
                                $scope.selectText = "Change";
                                if ($scope.shoppingCart.customer.customerAddresses.length > 0) {
                                    $scope.shoppingCart.billingAddress = $scope.shoppingCart.customer.customerAddresses[0];
                                    $scope.shoppingCart.billingAddress.id = null;

                                    $scope.shoppingCart.shippingAddress = $scope.shoppingCart.customer.customerAddresses[0];
                                    $scope.shoppingCart.shippingAddress.id = null;
                                }
                            }
                        );
                    };

                    function initCustomerOrder() {
                        var items = [];
                        var total = 0;

                        angular.forEach($scope.shoppingCart.items, function (item) {
                            var row = {};
                            row.product = item.product;
                            row.quantity = item.quantity;
                            row.unitPrice = item.unitPrice;
                            row.itemTotal = (item.quantity * item.unitPrice);
                            total += row.itemTotal;

                            items.push(row);
                        });

                        if ($scope.customerOrder == null) {
                            $scope.customerOrder = {};
                        }

                        var type = $scope.shoppingCart.orderType;
                        if (type != null && type != undefined &&
                            (type == 'PRODUCT' || type == 'SAMPLE')) {
                            $scope.customerOrder.orderType = type;
                        }
                        else {
                            $scope.customerOrder.orderType = "PRODUCT";
                        }

                        $scope.customerOrder.shipTo = $scope.shoppingCart.shipTo;
                        $scope.customerOrder.poNumber = $scope.shoppingCart.poNumber;
                        $scope.customerOrder.deliveryDate = $scope.shoppingCart.deliveryDate;
                        $scope.customerOrder.notes = $scope.shoppingCart.notes;
                        $scope.customerOrder.contactPhone = $scope.shoppingCart.contactPhone;
                        $scope.customerOrder.billingAddress = $scope.shoppingCart.billingAddress;

                        if ($scope.shoppingCart.shippingSameAsBilling == true) {
                            $scope.customerOrder.shippingAddress = angular.copy($scope.shoppingCart.billingAddress);
                            $scope.customerOrder.shippingAddress.addressType = commonFactory.getAddressTypeByName("Shipping");
                        }
                        else {
                            $scope.customerOrder.shippingAddress = $scope.shoppingCart.shippingAddress;
                        }

                        $scope.customerOrder.customer = $scope.shoppingCart.customer;
                        $scope.customerOrder.orderTotal = total;
                        $scope.customerOrder.netTotal = total;
                        $scope.customerOrder.details = items;

                        for (var i = 0; i < $scope.customerOrder.details.length; i++) {
                            $scope.customerOrder.details[i].serialNumber = i + 1;
                        }
                    }

                    (function () {
                        if ($rootScope.lastNewCustomer != null) {
                            $scope.shoppingCart.customer = angular.copy($rootScope.lastNewCustomer);
                            $rootScope.lastNewCustomer = null;
                        }
                    })();
                }
            ]
        );
    }
);