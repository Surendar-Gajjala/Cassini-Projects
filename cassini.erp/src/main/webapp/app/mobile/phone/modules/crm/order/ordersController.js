define(['app/app.modules',
        'app/shared/directives/commonDirectives',
        'app/components/crm/order/orderFactory'
    ],
    function ($app) {
        $app.controller('OrdersController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'orderFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                        orderFactory) {
                    $rootScope.viewName = "Orders";
                    $rootScope.backgroundColor = "#8bc34a";

                    $scope.searchText = null;
                    $scope.currentTab = "NEW";
                    $scope.totalResults = null;

                    $scope.pageable = {
                        page: 1,
                        size: 10,
                        sort: {
                            label: "modifiedDate",
                            field: "modifiedDate",
                            order: "desc"
                        }
                    };
                    $scope.criteria = {
                        searchQuery: null,
                        status: null,
                        orderedDate: {
                            startDate: null,
                            endDate: null
                        },
                        deliveryDate: {
                            startDate: null,
                            endDate: null
                        }
                    };

                    $scope.newOrders = null;
                    $scope.approvedOrders = null;
                    $scope.processingOrders = null;
                    $scope.pendingOrders = null;
                    $scope.shippedOrders = null;
                    $scope.canceledOrders = null;

                    function createEmptyObject() {
                        return {
                            title: "",
                            status: null,
                            orders: [],
                            loaded: false,
                            pageable: angular.copy($scope.pageable),
                            criteria: angular.copy($scope.criteria)
                        }
                    }

                    $scope.emptyPagedResults = {
                        content: [],
                        last: false,
                        totalPages: 0,
                        totalElements: 0,
                        size: 20,
                        number: 0,
                        sort: null,
                        first: true,
                        numberOfElements: 0
                    };

                    function initTabs() {
                        $scope.newOrders = createEmptyObject();
                        $scope.newOrders.pagedResults = angular.copy($scope.emptyPagedResults);
                        $scope.newOrders.status = "NEW";
                        $scope.newOrders.title = "NEW";
                        $scope.newOrders.criteria.status = "NEW";

                        $scope.approvedOrders = createEmptyObject();
                        $scope.approvedOrders.pagedResults = angular.copy($scope.emptyPagedResults);
                        $scope.approvedOrders.status = "APPROVED";
                        $scope.approvedOrders.title = "APPROVED";
                        $scope.approvedOrders.criteria.status = "APPROVED";

                        $scope.processingOrders = createEmptyObject();
                        $scope.processingOrders.pagedResults = angular.copy($scope.emptyPagedResults);
                        $scope.processingOrders.status = "PROCESSING";
                        $scope.processingOrders.title = "PROCESSING";
                        $scope.processingOrders.criteria.status = "PROCESSING";

                        $scope.pendingOrders = createEmptyObject();
                        $scope.pendingOrders.pagedResults = angular.copy($scope.emptyPagedResults);
                        $scope.pendingOrders.status = "PENDING";
                        $scope.pendingOrders.title = "PENDING";
                        $scope.pendingOrders.criteria.status = "PROCESSED,PARTIALLYSHIPPED";

                        $scope.shippedOrders = createEmptyObject();
                        $scope.shippedOrders.pagedResults = angular.copy($scope.emptyPagedResults);
                        $scope.shippedOrders.status = "SHIPPED";
                        $scope.shippedOrders.title = "SHIPPED";
                        $scope.shippedOrders.criteria.status = "SHIPPED";

                        $scope.canceledOrders = createEmptyObject();
                        $scope.canceledOrders.pagedResults = angular.copy($scope.emptyPagedResults);
                        $scope.canceledOrders.status = "CANCELED";
                        $scope.canceledOrders.title = "CANCELED";
                        $scope.canceledOrders.criteria.status = "CANCELLED";
                    }

                    initTabs();

                    $scope.tabs = [
                        $scope.newOrders,
                        $scope.approvedOrders,
                        $scope.processingOrders,
                        $scope.pendingOrders,
                        $scope.shippedOrders,
                        $scope.canceledOrders
                    ];

                    $scope.approveOrder = function(order) {
                        orderFactory.approveOrder(order.id).then(
                            function(data) {
                                var index = $scope.newOrders.orders.indexOf(order);
                                if(index != -1) {
                                    $scope.newOrders.orders.splice(index, 1);
                                    var title = "NEW (" + data.totalElements + ")";
                                    $scope.newOrders.title = title;
                                }
                            }
                        );
                    };

                    $scope.tabChanged = function(tab) {
                        $scope.currentTab = tab;

                        if($scope.searchText != null && $scope.searchText != "") {
                            tab.searchText = $scope.searchText;
                        }
                        else {
                            tab.searchText = null;
                        }

                        if(tab.loaded == false){
                            tab.orders = [];
                            $scope.loadOrders(tab);
                        }

                        window.$("md-tab-content").hide();
                        $timeout(function() {
                            window.$("md-tab-content").show();
                        }, 100);
                    };

                    $scope.nextPage = function() {
                        var tab = $scope.currentTab;
                        if(tab.pagedResults.last == false) {
                            tab.pageable.page = tab.pageable.page + 1;
                            $scope.loadOrders(tab);
                        }
                    };

                    $scope.loadOrders = function(orderStatus) {
                        var search = hasSearchQuery();

                        if(search == true) {
                            orderStatus.criteria.searchQuery = $scope.searchText;
                        }

                        var promise = orderFactory.getOrders(orderStatus.criteria, orderStatus.pageable);
                        if(search == true) {
                            promise = orderFactory.searchOrders(orderStatus.criteria, orderStatus.pageable);
                        }
                        promise.then(
                            function(data) {
                                orderStatus.pagedResults = data;
                                angular.forEach(data.content, function(order) {
                                    orderStatus.orders.push(order);
                                });
                                orderStatus.loaded = true;
                            }
                        );
                    };

                    $scope.search = function() {
                        var inputs = window.$('#search').find('input');
                        inputs.blur();

                        var search = hasSearchQuery();

                        angular.forEach($scope.tabs, function(tab) {
                            tab.orders = [];
                            tab.loaded = false;
                        });

                        var pageable = {
                            page: 1,
                            size: 1,
                            sort: {
                                label: "modifiedDate",
                                field: "modifiedDate",
                                order: "desc"
                            }
                        };
                        var filters = angular.copy($scope.criteria);
                        var promise = orderFactory.getOrders(filters, pageable);

                        if(search == true) {
                            filters.searchQuery = $scope.searchText;
                            promise = orderFactory.searchOrders(filters, pageable);
                        }

                        $scope.totalResults = null;
                        promise.then(
                            function(data) {
                                $scope.totalResults = data.totalElements;
                                loadCounts(search);
                                $scope.tabChanged($scope.currentTab);
                            }
                        );
                    };

                    function hasSearchQuery() {
                        var search = false;
                        if($scope.searchText != null && $scope.searchText != "") {
                            angular.forEach($scope.tabs, function(tab) {
                                tab.criteria.searchQuery = $scope.searchText;
                            });
                            search = true;
                        }

                        return search;
                    }


                    function loadCounts(search) {
                        var pageable = angular.copy($scope.pageable);
                        pageable.page = 1;
                        pageable.size = 1;

                        angular.forEach($scope.tabs, function(tab) {
                            var criteria = angular.copy($scope.criteria);
                            criteria.status = tab.criteria.status;

                            if(search == true) {
                                criteria.searchQuery = $scope.searchText;
                            }

                            var promise = orderFactory.getOrders(criteria, pageable);
                            if(search == true) {
                                promise = orderFactory.searchOrders(criteria, pageable);
                            }
                            promise.then(
                                function(data) {
                                    var title = tab.status + " (" + data.totalElements + ")";
                                    tab.title = title;
                                }
                            )
                        });
                    }


                    (function() {
                        var filters = angular.copy($scope.criteria);
                        var pageable = {
                            page: 1,
                            size: 1,
                            sort: {
                                label: "modifiedDate",
                                field: "modifiedDate",
                                order: "desc"
                            }
                        };
                        orderFactory.getOrders(filters, pageable).then(
                            function(data) {
                                $scope.totalResults = data.totalElements;
                                loadCounts(false);
                            }
                        );

                    })();

                }
            ]
        );
    }
);