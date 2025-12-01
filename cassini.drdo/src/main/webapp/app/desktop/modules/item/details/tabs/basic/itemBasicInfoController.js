define(
    [
        'app/desktop/modules/item/item.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],
    function (module) {
        module.controller('ItemBasicInfoController', ItemBasicInfoController);

        function ItemBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $translate, $stateParams, $cookies, CommonService, ItemTypeService,
                                         ItemService, ObjectAttributeService, AttachmentService, AttributeAttachmentService,
                                         DialogService) {

            $rootScope.viewInfo.icon = "fa fa-th";

            var vm = this;

            vm.loading = true;
            vm.loadingAttributes = true;
            vm.itemId = $stateParams.itemId;
            $rootScope.selectedItemDetails = null;

            vm.item = null;
            vm.itemRevision = null;
            vm.itemProperties = [];
            vm.itemRevisionProperties = [];

            var currencyMap = new Hashtable();

            $scope.opened = {};
            vm.barcodeImageLoading = true;
            vm.qrcodeImageLoading = true;
            vm.updateItem = updateItem;
            vm.barcodeImageLoaded = barcodeImageLoaded;
            vm.qrcodeImageLoaded = qrcodeImageLoaded;
            vm.validateAttribute = validateAttribute;
            vm.change = change;
            vm.cancel = cancel;
            vm.saveImage = saveImage;
            vm.showImageProperty = showImageProperty;
            vm.showRevisionImageProperty = showRevisionImageProperty;
            vm.addAttachment = addAttachment;
            vm.cancelAttachment = cancelAttachment;
            vm.saveAttachments = saveAttachments;
            vm.saveRevisionAttachments = saveRevisionAttachments;
            vm.changeTime = changeTime;
            vm.cancelTime = cancelTime;
            vm.saveTimeProperty = saveTimeProperty;
            vm.changeTimestamp = changeTimestamp;
            vm.openPropertyAttachment = openPropertyAttachment;
            vm.deleteAttachments = deleteAttachments;
            vm.changeCurrencyValue = changeCurrencyValue;
            vm.saveThumbnail = saveThumbnail;
            vm.thumbnail = null;
            vm.cancelThumbnail = cancelThumbnail;
            vm.addImage = false;
            vm.changeThumbnail = changeThumbnail;
            vm.cancelChanges = cancelChanges;
            vm.changeAttribute = changeAttribute;
            vm.saveItemProperties = saveItemProperties;

            /*---------  To get Item Details  ---------*/

            function loadItem() {
                vm.loading = true;
                ItemService.getRevisionId(vm.itemId).then(
                    function (data) {
                        vm.itemRevision = data;
                        vm.item = vm.itemRevision.itemMaster;
                        vm.item.drawingNumber = vm.itemRevision.drawingNumber;
                        if (vm.item.thumbnail != null) {
                            vm.item.thumbnailImage = "api/plm/items/" + vm.item.id + "/itemImageAttribute/download?" + new Date().getTime();
                        }
                        loadPersons();
                        loadObjectAttributeDefs();
                    }
                )
            }

            /*------  To get createdBy and CreatedDate by Ids ---------*/

            function loadPersons() {
                var personIds = [vm.item.createdBy];

                if (vm.item.createdBy != vm.item.modifiedBy) {
                    personIds.push(vm.item.modifiedBy);
                }

                CommonService.getPersons(personIds).then(
                    function (persons) {
                        var map = new Hashtable();
                        angular.forEach(persons, function (person) {
                            map.put(person.id, person);
                        });

                        if (vm.item.createdBy != null) {
                            var person = map.get(vm.item.createdBy);
                            if (person != null) {
                                vm.item.createdByPerson = person;
                            }
                            else {
                                vm.item.createdByPerson = {firstName: ""};
                            }
                        }

                        if (vm.item.modifiedBy != null) {
                            person = map.get(vm.item.modifiedBy);
                            if (person != null) {
                                vm.item.modifiedByPerson = person;
                            }
                            else {
                                vm.item.modifiedByPerson = {firstName: ""};
                            }
                        }
                        vm.loading = false;
                    }
                );
            }

            /*----  To update changes Item Details  -----*/

            function updateItem() {
                ItemService.updateItem(vm.item).then(
                    function (data) {
                        vm.item.name = data.name;
                        vm.item.description = data.description;
                        vm.itemRevision.drawingNumber = vm.item.drawingNumber;
                        $rootScope.viewInfo.description = vm.item.itemName;
                        $rootScope.showSuccessMessage("Item updated successfully");

                    }
                )
            }

            function isInteger(x) {
                return x % 1 === 0;
            }

            /*-- To check given value Integer or not */

            function validateAttribute(value) {
                if (isInteger(value)) {
                    return true;
                } else {
                    return integerValid;
                }
            }

            function barcodeImageLoaded() {
                vm.barcodeImageLoading = false;
            }

            function qrcodeImageLoaded() {
                vm.qrcodeImageLoading = false;
            }

            /*---------  To load custom properties using OBJECTTYPE --------*/

            function loadObjectAttributeDefs() {
                ItemTypeService.getAttributesByObjectType("ITEMMASTER").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.item.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                value: {
                                    id: {
                                        objectId: vm.item.id,
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
                            vm.itemProperties.push(att);
                        });
                        return ItemTypeService.getAttributesByObjectType("ITEMREVISION");
                    }
                ).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.itemId,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                value: {
                                    id: {
                                        objectId: vm.itemId,
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
                                revAttachmentValues: [],
                                showTimeAttribute: false,
                                showTimestamp: false,
                                timestampValue: null,
                                editMode: false,
                                changeCurrency: false
                            };
                            vm.itemRevisionProperties.push(att);
                        });

                        loadObjectAttributes();
                    }
                )
            }

            /*------- To get Property values by itemId and revisionId ----------*/

            function loadObjectAttributes() {
                ObjectAttributeService.getAllObjectAttributes(vm.item.id).then(
                    function (data) {
                        var map = new Hashtable();

                        angular.forEach(data, function (attribute) {
                            map.put(attribute.id.attributeDef, attribute);
                        });

                        angular.forEach(vm.itemProperties, function (attribute) {
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
                        return ObjectAttributeService.getAllObjectAttributes(vm.itemId);
                    }
                ).then(
                    function (data) {
                        vm.loadingAttributes = false;
                        var map = new Hashtable();
                        angular.forEach(data, function (attribute) {
                            map.put(attribute.id.attributeDef, attribute);
                        });

                        angular.forEach(vm.itemRevisionProperties, function (attribute) {
                            var attachmentIds = [];
                            var value = map.get(attribute.attributeDef.id);
                            if (value != null) {
                                if (value.attachmentValues.length > 0) {
                                    angular.forEach(value.attachmentValues, function (attachment) {
                                        attachmentIds.push(attachment);
                                    })
                                    AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                        function (data) {
                                            vm.itemRevisionAttachments = data;
                                            attribute.value.attachmentValues = vm.itemRevisionAttachments;
                                        }
                                    )
                                }

                                attribute.value.stringValue = value.stringValue;
                                attribute.value.integerValue = value.integerValue;
                                if (value.doubleValue != null) {
                                    attribute.value.doubleValue = parseFloat(value.doubleValue).toFixed(5);
                                } else {
                                    attribute.value.doubleValue = value.doubleValue;
                                }
                                attribute.value.booleanValue = value.booleanValue;
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
                                attribute.value.revisionImagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                            }
                        });
                    }
                )
            }

            function saveItemProperties(attribute) {
                if (attribute.attributeDef.objectType == 'ITEMMASTER') {
                    ObjectAttributeService.updateObjectAttribute(vm.item.id, attribute.value).then(
                        function (data) {
                            attribute.changeCurrency = false;
                            attribute.editMode = false;
                            loadObjectAttributes();
                            $rootScope.showSuccessMessage("Value saved successfully");
                            attribute.listValueEditMode = false;
                        }
                    )
                } else if (attribute.attributeDef.objectType == 'ITEMREVISION') {
                    ObjectAttributeService.updateObjectAttribute(vm.itemId, attribute.value).then(
                        function (data) {
                            attribute.changeCurrency = false;
                            attribute.editMode = false;
                            loadObjectAttributes();
                            $rootScope.showSuccessMessage("Value saved successfully");
                            attribute.listValueEditMode = false;
                        }
                    )
                }
            }

            function saveTimeProperty(attribute) {
                if (attribute.timeValue != null) {
                    attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");
                    attribute.value.timeValue = attribute.timeValue;
                    if (attribute.attributeDef.objectType == 'ITEMMASTER') {
                        ObjectAttributeService.updateObjectAttribute(vm.item.id, attribute.value).then(
                            function (data) {
                                attribute.showTimeAttribute = false;
                                $rootScope.showSuccessMessage("Value updated successfully");
                                loadObjectAttributes();
                            }
                        )
                    } else if (attribute.attributeDef.objectType == 'ITEMREVISION') {
                        ObjectAttributeService.updateObjectAttribute(vm.itemId, attribute.value).then(
                            function (data) {
                                attribute.showTimeAttribute = false;
                                $rootScope.showSuccessMessage("Value updated successfully");
                                loadObjectAttributes();
                            }
                        )
                    }

                } else if (attribute.timestampValue != null) {
                    attribute.timestampValue = moment(attribute.timestampValue).format('DD/MM/YYYY, HH:mm:ss');
                    attribute.value.timestampValue = attribute.timestampValue;
                    if (attribute.attributeDef.objectType == 'ITEMMASTER') {
                        ObjectAttributeService.updateObjectAttribute(vm.item.id, attribute.value).then(
                            function (data) {
                                attribute.showTimestamp = false;
                                $rootScope.showSuccessMessage("Value updated successfully");
                                loadObjectAttributes();
                            }
                        )
                    } else if (attribute.attributeDef.objectType == 'ITEMREVISION') {
                        ObjectAttributeService.updateObjectAttribute(vm.itemId, attribute.value).then(
                            function (data) {
                                attribute.showTimestamp = false;
                                $rootScope.showSuccessMessage("Value updated successfully");
                                loadObjectAttributes();
                            }
                        )
                    }

                }
            }

            function changeTime(attribute) {
                attribute.showTimeAttribute = true;
            }

            function cancelTime(attribute) {
                attribute.showTimeAttribute = false;
                attribute.showTimestamp = false;
            }

            function changeTimestamp(attribute) {
                attribute.showTimestamp = true;
            }

            function changeAttribute(attribute) {
                vm.stringValue = null;
                vm.integerValue = null;
                vm.dateValue = null;
                vm.doubleValue = null;
                vm.booleanValue = null;
                vm.currencyValue = null;
                attribute.editMode = true;
                vm.stringValue = attribute.value.stringValue;
                vm.integerValue = attribute.value.integerValue;
                vm.dateValue = attribute.value.dateValue;
                vm.doubleValue = attribute.value.doubleValue;
                vm.booleanValue = attribute.value.booleanValue;
                vm.currencyValue = attribute.value.currencyValue;

            }

            vm.changeCurrencyAttribute = changeCurrencyAttribute;

            function changeCurrencyAttribute(attribute) {
                attribute.changeCurrency = true;
            }

            vm.cancelCurrencyChanges = cancelCurrencyChanges;
            function cancelCurrencyChanges(attribute) {
                attribute.changeCurrency = false;
            }

            function cancelChanges(attribute) {
                attribute.editMode = false;
                attribute.changeCurrency = false;
                attribute.value.stringValue = vm.stringValue;
                attribute.value.integerValue = vm.integerValue;
                attribute.value.dateValue = vm.dateValue;
                attribute.value.doubleValue = vm.doubleValue;
                attribute.value.booleanValue = vm.booleanValue;
                attribute.value.currencyValue = vm.currencyValue;
            }

            function change(attribute) {
                attribute.changeImage = true;
                var itemImageFile = document.getElementById("itemImageFile");
                if (itemImageFile != null && itemImageFile != undefined) {
                    document.getElementById("itemImageFile").value = "";
                }
                var itemRevisionImageFile = document.getElementById("itemRevisionImageFile");
                if (itemRevisionImageFile != null && itemRevisionImageFile != undefined) {
                    document.getElementById("itemRevisionImageFile").value = "";
                }
            }

            function cancel(attribute) {
                attribute.changeImage = false;
            }

            function addAttachment(attribute) {
                attribute.showAttachment = true;
                var itemAttachmentFile = document.getElementById("itemAttachmentFile");
                if (itemAttachmentFile != null && itemAttachmentFile != undefined) {
                    document.getElementById("itemAttachmentFile").value = "";
                }
                var itemRevisionAttachmentFile = document.getElementById("itemRevisionAttachmentFile");
                if (itemRevisionAttachmentFile != null && itemRevisionAttachmentFile != undefined) {
                    document.getElementById("itemRevisionAttachmentFile").value = "";
                }
            }

            function cancelAttachment(attribute) {
                attribute.showAttachment = false;
                attribute.attachmentValues = [];
            }

            function changeCurrencyValue(attribute) {
                attribute.changeCurrency = true;
            }

            $scope.open = function ($event, elementOpened) {
                $event.preventDefault();
                $event.stopPropagation();

                $scope.opened[elementOpened] = !$scope.opened[elementOpened];
            };

            $scope.$on('attributes', function (event, arg) {
                vm.attributes = arg;
            });


            /*--- To save Image property ---*/

            function saveImage(attribute) {
                if (attribute.newImageValue != null) {
                    $rootScope.showBusyIndicator($(".view-content"));
                    attribute.imageValue = attribute.newImageValue;
                    if (attribute.attributeDef.objectType == 'ITEMMASTER') {
                        ObjectAttributeService.updateObjectAttribute(vm.item.id, attribute.value).then(
                            function (data) {
                                ObjectAttributeService.uploadObjectAttributeImage(attribute.id.objectId, attribute.id.attributeDef, attribute.imageValue).then(
                                    function (data) {
                                        attribute.changeImage = false;
                                        attribute.newImageValue = null;
                                        attribute.value.itemImagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();
                                        $rootScope.showSuccessMessage("Image saved successfully");
                                        loadObjectAttributes();
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        )
                    } else if (attribute.attributeDef.objectType == 'ITEMREVISION') {
                        ObjectAttributeService.updateObjectAttribute(vm.itemId, attribute.value).then(
                            function (data) {
                                ObjectAttributeService.uploadObjectAttributeImage(attribute.id.objectId, attribute.id.attributeDef, attribute.imageValue).then(
                                    function (data) {
                                        attribute.changeImage = false;
                                        attribute.newImageValue = null;
                                        attribute.value.revisionImagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();
                                        $rootScope.showSuccessMessage("Image saved successfully");
                                        loadObjectAttributes();
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        )
                    }
                }
            }

            /*---- To save Item Attachment Property ----*/

            function saveAttachments(attribute) {
                if (attribute.attachmentValues.length == 0) {
                    $rootScope.showWarningMessage("Please select file");
                }
                if (attribute.attachmentValues.length > 0) {
                    var itemAttachPropertyIds = [];
                    var itemAttachments = attribute.value.attachmentValues;
                    attribute.value.attachmentValues = [];
                    angular.forEach(itemAttachments, function (attachment) {
                        itemAttachPropertyIds.push(attachment.id);
                    });
                    angular.forEach(attribute.attachmentValues, function (attachmentValue) {
                        var itemAttachmentExists = false;
                        angular.forEach(itemAttachments, function (itemAttachment) {
                            if ((attribute.id.objectId == itemAttachment.objectId) && (attribute.id.attributeDef == itemAttachment.attributeDef) && (attachmentValue.name == itemAttachment.name)) {
                                itemAttachmentExists = true;
                                itemAttachPropertyIds.remove(itemAttachment.id);

                                var options = {
                                    title: "Add Attachment",
                                    message: attachmentValue.name + " : Attachment already exists. Do you want to override ?",
                                    okButtonClass: 'btn-danger'
                                }
                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        $rootScope.showBusyIndicator($(".view-content"));
                                        AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'ITEMMASTER', attachmentValue).then(
                                            function (data) {
                                                itemAttachPropertyIds.push(data[0].id);
                                                attribute.attachmentValues.remove(attachmentValue);
                                                if (itemAttachPropertyIds.length > 0) {
                                                    angular.forEach(itemAttachPropertyIds, function (revAttachId) {
                                                        attribute.value.attachmentValues.push(revAttachId);
                                                    });
                                                    ObjectAttributeService.updateObjectAttribute(vm.item.id, attribute.value).then(
                                                        function (data) {
                                                            $rootScope.hideBusyIndicator();
                                                            if (attribute.attachmentValues.length == 0) {
                                                                loadObjectAttributes();
                                                                attribute.showAttachment = false;
                                                                $rootScope.showSuccessMessage("Attachments saved successfully");
                                                            }
                                                        }
                                                    )
                                                }
                                            }
                                        )
                                    } else {
                                        attribute.showAttachment = false;
                                        loadObjectAttributes();
                                    }
                                });
                            }
                        })
                        if (itemAttachmentExists == false) {
                            $rootScope.showBusyIndicator($(".view-content"));
                            AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'ITEMMASTER', attachmentValue).then(
                                function (data) {
                                    itemAttachPropertyIds.push(data[0].id);
                                    attribute.attachmentValues.remove(attachmentValue);
                                    if (itemAttachPropertyIds.length > 0) {
                                        angular.forEach(itemAttachPropertyIds, function (itemAttachId) {
                                            attribute.value.attachmentValues.push(itemAttachId);
                                        });
                                        ObjectAttributeService.updateObjectAttribute(vm.item.id, attribute.value).then(
                                            function (data) {
                                                $rootScope.hideBusyIndicator();
                                                if (attribute.attachmentValues.length == 0) {
                                                    loadObjectAttributes();
                                                    attribute.showAttachment = false;
                                                    $rootScope.showSuccessMessage("Attachments saved successfully");
                                                }

                                            }
                                        )
                                    }
                                }
                            )
                        }
                    })
                }
            }

            /*---- To save ItemRevision Attachment Property ----*/

            function saveRevisionAttachments(attribute) {
                if (attribute.revAttachmentValues.length == 0) {
                    $rootScope.showWarningMessage("Please select file");
                }
                if (attribute.revAttachmentValues.length > 0) {
                    var revisionAttachPropertyIds = [];
                    var revisionAttachments = attribute.value.attachmentValues;
                    attribute.value.attachmentValues = [];
                    angular.forEach(revisionAttachments, function (attachment) {
                        revisionAttachPropertyIds.push(attachment.id);
                    })
                    angular.forEach(attribute.revAttachmentValues, function (attachmentValue) {
                        var revisionAttachmentExists = false;
                        angular.forEach(revisionAttachments, function (revisionAttachment) {
                            if ((attribute.id.objectId == revisionAttachment.objectId) && (attribute.id.attributeDef == revisionAttachment.attributeDef) && (attachmentValue.name == revisionAttachment.name)) {
                                revisionAttachmentExists = true;
                                revisionAttachPropertyIds.remove(revisionAttachment.id);

                                var options = {
                                    title: "Add Attachment",
                                    message: attachmentValue.name + " : Attachment already exists. Do you want to override ?",
                                    okButtonClass: 'btn-danger'
                                }
                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        $rootScope.showBusyIndicator($(".view-content"));
                                        AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'ITEMREVISION', attachmentValue).then(
                                            function (data) {
                                                revisionAttachPropertyIds.push(data[0].id);
                                                attribute.revAttachmentValues.remove(attachmentValue);
                                                if (revisionAttachPropertyIds.length > 0) {
                                                    angular.forEach(revisionAttachPropertyIds, function (revAttachId) {
                                                        attribute.value.attachmentValues.push(revAttachId);
                                                    });
                                                    ObjectAttributeService.updateObjectAttribute(vm.itemId, attribute.value).then(
                                                        function (data) {
                                                            $rootScope.hideBusyIndicator();
                                                            if (attribute.revAttachmentValues.length == 0) {
                                                                loadObjectAttributes();
                                                                attribute.showAttachment = false;
                                                                $rootScope.showSuccessMessage("Attachments saved successfully");
                                                            }
                                                        }
                                                    )
                                                }
                                            }
                                        )
                                    } else {
                                        attribute.showAttachment = false;
                                        loadObjectAttributes();
                                    }
                                });
                            }
                        })
                        if (revisionAttachmentExists == false) {
                            $rootScope.showBusyIndicator($(".view-content"));
                            AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'ITEMREVISION', attachmentValue).then(
                                function (data) {
                                    revisionAttachPropertyIds.push(data[0].id);
                                    attribute.revAttachmentValues.remove(attachmentValue);
                                    if (revisionAttachPropertyIds.length > 0) {
                                        angular.forEach(revisionAttachPropertyIds, function (revAttachId) {
                                            attribute.value.attachmentValues.push(revAttachId);
                                        });
                                        ObjectAttributeService.updateObjectAttribute(vm.itemId, attribute.value).then(
                                            function (data) {
                                                $rootScope.hideBusyIndicator();
                                                if (attribute.revAttachmentValues.length == 0) {
                                                    loadObjectAttributes();
                                                    attribute.showAttachment = false;
                                                    $rootScope.showSuccessMessage("Attachments saved successfully");
                                                }
                                            }
                                        )
                                    }
                                }
                            )
                        }
                    })
                }
            }

            function deleteAttachments(attribute, attachment) {

                var options = {
                    title: "Delete Attachment",
                    message: "Please confirm to delete this Attachment",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        var attachments = attribute.value.attachmentValues;
                        attribute.value.attachmentValues = [];
                        angular.forEach(attachments, function (attach) {
                            if (attach.id != attachment.id) {
                                attribute.value.attachmentValues.push(attach.id);
                            }
                        });
                        if (attribute.attributeDef.objectType == 'ITEMMASTER') {
                            ObjectAttributeService.updateObjectAttribute(vm.item.id, attribute.value).then(
                                function (data) {
                                    AttributeAttachmentService.deleteAttributeAttachment(attachment.id).then(
                                        function (data) {
                                            loadObjectAttributes();
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.showSuccessMessage(attachment.name + ' : Attachment deleted successfully');
                                        }
                                    )

                                }
                            )
                        } else if (attribute.attributeDef.objectType == 'ITEMREVISION') {
                            ObjectAttributeService.updateObjectAttribute(vm.itemId, attribute.value).then(
                                function (data) {
                                    AttributeAttachmentService.deleteAttributeAttachment(attachment.id).then(
                                        function (data) {
                                            loadObjectAttributes();
                                            $rootScope.showSuccessMessage(attachment.name + ' : Attachment deleted successfully');
                                        }
                                    )

                                }
                            )
                        }
                    }
                });
            }

            /*---- To show Large Image of item image property -----*/

            function showImageProperty(attribute) {
                var modal = document.getElementById('myModal22');
                var modalImg = document.getElementById("img01");

                modal.style.display = "block";
                modalImg.src = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                var span = document.getElementsByClassName("closeimage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            /*---- To show Large Image of itemRevision image property -----*/

            function showRevisionImageProperty(attribute) {
                var modal = document.getElementById('myModal11');
                var modalImg = document.getElementById("img02");

                modal.style.display = "block";
                modalImg.src = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                var span = document.getElementsByClassName("closeImage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }


            vm.showThumbnailImage = showThumbnailImage;
            function showThumbnailImage(item) {
                var modal = document.getElementById('myModal21');
                var modalImg = document.getElementById("img012");

                modal.style.display = "block";
                modalImg.src = "api/plm/items/" + vm.item.id + "/itemImageAttribute/download?" + new Date().getTime();

                var span = document.getElementsByClassName("closeimage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            /*---- To download Attachment Property File -----*/

            function openPropertyAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }

            function saveThumbnail(image) {
                if (image == null) {
                    $rootScope.showWarningMessage("Please select the image");
                } else {
                    if (validate()) {
                        ItemService.uploadImage(vm.item.id, image).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Image saved successfully");
                                loadItem();
                            }
                        );
                    }
                }
            }

            function validate() {
                var valid = true;
                if (vm.thumbnail != null) {
                    var fup = document.getElementById('filename');
                    var fileName = fup.value;
                    var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                    if (ext == "JPEG" || ext == "jpeg" || ext == "jpg" || ext == "JPG" || ext == "PNG" || ext == "png" || ext == "GIF" || ext == "gif") {
                        return true;
                    } else {
                        $rootScope.showWarningMessage("Please upload image file");
                        fup.focus();
                        valid = false;
                    }
                }

                return valid;
            }

            function cancelThumbnail() {
                vm.thumbnail = null;
                vm.addImage = false;
            }

            function changeThumbnail() {
                vm.addImage = true;
            }

            (function () {
                if ($application.homeLoaded == true) {
                    ObjectAttributeService.getCurrencies().then(
                        function (data) {
                            vm.currencies = data;
                            angular.forEach(vm.currencies, function (currency) {
                                currencyMap.put(currency.id, $sce.trustAsHtml(currency.symbol));
                            });
                        }
                    );

                    loadItem();
                }
            })();
        }
    }
);