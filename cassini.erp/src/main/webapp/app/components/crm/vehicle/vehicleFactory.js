define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('vehicleFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        getVehicles: function() {
                            var dfd = $q.defer(),
                                url = "api/crm/vehicles";
                            return httpFactory.get(url);
                        },
                        createVehicle: function(vehicle) {
                            var dfd = $q.defer(),
                                url = "api/crm/vehicles";
                            return httpFactory.post(url, vehicle);
                        },
                        getVehicle: function(vehicleId) {
                            var dfd = $q.defer(),
                                url = "api/crm/vehicles/" + vehicleId;
                            return httpFactory.get(url);
                        },
                        updateVehicle: function(vehicle) {
                            var dfd = $q.defer(),
                                url = "api/crm/vehicles/" + vehicle.id;
                            return httpFactory.put(url, vehicle);
                        }
                    }
                }
            ]
        )
    }
);