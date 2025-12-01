define(
    [
        'app/desktop/modules/pdm/pdm.module',
        'app/shared/services/core/pdmVaultService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('AllAssembliesController', AllAssembliesController);

        function AllAssembliesController($scope, $rootScope, $timeout, $sce, $state, $stateParams, $window, $application, $cookieStore, $translate,
                                         PDMVaultService) {
            $rootScope.viewInfo.showDetails = false;
            var vm = this;


            (function() {

            })();
        }
    }
);