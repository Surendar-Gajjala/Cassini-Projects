define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('ItemService', ItemService);

        function ItemService(httpFactory) {
            return {
                searchItems: searchItems,
                getItems: getItems,
                getItemsByType: getItemsByType,
                getItemsByIds: getItemsByIds,
                findByItemNumber: findByItemNumber,
                getItemReferences: getItemReferences,
                getItem: getItem,
                createItem: createItem,
                updateItem: updateItem,
                deleteItem: deleteItem,
                getItemAttributes: getItemAttributes,
                saveItemAttributes: saveItemAttributes,
                freeTextSearch: freeTextSearch,
                createItemAttribute : createItemAttribute,
                updateItemAttribute : updateItemAttribute,
                searchItem : searchItem,
                advancedSearchItem : advancedSearchItem

            };

            function getItems(pageable) {
                var url = "api/pdm/items/page?page={0}&size={1}&sort={2}:{3}".
                            format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }


            function searchItems(searchQuery, pageable) {
                var url = "api/pdm/items/search?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".format(searchQuery);
                return httpFactory.get(url);
            }

            function getItemsByType(typeId, pageable) {
                var url = "api/pdm/items/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getItemsByIds(ids) {
                var url = "api/pdm/items/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getItemReferences(objects, property) {
                var ids = [];
                angular.forEach(objects, function(object) {
                    if(object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if(ids.length > 0) {
                    getItemsByIds(ids).then(
                        function(data) {
                            var map = new Hashtable();

                            angular.forEach(data, function(item) {
                                map.put(item.id, item);
                            });

                            angular.forEach(objects, function(object) {
                                if(object[property] != null) {
                                    var item = map.get(object[property]);
                                    if(item != null) {
                                        object[property + "Object"] = item;
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function getItem(itemId) {
                var url = "api/pdm/items/" + itemId;
                return httpFactory.get(url);
            }

            function createItem(item) {
                var url = "api/pdm/items";
                return httpFactory.post(url, item);
            }

            function updateItem(item) {
                var url = "api/pdm/items/" + item.id;
                return httpFactory.put(url, item);
            }

            function deleteItem(itemId) {
                var url = "api/pdm/items/" + itemId;
                return httpFactory.delete(url);
            }

            function createItemAttribute(itemId, attribute) {
                var url = "api/pdm/items/" + itemId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateItemAttribute(itemId, attribute) {
                var url = "api/pdm/items/" + itemId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function getItemAttributes(itemId) {
                var url = "api/pdm/items/" + itemId + "/attributes";
                return httpFactory.get(url);
            }

            function saveItemAttributes(itemId, attributes) {
                var url = "api/pdm/items/" + itemId + "/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function findByItemNumber(itemNumber) {
                var url = "api/pdm/items/number/" + itemNumber;
                return httpFactory.get(url);
            }
            function freeTextSearch(pageable, freeText) {
                var url = "api/pdm/items/freesearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);

                url += "&searchQuery={0}".
                    format(freeText);
                return httpFactory.get(url);
            }

            function searchItem(pageable, criteria) {
                var url = "api/pdm/items/search?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                if (criteria.itemType == null) {
                    url += "&itemNumber={0}&description={1}&revision={2}&status={3}".
                        format(criteria.itemNumber, criteria.description, criteria.revision, criteria.status);
                } else {
                    url += "&itemNumber={0}&description={1}&revision={2}&status={3}&itemType={4}".
                        format(criteria.itemNumber, criteria.description, criteria.revision, criteria.status, criteria.itemType.id);
                }
                return httpFactory.get(url);
            }

            function advancedSearchItem(pageable, criteria) {
                var url = "api/pdm/items/advancedsearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);

                return httpFactory.post(url, criteria);
            }
        }
    }
);