define(['app/app.modules',
        'app/shared/directives/authorizationDirective',
        'app/components/crm/customer/customerFactory',
        'app/components/crm/order/orderFactory',
        'app/components/prod/product/productFactory',
        'app/components/crm/order/shipment/shipmentController',
        'app/components/crm/order/shipment/shipmentFactory',
        'app/components/hrm/employee/dialog/employeeSelectionController'
    ],
    function ($app) {
        $app.controller('OrderDetailsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$location', '$state', '$stateParams', '$cookies', '$modal',
                'commonFactory', 'orderFactory', 'productFactory', 'shipmentFactory', 'uiGmapGoogleMapApi',

                function ($scope, $rootScope, $timeout, $interval, $location, $state, $stateParams, $cookies, $modal,
                          commonFactory, orderFactory, productFactory, shipmentFactory, GoogleMapApi) {

                    $rootScope.iconClass = "fa flaticon-chart44";
                    $rootScope.viewTitle = "Order Details";
                    $rootScope.approvingOrder = false;
                    $scope.Items = null;

                    $rootScope.authorizationFactory = $app.authorizationFactory;

                    $scope.states = commonFactory.getCountryAndStatesMapByCountry("India").states;
                    $rootScope.editOrder = false;
                    $scope.showNewShipment = false;
                    $scope.lastSelectedShipment = null;
                    $scope.shipmentDetailsRow = {
                        showDetails: false
                    };

                    $scope.editingShipment = null;
                    $scope.pendingItems =[];


                    $scope.templates = {
                        newShipment: "app/components/crm/order/shipment/shipmentView.jsp"
                    };

                    $scope.editMode = false;

                    $rootScope.order = null;
                    $scope.orderHistory = [];

                    $scope.loading = true;
                    $scope.statusDates = {
                        created: "",
                        approved: "",
                        processed: "",
                        partiallyshipped: "",
                        shipped: ""
                    };

                    $scope.orderShipments = [];

                    $scope.map = {
                        show: true,
                        control: {},
                        version: "unknown",
                        showTraffic: true,
                        showBicycling: false,
                        showWeather: false,
                        showHeat: false,
                        center: {
                            latitude: 17.8760325,
                            longitude: 79.2727298
                        },
                        options: {
                            streetViewControl: false,
                            panControl: false,
                            maxZoom: 20,
                            minZoom: 3,
                            mapTypeControl: false
                        },
                        zoom: 6,
                        dragging: false,
                        bounds: {},
                        events: {}
                    };

                    $scope.marker = {
                        id: 1,
                        showWindow: false
                    };

                    $scope.hasAnyPartialShipments = false;


                    $scope.shippers = [];

                    GoogleMapApi.then(function (maps) {
                        $scope.api = maps;
                    });

                    $scope.$on('$viewContentLoaded', function () {
                        $rootScope.setToolbarTemplate('order-deatils-tb');
                    });

                    $scope.sayHello = function () {
                        console.log("Hello Cassini!");
                    };

                    $rootScope.newShipment = function () {
                        orderFactory.updateOrder($scope.order).then(
                            function(data){

                            }
                        )
                        $scope.showNewShipment = true;
                    };

                    $scope.closeNewShipment = function () {
                        $scope.showNewShipment = false;
                        $rootScope.viewTitle = "Order Details (" + $rootScope.order.orderNumber + ")";
                    };

                    $rootScope.showOrders = function () {
                        $state.go('app.crm.orders.all');
                    };

                    $rootScope.shipOrder = function () {
                        $state.go('app.crm.orders.dispatch', {orderId: $scope.order.id});
                    };

                    $scope.modifyOrder = function () {
                        $scope.editMode = true;
                    };

                    $scope.cancelModifyOrder = function () {
                        $scope.editMode = false;
                    };

                    $scope.isRemove = function (item) {

                        if ((item.quantity - item.quantityProcessed) > 0)
                            return true;
                        else
                            return false;

                    };

                    $scope.updateOrder = function () {
                        orderFactory.updateOrder($rootScope.order).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Order updated successfully!");
                                $scope.editMode = false;
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error);
                            }
                        )
                    };

                    function validateUpdates() {
                        var valid = false;

                        return valid;
                    }

                    $scope.quantityChanged = function () {
                        var total = 0;
                        angular.forEach($rootScope.order.details, function (item) {
                            total = total + (item.quantity * item.product.unitPrice);
                        });

                        $rootScope.order.orderTotal = total;
                    };

                    $scope.removeItem = function (item) {
                        orderFactory.deleteOrderItem(item.rowId).then(
                            function (data) {
                                $scope.order.orderTotal -= (item.unitPrice * item.quantity);
                                var index = $rootScope.order.details.indexOf(item);
                                $rootScope.order.details.splice(index, 1);
                                $rootScope.showSuccessMessage("Item has been removed");
                                     orderFactory.updateOrder($scope.order).then(
                                         function (data) {
                                             $scope.Items = data;
                                             angular.forEach($scope.Items.details, function (Obj) {
                                                 if (Obj.quantityShipped == null || Obj.quantityShipped == undefined ||
                                                     (Obj.quantity - Obj.quantityShipped) != 0) {
                                                     $scope.pendingItems.push(Obj);
                                                 }
                                             });
                                         });
                                         if ($rootScope.order.status == 'PARTIALLYSHIPPED' && $scope.pendingItems.length == 0) {
                                              $scope.order.status = "SHIPPED";
                                              orderFactory.updateOrder($scope.order).then(
                                                  function (data) {
                                                    $rootScope.showSuccessMessage("Status Changed To Shipped")
                                                  })
                                              }
                                       });
                                   };

                    /*function recalculateOrderTotal(){ }*/
                    function quantityProcessed(it) {
                        var quantity = 0;
                        angular.forEach($rootScope.order.shipments, function (shipment) {
                            if (shipment.status != 'CANCELLED') {


                                var items = shipment.details;
                                angular.forEach(items, function (item) {
                                    if (it.product.id == item.product.id) {
                                        quantity = quantity + item.quantityShipped;
                                    }
                                });
                            }
                        });

                        return quantity;
                    }

                    $scope.updateQuantityProcessed = function () {
                        angular.forEach($rootScope.order.details, function (orderItem) {
                            orderItem.quantityProcessed = quantityProcessed(orderItem);
                        });
                    };

                    $rootScope.isEntireOrderProcessed = function () {
                        var processed = true;

                        if ($rootScope.order.shipments.length == 0) {
                            processed = false;
                        }

                        if ($rootScope.order.shipments != undefined && $rootScope.order.shipments.length > 0) {
                            var items = $rootScope.order.details;
                            angular.forEach(items, function (item) {
                                var processedQuantity = quantityProcessed(item);
                                if (item.quantity != processedQuantity) {
                                    processed = false;
                                }
                            });
                        }

                        if (processed == false && $rootScope.order.status != "SHIPPED" && hasAnyPendingShipments()) {
                            $rootScope.showWarningMessage("This order is processed completely. But there are pending shipments. " +
                                "Process these shipments to complete the order.")
                        }
                        return processed;
                    };


                    function hasAnyPendingShipments() {
                        var pending = false;
                        angular.forEach($rootScope.order.shipments, function (shipment) {
                            if (shipment.status == "PENDING") {
                                pending == true;
                            }
                        });
                        return pending;
                    }

                    $scope.processOrder = function () {
                        var productIds = "";
                        for (var i = 0; i < $rootScope.order.details.length; i++) {
                            productIds = productIds + $rootScope.order.details[i].product.id;

                            if (i != $rootScope.order.details.length - 1) {
                                productIds = productIds + ",";
                            }
                        }

                        productFactory.getProductsInventory(productIds).then(
                            function (data) {
                                var proceed = checkInventory(data);
                                if (!proceed) {
                                    $rootScope.showErrorMessage("Inventory on some of the items in this order is insufficient!")
                                }
                                else {
                                    orderFactory.processOrder($scope.order.id).then(
                                        function (data) {
                                            $rootScope.$broadcast('app.updateNotification');
                                            $rootScope.order = data;
                                            $scope.loadOrderHistory();
                                            $rootScope.showSuccessMessage("Order has been processed");
                                        },
                                        function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                            }
                        );
                    };

                    $scope.showCustomerOnMap = function () {
                        if ($rootScope.order.customer.geoLocation != null) {
                            $scope.marker.latitude = $rootScope.order.customer.geoLocation.latitude;
                            $scope.marker.longitude = $rootScope.order.customer.geoLocation.longitude;

                            var bounds = new $scope.api.LatLngBounds();
                            var latlang = new $scope.api.LatLng($scope.marker.latitude, $scope.marker.longitude);
                            bounds.extend(latlang);

                            $scope.map.center = {
                                latitude: bounds.getCenter().lat(),
                                longitude: bounds.getCenter().lng()
                            };
                            var origCenter = {
                                latitude: $scope.map.center.latitude,
                                longitude: $scope.map.center.longitude
                            };
                            $scope.map.zoom = 16;
                            $scope.map.control.refresh(origCenter);
                        }
                    };

                    $rootScope.approveOrder = function () {
                        orderFactory.approveOrder($rootScope.order.id).then(
                            function (data) {
                                $rootScope.order = data;
                                $scope.loadOrderHistory();
                                $rootScope.showSuccessMessage("Order has been approved");
                                $rootScope.$broadcast('app.updateNotification');
                                $rootScope.approvingOrder = true;
                                $scope.loadOrder();
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    };

                    $rootScope.cancelOrder = function () {
                        orderFactory.cancelOrder($rootScope.order.id).then(
                            function (data) {
                                $rootScope.order = data;
                                $scope.loadOrderHistory();
                                $rootScope.showSuccessMessage("Order has been cancelled");
                                $rootScope.$broadcast('app.updateNotification');
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    };

                    function checkInventory(inventoryItems) {
                        var map = new Hashtable();
                        angular.forEach(inventoryItems, function (item) {
                            map.put(item.product.id, item);
                        });

                        var proceed = true;

                        angular.forEach($rootScope.order.details, function (detail) {
                            detail.inventory = null;
                            var inv = map.get(detail.product.id);
                            if (inv != null && inv.inventory < detail.quantity) {
                                proceed = false;
                                detail.inventory = inv;
                            }
                            else {
                                detail.inventory = {inventory: 0}
                            }
                        });

                        return proceed;
                    }

                    $scope.showConsignment = function (consignment) {
                        $state.go('app.crm.consigndetails', {consignmentId: consignment.id});
                    };


                    $scope.getClass = function () {
                        if ($rootScope.order != null) {
                            if ($rootScope.order.status == 'NEW') {
                                return "c1";
                            }
                            else if ($scope.order.status == 'APPROVED') {
                                return "c2";
                            }
                            else if ($scope.order.status == 'PROCESSED') {
                                return "c3";
                            }
                            else if ($scope.order.status == 'PARTIALLYSHIPPED') {
                                return "c5";
                            }
                            else if ($scope.order.status == 'SHIPPED') {
                                return "c4";
                            }
                            else {
                                return "";
                            }
                        }
                    };

                    $scope.isActiveClass = function (s) {
                        if ($rootScope.order.status == s) {
                            return "current-order-status";
                        }
                        else {
                            return "";
                        }
                    };

                    $rootScope.printOrder = function () {
                        var url = "{0}//{1}/api/crm/customerorders/{2}/print".
                            format(window.location.protocol, window.location.host,
                            $rootScope.order.id);
                        var printWindow = window.open(url, '',
                            'scrollbars=yes,menubar=no,width=500, resizable=yes,toolbar=no,location=no,status=no');

                    };

                    $rootScope.starOrder = function () {
                        $rootScope.order.starred = true;
                        $scope.order.starred = true;
                        $rootScope.saveOrder();
                        $rootScope.showSuccessMessage("Order has been bookmarked");
                    };

                    $scope.loadOrder = function () {
                        orderFactory.getOrder($stateParams.orderId).then(
                            function (data) {
                                $rootScope.order = data;
                                $rootScope.viewTitle = "Order Details (" + data.orderNumber + ")";
                                orderFactory.getOrderItems($stateParams.orderId).then(
                                    function (data) {
                                        $scope.order.details = data;
                                        $scope.loading = false;

                                        loadOrderData();
                                    }
                                );
                            }
                        );
                    };

                    $rootScope.cancelOrder = function () {
                        orderFactory.cancelOrder($rootScope.order.id).then(
                            function (data) {
                                $rootScope.order = data;
                                $scope.loadOrderHistory();
                                $rootScope.showSuccessMessage("Order has been cancelled");
                                $rootScope.$broadcast('app.updateNotification');
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    };
                    $rootScope.cancelShipment = function (shipment) {
                        shipmentFactory.cancelShipment(shipment.id).then(
                            function (data) {
                                $rootScope.showErrorMessage("Shipment has been cancelled");
                            }
                        );
                    };

                    function loadOrderData() {
                        $timeout(function () {
                            $scope.showCustomerOnMap();
                        }, 500);
                        $scope.loadOrderShipments();
                        $scope.loadOrderHistory();
                        $scope.loadVerifications();
                        $scope.isEntireOrderProcessed();
                        $scope.updateQuantityProcessed();
                    }

                    $scope.loadVerifications = function () {
                        orderFactory.getVerifications($stateParams.orderId).then(
                            function (data) {
                                $scope.verifications = data;
                            }
                        )
                    };

                    /* $scope.loadShipmentHistory = function() {
                     shipmentFactory.getShipmentHistory($scope.lastSelectedShipment.id).then (
                     function(data) {
                     $scope.orderHistory = data;

                     angular.forEach(data, function(item) {
                     if(item.newStatus == 'NEW') {
                     $scope.statusDates.created = item.modifiedDate;
                     }
                     else if(item.newStatus == 'APPROVED') {
                     $scope.statusDates.approved = item.modifiedDate;
                     }
                     else if(item.newStatus == 'PROCESSED') {
                     $scope.statusDates.processed = item.modifiedDate;
                     }
                     else if(item.newStatus == 'PARTIALLYSHIPPED') {
                     $scope.statusDates.partiallyshipped = item.modifiedDate;
                     $scope.hasAnyPartialShipments = true;
                     }
                     else if(item.newStatus == 'SHIPPED') {
                     $scope.statusDates.shipped = item.modifiedDate;
                     }
                     });
                     }
                     )
                     };*/
                    $scope.loadOrderHistory = function () {
                        orderFactory.getOrderHistory($stateParams.orderId).then(
                            function (data) {
                                $scope.orderHistory = data;

                                angular.forEach(data, function (item) {
                                    if (item.newStatus == 'NEW') {
                                        $scope.statusDates.created = item.modifiedDate;
                                    }
                                    else if (item.newStatus == 'APPROVED') {
                                        $scope.statusDates.approved = item.modifiedDate;
                                    }
                                    else if (item.newStatus == 'PROCESSED') {
                                        $scope.statusDates.processed = item.modifiedDate;
                                    }
                                    else if (item.newStatus == 'PARTIALLYSHIPPED') {
                                        $scope.statusDates.partiallyshipped = item.modifiedDate;
                                        $scope.hasAnyPartialShipments = true;
                                    }
                                    else if (item.newStatus == 'SHIPPED') {
                                        $scope.statusDates.shipped = item.modifiedDate;
                                    }
                                });
                            }
                        )
                    };
                    $scope.toggleShipmentDetails = function (shipment) {
                        if ($scope.lastSelectedShipment != shipment) {
                            if ($scope.lastSelectedShipment != null) {
                                $scope.lastSelectedShipment.detailsOpen = false;
                            }
                            shipment.detailsOpen = true;
                        }

                        var index = $scope.orderShipments.indexOf($scope.shipmentDetailsRow);
                        if (index != -1) {
                            $scope.shipmentDetailsRow.showDetails = false;
                            $scope.orderShipments.splice(index, 1);
                        }
                        if ($scope.lastSelectedShipment == null || $scope.lastSelectedShipment != shipment ||
                            $scope.lastSelectedShipment.detailsOpen == false) {
                            $scope.shipmentDetailsRow.showDetails = true;
                            index = $scope.orderShipments.indexOf(shipment);
                            $scope.orderShipments.splice(index + 1, 0, $scope.shipmentDetailsRow);
                        }

                        if ($scope.lastSelectedShipment != null && $scope.lastSelectedShipment != shipment) {
                            $scope.lastSelectedShipment = false;
                        }
                        else if ($scope.lastSelectedShipment != null) {
                            $scope.lastSelectedShipment.detailsOpen = !$scope.lastSelectedShipment.detailsOpen;
                        }

                        $scope.lastSelectedShipment = shipment;
                    };

                    $scope.loadOrderShipments = function () {
                        shipmentFactory.getOrderShipments($rootScope.order.id).then(
                            function (data) {
                                angular.forEach(data, function (item) {
                                    item.showDetails = false;
                                    item.detailsOpen = false;
                                    item.editMode = false;
                                });
                                $scope.orderShipments = data;

                                $scope.loadConsignments();
                            }
                        )
                    };

                    $scope.loadConsignments = function () {
                        if ($scope.orderShipments.length > 0) {
                            var shipIds = "";
                            for (var i = 0; i < $scope.orderShipments.length; i++) {
                                shipIds = shipIds + $scope.orderShipments[i].id;

                                if (i != $scope.orderShipments.length - 1) {
                                    shipIds = shipIds + ",";
                                }
                            }

                            shipmentFactory.getConsignmentsForShipments(shipIds).then(
                                function (data) {
                                    setConsignmentsForShipments(data);
                                }
                            )
                        }
                    };

                    $rootScope.beginOrderProcessing = function () {
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/hrm/employee/dialog/employeeSelectionDialog.jsp',
                            controller: 'EmployeeSelectionController',
                            size: 'lg',
                            resolve: {
                                selectedEmployee: function () {
                                    return null;
                                },
                                "dialogTitle": "Select employee for processing"
                            }
                        });

                        modalInstance.result.then(
                            function (selectedEmployee) {
                                var verification = {
                                    verifiedBy: selectedEmployee
                                };

                                orderFactory.verifyOrder($rootScope.order.id, verification).then(
                                    function (data) {
                                        $scope.loadOrder();
                                        $scope.printOrder();
                                    }
                                )
                            }
                        );
                    };

                    function setConsignmentsForShipments(consignments) {
                        var map = new Hashtable();
                        angular.forEach(consignments, function (consignment) {
                            angular.forEach(consignment.shipments, function (shipment) {
                                map.put(shipment.id, consignment);
                            })
                        });

                        angular.forEach($scope.orderShipments, function (shipment) {
                            var consignment = map.get(shipment.id);
                            if (consignment != null) {
                                shipment.consignment = consignment;
                            }
                        });
                    }

                    $scope.shipShipment = function (shipment, shipper) {
                        if (shipper != null) {
                            shipment.shipper = shipper;
                        }

                        $rootScope.closeNotification();

                        shipmentFactory.processShipment($rootScope.order.id, shipment).then(
                            function (data) {
                                shipment = data;
                                $scope.loadOrder();
                                $rootScope.showSuccessMessage("Shipment has been shipped successfully!");
                            },
                            function (error) {
                                shipment.shipper = null;
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    };

                    function loadShippers() {
                        shipmentFactory.getShippers().then(
                            function (data) {
                                $scope.shippers = data;
                            }
                        );
                    }

                    $rootScope.saveOrder = function () {
                        if ($scope.order.billingAddress != null && $scope.order.shippingAddress == null) {
                            $scope.order.shippingAddress = angular.copy($scope.order.billingAddress);
                        }
                        if ($scope.order.shippingAddress != null && $scope.order.billingAddress == null) {
                            $scope.order.billingAddress = angular.copy($scope.order.shippingAddress);
                        }

                        if ($scope.order.shippingAddress != null) {
                            $scope.order.shippingAddress.addressType = {
                                typeId: 3
                            };
                            $scope.order.shippingAddress.country = commonFactory.getCountryAndStatesMapByCountry("India").country;

                        }
                        if ($scope.order.billingAddress != null) {
                            $scope.order.billingAddress.addressType = {
                                typeId: 4
                            };
                            $scope.order.billingAddress.country = commonFactory.getCountryAndStatesMapByCountry("India").country;
                        }

                        for (var i = 0; i < $scope.order.details.length; i++) {
                            $scope.order.details[i].serialNumber = i + 1;
                        }


                        orderFactory.updateOrder($scope.order).then(
                            function (data) {
                                $scope.order.details = data.details;
                                $rootScope.editOrder = false;
                            }
                        )
                    };

                    $rootScope.setEditOrder = function () {
                        $rootScope.editOrder = true;
                        $scope.$apply();
                    };


                    $scope.addItems = function addItems() {
                        $app.shoppingCart = $scope.order;
                        $app.shoppingCart.mode = 'editOrder';
                        $app.shoppingCart.items = $scope.order.details;
                        $state.go('app.prod.products', {mode: 'buy'});
                    };

                    function onPageLoad() {

                        if ($app.shoppingCart != null &&
                            $app.shoppingCart.orderNumber != null &&
                            $app.shoppingCart.orderNumber != undefined) {
                            $rootScope.order = angular.copy($app.shoppingCart);

                            var orderTotal = 0;
                            angular.forEach($rootScope.order.items, function (item) {
                                var itemTotal = item.unitPrice * item.quantity;
                                orderTotal += itemTotal;
                            });
                            $rootScope.order.orderTotal = orderTotal;
                            $app.shoppingCart = null;
                            $scope.loading = false;
                            $rootScope.editOrder = true;
                            loadOrderData();
                        }
                        else {
                            $scope.loadOrder();
                        }
                        loadShippers();
                    }

                    $rootScope.holdOrder = function () {
                        $rootScope.order.onhold = true;
                        $scope.saveOrder();
                    };

                    $rootScope.removeHold = function () {
                        $rootScope.order.onhold = false;
                        $scope.saveOrder();
                    };


                    $scope.editShipment = function () {
                        $scope.editingShipment = angular.copy($scope.lastSelectedShipment);
                        $scope.editingShipment.order = {
                            id: $rootScope.order.id
                        };

                        var map = new Hashtable();
                        angular.forEach($scope.editingShipment.details, function (item) {
                            map.put(item.product.id, item);
                        });

                        angular.forEach($rootScope.order.details, function (item) {
                            if (map.get(item.product.id) == null) {
                                var sItem = {
                                    quantityShipped: 0,
                                    product: item.product,
                                    unitPrice: item.unitPrice,
                                    itemTotal: 0.0
                                };
                                $scope.editingShipment.details.push(sItem);
                            }
                        });
                        $scope.lastSelectedShipment.editMode = true;
                    };

                    $scope.updateShipment = function () {
                        if (validateShipment($scope.editingShipment) == true) {
                            $scope.calculateInvoiceAmount($scope.editingShipment);
                            shipmentFactory.updateShipment($scope.editingShipment).then(
                                function (data) {
                                    $scope.lastSelectedShipment.details = angular.copy(data.details);
                                    $scope.lastSelectedShipment.editMode = false;
                                    updateShipmentDetails($scope.editingShipment);
                                    $scope.updateQuantityProcessed();
                                }
                            )
                        }
                    };

                    function validateShipment(shipment) {
                        var valid = true;

                        if (shipment.shippingAddress == null || shipment.shippingAddress == undefined) {
                            valid = false;
                            $rootScope.showErrorMessage("Shipping address cannot be empty");
                        }
                        else if (shipment.shippingAddress.addressText == null ||
                            shipment.shippingAddress.addressText == null ||
                            shipment.shippingAddress.addressText == "") {
                            valid = false;
                            $rootScope.showErrorMessage("Shipping address cannot be empty");
                        }
                        else if (shipment.shippingAddress.city == null ||
                            shipment.shippingAddress.city == null ||
                            shipment.shippingAddress.city == "") {
                            valid = false;
                            $rootScope.showErrorMessage("Shipping address city cannot be empty");
                        }
                        else if (shipment.shippingAddress.state == null ||
                            shipment.shippingAddress.state == null ||
                            shipment.shippingAddress.state == undefined) {
                            valid = false;
                            $rootScope.showErrorMessage("Shipping address state cannot be empty");
                        }
                        else if (shipment.shippingAddress.country == null ||
                            shipment.shippingAddress.country == null ||
                            shipment.shippingAddress.country == undefined) {
                            valid = false;
                            $rootScope.showErrorMessage("Shipping address country cannot be empty");
                        }

                        return valid;
                    }

                    $scope.calculateInvoiceAmount = function (shipment) {
                        var invoiceAmount = 0.0;

                        angular.forEach(shipment.details, function (item) {
                            var quantityShipped = item.quantityShipped;
                            var unitPrice = item.unitPrice;

                            item.itemTotal = quantityShipped * unitPrice;
                            invoiceAmount = invoiceAmount + item.itemTotal;
                        });

                        if (shipment.discount != null && !isNaN(shipment.discount)) {
                            invoiceAmount = invoiceAmount - (invoiceAmount * shipment.discount / 100);
                        }

                        if (shipment.specialDiscount != null && !isNaN(shipment.specialDiscount)) {
                            invoiceAmount = invoiceAmount - (invoiceAmount * shipment.specialDiscount / 100);
                        }

                        shipment.invoiceAmount = invoiceAmount;

                        return invoiceAmount;
                    };

                    $scope.removeShipmentItem = function (shipmentItem) {
                        shipmentFactory.deleteShipmentItem($scope.lastSelectedShipment.id, shipmentItem.id).then(
                            function () {
                                var index = $scope.lastSelectedShipment.details.indexOf(shipmentItem);
                                $scope.lastSelectedShipment.details.splice(index, 1);
                                $scope.calculateInvoiceAmount($scope.lastSelectedShipment);
                                shipmentFactory.updateShipment($scope.lastSelectedShipment).then(
                                    function (data) {
                                        updateShipmentDetails($scope.lastSelectedShipment);
                                        $scope.updateQuantityProcessed();
                                    }
                                )
                            }
                        )
                    };

                    function updateShipmentDetails(shipment) {
                        angular.forEach($rootScope.order.shipments, function (ship) {
                            if (shipment.id == ship.id) {
                                ship.details = shipment.details;
                            }
                        });
                    }

                    (function () {
                        onPageLoad();
                    })();
                }
            ]
        );
    }
);