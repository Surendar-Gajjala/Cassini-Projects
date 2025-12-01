define(
    [
        'app/shared/services/services.module',
        'app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('DocumentService', DocumentService);

        function DocumentService(httpFactory) {
            return {
                getProjectRootFolders: getProjectRootFolders,
                getAllProjectFolders: getAllProjectFolders,
                createFolder: createFolder,
                updateFolder: updateFolder,
                deleteFolder: deleteFolder,
                getFolder: getFolder,
                getChildren: getChildren,
                getDocuments: getDocuments,
                getDocument: getDocument,
                updateDocument: updateDocument,
                deleteDocument: deleteDocument,
                updateRolePermissions: updateRolePermissions,
                getPermissionsByFolder: getPermissionsByFolder,
                getDocumentsByProject: getDocumentsByProject,
                getFolders: getFolders,
                getFolderReferences: getFolderReferences,
                getMultipleDocuments: getMultipleDocuments,
                getDocumentReferences: getDocumentReferences,
                getDocumentByFolder: getDocumentByFolder,
                getAllFileVersions: getAllFileVersions

            };

            function getFolders(projectId, folderIds) {
                var url = "api/projects/" + projectId + "/folders/multiple/[" + folderIds + "]";
                return httpFactory.get(url);
            }

            function getMultipleDocuments(projectId, documentIds) {
                var url = "api/projects/" + projectId + "/folders/multiple/documents/[" + documentIds + "]";
                return httpFactory.get(url);
            }

            function getDocumentsByProject(projectId) {
                var url = "api/projects/" + projectId + "/documents/all";
                return httpFactory.get(url);
            }

            function updateRolePermissions(projectId, folderId, objectPermissions) {
                var url = "api/projects/" + projectId + "/folders/" + folderId + "/permissions";
                return httpFactory.put(url, objectPermissions);
            }

            function getPermissionsByFolder(projectId, folderId) {
                var url = "api/projects/" + projectId + "/folders/" + folderId + "/permissions";
                return httpFactory.get(url);
            }

            function getProjectRootFolders(projectId, folderType) {
                if (folderType == null || folderType == undefined) {
                    folderType = 'DEFAULT';
                }
                var url = "api/projects/" + projectId + "/folders?folderType=" + folderType;
                return httpFactory.get(url);
            }

            function getAllProjectFolders(projectId, folderType) {
                if (folderType == null || folderType == undefined) {
                    folderType = 'DEFAULT';
                }
                var url = "api/projects/" + projectId + "/folders/" + folderType + "/all";
                return httpFactory.get(url);
            }

            function createFolder(projectId, folder) {
                var url = "api/projects/" + projectId + "/folders";
                return httpFactory.post(url, folder);
            }

            function updateFolder(projectId, folder) {
                var url = "api/projects/" + projectId + "/folders/" + folder.id;
                return httpFactory.put(url, folder);
            }

            function getFolder(projectId, folderId) {
                var url = "api/projects/" + projectId + "/folders/" + folderId;
                return httpFactory.get(url);
            }

            function deleteFolder(projectId, folderId) {
                var url = "api/projects/" + projectId + "/folders/" + folderId;
                return httpFactory.delete(url);
            }

            function getChildren(projectId, folderId) {
                var url = "api/projects/" + projectId + "/folders/" + folderId + "/children";
                return httpFactory.get(url);
            }

            function getDocuments(projectId, folderId) {
                var url = "api/projects/" + projectId + "/folders/" + folderId + "/documents";
                return httpFactory.get(url);
            }

            function getDocumentByFolder(projectId, folderId, documentId) {
                var url = "api/projects/" + projectId + "/folders/" + folderId + "/documents/" + documentId;
                return httpFactory.get(url);
            }

            function getDocument(projectId, documentId) {
                var url = "api/projects/" + projectId + "/documents/" + documentId;
                return httpFactory.get(url);
            }

            function updateDocument(projectId, folderId, document) {
                var url = "api/projects/" + projectId + "/folders/" + folderId + "/documents/" + document.id;
                return httpFactory.put(url, document);
            }

            function deleteDocument(projectId, folderId, documentId) {
                var url = "api/projects/" + projectId + "/folders/" + folderId + "/documents/" + documentId;
                return httpFactory.delete(url);
            }

            function getAllFileVersions(projectId, folderId, documentId) {
                var url = "api/projects/" + projectId + "/folders/" + folderId + "/documents/" + documentId + "/versions";
                return httpFactory.get(url);
            }

            function getFolderReferences(projectId, objects, property) {
                var folderIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && folderIds.indexOf(object[property]) == -1) {
                        folderIds.push(object[property]);
                    }
                });


                if (folderIds.length > 0) {
                    getFolders(projectId, folderIds).then(
                        function (folders) {
                            var map = new Hashtable();
                            angular.forEach(folders, function (folder) {
                                map.put(folder.id, folder);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var folder = map.get(object[property]);
                                    if (folder != null) {
                                        object[property + "Object"] = folder;
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function getDocumentReferences(projectId, objects, property) {
                var documentIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && documentIds.indexOf(object[property]) == -1) {
                        documentIds.push(object[property]);
                    }
                });


                if (documentIds.length > 0) {
                    getMultipleDocuments(projectId, documentIds).then(
                        function (documents) {
                            var map = new Hashtable();
                            angular.forEach(documents, function (document) {
                                map.put(document.id, document);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var document = map.get(object[property]);
                                    if (document != null) {
                                        object[property + "Object"] = document;
                                    }
                                }
                            });
                        }
                    );
                }
            }


        }
    }
);