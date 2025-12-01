/**
 * Created by swapna on 29/01/19.
 */
define(['app/desktop/modules/proc/proc.module',
        'app/shared/services/core/subContractService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('ContractorSelectionController', ContractorSelectionController);

        function ContractorSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies,
                                               $uibModal, SubContractService, CommonService) {

            var vm = this;

            vm.loading = true;
            vm.selectRadio = selectRadio;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
            vm.selectedObj = null;
            var criteria = {
                searchQuery: null
            }
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;
            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
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

            vm.contractors = angular.copy(pagedResults);

            function loadContractors() {
                vm.clear = false;
                vm.loading = true;
                SubContractService.getPageableContractors(pageable).then(
                    function (data) {
                        vm.contractors = data;
                        CommonService.getPersonReferences(vm.contractors.content, 'contact');
                        CommonService.getPersonReferences(vm.contractors.content, 'createdBy');
                        angular.forEach(data.content, function (contractor) {
                            contractor.isChecked = false;
                        });
                        vm.loading = false;
                    }
                );
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.contractors = [];
                    criteria.searchQuery = freeText;
                    SubContractService.contractorsFreeTextSearch(pageable, criteria).then(
                        function (data) {
                            vm.contractors = data;
                            CommonService.getPersonReferences(vm.contractors.content, 'contact');
                            CommonService.getPersonReferences(vm.contractors.content, 'createdBy');
                            vm.clear = true;
                            angular.forEach(data.content, function (contractor) {
                                contractor.isChecked = false;
                            });
                        }
                    )
                } else {
                    resetPage();
                    loadContractors();
                }
            }

            function clearFilter() {
                loadContractors();
                vm.clear = false;
            }

            function resetPage() {
                pageable.page = 0;
            }

            function selectRadioChange(contractor, $event) {
                radioChange(contractor, $event);
                selectRadio();
            }

            function radioChange(contractor, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === contractor) {
                    contractor.isChecked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = contractor;
                }
            }

            function selectRadio() {
                $scope.callback(vm.selectedObj);
                $rootScope.hideSidePanel('left');
            }

            function nextPage() {
                if (vm.contractors.last != true) {
                    pageable.page++;
                    loadContractors();
                }
            }

            function previousPage() {
                if (vm.contractors.first != true) {
                    pageable.page--;
                    loadContractors();
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.contractor.selected', selectRadio);
                    loadContractors();
                }
            })();
        }
    }
)
;