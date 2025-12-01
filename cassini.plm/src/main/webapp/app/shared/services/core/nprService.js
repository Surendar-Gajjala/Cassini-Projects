define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('NprService', NprService);

        function NprService(httpFactory) {
            return {
                createNpr: createNpr,
                updateNpr: updateNpr,
                getNpr: getNpr,
                deleteNpr: deleteNpr,
                getAllNprs: getAllNprs,
                getMultipleNprs: getMultipleNprs,

                createNprItem: createNprItem,
                updateNprItem: updateNprItem,
                deleteNprItem: deleteNprItem,
                getNprItems: getNprItems,
                getNprTabCounts: getNprTabCounts,
                submitNpr: submitNpr,
                approveNpr: approveNpr,
                rejectNpr: rejectNpr,
                assignItemNumber: assignItemNumber,
                attachNprWorkflow: attachNprWorkflow

            };

            function createNpr(npr) {
                var url = "api/plm/nprs";
                return httpFactory.post(url, npr)
            }

            function updateNpr(npr) {
                var url = "api/plm/nprs/" + npr.id;
                return httpFactory.put(url, npr);
            }

            function getNpr(id) {
                var url = "api/plm/nprs/" + id;
                return httpFactory.get(url)
            }

            function deleteNpr(npr) {
                var url = "api/plm/nprs/" + npr;
                return httpFactory.delete(url);
            }

            function getAllNprs(pageable, filters) {
                var url = "api/plm/nprs/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&description={1}&reasonForRequest={2}&notes={3}&searchQuery={4}".
                    format(filters.number, filters.description, filters.reasonForRequest, filters.notes, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getMultipleNprs(nprIds) {
                var url = "api/plm/nprs/multiple/[" + nprIds + "]";
                return httpFactory.get(url);
            }

            function createNprItem(nprId, nprItem) {
                var url = "api/plm/nprs/" + nprId + "/items";
                return httpFactory.post(url, nprItem)
            }

            function updateNprItem(nprId, nprItem) {
                var url = "api/plm/nprs/" + nprId + "/items/" + nprItem.id;
                return httpFactory.put(url, nprItem)
            }

            function deleteNprItem(nprId, nprItemId) {
                var url = "api/plm/nprs/" + nprId + "/items/" + nprItemId;
                return httpFactory.delete(url)
            }

            function getNprItems(nprId) {
                var url = "api/plm/nprs/" + nprId + "/items";
                return httpFactory.get(url);
            }

            function getNprTabCounts(nprId) {
                var url = "api/plm/nprs/" + nprId + "/counts";
                return httpFactory.get(url);
            }

            function submitNpr(nprId) {
                var url = "api/plm/nprs/" + nprId + "/submit";
                return httpFactory.get(url);
            }

            function approveNpr(nprId) {
                var url = "api/plm/nprs/" + nprId + "/approve";
                return httpFactory.get(url);
            }

            function rejectNpr(npr) {
                var url = "api/plm/nprs/" + npr.id + "/reject";
                return httpFactory.put(url, npr);
            }

            function assignItemNumber(nprId, itemId) {
                var url = "api/plm/nprs/" + nprId + "/items/" + itemId + "/assignnumber";
                return httpFactory.get(url);
            }

            function attachNprWorkflow(nprId, wfId) {
                var url = "api/plm/nprs/" + nprId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }
        }
    }
);