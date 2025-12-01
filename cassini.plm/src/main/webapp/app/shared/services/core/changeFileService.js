define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('ChangeFileService', ChangeFileService);

        function ChangeFileService(httpFactory) {
            return {
                uploadChangeFiles: uploadChangeFiles,
                updateChangeFile: updateChangeFile,
                createChangeFileFolder: createChangeFileFolder,
                getFolderChildren: getFolderChildren,
                updateFileName: updateFileName,
                getLatestUploadedFile: getLatestUploadedFile,
                deleteChangeFile: deleteChangeFile,
                deleteChangeFolder: deleteChangeFolder,
                moveChangeFileToFolder: moveChangeFileToFolder,
                pasteChangeFilesFromClipboard: pasteChangeFilesFromClipboard,
                undoCopiedChangeFiles: undoCopiedChangeFiles,
                updateChangeFileDownloadHistory: updateChangeFileDownloadHistory,
                getChangeFilesByName: getChangeFilesByName,
                getFileVersionComments: getFileVersionComments,
                getChangeFiles: getChangeFiles,
                getAllFileVersions: getAllFileVersions
            };

            function uploadChangeFiles(id, folderId, files) {
                var url = 'api/changes/' + id + "/files/" + folderId;
                return httpFactory.uploadMultiple(url, files);
            }

            function updateChangeFile(id, fileId, qualityFile) {
                var url = "api/changes/" + id + "/files/" + fileId + "/update";
                return httpFactory.put(url, qualityFile);
            }

            function createChangeFileFolder(id, folder) {
                var url = "api/changes/" + id + "/folders";
                return httpFactory.post(url, folder);
            }

            function getFolderChildren(id, folderId) {
                var url = "api/changes/" + id + "/folders/" + folderId + "/children";
                return httpFactory.get(url);
            }

            function updateFileName(id, fileId, newName) {
                var url = "api/changes/" + id + "/files/" + fileId + "/renameFile";
                return httpFactory.put(url, newName);
            }

            function getLatestUploadedFile(id, fileId) {
                var url = "api/changes/" + id + "/files/" + fileId + "/latest/uploaded";
                return httpFactory.get(url);
            }

            function deleteChangeFile(id, fileId) {
                var url = "api/changes/" + id + "/files/" + fileId;
                return httpFactory.delete(url);
            }

            function deleteChangeFolder(id, folderId) {
                var url = 'api/changes/' + id + "/folders/" + folderId;
                return httpFactory.delete(url);
            }

            function moveChangeFileToFolder(id, file) {
                var url = 'api/changes/' + id + "/files/" + file.id + "/move";
                return httpFactory.put(url, file);
            }

            function pasteChangeFilesFromClipboard(id, fileId, files) {
                var url = 'api/changes/' + id + "/files/paste?fileId=" + fileId;
                return httpFactory.put(url, files);
            }

            function undoCopiedChangeFiles(id, files) {
                var url = "api/changes/" + id + "/files/undo";
                return httpFactory.put(url, files);
            }

            function updateChangeFileDownloadHistory(id, fileId) {
                var url = 'api/changes/' + id + "/files/" + fileId + "/fileDownloadHistory";
                return httpFactory.post(url);
            }

            function getChangeFilesByName(itemId, name) {
                var url = 'api/changes/' + itemId + "/files/byName/" + name;
                return httpFactory.get(url);
            }

            function getChangeFiles(itemId) {
                var url = 'api/changes/' + itemId + "/files";
                return httpFactory.get(url);
            }

            function getFileVersionComments(fileId, objectType) {
                var url = "api/changes/files/" + fileId + "/versionComments/" + objectType;
                return httpFactory.get(url);
            }

            function getAllFileVersions(objectId, fileId) {
                var url = "api/changes/" + objectId + "/files/" + fileId + "/versions";
                return httpFactory.get(url)
            }

        }
    }
);