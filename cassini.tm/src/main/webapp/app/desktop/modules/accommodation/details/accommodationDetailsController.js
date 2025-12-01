
define(['app/desktop/modules/accommodation/accommodation.module',
        'app/shared/services/accommodationService'
    ],
    function(module) {
        module.controller('AccommodationDetailsController', AccommodationDetailsController);

        function AccommodationDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, AccommodationService) {
            if ($application.homeLoaded == false) {
                return;
            }
            $rootScope.viewInfo.icon = "fa flaticon-office42";
            $rootScope.viewInfo.title = "Accommodation Details";

            var vm = this;

            vm.loading = true;
            var accId = $stateParams.accommodationId;
            vm.accommodation = null;
            vm.loadAccommodation = loadAccommodation;
            vm.updateAccommodation = updateAccommodation;
            vm.back = back;


            function loadAccommodation() {
                vm.loading = true;
                AccommodationService.getAccommodationById(accId).then(
                    function (data) {
                        vm.accommodation = data;
                        vm.loading = false;
                    }
                )
            }

            function updateAccommodation() {
                AccommodationService.updateAccommodation(vm.accommodation).then(
                    function (data) {
                        $rootScope.showSuccessMessage("Accommodation Updated Successfully!");
                    }
                )
            }

            function back() {
                window.history.back();
                loadAccommodation();
            }

            (function() {
                loadAccommodation();
            })();
        }
    }
);

