/**
 * Created by swapna on 28/12/18.
 */
define(['app/desktop/modules/proc/proc.module',
        'app/shared/services/store/roadChallanService'
    ],
    function (module) {
        module.controller('CustomRoadChallanSelectionController', CustomRoadChallanSelectionController);

        function CustomRoadChallanSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies,
                                                      $uibModal, RoadChallanService) {

            var vm = this;

            vm.loading = true;
            vm.selectRadio = selectRadio;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
            vm.selectedObj = null;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                first: true,
                numberOfElements: 0
            };

            vm.customRoadChallans = angular.copy(pagedResults);

            function loadCustomRoadChallans() {
                vm.clear = false;
                vm.loading = true;
                RoadChallanService.getAllRoadChallans(null, pageable).then(
                    function (data) {
                        vm.customRoadChallans = data;
                        angular.forEach(data.content, function (customRoadChallan) {
                            customRoadChallan.isChecked = false;
                        });
                        vm.loading = false;
                    }
                );
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.customRoadChallans = [];
                    RoadChallanService.roadChallanFreeTextSearch(null, pageable, freeText).then(
                        function (data) {
                            vm.customRoadChallans = data;
                            vm.clear = true;
                            angular.forEach(data.content, function (customRoadChallan) {
                                customRoadChallan.isChecked = false;
                            });
                        }
                    )
                } else {
                    resetPage();
                    loadCustomRoadChallans();
                }
            }

            function clearFilter() {
                loadCustomRoadChallans();
                vm.clear = false;
            }

            function resetPage() {
                pageable.page = 0;
            }

            function selectRadioChange(customRoadChallan, $event) {
                radioChange(customRoadChallan, $event);
                selectRadio();
            }

            function radioChange(customRoadChallan, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === customRoadChallan) {
                    customRoadChallan.isChecked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = customRoadChallan;
                }
            }

            function selectRadio() {
                $scope.callback(vm.selectedObj);
                $rootScope.hideSidePanel('left');
            }

            function nextPage() {
                if (vm.customIndents.last != true) {
                    pageable.page++;
                    loadCustomRoadChallans();
                }
            }

            function previousPage() {
                if (vm.customIndents.first != true) {
                    pageable.page--;
                    loadCustomRoadChallans();
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.customRoadChallan.selected', selectRadio);
                    loadCustomRoadChallans();
                }
            })();
        }
    }
)
;
