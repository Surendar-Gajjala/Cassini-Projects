define(['app/desktop/modules/stores/store.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/shared/services/store/topStoreService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'

    ],
    function (module) {
        module.controller('StoresController', StoresController);

        function StoresController($scope, $rootScope, $timeout, $window, $state, ObjectTypeAttributeService, $uibModal, $stateParams, ObjectAttributeService,
                                  CommonService, DialogService, TopStoreService, ItemService, AttributeAttachmentService, $sce) {
            $rootScope.viewInfo.icon = "fa fa-shopping-cart";
            $rootScope.viewInfo.title = "Stores";

            var vm = this;
            vm.loading = true;
            vm.showSearchMode = false;
            vm.stores = [];
            vm.storeAttributes = [];
            var currencyMap = new Hashtable();
            vm.storeIds = [];
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "createdDate",
                    order: "ASC"
                }
            };

            vm.showImage = showImage;
            vm.openAttachment = openAttachment;
            vm.removeAttribute = removeAttribute;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.storeAttributes = [];

            var pagedResults = {
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

            $rootScope.stores = pagedResults;

            vm.emptyFilters = {
                storeName: null,
                description: null,
                locationName: null,
                createdBy: "",
                createdOn: null,
                searchQuery: null
            };

            vm.filters = angular.copy(vm.emptyFilters);
            vm.resetPage = resetPage;

            vm.mode = null;
            vm.objectIds = [];
            vm.newStore = newStore;
            vm.openStore = openStore;
            vm.deleteStore = deleteStore;
            vm.freeTextSearch = freeTextSearch;
            vm.allScrapRequest = allScrapRequest;
            vm.newScrap = newScrap;
            $scope.freeTextQuery = null;

            $rootScope.selectedStore = null;

            vm.persons = [];
            vm.applyFilters = applyFilters;
            vm.resetFilters = resetFilters;
            vm.showStoreAttributes = showStoreAttributes;

            function applyFilters() {
                vm.pageable.page = 0;
                loadStores();
            }

            function resetFilters() {
                vm.filters = angular.copy(vm.emptyFilters);
                vm.pageable.page = 0;
                loadStores();
            }

            function resetPage() {
                vm.showSearchMode = false;
                loadStores();
            }

            function openStore(store) {
                $rootScope.storeId = store.id;
                $state.go('app.store.details', {storeId: store.id});
                $rootScope.selectedStore = store;
            }

            function allScrapRequest() {
                $state.go('app.store.allScrapDetails');
            }

            function newScrap() {
                $state.go('app.newScrap');
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("storesAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            /*  attributes block start */

            function loadAttributes() {
                if (validateJSON()) {
                    var setAttributes = JSON.parse($window.localStorage.getItem("storesAttributes"));
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
                                $window.localStorage.setItem("storesAttributes", "");
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
                ItemService.getTypeAttributesRequiredTrue("STORE").then(
                    function (data) {
                        vm.requiredObjectAttributes = data;
                        loadStores();
                    }
                )
            }

            vm.showRequiredImage = showRequiredImage;

            function showImage(attribute) {
                var modal = document.getElementById('storeModal');
                var modalImg = document.getElementById('storeImg');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function showRequiredImage(attribute) {
                var modal = document.getElementById('storeModal1');
                var modalImg = document.getElementById('storeImg1');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage1")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function removeAttribute(att) {
                vm.objectAttributes.remove(att);
            }

            function showStoreAttributes() {
                var options = {
                    title: 'Store Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.objectAttributes,
                        attributesMode: 'STORE'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.objectAttributes = result;
                        $window.localStorage.setItem("storesAttributes", JSON.stringify(vm.objectAttributes));
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
                angular.forEach(vm.stores.content, function (obj) {
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

                            angular.forEach(vm.stores.content, function (item) {
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

                            angular.forEach(vm.stores.content, function (item) {
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

            function newStore() {
                var options = {
                    title: 'New Store',
                    showMask: true,
                    template: 'app/desktop/modules/im/stores/all/newStoreDialog.jsp',
                    controller: 'NewStoreDialogController as newStoreVm',
                    resolve: 'app/desktop/modules/im/stores/all/newStoreDialogController',
                    width: 500,
                    data: {
                        storeType: 'Stores'
                    },
                    buttons: [
                        {text: 'Create', broadcast: 'app.stores.new'}
                    ],
                    callback: function () {
                        loadStores();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function freeTextSearch(freeText) {
                $scope.freeTextQuery = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.filters.searchQuery = freeText;
                    TopStoreService.freeTextSearch(vm.pageable, vm.filters).then(
                        function (data) {
                            vm.stores = data;
                            vm.showSearchMode = true;
                            CommonService.getPersonReferences(vm.stores.content, 'createdBy');
                        }
                    );
                } else {
                    resetPage();
                    loadStores();
                }
            }

            function deleteStore(store) {
                var options = {
                    title: 'Delete Store',
                    message: 'Are you sure you want to delete (' + store.storeName + ') Store?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        TopStoreService.deleteTopStore(store.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(store.storeName + " : Store deleted successfully");
                                loadStores();
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                })
            }

            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }

            function loadStores() {
                TopStoreService.getAllTopStores(vm.filters, vm.pageable).then(
                    function (data) {
                        vm.stores = data;
                        vm.loading = false;
                        CommonService.getPersonReferences(vm.stores.content, 'createdBy');
                        $rootScope.hideBusyIndicator();
                        angular.forEach(vm.stores.content, function (store) {
                            vm.storeIds.push(store.id);
                        });
                        loadAttributeValues();
                    }
                );

            }

            function nextPage() {
                if (vm.stores.last != true) {
                    vm.pageable.page++;
                    loadStores();
                }
            }

            function previousPage() {
                if (vm.stores.first != true) {
                    vm.pageable.page--;
                    loadStores();
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadAttributes();
                    loadCurrencies();
                }
            })();
        }
    }
)
;