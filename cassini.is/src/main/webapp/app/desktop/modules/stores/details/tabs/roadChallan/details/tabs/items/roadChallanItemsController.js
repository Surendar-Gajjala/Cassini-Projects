/**
 * Created by swapna on 19/09/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/roadChallanService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/shared/services/store/topStoreService',
        'app/shared/services/core/itemService'

    ],
    function (module) {
        module.controller('RoadChallanItemsController', RoadChallanItemsController);

        function RoadChallanItemsController($scope, $rootScope, $window, $state, $sce, ObjectAttributeService, AttributeAttachmentService, ItemService, ObjectTypeAttributeService, $stateParams, RoadChallanService, TopStoreService) {
            var vm = this;

            vm.objectIds = [];
            $rootScope.storeRoadChallanItemsList = [];
            vm.showItemDetails = showItemDetails;
            var currencyMap = new Hashtable();

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

            $rootScope.storeRoadChallanItemsList = angular.copy(pagedResults);

            function loadRoadChallanItems() {
                RoadChallanService.getPagedRoadChallanItems($rootScope.storeId, $stateParams.roadchallanId, pageable).then(
                    function (data) {
                        $rootScope.storeRoadChallanItemsList = data;
                        loadAttributeValues();
                    }
                )
            }

            function showItemDetails(item) {
                item.materialItem.quantity = item.quantity;
                var options = {
                    title: 'Item Details',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/itemBasicInfoView.jsp',
                    controller: 'ItemBasicInfoController as itemBasicInfoVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/itemBasicInfoController',
                    width: 600,
                    data: {
                        item: item.materialItem
                    },
                    buttons: [
                        /*{text: 'Update', broadcast: 'app.stores.receivedItem.info'}*/
                    ],
                    callback: function () {
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function back() {
                $window.history.back();
            }

            function resize() {
                var height = $(window).height();
                $('#contentpanel').height(height - 297);
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
                        hasPermission: $rootScope.hasPermission('permission.roadChallans.edit')
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
                    title: 'Road Challan Item Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.objectAttributes,
                        attributesMode: 'ROADCHALLANITEM'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.objectAttributes = result;
                        $window.localStorage.setItem("roadChallanItemAttributes", JSON.stringify(vm.objectAttributes));
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
                var modal = document.getElementById('myModal2');
                var modalImg = document.getElementById('img03');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            vm.showRequiredImage = showRequiredImage;

            function showRequiredImage(attribute) {
                var modal = document.getElementById('image');
                var modalImg = document.getElementById('img');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage2")[0];

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
                    JSON.parse($window.localStorage.getItem("roadChallanItemAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function loadRequiredAttributes() {
                ItemService.getTypeAttributesRequiredTrue("ROADCHALLANITEM").then(
                    function (data) {
                        vm.requiredObjectAttributes = data;
                        loadRoadChallanItems();
                    }
                )
            }

            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                vm.requiredAttributeIds = [];
                angular.forEach($rootScope.storeRoadChallanItemsList.content, function (obj) {
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

                            angular.forEach($rootScope.storeRoadChallanItemsList.content, function (item) {
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

                            angular.forEach($rootScope.storeRoadChallanItemsList.content, function (item) {
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
                    var setAttributes = JSON.parse($window.localStorage.getItem("roadChallanItemAttributes"));
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
                                $window.localStorage.setItem("roadChallanItemAttributes", "");
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

            function nextPage() {
                if ($rootScope.storeRoadChallanItemsList.last != true) {
                    pageable.page++;
                    loadRoadChallanItems();
                }
            }

            function previousPage() {
                if ($rootScope.storeRoadChallanItemsList.first != true) {
                    pageable.page--;
                    loadRoadChallanItems();
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

            (function () {
                loadCurrencies();
                if ($application.homeLoaded == true) {
                    loadAttributes();
                }
                $scope.$on('app.roadChallan.items.attributes', function () {
                    showAttributes();
                });
                $rootScope.$on('app.roadChallan.items', loadAttributes);
                $scope.$on('app.roadChallan.items.nextPageDetails', nextPage);
                $scope.$on('app.roadChallan.items.previousPageDetails', previousPage);
            })();
        }
    }
)
;