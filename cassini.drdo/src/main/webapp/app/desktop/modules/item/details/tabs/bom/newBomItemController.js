define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/bomService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService'
    ],
    function (module) {
        module.controller('NewBomItemController', NewBomItemController);

        function NewBomItemController($scope, $rootScope, $q, $timeout, $state, $stateParams, $uibModal, $cookies, $window, $translate,
                                      CommonService, ItemService, DialogService, ItemTypeService, BomService, AutonumberService,
                                      AttributeAttachmentService, ObjectAttributeService) {

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.loading = true;
            $rootScope.newItemBomButton = false;
            vm.enableNewItemButton = false;
            vm.itemId = $stateParams.itemId;

            var selectedItem = $scope.data.selectedBomItem;
            var button = $scope.data.buttonTitle;
            vm.typeCodeMap = new Hashtable();

            vm.bomItems = [];
            vm.preventClick = preventClick;
            vm.performSearch = performSearch;
            vm.addItemToBom = addItemToBom;
            vm.addNew = addNew;
            vm.cancelNew = cancelNew;

            vm.filters = {
                searchQuery: null,
                item: '',
                selectedItem: ''
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

            vm.searchResults = angular.copy(pagedResults);

            vm.newBomItem = {
                id: null,
                bom: null,
                item: null,
                newItem: {
                    id: null,
                    itemType: null,
                    itemNumber: null,
                    itemName: null,
                    description: null,
                    units: "Each",
                    drawingNumber: null,
                    itemTypeSpec: null
                },
                quantity: null,
                fractionalQuantity: null
            };

            vm.searchValue = null;
            vm.searchTree = searchTree;

            function preventClick(event) {
                event.stopPropagation();
                event.preventDefault();
            }

            function performSearch() {
                if (vm.filters.searchQuery == "") {
                    $('#newItemSearchResults').hide();
                }
                if (vm.filters.searchQuery != "") {
                    vm.filters.item = $stateParams.itemId;
                    vm.filters.selectedItem = selectedItem.id;
                    $('#newItemSearchResults').show();
                    ItemService.searchItems(pageable, vm.filters).then(
                        function (data) {
                            $rootScope.newItemBomButton = false;
                            button.text = "CLose";
                            vm.enableNewItemButton = true;
                            vm.searchResultData = angular.copy(data);
                            ItemService.getRevisionReferences(vm.searchResultData.content, 'latestRevision');
                            vm.searchResults = data;
                            vm.searchResults.content = [];
                            angular.forEach(vm.searchResultData.content, function (item) {
                                if (!checkIfAlreadyAdded(item)) {
                                    vm.searchResults.content.push(item);
                                }
                            });

                        }
                    )
                }
            }

            function checkIfAlreadyAdded(result) {
                var found = false;
                angular.forEach($rootScope.addedNewItems, function (item) {
                    if (result.itemType.id == item.itemType.id &&
                        result.id == item.id) {
                        found = true;
                    }
                });

                return found;
            }

            function addItemToBom(item) {
                $rootScope.addSearchItemToItemBom(item);
                $rootScope.addedNewItems.push(item);
                var index = vm.searchResults.content.indexOf(item);
                vm.searchResults.content.splice(index, 1);
            }

            function addNew() {
                vm.filters.searchQuery = null;
                $('#newItemSearchResults').hide();
                $rootScope.newItemBomButton = true;
                button.text = "Create";
            }

            function cancelNew() {
                $rootScope.newItemBomButton = false;
                button.text = "Close";
            }

            vm.onSelectType = onSelectType;
            function onSelectType(itemType) {
                $("#typeTree").show();
                if (itemType != null && itemType != undefined) {
                    $("#typeTree").hide();
                    vm.newBomItem.newItem.itemType = itemType;
                    loadSelectTypeParentNode();
                }
            }

            function loadSelectTypeParentNode() {
                ItemTypeService.getTypeParentNode(vm.newBomItem.newItem.itemType.id).then(
                    function (data) {
                        vm.typeParentNode = data.itemType;
                        vm.items = data.items;
                        vm.itemTypeSpecs = data.itemTypeSpecs;
                        if (vm.itemTypeSpecs.length == 0) {
                            vm.specificationTitle = "No Specifications";
                        } else {
                            vm.specificationTitle = "Select Specification";
                        }
                        vm.typeCodeMap = new Hashtable();
                        angular.forEach(vm.items, function (item) {
                            vm.typeCodeMap.put(item.itemCode, item);
                        })
                        loadAttributeDefs();
                    }
                )
            }

            function autoNumber() {
                if (vm.newBomItem.newItem.itemType != null && vm.newBomItem.newItem.itemType.itemNumberSource != null) {
                    var source = vm.newBomItem.newItem.itemType.itemNumberSource;
                    AutonumberService.getNextNumber(source.id).then(
                        function (data) {
                            vm.newBomItem.newItem.itemNumber = data;
                        }
                    )
                }
            }

            function addItem() {
                if ($rootScope.newItemBomButton == true) {
                    if (validateItem() && validateCustomAttributes() && validateTypeAttributes()) {
                        $rootScope.showBusyIndicator($("#rightSidePanel"));
                        if (selectedItem.bomItemType == 'SECTION') {
                            vm.newBomItem.hierarchicalCode = selectedItem.hierarchicalCode + "" + selectedItem.typeRef.code + "" + "000";
                        } else if (selectedItem.bomItemType == 'SUBSYSTEM') {
                            vm.newBomItem.hierarchicalCode = selectedItem.hierarchicalCode + "" + selectedItem.typeRef.code + "" + "00";
                        } else if (selectedItem.bomItemType == 'UNIT') {
                            vm.newBomItem.hierarchicalCode = selectedItem.hierarchicalCode + "" + selectedItem.typeRef.code;
                        }
                        if (vm.typeParentNode.name != "Part") {
                            vm.newBomItem.quantity = null;
                            vm.newBomItem.fractionalQuantity = null;
                        }
                        BomService.createNewBomItem(selectedItem.id, vm.newBomItem).then(
                            function (data) {
                                vm.newBomItem.newItem = data.item.itemMaster;
                                saveObjectAttributes().then(
                                    function (custom) {
                                        saveAttributes().then(
                                            function (attributes) {
                                                vm.newItemAttributes = [];
                                                vm.newItemRevisionAttributes = [];
                                                loadObjectAttributeDefs();
                                                vm.newBomItem = {
                                                    id: null,
                                                    bom: null,
                                                    item: null,
                                                    newItem: {
                                                        id: null,
                                                        itemType: null,
                                                        itemNumber: null,
                                                        itemName: null,
                                                        description: null,
                                                        units: "Each",
                                                        drawingNumber: null,
                                                        itemTypeSpec: null
                                                    },
                                                    quantity: null,
                                                    fractionalQuantity: null
                                                };
                                                $scope.callback(data);
                                                $rootScope.showSuccessMessage("Item added successfully");
                                                $rootScope.hideBusyIndicator();
                                            }
                                        )
                                    }
                                )
                            }, function (error) {
                                $rootScope.hideBusyIndicator();
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                } else {
                    $scope.callback();
                    $rootScope.hideSidePanel();

                }
            }

            function validateItem() {
                var valid = true;
                if (vm.newBomItem.newItem.itemType == null || vm.newBomItem.newItem.itemType == "") {
                    valid = false;
                    $rootScope.showWarningMessage("Please select Type");
                } else if (vm.newBomItem.newItem.itemName == null || vm.newBomItem.newItem.itemName == "") {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Nomenclature");
                } else if (vm.newBomItem.newItem.itemType.hasSpec && (vm.newBomItem.newItem.itemTypeSpec == null || vm.newBomItem.newItem.itemTypeSpec == "" || vm.newBomItem.newItem.itemTypeSpec == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select Specification");
                } else if ((vm.typeParentNode.name == "Section" || vm.typeParentNode.name == "Sub System")
                    && (vm.newBomItem.newItem.itemCode == null || vm.newBomItem.newItem.itemCode == "" || vm.newBomItem.newItem.itemCode == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Item code at least 1 character (or) digit");
                } else if ((vm.typeParentNode.name == "System" || vm.typeParentNode.name == "Unit" || vm.typeParentNode.name == "Part")
                    && (vm.newBomItem.newItem.itemCode == null || vm.newBomItem.newItem.itemCode == "" || vm.newBomItem.newItem.itemCode == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Item code at least 2 character (or) digit");
                } else if (vm.newBomItem.newItem.itemCode != null && !validTypeCodeLength()) {
                    valid = false;
                } else if (vm.newBomItem.newItem.itemCode != null && !validTypeCode()) {
                    valid = false;
                } else if (vm.typeParentNode.name == "Part" && !vm.newBomItem.newItem.itemType.hasLots && (vm.newBomItem.quantity == null || vm.newBomItem.quantity == "" || vm.newBomItem.quantity == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Quantity");
                } else if (vm.typeParentNode.name == "Part" && vm.newBomItem.newItem.itemType.hasLots && (vm.newBomItem.fractionalQuantity == null || vm.newBomItem.fractionalQuantity == "" || vm.newBomItem.fractionalQuantity == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Quantity");
                }

                return valid;
            }

            function validTypeCode() {
                var valid = true;

                var itemCode = vm.typeCodeMap.get(vm.newBomItem.newItem.itemCode.toUpperCase());

                if (itemCode != null) {
                    valid = false;
                    $rootScope.showWarningMessage(itemCode.itemCode + " : Item Code already exist");
                }

                return valid;
            }

            function validTypeCodeLength() {
                var valid = true;

                if (vm.typeParentNode.name == "Section" || vm.typeParentNode.name == "Sub System") {
                    if (vm.newBomItem.newItem.itemCode.length != 1) {
                        valid = false;
                        $rootScope.showWarningMessage("Item code accepts 1 character (or) digit only")
                    }
                } else if (vm.typeParentNode.name == "System" || vm.typeParentNode.name == "Unit" || vm.typeParentNode.name == "Part") {
                    if (vm.newBomItem.newItem.itemCode.length != 2) {
                        valid = false;
                        $rootScope.showWarningMessage("Item code accepts 2 characters (or) digits only")
                    }
                }

                return valid;
            }

            function validateTypeAttributes() {
                var valid = true;
                angular.forEach(vm.requiredAttributes, function (attribute) {
                    if (valid) {
                        if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                            attribute.attributeDef.dataType != 'TIMESTAMP') {
                            if ($rootScope.checkAttribute(attribute)) {
                                valid = true;
                            }
                            else {
                                valid = false;
                                $rootScope.showErrorMessage(attribute.attributeDef.name + " : Attribute value required");
                            }
                        }
                    }
                });
                return valid;
            }

            function validateCustomAttributes() {
                var valid = true;

                vm.customAttritues = [];
                vm.customAttritues = vm.newItemAttributes;
                vm.customAttritues = vm.customAttritues.concat(vm.newItemRevisionAttributes);

                angular.forEach(vm.customAttritues, function (attribute) {
                    if (valid) {
                        if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                            attribute.attributeDef.dataType != 'TIMESTAMP') {
                            if ($rootScope.checkAttribute(attribute)) {
                                valid = true;
                            }
                            else {
                                valid = false;
                                $rootScope.showErrorMessage(attribute.attributeDef.name + " : Attribute value required");
                            }
                        }
                    }
                });
                return valid;
            }


            function saveObjectAttributes() {
                var defered = $q.defer();
                if (vm.newItemAttributes.length > 0) {
                    vm.propertyImageAttributes = [];
                    var propertyImages = new Hashtable();
                    angular.forEach(vm.newItemAttributes, function (attribute) {
                        attribute.id.objectId = vm.newBomItem.newItem.id;
                        if (attribute.timeValue != null) {
                            attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");

                        }
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            propertyImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            attribute.attachmentValues = addItemPropertyAttachment(attribute);
                        }
                    });
                    $timeout(function () {
                        ObjectAttributeService.saveItemObjectAttributes(vm.newBomItem.newItem.id, vm.newItemAttributes).then(
                            function (data) {
                                if (vm.propertyImageAttributes.length > 0) {
                                    angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                        ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, propertyImages.get(propImgAtt.id.attributeDef)).then(
                                            function (data) {
                                                defered.resolve();
                                            }
                                        )
                                    })
                                } else {
                                    defered.resolve();
                                }
                                defered.resolve();
                            },
                            function (error) {
                                $rootScope.hideBusyIndicator();
                                defered.reject();
                            }
                        )
                    }, 2000);
                } else if (vm.newItemRevisionAttributes <= 0) {
                    defered.resolve();
                }
                if (vm.newItemRevisionAttributes.length > 0) {
                    vm.itemRevisionPropertyImageAttributes = [];
                    var itemRevisionPropertyImages = new Hashtable();
                    angular.forEach(vm.newItemRevisionAttributes, function (attribute) {
                        attribute.id.objectId = vm.newBomItem.newItem.latestRevision;
                        if (attribute.timeValue != null) {
                            attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");

                        }
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            itemRevisionPropertyImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.itemRevisionPropertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            attribute.attachmentValues = addRevisionPropertyAttachment(attribute);
                        }
                    });
                    $timeout(function (data) {
                        ObjectAttributeService.saveItemObjectAttributes(vm.newBomItem.newItem.latestRevision, vm.newItemRevisionAttributes).then(
                            function (data) {
                                if (vm.itemRevisionPropertyImageAttributes.length > 0) {
                                    angular.forEach(vm.itemRevisionPropertyImageAttributes, function (revpropImgAtt) {
                                        ObjectAttributeService.uploadObjectAttributeImage(revpropImgAtt.id.objectId, revpropImgAtt.id.attributeDef, itemRevisionPropertyImages.get(revpropImgAtt.id.attributeDef)).then(
                                            function (data) {
                                                defered.resolve();

                                            }
                                        )
                                    })
                                } else {
                                    defered.resolve();
                                }
                                defered.resolve();
                            },
                            function (error) {
                                $rootScope.hideBusyIndicator();
                                defered.reject();
                            }
                        )
                    }, 2000);
                } else if (vm.newItemAttributes <= 0) {
                    defered.resolve();
                }
                return defered.promise;
            }

            /*-------------- To save Item Property Attachment  -------------------*/

            function addItemPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'ITEMMASTER', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }
                    )
                })
                return propertyAttachmentIds;
            }

            /*-------------- To save ItemRevision property Attachment  ----------------*/

            function addRevisionPropertyAttachment(attribute) {
                var revisionPropertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'ITEMREVISION', attachmentFile).then(
                        function (data) {
                            revisionPropertyAttachmentIds.push(data[0].id);
                        }
                    )
                })

                return revisionPropertyAttachmentIds;
            }


            /*-----------  To save Selected Type Attributes  ----------------*/

            function saveAttributes() {
                var defered = $q.defer();
                vm.imageAttributes = [];
                var images = new Hashtable();
                angular.forEach(vm.requiredAttributes, function (reqatt) {
                    vm.attributes.push(reqatt);
                })
                if (vm.attributes.length > 0) {
                    vm.requiredAttributes = [];
                    angular.forEach(vm.attributes, function (attribute) {
                        if (attribute.attributeDef.revisionSpecific == false) {
                            attribute.id.objectId = vm.newBomItem.newItem.id;

                            if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                                images.put(attribute.id.attributeDef, attribute.imageValue);
                                vm.imageAttributes.push(attribute);
                                attribute.imageValue = null;
                            }
                            if (attribute.timeValue != null) {
                                attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");
                            }
                            if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                                attribute.attachmentValues = addAttachment(attribute);
                            }
                        } else if (attribute.attributeDef.revisionSpecific == true) {
                            attribute.id.objectId = vm.newBomItem.newItem.latestRevision;

                            if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                                images.put(attribute.id.attributeDef, attribute.imageValue);
                                vm.imageAttributes.push(attribute);
                                attribute.imageValue = null;
                            }
                            if (attribute.timeValue != null) {
                                attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");
                            }
                            if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                                attribute.attachmentValues = addRevisionAttachment(attribute);
                            }
                        }

                    });
                    $timeout(function () {
                        if (vm.attributes.length > 0) {
                            ItemService.saveItemAttributes(vm.newBomItem.newItem.id, vm.attributes).then(
                                function (data) {
                                    if (vm.imageAttributes.length > 0) {
                                        angular.forEach(vm.imageAttributes, function (imgAtt) {
                                            ItemService.updateImageValue(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
                                                function (data) {

                                                    defered.resolve();
                                                }
                                            )
                                        })
                                    } else {
                                        defered.resolve();
                                    }
                                }, function () {
                                    defered.reject();
                                }
                            )
                        }
                    }, 5000)
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            /*------------- To save Non-Revision specific Attachment ------------*/

            function addAttachment(attribute) {
                var attachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'ITEMMASTER', attachmentFile).then(
                        function (data) {
                            attachmentIds.push(data[0].id);
                        }
                    )
                })

                return attachmentIds;
            }

            /*------------ To save Revision specific Attachments  --------------*/

            function addRevisionAttachment(attribute) {
                var revisionAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'ITEMREVISION', attachmentFile).then(
                        function (data) {
                            revisionAttachmentIds.push(data[0].id);
                        })
                })
                return revisionAttachmentIds;
            }


            vm.addToListValue = addToListValue;
            vm.cancelToListValue = cancelToListValue;

            function addToListValue(attribute) {
                if (attribute.listValue == undefined) {
                    attribute.listValue = [];
                }
                attribute.listValue.push(attribute.newListValue);
            }

            function cancelToListValue(attribute) {
                attribute.listValueEditMode = false;
            }

            function closeRightSidePanel() {
                $rootScope.newItemBomButton = false;
                vm.newBomItem = {
                    id: null,
                    bom: null,
                    item: null,
                    newItem: {
                        id: null,
                        itemType: null,
                        itemNumber: null,
                        itemName: null,
                        description: null,
                        units: "Each",
                        drawingNumber: null
                    },
                    quantity: 0,
                    fractionalQuantity: 0.0
                };
            }

            function loadObjectAttributeDefs() {
                vm.newItemAttributes = [];
                vm.newItemRevisionAttributes = [];
                ItemTypeService.getAttributesByObjectType("ITEMMASTER").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newBomItem.newItem.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                listValue: null,
                                newListValue: null,
                                timeValue: null,
                                timestampValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }

                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            vm.newItemAttributes.push(att);
                        });
                        return ItemTypeService.getAttributesByObjectType("ITEMREVISION");
                    }
                ).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newBomItem.newItem.latestRevision,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                listValue: null,
                                newListValue: null,
                                timeValue: null,
                                timestampValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            vm.newItemRevisionAttributes.push(att);
                        });
                    }
                )
            }

            function loadAttributeDefs() {
                vm.requiredAttributes = [];
                vm.attributes = [];
                ItemTypeService.getAttributesByTypeId(vm.newBomItem.newItem.itemType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newBomItem.newItem.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                newListValue: null,
                                listValueEditMode: false,
                                timestampValue: null,
                                booleanValue: false,
                                dateValue: null,
                                timeValue: null,
                                imageValue: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            if (attribute.required == false) {
                                vm.attributes.push(att);
                            } else {
                                vm.requiredAttributes.push(att);
                            }
                        });
                    }
                )
            }

            function searchTree() {
                if (vm.searchValue != null) {
                    $('#classificationTree').tree('expandAll');
                }
                $('#classificationTree').tree('doFilter', vm.searchValue);
            }

            vm.showDropMenu = showDropMenu;
            function showDropMenu() {

                $("#typeTree").show();
                vm.searchValue = null;
                $('#classificationTree').tree('doFilter', vm.searchValue)
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $scope.$on('app.bom.new', addItem);
                    loadObjectAttributeDefs();
                    button.text = "Close";
                    $rootScope.$on('app.rightside.panel.closing', closeRightSidePanel);
                }
            })();
        }
    }
);