/**
 * Created by Nageshreddy on 02-01-2019.
 */
define([
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('FailureListsItemService', FailureListsItemService);
        function FailureListsItemService(httpFactory) {
            var url = "api/drdo/itemfailurelists";
            return {
                getFailureListItemById: getFailureListItemById,
                getFailureListItems: getFailureListItems,
                deleteFailureListItems: deleteFailureListItems,
                updateFailureListItem: updateFailureListItem,
                createFailureListItem: createFailureListItem,
                createFailureListItems: createFailureListItems,
                getFailureListItemsByBomId: getFailureListItemsByBomId,
                createFailureList: createFailureList,
                updateFailureList: updateFailureList,
                getFailureListByCheckIds: getFailureListByCheckIds,
                clearanceFailureListReport: clearanceFailureListReport
            };

            function getFailureListItemById(id) {
                return httpFactory.get(url + "/" + id);
            }

            function getFailureListItems() {
                return httpFactory.get(url);
            }

            function deleteFailureListItems() {
                return httpFactory.delete(url);
            }

            function updateFailureListItem(failureList) {
                return httpFactory.put(url, failureList);
            }

            function createFailureListItem(failureList) {
                return httpFactory.post(url, failureList);
            }

            function createFailureListItems(failureLists) {
                return httpFactory.post(url + "/multiple", failureLists);
            }

            function getFailureListItemsByBomId(id) {
                return httpFactory.get(url + "/bomId/" + id);
            }

            function createFailureList(failureList) {
                return httpFactory.post(url + "/faillist", failureList);
            }

            function updateFailureList(failureList) {
                return httpFactory.put(url + "/faillist/" + failureList.id, failureList);
            }

            function getFailureListByCheckIds(ids) {
                return httpFactory.get(url + "/faillist/byCheckId/[" + ids + "]");
            }

            function clearanceFailureListReport(bomId) {
                return httpFactory.get(url + "/faillist/report/" + bomId);
            }
        }
    }
);
