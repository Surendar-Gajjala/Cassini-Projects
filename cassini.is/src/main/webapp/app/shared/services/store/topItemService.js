define(
    [
        'app/shared/services/services.module',
        'app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('TopItemService', TopItemService);

        function TopItemService(httpFactory) {
            return {
                createTopItem: createTopItem,
                updateTopItem: updateTopItem,
                deleteTopItem: deleteTopItem,
                getTopItem: getTopItem,
                getTopItems: getTopItems
            };

            function createTopItem(topItem) {
                var url = "api/is/stores/items";
                return httpFactory.post(url, topItem);
            }

            function updateTopItem(topItem) {
                var url = "api/is/stores/items/" + topItem.id;
                return httpFactory.put(url, topItem);
            }

            function deleteTopItem(ItemId) {
                var url = "api/is/stores/items/" + ItemId;
                return httpFactory.delete(url);
            }

            function getTopItem(topItem) {
                var url = "api/is/stores/items/" + topItem.id;
                return httpFactory.get(url);
            }

            function getTopItems() {
                var url = "api/is/stores/items";
                return httpFactory.get(url);
            }
        }
    }
);