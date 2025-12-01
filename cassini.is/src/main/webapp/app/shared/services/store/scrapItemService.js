define(
    [
        'app/shared/services/services.module',
        'app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('ScrapItemService', ScrapItemService);

        function ScrapItemService(httpFactory) {
            return {
                createScrapReqItem: createScrapReqItem,
                updateScrapItem: updateScrapItem,
                getScrapItems: getScrapItems,
                getAllScrapItems: getAllScrapItems,

            };

            function createScrapReqItem(scrapReqItems) {
                var url = "api/is/stores/scrapItem";
                return httpFactory.post(url, scrapReqItems);
            }


            function updateScrapItem(scrapItem) {
                var url = "api/is/stores/scrapItem/" + scrapItem.id;
                return httpFactory.put(url, scrapItem);
            }

            function getAllScrapItems() {
                var url = "api/is/stores/scrapItem";
                return httpFactory.get(url);
            }

            function getScrapItems(scrapId) {
                var url = "api/is/stores/scrapItem/" + scrapId;
                return httpFactory.get(url);
            }
        }
    }
);