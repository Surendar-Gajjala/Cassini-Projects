define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/store/topStoreService',
        'app/shared/services/store/topStockIssuedService'
    ],
    function (module) {
        module.controller('IssueSelectionController', IssueSelectionController);

        function IssueSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore,
                                          CommonService, ProjectService, TopStoreService, TopStockIssuedService) {

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

            vm.stockIssues = angular.copy(pagedResults);

            function loadIssues() {
                vm.clear = false;
                vm.loading = true;
                TopStockIssuedService.getPagedStockIssues($stateParams.storeId, pageable).then(
                    function (data) {
                        vm.stockIssues = data;
                        ProjectService.getProjectReferences(vm.stockIssues.content, 'project');
                        TopStoreService.getStoreReferences(vm.stockIssues.content, 'store');
                        angular.forEach(vm.stockIssues.content, function (stockIssue) {
                            stockIssue.checked = false;
                        });
                        vm.loading = false;
                    }
                );
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.stockIssues = [];
                    TopStockIssuedService.stockIssuedFreeTextSearch(0, pageable, freeText).then(
                        function (data) {
                            vm.stockIssues = data;
                            vm.clear = true;
                            ProjectService.getProjectReferences(vm.stockIssues.content, 'project');
                            TopStoreService.getStoreReferences(vm.stockIssues.content, 'store');
                            angular.forEach(vm.stockIssues.content, function (stockIssue) {
                                stockIssue.checked = false;
                            });
                        }
                    )
                } else {
                    resetPage();
                    loadIssues();
                }
            }

            function clearFilter() {
                loadIssues();
                vm.clear = false;
            }

            function resetPage() {
                pageable.page = 0;
            }

            function selectRadioChange(stockIssue, $event) {
                radioChange(stockIssue, $event);
                selectRadio();
            }

            function radioChange(stockIssue, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === stockIssue) {
                    stockIssue.checked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = stockIssue;
                }
            }

            function selectRadio() {
                if (vm.selectedObj != null) {
                    $rootScope.hideSidePanel('left');
                    $scope.callback(vm.selectedObj);
                }

                if (vm.selectedObj == null) {
                    $rootScope.showWarningMessage("Please select Stock Issue");
                }
            }

            function nextPage() {
                if (vm.customIndents.last != true) {
                    pageable.page++;
                    loadIssues();
                }
            }

            function previousPage() {
                if (vm.customIndents.first != true) {
                    pageable.page--;
                    loadIssues();
                }
            }

            (function () {
                $rootScope.$on('app.stockIssue.selected', selectRadio);
                loadIssues();
            })();
        }
    }
)
;

