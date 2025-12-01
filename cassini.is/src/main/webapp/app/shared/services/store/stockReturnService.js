/**
 * Created by swapna on 05/12/18.
 */
define(
    [
        'app/shared/services/services.module',
        'app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('StockReturnService', StockReturnService);

        function StockReturnService(httpFactory) {
            return {
                createStockReturn: createStockReturn,
                getStockReturn: getStockReturn,
                getStoreStockReturns: getStoreStockReturns,
                updateStockReturn:updateStockReturn,
                deleteStockReturn:deleteStockReturn,
                creteStockReturnItems: creteStockReturnItems,
                getStockReturnItems: getStockReturnItems,
                printStockReturnChallan: printStockReturnChallan,
                getPageableStockReturns: getPageableStockReturns,
                freeSearch: freeSearch
            };

            function createStockReturn(storeId, stockReturn) {
                var url = "api/is/stores/" + storeId + "/stockReturn";
                return httpFactory.post(url, stockReturn);
            }

            function getStockReturn(storeId, stockReturnId) {
                var url = "api/is/stores/" + storeId + "/stockReturn/" + stockReturnId;
                return httpFactory.get(url);
            }

            function getStoreStockReturns(storeId, pageable) {
                var url = "api/is/stores/" + storeId + "/stockReturn/pageable?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function getPageableStockReturns(storeId, pageable) {
                var url = "api/is/stores/" + storeId + "/stockReturn?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function updateStockReturn(storeId, stockReturn) {
                var url = "api/is/stores/" + storeId + "/stockReturn";
                return httpFactory.put(url, stockReturn)
            }

            function deleteStockReturn(storeId, stockReturnId) {
                var url = "api/is/stores/" + storeId + "/stockReturn/" + stockReturnId;
                return httpFactory.delete(url)
            }

            function creteStockReturnItems(storeId, stockReturnId, stockReturnItems) {
                var url = "api/is/stores/" + storeId + "/stockReturn" + stockReturnId + "/stockReturnItems";
                return httpFactory.post(url, stockReturnItems);
            }

            function getStockReturnItems(storeId, stockReturnId, pageable) {
                var url = "api/is/stores/" + storeId + "/stockReturn/" + stockReturnId + "/stockReturnItems/pageable?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function printStockReturnChallan(storeId, stockReturnId) {
                var url = "api/is/stores/" + storeId + "/stockReturn/" + stockReturnId + "/printStockReturnChallan";
                return httpFactory.get(url);
            }

            function freeSearch(storeId, pageable, freetext) {
                var url = "api/is/stores/" + storeId + "/stockReturn/freesearch?page={0}&size={1}".
                    format(pageable.page, pageable.size);

                url += "&searchQuery={0}".
                    format(freetext);
                return httpFactory.get(url);
            }

        }
    }
);