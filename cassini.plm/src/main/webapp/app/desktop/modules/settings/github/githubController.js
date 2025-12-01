define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/shared/services/core/githubService'
    ],
    function (module) {
        module.controller('GitHubController', GitHubController);

        function GitHubController($q, $scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate, $application,
                                  DialogService, GitHubService) {
            var vm = this;

            vm.loading = true;
            vm.githubAccounts = [];
            vm.githubRepositories = [];
            vm.selectedAccount = null;
            var parsed = angular.element("<div></div>");

            function loadAccounts() {
                GitHubService.getAllGitHubAccounts().then(
                    function (data) {
                        vm.githubAccounts = data;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.createNewGitHubAccount = createNewGitHubAccount;
            function createNewGitHubAccount() {
                var options = {
                    title: "GitHub Account",
                    template: 'app/desktop/modules/settings/github/account/githubAccountView.jsp',
                    resolve: 'app/desktop/modules/settings/github/account/githubAccountController',
                    controller: 'GitHubAccountController as githubAccountVm',
                    width: 600,
                    data: null,
                    showMask: true,
                    buttons: [
                        {text: "Create", broadcast: 'app.github.account'}
                    ],
                    callback: function (result) {
                        vm.githubAccounts.push(result)
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.editGitHubAccount = editGitHubAccount;
            function editGitHubAccount($event, account) {
                $event.stopPropagation();
                var options = {
                    title: "GitHub Account",
                    template: 'app/desktop/modules/settings/github/account/githubAccountView.jsp',
                    resolve: 'app/desktop/modules/settings/github/account/githubAccountController',
                    controller: 'GitHubAccountController as githubAccountVm',
                    width: 600,
                    data: {
                        account: account
                    },
                    showMask: true,
                    buttons: [
                        {text: "Update", broadcast: 'app.github.account'}
                    ],
                    callback: function (result) {
                        vm.githubAccounts.push(result)
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.deleteGitHubAccount = deleteGitHubAccount;
            function deleteGitHubAccount($event, account) {
                $event.stopPropagation();
                var options = {
                    title: "Delete GitHub Account",
                    message: "Are you sure you want to delete this account?",
                    okButtonClass: 'btn-danger',
                    okButtonText: "Yes",
                    cancelButtonText: "No"
                };

                DialogService.confirm(options, function (yes) {
                    if (yes) {
                        GitHubService.deleteGitHubAccount(account.id).then(
                            function () {
                                vm.githubAccounts.splice(vm.githubAccounts.indexOf(account), 1);
                                vm.selectedAccount = null;
                                $rootScope.showSuccessMessage("GitHub account deleted successfully.")
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                });
            }

            vm.selectAccount = selectAccount;
            function selectAccount(account) {
                vm.selectedAccount = account;
                loadRepositories();
            }

            function loadRepositories() {
                $rootScope.showBusyIndicator();
                GitHubService.getRepositoriesFromGitHub(vm.selectedAccount.id).then(
                    function (data) {
                        vm.githubRepositories = data;
                        angular.forEach(vm.githubRepositories, function (repo) {
                            repo.time = moment(repo.updatedAt, 'DD/MM/YYYY, HH:mm:sss').fromNow();
                        });
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.toggleRepositorySelection = toggleRepositorySelection;
            function toggleRepositorySelection(repo) {
                if (repo.selected) {
                    GitHubService.addGitHubRepository(vm.selectedAccount.id, repo).then(
                        function (data) {
                            repo.id = data.id;
                            $rootScope.showSuccessMessage("Repository selected successfully.")
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
                else {
                    var options = {
                        title: "De-select GitHub Account",
                        message: "Are you sure you want to de-select this repository?",
                        okButtonClass: 'btn-danger',
                        okButtonText: "Yes",
                        cancelButtonText: "No"
                    };

                    DialogService.confirm(options, function (yes) {
                        if (yes) {
                            GitHubService.deleteGitHubRepository(vm.selectedAccount.id, repo.id).then(
                                function () {
                                    $rootScope.showSuccessMessage("GitHub account de-selected successfully.")
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                        else {
                            repo.selected = true;
                        }
                    });
                }
            }

            (function () {
                loadAccounts();
            })();
        }
    }
);