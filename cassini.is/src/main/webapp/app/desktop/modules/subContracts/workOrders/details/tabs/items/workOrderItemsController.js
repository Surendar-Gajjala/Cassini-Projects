/**
 * Created by swapna on 23/01/19.
 */
define(['app/desktop/modules/subContracts/contracts.module',
        'app/shared/services/core/subContractService',
        'app/shared/services/tm/taskService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/shared/services/core/itemService'
    ],
    function (module) {
        module.controller('WorkOrderItemsController', WorkOrderItemsController);

        function WorkOrderItemsController($scope, $rootScope, $timeout, $state, $cookies, $stateParams, $sce, $window,
                                          SubContractService, TaskService, CommonService, AttributeAttachmentService, ObjectTypeAttributeService, ObjectAttributeService, ItemService) {
            var vm = this;
            var itemsMap = new Hashtable();
            vm.Ids = [];
            var currencyMap = new Hashtable();
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.editItemAttributes = editItemAttributes;
            vm.showItemAttributes = showItemAttributes;
            vm.removeAttribute = removeAttribute;

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
            $rootScope.workOrderItems = pagedResults;

            function loadWOItems() {
                vm.loading = true;
                SubContractService.getPageableWorkOrderItems($stateParams.workOrderId, pageable).then(
                    function (data) {
                        $rootScope.workOrderItems = data;
                        TaskService.getTaskReferencesByResources(vm.workOrder.project, $rootScope.workOrderItems.content, 'task');
                        CommonService.getPersonReferences($rootScope.workOrderItems.content, 'createdBy');
                        vm.loading = false;
                        loadAttributeValues();
                    }
                )
            }

            function loadItems() {
                SubContractService.getWorkOrderItems($stateParams.workOrderId).then(
                    function (data) {
                        angular.forEach(data, function (item) {
                            itemsMap.put(item.task, item);
                        });
                    }
                )
            }

            function previousPage() {
                if ($rootScope.workOrderItems.first != true) {
                    pageable.page--;
                    loadWOItems();
                }
            }

            function nextPage() {
                if ($rootScope.workOrderItems.last != true) {
                    pageable.page++;
                    loadWOItems();
                }
            }

            function loadWorkOrder() {
                SubContractService.getWorkOrder($stateParams.workOrderId).then(
                    function (data) {
                        vm.workOrder = data;
                        loadAttributes();
                    }
                )
            }

            function newItem() {
                var options = {
                    title: 'New Work Order Item',
                    showMask: true,
                    template: 'app/desktop/modules/subContracts/workOrders/details/tabs/items/newWorkOrderItemDialogView.jsp',
                    controller: 'NewWorkOrderItemController as newWOItemVm',
                    resolve: 'app/desktop/modules/subContracts/workOrders/details/tabs/items/newWorkOrderItemDialogController',
                    width: 700,
                    data: {
                        workOrder: vm.workOrder,
                        itemsMap: itemsMap
                    },
                    buttons: [
                        {text: 'Create', broadcast: 'app.workOrder.newItem'}
                    ],
                    callback: function (data) {
                        loadWOItems();
                    }
                };

                $rootScope.showSidePanel(options);
            }

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
                        {text: 'Create', broadcast: 'app.stores.item.attributes'}
                    ],
                    callback: function () {
                        loadAttributes();
                    }
                };
                $rootScope.showSidePanel(options);
            }

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
                        hasPermission: $rootScope.hasPermission('permission.workOrders.editBasic')
                    },
                    buttons: [{text: 'Update', broadcast: 'app.stores.item.update.attributes'}],
                    callback: function () {
                        loadAttributes();
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function showAttributes() {
                var options = {
                    title: 'Work Order Item Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.objectAttributes,
                        attributesMode: 'WORKORDERITEM'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.objectAttributes = result;
                        $window.localStorage.setItem("workOrderItemAttributes", JSON.stringify(vm.objectAttributes));
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
                var modal = document.getElementById('woItemModal');
                var modalImg = document.getElementById('woItemImg');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            vm.showRequiredImage = showRequiredImage;

            function showRequiredImage(attribute) {
                var modal = document.getElementById('woItemModal1');
                var modalImg = document.getElementById('woItemImg1');

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
                    JSON.parse($window.localStorage.getItem("workOrderItemAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function loadRequiredAttributes() {
                ItemService.getTypeAttributesRequiredTrue("WORKORDERITEM").then(
                    function (data) {
                        vm.requiredObjectAttributes = data;
                        loadWOItems();
                    }
                )
            }

            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                vm.requiredAttributeIds = [];
                angular.forEach($rootScope.workOrderItems.content, function (obj) {
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

                            angular.forEach($rootScope.workOrderItems.content, function (item) {
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

                            angular.forEach($rootScope.workOrderItems.content, function (item) {
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
                    var setAttributes = JSON.parse($window.localStorage.getItem("workOrderItemAttributes"));
                } else {
                    var setAttributes = null;
                }

                if (setAttributes != null && setAttributes != undefined) {
                    angular.forEach(setAttributes, function (setAtt) {
                        if (setAtt.id != null && setAtt.id != "" && setAtt.id != 0) {
                            vm.Ids.push(setAtt.id);
                        }
                    });
                    ObjectTypeAttributeService.getObjectTypeAttributesByIds(vm.Ids).then(
                        function (data) {
                            if (data.length == 0) {
                                setAttributes = null;
                                $window.localStorage.setItem("workOrderItemAttributes", "");
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

            (function () {
                $scope.$on('app.workOrder.tabactivated', function (event, data) {
                    if (data.tabId == 'details.items') {
                        loadWorkOrder();
                        loadCurrencies();
                        loadItems();
                    }
                });
                $rootScope.$on('app.workOrder.items', function () {
                    loadWorkOrder();
                    loadCurrencies();
                    loadItems();
                });
                $scope.$on('app.workOrder.items.nextPageDetails', nextPage);
                $scope.$on('app.workOrder.items.previousPageDetails', previousPage);
                $scope.$on('app.workOrder.createWOItem', newItem);
                $scope.$on('app.workOrder.items.attributes', showAttributes);
            })();
        }
    }
);