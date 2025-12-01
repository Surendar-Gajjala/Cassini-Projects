define(['app/app.modules',,
        'app/components/common/commonFactory',
        'app/components/crm/consignment/consignmentFactory',
        'app/components/crm/order/shipment/shipmentFactory',
        'app/components/crm/consignment/new/selectShipmentsController',
        'app/components/crm/consignment/new/newShipmentDialogueController',
        'app/components/crm/order/details/orderDetailsController',
        'app/components/crm/order/shipment/shipmentController'
    ],
    function ($app) {
        $app.controller('NewConsignmentController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'commonFactory', 'consignmentFactory', 'shipmentFactory','$modal',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          commonFactory, consignmentFactory, shipmentFactory,$modal) {

                    $scope.shippers = [];
                    $scope.shipped = false;
                    $scope.shipments = [];

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
                        paidBy: 'CUSTOMER',
                        deliveryMode: 'WITH_PASS',
                        shippingCost: null,
                        shippingAddress: angular.copy($scope.emptyAddress),
                        shipments: $scope.selectedShipments
                    };
                    $scope.states = commonFactory.getCountryAndStatesMapByCountry("India").states;
                    $scope.showShipmentSelection = false;

                    $scope.consignmentTemplates = {
                        selectShipments: 'app/components/crm/consignment/new/selectShipmentsView.jsp'
                    };

                    if($scope.formMode == 'details') {
                        $scope.consignment = $scope.selectedConsignment;
                    }

                    $scope.lastSelectedShipment = null;
                    $scope.shipmentDetailsRow = {
                        showDetails: false
                    };

                    function updateConsignmentInfo() {
                        if($scope.consignment.shipments.length > 0) {
                            var shipment = $scope.consignment.shipments[0];
                            if(shipment.order.shipTo != null && shipment.order.shipTo.trim() != "") {
                                $scope.consignment.consignee = shipment.order.shipTo;
                            }
                            else {
                                $scope.consignment.consignee = shipment.order.customer.name;
                            }
                            $scope.consignment.shippingAddress = shipment.shippingAddress != null ? shipment.shippingAddress : shipment.order.shippingAddress;
                            $scope.consignment.shippingAddress.id = null;
                            $scope.consignment.mobilePhone = getMobilePhone(shipment);
                            $scope.consignment.officePhone = getOfficePhone(shipment);
                        }
                        updateConsignmentValue();
                    }

                    $scope.addShipments = function() {
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/crm/consignment/new/newShipmentDialogue.jsp',
                            controller: 'NewShipmentDialogueController',
                            size: 'md'
                        });

                        modalInstance.result.then(
                            function (result) {
                                /*$scope.shipments.unshift(result);*/
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
                    }

                    $scope.selectShipments = function() {
                        $scope.showShipmentSelection = true;
                    };

                    $scope.closeShipments = function() {
                        $scope.showShipmentSelection = false;
                    };

                    $scope.isShipmentSelected = function(shipment) {
                        var selected = false;
                        angular.forEach($scope.consignment.shipments, function(item) {
                             if(item.id == shipment.id) {
                                 selected = true;
                             }
                        });
                        return selected;
                    };

                    $scope.getShipmentFromSelection = function(id) {
                        var shipment = null;
                        angular.forEach($scope.consignment.shipments, function(item) {
                            if(item.id == id) {
                                shipment = item;
                            }
                        });
                        return shipment
                    };

                    $scope.addShipment = function(shipment) {
                        if($scope.consignment.consignee == null) {
                            if(shipment.shippingAddress != null &&
                                shipment.shippingAddress.addressText != null &&
                                $scope.consignment.shippingAddress.addressText == null) {
                                $scope.consignment.shippingAddress = angular.copy(shipment.shippingAddress);
                                delete $scope.consignment.shippingAddress.id
                            }

                            if($scope.consignment.consignee == null) {
                                $scope.consignment.consignee = shipment.order.customer.name;
                            }

                            if($scope.consignment.officePhone == null) {
                                $scope.consignment.officePhone = getOfficePhone(shipment);
                            }

                            if($scope.consignment.mobilePhone == null) {
                                $scope.consignment.mobilePhone = getMobilePhone(shipment);
                            }
                        }

                        $scope.consignment.shipments.push(shipment);

                        updateConsignmentValue();
                    };

                    function getMobilePhone(shipment) {
                        var phone = null;

                        var contact = shipment.order.customer.contactPerson;
                        if(contact != null) {
                            phone = contact.phoneMobile;
                        }

                        return phone;
                    }

                    function getOfficePhone(shipment) {
                        return shipment.order.customer.officePhone;
                    }


                    $scope.removeShipment = function(shipment) {
                        $scope.consignment.shipments.splice(shipment, 1);

                        if($scope.consignment.shipments.length == 0) {
                            $scope.consignment.shippingAddress = angular.copy($scope.emptyAddress);
                            $scope.consignment.consignee = null;
                            $scope.consignment.mobilePhone = null;
                            $scope.consignment.officePhone = null;
                            $scope.consignment.contents = null;
                            $scope.consignment.value = null;
                            $scope.consignment.description = null;
                        }

                        updateConsignmentValue();
                    };

                    function updateConsignmentValue() {
                        var value = 0;
                        angular.forEach($scope.consignment.shipments, function(shipment) {
                           value = value + shipment.invoiceAmount;
                        });

                        if(value != 0) {
                            $scope.consignment.value = value;
                        }
                    }

                    $scope.shipConsignment = function() {
                        if(validateConsignment() == true) {
                            var savedShipments = $scope.consignment.shipments;
                            var shipments =[];
                            angular.forEach($scope.consignment.shipments, function(shipment) {
                                var item = {
                                    id: shipment.id
                                };
                                shipments.push(item)
                            });

                            $scope.consignment.shipments = shipments;

                            consignmentFactory.createConsignment($scope.consignment).then(
                                function(data) {
                                    $scope.consignment.id = data.id;
                                    $scope.consignment.shipments = savedShipments;
                                    $rootScope.$broadcast('app.updateNotification');
                                    $scope.shipped = true;
                                    $rootScope.showSuccessMessage("Consignment is shipped successfully. You can now print the consignment.");
                                    $rootScope.$broadcast('broadcast.crm.consignment.shipped');
                                },
                                function(error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    };

                    $scope.printConsignment = function() {
                        var url = "{0}//{1}/api/crm/consignments/{2}/print".
                            format(window.location.protocol, window.location.host,
                            $scope.consignment.id);
                        var printWindow = window.open(url, '',
                            'scrollbars=yes,menubar=no,width=500, resizable=yes,toolbar=no,location=no,status=no');
                    };

                    $scope.printConsignmentShipments = function() {
                        var url = "{0}//{1}/api/crm/consignments/{2}/shipments/print".
                            format(window.location.protocol, window.location.host,
                            $scope.consignment.id);
                        var printWindow = window.open(url, '',
                            'scrollbars=yes,menubar=no,width=500,resizable=yes,toolbar=no,location=no,status=no');
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

                    function validateConsignment() {
                        var valid = true;

                        if($scope.consignment.shipments.length == 0) {
                            $rootScope.showErrorMessage("There are no shipments in this consignment")
                            valid = false;
                        }
                        else if($scope.consignment.shipper == null) {
                            $rootScope.showErrorMessage("A shipper has to be selected")
                            valid = false;
                        }
                        else if($scope.consignment.consignee == null || $scope.consignment.consignee == "") {
                            $rootScope.showErrorMessage("A consignee has to be specified")
                            valid = false;
                        }
                        else if($scope.consignment.shippingAddress.addressText == null || $scope.consignment.shippingAddress.addressText == "") {
                            $rootScope.showErrorMessage("Shipping address is empty")
                            valid = false;
                        }

                        return valid;
                    }

                    function initConsignmentShipments() {
                        angular.forEach($scope.consignment.shipments, function(shipment) {
                            shipment.showDetails = false;
                            shipment.detailsOpen = false;
                        });
                    }

                    function loadShippers() {
                        shipmentFactory.getShippers().then(
                            function(data) {
                                $scope.shippers = data;
                            }
                        );
                    }


                    (function() {
                        loadShippers();
                        initConsignmentShipments();
                        updateConsignmentInfo();
                    })();
                }
            ]
        );
    }
);