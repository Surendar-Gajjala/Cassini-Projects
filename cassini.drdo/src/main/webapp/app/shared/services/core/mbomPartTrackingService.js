
define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('MBomPartTrackingService', MBomPartTrackingService);

        function MBomPartTrackingService(httpFactory) {
            return {
                createMBomPartTracking: createMBomPartTracking,
                updateMBomPartTracking: updateMBomPartTracking,
                deleteMBomPartTracking: deleteMBomPartTracking
            };

            var url = 'api/drdo/mbomPartTracking';

            function createMBomPartTracking(mbomPartTracking) {
                return httpFactory.post(url, mbomPartTracking);
            }

            function updateMBomPartTracking(mbomPartTracking) {
                return httpFactory.put(url + "/" + area.id, mbomPartTracking);
            }

            function deleteMBomPartTracking(mbomPartTracking) {
                return httpFactory.delete(url + "/" + mbomPartTracking.id);
            }

        }
    }
);