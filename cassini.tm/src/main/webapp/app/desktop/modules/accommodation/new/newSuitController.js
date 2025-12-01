define(['app/desktop/modules/accommodation/accommodation.module'

    ],
    function (module) {
        module.controller('NewSuitController', NewSuitController);

        function NewSuitController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application, $uibModalInstance) {
            var vm = this;

            vm.cancel = cancel;
            vm.create = create;

            vm.newSuit = {
                name: null,
                description: null

            }

            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            function create() {
                $uibModalInstance.close(vm.newSuit);
            }


            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);




