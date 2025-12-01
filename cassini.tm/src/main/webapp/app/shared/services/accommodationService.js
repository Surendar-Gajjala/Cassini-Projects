define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('AccommodationService', AccommodationService);

        function AccommodationService(httpFactory) {
            return {
                createAccommodation: createAccommodation,
                getAccommodation: getAccommodation,
                getAccommodationById: getAccommodationById,
                updateAccommodation: updateAccommodation,
                deleteAccommodation: deleteAccommodation,
                getAllAccommodations: getAllAccommodations,
                getMultipleAccommodations: getMultipleAccommodations,

                createSuit: createSuit,
                createBed: createBed,
                updateBed: updateBed,
                getSuit: getSuit,
                getBedsBySuit:getBedsBySuit,
                updateSuit: updateSuit,
                deleteSuit: deleteSuit,
                deleteBed:deleteBed,
                getAllSuits: getAllSuits,
                getSuitById: getSuitById,
                getSuitsByAccommodation: getSuitsByAccommodation,
                freeTextSearch:freeTextSearch,

                getCounts: getCounts,
                getSuiteCounts: getSuiteCounts,
                getSuiteBedsOccupied: getSuiteBedsOccupied,
                getSuiteBedsAvailable: getSuiteBedsAvailable,
                getBedCounts: getBedCounts,
                getOccupiedBeds: getOccupiedBeds,
                getAvailableBeds: getAvailableBeds,
                getBedByAssignedTo: getBedByAssignedTo
            };

            function freeTextSearch(criteria, pageable) {
                var url = "api/accommodations/freesearch?page={0}&size={1}".format(pageable.page, pageable.size);
                url += "&searchQuery={0}".
                    format(criteria.searchQuery);
                return httpFactory.get(url);
            }

            function getMultipleAccommodations(ids) {
                var url = 'api/accommodations/multiple/' + ids;
                return httpFactory.get(url);
            }

            function getAllAccommodations(pageable) {
                var url = 'api/accommodations?page={0}&size={1}'.format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function getAccommodationById(accId) {
                var url = "api/accommodations/" + accId;
                return httpFactory.get(url);
            }

            function createAccommodation(accomediation) {
                var url = 'api/accommodations';
                return httpFactory.post(url, accomediation);
            }

            function updateAccommodation(accommodations) {
                var url = "api/accommodations/" + accommodations.id;
                return httpFactory.put(url,accommodations );
            }

            function deleteAccommodation(accommodationId) {
                var url = "api/accommodations/" + accommodationId;
                return httpFactory.delete(url);
            }

            function getAccommodation(accommodationId) {
                var url = "api/accommodations/" + accommodationId;
                return httpFactory.get(url);
            }

            function createSuit(accommodationId,suit) {
                var url = "api/accommodations/" + accommodationId + "/suits";
                return httpFactory.post(url, suit);
            }

            function getBedsBySuit(accommodationId ,suitId) {
                var url = "api/accommodations/" + accommodationId + "/suits/" + suitId +"/beds";
                return httpFactory.get(url);
            }

            function createBed(accommodationId ,suitId ,bed) {
                var url = "api/accommodations/" + accommodationId + "/suits/" + suitId +"/beds";
                return httpFactory.post(url,bed);
            }

            function updateBed(accommodationId ,suitId ,bed) {
                var url = "api/accommodations/" + accommodationId + "/suits/" + suitId +"/beds/" + bed.bedId;
                return httpFactory.put(url,bed);
            }

            function getSuitsByAccommodation(accommodationId) {
                var url = "api/accommodations/" + accommodationId + "/suits";
                return httpFactory.get(url);
            }


            function getSuit(suit, accommodationId, suitId) {
                var url = "api/accommodations/" + accommodationId + '/suits' + suitId;
                return httpFactory.get(url, suit);
            }

            function getSuitById(accommodationId, suitId) {
                var url = "api/accommodations/" + accommodationId + "/suits/" +suitId;
                return httpFactory.get(url);
            }

            function updateSuit(suit, accommodationId, suitId) {
                var url = "api/accommodations/" + accommodationId / suit + suitId;
                return httpFactory.put(url, suit);
            }

            function deleteSuit(accommodationId, suitId) {
                var url = "api/accommodations/" + accommodationId +"/suits/" + suitId;
                return httpFactory.delete(url);
            }

            function deleteBed(suitId,bedId) {
                var url = "api/accommodations/"+ suitId +"/beds/"+ bedId;
                return httpFactory.delete(url);
            }

            function getAllSuits() {
                var url = "api/accommodations/suits/all";
                return httpFactory.get(url);
            }

            function getCounts() {
                var url = "api/accommodations/counts";
                return httpFactory.get(url);
            }

            function getSuiteCounts(suiteId) {
                var url = "api/accommodations/suits/" + suiteId + "/counts";
                return httpFactory.get(url);
            }

            function getSuiteBedsOccupied(suiteId) {
                var url = "api/accommodations/suits/" + suiteId + "/occupied";
                return httpFactory.get(url);
            }

            function getSuiteBedsAvailable(suiteId) {
                var url = "api/accommodations/suits/" + suiteId + "/available";
                return httpFactory.get(url);
            }

            function getBedCounts() {
                var url = "api/accommodations/suits/beds/counts";
                return httpFactory.get(url);
            }

            function getOccupiedBeds() {
                var url = "api/accommodations/suits/beds/occupied";
                return httpFactory.get(url);
            }

            function getAvailableBeds() {
                var url = "api/accommodations/suits/beds/available";
                return httpFactory.get(url);
            }

            function getBedByAssignedTo(personId) {
                var url = "api/accommodations/suits/beds/" + personId;
                return httpFactory.get(url);
            }
        }
    }
)
;