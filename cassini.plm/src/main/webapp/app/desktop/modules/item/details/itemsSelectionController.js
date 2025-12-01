define([
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemBomService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/problemReportService'
    ],
    function (module) {
        module.controller('ItemsSelectionController', ItemsSelectionController);

        function ItemsSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate, ItemService, ItemBomService, ECOService, ProblemReportService) {

            var vm = this;
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };
            vm.loading = true;
            vm.error = "";
            vm.selectionMode = null;
            var selectedItemsMap = new Hashtable();
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

            vm.itemsFilters = {
                itemType: '',
                itemNumber: null,
                itemName: null,
                item: null,
                typeName: null,
                bomItem: '',
                replaceBomItem: '',
                substitutePart: '',
                alternatePart: '',
                eco: '',
                variance: '',
                dcr: '',
                dco: '',
                ecr: '',
                problemReport: '',
                ncr: '',
                qcr: '',
                related: false,
                affectedItemIds: [],
                requirement: ''
            };

            vm.items = angular.copy(pagedResults);
            vm.onSelectType = onSelectType;
            vm.selectCheck = selectCheck;
            vm.selectAll = selectAll;
            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.onOk = onOk;

            vm.loadFilteredItems = loadFilteredItems;

            var parsed = angular.element("<div></div>");
            var selectMessage = parsed.html($translate.instant("ATLEAST_ONE_ITEM_VALIDATION")).html();
            $scope.itemHasPendingChangeOrder = parsed.html($translate.instant("ITEM_HAS_PENDING_C_O")).html();
            $scope.alreadyAddedHasProblemItem = parsed.html($translate.instant("ADDED_HAS_PROBLEM_ITEM")).html();

            vm.selectedItems = [];
            $scope.check = false;
            var revisionId = $stateParams.itemId;
            var selectedItem = $scope.data.multipleItemSelection;
            var variance = $scope.data.selectedMultipleVariance;
            var objectId = $scope.data.selectedObjectId;

            function selectAll(check) {
                if (check) {
                    $scope.check = false;
                    angular.forEach(vm.items.content, function (item) {
                        item.selected = false;
                        var itemExist = selectedItemsMap.get(item.id);
                        if (itemExist != null) {
                            var index = vm.selectedItems.indexOf(itemExist);
                            if (index != -1) {
                                vm.selectedItems.splice(index, 1);
                                selectedItemsMap.remove(item.id);
                            }
                        }
                    })
                } else {
                    $scope.check = true;
                    vm.error = "";
                    angular.forEach(vm.items.content, function (item) {
                        if (!item.pendingEco && !item.alreadyExist) {
                            item.selected = true;
                            var itemExist = selectedItemsMap.get(item.id);
                            if (itemExist == null) {
                                vm.selectedItems.push(item);
                                selectedItemsMap.put(item.id, item);
                            }
                        }
                    })
                }
            }

            function nextPage() {
                if (vm.items.last != true) {
                    vm.pageable.page++;
                    $scope.check = false;
                    loadFilteredItems();
                }
            }

            function previousPage() {
                if (vm.items.first != true) {
                    vm.pageable.page--;
                    $scope.check = false;
                    loadFilteredItems();
                }
            }

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.selectedType = itemType;
                    vm.itemsFilters.itemType = itemType.id;
                    vm.itemsFilters.typeName = itemType.name;
                    vm.pageable.page = 0;
                    loadFilteredItems();
                    vm.clear = true;
                }
            }

            vm.clearSelection = clearSelection;
            function clearSelection() {
                selectedItemsMap = new Hashtable();
                vm.selectedItems = [];
                vm.selectAllCheck = false;
                angular.forEach(vm.items.content, function (item) {
                    item.selected = false;
                })
            }

            function clearFilter() {
                vm.itemsFilters.itemName = null;
                vm.itemsFilters.itemNumber = null;
                vm.itemsFilters.itemType = '';
                vm.itemsFilters.typeName = null;

                vm.selectedType = null;
                $scope.check = false;
                vm.selectAllCheck = false;
                setReferenceIds();
                vm.clear = false;
            }

            function onOk() {
                if (vm.selectedItems.length != 0) {
                    $rootScope.hideSidePanel();
                    $rootScope.hasUnsavedEdits = true;
                    $scope.callback(vm.selectedItems);
                }

                if (vm.selectedItems.length == 0) {
                    $rootScope.showWarningMessage(selectMessage);
                }

            }

            vm.selectAllCheck = false;

            function selectCheck(item) {
                var flag = true;
                vm.error = "";
                angular.forEach(vm.selectedItems, function (selectedItem) {
                    if (selectedItem.id == item.id) {
                        flag = false;
                        var index = vm.selectedItems.indexOf(selectedItem);
                        if (index != -1) {
                            vm.selectedItems.splice(index, 1);
                            selectedItemsMap.remove(item.id);
                        }
                    }
                });
                if (flag) {
                    vm.selectedItems.push(item);
                    selectedItemsMap.put(item.id, item);
                }
                var count = 0;
                angular.forEach(vm.items.content, function (item) {
                    if (item.selected) {
                        count++;
                    }
                })
                if (count != vm.items.content.length) {
                    vm.selectAllCheck = false;
                } else {
                    vm.selectAllCheck = true;
                }
            }

            vm.selectReplaceItem = selectReplaceItem;
            function selectReplaceItem(item) {
                vm.selectedItems = item;
            }

            function loadFilteredItems() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                ItemService.getFilteredItems(vm.pageable, vm.itemsFilters).then(
                    function (data) {
                        vm.items = data;
                        var count = 0;
                        angular.forEach(vm.items.content, function (item) {
                            item.expanded = false;
                            item.level = 0;
                            item.bomChildren = [];
                            var selectedItemExist = selectedItemsMap.get(item.id);
                            if (selectedItemExist != null) {
                                item.selected = true;
                                count++;
                            }
                        })
                        if (vm.items.content.length == count) {
                            vm.selectAllCheck = false;
                        } else {
                            vm.selectAllCheck = false;
                        }
                        ItemService.getLatestRevisionReferences(vm.items.content, 'latestRevision');
                        ItemService.getLatestRevisionReferences(vm.items.content, 'latestReleasedRevision');
                        ItemService.getLatestRevisionReferences(vm.items.content, 'asReleasedRevision');
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.loadFilteredRelatedItems = loadFilteredRelatedItems;
            function loadFilteredRelatedItems() {
                vm.itemsFilters.related = true;
                ECOService.getFilteredItems(vm.pageable, vm.itemsFilters).then(
                    function (data) {
                        vm.items = data;
                        vm.selectAllCheck = false;
                        ItemService.getLatestRevisionReferences(vm.items.content, 'latestRevision');
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.searchFilterItem = searchFilterItem;
            function searchFilterItem() {
                vm.pageable.page = 0;
                setReferenceIds();
                vm.clear = true;
                vm.selectAllCheck = false;
                if ((vm.itemsFilters.itemName == "" || vm.itemsFilters.itemName == null) && (vm.itemsFilters.itemNumber == "" || vm.itemsFilters.itemNumber == null)) {
                    vm.clear = false;
                    vm.selectAllCheck = false;
                }
            }

            function setReferenceIds() {
                if ($scope.data.mode == "ecos") {
                    vm.itemsFilters.eco = $scope.data.selectedMultipleEco;
                    vm.itemsFilters.affectedItemIds = $scope.data.selectedAffectedItemIds;
                    loadFilteredItems();
                } else if ($scope.data.mode == "variances") {
                    vm.itemsFilters.variance = variance.id;
                    loadFilteredItems();
                } else if ($scope.data.mode == "relatedVariances") {
                    vm.itemsFilters.variance = variance.id;
                    loadFilteredRelatedItems();
                } else if ($scope.data.mode == "items") {
                    vm.itemsFilters.item = revisionId;
                    if ($rootScope.selectedMasterItemId != null) {
                        vm.itemsFilters.item = $rootScope.selectedMasterItemId;
                    }
                    if (selectedItem != null) {
                        vm.itemsFilters.bomItem = selectedItem.id;
                    }
                    loadFilteredItems();
                } else if ($scope.data.mode == "substituteItem") {
                    vm.itemsFilters.item = revisionId;
                    if (selectedItem != null) {
                        vm.itemsFilters.replaceBomItem = selectedItem.id;
                    }
                    loadFilteredItems();
                } else if ($scope.data.mode == "substitutePart") {
                    vm.itemsFilters.substitutePart = selectedItem.id;
                    loadFilteredItems();
                } else if ($scope.data.mode == "alternatePart") {
                    vm.itemsFilters.alternatePart = selectedItem.id;
                    loadFilteredItems();
                } else if ($scope.data.mode == "DCR") {
                    vm.itemsFilters.dcr = objectId;
                    loadFilteredItems();
                } else if ($scope.data.mode == "ECR") {
                    vm.itemsFilters.ecr = objectId;
                    loadFilteredItems();
                } else if ($scope.data.mode == "dcos") {
                    vm.itemsFilters.dco = $scope.data.selectedDco;
                    loadFilteredItems();
                } else if ($scope.data.mode == "PROBLEMREPORT") {
                    vm.itemsFilters.problemReport = objectId;
                    loadFilteredItems();
                } else if ($scope.data.mode == "ncr") {
                    vm.itemsFilters.ncr = $scope.data.selectedNcrId;
                    loadFilteredItems();
                } else if ($scope.data.mode == "QCR") {
                    vm.itemsFilters.qcr = objectId;
                    loadFilteredItems();
                } else if ($scope.data.mode == "requirements") {
                    vm.itemsFilters.requirement = $scope.data.selectedItemFilterId;
                    loadFilteredItems();
                }
            }

            vm.toggleNode = toggleNode;
            function toggleNode(bomItem) {
                bomItem.expanded = !bomItem.expanded;
                var index = vm.items.content.indexOf(bomItem);
                if (bomItem.expanded == false) {
                    removeChildren(bomItem);
                } else {
                    if (bomItem.asReleasedRevision != null && bomItem.asReleasedRevision != undefined) {
                        $rootScope.showBusyIndicator($('#rightSidePanel'));
                        ItemBomService.getAsReleasedItemBom(bomItem.asReleasedRevision, objectId).then(
                            function (data) {
                                angular.forEach(data, function (item) {
                                    item.parentBom = bomItem;
                                    item.expanded = false;
                                    item.level = bomItem.level + 1;
                                    item.bomChildren = [];
                                    if (bomItem.bomChildren == undefined) {
                                        bomItem.bomChildren = [];
                                    }
                                    bomItem.bomChildren.push(item);
                                });

                                angular.forEach(bomItem.bomChildren, function (item) {
                                    index = index + 1;
                                    vm.items.content.splice(index, 0, item);
                                });
                                ItemService.getLatestRevisionReferences(vm.items.content, 'asReleasedRevision');
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function removeChildren(bomItem) {
                if (bomItem != null && bomItem.bomChildren != null && bomItem.bomChildren != undefined) {
                    angular.forEach(bomItem.bomChildren, function (item) {
                        removeChildren(item);
                    });

                    var index = vm.items.content.indexOf(bomItem);
                    vm.items.content.splice(index + 1, bomItem.bomChildren.length);
                    bomItem.bomChildren = [];
                    bomItem.expanded = false;

                }
            }

            vm.problemReport = null;
            function loadPr() {
                if (vm.selectionMode == "PROBLEMREPORT") {
                    ProblemReportService.getProblemReport(objectId).then(
                        function (data) {
                            vm.problemReport = data;
                        }
                    )
                }
            }

            (function () {
                vm.selectionMode = $scope.data.mode;
                setReferenceIds();
                loadPr();
                $rootScope.$on("add.select.items", onOk);
            })();
        }
    }
)
;