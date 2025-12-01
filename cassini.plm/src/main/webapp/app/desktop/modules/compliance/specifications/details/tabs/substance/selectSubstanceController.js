define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/shared/services/core/specificationService',
        'app/desktop/modules/directives/pgcObjectTypeDirective'
    ],
    function (module) {
        module.controller('SelectSpecSubstanceController', SelectSpecSubstanceController);

        function SelectSpecSubstanceController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                               ItemService, MfrPartsService, SpecificationService, $translate) {
            var vm = this;

            vm.loading = true;
            vm.items = [];
            vm.onOk = onOk;
            vm.selectedItems = [];
            var specificationId = $scope.data.selectedSpecId;
            vm.item = null;
            vm.onSelectType = onSelectType;
            vm.clearFilter = clearFilter;
            vm.searchSubstances = searchSubstances;
            vm.checkAll = checkAll;
            vm.select = select;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            $scope.check = false;
            var parsed = angular.element("<div></div>");
            vm.number = parsed.html($translate.instant("NUMBER")).html();
            vm.name = parsed.html($translate.instant("NAME")).html();
            var SelectAtleastOneSubstance = parsed.html($translate.instant("SELECT_SUBSTANCE")).html();
            var PleaseEnterSearchValue = parsed.html($translate.instant("SEARCH_VALUE")).html();
            vm.replacementType = false;

            vm.filters = {
                number: null,
                name: null,
                type: '',
                typeName: null,
                specification: ''
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
                vm.filters.specification = specificationId;
                SpecificationService.getSpecificationSubstances(vm.filters, vm.pageable).then(
                    function (data) {
                        vm.substances = data;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    })
            }

            function onOk() {
                if (vm.selectedItems.length == 0) {
                    $rootScope.showWarningMessage(SelectAtleastOneSubstance)
                } else {
                    $rootScope.hideSidePanel();
                    $scope.callback(vm.selectedItems);
                }
            }

            function checkAll() {
                if (vm.selectAllCheck) {
                    vm.selectedItems = [];
                    vm.selectAllCheck = true;
                    angular.forEach(vm.substances.content, function (substance) {
                        substance.selected = vm.selectAllCheck;
                        vm.selectedItems.push(substance);
                    });
                } else {
                    vm.selectAllCheck = false;
                    angular.forEach(vm.substances.content, function (substance) {
                        substance.selected = vm.selectAllCheck;
                        vm.selectedItems = [];
                    });
                }
            }

            vm.selectAllCheck = false;
            function select(substance) {
                var flag = true;
                if (substance.selected == false) {
                    vm.selectAllCheck = false;
                    var index = vm.selectedItems.indexOf(substance);
                    vm.selectedItems.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedItems, function (selectedItem) {
                        if (selectedItem.id == substance.id) {
                            flag = false;
                            var index = vm.selectedItems.indexOf(substance);
                            vm.selectedItems.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedItems.push(substance);
                    }

                    if (vm.selectedItems.length == vm.substances.content.length) {
                        vm.selectAllCheck = true;
                    }
                    if (vm.selectedItems.length != vm.substances.content.length) {
                        vm.selectAllCheck = false;
                    }
                }
            }

            function onSelectType(substanceType) {
                if (substanceType != null && substanceType != undefined) {
                    vm.type = substanceType;
                    vm.type = substanceType;
                    vm.filters.typeName = substanceType.name;
                    vm.filters.type = substanceType.id;
                    searchSubstances();
                }
            }

            function searchSubstances() {
                if (vm.filters.name == null && vm.filters.type == "" && vm.filters.number == null) {
                    $rootScope.showWarningMessage(PleaseEnterSearchValue)
                } else {
                    vm.pageable.page = 0;
                    SpecificationService.getSpecificationSubstances(vm.filters, vm.pageable).then(
                        function (data) {
                            vm.substances = data;
                            vm.clear = true;
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
                    typeName: null,
                    specification: ''
                };
                vm.type=null;
                vm.selectAllCheck = false;
                vm.selectedItems = [];
                vm.pageable.page = 0;
                vm.clear = false;
                loadSubstances();
            }

            function nextPage() {
                if (!vm.substances.last) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.pageable.page++;
                    vm.selectAllCheck = false;
                    vm.selectedItems = [];
                    loadSubstances();
                }
            }

            function previousPage() {
                if (!vm.substances.first) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.pageable.page--;
                    vm.selectAllCheck = false;
                    vm.selectedItems = [];
                    loadSubstances();
                }
            }


            (function () {
                loadSubstances();
                $rootScope.$on('app.select.substance.new', onOk);
            })();
        }
    });