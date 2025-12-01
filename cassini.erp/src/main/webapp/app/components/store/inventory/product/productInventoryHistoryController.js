define(['app/app.modules',
        'app/components/prod/product/productFactory',
        'app/components/crm/order/shipment/shipmentFactory',
        'app/components/crm/order/orderFactory'],
    function ($app) {
        $app.controller('ProductInventoryHistoryController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'productFactory', 'shipmentFactory', 'orderFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          productFactory, shipmentFactory, orderFactory) {


                    $scope.loading = true;
                    $scope.loadingReferences = true;


                    $scope.emptyPagedResults = {
                        content: [],
                        last: false,
                        totalPages: 0,
                        totalElements: Infinity,
                        size: 0,
                        number: 0,
                        sort: null,
                        first: false,
                        numberOfElements: 0
                    };
                    $scope.pageable = {
                        page: 1,
                        size: 10,
                        sort: {
                            label: "timestamp",
                            field: "timestamp",
                            order: "desc"
                        }
                    };

                    $scope.emptyFilters = {
                        stockType: null,
                        product: null
                    };

                    $scope.filters = angular.copy($scope.emptyFilters);

                    $scope.history = angular.copy($scope.emptyPagedResults);

                    $scope.pageChanged = function () {
                        $scope.loading = true;
                        $scope.history.content = [];
                        $scope.loadProductInventoryHistoryBYStockType($scope.filters.stockType);
                    };

                    $scope.loadProductInventoryHistoryBYStockType = function(stockType) {
                        $scope.history = $scope.emptyPagedResults;
                        $scope.filters.stockType = stockType;
                        $scope.filters.product = $scope.selectedProduct.id;
                        productFactory.getProductInventoryHistoryByStockType($scope.selectedProduct.id, $scope.pageable,$scope.filters).then(
                            function (data) {
                                $scope.loading = false;
                                $scope.history = data;
                                loadShipmentReferences();
                            }
                        )
                    };

                    function loadProductInventoryHistory() {
                        $scope.history = $scope.emptyPagedResults;
                        productFactory.getProductInventoryHistory($scope.selectedProduct.id, $scope.pageable).then(
                            function (data) {
                                $scope.loading = false;
                                $scope.history = data;
                                loadShipmentReferences();
                            }
                        )
                    }

                    function loadShipmentReferences() {
                        var shipIds = [];
                        var map = new Hashtable();

                        for (var i = 0; i < $scope.history.content.length; i++) {
                            var row = $scope.history.content[i];
                            row.referenceObject = {};
                            if (row.reference != null && row.reference != undefined &&
                                row.type == 'STOCKOUT') {
                                map.put(row.reference, row);
                                shipIds.push(row.reference)
                            }
                        }

                        if (shipIds.length > 0) {
                            shipmentFactory.getShipmentsByIds(shipIds).then(
                                function (shipments) {
                                    angular.forEach(shipments, function (shipment) {
                                        var row = map.get(shipment.id);
                                        if (row != null) {
                                            row.referenceObject = shipment;
                                        }
                                    });
                                    orderFactory.getOrdersForShipments(shipIds).then(
                                        function (orders) {
                                            angular.forEach(orders, function (order) {
                                                var orderShipments = order.shipments;
                                                angular.forEach(orderShipments, function (orderShipment) {
                                                    var row = map.get(orderShipment.id);
                                                    if (row != null) {
                                                        row.referenceObject.order = order;
                                                    }
                                                })
                                            })
                                        }
                                    );

                                    $scope.loadingReferences = false;
                                }
                            )
                        }
                    }

                    (function () {
                        //loadProductInventoryHistory();
                        $scope.loadProductInventoryHistoryBYStockType($scope.filters.stockType);
                    })();
                }
            ]
        );
    }
);