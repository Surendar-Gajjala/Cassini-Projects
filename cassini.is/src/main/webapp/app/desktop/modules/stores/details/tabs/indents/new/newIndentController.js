/**
 * Created by Rajabrahmachary on 14/09/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/store/customIndentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController'

    ],
    function (module) {
        module.controller('NewIndentController', NewIndentController);

        function NewIndentController($scope, $rootScope, $window, $state, AutonumberService, $timeout, $application, AttributeAttachmentService, $stateParams, DialogService, ObjectTypeAttributeService, $q, ObjectAttributeService, ProjectService, CustomIndentService) {

            $rootScope.viewInfo.icon = "fa fa-indent";
            $rootScope.viewInfo.title = $stateParams.mode == 'edit' || $stateParams.mode == 'approved' ? "Indent Details" : "New Indent";

            var vm = this;

            vm.mode = $stateParams.mode;
            vm.buttonText = $stateParams.mode == 'edit' || $stateParams.mode == 'approved' ? "Update" : "Create";
            vm.showAddRequestsButton = false;
            vm.showCreateIndentButton = false;
            vm.editMode = vm.mode == 'new' ? true : false;
            vm.loading = false;

            vm.openRequestsDialogue = openRequestsDialogue;
            vm.back = back;
            vm.createIndent = createIndent;
            vm.editIndentDetails = editIndentDetails;
            vm.editIndentItem = editIndentItem;
            vm.applyChanges = applyChanges;
            vm.cancelChanges = cancelChanges;
            vm.deleteIndentItem = deleteIndentItem;
            vm.projectChanged = projectChanged;
            vm.approveIndent = approveIndent;
            vm.removeFromIndentItems = removeFromIndentItems;
            vm.generateAutoNumber = generateAutoNumber;

            var reqItemsMap = new Hashtable();
            vm.groupedItems = [];
            vm.indentRequests = [];
            vm.projects = [];
            vm.requiredAttributes = [];
            vm.attributes = [];

            vm.indentItem = {
                id: null,
                customIndent: null,
                requisition: null,
                materialItem: null,
                indentItemQuantity: null,
                indentItemNotesObject: null,
                reqItemQuantity: null,
                quantity: null,
                notes: null
            }

            vm.newIndent = {
                id: null,
                projectObject: null,
                raisedDateObject: null,
                raisedByObject: null,
                approvedByObject: null,
                status: null,
                notesObject: null,
                project: null,
                indentNumber: null,
                store: $stateParams.storeId,
                raisedDate: null,
                approvedBy: null,
                notes: null,
                customIndentItems: []
            }

            function approveIndent() {
                if (validateIndent()) {
                    vm.newIndent.status = 'APPROVED';
                    CustomIndentService.updateIndent(vm.newIndent).then(
                        function (data) {
                            $rootScope.showSuccessMessage("Indent (" + data.indentNumber + ") approved successfully");
                        }, function (error) {

                        });
                }
            }

            function editIndentDetails() {
                vm.editMode = true;
            }

            function editIndentItem(item) {
                item.editMode = true;
                item.showEditButton = false;
            }

            function applyChanges(item) {
                item.showEditButton = true;
                item.editMode = false;
                vm.indentItem.notes = item.indentItemNotesObject;
                vm.indentItem.quantity = item.indentItemQuantity;
            }

            function deleteIndentItem(item) {
                var options = {
                    title: 'Delete Indent Item',
                    message: 'Are you sure you want to delete this Indent Item?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        removeFromIndentItems(item);
                        CustomIndentService.deleteIndentItem(vm.newIndent.id, item.id).then(
                            function (data) {
                                $rootScope.showWarningMessage("Indent Item deleted successfully");
                            }, function (error) {

                            }
                        )
                    }
                });
            }

            function cancelChanges(item) {
                item.showEditButton = true;
                item.editMode = false;
                item.indentItemNotesObject = item.notes;
                item.indentItemQuantity = item.quantity;
            }

            function generateAutoNumber() {
                var prefix = $application.config.autoNumber.prefix;
                var year = $application.config.autoNumber.year;
                AutonumberService.getAutonumberByName('Default Indent Number Source').then(
                    function (data) {
                        AutonumberService.getNextNumber(data.id).then(
                            function (data) {
                                vm.generatedNumber = data;
                                if (prefix != "" && year != "") {
                                    vm.newIndent.indentNumber = prefix + "/ " + $rootScope.selectedStore.storeName + "/" + year + "/" + vm.generatedNumber;
                                }
                                else {
                                    vm.newIndent.indentNumber = vm.generatedNumber;
                                }
                            }
                        )
                    });
            }

            function removeFromIndentItems(indentItem) {
                var indentItemIndex = vm.groupedItems.findIndex(item = > item.materialItem.id == indentItem.materialItem.id
            )
                ;
                var items = reqItemsMap.get(indentItem.requisition.requisitionNumber);
                if (items.length == 1) {
                    if (indentItemIndex != -1) {
                        vm.groupedItems.splice(indentItemIndex, 1);
                    }
                    var requestNumberIndex = vm.groupedItems.findIndex(item = > item.materialItem.itemNumber == indentItem.requisition.requisitionNumber
                )
                    ;
                    if (requestNumberIndex != -1) {
                        vm.groupedItems.splice(requestNumberIndex, 1);
                        reqItemsMap.remove(indentItem.requisition.requisitionNumber);
                    }
                } else {
                    if (indentItemIndex != -1) {
                        vm.groupedItems.splice(indentItemIndex, 1);
                        items = reqItemsMap.get(indentItem.requisition.requisitionNumber);
                        var removableItemIndex = items.findIndex(item = > item.materialItem.id == indentItem.materialItem.id
                    )
                        ;
                        if (removableItemIndex != -1) {
                            items.splice(removableItemIndex, 1);
                            reqItemsMap.put(indentItem.requisition.requisitionNumber, items);
                        }
                    }
                }

                if (vm.groupedItems.length == 0) {
                    vm.showCreateIndentButton = false;
                }
            }

            function projectChanged() {
                vm.showAddRequestsButton = true;
            }

            function createIndent() {
                if (validateIndent()) {
                    attributesValidate().then(
                        function (success) {
                            CustomIndentService.createIndent(vm.newIndent).then(
                                function (data) {
                                    vm.showCreateIndentButton = false;
                                    vm.editMode = false;
                                    vm.newIndent.status = data.status;
                                    vm.newIndent.id = data.id;
                                    intializationForAttributesSave().then(
                                        function (success) {
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.indentId = vm.newIndent.id;
                                            $state.go('app.store.stock.indentDetails', {
                                                storeId: $rootScope.storeId,
                                                indentId: vm.newIndent.id
                                            });
                                            $rootScope.showSuccessMessage("Indent (" + vm.newIndent.indentNumber + ") created successfully");
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

            function openRequestsDialogue() {
                var options = {
                    title: 'Items',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/indents/new/indentRequestsView.jsp',
                    controller: 'IndentRequestsController as indentRequestsVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/indents/new/indentRequestsController',
                    width: 700,
                    data: {
                        projectObj: vm.newIndent.projectObject,
                        groupedItems: vm.groupedItems,
                        requestItemsMap: reqItemsMap
                    },
                    buttons: [],
                    callback: function (requistion, reqItem, requisitionsMap) {
                        loadGroupByRequisitionItems(requistion, reqItem, requisitionsMap);
                    }
                };

                $rootScope.showSidePanel(options);
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

            function groupItems(items, requistion, reqItem) {
                angular.forEach(items, function (item) {
                    var indentItem = angular.copy(vm.indentItem);
                    indentItem.rowType = "item";
                    indentItem.id = item.id;
                    indentItem.requisition = requistion;
                    indentItem.materialItem = item.materialItem;
                    indentItem.reqItemQuantity = item.quantity;
                    indentItem.indentItemQuantity = item.quantity;
                    indentItem.notes = item.notes;
                    indentItem.quantity = item.quantity;
                    indentItem.isNew = item.isNew;
                    indentItem.editMode = item.editMode;
                    indentItem.showEditButton = item.showEditButton;
                    indentItem.indentItemNotesObject = indentItem.isNew == true ? null : item.notes;
                    vm.groupedItems.push(indentItem);
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

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'IND'});
            }

            function loadProjects() {
                ProjectService.getAllProjects().then(
                    function (data) {
                        vm.projects = data;
                    }, function (error) {

                    }
                );
            }

            function validateIndent() {
                var valid = false;
                if (vm.newIndent.projectObject == null || vm.newIndent.projectObject == undefined || vm.newIndent.projectObject == "") {
                    valid = false;
                    $rootScope.showWarningMessage("Please select Project");
                } else if (vm.newIndent.raisedDateObject == null || vm.newIndent.raisedDateObject == undefined || vm.newIndent.raisedDateObject == "") {
                    valid = false;
                    $rootScope.showWarningMessage("Please select Created Date");
                } else if (vm.newIndent.raisedByObject == null || vm.newIndent.raisedByObject == undefined || vm.newIndent.raisedByObject == "") {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter CreatedBy");
                } else if (vm.newIndent.status == 'NEW') {
                    if (vm.newIndent.approvedByObject == null || vm.newIndent.approvedByObject == undefined || vm.newIndent.approvedByObject == "") {
                        valid = false;
                        $rootScope.showWarningMessage("Please enter ApprovedBy");
                    } else {
                        vm.newIndent.approvedBy = vm.newIndent.approvedByObject;
                        vm.newIndent.project = vm.newIndent.projectObject;
                        vm.newIndent.raisedDate = vm.newIndent.raisedDateObject;
                        vm.newIndent.store = $stateParams.storeId;
                        vm.newIndent.raisedBy = vm.newIndent.raisedByObject;
                        vm.newIndent.notes = vm.newIndent.notesObject;
                        if (vm.groupedItems.length > 0) {
                            angular.forEach(vm.groupedItems, function (grpItem) {
                                if (grpItem.rowType == 'item') {
                                    grpItem.quantity = grpItem.indentItemQuantity;
                                    grpItem.notes = grpItem.indentItemNotesObject;
                                    vm.newIndent.customIndentItems.push(grpItem);
                                }
                            });
                            valid = true;
                        } else {
                            $rootScope.showWarningMessage("Please add atleast one Item to Indent");
                            valid = false;
                        }
                    }
                } else {
                    vm.newIndent.project = vm.newIndent.projectObject;
                    vm.newIndent.raisedDate = vm.newIndent.raisedDateObject;
                    vm.newIndent.store = $stateParams.storeId;
                    vm.newIndent.raisedBy = vm.newIndent.raisedByObject;
                    vm.newIndent.notes = vm.newIndent.notesObject;
                    if (vm.groupedItems.length > 0) {
                        angular.forEach(vm.groupedItems, function (grpItem) {
                            if (grpItem.rowType == 'item') {
                                grpItem.quantity = grpItem.indentItemQuantity;
                                grpItem.notes = grpItem.indentItemNotesObject;
                                vm.newIndent.customIndentItems.push(grpItem);
                            }
                        });
                        valid = true;
                    } else {
                        $rootScope.showWarningMessage("Please add atleast one Item to Indent");
                        valid = false;
                    }
                }
                return valid;
            }

            /* ............. start indent attributes methods block ............. */

            function addAttachment(attribute) {
                var defered = $q.defer();
                vm.propertyAttachmentIds = [];
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'CUSTOM_INDENT', attachmentFile).then(
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
                if (vm.indentAttributes.length > 0) {
                    angular.forEach(vm.indentAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                    });
                    ObjectAttributeService.saveItemObjectAttributes(vm.newIndent.id, vm.indentAttributes).then(
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
                if (vm.indentAttributes != null && vm.indentAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.indentAttributes);
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
                vm.indentAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("CUSTOM_INDENT").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newIndent.id,
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

                            vm.indentAttributes.push(att);
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
                if (vm.indentAttributes.length == 0) {
                    defered.resolve();
                }
                else {
                    angular.forEach(vm.indentAttributes, function (attribute) {
                        attribute.id.objectId = vm.newIndent.id;
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
                                    if (attrCount == vm.indentAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (data) {
                                                vm.indentAttributes = [];
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
                            if (attrCount == vm.indentAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        vm.indentAttributes = [];
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

            /* ............. indent attributes methods end block ............. */

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
                loadProjects();
                loadObjectAttributeDefs();
                resize();
            })();
        }
    }
);