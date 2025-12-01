define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/shared/services/core/githubService'
    ],
    function (module) {
        module.controller('GitHubRepositoryController', GitHubRepositoryController);

        function GitHubRepositoryController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies, $translate, $application,
                                  GitHubService) {
            var vm = this;


            (function () {
                
            })();
        }
    }
);