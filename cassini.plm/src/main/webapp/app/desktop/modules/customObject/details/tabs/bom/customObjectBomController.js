define(
    [
        'app/desktop/modules/customObject/customObject.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('CustomObjectBomController', CustomObjectBomController);

        function CustomObjectBomController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application,
                                           CustomObjectService, DialogService) {
            var vm = this;
            vm.customId = $stateParams.customId;
            vm.bomObjects = [];

            var parsed = angular.element("<div></div>");
            var pleaseEnterQuantityForItem = parsed.html($translate.instant("PLEASE_ENTER_QUANTITY_FOR_ITEM")).html();
            var pleaseEnterPositiveQuantity = parsed.html($translate.instant("PLEASE_ENTER_POSITIVE_QUANTITY")).html();
            var pleaseEnterPositiveQuantityForItem = parsed.html($translate.instant("PLEASE_ENTER_POSITIVE_QUANTITY_FOR_ITEM")).html();
            var removeObjectTitle = parsed.html($translate.instant("REMOVE_OBJECT_TITLE_MSG")).html();
            var objectRemovedMsg = parsed.html($translate.instant("OBJECT_REMOVED_MSG_MESSAGE")).html();
            var removeObject = parsed.html($translate.instant("OBJECT_REMOVE_MSG")).html();
            var objectSavedMsg = parsed.html($translate.instant("OBJECT_SAVED_MSG")).html();


            vm.loadCustomObjectBom = loadCustomObjectBom;
            vm.addCustomObjects = addCustomObjects;
            vm.editObject = editObject;
            vm.save = save;
            vm.updateObject = updateObject;
            vm.cancelChanges = cancelChanges;
            vm.onCancel = onCancel;
            vm.saveAll = saveAll;
            vm.removeAll = removeAll;
            vm.removeCustomObjectBom = removeCustomObjectBom;
            vm.toggleNode = toggleNode;
            vm.expandAllBom = expandAllBom;
            vm.addedCustomObjects = [];
            vm.expandedAll = false;
            var lastSelectedItem = null;
            var emptyBomItem = {
                id: null,
                child: null,
                parent: null,
                quantity: null,
                notes: null,
                level: 0,
                expanded: false,
                editMode: true,
                isNew: true,
                bomChildren: [],
                sequence: null
            };

            function addCustomObjects(bomObject) {
                if (bomObject != undefined && bomObject != null) {
                    lastSelectedItem = bomObject;
                } else {
                    lastSelectedItem = null;
                }
                var options = {
                    title: "Add Objects",
                    template: 'app/desktop/modules/customObject/details/tabs/bom/customObjectSelectionView.jsp',
                    controller: 'CustomObjectSelectionController as customObjectSelectionVm',
                    resolve: 'app/desktop/modules/customObject/details/tabs/bom/customObjectSelectionController',
                    data: {
                        selectedCustomObjectBom: lastSelectedItem
                    },
                    width: 700,
                    showMask: true,
                    buttons: [
                        {text: "Add", broadcast: 'add.customobject.bom.objects'}
                    ],
                    callback: function (result) {
                        if (lastSelectedItem != null && lastSelectedItem.expanded == false) {
                            toggleNode(lastSelectedItem);
                        }

                        $timeout(function () {
                            addMultiple(result);
                        }, 1000)
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function addMultiple(result) {
                angular.forEach(result, function (object) {
                    vm.editModeItemsCount++;
                    var bomObject = angular.copy(emptyBomItem);
                    bomObject.quantity = 1;
                    bomObject.child = object.id;
                    bomObject.isNew = true;
                    bomObject.number = object.number;
                    bomObject.name = object.name;
                    bomObject.typeName = object.type.name;
                    bomObject.description = object.description;
                    if (lastSelectedItem != null) {
                        bomObject.parent = lastSelectedItem.child;
                        bomObject.parentBom = lastSelectedItem;
                        bomObject.level = lastSelectedItem.level + 1;
                    }
                    else {
                        bomObject.parent = vm.customId;
                        bomObject.parentBom = {
                            level: -1
                        }
                    }

                    if (lastSelectedItem != null) {
                        bomObject.parent = lastSelectedItem.child;
                        bomObject.parentBom = lastSelectedItem;
                        var parentIndex = vm.bomObjects.indexOf(lastSelectedItem);
                        if (lastSelectedItem.bomChildren == undefined) {
                            lastSelectedItem.bomChildren = [];
                        }

                        var index = parentIndex + getIndexTopInsertNewChild(lastSelectedItem) + 1;
                        lastSelectedItem.bomChildren.push(bomObject);
                        vm.addedCustomObjects.push(bomObject);
                        vm.bomObjects.splice(index, 0, bomObject);
                    }
                    else {
                        vm.addedCustomObjects.push(bomObject);
                        vm.bomObjects.push(bomObject);
                    }
                });
                $rootScope.hideSidePanel();
                $rootScope.hideBusyIndicator();
            }

            function getIndexTopInsertNewChild(bomObject) {
                var index = 0;

                if (bomObject.bomChildren != undefined && bomObject.bomChildren != null) {
                    index = bomObject.bomChildren.length;
                    angular.forEach(bomObject.bomChildren, function (child) {
                        var childCount = getIndexTopInsertNewChild(child);
                        index = index + childCount;
                    })
                }

                return index;
            }

            function save(object) {
                if (validateItem(object)) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    var parentBom = object.parentBom;
                    object.parentBom = null;
                    CustomObjectService.createCustomObjectBom(vm.customId, object).then(
                        function (data) {
                            object.editMode = false;
                            object.isNew = false;
                            object.id = data.id;
                            object.hasBom = data.hasBom;
                            object.parentBom = parentBom;
                            object.parentBom.hasBom = true;
                            object.parentBom.count = object.parentBom.count + 1;
                            vm.addedCustomObjects.splice(vm.addedCustomObjects.indexOf(object), 1);
                            checkExpandAll();
                            $rootScope.loadCustomObjectTabCount();
                            $rootScope.showSuccessMessage(objectSavedMsg);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function updateObject(object) {
                if (validateItem(object)) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    var parentBom = object.parentBom;
                    var children = object.children;
                    var bomChildren = object.bomChildren;
                    object.parentBom = null;
                    object.children = [];
                    object.bomChildren = [];
                    CustomObjectService.updateCustomObjectBom(vm.customId, object).then(
                        function (data) {
                            object.editMode = false;
                            object.id = data.id;
                            if (parentBom != null) {
                                parentBom.hasBom = true;
                                object.parentBom = parentBom;
                            }
                            object.children = children;
                            object.bomChildren = bomChildren;
                            $rootScope.showSuccessMessage(objectSavedMsg);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function saveAll() {
                if (validateItems()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    angular.forEach(vm.addedCustomObjects, function (object) {
                        object.parentBom = null;
                        object.children = [];
                        object.bomChildren = [];
                    })
                    CustomObjectService.createMultipleCustomObjectBom(vm.customId, vm.addedCustomObjects).then(
                        function (data) {
                            loadCustomObjectBom();
                            $rootScope.loadCustomObjectTabCount();
                            vm.addedCustomObjects = [];
                            $rootScope.showSuccessMessage(objectSavedMsg);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validateItem(object) {
                var valid = true;

                if (object.quantity == null || object.quantity == "" || object.quantity == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterPositiveQuantity);
                } else if (object.quantity != null && object.quantity < 0) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterPositiveQuantity);
                }

                return valid;
            }

            function validateItems() {
                var valid = true;
                angular.forEach(vm.addedCustomObjects, function (object) {
                    if (valid) {
                        if (object.quantity == null || object.quantity == "" || object.quantity == undefined) {
                            valid = false;
                            $rootScope.showWarningMessage(pleaseEnterQuantityForItem.format(object.number));
                        } else if (object.quantity != null && object.quantity < 0) {
                            valid = false;
                            $rootScope.showWarningMessage(pleaseEnterPositiveQuantityForItem.format(object.number));
                        }
                    }
                })
                return valid;
            }

            function removeAll() {
                angular.forEach(vm.addedCustomObjects, function (object) {
                    vm.bomObjects.splice(vm.bomObjects.indexOf(object), 1);
                })
                vm.addedCustomObjects = [];
            }

            function onCancel(object) {
                vm.bomObjects.splice(vm.bomObjects.indexOf(object), 1);
                vm.addedCustomObjects.splice(vm.addedCustomObjects.indexOf(object), 1);
            }

            function editObject(object) {
                object.editMode = true;
                object.oldQuantity = object.quantity;
                object.oldNotes = object.notes;
            }

            function cancelChanges(object) {
                object.editMode = false;
                object.quantity = object.oldQuantity;
                object.notes = object.oldNotes;
            }

            function removeCustomObjectBom(object) {
                var options = {
                    title: removeObject,
                    message: removeObjectTitle + " [ " + object.number + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        if (object.id != null && object.id != undefined) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            CustomObjectService.deleteCustomObjectBom(vm.customId, object.id).then(
                                function (data) {
                                    if (object.parentBom != undefined && object.parentBom != null && object.parentBom.bomChildren != undefined) {
                                        object.parentBom.bomChildren.splice(object.parentBom.bomChildren.indexOf(object), 1);
                                        object.parentBom.count = object.parentBom.count - 1;
                                        if (object.parentBom.bomChildren.length == 0) {
                                            object.parentBom.hasBom = false;
                                        }
                                        checkExpandAll();
                                    }
                                    $rootScope.loadCustomObjectTabCount();
                                    vm.bomObjects.splice(vm.bomObjects.indexOf(object), 1);
                                    $rootScope.showSuccessMessage(objectRemovedMsg);
                                    $rootScope.hideBusyIndicator();
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                });
            }

            function removeChildren(bomObject) {
                if (bomObject != null && bomObject.bomChildren != null && bomObject.bomChildren != undefined) {
                    angular.forEach(bomObject.bomChildren, function (object) {
                        removeChildren(object);
                    });

                    var index = vm.bomObjects.indexOf(bomObject);
                    vm.bomObjects.splice(index + 1, bomObject.bomChildren.length);
                    bomObject.bomChildren = [];
                    bomObject.expanded = false;

                }
            }

            function checkExpandAll() {
                var childCount = 0;
                var expandedCount = 0;
                angular.forEach(vm.bomObjects, function (object) {
                    if (object.level == 0) {
                        if (object.hasBom) {
                            childCount++;
                            vm.showExpandAll = true;
                            if (!object.expanded) {
                                vm.expandedAll = false;
                            } else {
                                expandedCount++;
                            }
                        }
                    }
                })
                if (childCount == expandedCount) {
                    vm.expandedAll = true;
                }
                if (childCount == 0) {
                    vm.showExpandAll = false;
                }
            }

            function toggleNode(bomObject) {
                bomObject.expanded = !bomObject.expanded;
                var index = vm.bomObjects.indexOf(bomObject);
                if (bomObject.expanded == false) {
                    removeChildren(bomObject);
                    vm.collapseAction = true;
                    vm.expandedAll = false;
                    checkExpandAll();
                }
                else {
                    if (bomObject.hasBom) {
                        vm.collapseAction = false;
                        CustomObjectService.getAllCustomObjectBom(bomObject.child, false).then(
                            function (data) {
                                bomObject.count = data.length;
                                angular.forEach(data, function (object) {
                                    object.parentBom = bomObject;
                                    object.expanded = false;
                                    object.editMode = false;
                                    object.isNew = false;
                                    object.level = bomObject.level + 1;
                                    object.bomChildren = [];
                                    bomObject.bomChildren.push(object);
                                });

                                angular.forEach(bomObject.bomChildren, function (object) {
                                    index = index + 1;
                                    vm.bomObjects.splice(index, 0, object);
                                });

                                vm.expandedAll = false;
                                checkExpandAll();
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function loadCustomObjectBom() {
                $rootScope.showBusyIndicator($('.view-container'));
                vm.loading = true;
                CustomObjectService.getAllCustomObjectBom(vm.customId, false).then(
                    function (data) {
                        angular.forEach(data, function (object) {
                            object.parentBom = null;
                            object.isNew = false;
                            object.editMode = false;
                            object.expanded = false;
                            object.level = 0;
                            if (object.hasBom) {
                                vm.showExpandAll = true;
                            }
                            object.bomChildren = [];
                        });
                        vm.bomObjects = data;
                        checkExpandAll();
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function expandAllBom() {
                vm.expandedAll = !vm.expandedAll;
                vm.bomObjects = [];
                vm.loading = true;
                if (!vm.expandedAll) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    loadCustomObjectBom();
                } else {
                    $rootScope.showBusyIndicator($('.view-container'));
                    CustomObjectService.getAllCustomObjectBom(vm.customId, true).then(
                        function (data) {
                            vm.bomObjects = [];
                            angular.forEach(data, function (object) {
                                object.parentBom = null;
                                object.isNew = false;
                                object.selected = false;
                                object.editMode = false;
                                object.expanded = true;
                                object.level = 0;
                                object.count = 0;
                                object.bomChildren = [];
                                vm.bomObjects.push(object);
                                var index = vm.bomObjects.indexOf(object);
                                index = populateChildren(object, index);
                            });
                            vm.loading = false;
                            $rootScope.hideBusyIndicator();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function populateChildren(bomObject, lastIndex) {
                angular.forEach(bomObject.children, function (object) {
                    lastIndex++;
                    object.parentBom = bomObject;
                    object.isNew = false;
                    object.expanded = true;
                    object.editMode = false;
                    object.level = bomObject.level + 1;
                    object.count = 0;
                    object.bomChildren = [];
                    vm.bomObjects.splice(lastIndex, 0, object);
                    bomObject.count = bomObject.count + 1;
                    bomObject.bomChildren.push(object);
                    lastIndex = populateChildren(object, lastIndex)
                });

                return lastIndex;
            }


            vm.showCustomObject = showCustomObject;

            function showCustomObject(object) {
                $state.go('app.customobjects.details', {customId: object.child, tab: 'details.basic'});
            }

            (function () {
                $scope.$on('app.customObj.tabActivated', function (event, data) {
                    if (data.tabId == 'details.bom') {
                        loadCustomObjectBom();
                    }
                });
            })();
        }
    }
);
