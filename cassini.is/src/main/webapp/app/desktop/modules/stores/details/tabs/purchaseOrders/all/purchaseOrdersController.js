/**
 * Created by swapna on 13/09/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/customPurchaseOrderService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/shared/services/core/itemService'

    ],
    function (module) {
        module.controller('PurchaseOrdersController', PurchaseOrdersController);

        function PurchaseOrdersController($scope, $rootScope, $stateParams, ObjectAttributeService, $window, ObjectTypeAttributeService, $state, CustomPurchaseOrderService,
                                          ItemService, AttributeAttachmentService, $sce) {
            var vm = this;

            vm.loading = false;
            vm.previousPage = previousPage;
            $rootScope.purchaseOrdersList = [];
            var currencyMap = new Hashtable();
            vm.nextPage = nextPage;
            vm.openPurchaseOrderDetails = openPurchaseOrderDetails;
            vm.objectAttributes = [];

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            $rootScope.purchaseOrdersList = angular.copy(vm.pagedResults);
            vm.openAttachment = openAttachment;
            vm.objectIds = [];

            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }

            function openPurchaseOrderDetails(purchaseOrder) {
                $rootScope.purchaseOrderId = purchaseOrder.id;
                $state.go('app.store.stock.purchaseOrderDetails', {
                    storeId: $stateParams.storeId,
                    purchaseOrderId: purchaseOrder.id
                });

            }

            function nextPage() {
                vm.pageable.page++;
                loadPurchaseOrders();
            }

            function previousPage() {
                vm.pageable.page--;
                loadPurchaseOrders();
            }

            function back() {
                $window.history.back();
            }

            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.objectAttributes.remove(att);
            }

            function loadPurchaseOrders() {
                CustomPurchaseOrderService.getPageablePurchaseOrders($stateParams.storeId, vm.pageable).then(
                    function (data) {
                        $rootScope.purchaseOrdersList = data;
                        if ($stateParams.mode == "PO") {
                            $rootScope.viewInfo.title = "Purchase Orders";
                        }
                        $rootScope.storePurchaseOrdersList = data;
                        loadAttributeValues();
                    }, function (error) {

                    });
            }

            function nextPage() {
                if ($rootScope.purchaseOrdersList.last != true) {
                    vm.pageable.page++;
                    loadPurchaseOrders();
                }
            }

            function previousPage() {
                if ($rootScope.purchaseOrdersList.first != true) {
                    vm.pageable.page--;
                    loadPurchaseOrders();
                }
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("objectAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function loadRequiredAttributes() {
                ItemService.getTypeAttributesRequiredTrue("CUSTOM_PURCHASEORDER").then(
                    function (data) {
                        vm.requiredObjectAttributes = data;
                        loadPurchaseOrders();
                    }
                )
            }

            /*  attributes block start */

            function loadAttributes() {
                if (validateJSON()) {
                    var setAttributes = JSON.parse($window.localStorage.getItem("poAttributes"));
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
                                $window.localStorage.setItem("poAttributes", "");
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

            vm.showImage = showImage;
            function showImage(attribute) {
                var modal = document.getElementById('poModal');
                var modalImg = document.getElementById('poImg');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            vm.showRequiredImage = showRequiredImage;

            function showRequiredImage(attribute) {
                var modal = document.getElementById('poModal1');
                var modalImg = document.getElementById('poImg1');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage3")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function showAttributes() {
                var options = {
                    title: 'Purchase Order Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.objectAttributes,
                        attributesMode: 'CUSTOM_PURCHASEORDER'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.objectAttributes = result;
                        $window.localStorage.setItem("poAttributes", JSON.stringify(vm.objectAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage("Attributes added successfully");
                        }
                        loadAttributes();
                        $rootScope.hideSidePanel('right');
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                vm.requiredAttributeIds = [];
                angular.forEach($rootScope.purchaseOrdersList.content, function (obj) {
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

                            angular.forEach($rootScope.purchaseOrdersList.content, function (item) {
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

                            angular.forEach($rootScope.purchaseOrdersList.content, function (item) {
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

            /* ........... end attribute block........*/

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
                loadCurrencies();
                $scope.$on('app.store.tabactivated', function (event, data) {
                    if (data.tabId == 'details.purchaseOrders') {
                        loadAttributes();
                    }
                });
                if ($stateParams.mode == "PO") {
                    loadAttributes();
                }
                $scope.$on('app.stores.purchaseOrders.nextPageDetails', nextPage);
                $scope.$on('app.stores.purchaseOrders.previousPageDetails', previousPage);
                $scope.$on('app.Store.purchaseOrder.attributes', function () {
                    showAttributes();
                })
            })();

        }
    }
)
;