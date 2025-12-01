define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/relatedItemService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('RelatedItemsController', RelatedItemsController);

        function RelatedItemsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window,
                                        ItemService, RelatedItemService, DialogService, $translate) {
            var vm = this;

            vm.loading = true;
            vm.items = null;
            vm.itemId = $stateParams.itemId;
            vm.showItem = showItem;
            vm.showRelatedItemAttributes = showRelatedItemAttributes;

            var parsed = angular.element("<div></div>");
            var createButton = $translate.instant("CREATE");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var createRelatedItemTitle = parsed.html($translate.instant("ADD_RELATED_ITEM")).html();
            vm.showAttributesTitle = parsed.html($translate.instant("CLICK_TO_SHOW_ATTRIBUTES")).html();
            vm.deleteRelatedItemTitle = parsed.html($translate.instant("DELETE_RELATED_ITEM")).html();

            var relatedItemDeleteMessage = parsed.html($translate.instant("RELATED_ITEM_DELETE_MESSAGE")).html();
            var deleteRelatedItemTitle = parsed.html($translate.instant("DELETE_RELATED_ITEM_TITLE")).html();
            var deleteRelatedItemMessage = parsed.html($translate.instant("DELETE_RELATED_ITEM_MESSAGE")).html();
            var alternatePartRemoveMessage = parsed.html($translate.instant("ALTERNATE_PART_REMOVED_MESSAGE")).html();
            var removeAlternatePartTitle = parsed.html($translate.instant("REMOVE_ALTERNATE_PART_TITLE")).html();
            var removeAlternatePartMessage = parsed.html($translate.instant("REMOVE_ALTERNATE_PART_MESSAGE")).html();
            var substitutePartRemoveMessage = parsed.html($translate.instant("SUBSTITUTE_PART_REMOVED_MESSAGE")).html();
            var removeSubstitutePartTitle = parsed.html($translate.instant("REMOVE_SUBSTITUTE_PART_TITLE")).html();
            var removeSubstitutePartMessage = parsed.html($translate.instant("REMOVE_SUBSTITUTE_PART_MESSAGE")).html();
            var relatedItemAttribute = parsed.html($translate.instant("RELATED_ITEM_ATTRIBUTES")).html();
            var yes = parsed.html($translate.instant("YES")).html();
            var no = parsed.html($translate.instant("NO")).html();
            $scope.fromItemTitle = parsed.html($translate.instant("FROM_ITEM_TITLE")).html();
            $scope.toItemTitle = parsed.html($translate.instant("TO_ITEM_TITLE")).html();
            $scope.existOnBoth = parsed.html($translate.instant("EXIST_ON_BOTH")).html();
            $scope.noRelatedItems = parsed.html($translate.instant("NO_RELATED_ITEMS")).html();

            function loadItem() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                ItemService.getRevisionId(vm.itemId).then(
                    function (data) {
                        vm.itemRevision = data;
                        ItemService.getItem(vm.itemRevision.itemMaster).then(
                            function (data) {
                                vm.item = data;
                                loadRelatedItems();
                                loadAlternateParts();
                                loadSubstituteParts();
                                vm.loading = false;
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.hideBusyIndicator();
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $rootScope.loadRelatedItems = loadRelatedItems;
            function loadRelatedItems() {
                RelatedItemService.getRelatedItems(vm.itemRevision).then(
                    function (data) {
                        vm.relatedItems = data;
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.deleteRelatedItem = deleteRelatedItem;

            function deleteRelatedItem(relatedItem) {
                var options = {
                    title: deleteRelatedItemTitle,
                    message: deleteRelatedItemMessage,
                    okButtonClass: 'btn-danger',
                    okButtonText: yes,
                    cancelButtonText: no
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        RelatedItemService.deleteRelatedItem(relatedItem).then(
                            function (data) {
                                loadRelatedItems();
                                $rootScope.showSuccessMessage(relatedItemDeleteMessage);
                                /*$rootScope.getItemData();*/
                                $rootScope.loadItemDetails();
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    }
                });
            }

            function showRelatedItemAttributes(relatedItem) {
                var options = {
                    title: relatedItemAttribute,
                    template: 'app/desktop/modules/item/details/tabs/relatedItems/attributes/relatedItemAttributesView.jsp',
                    controller: 'RelatedItemAttributesController as relatedItemAttributesVm',
                    resolve: 'app/desktop/modules/item/details/tabs/relatedItems/attributes/relatedItemAttributesController',
                    width: 600,
                    showMask: true,
                    data: {
                        selectedRelatedItemData: relatedItem
                    },
                    buttons: [],
                    callback: function (data) {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            $rootScope.createRelatedItem = createRelatedItem;

            function createRelatedItem() {
                var options = {
                    title: createRelatedItemTitle,
                    template: 'app/desktop/modules/item/details/tabs/relatedItems/new/newRelatedItemView.jsp',
                    controller: 'NewRelatedItemController as newRelatedItemVm',
                    resolve: 'app/desktop/modules/item/details/tabs/relatedItems/new/newRelatedItemController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedItemRevisionData: vm.itemRevision,
                        selectedFromItemData: vm.item,
                        editRelatedItem: null
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.items.details.relatedItems.new'}
                    ],
                    callback: function () {
                        loadRelatedItems();
                        $rootScope.loadItemDetails();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function showItem(itemId) {
                $rootScope.selectedMasterItemId = null;
                $state.go('app.items.details', {itemId: itemId});
            }

            function loadAlternateParts() {
                vm.selectedAlternateParts = [];
                ItemService.getAlternateParts(vm.item.id).then(
                    function (data) {
                        vm.alternateParts = data;
                        angular.forEach(vm.alternateParts, function (alternatePart) {
                            alternatePart.editMode = false;
                            alternatePart.isNew = false;
                        })
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadSubstituteParts() {
                ItemService.getSubstituteParts(vm.item.id).then(
                    function (data) {
                        vm.substituteParts = data;
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.directions = ['ONEWAY', 'TWOWAY'];

            vm.selectedAlternateParts = [];
            var multipleItemsSelectionTitle = parsed.html($translate.instant("MULTIPLE_ITEM_SELECTION")).html();
            vm.addAlternateParts = addAlternateParts;
            function addAlternateParts() {
                var options = {
                    title: multipleItemsSelectionTitle,
                    template: 'app/desktop/modules/item/details/itemsSelectionView.jsp',
                    controller: 'ItemsSelectionController as itemsSelectionVm',
                    resolve: 'app/desktop/modules/item/details/itemsSelectionController',
                    data: {
                        multipleItemSelection: vm.item,
                        mode: "alternatePart"
                    },
                    width: 700,
                    showMask: true,
                    buttons: [
                        {text: addButton, broadcast: 'add.select.items'}
                    ],
                    callback: function (items) {
                        var emptyAlternatePart = {
                            id: null,
                            part: vm.item.id,
                            replacementPart: null,
                            direction: "ONEWAY"
                        }
                        angular.forEach(items, function (item) {
                            var alternatePart = angular.copy(emptyAlternatePart);
                            alternatePart.replacementPart = item.id;
                            alternatePart.editMode = true;
                            alternatePart.isNew = true;
                            alternatePart.replacementPartName = item.itemName;
                            alternatePart.replacementPartType = item.itemType.name;
                            alternatePart.replacementPartNumber = item.itemNumber;
                            alternatePart.replacementPartRevision = item.latestRevisionObject.id;
                            alternatePart.replacementRevision = item.latestRevisionObject.revision;
                            alternatePart.lifeCyclePhase = item.latestRevisionObject.lifeCyclePhase;
                            vm.selectedAlternateParts.unshift(alternatePart);
                            vm.alternateParts.unshift(alternatePart);
                        })
                    }
                };
                $rootScope.showSidePanel(options);
            }

            var alternatePartsSuccess = parsed.html($translate.instant("ALTERNATE_PARTS_SUCCESS")).html();
            var alternatePartSuccess = parsed.html($translate.instant("ALTERNATE_PART_SUCCESS")).html();
            var alternatePartUpdated = parsed.html($translate.instant("ALTERNATE_PART_UPDATED")).html();

            vm.saveAlternateParts = saveAlternateParts;
            function saveAlternateParts() {
                $rootScope.showBusyIndicator($('.view-container'));
                ItemService.createAlternateParts(vm.item.id, vm.selectedAlternateParts).then(
                    function (data) {
                        loadAlternateParts();
                        vm.selectedAlternateParts = [];
                        $rootScope.loadItemDetails();
                        $rootScope.showSuccessMessage(alternatePartsSuccess);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.saveAlternatePart = saveAlternatePart;
            function saveAlternatePart(alternatePart) {
                $rootScope.showBusyIndicator($('.view-container'));
                ItemService.createAlternatePart(vm.item.id, alternatePart).then(
                    function (data) {
                        alternatePart.id = data.id;
                        alternatePart.editMode = false;
                        alternatePart.isNew = false;
                        vm.selectedAlternateParts.splice(vm.selectedAlternateParts.indexOf(alternatePart), 1);
                        if (vm.selectedAlternateParts.length == 0) {
                            loadAlternateParts();
                        }
                        $rootScope.loadItemDetails();
                        $rootScope.showSuccessMessage(alternatePartSuccess);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.updateAlternatePart = updateAlternatePart;
            function updateAlternatePart(alternatePart) {
                $rootScope.showBusyIndicator($('.view-container'));
                ItemService.updateAlternatePart(vm.item.id, alternatePart).then(
                    function (data) {
                        alternatePart.id = data.id;
                        alternatePart.editMode = false;
                        alternatePart.isNew = false;
                        loadAlternateParts();
                        $rootScope.showSuccessMessage(alternatePartUpdated);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.editAlternatePart = editAlternatePart;
            function editAlternatePart(alternatePart) {
                alternatePart.oldDirection = alternatePart.direction;
                alternatePart.editMode = true;
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(alternatePart) {
                alternatePart.direction = alternatePart.oldDirection;
                alternatePart.editMode = false;
            }

            vm.removeAlternatePart = removeAlternatePart;
            function removeAlternatePart(alternatePart) {
                vm.alternateParts.splice(vm.alternateParts.indexOf(alternatePart), 1);
                vm.selectedAlternateParts.splice(vm.selectedAlternateParts.indexOf(alternatePart), 1);
            }

            vm.removeAllAlternateParts = removeAllAlternateParts;
            function removeAllAlternateParts() {
                angular.forEach(vm.selectedAlternateParts, function (alternatePart) {
                    vm.alternateParts.splice(vm.alternateParts.indexOf(alternatePart), 1);
                })
                vm.selectedAlternateParts = [];
            }

            vm.deleteAlternatePart = deleteAlternatePart;

            function deleteAlternatePart(alternatePart) {
                var options = {
                    title: removeAlternatePartTitle,
                    message: removeAlternatePartMessage,
                    okButtonClass: 'btn-danger',
                    okButtonText: yes,
                    cancelButtonText: no
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        ItemService.deleteAlternatePart(vm.item.id, alternatePart.id).then(
                            function (data) {
                                loadAlternateParts();
                                $rootScope.showSuccessMessage(alternatePartRemoveMessage);
                                $rootScope.loadItemDetails();
                                $rootScope.hideBusyIndicator();
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        );
                    }
                });
            }

            vm.deleteSubstitutePart = deleteSubstitutePart;

            function deleteSubstitutePart(substitutePart) {
                var options = {
                    title: removeSubstitutePartTitle,
                    message: removeSubstitutePartMessage,
                    okButtonClass: 'btn-danger',
                    okButtonText: yes,
                    cancelButtonText: no
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        ItemService.deleteSubstitutePart(vm.item.id, substitutePart.id).then(
                            function (data) {
                                loadSubstituteParts();
                                $rootScope.showSuccessMessage(substitutePartRemoveMessage);
                                $rootScope.loadItemDetails();
                                $rootScope.hideBusyIndicator();
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        );
                    }
                });
            }

            (function () {
                $scope.$on('app.item.tabactivated', function (event, data) {
                    if (data.tabId == 'details.relatedItems') {
                        vm.itemRelatedTabId = data.tabId;
                        if ($rootScope.selectedMasterItemId != null) {
                            vm.itemId = $rootScope.selectedMasterItemId;
                        }
                        loadItem();
                    }
                });
            })();
        }
    }
);