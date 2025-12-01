define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('ItemTypeService', ItemTypeService);

        function ItemTypeService($q, httpFactory) {
            return {
                createItemType: createItemType,
                updateItemType: updateItemType,
                deleteItemType: deleteItemType,
                getItemType: getItemType,
                getChildrenByTypeId: getChildrenByTypeId,
                getClassificationTree: getClassificationTree,
                getItemTypesByIds: getItemTypesByIds,
                getItemTypeItems: getItemTypeItems,
                getItemTypeReferences: getItemTypeReferences,

                createAttribute: createAttribute,
                updateAttribute: updateAttribute,
                deleteAttribute: deleteAttribute,
                getAttributesByTypeId: getAttributesByTypeId,
                getAttribute: getAttribute,
                getAttributeValues: getAttributeValues,
                getAttributesByObjectType: getAttributesByObjectType,

                findItemsByAutoNumberId: findItemsByAutoNumberId,
                getTypeParentNode: getTypeParentNode,
                getItemTypeSpecs: getItemTypeSpecs,
                createItemTypeSpec: createItemTypeSpec,
                updateItemTypeSpec: updateItemTypeSpec,
                deleteItemTypeSpec: deleteItemTypeSpec,
                getItemsBySpec: getItemsBySpec,
                getAllItemTypesWithCode: getAllItemTypesWithCode,
                getAllItemTypesWithoutSystemType: getAllItemTypesWithoutSystemType,
                importClassificationTypes: importClassificationTypes
            };

            function createItemType(itemType) {
                var url = "api/drdo/itemTypes";
                return httpFactory.post(url, itemType);
            }

            function updateItemType(itemType) {
                var url = "api/drdo/itemTypes/" + itemType.id;
                return httpFactory.put(url, itemType);
            }

            function deleteItemType(typeId) {
                var url = "api/drdo/itemTypes/" + typeId;
                return httpFactory.delete(url);
            }

            function getItemType(typeId) {
                var url = "api/drdo/itemTypes/" + typeId;
                return httpFactory.get(url);
            }

            function getChildrenByTypeId(typeId) {
                var url = "api/drdo/itemTypes/" + typeId + "/children";
                return httpFactory.get(url);
            }

            function getClassificationTree() {
                var url = "api/drdo/itemTypes/tree";
                return httpFactory.get(url);
            }

            function getItemTypesByIds(ids) {
                var url = "api/drdo/itemTypes/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getItemTypeItems(typeId) {
                var url = "api/drdo/itemTypes/" + typeId + "/items";
                return httpFactory.get(url);
            }

            function getItemTypeReferences(objects, property) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getItemTypesByIds(ids).then(
                        function (data) {
                            var map = new Hashtable();

                            angular.forEach(data, function (itemType) {
                                map.put(itemType.id, itemType);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var itemType = map.get(object[property]);
                                    if (itemType != null) {
                                        object[property + "Object"] = itemType;
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function createAttribute(typeId, attribute) {
                var url = "api/drdo/itemTypes/" + typeId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateAttribute(typeId, attribute) {
                var url = "api/drdo/itemTypes/" + typeId + "/attributes/" + attribute.id;
                return httpFactory.put(url, attribute);
            }

            function deleteAttribute(typeId, attributeId) {
                var url = "api/drdo/itemTypes/" + typeId + "/attributes/" + attributeId;
                return httpFactory.delete(url);
            }

            function getAttributesByTypeId(typeId) {
                var url = "api/drdo/itemTypes/" + typeId + "/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function getAttribute(typeId, attributeId) {
                var url = "api/drdo/itemTypes/" + typeId + "/attributes/" + attributeId;
                return httpFactory.get(url);
            }

            function getAttributeValues(attributeId) {
                var url = "api/drdo/itemTypes/items/attributes/" + attributeId;
                return httpFactory.get(url);
            }

            function getAttributesByObjectType(attributeId) {
                var url = "api/drdo/itemTypes/objectTypeAttributes/" + attributeId;
                return httpFactory.get(url);
            }

            function findItemsByAutoNumberId(autoNumberId) {
                var url = "api/drdo/itemTypes/autoNumber/" + autoNumberId + "/items";
                return httpFactory.get(url);
            }

            function getTypeParentNode(typeId) {
                var url = "api/drdo/itemTypes/" + typeId + "/parentNode";
                return httpFactory.get(url);
            }

            function getItemTypeSpecs(typeId) {
                var url = "api/drdo/itemTypes/" + typeId + "/typeSpecs";
                return httpFactory.get(url);
            }

            function createItemTypeSpec(typeId, spec) {
                var url = "api/drdo/itemTypes/" + typeId + "/specs";
                return httpFactory.post(url, spec);
            }

            function updateItemTypeSpec(typeId, spec) {
                var url = "api/drdo/itemTypes/" + typeId + "/specs/" + spec.id;
                return httpFactory.put(url, spec);
            }

            function getItemsBySpec(typeId, spec) {
                var url = "api/drdo/itemTypes/" + typeId + "/specs/" + spec + "/items";
                return httpFactory.get(url);
            }

            function deleteItemTypeSpec(typeId, spec) {
                var url = "api/drdo/itemTypes/" + typeId + "/specs/" + spec.id;
                return httpFactory.delete(url);
            }

            function getAllItemTypesWithCode() {
                var url = "api/drdo/itemTypes/all";
                return httpFactory.get(url);
            }

            function getAllItemTypesWithoutSystemType() {
                var url = "api/drdo/itemTypes/all/system";
                return httpFactory.get(url);
            }

            function importClassificationTypes(parentId, files) {
                var url = "api/drdo/itemTypes/" + parentId + "/import";
                return httpFactory.upload(url, files);
            }

        }
    }
);