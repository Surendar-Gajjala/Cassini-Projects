define(['app/app.modules',
        'app/components/crm/customer/customerFactory',
        'app/components/common/commonFactory',
        'app/components/crm/customer/search/infoWindowController'
    ],
    function($app) {
        $app.controller('GeoSearchController',
            [
                '$scope', '$rootScope', '$compile', '$timeout', '$interval', '$state', '$stateParams', '$cookies',
                'customerFactory', 'commonFactory', 'uiGmapGoogleMapApi', 'uiGmapIsReady',

                function($scope, $rootScope, $compile, $timeout, $interval, $state, $stateParams, $cookies,
                         customerFactory, commonFactory, GoogleMapApi, uiGmapIsReady) {
                    $scope.api = null;

                    $scope.loadingCustomerData = false;
                    $scope.selectedMarker = null;
                    $scope.templateLnkFunc = null;
                    $scope.infoWindowTemplate = "";

                    $scope.clickMarker = {
                        id: 1000000
                    };

                    $scope.infoWindow = null;

                    $scope.mode = "box"; //radius or box

                    $scope.circle = {
                        stroke: {
                            //color: '#08B21F',
                            color: '#428BCA',
                            weight: 2,
                            opacity: 1
                        },
                        fill: {
                            //color: '#08B21F',
                            color: '#428BCA',
                            opacity: 0.2
                        },
                        radius: 1,
                        radiusInMeters: 1000,
                        control: {},
                        visible: true
                    };

                    $scope.customer = [];

                    $scope.map = {
                        show: true,
                        control: {},
                        version: "unknown",
                        showTraffic: true,
                        showBicycling: false,
                        showWeather: false,
                        showHeat: false,
                        center: {
                            latitude:  17.404159,
                            longitude: 78.465724
                        },
                        options: {
                            streetViewControl: false,
                            panControl: false,
                            maxZoom: 20,
                            minZoom: 3,
                            mapTypeControl: false
                        },
                        zoom: 10,
                        dragging: false,
                        bounds: {},
                        events: {},
                        markers: [],
                        clusterMarkers: true,
                        clusterOptions: {
                        title: 'Cluster Markers',
                            gridSize: 60,
                            ignoreHidden: true,
                            minimumClusterSize: 10
                        }
                    };

                    $scope.markers = [];

                    $scope.circleEvents = {
                        'center_changed': function() {
                            if($scope.mode == 'radius') {
                                $scope.markers = [];
                                zoomAndCenter();
                                $scope.geoSearch();
                            }
                        },
                        'radius_changed': function() {
                            if($scope.mode == 'radius') {
                                var r = $scope.circle.control.getCircle().getRadius() / 1000;
                                $scope.circle.radius = +r.toFixed(2);
                                $scope.markers = [];
                                zoomAndCenter();
                                $scope.geoSearch();
                            }
                        },
                        'drag': function() {
                        }
                    };

                    GoogleMapApi.then(function(maps) {
                        $scope.map.markers = $scope.markers;
                        $scope.api = maps;

                        waitAndAddListener();
                    });

                    function waitAndAddListener() {
                        if(!$scope.map.control.hasOwnProperty('getGMap')) {
                            $timeout(function() {
                                waitAndAddListener();
                            }, 1000);
                        }
                        else {
                            $scope.api.event.addListener($scope.map.control.getGMap(), 'click', function(event) {
                                if($scope.mode == 'radius') {
                                    $scope.circle.visible = true;
                                    $scope.markers = [];
                                    $scope.clickMarker = {
                                        id: 1000000,
                                        latitude: event.latLng.lat(),
                                        longitude: event.latLng.lng()
                                    };
                                    zoomAndCenter();
                                    $scope.$apply();
                                    $scope.geoSearch();
                                }

                                $scope.infoWindow.close();
                                $scope.selectedMarker = {};
                            });

                            $scope.api.event.addListener($scope.map.control.getGMap(), 'zoom_changed', function(event) {
                                if($scope.mode == 'box') {
                                    $scope.markers = [];
                                    $scope.geoSearch();
                                }
                            });
                            $scope.api.event.addListener($scope.map.control.getGMap(), 'center_changed', function(event) {
                                if($scope.mode == 'box') {
                                    $scope.markers = [];
                                    $scope.geoSearch();
                                }
                            });

                            if($scope.mode == 'box') {
                                $timeout(function() {
                                    $scope.geoSearch();
                                }, 500);
                            }

                            $scope.infoWindow = new $scope.api.InfoWindow();
                        }
                    }

                    function zoomAndCenter() {
                        if($scope.api != null) {
                            var center = new $scope.api.LatLng($scope.clickMarker.latitude, $scope.clickMarker.longitude);
                            var circle = new $scope.api.Circle();
                            circle.setRadius($scope.circle.radius * 1000);
                            circle.setCenter(center);

                            $scope.map.control.getGMap().fitBounds(circle.getBounds());
                            $scope.map.control.refresh($scope.clickMarker);
                        }
                    }

                    $scope.setMode = function(mode) {
                        if($scope.mode != mode) {
                            $scope.mode = mode;
                            $scope.markers = [];
                            $scope.map.markers = $scope.markers;

                            if($scope.mode == 'box') {
                                $scope.circle.visible = false;
                                $scope.geoSearch();
                            }
                            else {
                                $scope.clickMarker = {
                                    id: 1000000,
                                    latitude: $scope.map.center.latitude,
                                    longitude: $scope.map.center.longitude
                                };
                                $scope.circle.visible = true;
                                $scope.geoSearch();
                            }
                        }
                    };

                    $scope.markerClicked = function(marker) {
                        if($scope.selectedMarker == marker) {
                            $scope.infoWindow.close();
                            $scope.selectedMarker = {};
                        }
                        else {
                            $scope.loadingCustomerData = true;
                            $scope.selectedMarker = marker;

                            $scope.infoWindow.close();

                            if ($scope.templateLnkFunc == null) {
                                $scope.templateLnkFunc = $compile($scope.infoWindowTemplate);
                            }

                            var compiled = $scope.templateLnkFunc($scope);
                            $scope.infoWindow.setContent(compiled[0]);
                        }
                    };

                    $scope.showInfoWindow = function() {
                        $scope.infoWindow.open($scope.map.control.getGMap() , $scope.selectedMarker);
                    };

                    $scope.$watch('circle.radius', function() {
                        if($scope.api != null) {
                            $scope.circle.radiusInMeters = $scope.circle.radius * 1000;
                        }
                    });

                    $rootScope.$on("windowResize", function() {
                        $scope.api.event.trigger($scope.map.control.getGMap(), "resize");
                    });

                    $scope.geoSearch = function() {
                        if($scope.mode == 'radius') {
                            if ($scope.clickMarker != null && $scope.circle.radius != null) {
                                customerFactory.radiusSearch($scope.clickMarker.latitude,
                                    $scope.clickMarker.longitude, $scope.circle.radius).then(
                                    function (data) {
                                        $scope.customers = data;
                                        createMarkers();
                                    }
                                )
                            }
                        }
                        else if($scope.mode == 'box') {
                            var bounds = $scope.map.control.getGMap().getBounds();
                            var ne = bounds.getNorthEast();
                            var sw = bounds.getSouthWest();
                            customerFactory.boxSearch(ne, sw).then(
                                function (data) {
                                    $scope.customers = data;
                                    createMarkers();
                                }
                            )
                        }
                    };

                    function createMarkers() {
                        var circleCenter = new $scope.api.LatLng( $scope.clickMarker.latitude, $scope.clickMarker.longitude );
                        var radius = $scope.circle.radius * 1000;

                        angular.forEach($scope.customers, function(customer) {
                            var customerLoc = new $scope.api.LatLng( customer.latitude, customer.longitude );
                            var distance = $scope.api.geometry.spherical.computeDistanceBetween(circleCenter, customerLoc);

                            if(($scope.mode == 'radius' && distance < radius) ||
                                $scope.mode == 'box') {
                                var marker = {
                                    id: customer.id,
                                    latitude: customer.latitude,
                                    longitude: customer.longitude
                                };
                                customer.marker = marker;
                                $scope.markers.push(marker);
                            }
                        });

                        angular.forEach($scope.markers, function(marker) {

                        });

                        $scope.map.markers = $scope.markers;

                    }

                    (function() {
                        commonFactory.getTemplate('app/components/crm/customer/search/infoWindow.jsp').then(
                            function(data) {
                                $scope.infoWindowTemplate = data;
                            }
                        );
                    })();
                }
            ]
        );
    }
);