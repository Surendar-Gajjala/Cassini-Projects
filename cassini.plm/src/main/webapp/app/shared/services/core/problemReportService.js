define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('ProblemReportService', ProblemReportService);

        function ProblemReportService(httpFactory) {
            return {
                createProblemReport: createProblemReport,
                updateProblemReport: updateProblemReport,
                deleteProblemReport: deleteProblemReport,
                getProblemReport: getProblemReport,
                getAllProblemReports: getAllProblemReports,
                getMultipleProblemReports: getMultipleProblemReports,
                getProblemReportFiles: getProblemReportFiles,
                createPrAttribute: createPrAttribute,
                updatePrAttribute: updatePrAttribute,
                deletePrRelatedItem: deletePrRelatedItem,
                deletePrAffectedItem: deletePrAffectedItem,
                createPrRelatedItem: createPrRelatedItem,
                createPrRelatedItems: createPrRelatedItems,
                getPrRelatedItems: getPrRelatedItems,
                createPrAffectedItem: createPrAffectedItem,
                createPrAffectedItems: createPrAffectedItems,
                getPrAffectedItems: getPrAffectedItems,
                getDetailsCount: getDetailsCount,
                updatePrAffectedItem: updatePrAffectedItem,
                getPRCount: getPRCount
            };

            function createProblemReport(plan) {
                var url = "api/pqm/problemreports";
                return httpFactory.post(url, plan);
            }

            function updateProblemReport(id, plan) {
                var url = "api/pqm/problemreports/" + id;
                return httpFactory.put(url, plan);
            }

            function deleteProblemReport(planId) {
                var url = "api/pqm/problemreports/" + planId;
                return httpFactory.delete(url);
            }

            function getProblemReport(planId) {
                var url = "api/pqm/problemreports/" + planId;
                return httpFactory.get(url);
            }

            function getMultipleProblemReports(planIds) {
                var url = "api/pqm/problemreports/multiple/[" + planIds + "]";
                return httpFactory.get(url);
            }

            function getProblemReportFiles(planId) {
                var url = "api/pqm/problemreports/" + planId + "/files";
                return httpFactory.get(url);
            }

            function getAllProblemReports(pageable, filters) {
                var url = "api/pqm/problemreports/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&prNumber={0}&prType={1}&problem={2}&searchQuery={3}&product={4}&qcr={5}&released={6}&ecr={7}".
                    format(filters.prNumber, filters.prType, filters.problem, filters.searchQuery, filters.product, filters.qcr, filters.released, filters.ecr);
                return httpFactory.get(url);
            }

            function createPrAttribute(planId, attribute) {
                var url = "api/pqm/problemreports/" + planId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updatePrAttribute(planId, attribute) {
                var url = "api/pqm/problemreports/" + planId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function createPrRelatedItem(planId, relatedItem) {
                var url = "api/pqm/problemreports/" + planId + "/relatedItems";
                return httpFactory.post(url, relatedItem);
            }

            function deletePrRelatedItem(planId, relatedItem) {
                var url = "api/pqm/problemreports/" + planId + "/relatedItems/" + relatedItem;
                return httpFactory.delete(url);
            }

            function createPrRelatedItems(planId, relatedItem) {
                var url = "api/pqm/problemreports/" + planId + "/relatedItems/multiple";
                return httpFactory.post(url, relatedItem);
            }

            function getPrRelatedItems(planId) {
                var url = "api/pqm/problemreports/" + planId + "/relatedItems";
                return httpFactory.get(url);
            }

            function createPrAffectedItem(planId, relatedItem) {
                var url = "api/pqm/problemreports/" + planId + "/problemItems";
                return httpFactory.post(url, relatedItem);
            }

            function updatePrAffectedItem(planId, relatedItem) {
                var url = "api/pqm/problemreports/" + planId + "/problemItems/" + relatedItem.id;
                return httpFactory.put(url, relatedItem);
            }

            function deletePrAffectedItem(planId, afftectedItem) {
                var url = "api/pqm/problemreports/" + planId + "/problemItems/" + afftectedItem;
                return httpFactory.delete(url);
            }

            function createPrAffectedItems(planId, relatedItem) {
                var url = "api/pqm/problemreports/" + planId + "/problemItems/multiple";
                return httpFactory.post(url, relatedItem);
            }

            function getPrAffectedItems(planId) {
                var url = "api/pqm/problemreports/" + planId + "/problemItems";
                return httpFactory.get(url);
            }

            function getDetailsCount(planId) {
                var url = "api/pqm/problemreports/" + planId + "/details/count";
                return httpFactory.get(url);
            }

            function getPRCount() {
                var url = "api/pqm/problemreports/count";
                return httpFactory.get(url);
            }
        }
    }
);