define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/operationService',
        'app/desktop/modules/directives/mesObjectTypeDirective'
    ],
    function (module) {
        module.controller('OperationsSelectionController', OperationsSelectionController);

        function OperationsSelectionController($scope, $q, $rootScope, $timeout, $state, $stateParams, $cookies,
                                               $translate, OperationService) {
            var vm = this;
            var parsed = angular.element("<div></div>");
            var selectMessage = parsed.html($translate.instant("ATLEAST_ONE_MBOM_VALIDATION")).html();
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

            vm.operationsFilters = {
                searchQuery: null,
                number: null,
                name: null,
                type: '',
                typeName: null,
                bop: $scope.data.selectedBopId,
                bopPlan: '',
                workCenter:''
            };
            vm.selectedOperations = [];
            vm.loading = false;
            vm.operations = angular.copy(pagedResults);
            vm.selectAllCheck = false;
            vm.selectCheck = selectCheck;
            vm.clearFilter = clearFilter;

            function selectCheck(mbom) {
                var flag = true;
                vm.error = "";
                angular.forEach(vm.selectedOperations, function (selectedOperation) {
                    if (selectedOperation.id == mbom.id) {
                        flag = false;
                        var index = vm.selectedOperations.indexOf(selectedOperation);
                        if (index != -1) {
                            vm.selectedOperations.splice(index, 1);
                            selectedOperationsMap.remove(mbom.id);
                        }
                    }
                });
                if (flag) {
                    vm.selectedOperations.push(mbom);
                    selectedOperationsMap.put(mbom.id, mbom);
                }
                var count = 0;
                angular.forEach(vm.operations.content, function (mbom) {
                    if (mbom.selected) {
                        count++;
                    }
                })
                if (count != vm.operations.content.length) {
                    vm.selectAllCheck = false;
                } else {
                    vm.selectAllCheck = true;
                }
            }

            vm.onSelectType = onSelectType;
            function onSelectType(operationType) {
                if (operationType != null && operationType != undefined) {
                    vm.selectedType = operationType;
                    vm.operationsFilters.type = operationType.id;
                    vm.operationsFilters.typeName = operationType.name;
                    vm.pageable.page = 0;
                    loadOperations();
                    vm.clear = true;
                }
            }

            vm.clearSelection = clearSelection;
            function clearSelection() {
                selectedOperationsMap = new Hashtable();
                vm.selectedOperations = [];
                vm.selectAllCheck = false;
                angular.forEach(vm.operations.content, function (mbom) {
                    mbom.selected = false;
                })
            }

            function clearFilter() {
                vm.operationsFilters.name = null;
                vm.operationsFilters.number = null;
                vm.operationsFilters.type = '';
                vm.operationsFilters.typeName = null;

                vm.selectedType = null;
                $scope.check = false;
                vm.selectAllCheck = false;
                vm.clear = false;
                loadOperations();
            }

            vm.selectAll = selectAll;
            function selectAll(check) {
                if (check) {
                    $scope.check = false;
                    angular.forEach(vm.operations.content, function (mbom) {
                        mbom.selected = false;
                        var mbomExist = selectedOperationsMap.get(mbom.id);
                        if (mbomExist != null) {
                            var index = vm.selectedOperations.indexOf(mbomExist);
                            if (index != -1) {
                                vm.selectedOperations.splice(index, 1);
                                selectedOperationsMap.remove(mbom.id);
                            }
                        }
                    })
                } else {
                    $scope.check = true;
                    vm.error = "";
                    angular.forEach(vm.operations.content, function (mbom) {
                        mbom.selected = true;
                        var mbomExist = selectedOperationsMap.get(mbom.id);
                        if (mbomExist == null) {
                            vm.selectedOperations.push(mbom);
                            selectedOperationsMap.put(mbom.id, mbom);
                        }
                    })
                }
            }

            vm.searchFilterOperations = searchFilterOperations;
            function searchFilterOperations() {
                vm.pageable.page = 0;
                loadOperations();
                vm.clear = true;
                vm.selectAllCheck = false;
                if ((vm.operationsFilters.name == "" || vm.operationsFilters.name == null) && (vm.operationsFilters.number == "" || vm.operationsFilters.number == null)) {
                    vm.clear = false;
                    vm.selectAllCheck = false;
                }
            }

            vm.onOk = onOk;
            function onOk() {
                if (vm.selectedOperations.length != 0) {
                    $rootScope.hideSidePanel();
                    $scope.callback(vm.selectedOperations);
                }

                if (vm.selectedOperations.length == 0) {
                    $rootScope.showWarningMessage(selectMessage);
                }

            }

            vm.nextPage = nextPage;
            function nextPage() {
                if (vm.operations.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadOperations();
                }
            }

            vm.previousPage = previousPage;
            function previousPage() {
                if (vm.operations.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadOperations();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadOperations();
            }

            vm.resetPage = resetPage;
            function resetPage() {
                vm.operations = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.operationsFilters.searchQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadOperations();
            }

            var selectedOperationsMap = new Hashtable();

            function loadOperations() {
                if ($scope.data.selectedBopPlanId != null && $scope.data.selectedBopPlanId != undefined) {
                    vm.operationsFilters.bopPlan = $scope.data.selectedBopPlanId;
                }
                OperationService.getAllOperations(vm.pageable, vm.operationsFilters).then(
                    function (data) {
                        vm.operations = data;
                        var count = 0;
                        angular.forEach(vm.operations.content, function (operation) {
                            operation.selected = false;
                            var selectedItemExist = selectedOperationsMap.get(operation.id);
                            if (selectedItemExist != null) {
                                operation.selected = true;
                                count++;
                            }
                        })
                        if (vm.operations.content.length == count) {
                            vm.selectAllCheck = false;
                        } else {
                            vm.selectAllCheck = false;
                        }
                    }
                )
            }


            (function () {
                loadOperations();
                $rootScope.$on('app.select.bop.plan', onOk);
            })();
        }
    });