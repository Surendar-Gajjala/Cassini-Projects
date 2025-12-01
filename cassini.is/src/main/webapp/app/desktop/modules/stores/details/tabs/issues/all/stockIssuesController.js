define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/topStockReceivedService',
        'app/shared/services/store/topStockIssuedService',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/tm/taskService',
        'app/shared/services/store/topStoreService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'

    ],
    function (module) {
        module.controller('StockIssuesController', StockIssuesController);

        function StockIssuesController($scope, $rootScope, $timeout, $window, $state, $cookies, $sce,
                                       ObjectTypeAttributeService, TopStoreService, AttributeAttachmentService, ItemService, $stateParams,
                                       TopStockIssuedService, ProjectService, TaskService, CommonService, ObjectAttributeService, ClassificationService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-shopping-cart";
            vm.stockIssues = [];
            vm.stockIssued = [];
            vm.Ids = [];
            vm.itemIds = [];
            vm.attributeIds = [];
            vm.stockissue = [];
            var currencyMap = new Hashtable();
            var attributeMap = new Hashtable();
            vm.issueItemsProperties = [];
            $rootScope.stockIssueList = [];

            vm.back = back;
            vm.showDetails = showDetails;
            vm.newIssue = newIssue;
            vm.showIssueAttributes = showIssueAttributes;
            vm.openAttachment = openAttachment;
            vm.removeAttribute = removeAttribute;
            vm.showImage = showImage;
            vm.loadStockIssues = loadStockIssues;
            vm.resetPage = resetPage;
            /*vm.onSelectType = onSelectType;*/
            vm.attributeFilter = attributeFilter;
            vm.undoIssueTypeSelection = undoIssueTypeSelection;
            vm.applyFilters = applyFilters;
            var nodeId = 0;
            var classificationTree1 = null;
            var rootNode = null;
            vm.itemType = null;
            vm.emptyFilters = {
                issueNumberSource: null,
                storeId: $rootScope.storeId,
                searchAttributes: [],
                notes: null,
                materialIssueType: null,
                attributeSearch: true,
                project: null
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

            $rootScope.stockIssueList = pagedResults;
            vm.stockIssues = pagedResults;

            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }

            function showImage(attribute) {
                var modal = document.getElementById('myModal8');
                var modalImg = document.getElementById('img08');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage8")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function loadStore() {
                TopStoreService.getTopStore($rootScope.storeId).then(
                    function (data) {
                        $rootScope.viewInfo.description = "Store : " + data.storeName;
                        $timeout(function () {
                            classificationTree1 = null;
                            initIssueTypeTree();
                        }, 1000);
                    }
                )
            }

            function newIssue() {
                var options = {
                    title: 'New Stock Issue',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/issues/new/newStockIssueView.jsp',
                    controller: 'NewStockIssueController as stockIssueVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/issues/new/newStockIssueController',
                    width: 500,
                    data: {},
                    buttons: [
                        {text: 'Add', broadcast: 'app.storeItems.issue'}
                    ]
                };

                $rootScope.showSidePanel(options);
            }

            function showDetails(issue) {
                $rootScope.stockIssueId = issue.id;
                $state.go('app.store.stock.issueDetails', {issueId: issue.id});
            }

            function back() {
                $window.history.back();
            }

            function loadRequiredAttributes() {
                ItemService.getTypeAttributesRequiredTrue("ISSUE").then(
                    function (data) {
                        vm.requiredObjectAttributes = data;
                        loadStockIssues();
                    }
                )
            }

            function showIssueAttributes() {
                var options = {
                    title: 'Issue Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.issueAttributes,
                        attributesMode: 'ISSUE'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.issueAttributes = result;
                        loadAttributeValues();
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage("Attributes added successfully");
                        }
                        $rootScope.hideSidePanel('right');
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                vm.requiredAttributeIds = [];
                angular.forEach(vm.stockIssues.content, function (obj) {
                    obj.refValueString = null;
                    vm.objectIds.push(obj.id);
                });
                angular.forEach(vm.issueAttributes, function (objAttr) {
                    if (objAttr.id != null && objAttr.id != "" && objAttr.id != 0) {
                        vm.attributeIds.push(objAttr.id);
                    }
                });

                if (vm.objectIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByObjectIdsAndAttributeIds(vm.objectIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;
                            var map = new Hashtable();
                            angular.forEach(vm.issueAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.stockIssues.content, function (item) {
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

                        }, function (error) {

                        }
                    );
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

            function nextPage() {
                if (vm.stockIssues.last != true) {
                    pageable.page++;
                    loadStockIssues();
                }
            }

            function previousPage() {
                if (vm.stockIssues.first != true) {
                    pageable.page--;
                    loadStockIssues();
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
                loadStockIssues();
            }

            function resetPage() {
                vm.emptyFilters = {
                    issueNumberSource: null,
                    storeId: $rootScope.storeId,
                    searchAttributes: [],
                    notes: null,
                    materialIssueType: null,
                    attributeSearch: true
                };
                vm.itemType = null;
                angular.forEach(vm.issueAttributes, function (attribute) {
                    attribute.textValue = null;
                    attribute.integerValue = null;
                    attribute.booleanValue = null;
                    attribute.currencyValue = null;
                    attribute.longTextValue = null
                });
                loadStockIssues();
            }

            function initIssueTypeTree() {
                nodeId = 0;
                classificationTree1 = $('#issueTypeTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: 'IssueType',
                            iconCls: 'sitemap-node',
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
                ClassificationService.getClassificationTree('MATERIALISSUETYPE').then(
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
                    iconCls: 'sitemap-node',
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
                vm.emptyFilters.materialIssueType = itemType.attributes.itemType.id;
                loadStockIssues();
            }

            function undoIssueTypeSelection() {
                pageable.page = 0;
                vm.itemType = null;
                vm.emptyFilters.materialIssueType = null;
                loadStockIssues();
            }

            function removeAttribute(att) {
                vm.issueAttributes.remove(att);
                var existingAttributeSearchDto = attributeMap.get(att.id);
                attributeMap.remove(att.id, existingAttributeSearchDto);
                var index = vm.emptyFilters.searchAttributes.indexOf(existingAttributeSearchDto);
                vm.emptyFilters.searchAttributes.splice(index, 1);
                applyFilters();
            }

            function loadStockIssues() {
                vm.loading = true;
                vm.stockIssues = pagedResults;
                TopStockIssuedService.getStockIssuesByAttributes($rootScope.storeId, pageable, vm.emptyFilters).then(
                    function (data) {
                        vm.loading = false;
                        vm.stockIssues = data;
                        if ($stateParams.mode == "ISSUE") {
                            $rootScope.viewInfo.title = "Issues";
                        }
                        $rootScope.stockIssueList = data;
                        CommonService.getPersonReferences(vm.stockIssues.content, 'issuedTo');
                        ProjectService.getProjectReferences(vm.stockIssues.content, 'project');
                        angular.forEach(vm.stockIssues.content, function (item) {
                            vm.itemIds.push(item.id);
                            if (item.task != null) {
                                TaskService.getProjectTask(item.project, item.task).then(
                                    function (data) {
                                        item.task = data.name;
                                    }
                                )
                            }
                        });
                        loadAttributeValues();
                    }
                );
            }

            function applyFilters() {
                pageable.page = 0;
                loadStockIssues();
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $scope.$on('app.store.tabactivated', function (event, data) {
                        if (data.tabId == 'details.stockIssues') {
                            pageable.page = 0;
                            loadRequiredAttributes();
                            loadCurrencies();
                            loadStore();
                        }
                    });
                    if ($stateParams.mode == "ISSUE") {
                        loadRequiredAttributes();
                        loadCurrencies();
                        loadStore();
                    }

                    $scope.$on('app.Store.issue.attributes', function () {
                        showIssueAttributes();
                    });
                    $scope.$on('app.stores.issues.nextPageDetails', nextPage);
                    $scope.$on('app.stores.issues.previousPageDetails', previousPage);
                }
            })();
        }
    }
)
;