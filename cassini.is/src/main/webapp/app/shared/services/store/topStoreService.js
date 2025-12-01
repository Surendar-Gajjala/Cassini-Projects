define(
    [
        'app/shared/services/services.module',
        'app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('TopStoreService', TopStoreService);

        function TopStoreService(httpFactory) {
            return {
                createTopStore: createTopStore,
                updateTopStore: updateTopStore,
                deleteTopStore: deleteTopStore,
                getTopStore: getTopStore,
                getTopStores: getTopStores,
                getAllTopStores: getAllTopStores,
                getPageableTopStores: getPageableTopStores,
                updateTopStock: updateTopStock,
                getStockMovementByStore: getStockMovementByStore,
                getInventoryByStore: getInventoryByStore,
                getQuantityByStoreAndItem: getQuantityByStoreAndItem,
                getIssuedQuantityByStoreAndItem: getIssuedQuantityByStoreAndItem,
                getInventoryByStoreAndItem: getInventoryByStoreAndItem,
                getStoreByName: getStoreByName,
                freeTextSearch: freeTextSearch,
                getTotalQuantityByItem: getTotalQuantityByItem,
                getStockReceivedQuantities: getStockReceivedQuantities,
                getInventoryByMultipleStores: getInventoryByMultipleStores,
                getIssuedQuantityByItem: getIssuedQuantityByItem,
                storeInventoryFreeTextSearchWithoutProject: storeInventoryFreeTextSearchWithoutProject,
                storeInventoryFreeTextSearchWithProject: storeInventoryFreeTextSearchWithProject,
                getStoreInventoryByStoreIdAndFilters: getStoreInventoryByStoreIdAndFilters,
                getStockMovementByStoreAndFreeTextSearch: getStockMovementByStoreAndFreeTextSearch,
                getReceivedItemProperties: getReceivedItemProperties,
                getStoresByIds: getStoresByIds,
                getStoreReferences: getStoreReferences,
                getStoreInventory: getStoreInventory,
                searchStores: searchStores,
                getReportByDates: getReportByDates,
                exportStoreReport: exportStoreReport,
                printReceiveChallan: printReceiveChallan,
                printScrapChallan: printScrapChallan,
                printRoadChallan: printRoadChallan,
                printIssueChallan: printIssueChallan,
                printIndentChallan: printIndentChallan,
                printPurchaseOrderChallan: printPurchaseOrderChallan,
                importStoreItems: importStoreItems,
                downloadStoreImportFileFormat: downloadStoreImportFileFormat
            };

            function getStockMovementByStoreAndFreeTextSearch(storeId, pageable, freeText) {
                var url = "api/is/stores/stockMovement/" + storeId + "/freeTextSearch?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(freeText);
                return httpFactory.get(url);
            }

            function getStoreInventoryByStoreIdAndFilters(storeId, criteria, pageable) {
                var url = "api/is/stores/" + storeId + "/filters";
                url += "?itemNumber={0}&itemName={1}&itemType={2}&description={3}&boqName={4}&units={5}&receivedQuantity={6}&stockOnHand={7}&issuedQuantity={8}".
                    format(criteria.itemNumber, criteria.itemName, criteria.itemType,
                    criteria.description, criteria.boqName, criteria.units, criteria.receivedQuantity, criteria.stockOnHand, criteria.issuedQuantity);

                url += "&page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function storeInventoryFreeTextSearchWithoutProject(storeId, pageable, freeText) {
                var url = "api/is/stores/" + storeId + "/freeTextSearch?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                url += "&searchQuery={0}".
                    format(freeText);
                return httpFactory.get(url);
            }

            function storeInventoryFreeTextSearchWithProject(storeId, projectId, pageable, freeText) {
                var url = "api/is/stores/" + storeId + "/project/" + projectId + "/freeTextSearch?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                url += "&searchQuery={0}".
                    format(freeText);
                return httpFactory.get(url);
            }

            function createTopStore(topStore) {
                var url = "api/is/stores";
                return httpFactory.post(url, topStore);
            }

            function updateTopStore(topStore) {
                var url = "api/is/stores/" + topStore.id;
                return httpFactory.put(url, topStore);
            }

            function deleteTopStore(StoreId) {
                var url = "api/is/stores/" + StoreId;
                return httpFactory.delete(url);
            }

            function getTopStore(topStoreId) {
                var url = "api/is/stores/" + topStoreId;
                return httpFactory.get(url);
            }

            function getTopStores() {
                var url = "api/is/stores";
                return httpFactory.get(url);
            }

            function getPageableTopStores(pageable) {
             var url = "api/is/stores/pageable?page={0}&size={1}&sort={2}".
             format(pageable.page, pageable.size, pageable.sort.field);
             return httpFactory.get(url);
             }

            function getAllTopStores(criteria, pageable) {
                var url = "api/is/stores/filters";
                url += "?storeName={0}&description={1}&locationName={2}&createdBy={3}&createdOn={4}".
                    format(criteria.storeName, criteria.description, criteria.locationName,
                    criteria.createdBy, criteria.createdOn);

                url += "&page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function updateTopStock(stock, storeId, stockType) {
                var url = "api/is/stores/" + storeId + "/" + stockType;
                return httpFactory.post(url, stock);
            }

            function getStockMovementByStore(storeId, movementType) {
                var url = "api/is/stores/" + storeId + "/stockMovement/" + movementType;
                return httpFactory.get(url);
            }

            function getInventoryByStore(storeId) {
                var url = "api/is/stores/" + storeId + "/storeInventory";
                return httpFactory.get(url);
            }

            function getStoreInventory(pageable, storeId) {
                var url = "api/is/stores/" + storeId + "/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getInventoryByStoreAndItem(storeId, itemId) {
                var url = "api/is/stores/" + storeId + "/storeInventory/" + itemId;
                return httpFactory.get(url);
            }

            function getQuantityByStoreAndItem(storeId, itemId) {
                var url = "api/is/stores/" + storeId + "/stockReceived/" + itemId;
                return httpFactory.get(url);
            }

            function getIssuedQuantityByStoreAndItem(storeId, itemId) {
                var url = "api/is/stores/" + storeId + "/stockIssued/" + itemId;
                return httpFactory.get(url);
            }

            function getIssuedQuantityByItem(itemId) {
                var url = "api/is/stores/stockIssued/" + itemId;
                return httpFactory.get(url);
            }

            function getStoreByName(storeName) {
                var url = "api/is/stores/storeName/" + storeName;
                return httpFactory.get(url);
            }

            function freeTextSearch(pageable, criteria) {
                var url = "api/is/stores/search?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(criteria.searchQuery);
                return httpFactory.get(url);
            }

            //stockReceived Items
            function getTotalQuantityByItem(itemId) {
                var url = "api/is/stores/itemQuantity/" + itemId;
                return httpFactory.get(url);
            }

            function getStockReceivedQuantities(itemIds) {
                var url = "api/is/stores/items/[" + itemIds + "]/stockReceived";
                return httpFactory.get(url);
            }

            function getInventoryByMultipleStores(storeIds, itemIds) {
                var url = "api/is/stores/[" + storeIds + "]" + "/inventory/[" + itemIds + "]";
                return httpFactory.get(url);
            }

            function getReceivedItemProperties(objectType) {
                var url = "api/is/stores/receivedAttributes/" + objectType;
                return httpFactory.get(url);
            }

            function getStoresByIds(ids) {
                var url = "api/is/stores/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function searchStores(filters) {
                var url = "api/is/stores/searchAll?searchQuery={0}".format(filters.searchQuery);
                return httpFactory.get(url);
            }


            function getReportByDates(id, startDate, endDate) {
                var url = "api/is/stores/report?id={0}&fromDate={1}&toDate={2}"
                    .format(id, startDate, endDate);
                return httpFactory.get(url);
            }

            function exportStoreReport() {
                var url = "api/is/stores//" + 'excel' + "/report";
                return httpFactory.get(url);
            }

            function getStoreReferences(objects, property) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getStoresByIds(ids).then(
                        function (data) {
                            var map = new Hashtable();

                            angular.forEach(data, function (item) {
                                map.put(item.id, item);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var item = map.get(object[property]);
                                    if (item != null) {
                                        object[property + "Object"] = item;
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function printReceiveChallan(receiveId) {
                var url = "api/is/stores/printReceiveChallan/" + receiveId;
                return httpFactory.get(url);
            }

            function printScrapChallan(scrapDetailsId) {
                var url = "api/is/stores/printScrapChallan/" + scrapDetailsId;
                return httpFactory.get(url);
            }

            function printRoadChallan(roadChalanId) {
                var url = "api/is/stores/print/RoadChallan/" + roadChalanId;
                return httpFactory.get(url);
            }

            function printIssueChallan(issueChalanId) {
                var url = "api/is/stores/print/IssueChallan/" + issueChalanId ;
                return httpFactory.get(url);
            }

            function printIndentChallan(indentChalanId) {
                var url = "api/is/stores/print/IndentChallan/" + indentChalanId;
                return httpFactory.get(url);
            }

            function printPurchaseOrderChallan(purchaseOrderId) {
                var url = "api/is/stores/print/PurchaseChallan/" + purchaseOrderId;
                return httpFactory.get(url);
            }

            function importStoreItems(storeId, files) {
                var url = "api/is/stores/" + storeId + "/import";
                return httpFactory.upload(url, files);
            }

            function downloadStoreImportFileFormat(fileType, objects) {
                var url = "api/is/stores/storeImportFileFormat/download";
                url += "?fileType={0}".format(fileType);
                return httpFactory.post(url, objects);
            }
        }

    }
);