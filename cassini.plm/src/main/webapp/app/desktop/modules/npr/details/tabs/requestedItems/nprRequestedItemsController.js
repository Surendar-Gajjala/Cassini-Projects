define(
    [
        'app/desktop/modules/npr/npr.module',
        'app/shared/services/core/nprService',
        'app/shared/services/core/itemService'

    ],
    function (module) {
        module.controller('NprRequestedItemsController', NprRequestedItemsController);

        function NprRequestedItemsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                             $translate, NprService, ItemService, DialogService) {
            var vm = this;
            vm.nprId = $stateParams.nprId;
            vm.loading = true;
            var parsed = angular.element("<div></div>");
            var deleteDialogTitle = parsed.html($translate.instant("DELETE_ITEM")).html();
            var deleteItemDialogMessage = parsed.html($translate.instant("DELETE_ITEM_DIALOG_MESSAGE")).html();
            var itemDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            var itemDeletedMessage = parsed.html($translate.instant("ITEM_DELETED_MESSAGE")).html();
            var itemUpdated = parsed.html($translate.instant("ITEM_UPDATE_MESSAGE")).html();
            var newItemTitle = $translate.instant("NEW_ITEM_TITLE");
            var createButton = $translate.instant("CREATE");
            var itemNumberAssigned = $translate.instant("ITEM_NUMBER_ASSIGNED");
            vm.createRequestedItem = createRequestedItem;

            function createRequestedItem() {
                var options = {
                    title: newItemTitle,
                    template: 'app/desktop/modules/npr/details/tabs/requestedItems/newRequestedItemView.jsp',
                    controller: 'NewRequestedItemController as newRequestedItemVm',
                    resolve: 'app/desktop/modules/npr/details/tabs/requestedItems/newRequestedItemController',
                    width: 600,
                    showMask: true,
                    data: {
                        selectedNpr: vm.nprId
                    },
                    buttons: [
                        {text: createButton, broadcast: 'app.newNprItem.new'}
                    ],
                    callback: function (result) {
                        loadNprItems();
                        $rootScope.loadNprTabCounts();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            $rootScope.loadNprItems = loadNprItems;
            function loadNprItems() {
                NprService.getNprItems(vm.nprId).then(
                    function (data) {
                        vm.nprItems = data;
                        angular.forEach(vm.nprItems, function (nprItem) {
                            nprItem.editMode = false;
                        })
                        ItemService.getLatestRevisionReferences(vm.nprItems, 'latestRevision');
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.updateItem = updateItem;
            function updateItem(item) {
                $rootScope.showBusyIndicator($('.view-container'));
                NprService.updateNprItem(vm.nprId, item).then(
                    function (data) {
                        item.editMode = false;
                        $rootScope.showSuccessMessage(itemUpdated);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(substance) {
                substance.notes = substance.oldNotes;
                substance.editMode = false;
            }

            vm.editItem = editItem;
            function editItem(substance) {
                substance.oldNotes = substance.notes;
                substance.editMode = true;
            }

            vm.deleteItem = deleteItem;
            function deleteItem(item) {
                var options = {
                    title: deleteDialogTitle,
                    message: deleteItemDialogMessage + " [" + item.itemNumber + "]" + itemDelete + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        NprService.deleteNprItem(vm.nprId, item.id).then(
                            function (data) {
                                loadNprItems();
                                $rootScope.showSuccessMessage(itemDeletedMessage);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            vm.assignItemNumber = assignItemNumber;
            function assignItemNumber(item) {
                $rootScope.showBusyIndicator($('.view-container'));
                NprService.assignItemNumber(vm.nprId, item.id).then(
                    function (data) {
                        item.assignedNumber = data.assignedNumber;
                        item.itemNumber = data.itemNumber;
                        $rootScope.loadNprTabCounts();
                        $rootScope.showSuccessMessage(itemNumberAssigned);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.showItem = showItem;
            function showItem(itemId) {
                $state.go('app.items.details', {itemId: itemId});
            }

            (function () {
                $scope.$on('app.npr.tabActivated', function (event, data) {
                    if (data.tabId == 'details.requestedItems') {
                        loadNprItems()
                    }
                });

            })();
        }
    }
);