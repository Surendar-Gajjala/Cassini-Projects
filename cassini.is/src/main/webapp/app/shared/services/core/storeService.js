define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('StoreService', StoreService);

        function StoreService(httpFactory) {
            return {
                createStore: createStore,
                getStores: getStores,
                getAllStores: getAllStores,
                getStore: getStore,
                deleteStore: deleteStore,
                updateStore: updateStore,
                getStoreReferences: getStoreReferences,
                getInventory: getInventory,
                getHistory: getHistory,
                getItems: getItems,
                updateStock: updateStock,
                getByProject: getByProject,
                getBoqItems: getBoqItems,
                searchBoqItems: searchBoqItems,
                getInventoryByMultipleStores: getInventoryByMultipleStores,
                freeTextSearch: freeTextSearch,
                getsitesByStore: getsitesByStore,
                getStockReceivedQuantities: getStockReceivedQuantities,
                getStockIssuedQuantities: getStockIssuedQuantities,
                getUnAssignedStores: getUnAssignedStores,
                getStoresBySite: getStoresBySite
            };

            function getsitesByStore(projectId, storeId) {
                var url = "api/projects/" + projectId + "/siteStores/" + storeId;
                return httpFactory.get(url);
            }

            function getStoresBySite(projectId, siteId) {
                var url = "api/projects/" + projectId + "/getStoresBySite/" + siteId;
                return httpFactory.get(url);
            }

            function getUnAssignedStores(projectId) {
                var url = "api/projects/" + projectId + "/unAssignedStores" ;
                return httpFactory.get(url);
            }

            function getInventoryByMultipleStores(projectId, storeIds, itemIds) {
                var url = "api/projects/" + projectId + "/stores/[" + storeIds + "]" + "/inventory/[" + itemIds + "]";
                return httpFactory.get(url);
            }

            function createStore(projectId, store) {
                var url = "api/projects/" + projectId + "/stores";
                return httpFactory.post(url, store);
            }

            function updateStore(projectId, store) {
                var url = "api/projects/" + projectId + "/stores/" + store.id;
                return httpFactory.put(url, store);
            }

            function deleteStore(projectId, storeId) {
                var url = "api/projects/" + projectId + "/stores/" + storeId;
                return httpFactory.delete(url);
            }


            function updateStock(projectId, stock, storeId, stockType) {
                var url = "api/projects/" + projectId + "/stores/" + storeId + "/" + stockType;
                return httpFactory.post(url, stock);
            }

            function getStores(projectId, pageable) {
                var url = "api/projects/" + projectId + "/stores/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getAllStores(projectId) {
                var url = "api/projects/" + projectId + "/stores";
                return httpFactory.get(url);
            }

            function getByProject(projectId) {
                var url = "api/projects/" + projectId + "/stores";
                return httpFactory.get(url);
            }

            function getStore(projectId, storeId) {
                var url = "api/projects/" + projectId + "/stores/" + storeId;
                return httpFactory.get(url);
            }

            function getInventory(projectId, storeId) {
                var url = "api/projects/" + projectId + "/stores/" + storeId + "/inventory";
                return httpFactory.get(url);
            }

            function getHistory(projectId, storeId) {
                var url = "api/projects/" + projectId + "/stores/" + storeId + "/inventory/history";
                return httpFactory.get(url);
            }

            function getItems(projectId, pageable, filters) {
                var url = "api/projects/" + projectId + "/items?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&field={0}".format(filters.field);

                return httpFactory.get(url);
            }

            function getBoqItems(projectId, storeId) {
                var url = "api/projects/" + projectId + "/stores/" + storeId + "/items";
                return httpFactory.get(url);
            }


            function searchBoqItems(projectId, searchQuery, pageable) {
                var url = "api/projects/" + projectId + "/items/search?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".format(searchQuery);
                return httpFactory.get(url);
            }


            function getStockReceivedQuantities(projectId, itemIds) {
                var url = "api/projects/" + projectId + "/stores/[" + itemIds + "]/stockReceived";
                return httpFactory.get(url);
            }

            function getStockIssuedQuantities(projectId, itemIds) {
                var url = "api/projects/" + projectId + "/stores/[" + itemIds + "]/stockIssued";
                return httpFactory.get(url);
            }

            function getStoresByIds(projectId, ids) {
                var url = "api/projects/" + projectId + "/stores/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getStoreReferences(projectId, objects, property) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getStoresByIds(projectId, ids).then(
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

            function freeTextSearch(projectId, pageable, criteria) {
                var url = "api/projects/" + projectId + "/stores/freesearch?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(criteria.searchQuery);
                return httpFactory.get(url);
            }

        }
    }
);