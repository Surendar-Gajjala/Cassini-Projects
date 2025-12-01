define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('PDMVaultService', PDMVaultService);

        function PDMVaultService($q, httpFactory) {
            var baseUrl = "api/pdm/vaults";

            return {
                createVault: createVault,
                getVaults: getVaults,
                getSearchVaults: getSearchVaults,
                getVault: getVault,
                updateVault: updateVault,
                deleteVault: deleteVault,
                getFileVersions: getFileVersions,
                getCommits: getCommits,
                getCommitReferences: getCommitReferences,

                getFolder: getFolder,
                getVaultChildrenByPath: getVaultChildrenByPath,
                getSearchVaultChildrenByPath: getSearchVaultChildrenByPath,
                getAttachedToObject: getAttachedToObject,
                generateVisualization: generateVisualization,
                getAttachedToReferences: getAttachedToReferences,

                getFilesByType: getFilesByType,
                search: search
            };

            function createVault(vault) {
                return httpFactory.post(baseUrl, vault);
            }

            function getVaults() {
                return httpFactory.get(baseUrl);
            }

            function getSearchVaults(filters) {
                var url = baseUrl;
                if (filters.searchQuery != null && filters.searchQuery !== undefined) {
                    url += "/free/search?searchQuery=" + filters.searchQuery;
                }
                return httpFactory.get(url);
            }

            function getVault(vaultId) {
                var url = baseUrl + "/" + vaultId;
                return httpFactory.get(url);
            }

            function updateVault(vault) {
                var url = baseUrl + "/" + vault.id;
                return httpFactory.put(url, vault);
            }

            function deleteVault(vaultId) {
                var url = baseUrl + "/" + vaultId;
                return httpFactory.delete(url);
            }

            function getFolder(vaultId, folderId) {
                var url = baseUrl + "/" + vaultId + "/folders/" + folderId;
                return httpFactory.get(url);
            }

            function getVaultChildrenByPath(vaultId, path) {
                var url = baseUrl + "/" + vaultId + "/children";
                if (path != null) {
                    url += "?path=" + path;
                }

                return httpFactory.get(url);
            }

            function getSearchVaultChildrenByPath(vaultId, pageable, filters) {
                var url = baseUrl + "/" + vaultId + "/children?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);

                url += "&searchQuery=" + filters.searchQuery + "&path=" + filters.path;

                return httpFactory.get(url);
            }

            function getAttachedToObject(fileVersion) {
                var url = baseUrl + "/0/folders/0/files/" + fileVersion + "/attachedto";
                return httpFactory.get(url);
            }

            function generateVisualization(fileVersion) {
                var url = baseUrl + "/0/folders/0/files/" + fileVersion + "/visualization";
                return httpFactory.get(url);
            }

            function getFilesByType(fileType, pageable) {
                var url = baseUrl + "/files/type/{0}?page={1}&size={2}&sort={3}:{4}".format(fileType,
                        pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function search(filters, pageable) {
                var url = baseUrl + "/search?page={0}&size={1}&sort={2}:{3}".format(
                        pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                if (filters.fileType != null && filters.fileType !== undefined) {
                    url += "&fileType=" + filters.fileType;
                }
                if (filters.searchQuery != null && filters.searchQuery !== undefined) {
                    url += "&searchQuery=" + filters.searchQuery;
                }

                return httpFactory.get(url);
            }

            function getFileVersions(fileMasterId) {
                var url = baseUrl + "/0/folders/0/files/" + fileMasterId + "/versions";
                return httpFactory.get(url);
            }

            function getCommits(commitIds) {
                var url = baseUrl + "/0/commits/multiple/[" + commitIds + "]";
                return httpFactory.get(url);
            }

            function getAttachedToObjects(attachedToIds) {
                var url = baseUrl + "/0/folders/0/files/attachedto/multiple/[" + attachedToIds + "]";
                return httpFactory.get(url);
            }

            function getAttachedToReferences(objects, property) {
                var attachedToIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && attachedToIds.indexOf(object[property]) == -1) {
                        attachedToIds.push(object[property]);
                    }
                });

                if (attachedToIds.length > 0) {
                    getAttachedToObjects(attachedToIds).then(
                        function (commits) {
                            var map = new Hashtable();
                            angular.forEach(commits, function (commit) {
                                map.put(commit.id, commit);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var commit = map.get(object[property]);
                                    if (commit != null) {
                                        object[property + "Object"] = commit
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function getCommitReferences(objects, property) {
                var commitIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && commitIds.indexOf(object[property]) == -1) {
                        commitIds.push(object[property]);
                    }
                });

                if (commitIds.length > 0) {
                    getCommits(commitIds).then(
                        function (commits) {
                            var map = new Hashtable();
                            angular.forEach(commits, function (commit) {
                                map.put(commit.id, commit);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var commit = map.get(object[property]);
                                    if (commit != null) {
                                        object[property + "Object"] = commit
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