define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('DCOService', DCOService);

        function DCOService(httpFactory) {
            return {

                createDCO: createDCO,
                getDCO: getDCO,
                updateDCO: updateDCO,
                deleteDCO: deleteDCO,
                getAllDCOs: getAllDCOs,
                attachDcoWorkflow: attachDcoWorkflow,
                deleteDcoWorkflow: deleteDcoWorkflow,
                getChangeTypeReferences: getChangeTypeReferences,
                getDCOCounts: getDCOCounts,
                deleteDcoChangeRequest: deleteDcoChangeRequest,

                /*getAllDCRs: getAllDCRs,*/
                deleteDcoAffectedItem: deleteDcoAffectedItem,
                deleteDcoRelatedItem: deleteDcoRelatedItem,
                getFilteredDcrItems: getFilteredDcrItems,
                createDCOChangeRequest: createDCOChangeRequest,
                getDCOChangeRequestItems: getDCOChangeRequestItems,
                createDcoAffectedItem: createDcoAffectedItem,
                getDcoAffectedItems: getDcoAffectedItems,
                createDCOObject: createDCOObject,
                updateAttributeAttachmentValues: updateAttributeAttachmentValues,
                updateAttributeImageValue: updateAttributeImageValue,

                getDCOFiles: getDCOFiles,
                getDcoFilesByName: getDcoFilesByName,
                updateDcoFileDownloadHistory: updateDcoFileDownloadHistory,
                updateDCOFile: updateDCOFile,
                deleteDCOFile: deleteDCOFile,
                updateFileName: updateFileName,
                getLatestUploadedFile: getLatestUploadedFile,
                moveDCOFileToFolder: moveDCOFileToFolder,
                getDCOFolderChildren: getDCOFolderChildren,
                deleteDCOFolder: deleteDCOFolder,
                uploadDCOFolderFiles: uploadDCOFolderFiles,
                pasteDcoFilesFromClipboard: pasteDcoFilesFromClipboard,
                undoCopiedFiles: undoCopiedFiles,
                createDCOFolder: createDCOFolder,
                getAllFileVersions: getAllFileVersions,
                getAllDcoFileVersionComments: getAllDcoFileVersionComments,
                updateDcoFileDescription: updateDcoFileDescription,
                attachNewDcoWorkflow: attachNewDcoWorkflow,
                getChangeAnalysts: getChangeAnalysts,
                getStatus: getStatus

            };

            function createDCO(dco) {
                var url = "api/cms/dcos";
                return httpFactory.post(url, dco)
            }

            function getDCO(id) {
                var url = "api/cms/dcos/" + id;
                return httpFactory.get(url)
            }

            function updateDCO(dco) {
                var url = "api/cms/dcos/" + dco.id;
                return httpFactory.put(url, dco);
            }

            function deleteDCO(dco) {
                var url = "api/cms/dcos/" + dco;
                return httpFactory.delete(url);
            }

            function attachDcoWorkflow(dcoId, wfId) {
                var url = "api/cms/dcos/" + dcoId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

            function attachNewDcoWorkflow(dcoId, wfId, rule, statusId) {
                var url = "api/cms/dcos/" + dcoId + "/attachnewworkflow/" + wfId + "?rule=" + rule + "&status=" + statusId;
                return httpFactory.get(url);
            }
            function deleteDcoWorkflow(dcoId) {
                var url = 'api/cms/dcos/' + dcoId + "/workflow/delete";
                return httpFactory.delete(url);
            }


            function getAllDCOs(pageable, filters) {
                var url = "api/cms/dcos/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&dcoNumber={0}&description={1}&dcoType={2}&searchQuery={3}&changeAnalyst={4}&status={5}".
                    format(filters.dcoNumber, filters.description, filters.dcoType, filters.searchQuery, filters.changeAnalyst, filters.status);
                return httpFactory.get(url);
            }

            /* function getAllDCRs(pageable, filters) {
             var url = "api/cms/dcrs/all?page={0}&size={1}&sort={2}:{3}".
             format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
             url += "&name={0}&number={1}&planType={2}&description={3}&searchQuery={4}".
             format(filters.name, filters.number, filters.planType, filters.description, filters.searchQuery);
             return httpFactory.get(url);
             }*/

            function deleteDcoAffectedItem(dco, id) {
                var url = "api/cms/dcos/" + dco + "/affectedItem/delete/" + id;
                return httpFactory.delete(url)
            }

            function deleteDcoRelatedItem(dco, id) {
                var url = "api/cms/dcos/" + dco + "/relatedItem/delete/" + id;
                return httpFactory.delete(url)
            }

            function getFilteredDcrItems(pageable, filters) {
                var url = "api/cms/dcos/filtered/dcr/items?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&crNumber={0}&crType={1}&dco={2}&title={3}".
                    format(filters.crNumber, filters.crType, filters.dco, filters.title);
                return httpFactory.get(url);
            }

            function createDCOChangeRequest(dco, items) {
                var url = "api/cms/dcos/" + dco + "/changeRequest/multiple";
                return httpFactory.post(url, items);
            }

            function getDCOChangeRequestItems(dco) {
                var url = "api/cms/dcos/" + dco + "/changeRequestItems";
                return httpFactory.get(url);
            }

            function getDcoAffectedItems(dco) {
                var url = "api/cms/dcos/affectedItems/" + dco;
                return httpFactory.get(url);
            }

            function createDcoAffectedItem(dcoId, dcoItem) {
                var url = "api/cms/dcos/" + dcoId + "/affectedItem";
                return httpFactory.post(url, dcoItem)
            }


            function createDCOObject(objectType, dto) {
                var url = "api/cms/dcos/" + objectType + "/object";
                return httpFactory.post(url, dto);
            }

            function updateAttributeAttachmentValues(objectType, objectId, attributeId, file) {
                var url = "api/cms/dcos/" + objectType + "/" + objectId + "/" + attributeId + "/attachments/upload";
                return httpFactory.uploadMultiple(url, file);
            }

            function updateAttributeImageValue(objectType, objectId, attributeId, file) {
                var url = "api/cms/dcos/" + objectType + "/" + objectId + "/" + attributeId + "/images/upload";
                return httpFactory.upload(url, file);
            }

            function getDCOFiles(dcoId) {
                var url = "api/cms/dcos/" + dcoId + "/files";
                return httpFactory.get(url);
            }

            function getDcoFilesByName(dcoId, name) {
                var url = "api/cms/dcos/" + dcoId + "/files/byName/" + name;
                return httpFactory.get(url);
            }

            function updateDcoFileDownloadHistory(dcoId, fileId) {
                var url = "api/cms/dcos/" + dcoId + "/files/" + fileId + "/fileDownloadHistory";
                return httpFactory.post(url);
            }

            function updateDCOFile(dcoId, file) {
                var url = "api/cms/dcos/" + dcoId + "/files/" + file.id;
                return httpFactory.put(url, file);
            }

            function deleteDCOFile(dcoId, fileId) {
                var url = "api/cms/dcos/" + dcoId + "/files/" + fileId;
                return httpFactory.delete(url);
            }

            function updateFileName(dcoId, fileId, newFileName) {
                var url = 'api/cms/dcos/' + dcoId + "/files/" + fileId + "/renameFile";
                return httpFactory.put(url, newFileName)
            }

            function getLatestUploadedFile(dcoId, fileId) {
                var url = 'api/cms/dcos/' + dcoId + "/files/" + fileId + "/latest/uploaded";
                return httpFactory.get(url);
            }

            function moveDCOFileToFolder(folderId, file) {
                var url = 'api/cms/dcos/' + folderId + "/move";
                return httpFactory.put(url, file);
            }

            function getDCOFolderChildren(dcoId, folderId) {
                var url = 'api/cms/dcos/' + folderId + "/children";
                return httpFactory.get(url);
            }

            function deleteDCOFolder(dcoId, folderId) {
                var url = 'api/cms/dcos/' + dcoId + "/folder/" + folderId + "/delete";
                return httpFactory.delete(url)
            }

            function uploadDCOFolderFiles(dcoId, folderId, files) {
                var url = 'api/cms/dcos/' + dcoId + "/folder/" + folderId + "/upload";
                return httpFactory.uploadMultiple(url, files);
            }

            function pasteDcoFilesFromClipboard(dcoId, fileId, files) {
                var url = 'api/cms/dcos/' + dcoId + "/files/paste?fileId=" + fileId;
                return httpFactory.put(url, files);
            }

            function undoCopiedFiles(dcoId, files) {
                var url = 'api/cms/dcos/' + dcoId + "/files/undo";
                return httpFactory.put(url, files);
            }

            function createDCOFolder(dcoId, folder) {
                var url = 'api/cms/dcos/' + dcoId + "/folder";
                return httpFactory.post(url, folder)
            }

            function getAllFileVersions(dcoId, fileId) {
                var url = "api/cms/dcos/" + dcoId + "/files/" + fileId + "/versions";
                return httpFactory.get(url)
            }

            function getAllDcoFileVersionComments(fileId, objectType) {
                var url = 'api/cms/dcos/files/' + fileId + "/versionComments/" + objectType;
                return httpFactory.get(url);
            }

            function updateDcoFileDescription(files) {
                var url = 'api/cms/dcos/description/' + 0 + "/files/" + files.fileId;
                return httpFactory.put(url, files);
            }

            function getChangeTypesByIds(ids) {
                var url = "api/cms/dcos/changeTypes/multiple/[" + ids + "]";
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

            function getDCOCounts(id) {
                var url = "api/cms/dcos/" + id + "/details";
                return httpFactory.get(url);
            }

            function deleteDcoChangeRequest(dcoId, crId) {
                var url = "api/cms/dcos/" + dcoId + "/changeRequestItems/" + crId;
                return httpFactory.delete(url);
            }

            function getChangeAnalysts() {
                var url = "api/cms/dcos/changeAnalysts";
                return httpFactory.get(url);
            }

            function getStatus() {
                var url = "api/cms/dcos/status" ;
                return httpFactory.get(url);
            }
        }
    }
);