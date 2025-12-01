/**
 * Created by anuko on 25-09-2018.
 */
define(['app/desktop/modules/stores/store.module',
        'app/desktop/modules/stores/supplier/new/supplierController',
        'app/shared/services/supplier/supplierService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'
    ],
    function (module) {
        module.controller('SuppliersAllController', SuppliersAllController);

        function SuppliersAllController($scope, $rootScope, $stateParams, $timeout, $state, $cookies,
                                        CommonService, $uibModal, DialogService, $window, SupplierService, ItemService,
                                        AttributeAttachmentService, ObjectTypeAttributeService, ObjectAttributeService, $sce) {

            var vm = this;

            $rootScope.viewInfo.icon = "fa fa flaticon-businessman276";
            $rootScope.viewInfo.title = "Suppliers";

            vm.loading = true;
            vm.showSearchMode = false;

            vm.newSupplier = newSupplier;
            vm.showSupplier = showSupplier;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.deleteSupplier = deleteSupplier;
            vm.freeTextSearch = freeTextSearch;
            vm.openAttachment = openAttachment;
            vm.showSupplierAttributes = showSupplierAttributes;
            vm.showImage = showImage;
            vm.showRequiredImage = showRequiredImage;
            vm.removeAttribute = removeAttribute;
            vm.supplierAttributes = [];
            vm.supplierList = [];
            vm.objectIds = [];
            var supplierMap = new Hashtable();
            var currencyMap = new Hashtable();
            vm.resetPage = resetPage;

            vm.emptyFilters = {
                searchQuery: null,
                name: null,
                description: null
            };

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
            vm.supplierList = angular.copy(pagedResults);
            vm.filters = angular.copy(vm.emptyFilters);
            $scope.freeTextQuery = null;

            function freeTextSearch(freeText) {
                vm.itemIds = [];
                vm.attributeIds = [];
                vm.requiredAttributeIds = [];
                vm.requiredSupplierAttributes = [];

                $scope.freeTextQuery = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    //vm.filters.searchQuery = freeText;
                    SupplierService.freeTextSearch(pageable, freeText).then(
                        function (data) {
                            vm.supplierList = data;
                            vm.showSearchMode = true;
                            angular.forEach(vm.supplierList.content, function (site) {
                                site.editMode = false;
                                site.showValues = true;
                                site.newDescription = site.description;
                            });
                            vm.clear = true;
                        }
                    );
                } else {
                    resetPage();
                    loadSuppliers();
                }
            }

            function showImage(attribute) {
                var modal = document.getElementById('supModal');
                var modalImg = document.getElementById('supImg');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function showRequiredImage(attribute) {
                var modal = document.getElementById('supModal1');
                var modalImg = document.getElementById('supImg1');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage1")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function resetPage() {
                pageable.page = 0;
                vm.showSearchMode = false;
            }

            function newSupplier() {
                var options = {
                    title: 'New Supplier',
                    showMask: true,
                    template: 'app/desktop/modules/stores/supplier/new/supplierView.jsp',
                    controller: 'NewSupplierController as supplierVm',
                    resolve: 'app/desktop/modules/stores/supplier/new/supplierController',
                    width: 500,
                    data: {
                        supplierMap: supplierMap
                    },
                    buttons: [
                        {text: 'Create', broadcast: 'app.store.supplier.new'}
                    ],
                    callback: function () {
                        loadSuppliers();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadSuppliers() {
                SupplierService.getSuppliers(pageable).then(
                    function (data) {
                        vm.loading = false;
                        vm.supplierList = data;
                        angular.forEach(vm.supplierList.content, function (supplier) {
                            supplier.refValueString = null;
                            supplierMap.put(supplier.name, supplier);
                        });
                        loadSiteAttributeValues();
                    }
                );
            }

            function deleteSupplier(supplier) {
                var options = {
                    title: 'Delete Supplier',
                    message: 'Are you sure you want to delete this (' + supplier.name + ') Supplier ?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        SupplierService.deleteSupplier(supplier.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(supplier.name + " : Supplier deleted successfully");
                                loadSuppliers();
                            }
                        )
                    }
                })
            }

            function showSupplier(supplier) {
                $rootScope.supplierId = supplier.id;
                $state.go('app.store.supplierDetails', {supplierId: supplier.id});
            }

            function nextPage() {
                if (vm.supplierList.last != true) {
                    vm.pageable.page++;
                    if (vm.showSearchMode) {
                        freeTextSearch($scope.freeTextQuery)
                    }
                    else {
                        loadSuppliers();
                    }
                    // loadSuppliers();
                }
            }

            function previousPage() {
                if (vm.supplierList.first != true) {
                    vm.pageable.page--;
                    if (vm.showSearchMode) {
                        freeTextSearch($scope.freeTextQuery)
                    }
                    else {
                        loadSuppliers();
                    }
                    //loadSuppliers();
                }
            }

            function showSupplierAttributes() {
                var options = {
                    title: 'Supplier Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.supplierAttributes,
                        attributesMode: 'SUPPLIER'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.supplierAttributes = result;
                        $window.localStorage.setItem("supplierAttributes", JSON.stringify(vm.supplierAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage("Attributes added successfully");
                        }
                        loadRequiredSupplierAttributes();
                        $rootScope.hideSidePanel('right');
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeAttribute(att) {
                vm.supplierAttributes.remove(att);
            }

            vm.requiredSupplierAttributes = [];
            function loadRequiredSupplierAttributes() {
                ItemService.getTypeAttributesRequiredTrue("SUPPLIER").then(
                    function (data) {
                        vm.requiredSupplierAttributes = data;
                        loadSuppliers();
                    }
                )
            }

            function loadSiteAttributeValues() {
                vm.itemIds = [];
                vm.attributeIds = [];
                vm.requiredAttributeIds = [];
                angular.forEach(vm.supplierList.content, function (item) {
                    item.refValueString = null;
                    vm.itemIds.push(item.id);
                });
                angular.forEach(vm.supplierAttributes, function (supplierAttribute) {
                    if (supplierAttribute.id != null && supplierAttribute.id != "" && supplierAttribute.id != 0) {
                        vm.attributeIds.push(supplierAttribute.id);
                    }
                });

                angular.forEach(vm.requiredSupplierAttributes, function (supplierAttribute) {
                    if (supplierAttribute.id != null && supplierAttribute.id != "" && supplierAttribute.id != 0) {
                        vm.requiredAttributeIds.push(supplierAttribute.id);
                    }
                });

                if (vm.itemIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.supplierAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.supplierList.content, function (item) {
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
                                                        vm.supplierAttachments = data;
                                                        item[attributeName] = vm.supplierAttachments;
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
                                                    item.attributeName = null;
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
                            angular.forEach(vm.requiredSupplierAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.supplierList.content, function (item) {
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
                                        }
                                        else if (selectatt.dataType == 'OBJECT') {
                                            if (attribute.refValue != null) {
                                                var objectSelector = $application.getObjectSelector(selectatt.refType);
                                                if (objectSelector != null && attribute.refValue != null) {
                                                    objectSelector.getDetails(attribute.refValue, item);
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

            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }

            (function () {
                ObjectAttributeService.getCurrencies().then(
                    function (data) {
                        vm.currencies = data;
                        angular.forEach(vm.currencies, function (currency) {
                            currencyMap.put(currency.id, $sce.trustAsHtml(currency.symbol));
                        });
                    }
                );

                function validateJSON() {
                    try {
                        JSON.parse($window.localStorage.getItem("supplierAttributes"));
                    } catch (e) {
                        return false;
                    }
                    return true;
                }

                function loadAttributes() {
                    if (validateJSON()) {
                        var setAttributes = JSON.parse($window.localStorage.getItem("supplierAttributes"));
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
                                    $window.localStorage.setItem("supplierAttributes", "");
                                    vm.supplierAttributes = setAttributes
                                } else {
                                    vm.supplierAttributes = setAttributes;
                                }

                                if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                                    loadRequiredSupplierAttributes();
                                }
                            }
                        )
                    } else {
                        if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                            loadRequiredSupplierAttributes();
                        }
                    }

                }

                if ($application.homeLoaded == true) {
                    loadAttributes();
                    $scope.$on("app.store.supplier.all", function () {
                        loadRequiredSupplierAttributes();
                    });
                }
            })();
        }
    }
)
;
