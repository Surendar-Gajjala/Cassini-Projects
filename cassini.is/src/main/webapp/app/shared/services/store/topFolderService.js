define(
    [
        'app/shared/services/services.module',
        'app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('TopFolderService', TopFolderService);

        function TopFolderService(httpFactory) {
            return {
                createTopFolder: createTopFolder,
                updateTopFolder: updateTopFolder,
                deleteTopFolder: deleteTopFolder,
                getTopFolder: getTopFolder,
                getTopFolders: getTopFolders,
                getFolderTree: getFolderTree,
                deleteFolderDocument: deleteFolderDocument,
                saveFolderPermissions: saveFolderPermissions,
                getFolderPermissions: getFolderPermissions
            };

            function createTopFolder(topFolder) {
                var url = "api/is/folders";
                return httpFactory.post(url, topFolder);
            }

            function updateTopFolder(topFolder) {
                var url = "api/is/folders/" + topFolder.id;
                return httpFactory.put(url, topFolder);
            }

            function deleteTopFolder(folderId) {
                var url = "api/is/folders/" + folderId;
                return httpFactory.delete(url);
            }

            function getTopFolder(topFolder) {
                var url = "api/is/folders/" + topFolder.id;
                return httpFactory.get(url);
            }

            function getTopFolders() {
                var url = "api/is/folders";
                return httpFactory.get(url);
            }

            function getFolderTree() {
                var url = "api/is/folders/tree";
                return httpFactory.get(url);
            }

            function deleteFolderDocument(folderId, documentId) {
                var url = "api/is/folders/" + folderId + "/documents/" + documentId;
                return httpFactory.delete(url);
            }

            function saveFolderPermissions(folderId, objectPermissions) {
                var url = "api/is/folders/" + folderId + "/permissions";
                return httpFactory.post(url, objectPermissions);
            }

            function getFolderPermissions(folderId) {
                var url = "api/is/folders/" + folderId + "/permissions";
                return httpFactory.post(url);
            }
        }
    }
);