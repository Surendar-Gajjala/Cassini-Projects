define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('DocumentService', DocumentService);

        function DocumentService(httpFactory) {
            return {
                getDocumentFolderTree: getDocumentFolderTree,
                getObjectDocumentFolderTree: getObjectDocumentFolderTree,
                getFilteredDocuments: getFilteredDocuments,
                getDocumentChildren: getDocumentChildren,
                createDocumentFolder: createDocumentFolder,
                updateDocumentFolder: updateDocumentFolder,
                deleteDocumentFolder: deleteDocumentFolder,
                createMultipleObjectDocuments: createMultipleObjectDocuments,
                deleteObjectDocument: deleteObjectDocument,
                getTotalDocumentsCount: getTotalDocumentsCount,
                getFolderDocumentsCount: getFolderDocumentsCount,
                updateObjectDocumentToLatest: updateObjectDocumentToLatest,
                promoteDocument: promoteDocument,
                demoteDocument: demoteDocument,
                reviseDocument: reviseDocument,
                addReviewer: addReviewer,
                updateReviewer: updateReviewer,
                deleteReviewer: deleteReviewer,
                getReviewersAndApprovers: getReviewersAndApprovers,
                submitDocumentReview: submitDocumentReview,
                getDocumentFolderPermissions: getDocumentFolderPermissions,
                getDocument: getDocument
            };

            function getDocumentFolderTree() {
                var url = "api/plm/documents/folders/tree";
                return httpFactory.get(url);
            }

            function getObjectDocumentFolderTree(object, objectType, folder) {
                var url = "api/plm/documents/folders/" + object + "/" + objectType + "/" + folder + "/tree";
                return httpFactory.get(url);
            }

            function getDocumentChildren(id) {
                var url = "api/plm/documents/" + id + "/children";
                return httpFactory.get(url);
            }

            function getDocument(id) {
                var url = "api/plm/documents/" + id;
                return httpFactory.get(url);
            }

            function getFilteredDocuments(filters) {
                var url = "api/plm/documents/filtered";
                url += "?objectId={0}&objectFolder={1}&folder={2}&searchQuery={3}&documentType={4}"
                    .format(filters.objectId, filters.objectFolder, filters.folder, filters.searchQuery, filters.documentType);
                return httpFactory.get(url);
            }

            function createDocumentFolder(document) {
                var url = "api/plm/documents/folders";
                return httpFactory.post(url, document);
            }

            function updateDocumentFolder(document) {
                var url = "api/plm/documents/folders/" + document.id;
                return httpFactory.put(url, document);
            }

            function deleteDocumentFolder(documentId) {
                var url = "api/plm/documents/folders/" + documentId;
                return httpFactory.delete(url);
            }

            function createMultipleObjectDocuments(objectDocuments) {
                var url = "api/plm/documents/object/multiple";
                return httpFactory.post(url, objectDocuments);
            }

            function deleteObjectDocument(fileId) {
                var url = "api/plm/documents/object/document/" + fileId;
                return httpFactory.delete(url);
            }

            function getTotalDocumentsCount() {
                var url = "api/plm/documents/files/count";
                return httpFactory.get(url);
            }

            function getFolderDocumentsCount(folder) {
                var url = "api/plm/documents/folders/" + folder + "/files/count";
                return httpFactory.get(url);
            }

            function updateObjectDocumentToLatest(fileId) {
                var url = "api/plm/documents/object/document/" + fileId + "/latest";
                return httpFactory.get(url);
            }

            function promoteDocument(file) {
                var url = "api/plm/documents/" + file.id + "/promote";
                return httpFactory.put(url, file);
            }

            function demoteDocument(file) {
                var url = "api/plm/documents/" + file.id + "/demote";
                return httpFactory.put(url, file);
            }

            function reviseDocument(file) {
                var url = "api/plm/documents/" + file.id + "/revise";
                return httpFactory.put(url, file);
            }


            function addReviewer(id, reviewer) {
                var url = "api/plm/documents/{0}/reviewers".format(id);
                return httpFactory.post(url, reviewer);
            }

            function updateReviewer(id, reviewer) {
                var url = "api/plm/documents/{0}/reviewers/{1}".format(id, reviewer.id);
                return httpFactory.put(url, reviewer);
            }

            function deleteReviewer(id, reviewer) {
                var url = "api/plm/documents/{0}/reviewers/{1}".format(id, reviewer.id);
                return httpFactory.delete(url);
            }

            function getReviewersAndApprovers(reqId) {
                var url = "api/plm/documents/" + reqId + "/reviewers";
                return httpFactory.get(url)
            }

            function submitDocumentReview(reqId, reviewer) {
                var url = "api/plm/documents/" + reqId + "/reviewer/submit";
                return httpFactory.put(url, reviewer)
            }

            function getDocumentFolderPermissions() {
                var url = "api/plm/documents/permissions";
                return httpFactory.get(url)
            }
        }
    }
);