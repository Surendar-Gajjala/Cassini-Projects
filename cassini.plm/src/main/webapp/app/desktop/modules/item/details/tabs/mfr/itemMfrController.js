define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/mfrPartsService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('ItemMfrController', ItemMfrController);

        function ItemMfrController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window,
                                   ItemService, MfrPartsService, DialogService, $translate) {
            var vm = this;

            vm.loading = true;
            vm.items = null;
            $rootScope.mfrparts = null;
            vm.itemId = $stateParams.itemId;
            vm.deleteMfrpart = deleteMfrpart;
            vm.showMfrPartsDetails = showMfrPartsDetails;
            $rootScope.selectMfrParts = selectMfrParts;
            vm.statuses = ['ALTERNATE', 'PREFERRED'];
            vm.parts = [];
            vm.itemFlag = false;
            vm.lockedOwner = false;

            var parsed = angular.element("<div></div>");

            var deleteMfrPartDialogTitle = parsed.html($translate.instant("DELETE_MFR_PART_DIALOG_TITLE")).html();
            var deleteMfrPartDialogMessage = parsed.html($translate.instant("DELETE_MFR_PART_DIALOG_MESSAGE")).html();
            var deleteMfrPartMessage = parsed.html($translate.instant("DELETE_MFR_PART_MESSAGE")).html();
            vm.addMfrParts = parsed.html($translate.instant("ADD_MFR_PARTS")).html();
            var itemDelete = parsed.html($translate.instant("ITEMDELETE")).html();

            function deleteMfrpart(mfrpart) {
                var options = {
                    title: deleteMfrPartDialogTitle,
                    message: deleteMfrPartDialogMessage + " [ " + mfrpart.manufacturerPart.partNumber + " ] " + itemDelete + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        MfrPartsService.deleteMfrPartByItem(vm.itemId, mfrpart.manufacturerPart.id).then(
                            function (data) {
                                loadMfrItems();
                                // $rootScope.getItemMfrParts();
                                var index = $rootScope.mfrparts.indexOf(mfrpart);
                                $rootScope.mfrparts.splice(index, 1);
                                $rootScope.showSuccessMessage(deleteMfrPartMessage);
                                $rootScope.loadItemDetails();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                             }
                        )
                    }
                });
            }

            function loadItem() {
                ItemService.getRevisionId(vm.itemId).then(
                    function (data) {
                        vm.itemRevision = data;
                        ItemService.getItem(vm.itemRevision.itemMaster).then(
                            function (data) {
                                vm.item = data;
                                if (vm.item.lockObject) {
                                    vm.itemLocked = true;
                                    angular.forEach($rootScope.loginPersonDetails.groups, function (grp) {
                                        if (vm.itemLocked) {
                                            if (vm.item.lockedBy.defaultGroup == grp.groupId) {
                                                vm.itemLocked = false;
                                                vm.lockedOwner = true;
                                            }
                                        }
                                    });
                                } else {
                                    vm.lockedOwner = true;
                                }
                                loadMfrItems();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                             }
                        )
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }


            function loadMfrItems() {
                vm.loading = true;
                var ids = [];
                var revisionId = null;
                $rootScope.mfrparts = [];
                if (vm.item.configured) {
                    MfrPartsService.getAllItemMfrPart(vm.itemId).then(
                        function (data) {
                            $rootScope.mfrparts = data;
                            MfrPartsService.getAllItemMfrPart($rootScope.itemRevision.instance).then(
                                function (data) {
                                    Array.prototype.push.apply($rootScope.mfrparts, data);
                                    angular.forEach($rootScope.mfrparts, function (part) {
                                        part.editMode = false;
                                    })
                                    vm.loading = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                 }
                            )
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                } else {
                    MfrPartsService.getAllItemMfrPart(vm.itemId).then(
                        function (data) {
                            $rootScope.mfrparts = data;
                            angular.forEach($rootScope.mfrparts, function (part) {
                                part.editMode = false;
                            })
                            vm.loading = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                }
            }

            var mfrPartsSelectionTitle = $translate.instant("SELECT_MFR_PARTS_SELECTION");
            var partsAddedMessage = $translate.instant("MFR_PARTS_ADDED_MESSAGE");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var selectMfrPartsTitle = parsed.html($translate.instant("ITEM_DETAILS_SELECT_MFR_PARTS")).html();
            var itemManufacturerAddedSuccess = parsed.html($translate.instant("ITEM_MFR_PARTS_ADDED")).html();
            var itemManufacturerUpdatedSuccess = parsed.html($translate.instant("ITEM_MFR_PARTS_UPDATED")).html();
            $scope.thisIsConfigurableItemPart = parsed.html($translate.instant("THIS_IS_CONFIGURABLE_ITEM_PART")).html();
            $scope.configurableItemPart = parsed.html($translate.instant("CONFIGURABLE_ITEM_PART")).html();

            vm.selectedParts = [];
            function selectMfrParts() {
                var options = {
                    title: mfrPartsSelectionTitle,
                    template: 'app/desktop/modules/item/details/selectMfrItemView.jsp',
                    controller: 'SelectMfrItemController as mfrItemVm',
                    resolve: 'app/desktop/modules/item/details/selectMfrItemController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedItemId: vm.itemId,
                        selectMfrPartsMode: "ITEM",
                        mfrParts: $rootScope.mfrparts
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.item.mfr.new'}
                    ],
                    callback: function (result) {
                        vm.parts = result;
                        vm.itemFlag = true;
                        var itemMfrPart = {
                            manufacturerPart: null,
                            statuses: vm.statuses,
                            status: vm.statuses[0],
                            item: vm.itemId
                        };
                        angular.forEach(vm.parts, function (part) {
                            var itemPart = angular.copy(itemMfrPart);
                            itemPart.editMode = true;
                            itemPart.manufacturerPart = part;
                            itemPart.manufacturerPart.mfrName = part.manufacturerObject.name;
                            $rootScope.mfrparts.unshift(itemPart);
                            vm.selectedParts.unshift(itemPart);
                        });

                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.onCancel = onCancel;
            function onCancel(item) {
                $rootScope.mfrparts.splice($rootScope.mfrparts.indexOf(item), 1);
                vm.selectedParts.splice(vm.selectedParts.indexOf(item), 1);
            }

            vm.createMfrPart = createMfrPart;
            function createMfrPart(mfrPart) {
                if (mfrPart.id == null) {
                    MfrPartsService.createItemPart(vm.itemId, mfrPart).then(
                        function (data) {
                            mfrPart.id = data.id;
                            mfrPart.editMode = false;
                            vm.selectedParts.splice(vm.selectedParts.indexOf(mfrPart), 1);
                            $rootScope.loadItemDetails();
                            $rootScope.showSuccessMessage(itemManufacturerAddedSuccess);
                            // $rootScope.getItemMfrParts();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message)
                        }
                    )
                } else {
                    MfrPartsService.updateItemPart(vm.itemId, mfrPart).then(
                        function (data) {
                            mfrPart.id = data.id;
                            mfrPart.editMode = false;
                            $rootScope.showSuccessMessage(itemManufacturerUpdatedSuccess);
                            loadMfrItems();
                            //$rootScope.getItemMfrParts();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message)
                        }
                    )
                }
            }

            vm.saveAll = saveAll;
            function saveAll() {
                $rootScope.showBusyIndicator($('.view-container'));
                MfrPartsService.createItemParts(vm.itemId, vm.selectedParts).then(
                    function (data) {
                        loadMfrItems();
                        vm.selectedParts = [];
                        $rootScope.loadItemDetails();
                        $rootScope.showSuccessMessage(itemManufacturerAddedSuccess);
                        $rootScope.hideBusyIndicator();
                        $rootScope.getItemMfrParts();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.removeAll = removeAll;
            function removeAll() {
                angular.forEach(vm.selectedParts, function (part) {
                    $rootScope.mfrparts.splice($rootScope.mfrparts.indexOf(part), 1);
                })
                vm.selectedParts = [];
            }

            vm.editMfrPart = editMfrPart;
            function editMfrPart(part) {
                $rootScope.partStatus = null;
                vm.itemFlag = false;
                part.editMode = true;
                part.statuses = vm.statuses;
                $rootScope.partStatus = part.status;
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(part) {
                part.editMode = false;
                part.status = $rootScope.partStatus;
            }

            vm.showMfr = showMfr;
            function showMfr(part) {
                $state.go('app.mfr.details', {manufacturerId: part.manufacturerPart.manufacturer});
            }

            function showMfrPartsDetails(mfrpart) {
                $window.localStorage.setItem("lastSelectedItemTab", JSON.stringify(vm.itemMfrTabId));
                $state.go('app.mfr.mfrparts.details', {
                    mfrId: mfrpart.manufacturerPart.manufacturer,
                    manufacturePartId: mfrpart.manufacturerPart.id
                });
            }

            (function () {
                $scope.$on('app.item.tabactivated', function (event, data) {
                    if (data.tabId == 'details.mfr') {
                        vm.selectedParts = [];
                        vm.itemMfrTabId = data.tabId;
                        if ($rootScope.selectedMasterItemId != null) {
                            vm.itemId = $rootScope.selectedMasterItemId;
                            loadItem();
                        }
                        if ($rootScope.selectedMasterItemId == undefined) {
                            loadItem();
                        }
                    }
                });
            })();
        }
    }
);