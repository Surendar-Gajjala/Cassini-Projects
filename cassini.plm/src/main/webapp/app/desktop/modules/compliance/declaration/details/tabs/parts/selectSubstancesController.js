define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/shared/services/core/substanceService',
        'app/desktop/modules/directives/pgcObjectTypeDirective'
    ],
    function (module) {
        module.controller('SelectSubstancesController', SelectSubstancesController);

        function SelectSubstancesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate, SubstanceService) {
            var vm = this;

            vm.loading = true;
            vm.onOk = onOk;
            vm.selectedSubstances = [];
            var declarationPart = $scope.data.selectedDeclarationPartId;

            vm.onSelectType = onSelectType;
            vm.clearFilter = clearFilter;
            vm.searchSubstances = searchSubstances;
            vm.checkAll = checkAll;
            vm.select = select;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            $scope.check = false;
            var parsed = angular.element("<div></div>");
            vm.partNumber = parsed.html($translate.instant("PART_NUMBER")).html();
            vm.partName = parsed.html($translate.instant("PART_NAME")).html();
            var SelectAtleastOnePart = parsed.html($translate.instant("SELECT_SUBSTANCE")).html();
            var PleaseEnterSearchValue = parsed.html($translate.instant("SEARCH_VALUE")).html();
            vm.replacementType = false;

            vm.filters = {
                number: null,
                name: null,
                type: '',
                description: null,
                searchQuery: null,
                casNumber: null,
                typeName: null,
                declarationPart: declarationPart
            };

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
            vm.substances = angular.copy(pagedResults);
            function loadSubstances() {
                SubstanceService.getAllSubstances(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.substances = data;
                        angular.forEach(vm.substances.content, function (substance) {
                            substance.checked = false;
                        })
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function onOk() {
                if (vm.selectedSubstances.length == 0) {
                    $rootScope.showWarningMessage(SelectAtleastOnePart)
                } else {
                    $rootScope.hideSidePanel();
                    $scope.callback(vm.selectedSubstances);
                }
            }

            function checkAll() {
                if (vm.selectAllCheck) {
                    vm.selectedSubstances = [];
                    vm.selectAllCheck = true;
                    angular.forEach(vm.substances.content, function (sparePart) {
                        sparePart.selected = vm.selectAllCheck;
                        vm.selectedSubstances.push(sparePart);
                    });
                } else {
                    vm.selectAllCheck = false;
                    angular.forEach(vm.substances.content, function (sparePart) {
                        sparePart.selected = vm.selectAllCheck;
                        vm.selectedSubstances = [];
                    });
                }
            }

            vm.selectAllCheck = false;
            function select(sparePart) {
                var flag = true;
                if (sparePart.selected == false) {
                    vm.selectAllCheck = false;
                    var index = vm.selectedSubstances.indexOf(sparePart);
                    vm.selectedSubstances.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedSubstances, function (selectedItem) {
                        if (selectedItem.id == sparePart.id) {
                            flag = false;
                            var index = vm.selectedSubstances.indexOf(sparePart);
                            vm.selectedSubstances.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedSubstances.push(sparePart);
                    }

                    if (vm.selectedSubstances.length == vm.substances.content.length) {
                        vm.selectAllCheck = true;
                    }
                    if (vm.selectedSubstances.length != vm.substances.content.length) {
                        vm.selectAllCheck = false;
                    }
                }
            }

            function onSelectType(partType) {
                if (partType != null && partType != undefined) {
                    vm.partType = partType;
                    vm.filters.typeName = partType.name;
                    vm.filters.type = partType.id;
                    searchSubstances();
                }
            }

            function searchSubstances() {
                if (vm.filters.casNumber == null && vm.filters.type == "" && vm.filters.name == null) {
                    $rootScope.showWarningMessage(PleaseEnterSearchValue)
                } else {
                    vm.pageable.page = 0;
                    vm.clear = true;
                    SubstanceService.getAllSubstances(vm.pageable, vm.filters).then(
                        function (data) {
                            vm.substances = data;
                            angular.forEach(vm.substances.content, function (substance) {
                                substance.checked = false;
                            })
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         })
                }
            }

            function clearFilter() {
                vm.filters = {
                    number: null,
                    name: null,
                    type: '',
                    description: null,
                    searchQuery: null,
                    casNumber: null,
                    typeName: null,
                    declarationPart: declarationPart
                };
                vm.partType = null;
                vm.clear = false;
                loadSubstances();
            }

            function nextPage() {
                if (!vm.substances.last) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.pageable.page++;
                    vm.selectAllCheck = false;
                    vm.selectedSubstances = [];
                    loadSubstances();
                }
            }

            function previousPage() {
                if (!vm.substances.first) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.pageable.page--;
                    vm.selectAllCheck = false;
                    vm.selectedSubstances = [];
                    loadSubstances();
                }
            }


            (function () {
                loadSubstances();
                $rootScope.$on('app.declaration.substances.add', onOk);
            })();
        }
    }
);