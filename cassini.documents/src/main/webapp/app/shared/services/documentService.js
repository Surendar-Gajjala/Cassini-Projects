define(
    [
        'app/shared/services/services.module',
        'app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('DocumentService', DocumentService);

        function DocumentService(httpFactory) {
            return {
                createTopDocument: createTopDocument,
                updateTopDocument: updateTopDocument,
                deleteTopDocument: deleteTopDocument,
                getTopDocument: getTopDocument,
                getTopDocuments: getTopDocuments,
                getSelectedFolderDocuments: getSelectedFolderDocuments,
                getFolderPermissions: getFolderPermissions,
                getAllFileVersions: getAllFileVersions
            };

            function createTopDocument(topDocument) {
                var url = "api/dm/documents";
                return httpFactory.post(url, topDocument);
            }

            function updateTopDocument(folder, topDocument) {
                var url = "api/dm/folders/" + folder + "/documents/" + topDocument.id;
                return httpFactory.put(url, topDocument);
            }

            function deleteTopDocument(DocumentId) {
                var url = "api/dm/documents/" + DocumentId;
                return httpFactory.delete(url);
            }

            function getTopDocument(topDocument) {
                var url = "api/dm/documents/" + topDocument.id;
                return httpFactory.get(url);
            }

            function getTopDocuments() {
                var url = "api/dm/documents";
                return httpFactory.get(url);
            }

            function getSelectedFolderDocuments(folderId) {
                var url = "api/dm/documents/byFolder/" + folderId;
                return httpFactory.get(url);
            }

            function getFolderPermissions(folderId) {
                var url = "api/dm/folders/" + folderId + "/permissions";
                return httpFactory.get(url);
            }

            function getAllFileVersions(folderId, documentId) {
                var url = "api/dm/documents/" + documentId + "/folder/" + folderId + "/versions";
                return httpFactory.get(url);
            }

        }
    }
);