/**
 * Created by swapna on 13/09/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/roadChallanService',
        'app/shared/services/store/topStoreService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'

    ],
    function (module) {
        module.controller('RoadChallansController', RoadChallansController);

        function RoadChallansController($scope, $rootScope, $window, $stateParams, $state, RoadChallanService, ObjectTypeAttributeService, TopStoreService, ItemService,
                                        AttributeAttachmentService, ObjectAttributeService, $sce) {
            var vm = this;

            vm.itemIds = [];
            vm.showRoadChallanDetails = showRoadChallanDetails;
            vm.showRoadChallanAttributes = showRoadChallanAttributes;
            vm.openAttachment = openAttachment;
            vm.showImage = showImage;
            vm.objectAttributes = [];
            var currencyMap = new Hashtable();
            $rootScope.storeRoadChallanList = [];

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
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

            $rootScope.storeRoadChallanList = angular.copy(pagedResults);

            function loadStore() {
                TopStoreService.getTopStore($rootScope.storeId).then(
                    function (data) {
                        $rootScope.viewInfo.description = "Store : " + data.storeName;
                    }
                );
            }

            function loadRoadChallans() {
                vm.loading = true;
                RoadChallanService.getPagedRoadChallans($rootScope.storeId, pageable).then(
                    function (data) {
                        vm.loading = false;
                        $rootScope.storeRoadChallanList = data;
                        if ($stateParams.mode == "RC") {
                            $rootScope.viewInfo.title = "Road Challans";
                        }
                        $rootScope.storeRoadChallanList = data;
                        loadRequiredRoadChallanAttributes();
                    }
                )
            }

            function showRoadChallanDetails(roadChallan) {
                $rootScope.roadchallanId = roadChallan.id;
                $state.go('app.store.stock.roadChallanDetails', {roadchallanId: roadChallan.id})
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

            function showImage(attribute) {

                var modal = document.getElementById('rcModal');
                var modalImg = document.getElementById('rcImg');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage4")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("rdChalanAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            /*  attributes block start */

            function loadAttributes() {
                if (validateJSON()) {
                    var setAttributes = JSON.parse($window.localStorage.getItem("rdChalanAttributes"));
                } else {
                    var setAttributes = null;
                }

                if (setAttributes != null && setAttributes != undefined) {
                    angular.forEach(setAttributes, function (setAtt) {
                        if (setAtt.id != null && setAtt.id != "" && setAtt.id != 0) {
                            vm.itemIds.push(setAtt.id);
                        }
                    });
                    ObjectTypeAttributeService.getObjectTypeAttributesByIds(vm.itemIds).then(
                        function (data) {
                            if (data.length == 0) {
                                setAttributes = null;
                                $window.localStorage.setItem("rdChalanAttributes", "");
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

            function loadRequiredAttributes() {
                ItemService.getTypeAttributesRequiredTrue("CUSTOM_ROADCHALAN").then(
                    function (data) {
                        vm.requiredObjectAttributes = data;
                        loadRoadChallans();
                    }
                )
            }

            function loadRequiredRoadChallanAttributes() {
                ItemService.getTypeAttributesRequiredTrue("CUSTOM_ROADCHALAN").then(
                    function (data) {
                        vm.requiredRoadChallanAttributes = data;
                        loadRoadChallanAttributeValues();
                    }
                )
            }

            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.objectAttributes.remove(att);
            }

            function showRoadChallanAttributes() {
                var options = {
                    title: 'Road Challan Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.objectAttributes,
                        attributesMode: 'CUSTOM_ROADCHALAN'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.objectAttributes = result;
                        $window.localStorage.setItem("rdChalanAttributes", JSON.stringify(vm.objectAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage("Attributes added successfully");
                        }
                        loadRoadChallanAttributeValues();
                        loadAttributes();
                        $rootScope.hideSidePanel('right');
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadRoadChallanAttributeValues() {
                vm.itemIds = [];
                vm.attributeIds = [];
                vm.requiredAttributeIds = [];
                angular.forEach($rootScope.storeRoadChallanList.content, function (item) {
                    item.refValueString = null;
                    vm.itemIds.push(item.id);
                });
                angular.forEach(vm.objectAttributes, function (roadChallanAttribute) {
                    if (roadChallanAttribute.id != null && roadChallanAttribute.id != "" && roadChallanAttribute.id != 0) {
                        vm.attributeIds.push(roadChallanAttribute.id);
                    }
                });

                angular.forEach(vm.requiredRoadChallanAttributes, function (roadChallanAttribute) {
                    if (roadChallanAttribute.id != null && roadChallanAttribute.id != "" && roadChallanAttribute.id != 0) {
                        vm.requiredAttributeIds.push(roadChallanAttribute.id);
                    }
                });

                if (vm.itemIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.objectAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach($rootScope.storeRoadChallanList.content, function (item) {
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
                                                        vm.roadChallanAttachments = data;
                                                        item[attributeName] = vm.roadChallanAttachments;
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

                if (vm.itemIds.length > 0 && vm.requiredAttributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.requiredAttributeIds).then(
                        function (data) {
                            vm.requiredAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.requiredRoadChallanAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach($rootScope.storeRoadChallanList.content, function (item) {
                                var attributes = [];

                                var itemAttributes = vm.requiredAttributes[item.id];
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
                                                        vm.requiredMaterialAttachments = data;
                                                        item[attributeName] = vm.requiredMaterialAttachments;
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
                $scope.$on('app.store.tabactivated', function (event, data) {
                    if (data.tabId == 'details.roadChallan') {
                        loadStore();
                        loadCurrencies();
                        loadAttributes();
                    }
                });
                if ($stateParams.mode == "RC") {
                    loadStore();
                    loadCurrencies();
                    loadAttributes();
                }

                function nextPage() {
                    if ($rootScope.storeRoadChallanList.last != true) {
                        pageable.page++;
                        loadRoadChallans();
                    }
                }

                function previousPage() {
                    if ($rootScope.storeRoadChallanList.first != true) {
                        pageable.page--;
                        loadRoadChallans();
                    }
                }

                $scope.$on('app.Store.roadChallan.attributes', function () {
                    showRoadChallanAttributes();
                });
                $scope.$on('app.stores.roadChallans.nextPageDetails', nextPage);
                $scope.$on('app.stores.roadChallans.previousPageDetails', previousPage);
            })();
        }
    }
)
;