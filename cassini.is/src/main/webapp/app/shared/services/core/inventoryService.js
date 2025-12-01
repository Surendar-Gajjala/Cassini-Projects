define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('InventoryService', InventoryService);

        function InventoryService(httpFactory) {
            return {
                getInventoryByProject: getInventoryByProject,
                getInventoryByIds: getInventoryByIds,
                getItemInventory: getItemInventory,
                getItemInventoryHistory: getItemInventoryHistory,
                getAllCustomInventories: getAllCustomInventories,
                getInventoryByItems: getInventoryByItems,
                getInventoryHistoryByItems: getInventoryHistoryByItems
            };

            function getInventoryByProject(projectId, pageable) {
                var url = "api/projects/" + projectId + "/inventory?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getInventoryByIds(projectId, ids) {
                var url = "api/projects/" + projectId + "/inventory/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getItemInventory(projectId, id) {
                var url = "api/projects/" + projectId + "/inventory/" + id;
                return httpFactory.get(url);
            }

            function getItemInventoryHistory(projectId, id) {
                var url = "api/projects/" + projectId + "/inventory/" + id + "/history/";
                return httpFactory.get(url);
            }

            function getAllCustomInventories(projectId) {
                var url = "api/projects/" + projectId + "/inventory/allinvstoredetails";
                return httpFactory.get(url);
            }

            function getInventoryByItems(projectId, ids) {
                var url = "api/projects/" + projectId + "/inventory/items/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getInventoryHistoryByItems(projectId, ids) {
                var url = "api/projects/" + projectId + "/inventory/items/[" + ids + "]/history";
                return httpFactory.get(url);
            }

        }
    }
);