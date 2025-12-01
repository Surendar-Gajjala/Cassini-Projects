define(['app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'],
    function (mdoule) {
        mdoule.factory('PpapService', PpapService);

        function PpapService(httpFactory) {
            return {
                createPpap: createPpap,
                updatePpap: updatePpap,
                deletePpap: deletePpap,
                getAllPpaps: getAllPpaps,
                getPpap: getPpap,
                getPPAPTabCounts: getPPAPTabCounts,
                promotePpap: promotePpap,
                demotePpap: demotePpap,
                createPPAPAttribute: createPPAPAttribute,
                updatePPAPAttribute: updatePPAPAttribute
            };

            function createPpap(obj) {
                var url = "api/pqm/ppap";
                return httpFactory.post(url, obj);
            }

            function updatePpap(ppap) {
                var url = "api/pqm/ppap/" + ppap.id;
                return httpFactory.put(url, ppap);
            }

            function deletePpap(ppapId) {
                var url = "api/pqm/ppap/" + ppapId;
                return httpFactory.delete(url);
            }

            function getPpap(ppapId) {
                var url = "api/pqm/ppap/" + ppapId;
                return httpFactory.get(url);
            }

            function getAllPpaps(pageable, filters) {
                var url = "api/pqm/ppap/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&name={1}&description={2}&searchQuery={3}".
                    format(filters.number, filters.name, filters.description, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getPPAPTabCounts(id) {
                var url = "api/pqm/ppap/" + id + "/counts";
                return httpFactory.get(url);
            }

            function promotePpap(id, ppap) {
                var url = "api/pqm/ppap/" + id + "/promote";
                return httpFactory.put(url, ppap);
            }

            function demotePpap(id, ppap) {
                var url = "api/pqm/ppap/" + id + "/demote";
                return httpFactory.put(url, ppap);
            }

            function createPPAPAttribute(id, attribute) {
                var url = "api/pqm/ppap/" + id + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updatePPAPAttribute(id, attribute) {
                var url = "api/pqm/ppap/" + id + "/attributes";
                return httpFactory.put(url, attribute);
            }
        }
    }
);
