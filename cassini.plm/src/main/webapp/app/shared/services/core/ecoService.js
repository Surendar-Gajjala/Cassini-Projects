define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('ECOService', ECOService);

        function ECOService(httpFactory) {
            return {
                getECOs: getECOs,
                getECO: getECO,
                getECOByNumber: getECOByNumber,
                getECOsByIds: getECOsByIds,
                getECOReferences: getECOReferences,
                createECO: createECO,
                updateECO: updateECO,
                deleteECO: deleteECO,

                getEcoItems: getEcoItems,
                getEcoItem: getEcoItem,
                createEcoItem: createEcoItem,
                updateEcoItem: updateEcoItem,
                deleteEcoItem: deleteEcoItem,

                getEcoSignOffs: getEcoSignOffs,
                getEcoSignOff: getEcoSignOff,
                createEcoSignOff: createEcoSignOff,
                updateEcoSignOff: updateEcoSignOff,
                deleteEcoSignOff: deleteEcoSignOff,

                getEcoItemChanges: getEcoItemChanges,
                getEcoItemChange: getEcoItemChange,
                createEcoItemChange: createEcoItemChange,
                updateEcoItemChange: updateEcoItemChange,
                deleteEcoItemChange: deleteEcoItemChange,
                getEcoSearches: getEcoSearches,
                searchEco: searchEco,
                freeTextSearch: freeTextSearch,
                advancedSearchEcr: advancedSearchEcr,
                createEcoAffectedItem: createEcoAffectedItem,
                updateEcoAffectedItem: updateEcoAffectedItem,
                getEcoAffectedItems: getEcoAffectedItems,
                deleteEcoAffectedItem: deleteEcoAffectedItem,

                attachWorkflow: attachWorkflow,
                getWorkflow: getWorkflow,

                getChangeTypeReferences: getChangeTypeReferences,
                getChangeTypesByIds: getChangeTypesByIds,
                getEcoAttributesWithHierarchy: getEcoAttributesWithHierarchy,
                getChangeType: getChangeType,
                saveECOAttributes: saveECOAttributes,
                getEcoAttributes: getEcoAttributes,
                createEcoAttribute: createEcoAttribute,
                updateEcoAttribute: updateEcoAttribute,
                getLatestReleasedRevisions: getLatestReleasedRevisions,
                findByReleasedTrue: findByReleasedTrue,

                getECOsByType: getECOsByType,
                getChangeTypeByName: getChangeTypeByName,
                getEcoAttributeValues: getEcoAttributeValues,
                updateEcoImageValue: updateEcoImageValue,
                getECOCounts: getECOCounts,
                deleteWorkflow: deleteWorkflow,
                getWorkflows: getWorkflows,
                getECRTypeTree: getECRTypeTree,
                getMCOTypeTree: getMCOTypeTree,
                getDCRTypeTree: getDCRTypeTree,
                getDCOTypeTree: getDCOTypeTree,
                getECOTypeTree: getECOTypeTree,
                getFilteredItems: getFilteredItems,
                createVarianceRelatedItem: createVarianceRelatedItem,
                deleteVarianceRelatedItem: deleteVarianceRelatedItem,
                getAllChangeRelatedItems: getAllChangeRelatedItems,
                createAffectedItem: createAffectedItem,
                getConfigurableAffectedItems: getConfigurableAffectedItems,
                getObjectsByType: getObjectsByType,
                createMultipleAffectedItem: createMultipleAffectedItem,
                attachNewWorkflow: attachNewWorkflow,
                getItemMCOTypeTree: getItemMCOTypeTree,
                getManufacturerMCOTypeTree: getManufacturerMCOTypeTree,

                getECOCount: getECOCount,
                getChangeObjects: getChangeObjects,
                getChangeObject: getChangeObject,
                getChangeTypeTree: getChangeTypeTree,
                getChangeAffectedItems: getChangeAffectedItems,
                getChangeAnalysts: getChangeAnalysts,
                getECOStatus: getECOStatus,
                getAllItemECOs: getAllItemECOs,
                getChangeTypeFilterObjects:getChangeTypeFilterObjects
               

            };

            function advancedSearchEcr(pageable, filters) {
                var url = "api/cm/ecos/advancedsearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);

                return httpFactory.post(url, filters);
            }

            function freeTextSearch(pageable, freetext) {
                var url = "api/cm/ecos/freesearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);

                url += "&searchQuery={0}".
                    format(freetext);
                return httpFactory.get(url);
            };

            function searchEco(pageable, criteria) {
                var url = "api/cm/ecos/search?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);

                url += "&ecoNumber={0}&title={1}&description={2}&statusType={3}&ecoOwner={4}".
                    format(criteria.ecoNumber, criteria.title, criteria.description, criteria.statusType, criteria.ecoOwnerObject.id);

                return httpFactory.get(url);
            }

            function getECOs(pageable) {
                var url = "api/cm/ecos?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getECO(ecoId) {
                var url = "api/cm/ecos/" + ecoId;
                return httpFactory.get(url);
            }

            function getECOByNumber(number) {
                var url = "api/cm/ecos/byNumber/" + number;
                return httpFactory.get(url);
            }

            function createECO(eco) {
                var url = "api/cm/ecos";
                return httpFactory.post(url, eco);
            }

            function updateECO(eco) {
                var url = "api/cm/ecos/" + eco.id;
                return httpFactory.put(url, eco);
            }

            function deleteECO(ecoId) {
                var url = "api/cm/ecos/" + ecoId;
                return httpFactory.delete(url);
            }

            function getECOsByIds(ids) {
                var url = "api/cm/ecos/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getECOReferences(objects, property) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getECOsByIds(ids).then(
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

            function getEcoItems(ecoId, type) {
                var url = "api/cm/ecos/" + ecoId + "/items?type=" + type;
                return httpFactory.get(url);
            }

            function getEcoAffectedItems(ecoId) {
                var url = "api/cm/ecos/" + ecoId + "/ecoAffectedItems";
                return httpFactory.get(url);
            }

            function getEcoItem(ecoId, itemId) {
                var url = "api/cm/ecos/" + ecoId + "/items/" + itemId;
                return httpFactory.get(url);
            }

            function createEcoItem(ecoId, item) {
                var url = "api/cm/ecos/" + ecoId + "/items";
                return httpFactory.post(url, item)
            }

            function createEcoAffectedItem(ecoId, item) {
                var url = "api/cm/ecos/" + ecoId + "/ecoAffectedItems";
                return httpFactory.post(url, item)
            }

            function createAffectedItem(ecoId, item) {
                var url = "api/cm/ecos/" + ecoId + "/affectedItems";
                return httpFactory.post(url, item)
            }

            function createMultipleAffectedItem(ecoId, items) {
                var url = "api/cm/ecos/" + ecoId + "/affectedItems/multiple";
                return httpFactory.post(url, items)
            }

            function updateEcoAffectedItem(ecoId, item) {
                var url = "api/cm/ecos/" + ecoId + "/ecoAffectedItems/" + item.id;
                return httpFactory.put(url, item)
            }

            function updateEcoItem(ecoId, item) {
                var url = "api/cm/ecos/" + ecoId + "/items/" + item.id;
                return httpFactory.put(url, item)
            }

            function deleteEcoItem(ecoId, itemId) {
                var url = "api/cm/ecos/" + ecoId + "/items/" + itemId;
                return httpFactory.delete(url)
            }

            function deleteEcoAffectedItem(ecoId, itemId) {
                var url = "api/cm/ecos/" + ecoId + "/ecoAffectedItems/" + itemId;
                return httpFactory.delete(url)
            }

            //
            function getEcoItemChanges(ecoId, itemId) {
                var url = "api/cm/ecos/" + ecoId + "/items/" + itemId + "/changes";
                return httpFactory.get(url);
            }

            function getEcoItemChange(ecoId, itemId, changeId) {
                var url = "api/cm/ecos/" + ecoId + "/items/" + itemId + "/changes/" + changeId;
                return httpFactory.get(url);
            }

            function createEcoItemChange(ecoId, itemId, change) {
                var url = "api/cm/ecos/" + ecoId + "/items" + itemId + "/changes";
                return httpFactory.post(url, change)
            }

            function updateEcoItemChange(ecoId, itemId, changeId, change) {
                var url = "api/cm/ecos/" + ecoId + "/items" + itemId + "/changes/" + changeId;
                return httpFactory.put(url, change)
            }

            function deleteEcoItemChange(ecoId, itemId, changeId) {
                var url = "api/cm/ecos/" + ecoId + "/items/" + itemId + "/changes/" + changeId;
                return httpFactory.delete(url)
            }

            function getEcoSignOffs(ecoId) {
                var url = "api/cm/ecos/" + ecoId + "/signoffs";
                return httpFactory.get(url);
            }

            function getEcoSignOff(ecoId, signOffId) {
                var url = "api/cm/ecos/" + ecoId + "/signoffs/" + signOffId;
                return httpFactory.get(url);
            }

            function createEcoSignOff(ecoId, signOff) {
                var url = "api/cm/ecos/" + ecoId + "/signoffs";
                return httpFactory.post(url, signOff)
            }

            function updateEcoSignOff(ecoId, signOff) {
                var url = "api/cm/ecos/" + ecoId + "/signoffs/" + signOff.id;
                return httpFactory.put(url, signOff)
            }

            function deleteEcoSignOff(ecoId, signOffId) {
                var url = "api/cm/ecos/" + ecoId + "/signoffs/" + signOffId;
                return httpFactory.delete(url)
            }

            function getEcoSearches(pageable) {
                var url = "api/cm/ecos/search?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function attachWorkflow(ecoId, wfId) {
                var url = "api/cm/ecos/" + ecoId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

            function attachNewWorkflow(ecoId, wfId, rule, statusId) {
                var url = "api/cm/ecos/" + ecoId + "/attachnewworkflow/" + wfId + "?rule=" + rule + "&status=" + statusId;
                return httpFactory.get(url);
            }

            function getWorkflow(ecoId) {
                var url = "api/cm/ecos/" + ecoId + "/workflow";
                return httpFactory.get(url);
            }


            function getChangeTypesByIds(ids) {
                var url = "api/cm/ecos/changeTypes/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getChangeTypeReferences(objects, property, callback) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getChangeTypesByIds(ids).then(
                        function (data) {
                            var map = new Hashtable();

                            angular.forEach(data, function (changeType) {
                                map.put(changeType.id, changeType);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var changeType = map.get(object[property]);
                                    if (changeType != null) {
                                        object[property + "Object"] = changeType;
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

            function createEcoAttribute(ecoId, attribute) {
                var url = "api/cm/ecos/" + ecoId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateEcoAttribute(ecoId, attribute) {
                var url = "api/cm/ecos/" + ecoId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function getEcoAttributes(ecoId) {
                var url = "api/cm/ecos/" + ecoId + "/attributes";
                return httpFactory.get(url);
            }

            function getChangeType(changeType) {
                var url = "api/cm/ecos/changeType/" + changeType;
                return httpFactory.get(url);
            }

            function getEcoAttributesWithHierarchy(type) {
                var url = "api/cm/ecos/ecoTypes/" + type + "/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function saveECOAttributes(ecoId, attributes) {
                var url = "api/cm/ecos/" + ecoId + "/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function getLatestReleasedRevisions(pageable) {
                var url = "api/cm/ecos/latestReleasedRevs?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function findByReleasedTrue(pageable) {
                var url = "api/cm/ecos/releasedEcos?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getECOsByType(typeId, pageable) {
                var url = "api/cm/ecos/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getObjectsByType(typeId) {
                var url = "api/cm/ecos/type/" + typeId + "/count";
                return httpFactory.get(url);
            }

            function getChangeTypeByName(name) {
                var url = "api/cm/ecos/byName/" + name;
                return httpFactory.get(url);
            }

            function getEcoAttributeValues(attributeId) {
                var url = "api/cm/ecos/attributes/" + attributeId;
                return httpFactory.get(url);
            }

            function updateEcoImageValue(objectId, attributeId, file) {
                var url = "api/cm/ecos/updateImageValue/" + objectId + "/" + attributeId;
                return httpFactory.upload(url, file);
            }

            function getECOCounts(ecoId) {
                var url = 'api/cm/ecos/' + ecoId + "/details";
                return httpFactory.get(url);
            }

            function deleteWorkflow(ecoId) {
                var url = 'api/cm/ecos/' + ecoId + "/workflow/delete";
                return httpFactory.delete(url);
            }

            function getWorkflows(typeId, type) {
                var url = "api/cm/ecos/workflow/" + typeId + "/changeType/" + type;
                return httpFactory.get(url);
            }

            function getFilteredItems(pageable, filters) {
                var url = "api/cm/ecos/filteredItems?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&itemNumber={0}&itemName={1}&itemType={2}&item={3}&bomItem={4}&variance={5}&related={6}".
                    format(filters.itemNumber, filters.itemName, filters.itemType, filters.item, filters.bomItem, filters.variance, filters.related);
                return httpFactory.get(url);
            }

            function createVarianceRelatedItem(changeId, item) {
                var url = "api/cm/ecos/changeRelatedItem/" + changeId;
                return httpFactory.post(url, item);
            }

            function deleteVarianceRelatedItem(changeId) {
                var url = "api/cm/ecos/changeRelatedItem/" + changeId;
                return httpFactory.delete(url);
            }

            function getAllChangeRelatedItems(changeId) {
                var url = "api/cm/ecos/changeRelatedItem/" + changeId;
                return httpFactory.get(url);
            }

            function getECRTypeTree() {
                var url = "api/plm/classification/CHANGETYPE/ecr/tree";
                return httpFactory.get(url)
            }

            function getMCOTypeTree() {
                var url = "api/plm/classification/CHANGETYPE/mco/tree";
                return httpFactory.get(url)
            }

            function getItemMCOTypeTree() {
                var url = "api/plm/classification/CHANGETYPE/itemMco/tree";
                return httpFactory.get(url)
            }

            function getManufacturerMCOTypeTree() {
                var url = "api/plm/classification/CHANGETYPE/manufacturerMco/tree";
                return httpFactory.get(url)
            }

            function getECOTypeTree() {
                var url = "api/plm/classification/CHANGETYPE/eco/tree";
                return httpFactory.get(url)
            }

            function getDCRTypeTree() {
                var url = "api/plm/classification/CHANGETYPE/dcr/tree";
                return httpFactory.get(url)
            }

            function getDCOTypeTree() {
                var url = "api/plm/classification/CHANGETYPE/dco/tree";
                return httpFactory.get(url)
            }

            function getConfigurableAffectedItems(ecoId) {
                var url = "api/cm/ecos/" + ecoId + "/affectedItems/configurable";
                return httpFactory.get(url)
            }

            function getECOCount() {
                var url = "api/cm/ecos/count";
                return httpFactory.get(url);
            }

            function getChangeObjects(pageable, filters) {
                var url = "api/cm/ecos/change/all/objects?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&type={0}&searchQuery={1}".
                    format(filters.type, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getChangeObject(changeId) {
                var url = "api/cm/ecos/change/" + changeId;
                return httpFactory.get(url);
            }

            function getChangeTypeTree() {
                var url = "api/cm/ecos/change/type/tree";
                return httpFactory.get(url);
            }

            function getChangeAffectedItems(id) {
                var url = "api/cm/ecos/" + id + "/change/items";
                return httpFactory.get(url);
            }

            function getChangeAnalysts() {
                var url = "api/cm/ecos/changeAnalysts";
                return httpFactory.get(url);
            }

            function getECOStatus() {
                var url = "api/cm/ecos/status";
                return httpFactory.get(url);
            }

            function getAllItemECOs(pageable, filters) {
                var url = "api/cm/ecos/item/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&ecoNumber={0}&ecoType={1}&title={2}&searchQuery={3}&ecoOwner={4}&status={5}".
                    format(filters.ecoNumber, filters.ecoType, filters.title, filters.searchQuery, filters.ecoOwner, filters.status);
                return httpFactory.get(url);
            }

            function getChangeTypeFilterObjects(type) {
                var url = "api/cm/ecos/change/filter/objects/" + type;
                return httpFactory.get(url);
            }

            
        }

    }
);