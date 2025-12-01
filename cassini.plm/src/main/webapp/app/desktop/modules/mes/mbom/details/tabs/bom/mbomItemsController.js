define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/itemBomService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService'
    ],
    function (module) {
        module.controller('MBOMItemsController', MBOMItemsController);

        function MBOMItemsController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application, MBOMService, ItemBomService, DialogService, AutonumberService) {
            var vm = this;
            vm.loading = true;
            vm.mbomId = $stateParams.mbomId;
            vm.bomItems = [];
            vm.mBomItems = [];

            var parsed = angular.element("<div></div>");
            var removeItemTitle = parsed.html($translate.instant("REMOVE_ITEM_DIALOG")).html();
            var removeItemDialogMsg = parsed.html($translate.instant("REMOVE_ITEM_DIALOG_MESSAGE")).html();
            var itemRemovedMsg = parsed.html($translate.instant("REMOVE_ITEM_SUCCESS_MSG")).html();
            var itemCreatedMsg = parsed.html($translate.instant("ITEM_CREATED_SUCCESSFULLY")).html();
            var itemUpdatedMsg = parsed.html($translate.instant("ITEM_UPDATE_MESSAGE")).html();
            vm.dragAndDropItems = parsed.html($translate.instant("DRAG_DROP_EBOM_ITEM")).html();
            var pleaseEnterName = parsed.html($translate.instant("PLEASE_ENTER_NAME")).html();
            var pleaseEnterQuantityForItem = parsed.html($translate.instant("PLEASE_ENTER_QUANTITY_FOR_ITEM")).html();
            var pleaseEnterPositiveQuantity = parsed.html($translate.instant("PLEASE_ENTER_POSITIVE_QUANTITY")).html();

            vm.createMBomItem = createMBomItem;
            var emptyRow = {
                id: null,
                mbomRevision: vm.mbomId,
                bomItem: null,
                quantity: null,
                type: "NORMAL",
                parent: null,
                objectType: "MBOMITEM"
            }

            function loadMBOMItems() {
                MBOMService.getMBOMItems(vm.mbomId, true, false).then(
                    function (data) {
                        vm.mBomItems = [];
                        angular.forEach(data, function (item) {
                            item.parentBom = null;
                            item.editMode = false;
                            item.expanded = false;
                            item.level = 0;
                            item.isRoot = true;
                            item.count = 0;
                            item.bomChildren = [];
                            item.isNew = false;
                            vm.mBomItems.push(item);
                            var index = vm.mBomItems.indexOf(item);
                            index = populateMBOMItemChildren(item, index);
                            vm.loading = false;
                        });
                    }
                )
            }

            function populateMBOMItemChildren(mBomItem, lastIndex) {
                angular.forEach(mBomItem.children, function (item) {
                    lastIndex++;
                    item.parentBom = mBomItem;
                    item.expanded = true;
                    item.editMode = false;
                    item.level = mBomItem.level + 1;
                    item.count = 0;
                    item.bomChildren = [];
                    item.isNew = false;
                    vm.mBomItems.splice(lastIndex, 0, item);
                    mBomItem.count = mBomItem.count + 1;
                    mBomItem.expanded = true;
                    mBomItem.bomChildren.push(item);
                    lastIndex = populateMBOMItemChildren(item, lastIndex)
                });

                return lastIndex;
            }

            vm.selectedBomRule = "bom.as.released";
            function loadItemBom() {
                MBOMService.getReleasedBom(vm.mbomId, $rootScope.mbomRevision.itemRevision, true).then(
                    function (data) {
                        vm.bomItems = [];
                        angular.forEach(data, function (item) {
                            item.editMode = false;
                            item.expanded = true;
                            item.level = 0;
                            item.isRoot = true;
                            item.count = 0;
                            item.bomChildren = [];
                            item.selected = false;
                            vm.bomItems.push(item);
                            var index = vm.bomItems.indexOf(item);
                            index = populateChildren(item, index);
                            vm.loading = false;
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function populateChildren(bomItem, lastIndex) {
                angular.forEach(bomItem.children, function (item) {
                    lastIndex++;
                    item.expanded = true;
                    item.editMode = false;
                    item.level = bomItem.level + 1;
                    item.count = 0;
                    item.bomChildren = [];
                    item.selected = false;
                    vm.bomItems.splice(lastIndex, 0, item);
                    bomItem.count = bomItem.count + 1;
                    bomItem.bomChildren.push(item);
                    lastIndex = populateChildren(item, lastIndex)
                });

                return lastIndex;
            }

            vm.toggleItemNode = toggleItemNode;
            function toggleItemNode(bomItem) {
                if (bomItem.expanded == null || bomItem.expanded == undefined) {
                    bomItem.expanded = false;
                }
                bomItem.expanded = !bomItem.expanded;
                var index = vm.bomItems.indexOf(bomItem);
                if (bomItem.expanded == false) {
                    removeItemChildren(bomItem);
                }
                else {
                    if (bomItem.hasBom) {
                        MBOMService.getReleasedBom(vm.mbomId, bomItem.latestRevision, false).then(
                            function (data) {
                                angular.forEach(data, function (item) {
                                    item.isNew = false;
                                    item.expanded = false;
                                    item.editMode = false;
                                    item.level = bomItem.level + 1;
                                    item.bomChildren = [];
                                    bomItem.bomChildren.push(item);
                                });

                                angular.forEach(bomItem.bomChildren, function (item) {
                                    index = index + 1;
                                    vm.bomItems.splice(index, 0, item);
                                });
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function removeItemChildren(bomItem) {
                if (bomItem != null && bomItem.bomChildren != null && bomItem.bomChildren != undefined) {
                    angular.forEach(bomItem.bomChildren, function (item) {
                        removeItemChildren(item);
                    });

                    var index = vm.bomItems.indexOf(bomItem);
                    vm.bomItems.splice(index + 1, bomItem.bomChildren.length);
                    bomItem.bomChildren = [];
                    bomItem.expanded = false;

                }
            }


            function createMBomItem(bomItemRow, mBomItemRow, type, objectType) {
                if (objectType == "BOMITEM") {
                    if (type == "single") {
                        var newRow = angular.copy(emptyRow);
                        newRow.bomItem = bomItemRow.id;
                        newRow.quantity = bomItemRow.quantity - bomItemRow.consumedQty;
                        newRow.itemName = bomItemRow.itemName;
                        newRow.itemNumber = bomItemRow.itemNumber;
                        newRow.itemTypeName = bomItemRow.itemTypeName;
                        newRow.revision = bomItemRow.revision;
                        newRow.isNew = false;
                        newRow.editMode = false;
                        newRow.level = 0;
                        newRow.bomChildren = [];
                        if (mBomItemRow != null) {
                            newRow.parent = mBomItemRow.id;
                            newRow.level = mBomItemRow.level + 1;
                            mBomItemRow.hasBom = true;
                        }
                        $rootScope.showBusyIndicator();
                        MBOMService.createMBOMItem(vm.mbomId, newRow).then(
                            function (data) {
                                newRow.id = data.id;
                                newRow.hasBom = bomItemRow.hasBom;
                                bomItemRow.consumedQty = bomItemRow.consumedQty + newRow.quantity;
                                if (mBomItemRow != null) {
                                    newRow.parent = mBomItemRow.id;
                                    mBomItemRow.hasBom = true;
                                    if (mBomItemRow.expanded) {
                                        newRow.mfrPartName = data.mfrPartName;
                                        newRow.mfrPartNumber = data.mfrPartNumber;
                                        newRow.mfrPartId = data.mfrPartId;
                                        newRow.mfrId = data.mfrId;
                                        newRow.makeOrBuy = data.makeOrBuy;
                                        mBomItemRow.bomChildren.push(newRow);
                                        vm.mBomItems.splice(vm.mBomItems.indexOf(mBomItemRow) + mBomItemRow.bomChildren.length, 0, newRow);
                                    } else {
                                        toggleMBOMItemNode(mBomItemRow);
                                    }
                                } else {
                                    newRow.mfrPartName = data.mfrPartName;
                                    newRow.mfrPartNumber = data.mfrPartNumber;
                                    newRow.mfrPartId = data.mfrPartId;
                                    newRow.mfrId = data.mfrId;
                                    newRow.makeOrBuy = data.makeOrBuy;
                                    vm.mBomItems.push(newRow);
                                    if (newRow.hasBom) {
                                        toggleMBOMItemNode(newRow);
                                    }
                                }
                                loadItemBom();
                                $rootScope.showSuccessMessage("Item saved successfully");
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else {
                        var bomItems = [];
                        angular.forEach(bomItemRow, function (item) {
                            var newRow = angular.copy(emptyRow);
                            newRow.bomItem = item.id;
                            newRow.quantity = item.quantity - item.consumedQty;
                            newRow.itemName = item.itemName;
                            newRow.itemNumber = item.itemNumber;
                            newRow.itemTypeName = item.itemTypeName;
                            newRow.revision = item.revision;
                            newRow.isNew = false;
                            newRow.editMode = false;
                            newRow.level = 0;
                            newRow.bomChildren = [];
                            if (mBomItemRow != null) {
                                newRow.parent = mBomItemRow.id;
                                newRow.level = mBomItemRow.level + 1;
                                mBomItemRow.hasBom = true;
                            }
                            bomItems.push(newRow);
                        });
                        $rootScope.showBusyIndicator();
                        MBOMService.createMultipleMBOMItems(vm.mbomId, bomItems).then(
                            function (data) {
                                vm.selectedItemsToDrag = [];
                                loadMBOMItems();
                                loadItemBom();
                                $rootScope.showSuccessMessage("Item saved successfully");
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                } else {
                    if (mBomItemRow != null) {
                        bomItemRow.parent = mBomItemRow.id;
                    } else {
                        bomItemRow.parent = null;
                    }
                    var parentBom = bomItemRow.parentBom;
                    bomItemRow.parentBom = null;
                    MBOMService.updateMBOMItem(vm.mbomId, bomItemRow).then(
                        function (data) {
                            loadItemBom();
                            removeMBOMItemChildren(bomItemRow);
                            vm.mBomItems.splice(vm.mBomItems.indexOf(bomItemRow), 1);
                            if (parentBom != null && parentBom.bomChildren != undefined && parentBom.bomChildren.length > 0) {
                                parentBom.bomChildren.splice(parentBom.bomChildren.indexOf(bomItemRow), 1);
                                if (parentBom.bomChildren.length == 0) {
                                    parentBom.hasBom = false;
                                }
                            }
                            if (mBomItemRow != null) {
                                bomItemRow.parentBom = mBomItemRow;
                                if (mBomItemRow.expanded) {
                                    bomItemRow.level = mBomItemRow.level + 1;
                                    mBomItemRow.bomChildren.push(bomItemRow);
                                    vm.mBomItems.splice(vm.mBomItems.indexOf(mBomItemRow) + mBomItemRow.bomChildren.length, 0, bomItemRow);
                                } else {
                                    toggleMBOMItemNode(mBomItemRow);
                                }
                            } else {
                                bomItemRow.level = 0;
                                vm.mBomItems.push(bomItemRow);
                            }
                            $rootScope.showSuccessMessage("Item updated successfully");
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.toggleMBOMItemNode = toggleMBOMItemNode;
            function toggleMBOMItemNode(mBomItem) {
                if (mBomItem.expanded == null || mBomItem.expanded == undefined) {
                    mBomItem.expanded = false;
                }
                mBomItem.expanded = !mBomItem.expanded;
                var index = vm.mBomItems.indexOf(mBomItem);
                if (mBomItem.expanded == false) {
                    removeMBOMItemChildren(mBomItem);
                }
                else {
                    MBOMService.getMBOMItemChildren(vm.mbomId, mBomItem.id).then(
                        function (data) {
                            if (data.length > 0) {
                                mBomItem.hasBom = true;
                            }
                            angular.forEach(data, function (item) {
                                item.parentBom = mBomItem;
                                item.isNew = false;
                                item.expanded = false;
                                item.editMode = false;
                                item.level = mBomItem.level + 1;
                                item.bomChildren = [];
                                mBomItem.bomChildren.push(item);
                            });

                            angular.forEach(mBomItem.bomChildren, function (item) {
                                index = index + 1;
                                vm.mBomItems.splice(index, 0, item);
                            });
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function removeMBOMItemChildren(mBomItem) {
                if (mBomItem != null && mBomItem.bomChildren != null && mBomItem.bomChildren != undefined) {
                    angular.forEach(mBomItem.bomChildren, function (item) {
                        removeMBOMItemChildren(item);
                    });

                    var index = vm.mBomItems.indexOf(mBomItem);
                    vm.mBomItems.splice(index + 1, mBomItem.bomChildren.length);
                    mBomItem.bomChildren = [];
                    mBomItem.expanded = false;

                }
            }

            vm.addPhantomItem = addPhantomItem;
            function addPhantomItem(mbomItem) {
                AutonumberService.getNextNumberByName(vm.phantomNumberSource.name).then(
                    function (data) {
                        var newRow = angular.copy(emptyRow);
                        newRow.phantomNumber = data;
                        newRow.type = "PHANTOM";
                        newRow.quantity = 1;
                        newRow.editMode = true;
                        newRow.isNew = true;
                        newRow.level = 0;
                        newRow.bomChildren = [];
                        newRow.asReleasedRevision = null;
                        newRow.itemRevisionHasBom = false;
                        if (mbomItem != null) {
                            newRow.parent = mbomItem.id;
                            newRow.parentBom = mbomItem;
                            newRow.level = mbomItem.level + 1;
                            vm.mBomItems.splice(vm.mBomItems.indexOf(mbomItem) + 1, 0, newRow);
                        } else {
                            vm.mBomItems.push(newRow);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.editMBOMItem = editMBOMItem;
            vm.deleteMBOMItem = deleteMBOMItem;
            function editMBOMItem(mbomItem) {
                mbomItem.editMode = true;
                mbomItem.phantomName = mbomItem.phantomName;
                mbomItem.oldQty = mbomItem.quantity;
            }

            function deleteMBOMItem(mbomItem) {
                var options = {
                    title: removeItemTitle,
                    message: removeItemDialogMsg,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        var promise = null;
                        $rootScope.showBusyIndicator($('.view-container'));
                        MBOMService.deleteMBOMItem(vm.mbomId, mbomItem.id).then(
                            function (data) {
                                loadItemBom();
                                removeMBOMItemChildren(mbomItem);
                                if(mbomItem.parentBom != null && mbomItem.parentBom != undefined){
                                    mbomItem.parentBom.bomChildren.splice(mbomItem.parentBom.bomChildren.indexOf(mbomItem), 1);
                                    if(mbomItem.parentBom.bomChildren.length == 0){
                                        mbomItem.parentBom.hasBom = false;
                                        mbomItem.parentBom.expanded = false;
                                    }
                                }
                                vm.mBomItems.splice(vm.mBomItems.indexOf(mbomItem), 1);
                                $rootScope.showSuccessMessage(itemRemovedMsg);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(mbomItem) {
                if (mbomItem.isNew) {
                    vm.mBomItems.splice(vm.mBomItems.indexOf(mbomItem), 1);
                } else {
                    mbomItem.editMode = false;
                    mbomItem.quantity = mbomItem.oldQty;
                }
            }

            vm.save = save;
            function save(mbomItem) {
                if (validate(mbomItem)) {
                    MBOMService.createMBOMItem(vm.mbomId, mbomItem).then(
                        function (data) {
                            mbomItem.id = data.id;
                            mbomItem.phantom = data.phantom;
                            mbomItem.editMode = false;
                            mbomItem.isNew = false;
                            if(mbomItem.parentBom != null && mbomItem.parentBom != undefined){
                            mbomItem.parentBom.hasBom = true;
                            mbomItem.parentBom.expanded = true;
                            mbomItem.parentBom.bomChildren.push(mbomItem);
                            }
                            $rootScope.showSuccessMessage(itemCreatedMsg);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.updateMBOMItem = updateMBOMItem;
            function updateMBOMItem(mbomItem) {
                if (validate(mbomItem)) {
                    var parentBom = mbomItem.parentBom;
                    var bomChildren = mbomItem.bomChildren;
                    var children = mbomItem.children;
                    mbomItem.parentBom = null;
                    mbomItem.bomChildren = [];
                    mbomItem.children = [];
                    MBOMService.updateMBOMItem(vm.mbomId, mbomItem).then(
                        function (data) {
                            loadItemBom();
                            mbomItem.parentBom = parentBom;
                            mbomItem.bomChildren = bomChildren;
                            mbomItem.children = children;
                            mbomItem.editMode = false;
                            $rootScope.showSuccessMessage(itemUpdatedMsg);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            mbomItem.parentBom = parentBom;
                            mbomItem.bomChildren = bomChildren;
                            mbomItem.children = children;
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate(mBomItem) {
                var valid = true;
                if (mBomItem.type == "PHANTOM" && (mBomItem.phantomName == null || mBomItem.phantomName == "" || mBomItem.phantomName == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterName);
                } else if (mBomItem.type == "NORMAL" && (mBomItem.quantity == null || mBomItem.quantity == "" || mBomItem.quantity == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterQuantityForItem.format(mBomItem.itemNumber));
                } else if (mBomItem.type == "NORMAL" && (mBomItem.quantity != null && mBomItem.quantity <= 0)) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterPositiveQuantity.format(mBomItem.itemNumber));
                }
                return valid;
            }

            function loadAutoNumberSource() {
                AutonumberService.getAutonumberName("Default Phantom Number Source").then(
                    function (data) {
                        vm.phantomNumberSource = data;
                        if (vm.phantomNumberSource == null || vm.phantomNumberSource == "") {
                            addAutoNumber();
                        } else {
                            loadItemBom();
                            loadMBOMItems();
                        }
                    }
                )
            }

            function addAutoNumber() {
                var newAutonumber = {
                    id: null,
                    name: "Default Phantom Number Source",
                    description: "Phantom Number Source",
                    numbers: 5,
                    start: 1,
                    increment: 1,
                    nextNumber: 1,
                    padwith: "0",
                    prefix: "PHTM-",
                    suffix: "",
                    newName: "",
                    newDescription: "",
                    newNumber: 5,
                    newStart: 1,
                    newIncrement: 1,
                    newPadwith: "0",
                    newPrefix: "",
                    newSuffix: "",
                    editMode: true,
                    showValues: false
                };
                AutonumberService.createAutonumber(newAutonumber).then(
                    function (data) {
                        vm.phantomNumberSource = data;
                        loadItemBom();
                        loadMBOMItems();
                    }
                )
            }

            $rootScope.validateEBOM = validateEBOM;
            function validateEBOM() {
                MBOMService.getValidateReleasedBom(vm.mbomId, $rootScope.mbomRevision.itemRevision, true).then(
                    function (data) {
                        vm.validatedItems = data;
                        showValidateBom();
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.hideValidateBom = hideValidateBom;
            function hideValidateBom() {
                var modal = document.getElementById("validate-bom");
                if (modal != null && modal != undefined) {
                    modal.style.display = "none";
                }
            }

            vm.showValidateBom = showValidateBom;
            function showValidateBom() {
                var modal = document.getElementById("validate-bom");
                if (modal != null && modal != undefined) {
                    modal.style.display = "block";
                }
            }

            vm.resizeView = resizeView;
            function resizeView() {
                $timeout(function () {
                    var viewContent = $('.view-content').outerHeight();
                    if ($rootScope.mbomRevision != undefined && ($rootScope.mbomRevision.released || $rootScope.mbomRevision.rejected)) {
                        $('.mbom-items-right .responsive-table').height(viewContent - 100);
                    } else {
                        $('.mbom-items-right .responsive-table').height(viewContent - 130);
                    }
                }, 500)
            }

            vm.selectedItemsToDrag = [];
            vm.selectBomItem = selectBomItem;
            function selectBomItem(bomItem) {
                if (bomItem.consumedQty < bomItem.quantity) {
                    bomItem.selected = !bomItem.selected;
                    if (bomItem.selected) {
                        vm.selectedItemsToDrag.push(bomItem);
                    } else {
                        vm.selectedItemsToDrag.splice(vm.selectedItemsToDrag.indexOf(bomItem), 1);
                    }
                }
            }

            (function () {
                $scope.$on('app.mbom.tabActivated', function (event, data) {
                    if (data.tabId == 'details.bom') {
                        $('.tab-content .tab-pane').css("overflow", "hidden");
                        loadAutoNumberSource();
                        resizeView();
                        $(window).resize(function () {
                            resizeView();
                        });
                    }
                });
            })();
        }
    }
)
;

