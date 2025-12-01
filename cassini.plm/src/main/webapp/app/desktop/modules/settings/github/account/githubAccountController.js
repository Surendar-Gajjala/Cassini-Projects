define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/shared/services/core/githubService'
    ],
    function (module) {
        module.controller('GitHubAccountController', GitHubAccountController);

        function GitHubAccountController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies, $translate, $application,
                                         GitHubService) {
            var vm = this;
            var parsed = angular.element("<div></div>");
            if ($scope.data !== undefined && $scope.data != null) {
                vm.githubAccount = angular.copy($scope.data.account);
            }
            else {
                vm.githubAccount = {
                    name: null,
                    description: null,
                    authType: "TOKEN",
                    username: null,
                    password: null,
                    oauthToken: null,
                    organization: null
                };
            }

            function validateAccount() {
                if (vm.githubAccount.name === null || vm.githubAccount.name.trim() === "") {
                    $rootScope.showErrorMessage("GitHub account name is required");
                    return false;
                }
                else if (vm.githubAccount.description === null || vm.githubAccount.description.trim() === "") {
                    $rootScope.showErrorMessage("GitHub account description is required");
                    return false;
                }
                else if (vm.githubAccount.authType === 'TOKEN' && (vm.githubAccount.oauthToken === null || vm.githubAccount.oauthToken.trim() === "")) {
                    $rootScope.showErrorMessage("GitHub account OAuth token is required");
                    return false;
                }
                else if (vm.githubAccount.authType === 'PASSWORD' && (vm.githubAccount.username === null || vm.githubAccount.username.trim() === "")) {
                    $rootScope.showErrorMessage("GitHub account user name is required");
                    return false;
                }
                else if (vm.githubAccount.authType === 'PASSWORD' && (vm.githubAccount.password === null || vm.githubAccount.password.trim() === "")) {
                    $rootScope.showErrorMessage("GitHub account password is required");
                    return false;
                }
                else if (vm.githubAccount.organization === null || vm.githubAccount.organization.trim() === "") {
                    $rootScope.showErrorMessage("GitHub account organization is required");
                    return false;
                }

                return true;
            }

            function createGitHubAccount() {
                if (validateAccount()) {
                    var promise = null;
                    if (vm.githubAccount.id == null) {
                        promise = GitHubService.createGitHubAccount(vm.githubAccount);
                    }
                    else {
                        promise = GitHubService.updateGitHubAccount(vm.githubAccount);
                    }
                    promise.then(
                        function (data) {
                            $rootScope.hideSidePanel();
                            if (vm.githubAccount.id == null) {
                                $rootScope.showSuccessMessage("GitHub account created successfully.");
                                $scope.callback(data);
                            }
                            else {
                                $scope.data.account.name = data.name;
                                $scope.data.account.description = data.description;
                                $scope.data.account.authType = data.authType;
                                $scope.data.account.username = data.username;
                                $scope.data.account.password = data.password;
                                $scope.data.account.oauthToken = data.oauthToken;
                                $scope.data.account.organizaiton = data.organizaiton;

                                $rootScope.showSuccessMessage("GitHub account updated successfully.");
                            }
                        },
                        function (error) {
                            var message = error.message;
                            try {
                                var e = JSON.parse(error.message);
                                message = e.message;
                            } catch (e) {
                            }
                            $rootScope.showErrorMessage(message);
                        }
                    )
                }
            }

            (function () {
                $rootScope.$on('app.github.account', createGitHubAccount);
            })();
        }
    }
);