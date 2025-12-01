define(
    [
        'app/phone/modules/person/person.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application'

    ],
    function (module) {
        module.controller('PersonMainController', PersonMainController);

        function PersonMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application) {
            var vm = this;


            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);