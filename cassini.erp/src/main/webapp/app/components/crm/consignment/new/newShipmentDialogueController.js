define(['app/app.modules',
        'app/components/crm/order/shipment/shipmentFactory',
        'app/components/crm/order/orderFactory'
    ],
    function(module) {
        module.controller('NewShipmentDialogueController', NewShipmentDialogueController);

        function NewShipmentDialogueController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, shipmentFactory,orderFactory,$modalInstance) {

            $scope.shipments =[];
            
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
                totalElements: Infinity,
                size: $scope.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            $scope.shipments = $scope.pagedResults;

            $scope.items = angular.copy($scope.pagedResults);

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

            $scope.searchShipments = function(searchText){
                $scope.filters.orderNumber = searchText;
                loadShipments();
            }

            $scope.previousPage = function(){
                $scope.pageable.page--;
                loadShipments();
            };

            $scope.nextPage = function() {
                $scope.pageable.page++;
                loadShipments();
            };


            $scope.pageChanged = function() {
                $scope.loading = true;
                var pagenum =   $scope.pageable.page;
                if($scope.viewType == 'grid') {
                    $scope.customers.content = [];
                }
                loadShipments();
                //$state.go("app.crm.shipments", { page: pagenum});
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

            $scope.getSelectedShipments = function() {
                var selected = [];
                angular.forEach($scope.shipments.content, function(shipment) {
                    if(shipment.selected == true) {
                        selected.push(shipment);
                    }
                });

                return selected;
            };

            $scope.ok = function() {
                var getSelectedShipments = $scope.getSelectedShipments();
                $modalInstance.close(getSelectedShipments);
            };

            $scope.cancel = function() {
                $modalInstance.dismiss('cancel');
            };

            function loadShipments () {
                $scope.loading = true;
                shipmentFactory.getShipments($scope.filters, $scope.pageable).then(
                    function(data) {
                        $scope.shipments = data;
                        $scope.loading = false;
                        loadOrdersForShipments();
                    }
                );
            };

            (function() {
                loadShipments();
            })();
        }
    }
);
