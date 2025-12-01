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
                deleteItemFile: deleteItemFile
            };

            function getItemFiles(itemId) {
                var url = 'api/pdm/itemFiles/' + itemId + "/files";
                return httpFactory.get(url);
            }

            function getItemFile(itemId, fileId) {
                var url = 'api/pdm/itemFiles/' + itemId + "/files/" + fileId;
                return httpFactory.get(url);
            }

            function createItemFile(itemId, file) {
                var url = 'api/pdm/itemFiles/' + itemId + "/files";
                return httpFactory.post(url, file);
            }

            function updateItemFile(itemId, file) {
                var url = 'api/pdm/itemFiles/' + itemId + "/files/" + file.id;
                return httpFactory.put(url, file);
            }

            function deleteItemFile(itemId, fileId) {
                var url = 'api/pdm/itemFiles/' + itemId + "/files/" + fileId;
                return httpFactory.delete(url);
            }

            function getAllFileVersions(itemId, fileId) {
                var url = 'api/pdm/itemFiles/' + itemId + "/files/" + fileId + "/versions";
                return httpFactory.get(url);
            }
        }
    }
);