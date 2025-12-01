/**
 * Created by swapna on 28/12/18.
 */
define(['app/desktop/modules/proc/proc.module',
        'app/shared/services/store/requisitionService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('CustomRequisitionSelectionController', CustomRequisitionSelectionController);

        function CustomRequisitionSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies,
                                                      $uibModal, RequisitionService) {

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

            vm.customRequisitions = angular.copy(pagedResults);

            function loadCustomRequisitions() {
                vm.clear = false;
                vm.loading = true;
                RequisitionService.getAllPageableRequisitions(null, pageable).then(
                    function (data) {
                        vm.customRequisitions = data;
                        angular.forEach(data.content, function (customRequisition) {
                            customRequisition.isChecked = false;
                        });
                        vm.loading = false;
                    }
                );
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.customRequisitions = [];
                    RequisitionService.requisitionFreeTextSearch(null, pageable, freeText).then(
                        function (data) {
                            vm.customRequisitions = data;
                            vm.clear = true;
                            angular.forEach(data.content, function (customRequisition) {
                                customRequisition.isChecked = false;
                            });
                        }
                    )
                } else {
                    resetPage();
                    loadCustomRequisitions();
                }
            }

            function clearFilter() {
                loadCustomRequisitions();
                vm.clear = false;
            }

            function resetPage() {
                pageable.page = 0;
            }

            function selectRadioChange(customRequisition, $event) {
                radioChange(customRequisition, $event);
                selectRadio();
            }

            function radioChange(customRequisition, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === customRequisition) {
                    customRequisition.isChecked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = customRequisition;
                }
            }

            function selectRadio() {
                $scope.callback(vm.selectedObj);
                $rootScope.hideSidePanel('left');
            }

            function nextPage() {
                if (vm.customIndents.last != true) {
                    pageable.page++;
                    loadCustomRequisitions();
                }
            }

            function previousPage() {
                if (vm.customIndents.first != true) {
                    pageable.page--;
                    loadCustomRequisitions();
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.customRequisition.selected', selectRadio);
                    loadCustomRequisitions();
                }
            })();
        }
    }
)
;
