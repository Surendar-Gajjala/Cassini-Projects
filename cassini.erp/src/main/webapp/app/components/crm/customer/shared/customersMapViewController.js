define(['app/app.modules',
        'app/shared/directives/commonDirectives'
    ],
    function($app) {
        $app.controller('CustomersMapViewController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'uiGmapGoogleMapApi',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies,
                         GoogleMapApi) {

                    $scope.selectedMarker = null;
                    $scope.markersMap = new Hashtable();
                    $scope.selectedCustomer = null;
                    $scope.api = null;

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
                        events: {},
                        markers: []
                    };

                    $scope.markers = [];

                    GoogleMapApi.then(function(maps) {
                        $scope.map.markers = $scope.markers;
                        $scope.api = maps;
                    });

                    $scope.loadMarkers = function() {
                        $scope.markers = [];
                        var index = 0;
                        angular.forEach($scope.customers.content, function(customer) {
                            var geo = customer.geoLocation;

                            if(geo != null) {
                                index = index + 1;
                                var marker = {
                                    id: index,
                                    latitude: geo.latitude,
                                    longitude: geo.longitude,
                                    showWindow: false
                                };
                                marker.closeClick = function () {
                                    marker.showWindow = false;
                                };
                                customer.marker = marker;
                                $scope.markers.push(marker);

                                $scope.markersMap.put(marker, customer);
                            }
                        });

                        $scope.map.markers = $scope.markers;

                        $timeout(function() {
                            zoomFit();
                        }, 300)

                    };

                    function zoomFit() {
                        if($scope.api != null) {
                            var bounds = new $scope.api.LatLngBounds();
                            angular.forEach($scope.markers, function (marker) {
                                var latlang = new $scope.api.LatLng(marker.latitude, marker.longitude);
                                bounds.extend(latlang);
                            });

                            $scope.map.center = {
                                latitude: bounds.getCenter().lat(),
                                longitude: bounds.getCenter().lng()
                            };
                            var origCenter = {latitude: $scope.map.center.latitude, longitude: $scope.map.center.longitude};
                            $scope.map.control.getGMap().fitBounds(bounds);
                            $scope.map.control.refresh(origCenter);
                        }
                    }

                    $scope.markerClicked = function(marker) {
                        if ($scope.selectedMarker != null) {
                            $scope.selectedMarker.showWindow = false;
                            $scope.$apply();
                        }
                        marker.showWindow = true;
                        $scope.selectedMarker = marker;

                        $scope.scrollSelectionIntoView(marker);
                        $scope.$apply();
                    };

                    $scope.scrollSelectionIntoView = function(marker) {
                        var cust = $scope.getCustomerFromMarker(marker);
                        if(cust != null) {
                            var index = $scope.customers.content.indexOf(cust);
                            if(index != -1) {
                                var id = "#custOnMap" + index;
                                //$(id).parent().scrollTop($(id).position().top);
                                $(id).parent().animate({scrollTop: $(id).parent().scrollTop() + $(id).position().top},'slow');
                            }
                        }
                    };


                    $scope.getCustomerFromMarker = function(marker) {
                        return $scope.markersMap.get(marker);
                    };

                    $scope.zoomSelectCustomer = function(customer) {
                        if($scope.selectedMarker != null) {
                            $scope.selectedMarker.show = false;
                            $scope.selectedMarker.showWindow = false;
                        }
                        if(customer.marker != null && customer.marker != undefined) {
                            customer.marker.showWindow = true;
                            $scope.selectedMarker = customer.marker;

                            $scope.map.center = {
                                latitude: customer.marker.latitude,
                                longitude: customer.marker.longitude
                            };
                        }
                    };

                    $scope.getContactPhone = function(customer) {
                        var phone = null;
                        if(customer.contactPerson != null) {
                            phone = customer.contactPerson.phoneMobile;

                            if(phone == null) {
                                phone = customer.contactPerson.phoneOffice;
                            }
                        }

                        if(phone == null) {
                            phone = customer.officePhone;
                        }

                        return phone;
                    };

                    $rootScope.$on('customersLoaded', function(customers) {
                        $scope.customer = customers;
                        $scope.loadMarkers();
                    });
                }
            ]
        );
    }
);
