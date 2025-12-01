/**
 * Created by Nageshreddy on 10-12-2018.
 */
define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('AllocationService', AllocationService);

        function AllocationService($q, httpFactory) {
            return {
                getBomAllocation: getBomAllocation,
                getBomChildrenAllocation: getBomChildrenAllocation,
                planInventory: planInventory,
                getBomInstanceAllocation: getBomInstanceAllocation,
                getBomInstanceChildrenAllocation: getBomInstanceChildrenAllocation,
                exportPlanningReport: exportPlanningReport,
                reSetPlanInventory: reSetPlanInventory,
                planSelectedMissiles: planSelectedMissiles,
                reSetPlanForSelectedMissiles: reSetPlanForSelectedMissiles,
                loadBomChildrenAllocation: loadBomChildrenAllocation,
                loadBomChildrenWithOutSecAllocation: loadBomChildrenWithOutSecAllocation
            };

            function reSetPlanForSelectedMissiles(missileIds) {
                var url = "api/drdo/allocation/missiles/reset/[" + missileIds + "]";
                return httpFactory.get(url);
            }

            function planSelectedMissiles(missileIds) {
                var url = "api/drdo/allocation/missiles/plan/[" + missileIds + "]";
                return httpFactory.get(url);
            }

            function planInventory(items) {
                var url = "api/drdo/allocation/plan";
                return httpFactory.post(url, items);
            }

            function reSetPlanInventory(items) {
                var url = "api/drdo/allocation/resetPlan";
                return httpFactory.post(url, items);
            }

            function getBomAllocation(bomId) {
                var url = "api/drdo/allocation/" + bomId + "/bom";
                return httpFactory.get(url, bomId);
            }

            function getBomChildrenAllocation(bomId, bomItemId, missileIds, selectedMissileIds, workCenter, searchBomText, expandAll) {
                var url = "api/drdo/allocation/" + bomId + "/bom/" + bomItemId + "/children/[" + missileIds + "]?workCenter=" + workCenter + "&searchBomText=" + searchBomText + "&expandAll=" + expandAll;
                return httpFactory.post(url, selectedMissileIds);
            }

            function loadBomChildrenAllocation(bomId, bomItemId, missileIds, selectedMissileIds, expandAll) {
                var url = "api/drdo/allocation/load/" + bomId + "/bom/" + bomItemId + "/children/[" + missileIds + "]?expandAll=" + expandAll;
                return httpFactory.post(url, selectedMissileIds);
            }


            function loadBomChildrenWithOutSecAllocation(bomId, missileIds, selectedMissileIds, expandAll) {
                var url = "api/drdo/allocation/load/" + bomId + "/bom/children/[" + missileIds + "]?expandAll=" + expandAll;
                return httpFactory.post(url, selectedMissileIds);
            }

            function getBomInstanceAllocation(bomId) {
                var url = "api/drdo/allocation/" + bomId + "/bom/instance";
                return httpFactory.get(url, bomId);
            }

            function getBomInstanceChildrenAllocation(bomId, bomItemId) {
                var url = "api/drdo/allocation/" + bomId + "/bom/instance/" + bomItemId + "/children";
                return httpFactory.get(url, bomId);
            }

            function exportPlanningReport(fileName, fileType, columns, boms, itemId) {
                var url = 'api/drdo/allocation/' + itemId + '/' + fileName + '/' + fileType + '/[' + columns + ']';
                return httpFactory.post(url, boms);
            }

        }
    }
);