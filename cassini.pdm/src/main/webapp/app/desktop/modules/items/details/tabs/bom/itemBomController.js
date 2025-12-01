define(
    [
        'app/desktop/modules/items/item.module',
        'app/shared/services/itemTypeService',
        'app/shared/services/itemService',
        'app/shared/services/itemBomService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService'

    ],
    function (module) {
        module.controller('ItemBomController', ItemBomController);

        function ItemBomController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                   ItemTypeService, ItemService, ItemBomService, DialogService, $uibModal) {
            var vm = this;


            vm.loading = true;
            vm.itemId = $stateParams.itemId;
            vm.bomItems = [];
            vm.rootItems = [];
            vm.searchItems = [];
            vm.bomRevisions = [];
            vm.bomRules = {
                latest: {
                    key: 'bom.latest',
                    label: 'Latest Revision'
                },
                latestReleased: {
                    key: 'bom.latest.released',
                    label: 'Latest Released Revision'
                },
                asReleased: {
                    key: 'bom.as.released',
                    label: 'As Released Revision'
                }
            };

            vm.selectedBomRule = vm.bomRules.latest.key;

            vm.toggleNode = toggleNode;
            vm.onOk = onOk;
            vm.onCancel = onCancel;
            vm.findItem = findItem;
            vm.editItem = editItem;
            vm.deleteItem = deleteItem;
            vm.itemSelection = itemSelection;
            vm.selectItem = selectItem;
            vm.applyBomRuleForAll = applyBomRuleForAll;
            vm.loadBom = loadBom;


            vm.revisions = [];
            var emptyBomItem = {
                id: null,
                item: null,
                parent: null,
                quantity: null,
                refdes: null,
                notes: null,
                level: 0,
                expanded: false,
                editMode: true,
                isNew: true,
                itemNumber: null,
                editItemNumber: true,
                bomChildren: [],
                disableEdit: false,
                itemRevision: null

            };

            var lastSelectedItem = null;


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

            function selectItem(item) {
                if (lastSelectedItem != null && lastSelectedItem != item) {
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

            function toggleNode(bomItem) {
                if (bomItem.expanded == null || bomItem.expanded == undefined) {
                    bomItem.expanded = false;
                }
                bomItem.expanded = !bomItem.expanded;
                var index = vm.bomItems.indexOf(bomItem);
                if (bomItem.expanded == false) {
                    removeChildren(bomItem);

                }
                else {
                    if (bomItem.itemRevision.hasBom) {
                        ItemBomService.getItemBom(bomItem.itemRevision.id).then(
                            function (data) {
                                angular.forEach(data, function (item) {
                                    item.parentBom = bomItem;
                                    item.isNew = false;
                                    item.editItemNumber = false;
                                    item.expanded = false;
                                    item.editMode = false;
                                    item.level = bomItem.level + 1;
                                    item.bomChildren = [];
                                    item.itemNumber = null;
                                    item.itemRevision = null;

                                    item.disableEdit = (item.parent.lifeCyclePhase.phaseType == 'RELEASED' ||
                                    item.parent.lifeCyclePhase.phaseType == 'OBSOLETE');
                                    bomItem.bomChildren.push(item);
                                });

                                angular.forEach(bomItem.bomChildren, function (item) {
                                    index = index + 1;
                                    vm.bomItems.splice(index, 0, item);
                                });

                                applyBomRule(data);
                            }
                        )
                    }
                }
            }

            function onOk(item) {
                var copyItem = angular.copy(item);
                copyItem.quantity = item.newQuantity;
                copyItem.refdes = item.newRefdes;
                copyItem.notes = item.newNotes;
                var parentBom = copyItem.parentBom;
                copyItem.parentBom = null;

                if (item.id == null) {
                    if (copyItem.newQuantity == null || copyItem.newQuantity == undefined || copyItem.newQuantity == ""
                        || copyItem.newQuantity < 1) {
                        $rootScope.showErrorMessage("Please enter positive number for quantity");
                    } else {
                        ItemBomService.createItemBom(copyItem.parent.id, copyItem).then(
                            function (data) {
                                item.id = data.id;
                                item.refdes = data.refdes;
                                item.notes = data.notes;
                                item.quantity = item.newQuantity;
                                item.isNew = false;
                                item.editItemNumber = false;
                                item.isRoot = true;
                                item.editMode = false;
                                item.selected = false;
                                item.parent.hasBom = true;
                                item.parent.expanded = true;
                                item.expanded = true;
                                item.parentBom = parentBom;
                                item.level = item.parentBom.level + 1;

                                $rootScope.showSuccessMessage("BOM item created successfully..");
                            }
                        )
                    }
                }
                else {
                    ItemBomService.updateBomItem(copyItem.parent.id, copyItem).then(
                        function (data) {
                            item.quantity = data.quantity;
                            item.refdes = data.refdes;
                            item.notes = data.notes;
                            item.editMode = false;
                            $rootScope.showSuccessMessage("BOM item updated successfully..");
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message)
                        }
                    )
                }
            }

            function onCancel(item) {
                if (item.isNew == true) {
                    var index = vm.bomItems.indexOf(item);
                    vm.bomItems.splice(index, 1);
                }
                else if (item.item.isNew == true) {
                    var index = vm.bomItems.indexOf(item);
                    vm.bomItems.splice(index, 1);
                }
                else {
                    item.editMode = false;
                    item.flag = true;
                    item.newQuantity = item.quantity;
                    item.newRefdes = item.refdes;
                    item.newNotes = item.notes;
                }
            }

            function findItem(bomItem) {
                ItemService.findByItemNumber(bomItem.itemNumber).then(
                    function (data) {
                        if (data.length == 1) {
                            var foundItem = data[0];
                            bomItem.item = foundItem;
                            applyBomRule([bomItem]);
                        }
                    }
                )
            }

            function loadItem() {
                vm.loading = true;
                ItemService.getItem(vm.itemId).then(
                    function (data) {
                        vm.itemRevision.itemMasterObject = data;
                    }
                );
                loadBom();
            }

            function loadBom() {
                ItemBomService.getItemBom(vm.itemId).then(
                    function (data) {
                        angular.forEach(data, function (item) {
                            item.parentBom = null;
                            item.isNew = false;
                            item.editItemNumber = false;
                            item.selected = false;
                            item.editMode = false;
                            item.expanded = false;
                            item.level = 0;
                            item.isRoot = true;

                            item.latestRevisionObject = null;
                            item.latestReleasedRevisionObject = null;
                            item.asReleasedRevisionObject = null;


                            item.disableEdit = (item.parent.lifeCyclePhase.phaseType == 'RELEASED' ||
                            item.parent.lifeCyclePhase.phaseType == 'OBSOLETE');
                        });
                        vm.bomItems = data;
                        vm.rootItems = data;
                        vm.loading = false;

                        var masters = [];
                        angular.forEach(vm.bomItems, function (bomItem) {
                            masters.push(bomItem.item);
                        });

                        ItemService.getItemReferences(masters, 'itemMaster');

                        applyBomRuleForAll();
                    }
                )
            }


            function applyBomRuleForAll() {
                applyBomRule(vm.bomItems);
            }

            function applyBomRule(bomItems) {
                var items = [];
                angular.forEach(bomItems, function (bomItem) {
                    if (vm.selectedBomRule == vm.bomRules.latest.key &&
                        bomItem.item.latestRevision != null) {
                        items.push(bomItem.item)
                    }
                    else if (vm.selectedBomRule == vm.bomRules.latestReleased.key &&
                        bomItem.item.latestRevision != null) {
                        items.push(bomItem.item)
                    }
                    else if (vm.selectedBomRule == vm.bomRules.asReleased.key &&
                        bomItem.asReleasedRevision != null) {
                        items.push(bomItem)
                    }
                });

                if (vm.selectedBomRule == vm.bomRules.latest.key) {
                    ItemService.getRevisionReferences(items, 'latestRevision', function () {
                        updateRevisions(bomItems)
                    });
                }
                else if (vm.selectedBomRule == vm.bomRules.latestReleased.key) {
                    ItemService.getRevisionReferences(items, 'latestReleasedRevision', function () {
                        updateRevisions(bomItems)
                    });
                }
                else if (vm.selectedBomRule == vm.bomRules.asReleased.key) {
                    ItemService.getRevisionReferences(items, 'asReleasedRevision', function () {
                        updateRevisions(bomItems)
                    });
                }
            }

            function updateRevisions(bomItems) {
                angular.forEach(bomItems, function (bomItem) {
                    bomItem.itemRevision = {
                        revision: '?',
                        lifeCyclePhase: {
                            phase: '?'
                        },
                        hasBom: false
                    };
                    if (vm.selectedBomRule == vm.bomRules.latest.key &&
                        bomItem.item.latestRevisionObject != null &&
                        bomItem.item.latestRevisionObject != undefined) {
                        bomItem.itemRevision = bomItem.item.latestRevisionObject;
                    }
                    else if (vm.selectedBomRule == vm.bomRules.latestReleased.key &&
                        bomItem.item.latestReleasedRevisionObject != null &&
                        bomItem.item.latestReleasedRevisionObject != undefined) {
                        bomItem.itemRevision = bomItem.item.latestReleasedRevisionObject;
                    }
                    else if (vm.selectedBomRule == vm.bomRules.asReleased.key &&
                        bomItem.asReleasedRevisionObject != null &&
                        bomItem.asReleasedRevisionObject != undefined) {
                        bomItem.itemRevision = bomItem.asReleasedRevisionObject;
                    }

                    if (bomItem.itemRevision.hasBom == true) {
                        bomItem.bomChildren = [];
                    }
                });
            }

            function editItem(item) {
                ItemService.getItemRevisions(item.item.id).then(
                    function (data) {
                        vm.revisions = data;
                        angular.forEach(vm.revisions, function (revision) {
                            vm.bomRevisions.push(revision.revision);
                        })
                    }
                );
                item.newQuantity = item.quantity;
                item.newRefdes = item.refdes;
                item.newNotes = item.notes;
                item.editMode = true;
                item.flag = false;
                item.item.isNew = false;
            }

            function deleteItem(item) {
                var options = {
                    title: 'Delete BOM Item',
                    message: 'Are you sure you want to delete this BOM item?',
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        var parentId = item.item.id;
                        ItemBomService.deleteBomItem(parentId, item.id).then(
                            function () {
                                if (item.bomChildren != undefined) {
                                    if (item.bomChildren.length > 0) {
                                        angular.forEach(item.bomChildren, function (children) {
                                            var childrenIndex = item.bomChildren.indexOf(children);
                                            vm.bomItems.splice(childrenIndex, 1);
                                        })
                                    }
                                }
                                var index = vm.bomItems.indexOf(item);
                                vm.bomItems.splice(index, 1);
                                lastSelectedItem = null;
                            }
                        )
                    }
                });
            }

            function itemSelection(item) {
                var options = {
                    title: 'Item Selection',
                    template: 'app/desktop/modules/item/details/itemSelectionView.jsp',
                    controller: 'ItemSelectionController as itemSelectionVm',
                    resolve: 'app/desktop/modules/item/details/itemSelectionController',
                    width: 700,
                    data: {
                        itemSelectionItem: item,
                        mode: "items"
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'add.select.item'}
                    ],
                    callback: function (result) {
                        item.itemNumber = result.itemNumber;
                        findItem(item);
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function onAddSingle() {
                $scope.$on('app.item.addbom', function () {
                    var newRow = angular.copy(emptyBomItem);
                    if (lastSelectedItem == null || lastSelectedItem == undefined) {
                        newRow.parent = vm.itemRevision;
                        newRow.parentBom = {
                            level: -1
                        };
                        vm.bomItems.push(newRow);
                    }
                    else if (lastSelectedItem.itemRevision == null ||
                        lastSelectedItem.itemRevision == undefined ||
                        lastSelectedItem.itemRevision.revision == '?') {
                        $rootScope.showErrorMessage("BOM items cannot be added to a unresolved parent item");
                    }
                    else if (lastSelectedItem.itemRevision != null &&
                        lastSelectedItem.itemRevision != undefined &&
                        lastSelectedItem.itemRevision.revision != '?' &&
                        lastSelectedItem.itemRevision.lifeCyclePhase.phaseType == 'RELEASED') {
                        $rootScope.showErrorMessage("BOM items cannot be added to a released parent item");
                    }
                    else {
                        if (lastSelectedItem.expanded == true) {
                            addSingle();
                        }
                        else {
                            ItemBomService.getItemBom(lastSelectedItem.item.id).then(
                                function (data) {
                                    var index = vm.bomItems.indexOf(lastSelectedItem);
                                    angular.forEach(data, function (item) {
                                        item.parentBom = lastSelectedItem;
                                        item.isNew = false;
                                        item.editItemNumber = false;
                                        item.expanded = false;
                                        item.editMode = false;
                                        item.level = lastSelectedItem.level + 1;
                                    });

                                    angular.forEach(lastSelectedItem.bomChildren, function (item) {
                                        index = index + 1;
                                        vm.bomItems.splice(index, 0, item);
                                    });

                                    addSingle();
                                    lastSelectedItem.expanded = true;
                                }
                            )
                        }
                    }
                });
            }

            function addSingle() {
                var newRow = angular.copy(emptyBomItem);
                newRow.parent = lastSelectedItem.itemRevision;
                newRow.parentBom = lastSelectedItem;
                var index = vm.bomItems.indexOf(lastSelectedItem);
                if (lastSelectedItem.bomChildren == undefined) {
                    lastSelectedItem.bomChildren = [];
                }

                index = index + getIndexTopInsertNewChild(lastSelectedItem) + 1;
                lastSelectedItem.bomChildren.push(newRow);

                vm.bomItems.splice(index, 0, newRow);
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

            function onAddMultiple() {
                $scope.$on('app.item.bom.multiple', function () {
                    if (lastSelectedItem != null && (lastSelectedItem.itemRevision == null ||
                        lastSelectedItem.itemRevision == undefined ||
                        lastSelectedItem.itemRevision.revision == '?')) {
                        $rootScope.showErrorMessage("BOM items cannot be added to a unresolved parent item");
                    }
                    else if (lastSelectedItem != null && lastSelectedItem.itemRevision != null &&
                        lastSelectedItem.itemRevision != undefined &&
                        lastSelectedItem.itemRevision.revision != '?' &&
                        lastSelectedItem.itemRevision.lifeCyclePhase.phaseType == 'RELEASED') {
                        $rootScope.showErrorMessage("BOM items cannot be added to a released parent item");
                    }
                    else {
                        var options = {
                            title: 'Multiple Items Selection',
                            template: 'app/desktop/modules/item/details/itemsSelectionView.jsp',
                            controller: 'ItemsSelectionController as itemsSelectionVm',
                            resolve: 'app/desktop/modules/item/details/itemsSelectionController',
                            data: {
                                multipleItemSelection: lastSelectedItem,
                                mode: "items"
                            },
                            width: 700,
                            buttons: [
                                {text: 'Add', broadcast: 'add.select.items'}
                            ],
                            callback: function (result) {
                                var bomItems = [];

                                angular.forEach(result, function (item) {
                                    var bomItem = angular.copy(emptyBomItem);
                                    bomItem.item = item;
                                    bomItem.isNew = false;
                                    bomItem.item.isNew = true;
                                    bomItem.editItemNumber = false;

                                    if (lastSelectedItem != null) {
                                        bomItem.parent = lastSelectedItem.item;
                                        bomItem.parentBom = lastSelectedItem;
                                        bomItem.level = lastSelectedItem.level + 1;
                                    }
                                    else {
                                        bomItem.parent = vm.itemRevision;
                                        bomItem.parentBom = {
                                            level: -1
                                        }
                                    }

                                    if (lastSelectedItem != null) {
                                        bomItem.parent = lastSelectedItem.itemRevision;
                                        bomItem.parentBom = lastSelectedItem;
                                        var index = vm.bomItems.indexOf(lastSelectedItem);
                                        if (lastSelectedItem.bomChildren == undefined) {
                                            lastSelectedItem.bomChildren = [];
                                        }

                                        var index = index + getIndexTopInsertNewChild(lastSelectedItem) + 1;
                                        lastSelectedItem.bomChildren.push(bomItem);

                                        vm.bomItems.splice(index, 0, bomItem);
                                    }
                                    else {
                                        vm.bomItems.push(bomItem);
                                    }

                                    bomItems.push(bomItem);
                                });

                                applyBomRule(bomItems);
                            }
                        };
                        $rootScope.showSidePanel(options);
                    }
                });
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadItem();
                    onAddSingle();
                    onAddMultiple();
                }
            })();
        }
    }
);