define(['app/app.modules',
        'app/components/crm/order/shipment/shipmentFactory',
        'app/components/crm/order/orderFactory'
    ],
    function ($app) {
        $app.controller('SelectShipmentsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'shipmentFactory', 'orderFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                            shipmentFactory, orderFactory) {

                    $scope.loading = true;
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
                        last: false,
                        totalPages: 0,
                        totalElements: 0,
                        size: $scope.pageable.size,
                        number: 0,
                        sort: null,
                        first: false,
                        numberOfElements: 0
                    };

                    $scope.shipments = $scope.pagedResults;

                    $scope.emptyFilters = {
                        invoiceNumber: null,
                        status: 'PENDING',
                        orderNumber: null,
                        customer: null
                    };
                    $scope.filters = angular.copy($scope.emptyFilters);


                    $scope.lastSelectedShipment = null;
                    $scope.shipmentDetailsRow = {
                        showDetails: false
                    };

                    $scope.pageChanged = function() {
                        $scope.loading = true;
                        $scope.consignments.content = [];
                        loadShipments();
                    };

                    $scope.resetFilters = function() {
                        $scope.filters = angular.copy($scope.emptyFilters);
                        $scope.pageable.page = 1;
                        loadShipments();
                    };

                    $scope.applyFilters = function() {
                        $scope.pageable.page = 1;
                        loadShipments();
                    };

                    $scope.toggleShipmentDetails = function(shipment) {
                        if($scope.lastSelectedShipment != shipment) {
                            if($scope.lastSelectedShipment != null){
                                $scope.lastSelectedShipment.detailsOpen = false;
                            }

                            shipment.detailsOpen = true;
                        }

                        var index = $scope.shipments.content.indexOf($scope.shipmentDetailsRow);
                        if(index != -1) {
                            $scope.shipmentDetailsRow.showDetails = false;
                            $scope.shipments.content.splice(index, 1);
                        }

                        if($scope.lastSelectedShipment == null || $scope.lastSelectedShipment != shipment ||
                            $scope.lastSelectedShipment.detailsOpen == false) {
                            $scope.shipmentDetailsRow.showDetails = true;
                            index = $scope.shipments.content.indexOf(shipment);
                            $scope.shipments.content.splice(index + 1, 0, $scope.shipmentDetailsRow);
                        }

                        if($scope.lastSelectedShipment != null && $scope.lastSelectedShipment != shipment) {
                            $scope.lastSelectedShipment = false;
                        }
                        else if ($scope.lastSelectedShipment != null) {
                            $scope.lastSelectedShipment.detailsOpen = !$scope.lastSelectedShipment.detailsOpen;
                        }

                        $scope.lastSelectedShipment = shipment;
                    };

                    $scope.toggleSelection = function(shipment) {
                        if(shipment.selected == true && $scope.isShipmentSelected(shipment) == false) {
                            $scope.addShipment(shipment);
                        }
                        else if ($scope.isShipmentSelected(shipment) == true){
                            if(shipment != null) {
                                var s = $scope.getShipmentFromSelection(shipment.id);
                                $scope.removeShipment(s);
                            }
                        }
                    };

                    function setOrdersForShipments(orders) {
                        var map = new Hashtable();
                        angular.forEach(orders, function(order) {
                            angular.forEach(order.shipments, function(shipment) {
                                map.put(shipment.id, order);
                            });
                        });

                        angular.forEach($scope.shipments.content, function(shipment) {
                            var order = map.get(shipment.id);
                            if(order != null) {
                                shipment.order = order;
                            }
                        })
                    }

                    function loadOrdersForShipments() {
                        if($scope.shipments.content.length > 0) {
                            var shipmentIds = [];
                            angular.forEach($scope.shipments.content, function(shipment) {
                                shipmentIds.push(shipment.id);
                            });

                            orderFactory.getOrdersForShipments(shipmentIds).then(
                                function(data) {
                                    setOrdersForShipments(data);
                                }
                            )
                        }
                    }


                    function loadShipments() {
                        shipmentFactory.getShipments($scope.filters, $scope.pageable).then(
                            function(data) {
                                angular.forEach(data.content, function(item) {
                                    item.showDetails = false;
                                    item.detailsOpen = false;
                                    item.selected = $scope.isShipmentSelected(item) == true;
                                });
                                $scope.shipments = data;
                                $scope.loading = false;

                                loadOrdersForShipments();
                            }
                        );
                    }

                    (function() {
                        loadShipments();
                    })();
                }
            ]
        );
    }
);