define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('ObjectFileService', ObjectFileService);

        function ObjectFileService(httpFactory) {
            return {
                uploadObjectFiles: uploadObjectFiles,
                updateObjectFile: updateObjectFile,
                createObjectFileFolder: createObjectFileFolder,
                getFolderChildren: getFolderChildren,
                updateFileName: updateFileName,
                getLatestUploadedFile: getLatestUploadedFile,
                deleteObjectFile: deleteObjectFile,
                deleteObjectFolder: deleteObjectFolder,
                moveObjectFileToFolder: moveObjectFileToFolder,
                pasteObjectFilesFromClipboard: pasteObjectFilesFromClipboard,
                undoCopiedObjectFiles: undoCopiedObjectFiles,
                updateObjectFileDownloadHistory: updateObjectFileDownloadHistory,
                getObjectFilesByName: getObjectFilesByName,
                getFileVersionComments: getFileVersionComments,
                getObjectFiles: getObjectFiles,
                getObjectFilesCount: getObjectFilesCount,
                getObjectFile: getObjectFile,
                updateObjectFileFolder: updateObjectFileFolder,
                sendFileCommentNotification: sendFileCommentNotification,
                getCreatedByPersons: getCreatedByPersons,
                shareObjectFilesToObjects: shareObjectFilesToObjects
            };

            function uploadObjectFiles(id, type, folderId, files) {
                var url = 'api/plm/objects/' + id + "/" + type + "/files/" + folderId;
                return httpFactory.uploadMultiple(url, files);
            }

            function getObjectFiles(id, type, hierarchy) {
                var url = 'api/plm/objects/' + id + "/" + type + "/files?hierarchy=" + hierarchy;
                return httpFactory.get(url);
            }

            function getObjectFile(id, type, fileId) {
                var url = 'api/plm/objects/' + id + "/" + type + "/files/" + fileId;
                return httpFactory.get(url);
            }

            function getObjectFilesCount(id, type) {
                var url = 'api/plm/objects/' + id + "/" + type + "/files/count";
                return httpFactory.get(url);
            }

            function updateObjectFile(id, fileId, type, qualityFile) {
                var url = "api/plm/objects/" + id + "/files/" + fileId + "/" + type;
                return httpFactory.put(url, qualityFile);
            }

            function createObjectFileFolder(id, type, folder) {
                var url = "api/plm/objects/" + id + "/" + type + "/folders";
                return httpFactory.post(url, folder);
            }

            function updateObjectFileFolder(id, type, folderId, folder) {
                var url = "api/plm/objects/" + id + "/" + type + "/folders/" + folderId;
                return httpFactory.put(url, folder);
            }

            function getFolderChildren(id, type, folderId, hierachy) {
                var url = "api/plm/objects/" + id + "/" + type + "/folders/" + folderId + "/children?hierarchy=" + hierachy;
                return httpFactory.get(url);
            }

            function updateFileName(id, type, fileId, newName) {
                var url = "api/plm/objects/" + id + "/" + type + "/files/" + fileId + "/renameFile";
                return httpFactory.put(url, newName);
            }

            function getLatestUploadedFile(id, type, fileId) {
                var url = "api/plm/objects/" + id + "/" + type + "/files/" + fileId + "/latest/uploaded";
                return httpFactory.get(url);
            }

            function deleteObjectFile(id, type, fileId) {
                var url = "api/plm/objects/" + id + "/" + type + "/files/" + fileId;
                return httpFactory.delete(url);
            }

            function deleteObjectFolder(id, type, folderId) {
                var url = 'api/plm/objects/' + id + "/" + type + "/folders/" + folderId;
                return httpFactory.delete(url);
            }

            function moveObjectFileToFolder(id, type, fileId, file) {
                var url = 'api/plm/objects/' + id + "/" + type + "/files/" + fileId + "/move";
                return httpFactory.put(url, file);
            }

            function pasteObjectFilesFromClipboard(id, type, fileId, files) {
                var url = 'api/plm/objects/' + id + "/" + type + "/files/paste?fileId=" + fileId;
                return httpFactory.put(url, files);
            }

            function undoCopiedObjectFiles(id, type, files) {
                var url = "api/plm/objects/" + id + "/" + type + "/files/undo";
                return httpFactory.put(url, files);
            }

            function updateObjectFileDownloadHistory(id, type, fileId) {
                var url = 'api/plm/objects/' + id + "/" + type + "/files/" + fileId + "/fileDownloadHistory";
                return httpFactory.post(url);
            }

            function getObjectFilesByName(itemId, type, name) {
                var url = 'api/plm/objects/' + itemId + "/" + type + "/files/byName/" + name;
                return httpFactory.get(url);
            }

            function getFileVersionComments(fileId, qualityType, objectType) {
                var url = "api/plm/objects/" + qualityType + "/files/" + fileId + "/versionComments/" + objectType;
                return httpFactory.get(url);
            }

            function sendFileCommentNotification(id, fileId, objectType, comment, type) {
                var url = "api/plm/objects/" + id + "/" + objectType + "/files/" + fileId + "/comment/notification/" + type;
                return httpFactory.post(url, comment);
            }

            function getCreatedByPersons(id, objectType) {
                var url = "api/plm/objects/" + id + "/" + objectType + "/files/createdByPersons";
                return httpFactory.get(url);
            }

            function shareObjectFilesToObjects(id, objectType, objectFileDto) {
                var url = "api/plm/objects/" + id + "/" + objectType + "/files/share/objects";
                return httpFactory.post(url, objectFileDto);
            }

        }
    }
);