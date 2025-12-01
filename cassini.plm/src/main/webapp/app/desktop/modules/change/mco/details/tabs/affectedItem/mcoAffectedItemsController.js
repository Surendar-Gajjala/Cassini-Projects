define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/dcrService',
        'app/shared/services/core/mcoService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'

    ],
    function (module) {
        module.controller('MCOAffectedItemsController', MCOAffectedItemsController);

        function MCOAffectedItemsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                            $translate, DialogService, CommentsService, MCOService) {
            var vm = this;
            vm.loading = true;
            vm.mcoId = $stateParams.mcoId;

            var parsed = angular.element("<div></div>");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var removePartTitle = parsed.html($translate.instant("REMOVE_PART")).html();
            var removeMbomTitle = parsed.html($translate.instant("REMOVE_MBOM")).html();
            var removePartDialogMsg = parsed.html($translate.instant("REMOVE_PART_TITLE_MSG")).html();
            var removeMbomDialogMsg = parsed.html($translate.instant("REMOVE_MBOM_TITLE_MSG")).html();
            var partRemovedMessage = parsed.html($translate.instant("REMOVE_PART_SUCCESS_MSG")).html();
            var partAddedMessage = parsed.html($translate.instant("PART_ADDED_MSG")).html();
            var mbomRemovedMessage = parsed.html($translate.instant("REMOVE_MBOM_SUCCESS_MSG")).html();
            var mbomAddedMessage = parsed.html($translate.instant("MBOM_ADDED_MSG")).html();
            var partUpdatedMessage = parsed.html($translate.instant("PART_UPDATED_MSG")).html();
            var mbomUpdatedMessage = parsed.html($translate.instant("MBOM_UPDATED_MSG")).html();
            var selectReplacementPart = parsed.html($translate.instant("SELECT_REPLACEMENT_PART")).html();
            $scope.addAffectedItemTitle = parsed.html($translate.instant("ADD_AFFECTED_ITEMS")).html();
            $scope.addAffectedMBOMTitle = parsed.html($translate.instant("ADD_AFFECTED_MBOMS")).html();
            $scope.saveItemTitle = parsed.html($translate.instant("SAVE_THIS_ITEM")).html();

            var emptyItem = {
                id: null,
                mco: vm.mcoId,
                material: null,
                replacement: null,
                changeType: "REPLACED",
                notes: null
            };

            var emptyMbom = {
                id: null,
                mco: vm.mcoId,
                item: null,
                toItem: null,
                type: null,
                name: null,
                number: null,
                fromRevision: null,
                toRevision: null,
                notes: null,
                effectiveDate: null
            };

            vm.changeTypes = [
                {label: "REPLACE", value: "REPLACED"},
                {label: "REMOVE", value: "REMOVED"}
            ]

            vm.save = save;
            vm.updateItem = updateItem;
            vm.updateMbom = updateMbom;
            vm.addAffectedItems = addAffectedItems;
            vm.addAffectedMboms = addAffectedMboms;
            vm.affectedItems = [];
            vm.itemFlag = false;
            vm.selectedItems = [];
            vm.selectedItemIds = [];
            vm.selectedMboms = [];
            vm.selectedMbomIds = [];
            vm.affectedMboms = [];
            vm.mbomFlag = false;
            function addAffectedItems() {
                var options = {
                    title: "Select Mfr Parts",
                    template: 'app/desktop/modules/item/details/selectMfrItemView.jsp',
                    controller: 'SelectMfrItemController as mfrItemVm',
                    resolve: 'app/desktop/modules/item/details/selectMfrItemController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedMcoId: vm.mcoId,
                        selectMfrPartsMode: "MCO",
                        addAffectedParts: vm.selectedItemIds
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.item.mfr.new'}
                    ],
                    callback: function (result) {
                        vm.parts = result;
                        angular.forEach(vm.parts, function (part) {
                            var newAffectedPart = angular.copy(emptyItem);
                            newAffectedPart.material = part.id;
                            newAffectedPart.materialName = part.partName;
                            newAffectedPart.materialNumber = part.partNumber;
                            newAffectedPart.materialType = part.mfrPartType.name;
                            newAffectedPart.editMode = true;
                            newAffectedPart.isNew = true;
                            vm.affectedItems.unshift(newAffectedPart);
                            vm.selectedItems.unshift(newAffectedPart);
                            vm.selectedItemIds.push(part.id);
                        });

                    }
                };

                $rootScope.showSidePanel(options);
            }

            function addAffectedMboms() {
                var options = {
                    title: "Select MBOM(s)",
                    template: 'app/desktop/modules/mes/mbom/details/mbomSelectionView.jsp',
                    controller: 'MBOMSelectionController as mbomSelectionVm',
                    resolve: 'app/desktop/modules/mes/mbom/details/mbomSelectionController',
                    width: 750,
                    showMask: true,
                    data: {
                        selectedMcoId: vm.mcoId,
                        selectMbomsMode: "MCO",
                        addAffectedMboms: vm.selectedMbomIds
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.mboms'}
                    ],

                    callback: function (result) {
                        vm.mboms = result;
                        angular.forEach(vm.mboms, function (mbom) {
                            var newAffectedMbom = angular.copy(emptyMbom);
                            newAffectedMbom.name = mbom.name;
                            newAffectedMbom.number = mbom.number;
                            newAffectedMbom.type = mbom.typeName;
                            newAffectedMbom.itemName = mbom.itemName;
                            newAffectedMbom.itemRevision = mbom.itemRevision;
                            newAffectedMbom.description = mbom.description;
                            newAffectedMbom.fromRevision = mbom.revision;
                            newAffectedMbom.toRevision = mbom.toRevision;
                            newAffectedMbom.item = mbom.latestRevision;
                            newAffectedMbom.editMode = true;
                            newAffectedMbom.isNew = true;
                            vm.affectedMboms.unshift(newAffectedMbom);
                            vm.selectedMboms.push(newAffectedMbom);
                            vm.selectedMbomIds.push(mbom.id);
                        });
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.saveMbom = saveMbom;

            function saveMbom(mbom) {
                vm.creating = true;
                mbom.notes = mbom.newNotes;
                mbom.effectiveDate = mbom.newEffectiveDate;
                MCOService.createMcoMbom(vm.mcoId, mbom).then(
                    function (data) {
                        mbom.id = data.id;
                        mbom.isNew = false;
                        mbom.editMode = false;
                        mbom.flag = true;
                        mbom.revisionId = data.item;
                        vm.selectedMboms.splice(vm.selectedMboms.indexOf(mbom), 1);
                        if (vm.selectedMboms.length == 0) {
                            loadMcoAffectedMboms();
                        }
                        $rootScope.loadMcoDetails();
                        $rootScope.showSuccessMessage(mbomAddedMessage);
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            vm.loadReplacementParts = loadReplacementParts;
            function loadReplacementParts(affectedItem) {
                var options = {
                    title: "Select Replacement Part",
                    template: 'app/desktop/modules/item/details/selectMfrItemView.jsp',
                    controller: 'SelectMfrItemController as mfrItemVm',
                    resolve: 'app/desktop/modules/item/details/selectMfrItemController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedMcoId: vm.mcoId,
                        selectMfrPartsMode: "MCO",
                        replacementType: affectedItem.material,
                        addAffectedParts: vm.selectedItemIds
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.item.mfr.new'}
                    ],
                    callback: function (result) {
                        affectedItem.replacement = result.id;
                        affectedItem.replacementName = result.partName;
                        affectedItem.replacementNumber = result.partNumber;

                    }
                };

                $rootScope.showSidePanel(options);
            }

            function save(item) {
                if (item.changeType == "REPLACED" && (item.replacement == null || item.replacement == "" || item.replacement == undefined)) {
                    $rootScope.showWarningMessage(selectReplacementPart.format(item.materialName));
                } else {
                    $rootScope.showBusyIndicator($('.view-container'));
                    MCOService.createMcoItem(vm.mcoId, item).then(
                        function (data) {
                            item.id = data.id;
                            item.editMode = false;
                            if (item.changeType == "REMOVED") {
                                item.replacementName = null;
                                item.replacement = null;
                            }
                            $rootScope.loadMcoDetails();
                            vm.selectedItems.splice(vm.selectedItems.indexOf(item), 1);
                            $rootScope.showSuccessMessage(partAddedMessage);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.showFromRevision = showFromRevision;
            function showFromRevision(mbom) {
                $state.go('app.mes.mbom.details', {mbomId: mbom.item})
            }

            vm.showToRevision = showToRevision;
            function showToRevision(mbom) {
                $state.go('app.mes.mbom.details', {mbomId: mbom.toItem})
            }

            vm.saveAll = saveAll;
            function saveAll() {
                if (validateItems()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    MCOService.createMcoItems(vm.mcoId, vm.selectedItems).then(
                        function (data) {
                            vm.selectedItems = [];
                            $rootScope.loadMcoDetails();
                            vm.selectedItems = [];
                            loadMcoAffectedItems();
                            $rootScope.showSuccessMessage(partAddedMessage)
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validateItems() {
                var valid = true;
                angular.forEach(vm.selectedItems, function (item) {
                    if (valid) {
                        if (item.changeType == "REPLACED" && (item.replacement == null || item.replacement == "" || item.replacement == undefined)) {
                            valid = false;
                            $rootScope.showWarningMessage(selectReplacementPart.format(item.materialName));
                        }
                    }
                })

                return valid;
            }

            vm.saveAllMboms = saveAllMboms;
            function saveAllMboms() {
                $rootScope.showBusyIndicator($('.view-container'));
                MCOService.createMcoMboms(vm.mcoId, vm.selectedMboms).then(
                    function (data) {
                        vm.selectedMboms = [];
                        $rootScope.loadMcoDetails();
                        loadMcoAffectedMboms();
                        $rootScope.showSuccessMessage(mbomAddedMessage)
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.onOk = onOk;

            function onOk(mbom) {
                if (mbom.id == null || mbom.id == "" || mbom.id == undefined) {
                    saveMbom(mbom);
                } else {
                    updateMbom(mbom);
                }
            }

            function loadMcoAffectedMboms() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                MCOService.getProductAffectedItems(vm.mcoId).then(
                    function (data) {
                        vm.affectedMboms = data;
                        angular.forEach(vm.affectedMboms, function (mbom) {
                            mbom.editMode = false;
                            mbom.isNew = false;
                        });
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.removeAll = removeAll;
            function removeAll() {
                angular.forEach(vm.selectedItems, function (item) {
                    vm.affectedItems.splice(vm.affectedItems.indexOf(item), 1);
                })
                vm.selectedItems = [];
            }

            vm.removeAllMboms = removeAllMboms;
            function removeAllMboms() {
                angular.forEach(vm.selectedMboms, function (mbom) {
                    vm.affectedMboms.splice(vm.affectedMboms.indexOf(mbom), 1);
                })
                vm.selectedMboms = [];
            }

            function loadMcoAffectedItems() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                MCOService.getAffectedItems(vm.mcoId).then(
                    function (data) {
                        vm.affectedItems = data;
                        angular.forEach(vm.affectedItems, function (item) {
                            item.editMode = false;
                            item.isNew = false;
                        })
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            function updateItem(item) {
                if (item.changeType == "REPLACED" && (item.replacement == null || item.replacement == "" || item.replacement == undefined)) {
                    $rootScope.showWarningMessage(selectReplacementPart.format(item.materialName));
                } else {
                    $rootScope.showBusyIndicator($('.view-container'));
                    MCOService.updateMcoItem(vm.mcoId, item).then(
                        function (data) {
                            item.id = data.id;
                            item.editMode = false;
                            if (item.changeType == "REMOVED") {
                                item.replacementName = null;
                                item.replacement = null;
                            }
                            $rootScope.loadMcoDetails();
                            $rootScope.showSuccessMessage(partUpdatedMessage);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function updateMbom(mbom) {
                $rootScope.showBusyIndicator($('.view-container'));
                mbom.notes = mbom.newNotes;
                mbom.effectiveDate = mbom.newEffectiveDate;
                MCOService.updateMcoMbom(vm.mcoId, mbom).then(
                    function (data) {
                        mbom.id = data.id;
                        mbom.editMode = false;
                        mbom.notes = mbom.newNotes;
                        $rootScope.loadMcoDetails();
                        $rootScope.showSuccessMessage(mbomUpdatedMessage);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.editItem = editItem;
            function editItem(item) {
                item.editMode = true;
                item.isNew = false;
                item.oldReplacement = item.replacement;
                item.oldReplacementName = item.replacementName;
                item.oldChangeType = item.changeType;
                item.oldNotes = item.notes;
            }

            vm.editMbom = editMbom;
            function editMbom(mbom) {
                mbom.editMode = true;
                mbom.isNew = false;
                mbom.newEffectiveDate = mbom.effectiveDate;
                mbom.newNotes = mbom.notes;
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(item) {
                item.editMode = false;
                item.replacement = item.oldReplacement;
                item.replacementName = item.oldReplacementName;
                item.changeType = item.oldChangeType;
                item.notes = item.oldNotes;
            }

            vm.cancelMbomChanges = cancelMbomChanges;
            function cancelMbomChanges(mbom) {
                mbom.editMode = false;
                mbom.effectiveDate = mbom.newEffectiveDate;
                mbom.notes = mbom.newNotes;
            }

            vm.onCancel = onCancel;
            function onCancel(item) {
                vm.affectedItems.splice(vm.affectedItems.indexOf(item), 1);
                vm.selectedItems.splice(vm.selectedItems.indexOf(item), 1);
                vm.selectedItemIds.splice(vm.selectedItemIds.indexOf(item.material), 1);
            }

            vm.onCancelMbom = onCancelMbom;
            function onCancelMbom(mbom) {
                vm.affectedMboms.splice(vm.affectedMboms.indexOf(mbom), 1);
                vm.selectedMboms.splice(vm.selectedMboms.indexOf(mbom), 1);
                vm.selectedMbomIds.splice(vm.selectedMbomIds.indexOf(mbom.item), 1);
            }

            vm.deleteItem = deleteItem;

            var wfStartedOneAffectedItemSbt = parsed.html($translate.instant("WF_STARTED_ONE_AT_SBT")).html();

            function deleteItem(item) {
                if ($rootScope.mcoWorkflowStarted && $rootScope.mcoDetailsCount.affectedItems == 1) {
                    $rootScope.showWarningMessage(wfStartedOneAffectedItemSbt);
                } else {
                    var options = {
                        title: removePartTitle,
                        message: removePartDialogMsg,
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            MCOService.deleteMcoAffectedItem(vm.mcoId, item.id).then(
                                function (data) {
                                    $rootScope.loadMcoDetails();
                                    $rootScope.showSuccessMessage(partRemovedMessage);
                                    loadMcoAffectedItems();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    });
                }
            }

            vm.deleteMbom = deleteMbom;

            function deleteMbom(mbom) {
                if ($rootScope.mcoWorkflowStarted && $rootScope.mcoDetailsCount.affectedMboms == 1) {
                    $rootScope.showWarningMessage(wfStartedOneAffectedItemSbt);
                } else {
                    var options = {
                        title: removeMbomTitle,
                        message: removeMbomDialogMsg.format(mbom.number),
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            MCOService.deleteMcoProductAffectedItem(vm.mcoId, mbom.id).then(
                                function (data) {
                                    $rootScope.loadMcoDetails();
                                    $rootScope.showSuccessMessage(mbomRemovedMessage);
                                    loadMcoAffectedMboms();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    });
                }
            }


            $scope.showPart = showPart;
            function showPart() {
                $window.localStorage.setItem("lastSelectedMcoTab", JSON.stringify("details.affectedItems"));
            }

            vm.showMbom = showMbom;
            function showMbom(affectedItem) {
                if (affectedItem.toItem != null && affectedItem.toItem != "") {
                    $state.go('app.mes.mbom.details', {mbomId: affectedItem.toItem})
                } else {
                    $state.go('app.mes.mbom.details', {mbomId: affectedItem.item})
                }

            }

            (function () {
                //if ($application.homeLoaded == true) {
                $scope.$on('app.mco.tabActivated', function (event, args) {
                    if (args.tabId == 'details.affectedItems') {
                        vm.selectedItemIds = [];
                        vm.selectedMbomIds = [];
                        if ($rootScope.mco.mcoType.mcoType == "ITEMMCO") {
                            loadMcoAffectedMboms();
                        } else {
                            loadMcoAffectedItems();
                        }
                    }

                });
                //}
            })();
        }
    }
);