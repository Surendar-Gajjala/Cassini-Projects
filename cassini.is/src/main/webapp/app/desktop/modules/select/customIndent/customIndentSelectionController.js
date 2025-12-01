/**
 * Created by swapna on 28/12/18.
 */
define(['app/desktop/modules/proc/proc.module',
        'app/shared/services/store/customIndentService',
        'app/shared/services/store/topStoreService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('CustomIndentSelectionController', CustomIndentSelectionController);

        function CustomIndentSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies,
                                                 $uibModal, CustomIndentService, TopStoreService) {

            var vm = this;

            vm.loading = true;
            vm.selectRadio = selectRadio;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
            vm.selectedObj = null;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;
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

            vm.customIndents = angular.copy(pagedResults);

            function loadCustomIndents() {
                vm.clear = false;
                vm.loading = true;
                CustomIndentService.getPageableIndents(pageable).then(
                    function (data) {
                        vm.customIndents = data;
                        TopStoreService.getStoreReferences(vm.customIndents.content, 'store');
                        angular.forEach(data.content, function (customIndent) {
                            customIndent.isChecked = false;
                        });
                        vm.loading = false;
                    }
                );
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.customIndents = [];
                    CustomIndentService.customIndentFreeTextSearch(pageable, freeText).then(
                        function (data) {
                            vm.customIndents = data;
                            vm.clear = true;
                            TopStoreService.getStoreReferences(vm.customIndents.content, 'store');
                            angular.forEach(data.content, function (customIndent) {
                                customIndent.isChecked = false;
                            });
                        }
                    )
                } else {
                    resetPage();
                    loadCustomIndents();
                }
            }

            function clearFilter() {
                loadCustomIndents();
                vm.clear = false;
            }

            function resetPage() {
                pageable.page = 0;
            }

            function selectRadioChange(customIndent, $event) {
                radioChange(customIndent, $event);
                selectRadio();
            }

            function radioChange(customIndent, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === customIndent) {
                    customIndent.isChecked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = customIndent;
                }
            }

            function selectRadio() {
                $scope.callback(vm.selectedObj);
                $rootScope.hideSidePanel('left');
            }

            function nextPage() {
                if (vm.customIndents.last != true) {
                    pageable.page++;
                    loadCustomIndents();
                }
            }

            function previousPage() {
                if (vm.customIndents.first != true) {
                    pageable.page--;
                    loadCustomIndents();
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.customIndent.selected', selectRadio);
                    loadCustomIndents();
                }
            })();
        }
    }
)
;
