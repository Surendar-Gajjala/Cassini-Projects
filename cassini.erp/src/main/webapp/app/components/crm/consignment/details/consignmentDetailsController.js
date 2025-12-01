define(['app/app.modules',,
        'app/components/common/commonFactory',
        'app/components/crm/consignment/consignmentFactory',
        'app/components/crm/order/shipment/shipmentFactory',
        'app/components/crm/consignment/new/selectShipmentsController',
        'app/components/crm/order/orderFactory'
    ],
    function ($app) {
        $app.controller('ConsignmentDetailsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams', '$cookies',
                'commonFactory', 'consignmentFactory', 'shipmentFactory', 'orderFactory','$modal',

                function ($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies,
                          commonFactory, consignmentFactory, shipmentFactory, orderFactory,$modal) {

                    $scope.shippers = [];
                    $scope.shipment = false;
                    $scope.emptyAddress = {
                        addressType: commonFactory.getAddressTypeByName("Office"),
                        addressText: null,
                        city: null,
                        state: null,
                        pincode: null,
                        country: commonFactory.getCountryAndStatesMapByCountry("India").country
                    };
                    $scope.consignment = {
                        shipper: null,
                        consignee: null,
                        contents: null,
                        value: null,
                        description: null,
                        mobilePhone: null,
                        officePhone: null,
                        shippingAddress: angular.copy($scope.emptyAddress),
                        shipments: []
                    };
                    $scope.states = commonFactory.getCountryAndStatesMapByCountry("India").states;
                    $scope.showShipmentSelection = false;

                    $rootScope.editConsignment = false;

                    $scope.lastSelectedShipment = null;
                    $scope.shipmentDetailsRow = {
                        showDetails: false
                    };

                    $rootScope.printConsignment = function() {
                        var url = "{0}//{1}/api/crm/consignments/{2}/print".
                            format(window.location.protocol, window.location.host,
                            $scope.consignment.id);
                        var printWindow = window.open(url, '',
                            'scrollbars=yes,menubar=no,width=500,resizable=yes,toolbar=no,location=no,status=no');
                    };

                    $rootScope.printConsignmentShipments = function() {
                        var url = "{0}//{1}/api/crm/consignments/{2}/shipments/print".
                            format(window.location.protocol, window.location.host,
                            $scope.consignment.id);
                        var printWindow = window.open(url, '',
                            'scrollbars=yes,menubar=no,width=500,resizable=yes,toolbar=no,location=no,status=no');
                    };

                    $rootScope.goBack = function() {
                        window.history.back();
                    };

                    $scope.addShipments = function() {
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/crm/consignment/new/newShipmentDialogue.jsp',
                            controller: 'NewShipmentDialogueController',
                            size: 'lg'
                        });

                        modalInstance.result.then(
                            function (result) {
                                angular.forEach(result, function(item) {
                                    if(!doesShipmentExist(item)) {
                                        item.showDetails = false;
                                        item.detailsOpen = false;
                                        $scope.consignment.shipments.unshift(item);
                                    }
                                });
                            }
                        );
                    };

                    function doesShipmentExist(shipment) {
                        var exists = false;
                        angular.forEach($scope.consignment.shipments, function(item) {
                            if(shipment.id == item.id) {
                                exists = true;
                                if(exists == true){
                                    $rootScope.showSuccessMessage("This Shipment ***"+ item.order.orderNumber +"***Already Exist.");
                                }
                            }
                        });
                        return exists;
                    };

                    $rootScope.setEditConsignment = function () {
                        $rootScope.editConsignment = true;
                        $scope.$apply();
                    };

                    $scope.toggleShipmentDetails = function(shipment) {
                        if($scope.lastSelectedShipment != shipment) {
                            if($scope.lastSelectedShipment != null){
                                $scope.lastSelectedShipment.detailsOpen = false;
                            }

                            shipment.detailsOpen = true;
                        }

                        var index = $scope.consignment.shipments.indexOf($scope.shipmentDetailsRow);
                        if(index != -1) {
                            $scope.shipmentDetailsRow.showDetails = false;
                            $scope.consignment.shipments.splice(index, 1);
                        }

                        if($scope.lastSelectedShipment == null || $scope.lastSelectedShipment != shipment ||
                            $scope.lastSelectedShipment.detailsOpen == false) {
                            $scope.shipmentDetailsRow.showDetails = true;
                            index = $scope.consignment.shipments.indexOf(shipment);
                            $scope.consignment.shipments.splice(index + 1, 0, $scope.shipmentDetailsRow);
                        }

                        if($scope.lastSelectedShipment != null && $scope.lastSelectedShipment != shipment) {
                            $scope.lastSelectedShipment = false;
                        }
                        else if ($scope.lastSelectedShipment != null) {
                            $scope.lastSelectedShipment.detailsOpen = !$scope.lastSelectedShipment.detailsOpen;
                        }

                        $scope.lastSelectedShipment = shipment;
                    };

                    function initConsignmentShipments() {
                        angular.forEach($scope.consignment.shipments, function(shipment) {
                            shipment.showDetails = false;
                            shipment.detailsOpen = false;
                        });
                    }

                    $scope.removeItem = function (shipment) {
                        shipmentFactory.deleteShipmentFromConsignment(shipment.id).then(
                            function (data) {
                                var index = $scope.consignment.shipments.indexOf(shipment);
                                $scope.consignment.shipments.splice(index, 1);
                                $rootScope.showSuccessMessage("Item has been removed");
                                shipmentFactory.updateShipment( $scope.consignment.shipments).then(
                                    function (data) {
                                        $scope.consignment.shipments = data;
                                        if($scope.consignment.shipments == null){
                                          $scope.shipment = true;

                                        }
                                    });
                            });
                    }

                    function loadShippers() {
                        shipmentFactory.getShippers().then(
                            function(data) {
                                $scope.shippers = data;
                            }
                        );
                    }

                    function setOrdersForShipments(orders) {
                        var map = new Hashtable();
                        angular.forEach(orders, function(order) {
                            angular.forEach(order.shipments, function(shipment) {
                                map.put(shipment.id, order);
                            });
                        });

                        angular.forEach($scope.consignment.shipments, function(shipment) {
                            var order = map.get(shipment.id);
                            if(order != null) {
                                shipment.order = order;
                            }
                        })
                    }

                    function loadOrdersForShipments() {
                        var shipmentIds = [];

                        for(var i=0; i<$scope.consignment.shipments.length; i++) {
                            shipmentIds.push($scope.consignment.shipments[i].id);
                        }

                        orderFactory.getOrdersForShipments(shipmentIds).then(
                            function(data) {
                                setOrdersForShipments(data);
                            }
                        )
                    }

                    function loadConsignment() {
                        var consignmentId = $stateParams.consignmentId;
                        consignmentFactory.getConsignment(consignmentId).then(
                            function(data) {
                                $scope.consignment = data;
                                initConsignmentShipments();
                                loadOrdersForShipments();
                            }
                        )
                    }

                    $scope.$on('$viewContentLoaded', function(){
                        $rootScope.setToolbarTemplate('consignment-view-tb')
                    });

                    $rootScope.saveChanges = function() {
                        consignmentFactory.updateConsignment($scope.consignment).then (
                            function(data) {
                                $rootScope.editConsignment = false;
                                $rootScope.showSuccessMessage("Consignment changes saved successfully!");
                            }
                        )
                    };

                    (function() {
                        $rootScope.editConsignment = false;
                        loadShippers();
                        loadConsignment();
                    })();
                }
            ]
        );
    }
);