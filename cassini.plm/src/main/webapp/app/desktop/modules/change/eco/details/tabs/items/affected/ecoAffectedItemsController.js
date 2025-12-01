define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemTypeService',
        'app/desktop/modules/item/details/itemSelectionController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/modules/item/details/itemsSelectionController',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('ECOAffectedItemsController', ECOAffectedItemsController);

        function ECOAffectedItemsController($scope, $rootScope, $timeout, $state, $stateParams, $application, $cookies, $window, DialogService,
                                            ECOService, ItemService, ItemTypeService, $uibModal, $translate) {
            var vm = this;

            vm.loading = true;
            vm.ecoId = $stateParams.ecoId;
            vm.items = [];

            vm.findItem = findItem;
            vm.onOk = onOk;
            vm.save = save;
            vm.saveAll = saveAll;
            vm.onCancel = onCancel;
            vm.editItem = editItem;
            vm.deleteItem = deleteItem;
            vm.affectedItemSelection = affectedItemSelection;
            vm.showItem = showItem;
            vm.copiedItems = $application.clipboard.items;

            var parsed = angular.element("<div></div>");

            vm.affectedItems = [];
            vm.selectedItems = [];
            vm.selectedItemIds = [];
            var sctItemLength = 0;
            var emptyItem = {
                id: null,
                type: 'AFFECTED',
                change: vm.ecoId,
                itemObject: {
                    id: null,
                    itemType: null,
                    itemNumber: null,
                    itemName: null,
                    description: null,
                    revision: null,
                    status: null
                },
                fromRevision: null,
                fromLifeCycle: null,
                toRevision: null,
                toLifeCycle: null,
                notes: null,
                isNew: true,
                editMode: true,
                toLifecycles: []
            };

            vm.flag = false;

            var affectedItemCreatedMessage = parsed.html($translate.instant("AFFECTED_ITEM_CREATE_MESSAGE")).html();
            $scope.deleteThisItem = parsed.html($translate.instant("DELETE_THIS_ITEM")).html();
            $scope.editThisItem = parsed.html($translate.instant("EDIT_THIS_ITEM")).html();
            $scope.saveChanges = parsed.html($translate.instant("SAVE_CHANGES")).html();
            $scope.cancelChanges = parsed.html($translate.instant("CANCEL_CHANGES")).html();
            $scope.redLineTitle = parsed.html($translate.instant("BOM_RED_LINE_TITLE")).html();
            vm.addItems = parsed.html($translate.instant("ADD_ITEM")).html();
            $scope.cannotDeleteItem = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_ITEM")).html();


            function save(item) {
                vm.creating = true;
                item.notes = item.newNotes;
                //item.toLifeCycle = item.newToLifeCycle;
                item.effectiveDate = item.newEffectiveDate;
                if (validate(item)) {
                    ECOService.createAffectedItem(vm.ecoId, item).then(
                        function (data) {
                            item.id = data.id;
                            item.isNew = false;
                            item.editMode = false;
                            item.flag = true;
                            item.revisionId = data.itemObject.latestRevision;
                            sctItemLength--;
                            if (sctItemLength == 0) {
                                loadItems();
                            }
                            vm.selectedItemIds.splice(vm.selectedItemIds.indexOf(item.item), 1);
                            vm.addedAffectedItemsToEco.splice(vm.addedAffectedItemsToEco.indexOf(item), 1);
                            $rootScope.loadECOCounts();
                            $rootScope.showSuccessMessage(affectedItemCreatedMessage)
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }
            }

            function saveAll() {
                vm.creating = true;
                if (validateItems()) {
                    var count = 0;
                    angular.forEach(vm.addedAffectedItemsToEco, function (item) {
                        item.notes = item.newNotes;
                        //item.toLifeCycle = item.newToLifeCycle;
                        item.effectiveDate = item.newEffectiveDate;
                        count++;
                        if (count == vm.addedAffectedItemsToEco.length) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            ECOService.createMultipleAffectedItem(vm.ecoId, vm.addedAffectedItemsToEco).then(
                                function (data) {
                                    loadItems();
                                    vm.selectedItemIds = [];
                                    vm.addedAffectedItemsToEco = [];
                                    $rootScope.loadECOCounts();
                                    $rootScope.showSuccessMessage(affectedItemCreatedMessage)
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                    })
                }
            }

            vm.removeAddedItems = removeAddedItems;
            function removeAddedItems() {
                angular.forEach(vm.addedAffectedItemsToEco, function (item) {
                    vm.selectedItemIds.splice(vm.selectedItemIds.indexOf(item.item), 1);
                    vm.items.splice(vm.items.indexOf(item), 1);
                })
                vm.addedAffectedItemsToEco = [];
            }

            var selectToRevision = parsed.html($translate.instant("SELECT_TO_REVISION")).html();
            var selectLifecyclePhase = parsed.html($translate.instant("SELECT_LIFE_CYCLE_PHASE")).html();
            $scope.effectiveDatePlaceholder = parsed.html($translate.instant("EFFECTIVE_DATE_PLACEHOLDER")).html();

            function validate(item) {
                var valid = true;
                if (item.toRevision == null || item.toRevision == undefined
                    || item.toRevision == "") {
                    $rootScope.showErrorMessage(selectToRevision);
                    valid = false;
                }
                /*else if (item.toLifeCycle == null || item.toLifeCycle == undefined
                 || item.toLifeCycle == "") {
                 $rootScope.showErrorMessage(selectLifecyclePhase);
                 valid = false;
                 }*/
                return valid;
            }

            function validateItems() {
                var valid = true;
                /*angular.forEach(vm.addedAffectedItemsToEco, function (item) {
                 if (valid) {
                 if (item.newToLifeCycle == null || item.newToLifeCycle == undefined
                 || item.newToLifeCycle == "") {
                 $rootScope.showErrorMessage(item.itemNumber + " : " + selectLifecyclePhase);
                 valid = false;
                 }
                 }
                 })*/
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
                    showMask: true,
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
                                            $rootScope.hideBusyIndicator();
                                        }
                                    );
                                    loadLifecyclePhases(item);
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function loadItems() {
                vm.loading = true;
                $rootScope.showBusyIndicator();
                vm.affectedItems = [];
                ECOService.getEcoAffectedItems(vm.ecoId).then(
                    function (data) {
                        vm.items = data;
                        angular.forEach(vm.items, function (item) {
                            item.newNotes = item.notes;
                            //item.newToLifeCycle = item.toLifeCycle;
                            item.isNew = false;
                            item.editMode = false;
                            item.latestToRev = null;
                            item.bomAvailable = false;
                            item.flag = true;
                            item.revisionId = item.revisedItem.id;
                            if (item.toRevision != "A" && item.revisedItem.hasBom == true) {
                                item.bomAvailable = true;
                            }
                            item.itemId = item.itemObject.id;
                            item.itemNumber = item.itemObject.itemNumber;
                            item.itemType = item.itemObject.itemType;
                            item.itemName = item.itemObject.itemName;
                            item.description = item.itemObject.description;
                            if (item.itemObject.latestReleasedRevision != null) {
                                item.latestToRev = item.itemObject.latestReleasedRevision;
                            } else {
                                item.latestToRev = item.itemObject.latestRevision;
                            }
                            item.latestToRev = item.itemObject.latestRevision;
                            vm.affectedItems.push(item.itemObject);

                        });
                        loadEco();
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $rootScope.itemList = [];
            $rootScope.itemFilterList = [];
            vm.compareBom = compareBom;
            vm.compareTitle = null;
            function compareBom(fromRev, toRev) {
                vm.bomCompareFilter = false;
                vm.compareTitle = fromRev.itemName + " (" + fromRev.itemNumber + " Rev -" + fromRev.fromRevision + " To - " + fromRev.toRevision + ")";
                $rootScope.showBusyIndicator();
                ItemService.getComparedIndividualRevisions(fromRev.item, toRev.toItem, true).then(
                    function (data) {
                        $rootScope.itemList = data.itemList;
                        $rootScope.itemFilterList = data.itemList;
                        $rootScope.hideBusyIndicator();
                        $('#myModal1').modal('show');
                        resizeBomRedLine();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.bomCompareFilter = false;

            vm.clearBomCompareFilter = clearBomCompareFilter;
            function clearBomCompareFilter() {
                vm.bomCompareFilter = false;
                vm.filterMessage = null;
                $rootScope.itemList = $rootScope.itemFilterList;
            }


            vm.showAddedItems = showAddedItems;
            function showAddedItems() {
                vm.bomCompareFilter = true;
                $rootScope.itemList = [];
                vm.filterMessage = parsed.html($translate.instant("BOM_COMPARE_FILTER_ADDED_MESSAGE")).html();
                angular.forEach($rootScope.itemFilterList, function (item) {
                    if (item.color != null && item.color != "") {
                        if (item.color == "green_color") {
                            $rootScope.itemList.push(item);
                        }
                    }
                });
            }

            vm.showChangedItems = showChangedItems;
            function showChangedItems() {
                vm.bomCompareFilter = true;
                vm.filterMessage = parsed.html($translate.instant("BOM_COMPARE_FILTER_CHANGED_MESSAGE")).html();
                $rootScope.itemList = [];
                angular.forEach($rootScope.itemFilterList, function (item) {
                    if (item.color != null && item.color != "") {
                        if (item.color == "orange_color") {
                            $rootScope.itemList.push(item);
                        }
                    }
                });
            }


            vm.showDeletedItems = showDeletedItems;
            function showDeletedItems() {
                vm.bomCompareFilter = true;
                $rootScope.itemList = [];
                vm.filterMessage = parsed.html($translate.instant("BOM_COMPARE_FILTER_DELETED_MESSAGE")).html();
                angular.forEach($rootScope.itemFilterList, function (item) {
                    if (item.color != null && item.color != "") {
                        if (item.color == "red_color") {
                            $rootScope.itemList.push(item);
                        }
                    }
                });
            }


            vm.showNoChangeItems = showNoChangeItems;
            function showNoChangeItems() {
                vm.bomCompareFilter = true;
                $rootScope.itemList = [];
                vm.filterMessage = parsed.html($translate.instant("BOM_COMPARE_FILTER_NO_CHANGES_MESSAGE")).html();
                angular.forEach($rootScope.itemFilterList, function (item) {
                    if (item.color != null && item.color != "") {
                        if (item.color == "white_color") {
                            $rootScope.itemList.push(item);
                        }
                    }
                });
            }


            function addItem() {
                var newItem = angular.copy(emptyItem);
                newItem.isNew = true;
                newItem.editMode = true;
                newItem.flag = true;
                newItem.editItemNumber = true;
                vm.items.push(newItem);

            }

            function loadLifecyclePhases(item) {
                item.toLifecyclePhases = [];

                var arr = item.itemType.lifecycle.phases;
                angular.forEach(arr, function (phase) {
                    if (phase.phaseType == 'RELEASED') {
                        item.toLifecyclePhases.push(phase);
                    }
                });

                if (item.toLifecyclePhases.length > 0) {
                    item.toLifeCycle = item.toLifecyclePhases[0];
                }
            }

            var affectedItemUpdateMessage = $translate.instant("AFFECTED_ITEM_UPDATE_MESSAGE");

            function onOk(item) {
                item.notes = item.newNotes;
                //item.toLifeCycle = item.newToLifeCycle;
                item.effectiveDate = item.newEffectiveDate;
                if (item.id != null && item.id != "" && item.id != undefined) {
                    ECOService.updateEcoAffectedItem(vm.ecoId, item).then(
                        function (data) {
                            item.isNew = false;
                            item.editMode = false;
                            item.flag = false;
                            loadEco();
                            loadItems();
                            $rootScope.loadECOCounts();
                            $rootScope.showSuccessMessage(affectedItemUpdateMessage);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    save(item);
                }

            }

            function onCancel(item) {
                if (item.isNew == true) {
                    var index = vm.items.indexOf(item);
                    vm.items.splice(index, 1);
                    vm.selectedItemIds.splice(vm.selectedItemIds.indexOf(item.item), 1);
                    vm.addedAffectedItemsToEco.splice(vm.addedAffectedItemsToEco.indexOf(item), 1);
                }
                else {
                    item.editMode = false;
                    item.flag = true;
                    item.newNotes = item.notes;
                    //item.toLifeCycle.phase = item.newToLifeCycle.phase;
                    //item.newToLifeCycle = item.toLifeCycle;
                    item.effectiveDate = item.newEffectiveDate;
                }
            }

            function editItem(item) {
                item.isNew = false;
                item.editMode = true;
                item.flag = false;
                item.newEffectiveDate = item.effectiveDate;
                loadLifecyclePhases(item);
            }

            var deleteAffectedItemDialogTitle = parsed.html($translate.instant("REMOVE_ECO_AFFECTED_ITEM_TITLE")).html();
            var deleteAffectedItemDialogMessage = parsed.html($translate.instant("REMOVE_ECO_AFFECTED_ITEM_MESSAGE")).html();
            var deleteAffectedItemMessage = parsed.html($translate.instant("ECO_AFFECTED_ITEM_DELETE_MESSAGE")).html();
            var itemAddedByECR = parsed.html($translate.instant("ECO_ITEM_ADDED_BY_ECR")).html();

            function deleteItem(item) {
                if (item.ecrList.length == 0) {
                    var options = {
                        title: deleteAffectedItemDialogTitle,
                        message: deleteAffectedItemDialogMessage.format(item.itemName),
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            ECOService.deleteEcoAffectedItem(vm.ecoId, item.id).then(
                                function (data) {
                                    var index = vm.items.indexOf(item);
                                    vm.items.splice(index, 1);
                                    $rootScope.showSuccessMessage(deleteAffectedItemMessage);
                                    loadItems();
                                    $rootScope.loadECOCounts();
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    });
                } else {
                    $rootScope.showWarningMessage(item.itemName + " : " + itemAddedByECR);
                }

            }

            var multipleItemSelectionTitle = parsed.html($translate.instant("MULTIPLE_ITEM_SELECTION")).html();
            var itemsAlreadyExistsInAffectedItems = parsed.html($translate.instant("ALREADY_EXISTS_IN_AFFECTED_ITEMS")).html();

            vm.addedAffectedItemsToEco = [];
            vm.addMultipleItems = addMultipleItems;
            function addMultipleItems() {
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
                        mode: "ecos",
                        selectedMultipleEco: vm.ecoId,
                        selectedAffectedItemIds: vm.selectedItemIds
                    },

                    callback: function (result) {
                        angular.forEach(result, function (item) {
                            var affectedItem = angular.copy(emptyItem);
                            affectedItem.itemNumber = item.itemNumber;
                            affectedItem.itemType = item.itemType;
                            affectedItem.itemName = item.itemName;
                            if (!item.latestRevisionObject.rejected) {
                                affectedItem.fromRevision = item.latestRevisionObject.revision;
                                affectedItem.fromLifeCycle = item.latestRevisionObject.lifeCyclePhase;
                                affectedItem.item = item.latestRevision;
                            } else if (item.latestReleasedRevisionObject != null && item.latestReleasedRevisionObject != "") {
                                affectedItem.fromRevision = item.latestReleasedRevisionObject.revision;
                                affectedItem.fromLifeCycle = item.latestReleasedRevisionObject.lifeCyclePhase;
                                affectedItem.item = item.latestReleasedRevision;
                            } else {
                                affectedItem.fromRevision = item.rev.revision;
                                affectedItem.fromLifeCycle = item.rev.lifeCyclePhase;
                                affectedItem.item = item.rev.id;
                            }
                            affectedItem.toRevision = item.toRevision;
                            affectedItem.toLifecyclePhases = item.toLifecyclePhases;
                            affectedItem.isNew = true;
                            affectedItem.editMode = true;
                            affectedItem.flag = true;
                            affectedItem.editItemNumber = false;
                            affectedItem.effectiveDate = null;
                            affectedItem.newEffectiveDate = null;
                            /*if (item.toLifecyclePhases.length == 1) {
                             affectedItem.newToLifeCycle = item.toLifecyclePhases[0];
                             }*/
                            vm.items.unshift(affectedItem);
                            vm.selectedItemIds.push(item.latestRevision);
                            vm.addedAffectedItemsToEco.unshift(affectedItem);
                            sctItemLength++;
                        })
                        /*angular.forEach(result, function (item) {
                         var itemNumber = item.itemNumber;
                         var revision = item.latestRevision;
                         item = angular.copy(emptyItem);
                         item.itemObject.itemNumber = itemNumber;
                         item.itemObject.revision = revision;
                         item.isNew = true;
                         item.editMode = true;
                         item.flag = true;
                         item.editItemNumber = false;
                         item.effectiveDate = null;
                         item.newEffectiveDate = null;
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
                         $rootScope.showErrorMessage(numbers + " : " + itemsAlreadyExistsInAffectedItems);
                         }
                         }
                         )
                         if (vm.valid) {
                         sctItemLength++;
                         vm.items.unshift(item);
                         findItem(item);
                         }

                         })*/

                    }
                };
                $rootScope.showSidePanel(options);
            }

            function showItem(item) {
                $window.localStorage.setItem("lastSelectedEcoTab", JSON.stringify(vm.ecoDetailsTabId));
                if (item.toItem != null && item.toItem != "") {
                    $state.go('app.items.details', {itemId: item.toItem})
                } else {
                    $state.go('app.items.details', {itemId: item.item})
                }
            }

            vm.showFromRevision = showFromRevision;
            function showFromRevision(item) {
                $window.localStorage.setItem("lastSelectedEcoTab", JSON.stringify(vm.ecoDetailsTabId));
                $state.go('app.items.details', {itemId: item.item})
            }

            vm.showToRevision = showToRevision;
            function showToRevision(item) {
                $window.localStorage.setItem("lastSelectedEcoTab", JSON.stringify(vm.ecoDetailsTabId));
                $state.go('app.items.details', {itemId: item.toItem})
            }

            vm.showEcr = showEcr;
            function showEcr() {
                $window.localStorage.setItem("lastSelectedEcoTab", JSON.stringify(vm.ecoDetailsTabId));
            }

            function loadEco() {
                vm.eco = $rootScope.eco;
                vm.ecoStatus = vm.eco.released;
                if (vm.eco.statusType == 'REJECTED') {
                    vm.ecoStatus = true;
                }
                if (vm.ecoStatus) {
                    $scope.cannotDeleteItem = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_ITEM")).html();
                } else if (vm.eco.revisionsCreated) {
                    $scope.cannotDeleteItem = parsed.html($translate.instant("CANNOT_DELETE_REVISED_ITEM")).html();
                }
            }

            $scope.getTooltip = getTooltip;
            function getTooltip(item) {
                var msg = "";
                if (item.ecrList.length > 0) {
                    msg = parsed.html($translate.instant("ITEM_ADDED_THROUGH_ECR")).html();
                } else {
                    msg = $scope.cannotDeleteItem;
                }
                return msg;
            }

            function resizeBomRedLine() {
                var modal = document.getElementById("myModal1");
                if (modal != null) {
                    $timeout(function () {
                        var headerHeight = $('.compareHeadreBomRedLine').outerHeight();
                        var bomContentHeight = $('.compareContentBomRedLine').outerHeight();
                        $(".bomRedLineBody").height(bomContentHeight - (headerHeight ) - 20);
                    }, 500)
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
                                //$rootScope.showErrorMessage(numbers + " : " + itemsAlreadyExistsInAffectedItems);
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

            (function () {
                $(window).resize(resizeBomRedLine);
                $scope.$on('app.eco.details.single', function () {
                    addItem();
                });
                $scope.$on('app.eco.details.multiple', function () {
                    addMultipleItems();

                });
                $scope.$on('app.eco.tabactivated', function (event, data) {
                    if (data.tabId == 'details.affecteditems') {
                        vm.ecoDetailsTabId = data.tabId;
                        loadItems();
                        /*loadEco();*/
                    }
                });
            })();
        }
    }
)
;