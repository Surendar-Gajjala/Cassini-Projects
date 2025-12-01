define(
    [
        'app/desktop/modules/inward/inward.module',
        'jquery.easyui',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/inwardService',
        'app/shared/services/core/bomService',
        'app/shared/services/core/allocationService',
        'app/shared/services/core/procurementService',
        'app/desktop/modules/bom/directive/bomTreeDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('InwardDetailsController', InwardDetailsController);

        function InwardDetailsController($scope, $window, $rootScope, $timeout, $state, $stateParams, $cookies, ItemTypeService,
                                         ItemService, $uibModal, $interval, AttachmentService, InwardService,
                                         CommonService, DialogService, BomService, AllocationService, ProcurementService, ObjectAttributeService, AttributeAttachmentService) {

            $rootScope.viewInfo.icon = "fa fa-sign-in";
            $rootScope.viewInfo.title = "Inward Details";

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;
            vm.mode = $stateParams.mode;
            vm.inwardProperties = [];
            vm.showSearchBox = false;
            vm.showUpdateButton = false;
            vm.showItemColumns = true;

            var currencyMap = new Hashtable();

            vm.preventClick = preventClick;
            vm.performSearch = performSearch;
            vm.showInwardInfoPanel = showInwardInfoPanel;
            vm.onSelectType = onSelectType;
            vm.back = back;
            vm.updateInward = updateInward;
            vm.addItemToInward = addItemToInward;
            vm.removeInwardItem = removeInwardItem;
            vm.saveInwardItems = saveInwardItems;
            vm.downloadGatePass = downloadGatePass;
            vm.createInwardItem = createInwardItem;
            vm.saveInwardItem = saveInwardItem;
            vm.toggleRow = toggleRow;
            vm.updateUPN = updateUPN;
            vm.showLotAllocationPanel = showLotAllocationPanel;
            vm.showInwardItemColumns = showInwardItemColumns;

            vm.editInwardItemInstance = editInwardItemInstance;
            vm.cancelInwardItemInstanceEdit = cancelInwardItemInstanceEdit;
            vm.saveInwardItemInstance = saveInwardItemInstance;
            vm.acceptInwardItem = acceptInwardItem;
            vm.provAcceptInwardItem = provAcceptInwardItem;

            vm.acceptInwardItemInstance = acceptInwardItemInstance;
            vm.provAcceptInwardItemInstance = provAcceptInwardItemInstance;
            vm.returnItemInstance = returnItemInstance;

            vm.allocateStorageToAll = allocateStorageToAll;
            vm.allocateStorage = allocateStorage;
            vm.verifyItem = verifyItem;

            vm.buttonTitle = "Click to save";
            vm.buttonName = "Save";

            vm.searchFilter = {
                searchQuery: null,
                bom: ''
            };

            var pageable = {
                page: 0,
                size: 30,
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


            vm.emptyInwardItem = {
                id: null,
                inward: null,
                bomItem: null,
                quantity: null,
                fractionalQuantity: null
            };

            vm.inwardItems = [];
            vm.inwardItemRows = [];
            vm.searchResults = angular.copy(pagedResults);
            vm.loading = false;

            function back() {
                if (vm.mode == 'home') {
                    $state.go('app.home');
                } else if (vm.mode == 'inwards') {
                    $state.go('app.inwards.all');
                } else {
                    $state.go('app.inwards.all');
                }
            }

            function preventClick(event) {
                event.stopPropagation();
                event.preventDefault();
            }

            function initSearchBox() {
                $(document).click(function () {
                    $('#inwardDetailsSearchResults').hide();
                });
                $(document).on('keydown', function (evt) {
                    if (evt.keyCode == 27) {
                        $('#inwardDetailsSearchResults').hide();
                    }
                });

                $timeout(function () {
                    $('#inwardDetailsSearchResults').click(function (event) {
                        event.stopPropagation();
                        event.preventDefault();
                    });
                }, 1000);
            }

            function showInwardInfoPanel(show) {
                if (show) {
                    $('#inwardInfoPanel').show('slide', {direction: 'left'}, 600);
                    if (vm.inwardItems.length == 0) {
                        vm.showUpdateButton = true;
                        vm.buttonName = "Save";
                        vm.buttonTitle = "Click to save";
                    }
                }
                else {
                    $('#inwardInfoPanel').hide('slide', {direction: 'left'}, 600);
                    if (vm.inwardItems.length == 0) {
                        vm.showUpdateButton = false;
                        vm.buttonName = "Submit";
                        vm.buttonTitle = "Click to submit";
                    }
                }
            }

            function showInwardItemColumns() {
                vm.showItemColumns = !vm.showItemColumns;
            }

            function onSelectType(bom) {
                if (bom != undefined) {
                    vm.inward.bom = bom;
                }
            }

            vm.previousPage = previousPage;
            vm.nextPage = nextPage;

            function previousPage() {
                pageable.page--;
                searchInwardItems();
            }

            function nextPage() {
                pageable.page++;
                searchInwardItems();
            }

            function performSearch() {
                if (vm.searchFilter.searchQuery == "") {
                    $('#inwardDetailsSearchResults').hide();
                }
                if (vm.searchFilter.searchQuery != "") {
                    showInwardInfoPanel(false);
                    if (vm.searchFilter.searchQuery.length > 2) {
                        $('#inwardDetailsSearchResults').show();
                        vm.searchFilter.bom = vm.inward.bom.id;
                        pageable.page = 0;
                        var height = $('.view-content').outerHeight();
                        var width = $('.view-content').width();
                        $('#inwardDetailsSearchResults').height(height);
                        $('#inwardDetailsSearchResults').width(width / 2);
                        searchInwardItems();
                    } else {
                        $('#inwardDetailsSearchResults').hide();
                    }
                }
            }

            function checkIfAlreadyAdded(item) {
                var found = false;
                angular.forEach(vm.inwardItems, function (inwardItem) {
                    if (inwardItem.bomItem.id == item.id) {
                        found = true;
                    }
                });

                return found;
            }

            function searchInwardItems() {
                $rootScope.showBusyIndicator($("#inwardDetailsSearchResults"));
                BomService.searchInwardItems(vm.searchFilter, pageable).then(
                    function (data) {

                        vm.searchResults = data;

                        /*vm.searchResultsData = angular.copy(data);
                         vm.searchResults = angular.copy(data);
                         vm.searchResults.content = [];
                         angular.forEach(vm.searchResultsData.content, function (item) {
                         if (!checkIfAlreadyAdded(item)) {
                         vm.searchResults.content.push(item);
                         }
                         })*/
                        $rootScope.hideBusyIndicator();

                    }
                )
            }

            function addItemToInward(item) {
                var newInwardItem = angular.copy(vm.emptyInwardItem);
                newInwardItem.inward = vm.inward.id;
                newInwardItem.bomItem = item;
                newInwardItem.instances = [];
                newInwardItem.level = 0;
                newInwardItem.type = 'INWARDITEM';

                vm.searchResults.content.splice(vm.searchResults.content.indexOf(item), 1);

                /*vm.searchResults.totalElements = vm.searchResults.totalElements - 1;
                 vm.searchResults.numberOfElements = vm.searchResults.totalElements - 1;*/

                vm.inwardItems.push(newInwardItem);
                updateRows();
            }

            function removeInwardItem(item) {
                vm.inwardItems.splice(vm.inwardItems.indexOf(item), 1);
                vm.inwardItemRows.splice(vm.inwardItemRows.indexOf(item), 1);
            }

            var newValue = {
                id: {
                    objectId: null,
                    attributeDef: null
                },
                stringValue: null,
                integerValue: null,
                doubleValue: null,
                booleanValue: null,
                dateValue: null,
                listValue: null,
                newListValue: null,
                listValueEditMode: false,
                timeValue: null,
                timestampValue: null,
                refValue: null,
                imageValue: null,
                attachmentValues: [],
                currencyValue: null,
                currencyType: null
            }

            function loadInwardItemInstancesAttributes() {
                vm.itemInstanceAttributes = [];
                $timeout(function () {
                    $rootScope.showBusyIndicator($('.view-container'));
                }, 100);
                ItemTypeService.getAttributesByObjectType("ITEMINSTANCE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: null,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                value: {
                                    id: {
                                        objectId: null,
                                        attributeDef: attribute.id
                                    },
                                    stringValue: null,
                                    integerValue: null,
                                    doubleValue: null,
                                    booleanValue: null,
                                    dateValue: null,
                                    listValue: null,
                                    newListValue: null,
                                    listValueEditMode: false,
                                    timeValue: null,
                                    timestampValue: null,
                                    refValue: null,
                                    imageValue: null,
                                    attachmentValues: [],
                                    currencyValue: null,
                                    currencyType: null
                                },
                                stringValue: null,
                                integerValue: null,
                                doubleValue: null,
                                booleanValue: null,
                                dateValue: null,
                                listValue: null,
                                newListValue: null,
                                listValueEditMode: false,
                                timeValue: null,
                                timestampValue: null,
                                refValue: null,
                                imageValue: null,
                                attachmentValues: [],
                                currencyValue: null,
                                currencyType: null
                            };
                            vm.itemInstanceAttributes.push(att);
                        });
                        loadInward();
                    }
                )
            }

            function loadInward() {
                $rootScope.showBusyIndicator($('.view-container'));
                InwardService.getInward($stateParams.inwardId).then(
                    function (data) {
                        vm.inward = data;

                        if (vm.inward.bom != null) {
                            $rootScope.viewInfo.description = "BOM - " + vm.inward.bom.item.itemMaster.itemName;
                            vm.showSearchBox = true;
                            vm.buttonName = "Submit";
                            vm.buttonTitle = "Click to submit";
                            vm.showUpdateButton = false;
                        } else {
                            showInwardInfoPanel(true);
                        }
                        loadInwardItems();
                        loadInwardAttributes();
                        showStatuses();
                        loadManufacturers();
                    }
                )
            }

            function loadInwardItems() {
                vm.loading = true;
                InwardService.getInwardItems($stateParams.inwardId).then(
                    function (data) {
                        vm.inwardItems = data;

                        angular.forEach(vm.inwardItems, function (item) {
                            item.level = 0;
                            item.type = 'INWARDITEM';
                            item.expanded = true;
                            if (item.instances.length == 1) {
                                item.hideAcceptAll = true;
                                item.hidePAcceptAll = true;
                            }
                            var submittedInstances = 0;
                            var noneInventoryInstances = 0;
                            angular.forEach(item.instances, function (instance) {

                                angular.forEach(vm.itemInstanceAttributes, function (attribute) {
                                    var attributeData = angular.copy(attribute);
                                    instance[attributeData.attributeDef.name] = attributeData;
                                })

                                if (instance.item.status == "STORE_SUBMITTED") {
                                    submittedInstances++;
                                }
                                if (instance.item.status == "ACCEPT" || instance.item.status == "P_ACCEPT") {
                                    noneInventoryInstances++;
                                }
                                instance.bomItem = item.bomItem;
                                instance.level = 1;
                                instance.type = 'INWARDITEMINSTANCE';
                                instance.quantity = 1;
                                instance.expanded = false;
                                instance.instancesCreated = true;
                                instance.editMode = false;
                                instance.item.presentReason = instance.item.reason;
                            });

                            if (submittedInstances == 1 || submittedInstances == 0) {
                                item.hideAcceptAll = true;
                                item.hidePAcceptAll = true;
                            }

                            if (noneInventoryInstances == 0 || noneInventoryInstances == 1) {
                                item.hideAllocateAll = true;
                            }
                        });
                        vm.loading = false;
                        updateRows();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function showStatuses() {
                var currentPhase = null;
                InwardService.getInward($stateParams.inwardId).then(
                    function (data) {
                        vm.inward = data;

                        var statuses = [];
                        currentPhase = vm.inward.status;
                        var underReviewState = vm.inward.underReview;
                        var defs = ['SECURITY', 'STORE', 'SSQAG', 'INVENTORY', 'FINISH'];
                        defs.sort(function (a, b) {
                            return a.id - b.id;
                        });
                        angular.forEach(defs, function (def) {
                            if (def == 'FINISH' && currentPhase == 'FINISH') {
                                statuses.push({
                                    name: def,
                                    finished: true,
                                    current: (def == currentPhase),
                                    underReview: underReviewState
                                })
                            } else {
                                statuses.push({
                                    name: def,
                                    finished: false,
                                    current: (def == currentPhase),
                                    underReview: underReviewState
                                })
                            }

                        });

                        var index = -1;
                        for (var i = 0; i < statuses.length; i++) {
                            if (statuses[i].current == true) {
                                index = i;
                            }
                        }

                        if (index > 0) {
                            for (i = 0; i < index; i++) {
                                statuses[i].finished = true;
                            }
                        }
                        $rootScope.setStatuses(statuses);
                    }
                );
                /*$timeout(function () {
                 if (currentPhase == 'FINISH') {
                 doPlanning();
                 }
                 }, 2000);*/
            }

            function doPlanning() {
                var missileIds = [];
                ItemService.getItemInstances(vm.inward.bom.item.id).then(
                    //ItemService.getItemInstancesFrom20(vm.inward.bom.item.id).then(
                    function (data) {
                        angular.forEach(data, function (miss) {
                            missileIds.push(miss.id);
                        });
                        AllocationService.loadBomChildrenWithOutSecAllocation(vm.inward.bom.id, missileIds, missileIds).then(
                            function (data) {
                                AllocationService.planSelectedMissiles(missileIds).then(
                                    function (data) {


                                    })
                            })
                    }
                )
            }

            function loadManufacturers() {
                ProcurementService.getManufacturers().then(
                    function (data) {
                        vm.manufacturers = data;
                    }
                )
            }

            function loadInwardAttributes() {
                ItemTypeService.getAttributesByObjectType("INWARD").then(
                    function (data) {
                        vm.inwardProperties = [];
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.inward.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                value: {
                                    id: {
                                        objectId: vm.inward.id,
                                        attributeDef: attribute.id
                                    },
                                    stringValue: null,
                                    integerValue: null,
                                    doubleValue: null,
                                    booleanValue: null,
                                    dateValue: null,
                                    listValue: null,
                                    newListValue: null,
                                    listValueEditMode: false,
                                    timeValue: null,
                                    timestampValue: null,
                                    refValue: null,
                                    imageValue: null,
                                    attachmentValues: [],
                                    currencyValue: null,
                                    currencyType: null
                                },
                                changeImage: false,
                                imageValue: null,
                                newImageValue: null,
                                timeValue: null,
                                showAttachment: false,
                                attachmentValues: [],
                                showTimeAttribute: false,
                                showTimestamp: false,
                                timestampValue: null,
                                editMode: false,
                                changeCurrency: false
                            };
                            vm.inwardProperties.push(att);
                        });

                        loadObjectAttributes();
                    }
                )
            }

            function loadObjectAttributes() {
                ObjectAttributeService.getAllObjectAttributes(vm.inward.id).then(
                    function (data) {
                        var map = new Hashtable();

                        angular.forEach(data, function (attribute) {
                            map.put(attribute.id.attributeDef, attribute);
                        });

                        angular.forEach(vm.inwardProperties, function (attribute) {
                            var attachmentIds = [];
                            var value = map.get(attribute.attributeDef.id);
                            if (value != null) {
                                if (value.attachmentValues.length > 0) {
                                    angular.forEach(value.attachmentValues, function (attachment) {
                                        attachmentIds.push(attachment);
                                    });
                                    AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                        function (data) {
                                            vm.itemPropertyAttachments = data;
                                            attribute.value.attachmentValues = vm.itemPropertyAttachments;
                                        }
                                    )
                                }

                                attribute.value.stringValue = value.stringValue;
                                attribute.value.integerValue = value.integerValue;
                                attribute.value.booleanValue = value.booleanValue;
                                if (value.doubleValue != null) {
                                    attribute.value.doubleValue = parseFloat(value.doubleValue).toFixed(5);
                                } else {
                                    attribute.value.doubleValue = value.doubleValue;
                                }
                                attribute.value.dateValue = value.dateValue;
                                attribute.value.listValue = value.listValue;
                                attribute.value.timeValue = value.timeValue;
                                attribute.value.timestampValue = value.timestampValue;
                                attribute.value.imageValue = value.imageValue;
                                attribute.value.currencyValue = value.currencyValue;
                                if (value.currencyType != null) {
                                    attribute.value.currencyType = value.currencyType;
                                    attribute.value.encodedCurrencyType = currencyMap.get(value.currencyType);
                                }
                                attribute.value.itemImagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                            }
                        });
                    }
                )
            }

            vm.editAttachment = editAttachment;
            vm.cancelChanges = cancelChanges;
            vm.saveInwardAttachment = saveInwardAttachment;
            vm.deleteInwardAttachment = deleteInwardAttachment;

            function editAttachment(attribute) {
                attribute.editMode = true;
            }

            function cancelChanges(attribute) {
                attribute.editMode = false;
            }


            function deleteInwardAttachment(attachment) {
                AttributeAttachmentService.deleteAttributeAttachment(attachment.id).then(
                    function (data) {
                        loadInwardAttributes();
                        $rootScope.hideBusyIndicator();
                        $rootScope.showSuccessMessage(attachment.name + ' : Attachment deleted successfully');
                    }
                )
            }

            function saveInwardAttachment(attribute) {
                var inwardAttachmentIds = [];

                attribute.value.attachmentValues = [];

                AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'INWARD', attribute.attachmentValues).then(
                    function (data) {
                        inwardAttachmentIds.push(data[0].id);
                        if (inwardAttachmentIds.length > 0) {
                            angular.forEach(inwardAttachmentIds, function (revAttachId) {
                                attribute.value.attachmentValues.push(revAttachId);
                            });
                            ObjectAttributeService.updateObjectAttribute(vm.inward.id, attribute.value).then(
                                function (data) {
                                    $rootScope.hideBusyIndicator();
                                    loadInwardAttributes();
                                    attribute.editMode = false;
                                    $rootScope.showSuccessMessage("Attachment saved successfully");
                                }
                            )
                        }
                    }
                )

            }

            function updateInward() {
                if (validateInward()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    InwardService.updateInwardData(vm.inward).then(
                        function (data) {
                            vm.inward = data;
                            vm.showInwardInfoPanel(false);
                            vm.buttonName = "Submit";
                            vm.buttonTitle = "Click to submit";
                            $rootScope.showSuccessMessage("Inward updated successfully");
                            $rootScope.hideBusyIndicator();
                            loadInward();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }

            }

            function validateInward() {
                var valid = true;
                if (vm.inward.bom == null || vm.inward.bom == "" || vm.inward.bom == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select BOM");
                } else if (vm.inward.supplier == null || vm.inward.supplier == "" || vm.inward.supplier == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select Supplier");
                }

                return valid;
            }

            function updateRows() {
                vm.inwardItemRows = [];
                var instanceIds = [];
                var attributeDefs = [];
                angular.forEach(vm.itemInstanceAttributes, function (attribute) {
                    if (attribute.attributeDef.id != null && attribute.attributeDef.id != "" && attribute.attributeDef.id != 0) {
                        attributeDefs.push(attribute.attributeDef.id);
                    }
                });

                angular.forEach(vm.inwardItems, function (item) {
                    vm.inwardItemRows.push(item);
                    if (item.expanded) {
                        angular.forEach(item.instances, function (instance) {
                            vm.inwardItemRows.push(instance);
                            instanceIds.push(instance.item.id);
                        });
                    }
                });

                if (instanceIds.length > 0 && attributeDefs.length > 0) {
                    InwardService.getAttributesByItemIdAndAttributeId(instanceIds, attributeDefs).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.itemInstanceAttributes, function (att) {
                                if (att.attributeDef.id != null && att.attributeDef.id != "" && att.attributeDef.id != 0) {
                                    map.put(att.attributeDef.id, att);
                                }
                            });

                            angular.forEach(vm.inwardItemRows, function (inward) {
                                if (inward.type != "INWARDITEM") {
                                    var attributes = [];
                                    attributes = vm.selectedObjectAttributes[inward.item.id];
                                    if (attributes == null || attributes == undefined) {
                                        inward.showApplyAll = false;
                                    }
                                    angular.forEach(attributes, function (attribute) {
                                        var selectAtt = map.get(attribute.id.attributeDef);
                                        if (selectAtt != null) {
                                            inward.showApplyAll = true;
                                            inward.applyAllChecked = false;
                                            var attributeName = selectAtt.attributeDef.name;
                                            inward[attributeName].value = attribute;
                                            if (selectAtt.attributeDef.dataType == 'ATTACHMENT') {
                                                var revisionAttachmentIds = [];
                                                if (attribute.attachmentValues.length > 0) {
                                                    angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                        revisionAttachmentIds.push(attachmentId);
                                                    });
                                                    AttributeAttachmentService.getMultipleAttributeAttachments(revisionAttachmentIds).then(
                                                        function (data) {
                                                            vm.revisionAttachments = data;
                                                            inward[attributeName].value.attachmentValues = vm.revisionAttachments;
                                                        }
                                                    )
                                                }
                                            } else if (selectAtt.attributeDef.dataType == 'IMAGE') {
                                                if (attribute.imageValue != null) {
                                                    inward[attributeName].imageValue = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                                }
                                            }
                                        }
                                    });


                                    $timeout(function () {
                                        angular.forEach(vm.inwardItems, function (inwardItem) {
                                            var showApplyAllNumber = 0;
                                            angular.forEach(inwardItem.instances, function (instance) {

                                                angular.forEach(vm.inwardItemRows, function (inwardRow) {
                                                    if (instance.id == inwardRow.id) {
                                                        if (inwardRow.showApplyAll) {
                                                            showApplyAllNumber++;
                                                        }
                                                    }
                                                })
                                            })

                                            if (inwardItem.instances.length == showApplyAllNumber) {
                                                angular.forEach(inwardItem.instances, function (instance) {
                                                    instance.showApplyAll = false;
                                                })
                                            }
                                        })
                                    }, 1000);
                                }
                            })
                            $timeout(function () {
                                $rootScope.hideBusyIndicator();
                            }, 100);
                        }
                    )
                } else {
                    $timeout(function () {
                        $rootScope.hideBusyIndicator();
                    }, 100)
                }
            }


            function saveInwardItems() {
                if (vm.showUpdateButton) {
                    updateInward();
                }
                else if (vm.inwardItems.length === 0) {
                    $rootScope.showWarningMessage("Please add at least one item");
                } else {
                    if (vm.inward.status === "STORE") {
                        if (validateQuantities() && validateOemNumbers()) {
                            var options = {
                                title: "Save Items",
                                message: "Please confirm to final submit",
                                okButtonClass: 'btn-danger'
                            };

                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    $rootScope.showBusyIndicator($('.view-container'));
                                    InwardService.updateInwardStatus(vm.inward).then(
                                        function (data) {
                                            loadInward();
                                            $rootScope.showSuccessMessage("Inward items submitted successfully");
                                            $rootScope.hideBusyIndicator();
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    );
                                }
                            });
                        }
                    } else if (vm.inward.status == "SSQAG") {
                        if (SSQAGValidate()) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            InwardService.updateInwardStatus(vm.inward).then(
                                function (data) {
                                    loadInward();
                                    $rootScope.showSuccessMessage("Inward items submitted successfully");
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            );
                        }
                    } else if (vm.inward.status == "INVENTORY") {
                        if (InventoryValidate()) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            InwardService.updateInwardStatus(vm.inward).then(
                                function (data) {
                                    loadInward();
                                    $rootScope.showSuccessMessage("Inward items submitted successfully");
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            );
                        }
                    }

                }
            }

            function validateQuantities() {
                var valid = true;
                angular.forEach(vm.inwardItems, function (inwardItem) {
                    if (valid) {
                        if (inwardItem.bomItem.item.itemMaster.itemType.hasLots && (inwardItem.fractionalQuantity == 0 || inwardItem.fractionalQuantity == null
                            || inwardItem.fractionalQuantity == "" || inwardItem.fractionalQuantity == undefined)) {

                            valid = false;
                            $rootScope.showWarningMessage("Please enter Quantity for " + inwardItem.bomItem.item.itemMaster.itemName);
                        } else if (!inwardItem.bomItem.item.itemMaster.itemType.hasLots && (inwardItem.fractionalQuantity == 0 || inwardItem.quantity == null
                            || inwardItem.quantity == "" || inwardItem.quantity == undefined)) {

                            valid = false;
                            $rootScope.showWarningMessage("Please enter Quantity for " + inwardItem.bomItem.item.itemMaster.itemName);
                        } else if (inwardItem.type == "INWARDITEM" && !inwardItem.instancesCreated) {
                            valid = false;
                            $rootScope.showWarningMessage("Please save " + inwardItem.bomItem.item.itemMaster.itemName);
                        }
                    }
                });

                return valid;
            }

            function validateOemNumbers() {
                var valid = true;
                angular.forEach(vm.inwardItems, function (inwardItem) {
                    if (valid) {
                        angular.forEach(inwardItem.instances, function (instance) {
                            if (instance.item.manufacturer == null || instance.item.manufacturer == "" || instance.item.manufacturer == undefined) {
                                valid = false;
                                $rootScope.showWarningMessage("Please select Manufacturer for " + inwardItem.bomItem.item.itemMaster.itemName);
                            } else if (instance.item.oemNumber == null || instance.item.oemNumber == "" || instance.item.oemNumber == undefined) {
                                valid = false;
                                $rootScope.showWarningMessage("Please enter Serial Number for " + inwardItem.bomItem.item.itemMaster.itemName);
                            } else if (instance.editMode || instance.checkBoxChecked) {
                                valid = false;
                                $rootScope.showWarningMessage("Please save the " + inwardItem.bomItem.item.itemMaster.itemName);
                            }
                        })

                        angular.forEach(vm.itemInstanceAttributes, function (attribute) {
                            if (valid) {
                                angular.forEach(inwardItem.instances, function (instance) {
                                    if (instance[attribute.attributeDef.name].attributeDef.required && !$rootScope.checkAttribute(instance[attribute.attributeDef.name].value)) {
                                        valid = false;
                                        $rootScope.showWarningMessage(inwardItem.bomItem.item.itemMaster.itemName + " item " + instance[attribute.attributeDef.name].attributeDef.name + " is required");
                                    }
                                })
                            }
                        });
                    }
                });

                return valid;
            }

            function SSQAGValidate() {
                var valid = true;
                angular.forEach(vm.inwardItems, function (inwardItem) {
                    angular.forEach(inwardItem.instances, function (instance) {
                        if (valid) {
                            if (instance.item.status == "STORE_SUBMITTED") {
                                valid = false;
                                $rootScope.showWarningMessage("Please Accept : " + inwardItem.bomItem.item.itemMaster.itemName + " item")
                            }
                        }
                    })
                });

                return valid;
            }

            function InventoryValidate() {
                var valid = true;
                angular.forEach(vm.inwardItems, function (inwardItem) {
                    if (valid) {
                        angular.forEach(inwardItem.instances, function (instance) {
                            if (valid) {
                                if (instance.item.status == "ACCEPT" || instance.item.status == "P_ACCEPT") {
                                    valid = false;
                                    $rootScope.showWarningMessage("Please allocate storage to : " + instance.item.upnNumber + " " + inwardItem.bomItem.item.itemMaster.itemName)
                                } else if (instance.item.status == "INVENTORY") {
                                    valid = false;
                                    $rootScope.showWarningMessage("Please verify storage for : " + instance.item.upnNumber + " " + inwardItem.bomItem.item.itemMaster.itemName)
                                }
                            }
                        })
                    }
                });

                return valid;
            }

            function downloadGatePass(gatePass) {
                var url = "{0}//{1}/api/drdo/inwards/gatePass/{2}/{3}/preview".
                    format(window.location.protocol, window.location.host,
                    gatePass.id, gatePass.gatePass.id);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = gatePass.gatePass.name;
                });
                /*window.open(url);
                 $timeout(function () {
                 window.close();
                 }, 2000);*/
            }

            /*---------------------------------------------   STORE Methods  -------------------------------------------*/

            function createInwardItem(inwardItem) {
                if (validateQuantity(inwardItem)) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    InwardService.createInwardItem(vm.inward.id, inwardItem).then(
                        function (data) {
                            inwardItem.id = data.id;
                            inwardItem.instancesCreated = data.instancesCreated;
                            inwardItem.level = 0;
                            inwardItem.type = "INWARDITEM";
                            inwardItem.expanded = true;
                            inwardItem.instances = data.instances;
                            inwardItem.hideAllocateAll = true;
                            inwardItem.section = data.section;
                            angular.forEach(inwardItem.instances, function (instance) {

                                angular.forEach(vm.itemInstanceAttributes, function (attribute) {
                                    var attributeData = angular.copy(attribute);
                                    instance[attributeData.attributeDef.name] = attributeData;
                                })

                                instance.bomItem = inwardItem.bomItem;
                                instance.level = 1;
                                instance.type = 'INWARDITEMINSTANCE';
                                instance.quantity = 1;
                                instance.expanded = false;
                                instance.instancesCreated = true;
                                instance.editMode = false;
                            });
                            updateRows();
                            $rootScope.showSuccessMessage("Inward Item saved successfully");
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validateQuantity(inwardItem) {
                var valid = true;
                if (inwardItem.bomItem.item.itemMaster.itemType.hasLots && (inwardItem.fractionalQuantity == 0 || inwardItem.fractionalQuantity == null
                    || inwardItem.fractionalQuantity == "" || inwardItem.fractionalQuantity == undefined || inwardItem.fractionalQuantity < 0)) {

                    valid = false;
                    $rootScope.showWarningMessage("Please enter Quantity for " + inwardItem.bomItem.item.itemMaster.itemName);
                } else if (!inwardItem.bomItem.item.itemMaster.itemType.hasLots && (inwardItem.quantity == 0 || inwardItem.quantity == null
                    || inwardItem.quantity == "" || inwardItem.quantity == undefined || inwardItem.quantity < 0)) {

                    valid = false;
                    $rootScope.showWarningMessage("Please enter Quantity for " + inwardItem.bomItem.item.itemMaster.itemName);
                }

                return valid;
            }

            function saveInwardItem(inwardItem) {
                if (validateInstances(inwardItem)) {

                }
            }


            vm.deleteInwardItem = deleteInwardItem;

            function deleteInwardItem(inwardItem) {
                var options = {
                    title: "Confirmation",
                    message: "Please confirm to delete this Inward Item and related data will be lost",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-content'));
                            var store = true;
                            if (vm.inward.status == "STORE") {
                                store = true;
                            } else {
                                store = false;
                            }
                            InwardService.deleteInwardItem($stateParams.inwardId, inwardItem, store).then(
                                function (data) {
                                    loadInwardItems();
                                    $rootScope.showSuccessMessage("Inward Item deleted successfully");
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                )
            }

            function validateInstances(inwardItem) {
                var valid = true;
                angular.forEach(inwardItem.instances, function (instance) {
                    if (valid) {
                        if (instance.item.manufacturer == null || instance.item.manufacturer == "" || instance.item.manufacturer == undefined) {
                            valid = false;
                            $rootScope.showWarningMessage("Please select Manufacturer");
                        } else if (!instance.item.item.itemMaster.itemType.hasLots && (instance.item.oemNumber == null || instance.item.oemNumber == "" || instance.item.oemNumber == undefined)) {
                            valid = false;
                            $rootScope.showWarningMessage("Please enter Serial Numbers");
                        }

                        angular.forEach(vm.itemInstanceAttributes, function (attribute) {
                            if (valid) {
                                if (instance[attribute.attributeDef.name].attributeDef.required && !$rootScope.checkAttribute(instance[attribute.attributeDef.name].value)) {
                                    valid = false;
                                    $rootScope.showWarningMessage(inwardItem.bomItem.item.itemMaster.itemName + " item " + instance[attribute.attributeDef.name].attributeDef.name + " is required");
                                }
                            }
                        });
                    }
                });

                return valid;
            }


            function toggleRow(inwardItemRow) {
                inwardItemRow.expanded = !inwardItemRow.expanded;
                updateRows();
            }

            function updateUPN(inwardItemRow) {
                var uptoPartType = inwardItemRow.item.initialUpn.substr(0, 10);

                var padding = '0000';
                var sn = '';
                if (inwardItemRow.item.oemNumber.length > 4) {
                    sn = inwardItemRow.item.oemNumber.substr(inwardItemRow.item.oemNumber.length - 4);
                }
                else {
                    sn = inwardItemRow.item.oemNumber;
                }
                var snPart = padding.substring(sn.length) + sn;
                snPart = snPart.toUpperCase();
                var lot = inwardItemRow.item.initialUpn.substr(14, 16);

                inwardItemRow.item.initialUpn = "{0}{1}{2}".format(uptoPartType, snPart, lot);
            }

            function showLotAllocationPanel(inwardItem, mode) {
                var button = {};
                if (mode == 'NEW') {
                    button = {text: "Create", broadcast: 'app.inward.lotAllocation.create'}
                } else {
                    button = {text: "Close", broadcast: 'app.inward.lotAllocation.create'};
                }
                if (inwardItem.quantity > 0 || inwardItem.fractionalQuantity > 0) {
                    var options = {
                        title: "Lot Allocation",
                        template: 'app/desktop/modules/inward/details/inwardLotAllocationView.jsp',
                        controller: 'InwardLotAllocationController as lotAllocationVm',
                        resolve: 'app/desktop/modules/inward/details/inwardLotAllocationController',
                        width: 600,
                        showMask: true,
                        data: {
                            inwardItem: inwardItem,
                            inwardLotMode: mode
                        },
                        buttons: [
                            button
                        ],
                        callback: function (data) {
                            if (mode == "NEW") {
                                inwardItem.id = data.id;
                                inwardItem.instancesCreated = data.instancesCreated;
                                inwardItem.level = 0;
                                inwardItem.type = "INWARDITEM";
                                inwardItem.expanded = true;
                                inwardItem.instances = data.instances;
                                angular.forEach(inwardItem.instances, function (instance) {
                                    instance.bomItem = inwardItem.bomItem;
                                    instance.level = 1;
                                    instance.type = 'INWARDITEMINSTANCE';
                                    instance.quantity = instance.item.fractionalQuantity;
                                    instance.expanded = false;
                                    instance.instancesCreated = true;
                                    instance.editMode = false;

                                    angular.forEach(vm.itemInstanceAttributes, function (attribute) {
                                        var attributeData = angular.copy(attribute);
                                        instance[attributeData.attributeDef.name] = attributeData;
                                    })
                                });
                                updateRows();
                            } else {
                                loadInward();
                            }

                        }
                    };

                    $rootScope.showSidePanel(options);
                }
                else {
                    $rootScope.showWarningMessage("Inward item quantity must be greater than zero.")
                }

            }

            function editInwardItemInstance(itemInstance) {
                itemInstance.editOemNumber = itemInstance.item.oemNumber;
                itemInstance.editExpiryDate = itemInstance.item.expiryDate;
                itemInstance.editUpnNumber = itemInstance.item.upnNumber;
                itemInstance.editInitialUpn = itemInstance.item.initialUpn;
                itemInstance.editManufacturer = itemInstance.item.manufacturer;
                itemInstance.editExpiryDate = itemInstance.item.expiryDate;
                angular.forEach(vm.itemInstanceAttributes, function (attribute) {
                    itemInstance[attribute.attributeDef.name].stringValue = itemInstance[attribute.attributeDef.name].value.stringValue;
                    itemInstance[attribute.attributeDef.name].integerVlaue = itemInstance[attribute.attributeDef.name].value.integerVlaue;
                    itemInstance[attribute.attributeDef.name].doubleValue = itemInstance[attribute.attributeDef.name].value.doubleValue;
                    itemInstance[attribute.attributeDef.name].dateValue = itemInstance[attribute.attributeDef.name].value.dateValue;
                    itemInstance[attribute.attributeDef.name].timeValue = itemInstance[attribute.attributeDef.name].value.timeValue;
                    itemInstance[attribute.attributeDef.name].timestampValue = itemInstance[attribute.attributeDef.name].value.timestampValue;
                    itemInstance[attribute.attributeDef.name].attachmentValues = itemInstance[attribute.attributeDef.name].value.attachmentValues;
                    itemInstance[attribute.attributeDef.name].currencyValue = itemInstance[attribute.attributeDef.name].value.currencyValue;
                })
                itemInstance.editMode = true;
            }

            vm.editInwardItemInstanceAfterSubmit = editInwardItemInstanceAfterSubmit;
            vm.cancelInwardItemInstanceEditAfterSubmit = cancelInwardItemInstanceEditAfterSubmit;
            vm.saveInwardItemInstanceAfterSubmit = saveInwardItemInstanceAfterSubmit;

            function saveInwardItemInstanceAfterSubmit(itemInstance) {
                $rootScope.showBusyIndicator($('.view-container'));
                InwardService.updateInwardItemInstanceAfterSubmit(vm.inward.id, itemInstance).then(
                    function (data) {
                        itemInstance.editMode = false;
                        saveItemInstanceAttributes(itemInstance);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function editInwardItemInstanceAfterSubmit(itemInstance) {
                itemInstance.editOemNumber = itemInstance.item.oemNumber;
                itemInstance.editExpiryDate = itemInstance.item.expiryDate;
                itemInstance.editUpnNumber = itemInstance.item.upnNumber;
                itemInstance.editInitialUpn = itemInstance.item.initialUpn;
                itemInstance.editManufacturer = itemInstance.item.manufacturer;
                itemInstance.editExpiryDate = itemInstance.item.expiryDate;
                angular.forEach(vm.itemInstanceAttributes, function (attribute) {
                    itemInstance[attribute.attributeDef.name].stringValue = itemInstance[attribute.attributeDef.name].value.stringValue;
                    itemInstance[attribute.attributeDef.name].integerVlaue = itemInstance[attribute.attributeDef.name].value.integerVlaue;
                    itemInstance[attribute.attributeDef.name].doubleValue = itemInstance[attribute.attributeDef.name].value.doubleValue;
                    itemInstance[attribute.attributeDef.name].dateValue = itemInstance[attribute.attributeDef.name].value.dateValue;
                    itemInstance[attribute.attributeDef.name].timeValue = itemInstance[attribute.attributeDef.name].value.timeValue;
                    itemInstance[attribute.attributeDef.name].timestampValue = itemInstance[attribute.attributeDef.name].value.timestampValue;
                    itemInstance[attribute.attributeDef.name].attachmentValues = itemInstance[attribute.attributeDef.name].value.attachmentValues;
                    itemInstance[attribute.attributeDef.name].currencyValue = itemInstance[attribute.attributeDef.name].value.currencyValue;
                })
                itemInstance.editAfterSubmit = true;
            }

            function cancelInwardItemInstanceEditAfterSubmit(itemInstance) {
                itemInstance.item.oemNumber = itemInstance.editOemNumber;
                itemInstance.item.upnNumber = itemInstance.editUpnNumber;
                itemInstance.item.initialUpn = itemInstance.editInitialUpn;
                itemInstance.item.manufacturer = itemInstance.editManufacturer;
                itemInstance.item.expiryDate = itemInstance.editExpiryDate;
                angular.forEach(vm.itemInstanceAttributes, function (attribute) {
                    itemInstance[attribute.attributeDef.name].value.stringValue = itemInstance[attribute.attributeDef.name].stringValue;
                    itemInstance[attribute.attributeDef.name].value.integerVlaue = itemInstance[attribute.attributeDef.name].integerVlaue;
                    itemInstance[attribute.attributeDef.name].value.doubleValue = itemInstance[attribute.attributeDef.name].doubleValue;
                    itemInstance[attribute.attributeDef.name].value.dateValue = itemInstance[attribute.attributeDef.name].dateValue;
                    itemInstance[attribute.attributeDef.name].value.timeValue = itemInstance[attribute.attributeDef.name].timeValue;
                    itemInstance[attribute.attributeDef.name].value.timestampValue = itemInstance[attribute.attributeDef.name].timestampValue;
                    itemInstance[attribute.attributeDef.name].value.attachmentValues = itemInstance[attribute.attributeDef.name].attachmentValues;
                    itemInstance[attribute.attributeDef.name].value.currencyValue = itemInstance[attribute.attributeDef.name].currencyValue;
                })
                itemInstance.editAfterSubmit = false;
            }


            vm.editItemInstance = editItemInstance;

            function editItemInstance(itemInstance) {
                itemInstance.editOemNumber = itemInstance.item.oemNumber;
                itemInstance.editUpnNumber = itemInstance.item.upnNumber;
                itemInstance.editInitialUpn = itemInstance.item.initialUpn;
                itemInstance.editManufacturer = itemInstance.item.manufacturer;
                itemInstance.editExpiryDate = itemInstance.item.expiryDate;
                angular.forEach(vm.itemInstanceAttributes, function (attribute) {
                    itemInstance[attribute.attributeDef.name].stringValue = itemInstance[attribute.attributeDef.name].value.stringValue;
                    itemInstance[attribute.attributeDef.name].integerVlaue = itemInstance[attribute.attributeDef.name].value.integerVlaue;
                    itemInstance[attribute.attributeDef.name].doubleValue = itemInstance[attribute.attributeDef.name].value.doubleValue;
                    itemInstance[attribute.attributeDef.name].dateValue = itemInstance[attribute.attributeDef.name].value.dateValue;
                    itemInstance[attribute.attributeDef.name].timeValue = itemInstance[attribute.attributeDef.name].value.timeValue;
                    itemInstance[attribute.attributeDef.name].timestampValue = itemInstance[attribute.attributeDef.name].value.timestampValue;
                    itemInstance[attribute.attributeDef.name].attachmentValues = itemInstance[attribute.attributeDef.name].value.attachmentValues;
                    itemInstance[attribute.attributeDef.name].currencyValue = itemInstance[attribute.attributeDef.name].value.currencyValue;
                })

                itemInstance.editMode = true;
            }

            function cancelInwardItemInstanceEdit(itemInstance) {
                itemInstance.item.oemNumber = itemInstance.editOemNumber;
                itemInstance.item.upnNumber = itemInstance.editUpnNumber;
                itemInstance.item.initialUpn = itemInstance.editInitialUpn;
                itemInstance.item.manufacturer = itemInstance.editManufacturer;
                itemInstance.item.expiryDate = itemInstance.editExpiryDate;
                angular.forEach(vm.itemInstanceAttributes, function (attribute) {
                    itemInstance[attribute.attributeDef.name].value.stringValue = itemInstance[attribute.attributeDef.name].stringValue;
                    itemInstance[attribute.attributeDef.name].value.integerVlaue = itemInstance[attribute.attributeDef.name].integerVlaue;
                    itemInstance[attribute.attributeDef.name].value.doubleValue = itemInstance[attribute.attributeDef.name].doubleValue;
                    itemInstance[attribute.attributeDef.name].value.dateValue = itemInstance[attribute.attributeDef.name].dateValue;
                    itemInstance[attribute.attributeDef.name].value.timeValue = itemInstance[attribute.attributeDef.name].timeValue;
                    itemInstance[attribute.attributeDef.name].value.timestampValue = itemInstance[attribute.attributeDef.name].timestampValue;
                    itemInstance[attribute.attributeDef.name].value.attachmentValues = itemInstance[attribute.attributeDef.name].attachmentValues;
                    itemInstance[attribute.attributeDef.name].value.currencyValue = itemInstance[attribute.attributeDef.name].currencyValue;
                })
                itemInstance.editMode = false;
            }


            function saveInwardItemInstance(itemInstance) {
                if (validItemInstance(itemInstance)) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    InwardService.getReturnInstanceByInstance(itemInstance.item.item.id, itemInstance.item.manufacturer.id, itemInstance.item.oemNumber).then(
                        function (data) {
                            $rootScope.hideBusyIndicator();
                            if (data == "" || data == null) {
                                if (itemInstance.item.status == "REVIEW" || itemInstance.item.status == "P_ACCEPT" || itemInstance.item.status == "INVENTORY"
                                    || itemInstance.item.status == "VERIFIED" || itemInstance.item.status == "ISSUE" || itemInstance.item.provisionalAccept) {
                                    var options = {
                                        title: "Add Comment",
                                        template: 'app/desktop/modules/inward/details/comment/itemCommentDialogView.jsp',
                                        controller: 'ItemCommentDialogController as itemCommentDialogVm',
                                        resolve: 'app/desktop/modules/inward/details/comment/itemCommentDialogController',
                                        width: 600,
                                        showMask: true,
                                        buttons: [
                                            {text: "Add", broadcast: 'app.inward.itemInstance.comment'}
                                        ],
                                        callback: function (data) {
                                            itemInstance.item.reason = data;
                                            $rootScope.showBusyIndicator($('.view-container'));
                                            InwardService.updateInwardItemInstance(vm.inward.id, itemInstance).then(
                                                function (data) {
                                                    itemInstance.editMode = false;
                                                    saveItemInstanceAttributes(itemInstance);
                                                }, function (error) {
                                                    $rootScope.showErrorMessage(error.message);
                                                    $rootScope.hideBusyIndicator();
                                                }
                                            )
                                        }
                                    };

                                    $rootScope.showSidePanel(options);
                                } else {
                                    $rootScope.showBusyIndicator($('.view-container'));
                                    InwardService.updateInwardItemInstance(vm.inward.id, itemInstance).then(
                                        function (data) {
                                            itemInstance.editMode = false;
                                            saveItemInstanceAttributes(itemInstance);
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    )
                                }
                            } else {
                                var options = {
                                    title: "Alert",
                                    message: itemInstance.item.initialUpn + " is Returning Part. Do you want to continue.",
                                    okButtonClass: 'btn-danger'
                                };

                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        $rootScope.showBusyIndicator($('.view-container'));
                                        InwardService.updateReturnedInwardItemInstance(vm.inward.id, itemInstance, data.id).then(
                                            function (data) {
                                                itemInstance.item = data.item;
                                                saveItemInstanceAttributes(itemInstance);
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        )
                                    }
                                });
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validItemInstance(itemInstance) {
                var valid = true;

                if (itemInstance.item.manufacturer == null || itemInstance.item.manufacturer == "" || itemInstance.item.manufacturer == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select Manufacturer");
                } else if (itemInstance.item.oemNumber == null || itemInstance.item.oemNumber == "" || itemInstance.item.oemNumber == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Serial Number");
                }

                angular.forEach(vm.itemInstanceAttributes, function (attribute) {
                    if (valid) {
                        if (itemInstance[attribute.attributeDef.name].attributeDef.dataType == "TIME" && itemInstance[attribute.attributeDef.name].timeValue != null) {
                            itemInstance[attribute.attributeDef.name].value.timeValue = itemInstance[attribute.attributeDef.name].timeValue;
                        }
                        if (itemInstance[attribute.attributeDef.name].attributeDef.required && !$rootScope.checkAttribute(itemInstance[attribute.attributeDef.name].value)) {
                            valid = false;
                            $rootScope.showWarningMessage(itemInstance[attribute.attributeDef.name].attributeDef.name + " is required");
                        }

                        if (itemInstance[attribute.attributeDef.name].attributeDef.dataType == "ATTACHMENT") {
                            angular.forEach(itemInstance[attribute.attributeDef.name].value.attachmentValues, function (attachment) {
                                var fileExtension = attachment.name.substring(attachment.name.lastIndexOf(".") + 1);

                                if (fileExtension.toLowerCase() != "pdf") {
                                    valid = false;
                                    $rootScope.showErrorMessage("Please upload pdf format certificate only");
                                }
                            })
                        }
                    }
                });

                return valid;
            }


            function saveItemInstanceAttributes(itemInstance) {
                var savedAttributes = 0;
                var normalAttributes = [];
                angular.forEach(vm.itemInstanceAttributes, function (attribute) {
                    if (itemInstance[attribute.attributeDef.name].attributeDef.dataType == "TEXT" || itemInstance[attribute.attributeDef.name].attributeDef.dataType == "INTEGER"
                        || itemInstance[attribute.attributeDef.name].attributeDef.dataType == "DOUBLE" || itemInstance[attribute.attributeDef.name].attributeDef.dataType == "DATE"
                        || itemInstance[attribute.attributeDef.name].attributeDef.dataType == "CURRENCY" || itemInstance[attribute.attributeDef.name].attributeDef.dataType == "TIME") {

                        normalAttributes.push(itemInstance[attribute.attributeDef.name].value);
                        savedAttributes++;
                        if (savedAttributes == vm.itemInstanceAttributes.length) {
                            saveAttributes(itemInstance, normalAttributes);
                        }
                    } else if (itemInstance[attribute.attributeDef.name].attributeDef.dataType == "IMAGE") {
                        InwardService.saveInstanceImage(itemInstance.item.id, itemInstance[attribute.attributeDef.name].attributeDef.id,
                            itemInstance[attribute.attributeDef.name].imageValue).then(
                            function (data) {
                                savedAttributes++;
                                if (savedAttributes == vm.itemInstanceAttributes.length) {
                                    saveAttributes(itemInstance, normalAttributes);
                                }
                            }
                        )
                    } else if (itemInstance[attribute.attributeDef.name].attributeDef.dataType == "ATTACHMENT") {
                        InwardService.saveInstanceAttachment(itemInstance.item.id, itemInstance[attribute.attributeDef.name].attributeDef.id,
                            itemInstance[attribute.attributeDef.name].value.attachmentValues).then(
                            function (data) {
                                savedAttributes++;
                                if (savedAttributes == vm.itemInstanceAttributes.length) {
                                    saveAttributes(itemInstance, normalAttributes);
                                }
                            }
                        )
                    }
                });
                $rootScope.hideBusyIndicator();
            }

            function saveAttributes(itemInstance, normalAttributes) {
                if (normalAttributes.length > 0) {
                    InwardService.saveInstanceAttributes(itemInstance.item.id, normalAttributes).then(
                        function (data) {
                            loadInward();
                            $rootScope.showSuccessMessage("Item updated successfully");
                        }
                    )
                } else {
                    loadInward();
                    $rootScope.showSuccessMessage("Item updated successfully");
                }
            }

            vm.selectedInstancesToApply = [];
            vm.checkApplyAll = checkApplyAll;
            function checkApplyAll(instance) {
                vm.selectedInstancesToApply = [];
                vm.selectedItemInstance = instance;
                instance.applyAllChecked = true;
                angular.forEach(vm.inwardItemRows, function (inwardItemRow) {
                    if (inwardItemRow.type == "INWARDITEMINSTANCE") {
                        if (inwardItemRow.inwardItem == instance.inwardItem && inwardItemRow.item.id != instance.item.id && !inwardItemRow.showApplyAll) {
                            inwardItemRow.showCheckBox = true;
                            inwardItemRow.checkBoxChecked = false;
                        } else if (inwardItemRow.inwardItem == instance.inwardItem && inwardItemRow.item.id != instance.item.id && inwardItemRow.showApplyAll) {
                            inwardItemRow.applyAllChecked = false;
                        }
                    }
                })
            }

            vm.unCheckApplyAll = unCheckApplyAll;
            function unCheckApplyAll(instance) {
                vm.selectedInstancesToApply = [];
                vm.selectedItemInstance = null;
                instance.applyAllChecked = false;
                angular.forEach(vm.inwardItemRows, function (inwardItemRow) {
                    if (inwardItemRow.type == "INWARDITEMINSTANCE") {
                        if (inwardItemRow.inwardItem == instance.inwardItem && inwardItemRow.item.id != instance.item.id && !inwardItemRow.showApplyAll) {
                            inwardItemRow.showCheckBox = false;
                            inwardItemRow.checkBoxChecked = false;
                            inwardItemRow.editMode = false;
                            inwardItemRow.editInstance = false;
                            angular.forEach(vm.itemInstanceAttributes, function (attribute) {
                                if (inwardItemRow[attribute.attributeDef.name].value.id.objectId == null) {
                                    var attributeDefId = instance[attribute.attributeDef.name].value.id.attributeDef;
                                    inwardItemRow[attribute.attributeDef.name].value = angular.copy(newValue);
                                    inwardItemRow[attribute.attributeDef.name].value.id.attributeDef = attributeDefId;
                                }
                            });

                        } else if (inwardItemRow.inwardItem == instance.inwardItem && inwardItemRow.item.id != instance.item.id && inwardItemRow.showApplyAll) {
                            inwardItemRow.applyAllChecked = false;
                        }


                    }
                })
            }

            vm.selectInstance = selectInstance;
            vm.unSelectInstance = unSelectInstance;

            function selectInstance(instance) {
                instance.item.manufacturer = vm.selectedItemInstance.item.manufacturer;
                instance.item.manufacturer.mfrCode = vm.selectedItemInstance.item.manufacturer.mfrCode;
                instance.item.oemNumber = vm.selectedItemInstance.item.oemNumber;
                instance.item.expiryDate = vm.selectedItemInstance.item.expiryDate;
                angular.forEach(vm.itemInstanceAttributes, function (attribute) {
                    instance[attribute.attributeDef.name].value = vm.selectedItemInstance[attribute.attributeDef.name].value;
                    instance[attribute.attributeDef.name].value.id.objectId = null;
                });
                vm.showApplyAllButton = true;
                instance.editInstance = true;
                vm.selectedInstancesToApply.push(instance);
                instance.checkBoxChecked = true;
            }

            function unSelectInstance(instance) {
                instance.editInstance = false;
                instance.editMode = false;
                instance.item.manufacturer = null;
                instance.item.expiryDate = null;
                angular.forEach(vm.itemInstanceAttributes, function (attribute) {
                    if (instance[attribute.attributeDef.name].value.id.objectId == null) {
                        var attributeDefId = instance[attribute.attributeDef.name].value.id.attributeDef;
                        instance[attribute.attributeDef.name].value = angular.copy(newValue);
                        instance[attribute.attributeDef.name].value.id.attributeDef = attributeDefId;
                    }
                });

                vm.selectedInstancesToApply.splice(vm.selectedInstancesToApply.indexOf(instance), 1);
                instance.checkBoxChecked = false;
                if (vm.selectedInstancesToApply.length == 0) {
                    vm.showApplyAllButton = false;
                }
            }

            vm.applyAttributesToInstances = applyAttributesToInstances;

            function applyAttributesToInstances(instance) {
                if (validateSelectedInstances()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    InwardService.saveSelectedInstanceAttributes(instance.item.id, vm.selectedInstancesToApply).then(
                        function (data) {
                            $rootScope.showSuccessMessage("Details saved successfully");
                            $rootScope.hideBusyIndicator();
                            loadInward();
                        }, function (error) {
                            $rootScope.showWarningMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }

            }

            function validateSelectedInstances() {
                var valid = true;
                angular.forEach(vm.selectedInstancesToApply, function (itemInstance) {
                    if (valid) {
                        if (itemInstance.item.manufacturer == null || itemInstance.item.manufacturer == "" || itemInstance.item.manufacturer == undefined) {
                            valid = false;
                            $rootScope.showWarningMessage("Please select Manufacturer");
                        } else if (!itemInstance.item.item.itemMaster.itemType.hasLots && (itemInstance.item.oemNumber == null || itemInstance.item.oemNumber == "" || itemInstance.item.oemNumber == undefined)) {
                            valid = false;
                            $rootScope.showWarningMessage("Please enter Serial Number");
                        }
                    }
                })

                return valid;
            }

            vm.openPropertyAttachment = openPropertyAttachment;
            function openPropertyAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/preview".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = attachment.name;
                });
                /*window.open(url);
                 $timeout(function () {
                 window.close();
                 }, 2000);*/
                //launchUrl(url);
            }


            vm.checkExpiryDate = checkExpiryDate;
            function checkExpiryDate(inwardItemInstance) {
                if (!inwardItemInstance.item.hasExpiry) {
                    inwardItemInstance.item.expiryDate = null;
                }
            }

            vm.deleteObjectAttachment = deleteObjectAttachment;

            function deleteObjectAttachment(attchments, object, attachment) {
                var options = {
                    title: "Delete Attachment",
                    message: "Please confirm to delete this attachment",
                    okButtonClass: 'btn-danger'
                }

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        AttributeAttachmentService.deleteObjectAttributeAttachment(attachment.id, object.id).then(
                            function () {
                                attchments.splice(attchments.indexOf(attachment), 1);
                                $rootScope.showSuccessMessage("Attachment deleted successfully");
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage("Attachment deleted successfully");
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            vm.deleteAttachmentAfterSubmit = deleteAttachmentAfterSubmit;

            function deleteAttachmentAfterSubmit(attchments, object, attachment) {

                if (attchments.length > 1) {
                    var options = {
                        title: "Delete Attachment",
                        message: "Please confirm to delete this attachment",
                        okButtonClass: 'btn-danger'
                    }

                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            AttributeAttachmentService.deleteObjectAttributeAttachment(attachment.id, object.id).then(
                                function () {
                                    attchments.splice(attchments.indexOf(attachment), 1);
                                    $rootScope.showSuccessMessage("Attachment deleted successfully");
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage("Attachment deleted successfully");
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    })
                } else {
                    $rootScope.showWarningMessage("At least one certificate should be available")
                }
            }

            /*--------------------------------------  SSQAG Methods  ----------------------------------------------------*/

            function acceptInwardItem(inwardItem) {
                var options = {
                    title: "Accept Inward Item",
                    message: "Please confirm to Accept this Inward Item",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        InwardService.acceptItem(vm.inward.id, inwardItem).then(
                            function (data) {
                                var submittedInstances = 0;
                                var noneInventoryInstances = 0;
                                angular.forEach(inwardItem.instances, function (instance) {
                                    angular.forEach(data.instances, function (itemInstance) {
                                        if (instance.id == itemInstance.id) {
                                            instance.bomItem = inwardItem.bomItem;
                                            instance.level = 1;
                                            instance.type = 'INWARDITEMINSTANCE';
                                            instance.quantity = 1;
                                            instance.item.status = itemInstance.item.status;
                                            instance.item.presentStatus = itemInstance.item.presentStatus;
                                            if (instance.item.status == "STORE_SUBMITTED") {
                                                submittedInstances++;
                                            }

                                            if (instance.item.status == "ACCEPT" || instance.item.status == "P_ACCEPT") {
                                                noneInventoryInstances++;
                                            }
                                        }
                                    });
                                });

                                if (submittedInstances == 0) {
                                    inwardItem.hidePAcceptAll = true;
                                    inwardItem.hideAcceptAll = true;
                                }

                                if (noneInventoryInstances > 1) {
                                    inwardItem.hideAllocateAll = false;
                                }
                                updateRows();
                                showStatuses();
                                $rootScope.showSuccessMessage("Item accepted successfully");
                                $rootScope.hideBusyIndicator();


                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            function provAcceptInwardItem(inwardItem) {

                var options = {
                    title: "Provisional Accept",
                    template: 'app/desktop/modules/inward/details/return/itemReturnDialogView.jsp',
                    controller: 'ItemReturnDialogController as itemReturnDialogVm',
                    resolve: 'app/desktop/modules/inward/details/return/itemReturnDialogController',
                    width: 600,
                    showMask: true,
                    data: {
                        inwardItemInstance: inwardItem,
                        itemInstanceMode: "P_ACCEPTALL"
                    },
                    buttons: [
                        {text: "Save", broadcast: 'app.inward.provisionalAcceptAll'}
                    ],
                    callback: function (data) {
                        var noneInstacnes = 0;
                        var noneInventoryInstances = 0;
                        angular.forEach(inwardItem.instances, function (instance) {
                            angular.forEach(data.instances, function (itemInstance) {
                                if (instance.id == itemInstance.id) {
                                    instance.bomItem = inwardItem.bomItem;
                                    instance.level = 1;
                                    instance.type = 'INWARDITEMINSTANCE';
                                    instance.quantity = 1;
                                    instance.item.status = itemInstance.item.status;
                                    instance.item.presentStatus = itemInstance.item.presentStatus;
                                    instance.item.provisionalAccept = itemInstance.item.provisionalAccept;
                                    if (instance.item.status == "STORE_SUBMITTED") {
                                        noneInstacnes++;
                                    }

                                    if (instance.item.status == "ACCEPT" || instance.item.status == "P_ACCEPT") {
                                        noneInventoryInstances++;
                                    }
                                }
                            });
                        });

                        if (noneInstacnes == 0) {
                            inwardItem.hidePAcceptAll = true;
                            inwardItem.hideAcceptAll = true;
                        }

                        if (noneInventoryInstances > 1) {
                            inwardItem.hideAllocateAll = false;
                        } else {
                            inwardItem.hideAllocateAll = true;
                        }
                        updateRows();
                        showStatuses();
                        $rootScope.showSuccessMessage("Item Provisional Accepted successfully");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function acceptInwardItemInstance(inwardItem) {
                var options = {
                    title: "Accept Item",
                    message: "Please confirm to Accept this Item",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        InwardService.acceptItemInstance(vm.inward.id, inwardItem.id, inwardItem).then(
                            function (data) {
                                angular.forEach(vm.inwardItemRows, function (inwardItemRow) {
                                    if (inwardItemRow.id == data.id) {
                                        inwardItemRow.accepted = data.accepted;
                                        inwardItemRow.pAccepted = data.pAccepted;

                                        var submittedInstances = 0;
                                        var nonInventoryInstances = 0;

                                        angular.forEach(data.instances, function (itemInstance) {
                                            if (inwardItem.id == itemInstance.id) {
                                                inwardItem.item.status = itemInstance.item.status;
                                                inwardItem.item.presentStatus = itemInstance.item.presentStatus;
                                                inwardItem.item.review = itemInstance.item.review;
                                                inwardItem.item.provisionalAccept = itemInstance.item.provisionalAccept;
                                                inwardItem.item.reason = null;
                                                inwardItem.item.presentReason = itemInstance.item.reason;
                                            }
                                            if (itemInstance.item.status == "STORE_SUBMITTED" || itemInstance.item.status == "REVIEWED") {
                                                submittedInstances++;
                                            }

                                            if (itemInstance.item.status == "ACCEPT" || itemInstance.item.status == "P_ACCEPT") {
                                                nonInventoryInstances++;
                                            }
                                        });

                                        if (submittedInstances == 1) {
                                            inwardItemRow.hideAcceptAll = true;
                                            inwardItemRow.hidePAcceptAll = true;
                                        }

                                        if (nonInventoryInstances > 0) {
                                            inwardItem.hideAllocateAll = false;
                                        } else {
                                            inwardItem.hideAllocateAll = true;
                                        }
                                    }
                                });


                                updateRows();
                                showStatuses();
                                $rootScope.showSuccessMessage("Item accepted successfully");
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })

            }

            vm.acceptInwardItemInstanceLater = acceptInwardItemInstanceLater;
            function acceptInwardItemInstanceLater(inwardItem) {
                var options = {
                    title: "Accept Item",
                    message: "Please confirm to Accept this Item",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        InwardService.acceptItemInstanceLater(vm.inward.id, inwardItem.id, inwardItem).then(
                            function (data) {
                                angular.forEach(vm.inwardItemRows, function (inwardItemRow) {
                                    if (inwardItemRow.id == data.id) {
                                        inwardItemRow.accepted = data.accepted;
                                        inwardItemRow.pAccepted = data.pAccepted;

                                        var submittedInstances = 0;
                                        var nonInventoryInstances = 0;

                                        angular.forEach(data.instances, function (itemInstance) {
                                            if (inwardItem.id == itemInstance.id) {
                                                inwardItem.item.status = itemInstance.item.status;
                                                inwardItem.item.presentStatus = itemInstance.item.presentStatus;
                                                inwardItem.item.review = itemInstance.item.review;
                                                inwardItem.item.provisionalAccept = itemInstance.item.provisionalAccept;
                                                inwardItem.item.reason = null;
                                                inwardItem.item.presentReason = itemInstance.item.reason;
                                            }
                                            if (itemInstance.item.status == "STORE_SUBMITTED" || itemInstance.item.status == "REVIEWED") {
                                                submittedInstances++;
                                            }

                                            if (itemInstance.item.status == "ACCEPT" || itemInstance.item.status == "P_ACCEPT") {
                                                nonInventoryInstances++;
                                            }
                                        });

                                        if (submittedInstances == 1) {
                                            inwardItemRow.hideAcceptAll = true;
                                            inwardItemRow.hidePAcceptAll = true;
                                        }

                                        if (nonInventoryInstances > 0) {
                                            inwardItem.hideAllocateAll = false;
                                        } else {
                                            inwardItem.hideAllocateAll = true;
                                        }
                                    }
                                });


                                updateRows();
                                showStatuses();
                                $rootScope.showSuccessMessage("Item accepted successfully");
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })

            }

            function provAcceptInwardItemInstance(inwardItem) {

                var options = {
                    title: "Provisional Accept",
                    template: 'app/desktop/modules/inward/details/return/itemReturnDialogView.jsp',
                    controller: 'ItemReturnDialogController as itemReturnDialogVm',
                    resolve: 'app/desktop/modules/inward/details/return/itemReturnDialogController',
                    width: 600,
                    showMask: true,
                    data: {
                        inwardItemInstance: inwardItem,
                        itemInstanceMode: "P_ACCEPT"
                    },
                    buttons: [
                        {text: "Save", broadcast: 'app.inward.itemInstance.return'}
                    ],
                    callback: function (data) {
                        angular.forEach(vm.inwardItemRows, function (inwardItemRow) {
                            if (inwardItemRow.id == data.id) {
                                inwardItemRow.accepted = data.accepted;
                                inwardItemRow.pAccepted = data.pAccepted;
                                var submittedInstances = 0;
                                var nonInventoryInstances = 0;

                                angular.forEach(data.instances, function (itemInstance) {
                                    if (inwardItem.id == itemInstance.id) {
                                        inwardItem.item.status = itemInstance.item.status;
                                        inwardItem.item.presentStatus = itemInstance.item.presentStatus;
                                        inwardItem.item.reason = null;
                                        inwardItem.item.presentReason = itemInstance.item.reason;
                                        inwardItem.item.provisionalAccept = itemInstance.item.provisionalAccept;
                                    }

                                    if (itemInstance.item.status == "STORE_SUBMITTED" || itemInstance.item.status == "REVIEWED") {
                                        submittedInstances++;
                                    }
                                    if (itemInstance.item.status == "ACCEPT" || itemInstance.item.status == "P_ACCEPT") {
                                        nonInventoryInstances++;
                                    }
                                });

                                if (submittedInstances == 1) {
                                    inwardItemRow.hideAcceptAll = true;
                                    inwardItemRow.hidePAcceptAll = true;
                                }

                                if (nonInventoryInstances > 1) {
                                    inwardItem.hideAllocateAll = false;
                                } else {
                                    inwardItem.hideAllocateAll = true;
                                }
                            }
                        });

                        updateRows();
                        showStatuses();
                        $rootScope.showSuccessMessage("Item provisionally accepted successfully");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function returnItemInstance(inwardItemRow, mode) {
                var title = "";
                if (mode == "REJECTED") {
                    title = "Reject Item";
                } else if (mode == "REVIEW") {
                    title = "Review Item";
                }
                var options = {
                    title: title,
                    template: 'app/desktop/modules/inward/details/return/itemReturnDialogView.jsp',
                    controller: 'ItemReturnDialogController as itemReturnDialogVm',
                    resolve: 'app/desktop/modules/inward/details/return/itemReturnDialogController',
                    width: 600,
                    showMask: true,
                    data: {
                        inwardItemInstance: inwardItemRow,
                        itemInstanceMode: mode
                    },
                    buttons: [
                        {text: "Save", broadcast: 'app.inward.itemInstance.return'}
                    ],
                    callback: function (itemInstaneRow) {
                        angular.forEach(vm.inwardItemRows, function (inwardItem) {
                            if (inwardItem.id == itemInstaneRow.id) {
                                inwardItem.accepted = itemInstaneRow.accepted;
                                inwardItem.pAccepted = itemInstaneRow.pAccepted;
                                var submittedInstances = 0;

                                angular.forEach(itemInstaneRow.instances, function (itemInstance) {
                                    if (inwardItemRow.id == itemInstance.id) {
                                        inwardItemRow.item.status = itemInstance.item.status;
                                        inwardItemRow.item.presentStatus = itemInstance.item.presentStatus;
                                        inwardItemRow.item.storage = itemInstance.item.storage;
                                        inwardItemRow.item.review = itemInstance.item.review;
                                        inwardItemRow.item.provisionalAccept = itemInstance.item.provisionalAccept;
                                        inwardItemRow.item.reason = null;
                                        inwardItemRow.item.presentReason = itemInstance.item.reason;
                                    }

                                    if (itemInstance.item.status == "STORE_SUBMITTED" || itemInstance.item.status == "REVIEWED") {
                                        submittedInstances++;
                                    }
                                });

                                if (submittedInstances == 1 || submittedInstances == 0) {
                                    inwardItem.hideAcceptAll = true;
                                    inwardItem.hidePAcceptAll = true;
                                }
                            }
                        });

                        updateRows();
                        showStatuses();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            /*-----------------------------------------------  INVENTORY Methods ------------------------------------------------*/

            function allocateStorageToAll(inwardItem) {
                $rootScope.showBusyIndicator($('.view-container'));
                InwardService.allocateStorageToItem(vm.inward.id, inwardItem).then(
                    function (data) {
                        angular.forEach(inwardItem.instances, function (instance) {
                            angular.forEach(data.instances, function (itemInstance) {
                                if (instance.id == itemInstance.id) {
                                    instance.bomItem = inwardItem.bomItem;
                                    instance.level = 1;
                                    instance.type = 'INWARDITEMINSTANCE';
                                    instance.quantity = 1;
                                    instance.item.status = itemInstance.item.status;
                                    instance.item.presentStatus = itemInstance.item.presentStatus;
                                    instance.item.storage = itemInstance.item.storage;
                                    instance.item.provisionalAccept = itemInstance.item.provisionalAccept;
                                    instance.item.storagePath = itemInstance.item.storagePath;
                                }
                            });
                        });
                        showStatuses();
                        $rootScope.showSuccessMessage("Storage allocated successfully");
                        inwardItem.hideAllocateAll = true;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function allocateStorage(inwardItemInstance) {
                $rootScope.showBusyIndicator($('.view-container'));
                InwardService.allocateStorageToInstance(vm.inward.id, inwardItemInstance).then(
                    function (data) {
                        angular.forEach(vm.inwardItemRows, function (inwardItemRow) {
                            if (inwardItemRow.id == data.id) {

                                var noneInventory = 0;

                                angular.forEach(data.instances, function (itemInstance) {
                                    if (inwardItemInstance.id == itemInstance.id) {
                                        inwardItemInstance.item.status = itemInstance.item.status;
                                        inwardItemInstance.item.presentStatus = itemInstance.item.presentStatus;
                                        inwardItemInstance.item.storage = itemInstance.item.storage;
                                        inwardItemInstance.item.provisionalAccept = itemInstance.item.provisionalAccept;
                                        inwardItemInstance.item.storagePath = itemInstance.item.storagePath;

                                        $rootScope.showSuccessMessage(inwardItemInstance.item.storage.name + " : Storage allocated successfully");
                                    }

                                    if (itemInstance.item.status != "INVENTORY" && itemInstance.item.status != 'REVIEW') {
                                        noneInventory++;
                                    }
                                });

                                if (noneInventory == 1 || noneInventory == 0) {
                                    inwardItemRow.hideAllocateAll = true;
                                }
                            }
                        });

                        updateRows();

                        showStatuses();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.addStorageToReturnedItemInstance = addStorageToReturnedItemInstance;
            function addStorageToReturnedItemInstance(inwardItemInstance) {
                $rootScope.showBusyIndicator($('.view-container'));
                InwardService.allocateStorageToReturnedItemInstance(vm.inward.id, inwardItemInstance).then(
                    function (data) {
                        angular.forEach(vm.inwardItemRows, function (inwardItemRow) {
                            if (inwardItemRow.id == data.id) {

                                var noneInventory = 0;

                                angular.forEach(data.instances, function (itemInstance) {
                                    if (inwardItemInstance.id == itemInstance.id) {
                                        inwardItemInstance.item.status = itemInstance.item.status;
                                        inwardItemInstance.item.presentStatus = itemInstance.item.presentStatus;
                                        inwardItemInstance.item.storage = itemInstance.item.storage;
                                        inwardItemInstance.item.provisionalAccept = itemInstance.item.provisionalAccept;
                                        inwardItemInstance.item.storagePath = itemInstance.item.storagePath;

                                        $rootScope.showSuccessMessage(inwardItemInstance.item.storage.name + " : Storage allocated successfully");
                                    }

                                    if (itemInstance.item.status != "INVENTORY" && itemInstance.item.status != 'REVIEW') {
                                        noneInventory++;
                                    }
                                });

                                if (noneInventory == 1 || noneInventory == 0) {
                                    inwardItemRow.hideAllocateAll = true;
                                }
                            }
                        });

                        updateRows();

                        showStatuses();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function verifyItem(inwardItemRow) {
                var options = {
                    title: "Verify Item",
                    template: 'app/desktop/modules/inward/details/verify/verifyPartView.jsp',
                    controller: 'VerifyPartController as verifyPartVm',
                    resolve: 'app/desktop/modules/inward/details/verify/verifyPartController',
                    width: 600,
                    showMask: true,
                    data: {
                        verifyInwardItemInstance: inwardItemRow
                    },
                    buttons: [
                        {text: "Verify", broadcast: 'app.inward.itemInstance.verify'}
                    ],
                    callback: function (data) {
                        angular.forEach(vm.inwardItemRows, function (inwardItem) {
                            if (inwardItem.id == data.id) {
                                angular.forEach(data.instances, function (itemInstance) {
                                    if (inwardItemRow.id == itemInstance.id) {
                                        inwardItemRow.item.status = itemInstance.item.status;
                                        inwardItemRow.item.presentStatus = itemInstance.item.presentStatus;
                                        inwardItemRow.item.storage = itemInstance.item.storage;
                                        inwardItemRow.item.provisionalAccept = itemInstance.item.provisionalAccept;
                                    }
                                });
                            }
                        });

                        updateRows();
                        showStatuses();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.generateLotNumber = generateLotNumber;
            vm.deleteLotNumber = deleteLotNumber;

            function generateLotNumber(inwardItemRow) {
                InwardService.generateNextLotNumber(inwardItemRow.item.id).then(
                    function (data) {
                        inwardItemRow.item.oemNumber = "Lot-" + data.lotSequence;
                        inwardItemRow.item.lotNumber = data.lotSequence;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            function deleteLotNumber(inwardItemRow) {
                InwardService.deleteGeneratedLotNumber(inwardItemRow.item.id).then(
                    function (data) {
                        inwardItemRow.item.oemNumber = null;
                        inwardItemRow.item.lotNumber = null;
                    }
                )
            }

            (function () {
                initSearchBox();
                loadInwardItemInstancesAttributes();
            })();
        }
    }
);