define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/topStoreService',
        'app/shared/services/pm/project/bomService',
        'app/shared/services/store/topStockMovementService',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/core/classificationService',
        "app/desktop/modules/proc/classification/directive/classificationTreeDirective",
        "app/desktop/modules/proc/classification/directive/classificationTreeController",
        'jquery.easyui',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('StockMovementController', StockMovementController);

        function StockMovementController($scope, $rootScope, $timeout, $stateParams, $state, $cookies,
                                         TopStoreService, BomService, TopStockMovementService, ProjectService, ClassificationService, ItemService, AttributeAttachmentService) {
            var vm = this;

            vm.historyList = [];
            vm.itemIds = [];
            vm.attributeIds = [];
            var isSearchMode = false;
            vm.loading = true;
            var nodeId = 0;
            var classificationTree = null;
            var rootNode1 = null;
            vm.itemType = null;
            var searchMode = null;
            var freeTextQuery = null;
            var currencyMap = new Hashtable();
            vm.showImage = showImage;

            var pageable = {
                page: 0,
                size: 20
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

            $rootScope.stockMovement = pagedResults;

            vm.types = ["RECEIVED", "ISSUED", "LOANRECEIVED", "LOANISSUED", "LOANRETURNITEMISSUED", "LOANRETURNITEMRECEIVED", "ALLOCATED", "OPENINGBALANCE", "RETURNED"];

            vm.emptyFilters = {
                itemName: null,
                description: null,
                itemNumber: null,
                projectName: null,
                units: null,
                quantity: 0,
                quan: null,
                movementType: "",
                movementType1: null,
                itemType: 0,
                fromDate: null,
                toDate: null,
                storeId: $stateParams.storeId
            };

            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.applyFilters = applyFilters;
            vm.removeAttribute = removeAttribute;
            vm.openAttachment = openAttachment;

            function resetPage() {
                vm.emptyFilters = {
                    itemName: null,
                    description: null,
                    itemNumber: null,
                    itemType: 0,
                    units: null,
                    projectName: null,
                    quantity: null,
                    timeStamp: null,
                    movementType: "",
                    fromDate: null,
                    toDate: null,
                    storeId: $stateParams.storeId
                };
                pageable.page = 0;
                $rootScope.showSearch = false;
                $rootScope.searchModeType = false;
                isSearchMode = false;
                vm.itemType = null;
                loadStockMovement();
            }

            function applyFilters() {
                if (!isSearchMode) {
                    pageable.page = 0;
                }
                isSearchMode = true;
                $rootScope.storeStockMovementList = [];
                vm.emptyFilters.quantity = 0;
                if (vm.emptyFilters.quan != null) {
                    vm.emptyFilters.quantity = vm.emptyFilters.quan;
                }
                if (vm.emptyFilters.movementType1 != null) {
                    vm.emptyFilters.movementType = vm.emptyFilters.movementType1;
                }
                if (vm.itemType != null) {
                    vm.emptyFilters.itemType = vm.itemType.attributes.typeObject.id;
                }
                TopStockMovementService.getPageableStockMovementByFilter($stateParams.storeId, pageable, vm.emptyFilters).then(
                    function (data) {
                        $rootScope.storeStockMovementList = data;
                        loadAttributeValues();
                        ProjectService.getProjectReferences($rootScope.storeStockMovementList.content, 'project');
                    });
            }

            function freeTextSearch(event, args) {
                searchMode = "freetext";
                freeTextQuery = args.search;
                pageable.page = 0;
                if (args.search != null && args.search != undefined && args.search.trim() != "") {
                    TopStoreService.getStockMovementByStoreAndFreeTextSearch($stateParams.storeId, pageable, args.search).then(
                        function (data) {
                            vm.loading = false;
                            $rootScope.storeStockMovementList = data;
                            BomService.getBoqItemReferences($stateParams.projectId, $rootScope.storeStockMovementList, 'boqItem');

                        }
                    )
                }
                else {
                    resetPage();
                    loadStockMovement();
                }
            }

            function loadStockMovement() {
                isSearchMode = false;
                TopStockMovementService.getStockMovementByStore($stateParams.storeId, pageable).then(
                    function (data) {
                        vm.loading = false;
                        $rootScope.storeStockMovementList = data;
                        ProjectService.getProjectReferences($rootScope.storeStockMovementList.content, 'project');
                        loadAttributeValues();
                    }
                )
            }

            function initClassificationTree() {
                nodeId = 0;
                classificationTree = $('#classificationTree').tree({
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

                rootNode1 = classificationTree.tree('find', 0);
                if (rootNode1 != null) {
                    loadMaterialClassification();
                }

            }

            function showImage(attribute) {
                var modal = document.getElementById('movModal');
                var modalImg = document.getElementById('movImg');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage1")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function onSelectType1(itemType) {
                var data = itemType.attributes.typeObject.children;
                $rootScope.closeNotification();
                vm.itemType = itemType;
                window.$("body").trigger("click");
                applyFilters();
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

                        classificationTree.tree('append', {
                            parent: rootNode1.target,
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

            function nextPage() {
                if ($rootScope.storeStockMovementList.last != true) {
                    pageable.page++;
                    if (isSearchMode) {
                        applyFilters();
                    }
                    else {
                        loadStockMovement();
                    }
                }
            }

            function previousPage() {
                if ($rootScope.storeStockMovementList.first != true) {
                    pageable.page--;
                    if (isSearchMode) {
                        applyFilters();
                    }
                    else {
                        loadStockMovement();
                    }
                }
            }

            function resize() {
                var height = $(window).height();
                var projectHeaderHeight = $("#project-headerbar").outerHeight();
                if (projectHeaderHeight != null) {
                    if ($application.selectedProject != undefined && $application.selectedProject.locked == true) {
                        $('#contentpanel').height(height - 297);
                    } else {
                        $('#contentpanel').height(height - 267);
                    }
                } else if (projectHeaderHeight == null) {
                    $('#contentpanel').height(height - 217);
                }
            }

            function loadAttributeValues() {
                vm.itemIds = [];
                vm.attributeIds = [];
                angular.forEach($rootScope.storeStockMovementList.content, function (item) {
                    vm.itemIds.push(item.itemDTO.id);
                });
                angular.forEach(vm.stockAttributes, function (stockAttribute) {
                    if (stockAttribute.id != null && stockAttribute.id != "" && stockAttribute.id != 0) {
                        vm.attributeIds.push(stockAttribute.id);
                    }
                });

                if (vm.itemIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;
                            var map = new Hashtable();
                            angular.forEach(vm.stockAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach($rootScope.storeStockMovementList.content, function (item) {
                                var attributes = [];

                                var itemAttributes = vm.selectedObjectAttributes[item.itemDTO.id];
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

            function showAttributes() {
                var options = {
                    title: 'Item Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.stockAttributes,
                        attributesMode: 'MATERIAL'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.stockAttributes = result;
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
                vm.stockAttributes.remove(att);
            }

            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $scope.$on('app.store.tabactivated', function (event, data) {
                        if (data.tabId == 'details.stockMovement') {
                            loadStockMovement();
                            $timeout(function () {
                                classificationTree = null;
                                initClassificationTree();
                            }, 1000);
                        }
                    });
                    resize();
                    $scope.$on('app.stores.movement.freeText', freeTextSearch);
                    $scope.$on('app.stores.movement.reset', resetPage);
                    $scope.$on('app.stores.movement.nextPageDetails', nextPage);
                    $scope.$on('app.stores.movement.previousPageDetails', previousPage);
                    $scope.$on('app.store.stockMovement.attributes', showAttributes);

                }
            })();
        }
    }
);