define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('NcrService', NcrService);

        function NcrService(httpFactory) {
            return {
                createNcr: createNcr,
                updateNcr: updateNcr,
                deleteNcr: deleteNcr,
                getNcr: getNcr,
                getAllNcrs: getAllNcrs,
                getMultipleNcrs: getMultipleNcrs,
                getNcrFiles: getNcrFiles,
                createNcrAttribute: createNcrAttribute,
                updateNcrAttribute: updateNcrAttribute,
                createNcrRelatedItem: createNcrRelatedItem,
                deleteNcrRelatedItem: deleteNcrRelatedItem,
                createNcrRelatedItems: createNcrRelatedItems,
                getNcrRelatedItems: getNcrRelatedItems,
                createNcrProblemItem: createNcrProblemItem,
                deleteNcrProblemItem: deleteNcrProblemItem,
                createNcrProblemItems: createNcrProblemItems,
                getNcrProblemItems: getNcrProblemItems,
                getNcrDetailsCount: getNcrDetailsCount,
                updateNcrProblemItem: updateNcrProblemItem,
                updateNcrRelatedItem: updateNcrRelatedItem
            };

            function createNcr(plan) {
                var url = "api/pqm/ncrs";
                return httpFactory.post(url, plan);
            }

            function updateNcr(id, plan) {
                var url = "api/pqm/ncrs/" + id;
                return httpFactory.put(url, plan);
            }

            function deleteNcr(ncrId) {
                var url = "api/pqm/ncrs/" + ncrId;
                return httpFactory.delete(url);
            }

            function getNcr(ncrId) {
                var url = "api/pqm/ncrs/" + ncrId;
                return httpFactory.get(url);
            }

            function getAllNcrs(pageable, filters) {
                var url = "api/pqm/ncrs/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&title={0}&ncrNumber={1}&ncrType={2}&description={3}&searchQuery={4}&qcr={5}&released={6}".
                    format(filters.title, filters.ncrNumber, filters.ncrType, filters.description, filters.searchQuery, filters.qcr, filters.released);
                return httpFactory.get(url);
            }

            function getMultipleNcrs(ncrIds) {
                var url = "api/pqm/ncrs/multiple/[" + ncrIds + "]";
                return httpFactory.get(url);
            }

            function getNcrFiles(ncrId) {
                var url = "api/pqm/ncrs/" + ncrId + "/files";
                return httpFactory.get(url);
            }

            function createNcrAttribute(ncrId, attribute) {
                var url = "api/pqm/ncrs/" + ncrId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateNcrAttribute(ncrId, attribute) {
                var url = "api/pqm/ncrs/" + ncrId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function createNcrRelatedItem(planId, relatedItem) {
                var url = "api/pqm/ncrs/" + planId + "/relatedItems";
                return httpFactory.post(url, relatedItem);
            }

            function updateNcrRelatedItem(planId, relatedItem) {
                var url = "api/pqm/ncrs/" + planId + "/relatedItems/" + relatedItem.id;
                return httpFactory.put(url, relatedItem);
            }

            function deleteNcrRelatedItem(planId, relatedItem) {
                var url = "api/pqm/ncrs/" + planId + "/relatedItems/" + relatedItem;
                return httpFactory.delete(url);
            }

            function createNcrRelatedItems(planId, relatedItem) {
                var url = "api/pqm/ncrs/" + planId + "/relatedItems/multiple";
                return httpFactory.post(url, relatedItem);
            }

            function getNcrRelatedItems(planId) {
                var url = "api/pqm/ncrs/" + planId + "/relatedItems";
                return httpFactory.get(url);
            }

            function createNcrProblemItem(planId, relatedItem) {
                var url = "api/pqm/ncrs/" + planId + "/problemItems";
                return httpFactory.post(url, relatedItem);
            }

            function updateNcrProblemItem(planId, relatedItem) {
                var url = "api/pqm/ncrs/" + planId + "/problemItems/" + relatedItem.id;
                return httpFactory.put(url, relatedItem);
            }

            function deleteNcrProblemItem(planId, afftectedItem) {
                var url = "api/pqm/ncrs/" + planId + "/problemItems/" + afftectedItem;
                return httpFactory.delete(url);
            }

            function createNcrProblemItems(planId, relatedItem) {
                var url = "api/pqm/ncrs/" + planId + "/problemItems/multiple";
                return httpFactory.post(url, relatedItem);
            }

            function getNcrProblemItems(planId) {
                var url = "api/pqm/ncrs/" + planId + "/problemItems";
                return httpFactory.get(url);
            }

            function getNcrDetailsCount(planId) {
                var url = "api/pqm/ncrs/" + planId + "/details/count";
                return httpFactory.get(url);
            }
        }
    }
);