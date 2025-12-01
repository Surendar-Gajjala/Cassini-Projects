define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/dcrService',
        'app/shared/services/core/dcoService',
        'app/shared/services/core/itemService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'

    ],
    function (module) {
        module.controller('DCOAffectedItemsController', DCOAffectedItemsController);

        function DCOAffectedItemsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                            $translate, DCRService, ItemService, DCOService, DialogService, CommentsService) {
            var vm = this;

            vm.dcoId = $stateParams.dcoId;

            var parsed = angular.element("<div></div>");
            var selectItems = parsed.html($translate.instant("MULTIPLE_ITEM_SELECTION")).html();
            var itemsAddedMsg = parsed.html($translate.instant("ITEMS_ADDED_SUCCESSFULLY")).html();
            var itemsUpdateMsg = parsed.html($translate.instant("ITEM_UPDATE_MSG")).html();
            $scope.addAffectedItemTitle = parsed.html($translate.instant("ADD_AFFECTED_ITEMS")).html();
            $scope.cannotDeleteItem = parsed.html($translate.instant("CANNOT_DELETE_REVISED_ITEM")).html();
            var affectedItem = [];
            var emptyItem = {
                id: null,
                dco: vm.dcoId,
                change: vm.dcoId,
                item: null,
                itemObject: null,
                notes: null
            };
            vm.selectedItemIds = [];
            vm.save = save;
            vm.updateItem = updateItem;
            vm.showDcoItems = showDcoItems;
            vm.items = [];
            vm.itemFlag = false;
            function showDcoItems() {
                vm.selectedItems = [];
                var options = {
                    title: selectItems,
                    template: 'app/desktop/modules/item/details/itemsSelectionView.jsp',
                    controller: 'ItemsSelectionController as itemsSelectionVm',
                    resolve: 'app/desktop/modules/item/details/itemsSelectionController',
                    width: 700,
                    showMask: true,
                    buttons: [
                        {text: $rootScope.add, broadcast: 'add.select.items'}
                    ],
                    data: {
                        mode: "dcos",
                        selectedDco: vm.dcoId
                    },
                    callback: function (result) {
                        vm.itemFlag = true;
                        angular.forEach(result, function (item) {
                            var affectedItem = angular.copy(emptyItem);
                            affectedItem.itemNumber = item.itemNumber;
                            affectedItem.itemType = item.itemType.name;
                            affectedItem.itemName = item.itemName;
                            if (item.latestRevisionObject.lifeCyclePhase.phaseType != "CANCELLED") {
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
                                affectedItem.toLifeCycle = item.toLifecyclePhases[0];
                             }*/
                            vm.items.unshift(affectedItem);
                            vm.selectedItemIds.push(item.latestRevision);
                        })
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadLifecyclePhases(item, dcoItem) {
                dcoItem.toLifecyclePhases = [];

                var arr = item.itemType.lifecycle.phases;
                angular.forEach(arr, function (phase) {
                    if (phase.phaseType == 'RELEASED') {
                        dcoItem.toLifecyclePhases.push(phase);
                    }
                });
            }

            function toRevision(item, dcoItem) {
                var mapRevToItem = new Hashtable();
                ItemService.getItemRevisions(item.id).then(
                    function (data) {
                        vm.revisions = data;
                        angular.forEach(vm.revisions, function (revision) {
                            mapRevToItem.put(revision.revision, revision);
                        });

                        return ItemService.getRevisionId(item.latestRevision);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
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
                                    dcoItem.toRevision = revs[i];
                                    break
                                }
                            }
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }


            function save(item) {
                if (validate(item)) {
                    DCOService.createDcoAffectedItem(vm.dcoId, item).then(
                        function (data) {
                            item.id = data.id;
                            item.editMode = false;
                            vm.selectedItemIds.splice(vm.selectedItemIds.indexOf(item.item), 1);
                            $rootScope.showSuccessMessage(itemsAddedMsg);
                            $rootScope.loadDCOCounts();
                        }, function (error) {
                            $rootScope.showWarningMessage(error.message);
                        }
                    )
                }
            }
            var selectLifecyclePhase = parsed.html($translate.instant("SELECT_LIFE_CYCLE_PHASE")).html();
            function validate(item) {
                var valid = true;
                /*if (item.toLifeCycle == null || item.toLifeCycle == undefined
                    || item.toLifeCycle == "") {
                    $rootScope.showErrorMessage(selectLifecyclePhase);
                    valid = false;
                 }*/
                return valid;
            }

            function loadDcoAffecteditems() {
                DCOService.getDcoAffectedItems(vm.dcoId).then(
                    function (data) {
                        vm.items = data;
                        angular.forEach(vm.items, function (item) {
                            item.editMode = false;
                            $rootScope.loadDCOCounts();
                        })
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            function updateItem(item) {
                item.change = item.dco;
                DCOService.createDcoAffectedItem(vm.dcoId, item).then(
                    function (data) {
                        item.id = data.id;
                        item.editMode = false;
                        $rootScope.showSuccessMessage(itemsUpdateMsg)
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.editItem = editItem;
            function editItem(item) {
                item.editMode = true;
                vm.itemFlag = false;
                $scope.notes = item.notes;
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(item) {
                item.editMode = false;
                item.notes = $scope.notes;
            }

            vm.onCancel = onCancel;
            function onCancel(item) {
                vm.selectedItemIds.splice(vm.selectedItemIds.indexOf(item.item), 1);
                vm.items.splice(vm.items.indexOf(item), 1);
            }

            vm.showDcr = showDcr;
            function showDcr(dcr) {
                $state.go('app.changes.dcr.details', {dcrId: dcr.id})
            }

            vm.showFromRevision = showFromRevision;
            function showFromRevision(item) {
                $state.go('app.items.details', {itemId: item.item})
            }

            vm.showToRevision = showToRevision;
            function showToRevision(item) {
                $state.go('app.items.details', {itemId: item.toItem})
            }

            var removeItems = parsed.html($translate.instant("REMOVE_ITEM")).html();
            var itemRemoveTitle = parsed.html($translate.instant("REMOVE_ITEM_TITLE_MSG")).html();
            var itemRemoveMsg = parsed.html($translate.instant("REMOVE_ITEM_SUCCESS_MSG")).html();
            vm.deleteItem = deleteItem;

            function deleteItem(item) {
                var options = {
                    title: removeItems,
                    message: itemRemoveTitle,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($(".view-container"));
                        DCOService.deleteDcoAffectedItem(vm.dcoId, item.id).then(
                            function (data) {
                                var index = vm.items.indexOf(item);
                                vm.items.splice(index, 1);
                                $rootScope.hideBusyIndicator();
                                $rootScope.showSuccessMessage(itemRemoveMsg);
                                loadDcoAffecteditems();
                            }, function (error) {
                                $rootScope.hideBusyIndicator();
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                });

            }

            vm.showItem = showItem;
            function showItem(item) {
                $state.go('app.items.details', {itemId: item.item});
                $window.localStorage.setItem("lastSelectedDcoTab", JSON.stringify("details.affectedItems"));
            }

            (function () {
                //if ($application.homeLoaded == true) {
                $scope.$on('app.dco.tabActivated', function (event, args) {
                    if (args.tabId == 'details.affectedItems') {
                        loadDcoAffecteditems();

                    }
                });
                //}
            })();
        }
    }
)
;
