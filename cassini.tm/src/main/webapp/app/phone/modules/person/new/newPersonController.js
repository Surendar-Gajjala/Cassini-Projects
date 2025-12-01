define(
    [
        'app/phone/modules/person/person.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('NewPersonController', NewPersonController);

        function NewPersonController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application, $uibModalInstance,CommonService) {
            var vm = this;


            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);


