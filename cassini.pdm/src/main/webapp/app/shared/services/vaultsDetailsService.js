define(['app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'],
    function (mdoule) {
        mdoule.factory('VaultsDetailsService', VaultsDetailsService);

        function VaultsDetailsService(httpFactory) {
            return {
                getVaultRootFolders: getVaultRootFolders,
                getAllVaultFolders: getAllVaultFolders,
                createFolder: createFolder,
                updateFolder: updateFolder,
                deleteFolder: deleteFolder,
                getFolder: getFolder,
                getFiles: getFiles,
                updateFile: updateFile,
                getFolders: getFolders,
                getFilesByFolder: getFilesByFolder,
                getAllFileVersion: getAllFileVersion,
                updateRolePermissions: updateRolePermissions,
                getPermissionsByFolder: getPermissionsByFolder,
                deleteFile: deleteFile,
                freeTextSearch: freeTextSearch,
                freeTextSearchforFolder: freeTextSearchforFolder,
                getAllCommits: getAllCommits,
                createCommit: createCommit,
                getCommits: getCommits

            };

            function getFilesByFolder(folderId) {
                var url = "api/pdm/vaults/folders/" + folderId + "/files";
                return httpFactory.get(url);
            }

            function getFolders(vaultId, folderIds) {
                var url = "api/pdm/vaults/" + vaultId + "/folders/multiple/[" + folderIds + "]";
                return httpFactory.get(url);
            }

            function getVaultRootFolders(vaultId) {

                var url = "api/pdm/vaults/" + vaultId + "/folders";
                return httpFactory.get(url);
            }

            function getAllVaultFolders(vaultId) {

                var url = "api/pdm/vaults/" + vaultId + "/folders/all";
                return httpFactory.get(url);
            }

            function createFolder(vaultId, folder) {
                var url = "api/pdm/vaults/" + vaultId + "/folders";
                return httpFactory.post(url, folder);
            }

            function updateFolder(vaultId, folder) {
                var url = "api/pdm/vaults/" + vaultId + "/folders/" + folder.id;
                return httpFactory.put(url, folder);
            }

            function getFolder(vaultId, folderId) {
                var url = "api/pdm/vaults/" + vaultId + "/folders/" + folderId;
                return httpFactory.get(url);
            }

            function deleteFolder(vaultId, folderId) {
                var url = "api/pdm/vaults/" + vaultId + "/folders/" + folderId;
                return httpFactory.delete(url);
            }

            function getFiles(vaultId, folderId) {
                var url = "api/pdm/vaults/" + vaultId + "/folders/" + folderId + "/files";
                return httpFactory.get(url);
            }

            function updateFile(folderId, file) {
                var url = "api/pdm/vaults/folders/" + folderId + "/files/" + file.id;
                return httpFactory.put(url, file);
            }

            function deleteFile(folderId, file) {
                var url = "api/pdm/vaults/folders/" + folderId + "/files/" + file.id;
                return httpFactory.delete(url);
            }

            function getAllFileVersion(folderId, fileId) {
                var url = "api/pdm/vaults/folders/" + folderId + "/files/" + fileId + "/versions";
                return httpFactory.get(url, fileId);
            }

            function updateRolePermissions(vaultId, folderId, objectPermissions) {
                var url = "api/pdm/vaults/" + vaultId + "/folders/" + folderId + "/permissions";
                return httpFactory.post(url, objectPermissions);
            }

            function getPermissionsByFolder(vaultId, folderId) {
                var url = "api/pdm/vaults/" + vaultId + "/folders/" + folderId + "/permissions";
                return httpFactory.get(url);
            }

            function freeTextSearch(freeText, folderId, pageable) {
                var url = "api/pdm/vaults/folders/" + folderId + "/files/freesearch/" + freeText + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function freeTextSearchforFolder(freeText, vaultId, pageable) {
                var url = "api/pdm/vaults/" + vaultId + "/folders/freesearch/" + freeText + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getAllCommits() {
                var url = "api/pdm/vaults/commits/getAllCommits";
                return httpFactory.get(url);
            }


            function getCommits(commitsId) {
                var url = "api/pdm/vaults/commits/" + commitsId;
                return httpFactory.get(url);
            }

            function createCommit(commit) {
                var url = "api/pdm/vaults/commits/createCommit";
                return httpFactory.post(url, commit);
            }

        }
    }
);