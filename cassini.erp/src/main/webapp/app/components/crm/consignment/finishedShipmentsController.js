define(['app/app.modules',
        'app/components/crm/consignment/consignmentFactory',
        'app/shared/directives/tableDirectives',
        'app/shared/directives/commonDirectives',
        'app/components/crm/order/orderFactory',
        'app/components/crm/vehicle/vehicleFactory',
        'app/components/hrm/employee/employeeFactory',
        'app/components/crm/vehicle/vehicleSelectionController',
        'app/components/hrm/employee/dialog/employeeSelectionController'
    ],
    function ($app) {
        $app.controller('FinishedShipmentsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', '$modal',
                'consignmentFactory', 'orderFactory', 'vehicleFactory', 'employeeFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies, $modal,
                          consignmentFactory, orderFactory, vehicleFactory, employeeFactory) {

                    $rootScope.iconClass = "fa fa-truck";
                    $rootScope.viewTitle = "Finished Shipments";

                    $scope.vehicles = [];
                    $scope.employees = [];

                    $scope.mode = "all";

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

                    $scope.consignments = $scope.pagedResults;

                    $scope.emptyFilters = {
                        number: null,
                        status: 'FINISHED',
                        consignee: null,
                        shipper: null,
                        poNumber: null,
                        shippedDate: {
                            startDate: null,
                            endDate: null
                        },
                        orderNumber: null,
                        invoiceNumber: null,
                        value: null,
                        confirmationNumber: null,
                        vehicle: null,
                        driver: null,
                        shippedTo: null,
                        through: null
                    };
                    $scope.filters = angular.copy($scope.emptyFilters);

                    $scope.$watch('filters.shippedDate', function(date){
                        $scope.applyFilters();
                    });

                    $scope.pageChanged = function() {
                        $scope.loading = true;
                        $scope.consignments.content = [];
                        $scope.loadConsignments();
                    };

                    $scope.resetFilters = function() {
                        $scope.filters = angular.copy($scope.emptyFilters);
                        $scope.pageable.page = 1;
                        $scope.loadConsignments();
                    };

                    $scope.applyFilters = function() {
                        $scope.pageable.page = 1;
                        $scope.loadConsignments();
                    };

                    $scope.newConsignment = function() {
                        $scope.mode = "item";
                    };

                    $scope.closeConsignment = function() {
                        $scope.mode = "all";
                    };

                    $scope.getInvoiceNumbers = function(consignment) {
                        var invoices = [];
                        var i = 0;
                        consignment.invs = "";
                        angular.forEach(consignment.shipments, function(shipment) {
                            var inv = shipment.invoiceNumber;
                            i++;
                            if(inv != null && inv != undefined) {
                                invoices.push(inv);
                                if(i%4!=0) {
                                    consignment.invs += inv + ";";
                                }else{
                                    consignment.invs += inv+"; ";
                                }
                            }
                        });
                        return invoices;
                    };

                    $rootScope.Back = function() {
                        $state.go( 'app.crm.shipping');
                    };

                    $scope.setOrdersForConsingment = function() {
                        angular.forEach($scope.consignments.content, function(consignment) {
                            consignment.orders = [];
                            var map = new Hashtable();
                            consignment.poNumbers = [];
                            consignment.pos = "";
                            var i = 0;
                            angular.forEach(consignment.shipments, function(shipment) {
                                i++;
                                if(shipment.order != null) {
                                    var orderNumber = shipment.order.orderNumber;
                                    if(map.get(orderNumber) == null) {
                                        consignment.orders.push(shipment.order);
                                        map.put(orderNumber, shipment.order);
                                        if(shipment.order.poNumber != null || shipment.order.poNumber == "") {
                                            consignment.poNumbers.push(shipment.order.poNumber);
                                            if(i%4!=0) {
                                                consignment.pos += shipment.order.poNumber + ";";
                                            }else{
                                                consignment.pos += shipment.order.poNumber + "; ";
                                            }
                                        }
                                    }
                                }
                            })
                        });
                    };

                    $rootScope.$on('broadcast.crm.consignment.shipped', function() {
                        $scope.loadConsignments();
                    });

                    $scope.showConsignmentDetails = function(consignment) {
                        $state.go('app.crm.consigndetails', {consignmentId: consignment.id});
                    };

                    function setOrdersForShipments(orders) {
                        var map = new Hashtable();
                        angular.forEach(orders, function(order) {
                            angular.forEach(order.shipments, function(shipment) {
                                map.put(shipment.id, order);
                            });
                        });

                        angular.forEach($scope.consignments.content, function(consignment) {
                            angular.forEach(consignment.shipments, function(shipment) {
                                var order = map.get(shipment.id);
                                if(order != null) {
                                    shipment.order = order;
                                }
                            })
                        });
                        $scope.setOrdersForConsingment();
                    }

                    function loadOrdersForShipments() {
                        if($scope.consignments.content.length > 0) {
                            var shipmentIds = [];
                            angular.forEach($scope.consignments.content, function(consignment) {
                                for(var i=0; i<consignment.shipments.length; i++) {
                                    shipmentIds.push(consignment.shipments[i].id);
                                }
                            });

                            orderFactory.getOrdersForShipments(shipmentIds).then(
                                function(data) {
                                    setOrdersForShipments(data);
                                }
                            )
                        }
                    }

                    $scope.updateConsignment = function(consignment) {
                        consignmentFactory.updateConsignment(consignment).then(
                            function(data) {
                                consignment = data;
                            }
                        );
                    };

                    $scope.selectVehicle = function(consignment) {
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/crm/vehicle/vehicleSelectionDialog.jsp',
                            controller: 'VehicleSelectionController',
                            size: 'lg',
                            resolve: {
                                selectedVehicle: function () {
                                    return consignment.vehicle;
                                }
                            }
                        });

                        modalInstance.result.then(
                            function (selectedVehicle) {
                                consignment.vehicle = selectedVehicle;
                                $scope.updateConsignment(consignment);
                            }
                        );
                    };

                    $scope.selectDriver = function(consignment) {
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/hrm/employee/dialog/employeeSelectionDialog.jsp',
                            controller: 'EmployeeSelectionController',
                            size: 'lg',
                            resolve: {
                                selectedEmployee: function () {
                                    return consignment.driver;
                                },
                                "dialogTitle": "Select a driver"
                            }
                        });

                        modalInstance.result.then(
                            function (selectedEmployee) {
                                consignment.driver = selectedEmployee;
                                $scope.updateConsignment(consignment);
                            }
                        );
                    };

                    $scope.loadConsignments = function() {
                        if($scope.filters.shippedDate != null &&
                            $scope.filters.shippedDate.startDate != null) {
                            var d = $scope.filters.shippedDate.startDate;
                            $scope.filters.shippedDate.startDate = moment(d).format('DD/MM/YYYY');
                        }
                        if($scope.filters.shippedDate != null &&
                            $scope.filters.shippedDate.endDate != null) {
                            var d = $scope.filters.shippedDate.endDate;
                            $scope.filters.shippedDate.endDate = moment(d).format('DD/MM/YYYY');
                        }
                        consignmentFactory.getConsignments($scope.filters, $scope.pageable).then(
                            function(data) {
                                $scope.consignments = data;
                                $scope.loading = false;
                                loadOrdersForShipments();
                            }
                        );
                    };

                    function loadEmployees() {
                        employeeFactory.getAllEmployees().then(
                            function(data) {
                                $scope.employees = data;
                            }
                        );
                    }

                    function loadVehicles() {
                        vehicleFactory.getVehicles().then(
                            function(data) {
                                $scope.vehicles = data;
                            }
                        )
                    }

                    $rootScope.loadConsignments = $scope.loadConsignments;

                    $scope.updateConsignment = function(consignment) {
                        if(consignment.confirmationNumber != null && consignment.confirmationNumber != undefined) {
                            consignment.status = 'FINISHED';
                        }

                        var copyConsignment = angular.copy(consignment);
                        delete copyConsignment['shipments'];
                        delete copyConsignment['orders'];

                        consignmentFactory.updateConsignment(copyConsignment).then(
                            function(data) {
                                if(data.confirmationNumber != null && data.confirmationNumber != undefined) {
                                    var index = $scope.consignments.content.indexOf(consignment);
                                    if(index != -1) {
                                        $scope.consignments.content.splice(index, 1);
                                    }

                                }
                            }
                        );
                    };

                    $scope.selectVehicle = function(consignment) {
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/crm/vehicle/vehicleSelectionDialog.jsp',
                            controller: 'VehicleSelectionController',
                            size: 'lg',
                            resolve: {
                                selectedVehicle: function () {
                                    return consignment.vehicle;
                                }
                            }
                        });

                        modalInstance.result.then(
                            function (selectedVehicle) {
                                consignment.vehicle = selectedVehicle;
                                $scope.updateConsignment(consignment);
                            }
                        );
                    };

                    $scope.selectDriver = function(consignment) {
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/hrm/employee/dialog/employeeSelectionDialog.jsp',
                            controller: 'EmployeeSelectionController',
                            size: 'lg',
                            resolve: {
                                selectedEmployee: function () {
                                    return consignment.driver;
                                },
                                "dialogTitle": "Select a driver"
                            }
                        });

                        modalInstance.result.then(
                            function (selectedEmployee) {
                                consignment.driver = selectedEmployee;
                                $scope.updateConsignment(consignment);
                            }
                        );
                    };

                    $scope.$on('$viewContentLoaded', function(){
                        $rootScope.setToolbarTemplate('consignments-view-tb')
                    });

                    (function() {
                        loadEmployees();
                        loadVehicles();
                        $scope.loadConsignments();
                    })();

                }
            ]
        );
    }
);