
define(['app/desktop/modules/accommodation/accommodation.module',
        'app/shared/services/accommodationService',

    ],
    function(module) {
        module.controller('NewAccommodationController', NewAccommodationController);

        function NewAccommodationController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application, $uibModalInstance,AccommodationService) {
            var vm = this;

            vm.cancel = cancel;
            vm.create = create;
            vm.validateAccommodation = validateAccommodation;

            vm.newAccommodation = {
                name: null,
                description:null

            }
            vm.errorMessage = ({
                name: null
            })

            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            function create() {
                if(validateAccommodation(vm.newAccommodation)){
                    $uibModalInstance.close(vm.newAccommodation);
                }

            }

            function validateAccommodation(result){
                var valid = false;

                if (result.name == null || result.name == undefined || result.name == "")
                {
                    vm.errorMessage.name = "Enter Accommodation Name"
                }
                else {
                    valid = true;
                }

                return valid;
            }


            (function() {
                if($application.homeLoaded == true) {

                }
            })();
        }
    }
);

