define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('GitHubService', GitHubService);

        function GitHubService(httpFactory) {
            return {
                createGitHubAccount: createGitHubAccount,
                getAllGitHubAccounts: getAllGitHubAccounts,
                getGitHubAccount: getGitHubAccount,
                updateGitHubAccount: updateGitHubAccount,
                deleteGitHubAccount: deleteGitHubAccount,

                getAllGitHubRepositories: getAllGitHubRepositories,
                getRepositoriesFromGitHub: getRepositoriesFromGitHub,
                addGitHubRepository: addGitHubRepository,
                deleteGitHubRepository: deleteGitHubRepository,

                getGitHubItemRepository: getGitHubItemRepository,
                setGitHubItemRepository: setGitHubItemRepository,

                getGitHubItemRevisionRelease: getGitHubItemRevisionRelease,
                getGitHubRepositoryReleases: getGitHubRepositoryReleases,
                setGitHubItemRevisionRelease: setGitHubItemRevisionRelease,
                deleteGitHubItemRevisionRelease: deleteGitHubItemRevisionRelease
            };

            function createGitHubAccount(account) {
                var url = "api/pdm/github/accounts";
                return httpFactory.post(url, account);
            }

            function getAllGitHubAccounts() {
                var url = "api/pdm/github/accounts";
                return httpFactory.get(url);
            }

            function getGitHubAccount(id) {
                var url = "api/pdm/github/accounts/" + id;
                return httpFactory.get(url);
            }

            function updateGitHubAccount(account) {
                var url = "api/pdm/github/accounts/" + account.id;
                return httpFactory.put(url, account);
            }

            function deleteGitHubAccount(id) {
                var url = "api/pdm/github/accounts/" + id;
                return httpFactory.delete(url);
            }

            function getAllGitHubRepositories() {
                var url = "api/pdm/github/accounts/0/repositories";
                return httpFactory.get(url);
            }

            function getRepositoriesFromGitHub(accountId) {
                var url = "api/pdm/github/accounts/" + accountId + "/repositories/fromgithub";
                return httpFactory.get(url);
            }

            function addGitHubRepository(accountId, repo) {
                var url = "api/pdm/github/accounts/" + accountId + "/repositories";
                return httpFactory.post(url, repo);
            }

            function deleteGitHubRepository(accountId, repoId) {
                var url = "api/pdm/github/accounts/" + accountId + "/repositories/" + repoId;
                return httpFactory.delete(url);
            }

            function getGitHubItemRepository(itemId) {
                var url = "api/pdm/github/accounts/0/repositories/0/item/" + itemId;
                return httpFactory.get(url);
            }

            function setGitHubItemRepository(repoId, itemId) {
                var url = "api/pdm/github/accounts/0/repositories/" + repoId + "/item/" + itemId;
                return httpFactory.post(url, {});
            }

            function getGitHubRepositoryReleases(repoId) {
                var url = "api/pdm/github/accounts/0/repositories/" + repoId + "/releases";
                return httpFactory.get(url);
            }

            function getGitHubItemRevisionRelease(itemRevisionId) {
                var url = "api/pdm/github/accounts/0/repositories/0/item/" + itemRevisionId + "/release";
                return httpFactory.get(url);
            }

            function setGitHubItemRevisionRelease(release) {
                var url = "api/pdm/github/accounts/0/repositories/0/item/" + release.itemRevision + "/release";
                return httpFactory.post(url, release);
            }

            function deleteGitHubItemRevisionRelease(release) {
                var url = "api/pdm/github/accounts/0/repositories/0/item/" + release.itemRevision + "/release";
                return httpFactory.delete(url);
            }
        }
    }
);