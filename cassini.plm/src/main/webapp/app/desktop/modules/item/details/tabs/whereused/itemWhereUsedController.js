define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemBomService'
    ],
    function (module) {
        module.controller('ItemWhereUsedController', ItemWhereUsedController);

        function ItemWhereUsedController($scope, $rootScope, $timeout, $state, $stateParams, $translate, $cookies, $window,
                                         ItemTypeService, ItemService, ItemBomService) {
            var vm = this;

            vm.loading = true;
            vm.itemId = $stateParams.itemId;
            vm.bomItems = [];
            vm.showAllRevisions = false;
            vm.whereUsedItem = whereUsedItem;
            vm.loadAllRevisions = loadAllRevisions;
            var parsed = angular.element("<div></div>");
            $scope.expandAllTitle = parsed.html($translate.instant("EXPAND_ALL")).html();
            $scope.collapseAllTitle = parsed.html($translate.instant("COLLAPSE_ALL")).html();
            vm.ExpandCollapse = parsed.html($translate.instant("EXPAND_COLLAPSE")).html();

            vm.expandedAll = false;
            vm.showExpandAll = false;
            function loadWhereUsed() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                var hierarchy = false;
                ItemService.getRevisionId(vm.itemId).then(
                    function (data) {
                        vm.itemRevision = data;
                        ItemBomService.getWhereUsed(vm.itemRevision.itemMaster, hierarchy).then(
                            function (data) {
                                vm.whereUsedData = data;
                                vm.bomItems = [];
                                vm.loading = false;

                                vm.itemMasters = [];
                                angular.forEach(vm.whereUsedData, function (bomItem) {
                                    vm.itemMasters.push(bomItem.parent);
                                    bomItem.expanded = false;
                                    bomItem.level = 0;
                                    bomItem.bomChildren = [];
                                    if (bomItem.children > 0) {
                                        vm.showExpandAll = true;
                                    }
                                    if (bomItem.parent.item.latestRevision == bomItem.parent.id) {
                                        vm.bomItems.push(bomItem);
                                    }
                                });

                                ItemService.getItemReferences(vm.itemMasters, 'itemMaster');
                                resizeTable();
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.hideBusyIndicator();
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.expandAllWhereUsed = expandAllWhereUsed;
            function expandAllWhereUsed() {
                vm.expandedAll = !vm.expandedAll;
                var promise = null;
                if (vm.expandedAll) {
                    promise = ItemBomService.getWhereUsed(vm.itemRevision.itemMaster, vm.expandedAll);
                } else {
                    promise = ItemBomService.getWhereUsed(vm.itemRevision.itemMaster, vm.expandedAll);
                }
                if (promise != null) {
                    promise.then(
                        function (data) {
                            vm.whereUsedData = data;
                            vm.bomItems = [];
                            vm.loading = false;

                            vm.itemMasters = [];
                            angular.forEach(vm.whereUsedData, function (bomItem) {
                                vm.itemMasters.push(bomItem.parent);
                                bomItem.expanded = false;
                                bomItem.level = 0;
                                bomItem.bomChildren = [];
                                if (bomItem.children > 0) {
                                    vm.showExpandAll = true;
                                }
                                if (vm.showAllRevisions) {
                                    vm.bomItems.push(bomItem);
                                    if (vm.expandedAll) {
                                        bomItem.expanded = true;
                                        var index = vm.bomItems.indexOf(bomItem);
                                        index = populateChildren(bomItem, index);
                                    }
                                } else if (bomItem.parent.item.latestRevision == bomItem.parent.id) {
                                    vm.bomItems.push(bomItem);
                                    if (vm.expandedAll) {
                                        bomItem.expanded = true;
                                        var index = vm.bomItems.indexOf(bomItem);
                                        index = populateChildren(bomItem, index);
                                    }
                                }
                            });

                            ItemService.getItemReferences(vm.itemMasters, 'itemMaster');
                            resizeTable();
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function populateChildren(bomItem, lastIndex) {
                angular.forEach(bomItem.childrens, function (item) {
                    vm.itemMasters.push(item.parent);
                    lastIndex++;
                    item.level = bomItem.level + 1;
                    item.expanded = true;
                    item.bomChildren = [];
                    vm.bomItems.splice(lastIndex, 0, item);
                    bomItem.bomChildren.push(item);
                    lastIndex = populateChildren(item, lastIndex)
                });

                return lastIndex;
            }

            function resizeTable() {
                if (vm.bomItems.length > 0) {
                    $('#whereUsedTable').height($('#whereUsedTable').closest('.tab-pane').outerHeight() - 60);
                } else {
                    $('#whereUsedTable').height($('#whereUsedTable').closest('.tab-pane').outerHeight());
                }
            }

            function loadAllRevisions(show) {
                $rootScope.showBusyIndicator();
                vm.itemMasters = [];
                if (show) {
                    vm.bomItems = [];
                    angular.forEach(vm.whereUsedData, function (bomItem) {
                        vm.itemMasters.push(bomItem.parent);
                        bomItem.expanded = false;
                        bomItem.level = 0;
                        bomItem.bomChildren = [];
                        vm.bomItems.push(bomItem);
                        if (vm.expandedAll) {
                            bomItem.expanded = true;
                            var index = vm.bomItems.indexOf(bomItem);
                            index = populateChildren(bomItem, index);
                        }
                    });

                    ItemService.getItemReferences(vm.itemMasters, 'itemMaster');
                    vm.loading = false;
                    $rootScope.hideBusyIndicator();
                } else {
                    vm.bomItems = [];
                    angular.forEach(vm.whereUsedData, function (bomItem) {
                        vm.itemMasters.push(bomItem.parent);
                        bomItem.expanded = false;
                        bomItem.level = 0;
                        bomItem.bomChildren = [];
                        if (bomItem.parent.item.latestRevision == bomItem.parent.id) {
                            vm.bomItems.push(bomItem);
                            if (vm.expandedAll) {
                                bomItem.expanded = true;
                                var index = vm.bomItems.indexOf(bomItem);
                                index = populateChildren(bomItem, index);
                            }
                        }
                    });

                    ItemService.getItemReferences(vm.itemMasters, 'itemMaster');
                    vm.loading = false;
                    $rootScope.hideBusyIndicator();
                }
            }

            vm.toggleNode = toggleNode;

            function toggleNode(item) {
                $rootScope.showBusyIndicator();
                item.expanded = !item.expanded;
                var index = vm.bomItems.indexOf(item);
                if (item.expanded) {
                    ItemBomService.getWhereUsedItems(item.parent).then(
                        function (data) {
                            vm.bomItem = data;
                            vm.itemMasters = [];
                            angular.forEach(vm.bomItem, function (bomItem) {
                                vm.itemMasters.push(bomItem.parent);
                                index = index + 1;
                                bomItem.level = item.level + 1;
                                bomItem.expanded = false;
                                bomItem.bomChildren = [];
                                item.bomChildren.push(bomItem);
                                vm.bomItems.splice(index, 0, bomItem);
                            });
                            var count = 0;
                            var levelCount = 0;
                            angular.forEach(vm.bomItems, function (bomItem) {
                                if (bomItem.level == 0 && bomItem.children > 0) {
                                    levelCount++;
                                    if (bomItem.expanded) {
                                        count++;
                                    }
                                }
                                if (levelCount == count) {
                                    vm.expandedAll = true;
                                } else {
                                    vm.expandedAll = false;
                                }
                            })
                            ItemService.getItemReferences(vm.itemMasters, 'itemMaster');
                            vm.loading = false;
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                } else {
                    removeChildren(item);
                    angular.forEach(vm.bomItems, function (bomItem) {
                        if (bomItem.level == 0 && bomItem.children > 0 && !bomItem.expanded) {
                            vm.expandedAll = false;
                        }
                    })
                }
                $rootScope.hideBusyIndicator();
            }

            function removeChildren(bomItem) {
                if (bomItem != null && bomItem.bomChildren != null && bomItem.bomChildren != undefined) {
                    angular.forEach(bomItem.bomChildren, function (item) {
                        removeChildren(item);
                    });

                    var index = vm.bomItems.indexOf(bomItem);
                    vm.bomItems.splice(index + 1, bomItem.bomChildren.length);
                    bomItem.bomChildren = [];
                    bomItem.expanded = false;

                }
            }

            function whereUsedItem(item) {
                //$window.localStorage.setItem("lastSelectedItemTab", JSON.stringify(vm.itemWhereUsedTab));
                $state.go('app.items.details', {itemId: item.parent.id})
            }

            (function () {
                $scope.$on('app.item.tabactivated', function (event, data) {
                    if (data.tabId == 'details.whereUsed') {
                        vm.itemWhereUsedTab = data.tabId;
                        if ($rootScope.selectedMasterItemId != null) {
                            vm.itemId = $rootScope.selectedMasterItemId;
                            loadWhereUsed();
                        }
                        if ($rootScope.selectedMasterItemId == null) {
                            loadWhereUsed();
                        }
                    }

                    $(window).resize(resizeTable);

                    $scope.$on('$destroy', function () {
                        $(window).off('resize', resizeTable);
                    });
                });
            })();
        }
    }
);