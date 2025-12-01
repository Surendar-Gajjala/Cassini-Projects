define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/problemReportService',
        'app/desktop/modules/pqm/directives/qualityTypeDirective'
    ],
    function (module) {
        module.controller('SelectProblemReportController', SelectProblemReportController);

        function SelectProblemReportController($scope, $rootScope, $timeout, $state, $sce, $stateParams, $cookies, $translate,
                                               ProblemReportService) {
            var vm = this;

            vm.selectedParts = [];
            var ecrId = $scope.data.selectedEcrId;
            vm.loading = true;
            vm.pageable = {
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
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.prFilter = {
                prNumber: null,
                prType: '',
                problem: null,
                searchQuery: null,
                product: null,
                qcr: '',
                ecr: ecrId,
                released: true,
                type: null
            };

            vm.problemReports = angular.copy(pagedResults);
            var parsed = angular.element("<div></div>");
            var selectAtleastOnePart = parsed.html($translate.instant("SELECT_PROBLEM_REPORT")).html();
            var pleaseEnterSearchValue = parsed.html($translate.instant("SEARCH_VALUE")).html();

            vm.nextPage = nextPage;
            function nextPage() {
                if (vm.problemReports.last != true) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadProblemReports();
                }
            }

            vm.previousPage = previousPage;
            function previousPage() {
                if (vm.problemReports.first != true) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadProblemReports();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadProblemReports();
            }

            function loadProblemReports() {
                $rootScope.showBusyIndicator();
                ProblemReportService.getAllProblemReports(vm.pageable, vm.prFilter).then(
                    function (data) {
                        vm.problemReports = data;
                        resizeView();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function onOk() {
                if (vm.selectedParts.length == 0) {
                    $rootScope.showWarningMessage(selectAtleastOnePart)
                } else {
                    $rootScope.hideSidePanel();
                    $scope.callback(vm.selectedParts);
                }
            }

            vm.checkAll = checkAll;
            function checkAll() {
                if (vm.selectAllCheck) {
                    vm.selectedParts = [];
                    vm.selectAllCheck = true;
                    angular.forEach(vm.problemReports.content, function (sparePart) {
                        sparePart.selected = vm.selectAllCheck;
                        vm.selectedParts.push(sparePart);
                    });
                } else {
                    vm.selectAllCheck = false;
                    angular.forEach(vm.problemReports.content, function (sparePart) {
                        sparePart.selected = vm.selectAllCheck;
                        vm.selectedParts = [];
                    });
                }
            }

            vm.selectAllCheck = false;
            vm.select = select;
            function select(sparePart) {
                var flag = true;
                if (sparePart.selected == false) {
                    vm.selectAllCheck = false;
                    var index = vm.selectedParts.indexOf(sparePart);
                    vm.selectedParts.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedParts, function (selectedItem) {
                        if (selectedItem.id == sparePart.id) {
                            flag = false;
                            var index = vm.selectedParts.indexOf(sparePart);
                            vm.selectedParts.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedParts.push(sparePart);
                    }

                    if (vm.selectedParts.length == vm.problemReports.content.length) {
                        vm.selectAllCheck = true;
                    }
                    if (vm.selectedParts.length != vm.problemReports.content.length) {
                        vm.selectAllCheck = false;
                    }
                }
            }

            vm.searchParts = searchParts;
            vm.onSelectType = onSelectType;
            function onSelectType(prType) {
                if (prType != null && prType != undefined) {
                    vm.prFilter.type = prType;
                    vm.prFilter.prType = prType.id;
                    searchParts();
                }
            }

            function searchParts() {
                if (vm.prFilter.prNumber == null && vm.prFilter.prNumber == "" && vm.prFilter.prType == null && vm.prFilter.prType == "" && vm.prFilter.problem == null && vm.prFilter.problem == "") {
                    $rootScope.showWarningMessage(pleaseEnterSearchValue)
                } else {
                    vm.pageable.page = 0;
                    vm.clear = true;
                    ProblemReportService.getAllProblemReports(vm.pageable, vm.prFilter).then(
                        function (data) {
                            vm.problemReports = data;
                            angular.forEach(vm.problemReports.content, function (part) {
                                part.checked = false;
                            })
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        })
                }
            }

            vm.clearFilter = clearFilter;
            function clearFilter() {
                vm.prFilter = {
                    prNumber: null,
                    prType: '',
                    problem: null,
                    searchQuery: null,
                    product: null,
                    qcr: '',
                    ecr: ecrId,
                    released: true,
                    type: null
                };
                vm.clear = false;
                loadProblemReports();
            }

            function resizeView() {
                $timeout(function () {
                    var sidePanelHeight = $('#rightSidePanelContent').outerHeight();
                    $('.problem-reports-view').height(sidePanelHeight - 93);
                }, 500);
            }

            (function () {
                $(window).resize(function () {
                    resizeView();
                });
                loadProblemReports();
                $rootScope.$on('app.ecr.problemreports.add', onOk);
            })();
        }
    }
);