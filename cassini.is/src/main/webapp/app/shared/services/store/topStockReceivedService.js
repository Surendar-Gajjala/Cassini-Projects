define(
    [
        'app/shared/services/services.module',
        'app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('TopStockReceivedService', TopStockReceivedService);

        function TopStockReceivedService(httpFactory) {
            return {
                createTopStockReceive: createTopStockReceive,
                updateTopStockReceive: updateTopStockReceive,
                deleteTopStockReceive: deleteTopStockReceive,
                getStockReceive: getStockReceive,
                getAllStockReceives: getAllStockReceives,
                getStockReceivedByStoreId: getStockReceivedByStoreId,
                getStockReceivedQtyByItemNumberAndBoq: getStockReceivedQtyByItemNumberAndBoq,
                getStockReceivedByFilter: getStockReceivedByFilter,
                getStockReceiveItems: getStockReceiveItems,
                getPagedStockReceiveItems: getPagedStockReceiveItems,
                getPagedStockReceives: getPagedStockReceives,
                freeSearch: freeSearch,
                saveReceiveTypeAttributes: saveReceiveTypeAttributes,
                getStockReceivesByType: getStockReceivesByType,
                getStockReceivedByAttributes: getStockReceivedByAttributes,
                exportStockReceiveItemsReport: exportStockReceiveItemsReport,
                getStockReceivedItemsByAttributes: getStockReceivedItemsByAttributes,
                getMinimumDate: getMinimumDate,
                getReceiveTypeAttributes: getReceiveTypeAttributes
            };

            function createTopStockReceive(storeId, topStockReceived) {
                var url = "api/is/stores/" + storeId + "/stockReceive";
                return httpFactory.post(url, topStockReceived);
            }

            function updateTopStockReceive(storeId, topStockReceived) {
                var url = "api/is/stores/" + storeId + "/stockReceive/" + topStockReceived.id;
                return httpFactory.put(url, topStockReceived);
            }

            function deleteTopStockReceive(storeId, StockReceivedId) {
                var url = "api/is/stores/" + storeId + "/stockReceive/" + StockReceivedId;
                return httpFactory.delete(url);
            }

            function getStockReceive(storeId, id) {
                var url = "api/is/stores/" + storeId + "/stockReceive/" + id;
                return httpFactory.get(url);
            }

            function getAllStockReceives(storeId, pageable) {
                var url = "api/is/stores/" + storeId + "/stockReceive/pageable?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function getStockReceiveItems(storeId, id) {
                var url = "api/is/stores/" + storeId + "/stockReceive/" + id + "/items";
                return httpFactory.get(url);
            }

            function getPagedStockReceiveItems(storeId, id, pageable) {
                var url = "api/is/stores/" + storeId + "/stockReceive/" + id + "/items/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function getStockReceivedByStoreId(storeId) {
                var url = "api/is/stores/" + storeId + "/stockReceive";
                return httpFactory.get(url);
            }

            function getPagedStockReceives(storeId, pageable) {
                var url = "api/is/stores/" + storeId + "/stockReceive/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url)
            }

            function getStockReceivedQtyByItemNumberAndBoq(storeId, itemNumber, boqId) {
                var url = "api/is/stores/" + storeId + "/stockReceive/itemNumber/" + itemNumber + "/boq/" + boqId;
                return httpFactory.get(url);
            }

            function getStockReceivedByFilter(storeId, pageable, criteria) {
                var url = "api/is/stores/" + storeId + "/stockReceive/freeSearch";
                url += "?store={0}&itemType={1}".
                    format(criteria.store, criteria.itemType);

                url += "&page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getStockReceivedByAttributes(storeId, pageable, criteria) {
                var url = "api/is/stores/" + storeId + "/stockReceive/by/attributes/pageable";

                url += "?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.post(url, criteria);
            }

            function getStockReceivedItemsByAttributes(storeId, criteria) {
                var url = "api/is/stores/" + storeId + "/stockReceive/by/attributes";
                return httpFactory.post(url, criteria);
            }

            function freeSearch(storeId, pageable, freetext) {
                var url = "api/is/stores/" + storeId + "/stockReceive/freeSearch?page={0}&size={1}".
                        format(pageable.page, pageable.size);

                url += "&searchQuery={0}".
                    format(freetext);
                return httpFactory.get(url);
            }

            function getStockReceivesByType(storeId, receiveTypeId, pageable) {
                var url = "api/is/stores/" + storeId + "/stockReceive/type/" + receiveTypeId + "/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function saveReceiveTypeAttributes(storeId, stockReceiveId, attributes) {
                var url = "api/is/stores/" + storeId + "/stockReceive/" + stockReceiveId + "/attributes";
                return httpFactory.post(url, attributes);
            }

            function exportStockReceiveItemsReport(storeId, criteria) {
                var url = "api/is/stores/" + storeId + "/stockReceive/export/type/" + 'excel';
                return httpFactory.post(url, criteria);
            }

            function getMinimumDate(storeId) {
                var url = "api/is/stores/" + storeId + "/stockReceive/minimumdate";
                return httpFactory.get(url);
            }

            function getReceiveTypeAttributes(storeId, stockReceiveId, receiveTypeId) {
                var url = "api/is/stores/" + storeId + "/stockReceive/" + stockReceiveId + "/receiveType/" + receiveTypeId + "/attributes";
                return httpFactory.get(url);
            }
        }
    }
);