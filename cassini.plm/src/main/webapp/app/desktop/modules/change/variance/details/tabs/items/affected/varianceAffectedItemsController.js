define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemTypeService',
        'app/desktop/modules/item/details/itemSelectionController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/modules/item/details/itemsSelectionController',
        'app/shared/services/core/varianceService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('VarianceAffectedItemsController', VarianceAffectedItemsController);

        function VarianceAffectedItemsController($scope, $rootScope, $timeout, $state, $stateParams, $application, $cookies, $window, DialogService,
                                                 ECOService, ItemService, VarianceService, ItemTypeService, $uibModal, $translate) {
            var vm = this;

            vm.loading = true;
            vm.varianceId = $stateParams.varianceId;
            vm.items = [];

            vm.findItem = findItem;
            vm.onOk = onOk;
            vm.save = save;
            vm.onCancel = onCancel;
            vm.editItem = editItem;
            vm.deleteItem = deleteItem;
            vm.affectedItemSelection = affectedItemSelection;
            vm.showItem = showItem;
            vm.newItems = false;
            vm.varianceStatus = false;
            vm.copiedItems = $application.clipboard.items;

            var parsed = angular.element("<div></div>");

            vm.affectedItems = [];
            vm.selectedItems = [];
            var sctItemLength = 0;
            var emptyItem = {
                id: null,
                type: 'AFFECTED',
                variance: vm.varianceId,
                itemObject: {
                    itemType: null,
                    itemNumber: null,
                    itemName: null,
                    description: null,
                    status: null
                },
                notes: null,
                isNew: true,
                editMode: true
            };

            vm.flag = false;

            var affectedItemAddedMessage = parsed.html($translate.instant("AFFECTED_ITEM_ADDED_MESSAGE")).html();
            $scope.deleteThisItem = parsed.html($translate.instant("DELETE_THIS_ITEM")).html();
            $scope.editThisItem = parsed.html($translate.instant("EDIT_THIS_ITEM")).html();
            $scope.saveChanges = parsed.html($translate.instant("SAVE_CHANGES")).html();
            $scope.cancelChanges = parsed.html($translate.instant("CANCEL_CHANGES")).html();
            $scope.redLineTitle = parsed.html($translate.instant("BOM_RED_LINE_TITLE")).html();
            vm.addItems = parsed.html($translate.instant("ADD_AFFECTED_ITEMS")).html();
            vm.recurringItem = parsed.html($translate.instant("RECURRING_ITEM")).html();

            function save(item) {
                vm.creating = true;
                item.variance = vm.varianceId;
                if (vm.variance.varianceFor == "ITEMS") {
                    item.item = item.itemId;
                    if (validate(item)) {
                        VarianceService.createVarianceAffectedItem(vm.varianceId, item).then(
                            function (data) {
                                item.id = data.id;
                                item.isNew = false;
                                item.editMode = false;
                                vm.newItems = false;
                                $rootScope.loadVarianceCounts();
                                checkIsRecurring(vm.varianceId);
                                $rootScope.showSuccessMessage(affectedItemAddedMessage);
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    }
                }
                if (vm.variance.varianceFor == "MATERIALS") {
                    if (validate(item)) {
                        item.material = item.itemId;
                        VarianceService.createVarianceAffectedPart(vm.varianceId, item).then(
                            function (data) {
                                item.id = data.id;
                                item.isNew = false;
                                item.editMode = false;
                                vm.newItems = false;
                                $rootScope.loadVarianceCounts();
                                checkIsRecurringParts(vm.varianceId);
                                $rootScope.showSuccessMessage(affectedItemAddedMessage);
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    }
                }
            }

            $scope.effectiveDatePlaceholder = parsed.html($translate.instant("EFFECTIVE_DATE_PLACEHOLDER")).html();

            function validate(item) {
                var valid = true;
                /*if (item.quantity == null || item.quantity == undefined
                 || item.quantity == "") {
                 $rootScope.showErrorMessage(selectToRevision);
                 valid = false;
                 }
                 else if (item.notes == null || item.notes == undefined
                 || item.notes == "") {
                 $rootScope.showErrorMessage(selectLifecyclePhase);
                 valid = false;
                 }
                 else if (item.serialsOrLots == null || item.serialsOrLots == undefined
                 || item.serialsOrLots == "") {
                 $rootScope.showErrorMessage(selectLifecyclePhase);
                 valid = false;
                 }*/
                return valid;
            }

            var selectItem = parsed.html($translate.instant("SELECT_ITEM")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();

            function affectedItemSelection(item) {
                var options = {
                    title: selectItem,
                    template: 'app/desktop/modules/item/details/itemSelectionView.jsp',
                    controller: 'ItemSelectionController as itemSelectionVm',
                    resolve: 'app/desktop/modules/item/details/itemSelectionController',
                    width: 700,
                    buttons: [
                        {text: addButton, broadcast: 'add.select.item'}
                    ],
                    data: {
                        mode: "ecos",
                        selectedEco: item
                    },
                    callback: function (result) {
                        item.itemObject.itemNumber = result.itemNumber;
                        findItem(item);
                    }
                };
                $rootScope.showSidePanel(options);
            }

            var itemNotExist = parsed.html($translate.instant("ITEM_NOT_EXIST")).html();
            var itemAlreadyExist = parsed.html($translate.instant("ITEM_ALREADY_EXIST")).html();

            function findItem(item) {
                if (item.itemObject.itemNumber != null && item.itemObject.itemNumber != undefined) {
                    vm.valid = true;
                    angular.forEach(vm.affectedItems, function (vItem) {
                        if (item.itemObject.itemNumber == vItem.itemNumber) {
                            vm.valid = false;
                            $rootScope.showErrorMessage(item.itemObject.itemNumber + " : " + itemAlreadyExist);
                        }
                    });
                    if (vm.valid == true) {
                        ItemService.findByItemNumber(item.itemObject.itemNumber).then(
                            function (data) {
                                if (data.length == 0) {
                                    $rootScope.showErrorMessage(item.itemObject.itemNumber + ' : ' + itemNotExist);
                                }
                                if (data.length == 1) {
                                    var foundItem = data[0];

                                    item.itemType = foundItem.itemType;
                                    item.itemNumber = foundItem.itemNumber;
                                    item.itemName = foundItem.itemName;

                                    var mapRevToItem = new Hashtable();
                                    ItemService.getItemRevisions(foundItem.id).then(
                                        function (data) {
                                            vm.revisions = data;
                                            angular.forEach(vm.revisions, function (revision) {
                                                mapRevToItem.put(revision.revision, revision);
                                            });

                                            return ItemService.getRevisionId(foundItem.latestRevision);
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    ).then(
                                        function (data) {
                                            item.id = data.id;
                                            item.fromRevision = data.revision;
                                            item.fromLifeCycle = data.lifeCyclePhase;
                                            var revs = item.itemType.revisionSequence.values;
                                            var index = revs.indexOf(data.revision);
                                            if (index != -1) {
                                                for (var i = index + 1; i < revs.length; i++) {
                                                    if (mapRevToItem.get(revs[i]) == null) {
                                                        item.toRevision = revs[i];
                                                        break
                                                    }
                                                }
                                            }
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    );
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                }
            }

            function loadItems() {
                vm.affectedItems = [];
                VarianceService.getVarianceAffectedItems(vm.varianceId).then(
                    function (data) {
                        vm.items = data;
                        angular.forEach(vm.items, function (item) {
                            item.editMode = false;
                            item.isNew = false;
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadParts() {
                vm.affectedItems = [];
                VarianceService.getVarianceAffectedParts(vm.varianceId).then(
                    function (data) {
                        vm.items = data;
                        angular.forEach(vm.items, function (item) {
                            item.editMode = false;
                            item.isNew = false;
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function addItem() {
                var newItem = angular.copy(emptyItem);
                newItem.isNew = true;
                newItem.editMode = true;
                newItem.flag = true;
                newItem.editItemNumber = true;
                vm.items.push(newItem);

            }

            var affectedItemUpdateMessage = $translate.instant("AFFECTED_ITEM_UPDATE_MESSAGE");

            function onOk(item) {
                item.variance = vm.varianceId;
                item.isRecurring = item.recurring;
                if (vm.variance.varianceFor == "ITEMS") {
                    item.item = item.itemId;
                    VarianceService.updateVarianceAffectedItem(vm.varianceId, item).then(
                        function (data) {
                            item.isNew = false;
                            item.editMode = false;
                            item.flag = false;
                            loadVariance();
                            loadItems();
                            $rootScope.loadVarianceCounts();
                            $rootScope.showSuccessMessage(affectedItemUpdateMessage);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
                if (vm.variance.varianceFor == "MATERIALS") {
                    item.material = item.itemId;
                    VarianceService.updateVarianceAffectedPart(vm.varianceId, item).then(
                        function (data) {
                            item.isNew = false;
                            item.editMode = false;
                            item.flag = false;
                            loadVariance();
                            loadParts();
                            $rootScope.loadVarianceCounts();
                            $rootScope.showSuccessMessage(affectedItemUpdateMessage);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function onCancel(item) {
                if (item.isNew == true) {
                    var index = vm.items.indexOf(item);
                    vm.items.splice(index, 1);
                }
                else {
                    item.editMode = false;
                    item.flag = true;
                    item.quantity = $scope.quantity;
                    item.serialsOrLots = $scope.serialsOrLots;
                    item.notes = $scope.notes;
                }
            }

            vm.saveAllItems = saveAllItems;
            function saveAllItems() {
                angular.forEach(vm.selectedItems, function (item) {
                    vm.creating = true;
                    item.variance = vm.varianceId;
                    if (vm.variance.varianceFor == "ITEMS") item.item = item.itemId;
                    if (vm.variance.varianceFor == "MATERIALS") item.material = item.itemId;
                });
                if (vm.variance.varianceFor == "ITEMS") {
                    VarianceService.createAllVarianceAffectedItems(vm.varianceId, vm.selectedItems).then(
                        function (data) {
                            angular.forEach(data, function (val) {
                                val.isNew = false;
                                val.editMode = false;
                            });
                            $rootScope.loadVarianceCounts();
                            checkIsRecurring(vm.varianceId);
                            loadItems();
                            vm.newItems = false;
                            $rootScope.showSuccessMessage(affectedItemAddedMessage);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }
                if (vm.variance.varianceFor == "MATERIALS") {
                    VarianceService.createAllVarianceAffectedParts(vm.varianceId, vm.selectedItems).then(
                        function (data) {
                            angular.forEach(data, function (val) {
                                val.isNew = false;
                                val.editMode = false;
                            });
                            $rootScope.loadVarianceCounts();
                            checkIsRecurringParts(vm.varianceId);
                            loadParts();
                            vm.newItems = false;
                            $rootScope.showSuccessMessage(affectedItemAddedMessage);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }

            }

            vm.removeAllItems = removeAllItems;
            function removeAllItems() {
                vm.newItems = false;
                loadItems();
            }

            function editItem(item) {
                item.isNew = false;
                item.editMode = true;
                item.flag = false;

                $scope.quantity = item.quantity;
                $scope.serialsOrLots = item.serialsOrLots;
                $scope.notes = item.notes;

            }

            vm.checkIsRecurring = checkIsRecurring;
            function checkIsRecurring(varianceId) {
                VarianceService.checkIsRecurring(varianceId).then(function (data) {
                })
            }

            vm.checkIsRecurringParts = checkIsRecurringParts;
            function checkIsRecurringParts(varianceId) {
                VarianceService.checkIsRecurringParts(varianceId).then(function (data) {
                })
            }

            var deleteDeviationAffectedItemDialogTitle = parsed.html($translate.instant("DELETE_DEVIATION_AFFECTED_ITEM_TITLE")).html();
            var deleteWaiverAffectedItemDialogTitle = parsed.html($translate.instant("DELETE_WAIVER_AFFECTED_ITEM_TITLE")).html();
            var deleteDeviationAffectedItemDialogMessage = parsed.html($translate.instant("DELETE_DEVIATION_AFFECTED_ITEM_MESSAGE")).html();
            var deleteWaiverAffectedItemDialogMessage = parsed.html($translate.instant("DELETE_WAIVER_AFFECTED_ITEM_MESSAGE")).html();
            var deleteAffectedItemMessage = parsed.html($translate.instant("VARIANCE_AFFECTED_ITEM_DELETE_MESSAGE")).html();
            vm.saveAffectedItems = parsed.html($translate.instant("SAVE_AFFECTED_ITEMS")).html();
            vm.removeAffedItems = parsed.html($translate.instant("REMOVE_AFFECTED_ITEMS")).html();
            var itemDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            var wfStartedOneAffectedItemSbt = parsed.html($translate.instant("WF_STARTED_ONE_AT_SBT")).html();

            function deleteItem(item) {
                var deleteAffectedItemDialogTitle = null;
                var deleteAffectedItemDialogMessage = null;
                if ($rootScope.varianceType == "Deviation") {
                    deleteAffectedItemDialogTitle = deleteDeviationAffectedItemDialogTitle;
                    deleteAffectedItemDialogMessage = deleteDeviationAffectedItemDialogMessage;
                }
                if ($rootScope.varianceType == "Waiver") {
                    deleteAffectedItemDialogTitle = deleteWaiverAffectedItemDialogTitle;
                    deleteAffectedItemDialogMessage = deleteWaiverAffectedItemDialogMessage;
                }
                if (vm.variance.startWorkflow && $rootScope.affectedItems == 1) {
                    $rootScope.showWarningMessage(wfStartedOneAffectedItemSbt);
                } else {
                    var itemId = item.itemId;
                    if (vm.variance.varianceFor == "ITEMS") {
                        var options = {
                            title: deleteAffectedItemDialogTitle,
                            message: deleteAffectedItemDialogMessage + " [ " + item.itemName + " ] " + "?",
                            okButtonClass: 'btn-danger'
                        };
                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                VarianceService.deleteVarianceAffectedItem(vm.varianceId, item.id).then(
                                    function (data) {
                                        var index = vm.items.indexOf(item);
                                        vm.items.splice(index, 1);
                                        checkIsRecurringAfterDelete(itemId);
                                        $rootScope.showSuccessMessage(deleteAffectedItemMessage);
                                        $rootScope.loadVarianceCounts();
                                    },
                                    function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            }
                        });
                    }
                    if (vm.variance.varianceFor == "MATERIALS") {
                        var options = {
                            title: deleteAffectedItemDialogTitle,
                            message: deleteAffectedItemDialogMessage + " [ " + item.itemName + " ] " + "?",
                            okButtonClass: 'btn-danger'
                        };
                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                VarianceService.deleteVarianceAffectedPart(vm.varianceId, item.id).then(
                                    function (data) {
                                        var index = vm.items.indexOf(item);
                                        vm.items.splice(index, 1);
                                        loadParts();
                                        checkIsRecurringAfterDeleteParts(itemId);
                                        $rootScope.showSuccessMessage(deleteAffectedItemMessage);
                                        $rootScope.loadVarianceCounts();
                                    },
                                    function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            }
                        });
                    }
                }
            }

            function checkIsRecurringAfterDelete(itemId) {
                VarianceService.checkIsRecurringAfterDelete(vm.varianceId, itemId).then(function (data) {
                    loadItems();
                });
            }

            function checkIsRecurringAfterDeleteParts(itemId) {
                VarianceService.checkIsRecurringAfterDeleteParts(vm.varianceId, itemId).then(function (data) {
                    loadParts();
                });
            }

            var multipleItemSelectionTitle = parsed.html($translate.instant("MULTIPLE_ITEM_SELECTION")).html();
            var itemsAlreadyExistsInAffectedItems = parsed.html($translate.instant("ALREADY_EXISTS_IN_AFFECTED_ITEMS")).html();

            vm.addMultipleItems = addMultipleItems;
            function addMultipleItems() {
                vm.selectedItems = [];
                if (vm.variance.varianceFor == 'ITEMS') {
                    var options = {
                        title: multipleItemSelectionTitle,
                        template: 'app/desktop/modules/item/details/itemsSelectionView.jsp',
                        controller: 'ItemsSelectionController as itemsSelectionVm',
                        resolve: 'app/desktop/modules/item/details/itemsSelectionController',
                        width: 700,
                        showMask: true,
                        buttons: [
                            {text: addButton, broadcast: 'add.select.items'}
                        ],
                        data: {
                            mode: "variances",
                            selectedMultipleVariance: vm.variance
                        },

                        callback: function (result) {
                            vm.selectedItems = [];
                            vm.newItems = true;
                            angular.forEach(result, function (item) {
                                var newItem = {};
                                newItem.itemNumber = item.itemNumber;
                                newItem.itemName = item.itemName;
                                newItem.itemType = item.itemType.name;
                                newItem.varianceId = item.varianceId;
                                newItem.isNew = true;
                                newItem.quantity = 1;
                                newItem.editMode = true;
                                newItem.flag = true;
                                newItem.itemId = item.latestRevision;
                                newItem.editItemNumber = false;
                                vm.valid = true;
                                vm.selectedItems.push(newItem);
                                vm.items.push(newItem);
                            })
                        }
                    };
                }
                if (vm.variance.varianceFor == 'MATERIALS') {
                    var options = {
                        title: "Select Mfr Parts",
                        template: 'app/desktop/modules/item/details/selectMfrItemView.jsp',
                        controller: 'SelectMfrItemController as mfrItemVm',
                        resolve: 'app/desktop/modules/item/details/selectMfrItemController',
                        width: 700,
                        showMask: true,
                        data: {
                            selectedVarianceId: vm.varianceId,
                            selectMfrPartsMode: "VARIANCE"
                        },
                        buttons: [
                            {text: addButton, broadcast: 'app.item.mfr.new'}
                        ],
                        callback: function (result) {
                            vm.selectedItems = [];
                            vm.newItems = true;
                            angular.forEach(result, function (item) {
                                var newItem = {};
                                newItem.itemNumber = item.partNumber;
                                newItem.itemName = item.partName;
                                newItem.itemType = item.mfrPartType.name;
                                newItem.varianceId = item.varianceId;
                                newItem.isNew = true;
                                newItem.quantity = 1;
                                newItem.editMode = true;
                                newItem.flag = true;
                                newItem.itemId = item.id;
                                newItem.editItemNumber = false;
                                vm.valid = true;
                                vm.selectedItems.push(newItem);
                                vm.items.push(newItem);
                            })
                        }
                    };
                }
                $rootScope.showSidePanel(options);
            }

            function showPart(item) {
                $window.localStorage.setItem("lastSelectedVarianceTab", JSON.stringify(vm.varinceDetailsTabId));
                $state.go('app.mfr.mfrparts.details', {manufacturePartId: item.itemId})
            }

            function showItem(item) {
                if (vm.variance.varianceFor == 'ITEMS') {
                    $window.localStorage.setItem("lastSelectedVarianceTab", JSON.stringify(vm.varinceDetailsTabId));
                    $state.go('app.items.details', {itemId: item.itemId})
                }
                if (vm.variance.varianceFor == 'MATERIALS') {
                    $window.localStorage.setItem("lastSelectedVarianceTab", JSON.stringify(vm.varinceDetailsTabId));
                    $state.go('app.mfr.mfrparts.details', {manufacturePartId: item.itemId})
                }
            }

            var itemsAddedSuccessfullyAnd = parsed.html($translate.instant("ITEMS_ADDED_SUCCESSFULLY_AND")).html();
            var itemsAddedSuccessfully = parsed.html($translate.instant("ITEMS_ADDED_SUCCESSFULLY")).html();
            var undoSuccessful = parsed.html($translate.instant("UNDO_SUCCESSFUL")).html();
            var itemExists = parsed.html($translate.instant("ITEMS_EXISTS")).html();
            vm.pasteItemsFromClipboard = pasteItemsFromClipboard;
            function pasteItemsFromClipboard() {
                var numbers = "";
                vm.selectedItems = [];
                vm.copiedData = [];
                var copiedItems = 0;
                angular.forEach(vm.copiedItems, function (item) {
                    var itemNumber = item.itemNumber;
                    var revision = item.latestRevision;
                    item = angular.copy(emptyItem);
                    item.itemObject.itemNumber = itemNumber;
                    item.itemObject.revision = revision;
                    item.isNew = true;
                    item.editMode = true;
                    item.flag = true;
                    item.editItemNumber = false;
                    vm.valid = true;
                    angular.forEach(vm.affectedItems, function (affectedItem) {
                            if (affectedItem.latestRevision == item.itemObject.revision) {
                                vm.valid = false;
                                if (vm.selectedItems.length == 0) {
                                    numbers = "";
                                    numbers = numbers + " " + item.itemObject.itemNumber;
                                } else {
                                    numbers = numbers + ", " + item.itemObject.itemNumber;
                                }
                                vm.selectedItems.push(item);
                            }
                        }
                    )
                    if (vm.valid) {
                        copiedItems++;
                        sctItemLength++;
                        vm.copiedData.push(item);
                        vm.items.unshift(item);
                        findItem(item);
                    }

                })

                var clipboardItemsCount = $application.clipboard.items.length;
                if ((clipboardItemsCount - copiedItems) > 0) {
                    $rootScope.showSuccessMessage("[ " + copiedItems + " ] " + itemsAddedSuccessfullyAnd + " [ " + numbers + " ] : " + itemAlreadyExist, true, "ECO_ITEMS");
                } else {
                    $rootScope.showSuccessMessage("[ " + copiedItems + " ] " + itemsAddedSuccessfully, true, "ECO_ITEMS");
                }
            }

            $rootScope.undoCopiedEcoItems = undoCopiedEcoItems;
            function undoCopiedEcoItems() {
                if (vm.copiedData.length > 0) {
                    $rootScope.closeNotification();
                    $rootScope.showBusyIndicator($(".view-content"));
                    angular.forEach(vm.copiedData, function (item) {
                        vm.items.splice(vm.items.indexOf(item), 1);
                    })
                    $rootScope.showSuccessMessage(undoSuccessful);
                    $rootScope.hideBusyIndicator();
                }
            }

            function loadVariance() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                VarianceService.getVariance(vm.varianceId).then(
                    function (data) {
                        vm.variance = data;
                        if (vm.variance.statusType == 'REJECTED' || vm.variance.statusType == 'RELEASED') {
                            vm.varianceStatus = true;
                        }
                        vm.loading = false;
                        if (vm.variance.varianceFor == 'ITEMS') {
                            loadItems();
                        }
                        if (vm.variance.varianceFor == 'MATERIALS') {
                            loadParts();
                        }
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadVariance();
                $scope.$on('app.variance.details.single', function () {
                    addItem();
                });
                $scope.$on('app.variance.details.multiple', function () {
                    addMultipleItems();
                });
                $scope.$on('app.variance.tabactivated', function (event, data) {
                    if (data.tabId == 'details.affecteditems') {
                        loadVariance();
                        vm.varinceDetailsTabId = data.tabId;
                    }
                });
            })();
        }
    }
)
;