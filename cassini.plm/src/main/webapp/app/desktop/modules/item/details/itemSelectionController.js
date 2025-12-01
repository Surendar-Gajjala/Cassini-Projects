define(['app/desktop/modules/item/item.module',
        'app/shared/services/core/itemService',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/desktop/modules/classification/directive/classificationTreeController',
        'app/shared/services/core/itemBomService',
        'app/shared/services/core/ecoService'
    ],
    function (module) {
        module.controller('ItemSelectionController', ItemSelectionController);

        function ItemSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                         ItemService, ItemBomService, ECOService) {
            var vm = this;
            vm.loadItems = loadItems;
            vm.onSelectType = onSelectType;
            vm.searchItem = searchItem;
            vm.clearFilter = clearFilter;
            var selectedItem = $scope.data.itemSelectionItem;
            var ecoId = $scope.data.selectedEco;
            vm.onOk = onOk;
            vm.prId = $stateParams.prId;
            vm.item = null;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;

            vm.loadFilteredItems = loadFilteredItems;

            var parsed = angular.element("<div></div>");
            var selectMessage = parsed.html($translate.instant("ATLEAST_ONE_ITEM_VALIDATION")).html();

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

            vm.revision = null;

            vm.filters = {
                itemType: null,
                itemName: null,
                itemNumber: null,
                latestRevision: null,
                status: null
                //description: null
            };

            vm.items = angular.copy(pagedResults);
            var item = null;
            vm.radioChange = radioChange;
            var revisionId = $stateParams.itemId;

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.selectedType = itemType;
                    vm.itemsFilters.itemType = itemType.id;
                    vm.pageable.page = 0;
                    loadFilteredItems();
                    vm.clear = true;
                }
            }

            function radioChange(item) {
                if (vm.item === item) {
                    item.checked = false;
                    vm.item = null
                } else {
                    vm.item = item;
                }
            }

            function nextPage() {
                if (vm.items.last != true) {
                    vm.pageable.size = pagedResults.size;
                    vm.pageable.page++;
                    loadFilteredItems();
                }
            }

            function previousPage() {
                if (vm.items.first != true) {
                    vm.pageable.page--;
                    loadFilteredItems();
                }
            }

            function clearFilter() {
                vm.itemsFilters.itemName = null;
                vm.itemsFilters.itemNumber = null;
                vm.itemsFilters.itemType = '';
                vm.selectedType = null;
                loadFilteredItems();
                vm.clear = false;
            }

            function searchItem() {
                ItemService.searchItem(pageable, vm.filters).then(
                    function (data) {
                        vm.items = angular.copy(pagedResults);
                        vm.items = data;
                        vm.clear = true;
                        if (revisionId != undefined) {
                            angular.forEach(vm.items.content, function (item) {
                                if (item.latestRevision == revisionId) {
                                    var index = vm.items.content.indexOf(item);
                                    vm.items.content.splice(index, 1);
                                }
                            })
                        }
                        if (vm.itemBoms != null && vm.itemBoms != undefined) {
                            angular.forEach(vm.itemBoms, function (itemBom) {
                                angular.forEach(vm.items.content, function (item) {
                                    if (item.id == itemBom.id) {
                                        var index = vm.items.content.indexOf(item);
                                        vm.items.content.splice(index, 1);
                                    }
                                })
                            })
                        }
                        if (selectedItem != null && selectedItem.parent != undefined) {
                            angular.forEach(vm.items.content, function (item) {
                                if (item.id == selectedItem.parent.itemMaster) {
                                    var index = vm.items.content.indexOf(item);
                                    vm.items.content.splice(index, 1);
                                }
                            })
                        }
                        if (vm.selectedBomChildren != null && vm.selectedBomChildren != undefined) {
                            angular.forEach(vm.selectedBomChildren, function (bomChildren) {
                                angular.forEach(vm.items.content, function (item) {
                                    if (item.id == bomChildren.id) {
                                        var index = vm.items.content.indexOf(item);
                                        vm.items.content.splice(index, 1);
                                    }
                                })
                            })
                        }
                        ItemService.getLatestRevisionReferences(vm.items.content, 'latestRevision');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    });

            }

            function onOk() {
                if (vm.item != null) {
                    $scope.callback(vm.item);
                    $rootScope.hideSidePanel();
                }

                if (vm.item == null) {
                    $rootScope.showWarningMessage(selectMessage);
                }

            }

            var itemMaster = null;

            function loadItem() {
                ItemService.getRevisionId(revisionId).then(
                    function (data) {
                        itemMaster = data.itemMaster;
                        loadItems();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadItems() {
                vm.items = angular.copy(pagedResults);
                var promise = ItemService.getItems(pageable);
                promise.then(
                    function (data) {
                        vm.items = data;
                        if (itemMaster != undefined) {
                            angular.forEach(vm.items.content, function (item) {
                                if (item.id == itemMaster) {
                                    var index = vm.items.content.indexOf(item);
                                    vm.items.content.splice(index, 1);
                                }
                            })
                        }
                        if (selectedItem != null && selectedItem.parent != undefined) {
                            if (vm.itemBoms != null && vm.itemBoms != undefined) {
                                angular.forEach(vm.itemBoms, function (itemBom) {
                                    angular.forEach(vm.items.content, function (item) {
                                        if (item.id == itemBom.id) {
                                            var index = vm.items.content.indexOf(item);
                                            vm.items.content.splice(index, 1);
                                        }
                                    })
                                })
                            }
                        }
                        if (selectedItem != null && selectedItem.parent != undefined) {
                            angular.forEach(vm.items.content, function (item) {
                                if (item.id == selectedItem.parent.itemMaster) {
                                    var index = vm.items.content.indexOf(item);
                                    vm.items.content.splice(index, 1);
                                }
                            })
                        }
                        if (selectedItem != null && selectedItem.parentBom.parent != undefined) {
                            angular.forEach(vm.items.content, function (item) {
                                if (item.id == selectedItem.parentBom.parent.itemMaster) {
                                    var index = vm.items.content.indexOf(item);
                                    vm.items.content.splice(index, 1);
                                }
                            })
                        }
                        if (vm.selectedBomChildren != null && vm.selectedBomChildren != undefined) {
                            angular.forEach(vm.selectedBomChildren, function (bomChildren) {
                                angular.forEach(vm.items.content, function (item) {
                                    if (item.id == bomChildren.id) {
                                        var index = vm.items.content.indexOf(item);
                                        vm.items.content.splice(index, 1);
                                    }
                                })
                            })
                        }
                        ItemService.getLatestRevisionReferences(vm.items.content, 'latestRevision');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadItemsForEcos() {
                vm.items = angular.copy(pagedResults);
                var promise = ItemService.getItems(pageable);
                promise.then(
                    function (data) {
                        vm.items = data;
                        angular.forEach(vm.affectedItems, function (affectedItem) {
                            angular.forEach(vm.items.content, function (item) {
                                if (item.id == affectedItem.itemMaster) {
                                    var index = vm.items.content.indexOf(item);
                                    vm.items.content.splice(index, 1);
                                }
                            })
                        })
                        ItemService.getLatestRevisionReferences(vm.items.content, 'latestRevision');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.affectedItems = [];
            function loadEcoAffectedItems() {
                vm.affectedItems = [];
                ECOService.getEcoAffectedItems(ecoId.change).then(
                    function (data) {
                        vm.ecoAffectedItems = data;
                        angular.forEach(vm.ecoAffectedItems, function (item) {
                            ItemService.getRevisionId(item.item).then(
                                function (data) {
                                    vm.affectedItems.push(data);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        });
                        $timeout(function () {
                            loadItemsForEcos();
                        }, 1000);

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.selectedBomChildren = [];
            function loadSelectedBomItems() {
                if (selectedItem != null || selectedItem != undefined) {
                    ItemBomService.getItemBom(selectedItem.parent.id).then(
                        function (data) {
                            angular.forEach(data, function (bom) {
                                vm.selectedBomChildren.push(bom.item);
                            })
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            vm.itemBoms = [];
            function loadItemBoms() {
                if (revisionId != null && revisionId != undefined) {
                    ItemBomService.getItemBom(revisionId).then(
                        function (data) {
                            angular.forEach(data, function (bom) {
                                vm.itemBoms.push(bom.item);
                            })

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            vm.itemsFilters = {
                itemType: '',
                itemNumber: null,
                itemName: null,
                item: '',
                bomItem: '',
                eco: ''
            };

            function loadFilteredItems() {
                ItemService.getFilteredItems(vm.pageable, vm.itemsFilters).then(
                    function (data) {
                        vm.items = data;
                        ItemService.getLatestRevisionReferences(vm.items.content, 'latestRevision');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.searchFilterItem = searchFilterItem;
            function searchFilterItem() {
                vm.pageable.page = 0;
                loadFilteredItems();
                vm.clear = true;
                if ((vm.itemsFilters.itemName == "" || vm.itemsFilters.itemName == null) && (vm.itemsFilters.itemNumber == "" || vm.itemsFilters.itemNumber == null)) {
                    vm.clear = false;
                }
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    if ($scope.data.mode == "ecos") {
                        vm.itemsFilters.eco = ecoId.change;
                        loadFilteredItems();
                    } else if ($scope.data.mode = "items") {
                        //loadItem();
                        vm.itemsFilters.item = revisionId;
                        if ($rootScope.selectedMasterItemId != null) {
                            vm.itemsFilters.item = $rootScope.selectedMasterItemId;
                        }
                        if (selectedItem != null && selectedItem.id != null) {
                            vm.itemsFilters.bomItem = selectedItem.id;
                        }
                        loadFilteredItems();
                    }
                    /*loadItemBoms();
                     loadSelectedBomItems();*/
                    $rootScope.$on("add.select.item", onOk);
                //}
            })();
        }
    }
)
;
