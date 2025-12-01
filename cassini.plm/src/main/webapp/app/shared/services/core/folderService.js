define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('FolderService', FolderService);

        function FolderService(httpFactory) {
            return {
                getFolders: getFolders,
                getFolderById: getFolderById,
                createFolder: createFolder,
                createFolderObject: createFolderObject,
                updateFolder: updateFolder,
                deleteFolder: deleteFolder,
                getClassificationTree: getClassificationTree,
                getAttributesWithHierarchy: getAttributesWithHierarchy,
                getFolderObjects: getFolderObjects,
                removeFolderObject: removeFolderObject,
                getFoldersTree: getFoldersTree
            };

            function getFolderObjects(folder) {
                var url = "api/plm/folders/" + folder.id + "/objects";
                return httpFactory.get(url);
            }

            function getFolders() {
                var url = "api/plm/folders";
                return httpFactory.get(url);
            }

            function getClassificationTree() {
                var url = "api/plm/folders/tree";
                return httpFactory.get(url);
            }

            function getFoldersTree(person) {
                var url = "api/plm/folders/tree/" + person;
                return httpFactory.get(url);
            }

            function getFolderById(folderId) {
                var url = "api/plm/folders/" + folderId;
                return httpFactory.get(url);
            }

            function createFolder(folder) {
                var url = "api/plm/folders";
                return httpFactory.post(url, folder);
            }

            function updateFolder(folder) {
                var url = "api/plm/folders/" + folder.id;
                return httpFactory.put(url, folder);
            }

            function deleteFolder(folderId) {
                var url = "api/plm/folders/" + folderId;
                return httpFactory.delete(url);
            }

            function getAttributesWithHierarchy(folderId) {
                var url = "api/plm/folders/" + folderId + "/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function createFolderObject(folderObjects, objectType) {
                var url = "api/plm/folders/objects?objectType={0}".format(objectType);
                return httpFactory.post(url, folderObjects);
            }

            function removeFolderObject(folderObjectId) {
                var url = "api/plm/folders/1/objects/{0}".format(folderObjectId);
                return httpFactory.delete(url);
            }
        }
    }
);