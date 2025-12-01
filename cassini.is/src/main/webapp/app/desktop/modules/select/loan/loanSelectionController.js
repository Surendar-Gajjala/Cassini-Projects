/**
 * Created by swapna on 27/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/store/loanService',
        'app/shared/services/store/topStoreService',
        'app/shared/services/pm/project/projectService'
    ],
    function (module) {
        module.controller('LoanSelectionController', LoanSelectionController);

        function LoanSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore,
                                         LoanService, TopStoreService, ProjectService) {

            var vm = this;

            vm.loading = true;
            vm.selectedObj = null;
            vm.selectRadio = selectRadio;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
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

            vm.loans = angular.copy(pagedResults);

            function loadLoans() {
                vm.clear = false;
                vm.loading = true;
                LoanService.getAllLoans(null, pageable).then(
                    function (data) {
                        vm.loans = data;
                        ProjectService.getProjectReferences(vm.loans.content, 'fromProject');
                        ProjectService.getProjectReferences(vm.loans.content, 'toProject');
                        TopStoreService.getStoreReferences(vm.loans.content, 'toStore');
                        TopStoreService.getStoreReferences(vm.loans.content, 'fromStore');
                        angular.forEach(vm.loans.content, function (loan) {
                            loan.checked = false;
                        });
                        vm.loading = false;
                    }
                );
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.loans = [];
                    LoanService.freeTextSearch(null, pageable, freeText).then(
                        function (data) {
                            vm.loans = data;
                            vm.clear = true;
                            ProjectService.getProjectReferences(vm.loans.content, 'fromProject');
                            ProjectService.getProjectReferences(vm.loans.content, 'toProject');
                            TopStoreService.getStoreReferences(vm.loans.content, 'toStore');
                            TopStoreService.getStoreReferences(vm.loans.content, 'fromStore');
                            angular.forEach(vm.loans.content, function (loan) {
                                loan.checked = false;
                            });
                        }
                    )
                } else {
                    resetPage();
                    loadLoans();
                }
            }

            function clearFilter() {
                loadLoans();
                vm.clear = false;
            }

            function resetPage() {
                pageable.page = 0;
            }

            function selectRadioChange(loan, $event) {
                radioChange(loan, $event);
                selectRadio();
            }

            function radioChange(loan, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === loan) {
                    loan.checked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = loan;
                }
            }

            function selectRadio() {
                if (vm.selectedObj != null) {
                    $rootScope.hideSidePanel('left');
                    $scope.callback(vm.selectedObj);
                }

                if (vm.selectedObj == null) {
                    $rootScope.showWarningMessage("Please select loan");
                }
            }

            function nextPage() {
                if (vm.loans.last != true) {
                    pageable.page++;
                    loadLoans();
                }
            }

            function previousPage() {
                if (vm.loans.first != true) {
                    pageable.page--;
                    loadLoans();
                }
            }

            (function () {
                $rootScope.$on('app.loan.selected', selectRadio);
                loadLoans();
            })();
        }
    }
)
;

