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
                getAllFileVersionComments: getAllFileVersionComments
            };

            function getItemFilesByName(itemId, name) {
                var url = 'api/drdo/items/' + itemId + "/files/byName/" + name;
                return httpFactory.get(url);

            }

            function getItemFiles(itemId) {
                var url = 'api/drdo/items/' + itemId + "/files";
                return httpFactory.get(url);
            }

            function getItemFile(itemId, fileId) {
                var url = 'api/drdo/items/' + itemId + "/files/" + fileId;
                return httpFactory.get(url);
            }

            function createItemFile(itemId, file) {
                var url = 'api/drdo/items/' + itemId + "/files";
                return httpFactory.post(url, file);
            }

            function updateItemFile(itemId, file) {
                var url = 'api/drdo/items/' + itemId + "/files/" + file.id;
                return httpFactory.put(url, file);
            }

            function deleteItemFile(itemId, fileId) {
                var url = 'api/drdo/items/' + itemId + "/files/" + fileId;
                return httpFactory.delete(url);
            }

            function getAllFileVersions(itemId, fileId) {
                var url = 'api/drdo/items/' + itemId + "/files/" + fileId + "/versions";
                return httpFactory.get(url);
            }

            function getFileDownloadHistory(itemId, fileId) {
                var url = 'api/drdo/items/' + itemId + "/files/" + fileId + "/downloadHistory";
                return httpFactory.get(url);
            }

            function updateFileDownloadHistory(itemId, fileId) {
                var url = 'api/drdo/items/' + itemId + "/files/" + fileId + "/fileDownloadHistory";
                return httpFactory.post(url);
            }

            function getAllFileVersionComments(fileId, objectType) {
                var url = 'api/drdo/items/' + 0 + "/files/" + fileId + "/versionComments/" + objectType;
                return httpFactory.get(url);
            }
        }
    }
);