define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('TripService', TripService);

        function TripService(httpFactory) {
            return {
                createTrip: createTrip,
                updateTrip: updateTrip,
                deleteTrip: deleteTrip,
                getTripById: getTripById,
                getAllTrips: getAllTrips,
                getPagedTrips: getPagedTrips,
                getTrips: getTrips,
                getGps: getGps,
                loadJsonFile: loadJsonFile,
                getElevation: getElevation,
                getDocuments: getDocuments
            };

            function createTrip(trip) {
                var url = "api/trips";
                return httpFactory.post(url, trip);
            }

            function updateTrip(trip) {
                var url = "api/trips/" + trip.id;
                return httpFactory.put(url, trip);
            }

            function deleteTrip(tripId) {
                var url = "/api/trips/" + tripId;
                return httpFactory.delete(url);
            }

            function getTripById(tripId) {
                var url = "api/trips/" + tripId;
                return httpFactory.get(url);
            }

            function getAllTrips() {
                var url = "api/trips";
                return httpFactory.get(url);
            }

            function getPagedTrips(pageable) {
                var url = "api/trips/pageable";
                url += "?page{0}&size{1}&sort{2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getTrips() {
                var url = 'app/assets/videos/trips.json';
                return httpFactory.get(url);
            }

            function getGps(tripId) {
                var url = 'app/assets/videos/{0}.gps'.format(tripId);
                return httpFactory.get(url);
            }

            function loadJsonFile(fname) {
                return httpFactory.get(fname);
            }

            function getElevation(latlng) {
                var url = "https://maps.googleapis.com/maps/api/elevation/json?callback=JSON_CALLBACK&locations={0},{1}&key=AIzaSyBlHwvmLJy0OZFI6d7C0shCZVe2tg3zu2g".
                        format(latlng.lat(), latlng.lng());
                return httpFactory.jsonp(url);
            }

            function getDocuments() {
                var url = "app/assets/videos/trip1.docs";
                return httpFactory.get(url);
            }

        }
    }
);