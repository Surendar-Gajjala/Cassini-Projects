define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/shared/services/core/workCenterService',
        'app/desktop/modules/directives/mesObjectTypeDirective'
    ],
    function (module) {
        module.controller('SelectAssemblyLineWorkCenterController', SelectAssemblyLineWorkCenterController);

        function SelectAssemblyLineWorkCenterController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, CommonService,
                                                        WorkCenterService, $translate) {
            var vm = this;

            vm.loading = true;
            vm.items = [];
            vm.onOk = onOk;
            vm.selectedItems = [];
            vm.item = null;
            vm.onSelectType = onSelectType;
            vm.clearFilter = clearFilter;
            vm.searchWorkCenters = searchWorkCenters;
            vm.checkAll = checkAll;
            vm.select = select;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            $scope.check = false;
            var parsed = angular.element("<div></div>");
            vm.number = parsed.html($translate.instant("NUMBER")).html();
            vm.name = parsed.html($translate.instant("NAME")).html();
            var SelectAtleastOneWorkcenter = parsed.html($translate.instant("SELECT_ASSEMBLY_LINE_WORK_CENTER")).html();
            var PleaseEnterSearchValue = parsed.html($translate.instant("SEARCH_VALUE")).html();
            var workCenterAddMsg = parsed.html($translate.instant("WORK_CENTERS_ADD_MSG")).html();
            vm.replacementType = false;
            var assemblyLineId = $scope.data.selectedAssemblyLineId;
            vm.filters = {
                searchQuery: null,
                name: null,
                number: null,
                type: '',
                typeName: null,
                assemblyLine: true
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
            vm.workCenters = angular.copy(pagedResults);

            function onOk() {
                if (vm.selectedItems.length == 0) {
                    $rootScope.showWarningMessage(SelectAtleastOneWorkcenter)
                } else {
                    WorkCenterService.updateMultipleAssemblyLineWorkCenters(assemblyLineId, vm.selectedItems).then(
                        function (data) {
                            $rootScope.hideSidePanel();
                            $rootScope.showSuccessMessage(workCenterAddMsg);
                            $scope.callback(vm.selectedItems);
                        }
                    );
                }
            }

            function checkAll() {
                if (vm.selectAllCheck) {
                    vm.selectedItems = [];
                    vm.selectAllCheck = true;
                    angular.forEach(vm.workCenters.content, function (substance) {
                        substance.selected = vm.selectAllCheck;
                        vm.selectedItems.push(substance);
                    });
                } else {
                    vm.selectAllCheck = false;
                    angular.forEach(vm.workCenters.content, function (substance) {
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

                    if (vm.selectedItems.length == vm.workCenters.content.length) {
                        vm.selectAllCheck = true;
                    }
                    if (vm.selectedItems.length != vm.workCenters.content.length) {
                        vm.selectAllCheck = false;
                    }
                }
            }

            function onSelectType(type) {
                if (type != null && type != undefined) {
                    vm.type = type;
                    $scope.type = type;
                    vm.filters.type = type.id;
                    vm.filters.typeName = type.name;
                    searchWorkCenters();
                }
            }

            function searchWorkCenters() {
                if (vm.filters.name == null && vm.filters.type == "" && vm.filters.number == null) {
                    $rootScope.showWarningMessage(PleaseEnterSearchValue)
                } else {
                    vm.pageable.page = 0;
                    WorkCenterService.getAllWorkCenters(vm.pageable, vm.filters).then(
                        function (data) {
                            vm.workCenters = data;
                            vm.clear = true;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        })
                }
            }

            function clearFilter() {
                vm.filters = {
                    searchQuery: null,
                    name: null,
                    number: null,
                    type: '',
                    typeName: null,
                    assemblyLine: true
                };
                vm.type = null;
                vm.selectAllCheck = false;
                vm.selectedItems = [];
                vm.pageable.page = 0;
                vm.clear = false;
                loadWorkCenters();
            }

            function nextPage() {
                if (!vm.workCenters.last) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.pageable.page++;
                    vm.selectAllCheck = false;
                    vm.selectedItems = [];
                    loadWorkCenters();
                }
            }

            function previousPage() {
                if (!vm.workCenters.first) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.pageable.page--;
                    vm.selectAllCheck = false;
                    vm.selectedItems = [];
                    loadWorkCenters();
                }
            }

            function loadWorkCenters() {
                vm.loading = true;
                WorkCenterService.getAllWorkCenters(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.workCenters = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            (function () {
                loadWorkCenters();
                $rootScope.$on('app.assl.wcs.add', onOk);
            })();
        }
    });