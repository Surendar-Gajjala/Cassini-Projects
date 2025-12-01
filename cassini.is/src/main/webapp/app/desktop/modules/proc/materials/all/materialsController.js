define(
    [
        'app/desktop/modules/proc/proc.module',
        'split-pane',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/pm/project/bomService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'
    ],
    function (module) {
        module.controller('MaterialsController', MaterialsController);

        function MaterialsController($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies, $window,
                                     CommonService, DialogService, ItemService, ObjectTypeAttributeService, AttributeAttachmentService,
                                     ItemTypeService, BomService, ObjectAttributeService, $sce) {

            $rootScope.viewInfo.icon = "fa fa-th";
            $rootScope.viewInfo.title = "Materials";

            var vm = this;

            vm.loading = true;
            vm.selectedItemType = null;
            vm.mode = $stateParams.mode;
            vm.showSearchMode = false;
            vm.editMaterialItem = editMaterialItem;
            vm.showNewMaterial = showNewMaterial;
            vm.showMaterialDetails = showMaterialDetails;
            vm.previousPage = previousPage;
            vm.nextPage = nextPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.deleteItem = deleteItem;
            $scope.freeTextQuery = null;
            vm.showMaterialAttributes = showMaterialAttributes;
            vm.showMaterialImage = showMaterialImage;
            vm.showMaterialTypeImage = showMaterialTypeImage;
            vm.materialAttributeSearch = materialAttributeSearch;
            vm.clearMaterialAttributeSearch = clearMaterialAttributeSearch;

            var currencyMap = new Hashtable();

            vm.objectIds = [];
            vm.mode = null;
            vm.pageable = {
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
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.materials = angular.copy(pagedResults);

            vm.emptyFilters = {
                itemType: null,
                itemNumber: null,
                active: null,
                unitPrice: null,
                unitCost: null,
                description: null,
                createdDate: null,
                modifiedDate: null
            };

            vm.filters = angular.copy(vm.emptyFilters);

            var materialCriteria = {
                freeTextSearch: false,
                searchQuery: null,
                attributeSearch: false
            };

            function editMaterialItem(material) {
                $state.go('app.proc.materials.edit', {materialId: material.id});
            }

            function showMaterialDetails(material) {
                $rootScope.materialId = material.id;
                $state.go('app.proc.materials.details', {materialId: material.id});
            }

            function showNewMaterial() {
                var options = {
                    title: 'New Material',
                    showMask: true,
                    template: 'app/desktop/modules/proc/materials/new/newMaterialView.jsp',
                    controller: 'NewMaterialController as newMaterialVm',
                    resolve: 'app/desktop/modules/proc/materials/new/newMaterialController',
                    width: 500,
                    buttons: [
                        {text: 'Create', broadcast: 'app.items.new'}
                    ],
                    callback: function () {
                        loadRequiredTrueAttributes();
                    }
                };

                $rootScope.showSidePanel(options);

            }

            vm.materialAttributes = [];
            function showMaterialAttributes() {
                var options = {
                    title: 'Material Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.materialAttributes,
                        attributesMode: 'MATERIAL'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.materialAttributes = result;
                        $window.localStorage.setItem("materialAttributes", JSON.stringify(vm.materialAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage("Attributes added successfully");
                        }
                        $rootScope.hideSidePanel();
                        loadRequiredTrueAttributes();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.itemIds = [];
            vm.attributeIds = [];
            vm.materialRequiredIds = [];
            vm.materialTypeRequiredIds = [];
            vm.materials = angular.copy(pagedResults);

            function loadMaterials() {
                vm.loading = true;
                ItemService.getMaterials(vm.pageable).then(
                    function (data) {
                        vm.loading = false;
                        vm.materials = data;
                        CommonService.getPersonReferences(vm.materials.content, 'createdBy');
                        CommonService.getPersonReferences(vm.materials.content, 'modifiedBy');

                        loadMaterialAttributeValues();
                    }
                )
            }

            function nextPage() {
                if (vm.materials.last != true) {
                    vm.pageable.page++;
                    if (vm.showSearchMode) {
                        freeTextSearch($scope.freeTextQuery)
                    }
                    else {
                        loadMaterials();
                    }
                }
            }

            function previousPage() {
                if (vm.materials.first != true) {
                    vm.pageable.page--;
                    if (vm.showSearchMode) {
                        freeTextSearch($scope.freeTextQuery)
                    }
                    else {
                        loadMaterials();
                    }
                }
            }

            function resetPage() {
                vm.pageable.page = 0;
                vm.showSearchMode = false;
                materialCriteria.freeTextSearch = false;
                materialCriteria.searchQuery = null;
            }

            function freeTextSearch(freeText) {
                vm.itemIds = [];
                vm.attributeIds = [];
                vm.materialRequiredIds = [];
                vm.materialTypeRequiredIds = [];

                $scope.freeTextQuery = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    materialCriteria.freeTextSearch = true;
                    materialCriteria.searchQuery = freeText;
                    ItemService.materialFreeTextSearch(vm.pageable, materialCriteria).then(
                        function (data) {
                            vm.materials = data;
                            vm.showSearchMode = true;
                            CommonService.getPersonReferences(vm.materials.content, 'createdBy');
                            CommonService.getPersonReferences(vm.materials.content, 'modifiedBy');
                            vm.clear = true;

                            loadMaterialAttributeValues();
                        }
                    );
                } else {
                    resetPage();
                    loadMaterials();
                }
            }

            function loadMaterialAttributeValues() {
                angular.forEach(vm.materials.content, function (item) {
                    vm.itemIds.push(item.id);
                });
                angular.forEach(vm.materialAttributes, function (materialAttribute) {
                    if (materialAttribute.id != null && materialAttribute.id != "" && materialAttribute.id != 0) {
                        vm.attributeIds.push(materialAttribute.id);
                    }
                });

                angular.forEach(vm.requiredTrueMaterialAttributes, function (materialAttribute) {
                    if (materialAttribute.id != null && materialAttribute.id != "" && materialAttribute.id != 0) {
                        vm.materialRequiredIds.push(materialAttribute.id);
                    }
                });

                /* angular.forEach(vm.requiredTrueMaterialTypeAttributes, function (materialAttribute) {
                 if (materialAttribute.id != null && materialAttribute.id != "" && materialAttribute.id != 0) {
                 vm.materialTypeRequiredIds.push(materialAttribute.id);
                 }
                 });*/

                if (vm.itemIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.materialAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.materials.content, function (item) {
                                var attributes = [];
                                item.refValueString = null;

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
                                                        vm.materialAttachments = data;
                                                        item[attributeName] = vm.materialAttachments;
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

                if (vm.itemIds.length > 0 && vm.materialRequiredIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.materialRequiredIds).then(
                        function (data) {
                            vm.requiredMaterialAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.requiredTrueMaterialAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.materials.content, function (item) {
                                var attributes = [];

                                var itemAttributes = vm.requiredMaterialAttributes[item.id];
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

                if (vm.itemIds.length > 0 && vm.materialTypeRequiredIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.materialTypeRequiredIds).then(
                        function (data) {
                            vm.materialTypeAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.requiredTrueMaterialTypeAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.materials.content, function (item) {
                                var attributes = [];

                                var itemAttributes = vm.materialTypeAttributes[item.id];
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
                                                        vm.materialTypeAttachments = data;
                                                        item[attributeName] = vm.materialTypeAttachments;
                                                    }
                                                )
                                            }
                                        } else if (selectatt.dataType == 'IMAGE') {
                                            if (attribute.imageValue != null) {
                                                item[attributeName].typeImage = null;
                                                item[attributeName].typeImage = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                            }
                                        }
                                    }
                                })
                            })

                        }
                    );
                }
            }

            function deleteItem(item) {
                var options = {
                    title: 'Delete Material',
                    message: 'Are you sure you want to delete (' + item.itemNumber + ') Material?',
                    okButtonClass: 'btn-danger'
                };
                BomService.getBoqItemByItemNumber(item.itemNumber).then(
                    function (data) {
                        if (data == null || data == '') {
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    ItemService.deleteItem(item.id).then(
                                        function (data) {
                                            var index = vm.materials.content.indexOf(item);
                                            vm.materials.content.splice(index, 1);
                                            $rootScope.showSuccessMessage(item.itemNumber + " : Material deleted successfully");
                                        }
                                    )
                                }
                            });
                        }
                        else {
                            $rootScope.showErrorMessage("This Material is already in use(BOQ)! We cannot delete this Material");
                        }
                    }
                )

            }

            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.materialAttributes.remove(att);
                $window.localStorage.setItem("materialAttributes", JSON.stringify(vm.materialAttributes));
            }

            vm.showImage = showImage;
            function showImage(attribute) {

                var modal = document.getElementById('matShowModal');
                var modalImg = document.getElementById('matShowImg');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function showMaterialImage(attribute) {

                var modal = document.getElementById('matShowModal1');
                var modal1 = document.getElementById('freeTextId');
                var modalImg = document.getElementById('matShowImg1');

                modal.style.display = "block";
                modal1.style.display = "none";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage1")[0];

                span.onclick = function () {
                    modal1.style.display = "block";
                    modal.style.display = "none";
                }
            }

            function showMaterialTypeImage(attribute) {

                var modal = document.getElementById('myModal2');
                var modal1 = document.getElementById('freeTextId');
                var modalImg = document.getElementById('img02');

                modal.style.display = "block";
                modal1.style.display = "none";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage1")[0];

                span.onclick = function () {
                    modal1.style.display = "block";
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
                    JSON.parse($window.localStorage.getItem("materialAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function loadRequiredTrueAttributes() {
                ItemService.getTypeAttributesRequiredTrue("MATERIAL").then(
                    function (data) {
                        vm.requiredTrueMaterialAttributes = data;
                        loadMaterials();
                    }
                )
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
                checkJson();
            }

            function objectTypeAttributesByObjectIds(setAttributes) {
                angular.forEach(setAttributes, function (setAtt) {
                    if (setAtt.id != null && setAtt.id != "" && setAtt.id != 0) {
                        vm.objectIds.push(setAtt.id);
                    }
                });
                ObjectTypeAttributeService.getObjectTypeAttributesByIds(vm.objectIds).then(
                    function (data) {
                        if (data.length == 0) {
                            setAttributes = null;
                            $window.localStorage.setItem("materialAttributes", "");
                            vm.materialAttributes = setAttributes
                        } else {
                            vm.materialAttributes = setAttributes;
                        }

                        if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                            loadRequiredTrueAttributes();
                        }
                    }
                )
            }

            function materialAttributeSearch() {
                var options = {
                    title: 'Attribute Search',
                    showMask: true,
                    template: 'app/desktop/modules/attributeSearch/attributeSearchView.jsp',
                    controller: 'AttributeSearchController as attributeSearchVm',
                    resolve: 'app/desktop/modules/attributeSearch/attributeSearchController.js',
                    width: 500,
                    buttons: [
                        {text: 'Search', broadcast: 'app.store.attribute.search'}
                    ],
                    data: {
                        objectType: 'MATERIAL',
                        type: 'MATERIALTYPE'
                    },
                    callback: function (result) {
                        materialCriteria.freeTextSearch = false;
                        materialCriteria.searchAttributes = result;
                        materialCriteria.attributeSearch = true;
                        vm.loading = true;
                        ItemService.materialFreeTextSearch(vm.pageable, materialCriteria).then(
                            function (data) {
                                vm.loading = false;
                                vm.attributeSearch = true;
                                vm.materials = data;
                                loadMaterialAttributeValues();
                            }
                        );
                        $rootScope.hideSidePanel();
                        $rootScope.hideBusyIndicator();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function clearMaterialAttributeSearch() {
                vm.attributeSearch = false;
                vm.pageable.page = 0;
                materialCriteria.attributeSearch = false;
                loadRequiredTrueAttributes();
            }

            function checkJson() {
                if (validateJSON()) {
                    var setAttributes = JSON.parse($window.localStorage.getItem("materialAttributes"));
                } else {
                    var setAttributes = null;
                }
                if (setAttributes != null && setAttributes != undefined) {
                    objectTypeAttributesByObjectIds(setAttributes);
                } else {
                    if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                        loadRequiredTrueAttributes();
                    }
                }
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    $('div.split-right-pane').css({left: 300});
                    $('div.split-pane').splitPane();
                });
                $rootScope.$on('app.materials.all', function () {
                    loadRequiredTrueAttributes();
                });
                loadCurrencies();
            })();
        }
    }
)
;


