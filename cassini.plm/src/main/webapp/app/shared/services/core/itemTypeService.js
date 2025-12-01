define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('ItemTypeService', ItemTypeService);

        function ItemTypeService($q, httpFactory) {
            return {
                getClassificationTree: getClassificationTree,
                createItemType: createItemType,
                getItemType: getItemType,
                updateItemType: updateItemType,
                deleteItemType: deleteItemType,
                getChildren: getChildren,

                getAttributes: getAttributes,
                getAttributesWithHierarchy: getAttributesWithHierarchy,
                getAttributesWithHierarchyAndRevision: getAttributesWithHierarchyAndRevision,
                getAttribute: getAttribute,
                createAttribute: createAttribute,
                updateAttribute: updateAttribute,
                deleteAttribute: deleteAttribute,
                getLifeCycleByName: getLifeCycleByName,
                getLifeCycles: getLifeCycles,
                getLifeCyclesPhases: getLifeCyclesPhases,
                getAllTypeAttributes: getAllTypeAttributes,
                getItemTypeReferences: getItemTypeReferences,
                getAllItemTypesByName: getAllItemTypesByName,
                findItemByAutoNumId: findItemByAutoNumId,
                getItemTypeItems: getItemTypeItems,
                getAttributeName: getAttributeName,
                getAttributeValues: getAttributeValues,
                updateAttributeSeq: updateAttributeSeq,
                generateSeq: generateSeq,
                updateChangeAttributeSeq: updateChangeAttributeSeq,
                updateMfrAttributeSeq: updateMfrAttributeSeq,
                updateRmAttributeSeq: updateRmAttributeSeq,
                updateWorkflowAttributeSeq: updateWorkflowAttributeSeq,
                updateMfrPartAttributeSeq: updateMfrPartAttributeSeq,
                systemInformation: systemInformation,
                getModalBomItemsWithAttributes: getModalBomItemsWithAttributes,
                getModalBomItemsWithAttributesDef: getModalBomItemsWithAttributesDef,
                updateItemTypeTabs: updateItemTypeTabs,
                getAttributesWithHierarchyFalse: getAttributesWithHierarchyFalse,
                getItemTypeByLovId: getItemTypeByLovId,
                getTypeAttributeName: getTypeAttributeName,
                getBomRollupAttributes: getBomRollupAttributes,
                getItemClassTypeTree: getItemClassTypeTree,
                getAllListOfValues: getAllListOfValues,
                getItemTypeTree: getItemTypeTree,
                checkRequiredAttributeValues: checkRequiredAttributeValues,
                getLovValueUsedCount: getLovValueUsedCount,
                getItemTypeLifecycles: getItemTypeLifecycles,
                getAutoNumberUsedCount: getAutoNumberUsedCount,
            };

            function systemInformation() {
                var url = "api/plm/itemtypes/system/information";
                return httpFactory.get(url);
            }

            function getAllItemTypesByName(name) {
                var url = "api/plm/itemtypes/byName/" + name;
                return httpFactory.get(url);
            }

            function findItemByAutoNumId(autoNumberId) {
                var url = "api/plm/itemtypes/item/" + autoNumberId;
                return httpFactory.get(url);
            }

            function getClassificationTree() {
                var url = "api/plm/itemtypes/tree";
                return httpFactory.get(url);
            }

            /*---- To avoid transactional error ------------*/
            function getItemTypeTree() {
                var url = "api/plm/itemtypes/tree/all";
                return httpFactory.get(url);
            }

            function getItemClassTypeTree(classType) {
                var url = "api/plm/itemtypes/" + classType + "/tree";
                return httpFactory.get(url);
            }

            function createItemType(itemType) {
                var url = "api/plm/itemtypes";
                return httpFactory.post(url, itemType);
            }

            function getItemType(typeId) {
                var url = "api/plm/itemtypes/" + typeId;
                return httpFactory.get(url);
            }

            function updateItemType(itemType) {
                var url = "api/plm/itemtypes/" + itemType.id;
                return httpFactory.put(url, itemType);
            }

            function deleteItemType(typeId) {
                var url = "api/plm/itemtypes/" + typeId;
                return httpFactory.delete(url);
            }

            function getChildren(typeId) {
                var url = "api/plm/itemtypes/" + typeId + "/children";
                return httpFactory.get(url);
            }

            function getAttributes(typeId) {
                var url = "api/plm/itemtypes/" + typeId + "/attributes";
                return httpFactory.get(url);
            }

            function getAttributesWithHierarchy(typeId) {
                var url = "api/plm/itemtypes/" + typeId + "/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function getAttributesWithHierarchyAndRevision(typeId, revisionId) {
                var url = "api/plm/itemtypes/" + typeId + "/" + revisionId + "/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function getAttribute(typeId, attributeId) {
                var url = "api/plm/itemtypes/" + typeId + "/attributes/" + attributeId;
                return httpFactory.get(url);
            }

            function createAttribute(typeId, attribute) {
                var url = "api/plm/itemtypes/" + typeId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateAttribute(typeId, attribute) {
                var url = "api/plm/itemtypes/" + typeId + "/attributes/" + attribute.id;
                return httpFactory.put(url, attribute);
            }

            function getAttributeName(name) {
                var url = "api/plm/itemtypes/getAttributeName/" + name;
                return httpFactory.get(url);
            }

            function getTypeAttributeName(type, name) {
                var url = "api/plm/itemtypes/" + type.id + "/getTypeAttributeName/" + name;
                return httpFactory.get(url);
            }

            function deleteAttribute(typeId, attributeId) {
                var url = "api/plm/itemtypes/" + typeId + "/attributes/" + attributeId;
                return httpFactory.delete(url);
            }

            function getLifeCycles() {
                var url = 'api/plm/lifecycles';
                return httpFactory.get(url);
            }

            function getLifeCyclesPhases() {
                var url = 'api/plm/lifecycles/phases';
                return httpFactory.get(url);
            }

            function getLifeCycleByName(name) {
                return $q(function (resolve, reject) {
                    getLifeCycles().then(
                        function (data) {
                            var found = [];
                            angular.forEach(data.content, function (item) {
                                if (item.name == name) {
                                    found.push(item);
                                }
                            });

                            resolve(found);
                        }
                    );
                });
            }

            function getAllTypeAttributes(objectType) {
                var url = "api/plm/itemtypes/attributes/" + objectType;
                return httpFactory.get(url);
            }

            function getItemTypesByIds(ids) {
                var url = "api/plm/itemtypes/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getItemTypeReferences(objects, property, callback) {
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

                            if (callback != null && callback != undefined) {
                                callback();
                            }
                        }
                    );
                }
            }

            function getItemTypeItems(typeId) {
                var url = "api/plm/itemtypes/" + typeId + "/items";
                return httpFactory.get(url);
            }

            function getAttributeValues(attributeId) {
                var url = "api/plm/itemtypes/items/attributes/" + attributeId;
                return httpFactory.get(url);
            }


            function updateAttributeSeq(targetId, actualId) {
                var url = "api/plm/itemtypes/" + targetId + "/attributeSeq/" + actualId;
                return httpFactory.post(url);
            }

            function updateChangeAttributeSeq(targetId, actualId) {
                var url = "api/plm/itemtypes/change/" + targetId + "/attributeSeq/" + actualId;
                return httpFactory.post(url);
            }

            function updateMfrAttributeSeq(targetId, actualId) {
                var url = "api/plm/itemtypes/mfr/" + targetId + "/attributeSeq/" + actualId;
                return httpFactory.post(url);
            }

            function updateMfrPartAttributeSeq(targetId, actualId) {
                var url = "api/plm/itemtypes/part/" + targetId + "/attributeSeq/" + actualId;
                return httpFactory.post(url);
            }

            function updateRmAttributeSeq(targetId, actualId) {
                var url = "api/plm/itemtypes/rm/" + targetId + "/attributeSeq/" + actualId;
                return httpFactory.post(url);
            }

            function updateWorkflowAttributeSeq(targetId, actualId) {
                var url = "api/plm/itemtypes/workflow/" + targetId + "/attributeSeq/" + actualId;
                return httpFactory.post(url);
            }

            function generateSeq() {
                var url = "api/plm/itemtypes/generateSeq";
                return httpFactory.post(url);
            }

            function getModalBomItemsWithAttributes(typeId) {
                var url = "api/plm/items/modal/bom/items/" + typeId;
                return httpFactory.get(url);
            }


            function getModalBomItemsWithAttributesDef(typeId) {
                var url = "api/plm/items/modal/bom/attributes/" + typeId;
                return httpFactory.get(url);
            }

            function updateItemTypeTabs() {
                var url = "api/plm/itemtypes/update/tabs";
                return httpFactory.get(url);
            }

            function getAttributesWithHierarchyFalse(typeId) {
                var url = "api/plm/itemtypes/" + typeId + "/attributes?hierarchy=false";
                return httpFactory.get(url);
            }

            function getItemTypeByLovId(lov) {
                var url = "api/plm/itemtypes/lov/" + lov.id;
                return httpFactory.get(url);
            }

            function getBomRollupAttributes() {
                var url = "api/plm/itemtypes/bomRollupAttributes";
                return httpFactory.get(url);
            }

            function getAllListOfValues() {
                var url = "api/plm/itemtypes/lovs";
                return httpFactory.get(url);
            }

            function getLovValueUsedCount(lov, lovValue) {
                var url = "api/plm/itemtypes/lovs/" + lov + "/value/count?lovValue=" + lovValue;
                return httpFactory.get(url);
            }

            function checkRequiredAttributeValues(attributeId) {
                var url = "api/plm/itemtypes/attribute/" + attributeId + "/values";
                return httpFactory.get(url);
            }

            function getItemTypeLifecycles() {
                var url = "api/plm/itemtypes/lifecycles";
                return httpFactory.get(url);
            }

            function getAutoNumberUsedCount(autoNumber, autoNumberValue) {
                var url = "api/plm/itemtypes/autoNumber/" + autoNumber + "/value/count?autoNumber=" + autoNumberValue;
                return httpFactory.get(url);
            }
        }
    }
);