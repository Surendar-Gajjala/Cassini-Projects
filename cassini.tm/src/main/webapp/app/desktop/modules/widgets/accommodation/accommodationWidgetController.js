/**
 * Created by Anusha on 13-08-2016.
 */
define(['app/desktop/modules/widgets/accommodation/accommodation.module',
        'app/shared/services/accommodationService'
    ],
    function (module) {
        module.controller('AccommodationWidgetController', AccommodationWidgetController);

        function AccommodationWidgetController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, AccommodationService) {
            var vm = this;

            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.loadAccommodations = loadAccommodations;
            vm.accommodations = [];
            vm.loading = true;


            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                }
            };
            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            vm.accommodations = angular.copy(pagedResults);

            function nextPage() {
                if (vm.accommodations.last != true) {
                    pageable.page++;
                    loadAccommodations();
                }
            }

            function previousPage() {
                if (vm.accommodations.first != true) {
                    pageable.page--;
                    loadAccommodations();
                }
            }


            function loadAccommodations() {
                AccommodationService.getAllAccommodations(pageable).then(
                    function (data) {
                        vm.accommodations = data;
                    });
                vm.loading = false;
            }

            (function () {
                loadAccommodations();
            })();
        }
    }
);

