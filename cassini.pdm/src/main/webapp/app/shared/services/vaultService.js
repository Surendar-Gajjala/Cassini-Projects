define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('VaultService', VaultService);

        function VaultService(httpFactory) {
            return {
                createVault: createVault,
                getVault: getVault,
                getAllVaults: getAllVaults,
                getAllVaultsPagable: getAllVaultsPagable,
                updateVault: updateVault,
                deleteVault: deleteVault,
                getFolderByPath: getFolderByPath,
                freeTextSearch: freeTextSearch,
                getFilesByPath: getFilesByPath,
                createFolderFiles: createFolderFiles

            };

            function createFolderFiles(folderId, commitId, files) {
                var url = "api/pdm/vaults/folders/" + folderId + "/files?commit=" + commitId;
                return httpFactory.uploadMultiple(url, files);
            }

            function createVault(vault) {
                var url = "api/pdm/vaults";
                return httpFactory.post(url, vault);
            }

            function getVault(vaultId) {
                var url = "api/pdm/vaults/" + vaultId;
                return httpFactory.get(url);
            }

            function getAllVaultsPagable(pageable) {
                var url = "api/pdm/vaults/page?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function freeTextSearch(pageable, freeText) {
                var url = "api/pdm/vaults/freesearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);

                url += "&searchQuery={0}".
                    format(freeText);
                return httpFactory.get(url);
            }


            function getFolderByPath(path) {
                var url = "api/pdm/vaults/find/folders?path=" + path;
                return httpFactory.get(url);
            }

            function getFilesByPath(path) {
                var url = "api/pdm/vaults/find/files?path=" + path;
                return httpFactory.get(url);
            }


            function getAllVaults() {
                var url = "api/pdm/vaults";
                return httpFactory.get(url);
            }


            function updateVault(vault) {
                var url = "api/pdm/vaults/" + vault.id;
                return httpFactory.put(url, vault);
            }

            function deleteVault(vaultId) {
                var url = "api/pdm/vaults/" + vaultId;
                return httpFactory.delete(url);
            }

        }
    }
);