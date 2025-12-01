define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('VarianceService', VarianceService);

        function VarianceService(httpFactory) {
            return {
                createVariance: createVariance,
                updateVariance: updateVariance,
                deleteVariance: deleteVariance,
                getVariance: getVariance,
                getVariances: getVariances,
                getAllVariance: getAllVariance,
                getMultipleVariance: getMultipleVariance,
                getVarianceByType: getVarianceByType,
                getVarianceCounts: getVarianceCounts,
                getAllFileVersions: getAllFileVersions,
                getVarianceByNumber: getVarianceByNumber,
                getVarianceByItem: getVarianceByItem,

                getVarianceFiles: getVarianceFiles,
                getVarianceFile: getVarianceFile,
                getLatestUploadedFile: getLatestUploadedFile,
                updateVarianceFile: updateVarianceFile,
                deleteVarianceFile: deleteVarianceFile,
                updateChangeFileDescription: updateChangeFileDescription,
                updateVarianceFileDownloadHistory: updateVarianceFileDownloadHistory,
                getAllVarianceFileVersionComments: getAllVarianceFileVersionComments,
                moveVarianceFileToFolder: moveVarianceFileToFolder,
                getDeviationTypeTree: getDeviationTypeTree,
                getWaiverTypeTree: getWaiverTypeTree,

                createVarianceAffectedItem: createVarianceAffectedItem,
                createVarianceAffectedPart: createVarianceAffectedPart,
                createAllVarianceAffectedItems: createAllVarianceAffectedItems,
                createAllVarianceAffectedParts: createAllVarianceAffectedParts,
                updateVarianceAffectedItem: updateVarianceAffectedItem,
                updateVarianceAffectedPart: updateVarianceAffectedPart,
                getVarianceAffectedItems: getVarianceAffectedItems,
                getVarianceAffectedParts: getVarianceAffectedParts,
                deleteVarianceAffectedItem: deleteVarianceAffectedItem,
                deleteVarianceAffectedPart: deleteVarianceAffectedPart,
                deleteVarianceFolder: deleteVarianceFolder,
                uploadVarianceFolderFiles: uploadVarianceFolderFiles,
                pasteVarianceFilesFromClipboard: pasteVarianceFilesFromClipboard,
                undoCopiedFiles: undoCopiedFiles,
                getVarianceFilesByName: getVarianceFilesByName,
                getVarianceFolderChildren: getVarianceFolderChildren,
                createVarianceFolder: createVarianceFolder,
                updateFileName: updateFileName,
                updateVarianceImageValue: updateVarianceImageValue,


                createDeviationType: createDeviationType,
                updateDeviationType: updateDeviationType,
                deleteDeviationType: deleteDeviationType,
                getDeviationType: getDeviationType,
                getAllDeviationTypes: getAllDeviationTypes,
                getMultipleDeviationTypes: getMultipleDeviationTypes,

                createWaiverType: createWaiverType,
                updateWaiverType: updateWaiverType,
                deleteWaiverType: deleteWaiverType,
                getWaiverType: getWaiverType,
                getAllWaiverTypes: getAllWaiverTypes,
                getMultipleWaiverTypes: getMultipleWaiverTypes,
                getQualityHistoryByDateWise: getVarianceHistoryByDateWise,

                getUsedAttributeValues: getUsedAttributeValues,
                getVarianceTypeAttributes: getVarianceTypeAttributes,
                createQualityObject: createVarianceObject,
                updateAttributeImageValue: updateAttributeImageValue,
                updateAttributeAttachmentValues: updateAttributeAttachmentValues,

                attachWorkflow: attachWorkflow,
                getWorkflow: getWorkflow,
                deleteVarianceWorkflow: deleteVarianceWorkflow,
                checkIsRecurring: checkIsRecurring,
                checkIsRecurringParts: checkIsRecurringParts,
                checkIsRecurringAfterDelete: checkIsRecurringAfterDelete,
                checkIsRecurringAfterDeleteParts: checkIsRecurringAfterDeleteParts,
                createVarianceRelatedItem: createVarianceRelatedItem,
                deleteVarianceRelatedItem: deleteVarianceRelatedItem,
                getOriginator: getOriginator,
                getStatus: getStatus
            };

            function getVariances(){
                var url = "api/plm/variance";
                return httpFactory.get(url);
            }

            function createVariance(variance) {
                var url = "api/plm/variance";
                return httpFactory.post(url, variance);
            }

            function getVarianceByNumber(number) {
                var url = "api/plm/variance/byNumber/" + number;
                return httpFactory.get(url);
            }

            function getVarianceByItem(item) {
                var url = "api/plm/variance/byItem/" + item;
                return httpFactory.get(url);
            }

            function updateVariance(id, variance) {
                var url = "api/plm/variance/" + id;
                return httpFactory.put(url, variance);
            }

            function deleteVariance(varianceId) {
                var url = "api/plm/variance/" + varianceId;
                return httpFactory.delete(url);
            }

            function getVariance(varianceId) {
                var url = "api/plm/variance/" + varianceId;
                return httpFactory.get(url);
            }

            function getAllVariance(pageable, filters) {
                var url = "api/plm/variance/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&name={0}&number={1}&planType={2}&description={3}&searchQuery={4}".
                    format(filters.name, filters.number, filters.planType, filters.description, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getMultipleVariance(varianceIds) {
                var url = "api/plm/variance/multiple/[" + varianceIds + "]";
                return httpFactory.get(url);
            }

            function getVarianceByType(pageable, filters) {
                var url = "api/plm/variance/all?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&varianceType={0}&description={1}&varianceNumber={2}&searchQuery={3}&originator={4}&status={5}&varianceFor={6}&effectivityType={7}".
                    format(filters.varianceType, filters.description, filters.varianceNumber, filters.searchQuery, filters.originator, filters.status, filters.varianceFor, filters.effectivityType);
                return httpFactory.get(url);
            }

            function getVarianceFiles(varianceId) {
                var url = "api/plm/variance/" + varianceId + "/files";
                return httpFactory.get(url);
            }

            function getVarianceFile(varianceId, fileId) {
                var url = "api/plm/variance/" + varianceId + "/files/" + fileId;
                return httpFactory.get(url);
            }

            function updateVarianceFile(varianceId, file) {
                var url = "api/plm/variance/" + varianceId + "/files/" + file.id;
                return httpFactory.put(url, file);
            }


            function updateChangeFileDescription(files) {
                var url = 'api/plm/variance/description/' + 0 + "/files/" + files.fileId;
                return httpFactory.put(url, files);
            }

            function deleteVarianceFile(varianceId, fileId) {
                var url = "api/plm/variance/" + varianceId + "/files/" + fileId;
                return httpFactory.delete(url);
            }

            function createVarianceAffectedItem(varianceId, item) {
                var url = "api/plm/variance/" + varianceId + "/varianceAffectedItems";
                return httpFactory.post(url, item)
            }

            function createVarianceAffectedPart(varianceId, item) {
                var url = "api/plm/variance/" + varianceId + "/varianceAffectedParts";
                return httpFactory.post(url, item)
            }

            function createAllVarianceAffectedItems(varianceId, items) {
                var url = "api/plm/variance/" + varianceId + "/varianceAffectedItems/all";
                return httpFactory.post(url, items)
            }

            function createAllVarianceAffectedParts(varianceId, items) {
                var url = "api/plm/variance/" + varianceId + "/varianceAffectedParts/all";
                return httpFactory.post(url, items)
            }

            function updateVarianceAffectedItem(varianceId, item) {
                var url = "api/plm/variance/" + varianceId + "/varianceAffectedItems/" + item.itemId;
                return httpFactory.put(url, item)
            }

            function updateVarianceAffectedPart(varianceId, item) {
                var url = "api/plm/variance/" + varianceId + "/varianceAffectedParts/" + item.itemId;
                return httpFactory.put(url, item)
            }

            function deleteVarianceAffectedItem(varianceId, itemId) {
                var url = "api/plm/variance/" + varianceId + "/varianceAffectedItems/" + itemId;
                return httpFactory.delete(url)
            }

            function deleteVarianceAffectedPart(varianceId, itemId) {
                var url = "api/plm/variance/" + varianceId + "/varianceAffectedParts/" + itemId;
                return httpFactory.delete(url)
            }

            function deleteVarianceRelatedItem(varianceId, itemId) {
                var url = "api/plm/variance/"+varianceId+"/relatedItem/delete/" + itemId;
                return httpFactory.delete(url);
            }

            function getVarianceAffectedItems(varianceId) {
                var url = "api/plm/variance/" + varianceId + "/varianceAffectedItems";
                return httpFactory.get(url);
            }

            function getVarianceAffectedParts(varianceId) {
                var url = "api/plm/variance/" + varianceId + "/varianceAffectedParts";
                return httpFactory.get(url);
            }

            function getVarianceCounts(varianceId) {
                var url = 'api/plm/variance/' + varianceId + "/details";
                return httpFactory.get(url);
            }

            function updateVarianceFileDownloadHistory(varianceId, fileId) {
                var url = "api/plm/variance/" + varianceId + "/files/" + fileId + "/fileDownloadHistory";
                return httpFactory.post(url);
            }

            function getAllVarianceFileVersionComments(fileId, objectType) {
                var url = 'api/plm/variance/files/' + fileId + "/versionComments/" + objectType;
                return httpFactory.get(url);
            }

            function getLatestUploadedFile(varianceId, fileId) {
                var url = 'api/plm/variance/' + varianceId + "/files/" + fileId + "/latest/uploaded";
                return httpFactory.get(url);
            }

            function moveVarianceFileToFolder(folderId, file) {
                var url = 'api/plm/variance/' + folderId + "/move";
                return httpFactory.put(url, file);
            }

            function deleteVarianceFolder(varianceId, folderId) {
                var url = 'api/plm/variance/' + varianceId + "/folder/" + folderId + "/delete";
                return httpFactory.delete(url)
            }

            function uploadVarianceFolderFiles(varianceId, folderId, files) {
                var url = 'api/plm/variance/' + varianceId + "/folder/" + folderId + "/upload";
                return httpFactory.uploadMultiple(url, files);
            }

            function pasteVarianceFilesFromClipboard(varianceId, fileId, files) {
                var url = 'api/plm/variance/' + varianceId + "/files/paste?fileId=" + fileId;
                return httpFactory.put(url, files);
            }

            function undoCopiedFiles(varianceId, files) {
                var url = 'api/plm/variance/' + varianceId + "/files/undo";
                return httpFactory.put(url, files);
            }

            function getVarianceFilesByName(varianceId, name) {
                var url = "api/plm/variance/" + varianceId + "/files/byName/" + name;
                return httpFactory.get(url);
            }

            function getVarianceFolderChildren(varianceId, folderId) {
                var url = 'api/plm/variance/' + folderId + "/children";
                return httpFactory.get(url);
            }

            function createVarianceFolder(varianceId, folder) {
                var url = 'api/plm/variance/' + varianceId + "/folder";
                return httpFactory.post(url, folder)
            }

            function getDeviationTypeTree() {
                var url = "api/plm/variance/devitionTypeTree/tree";
                return httpFactory.get(url);
            }

            function getWaiverTypeTree() {
                var url = "api/plm/variance/waiverTypeTree/tree";
                return httpFactory.get(url);
            }

            function createDeviationType(changeType) {
                var url = "api/plm/variance/deviationTypes";
                return httpFactory.post(url, changeType);
            }

            function updateDeviationType(changeType) {
                var url = "api/plm/variance/deviationTypes/" + changeType.id;
                return httpFactory.put(url, changeType);
            }

            function deleteDeviationType(id) {
                var url = "api/plm/variance/deviationTypes/" + id;
                return httpFactory.delete(url);
            }

            function getDeviationType(id) {
                var url = "api/plm/variance/deviationTypes/" + id;
                return httpFactory.get(url);
            }

            function getAllDeviationTypes() {
                var url = "api/plm/variance/deviationTypes";
                return httpFactory.get(url);
            }

            function getMultipleDeviationTypes(ids) {
                var url = "api/plm/variance/deviationTypes/multiple/[{ids}]";
                return httpFactory.get(url);
            }


            function createWaiverType(changeType) {
                var url = "api/plm/variance/waiverTypes";
                return httpFactory.post(url, changeType);
            }

            function updateWaiverType(changeType) {
                var url = "api/plm/variance/waiverTypes/" + changeType.id;
                return httpFactory.put(url, changeType);
            }

            function deleteWaiverType(id) {
                var url = "api/plm/variance/waiverTypes/" + id;
                return httpFactory.delete(url);
            }

            function getWaiverType(id) {
                var url = "api/plm/variance/waiverTypes/" + id;
                return httpFactory.get(url);
            }

            function getAllWaiverTypes() {
                var url = "api/plm/variance/waiverTypes";
                return httpFactory.get(url);
            }

            function getMultipleWaiverTypes(ids) {
                var url = "api/plm/variance/waiverTypes/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getAllFileVersions(varianceId, fileId) {
                var url = "api/plm/variance/" + varianceId + "/files/" + fileId + "/versions";
                return httpFactory.get(url)
            }

            function updateFileName(varianceId, fileId, newFileName) {
                var url = 'api/plm/variance/' + varianceId + "/files/" + fileId + "/renameFile";
                return httpFactory.put(url, newFileName)
            }

            function getVarianceHistoryByDateWise(id, type, pageable, filter) {
                var url = "api/plm/variance/" + id + "/" + type + "/history?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&type={0}&date={1}&user={2}&revision={3}".format(filter.type, filter.date, filter.user, filter.revision);
                return httpFactory.get(url);
            }

            function getUsedAttributeValues(id) {
                var url = "api/plm/variance/attributes/" + id;
                return httpFactory.get(url);
            }

            function getVarianceTypeAttributes(objectType, typeId, objectId) {
                var url = "api/plm/variance/" + objectType + "/" + typeId + "/" + objectId + "/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function createVarianceObject(objectType, dto) {
                var url = "api/plm/variance/" + objectType + "/object";
                return httpFactory.post(url, dto);
            }

            function updateAttributeAttachmentValues(objectType, objectId, attributeId, file) {
                var url = "api/plm/variance/" + objectType + "/" + objectId + "/" + attributeId + "/attachments/upload";
                return httpFactory.uploadMultiple(url, file);
            }

            function updateAttributeImageValue(objectType, objectId, attributeId, file) {
                var url = "api/plm/variance/" + objectType + "/" + objectId + "/" + attributeId + "/images/upload";
                return httpFactory.upload(url, file);
            }

            function attachWorkflow(varianceId, wfId) {
                var url = "api/plm/variance/" + varianceId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

            function getWorkflow(varianceId) {
                var url = "api/plm/variance/" + varianceId + "/workflow";
                return httpFactory.get(url);
            }

            function deleteVarianceWorkflow(varianceId) {
                var url = 'api/plm/variance/' + varianceId + "/workflow/delete";
                return httpFactory.delete(url);
            }

            function updateVarianceImageValue(objectId, attributeId, file) {
                var url = "api/plm/variance/updateImageValue/" + objectId + "/" + attributeId;
                return httpFactory.upload(url, file);
            }

            function checkIsRecurring(varianceId){
                var url = "api/plm/variance/checkIsRecurring/" + varianceId;
                return httpFactory.get(url);
            }

            function checkIsRecurringParts(varianceId){
                var url = "api/plm/variance/checkIsRecurringParts/" + varianceId;
                return httpFactory.get(url);
            }

            function checkIsRecurringAfterDelete(varianceId, itemId){
                var url = "api/plm/variance/" + varianceId + "/checkIsRecurringAfterDelete/" + itemId;
                return httpFactory.get(url);
            }

            function checkIsRecurringAfterDeleteParts(varianceId, itemId){
                var url = "api/plm/variance/" + varianceId + "/checkIsRecurringAfterDeleteParts/" + itemId;
                return httpFactory.get(url);
            }

            function createVarianceRelatedItem(changeId, item) {
                var url = "api/plm/variance/changeRelatedItem/" + changeId;
                return httpFactory.post(url, item);
            }

            function getOriginator(type) {
                var url = "api/plm/variance/originator/"+type;
                return httpFactory.get(url);
            }

            function getStatus(type) {
                var url = "api/plm/variance/status/"+type;
                return httpFactory.get(url);
            }
        }
    }
);