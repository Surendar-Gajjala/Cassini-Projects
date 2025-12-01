define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('CustomObjectFileService', CustomObjectFileService);

        function CustomObjectFileService(httpFactory) {

            var TYPE_BASE_URL = "api/customObjects";

            return {
                uploadCustomObjectFiles: uploadCustomObjectFiles,
                updateCustomObjectFile: updateCustomObjectFile,
                createCustomObjectFileFolder: createCustomObjectFileFolder,
                getFolderChildren: getFolderChildren,
                updateFileName: updateFileName,
                getLatestUploadedFile: getLatestUploadedFile,
                deleteCustomObjectFile: deleteCustomObjectFile,
                deleteCustomObjectFolder: deleteCustomObjectFolder,
                moveCustomObjectFileToFolder: moveCustomObjectFileToFolder,
                pasteCustomObjectFilesFromClipboard: pasteCustomObjectFilesFromClipboard,
                undoCopiedCustomObjectFiles: undoCopiedCustomObjectFiles,
                updateCustomObjectFileDownloadHistory: updateCustomObjectFileDownloadHistory,
                getCustomObjectFilesByName: getCustomObjectFilesByName,
                getFileVersionComments: getFileVersionComments,
                getCustomObjectFiles: getCustomObjectFiles,
                getAllFileVersions: getAllFileVersions,
                getFileDownloadHistory: getFileDownloadHistory,
                updateCustomObjectFileFolder: updateCustomObjectFileFolder
            };

            function uploadCustomObjectFiles(id, folderId, files) {
                var url = TYPE_BASE_URL + '/' + id + "/files/" + folderId;
                return httpFactory.uploadMultiple(url, files);
            }

            function updateCustomObjectFile(id, fileId, qualityFile) {
                var url = TYPE_BASE_URL + '/' + id + "/files/" + fileId + "/update";
                return httpFactory.put(url, qualityFile);
            }

            function createCustomObjectFileFolder(id, folder) {
                var url = TYPE_BASE_URL + '/' + id + "/folders";
                return httpFactory.post(url, folder);
            }

            function updateCustomObjectFileFolder(id, folder) {
                var url = TYPE_BASE_URL + '/' + id + "/folders/" + folder.id;
                return httpFactory.put(url, folder);
            }

            function getFolderChildren(id, folderId) {
                var url = TYPE_BASE_URL + '/' + id + "/folders/" + folderId + "/children";
                return httpFactory.get(url);
            }

            function updateFileName(id, fileId, newName) {
                var url = TYPE_BASE_URL + '/' + id + "/files/" + fileId + "/renameFile";
                return httpFactory.put(url, newName);
            }

            function getLatestUploadedFile(id, fileId) {
                var url = TYPE_BASE_URL + '/' + id + "/files/" + fileId + "/latest/uploaded";
                return httpFactory.get(url);
            }

            function deleteCustomObjectFile(id, fileId) {
                var url = TYPE_BASE_URL + '/' + id + "/files/" + fileId;
                return httpFactory.delete(url);
            }

            function deleteCustomObjectFolder(id, folderId) {
                var url = TYPE_BASE_URL + '/' + id + "/folders/" + folderId;
                return httpFactory.delete(url);
            }

            function moveCustomObjectFileToFolder(id, file) {
                var url = TYPE_BASE_URL + '/' + id + "/files/" + file.id + "/move";
                return httpFactory.put(url, file);
            }

            function pasteCustomObjectFilesFromClipboard(id, fileId, files) {
                var url = TYPE_BASE_URL + '/' + id + "/files/paste?fileId=" + fileId;
                return httpFactory.put(url, files);
            }

            function undoCopiedCustomObjectFiles(id, files) {
                var url = TYPE_BASE_URL + '/' + id + "/files/undo";
                return httpFactory.put(url, files);
            }

            function updateCustomObjectFileDownloadHistory(id, fileId) {
                var url = TYPE_BASE_URL + '/' + id + "/files/" + fileId + "/fileDownloadHistory";
                return httpFactory.post(url);
            }

            function getCustomObjectFilesByName(itemId, name) {
                var url = TYPE_BASE_URL + '/' + itemId + "/files/byName/" + name;
                return httpFactory.get(url);
            }

            function getCustomObjectFiles(itemId) {
                var url = TYPE_BASE_URL + '/' + itemId + "/files";
                return httpFactory.get(url);
            }

            function getFileVersionComments(fileId, objectType) {
                var url = TYPE_BASE_URL + "/files/" + fileId + "/versionComments/" + objectType;
                return httpFactory.get(url);
            }

            function getAllFileVersions(objectId, fileId) {
                var url = TYPE_BASE_URL + '/' + objectId + "/files/" + fileId + "/versions";
                return httpFactory.get(url)
            }

            function getFileDownloadHistory(objectId, fileId) {
                var url = TYPE_BASE_URL + '/' + objectId + "/files/" + fileId + "/downloadHistory";
                return httpFactory.get(url);
            }

        }
    }
);