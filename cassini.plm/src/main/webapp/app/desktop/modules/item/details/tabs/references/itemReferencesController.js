define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemReferenceService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService'

    ],
    function (module) {
        module.controller('ItemReferencesController', ItemReferencesController);

        function ItemReferencesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, ItemReferenceService,
                                          ItemService, DialogService, $uibModal) {
            var vm = this;
            vm.loading = true;
            vm.itemId = $stateParams.itemId;
            vm.item = null;
            vm.references = [];
            vm.rootItems = [];

            vm.toggleNode = toggleNode;
            vm.onOk = onOk;
            vm.onCancel = onCancel;
            vm.findItem = findItem;
            vm.editItem = editItem;
            vm.deleteItem = deleteItem;
            vm.refItemSelection = refItemSelection;

            var emptyReferenceItem = {
                id: null,
                parent: vm.itemId,
                itemObject: {
                    id: null,
                    itemNumber: null,
                    description: null,
                    itemType: {
                        id: null,
                        name: null
                    },
                    revision: null,
                    status: null,
                    notes: null,
                    hasReference: false
                },
                notes: null,
                editMode: true,
                isNew: true
            };

            function refItemSelection(item) {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/desktop/modules/item/details/itemSelectionView.jsp',
                    controller: 'ItemSelectionController as itemSelectionVm',
                    size: 'lg'
                });

                modalInstance.result.then(
                    function (result) {
                        item.itemObject.itemNumber = result.itemNumber;
                        findItem(item);
                    }
                );
            }

            function removeChildren(reference) {
                if (reference.children != null && reference.children != undefined) {
                    angular.forEach(reference.children, function (item) {
                        removeChildren(item);
                    });

                    var index = vm.references.indexOf(reference);
                    vm.references.splice(index + 1, reference.children.length);
                    reference.children = [];
                    reference.expanded = false;
                }
            }

            function loadReferences() {
                ItemReferenceService.getItemReferences(vm.itemId).then(
                    function (data) {
                        vm.references = data;

                        angular.forEach(vm.references, function (ref) {
                            ref.isNew = false;
                            ref.isRoot = true;
                            ref.editMode = false;
                            ref.newNotes = ref.notes;
                        });

                        ItemService.getItemReferences(vm.references, 'item');
                        vm.loading = false;

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            function toggleNode(reference) {
                reference.expanded = !reference.expanded;

                var index = vm.references.indexOf(reference);
                if (reference.expanded == false) {
                    removeChildren(reference);
                }
                else {
                    if (reference.children.length == 0) {
                        ItemReferenceService.getItemReference(reference.item.id).then(
                            function (data) {
                                angular.forEach(data, function (item) {
                                    item.isNew = false;
                                    item.expanded = false;
                                    item.level = reference.level + 1;
                                    if (reference.item.hasReference == true) {
                                        item.children = [];
                                    }
                                    reference.children.push(item);
                                });

                                angular.forEach(reference.children, function (item) {
                                    index = index + 1;
                                    vm.references.splice(index, 0, item);
                                });
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                    else {
                        angular.forEach(reference.children, function (item) {
                            item.level = reference.level + 1;
                            index = index + 1;
                            vm.references.splice(index, 0, item);
                        });
                    }
                }
            }

            function onOk(reference) {
                if (reference.id == null || reference.id == undefined) {
                    var itemReference = angular.copy(reference);
                    itemReference.revision = itemReference.itemObject.revision;
                    itemReference.status = itemReference.itemObject.status;
                    itemReference.item = itemReference.itemObject.id;
                    itemReference.notes = itemReference.newNotes;
                    ItemReferenceService.createItemReference(vm.itemId, itemReference).then(
                        function (data) {
                            reference.id = data.id;
                            reference.isNew = false;
                            reference.isRoot = true;
                            reference.editMode = false;
                            reference.notes = data.notes;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
                else {
                    var itemReference = angular.copy(reference);
                    itemReference.revision = itemReference.itemObject.revision;
                    itemReference.status = itemReference.itemObject.status;
                    itemReference.item = itemReference.itemObject.id;
                    itemReference.notes = itemReference.newNotes;
                    ItemReferenceService.updateItemReference(vm.itemId, itemReference, reference.id).then(
                        function (data) {
                            reference.notes = data.notes;
                            reference.editMode = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function onCancel(reference) {
                if (reference.isNew == true) {
                    var index = vm.references.indexOf(reference);
                    vm.references.splice(index, 1);
                }
                else {
                    reference.newNotes = reference.notes;
                    reference.editMode = false;
                }
            }

            function findItem(reference) {
                ItemService.findByItemNumber(reference.itemObject.itemNumber).then(
                    function (data) {
                        if (data.length == 1) {
                            var foundItem = data[0];
                            reference.itemObject.id = foundItem.id;
                            reference.itemObject.description = foundItem.description;
                            reference.itemObject.itemType = foundItem.itemType;
                            reference.itemObject.revision = foundItem.revision;
                            reference.itemObject.status = foundItem.status;
                            reference.itemObject.hasReference = foundItem.hasReference;
                            reference.itemObject.hasFiles = foundItem.hasFiles;
                            if (reference.itemObject.hasReference == true) {
                                reference.children = [];
                            }
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function editItem(item) {
                item.editMode = true;
            }

            function deleteItem(reference) {
                var options = {
                    title: 'Delete REFERENCE Item',
                    message: 'Are you sure you want to delete this REFERENCE item?',
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ItemReferenceService.deleteItemReference(vm.itemId, reference, reference.id).then(
                            function () {
                                var index = vm.references.indexOf(reference);
                                vm.references.splice(index, 1);
                                $rootScope.showSuccessMessage(reference.itemObject.itemNumber + " delete successfully!");
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                });
            }

            function addMultipleReferences() {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/desktop/modules/item/details/itemsSelectionView.jsp',
                    controller: 'ItemsSelectionController as itemsSelectionVm',
                    size: 'lg'
                });

                modalInstance.result.then(
                    function (result) {
                        angular.forEach(result, function (reference) {
                            var itemNumber = reference.itemNumber;
                            reference = angular.copy(emptyReferenceItem);
                            reference.itemObject.itemNumber = itemNumber;
                            vm.references.push(reference);
                            findItem(reference);
                        })
                    }
                );
            }

            function addReference() {
                var newRow = angular.copy(emptyReferenceItem);
                vm.references.push(newRow);
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    /*loadReferences();*/
                    $scope.$on('app.item.references.multiple', function () {
                        addMultipleReferences();
                    })
                    $scope.$on('app.item.addreference', function () {
                        addReference();
                    })
                //}
            })();
        }
    }
);

