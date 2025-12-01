define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],
    function (module) {
        module.controller('PreferencesController', PreferencesController);

        function PreferencesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {

            var vm = this;

            (function () {

            })();
        }
    }
);