define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemBomService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/shared/services/core/recentlyVisitedService'
    ],
    function (module) {
        module.controller('ItemConfiguredController', ItemConfiguredController);

        function ItemConfiguredController($scope, $rootScope, $timeout, $state, $stateParams, $translate, $cookies, ItemService,
                                          ItemTypeService, DialogService, ItemBomService, RecentlyVisitedService) {
            var vm = this;

            vm.itemId = $stateParams.itemId;
            vm.items = [];
            vm.loading = true;
            var parsed = angular.element("<div></div>");
            var deleteDialogTitle = parsed.html($translate.instant("REMOVE_ITEM_DIALOG")).html();
            var deleteItemDialogMessage = parsed.html($translate.instant("REMOVE_ITEM_DIALOG_MESSAGE")).html();
            var itemDeletedMessage = parsed.html($translate.instant("ITEM_DELETED_MESSAGE")).html();
            var itemDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            vm.removeItem = parsed.html($translate.instant("REMOVE_ITEM_TITLE")).html();
            vm.save = parsed.html($translate.instant("SAVE")).html();
            vm.cancel = parsed.html($translate.instant("CANCEL")).html();
            vm.cancel = parsed.html($translate.instant("CANCEL")).html();
            vm.addInstance = parsed.html($translate.instant("ADD_INSTANCE")).html();
            vm.removeCombinations = parsed.html($translate.instant("REMOVE_COMBINATIONS")).html();
            var itemNumberValidation = parsed.html($translate.instant("ITEM_NUMBER_VALIDATION")).html();
            var itemNameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var attributeRequired = parsed.html($translate.instant("ATTRIBUTE_REQUIRED")).html();
            var instanceCreated = parsed.html($translate.instant("INSTANCE_CREATED")).html();
            var instancesCreated = parsed.html($translate.instant("INSTANCES_CREATED")).html();
            var noCombinationsAvailable = parsed.html($translate.instant("NO_COMBINATIONS_AVAILABLE")).html();
            var combinationsAvailable = parsed.html($translate.instant("COMBINATIONS_AVAILABLE")).html();
            $scope.saveCombination = parsed.html($translate.instant("SAVE_ITEM_COMBINATIONS")).html();

            function loadInstanceItems() {
                ItemService.getItemInstances($stateParams.itemId).then(
                    function (data) {
                        vm.items = data;
                        ItemService.getRevisionReferences(vm.items, 'id');
                        ItemService.getLatestRevisionReferences(vm.items, 'latestRevision');
                        vm.loading = false;
                        $timeout(function () {
                            loadItemAttributeValues();
                        }, 1000);

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadAttributeDefs() {
                vm.requiredAttributes = [];
                vm.attributes = [];
                ItemTypeService.getAttributesWithHierarchyFalse($rootScope.item.itemType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: null,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                stringValue: null,
                                mlistValue: [],
                                newListValue: null,
                                listValueEditMode: false,
                                timestampValue: null,
                                booleanValue: false,
                                dateValue: null,
                                timeValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.dataType == "LIST" && !attribute.listMultiple) {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.configurable == true) {
                                vm.attributes.push(att);
                            }

                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.itemIds = [];
            function loadItemAttributeValues() {
                angular.forEach(vm.items, function (item) {
                    vm.itemIds.push(item.id);
                    vm.itemIds.push(item.latestRevision);
                });
                vm.attributeIds = [];
                angular.forEach(vm.attributes, function (selectedAttribute) {
                    if (selectedAttribute.attributeDef.id != null && selectedAttribute.attributeDef.id != "" && selectedAttribute.attributeDef.id != 0) {
                        vm.attributeIds.push(selectedAttribute.attributeDef.id);
                    }
                });
                if (vm.itemIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.attributeIds).then(
                        function (data) {
                            if (data != null) {
                                vm.selectedObjectAttributes = data;

                                var map = new Hashtable();
                                angular.forEach(vm.attributes, function (att) {
                                    if (att.attributeDef.id != null && att.attributeDef.id != "" && att.attributeDef.id != 0) {
                                        map.put(att.attributeDef.id, att.attributeDef);
                                    }
                                });

                                angular.forEach(vm.items, function (item) {
                                    var attributes = [];
                                    var revisionAttributes = vm.selectedObjectAttributes[item.latestRevision];
                                    if (revisionAttributes != null && revisionAttributes != undefined) {
                                        attributes = attributes.concat(revisionAttributes);
                                    }
                                    var itemAttributes = vm.selectedObjectAttributes[item.id];
                                    if (itemAttributes != null && itemAttributes != undefined) {
                                        attributes = attributes.concat(itemAttributes);
                                    }
                                    angular.forEach(attributes, function (attribute) {
                                        var selectatt = map.get(attribute.id.attributeDef);
                                        if (selectatt != null) {
                                            var attributeName = selectatt.id;
                                            if (selectatt.dataType == 'TEXT') {
                                                item[attributeName] = attribute.stringValue;
                                            } else if (selectatt.dataType == 'LONGTEXT') {
                                                item[attributeName] = attribute.longTextValue;
                                            } else if (selectatt.dataType == 'RICHTEXT') {
                                                item[attributeName] = attribute;
                                            } else if (selectatt.dataType == 'INTEGER') {
                                                item[attributeName] = attribute.integerValue;
                                            } else if (selectatt.dataType == 'BOOLEAN') {
                                                item[attributeName] = attribute.booleanValue;
                                            } else if (selectatt.dataType == 'DOUBLE') {
                                                item[attributeName] = attribute.doubleValue;
                                            } else if (selectatt.dataType == 'HYPERLINK') {
                                                item[attributeName] = attribute.hyperLinkValue;
                                            } else if (selectatt.dataType == 'DATE') {
                                                item[attributeName] = attribute.dateValue;
                                            } else if (selectatt.dataType == 'LIST') {
                                                item[attributeName] = attribute.listValue;
                                            }

                                        }
                                    })
                                })
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
            }

            vm.addRow = false;
            vm.createInstanceItem = createInstanceItem;
            vm.itemInstances = [];
            $rootScope.combinations = [];
            function createInstanceItem() {
                vm.newItemInstance = {
                    id: null,
                    itemNumber: $rootScope.item.itemNumber + "-",
                    itemType: $rootScope.item.itemType,
                    itemName: $rootScope.item.itemName,
                    units: $rootScope.item.units,
                    description: $rootScope.item.description,
                    instance: $rootScope.item.id,
                    configured: true,
                    configurable: false,
                    itemAttributes: angular.copy(vm.attributes),
                    editMode: true,
                    isNew: true,
                    bomConfiguration: null
                };
                $rootScope.combinations.push(vm.newItemInstance);
            }

            vm.onSelectBomConfiguration = onSelectBomConfiguration;
            function onSelectBomConfiguration(bomConfiguration) {
                $rootScope.showBusyIndicator($('.view-content'));
                ItemBomService.getConfiguredAttributeValues(vm.itemId, bomConfiguration.id).then(
                    function (data) {
                        vm.configuredAttributes = [];
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: null,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: attribute.value,
                                stringValue: null,
                                mlistValue: [],
                                newListValue: null,
                                listValueEditMode: false,
                                timestampValue: null,
                                booleanValue: false,
                                dateValue: null,
                                timeValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }

                            vm.configuredAttributes.push(att);
                        });
                        vm.newItemInstance = {
                            id: null,
                            itemNumber: $rootScope.item.itemNumber + "-",
                            itemType: $rootScope.item.itemType,
                            itemName: $rootScope.item.itemName,
                            units: $rootScope.item.units,
                            description: $rootScope.item.description,
                            instance: $rootScope.item.id,
                            configured: true,
                            configurable: false,
                            itemAttributes: angular.copy(vm.configuredAttributes),
                            editMode: true,
                            isNew: false,
                            bomConfiguration: bomConfiguration.id,
                            configurationName: bomConfiguration.name
                        };
                        $rootScope.combinations.push(vm.newItemInstance);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.cancelRow = cancelRow;

            function cancelRow(instance) {
                $rootScope.combinations.splice($rootScope.combinations.indexOf(instance), 1);
            }

            vm.removeAllCombinations = removeAllCombinations;
            function removeAllCombinations() {
                $rootScope.combinations = [];
            }

            vm.create = create;
            function create(instance) {
                if (validateItem(instance)) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    instance.itemNumber = instance.itemNumber.trimLeft();
                    instance.itemNumber = instance.itemNumber.trimRight();
                    ItemBomService.createItemInstances($stateParams.itemId, instance).then(
                        function (data) {
                            $rootScope.combinations.splice($rootScope.combinations.indexOf(instance), 1);
                            loadInstanceItems();
                            loadAvailableBomConfigurations();
                            $rootScope.loadItemDetails();
                            $rootScope.showSuccessMessage(instanceCreated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validateItem(instance) {
                var valid = true;

                if (instance.itemNumber == null || instance.itemNumber == "" || instance.itemNumber == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(itemNumberValidation);
                } else if (instance.itemName == null || instance.itemName == "" || instance.itemName == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(itemNameValidation);
                } else {
                    angular.forEach(instance.itemAttributes, function (attribute) {
                        if (valid) {
                            if (attribute.listValue == null || attribute.listValue == "" || attribute.listValue == undefined) {
                                valid = false;
                                $rootScope.showWarningMessage(attribute.attributeDef.name + ":" + attributeRequired);
                            }
                        }
                    })
                }

                return valid;
            }

            $rootScope.createAllInstance = createAllInstance;
            function createAllInstance() {
                if ($rootScope.combinations.length > 0) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    ItemBomService.createAllInstance($stateParams.itemId, $rootScope.combinations).then(
                        function (data) {
                            $rootScope.combinations = [];
                            loadInstanceItems();
                            loadAvailableBomConfigurations();
                            $rootScope.loadItemDetails();
                            $rootScope.showSuccessMessage(instancesCreated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            $rootScope.getAllCombinations = getAllCombinations;

            function getAllCombinations() {
                $rootScope.showBusyIndicator($('.view-container'));
                ItemBomService.createAllCombination($rootScope.item.id).then(
                    function (data) {
                        $rootScope.combinations = data;
                        angular.forEach($rootScope.combinations, function (combination) {
                            combination.editMode = true;
                            combination.isNew = false;
                        })
                        $rootScope.hideBusyIndicator();
                        if ($rootScope.combinations.length == 0) {
                            $rootScope.showWarningMessage(noCombinationsAvailable);
                        } else {
                            $rootScope.showSuccessMessage($rootScope.combinations.length + " : " + combinationsAvailable);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    })
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
                        ItemService.deleteItem(item.id).then(
                            function (data) {
                                loadAvailableBomConfigurations();
                                $rootScope.loadItemDetails();
                                vm.items.splice(vm.items.indexOf(item), 1);
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

            function loadItem() {
                ItemService.getRevisionId(vm.itemId).then(
                    function (data) {
                        vm.itemRevision = data;
                        loadAvailableBomConfigurations();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            function loadAvailableBomConfigurations() {
                ItemBomService.getAvailableItemBomConfigurations(vm.itemId).then(
                    function (data) {
                        vm.bomConfigurations = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            vm.recentlyVisited = {
                id: null,
                objectId: null,
                objectType: null,
                person: null,
                visitedDate: null
            };

            vm.showItem = showItem;
            function showItem(item) {
                if (item.latestReleasedRevision == null) {
                    $rootScope.seletedItemId = item.id;
                }
                else {
                    $rootScope.seletedItemId = item.latestReleasedRevision;
                }
                $rootScope.selectedMasterItemId = null;
                var session = JSON.parse(localStorage.getItem('local_storage_login'));
                $rootScope.loginPersonDetails = session.login;
                $state.go('app.items.details', {itemId: item.latestRevision, tab: "details.bom"});
                vm.recentlyVisited.objectId = item.id;
                vm.recentlyVisited.objectType = item.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            (function () {
                $scope.$on('app.item.tabactivated', function (event, data) {
                    if (data.tabId == 'details.configured') {
                        if ($rootScope.selectedMasterItemId != null) {
                            $stateParams.itemId = $rootScope.selectedMasterItemId;
                            vm.itemId = $stateParams.itemId;
                        }
                        $rootScope.combinations = [];
                        loadItem();
                        loadAttributeDefs();
                        loadInstanceItems();
                    }
                });
            })();
        }
    }
)
;