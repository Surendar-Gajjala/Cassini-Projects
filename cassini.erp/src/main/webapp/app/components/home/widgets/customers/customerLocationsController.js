define(['app/app.modules'
    ],
    function($app) {
        $app.controller('CustomerLocationsController',
            [
                '$scope', '$rootScope', '$state',

                function($scope, $rootScope, $state) {
                    $scope.map = {
                        show: true,
                        control: {},
                        version: "uknown",
                        showTraffic: true,
                        showBicycling: false,
                        showWeather: false,
                        showHeat: false,
                        center: {
                            latitude: 45,
                            longitude: -73
                        },
                        options: {
                            streetViewControl: false,
                            panControl: false,
                            maxZoom: 20,
                            minZoom: 3
                        },
                        zoom: 3,
                        dragging: false,
                        bounds: {},
                        events: {}
                    }
                }
            ]
        );
    }
);