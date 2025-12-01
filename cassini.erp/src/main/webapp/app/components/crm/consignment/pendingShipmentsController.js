define(['app/app.modules',
        'app/components/crm/order/shipment/shipmentFactory',
        'app/components/crm/order/orderFactory',
        'app/components/crm/consignment/new/newConsignmentController'
    ],
    function ($app) {
        $app.controller('PendingShipmentsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'shipmentFactory', 'orderFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          shipmentFactory, orderFactory) {

                    $rootScope.iconClass = "fa fa-truck";
                    $rootScope.viewTitle = "Pending Shipments";

                    $scope.selectedShipments = [];
                    $rootScope.mode = 'all';
                    $rootScope.selection = null;
                    $scope.tpls = {
                        newConsignment: "app/components/crm/consignment/new/newConsignmentView.jsp"
                    };
                    $scope.dateRangeOptions = {
                        ranges: {
                            'Today': [moment(), moment()],
                            'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                            'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                            'Last 30 Days': [moment().subtract(29, 'days'), moment()],
                            'This Month': [moment().startOf('month'), moment().endOf('month')],
                            'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                            'Yea to Date': [
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
                            order: "asc"
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

                    $scope.shipments = angular.copy($scope.pagedResults);
                    $scope.editingShipment = null;


                    $scope.emptyFilters = {
                        invoiceNumber: null,
                        poNumber: null,
                        invoiceAmount: null,
                        status: 'PENDING',
                        orderNumber: null,
                        customer: null,
                        shipTo: null,
                        createdDate: {
                            startDate: null,
                            endDate: null
                        }
                    };
                    $scope.filters = angular.copy($scope.emptyFilters);


                    $scope.$watch('filters.createdDate', function(date){
                        $scope.applyFilters();
                    });

                    $scope.lastSelectedShipment = null;
                    $scope.shipmentDetailsRow = {
                        showDetails: false
                    };

                    $scope.pageChanged = function() {
                        $scope.loading = true;
                        $scope.shipments.content = [];
                        $scope.loadShipments();
                    };

                    $scope.resetFilters = function() {
                        $scope.filters = angular.copy($scope.emptyFilters);
                        $scope.pageable.page = 1;
                        $scope.loadShipments();
                    };

                    $scope.applyFilters = function() {
                        $scope.pageable.page = 1;
                        $scope.loading = true;
                        $scope.shipments.content = [];
                        $scope.loadShipments();
                    };

                    $scope.shipSelectedItems = function() {
                        $rootScope.mode = 'new';
                    };

                    $rootScope.shipSelectedItems = $scope.shipSelectedItems;

                    $scope.closeConsignment = function() {
                        $rootScope.mode = 'all';
                        $scope.selectedShipments = [];
                        $scope.shipments = angular.copy($scope.pagedResults);
                        $rootScope.selection =null;
                        $scope.loadShipments();
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

                    $scope.saveShipment = function(shipment) {
                        orderFactory.updateInvoiceNumber(shipment).then(
                            function(data) {
                                $rootScope.showSuccessMessage("Invoice number updated")
                            }
                        )
                    };

                    $rootScope.Back = function() {
                        $state.go( 'app.crm.shipping');
                    };

                    $scope.toggleSelection = function(shipment) {
                        if(shipment.selected == false) {
                            var index = $scope.selectedShipments.indexOf(shipment);
                            if(index != -1) {
                                $scope.selectedShipments.splice(index, 1);
                            }
                        }
                        else {
                            $scope.selectedShipments.push(shipment);
                            $rootScope.selection = 'selected';
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

                    $scope.editShipment = function() {
                        $scope.editingShipment = angular.copy($scope.lastSelectedShipment);

                        var map = new Hashtable();
                        angular.forEach($scope.editingShipment.details, function(item) {
                            map.put(item.product.id, item);
                        });

                        angular.forEach($scope.lastSelectedShipment.order.details, function(item) {
                            if(map.get(item.product.id) == null) {
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

                    $rootScope.loadShipments = $scope.loadShipments;
                    $scope.loadShipments = function () {
                        $scope.loading = true;
                        if($scope.filters.createdDate != null &&
                            $scope.filters.createdDate.startDate != null) {
                            var d = $scope.filters.createdDate.startDate;
                            $scope.filters.createdDate.startDate = moment(d).format('DD/MM/YYYY');
                        }
                        if($scope.filters.createdDate != null &&
                            $scope.filters.createdDate.endDate != null) {
                            var d = $scope.filters.createdDate.endDate;
                            $scope.filters.createdDate.endDate = moment(d).format('DD/MM/YYYY');
                        }
                        shipmentFactory.getShipments($scope.filters, $scope.pageable).then(
                            function(data) {

                                var map = new Hashtable();
                                angular.forEach($scope.selectedShipments, function(shipment){
                                    map.put(shipment.id, shipment);
                                });

                                angular.forEach(data.content, function(item) {
                                    item.showDetails = false;
                                    item.detailsOpen = false;
                                    item.selected = map.get(item.id) != null;
                                    item.editMode = false;
                                });
                                $scope.shipments = data;
                                $scope.loading = false;

                                loadOrdersForShipments();
                            }
                        );
                    };

                    $scope.updateShipment = function () {
                        calculateInvoiceAmount($scope.editingShipment);
                        shipmentFactory.updateShipment($scope.editingShipment).then(
                            function (data) {
                                $scope.lastSelectedShipment.details = data.details;
                            }
                        );
                        $scope.lastSelectedShipment.editMode = false;
                    };

                    function calculateInvoiceAmount(shipment) {
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
                    }

                    $rootScope.cancelShipment = function (Shipment) {
                        shipmentFactory.cancelShipment(Shipment.id).then(
                            function (data) {
                                $scope.shipments = data;
                                $scope.loadShipments();
                                $rootScope.showSuccessMessage("Shipment has been cancelled");
                                $rootScope.$broadcast('app.updateNotification');
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    };

                    $scope.$on('$viewContentLoaded', function(){
                        $rootScope.setToolbarTemplate('pendingshipments-view-tb')
                    });

                    (function() {
                        $scope.loadShipments();
                    })();

                }
            ]
        );
    }
);