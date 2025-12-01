define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('InventoryService', InventoryService);

        function InventoryService($q, httpFactory) {
            return {
                getBomInventory: getBomInventory,
                getBomChildrenInventory: getBomChildrenInventory,

                getBomInstanceInventory: getBomInstanceInventory,
                getBomInstanceChildrenInventory: getBomInstanceChildrenInventory,
                getInventoryReportByBom: getInventoryReportByBom,
                getInventoryReportBySection: getInventoryReportBySection,
                getInventoryReportByInstance: getInventoryReportByInstance,
                getInventoryReportByInstanceSection: getInventoryReportByInstanceSection,
                searchBomInventory: searchBomInventory,
                searchBomInstanceInventory: searchBomInstanceInventory
            };

            function getBomInventory(bomId) {
                var url = "api/drdo/inventory/" + bomId + "/bom";
                return httpFactory.get(url, bomId);
            }

            function searchBomInventory(bomId, searchText) {
                var url = "api/drdo/inventory/" + bomId + "/bom/search?searchText=" + searchText;
                return httpFactory.get(url, bomId);
            }

            function searchBomInstanceInventory(bomId, searchText) {
                var url = "api/drdo/inventory/" + bomId + "/bomInstance/search?searchText=" + searchText;
                return httpFactory.get(url, bomId);
            }

            function getBomChildrenInventory(bomId, bomItemId) {
                var url = "api/drdo/inventory/" + bomId + "/bom/" + bomItemId + "/children";
                return httpFactory.get(url, bomId);
            }

            function getBomInstanceInventory(bomId) {
                var url = "api/drdo/inventory/" + bomId + "/bom/instance";
                return httpFactory.get(url, bomId);
            }

            function getBomInstanceChildrenInventory(bomId, bomItemId) {
                var url = "api/drdo/inventory/" + bomId + "/bom/instance/" + bomItemId + "/children";
                return httpFactory.get(url, bomId);
            }

            function getInventoryReportByBom(bomId) {
                var url = "api/drdo/inventory/" + bomId + "/bom/report";
                return httpFactory.get(url, bomId);
            }

            function getInventoryReportBySection(bomId, sectionId) {
                var url = "api/drdo/inventory/" + bomId + "/bom/" + sectionId + "/section/report";
                return httpFactory.get(url, bomId);
            }

            function getInventoryReportByInstance(bomId) {
                var url = "api/drdo/inventory/" + bomId + "/instance/report";
                return httpFactory.get(url, bomId);
            }

            function getInventoryReportByInstanceSection(bomId, sectionId) {
                var url = "api/drdo/inventory/" + bomId + "/instance/" + sectionId + "/section/report";
                return httpFactory.get(url, bomId);
            }

        }
    }
);