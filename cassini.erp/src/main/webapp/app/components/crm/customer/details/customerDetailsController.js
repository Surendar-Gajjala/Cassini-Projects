define(['app/app.modules',
        'app/components/crm/customer/customerFactory',
        'app/components/crm/customer/details/tabs/customerOrdersController',
        'app/components/crm/customer/details/tabs/customerReturnsController',
        'app/components/crm/customer/details/tabs/customerReportsController',
        'app/components/crm/salesrep/salesRepFactory',
        'app/components/crm/salesregion/salesRegionFactory',
        'app/components/crm/salesrep/details/tabs/fieldReportsController',
        'app/components/common/commonFactory',
        'app/components/crm/order/shipment/shipmentFactory'
    ],
    function($app) {
        $app.controller('CustomerDetailsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams', '$cookies',
                'customerFactory', 'commonFactory', 'shipmentFactory', 'salesRepFactory', 'salesRegionFactory', 'uiGmapGoogleMapApi',

                function($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies,
                         customerFactory, commonFactory, shipmentFactory, salesRepFactory, salesRegionFactory, GoogleMapApi) {

                    $rootScope.iconClass = "fa flaticon-office";
                    $rootScope.viewTitle = "Customer Details";

                    $rootScope.editMode = false;
                    $rootScope.ordersScope = null;
                    $rootScope.returnsScope = null;
                    $rootScope.reportsScope = {};
                    $scope.currentTab = "orders";
                    $scope.salesReps = [];
                    $scope.salesRegions = [];

                    $rootScope.tabs = {
                        orders: {
                            template: "app/components/crm/customer/details/tabs/customerOrdersView.jsp",
                            active: true
                        },
                        returns: {
                            template: "app/components/crm/customer/details/tabs/customerReturnsView.jsp",
                            active: false
                        },
                        reports: {
                            template: "app/components/crm/customer/details/tabs/customerReportsView.jsp",
                            active: false
                        }
                    };

                    $scope.customer = $rootScope.newCustomer;
                    $rootScope.customer = null;

                    $scope.geoLocation = {
                        latitude: null,
                        longitude: null
                    };

                    $scope.map = {
                        show: true,
                        control: {},
                        version: "unknown",
                        showTraffic: true,
                        showBicycling: false,
                        showWeather: false,
                        showHeat: false,
                        center: {
                            latitude:  17.8760325,
                            longitude: 79.2727298
                        },
                        options: {
                            streetViewControl: false,
                            panControl: false,
                            maxZoom: 20,
                            minZoom: 3,
                            mapTypeControl: false
                        },
                        zoom: 6,
                        dragging: false,
                        bounds: {},
                        events: {}
                    };

                    $scope.editInformation = {
                        address: null,
                        contactPerson: {
                            name: null,
                            phone: null
                        }
                    };

                    $scope.marker = {
                        id: 1,
                        showWindow: false
                    };

                    GoogleMapApi.then(function(maps) {
                        $scope.api = maps;
                    });


                    $rootScope.edit = function() {
                        $rootScope.editMode = true;
                    };

                    $rootScope.saveEdits = function() {
                        if($scope.geoLocation.latitude != null && $scope.geoLocation.longitude != undefined &&
                            $scope.geoLocation.longitude != null && $scope.geoLocation.longitude != undefined) {
                            $scope.customer.geoLocation= $scope.geoLocation;
                        }

                        if($scope.customer.contactPerson != null && $scope.customer.contactPerson.firstName != null) {
                            $scope.customer.contactPerson.personType = commonFactory.getPersonTypeByName("Customer");
                        }

                        customerFactory.updateCustomer($scope.customer).then(
                            function(data) {
                                $scope.customer = data;
                                $rootScope.customer = data;

                                $rootScope.editMode = false;

                                $timeout(function() {
                                    $scope.showCustomerOnMap();
                                }, 500);
                            }
                        );
                    };

                    $rootScope.newReturn = function() {
                        $rootScope.lastSelectedCustomer = $scope.customer;
                        $state.go('app.crm.returns.new',{source: 'customer.details'});
                    };

                    $scope.loadCustomer = function() {
                        customerFactory.getCustomer($stateParams.customerId).then(
                            function(data) {
                                $scope.customer = data;
                                $rootScope.customer = data;
                                if($scope.customer.customerAddresses == undefined ||
                                        $scope.customer.customerAddresses.length == 0) {
                                    $scope.customer.customerAddresses = [
                                        {
                                            addressType: commonFactory.getAddressTypeByName("Office"),
                                            addressText: "",
                                            district: $scope.customer.salesRegion.district,
                                            state: $scope.customer.salesRegion.state,
                                            country: $scope.customer.salesRegion.country,
                                            pincode: ""
                                        }
                                    ]
                                }

                                $timeout(function() {
                                    $scope.showCustomerOnMap();
                                }, 500);

                                $rootScope.$broadcast("customerLoaded");
                            }
                        )
                    };

                    $scope.showCustomerOnMap = function() {
                        if($scope.customer.geoLocation != null) {
                            $scope.marker.latitude = $scope.customer.geoLocation.latitude;
                            $scope.marker.longitude = $scope.customer.geoLocation.longitude;

                            var bounds = new $scope.api.LatLngBounds();
                            var latlang = new $scope.api.LatLng($scope.marker.latitude, $scope.marker.longitude);
                            bounds.extend(latlang);

                            $scope.map.center = {
                                latitude: bounds.getCenter().lat(),
                                longitude: bounds.getCenter().lng()
                            };
                            var origCenter = {latitude: $scope.map.center.latitude, longitude: $scope.map.center.longitude};
                            $scope.map.zoom = 16;
                            $scope.map.control.refresh(origCenter);

                            $scope.geoLocation = $scope.customer.geoLocation;
                        }
                    };

                    $rootScope.$on("windowResize", function() {
                        $scope.api.event.trigger($scope.map.control.getGMap(), "resize");
                        $scope.showCustomerOnMap();
                    });

                    $rootScope.createReport = function() {
                        $rootScope.reportsScope.showNew = true;
                        $rootScope.tabs.orders.active = false;
                        $rootScope.tabs.returns.active = false;
                        $rootScope.tabs.reports.active = true;
                    };

                    function loadSalesReps() {
                        var pageable = {
                            page: 1,
                            size: 1000,
                            sort: {
                                label: "firstName",
                                field: "firstName",
                                order: "asc"
                            }
                        };
                        salesRepFactory.getSalesReps(pageable).then(
                            function(data) {
                                $scope.salesReps = data.content
                            }
                        )
                    }

                    function loadSalesRegions() {
                       var criteria = {
                            name: "",
                            district: "",
                            state: "",
                            country: "",
                            salesRep: ""
                        };

                        var pageable = {
                            page: 1,
                            size: 1000000
                        };

                        salesRegionFactory.getSalesRegions(criteria, pageable).then(
                            function(data) {
                                $scope.salesRegions = data.content;
                            }
                        )
                    }

                    function loading(){
                        if($rootScope.lastselectedcustomer!=null &&$rootScope.lastselectedcustomer!=undefined){
                            $scope.customerReturn = angular.copy($rootScope.customer);
                            $rootScope.lastselectedcustomer = null;
                        }

                    }

                    $scope.blacklistCustomer = function(customer) {
                        customer.blacklisted = true;
                        customerFactory.updateCustomer(customer).then(
                            function(data) {
                                $rootScope.showSuccessMessage("Customer has been blacklisted");
                            }
                        )
                    };

                    $scope.removeBlacklist = function(customer) {
                        customer.blacklisted = false;
                        customerFactory.updateCustomer(customer).then(
                            function(data) {
                                $rootScope.showSuccessMessage("Customer has been removed from blacklist");
                            }
                        )
                    };


                    (function() {
                        $scope.$on('$viewContentLoaded', function(){
                            $rootScope.setToolbarTemplate('customer-details-tb');

                            $scope.loadCustomer();
                            loading();
                            loadSalesReps();
                            loadSalesRegions();
                        });

                    })();
                }
            ]
        );
    }
);