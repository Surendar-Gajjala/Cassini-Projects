define(['app/app.modules',
        'app/shared/services/cachingService',
        'app/components/crm/order/orderFactory',
        'app/components/crm/order/shipment/shipmentFactory',
        'app/shared/constants/listValues',
        'app/shared/directives/tableDirectives',
        'app/components/reporting/reportFactory'
    ],
    function ($app) {
        $app.controller('AllOrdersController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$location',
                '$state', '$stateParams', '$cookies', 'cachingService',
                'orderFactory', 'shipmentFactory', 'reportFactory', 'ERPLists',

                function ($scope, $rootScope, $timeout, $interval, $location,
                          $state, $stateParams, $cookies, cachingService,
                          orderFactory, shipmentFactory, reportFactory, ERPLists) {

                    if (!$app.homeLoaded) {
                        console.log("Switching to home");
                        $state.go('app.home');
                        return;
                    }

                    $rootScope.iconClass = "fa flaticon-chart44";
                    $rootScope.viewTitle = "Orders";

                    $scope.orderStatusList = ERPLists.orderStatus;
                    $scope.orderTypes = ERPLists.orderTypes;

                    $scope.dateRangeOptions = {
                        ranges: {
                            'Today': [moment(), moment()],
                            'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                            'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                            'Last 30 Days': [moment().subtract(29, 'days'), moment()],
                            'This Month': [moment().startOf('month'), moment().endOf('month')],
                            'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                            'Year to Date': [
                                moment().startOf('year').startOf('month').startOf('hour').startOf('minute').startOf('second'),
                                moment()
                            ],
                            'Last Year': [
                                moment().subtract(1, 'year').startOf('year'),
                                moment().subtract(1, 'year').endOf('year')
                            ]
                        }
                    };
                    $scope.loading = true;
                    $scope.pageable = {
                        page: 1,
                        size: 10,
                        sort: {
                            label: "modifiedDate",
                            field: "modifiedDate",
                            order: "desc"
                        }
                    };
                    $scope.pagedResults = {
                        content: [],
                        last: false,
                        totalPages: 0,
                        totalElements: 0,
                        size: $scope.pageable.size,
                        number: 0,
                        sort: null,
                        first: false,
                        numberOfElements: 0
                    };

                    $scope.orders = $scope.pagedResults;

                    $scope.orderCounts = {
                        newOrders: {
                            product: 0,
                            sample: 0
                        },
                        approvedOrders: {
                            product: 0,
                            sample: 0
                        },
                        pendingOrders: {
                            product: 0,
                            sample: 0
                        },
                        shippedOrders: {
                            product: 0,
                            sample: 0
                        }
                    };

                    $scope.emptyFilters = {
                        orderType: null,
                        orderNumber: null,
                        orderedDate: {
                            startDate: null,
                            endDate: null
                        },
                        deliveryDate: {
                            startDate: null,
                            endDate: null
                        },
                        customer: null,
                        shipTo: null,
                        region: null,
                        salesRep: null,
                        orderTotal: null,
                        status: null,
                        invoiceNumber: null,
                        trackingNumber: null
                    };
                    $scope.filters = angular.copy($scope.emptyFilters);

                    if ($stateParams.status != null && $stateParams.status != undefined) {
                        $scope.filters.status = $stateParams.status;
                    }

                    $scope.sortColumn = function (label, field) {
                        if ($scope.pageable.sort.label == label) {
                            if ($scope.pageable.sort.order == 'asc') {
                                $scope.pageable.sort.order = 'desc';
                            }
                            else {
                                $scope.pageable.sort.order = 'asc';
                            }
                        }
                        else {
                            $scope.pageable.sort.label = label;
                            $scope.pageable.sort.order = 'asc';
                        }

                        $scope.pageable.sort.field = field;

                        $scope.pageable.page = 1;
                        loadOrders();
                    };

                    $scope.$watch('filters.orderedDate', function (date) {
                        $scope.applyFilters();
                    });

                    $scope.$on('$viewContentLoaded', function () {
                        $rootScope.setToolbarTemplate('orders-view-tb')
                    });

                    $rootScope.showOrderVerifications = function () {
                        $state.go("app.crm.orders.verifications");
                    };


                    $scope.pageChanged = function () {
                        $scope.loading = true;
                        $scope.orders.content = [];
                        loadOrders();
                    };

                    $scope.resetFilters = function () {
                        $scope.filters = angular.copy($scope.emptyFilters);
                        $scope.pageable.page = 1;
                        loadOrders();
                    };

                    $scope.applyFilters = function () {
                        $scope.pageable.page = 1;
                        loadOrders();
                    };

                    $scope.openOrderDetails = function (order) {
                        $state.go('app.crm.orders.details', {orderId: order.id});
                    };

                    $scope.shipOrder = function (order) {
                        $state.go('app.crm.orders.dispatch', {orderId: order.id});
                    };

                    $scope.bookmarkOrder = function (order) {
                        order.starred = true;

                        var savedOrder = angular.copy(order);
                        delete savedOrder["shippingAddress"];
                        delete savedOrder["billingAddress"];


                        orderFactory.updateOrder(savedOrder).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Order bookmarked!");
                            }
                        )
                    };

                    $scope.showConsignment = function (consignment) {
                        //$location.url("/app/crm/consignments?mode=item&formMode=details&consignmentId=" + consignment.id);
                        $state.go('app.crm.consigndetails', {consignmentId: consignment.id});
                    };

                    function setConsignments() {
                        angular.forEach($scope.orders.content, function (order) {
                            order.consignments = [];

                            angular.forEach(order.shipments, function (shipment) {
                                var consignment = shipment.consignment;
                                if (consignment != null && consignment != undefined) {
                                    var s = consignment.number;
                                    if (s != null && s != undefined && order.consignments.indexOf(s) == -1) {
                                        order.consignments.push(consignment);
                                    }
                                }
                            });
                        });
                    }

                    function loadOrders() {
                        if ($scope.filters.orderedDate != null &&
                            $scope.filters.orderedDate.startDate != null) {
                            var d = $scope.filters.orderedDate.startDate;
                            $scope.filters.orderedDate.startDate = moment(d).format('DD/MM/YYYY');
                        }
                        if ($scope.filters.orderedDate != null &&
                            $scope.filters.orderedDate.endDate != null) {
                            var d = $scope.filters.orderedDate.endDate;
                            $scope.filters.orderedDate.endDate = moment(d).format('DD/MM/YYYY');
                        }
                        if ($scope.filters.deliveryDate != null &&
                            $scope.filters.deliveryDate.startDate != null) {
                            var d = $scope.filters.deliveryDate.startDate;
                            $scope.filters.deliveryDate.startDate = moment(d).format('DD/MM/YYYY');
                        }
                        if ($scope.filters.deliveryDate != null &&
                            $scope.filters.deliveryDate.endDate != null) {
                            var d = $scope.filters.deliveryDate.endDate;
                            $scope.filters.deliveryDate.endDate = moment(d).format('DD/MM/YYYY');
                        }

                        if ($scope.filters.status == 'LATERAPPROVED') {
                            orderFactory.getLateApproveOrders($scope.pageable).then(
                                function (data) {
                                    $scope.orders = data;
                                    $scope.loading = false;

                                    loadConsignments();
                                }
                            )
                        }
                        else {
                            orderFactory.getOrders($scope.filters, $scope.pageable).then(
                                function (data) {
                                    $scope.orders = data;
                                    $scope.loading = false;

                                    loadConsignments();
                                }
                            )
                        }

                    }

                    function loadConsignments() {
                        var shipments = [];
                        angular.forEach($scope.orders.content, function (order) {
                            angular.forEach(order.shipments, function (shipment) {
                                shipments.push(shipment.id)
                            })
                        });

                        if (shipments.length > 0) {
                            shipmentFactory.getConsignmentsForShipments(shipments).then(
                                function (data) {
                                    var map = new Hashtable();
                                    angular.forEach(data, function (consignment) {
                                        angular.forEach(consignment.shipments, function (shipment) {
                                            map.put(shipment.id, consignment);
                                        })
                                    });

                                    angular.forEach($scope.orders.content, function (order) {
                                        angular.forEach(order.shipments, function (shipment) {
                                            var con = map.get(shipment.id);
                                            if (con != null) {
                                                shipment.consignment = con;
                                            }
                                        })
                                    });

                                    setConsignments();
                                }
                            )
                        }
                    }

                    $scope.hasStringFilter = function (value) {
                        if (value != null && value != '') {
                            return "hasFilter";
                        }
                    };

                    $scope.filterStatus = function (status) {
                        $scope.filters = angular.copy($scope.emptyFilters);
                        $scope.filters.status = status;
                        $scope.pageable.page = 1;
                        loadOrders();
                    };

                    $scope.filterOrders = function (status, type) {
                        $scope.filters = angular.copy($scope.emptyFilters);
                        $scope.filters.status = status;
                        $scope.filters.orderType = type;
                        $scope.pageable.page = 1;
                        loadOrders();
                    };


                    function loadOrderCounts() {
                        /*
                         var filters = angular.copy($scope.emptyFilters);
                         filters.status = "NEW";
                         orderFactory.getOrders(filters, $scope.pageable).then(
                         function(data) {
                         $scope.orderCounts.newOrders = data.totalElements;
                         }
                         );

                         filters.status = "APPROVED";

                         orderFactory.getOrders(filters, $scope.pageable).then(
                         function(data) {
                         $scope.orderCounts.approvedOrders = data.totalElements;
                         }
                         );

                         filters.status = "PROCESSED,PARTIALLYSHIPPED";

                         orderFactory.getOrders(filters, $scope.pageable).then(
                         function(data) {
                         $scope.orderCounts.pendingOrders = data.totalElements;
                         }
                         );

                         filters.status = "SHIPPED";

                         orderFactory.getOrders(filters, $scope.pageable).then(
                         function(data) {
                         $scope.orderCounts.shippedOrders = data.totalElements;
                         }
                         );
                         */

                        var report = reportFactory.getReportById("reports.crm.order-count-by-status-and-type");
                        if (report != null) {
                            reportFactory.executeReport(report).then(
                                function (data) {
                                    var pendingProd = 0;
                                    var pendingSample = 0;
                                    angular.forEach(data, function (item) {
                                        if (item.status == "NEW") {
                                            angular.forEach(item.counts, function (type) {
                                                if (type.key == "PRODUCT") {
                                                    $scope.orderCounts.newOrders.product = type.number;
                                                }
                                                else if (type.key == "SAMPLE") {
                                                    $scope.orderCounts.newOrders.sample = type.number;
                                                }
                                            });
                                        }
                                        else if (item.status == "APPROVED") {
                                            angular.forEach(item.counts, function (type) {
                                                if (type.key == "PRODUCT") {
                                                    $scope.orderCounts.approvedOrders.product = type.number;
                                                }
                                                else if (type.key == "SAMPLE") {
                                                    $scope.orderCounts.approvedOrders.sample = type.number;
                                                }
                                            });
                                        }
                                        else if (item.status == "SHIPPED") {
                                            angular.forEach(item.counts, function (type) {
                                                if (type.key == "PRODUCT") {
                                                    $scope.orderCounts.shippedOrders.product = type.number;
                                                }
                                                else if (type.key == "SAMPLE") {
                                                    $scope.orderCounts.shippedOrders.sample = type.number;
                                                }
                                            });
                                        }
                                        else if (item.status == "PROCESSED" || item.key == "PARTIALLYSHIPPED") {
                                            //pending = pending + item.number;
                                            angular.forEach(item.counts, function (type) {
                                                if (type.key == "PRODUCT") {
                                                    pendingProd = pendingProd + type.number;
                                                }
                                                else if (type.key == "SAMPLE") {
                                                    pendingSample = pendingSample + type.number;
                                                }
                                            });
                                        }

                                        $scope.orderCounts.pendingOrders.product = pendingProd;
                                        $scope.orderCounts.pendingOrders.sample = pendingSample;
                                    });
                                }
                            )
                        }
                    }

                    (function () {
                        /*
                         if (cachingService.ordersViewCache != null &&
                         cachingService.ordersViewCache != undefined) {
                         $scope.pageable = cachingService.ordersViewCache.pageable;
                         $scope.filters = cachingService.ordersViewCache.filters;
                         if ($scope.filters.orderedDate == null) {
                         $scope.filters.orderedDate = {
                         startDate: null,
                         endDate: null
                         };
                         }
                         $scope.orders = cachingService.ordersViewCache.orders;
                         $scope.loading = false;
                         }
                         else {
                         loadOrders();
                         }
                         */

                        loadOrders();
                        loadOrderCounts();
                    })();
                }
            ]
        );
    }
);