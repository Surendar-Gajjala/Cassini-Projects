define(['app/app.modules',
        'app/components/crm/customer/customerFactory',
        'app/components/crm/order/orderFactory',
        'app/components/crm/order/shipment/shipmentFactory'
    ],
    function ($app) {
        $app.controller('CustomerOrdersController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams', '$cookies',
                'customerFactory', 'orderFactory', 'shipmentFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies,
                          customerFactory, orderFactory, shipmentFactory) {

                    $scope.$parent.ordersScope = $scope;

                    $scope.loading = true;
                    $scope.lastSelectedOrder = null;
                    $scope.consignments = null;
                    $scope.lrNumber = null;

                    $scope.sampleOrdersAmount = 0;
                    $scope.productOrdersAmount = 0;

                    $scope.loadingLrNumbers = true;

                    $scope.pageable = {
                        page: 1,
                        size: 15,
                        sort: {
                            label: "modifiedDate",
                            field: "modifiedDate",
                            order: "desc"
                        }
                    };


                    $scope.pagedResults = {
                        content: [],
                        last: true,
                        totalPages: 0,
                        totalElements: 0,
                        size: $scope.pageable.size,
                        number: 0,
                        sort: null,
                        first: true,
                        numberOfElements: 0
                    };

                    $scope.orders = angular.copy($scope.pagedResults);

                    $scope.emptyFilters = {
                        poNumber: null,
                        orderType: null,
                        status: null,
                        deliveryDate: {
                            startDate: null,
                            endDate: null
                        },
                        orderedDate: {
                            startDate: null,
                            endDate: null
                        }
                    };

                    $scope.filters = angular.copy($scope.emptyFilters);


                    $scope.toggleDetails = function (order) {
                        if ($scope.lastSelectedOrder != null && $scope.lastSelectedOrder != order) {
                            $scope.lastSelectedOrder.showDetails = false;
                        }
                        order.showDetails = !order.showDetails;
                        $scope.lastSelectedOrder = order;

                        if (order.showDetails == true) {
                            loadOrderDetails(order);
                            loadOrderHistory(order);
                            loadOrderShipments(order);
                        }
                    };

                    function updateQuantityProcessed(order){
                        angular.forEach(order.details, function (orderItem) {
                            orderItem.quantityProcessed = quantityProcessed(orderItem, order);
                        });
                    }

                    function quantityProcessed(it, order) {
                        var quantity = 0;
                        angular.forEach(order.orderShipments, function (shipment) {
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

                    $scope.nextPage = function() {
                        $scope.pageable.page++;
                        $scope.getCustomerOrders();
                    };

                    $scope.previousPage = function() {
                        $scope.pageable.page--;
                        $scope.getCustomerOrders();
                    };

                    function loadOrderDetails(order) {
                        orderFactory.getOrderItems(order.id).then(
                            function (data) {
                                order.details = data;
                            }
                        )
                    }

                    function loadOrderHistory(order) {
                        orderFactory.getOrderHistory(order.id).then(
                            function (data) {
                                order.orderHistory = data;
                            }
                        )
                    }

                    function loadOrderShipments(order) {
                        shipmentFactory.getOrderShipments(order.id).then(
                            function (data) {
                                order.orderShipments = data;

                                loadConsignments(order);
                            }
                        )
                    }

                    $scope.getCustomerOrders = function () {
                        $scope.filters.cusomterId = $scope.customer.id;
                        if($scope.filters.orderType == 'ALL') {
                            $scope.filters.orderType = null;
                        }
                        if($scope.filters.status == 'ALL') {
                            $scope.filters.status = null;
                        }

                        $scope.loading = true;
                        $scope.orders.content = [];

                        orderFactory.getOrders($scope.filters, $scope.pageable).then(
                            function (data) {
                                $scope.orders = data;
                                angular.forEach($scope.orders.content, function (order) {
                                    var ordercopy = order;
                                    order.lrNumbers = null;
                                    order.showDetails = false;
                                    order.orderHistory = [];
                                })
                                    if($scope.filters.cusomterId != null){
                                        loadTotals();
                                    }
                                loadConsignmentsForOrders();
                                $scope.loading = false;
                            }
                        );
                    };

                    $scope.showOrder = function (order) {
                        $state.go('app.crm.orders.details', {orderId: order.id})
                    };


                    function loadConsignments(order) {
                        if (order.orderShipments.length > 0) {
                            var shipIds = "";
                            for (var i = 0; i < order.orderShipments.length; i++) {
                                shipIds = shipIds + order.orderShipments[i].id;

                                if (i != order.orderShipments.length - 1) {
                                    shipIds = shipIds + ",";
                                }
                            }

                            shipmentFactory.getConsignmentsForShipments(shipIds).then(
                                function (data) {
                                    setConsignmentsForShipments(order, data);
                                    updateQuantityProcessed(order);
                                }
                            )
                        }
                    }

                    function loadConsignmentsForOrders() {
                        var shipIds = "";
                        var map = new Hashtable();
                        angular.forEach($scope.orders.content, function(order) {
                            for (var i = 0; i < order.shipments.length; i++) {
                                if (shipIds != "") {
                                    shipIds = shipIds + ",";
                                }
                                shipIds = shipIds + order.shipments[i].id;

                                map.put(order.shipments[i].id, order.shipments[i]);
                            }
                        });

                        if(shipIds != "") {
                            shipmentFactory.getConsignmentsForShipments(shipIds).then(
                                function (data) {
                                    angular.forEach(data, function(consignment) {
                                        angular.forEach(consignment.shipments, function(shipment){
                                            var found = map.get(shipment.id);

                                            if(found != null) {
                                                found.consignment = consignment;
                                            }
                                        })
                                    });

                                    angular.forEach($scope.orders.content, function(order) {
                                        var lrNumbers = "";
                                        for (var i = 0; i < order.shipments.length; i++) {
                                            var shipment = order.shipments[i];
                                            if(shipment.consignment != null) {
                                                lrNumbers += shipment.consignment.confirmationNumber;
                                            }
                                        }

                                        order.lrNumbers = lrNumbers;
                                    });

                                    $scope.loadingLrNumbers = false;
                                }
                            )
                        }
                    }

                    function getConsignmentsForLRNumbers(){
                        angular.forEach($scope.orders.content, function(order) {
                            shipmentFactory.getConsignmentsByOrderId(order.id).then(
                                function (data) {
                                    var lrNumbers = "";
                                    $scope.consignments = data;
                                    angular.forEach($scope.consignments, function(consignment) {
                                        lrNumbers = consignment.confirmationNumber;
                                        if (lrNumbers.length > 1) {
                                            $scope.lrNumber = lrNumbers + ",";
                                        }
                                    });
                                })
                             });
                          }

                    function setConsignmentsForShipments(order, consignments) {
                        var map = new Hashtable();
                        angular.forEach(consignments, function (consignment) {
                            angular.forEach(consignment.shipments, function (shipment) {
                                map.put(shipment.id, consignment);
                            })
                        });

                        angular.forEach(order.orderShipments, function (shipment) {
                            var consignment = map.get(shipment.id);
                            if (consignment != null) {
                                shipment.consignment = consignment;
                            }
                        });
                    }

                    $scope.$parent.$on("customerLoaded", function () {
                        $scope.getCustomerOrders();
                    });

                    function loadTotals() {
                        var pageable = angular.copy($scope.pageable);
                        pageable.size = 20;
                        orderFactory.getOrders($scope.filters, pageable).then(
                            function (orders) {
                                $scope.sampleOrdersAmount = 0;
                                $scope.productOrdersAmount = 0;
                                angular.forEach(orders.content, function (order) {
                                    if (order.orderType == 'SAMPLE') {
                                        $scope.sampleOrdersAmount += order.orderTotal;
                                    }
                                    else {
                                        angular.forEach(order.shipments, function(shipment) {
                                            $scope.productOrdersAmount += shipment.invoiceAmount;
                                        });
                                    }
                                });
                            }
                        )
                    }

                    (function () {
                        $rootScope.$on('customerLoaded', function() {
                            $scope.getCustomerOrders();
                            //loadTotals();
                        });
                    })();

                }
            ]
        );
    }
);