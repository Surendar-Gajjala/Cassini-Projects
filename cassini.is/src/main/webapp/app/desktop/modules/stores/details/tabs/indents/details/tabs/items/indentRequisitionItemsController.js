/**
 * Created by swapna on 19/09/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'

    ],
    function (module) {
        module.controller('IndentRequisitionItemsController', IndentRequisitionItemsController);

        function IndentRequisitionItemsController($scope, $rootScope, DialogService, $window, $state, $stateParams, CustomIndentService) {
            var vm = this;

            var reqItemsMap = new Hashtable();

            vm.editIndentItem = editIndentItem;
            vm.applyChanges = applyChanges;
            vm.removeFromIndentItems = removeFromIndentItems;
            vm.deleteIndentItem = deleteIndentItem;
            vm.cancelChanges = cancelChanges;
            vm.showItemDetails = showItemDetails;
            vm.indentRequesitionItems = [];
            vm.groupedItems = [];

            var pageable = {
                page: 0,
                size: 20
            };

            vm.indentItem = {
                id: null,
                customIndent: null,
                requisition: null,
                materialItem: null,
                indentItemQuantity: null,
                indentItemNotesObject: null,
                reqItemQuantity: null,
                quantity: null,
                notes: null
            };

            function editIndentItem(item) {
                item.editMode = true;
                item.showEditButton = false;
            }

            function applyChanges(item) {
                item.notes = item.indentItemNotesObject;
                item.quantity = item.indentItemQuantity;
                CustomIndentService.updateIndentItem(vm.indent, item).then(
                    function (data) {
                        item.showEditButton = true;
                        item.editMode = false;
                        vm.indentItem.notes = item.indentItemNotesObject;
                        vm.indentItem.quantity = item.indentItemQuantity;
                    });
            }

            function deleteIndentItem(item) {
                var options = {
                    title: 'Delete Indent Item',
                    message: 'Are you sure you want to delete this Indent Item?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        removeFromIndentItems(item);
                        CustomIndentService.deleteIndentItem(vm.indent.id, item.id).then(
                            function (data) {
                                $rootScope.showWarningMessage("Indent Item deleted successfully");
                            }, function (error) {

                            }
                        )
                    }
                });
            }

            function cancelChanges(item) {
                item.showEditButton = true;
                item.editMode = false;
                item.indentItemNotesObject = item.notes;
                item.indentItemQuantity = item.quantity;
            }

            function removeFromIndentItems(indentItem) {
                var indentItemIndex = vm.groupedItems.findIndex(item = > item.materialItem.id == indentItem.materialItem.id
            )
                ;
                var items = reqItemsMap.get(indentItem.requisition.requisitionNumber);
                if (items.length == 1) {
                    if (indentItemIndex != -1) {
                        vm.groupedItems.splice(indentItemIndex, 1);
                    }
                    var requestNumberIndex = vm.groupedItems.findIndex(item = > item.materialItem.itemNumber == indentItem.requisition.requisitionNumber
                )
                    ;
                    if (requestNumberIndex != -1) {
                        vm.groupedItems.splice(requestNumberIndex, 1);
                        reqItemsMap.remove(indentItem.requisition.requisitionNumber);
                    }
                } else {
                    if (indentItemIndex != -1) {
                        vm.groupedItems.splice(indentItemIndex, 1);
                        items = reqItemsMap.get(indentItem.requisition.requisitionNumber);
                        var removableItemIndex = items.findIndex(item = > item.materialItem.id == indentItem.materialItem.id
                    )
                        ;
                        if (removableItemIndex != -1) {
                            items.splice(removableItemIndex, 1);
                            reqItemsMap.put(indentItem.requisition.requisitionNumber, items);
                        }
                    }
                }

                if (vm.groupedItems.length == 0) {
                    vm.showCreateIndentButton = false;
                }
            }

            function back() {
                $window.history.back();
            }

            function loadIndentAndIndentItems() {
                reqItemsMap = new Hashtable();
                CustomIndentService.getIndent($stateParams.indentId).then(
                    function (data) {
                        vm.indent = data;
                        /*loading IndentItems */
                        angular.forEach(data.customIndentItems, function (item) {
                            item.isNew = false;
                            item.editMode = false;
                            item.showEditButton = true;
                            loadGroupByRequisitionItems(item.requisition, item);
                        });
                    }, function (error) {

                    }
                );

            }

            function loadGroupByRequisitionItems(requistion, reqItem) {
                vm.groupedItems = [];
                var items = reqItemsMap.get(requistion.requisitionNumber);
                if (items == null) {
                    items = [];
                    reqItemsMap.put(requistion.requisitionNumber, items);
                }
                items.push(reqItem);
                var keys = reqItemsMap.keys();
                angular.forEach(keys, function (key) {
                    vm.groupedItems.push({rowType: "req", materialItem: {itemNumber: key}});
                    items = reqItemsMap.get(key);
                    angular.forEach(items, function (item) {
                        var indentItem = angular.copy(vm.indentItem);
                        indentItem.rowType = "item";
                        indentItem.id = item.id;
                        indentItem.requisition = requistion;
                        indentItem.materialItem = item.materialItem;
                        indentItem.reqItemQuantity = item.quantity;
                        indentItem.indentItemQuantity = item.quantity;
                        indentItem.notes = item.notes;
                        indentItem.quantity = item.quantity;
                        indentItem.isNew = item.isNew;
                        indentItem.editMode = item.editMode;
                        indentItem.showEditButton = item.showEditButton;
                        indentItem.indentItemNotesObject = indentItem.isNew == true ? null : item.notes;
                        vm.groupedItems.push(indentItem);
                    });
                });
                if (vm.groupedItems.length > 0) {
                    $rootScope.groupedItems = vm.groupedItems;
                    vm.showCreateIndentButton = true;
                }
                if (requistion.customRequisitionItems != null) {
                    var requestNumberIndex = requistion.customRequisitionItems.findIndex(item = > item.materialItem.id == reqItem.materialItem.id
                )
                    ;
                    if (requestNumberIndex != -1) {
                        requistion.customRequisitionItems.splice(requestNumberIndex, 1);
                    }
                }
            }

            function addItems() {
                var options = {
                    title: 'Items',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/indents/new/indentRequestsView.jsp',
                    controller: 'IndentRequestsController as indentRequestsVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/indents/new/indentRequestsController',
                    width: 700,
                    data: {
                        projectObj: vm.indent.project,
                        groupedItems: vm.groupedItems,
                        requestItemsMap: reqItemsMap
                    },
                    buttons: [],
                    callback: function (requistion, reqItem) {
                        loadGroupByRequisitionItems(requistion, reqItem);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function update() {
                if (validateIndent()) {
                    CustomIndentService.updateIndent(vm.indent).then(
                        function (data) {
                            vm.showCreateIndentButton = false;
                            vm.editMode = false;
                            $rootScope.showSuccessMessage("Indent (" + data.indentNumber + ") updated successfully");
                            angular.forEach(data.customIndentItems, function (indentItem) {
                                var indentItemIndex = vm.groupedItems.findIndex(item = > item.materialItem.id == indentItem.materialItem.id
                                )
                                ;
                                vm.groupedItems[indentItemIndex].isNew = false;
                                vm.groupedItems[indentItemIndex].editMode = false;
                                vm.groupedItems[indentItemIndex].showEditButton = true;
                            });
                        }, function (error) {

                        }
                    )
                }
            }

            function nextPage() {
                pageable.page++;
                loadIndentAndIndentItems();
            }

            function previousPage() {
                pageable.page--;
                loadIndentAndIndentItems();
            }

            function validateIndent() {
                var valid = false;
                if (vm.indent.project == null || vm.indent.project == undefined || vm.indent.project == "") {
                    valid = false;
                    $rootScope.showWarningMessage("Please select Project");
                } else if (vm.indent.raisedDate == null || vm.indent.raisedDate == undefined || vm.indent.raisedDate == "") {
                    valid = false;
                    $rootScope.showWarningMessage("Please select Created Date");
                } else if (vm.indent.raisedBy == null || vm.indent.raisedBy == undefined || vm.indent.raisedBy == "") {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Created by");
                } else {
                    if (vm.groupedItems.length > 0) {
                        angular.forEach(vm.groupedItems, function (grpItem) {
                            if (grpItem.rowType == 'item') {
                                grpItem.quantity = grpItem.indentItemQuantity;
                                grpItem.notes = grpItem.indentItemNotesObject;
                                vm.indent.customIndentItems.push(grpItem);
                            }
                        });
                        valid = true;
                    } else {
                        $rootScope.showWarningMessage("Please add atleast one item to Indent");
                        valid = false;
                    }
                }
                return valid;
            }

            function showItemDetails(item) {
                item.materialItem.quantity = item.quantity;
                var options = {
                    title: 'Item Details',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/itemBasicInfoView.jsp',
                    controller: 'ItemBasicInfoController as itemBasicInfoVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/itemBasicInfoController',
                    width: 600,
                    data: {
                        item: item.materialItem
                    },
                    buttons: [
                        /*{text: 'Update', broadcast: 'app.stores.receivedItem.info'}*/
                    ],
                    callback: function () {
                    }
                };

                $rootScope.showSidePanel(options);
            }

            (function () {
                $scope.$on('app.indent.addItems', addItems);
                $scope.$on('app.indent.update', update);
                $scope.$on('app.indent.items.nextPageDetails', nextPage);
                $scope.$on('app.indent.items.previousPageDetails', previousPage);
                $scope.$on('app.indents.itemsTabActivated', function (event, data) {
                    if (data.tabId == 'details.items') {
                        loadIndentAndIndentItems();
                    }
                });
            })();
        }
    }
)
;