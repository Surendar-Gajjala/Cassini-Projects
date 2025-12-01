define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('ItemFileService', ItemFileService);

        function ItemFileService(httpFactory) {
            return {
                getItemFiles: getItemFiles,
                getItemFile: getItemFile,
                getAllFileVersions: getAllFileVersions,
                createItemFile: createItemFile,
                updateItemFile: updateItemFile,
                deleteItemFile: deleteItemFile,
                getItemFilesByName: getItemFilesByName,
                updateFileDownloadHistory: updateFileDownloadHistory,
                getFileDownloadHistory: getFileDownloadHistory,
                getAllFileVersionComments: getAllFileVersionComments,
                updateFileDescription: updateFileDescription,
                replaceItemFile: replaceItemFile,
                updateFileName: updateFileName,
                downloadItemFilesAsZip: downloadItemFilesAsZip,
                createItemFolder: createItemFolder,
                getFolderChildren: getFolderChildren,
                uploadFolderFiles: uploadFolderFiles,
                deleteItemFolder: deleteItemFolder,
                moveItemFileToFolder: moveItemFileToFolder,
                getLatestUploadedFile: getLatestUploadedFile,
                pasteItemFilesFromClipboard: pasteItemFilesFromClipboard,
                undoCopiedFiles: undoCopiedFiles,
                convertForgeCADFile: convertForgeCADFile
            };

            function getItemFilesByName(itemId, name) {
                var url = 'api/plm/items/' + itemId + "/files/byName/" + name;
                return httpFactory.get(url);

            }

            function getItemFiles(itemId) {
                var url = 'api/plm/items/' + itemId + "/files";
                return httpFactory.get(url);
            }

            function getItemFile(itemId, fileId) {
                var url = 'api/plm/items/' + itemId + "/files/" + fileId;
                return httpFactory.get(url);
            }

            function createItemFile(itemId, file) {
                var url = 'api/plm/items/' + itemId + "/files";
                return httpFactory.post(url, file);
            }

            function createItemFolder(itemId, file) {
                var url = 'api/plm/items/' + itemId + "/files/folder";
                return httpFactory.post(url, file);
            }


            function replaceItemFile(itemId, files, replaceFile) {
                var url = 'api/plm/items/' + itemId + "/files" + "/replaceFile/" + replaceFile;
                return httpFactory.upload(url, files)
            }

            function updateFileName(itemId, fileId, newFileName) {
                var url = 'api/plm/items/' + itemId + "/files/" + fileId + "/renameFile";
                return httpFactory.put(url, newFileName)
            }

            function getLatestUploadedFile(itemId, fileId) {
                var url = 'api/plm/items/' + itemId + "/files/" + fileId + "/latest/uploaded";
                return httpFactory.get(url)
            }

            function updateItemFile(itemId, file) {
                var url = 'api/plm/items/' + itemId + "/files/" + file.id;
                return httpFactory.put(url, file);
            }

            function moveItemFileToFolder(itemId, file) {
                var url = 'api/plm/items/' + itemId + "/files/" + file.id + "/move";
                return httpFactory.put(url, file);
            }

            function deleteItemFile(itemId, fileId) {
                var url = 'api/plm/items/' + itemId + "/files/" + fileId;
                return httpFactory.delete(url);
            }

            function getAllFileVersions(itemId, fileId) {
                var url = 'api/plm/items/' + itemId + "/files/" + fileId + "/versions";
                return httpFactory.get(url);
            }

            function getFileDownloadHistory(itemId, fileId) {
                var url = 'api/plm/items/' + itemId + "/files/" + fileId + "/downloadHistory";
                return httpFactory.get(url);
            }

            function updateFileDownloadHistory(itemId, fileId) {
                var url = 'api/plm/items/' + itemId + "/files/" + fileId + "/fileDownloadHistory";
                return httpFactory.post(url);
            }

            function getAllFileVersionComments(fileId, objectType) {
                var url = 'api/plm/items/' + 0 + "/files/" + fileId + "/versionComments/" + objectType;
                return httpFactory.get(url);
            }

            function updateFileDescription(files) {
                var url = 'api/plm/items/' + 0 + "/files/" + files.fileId;
                return httpFactory.put(url, files);
            }

            function downloadItemFilesAsZip(itemId) {
                var url = 'api/plm/items/' + itemId + "/files/zip";
                return httpFactory.get(url);
            }

            function getFolderChildren(itemId, folderId) {
                var url = 'api/plm/items/' + itemId + "/files/" + folderId + "/children";
                return httpFactory.get(url);
            }

            function uploadFolderFiles(itemId, folderId, files) {
                var url = 'api/plm/items/' + itemId + "/files/" + folderId + "/upload";
                return httpFactory.uploadMultiple(url, files);
            }

            function deleteItemFolder(itemId, folderId) {
                var url = 'api/plm/items/' + itemId + "/files/" + folderId + "/delete";
                return httpFactory.delete(url);
            }

            function pasteItemFilesFromClipboard(itemId, fileId, files) {
                var url = 'api/plm/items/' + itemId + "/files/paste?fileId=" + fileId;
                return httpFactory.put(url, files);
            }

            function undoCopiedFiles(itemId, files) {
                var url = "api/plm/items/" + itemId + "/files/undo";
                return httpFactory.put(url, files);
            }

            function convertForgeCADFile(id, file) {
                var url = "api/plm/items/" + id + "/files/convert/forge";
                return httpFactory.post(url, file);
            }
        }
    }
);