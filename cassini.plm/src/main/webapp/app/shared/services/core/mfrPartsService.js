define(['app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'],
    function (mdoule) {
        mdoule.factory('MfrPartsService', MfrPartsService);

        function MfrPartsService(httpFactory) {
            return {
                getManufacturerPartsByManufacturer: getManufacturerPartsByManufacturer,
                getManufacturepart: getManufacturepart,
                createManufacturepart: createManufacturepart,
                updateManufacturepart: updateManufacturepart,
                deleteManufacturepart: deleteManufacturepart,
                getManufacturerpartfiles: getManufacturerpartfiles,
                freeTextSearch: freeTextSearch,
                selectionFreeTextSearch: selectionFreeTextSearch,
                getManufacturerReference: getManufacturerReference,
                getManufacturerpartsByIds: getManufacturerpartsByIds,
                getManufacturerPartsByType: getManufacturerPartsByType,
                deleteManufacturePartFile: deleteManufacturePartFile,
                getItemMfrPart: getItemMfrPart,
                getItemMfrPartByMfrPart: getItemMfrPartByMfrPart,
                createItemMfrPart: createItemMfrPart,
                deleteItemMfrPart: deleteItemMfrPart,
                /*  getAllFileVersions: getAllFileVersions,*/
                getManufacturerParts: getManufacturerParts,
                getManufacturerpartLatestFiles: getManufacturerpartLatestFiles,
                searchPart: searchPart,
                getMfrPartTypeReferences: getMfrPartTypeReferences,
                getMfrPartTypesByIds: getMfrPartTypesByIds,
                getMfrPartAttributesWithHierarchy: getMfrPartAttributesWithHierarchy,
                saveMfrAttributes: saveMfrAttributes,
                createMfrPartAttribute: createMfrPartAttribute,
                updateMfrPartAttribute: updateMfrPartAttribute,
                getMfrPartAttributes: getMfrPartAttributes,
                getMostUsedMfrParts: getMostUsedMfrParts,
                getByPartNumber: getByPartNumber,
                getMfrPartsByType: getMfrPartsByType,
                getAllManufacturerPartTypesByName: getAllManufacturerPartTypesByName,
                getMfrPartFileName: getMfrPartFileName,
                getPartByNumberAndType: getPartByNumberAndType,
                getPartByMfrAndNumberAndType: getPartByMfrAndNumberAndType,
                getMfrPartAttributeValues: getMfrPartAttributeValues,
                getAllFileVersionAndCommentsAndDownloads: getAllFileVersionAndCommentsAndDownloads,
                updateMfrPartImageValue: updateMfrPartImageValue,
                deleteMfrPartByItem: deleteMfrPartByItem,
                updateManufacturePartfile: updateManufacturePartfile,
                updateMfrPartFileName: updateMfrPartFileName,
                getMfrPartCount: getMfrPartCount,
                promotePart: promotePart,
                demotePart: demotePart,
                createItemPart: createItemPart,
                getAllItemMfrPart: getAllItemMfrPart,
                getItemManufacturerParts: getItemManufacturerParts,
                getmfrPartItems: getmfrPartItems,
                getItemMfrParts: getItemMfrParts,
                saveItemMfrparts: saveItemMfrparts,
                attachWorkflow: attachWorkflow,
                deleteWorkflow: deleteWorkflow,
                getWorkflows: getWorkflows,
                updateItemMfrParts: updateItemMfrParts,
                getMfrPartChanges: getMfrPartChanges,
                getMfrPartQCRs: getMfrPartQCRs,
                getMfrPartNCRs: getMfrPartNCRs,
                getMfrParts: getMfrParts,
                updateItemPart: updateItemPart,
                getMfrPartVariances: getMfrPartVariances,
                createItemParts: createItemParts,
                uploadMfrPartImage: uploadMfrPartImage,
                getMfrPartsByTypeId: getMfrPartsByTypeId,
                getMfrPartTypeTree: getMfrPartTypeTree,
                subscribePart: subscribePart,
                addInspectionReportReviewer: addInspectionReportReviewer,
                updateInspectionReportReviewer: updateInspectionReportReviewer,
                deleteInspectionReportReviewer: deleteInspectionReportReviewer,
                getMfrPartUsedCount: getMfrPartUsedCount
            };

            function getMfrPartFileName(mfrId, partId, name) {
                var url = "api/plm/mfr/" + mfrId + "/parts/" + partId + "/files/byFileName/" + name;
                return httpFactory.get(url);
            }

            function getAllManufacturerPartTypesByName(name) {
                var url = "api/plm/mfr/parts/byPartName/" + name;
                return httpFactory.get(url);
            }

            function createMfrPartAttribute(partId, attribute) {
                var url = "api/plm/mfr/parts/" + partId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateMfrPartAttribute(partId, attribute) {
                var url = "api/plm/mfr/parts/" + partId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function getMfrPartAttributes(partId) {
                var url = "api/plm/mfr/parts/" + partId + "/attributes";
                return httpFactory.get(url);
            }

            function getMfrPartChanges(partId) {
                var url = "api/plm/mfr/parts/" + partId + "/changes";
                return httpFactory.get(url);
            }

            function getMfrPartVariances(partId) {
                var url = "api/plm/mfr/parts/" + partId + "/variances";
                return httpFactory.get(url);
            }

            function getAllFileVersionAndCommentsAndDownloads(mfrId, partId, fileId, type) {
                var url = "api/plm/mfr/" + mfrId + "/parts/" + partId + "/files/" + fileId + "/versionComments/" + type;
                return httpFactory.get(url);
            }


            /*   function getAllFileVersionAndCommentsAndDownloads(mfrId,partId, fileId, type) {
             var url = 'api/plm/mfr/' + mfrId + "/files/" + fileId + "/versionComments/" + type;
             return httpFactory.get(url);
             }*/

            function getItemMfrPart(itemId) {
                var url = "api/plm/items/" + itemId + "/mpns"
                return httpFactory.get(url);
            }

            function getItemMfrPartByMfrPart(itemId) {
                var url = "api/plm/items/" + itemId + "/byMfrPart/mpns";
                return httpFactory.get(url);
            }

            
            function getMfrPartUsedCount(id, partId) {
                var url = "api/plm/mfr/" + id + "/parts/"+ partId +"/usedCount";
                return httpFactory.get(url);
            }

            function createItemMfrPart(itemId, mfrParts) {
                var url = "api/plm/items/" + itemId + "/multiple/mpns";
                return httpFactory.post(url, mfrParts);
            }

            function deleteItemMfrPart(mfrPart) {
                var url = "api/plm/items/mpns/" + mfrPart;
                return httpFactory.delete(url);
            }

            function deleteMfrPartByItem(itemId, mfrPart) {
                var url = "api/plm/items/" + itemId + "/mfrPart/" + mfrPart;
                return httpFactory.delete(url);
            }

            function freeTextSearch(pageable, freetext, mfrId) {
                var url = "api/plm/mfr/freesearch/parts?page={0}&size={1}&sort={2}".
                    format(pageable.page, pageable.size, pageable.sort.field);

                url += "&searchQuery={0}&manufacturer={1}".
                    format(freetext, mfrId);
                return httpFactory.get(url);
            }

            function selectionFreeTextSearch(pageable, freetext) {
                var url = "api/plm/mfr/selectionSearch/parts?page={0}&size={1}&sort={2}&searchQuery={3}".
                    format(pageable.page, pageable.size, pageable.sort.field, freetext);

                return httpFactory.get(url);
            }

            function getManufacturerPartsByManufacturer(mfrid) {
                var url = "api/plm/mfr/" + mfrid + "/parts";
                return httpFactory.get(url);
            }

            function getManufacturerParts(pageable) {
                var url = "api/plm/mfr/allParts?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getItemManufacturerParts(filter, pageable) {
                var url = "api/plm/mfr/allParts?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&partNumber={0}&partName={1}&mfrPartType={2}&description={3}&item={4}&manufacturer={5}&mco={6}&mcoReplacement={7}&mcoAffectedItems={8}&related={9}&ncr={10}&qcr={11}&inspection={12}&variance={13}&supplier={14}&declaration={15}"
                    .format(filter.partNumber, filter.partName, filter.mfrPartType, filter.description, filter.item, filter.manufacturer, filter.mco, filter.mcoReplacement,
                    [filter.mcoAffectedItems], filter.related, filter.ncr, filter.qcr, filter.inspection, filter.variance, filter.supplier, filter.declaration);
                return httpFactory.get(url);
            }

            function getManufacturepart(partId) {
                var url = "api/plm/mfr/parts/" + partId;
                return httpFactory.get(url);
            }

            function createManufacturepart(mfrPart, mfrId) {
                var url = "api/plm/mfr/" + mfrId + "/parts";
                return httpFactory.post(url, mfrPart);
            }

            function updateManufacturepart(mfrId, part) {
                var url = "api/plm/mfr/" + mfrId + "/parts/" + part.id;
                return httpFactory.put(url, part);
            }

            function deleteManufacturepart(mfr, partId) {
                var url = "api/plm/mfr/" + mfr + "/parts/" + partId;
                return httpFactory.delete(url);
            }

            function deleteManufacturePartFile(mfrId, partId, fileId) {
                var url = "api/plm/mfr/" + mfrId + "/parts/" + partId + "/files/" + fileId;
                return httpFactory.delete(url);
            }

            function getManufacturerpartfiles(mfrId, partId) {
                var url = "api/plm/mfr/" + mfrId + "/parts/" + partId + "/files";
                return httpFactory.get(url);
            }

            function getManufacturerpartLatestFiles(mfrId, partId) {
                var url = "api/plm/mfr/" + mfrId + "/parts/" + partId + "/latestFiles";
                return httpFactory.get(url);
            }

            function getManufacturerpartsByIds(ids) {
                var url = "api/plm/mfr/parts/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getManufacturerPartsByType(typeId) {
                var url = "api/plm/mfr/parts/type/" + typeId;
                return httpFactory.get(url);
            }


            function getByPartNumber(partNumber) {
                var url = "api/plm/mfr/byPartNumber/" + partNumber;
                return httpFactory.get(url);
            }

            function getPartByNumberAndType(partNumber, type) {
                var url = "api/plm/mfr/partByNumberAndType/" + partNumber + "/" + type;
                return httpFactory.get(url);
            }

            function getPartByMfrAndNumberAndType(mfrId, partNumber, type) {
                var url = "api/plm/mfr/" + mfrId + "/partByMfrAndNumberAndType/" + partNumber + "/" + type;
                return httpFactory.get(url);
            }

            function getMultipleManufacturers(mftIds) {
                var url = "api/plm/mfr/multiple/[" + mftIds + "]";
                return httpFactory.get(url);
            }

            function getMfrPartAttributesWithHierarchy(type) {
                var url = "api/plm/mfr/mfrTypes/" + type + "/attributes/parts?hierarchy=true";
                return httpFactory.get(url);
            }

            function saveMfrAttributes(mfrPartId, attributes) {
                var url = "api/plm/mfr/" + mfrPartId + "/attributes/multiple/parts";
                return httpFactory.post(url, attributes);
            }


            function searchPart(pageable, criteria) {
                var url = "api/plm/items/search/parts?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                if (criteria.mfrPartType == null) {
                    url += "&partNumber={0}&partName={1}&status={2}".
                        format(criteria.partNumber, criteria.partName, criteria.status);
                } else {
                    url += "&partNumber={0}&partName={1}&status={2}&mfrPartType={3}".
                        format(criteria.partNumber, criteria.partName, criteria.status, criteria.mfrPartType.id);
                }
                return httpFactory.get(url);
            }

            function getManufacturerReference(objects, property) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getMultipleManufacturers(ids).then(
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

            function getMfrPartTypesByIds(ids) {
                var url = "api/plm/mfr/mfrPartTypes/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getMostUsedMfrParts(pageable) {
                var url = "api/plm/mfr/mostused/parts?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getMfrPartsByType(typeId, pageable) {
                var url = "api/plm/mfr/partType/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function updateManufacturePartfile(mfrId, partId, file) {
                var url = "api/plm/mfr/" + mfrId + "/parts/" + partId + "/files/" + file.id;
                return httpFactory.put(url, file);
            }

            function getMfrPartTypeReferences(objects, property, callback) {
                var ids = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && ids.indexOf(object[property]) == -1) {
                        ids.push(object[property]);
                    }
                });

                if (ids.length > 0) {
                    getMfrPartTypesByIds(ids).then(
                        function (data) {
                            var map = new Hashtable();

                            angular.forEach(data, function (mfrPartType) {
                                map.put(mfrPartType.id, mfrPartType);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var mfrPartType = map.get(object[property]);
                                    if (mfrPartType != null) {
                                        object[property + "Object"] = mfrPartType;
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


            function getMfrPartAttributeValues(attributeId) {
                var url = "api/plm/mfr/mfrParts/attributes/" + attributeId;
                return httpFactory.get(url);
            }

            function updateMfrPartImageValue(objectId, attributeId, file) {
                var url = "api/plm/mfr/updateMfrPartImageValue/" + objectId + "/" + attributeId;
                return httpFactory.upload(url, file);
            }

            function updateMfrPartFileName(mfrId, partId, fileId, newFileName) {
                var url = "api/plm/mfr/" + mfrId + "/parts/" + partId + "/files/" + fileId + "/reNameFile";
                return httpFactory.put(url, newFileName)
            }

            function getMfrPartCount(mfId) {
                var url = "api/plm/mfr/parts/" + mfId + "/count";
                return httpFactory.get(url);
            }

            function promotePart(mfId, part) {
                var url = "api/plm/mfr/parts/" + mfId + "/promote";
                return httpFactory.put(url, part);
            }

            function demotePart(mfId, part) {
                var url = "api/plm/mfr/parts/" + mfId + "/demote";
                return httpFactory.put(url, part);
            }

            function createItemPart(itemId, mfrParts) {
                var url = "api/plm/items/" + itemId + "/mfrPart";
                return httpFactory.post(url, mfrParts);
            }

            function createItemParts(itemId, mfrParts) {
                var url = "api/plm/items/" + itemId + "/mfrPart/multiple";
                return httpFactory.post(url, mfrParts);
            }

            function updateItemPart(itemId, mfrParts) {
                var url = "api/plm/items/" + itemId + "/mfrPart/" + mfrParts.id;
                return httpFactory.put(url, mfrParts);
            }

            function getAllItemMfrPart(itemId) {
                var url = "api/plm/items/" + itemId + "/mfParts";
                return httpFactory.get(url);
            }

            function getmfrPartItems(mfrId, selectedItems) {
                var url = "api/plm/mfr/parts/" + mfrId + "/mfParts/status/multiple";
                return httpFactory.post(url, selectedItems);
            }

            function getItemMfrParts(mfrId) {
                var url = "api/plm/mfr/parts/" + mfrId + "/items";
                return httpFactory.get(url);
            }

            function saveItemMfrparts(mfrId, itemMfrParts) {
                var url = "api/plm/mfr/parts/items/" + mfrId;
                return httpFactory.post(url, itemMfrParts);
            }

            function attachWorkflow(partId, wfId) {
                var url = "api/plm/mfr/parts/" + partId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

            function deleteWorkflow(partId) {
                var url = 'api/plm/mfr/parts/' + partId + "/workflow/delete";
                return httpFactory.delete(url);
            }


            function getWorkflows(typeId, type) {
                var url = "api/plm/mfr/parts/workflow/" + typeId + "/mfrPartType/" + type;
                return httpFactory.get(url);
            }

            function updateItemMfrParts(partId, part) {
                var url = "api/plm/mfr/parts/" + partId + "/partItems";
                return httpFactory.put(url, part);
            }

            function getMfrPartQCRs(partId) {
                var url = "api/plm/mfr/parts/" + partId + "/qcrs";
                return httpFactory.get(url);
            }

            function getMfrPartNCRs(partId) {
                var url = "api/plm/mfr/parts/" + partId + "/ncrs";
                return httpFactory.get(url);
            }

            function getMfrParts(pageable, filters) {
                var url = "api/plm/mfr/parts/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&partNumber={0}&partName={1}&description={2}&mfrPartType={3}&manufacturer={4}&freeTextSearch={5}&searchQuery={6}&type={7}".
                    format(filters.partNumber, filters.partName, filters.description, filters.mfrPartType, filters.manufacturer, filters.freeTextSearch, filters.searchQuery,filters.type);
                return httpFactory.get(url);
            }

            function uploadMfrPartImage(partId, file) {
                var url = "api/plm/mfr/parts/" + partId + "/image";
                return httpFactory.upload(url, file);
            }

            function getMfrPartsByTypeId(pageable, filters) {
                var url = "api/plm/mfr/parts/type?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&type={1}".format(filters.searchQuery, filters.type);
                return httpFactory.get(url);
            }

            function getMfrPartTypeTree() {
                var url = "api/plm/mfr/parts/type/tree";
                return httpFactory.get(url);
            }

            function subscribePart(id) {
                var url = "api/plm/mfr/parts/" + id + "/subscribe";
                return httpFactory.post(url);
            }

            function addInspectionReportReviewer(reportId, reviewer) {
                var url = "api/plm/mfr/parts/inspectionreports/" + reportId + "/reviewers";
                return httpFactory.post(url, reviewer);
            }

            function updateInspectionReportReviewer(reportId, reviewer) {
                var url = "api/plm/mfr/parts/inspectionreports/" + reportId + "/reviewers/" + reviewer.id;
                return httpFactory.put(url, reviewer);
            }

            function deleteInspectionReportReviewer(reportId, reviewerId) {
                var url = "api/plm/mfr/parts/inspectionreports/" + reportId + "/reviewers/" + reviewerId;
                return httpFactory.delete(url);
            }

        }
    }
)
;
