define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('WorkRequestService', WorkRequestService);

        function WorkRequestService(httpFactory) {
            return {
                createWorkRequest: createWorkRequest,
                updateWorkRequest: updateWorkRequest,
                getWorkRequest: getWorkRequest,
                getWorkRequests: getWorkRequests,
                getAllWorkRequests: getAllWorkRequests,
                deleteWorkRequest: deleteWorkRequest,
                getMultipleWorkRequests: getMultipleWorkRequests,
                uploadWorkRequestFiles: uploadWorkRequestFiles,
                getWorkRequestWorkOrders: getWorkRequestWorkOrders,
                getPendingWorkRequests: getPendingWorkRequests
            };

            function createWorkRequest(workRequest) {
                var url = "api/mro/workrequests";
                return httpFactory.post(url, workRequest)
            }

            function updateWorkRequest(workRequest) {
                var url = "api/mro/workrequests/" + workRequest.id;
                return httpFactory.put(url, workRequest);
            }

            function getWorkRequest(id) {
                var url = "api/mro/workrequests/" + id;
                return httpFactory.get(url)
            }

            function getWorkRequestWorkOrders(id) {
                var url = "api/mro/workrequests/" + id + "/workorders";
                return httpFactory.get(url)
            }

            function deleteWorkRequest(workRequest) {
                var url = "api/mro/workrequests/" + workRequest;
                return httpFactory.delete(url);
            }

            function getAllWorkRequests(pageable, filters) {
                var url = "api/mro/workrequests/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&type={1}&name={2}&searchQuery={3}".
                    format(filters.number, filters.type, filters.name, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getWorkRequests() {
                var url = "api/mro/workrequests";
                return httpFactory.get(url);
            }

            function getPendingWorkRequests() {
                var url = "api/mro/workrequests/pending";
                return httpFactory.get(url);
            }

            function getMultipleWorkRequests(workRequestIds) {
                var url = "api/mro/workrequests/multiple/[" + workRequestIds + "]";
                return httpFactory.get(url);
            }

            function uploadWorkRequestFiles(id, files) {
                var url = "api/mro/workrequests/" + id + "/uploadFiles";
                return httpFactory.uploadMultiple(url, files);
            }
        }
    }
);