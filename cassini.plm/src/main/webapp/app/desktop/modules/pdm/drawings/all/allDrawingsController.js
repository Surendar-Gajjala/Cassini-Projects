define(
    [
        'app/desktop/modules/pdm/pdm.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('AllDrawingsController', AllDrawingsController);

        function AllDrawingsController($scope, $rootScope, $timeout, $sce, $state, $stateParams, $window, $application, $cookieStore, $translate) {
            $rootScope.viewInfo.showDetails = false;

            var vm = this;

            (function() {

            })();
        }
    }
);