define(['app/desktop/modules/proc/proc.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/itemService'
    ],
    function (module) {
        module.controller('MaterialSelectionController', MaterialSelectionController);

        function MaterialSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore,
                                             $uibModal, ItemService) {

            var vm = this;

            vm.loading = true;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.selectRadio = selectRadio;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
            vm.freeText = null;

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
                sort: null,
                first: true,
                numberOfElements: 0
            };

            var materialCriteria = {
                freeTextSearch: true
            };

            vm.selectedObj = null;
            vm.materials = angular.copy(pagedResults);

            function nextPage() {
                if (vm.materials.last != true) {
                    pageable.page++;
                    loadMaterials();
                }
            }

            function previousPage() {
                if (vm.materials.first != true) {
                    pageable.page--;
                    loadMaterials();
                }
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    materialCriteria.searchQuery = freeText;
                    ItemService.materialFreeTextSearch(pageable, materialCriteria).then(
                        function (data) {
                            vm.materials = data;
                            vm.clear = true;
                        }
                    )
                } else {
                    resetPage();
                    loadMaterials();
                }
            }

            function clearFilter() {
                loadMaterials();
                vm.clear = false;
            }

            function resetPage() {
                pageable.page = 0;
            }

            function loadMaterials() {
                vm.clear = false;
                vm.loading = true;
                ItemService.getMaterials(pageable).then(
                    function (data) {
                        vm.materials = data;
                        vm.loading = false;
                        angular.forEach(data, function (material) {
                            material.isChecked = false;
                        });
                    }
                );
            }

            function selectRadioChange(material, $event) {
                radioChange(material, $event);
                selectRadio();
            }

            function radioChange(material, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === material) {
                    material.isChecked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = material;
                }
            }

            function selectRadio() {
                $scope.callback(vm.selectedObj);
                $rootScope.hideSidePanel('left');
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.material.selected', selectRadio);
                    loadMaterials();
                }
            })();
        }
    }
)
;
