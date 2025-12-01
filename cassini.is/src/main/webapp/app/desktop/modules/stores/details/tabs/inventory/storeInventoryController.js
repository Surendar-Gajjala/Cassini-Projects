define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/topStoreService',
        'app/shared/services/pm/project/bomService',
        'app/shared/services/store/topStockMovementService',
        'app/shared/services/store/topInventoryService',
        'app/shared/services/core/classificationService',
        "app/desktop/modules/proc/classification/directive/classificationTreeDirective",
        "app/desktop/modules/proc/classification/directive/classificationTreeController",
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('StoreInventoryController', StoreInventoryController);

        function StoreInventoryController($scope, $rootScope, TopStockMovementService, $stateParams, $state,
                                          BomService, TopStoreService, $timeout, ClassificationService, TopInventoryService, ItemService, AttributeAttachmentService) {
            var vm = this;

            vm.loading = true;
            $rootScope.storeInventoryList = [];
            vm.inventoryAttributes = [];
            var isSearchMode = false;
            vm.applyFilters = applyFilters;
            vm.resetPage = resetPage;
            vm.removeAttribute = removeAttribute;
            vm.openAttachment = openAttachment;
            vm.attributeFilter = attributeFilter;
            var attributeMap = new Hashtable();
            var currencyMap = new Hashtable();
            vm.attSearchValue = null;
            var nodeId = 0;
            var classificationTree1 = null;
            var rootNode = null;
            vm.itemType = null;
            vm.showImage = showImage;

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "item",
                    order: "ASC"
                }
            };
            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            $rootScope.storeInventoryList = pagedResults;

            vm.emptyFilters = {
                itemNumber: null,
                itemName: null,
                itemType: 0,
                itemDescription: null,
                units: null,
                storeId: $rootScope.storeId,
                searchAttributes: []
            };

            vm.filters = angular.copy(vm.emptyFilters);

            function applyFilters() {
                if (!isSearchMode) {
                    pageable.page = 0;
                }
                isSearchMode = true;
                if (vm.itemType != 0 && vm.itemType != null) {
                    vm.emptyFilters.itemType = vm.itemType.attributes.typeObject.id;
                }
                TopInventoryService.getInventoryByFilters($rootScope.storeId, pageable, vm.emptyFilters).then(
                    function (data) {
                        $rootScope.storeInventoryList = data;
                        loadAttributeValues();
                    }
                )
            }

            function showAttributes() {
                var options = {
                    title: 'Item Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.inventoryAttributes,
                        attributesMode: 'MATERIAL'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.inventoryAttributes = result;
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage("Attributes added successfully");
                        }
                        loadAttributeValues();
                        $rootScope.hideSidePanel();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeAttribute(att) {
                vm.inventoryAttributes.remove(att);
                var existingAttributeSearchDto = attributeMap.get(att.id);
                attributeMap.remove(att.id, existingAttributeSearchDto);
                var index = vm.emptyFilters.searchAttributes.indexOf(existingAttributeSearchDto);
                vm.emptyFilters.searchAttributes.splice(index, 1);
                applyFilters();
            }

            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }

            function initClassificationTree() {
                nodeId = 0;
                classificationTree1 = $('#classificationTree1').tree({
                    data: [
                        {
                            id: nodeId,
                            text: 'Classification',
                            iconCls: 'classification-root',
                            attributes: {
                                typeObject: null,
                                nodeType: 'ROOT'
                            },
                            children: []
                        }
                    ],
                    onSelect: onSelectType1
                });

                rootNode = classificationTree1.tree('find', 0);
                if (rootNode != null) {
                    loadMaterialClassification();
                }
            }

            function loadMaterialClassification() {
                var materialRoot = {
                    id: ++nodeId,
                    text: 'Material',
                    iconCls: 'material-node',
                    attributes: {
                        root: true,
                        nodeType: 'MATERIALTYPE'
                    },
                    children: []
                };

                ClassificationService.getClassificationTree('MATERIALTYPE').then(
                    function (data) {
                        if (data.length > 0) {
                            materialRoot.state = 'closed'
                        }
                        var nodes = [];
                        angular.forEach(data, function (materialType) {
                            var node = makeMaterialNode(materialType);

                            if (materialType.children != null && materialType.children != undefined && materialType.children.length > 0) {
                                node.state = "closed";
                                visitChildren(node, materialType.children);
                            }

                            nodes.push(node);
                        });

                        materialRoot.children = nodes;

                        classificationTree1.tree('append', {
                            parent: rootNode.target,
                            data: materialRoot
                        });
                    }
                )
            }

            function visitChildren(parent, itemTypes) {
                var nodes = [];
                angular.forEach(itemTypes, function (itemType) {
                    var node = makeMaterialNode(itemType);

                    if (itemType.children != null && itemType.children != undefined && itemType.children.length > 0) {
                        node.state = 'closed';
                        visitChildren(node, itemType.children);
                    }

                    nodes.push(node);
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
            }

            function makeMaterialNode(materialType) {
                return {
                    id: ++nodeId,
                    text: materialType.name,
                    iconCls: 'material-node',
                    attributes: {
                        typeObject: materialType,
                        nodeType: 'MATERIALTYPE'
                    }
                };
            }

            function makeMachineNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'machine-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'MACHINETYPE'
                    }
                };
            }

            function onSelectType1(itemType) {
                var data = itemType.attributes.typeObject.children;
                $rootScope.closeNotification();
                vm.itemType = itemType;
                window.$("body").trigger("click");
                applyFilters();
            }

            function resetPage() {
                vm.itemType = 0;
                isSearchMode = false;
                $rootScope.storeInventoryList = [];
                pageable.page = 0;
                $rootScope.showSearch = false;
                $rootScope.searchModeType = false;
                vm.emptyFilters = {
                    itemNumber: null,
                    itemName: null,
                    itemType: 0,
                    itemDescription: null,
                    units: null,
                    storeId: $rootScope.storeId,
                    searchAttributes: []
                };
                vm.attSearchValue = null;
                angular.forEach(vm.inventoryAttributes, function (attribute) {
                    attribute.textValue = null;
                    attribute.integerValue = null;
                    attribute.doubleValue = null;
                    attribute.dateValue = null;
                    attribute.listValue = null;
                    attribute.booleanValue = null;
                    attribute.currencyValue = null;
                })
                loadStoreInventory();
            }

            var searchMode = null;
            var freeTextQuery = null;

            function freeTextSearch(event, args) {
                searchMode = "freetext";
                freeTextQuery = args.search;
                pageable.page = 0;
                if (args.search != null && args.search != undefined && args.search.trim() != "") {
                    $rootScope.storeInventoryList = [];
                    vm.loading = true;
                    TopStoreService.storeInventoryFreeTextSearch($stateParams.storeId, pageable, args.search).then(
                        function (data) {
                            $rootScope.storeInventoryList = data;
                            vm.clear = true;
                            vm.loading = false;
                            $rootScope.showSearch = true;
                            $rootScope.searchModeType = true;
                            BomService.getBoqItemReferences($stateParams.projectId, $rootScope.storeInventoryList, 'boqItem');
                            angular.forEach($rootScope.storeInventoryList, function (inventory) {
                                inventory.issuedQuantity = 0;
                                inventory.receivedQuantity = 0;
                                TopStockMovementService.getStockMovementByStoreAndItem($stateParams.storeId, inventory.boqItem).then(
                                    function (stockMovements) {
                                        angular.forEach(stockMovements, function (stockMovement) {
                                            if (stockMovement.movementType == "ISSUED") {
                                                inventory.issuedQuantity = inventory.issuedQuantity + stockMovement.quantity;
                                            }
                                            else {
                                                inventory.receivedQuantity = inventory.receivedQuantity + stockMovement.quantity;
                                            }
                                        })
                                    }
                                )
                            })

                        }
                    )
                }
                else {
                    vm.resetPage();
                    loadStoreInventory();
                }
            }

            function loadStoreInventory() {
                vm.loading = true;
                isSearchMode = false;
                TopStoreService.getStoreInventory(pageable, $stateParams.storeId).then(
                    function (data) {
                        vm.loading = false;
                        $rootScope.storeInventoryList = data;
                        loadAttributeValues();
                    }
                );
            }

            function showImage(attribute) {

                var modal = document.getElementById('invModal');
                var modalImg = document.getElementById('invImg');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImageInv")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function loadStoreInventoryByFilters() {
                vm.loading = true;
                TopStoreService.getStoreInventoryByStoreIdAndFilters($stateParams.storeId, vm.filters, pageable).then(
                    function (data) {
                        vm.loading = false;
                        $rootScope.storeInventoryList = data;
                        BomService.getBoqItemReferences($stateParams.projectId, $rootScope.storeInventoryList, 'boqItem');
                        angular.forEach($rootScope.storeInventoryList, function (inventory) {
                            inventory.issuedQuantity = 0;
                            inventory.receivedQuantity = 0;
                            TopStockMovementService.getStockMovementByStoreAndItem($stateParams.storeId, inventory.boqItem).then(
                                function (stockMovements) {
                                    angular.forEach(stockMovements, function (stockMovement) {
                                        if (stockMovement.movementType == "ISSUED") {
                                            inventory.issuedQuantity = inventory.issuedQuantity + stockMovement.quantity;
                                        }
                                        else {
                                            inventory.receivedQuantity = inventory.receivedQuantity + stockMovement.quantity;
                                        }
                                    })
                                }
                            )
                        });
                    }
                )
            }

            function nextPage() {
                if ($rootScope.storeInventoryList.last != true) {
                    pageable.page++;
                    if (isSearchMode) {
                        applyFilters();
                    }
                    else {
                        loadStoreInventory();
                    }
                }
            }

            function previousPage() {
                if ($rootScope.storeInventoryList.first != true) {
                    pageable.page--;
                    if (isSearchMode) {
                        applyFilters();
                    }
                    else {
                        loadStoreInventory();
                    }
                }
            }

            function loadAttributeValues() {
                vm.itemIds = [];
                vm.attributeIds = [];
                angular.forEach($rootScope.storeInventoryList.content, function (item) {
                    vm.itemIds.push(item.stockMovementDTO.itemDTO.id);
                });
                angular.forEach(vm.inventoryAttributes, function (inventoryAttribute) {
                    if (inventoryAttribute.id != null && inventoryAttribute.id != "" && inventoryAttribute.id != 0) {
                        vm.attributeIds.push(inventoryAttribute.id);
                    }
                });

                if (vm.itemIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;
                            var map = new Hashtable();
                            angular.forEach(vm.inventoryAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach($rootScope.storeInventoryList.content, function (item) {
                                var attributes = [];

                                var itemAttributes = vm.selectedObjectAttributes[item.stockMovementDTO.itemDTO.id];
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
                                                        vm.taskAttachments = data;
                                                        item[attributeName] = vm.taskAttachments;
                                                    }
                                                )
                                            }
                                        } else if (selectatt.dataType == 'IMAGE') {
                                            if (attribute.imageValue != null) {
                                                item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                            }
                                        } else if (selectatt.dataType == 'OBJECT') {
                                            if (attribute.refValue != null) {
                                                var objectSelector = $application.getObjectSelector(selectatt.refType);
                                                if (objectSelector != null && attribute.refValue != null) {
                                                    objectSelector.getDetails(attribute.refValue, item, attributeName);
                                                }
                                            }
                                        }
                                    }
                                })
                            })

                        }
                    );
                }
            }

            function attributeFilter(selectatt) {
                var searchString = null;
                vm.attributeSearchDto = {
                    objectTypeAttribute: null,
                    text: null,
                    longText: null,
                    integer: null,
                    list: null,
                    aBoolean: null,
                    booleanSearch: false,
                    aDouble: null,
                    doubleSearch: false,
                    currency: null,
                    date: null,
                    time: null,
                    mListValue: []
                };
                if (selectatt.dataType == 'TEXT') {
                    vm.attributeSearchDto.text = selectatt.textValue;
                    searchString = selectatt.textValue;
                } else if (selectatt.dataType == 'INTEGER') {
                    vm.attributeSearchDto.integer = selectatt.integerValue;
                    searchString = selectatt.integerValue;
                } else if (selectatt.dataType == 'BOOLEAN') {
                    vm.attributeSearchDto.aBoolean = selectatt.booleanValue;
                    searchString = selectatt.booleanValue;
                } else if (selectatt.dataType == 'DOUBLE') {
                    vm.attributeSearchDto.aDouble = selectatt.doubleValue;
                    searchString = selectatt.doubleValue;
                } else if (selectatt.dataType == 'DATE') {
                    vm.attributeSearchDto.date = selectatt.dateValue;
                    searchString = selectatt.dateValue;
                } else if (selectatt.dataType == 'LIST') {
                    vm.attributeSearchDto.list = selectatt.listValue;
                    searchString = selectatt.listValue;
                } else if (selectatt.dataType == 'TIME') {
                    vm.attributeSearchDto.time = selectatt.timeValue;
                    searchString = selectatt.timeValue;
                } else if (selectatt.dataType == 'CURRENCY') {
                    vm.attributeSearchDto.currency = selectatt.currencyValue;
                    searchString = selectatt.currencyValue;
                } else if (selectatt.dataType == 'LONGTEXT') {
                    vm.attributeSearchDto.longText = selectatt.longTextValue;
                    searchString = selectatt.longTextValue;
                }
                var existingAttributeSearchDto = attributeMap.get(selectatt.id);
                if (existingAttributeSearchDto == null) {
                    vm.attributeSearchDto.objectTypeAttribute = selectatt;
                    attributeMap.put(selectatt.id, vm.attributeSearchDto);
                    vm.emptyFilters.searchAttributes.push(vm.attributeSearchDto);
                } else {
                    attributeMap.remove(selectatt.id, existingAttributeSearchDto);
                    var index = vm.emptyFilters.searchAttributes.indexOf(existingAttributeSearchDto);
                    vm.emptyFilters.searchAttributes.splice(index, 1);
                    if (searchString != null && searchString != "") {
                        vm.attributeSearchDto.objectTypeAttribute = selectatt;
                        vm.emptyFilters.searchAttributes.push(vm.attributeSearchDto);
                        attributeMap.put(selectatt.id, vm.attributeSearchDto);
                    }
                }
                applyFilters();
            }

            (function () {
                if ($application.homeLoaded == true) {

                    $scope.$on('app.store.tabactivated', function (event, data) {
                        if (data.tabId == 'details.inventory') {
                            loadStoreInventory();
                            $timeout(function () {
                                classificationTree1 = null;
                                initClassificationTree();
                            }, 1000);

                        }
                    });
                    $scope.$on('app.stores.inventory.freeText', freeTextSearch);
                    $scope.$on('app.stores.inventory.reset', freeTextSearch);
                    $scope.$on('app.stores.inventory.nextPageDetails', nextPage);
                    $scope.$on('app.stores.inventory.previousPageDetails', previousPage);
                    $scope.$on('app.store.inventory.attributes', showAttributes);
                }
            })();
        }
    }
);