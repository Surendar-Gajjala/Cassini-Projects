define(['app/desktop/modules/accommodation/accommodation.module',
        'app/shared/services/accommodationService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'

    ],
    function (module) {
        module.controller('NewBedController', NewBedController);

        function NewBedController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application, $uibModalInstance, AccommodationService,CommonService) {
            var vm = this;

            vm.cancel = cancel;
            vm.create = create;
            vm.persons = [];

            vm.newBed = {
                name: null,
                assignedTo: null,
                description: null

            }

            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            function create() {
                $uibModalInstance.close(vm.newBed);
            }


            function loadPersons() {
                CommonService.getAllPersonsByPersonType(1).then(
                    function(data){
                        vm.persons = data;
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadPersons();
                }
            })();
        }
    }
);