/**
 * Created by swapna on 13/09/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/store/customPurchaseOrderService',
        'app/shared/services/supplier/supplierService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController'

    ],

    function (module) {
        module.controller('NewPurchaseOrderController', NewPurchaseOrderController);

        function NewPurchaseOrderController($scope, $rootScope, $window, $state, AutonumberService, AttributeAttachmentService, $application, $stateParams, DialogService, ObjectAttributeService, $q, ObjectTypeAttributeService, $timeout, SupplierService, CommonService, CustomPurchaseOrderService) {

            $rootScope.viewInfo.icon = "fa fa-sign-in";
            $rootScope.viewInfo.title = $stateParams.mode == 'edit' || $stateParams.mode == 'approved' ? "PurchaseOrder Details" : "New PurchaseOrder";

            var vm = this;
            vm.mode = $stateParams.mode;
            vm.buttonText = $stateParams.mode == 'edit' || $stateParams.mode == 'approved' ? "Update" : "Create";
            vm.showCreatePurchaseButton = false;
            //vm.showAddRequestsButton = false;
            vm.editMode = vm.mode == 'new' ? true : false;
            vm.loading = false;

            vm.back = back;
            vm.openRequestsDialogue = openRequestsDialogue;
            vm.createPurchaseOrder = createPurchaseOrder;
            vm.editPurchaseOrderDetails = editPurchaseOrderDetails;
            vm.editPurchaseItem = editPurchaseItem;
            vm.applyChanges = applyChanges;
            vm.cancelChanges = cancelChanges;
            vm.deletePurchaseItem = deletePurchaseItem;
            vm.projectChanged = projectChanged;
            vm.approvePurchaseOrder = approvePurchaseOrder;
            vm.removeFromPurchaseItems = removeFromPurchaseItems;
            vm.openPurchaseItemDetails = openPurchaseItemDetails;
            vm.generateAutoNumber = generateAutoNumber;
            var reqItemsMap = new Hashtable();
            vm.groupedItems = [];
            vm.purchaseOrderRequests = [];
            vm.suppliers = [];
            vm.requiredAttributes = [];
            vm.attributes = [];

            vm.purchaseItem = {
                id: null,
                customPurchaseOrder: null,
                requisition: null,
                materialItem: null,
                purchaseItemQuantity: null,
                purchaseItemNotesObject: null,
                reqItemQuantity: null,
                quantity: null,
                notes: null
            }

            vm.newPurchaseOrder = {
                id: null,
                supplierObject: null,
                poDateObject: null,
                approvedByObject: null,
                notesObject: null,
                poDate: null,
                status: null,
                poNumber: null,
                store: $stateParams.storeId,
                supplier: null,
                approvedBy: null,
                notes: null,
                customPurchaseOrdersItems: []
            }

            var pageable = {
                page: 0,
                size: 10,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            function generateAutoNumber() {
                var prefix = $application.config.autoNumber.prefix;
                var year = $application.config.autoNumber.year;
                /* var year = new Date().getFullYear();*/
                AutonumberService.getAutonumberByName('Default Purchase Order Number Source').then(
                    function (data) {
                        AutonumberService.getNextNumber(data.id).then(
                            function (data) {
                                vm.generatedNumber = data;
                                if (prefix != "" && year != "") {
                                    vm.newPurchaseOrder.poNumber = prefix + "/ " + $rootScope.selectedStore.storeName + "/" + year + "/" + vm.generatedNumber;
                                }
                                else {
                                    vm.newPurchaseOrder.poNumber = vm.generatedNumber;
                                }
                            }
                        )
                    });
            }

            function openPurchaseItemDetails(material) {
                $rootScope.materialId = material.id;
                $state.go('app.proc.materials.details', {materialId: material.materialItem.id, mode: 'materialTab'});
            }

            function approvePurchaseOrder() {
                if (validatePurchaseOrder()) {
                    vm.newPurchaseOrder.status = 'APPROVED';
                    CustomPurchaseOrderService.updateCustomPurchaseOrder(vm.newPurchaseOrder).then(
                        function (data) {
                            $rootScope.showSuccessMessage("Purchase Order (" + data.poNumber + ") approved successfully");
                        }, function (error) {

                        }
                    )
                }
            }

            function editPurchaseOrderDetails() {
                vm.editMode = true;
            }

            function editPurchaseItem(item) {
                item.editMode = true;
                item.showEditButton = false;
            }

            function applyChanges(item) {
                item.showEditButton = true;
                item.editMode = false;
                vm.purchaseItem.notes = item.purchaseItemNotesObject;
                vm.purchaseItem.quantity = item.purchaseItemQuantity;
            }

            function deletePurchaseItem(item) {
                var options = {
                    title: 'Delete Purchase Order Item',
                    message: 'Are you sure you want to delete this Item?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        removeFromPurchaseItems(item);
                        CustomPurchaseOrderService.deletePurchaseItem(vm.newPurchaseOrder.id, item.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Purchase Order Item deleted successfully");
                            }, function (error) {

                            }
                        )
                    }
                });
            }

            function cancelChanges(item) {
                item.showEditButton = true;
                item.editMode = false;
                item.purchaseItemNotesObject = item.notes;
                item.purchaseItemQuantity = item.quantity;
            }

            function removeFromPurchaseItems(purchaseItem) {
                var purchaseItemIndex = vm.groupedItems.findIndex(item = > item.materialItem.id == purchaseItem.materialItem.id
            )
                ;
                var items = reqItemsMap.get(purchaseItem.requisition.requisitionNumber);
                if (items.length == 1) {
                    if (purchaseItemIndex != -1) {
                        vm.groupedItems.splice(purchaseItemIndex, 1);
                    }
                    var requestNumberIndex = vm.groupedItems.findIndex(item = > item.materialItem.itemNumber == purchaseItem.requisition.requisitionNumber
                )
                    ;
                    if (requestNumberIndex != -1) {
                        vm.groupedItems.splice(requestNumberIndex, 1);
                        reqItemsMap.remove(purchaseItem.requisition.requisitionNumber);
                        $rootScope.showSuccessMessage("Item removed successfully");
                    }
                } else {
                    if (purchaseItemIndex != -1) {
                        vm.groupedItems.splice(purchaseItemIndex, 1);
                        items = reqItemsMap.get(purchaseItem.requisition.requisitionNumber);
                        var removableItemIndex = items.findIndex(item = > item.materialItem.id == purchaseItem.materialItem.id
                    )
                        ;
                        if (removableItemIndex != -1) {
                            items.splice(removableItemIndex, 1);
                            reqItemsMap.put(purchaseItem.requisition.requisitionNumber, items);
                        }
                    }
                }

                if (vm.groupedItems.length == 0) {
                    vm.showCreatePurchaseButton = false;
                }
            }

            function createPurchaseOrder() {
                if (validatePurchaseOrder()) {
                    attributesValidate().then(
                        function (success) {
                            CustomPurchaseOrderService.createPurchaseOrder(vm.newPurchaseOrder).then(
                                function (data) {
                                    vm.showCreatePurchaseButton = false;
                                    vm.editMode = false;
                                    vm.newPurchaseOrder.status = data.status;
                                    vm.newPurchaseOrder.id = data.id;
                                    intializationForAttributesSave().then(
                                        function (sucess) {
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.purchaseOrderId = vm.newPurchaseOrder.id;
                                            $state.go('app.store.stock.purchaseOrderDetails', {
                                                storeId: $stateParams.storeId,
                                                purchaseOrderId: vm.newPurchaseOrder.id
                                            });
                                            $rootScope.showSuccessMessage("Purchase Order (" + vm.newPurchaseOrder.poNumber + ") created successfully");
                                        }, function (error) {

                                        }
                                    )
                                }, function (error) {

                                }
                            )
                        }, function (error) {

                        });
                }
            }

            function projectChanged() {
                vm.showAddRequestsButton = true;
            }

            function validatePurchaseOrder() {
                var valid = false;
                if (vm.newPurchaseOrder.supplierObject == null || vm.newPurchaseOrder.supplierObject == undefined || vm.newPurchaseOrder.supplierObject == "") {
                    valid = false;
                    $rootScope.showWarningMessage("Please select Supplier");
                } else if (vm.newPurchaseOrder.poDateObject == null || vm.newPurchaseOrder.poDateObject == undefined || vm.newPurchaseOrder.poDateObject == "") {
                    valid = false;
                    $rootScope.showWarningMessage("Please select PO Date");
                } else if (vm.newPurchaseOrder.status == 'NEW') {
                    if (vm.newPurchaseOrder.approvedByObject == null || vm.newPurchaseOrder.approvedByObject == undefined || vm.newPurchaseOrder.approvedByObject == "") {
                        valid = false;
                        $rootScope.showWarningMessage("Please enter ApprovedBy");
                    } else {
                        vm.newPurchaseOrder.supplier = vm.newPurchaseOrder.supplierObject.name;
                        vm.newPurchaseOrder.poDate = vm.newPurchaseOrder.poDateObject;
                        vm.newPurchaseOrder.store = $stateParams.storeId;
                        vm.newPurchaseOrder.approvedBy = vm.newPurchaseOrder.approvedByObject;
                        vm.newPurchaseOrder.notes = vm.newPurchaseOrder.notesObject;
                        if (vm.groupedItems.length > 0) {
                            angular.forEach(vm.groupedItems, function (grpItem) {
                                if (grpItem.rowType == 'item') {
                                    grpItem.quantity = grpItem.purchaseItemQuantity;
                                    grpItem.notes = grpItem.purchaseItemNotesObject;
                                    vm.newPurchaseOrder.customPurchaseOrdersItems.push(grpItem);
                                }
                            });
                            valid = true;
                        } else {
                            $rootScope.showWarningMessage("Please add atleast one item to Purchase Order");
                            valid = false;
                        }
                    }
                } else {
                    vm.newPurchaseOrder.supplier = vm.newPurchaseOrder.supplierObject.name;
                    vm.newPurchaseOrder.poDate = vm.newPurchaseOrder.poDateObject;
                    vm.newPurchaseOrder.store = $stateParams.storeId;
                    vm.newPurchaseOrder.approvedBy = vm.newPurchaseOrder.approvedByObject;
                    vm.newPurchaseOrder.notes = vm.newPurchaseOrder.notesObject;
                    if (vm.groupedItems.length > 0) {
                        angular.forEach(vm.groupedItems, function (grpItem) {
                            if (grpItem.rowType == 'item') {
                                grpItem.quantity = grpItem.purchaseItemQuantity;
                                grpItem.notes = grpItem.purchaseItemNotesObject;
                                vm.newPurchaseOrder.customPurchaseOrdersItems.push(grpItem);
                            }
                        });
                        valid = true;
                    } else {
                        $rootScope.showWarningMessage("Please add atleast one item to Purchase Order");
                        valid = false;
                    }
                }
                return valid;
            }

            function openRequestsDialogue() {
                var options = {
                    title: 'Items',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/purchaseOrders/new/purchaseOrderRequestsView.jsp',
                    controller: 'PurchaseOrderRequestsController as purchaseOrderRequestsVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/purchaseOrders/new/purchaseOrderRequestsController',
                    width: 700,
                    data: {
                        groupedItems: vm.groupedItems,
                        itemsMap: reqItemsMap
                    },
                    buttons: [],
                    callback: function (requistion, reqItem, requisitionsMap) {
                        loadGroupByRequisitionItems(requistion, reqItem, requisitionsMap);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function groupItems(items, requistion, reqItem) {
                angular.forEach(items, function (item) {
                    var purchaseItem = angular.copy(vm.purchaseItem);
                    purchaseItem.rowType = "item";
                    purchaseItem.id = item.id;
                    purchaseItem.requisition = requistion;
                    purchaseItem.materialItem = item.materialItem;
                    purchaseItem.reqItemQuantity = item.quantity;
                    purchaseItem.purchaseItemQuantity = item.quantity;
                    purchaseItem.notes = item.notes;
                    purchaseItem.quantity = item.quantity;
                    purchaseItem.isNew = item.isNew;
                    purchaseItem.editMode = item.editMode;
                    purchaseItem.showEditButton = item.showEditButton;
                    purchaseItem.purchaseItemNotesObject = purchaseItem.isNew == true ? null : item.notes;
                    vm.groupedItems.push(purchaseItem);
                });
                if (vm.groupedItems.length > 0 && vm.mode == 'new') {
                    vm.showCreateIndentButton = true;
                }
                if (requistion.customRequisitionItems != null) {
                    var requestNumberIndex = requistion.customRequisitionItems.findIndex(item = > item.materialItem.id == reqItem.materialItem.id
                )
                    ;
                    if (requestNumberIndex != -1) {
                        requistion.customRequisitionItems.splice(requestNumberIndex, 1);
                    }
                }
            }

            function findRequisition(keys, requistion) {
                var currentKey = requistion.requisitionNumber;
                var requestNumberIndex = keys.findIndex(item = > item == requistion.requisitionNumber
            )
                ;
                if (requestNumberIndex != -1) {
                    currentKey = keys[requestNumberIndex];
                }
                return currentKey;
            }

            function loadGroupByRequisitionItems(requistion, reqItem, requisitionsMap) {
                var keys = reqItemsMap.keys();
                var items = [];
                if (keys.length > 0) {
                    var key = findRequisition(keys, requistion);
                    var requestNumberIndex = vm.groupedItems.findIndex(item = > item.materialItem.itemNumber == key
                )
                    ;
                    if (requestNumberIndex != -1) {
                        items = reqItemsMap.get(key);
                        if (items == null) {
                            items = [];
                        }
                        var newItems = [];
                        items.push(reqItem);
                        newItems.push(reqItem);
                        reqItemsMap.put(key, items);
                        var request = requisitionsMap.get(key);
                        groupItems(newItems, request, reqItem);
                    } else {
                        vm.groupedItems.push({
                            rowType: "req",
                            materialItem: {itemNumber: requistion.requisitionNumber}
                        });
                        items.push(reqItem);
                        reqItemsMap.put(key, items);
                        groupItems(items, requistion, reqItem);
                    }
                } else {
                    vm.groupedItems = [];
                    vm.groupedItems.push({rowType: "req", materialItem: {itemNumber: requistion.requisitionNumber}});
                    items.push(reqItem);
                    reqItemsMap.put(requistion.requisitionNumber, items);
                    groupItems(items, requistion, reqItem);
                }
            }

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'PO'});
            }

            function loadSuppliers() {
                SupplierService.getSuppliers(pageable).then(
                    function (data) {
                        vm.suppliers = data.content;
                    }, function (error) {

                    }
                );
            }

            /* ............. start attributes methods block ............. */

            function addAttachment(attribute) {
                var defered = $q.defer();
                vm.propertyAttachmentIds = [];
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'CUSTOM_PURCHASEORDER', attachmentFile).then(
                        function (data) {
                            vm.propertyAttachmentIds.push(data[0].id);
                            counter++;
                            if (counter == attribute.attachmentValues.length) {
                                defered.resolve(true);
                            }
                        }
                    )
                });
                return defered.promise;
            }

            function saveObjectAttributes() {
                var defered = $q.defer();
                if (vm.purchaseOrderAttributes.length > 0) {
                    angular.forEach(vm.purchaseOrderAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                    });
                    ObjectAttributeService.saveItemObjectAttributes(vm.newPurchaseOrder.id, vm.purchaseOrderAttributes).then(
                        function (data) {
                            var secCount = 0;
                            if (vm.propertyImageAttributes.length > 0) {
                                angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                    ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, vm.propertyImages.get(propImgAtt.id.attributeDef)).then(
                                        function (data) {
                                        });
                                    secCount++;
                                    if (secCount == vm.propertyImageAttributes.length) {
                                        defered.resolve();
                                    }
                                });
                            } else {
                                defered.resolve();
                            }
                        },
                        function (error) {
                            defered.reject();
                        }
                    )
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function attributesValidate() {
                var defered = $q.defer();
                $rootScope.closeNotification();
                vm.objectAttributes = [];
                vm.validObjectAttributes = [];
                vm.validattributes = [];
                angular.forEach(vm.requiredAttributes, function (attribute) {
                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                        if (checkAttribute(attribute)) {
                            vm.validattributes.push(attribute);
                        }
                        else {
                            $rootScope.showErrorMessage(attribute.attributeDef.name + ": attribute is required");
                            $rootScope.hideBusyIndicator();
                        }
                    } else {
                        vm.validattributes.push(attribute);
                    }
                });
                vm.objectAttributes = [];
                if (vm.purchaseOrderAttributes != null && vm.purchaseOrderAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.purchaseOrderAttributes);
                }
                angular.forEach(vm.objectAttributes, function (attribute) {
                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                        if (checkAttribute(attribute)) {
                            vm.validObjectAttributes.push(attribute);
                        }
                        else {
                            $rootScope.showErrorMessage(attribute.attributeDef.name + ": attribute is required");
                            $rootScope.hideBusyIndicator();
                        }
                    } else {
                        vm.validObjectAttributes.push(attribute);
                    }
                });
                if (vm.requiredAttributes.length == vm.validattributes.length && vm.objectAttributes.length == vm.validObjectAttributes.length) {
                    defered.resolve();
                } else {
                    defered.reject();
                }
                return defered.promise;
            }

            function loadObjectAttributeDefs() {
                vm.purchaseOrderAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("CUSTOM_PURCHASEORDER").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newPurchaseOrder.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                listValue: null,
                                newListValue: null,
                                timeValue: null,
                                timestampValue: moment(new Date()).format("DD/MM/YYYY, HH:mm:ss"),
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.required == false) {
                                vm.attributes.push(att);
                            } else {
                                vm.requiredAttributes.push(att);
                            }

                            vm.purchaseOrderAttributes.push(att);
                        });
                    }, function (error) {

                    });
            }

            function checkAttribute(attribute) {
                if ((attribute.stringValue != null && attribute.stringValue != undefined && attribute.stringValue != "") ||
                    (attribute.integerValue != null && attribute.integerValue != undefined && attribute.integerValue != "") ||
                    (attribute.doubleValue != null && attribute.doubleValue != undefined && attribute.doubleValue != "") ||
                    (attribute.dateValue != null && attribute.dateValue != undefined && attribute.dateValue != "") ||
                    (attribute.imageValue != null && attribute.imageValue != undefined && attribute.imageValue != "") ||
                    (attribute.currencyValue != null && attribute.currencyValue != undefined && attribute.currencyValue != "") ||
                    (attribute.timeValue != null && attribute.timeValue != undefined && attribute.timeValue != "") ||
                    (attribute.attachmentValues.length != 0) ||
                    (attribute.refValue != null && attribute.refValue != undefined && attribute.refValue != "") ||
                    (attribute.listValue != null && attribute.listValue != undefined && attribute.listValue != "")) {
                    return true;
                } else {
                    return false;
                }
            }

            function intializationForAttributesSave() {
                var defered = $q.defer();
                var attrCount = 0;
                vm.propertyImageAttributes = [];
                vm.propertyImages = new Hashtable();
                vm.imageAttributes = [];
                vm.images = new Hashtable();
                vm.requiredAttributes = [];

                if (vm.purchaseOrderAttributes.length == 0) {
                    defered.resolve();
                } else {
                    angular.forEach(vm.purchaseOrderAttributes, function (attribute) {
                        attribute.id.objectId = vm.newPurchaseOrder.id;
                        if (attribute.attributeDef.dataType == "IMAGE" && attribute.imageValue != null) {
                            vm.propertyImages.put(attribute.attributeDef.id, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            addAttachment(attribute).then(
                                function (data) {
                                    attribute.attachmentValues = vm.propertyAttachmentIds;
                                    attrCount++;
                                    if (attrCount == vm.purchaseOrderAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (data) {
                                                vm.purchaseOrderAttributes = [];
                                                loadObjectAttributeDefs();
                                                defered.resolve();
                                            }, function (error) {
                                                defered.reject();
                                            }
                                        )
                                    }
                                });
                        } else {
                            attrCount++;
                            if (attrCount == vm.purchaseOrderAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        vm.purchaseOrderAttributes = [];
                                        loadObjectAttributeDefs();
                                        defered.resolve();
                                    }, function (error) {
                                        defered.reject();
                                    }
                                )
                            }
                        }
                    });
                }
                return defered.promise;
            }

            /* ............. attributes methods end block ............. */

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

            (function () {
                loadObjectAttributeDefs();
                loadSuppliers();
                resize();
            })();
        }
    }
);