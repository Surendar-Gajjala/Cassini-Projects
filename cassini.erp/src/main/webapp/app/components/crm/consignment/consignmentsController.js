define(['app/app.modules',
        'app/components/crm/consignment/consignmentFactory',
        'app/components/crm/consignment/new/newConsignmentController',
        'app/components/crm/consignment/pendingShipmentsController',
        'app/components/crm/consignment/shippedShipmentsController',
        'app/components/home/roles/orderprocessing/orderProcessingHomeController',
        'app/components/crm/order/shipment/shipmentFactory',
        'app/components/crm/order/orderFactory'
    ],
    function ($app) {
        $app.controller('ConsignmentsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$location', '$state', '$stateParams', '$cookies',
                'consignmentFactory', 'orderFactory', 'shipmentFactory',

                function ($scope, $rootScope, $timeout, $interval, $location, $state, $stateParams, $cookies,
                          consignmentFactory, orderFactory, shipmentFactory) {

                    $rootScope.iconClass = "fa fa-truck";
                    $rootScope.viewTitle = "Shipping";


                    $scope.mode = "all";
                    $scope.templates = {
                        processing: "app/components/home/roles/orderprocessing/orderProcessingHomeView.jsp",
                        pending: "app/components/crm/consignment/pendingShipmentsView.jsp",
                        shipped: "app/components/crm/consignment/shippedShipmentsView.jsp"
                    };

                    $scope.pendingShipments = null;
                    $scope.approvedOrders = null;

                    function loadPendingShipments() {
                        var filters = {
                            invoiceNumber: null,
                            poNumber: null,
                            invoiceAmount: null,
                            status: 'PENDING',
                            orderNumber: null,
                            customer: null,
                            createdDate: {
                                startDate: null,
                                endDate: null
                            }
                        };

                        var pageable = {
                            page: 1,
                            size: 1,
                            sort: {
                                label: "modifiedDate",
                                field: "modifiedDate",
                                order: "asc"
                            }
                        };

                        shipmentFactory.getShipments(filters, pageable).then(
                            function(data) {
                                $scope.pendingShipments = data.totalElements;
                            }
                        );
                    }

                    function loadApprovedOrders() {
                        var pageable = {
                            page: 1,
                            size: 1,
                            sort: {
                                label: "orderedDate",
                                field: "orderedDate",
                                order: "desc"
                            }
                        };

                        var filters = {
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
                            region: null,
                            salesRep: null,
                            orderTotal: null,
                            status: "APPROVED,PROCESSING",
                            invoiceNumber: null,
                            trackingNumber: null
                        };

                        orderFactory.getOrders(filters, pageable).then(
                            function(data) {
                                $scope.approvedOrders = data.totalElements;
                            }
                        )
                    }

                    $scope.goTo = function(route) {
                        $state.go(route);
                    };

                    (function() {
                        loadPendingShipments();
                        loadApprovedOrders();
                    })();
                }
            ]
        );
    }
);