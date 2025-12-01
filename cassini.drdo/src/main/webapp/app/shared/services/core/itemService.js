define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('ItemService', ItemService);

        function ItemService(httpFactory) {
            return {
                createItem: createItem,
                updateItem: updateItem,
                getItem: getItem,
                deleteItem: deleteItem,
                getItems: getItems,
                getAllItems: getAllItems,
                uploadImage: uploadImage,
                updateImageValue: updateImageValue,
                getItemByName: getItemByName,
                searchItems: searchItems,
                freeTextSearch: freeTextSearch,

                getRevisionId: getRevisionId,
                getRevisionReferences: getRevisionReferences,
                getRevisions: getRevisions,

                saveItemAttributes: saveItemAttributes,
                getItemRevisionAttributes: getItemRevisionAttributes,
                getItemAttributes: getItemAttributes,
                updateItemRevisionAttribute: updateItemRevisionAttribute,
                updateItemAttribute: updateItemAttribute,
                createItemRevisionAttribute: createItemRevisionAttribute,
                createItemAttribute: createItemAttribute,
                getAttributesByObjectIdAndAttributeId: getAttributesByObjectIdAndAttributeId,
                getItemInstances: getItemInstances,
                getItemInstancesFrom20: getItemInstancesFrom20,
                updateItemInstance: updateItemInstance,
                updateFilureItemInstance: updateFilureItemInstance,
                updateFilureLotInstance: updateFilureLotInstance,
                checkItemInstanceWithMfr: checkItemInstanceWithMfr,
                getItemReport: getItemReport
            };

            function createItem(item) {
                var url = "api/drdo/items";
                return httpFactory.post(url, item);
            }

            function updateItem(item) {
                var url = "api/drdo/items/" + item.id;
                return httpFactory.put(url, item);
            }

            function getItem(itemId) {
                var url = "api/drdo/items/" + itemId;
                return httpFactory.get(url);
            }

            function deleteItem(itemId) {
                var url = "api/drdo/items/" + itemId;
                return httpFactory.delete(url);
            }

            function getItems() {
                var url = "api/drdo/items";
                return httpFactory.get(url);
            }

            function getAllItems(pageable) {
                var url = "api/drdo/items/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function uploadImage(itemId, file) {
                var url = "api/drdo/items/" + itemId + "/uploadImage";
                return httpFactory.upload(url, file);
            }

            function saveItemAttributes(itemId, attributes) {
                var url = "api/drdo/items/" + itemId + "/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function updateImageValue(objectId, attributeId, file) {
                var url = "api/drdo/items/updateImageValue/" + objectId + "/" + attributeId;
                return httpFactory.upload(url, file);
            }

            function getRevisionId(revisionId) {
                var url = "api/drdo/items/revision/" + revisionId;
                return httpFactory.get(url);
            }

            function getItemRevisionAttributes(revisionId) {
                var url = "api/drdo/items/" + revisionId + "/revisionAttributes";
                return httpFactory.get(url);
            }

            function getItemAttributes(itemId) {
                var url = "api/drdo/items/" + itemId + "/attributes";
                return httpFactory.get(url);
            }

            function updateItemRevisionAttribute(revisionId, attribute) {
                var url = "api/drdo/items/" + revisionId + "/revisionAttributes";
                return httpFactory.put(url, attribute);
            }

            function updateItemAttribute(itemId, attribute) {
                var url = "api/drdo/items/" + itemId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function createItemRevisionAttribute(revisionId, attribute) {
                var url = "api/drdo/items/" + revisionId + "/revisionAttributes";
                return httpFactory.post(url, attribute);
            }

            function createItemAttribute(itemId, attribute) {
                var url = "api/drdo/items/" + itemId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function getItemByName(name) {
                var url = "api/drdo/items/byName/" + name;
                return httpFactory.get(url);
            }


            function searchItems(pageable, filters) {
                var url = "api/drdo/items/newItemSearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&item={1}&selectedItem={2}".format(filters.searchQuery, filters.item, filters.selectedItem);
                return httpFactory.get(url);
            }

            function getRevisions(revisionIds) {
                var url = "api/drdo/items/revisions/multiple/[" + revisionIds + "]";
                return httpFactory.get(url);
            }

            function getRevisionReferences(objects, property) {
                var revisionIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && revisionIds.indexOf(object[property]) == -1) {
                        revisionIds.push(object[property]);
                    }
                });

                if (revisionIds.length > 0) {
                    getRevisions(revisionIds).then(
                        function (revisions) {
                            var map = new Hashtable();
                            angular.forEach(revisions, function (revision) {
                                map.put(revision.id, revision);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var revision = map.get(object[property]);
                                    if (revision != null) {
                                        object[property + "Object"] = revision;
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function freeTextSearch(freeText, pageable) {
                var url = "api/drdo/items/newItemSearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".format(freeText);
                return httpFactory.get(url);
            }

            function getAttributesByObjectIdAndAttributeId(itemIds, attributeIds) {
                var url = "api/drdo/items/objectAttributes";
                return httpFactory.post(url, [itemIds, attributeIds]);
            }

            function getItemInstances(itemId) {
                var url = "api/drdo/items/instances/" + itemId;
                return httpFactory.get(url);
            }

            function getItemInstancesFrom20(itemId) {
                var url = "api/drdo/items/instances/from20/" + itemId;
                return httpFactory.get(url);
            }

            function updateItemInstance(itemInstance) {
                var url = "api/drdo/items/instances/" + itemInstance.id;
                return httpFactory.put(url, itemInstance);
            }

            function updateFilureItemInstance(itemInstance) {
                var url = "api/drdo/items/instances/failure/" + itemInstance.id;
                return httpFactory.put(url, itemInstance);
            }

            function updateFilureLotInstance(itemInstance) {
                var url = "api/drdo/items/lotInstances/failure/" + itemInstance.id;
                return httpFactory.put(url, itemInstance);
            }

            function checkItemInstanceWithMfr(mfr) {
                var url = "api/drdo/items/check/" + mfr;
                return httpFactory.get(url);
            }

            function getItemReport(itemId) {
                var url = "api/drdo/items/" + itemId + "/report";
                return httpFactory.get(url);
            }

        }
    }
);