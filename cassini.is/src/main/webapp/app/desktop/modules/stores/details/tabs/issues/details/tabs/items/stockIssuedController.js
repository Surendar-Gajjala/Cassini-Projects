define(['app/desktop/modules/stores/store.module',
        'app/shared/services/core/storeService',
        'app/shared/services/tm/taskService',
        'app/shared/services/pm/project/bomService',
        'app/shared/services/store/topStockIssuedService',
        'app/shared/services/pm/project/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/shared/services/store/topStoreService',
        'app/shared/services/core/itemService'

    ],
    function (module) {
        module.controller('StoreStockIssuedController', StoreStockIssuedController);

        function StoreStockIssuedController($scope, $rootScope, $timeout, $sce, ObjectAttributeService, $window, AttributeAttachmentService, ItemService, ObjectTypeAttributeService, $state, TopStoreService, $cookies, $stateParams, ProjectService,
                                            BomService, TaskService, StoreService, TopStockIssuedService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-shopping-cart";

            vm.back = back;
            vm.storeId = $rootScope.storeId;
            var currencyMap = new Hashtable();
            vm.objectIds = [];
            vm.stockIssue = null;
            vm.store = null;
            $rootScope.stockIssueItemsList = [];

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

            $rootScope.stockIssueItemsList = pagedResults;

            function back() {
                $window.history.back();
            }

            vm.editItemAttributes = editItemAttributes;

            function editItemAttributes(item, type) {
                $rootScope.itemId = item.id;
                $rootScope.obType = type;
                var options = {
                    title: 'Edit Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/issues/details/tabs/items/editItemAttributesView.jsp',
                    controller: 'EditItemAttributesController as editItemAttributesVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/issues/details/tabs/items/editItemAttributesController',
                    width: 600,
                    data: {
                        objType: type,
                        hasPermission: $rootScope.hasPermission('permission.stockIssues.edit')
                    },
                    buttons: [{text: 'Update', broadcast: 'app.stores.item.update.attributes'}],
                    callback: function () {
                        loadAttributes();
                    }
                };
                $rootScope.showSidePanel(options);
            }

            vm.showItemAttributes = showItemAttributes;

            function showItemAttributes(item, type) {
                var options = {
                    title: 'Item Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/issues/details/tabs/items/itemAttributesView.jsp',
                    controller: 'ItemAttributesController as itemAttributesVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/issues/details/tabs/items/itemAttributesController',
                    width: 600,
                    data: {
                        objType: type,
                        obje: item
                    },
                    buttons: [
                        {text: 'Update', broadcast: 'app.stores.item.attributes'}
                    ],
                    callback: function () {
                        loadAttributes();
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function loadIssueItems() {
                TopStockIssuedService.getPagedStockIssueItems($rootScope.storeId, $stateParams.issueId, pageable).then(
                    function (stockMovements) {
                        $rootScope.stockIssueItemsList = stockMovements;
                        loadAttributeValues();
                    }
                )
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

            $rootScope.loadProperties = loadProperties;

            function loadReceivedProperties() {
                vm.issueItemsProperties = [];
                TopStoreService.getReceivedItemProperties("ISSUE").then(
                    function (data) {
                        vm.issueItemsProperties = data;
                    }
                )
            }

            function loadStockMovement() {
                vm.loading = true;
                vm.stockIssued = [];
                vm.itemIds = [];
                vm.attributeIds = [];
                vm.stockissue = [];
                TopStoreService.getStockMovementByStore($stateParams.storeId, "ISSUED").then(
                    function (data) {
                        vm.loading = false;
                        vm.stockMovementList = data;
                        angular.forEach(data, function (stockMovent) {
                            if (stockMovent.movementType == "ISSUED") {
                                vm.stockIssued.push(stockMovent);
                                angular.forEach(vm.stockIssued, function (stock) {
                                    ProjectService.getProject(stock.project).then(
                                        function (data) {
                                            stock.refObject = data.name;

                                            var index = vm.stockissue.indexOf(stock);
                                            if (index == -1) {
                                                vm.stockissue.push(stock);
                                            }
                                        })
                                })
                            }
                            BomService.getBoqItemReferences($stateParams.projectId, vm.stockIssued, 'boqItem');

                        });
                        angular.forEach(vm.stockIssued, function (item) {
                            vm.itemIds.push(item.rowId);
                            ProjectService.getProjectByBoqId(item.boqItem).then(
                                function (project) {
                                    item.projectName = project.name;
                                }
                            )
                        });
                        angular.forEach(vm.issueItemsProperties, function (attribute) {
                            if (attribute.id != null && attribute.id != "" && attribute.id != 0) {
                                vm.attributeIds.push(attribute.id);
                            }
                        });
                        getAttributes();
                    }
                )
            }

            function loadProperties() {
                loadReceivedProperties();
                loadStockMovement();
            }

            vm.showItemDetails = showItemDetails;
            function showItemDetails(item) {
                item.itemDTO.issuedOn = new Date();
                item.itemDTO.quantity = item.quantity;
                var options = {
                    title: 'Issued Item Details',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/itemBasicInfoView.jsp',
                    controller: 'ItemBasicInfoController as itemBasicInfoVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/itemBasicInfoController',
                    width: 600,
                    data: {
                        item: item.itemDTO
                    },
                    buttons: [
                        {text: 'Update', broadcast: 'app.stores.issuedItem.info'}
                    ],
                    callback: function () {
                        //loadProperties();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function showIssuedAttributes() {
                var options = {
                    title: 'Issued Item Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.objectAttributes,
                        attributesMode: 'ISSUEITEM'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.objectAttributes = result;
                        $window.localStorage.setItem("issueItemAttributes", JSON.stringify(vm.objectAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage("Attributes added successfully");
                        }
                        loadAttributes();
                        $rootScope.hideSidePanel();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.showImage = showImage;
            function showImage(attribute) {
                var modal = document.getElementById('issueItemModal');
                var modalImg = document.getElementById('issueItemImg');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            vm.showRequiredImage = showRequiredImage;

            function showRequiredImage(attribute) {
                var modal = document.getElementById('issueItemModal1');
                var modalImg = document.getElementById('issueItemImg1');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage1")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            vm.openAttachment = openAttachment;
            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("issueItemAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function loadRequiredAttributes() {
                ItemService.getTypeAttributesRequiredTrue("ISSUEITEM").then(
                    function (data) {
                        vm.requiredObjectAttributes = data;
                        loadIssueItems();
                    }
                )
            }

            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                vm.requiredAttributeIds = [];
                angular.forEach($rootScope.stockIssueItemsList.content, function (obj) {
                    obj.refValueString = null;
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

                            angular.forEach($rootScope.stockIssueItemsList.content, function (item) {
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

                            angular.forEach($rootScope.stockIssueItemsList.content, function (item) {
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

            function loadAttributes() {
                if (validateJSON()) {
                    var setAttributes = JSON.parse($window.localStorage.getItem("issueItemAttributes"));
                } else {
                    var setAttributes = null;
                }

                if (setAttributes != null && setAttributes != undefined) {
                    angular.forEach(setAttributes, function (setAtt) {
                        if (setAtt.id != null && setAtt.id != "" && setAtt.id != 0) {
                            vm.objectIds.push(setAtt.id);
                        }
                    });
                    ObjectTypeAttributeService.getObjectTypeAttributesByIds(vm.objectIds).then(
                        function (data) {
                            if (data.length == 0) {
                                setAttributes = null;
                                $window.localStorage.setItem("issueItemAttributes", "");
                                vm.objectAttributes = setAttributes
                            } else {
                                vm.objectAttributes = setAttributes;
                            }

                            if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                                loadRequiredAttributes();
                            }
                        }
                    )
                } else {
                    if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                        loadRequiredAttributes();
                    }
                }
            }

            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.objectAttributes.remove(att);
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
                if ($rootScope.stockIssueItemsList.last != true) {
                    pageable.page++;
                    loadIssueItems();
                }
            }

            function previousPage() {
                if ($rootScope.stockIssueItemsList.first != true) {
                    pageable.page--;
                    loadIssueItems();
                }
            }

            $scope.$on('app.issue.items.attributes', function () {
                showIssuedAttributes();
            });

            (function () {
                if ($application.homeLoaded == true) {
                    loadCurrencies();
                    $rootScope.$on('app.stock.issueItems', function (event, data) {
                        loadAttributes();
                        resize();
                    });
                    $scope.$on('app.stores.issueItems.nextPageDetails', nextPage);
                    $scope.$on('app.stores.issueItems.previousPageDetails', previousPage);
                }
            })();
        }
    }
)
;