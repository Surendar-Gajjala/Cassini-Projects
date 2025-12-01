define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/topStockReceivedService',
        'app/shared/services/core/itemService',
        'app/shared/services/store/topStoreService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/supplier/supplierService',
        'app/shared/services/store/customPurchaseOrderService',
        'app/shared/services/core/classificationService'

    ],
    function (module) {
        module.controller('StoreStockReceivedController', StoreStockReceivedController);

        function StoreStockReceivedController($scope, $rootScope, $stateParams, AttributeAttachmentService, $timeout, $window, $state, ObjectAttributeService, ProjectService,
                                              TopStoreService, TopStockReceivedService, ItemService, $sce, CustomPurchaseOrderService, SupplierService, ClassificationService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-shopping-cart";
            vm.stockReceives = [];
            $rootScope.stockReceiveList = [];
            var filterType = false;
            vm.newReceive = newReceive;
            vm.back = back;
            vm.showDetails = showDetails;
            vm.objectIds = [];
            vm.attributeIds = [];
            vm.showReceiveAttributes = showReceiveAttributes;
            vm.attributeFilter = attributeFilter;
            vm.openAttachment = openAttachment;
            var currencyMap = new Hashtable();
            $rootScope.stockReceiveList = [];
            var attributeMap = new Hashtable();
            vm.removeAttribute = removeAttribute;
            $rootScope.loadProperties = loadProperties;
            vm.showImage = showImage;
            vm.applyFilters = applyFilters;
            vm.resetPage = resetPage;
            vm.onReceiveTypeSelected = onReceiveTypeSelected;
            var nodeId = 0;
            var classificationTree1 = null;
            var rootNode = null;
            vm.itemType = null;
            vm.emptyFilters = {
                receiveNumberSource: null,
                storeId: $rootScope.storeId,
                searchAttributes: [],
                notes: null,
                materialReceiveType: null,
                attributeSearch: true,
                project: null,
                supplier: null
            };

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "createdDate",
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

            $rootScope.stockReceiveList = pagedResults;
            vm.stockReceives = pagedResults;

            function showImage(attribute) {
                var modal = document.getElementById('stockRecModal');
                var modalImg = document.getElementById('stockRecImg');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage10")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function showReceiveAttributes() {
                var options = {
                    title: 'Receive Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.receiveAttributes,
                        attributesMode: 'RECEIVE'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.receiveAttributes = result;
                        loadReceiveAttributeValues();
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage("Attributes added successfully");
                        }
                        $rootScope.hideSidePanel('right');
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadStockReceives() {
                rootNode = null;
                TopStockReceivedService.getStockReceivedByAttributes($rootScope.storeId, pageable, vm.emptyFilters).then(
                    function (data) {
                        vm.loading = false;
                        vm.stockReceives = data;
                        if ($stateParams.mode == "RECEIVE") {
                            $rootScope.viewInfo.title = "Receives";
                        }
                        $rootScope.stockReceiveList = data;
                        ProjectService.getProjectReferences(vm.stockReceives.content, 'project');
                        CustomPurchaseOrderService.getPurchaseOrderReferences(vm.stockReceives.content, 'purchaseOrderNumber');
                        SupplierService.getSupplierReferences(vm.stockReceives.content, 'supplier');
                        loadRequiredReceiveAttributes();
                    }
                );
            }

            function removeAttribute(att) {
                vm.receiveAttributes.remove(att);
                var existingAttributeSearchDto = attributeMap.get(att.id);
                attributeMap.remove(att.id, existingAttributeSearchDto);
                var index = vm.emptyFilters.searchAttributes.indexOf(existingAttributeSearchDto);
                vm.emptyFilters.searchAttributes.splice(index, 1);
                applyFilters();
            }

            function applyFilters() {
                pageable.page = 0;
                loadStockReceives();
            }

            function loadRequiredReceiveAttributes() {
                ItemService.getTypeAttributesRequiredTrue("RECEIVE").then(
                    function (data) {
                        vm.receiveAttributes = data;
                        loadReceiveAttributeValues();
                    }, function (error) {

                    }
                )
            }

            function loadReceiveAttributeValues() {
                vm.itemIds = [];
                vm.attributeIds = [];
                vm.requiredAttributeIds = [];
                angular.forEach(vm.stockReceives.content, function (item) {
                    item.refValueString = null;
                    vm.itemIds.push(item.id);
                });
                angular.forEach(vm.receiveAttributes, function (receiveAttribute) {
                    if (receiveAttribute.id != null && receiveAttribute.id != "" && receiveAttribute.id != 0) {
                        vm.attributeIds.push(receiveAttribute.id);
                    }
                });

                if (vm.itemIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.receiveAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.stockReceives.content, function (item) {
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
                                                        vm.receiveAttachments = data;
                                                        item[attributeName] = vm.receiveAttachments;
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

            function loadStore() {
                TopStoreService.getTopStore($rootScope.storeId).then(
                    function (data) {
                        $rootScope.viewInfo.description = "Store : " + data.storeName;
                        $timeout(function () {
                            classificationTree1 = null;
                            initReceiveTypeTree();
                        }, 1000);
                    }
                )
            }

            function loadProperties() {
                loadReceivedProperties();
            }

            function loadReceivedProperties() {
                TopStoreService.getReceivedItemProperties("RECEIVE").then(
                    function (data) {
                        vm.receivedItemsProperties = data;
                        angular.forEach(vm.receivedItemsProperties, function (attribute) {
                            vm.attributeIds.push(attribute.id);
                            attribute.editMode = false;
                            attribute.stringValue = null;
                            attribute.integerValue = null;
                            attribute.doubleValue = null;
                        });
                        loadStockReceives();
                    }
                )
            }

            function showDetails(item) {
                $rootScope.receiveId = item.id;
                $state.go('app.store.stock.receiveDetails', {receiveId: item.id});
            }

            function newReceive() {
                var options = {
                    title: 'New StockReceive',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/receives/new/newStockReceiveDialogView.jsp',
                    controller: 'NewStockReceiveDialogController as stockReceiveVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/receives/new/newStockReceiveDialogController',
                    width: 500,
                    data: {},
                    buttons: [
                        {text: 'Add', broadcast: 'app.storeItems.receive'}
                    ],
                    callback: function () {
                        loadStockReceives();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }

            function back() {
                $window.history.back();
            }

            function nextPage() {
                if (vm.stockReceives.last != true) {
                    pageable.page++;
                    loadStockReceives();
                }
            }

            function previousPage() {
                if (vm.stockReceives.first != true) {
                    pageable.page--;
                    loadStockReceives();
                }
            }

            function loadCurrencies() {
                ObjectAttributeService.getCurrencies().then(
                    function (data) {
                        vm.currencies = data;
                        angular.forEach(vm.currencies, function (currency) {
                            currencyMap.put(currency.id, $sce.trustAsHtml(currency.symbol));
                        });
                    }
                );
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

            function resetPage() {
                vm.emptyFilters = {
                    receiveNumberSource: null,
                    storeId: $rootScope.storeId,
                    searchAttributes: [],
                    notes: null,
                    materialReceiveType: null,
                    attributeSearch: true
                };
                vm.itemType = null;
                angular.forEach(vm.receiveAttributes, function (attribute) {
                    attribute.textValue = null;
                    attribute.integerValue = null;
                    attribute.booleanValue = null;
                    attribute.currencyValue = null;
                    attribute.longTextValue = null
                });
                applyFilters();
            }

            function onReceiveTypeSelected(receiveType) {
                vm.emptyFilters.materialReceiveType = receiveType.id;
            }

            function initReceiveTypeTree() {
                nodeId = 0;
                classificationTree1 = $('#receiveTypeTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: 'ReceiveType',
                            iconCls: 'itemtype-node',
                            attributes: {
                                itemType: null
                            },
                            children: []
                        }
                    ],
                    onSelect: onSelectType1
                });

                rootNode = classificationTree1.tree('find', 0);
                loadReceiveTypeTree();
            }

            function loadReceiveTypeTree() {
                ClassificationService.getClassificationTree('MATERIALRECEIVETYPE').then(
                    function (data) {
                        var nodes = [];
                        angular.forEach(data, function (itemType) {
                            var node = makeNode(itemType);

                            if (itemType.children != null && itemType.children != undefined && itemType.children.length > 0) {
                                node.state = "closed";
                                visitChildren(node, itemType.children);
                            }

                            nodes.push(node);
                        });

                        classificationTree1.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });
                    }
                )
            }

            function visitChildren(parent, itemTypes) {
                var nodes = [];
                angular.forEach(itemTypes, function (itemType) {
                    var node = makeNode(itemType);

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

            function makeNode(itemType) {
                return {
                    id: ++nodeId,
                    text: itemType.name,
                    iconCls: 'itemtype-node',
                    attributes: {
                        itemType: itemType
                    }
                };
            }

            function onSelectType1(itemType) {
                pageable.page = 0;
                $rootScope.closeNotification();
                vm.itemType = itemType;
                window.$("body").trigger("click");
                vm.emptyFilters.materialReceiveType = itemType.attributes.itemType.id;
                loadStockReceives();
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadCurrencies();
                    $scope.$on('app.store.tabactivated', function (event, data) {
                        if (data.tabId == 'details.stockReceives') {
                            pageable.page = 0;
                            loadStockReceives();
                            loadStore();
                        }
                    });
                    $scope.$on('app.Store.receive.attributes', function () {
                        showReceiveAttributes();
                    });
                    if ($stateParams.mode == "RECEIVE") {
                        loadStockReceives();
                        loadStore();
                    }
                    $scope.$on('app.stores.receives.nextPageDetails', nextPage);
                    $scope.$on('app.stores.receives.previousPageDetails', previousPage);
                }
            })();
        }
    }
)
;