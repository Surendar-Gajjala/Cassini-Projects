define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/shared/services/core/meterService',
        'app/desktop/modules/directives/mesObjectTypeDirective'
    ],
    function (module) {
        module.controller('SelectMeterController', SelectMeterController);

        function SelectMeterController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate, MeterService) {
            var vm = this;

            vm.loading = true;
            vm.onOk = onOk;
            vm.selectedMeters = [];
            var assetId = $scope.data.selectedAssetId;
            var mode = $scope.data.mode;
            vm.item = null;
            vm.onSelectType = onSelectType;
            vm.clearFilter = clearFilter;
            vm.searchMeters = searchMeters;
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
                type: '',
                description: null,
                searchQuery: null,
                typeName: null,
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
            vm.meters = angular.copy(pagedResults);
            function loadMeters() {
                MeterService.getAllMeters(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.meters = data;
                        angular.forEach(vm.meters.content, function (part) {
                            part.checked = false;
                        })
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    })
            }

            function onOk() {
                if (vm.selectedMeters.length == 0) {
                    $rootScope.showWarningMessage(SelectAtleastOnePart)
                } else {
                    $rootScope.hideSidePanel();
                    $scope.callback(vm.selectedMeters);
                }
            }

            function checkAll() {
                if (vm.selectAllCheck) {
                    vm.selectedMeters = [];
                    vm.selectAllCheck = true;
                    angular.forEach(vm.meters.content, function (meter) {
                        meter.selected = vm.selectAllCheck;
                        vm.selectedMeters.push(meter);
                    });
                } else {
                    vm.selectAllCheck = false;
                    angular.forEach(vm.meters.content, function (meter) {
                        meter.selected = vm.selectAllCheck;
                        vm.selectedMeters = [];
                    });
                }
            }

            vm.selectAllCheck = false;
            function select(meter) {
                var flag = true;
                if (meter.selected == false) {
                    vm.selectAllCheck = false;
                    var index = vm.selectedMeters.indexOf(meter);
                    vm.selectedMeters.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedMeters, function (selectedItem) {
                        if (selectedItem.id == meter.id) {
                            flag = false;
                            var index = vm.selectedMeters.indexOf(meter);
                            vm.selectedMeters.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedMeters.push(meter);
                    }

                    if (vm.selectedMeters.length == vm.meters.content.length) {
                        vm.selectAllCheck = true;
                    }
                    if (vm.selectedMeters.length != vm.meters.content.length) {
                        vm.selectAllCheck = false;
                    }
                }
            }

            function onSelectType(type) {
                if (type != null && type != undefined) {
                    vm.type = type;
                    vm.filters.typeName = type.name;
                    vm.filters.type = type.id;
                    searchMeters();
                }
            }

            function searchMeters() {
                if (vm.filters.name == null && vm.filters.type == "" && vm.filters.number == null) {
                    $rootScope.showWarningMessage(PleaseEnterSearchValue)
                } else {
                    vm.pageable.page = 0;
                    vm.clear = true;
                    MeterService.getAllMeters(vm.pageable, vm.filters).then(
                        function (data) {
                            vm.meters = data;
                            angular.forEach(vm.meters.content, function (part) {
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
                    type: '',
                    description: null,
                    searchQuery: null,
                    typeName: null,
                    workOrder: '',
                    asset: ''
                };
                vm.type = null;
                vm.clear = false;
                setReferenceIds();
            }

            function nextPage() {
                if (!vm.meters.last) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.pageable.page++;
                    vm.selectAllCheck = false;
                    vm.selectedMeters = [];
                    loadMeters();
                }
            }

            function previousPage() {
                if (!vm.meters.first) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.pageable.page--;
                    vm.selectAllCheck = false;
                    vm.selectedMeters = [];
                    loadMeters();
                }
            }

            function setReferenceIds() {
                if ($scope.data.mode == "ASSET") {
                    vm.filters.asset = $scope.data.selectedAssetId;
                    loadMeters();
                }
            }

            (function () {
                setReferenceIds();
                $rootScope.$on('app.meter.meters.add', onOk);
            })();
        }
    }
);