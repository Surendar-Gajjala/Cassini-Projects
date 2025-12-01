define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('LifecycleService', LifecycleService);

        function LifecycleService($q, httpFactory) {
            var baseUrl = "api/plm/lifecycles";

            return {
                createLifecycle: createLifecycle,
                getLifecycles: getLifecycles,
                getLifecycle: getLifecycle,
                updateLifecycle: updateLifecycle,
                deleteLifecycle: deleteLifecycle,

                createPhase: createPhase,
                updatePhase: updatePhase,
                getPhases: getPhases,
                getPhase: getPhase,
                getAllPhases: getAllPhases,
                deletePhase: deletePhase
            };

            function createLifecycle(lifecycle) {
                return httpFactory.post(baseUrl, lifecycle);
            }

            function getLifecycles() {
                return httpFactory.get(baseUrl);
            }

            function getLifecycle(id) {
                var url = baseUrl + "/" + id;
                return httpFactory.get(url);
            }

            function updateLifecycle(lifecycle) {
                var url = baseUrl + "/" + lifecycle.id;
                return httpFactory.put(url, lifecycle);
            }

            function deleteLifecycle(lifecycle) {
                var url = baseUrl + "/" + lifecycle.id;
                return httpFactory.delete(url);
            }

            function createPhase(lifecycle, phase) {
                var url = baseUrl + "/{0}/phases".format(lifecycle.id);
                return httpFactory.post(url, phase)
            }

            function updatePhase(lifecycle, phase) {
                var url = baseUrl + "/{0}/phases/{1}".format(lifecycle.id, phase.id);
                return httpFactory.put(url, phase)
            }

            function getPhases(lifecycle) {
                var url = baseUrl + "/{0}/phases".format(lifecycle.id);
                return httpFactory.get(url)
            }

            function getPhase(lifecycle, phaseId) {
                var url = baseUrl + "/{0}/phases/{1}".format(lifecycle.id, phaseId);
                return httpFactory.get(url)
            }

            function deletePhase(lifecycle, phase) {
                var url = baseUrl + "/{0}/phases/{1}".format(lifecycle.id, phase.id);
                return httpFactory.delete(url)
            }

            function getAllPhases() {
                var url = baseUrl + "/allPhases";
                return httpFactory.get(url)
            }

        }
    }
);