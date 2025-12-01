define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('DCRService', DCRService);

        function DCRService(httpFactory) {
            return {

                createDCR: createDCR,
                getDCR: getDCR,
                updateDCR: updateDCR,
                deleteDCR: deleteDCR,
                getAllDCRs: getAllDCRs,
                attachDcrWorkflow: attachDcrWorkflow,
                deleteDcrWorkflow: deleteDcrWorkflow,
                getDCRCounts: getDCRCounts,

                createDcrItem: createDcrItem,
                getAffectedItems: getAffectedItems,
                getFilteredItems: getFilteredItems,
                createDcrRelatedItems: createDcrRelatedItems,
                getDcrRelatedItems: getDcrRelatedItems,
                deleteDcrAffectedItem: deleteDcrAffectedItem,
                deleteDcrRelatedItem: deleteDcrRelatedItem,

                getDCRFiles: getDCRFiles,
                getDcrFilesByName: getDcrFilesByName,
                updateDcrFileDownloadHistory: updateDcrFileDownloadHistory,
                updateDCRFile: updateDCRFile,
                deleteDCRFile: deleteDCRFile,
                updateFileName: updateFileName,
                getLatestUploadedFile: getLatestUploadedFile,
                moveDCRFileToFolder: moveDCRFileToFolder,
                getDCRFolderChildren: getDCRFolderChildren,
                deleteDCRFolder: deleteDCRFolder,
                uploadDCRFolderFiles: uploadDCRFolderFiles,
                pasteDcrFilesFromClipboard: pasteDcrFilesFromClipboard,
                undoCopiedFiles: undoCopiedFiles,
                createDCRFolder: createDCRFolder,
                getAllFileVersions: getAllFileVersions,
                getAllDcrFileVersionComments: getAllDcrFileVersionComments,
                updateDcrFileDescription: updateDcrFileDescription,
                createDcrItems: createDcrItems,
                getStatus:  getStatus,
                getUrgency: getUrgency,
                getChangeAnalysts: getChangeAnalysts,
                getOriginators: getOriginators,
                getRequestedBy: getRequestedBy,
                getChangeReasonType: getChangeReasonType
            };

            function createDCR(dcr) {
                var url = "api/cms/dcrs";
                return httpFactory.post(url, dcr)
            }

            function getDCR(id) {
                var url = "api/cms/dcrs/" + id;
                return httpFactory.get(url)
            }

            function updateDCR(dcr) {
                var url = "api/cms/dcrs/" + dcr.id;
                return httpFactory.put(url, dcr);
            }

            function deleteDCR(dcr) {
                var url = "api/cms/dcrs/" + dcr;
                return httpFactory.delete(url);
            }

            function attachDcrWorkflow(dcrId, wfId) {
                var url = "api/cms/dcrs/" + dcrId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

            function deleteDcrWorkflow(dcrId) {
                var url = 'api/cms/dcrs/' + dcrId + "/workflow/delete";
                return httpFactory.delete(url);
            }

            function getAllDCRs(pageable, filters) {
                var url = "api/cms/dcrs/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&crNumber={0}&crType={1}&descriptionofChange={2}&searchQuery={3}&originator={4}&changeAnalyst={5}&requestedBy={6}&urgency={7}&changeReasonType={8}&status={9}".
                    format(filters.crNumber, filters.crType, filters.descriptionofChange, filters.searchQuery,filters.originator, filters.changeAnalyst, filters.requestedBy,filters.urgency,filters.changeReasonType, filters.status);
                return httpFactory.get(url);
            }

            function createDcrItem(dcrId, dcrItem) {
                var url = "api/cms/dcrs/" + dcrId + "/affectedItem";
                return httpFactory.post(url, dcrItem)
            }

            function createDcrItems(dcrId, dcrItem) {
                var url = "api/cms/dcrs/" + dcrId + "/affectedItem/multiple";
                return httpFactory.post(url, dcrItem)
            }

            function getAffectedItems(dcr) {
                var url = "api/cms/dcrs/affectedItems/" + dcr;
                return httpFactory.get(url);
            }

            function getFilteredItems(pageable, filters) {
                var url = "api/cms/dcrs/filteredItems?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&itemNumber={0}&itemName={1}&itemType={2}&dcr={3}&dco={4}&ecr={5}&problemReport={6}&ncr={7}&qcr={8}&inspection={9}&variance={10}&related={11}".
                    format(filters.itemNumber, filters.itemName, filters.itemType, filters.dcr, filters.dco, filters.ecr, filters.problemReport,
                    filters.ncr, filters.qcr, filters.inspection, filters.variance, filters.related);
                return httpFactory.get(url);
            }

            function createDcrRelatedItems(dcr, items) {
                var url = "api/cms/dcrs/" + dcr + "/relatedItems/multiple";
                return httpFactory.post(url, items);
            }

            function getDcrRelatedItems(dcr) {
                var url = "api/cms/dcrs/relatedItems/" + dcr;
                return httpFactory.get(url);
            }

            function deleteDcrAffectedItem(dcr, id) {
                var url = "api/cms/dcrs/" + dcr + "/affectedItem/delete/" + id;
                return httpFactory.delete(url)
            }

            function deleteDcrRelatedItem(dcr, id) {
                var url = "api/cms/dcrs/" + dcr + "/relatedItem/delete/" + id;
                return httpFactory.delete(url)
            }

            function getDCRFiles(dcrId) {
                var url = "api/cms/dcrs/" + dcrId + "/files";
                return httpFactory.get(url);
            }

            function getDcrFilesByName(dcrId, name) {
                var url = "api/cms/dcrs/" + dcrId + "/files/byName/" + name;
                return httpFactory.get(url);
            }

            function updateDcrFileDownloadHistory(dcrId, fileId) {
                var url = "api/cms/dcrs/" + dcrId + "/files/" + fileId + "/fileDownloadHistory";
                return httpFactory.post(url);
            }

            function updateDCRFile(dcrId, file) {
                var url = "api/cms/dcrs/" + dcrId + "/files/" + file.id;
                return httpFactory.put(url, file);
            }

            function deleteDCRFile(dcrId, fileId) {
                var url = "api/cms/dcrs/" + dcrId + "/files/" + fileId;
                return httpFactory.delete(url);
            }

            function updateFileName(dcrId, fileId, newFileName) {
                var url = 'api/cms/dcrs/' + dcrId + "/files/" + fileId + "/renameFile";
                return httpFactory.put(url, newFileName)
            }

            function getLatestUploadedFile(dcrId, fileId) {
                var url = 'api/cms/dcrs/' + dcrId + "/files/" + fileId + "/latest/uploaded";
                return httpFactory.get(url);
            }

            function moveDCRFileToFolder(folderId, file) {
                var url = 'api/cms/dcrs/' + folderId + "/move";
                return httpFactory.put(url, file);
            }

            function getDCRFolderChildren(dcrId, folderId) {
                var url = 'api/cms/dcrs/' + folderId + "/children";
                return httpFactory.get(url);
            }

            function deleteDCRFolder(dcrId, folderId) {
                var url = 'api/cms/dcrs/' + dcrId + "/folder/" + folderId + "/delete";
                return httpFactory.delete(url)
            }

            function uploadDCRFolderFiles(dcrId, folderId, files) {
                var url = 'api/cms/dcrs/' + dcrId + "/folder/" + folderId + "/upload";
                return httpFactory.uploadMultiple(url, files);
            }

            function pasteDcrFilesFromClipboard(dcrId, fileId, files) {
                var url = 'api/cms/dcrs/' + dcrId + "/files/paste?fileId=" + fileId;
                return httpFactory.put(url, files);
            }

            function undoCopiedFiles(dcrId, files) {
                var url = 'api/cms/dcrs/' + dcrId + "/files/undo";
                return httpFactory.put(url, files);
            }

            function createDCRFolder(dcrId, folder) {
                var url = 'api/cms/dcrs/' + dcrId + "/folder";
                return httpFactory.post(url, folder)
            }

            function getAllFileVersions(dcrId, fileId) {
                var url = "api/cms/dcrs/" + dcrId + "/files/" + fileId + "/versions";
                return httpFactory.get(url)
            }

            function getAllDcrFileVersionComments(fileId, objectType) {
                var url = 'api/cms/dcrs/files/' + fileId + "/versionComments/" + objectType;
                return httpFactory.get(url);
            }

            function updateDcrFileDescription(files) {
                var url = 'api/cms/dcrs/description/' + 0 + "/files/" + files.fileId;
                return httpFactory.put(url, files);
            }

            function getDCRCounts(id) {
                var url = "api/cms/dcrs/" + id + "/details";
                return httpFactory.get(url);
            }


            function getStatus() {
                var url = "api/cms/dcrs/status" ;
                return httpFactory.get(url);
            }

            
            function getChangeReasonType() {
                var url = "api/cms/dcrs/changeReasonType" ;
                return httpFactory.get(url);
            }

            function getUrgency() {
                var url = "api/cms/dcrs/urgency" ;
                return httpFactory.get(url);
            }

            function getChangeAnalysts() {
                var url = "api/cms/dcrs/changeAnalysts";
                return httpFactory.get(url);
            }

            function getOriginators() {
                var url = "api/cms/dcrs/originators";
                return httpFactory.get(url);
            }

            function getRequestedBy() {
                var url = "api/cms/dcrs/requestedBy";
                return httpFactory.get(url);
            }
        }
    }
);