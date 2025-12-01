define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('ItemBomService', ItemBomService);

        function ItemBomService(httpFactory) {
            return {
                getItemBom: getItemBom,
                getBomItem: getBomItem,
                createItemBom: createItemBom,
                createBomItems: createBomItems,
                updateBomItem: updateBomItem,
                updateBomItems: updateBomItems,
                deleteBomItem: deleteBomItem,
                getBom: getBom,
                getBomList: getBomList,
                createBomItem: createBomItem
            };

            function getItemBom(itemId) {
                var url = 'api/pdm/items/' + itemId + "/bom";
                return httpFactory.get(url);
            }

            function getBomItem(itemId, bomItemId) {
                var url = 'api/pdm/items/' + itemId + "/bom/" + bomItemId;
                return httpFactory.get(url);
            }

            function getBomList(itemId, bomItemId) {
                var url = "api/pdm/items/" + itemId + "/bom/[" + bomItemId + "]";
                return httpFactory.get(url);
            }

            function getBom(itemId, bomItemId) {
                var url = 'api/pdm/items/' + itemId + "/bom/item/" + bomItemId;
                return httpFactory.get(url);
            }

            function createItemBom(itemId, bomItem) {
                var url = 'api/pdm/items/' + itemId + "/bom";
                return httpFactory.post(url, bomItem);
            }

            function createBomItem(revisionId, bomItem) {
                var url = 'api/pdm/items/' + revisionId + "/bom/bomItem";
                return httpFactory.post(url, bomItem);
            }

            function createBomItems(itemId, bomItems) {
                var url = 'api/pdm/items/' + itemId + "/bom/multiple";
                return httpFactory.post(url, bomItems);
            }

            function updateBomItem(itemId, bomItem) {
                var url = 'api/pdm/items/' + itemId + "/bom/" + bomItem.id;
                return httpFactory.put(url, bomItem);
            }

            function updateBomItems(itemId, bomItems) {
                var url = 'api/pdm/items/' + itemId + "/bom/multiple";
                return httpFactory.put(url, bomItems);
            }

            function deleteBomItem(itemId, bomItemId) {
                var url = 'api/pdm/items/' + itemId + "/bom/" + bomItemId;
                return httpFactory.delete(url);
            }
        }
    }
);