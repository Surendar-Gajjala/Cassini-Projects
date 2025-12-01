define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('ItemService', ItemService);

        function ItemService(httpFactory) {
            return {
                getItems: getItems,
                getAllItems: getAllItems,
                getUniqueRevisions: getUniqueRevisions,
                getAllLatestItemRevisions: getAllLatestItemRevisions,
                getItemsByType: getItemsByType,
                getItemsByIds: getItemsByIds,
                getItemReferences: getItemReferences,
                getItem: getItem,
                findByItemNumber: findByItemNumber,
                createItem: createItem,
                createItemRevision: createItemRevision,
                getItemRevision: getItemRevision,
                updateItem: updateItem,
                reviseItem: reviseItem,
                deleteItem: deleteItem,
                createItemAttribute: createItemAttribute,
                updateItemAttribute: updateItemAttribute,
                createItemRevisionAttribute: createItemRevisionAttribute,
                updateItemRevisionAttribute: updateItemRevisionAttribute,
                getItemAttributes: getItemAttributes,
                getItemRevisionAttributes: getItemRevisionAttributes,
                saveItemAttributes: saveItemAttributes,
                saveImageItemAttribute: saveImageItemAttribute,
                getItemChanges: getItemChanges,
                getItemECRs: getItemECRs,
                getItemDCOs: getItemDCOs,
                getItemDCRs: getItemDCRs,
                getItemPRs: getItemPRs,
                getItemQCRs: getItemQCRs,
                searchItem: searchItem,
                advancedSearchItem: advancedSearchItem,
                freeTextSearch: freeTextSearch,
                getRevisionReferences: getRevisionReferences,
                getRevisionId: getRevisionId,
                getLatestRevisionReferences: getLatestRevisionReferences,
                getRevisionsByIds: getRevisionsByIds,
                getLatestRevision: getLatestRevision,
                getItemRevisions: getItemRevisions,
                getItemRevisionHistory: getItemRevisionHistory,
                copyItem: copyItem,
                getAttributesByItemIdAndAttributeId: getAttributesByItemIdAndAttributeId,
                exportItems: exportItems,
                uploadImage: uploadImage,
                getImage: getImage,
                getPageableItemsByIds: getPageableItemsByIds,
                getFilteredItems: getFilteredItems,
                updateImageValue: updateImageValue,
                lockItem: lockItem,
                subscribe: subscribe,
                getSubscribeByPerson: getSubscribeByPerson,
                updateItemMaster: updateItemMaster,
                exportItemReport: exportItemReport,
                getProjectItem: getProjectItem,
                getItemRequirements: getItemRequirements,
                importBom: importBom,
                getItemDetails: getItemDetails,
                exportBom: exportBom,
                createItemComment: createItemComment,
                getAllLogsHistory: getAllLogsHistory,
                searchLogs: searchLogs,
                updateBomSequence: updateBomSequence,
                setFileAsItemImage: setFileAsItemImage,
                getEmailTemplate: getEmailTemplate,
                updateEmailTemplate: updateEmailTemplate,
                getItemRevisionsForBomCompare: getItemRevisionsForBomCompare,
                getHasBomItemsForBomCompare: getHasBomItemsForBomCompare,
                getComparedIndividualItems: getComparedIndividualItems,
                getComparedIndividualRevisions: getComparedIndividualRevisions,
                getHasBomItemsForBomCompareLatestFalse: getHasBomItemsForBomCompareLatestFalse,
                getInstanceItem: getInstanceItem,
                getItemInstances: getItemInstances,
                promtoItem: promtoItem,
                demoteItem: demoteItem,
                updateBomIncluions: updateBomIncluions,
                updateBomItemSequence: updateBomItemSequence,
                getAllItemsToCompare: getAllItemsToCompare,
                getAllItemsToCompareLatestFalse: getAllItemsToCompareLatestFalse,
                getComparedItems: getComparedItems,

                getWorkflows: getWorkflows,
                attachWorkflow: attachWorkflow,
                deleteWorkflow: deleteWorkflow,
                updateItemConfigurableAttributes: updateItemConfigurableAttributes,
                getConfigurableAttributes: getConfigurableAttributes,
                getItemConfigurableAttributes: getItemConfigurableAttributes,
                updateItemConfigAttribute: updateItemConfigAttribute,
                updateAllItemConfigurableAttributes: updateAllItemConfigurableAttributes,
                getItemsByItemClass: getItemsByItemClass,
                getItemByRevision: getItemByRevision,
                getItemTypeItems: getItemTypeItems,
                getItemsByClass: getItemsByClass,
                getNormalItemsByItemClass: getNormalItemsByItemClass,
                getItemsToCompare: getItemsToCompare,
                getProductItemPRs: getProductItemPRs,
                importUploadedFile: importUploadedFile,
                getLatestItemRevisionsByIds: getLatestItemRevisionsByIds,
                deleteItemRevision: deleteItemRevision,
                getTotalItems: getTotalItems,
                advancedSearchAllItems: advancedSearchAllItems,
                searchAllItems: searchAllItems,
                freeTextSearchAll: freeTextSearchAll,
                getItemMasterCount: getItemMasterCount,
                getItemRevisionCount: getItemRevisionCount,
                createItemSpecification: createItemSpecification,
                createMultipleItemSpecification: createMultipleItemSpecification,
                updateItemSpecification: updateItemSpecification,
                deleteItemSpecification: deleteItemSpecification,
                getItemSpecifications: getItemSpecifications,
                createSubstituteParts: createSubstituteParts,
                createAlternateParts: createAlternateParts,
                deleteSubstitutePart: deleteSubstitutePart,
                deleteAlternatePart: deleteAlternatePart,
                getSubstituteParts: getSubstituteParts,
                getAlternateParts: getAlternateParts,
                createAlternatePart: createAlternatePart,
                updateAlternatePart: updateAlternatePart,
                searchItemRevisions: searchItemRevisions,
                getNormalAndConfigurableItemsByItemType: getNormalAndConfigurableItemsByItemType,
                getNormalAndConfigurableItemsByItemClass: getNormalAndConfigurableItemsByItemClass,
                getReleasedItemsByItemClass: getReleasedItemsByItemClass,
                getItemsByTypeId: getItemsByTypeId,
                getItemRevisionIds: getItemRevisionIds,
                getReleasedItemsByHasBom: getReleasedItemsByHasBom
            }


            function getAllLogsHistory(pageable) {
                var url = "api/plm/items?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }


            function searchLogs(pageable, filters) {
                var url = "api/plm/items/logs/search/search?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&type={0}&dateTime={1}&userName={2}"
                    .format(filters.type, filters.dateTime, filters.userName);
                return httpFactory.get(url);
            }

            function exportItems(file) {
                var url = "api/plm/items/export/" + file;
                return httpFactory.get(url);
            }

            function searchItem(pageable, criteria) {
                var url = "api/plm/items/search?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                if (criteria.itemType == null) {
                    url += "&itemNumber={0}&itemName={1}&revision={2}&status={3}&itemClass={4}".format(criteria.itemNumber, criteria.itemName, criteria.revision, criteria.status, criteria.itemClass);
                } else {
                    url += "&itemNumber={0}&itemName={1}&revision={2}&status={3}&itemType={4}&itemClass={5}".format(criteria.itemNumber, criteria.itemName, criteria.revision, criteria.status, criteria.itemType.id, criteria.itemClass);
                }
                return httpFactory.get(url);
            }

            function advancedSearchItem(pageable, criteria) {
                var url = "api/plm/items/advancedsearch?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);

                return httpFactory.post(url, criteria);
            }

            function freeTextSearch(pageable, freetext, itemClass) {
                var url = "api/plm/items/freetextsearch?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);

                url += "&searchQuery={0}&itemClass={1}".format(freetext, itemClass);
                return httpFactory.get(url);
            }

            function getAllLatestItemRevisions(pageable) {
                var url = "api/plm/items/revisions?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);

            }

            function searchItemRevisions(pageable, filters) {
                var url = "api/plm/items/revisions/search?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&itemClass={1}&itemId={2}".format(filters.searchQuery, filters.itemClass, filters.itemId);
                return httpFactory.get(url);

            }

            function getItems(pageable) {
                var url = "api/plm/items?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getUniqueRevisions() {
                var url = "api/plm/items/revisions/unique";
                return httpFactory.get(url);
            }

            function getAllItems(pageable, filters) {
                var url = "api/plm/items/all?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&itemClass={1}&itemNumber={2}&itemName={3}&description={4}&type={5}&itemType={6}&phase={7}&revision={8}".
                    format(filters.searchQuery, filters.itemClass, filters.itemNumber, filters.itemName, filters.description, filters.type, filters.itemType, filters.phase, filters.revision);
                return httpFactory.get(url);
            }

            function getItemsByClass(itemClass, pageable) {
                var url = "api/plm/items/class/" + itemClass + "?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getFilteredItems(pageable, filters) {
                var url = "api/plm/items/filteredItems?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&itemNumber={0}&itemName={1}&itemType={2}&item={3}&bomItem={4}&eco={5}&variance={6}&dcr={7}&dco={8}&ecr={9}&problemReport={10}&&ncr={11}&qcr={12}&related={13}&affectedItemIds={14}&replaceBomItem={15}&substitutePart={16}&alternatePart={17}&requirement={18}".format(filters.itemNumber, filters.itemName, filters.itemType, filters.item, filters.bomItem, filters.eco, filters.variance, filters.dcr, filters.dco,
                    filters.ecr, filters.problemReport, filters.ncr, filters.qcr, filters.related, filters.affectedItemIds, filters.replaceBomItem, filters.substitutePart, filters.alternatePart, filters.requirement);
                return httpFactory.get(url);
            }

            function getItemsByType(typeId, pageable) {
                var url = "api/plm/items/type/{0}?page={1}&size={2}&sort={3}:{4}".format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getItemTypeItems(typeId) {
                var url = "api/plm/items/itemType/" + typeId;
                return httpFactory.get(url);
            }

            function getNormalAndConfigurableItemsByItemType(typeId) {
                var url = "api/plm/items/itemType/" + typeId + "/normalconfigurable";
                return httpFactory.get(url);
            }

            function getItemsByIds(ids) {
                var url = "api/plm/items/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getLatestItemRevisionsByIds(ids) {
                var url = "api/plm/items/multiple/[" + ids + "]/latest";
                return httpFactory.get(url);
            }

            function getPageableItemsByIds(ids, pageable) {
                var url = "api/plm/items/pageable/[" + ids + "]?page={0}&size={1}".format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function getItemReferences(objects, property, callback) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getItemsByIds(ids).then(
                        function (data) {
                            var map = new Hashtable();
                            if (callback == "shared") {
                                getLatestRevisionReferences(data, 'latestRevision');
                            }
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

                            if (callback != 'shared' && callback != null && callback != undefined) {
                                callback();
                            }
                        }
                    );
                }
            }

            function findByItemNumber(itemNumber) {
                var url = "api/plm/items/find?itemNumber=" + itemNumber;
                return httpFactory.get(url);
            }

            function getItem(itemId) {
                var url = "api/plm/items/" + itemId;
                return httpFactory.get(url);
            }

            function createItem(item) {
                var url = "api/plm/items";
                return httpFactory.post(url, item);
            }

            function copyItem(item) {
                var url = "api/plm/items/revisions/copy";
                return httpFactory.post(url, item);
            }

            function createItemRevision(itemId, itemRevision) {
                var url = "api/plm/items" + itemId + "/revisions";
                return httpFactory.post(url, itemRevision);
            }

            function getLatestRevision(itemMaster) {
                var url = "api/plm/items/" + itemMaster + "/latest";
                return httpFactory.get(url);
            }

            function getRevisionId(revisionId) {
                if (revisionId != null && revisionId != undefined && revisionId != "") {
                    var url = "api/plm/items/revision/" + revisionId;
                    return httpFactory.get(url);
                }

            }

            function updateItem(itemRevision) {
                var url = "api/plm/items/revisions/" + itemRevision.id;
                return httpFactory.put(url, itemRevision);
            }

            function getItemRevision(itemRevisionId) {
                var url = "api/plm/items/revisions/" + itemRevisionId;
                return httpFactory.get(url);
            }

            function deleteItemRevision(itemRevisionId) {
                var url = "api/plm/items/revisions/" + itemRevisionId;
                return httpFactory.delete(url);
            }

            function reviseItem(itemId, itemRevision) {
                var url = "api/plm/items/" + itemId + "/revise";
                return httpFactory.post(url);
            }

            function deleteItem(itemId) {
                var url = "api/plm/items/" + itemId;
                return httpFactory.delete(url);
            }

            function updateItemMaster(item) {
                var url = "api/plm/items/" + item.id;
                return httpFactory.put(url, item);
            }

            function getItemRevisionAttributes(revisionId) {
                var url = "api/plm/items/revisions/" + revisionId + "/attributes";
                return httpFactory.get(url);
            }

            function getItemAttributes(itemId) {
                var url = "api/plm/items/" + itemId + "/attributes";
                return httpFactory.get(url);
            }

            function createItemAttribute(itemId, attribute) {
                var url = "api/plm/items/" + itemId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateImageValue(objectId, attributeId, file) {
                var url = "api/plm/items/updateImageValue/" + objectId + "/" + attributeId;
                return httpFactory.upload(url, file);
            }

            function createItemRevisionAttribute(revisionId, attribute) {
                var url = "api/plm/items/revisions/" + revisionId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateItemAttribute(itemId, attribute) {
                var url = "api/plm/items/" + itemId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function updateItemRevisionAttribute(revisionId, attribute) {
                var url = "api/plm/items/revisions/" + revisionId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function saveItemAttributes(itemId, attributes) {
                var url = "api/plm/items/" + itemId + "/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function saveImageItemAttribute(itemId, attribute) {
                var url = "api/plm/items/" + itemId + "/imageAttribute";
                return httpFactory.upload(url, attribute.imageValue, attribute);
            }

            function getItemChanges(itemId) {
                var url = "api/plm/items/" + itemId + "/changes";
                return httpFactory.get(url);
            }

            function getItemECRs(itemId) {
                var url = "api/plm/items/" + itemId + "/ecrs";
                return httpFactory.get(url);
            }

            function getItemDCOs(itemId) {
                var url = "api/plm/items/" + itemId + "/dcos";
                return httpFactory.get(url);
            }

            function getItemDCRs(itemId) {
                var url = "api/plm/items/" + itemId + "/dcrs";
                return httpFactory.get(url);
            }

            function getItemPRs(itemId) {
                var url = "api/plm/items/" + itemId + "/prs";
                return httpFactory.get(url);
            }

            function getProductItemPRs(itemId) {
                var url = "api/plm/items/" + itemId + "/product/prs";
                return httpFactory.get(url);
            }

            function getItemQCRs(itemId) {
                var url = "api/plm/items/" + itemId + "/qcrs";
                return httpFactory.get(url);
            }

            function getItemRevisions(itemId) {
                var url = "api/plm/items/" + itemId + "/revisions";
                return httpFactory.get(url);
            }

            function getItemRevisionIds(itemId) {
                var url = "api/plm/items/" + itemId + "/revisions/ids";
                return httpFactory.get(url);
            }

            function getRevisionsByIds(revisionIds) {
                var url = "api/plm/items/revisions/multiple";
                return httpFactory.post(url, revisionIds);
            }

            function getRevisionReferences(objects, property, callback) {
                var revisionIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && revisionIds.indexOf(object[property]) == -1) {
                        revisionIds.push(object[property]);
                    }
                });

                if (revisionIds.length > 0) {
                    getRevisionsByIds(revisionIds).then(
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

                            if (callback != null && callback != undefined) {
                                callback();
                            }
                        }
                    );
                }
                else {
                    if (callback != null && callback != undefined) {
                        callback();
                    }
                }
            }

            function getLatestRevisionReferences(objects, property) {
                var revisionIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && revisionIds.indexOf(object[property]) == -1) {
                        revisionIds.push(object[property]);
                    }
                });

                if (revisionIds.length > 0) {
                    getRevisionsByIds(revisionIds).then(
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

            function getItemRevisionHistory(itemId) {
                var url = "api/plm/items/{0}/revisions/history".format(itemId);
                return httpFactory.get(url);
            }

            function getAttributesByItemIdAndAttributeId(itemIds, attributeIds) {
                var url = "api/plm/items/objectAttributes";
                return httpFactory.post(url, [itemIds, attributeIds]);
            }

            function uploadImage(itemId, file) {
                var url = "api/plm/items/" + itemId + "/uploadImage";
                return httpFactory.upload(url, file);
            }


            function getImage(itemId) {
                var url = "api/plm/items/" + itemId + "/itemImageAttribute/download";
                return httpFactory.get(url);
            }

            function lockItem(item) {
                var url = "api/plm/items/lockItem/" + item.id;
                return httpFactory.put(url, item);
            }

            function subscribe(item) {
                var url = "api/plm/items/subscribe/" + item;
                return httpFactory.post(url);
            }

            function getSubscribeByPerson(itemId, personId) {
                var url = "api/plm/items/subscribe/" + itemId + "/" + personId;
                return httpFactory.get(url);
            }

            function exportItemReport(fileType, objects) {
                var url = "api/itemexcel/exports";
                url += "?fileType={0}".format(fileType);
                return httpFactory.post(url, objects);
            }

            function getProjectItem(itemId) {
                var url = "api/plm/items/" + itemId + "/projectItems";
                return httpFactory.get(url);
            }

            function getItemRequirements(itemId) {
                var url = "api/plm/items/" + itemId + "/itemRequirements";
                return httpFactory.get(url);
            }

            function importBom(id, file) {
                var url = "api/plm/items/" + id + "/importBom";
                return httpFactory.upload(url, file);
            }

            function importUploadedFile(file) {
                var url = "api/plm/items/import/file";
                return httpFactory.upload(url, file);
            }

            function getItemDetails(revisionId) {
                var url = "api/plm/items/details/" + revisionId;
                return httpFactory.get(url);
            }

            function exportBom(id, hierarchy, bomRule) {
                var url = "api/plm/items/" + id + "/exportBom?hierarchy=" + hierarchy + "&bomRule=" + bomRule;
                return httpFactory.get(url);
            }

            function createItemComment(comment) {
                var url = "api/plm/items/comments";
                return httpFactory.post(url, comment);
            }

            function setFileAsItemImage(item, file) {
                var url = "api/plm/items/setFileAsItemImage/" + item + "/" + file;
                return httpFactory.put(url);
            }

            function updateBomSequence() {
                var url = "api/plm/items/update/bom/sequence";
                return httpFactory.get(url);
            }


            function updateBomItemSequence(itemId) {
                var url = "api/plm/items/update/bom/" + itemId + "/sequence";
                return httpFactory.get(url);
            }

            function updateEmailTemplate(emailTemplate) {
                var url = "api/plm/items/update/email/template";
                return httpFactory.put(url, emailTemplate);
            }

            function getEmailTemplate(emailTemplate) {
                var url = "api/plm/items/email/template";
                return httpFactory.post(url, emailTemplate);
            }


            function getItemRevisionsForBomCompare(itemId) {
                var url = "api/plm/items/itemRevisions/bom/compare/" + itemId;
                return httpFactory.get(url);
            }

            function getHasBomItemsForBomCompare(itemId) {
                var url = "api/plm/items/hasBom/items/" + itemId;
                return httpFactory.get(url);
            }

            function getHasBomItemsForBomCompareLatestFalse(itemId) {
                var url = "api/plm/items/hasBom/items/latestFalse/" + itemId;
                return httpFactory.get(url);
            }


            function getAllItemsToCompare(itemId) {
                var url = "api/plm/items/item/to/items/" + itemId;
                return httpFactory.get(url);
            }

            function getItemsToCompare(pageable, filters) {
                var url = "api/plm/items/item/compareitems?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&latest={1}&itemId={2}&bomCompare={3}".format(filters.searchQuery, filters.latest, filters.itemId, filters.bomCompare);
                return httpFactory.get(url);
            }

            function getAllItemsToCompareLatestFalse(itemId) {
                var url = "api/plm/items/item/to/items/latestFalse/" + itemId;
                return httpFactory.get(url);
            }


            function getComparedIndividualItems(id, itemId, latest) {
                var url = "api/plm/items/bom/item/comparision/" + id + "/" + itemId + "/" + latest;
                return httpFactory.get(url);
            }

            function getComparedIndividualRevisions(id, itemId, latest) {
                var url = "api/plm/items/bom/revision/comparision/" + id + "/" + itemId + "/" + latest;
                return httpFactory.get(url);
            }

            function getInstanceItem(type) {
                var url = "api/plm/items/instances/" + type.id;
                return httpFactory.get(url);
            }

            function getItemInstances(itemId) {
                var url = "api/plm/items/" + itemId + "/itemInstances";
                return httpFactory.get(url);
            }

            function promtoItem(itemId, revision) {
                var url = "api/plm/items/revisions/" + itemId + "/promote";
                return httpFactory.put(url, revision);
            }

            function demoteItem(itemId, revision) {
                var url = "api/plm/items/revisions/" + itemId + "/demote";
                return httpFactory.put(url, revision);
            }

            function updateBomIncluions(item) {
                var url = "api/plm/items/bom/inclusion";
                return httpFactory.put(url, item);
            }

            function getWorkflows(typeId, type) {
                var url = "api/plm/items/workflow/" + typeId + "/itemType/" + type;
                return httpFactory.get(url);
            }

            function attachWorkflow(itemId, wfId) {
                var url = "api/plm/items/" + itemId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

            function deleteWorkflow(itemId) {
                var url = 'api/plm/items/' + itemId + "/workflow/delete";
                return httpFactory.delete(url);
            }


            function updateAllItemConfigurableAttributes() {
                var url = "api/plm/items/update/configurableAttributes";
                return httpFactory.get(url);
            }

            function updateItemConfigurableAttributes(itemId) {
                var url = "api/plm/items/" + itemId + "/update/configurableAttributes";
                return httpFactory.get(url);
            }

            function getConfigurableAttributes(itemId) {
                var url = "api/plm/items/" + itemId + "/configurableAttributes";
                return httpFactory.get(url);
            }

            function getItemConfigurableAttributes(itemId) {
                var url = "api/plm/items/" + itemId + "/itemConfigurableAttributes";
                return httpFactory.get(url);
            }

            function updateItemConfigAttribute(attribute) {
                var url = "api/plm/items/" + attribute.id + "/itemConfigurableAttributes";
                return httpFactory.put(url, attribute);
            }

            function getItemsByItemClass(itemClass) {
                var url = "api/plm/items/itemClass/" + itemClass;
                return httpFactory.get(url);
            }

            function getNormalAndConfigurableItemsByItemClass(itemClass) {
                var url = "api/plm/items/itemClass/" + itemClass + "/normalconfigurable";
                return httpFactory.get(url);
            }

            function getReleasedItemsByItemClass(itemClass) {
                var url = "api/plm/items/itemClass/" + itemClass + "/released";
                return httpFactory.get(url);
            }

            function getReleasedItemsByHasBom(hasBom) {
                var url = "api/plm/items/released?hasBom=" + hasBom;
                return httpFactory.get(url);
            }

            function getNormalItemsByItemClass(itemClass) {
                var url = "api/plm/items/itemClass/" + itemClass + "/normal";
                return httpFactory.get(url);
            }

            function getItemByRevision(revId) {
                var url = "api/plm/items/revisions/" + revId + "/item";
                return httpFactory.get(url);
            }

            function getComparedItems(id, itemId) {
                var url = "api/plm/comparisons/item/to/item/comparision/" + id + "/" + itemId;
                return httpFactory.get(url);
            }

            function getTotalItems() {
                var url = "api/plm/items/export/all";
                return httpFactory.get(url);
            }

            function searchAllItems(criteria) {
                var url = "api/plm/items/search/all";
                if (criteria.itemType == undefined || criteria.itemType == null) {
                    url += "?itemNumber={0}&itemName={1}&revision={2}&status={3}&itemClass={4}".format(criteria.itemNumber, criteria.itemName, criteria.revision, criteria.status, criteria.itemClass);
                } else {
                    url += "?itemNumber={0}&itemName={1}&revision={2}&status={3}&itemType={4}&itemClass={5}".format(criteria.itemNumber, criteria.itemName, criteria.revision, criteria.status, criteria.itemType.id, criteria.itemClass);
                }
                return httpFactory.get(url);
            }

            function advancedSearchAllItems(criteria) {
                var url = "api/plm/items/advancedsearch/all";

                return httpFactory.post(url, criteria);
            }

            function freeTextSearchAll(freetext, itemClass) {
                var url = "api/plm/items/freetextsearch/all";

                url += "?searchQuery={0}&itemClass={1}".format(freetext, itemClass);
                return httpFactory.get(url);
            }

            function getItemMasterCount() {
                var url = "api/plm/items/count";
                return httpFactory.get(url);
            }

            function getItemRevisionCount() {
                var url = "api/plm/items/revisions/count";
                return httpFactory.get(url);
            }

            function createItemSpecification(itemId, spec) {
                var url = "api/plm/items/" + itemId + "/specifications";
                return httpFactory.post(url, spec);
            }

            function createMultipleItemSpecification(itemId, spec) {
                var url = "api/plm/items/" + itemId + "/specifications/multiple";
                return httpFactory.post(url, spec);
            }

            function updateItemSpecification(itemId, spec) {
                var url = "api/plm/items/" + itemId + "/specifications" + spec.id;
                return httpFactory.put(url, spec);
            }

            function deleteItemSpecification(itemId, specId) {
                var url = "api/plm/items/" + itemId + "/specifications/" + specId;
                return httpFactory.delete(url);
            }

            function getItemSpecifications(itemId) {
                var url = "api/plm/items/" + itemId + "/specifications";
                return httpFactory.get(url);
            }

            function createSubstituteParts(itemId, substituteParts) {
                var url = "api/plm/items/bom/" + itemId + "/substituteparts/multiple";
                return httpFactory.post(url, substituteParts);
            }

            function getSubstituteParts(itemId) {
                var url = "api/plm/items/" + itemId + "/substituteparts";
                return httpFactory.get(url);
            }

            function deleteSubstitutePart(itemId, substitutePartId) {
                var url = "api/plm/items/" + itemId + "/substituteparts/" + substitutePartId;
                return httpFactory.delete(url);
            }

            function createAlternateParts(itemId, substituteParts) {
                var url = "api/plm/items/" + itemId + "/alternateparts/multiple";
                return httpFactory.post(url, substituteParts);
            }

            function createAlternatePart(itemId, substitutePart) {
                var url = "api/plm/items/" + itemId + "/alternateparts";
                return httpFactory.post(url, substitutePart);
            }

            function updateAlternatePart(itemId, substitutePart) {
                var url = "api/plm/items/" + itemId + "/alternateparts/" + substitutePart.id;
                return httpFactory.put(url, substitutePart);
            }

            function getAlternateParts(itemId) {
                var url = "api/plm/items/" + itemId + "/alternateparts";
                return httpFactory.get(url);
            }

            function deleteAlternatePart(itemId, alternatePartId) {
                var url = "api/plm/items/" + itemId + "/alternateparts/" + alternatePartId;
                return httpFactory.delete(url);
            }

            function getItemsByTypeId(pageable, filters) {
                var url = "api/plm/items/itemtype?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&type={1}".format(filters.searchQuery, filters.type);
                return httpFactory.get(url);
            }
        }
    });

