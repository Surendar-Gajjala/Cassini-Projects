define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'moment',
        'moment-timezone-with-data',
        'app/shared/services/core/ncrService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('NcrProblemItemsController', NcrProblemItemsController);

        function NcrProblemItemsController($scope, $rootScope, $sce, $timeout, $state, $translate, $stateParams, $cookies, $window, DialogService, NcrService) {
            var vm = this;
            vm.ncrId = $stateParams.ncrId;

            var parsed = angular.element("<div></div>");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var removePartTitle = parsed.html($translate.instant("REMOVE_PART")).html();
            var removePartDialogMsg = parsed.html($translate.instant("REMOVE_PART_TITLE_MSG")).html();
            var partRemovedMessage = parsed.html($translate.instant("REMOVE_PART_SUCCESS_MSG")).html();
            var partAddedMessage = parsed.html($translate.instant("PART_ADDED_MSG")).html();
            var partsAddedMessage = parsed.html($translate.instant("PARTS_ADDED_MSG")).html();
            var partUpdatedMessage = parsed.html($translate.instant("PART_UPDATED_MSG")).html();
            var enterReceivedQty = parsed.html($translate.instant("ENTER_RECEIVED_QTY")).html();
            var enterPositiveReceivedQty = parsed.html($translate.instant("ENTER_POSITIVE_RECEIVED_QTY")).html();
            var enterInspectedQty = parsed.html($translate.instant("ENTER_INSPECTED_QTY")).html();
            var enterPositiveInspectedQty = parsed.html($translate.instant("ENTER_POSITIVE_INSPECTED_QTY")).html();
            var inspectedQtyShouldBeGreaterThanOne = parsed.html($translate.instant("INSPECTED_QTY_SHOULD_BE_GREATER_THAN_ONE")).html();
            var enterDefectiveQty = parsed.html($translate.instant("ENTER_DEFECTIVE_QTY")).html();
            var enterPostiveDefectiveQty = parsed.html($translate.instant("ENTER_POSITIVE_DEFECTIVE_QTY")).html();
            var inspectedNotMoreThanReceived = parsed.html($translate.instant("INSPECTED_NOT_MORE_THAN_RECEIVED")).html();
            var defectiveNotMoreThanReceived = parsed.html($translate.instant("DEFECTIVE_NOT_MORE_THAN_RECEIVED")).html();
            var defectiveNotMoreThanInspectedQty = parsed.html($translate.instant("DEFECTIVE_NOT_MORE_THAN_INSPECTED")).html();
            var receivedQtyShouldMore = parsed.html($translate.instant("RECEIVED_QTY_SHOULD_BE_MORE")).html();
            $scope.addProblemItemTitle = parsed.html($translate.instant("ADD_PROBLEM_ITEMS")).html();
            $scope.saveItemTitle = parsed.html($translate.instant("SAVE_THIS_ITEM")).html();
            var wfStartedOnePiSbt = parsed.html($translate.instant("WF_STARTED_ONE_PI_SBT")).html();


            var emptyItem = {
                id: null,
                ncr: vm.ncrId,
                material: null,
                receivedQty: 1,
                inspectedQty: 0,
                defectiveQty: 0,
                notes: null
            };

            vm.saveAll = saveAll;
            vm.save = save;
            vm.updateItem = updateItem;
            vm.addAffectedItems = addAffectedItems;
            vm.problemItems = [];
            vm.itemFlag = false;
            vm.selectedItems = [];
            vm.selectedItemIds = [];
            function addAffectedItems() {
                var options = {
                    title: "Select Mfr Parts",
                    template: 'app/desktop/modules/item/details/selectMfrItemView.jsp',
                    controller: 'SelectMfrItemController as mfrItemVm',
                    resolve: 'app/desktop/modules/item/details/selectMfrItemController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedNcrId: vm.ncrId,
                        selectMfrPartsMode: "NCR",
                        addAffectedParts: vm.selectedItemIds
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.item.mfr.new'}
                    ],
                    callback: function (result) {
                        vm.parts = result;
                        angular.forEach(vm.parts, function (part) {
                            var newAffectedPart = angular.copy(emptyItem);
                            newAffectedPart.material = part;
                            newAffectedPart.editMode = true;
                            newAffectedPart.isNew = true;
                            vm.problemItems.unshift(newAffectedPart);
                            vm.selectedItems.push(newAffectedPart);
                            vm.selectedItemIds.push(part.id);
                        });

                    }
                };

                $rootScope.showSidePanel(options);
            }

            var selectLifecyclePhase = parsed.html($translate.instant("SELECT_LIFE_CYCLE_PHASE")).html();
            function validateItems() {
                var valid = true;
                angular.forEach(vm.addedProblemItemsToNcr, function (item) {
                    if (valid) {
                        if (item.newToLifeCycle == null || item.newToLifeCycle == undefined
                            || item.newToLifeCycle == "") {
                            $rootScope.showErrorMessage(item.itemNumber + " : " + selectLifecyclePhase);
                            valid = false;
                        }
                    }
                })
                return valid;
            }

            function validateItems() {
                var valid = true;
                angular.forEach(vm.selectedItems, function (part) {
                    if (valid) {
                        if (part.receivedQty === null || part.receivedQty === "" || part.receivedQty === undefined) {
                            valid = false;
                            $rootScope.showWarningMessage(enterReceivedQty.format(part.material.partName));
                        } else if (part.receivedQty != null && part.receivedQty == 0) {
                            valid = false;
                            $rootScope.showWarningMessage(enterReceivedQty.format(part.material.partName));
                        } else if (part.receivedQty != null && part.receivedQty < 0) {
                            valid = false;
                            $rootScope.showWarningMessage(enterPositiveReceivedQty.format(part.material.partName));
                        } else if (part.receivedQty != null && (part.inspectedQty === null || part.inspectedQty === "" || part.inspectedQty === undefined)) {
                            valid = false;
                            $rootScope.showWarningMessage(enterInspectedQty.format(part.material.partName));
                        } else if (part.receivedQty != null && part.inspectedQty != null && part.inspectedQty == 0) {
                            valid = false;
                            $rootScope.showWarningMessage(inspectedQtyShouldBeGreaterThanOne.format(part.material.partName));
                        } else if (part.receivedQty != null && part.inspectedQty != null && part.inspectedQty < 0) {
                            valid = false;
                            $rootScope.showWarningMessage(enterPositiveInspectedQty.format(part.material.partName));
                        } else if (part.receivedQty != null && part.inspectedQty != null && (part.inspectedQty > part.receivedQty)) {
                            valid = false;
                            $rootScope.showWarningMessage(inspectedNotMoreThanReceived.format(part.material.partName, part.receivedQty));
                        } else if (part.receivedQty != null && (part.defectiveQty === null || part.defectiveQty === "" || part.defectiveQty === undefined)) {
                            valid = false;
                            $rootScope.showWarningMessage(enterDefectiveQty.format(part.material.partName));
                        } else if (part.receivedQty != null && part.defectiveQty != null && part.defectiveQty < 0) {
                            valid = false;
                            $rootScope.showWarningMessage(enterPostiveDefectiveQty.format(part.material.partName));
                        } else if (part.receivedQty != null && part.inspectedQty != null && part.defectiveQty != null && (part.defectiveQty > part.inspectedQty)) {
                            valid = false;
                            $rootScope.showWarningMessage(defectiveNotMoreThanInspectedQty.format(part.material.partName, part.inspectedQty));
                        } else if (part.receivedQty != null && part.defectiveQty != null && (part.defectiveQty > part.receivedQty)) {
                            valid = false;
                            $rootScope.showWarningMessage(defectiveNotMoreThanReceived.format(part.material.partName, part.receivedQty));
                        }

                    }
                })

                return valid;
            }

            vm.saveAll = saveAll;
            function saveAll() {
                if (validateItems()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    NcrService.createNcrProblemItems(vm.ncrId, vm.selectedItems).then(
                        function (data) {
                            vm.selectedItems = [];
                            vm.selectedItems = [];
                            loadNcrProblemItems();
                            $rootScope.loadNcrDetails();
                            $rootScope.showSuccessMessage(partsAddedMessage);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            vm.removeAll = removeAll;
            function removeAll() {
                angular.forEach(vm.selectedItems, function (item) {
                    vm.problemItems.splice(vm.problemItems.indexOf(item), 1);
                })
                vm.selectedItems = [];
                vm.selectedItemIds = [];
            }


            function save(item) {
                if (validatePart(item)) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    NcrService.createNcrProblemItem(vm.ncrId, item).then(
                        function (data) {
                            item.id = data.id;
                            item.editMode = false;
                            item.isNew = false;
                            $rootScope.loadNcrDetails();
                            vm.selectedItems.splice(vm.selectedItems.indexOf(item), 1);
                            vm.selectedItemIds.splice(vm.selectedItemIds.indexOf(item.material), 1);
                            $rootScope.showSuccessMessage(partAddedMessage);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validatePart(part) {
                var valid = true;
                if (part.receivedQty === null || part.receivedQty === "" || part.receivedQty === undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(enterReceivedQty.format(part.material.partName));
                } else if (part.receivedQty != null && part.receivedQty == 0) {
                    valid = false;
                    $rootScope.showWarningMessage(enterReceivedQty.format(part.material.partName));
                } else if (part.receivedQty != null && part.receivedQty < 0) {
                    valid = false;
                    $rootScope.showWarningMessage(enterPositiveReceivedQty.format(part.material.partName));
                } else if (part.receivedQty != null && (part.inspectedQty === null || part.inspectedQty === "" || part.inspectedQty === undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(enterInspectedQty.format(part.material.partName));
                } else if (part.receivedQty != null && part.inspectedQty != null && part.inspectedQty == 0) {
                    valid = false;
                    $rootScope.showWarningMessage(inspectedQtyShouldBeGreaterThanOne.format(part.material.partName));
                } else if (part.receivedQty != null && part.inspectedQty != null && part.inspectedQty < 0) {
                    valid = false;
                    $rootScope.showWarningMessage(enterPositiveInspectedQty.format(part.material.partName));
                } else if (part.receivedQty != null && part.inspectedQty != null && (part.inspectedQty > part.receivedQty)) {
                    valid = false;
                    $rootScope.showWarningMessage(inspectedNotMoreThanReceived.format(part.material.partName, part.receivedQty));
                } else if (part.receivedQty != null && (part.defectiveQty === null || part.defectiveQty === "" || part.defectiveQty === undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(enterDefectiveQty.format(part.material.partName));
                } else if (part.receivedQty != null && part.defectiveQty != null && part.defectiveQty < 0) {
                    valid = false;
                    $rootScope.showWarningMessage(enterPostiveDefectiveQty.format(part.material.partName));
                } else if (part.receivedQty != null && part.inspectedQty != null && part.defectiveQty != null && (part.defectiveQty > part.inspectedQty)) {
                    valid = false;
                    $rootScope.showWarningMessage(defectiveNotMoreThanInspectedQty.format(part.material.partName, part.inspectedQty));
                } else if (part.receivedQty != null && part.defectiveQty != null && (part.defectiveQty > part.receivedQty)) {
                    valid = false;
                    $rootScope.showWarningMessage(defectiveNotMoreThanReceived.format(part.material.partName, part.receivedQty));
                }

                return valid;
            }

            function loadNcrProblemItems() {
                $rootScope.showBusyIndicator();
                vm.loading=true;
                NcrService.getNcrProblemItems(vm.ncrId).then(
                    function (data) {
                        vm.problemItems = data;
                        angular.forEach(vm.problemItems, function (item) {
                            item.editMode = false;
                            item.isNew = false;
                        })
                        vm.loading=false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            function updateItem(item) {
                if (validatePart(item)) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    NcrService.updateNcrProblemItem(vm.ncrId, item).then(
                        function (data) {
                            item.id = data.id;
                            item.receivedQty = data.receivedQty;
                            item.inspectedQty = data.inspectedQty;
                            item.defectiveQty = data.defectiveQty;
                            item.editMode = false;
                            $rootScope.showSuccessMessage(partUpdatedMessage);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.editItem = editItem;
            function editItem(item) {
                item.editMode = true;
                item.isNew = false;
                item.oldDefectiveQty = item.defectiveQty;
                item.oldInspectedQty = item.inspectedQty;
                item.oldReceivedQty = item.receivedQty;
                item.oldNotes = item.notes;
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(item) {
                item.editMode = false;
                item.defectiveQty = item.oldDefectiveQty;
                item.inspectedQty = item.oldInspectedQty;
                item.receivedQty = item.oldReceivedQty;
                item.notes = item.oldNotes;
            }

            vm.onCancel = onCancel;
            function onCancel(item) {
                vm.problemItems.splice(vm.problemItems.indexOf(item), 1);
                vm.selectedItems.splice(vm.selectedItems.indexOf(item), 1);
                vm.selectedItemIds.splice(vm.selectedItemIds.indexOf(item.material.id), 1);
            }

            vm.deleteItem = deleteItem;

            function deleteItem(item) {
                if ($rootScope.ncrWorkflowStarted && $rootScope.ncrDetailsCount.problemItems == 1) {
                    $rootScope.showWarningMessage(wfStartedOnePiSbt);
                } else {
                    var options = {
                        title: removePartTitle,
                        message: removePartDialogMsg,
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            NcrService.deleteNcrProblemItem(vm.ncrId, item.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(partRemovedMessage);
                                    $rootScope.loadNcrDetails();
                                    loadNcrProblemItems();
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    });
                }
            }

            $scope.showAffectedItem = showAffectedItem;
            function showAffectedItem() {
                $window.localStorage.setItem("lastSelectedNcrTab", JSON.stringify("details.problemItem"));
            }


            (function () {
                $scope.$on('app.ncr.tabActivated', function (event, data) {
                    if (data.tabId == 'details.problemItem') {
                        loadNcrProblemItems();
                    }
                })
            })();
        }
    }
)
;