define(['app/desktop/modules/im/im.module',
        'app/desktop/modules/im/stores/all/newStoreDialogController',
        'app/shared/services/core/storeService',
        'app/shared/services/pm/project/projectSiteService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('StoresAllController', StoresAllController);

        function StoresAllController($scope, $rootScope, $timeout, $state, $q, $stateParams, $cookies, $uibModal, StoreService, ProjectSiteService, DialogService) {
            var vm = this;
            $rootScope.viewInfo.icon = "fa fa-shopping-cart";
            $rootScope.viewInfo.title = "Stores";

            vm.storeList = [];
            vm.sites = [];
            vm.loading = true;
            vm.project = $stateParams.projectId;

            vm.showStore = showStore;
            vm.newStore = newStore;
            vm.loadStores = loadStores;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.deleteStore = deleteStore;
            vm.editStore = editStore;
            vm.cancelChanges = cancelChanges;
            vm.applyChanges = applyChanges;
            vm.showValues = true;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.clearFilter = clearFilter;
            vm.showSearchMode = false;
            $scope.freeTextQuery = null;

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.emptyFilters = {
                name: null,
                description: null,
                searchQuery: null
            };

            vm.filters = angular.copy(vm.emptyFilters);

            function showStore(store) {
                $rootScope.storeId = store.id;
                $state.go('app.pm.project.stores.details', {storeId: store.id});
            }

            function newStore() {
                var options = {
                    title: 'New Store',
                    showMask: true,
                    template: 'app/desktop/modules/im/stores/all/newStoreDialog.jsp',
                    controller: 'NewStoreDialogController as newStoreVm',
                    resolve: 'app/desktop/modules/im/stores/all/newStoreDialogController',
                    width: 500,
                    data: {
                        storeType: 'ProjectStore'
                    },
                    buttons: [
                        {text: 'Create', broadcast: 'app.store.new'}
                    ],
                    callback: function () {
                        loadStores();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function freeTextSearch(freeText) {
                $scope.freeTextQuery = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.filters.searchQuery = freeText;
                    StoreService.freeTextSearch($stateParams.projectId, vm.pageable, vm.filters).then(
                        function (data) {
                            vm.storeList = data;
                            vm.showSearchMode = true;
                            angular.forEach(vm.storeList.content, function (store) {
                                store.editMode = false;
                                store.showValues = true;
                                store.newDescription = store.description;
                            });
                            vm.clear = true;
                        }
                    );
                } else {
                    resetPage();
                    loadStores();
                }
            }

            function resetPage() {
                vm.pageable.page = 0;
                vm.showSearchMode = false;
            }

            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                vm.requiredAttributeIds = [];
                angular.forEach(vm.storeList.content, function (obj) {
                    vm.objectIds.push(obj.id);
                });
                angular.forEach(vm.objectAttributes, function (objAttr) {
                    if (objAttr.id != null && objAttr.id != "" && objAttr.id != 0) {
                        vm.attributeIds.push(objAttr.id);
                    }
                });

                angular.forEach(vm.requiredObjectAttributes, function (reqAttr) {
                    if (reqAttr.id != null && reqAttr.id != "" && reqAttr.id != 0) {
                        vm.requiredAttributeIds.push(reqAttr.id);
                    }
                });

                if (vm.objectIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByObjectIdsAndAttributeIds(vm.objectIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;
                            var map = new Hashtable();
                            angular.forEach(vm.objectAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.storeList.content, function (item) {
                                var attributes = [];

                                var itemAttributes = vm.selectedObjectAttributes[item.id];
                                if (itemAttributes != null && itemAttributes != undefined) {
                                    attributes = attributes.concat(itemAttributes);
                                }
                                angular.forEach(attributes, function (attribute) {
                                    var selectatt = map.get(attribute.id.attributeDef);
                                    if (selectatt != null) {
                                        var attributeName = selectatt.name;
                                        if (selectatt.dataType == 'TEXT') {
                                            item[attributeName] = attribute.stringValue;
                                        } else if (selectatt.dataType == 'INTEGER') {
                                            item[attributeName] = attribute.integerValue;
                                        } else if (selectatt.dataType == 'BOOLEAN') {
                                            item[attributeName] = attribute.booleanValue;
                                        } else if (selectatt.dataType == 'DOUBLE') {
                                            item[attributeName] = attribute.doubleValue;
                                        } else if (selectatt.dataType == 'DATE') {
                                            item[attributeName] = attribute.dateValue;
                                        } else if (selectatt.dataType == 'LIST') {
                                            item[attributeName] = attribute.listValue;
                                        } else if (selectatt.dataType == 'TIME') {
                                            item[attributeName] = attribute.timeValue;
                                        } else if (selectatt.dataType == 'TIMESTAMP') {
                                            item[attributeName] = attribute.timestampValue;
                                        } else if (selectatt.dataType == 'CURRENCY') {
                                            item[attributeName] = attribute.currencyValue;
                                            if (attribute.currencyType != null) {
                                                item[attributeName + 'type'] = currencyMap.get(attribute.currencyType);
                                            }
                                        } else if (selectatt.dataType == 'ATTACHMENT') {
                                            var attachmentIds = [];
                                            if (attribute.attachmentValues.length > 0) {
                                                angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                    attachmentIds.push(attachmentId);
                                                });
                                                AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                                    function (data) {
                                                        vm.objectAttachments = data;
                                                        item[attributeName] = vm.objectAttachments;
                                                    }
                                                )
                                            }
                                        } else if (selectatt.dataType == 'IMAGE') {
                                            if (attribute.imageValue != null) {
                                                item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                            }
                                        }
                                    }
                                })
                            })

                        }, function (error) {

                        }
                    );
                }

                if (vm.objectIds.length > 0 && vm.requiredAttributeIds.length > 0) {
                    ItemService.getAttributesByObjectIdsAndAttributeIds(vm.objectIds, vm.requiredAttributeIds).then(
                        function (data) {
                            vm.requiredAttributes = data;
                            var map = new Hashtable();
                            angular.forEach(vm.requiredObjectAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.storeList.content, function (item) {
                                var attributes = [];

                                var objectAttributes = vm.requiredAttributes[item.id];
                                if (objectAttributes != null && objectAttributes != undefined) {
                                    attributes = attributes.concat(objectAttributes);
                                }
                                angular.forEach(attributes, function (attribute) {
                                    var selectatt = map.get(attribute.id.attributeDef);
                                    if (selectatt != null) {
                                        var attributeName = selectatt.name;
                                        if (selectatt.dataType == 'TEXT') {
                                            item[attributeName] = attribute.stringValue;
                                        } else if (selectatt.dataType == 'INTEGER') {
                                            item[attributeName] = attribute.integerValue;
                                        } else if (selectatt.dataType == 'BOOLEAN') {
                                            item[attributeName] = attribute.booleanValue;
                                        } else if (selectatt.dataType == 'DOUBLE') {
                                            item[attributeName] = attribute.doubleValue;
                                        } else if (selectatt.dataType == 'DATE') {
                                            item[attributeName] = attribute.dateValue;
                                        } else if (selectatt.dataType == 'LIST') {
                                            item[attributeName] = attribute.listValue;
                                        } else if (selectatt.dataType == 'TIME') {
                                            item[attributeName] = attribute.timeValue;
                                        } else if (selectatt.dataType == 'TIMESTAMP') {
                                            item[attributeName] = attribute.timestampValue;
                                        } else if (selectatt.dataType == 'CURRENCY') {
                                            item[attributeName] = attribute.currencyValue;
                                            if (attribute.currencyType != null) {
                                                item[attributeName + 'type'] = currencyMap.get(attribute.currencyType);
                                            }
                                        } else if (selectatt.dataType == 'ATTACHMENT') {
                                            var attachmentIds = [];
                                            if (attribute.attachmentValues.length > 0) {
                                                angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                    attachmentIds.push(attachmentId);
                                                });
                                                AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                                    function (data) {
                                                        vm.requiredObjectAttachments = data;
                                                        item[attributeName] = vm.requiredObjectAttachments;
                                                    }
                                                )
                                            }
                                        } else if (selectatt.dataType == 'IMAGE') {
                                            if (attribute.imageValue != null) {
                                                item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                            }
                                        }
                                    }
                                })
                            })

                        }, function (error) {

                        }
                    );
                }

            }

            function showAttributes() {
                var options = {
                    title: 'Store Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.objectAttributes,
                        attributesMode: 'STORE'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.objectAttributes = result;
                        $window.localStorage.setItem("objectAttributes", JSON.stringify(vm.objectAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage("Attributes added successfully");
                        }
                        loadAttributeValues();
                        loadAttributes();
                        $rootScope.hideSidePanel('right');
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.objectAttributes.remove(att);
            }

            function loadStores() {
                StoreService.getStores($stateParams.projectId, vm.pageable).then(
                    function (data) {
                        vm.loading = false;
                        vm.storeList = data;
                        angular.forEach(vm.storeList.content, function (store) {
                            store.editMode = false;
                            store.showValues = true;
                            store.newDescription = store.description;
                        });
                        vm.showValues = true;
                        loadAttributeValues();
                    }
                );
            }

            function clearFilter() {
                loadStores();
                vm.clear = false;
            }

            function applyChanges(store) {
                var promise = null;
                if (store.id == null || store.id == undefined) {
                    store.description = store.newDescription;
                    promise = StoreService.createStore($stateParams.projectId, store);
                }
                else {
                    store.description = store.newDescription;
                    promise = StoreService.updateStore($stateParams.projectId, store);
                }

                promise.then(
                    function (data) {
                        store.id = data.id;
                        store.editMode = false;
                        $timeout(function () {
                            store.showValues = true;
                        }, 300);
                        $rootScope.showSuccessMessage("Store updated successfully!");
                    }
                )

            }

            function cancelChanges(store) {
                var promise = null;
                if (store.id == null || store.id == undefined) {
                    vm.storeList.splice(vm.storeList.indexOf(store), 1);
                }
                else {
                    store.newDescription = store.description;
                    promise = StoreService.updateStore($stateParams.projectId, store);
                    store.editMode = false;
                    $timeout(function () {
                        store.showValues = true;
                    }, 300);
                }
            }

            function editStore(store) {
                store.showValues = false;
                store.editMode = true;
                $timeout(function () {
                }, 5000);
            }

            function deleteStore(store) {
                isSitesExistsInStore(store).then(
                    function (success) {
                        $rootScope.showErrorMessage("This Store is assigned to Site we can't delete");
                    },
                    function (fail) {
                        var options = {
                            title: 'Delete Store',
                            message: 'Are you sure you want to delete this Store?',
                            okButtonClass: 'btn-danger'
                        };
                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                StoreService.deleteStore($stateParams.projectId, store.id).then(
                                    function (data) {
                                        var index = vm.storeList.content.indexOf(store);
                                        vm.storeList.content.splice(index, 1);
                                        $rootScope.showErrorMessage("Store deleted successfully!");
                                        loadStores();
                                    }
                                )
                            }

                        });
                    });
            }

            function isSitesExistsInStore(store) {
                var defered = $q.defer();
                StoreService.getsitesByStore($stateParams.projectId, store.id).then(
                    function (data) {
                        if (data.length != 0) {
                            defered.resolve(true);
                        } else {
                            defered.reject(false);
                        }
                    });
                return defered.promise;
            }

            function nextPage() {
                vm.pageable.page++;
                loadStores();
            };

            function previousPage() {
                vm.pageable.page--;
                loadStores();
            };

            function loadAttributes() {
                ItemService.getTypeAttributesRequiredTrue("STORE").then(
                    function (data) {
                        vm.requiredObjectAttributes = data;
                        loadStores();
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadAttributes();
                    $scope.$on('app.store.storeAttributes', function () {
                        showAttributes();
                    })
                }
            })();
        }
    }
)
;