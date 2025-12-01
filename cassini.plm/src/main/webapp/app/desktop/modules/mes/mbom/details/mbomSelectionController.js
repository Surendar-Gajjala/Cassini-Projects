define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/modules/directives/mesObjectTypeDirective',
        'app/shared/services/core/mbomService'
    ],
    function (module) {
        module.controller('MBOMSelectionController', MBOMSelectionController);

        function MBOMSelectionController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application,
                                         $stateParams, $cookies, $sce, MBOMService, CommonService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var parsed = angular.element("<div></div>");
            $scope.itemHasPendingChangeOrder = parsed.html($translate.instant("MBOM_HAS_PENDING_C_O")).html();
            var selectMessage = parsed.html($translate.instant("ATLEAST_ONE_MBOM_VALIDATION")).html();
            vm.loading = false;
            vm.mboms = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.resetPage = resetPage;
            vm.clearFilter = clearFilter;
            vm.clearSelection = clearSelection;

            vm.searchText = null;
            vm.filterSearch = null;
            var selectedMbomsMap = new Hashtable();
            vm.selectedMboms = [];

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

            vm.mbomsFilters = {
                searchQuery: null,
                number: null,
                name: null,
                type: '',
                typeName: null,
                mco: $scope.data.selectedMcoId
            };

            vm.mboms = angular.copy(pagedResults);
            vm.selectAllCheck = false;
            vm.selectCheck = selectCheck;

            function selectCheck(mbom) {
                var flag = true;
                vm.error = "";
                angular.forEach(vm.selectedMboms, function (selectedMbom) {
                    if (selectedMbom.id == mbom.id) {
                        flag = false;
                        var index = vm.selectedMboms.indexOf(selectedMbom);
                        if (index != -1) {
                            vm.selectedMboms.splice(index, 1);
                            selectedMbomsMap.remove(mbom.id);
                        }
                    }
                });
                if (flag) {
                    vm.selectedMboms.push(mbom);
                    selectedMbomsMap.put(mbom.id, mbom);
                }
                var count = 0;
                angular.forEach(vm.mboms.content, function (mbom) {
                    if (mbom.selected) {
                        count++;
                    }
                })
                if (count != vm.mboms.content.length) {
                    vm.selectAllCheck = false;
                } else {
                    vm.selectAllCheck = true;
                }
            }

            vm.onSelectType = onSelectType;
            function onSelectType(mbomType) {
                if (mbomType != null && mbomType != undefined) {
                    vm.selectedType = mbomType;
                    vm.mbomsFilters.type = mbomType.id;
                    vm.mbomsFilters.typeName = mbomType.name;
                    vm.pageable.page = 0;
                    loadMBOMS();
                    vm.clear = true;
                }
            }

            vm.selectReplaceMbom = selectReplaceMbom;
            function selectReplaceMbom(mbom) {
                vm.selectedMboms = mbom;
            }

            vm.clearSelection = clearSelection;
            function clearSelection() {
                selectedMbomsMap = new Hashtable();
                vm.selectedMboms = [];
                vm.selectAllCheck = false;
                angular.forEach(vm.mboms.content, function (mbom) {
                    mbom.selected = false;
                })
            }

            function clearFilter() {
                vm.mbomsFilters.name = null;
                vm.mbomsFilters.number = null;
                vm.mbomsFilters.type = '';
                vm.mbomsFilters.typeName = null;

                vm.selectedType = null;
                $scope.check = false;
                vm.selectAllCheck = false;
                vm.clear = false;
                loadMBOMS();
            }

            vm.selectAll = selectAll;
            function selectAll(check) {
                if (check) {
                    $scope.check = false;
                    angular.forEach(vm.mboms.content, function (mbom) {
                        mbom.selected = false;
                        var mbomExist = selectedMbomsMap.get(mbom.id);
                        if (mbomExist != null) {
                            var index = vm.selectedMboms.indexOf(mbomExist);
                            if (index != -1) {
                                vm.selectedMboms.splice(index, 1);
                                selectedMbomsMap.remove(mbom.id);
                            }
                        }
                    })
                } else {
                    $scope.check = true;
                    vm.error = "";
                    angular.forEach(vm.mboms.content, function (mbom) {
                        if (!mbom.pendingMco) {
                            mbom.selected = true;
                            var mbomExist = selectedMbomsMap.get(mbom.id);
                            if (mbomExist == null) {
                                vm.selectedMboms.push(mbom);
                                selectedMbomsMap.put(mbom.id, mbom);
                            }
                        }
                    })
                }
            }

            vm.searchFilterMbom = searchFilterMbom;
            function searchFilterMbom() {
                vm.pageable.page = 0;
                loadMBOMS();
                vm.clear = true;
                vm.selectAllCheck = false;
                if ((vm.mbomsFilters.name == "" || vm.mbomsFilters.name == null) && (vm.mbomsFilters.number == "" || vm.mbomsFilters.number == null)) {
                    vm.clear = false;
                    vm.selectAllCheck = false;
                }
            }

            vm.onOk = onOk;
            function onOk() {
                if (vm.selectedMboms.length != 0) {
                    $rootScope.hideSidePanel();
                    $rootScope.hasUnsavedEdits = true;
                    $scope.callback(vm.selectedMboms);
                }

                if (vm.selectedMboms.length == 0) {
                    $rootScope.showWarningMessage(selectMessage);
                }

            }

            function nextPage() {
                if (vm.mboms.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadMBOMS();
                }
            }

            function previousPage() {
                if (vm.mboms.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadMBOMS();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadMBOMS();
            }

            function resetPage() {
                vm.mboms = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.mbomsFilters.searchQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadMBOMS();
            }

            function loadMBOMS() {
                MBOMService.getAllMBOMs(vm.pageable, vm.mbomsFilters).then(
                    function (data) {
                        vm.mboms = data;
                        var count = 0;
                        angular.forEach(vm.mboms.content, function (mbom) {
                            mbom.expanded = false;
                            mbom.level = 0;
                            var selectedItemExist = selectedMbomsMap.get(mbom.id);
                            if (selectedItemExist != null) {
                                mbom.selected = true;
                                count++;
                            }
                        })
                        if (vm.mboms.content.length == count) {
                            vm.selectAllCheck = false;
                        } else {
                            vm.selectAllCheck = false;
                        }
                    }
                )
            }

            vm.showMBOM = showMBOM;
            function showMBOM() {
                $state.go('app.mes.mbom.details', {mbomId: mbom.latestRevision, tab: 'details.basic'});
            }

            (function () {
                loadMBOMS();
                $rootScope.$on("add.select.mboms", onOk);
            })();

        }
    }
);