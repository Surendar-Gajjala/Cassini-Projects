define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('RequestService', RequestService);

        function RequestService(httpFactory) {
            return {
                createRequest: createRequest,
                updateRequest: updateRequest,
                deleteRequest: deleteRequest,
                getRequest: getRequest,
                getRequests: getRequests,
                getAllRequests: getAllRequests,

                getRequestsByBomInstance: getRequestsByBomInstance,
                getRequestReport: getRequestReport,

                createRequestItems: createRequestItems,
                getRequestItems: getRequestItems,
                getItemsByRequest: getItemsByRequest,
                validateUpnNumber: validateUpnNumber,

                approveRequest: approveRequest,
                rejectRequest: rejectRequest,
                getFinishedRequests: getFinishedRequests,
                acceptRequestItem: acceptRequestItem,
                approveRequestItem: approveRequestItem,
                acceptRequest: acceptRequest,
                rejectRequestItem: rejectRequestItem,
                getRequestReportSummary: getRequestReportSummary,
                getSummaryByRequest: getSummaryByRequest,
                getRequestReportSummaryByInstance: getRequestReportSummaryByInstance,
                updateRequestByNumber: updateRequestByNumber
            };

            function createRequest(request) {
                var url = "api/drdo/requests";
                return httpFactory.post(url, request);
            }

            function updateRequest(request) {
                var url = "api/drdo/requests/" + request.id;
                return httpFactory.put(url, request);
            }

            function deleteRequest(requestId) {
                var url = "api/drdo/requests/" + requestId;
                return httpFactory.delete(url);
            }

            function getRequest(requestId) {
                var url = "api/drdo/requests/" + requestId;
                return httpFactory.get(url);
            }

            function getRequests() {
                var url = "api/drdo/requests";
                return httpFactory.get(url);
            }

            function getAllRequests(pageable, filter) {
                var url = "api/drdo/requests/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&status={1}&notification={2}&issued={3}"
                    .format(filter.searchQuery, filter.status, filter.notification, filter.issued);
                url += "&fromDate={0}&toDate={1}&month={2}"
                    .format(filter.fromDate, filter.toDate, filter.month);
                return httpFactory.get(url);
            }

            function createRequestItems(requestId, requestItems) {
                var url = "api/drdo/requests/" + requestId + "/items";
                return httpFactory.post(url, requestItems);
            }

            function rejectRequestItem(requestId, requestItem) {
                var url = "api/drdo/requests/" + requestId + "/items/" + requestItem.id;
                return httpFactory.put(url, requestItem);
            }

            function getRequestItems(requestId) {
                var url = "api/drdo/requests/" + requestId + "/items";
                return httpFactory.get(url);
            }

            function getItemsByRequest(requestId) {
                var url = "api/drdo/requests/" + requestId + "/requestItems";
                return httpFactory.get(url);
            }

            function approveRequest(request) {
                var url = "api/drdo/requests/approve/" + request.id;
                return httpFactory.put(url, request);
            }

            function acceptRequest(request) {
                var url = "api/drdo/requests/accept/" + request.id;
                return httpFactory.put(url, request);
            }

            function acceptRequestItem(reqItem) {
                var url = "api/drdo/requests/accept/item/" + reqItem.id;
                return httpFactory.put(url, reqItem);
            }

            function approveRequestItem(reqItem) {
                var url = "api/drdo/requests/approve/item/" + reqItem.id;
                return httpFactory.put(url, reqItem);
            }

            function rejectRequest(request) {
                var url = "api/drdo/requests/reject/" + request.id;
                return httpFactory.put(url, request);
            }

            function getFinishedRequests() {
                var url = "api/drdo/requests/finished";
                return httpFactory.get(url);
            }

            function getRequestsByBomInstance(instanceId) {
                var url = "api/drdo/requests/instance/" + instanceId;
                return httpFactory.get(url);
            }

            function getRequestReport(requestId) {
                var url = "api/drdo/requests/" + requestId + "/report";
                return httpFactory.get(url);
            }

            function getRequestReportSummary() {
                var url = "api/drdo/requests/summary";
                return httpFactory.get(url);
            }

            function getRequestReportSummaryByInstance(instanceId) {
                var url = "api/drdo/requests/summary/" + instanceId;
                return httpFactory.get(url);
            }

            function getSummaryByRequest(requestId) {
                var url = "api/drdo/requests/" + requestId + "/summary";
                return httpFactory.get(url);
            }

            function validateUpnNumber(request, bomInstanceItemId, upnNumber) {
                var url = "api/drdo/requests/" + request + "/item/" + bomInstanceItemId + "/validate/upn?upnNumber=" + upnNumber;
                return httpFactory.get(url);
            }

            /*----------------- For Not General Use -------------------*/

            function updateRequestByNumber(requestNumber, partName) {
                var url = "api/drdo/requests/update/" + requestNumber + "?partName=" + partName;
                return httpFactory.get(url);
            }

            /*-----------------------------------------------------------*/
        }
    }
);