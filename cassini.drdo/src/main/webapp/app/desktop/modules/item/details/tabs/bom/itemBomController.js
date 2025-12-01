define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/bomService'
    ],
    function (module) {
        module.controller('ItemBomController', ItemBomController);

        function ItemBomController($scope, $rootScope, $timeout, $state, $stateParams, $uibModal, $cookies, $window, $translate,
                                   CommonService, ItemService, DialogService, BomService) {

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.addNewBomItem = addNewBomItem;
            $rootScope.addedNewItems = [];

            vm.selectItem = selectItem;

            vm.loading = true;
            vm.bomItems = [];

            vm.emptyBomItem = {
                id: null,
                bom: null,
                item: null,
                quantity: 0,
                fractionalQuantity: 0.0
            };

            var lastSelectedItem = null;

            function selectItem(item) {
                if (lastSelectedItem != null && lastSelectedItem.id != item.id) {
                    lastSelectedItem.selected = false;
                    lastSelectedItem = null;
                }
                item.selected = !item.selected;
                if (item.selected == true) {
                    lastSelectedItem = item;
                    if (item.bomChildren == 0) {
                        vm.bomItems.itemSelect = [];
                    }
                }
                else {
                    item.selected = false;
                    lastSelectedItem = null;
                }

            }

            function addNewBomItem(bomItem) {
                lastSelectedItem = bomItem;
                if (!bomItem.expanded) {
                    toggleNode(bomItem);
                }
                $rootScope.newItemBomButton = false;
                $rootScope.addedNewItems = [];
                var button = {text: "Add", broadcast: 'app.bom.new'};
                var options = {
                    title: "Add Item to : " + bomItem.typeRef.name,
                    template: 'app/desktop/modules/item/details/tabs/bom/newBomItemView.jsp',
                    controller: 'NewBomItemController as newBomItemVm',
                    resolve: 'app/desktop/modules/item/details/tabs/bom/newBomItemController',
                    width: 600,
                    showMask: true,
                    data: {
                        selectedBomItem: bomItem,
                        buttonTitle: button
                    },
                    buttons: [
                        button
                    ],
                    callback: function (item) {
                        if (item != null && item != undefined) {
                            item.editMode = false;
                            item.isNew = false;
                            var index = vm.bomItems.indexOf(bomItem);
                            if (bomItem.item.hasBom && !bomItem.expanded) {
                                toggleNode(bomItem);
                            } else {
                                bomItem.expanded = true;
                                if (bomItem.bomChildren == undefined) {
                                    bomItem.bomChildren = [];
                                }
                                item.level = bomItem.level + 1;
                                index = index + getIndexTopInsertNewChild(bomItem) + 1;
                                bomItem.bomChildren.push(item);

                                vm.bomItems.splice(index, 0, item);
                            }
                        } else {
                            $rootScope.newItemBomButton = false;
                        }
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.addItemToBom = addItemToBom;
            function addItemToBom(item) {
                lastSelectedItem = item;
                $rootScope.newItemBomButton = false;
                $rootScope.addedNewItems = [];
                var button = {text: "Add", broadcast: 'app.bom.new'};
                var options = {
                    title: "Add Item to : " + item.itemMaster.itemName,
                    template: 'app/desktop/modules/item/details/tabs/bom/newBomItemView.jsp',
                    controller: 'NewBomItemController as newBomItemVm',
                    resolve: 'app/desktop/modules/item/details/tabs/bom/newBomItemController',
                    width: 600,
                    showMask: true,
                    data: {
                        selectedBomItem: item,
                        buttonTitle: button
                    },
                    buttons: [
                        button
                    ],
                    callback: function (item) {
                        if (item != null && item != undefined) {
                            item.editMode = false;
                            item.isNew = false;
                            item.level = 1;
                            item.bomChildren = [];
                            vm.bomItems.push(item);
                        } else {
                            $rootScope.newItemBomButton = false;
                        }
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getIndexTopInsertNewChild(bomItem) {
                var index = 0;

                if (bomItem.bomChildren != undefined && bomItem.bomChildren != null) {
                    index = bomItem.bomChildren.length;
                    angular.forEach(bomItem.bomChildren, function (child) {
                        var childCount = getIndexTopInsertNewChild(child);
                        index = index + childCount;
                    })
                }

                return index;
            }

            $rootScope.addSearchItemToItemBom = addSearchItemToItemBom;

            function addSearchItemToItemBom(item) {
                var newBomItem = angular.copy(vm.emptyBomItem);
                if (lastSelectedItem.bomItemType == 'SECTION' || lastSelectedItem.bomItemType == 'COMMONPART') {
                    newBomItem.hierarchicalCode = lastSelectedItem.hierarchicalCode + "" + lastSelectedItem.typeRef.code + "" + "000";
                } else if (lastSelectedItem.bomItemType == 'SUBSYSTEM') {
                    newBomItem.hierarchicalCode = lastSelectedItem.hierarchicalCode + "" + lastSelectedItem.typeRef.code + "" + "00";
                } else {
                    newBomItem.hierarchicalCode = lastSelectedItem.hierarchicalCode + "" + lastSelectedItem.typeRef.code;
                }
                newBomItem.parent = lastSelectedItem.id;
                newBomItem.item = item.latestRevisionObject;
                newBomItem.editMode = true;
                newBomItem.isNew = true;
                if (lastSelectedItem.objectType == "ITEMREVISION") {
                    newBomItem.bomItemType = "PART";
                    newBomItem.bomChildren = [];
                    vm.bomItems.push(newBomItem);
                } else {
                    newBomItem.bomItemType = "PART";
                    var index = vm.bomItems.indexOf(lastSelectedItem);
                    lastSelectedItem.expanded = true;
                    if (lastSelectedItem.bomChildren == undefined) {
                        lastSelectedItem.bomChildren = [];
                    }
                    if (lastSelectedItem.children == undefined) {
                        lastSelectedItem.children = [];
                    }
                    lastSelectedItem.children.push(newBomItem);
                    newBomItem.level = lastSelectedItem.level + 1;
                    newBomItem.bomChildren = [];
                    index = index + getIndexTopInsertNewChild(lastSelectedItem) + 1;
                    lastSelectedItem.bomChildren.push(newBomItem);

                    vm.bomItems.splice(index, 0, newBomItem);
                }
            }

            vm.editItem = editItem;

            function editItem(item) {
                item.editMode = true;
                item.newQuantity = item.quantity;
            }

            vm.saveItem = saveItem;

            function saveItem(item) {
                if (item.id == null || item.id == undefined) {
                    if (validateItem(item)) {
                        if (item.item != null && item.item.itemMaster.itemType.parentNodeItemType != "Part") {
                            item.quantity = null;
                            item.fractionalQuantity = null;
                        }
                        BomService.createBomItem(lastSelectedItem.id, item).then(
                            function (data) {
                                item.id = data.id;
                                item.editMode = false;
                                item.isNew = false;
                                item.objectType = data.objectType;
                                $rootScope.showSuccessMessage("Bom Item created successfully");
                            }
                        )
                    }
                } else {
                    if (validateItem(item)) {
                        BomService.updateBomItem(item).then(
                            function (data) {
                                item.quantity = data.quantity;
                                item.editMode = false;
                                item.isNew = false;
                                $rootScope.showSuccessMessage("Bom Item updated successfully");
                            }
                        )
                    }
                }

            }

            function validateItem(item) {
                var valid = true;
                if (item.item != null && item.item.itemMaster.itemType.parentNodeItemType == 'Part' && !item.item.itemMaster.itemType.hasLots && (item.quantity == null || item.quantity == "" || item.quantity == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Quantity");
                } else if (item.item != null && item.item.itemMaster.itemType.parentNodeItemType == 'Part' && item.item.itemMaster.itemType.hasLots && (item.fractionalQuantity == null || item.fractionalQuantity == "" || item.fractionalQuantity == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Quantity");
                }

                return valid;
            }

            vm.cancelChanges = cancelChanges;
            vm.removeItem = removeItem;

            function cancelChanges(item) {
                item.editMode = false;
                item.quantity = item.newQuantity;
            }

            function removeItem(item) {
                var index = vm.bomItems.indexOf(item);
                var index1 = $rootScope.addedNewItems.indexOf(item);
                $rootScope.addedNewItems.splice(index1, item);
                vm.bomItems.splice(index, 1);
            }

            vm.deleteItem = deleteItem;
            function deleteItem(item) {
                var options = {
                    title: "Delete Item",
                    message: "Please confirm to delete this Item",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        BomService.deleteBomItem(item).then(
                            function (data) {
                                var index = vm.bomItems.indexOf(item);
                                vm.bomItems.splice(index, 1);
                                $rootScope.showWarningMessage("Bom Item deleted successfully");
                            }
                        )
                    }
                });
            }

            function loadBom() {
                lastSelectedItem = null;
                BomService.getItemBom($stateParams.itemId).then(
                    function (data) {
                        vm.bomItems = data;
                        angular.forEach(vm.bomItems, function (item) {
                            item.editMode = false;
                            item.isNew = false;
                            item.expanded = false;
                            item.level = 1;
                            item.bomChildren = [];
                        });
                        vm.loading = false;
                        $rootScope.viewInfo.title = $rootScope.selectedItemRevisionDetails.itemMaster.itemName
                            + " - " + $rootScope.selectedTab.heading;
                    }
                )
            }

            vm.toggleNode = toggleNode;
            function toggleNode(bomItem) {
                if (bomItem.expanded == null || bomItem.expanded == undefined) {
                    bomItem.expanded = false;
                }
                bomItem.expanded = !bomItem.expanded;
                var index = vm.bomItems.indexOf(bomItem);
                if (bomItem.objectType == 'BOMITEM') {
                    if (bomItem.expanded == false) {
                        removeChildren(bomItem);
                    }
                    else {
                        BomService.getBomItemChildren(bomItem.id).then(
                            function (data) {
                                angular.forEach(data, function (item) {
                                    item.isNew = false;
                                    item.editMode = false;
                                    item.expanded = false;
                                    item.level = bomItem.level + 1;
                                    item.bomChildren = [];
                                    bomItem.bomChildren.push(item);
                                });

                                angular.forEach(bomItem.bomChildren, function (item) {
                                    index = index + 1;
                                    vm.bomItems.splice(index, 0, item);
                                });
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

                    var index = vm.bomItems.indexOf(bomItem);
                    vm.bomItems.splice(index + 1, bomItem.bomChildren.length);
                    bomItem.bomChildren = [];
                    bomItem.expanded = false;
                }
            }

            vm.createBomGroupType = createBomGroupType;

            function createBomGroupType(item, type) {
                $rootScope.addedSectionTypes = [];
                lastSelectedItem = item;
                if (!item.expanded) {
                    toggleNode(item);
                }
                var title = null;
                if (type == "SECTION") {
                    title = "Add Item to : " + item.typeRef.name;
                } else {
                    title = "Add Item to : " + item.typeRef.name;
                }

                var options = {
                    title: title,
                    template: 'app/desktop/modules/bom/details/add/addBomGroupTypeView.jsp',
                    controller: 'AddBomGroupTypeController as addBomGroupTypeVm',
                    resolve: 'app/desktop/modules/bom/details/add/addBomGroupTypeController',
                    width: 600,
                    showMask: true,
                    data: {
                        selectedBomItem: item,
                        selectedBomItemType: type
                    },
                    buttons: [
                        {text: "Close", broadcast: 'app.type.bomItem.new'}
                    ],
                    callback: function (bomItem) {
                        if (bomItem != null && bomItem != undefined) {

                            item.editMode = false;
                            item.isNew = false;
                            var index = vm.bomItems.indexOf(item);
                            if (!item.expanded) {
                                toggleNode(bomItem);
                            } else {
                                item.expanded = true;
                                if (item.bomChildren == undefined) {
                                    item.bomChildren = [];
                                }
                                item.level = item.level + 1;
                                index = index + getIndexTopInsertNewChild(bomItem) + 1;
                                item.bomChildren.push(item);

                                vm.bomItems.splice(index, 0, item);
                            }
                        }
                    }
                };

                $rootScope.showSidePanel(options);
            }

            $rootScope.addBomGroupTypeToBom = addBomGroupTypeToBom;
            function addBomGroupTypeToBom(type) {
                var newBomItem = angular.copy(vm.emptyBomItem);
                if (type.type == "SECTION") {
                    newBomItem.typeRef = type;
                    newBomItem.bom = vm.selectedBom.id;
                    newBomItem.hierarchicalCode = vm.selectedBom.item.itemMaster.itemCode;
                    newBomItem.bomItemType = 'SECTION';
                } else {
                    newBomItem.typeRef = type;
                    newBomItem.parent = lastSelectedItem.id;
                    if (lastSelectedItem.bomItemType == "SECTION" && type.type == "UNIT") {
                        newBomItem.hierarchicalCode = lastSelectedItem.hierarchicalCode + "" + lastSelectedItem.typeRef.code + "0";
                    } else {
                        newBomItem.hierarchicalCode = lastSelectedItem.hierarchicalCode + "" + lastSelectedItem.typeRef.code;
                    }
                    newBomItem.bomItemType = type.type;
                }


                BomService.createBomItem(lastSelectedItem.id, newBomItem).then(
                    function (data) {
                        newBomItem.id = data.id;
                        newBomItem.editMode = false;
                        newBomItem.isNew = false;
                        newBomItem.objectType = data.objectType;
                        if (lastSelectedItem.objectType == "BOM") {
                            newBomItem.level = 1;
                            newBomItem.bomChildren = [];
                            vm.bomItems.push(newBomItem);
                        } else {
                            lastSelectedItem.expanded = true;
                            var index = vm.bomItems.indexOf(lastSelectedItem);
                            if (lastSelectedItem.bomChildren == undefined) {
                                lastSelectedItem.bomChildren = [];
                            }

                            if (lastSelectedItem.children == undefined) {
                                lastSelectedItem.children = [];
                            }
                            lastSelectedItem.children.push(newBomItem);
                            lastSelectedItem.expanded = true;
                            newBomItem.level = lastSelectedItem.level + 1;
                            newBomItem.bomChildren = [];
                            index = index + getIndexTopInsertNewChild(lastSelectedItem) + 1;
                            lastSelectedItem.bomChildren.push(newBomItem);

                            vm.bomItems.splice(index, 0, newBomItem);
                        }
                        $rootScope.showSuccessMessage("Bom Item created successfully");
                    }
                )
            }

            (function () {
                $scope.$on('app.item.tabActivated', function (event, data) {
                    if (data.tabId == 'details.bom') {
                        loadBom();
                    }
                });
            })();
        }
    }
);