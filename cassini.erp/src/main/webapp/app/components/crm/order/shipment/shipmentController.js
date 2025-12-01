define(['app/app.modules',
        'app/components/common/commonFactory',
        'app/components/crm/order/shipment/shipmentFactory',
        'app/components/crm/order/orderFactory'
    ],
    function ($app) {
        $app.controller('ShipmentController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'commonFactory', 'shipmentFactory', 'orderFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          commonFactory, shipmentFactory, orderFactory) {
                    $rootScope.iconClass = "fa flaticon-chart44";
                    $rootScope.viewTitle = "Order Shipment";

                    $scope.states = commonFactory.getCountryAndStatesMapByCountry("India").states;

                    $scope.shipment = {
                        invoiceNumber: null,
                        invoiceAmount: null,
                        discount: null,
                        specialDiscount: null,
                        trackingNumber: null,
                        shippingAddress: null,
                        billingAddress: null,
                        details: []
                    };

                    if ($scope.order.shippingAddress != null) {
                        $scope.shipment.shippingAddress = angular.copy($scope.order.shippingAddress);
                        $scope.shipment.shippingAddress.id = null;
                    }
                    if ($scope.order.billingAddress != null) {
                        $scope.shipment.billingAddress = angular.copy($scope.order.billingAddress);
                        $scope.shipment.billingAddress.id = null;
                    }
                    else if ($scope.shipment.billingAddress == null && $scope.order.shippingAddress != null) {
                        $scope.shipment.billingAddress = angular.copy($scope.order.shippingAddress);
                        $scope.shipment.billingAddress.id = null;
                    }

                    $scope.processShipment = function (ship) {
                        if (validateShipment()) {
                            $rootScope.closeNotification();

                            shipmentFactory.getShipmentByInvoiceNumber($scope.shipment.invoiceNumber).then(
                                function (data) {
                                    if (data == null || data == "" || data == undefined) {
                                        $scope.calculateInvoiceAmount();

                                        for (var i = 0; i < $scope.shipment.details.length; i++) {
                                            $scope.shipment.details[i].serialNumber = i + 1;
                                            $scope.order.details[i].boxes = $scope.shipment.details[i].boxes;
                                        }

                                        orderFactory.updateOrder($scope.order).then(
                                            function (data) {

                                                shipmentFactory.processShipment($scope.order.id, ship, $scope.shipment).then(
                                                    function (data) {
                                                        $rootScope.$broadcast('app.updateNotification');
                                                        $scope.loadOrder();
                                                        $scope.closeNewShipment();
                                                        $rootScope.showSuccessMessage("Order has been processed");
                                                    },
                                                    function (error) {
                                                        $rootScope.showErrorMessage(error.message);
                                                    }
                                                )
                                            }
                                        )
                                    }
                                    else {
                                        $rootScope.showErrorMessage("Duplicate invoice number " + $scope.shipment.invoiceNumber);
                                    }
                                }
                            );
                        }
                    };

                    function validateShipment() {
                        var valid = true;

                        if ($scope.shipment.invoiceNumber == null || $scope.shipment.invoiceNumber == "") {
                            valid = false;
                            $rootScope.showErrorMessage("Invoice number cannot be empty");
                        }
                        else {
                            angular.forEach($scope.shipment.details, function (item) {
                                if (item.quantityShipped > item.balanceQuantity) {
                                    valid = false;
                                    $rootScope.showErrorMessage("Shipped quantity cannot be greater than remaining quantity");
                                }
                            });

                            if (valid == true) {
                                var items = [];
                                angular.forEach($scope.shipment.details, function (item) {
                                    if (item.quantityShipped == null || item.quantityShipped == 0) {
                                        items.push(item);
                                    }
                                });

                                if (items.length == $scope.shipment.details.length) {
                                    valid = false;
                                    $rootScope.showErrorMessage("At least one item must be selected for order processing");
                                }
                            }

                            if (valid == true) {
                                angular.forEach($scope.shipment.details, function (item) {
                                    var temp = "" + item.quantityShipped;
                                    temp = temp.trim();
                                    if (temp == "") {
                                        item.quantityShipped = 0;
                                    }

                                    if (isNaN(item.quantityShipped) ||
                                        item.quantityShipped < 0) {
                                        valid = false;
                                    }
                                    else if (!isNaN(item.quantityShipped)) {
                                        var s = "" + item.quantityShipped;
                                        if (s.indexOf('.') != -1) {
                                            valid = false;
                                        }
                                    }
                                });

                                if (valid == false) {
                                    $rootScope.showErrorMessage("Quantity has to be a positive whole number");
                                }
                            }
                        }

                        return valid;
                    }

                    $scope.calculateInvoiceAmount = function () {
                        var invoiceAmount = 0.0;

                        angular.forEach($scope.shipment.details, function (item) {
                            var quantityShipped = item.quantityShipped;
                            var unitPrice = item.unitPrice;

                            item.itemTotal = quantityShipped * unitPrice;
                            invoiceAmount = invoiceAmount + item.itemTotal;
                        });

                        if ($scope.shipment.discount != null && !isNaN($scope.shipment.discount)) {
                            invoiceAmount = invoiceAmount - (invoiceAmount * $scope.shipment.discount / 100);
                        }

                        if ($scope.shipment.specialDiscount != null && !isNaN($scope.shipment.specialDiscount)) {
                            invoiceAmount = invoiceAmount - (invoiceAmount * $scope.shipment.specialDiscount / 100);
                        }

                        $scope.shipment.invoiceAmount = invoiceAmount;

                        return invoiceAmount;
                    };

                    function quantityProcessed(it) {
                        var quantity = 0;
                        angular.forEach($scope.order.shipments, function (shipment) {
                            var items = shipment.details;
                            angular.forEach(items, function (item) {
                                if (it.product.id == item.product.id) {
                                    quantity = quantity + item.quantityShipped;
                                }
                            });
                        });
                        return quantity;
                    }

                    function initShipment() {
                        var details = $scope.order.details;
                        angular.forEach(details, function (item) {
                            var pQty = quantityProcessed(item);
                            var shipmentItem = {
                                serialNumber: item.serialNumber,
                                product: item.product,
                                unitPrice: item.unitPrice,
                                discount: item.discount,
                                itemTotal: item.itemTotal,
                                quantity: item.quantity,
                                boxes: item.boxes,
                                processedQuantity: pQty,
                                balanceQuantity: (item.quantity - pQty),
                                quantityShipped: (item.quantity - pQty)
                            };

                            $scope.shipment.details.push(shipmentItem);
                        })
                    }


                    (function () {
                        $rootScope.viewTitle = "Order Shipment (" + $scope.order.orderNumber + ")";
                        initShipment();
                    })();

                }
            ]
        );
    }
);