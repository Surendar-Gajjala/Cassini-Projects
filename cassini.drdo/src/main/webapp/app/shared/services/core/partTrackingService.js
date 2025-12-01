/**
 * Created by Nageshreddy on 08-10-2018.
 */
define([
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('PartTrackingService', PartTrackingService);
        function PartTrackingService(httpFactory) {
            var url = "api/drdo/itemPartTrackings";
            return {
                getPartTrackingItemById: getPartTrackingItemById,
                getPartTrackingItems: getPartTrackingItems,
                deletePartTrackingItems: deletePartTrackingItems,
                updatePartTrackingItem: updatePartTrackingItem,
                createPartTrackingItem: createPartTrackingItem,
                createPartTrackingItems: createPartTrackingItems,
                getPartTrackingItemsByBomId: getPartTrackingItemsByBomId,
                createPartTracking: createPartTracking,
                updatePartTracking: updatePartTracking,
                getPartTrackingByCheckIds: getPartTrackingByCheckIds,
                clearancePartTrackingReport: clearancePartTrackingReport,
                getScannedUpnsByPartTrackingStep: getScannedUpnsByPartTrackingStep,
                saveScannedUpnsByPartTrackingStep: saveScannedUpnsByPartTrackingStep
            };

            function getPartTrackingItemById(id) {
                return httpFactory.get(url + "/" + id);
            }

            function getPartTrackingItems() {
                return httpFactory.get(url);
            }

            function deletePartTrackingItems() {
                return httpFactory.delete(url);
            }

            function updatePartTrackingItem(partTracking) {
                return httpFactory.put(url, partTracking);
            }

            function createPartTrackingItem(partTracking) {
                return httpFactory.post(url, partTracking);
            }

            function createPartTrackingItems(partTrackings) {
                return httpFactory.post(url + "/multiple", partTrackings);
            }

            function getPartTrackingItemsByBomId(id) {
                return httpFactory.get(url + "/bomId/" + id);
            }

            function createPartTracking(partTracking) {
                return httpFactory.post(url + "/partTracking", partTracking);
            }

            function updatePartTracking(partTracking) {
                return httpFactory.put(url + "/partTracking/" + partTracking.id, partTracking);
            }

            function getPartTrackingByCheckIds(ids) {
                return httpFactory.get(url + "/partTracking/byCheckId/[" + ids + "]");
            }

            function clearancePartTrackingReport(bomId) {
                return httpFactory.get(url + "/partTracking/report/" + bomId);
            }

            function saveScannedUpnsByPartTrackingStep(upns) {
                return httpFactory.post(url + "/scannedUpns/", upns);
            }

            function getScannedUpnsByPartTrackingStep(step) {
                return httpFactory.get(url + "/scannedUpns/" + step);
            }
        }
    }
);
