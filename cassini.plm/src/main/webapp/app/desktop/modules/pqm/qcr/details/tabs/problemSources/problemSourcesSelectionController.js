define([
        'app/desktop/modules/pqm/pqm.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemBomService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/problemReportService',
        'app/shared/services/core/ncrService',
        'app/desktop/modules/pqm/directives/qualityTypeDirective'
    ],
    function (module) {
        module.controller('ProblemSourcesSelectionController', ProblemSourcesSelectionController);

        function ProblemSourcesSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate, ItemService, ItemBomService, ECOService,
                                                   ProblemReportService, NcrService) {

            var vm = this;
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.error = "";

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.problemSources = angular.copy(pagedResults);

            vm.onSelectType = onSelectType;
            vm.selectCheck = selectCheck;
            vm.selectAll = selectAll;
            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.onOk = onOk;

            var parsed = angular.element("<div></div>");
            var selectOneNcr = parsed.html($translate.instant("SELECT_ONE_NCR")).html();
            var selectOnePr = parsed.html($translate.instant("SELECT_ONE_PR")).html();

            vm.selectedItems = [];
            vm.filters = {
                field1: null,
                field2: null
            };
            vm.prFilter = {
                prNumber: null,
                prType: '',
                prTypeName: null,
                problem: null,
                searchQuery: null,
                product: null,
                qcr: '',
                ecr: '',
                released: true
            };
            vm.ncrFilter = {
                ncrNumber: null,
                ncrType: '',
                ncrTypeName: null,
                title: null,
                description: null,
                searchQuery: null,
                qcr: '',
                released: true
            };
            $scope.check = false;

            function selectAll(check) {
                vm.selectedItems = [];
                if (check) {
                    $scope.check = false;
                    angular.forEach(vm.problemSources.content, function (item) {
                        item.selected = false;
                    })
                } else {
                    $scope.check = true;
                    vm.error = "";
                    angular.forEach(vm.problemSources.content, function (item) {
                        item.selected = true;
                        vm.selectedItems.push(item);
                    })
                }
            }

            function nextPage() {
                if (vm.problemSources.last != true) {
                    vm.pageable.page++;
                    $scope.check = false;
                    vm.selectedItems = [];
                    loadProblemSources();
                }
            }

            function previousPage() {
                if (vm.problemSources.first != true) {
                    vm.pageable.page--;
                    $scope.check = false;
                    vm.selectedItems = [];
                    loadProblemSources();
                }
            }

            function onSelectType(itemType) {
                if ($rootScope.qcr.qcrFor == "PR") {
                    if (itemType != null && itemType != undefined) {
                        vm.selectedType = itemType;
                        vm.prFilter.prType = itemType.id;
                        vm.prFilter.prTypeName = itemType.name;
                        vm.pageable.page = 0;
                        loadProblemSources();
                        vm.clear = true;
                    }
                } else {
                    if (itemType != null && itemType != undefined) {
                        vm.selectedType = itemType;
                        vm.ncrFilter.ncrType = itemType.id;
                        vm.ncrFilter.ncrTypeName = itemType.name;
                        vm.pageable.page = 0;
                        loadProblemSources();
                        vm.clear = true;
                    }
                }
            }


            function clearFilter() {
                if ($rootScope.qcr.qcrFor == "PR") {
                    vm.prFilter.prNumber = null;
                    vm.prFilter.prType = '';
                    vm.prFilter.prTypeName = null;
                    vm.prFilter.product = null;
                    vm.prFilter.problem = null;
                } else {
                    vm.ncrFilter.ncrType = '';
                    vm.ncrFilter.ncrTypeName = null;
                    vm.ncrFilter.ncrNumber = null;
                    vm.ncrFilter.title = null;
                }
                vm.filters.field1 = null;
                vm.filters.field2 = null;
                vm.selectedType = null;
                vm.pageable.page = 0;
                $scope.check = false;
                vm.selectedItems = [];
                vm.selectAllCheck = false;
                loadProblemSources();
                vm.clear = false;
            }

            function onOk() {
                if (vm.selectedItems.length != 0) {
                    $rootScope.hideSidePanel();
                    $scope.callback(vm.selectedItems);
                }

                if (vm.selectedItems.length == 0) {
                    if ($rootScope.qcr.qcrFor == "PR") {
                        $rootScope.showWarningMessage(selectOnePr);
                    } else {
                        $rootScope.showWarningMessage(selectOneNcr);
                    }
                }

            }

            vm.selectAllCheck = false;

            function selectCheck(item) {
                var flag = true;
                vm.error = "";
                angular.forEach(vm.selectedItems, function (selectedItem) {
                    if (selectedItem.id == item.id) {
                        flag = false;
                        var index = vm.selectedItems.indexOf(item);
                        vm.selectedItems.splice(index, 1);
                    }
                });
                if (flag) {
                    vm.selectedItems.push(item);
                }

                if (vm.selectedItems.length != vm.problemSources.content.length) {
                    vm.selectAllCheck = false;
                } else {
                    vm.selectAllCheck = true;
                }
            }

            vm.searchFilterItem = searchFilterItem;
            function searchFilterItem() {
                if ($rootScope.qcr.qcrFor == "PR") {
                    vm.prFilter.prNumber = vm.filters.field1;
                    vm.prFilter.problem = vm.filters.field2;
                    vm.pageable.page = 0;
                    loadProblemSources();
                } else {
                    vm.ncrFilter.ncrNumber = vm.filters.field1;
                    vm.ncrFilter.title = vm.filters.field2;
                    vm.pageable.page = 0;
                    loadProblemSources();
                }
                vm.clear = true;
                vm.selectAllCheck = false;
                if ((vm.filters.field1 == "" || vm.filters.field1 == null) && (vm.filters.field2 == "" || vm.filters.field2 == null)) {
                    vm.clear = false;
                    vm.selectAllCheck = false;
                }
            }

            function loadProblemSources() {
                if ($rootScope.qcr.qcrFor == "PR") {
                    $scope.field2Title = parsed.html($translate.instant("PROBLEM")).html();
                    vm.prFilter.qcr = $rootScope.qcr.id;
                    ProblemReportService.getAllProblemReports(vm.pageable, vm.prFilter).then(
                        function (data) {
                            vm.problemSources = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    $scope.field2Title = parsed.html($translate.instant("TITLE")).html();
                    vm.ncrFilter.qcr = $rootScope.qcr.id;
                    NcrService.getAllNcrs(vm.pageable, vm.ncrFilter).then(
                        function (data) {
                            vm.problemSources = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            (function () {
                //if ($application.homeLoaded == true) {
                $scope.qcrFor = $rootScope.qcr.qcrFor;
                loadProblemSources();
                $rootScope.$on("add.select.problemSources", onOk);
                //}
            })();
        }
    }
)
;