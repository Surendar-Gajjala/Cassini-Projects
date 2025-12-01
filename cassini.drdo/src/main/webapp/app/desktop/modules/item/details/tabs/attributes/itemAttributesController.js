define(
    [
        'app/desktop/modules/item/item.module',
        'moment',
        'moment-timezone-with-data',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('ItemAttributesController', ItemAttributesController);

        function ItemAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $translate, $cookies, ItemTypeService,
                                          ItemService, ObjectAttributeService, AttachmentService, AttributeAttachmentService, DialogService,
                                          ObjectTypeAttributeService, CommonService) {
            var vm = this;

            vm.itemId = $stateParams.itemId;
            vm.item = null;
            var currencyMap = new Hashtable();
            vm.trueValues = [];
            vm.falseValues = [];
            vm.flags = [{
                name: "True",
                value: true
            },
                {
                    name: "False",
                    value: false
                }
            ];
            vm.saveItemAttribute = saveItemAttribute;
            $scope.opened = {};
            vm.addToListValue = addToListValue;
            vm.cancelToListValue = cancelToListValue;
            vm.validateAttribute = validateAttribute;
            vm.change = change;
            vm.cancel = cancel;
            vm.changeTime = changeTime;
            vm.cancelTime = cancelTime;
            vm.saveTime = saveTime;
            vm.saveImage = saveImage;
            vm.showImage = showImage;
            vm.showRevisionImage = showRevisionImage;
            vm.addAttachment = addAttachment;
            vm.cancelAttachment = cancelAttachment;
            vm.saveAttachments = saveAttachments;
            vm.saveRevisionAttachments = saveRevisionAttachments;
            vm.changeTimestamp = changeTimestamp;
            vm.loading = true;
            vm.openAttachment = openAttachment;

            vm.deleteAttachments = deleteAttachments;
            vm.changeCurrencyValue = changeCurrencyValue;

            vm.choices = [
                {
                    name: "True",
                    value: true
                },
                {
                    name: "False",
                    value: false
                }
            ];

            function addToListValue(attribute) {
                if (attribute.value.listValue == undefined) {
                    attribute.value.listValue = [];
                }

                attribute.value.listValue.push(attribute.newListValue);
            }

            function cancelToListValue(attribute) {
                attribute.listValueEditMode = false;
            }

            function change(attribute) {
                attribute.changeImage = true;
                var attributeImageFile = document.getElementById("attributeImageFile");
                if (attributeImageFile != null && attributeImageFile != undefined) {
                    document.getElementById("attributeImageFile").value = "";
                }
                var attributeRevisionImageFile = document.getElementById("attributeRevisionImageFile");
                if (attributeRevisionImageFile != null && attributeRevisionImageFile != undefined) {
                    document.getElementById("attributeRevisionImageFile").value = "";
                }
            }

            function cancel(attribute) {
                attribute.changeImage = false;
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

            function addAttachment(attribute) {
                attribute.showAttachment = true;
                var attributeAttachmentFile = document.getElementById("attributeAttachmentFile");
                if (attributeAttachmentFile != null && attributeAttachmentFile != undefined) {
                    document.getElementById("attributeAttachmentFile").value = "";
                }
                var attributeRevisionAttachmentFile = document.getElementById("attributeRevisionAttachmentFile");
                if (attributeRevisionAttachmentFile != null && attributeRevisionAttachmentFile != undefined) {
                    document.getElementById("attributeRevisionAttachmentFile").value = "";
                }
            }

            function cancelAttachment(attribute) {
                attribute.showAttachment = false;
                attribute.attachmentValues = [];
                attribute.revAttachmentValues = [];
            }

            vm.cancelChanges = cancelChanges;

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

            /*----------- To Get Basic Item Details  ----------------*/

            function loadItem() {
                vm.loading = true;
                ItemService.getRevisionId(vm.itemId).then(
                    function (data) {
                        vm.itemRevision = data;
                        vm.item = vm.itemRevision.itemMaster;
                        loadAttributeDefs();
                        $rootScope.viewInfo.title = $rootScope.selectedItemRevisionDetails.itemMaster.itemName
                            + " - " + $rootScope.selectedTab.heading;
                    }
                )
            }

            /*------------ To get Attribute Names By Type ID --------------*/

            function loadAttributeDefs() {
                ItemTypeService.getAttributesByTypeId(vm.item.itemType.id).then(
                    function (data) {
                        vm.loading = false;
                        angular.forEach(data, function (attribute) {
                            if (attribute.revisionSpecific == false) {
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
                                    showAttachment: false,
                                    attachmentValues: [],
                                    timeValue: null,
                                    showTimeAttribute: false,
                                    timestampValue: null,
                                    showTimestamp: false,
                                    editMode: false,
                                    changeCurrency: false
                                };

                                vm.attributes.push(att);
                            } else if (attribute.revisionSpecific == true) {
                                var revisionAttribute = {
                                    id: {
                                        objectId: vm.item.latestRevision,
                                        attributeDef: attribute.id
                                    },
                                    attributeDef: attribute,
                                    value: {
                                        id: {
                                            objectId: vm.item.latestRevision,
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
                                    showAttachment: false,
                                    revAttachmentValues: [],
                                    timeValue: null,
                                    showTimeAttribute: false,
                                    timestampValue: null,
                                    showTimestamp: false,
                                    editMode: false,
                                    changeCurrency: false
                                };

                                vm.revisionAttributes.push(revisionAttribute);
                            }

                        });
                        loadAttributes();
                    }
                )
            }

            /*----- To get Attribute Values with ItemId and RevisionId -------*/

            function loadAttributes() {
                ItemService.getItemAttributes(vm.item.id).then(
                    function (data) {
                        var map = new Hashtable();

                        angular.forEach(data, function (attribute) {
                            map.put(attribute.id.attributeDef, attribute);
                        });

                        angular.forEach(vm.attributes, function (attribute) {
                                var attachmentIds = [];
                                var value = map.get(attribute.attributeDef.id);
                                if (value != null) {
                                    if (value.attachmentValues.length > 0) {
                                        angular.forEach(value.attachmentValues, function (attachment) {
                                            attachmentIds.push(attachment);
                                        })
                                        AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                            function (data) {
                                                vm.attachments = data;
                                                attribute.value.attachmentValues = vm.attachments;
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
                                    attribute.value.currencyType = value.currencyType;
                                    if (value.currencyType != null) {
                                        attribute.value.encodedCurrencyType = currencyMap.get(value.currencyType);
                                    }
                                    attribute.value.itemImagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                }
                            }
                        );
                        return ItemService.getItemRevisionAttributes(vm.itemId);
                    }
                ).then(
                    function (data) {
                        var map = new Hashtable();

                        angular.forEach(data, function (attribute) {
                            map.put(attribute.id.attributeDef, attribute);
                        });

                        angular.forEach(vm.revisionAttributes, function (attribute) {
                            var revisionAttachmentIds = [];
                            var value = map.get(attribute.attributeDef.id);
                            if (value != null) {
                                if (value.attachmentValues.length > 0) {
                                    angular.forEach(value.attachmentValues, function (attachment) {
                                        revisionAttachmentIds.push(attachment);
                                    });
                                    AttributeAttachmentService.getMultipleAttributeAttachments(revisionAttachmentIds).then(
                                        function (data) {
                                            vm.revisionAttachments = data;
                                            attribute.value.attachmentValues = vm.revisionAttachments;
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
                                attribute.value.currencyType = value.currencyType;
                                if (value.currencyType) {
                                    attribute.value.encodedCurrencyType = currencyMap.get(value.currencyType);
                                }
                                attribute.value.revisionImagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();
                            }
                        });
                    }
                )
            }


            function isInteger(x) {
                return x % 1 === 0;
            }

            /*---- To check given value is Integer or not -----*/

            function validateAttribute(value) {
                if (isInteger(value)) {
                    return true;
                } else {
                    return "This attribute only accept integer value.";
                }
            }

            /*------ To update String, Integer, Boolean, Date, Double Attributes ------*/

            function saveItemAttribute(attribute) {

                if (attribute.value.id == undefined || attribute.value.id == null) {
                    if (attribute.attributeDef.revisionSpecific == false) {
                        ItemService.createItemAttribute(vm.item.id, attribute.value).then(
                            function (data) {
                                attribute.value.id = data.id;
                                attribute.listValueEditMode = false;
                                attribute.changeCurrency = false;
                                attribute.editMode = false;
                                loadAttributes();
                                $rootScope.showSuccessMessage("Value saved successfully");
                            }
                        )
                    } else if (attribute.attributeDef.revisionSpecific == true) {
                        ItemService.createItemRevisionAttribute(vm.itemId, attribute.value).then(
                            function (data) {
                                attribute.value.id = data.id;
                                attribute.listValueEditMode = false;
                                attribute.changeCurrency = false;
                                attribute.editMode = false;
                                loadAttributes();
                                $rootScope.showSuccessMessage("Value saved successfully");
                            }
                        )
                    }

                }
                else {
                    if (attribute.attributeDef.revisionSpecific == false) {
                        ItemService.updateItemAttribute(vm.item.id, attribute.value).then(
                            function (data) {
                                attribute.changeCurrency = false;
                                attribute.listValueEditMode = false;
                                attribute.editMode = false;
                                loadAttributes();
                                $rootScope.showSuccessMessage("Value saved successfully");
                            }
                        )
                    } else if (attribute.attributeDef.revisionSpecific == true) {
                        ItemService.updateItemRevisionAttribute(vm.itemId, attribute.value).then(
                            function (data) {
                                attribute.changeCurrency = false;
                                attribute.listValueEditMode = false;
                                attribute.editMode = false;
                                loadAttributes();
                                $rootScope.showSuccessMessage("Value saved successfully");
                            }
                        )
                    }
                }
            }


            function saveTime(attribute) {
                if (attribute.timeValue != null) {
                    attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");
                    attribute.value.timeValue = attribute.timeValue;
                    if (attribute.attributeDef.revisionSpecific == false) {
                        ItemService.updateItemAttribute(vm.item.id, attribute.value).then(
                            function (data) {
                                attribute.showTimeAttribute = false;
                                $rootScope.showSuccessMessage("Value saved successfully");
                                loadAttributes();
                            }
                        )
                    } else if (attribute.attributeDef.revisionSpecific == true) {
                        ItemService.updateItemRevisionAttribute(vm.itemId, attribute.value).then(
                            function (data) {
                                attribute.showTimeAttribute = false;
                                $rootScope.showSuccessMessage("Value saved successfully");
                                loadAttributes();
                            }
                        )
                    }
                } else if (attribute.timestampValue != null) {
                    attribute.timestampValue = moment(attribute.timestampValue).format('DD/MM/YYYY, HH:mm:ss');
                    attribute.value.timestampValue = attribute.timestampValue;
                    if (attribute.attributeDef.revisionSpecific == false) {
                        ItemService.updateItemAttribute(vm.item.id, attribute.value).then(
                            function (data) {
                                attribute.showTimestamp = false;
                                $rootScope.showSuccessMessage("Value saved successfully");
                                loadAttributes();
                            }
                        )
                    } else if (attribute.attributeDef.revisionSpecific == true) {
                        ItemService.updateItemRevisionAttribute(vm.itemId, attribute.value).then(
                            function (data) {
                                attribute.showTimestamp = false;
                                $rootScope.showSuccessMessage("Value saved successfully");
                                loadAttributes();
                            }
                        )
                    }

                }
            }

            /*-----------------  To Save IMAGE Attribute Values  ----------------*/

            function saveImage(attribute) {
                if (attribute.newImageValue == null) {
                    $rootScope.showWarningMessage("Please select image");
                }
                if (attribute.newImageValue != null) {
                    $rootScope.showBusyIndicator($(".view-content"));
                    attribute.imageValue = attribute.newImageValue;
                    if (attribute.attributeDef.revisionSpecific == false) {
                        ItemService.updateItemAttribute(attribute.id.objectId, attribute.value).then(
                            function (data) {
                                ObjectAttributeService.uploadObjectAttributeImage(attribute.id.objectId, attribute.id.attributeDef, attribute.imageValue).then(
                                    function (data) {
                                        attribute.changeImage = false;
                                        attribute.newImageValue = null;
                                        attribute.value.itemImagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();
                                        $rootScope.showSuccessMessage("Image saved successfully");
                                        loadAttributes();
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        )
                    } else if (attribute.attributeDef.revisionSpecific == true) {
                        ItemService.updateItemRevisionAttribute(attribute.id.objectId, attribute.value).then(
                            function (data) {
                                ObjectAttributeService.uploadObjectAttributeImage(attribute.id.objectId, attribute.id.attributeDef, attribute.imageValue).then(
                                    function (data) {
                                        attribute.changeImage = false;
                                        attribute.newImageValue = null;
                                        attribute.value.revisionImagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();
                                        $rootScope.showSuccessMessage("Image saved successfully");
                                        loadAttributes();
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        )
                    }
                }
            }

            /*------------ To Save ITEM ATTACHMENT Attribute Values --------------*/

            function saveAttachments(attribute) {
                if (attribute.attachmentValues.length == 0) {
                    $rootScope.showWarningMessage("Please select file");
                }
                if (attribute.attachmentValues.length > 0) {
                    var attachmentIds = [];
                    var attachments = attribute.value.attachmentValues;
                    attribute.value.attachmentValues = [];
                    angular.forEach(attachments, function (attachment) {
                        attachmentIds.push(attachment.id);
                    });
                    angular.forEach(attribute.attachmentValues, function (attachmentValue) {
                        var itemAttachmentExists = false;
                        angular.forEach(attachments, function (itemAttachment) {
                            if ((attribute.id.objectId == itemAttachment.objectId) && (attribute.id.attributeDef == itemAttachment.attributeDef) && (attachmentValue.name == itemAttachment.name)) {
                                itemAttachmentExists = true;
                                attachmentIds.remove(itemAttachment.id);

                                var options = {
                                    title: "Add Attachment",
                                    message: attachmentValue.name + " : Attachment already exists. Do you want to override ?",
                                    okButtonClass: 'btn-danger'
                                };
                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        $rootScope.showBusyIndicator($(".view-content"));
                                        AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'ITEMMASTER', attachmentValue).then(
                                            function (data) {
                                                attachmentIds.push(data[0].id);
                                                attribute.attachmentValues.remove(attachmentValue);
                                                if (attachmentIds.length > 0) {
                                                    angular.forEach(attachmentIds, function (attachId) {
                                                        attribute.value.attachmentValues.push(attachId);
                                                    });
                                                    ItemService.updateItemAttribute(vm.item.id, attribute.value).then(
                                                        function (data) {
                                                            $rootScope.hideBusyIndicator();
                                                            if (attribute.attachmentValues.length == 0) {
                                                                loadAttributes();
                                                                attribute.showAttachment = false;
                                                                $rootScope.showSuccessMessage("Attachment(s) saved successfully");
                                                            }
                                                        }
                                                    )
                                                }
                                            }
                                        )
                                    } else {
                                        attribute.showAttachment = false;
                                        loadAttributes();
                                    }
                                });
                            }
                        });
                        if (itemAttachmentExists == false) {
                            $rootScope.showBusyIndicator($(".view-content"));
                            AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'ITEMMASTER', attachmentValue).then(
                                function (data) {
                                    attachmentIds.push(data[0].id);
                                    attribute.attachmentValues.remove(attachmentValue);
                                    if (attachmentIds.length > 0) {
                                        angular.forEach(attachmentIds, function (attachId) {
                                            attribute.value.attachmentValues.push(attachId);
                                        });
                                        ItemService.updateItemAttribute(vm.item.id, attribute.value).then(
                                            function (data) {
                                                $rootScope.hideBusyIndicator();
                                                if (attribute.attachmentValues.length == 0) {
                                                    loadAttributes();
                                                    attribute.showAttachment = false;
                                                    $rootScope.showSuccessMessage("Attachment(s) saved successfully");
                                                }

                                            }
                                        )
                                    }
                                }
                            )
                        }

                    });
                }

            }

            /*----------- To Save ITEMREVISION ATTACHMENT Attribute Values  --------------*/

            function saveRevisionAttachments(attribute) {
                if (attribute.revAttachmentValues.length == 0) {
                    $rootScope.showWarningMessage("Please select file");
                }
                if (attribute.revAttachmentValues.length > 0) {
                    var revAttachmentIds = [];
                    var revisionAttachments = attribute.value.attachmentValues;
                    attribute.value.attachmentValues = [];
                    angular.forEach(revisionAttachments, function (attachment) {
                        revAttachmentIds.push(attachment.id);
                    });
                    angular.forEach(attribute.revAttachmentValues, function (attachmentValue) {
                        var attachmentExists = false;
                        vm.flag = false;
                        angular.forEach(revisionAttachments, function (revisionAttachment) {
                            if ((attribute.id.objectId == revisionAttachment.objectId) && (attribute.id.attributeDef == revisionAttachment.attributeDef) && (attachmentValue.name == revisionAttachment.name)) {
                                attachmentExists = true;
                                revAttachmentIds.remove(revisionAttachment.id);

                                var options = {
                                    title: "Add Attachment",
                                    message: attachmentValue.name + " : Attachment already exists. Do you want to override ?",
                                    okButtonClass: 'btn-danger'
                                };
                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        $rootScope.showBusyIndicator($(".view-content"));
                                        AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'ITEMREVISION', attachmentValue).then(
                                            function (data) {
                                                revAttachmentIds.push(data[0].id);
                                                attribute.revAttachmentValues.remove(attachmentValue);
                                                if (revAttachmentIds.length > 0) {
                                                    angular.forEach(revAttachmentIds, function (revAttachId) {
                                                        attribute.value.attachmentValues.push(revAttachId);
                                                    });
                                                    ItemService.updateItemRevisionAttribute(vm.itemId, attribute.value).then(
                                                        function (data) {
                                                            $rootScope.hideBusyIndicator();
                                                            if (attribute.revAttachmentValues.length == 0) {
                                                                loadAttributes();
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
                                        loadAttributes();
                                    }
                                });
                            }
                        });
                        if (attachmentExists == false) {
                            $rootScope.showBusyIndicator($(".view-content"));
                            AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'ITEMREVISION', attachmentValue).then(
                                function (data) {
                                    revAttachmentIds.push(data[0].id);
                                    attribute.revAttachmentValues.remove(attachmentValue);
                                    if (revAttachmentIds.length > 0) {
                                        angular.forEach(revAttachmentIds, function (revAttachId) {
                                            attribute.value.attachmentValues.push(revAttachId);
                                        });
                                        ItemService.updateItemRevisionAttribute(vm.itemId, attribute.value).then(
                                            function (data) {
                                                $rootScope.hideBusyIndicator();
                                                if (attribute.revAttachmentValues.length == 0) {
                                                    loadAttributes();
                                                    attribute.showAttachment = false;
                                                    $rootScope.showSuccessMessage("Attachments saved successfully");
                                                }

                                            }
                                        )
                                    }
                                }
                            )
                        }
                    });
                }
            }

            function deleteAttachments(attribute, attachment) {
                $rootScope.showBusyIndicator($('.view-content'));
                var attachments = attribute.value.attachmentValues;
                attribute.value.attachmentValues = [];
                angular.forEach(attachments, function (attach) {
                    if (attach.id != attachment.id) {
                        attribute.value.attachmentValues.push(attach.id);
                    }
                });
                if (attribute.attributeDef.revisionSpecific == false) {
                    ItemService.updateItemAttribute(vm.item.id, attribute.value).then(
                        function (data) {
                            AttributeAttachmentService.deleteAttributeAttachment(attachment.id).then(
                                function (data) {
                                    loadAttributes();
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showSuccessMessage(attachment.name + ' : Attachment deleted successfully');
                                }
                            )

                        }
                    )
                } else if (attribute.attributeDef.revisionSpecific == true) {
                    ItemService.updateItemRevisionAttribute(vm.itemId, attribute.value).then(
                        function (data) {
                            AttributeAttachmentService.deleteAttributeAttachment(attachment.id).then(
                                function (data) {
                                    loadAttributes();
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showSuccessMessage(attachment.name + ' : Attachment deleted successfully');
                                }
                            )

                        }
                    )
                }
            }

            vm.changeAttribute = changeAttribute;
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

            /*-------------- To Display Large ITEM Attribute Image -----------------*/

            function showImage(attribute) {
                var modal = document.getElementById('myModal2');
                var modalImg = document.getElementById('img03');

                modal.style.display = "block";
                modalImg.src = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                var span = document.getElementsByClassName("closeimage1")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            /*-------------- To Display Large ITEMREVISION Attribute Image -----------------*/

            function showRevisionImage(attribute) {
                var modal = document.getElementById('myModal3');
                var modalImg = document.getElementById("img04");

                modal.style.display = "block";
                modalImg.src = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                var span = document.getElementsByClassName("closeImage1")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            /*--------- To Download ATTACHMENT Attribute File  --------------*/

            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }


            (function () {
                $scope.$on('app.item.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        ObjectAttributeService.getCurrencies().then(
                            function (data) {
                                vm.currencies = data;
                                angular.forEach(vm.currencies, function (currency) {
                                    currencyMap.put(currency.id, $sce.trustAsHtml(currency.symbol));
                                });
                            }
                        );
                        vm.attributes = [];
                        vm.revisionAttributes = [];
                        vm.itemAttributes = [];
                        vm.itemRevisionAttributes = [];
                        vm.itemAttributeAttachments = [];
                        loadItem();
                    }
                });
            })();
        }
    }
)
;