define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('DispatchService', DispatchService);

        function DispatchService(httpFactory) {
            return {
                createDispatch: createDispatch,
                updateDispatch: updateDispatch,
                deleteDispatch: deleteDispatch,
                getDispatch: getDispatch,
                getDispatches: getDispatches,
                getAllDispatches: getAllDispatches,
                getItemsToDispatch: getItemsToDispatch,
                getAllNotifications: getAllNotifications
            };

            function createDispatch(dispatch) {
                var url = "api/drdo/dispatch";
                return httpFactory.post(url, dispatch);
            }

            function updateDispatch(dispatch) {
                var url = "api/drdo/dispatch/" + dispatch.id;
                return httpFactory.put(url, dispatch);
            }

            function deleteDispatch(dispatch) {
                var url = "api/drdo/dispatch/" + dispatch.id;
                return httpFactory.delete(url);
            }

            function getDispatch(dispatchId) {
                var url = "api/drdo/dispatch/" + dispatchId;
                return httpFactory.get(url);
            }

            function getDispatches(dispatch) {
                var url = "api/drdo/dispatch";
                return httpFactory.get(url);
            }

            function getAllDispatches(pageable, filter) {
                var url = "api/drdo/dispatch/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&status={1}&notification={2}".format(filter.searchQuery, filter.status, filter.notification);
                url += "&fromDate={0}&toDate={1}&month={2}".format(filter.fromDate, filter.toDate, filter.month);
                return httpFactory.get(url);
            }

            function getItemsToDispatch(bomId, type) {
                var url = "api/drdo/dispatch/" + bomId + "/itemsToDispatch/" + type;
                return httpFactory.get(url);
            }

            function getAllNotifications(filters) {
                var url = "api/drdo/dispatch/notifications?adminPermission={0}&storeApprove={1}&ssqagApprove={2}&bdlApprove={3}&casApprove={4}&gatePassView={5}&newRequest={6}&bdlQcApprove={7}&bdlPpcReceive={8}&versity={9}&versityApprove={10}&versityQc={11}&versityPpc={12}"
                    .format(filters.adminPermission, filters.storeApprove, filters.ssqagApprove, filters.bdlApprove, filters.casApprove, filters.gatePassView,
                    filters.newRequest, filters.bdlQcApprove, filters.bdlPpcReceive, filters.versity, filters.versityApprove, filters.versityQc, filters.versityPpc);

                return httpFactory.get(url);
            }
        }
    }
);