define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/shared/services/core/sparePartsService',
        'app/desktop/modules/directives/mesObjectTypeDirective'
    ],
    function (module) {
        module.controller('SelectSparePartController', SelectSparePartController);

        function SelectSparePartController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate, SparePartService) {
            var vm = this;

            vm.loading = true;
            vm.onOk = onOk;
            vm.selectedParts = [];
            var workOrderId = $scope.data.selectedWorkOrderId;
            var assetId = $scope.data.selectedAssetId;
            var mode = $scope.data.mode;
            vm.item = null;
            vm.onSelectType = onSelectType;
            vm.clearFilter = clearFilter;
            vm.searchParts = searchParts;
            vm.checkAll = checkAll;
            vm.select = select;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            $scope.check = false;
            var parsed = angular.element("<div></div>");
            vm.partNumber = parsed.html($translate.instant("PART_NUMBER")).html();
            vm.partName = parsed.html($translate.instant("PART_NAME")).html();
            var SelectAtleastOnePart = parsed.html($translate.instant("SELECT_MFR_PART")).html();
            var PleaseEnterSearchValue = parsed.html($translate.instant("SEARCH_VALUE")).html();
            vm.replacementType = false;

            vm.filters = {
                number: null,
                name: null,
                partType: '',
                description: null,
                searchQuery: null,
                typeName: null,
                workOrder: '',
                asset: ''
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
            vm.spareParts = angular.copy(pagedResults);
            function loadSpareParts() {
                SparePartService.getAllSpareParts(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.spareParts = data;
                        angular.forEach(vm.spareParts.content, function (part) {
                            part.checked = false;
                        })
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    })
            }

            function onOk() {
                if (vm.selectedParts.length == 0) {
                    $rootScope.showWarningMessage(SelectAtleastOnePart)
                } else {
                    $rootScope.hideSidePanel();
                    $scope.callback(vm.selectedParts);
                }
            }

            function checkAll() {
                if (vm.selectAllCheck) {
                    vm.selectedParts = [];
                    vm.selectAllCheck = true;
                    angular.forEach(vm.spareParts.content, function (sparePart) {
                        sparePart.selected = vm.selectAllCheck;
                        vm.selectedParts.push(sparePart);
                    });
                } else {
                    vm.selectAllCheck = false;
                    angular.forEach(vm.spareParts.content, function (sparePart) {
                        sparePart.selected = vm.selectAllCheck;
                        vm.selectedParts = [];
                    });
                }
            }

            vm.selectAllCheck = false;
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

                    if (vm.selectedParts.length == vm.spareParts.content.length) {
                        vm.selectAllCheck = true;
                    }
                    if (vm.selectedParts.length != vm.spareParts.content.length) {
                        vm.selectAllCheck = false;
                    }
                }
            }

            function onSelectType(partType) {
                if (partType != null && partType != undefined) {
                    vm.partType = partType;
                    vm.filters.typeName = partType.name;
                    vm.filters.partType = partType.id;
                    searchParts();
                }
            }

            function searchParts() {
                if (vm.filters.name == null && vm.filters.partType == "" && vm.filters.number == null) {
                    $rootScope.showWarningMessage(PleaseEnterSearchValue)
                } else {
                    vm.pageable.page = 0;
                    vm.clear = true;
                    SparePartService.getAllSpareParts(vm.pageable, vm.filters).then(
                        function (data) {
                            vm.spareParts = data;
                            angular.forEach(vm.spareParts.content, function (part) {
                                part.checked = false;
                            })
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        })
                }
            }

            function clearFilter() {
                vm.filters = {
                    number: null,
                    name: null,
                    partType: '',
                    description: null,
                    searchQuery: null,
                    typeName: null,
                    workOrder: '',
                    asset: ''
                };
                vm.partType = null;
                vm.clear = false;
                setReferenceIds();
            }

            function nextPage() {
                if (!vm.spareParts.last) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.pageable.page++;
                    vm.selectAllCheck = false;
                    vm.selectedParts = [];
                    loadSpareParts();
                }
            }

            function previousPage() {
                if (!vm.spareParts.first) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.pageable.page--;
                    vm.selectAllCheck = false;
                    vm.selectedParts = [];
                    loadSpareParts();
                }
            }

            function setReferenceIds() {
                if ($scope.data.mode == "WORKORDER") {
                    vm.filters.workOrder = $scope.data.selectedWorkOrderId;
                    loadSpareParts();
                }
                else if ($scope.data.mode == "ASSET") {
                    vm.filters.asset = $scope.data.selectedAssetId;
                    loadSpareParts();
                }
            }

            (function () {
                setReferenceIds();
                $rootScope.$on('app.workorder.spareparts.add', onOk);
            })();
        }
    }
);