define(['app/desktop/modules/stores/store.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemService',
        'app/shared/services/store/topStockReceivedService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('ItemBasicInfoController', ItemBasicInfoController);

        function ItemBasicInfoController($scope, $rootScope, $stateParams, $timeout, $state, $sce, $cookies, DialogService,
                                         AttributeAttachmentService, ObjectAttributeService, ItemService,
                                         CommonService, TopStockReceivedService) {
            var vm = this;

            vm.receivedItem = $scope.data.item;
            vm.receivedItemProperties = [];

            vm.materialProperties = [];
            vm.materialPropertyAttachments = [];
            vm.clearBrowse = true;

            var currencyMap = new Hashtable();

            vm.flags = [{
                name: "True",
                value: true
            },
                {
                    name: "False",
                    value: false
                }
            ];

            vm.change = change;
            vm.cancel = cancel;
            vm.showImage = showImage;
            vm.saveImage = saveImage;
            vm.changeTime = changeTime;
            vm.cancelTime = cancelTime;
            vm.changeTimestamp = changeTimestamp;
            vm.validateAttribute = validateAttribute;
            vm.saveMaterialProperties = saveMaterialProperties;
            vm.saveTimeProperty = saveTimeProperty;
            vm.saveObject = saveObject;
            vm.showRefValueDetails = showRefValueDetails;
            vm.changeCurrencyValue = changeCurrencyValue;
            vm.saveAttachments = saveAttachments;
            vm.openAttachment = openAttachment;
            vm.addAttachment = addAttachment;
            vm.cancelAttachment = cancelAttachment;
            vm.deleteAttachments = deleteAttachments;
            vm.cancelChanges = cancelChanges;
            vm.changeAttribute = changeAttribute;
            vm.addToListValue = addToListValue;
            vm.cancelToListValue = cancelToListValue;

            function changeAttribute(attribute) {
                attribute.editMode = true;
            }

            function change(attribute) {
                attribute.changeImage = true;
            }

            function changeCurrencyValue(attribute) {
                attribute.changeCurrency = true;
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

            function cancelChanges(attribute) {
                attribute.editMode = false;
                attribute.value.refValue = attribute.refValue;
                attribute.changeCurrency = false;
            }

            function addAttachment(attribute) {
                attribute.showAttachment = true;
            }

            function cancelAttachment(attribute) {
                attribute.showAttachment = false;
                attribute.attachmentValues = [];
            }

            function addToListValue(attribute) {
                attribute.value.listValue = attribute.newListValue;
            }

            function cancelToListValue(attribute) {
                attribute.listValueEditMode = false;
            }

            function loadObjectAttributeDefs() {
                ItemService.getAllTypeAttributes("MATERIAL").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.receivedItem.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                value: {
                                    id: {
                                        objectId: vm.receivedItem.id,
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
                                changeCurrency: false,
                                editTimeValue: null
                            };
                            vm.materialProperties.push(att);
                        });

                        loadObjectAttributes();
                    });
            }

            /*------- To get Property values by materialId ----------*/

            function loadObjectAttributes() {
                ObjectAttributeService.getAllObjectAttributes(vm.receivedItem.id).then(
                    function (data) {
                        var map = new Hashtable();

                        angular.forEach(data, function (attribute) {
                            map.put(attribute.id.attributeDef, attribute);
                        });

                        angular.forEach(vm.materialProperties, function (attribute) {
                            var attachmentIds = [];
                            var value = map.get(attribute.attributeDef.id);
                            if (value != null) {
                                if (value.attachmentValues.length > 0) {
                                    angular.forEach(value.attachmentValues, function (attachment) {
                                        attachmentIds.push(attachment);
                                    });
                                    AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                        function (data) {
                                            vm.materialPropertyAttachments = data;
                                            attribute.value.attachmentValues = vm.materialPropertyAttachments;
                                        }
                                    )
                                }
                                if (value.refValue != null) {
                                    if (attribute.attributeDef.refType == 'MATERIALTYPE') {
                                        ItemService.getMaterialItem(value.refValue).then(
                                            function (materialValue) {
                                                attribute.value.refValue = materialValue;
                                            }
                                        )
                                    } else if (attribute.attributeDef.refType == 'MACHINETYPE') {
                                        ItemService.getMachineItem(value.refValue).then(
                                            function (machineValue) {
                                                attribute.value.refValue = machineValue;
                                            }
                                        )
                                    } else if (attribute.attributeDef.refType == 'MANPOWERTYPE') {
                                        ItemService.getManpowerItem(value.refValue).then(
                                            function (manpowerValue) {
                                                attribute.value.refValue = manpowerValue;
                                            }
                                        )
                                    }
                                }
                                attribute.value.stringValue = value.stringValue;
                                attribute.value.integerValue = value.integerValue;
                                attribute.value.doubleValue = value.doubleValue;
                                attribute.value.booleanValue = value.booleanValue;
                                attribute.value.dateValue = value.dateValue;
                                attribute.value.listValue = value.listValue;
                                attribute.value.timeValue = value.timeValue;
                                attribute.value.timestampValue = value.timestampValue;
                                attribute.value.imageValue = value.imageValue;
                                attribute.value.currencyValue = value.currencyValue;
                                attribute.editTimeValue = moment(value.timeValue, "hh:mm:ss A");
                                if (value.currencyType != null) {
                                    attribute.value.currencyType = value.currencyType;
                                    attribute.value.encodedCurrencyType = currencyMap.get(value.currencyType);
                                }
                                attribute.value.imagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                            }
                        });
                        $rootScope.hideBusyIndicator();
                    });
            }

            function isInteger(x) {
                return x % 1 === 0;
            }

            function validateAttribute(value) {
                if (isInteger(value)) {
                    return true;
                } else {
                    return "This attribute accepts only integer value";
                }
            }

            function saveMaterialProperties(attribute) {
                if (attribute.attributeDef.objectType == 'MATERIAL') {
                    ObjectAttributeService.updateObjectAttribute(vm.receivedItem.id, attribute.value).then(
                        function (data) {
                            attribute.changeCurrency = false;
                            attribute.editMode = false;
                            loadObjectAttributes();
                            $rootScope.showSuccessMessage(attribute.attributeDef.name + " : Attribute updated successfully");
                            attribute.listValueEditMode = false;
                        }
                    )
                }
            }

            /*-----------------  To save TIME & TIMESTAMP Attribute values  --------------*/

            function saveTimeProperty(attribute) {
                if (attribute.editTimeValue != null && attribute.attributeDef.dataType == 'TIME') {
                    attribute.value.timeValue = moment(attribute.editTimeValue).format("HH:mm:ss");
                    if (attribute.attributeDef.objectType == "MATERIAL") {
                        ObjectAttributeService.updateObjectAttribute(vm.receivedItem.id, attribute.value).then(
                            function (data) {
                                attribute.showTimeAttribute = false;
                                $rootScope.showSuccessMessage(attribute.attributeDef.name + " : Time property updated successfully");
                                loadObjectAttributes();
                            }
                        )
                    }
                }
                else if (attribute.timestampValue != null) {
                    attribute.timestampValue = moment(attribute.timestampValue).format('DD/MM/YYYY, HH:mm:ss');
                    attribute.value.timestampValue = attribute.timestampValue;
                    if (attribute.attributeDef.objectType == 'MATERIAL') {
                        ObjectAttributeService.updateObjectAttribute(vm.receivedItem.id, attribute.value).then(
                            function (data) {
                                attribute.showTimestamp = false;
                                $rootScope.showSuccessMessage(attribute.attributeDef.name + " : Timestamp attribute updated successfully");
                                loadObjectAttributes();
                            }
                        )
                    }
                }
            }

            /*- It goes to specified MATERIALTYPE, MACHINETYPE and MANPOWERTYPE details page ------*/

            function showRefValueDetails(attribute) {
                if (attribute.value.refValue.objectType == 'MATERIALTYPE') {
                    $state.go('app.proc.materials.details', {materialId: attribute.value.refValue.id});
                } else if (attribute.value.refValue.objectType == 'MACHINETYPE') {
                    $state.go('app.proc.machines.details', {machineId: attribute.value.refValue.id});
                } else if (attribute.value.refValue.objectType == 'MANPOWERTYPE') {
                    $state.go('app.proc.manpower.details', {manpowerId: attribute.value.refValue.id});
                }
            }

            /*-------------  To save selected OBJECT attribute value  -------------*/

            function saveObject(attribute) {
                $rootScope.showBusyIndicator($('.view-content'));
                attribute.value.refValue = attribute.value.refValue.id;
                if (attribute.value.id == undefined || attribute.value.id == null) {
                    ObjectAttributeService.updateObjectAttribute(vm.receivedItem.id, attribute.value).then(
                        function (data) {
                            $rootScope.showSuccessMessage("Attribute updated successfully");
                            attribute.editMode = false;
                            loadObjectAttributes();
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            /*----------------  To update Image Attribute value  -------------------------*/

            function saveImage(attribute) {
                if (attribute.newImageValue == null) {
                    $rootScope.showWarningMessage('No Image file selected');
                }
                if (attribute.newImageValue != null) {
                    vm.clearBrowse = false;
                    $rootScope.showBusyIndicator($('.view-content'));
                    attribute.imageValue = attribute.newImageValue;
                    ObjectAttributeService.updateObjectAttribute(vm.materialId, attribute.value).then(
                        function (data) {
                            ObjectAttributeService.uploadObjectAttributeImage(attribute.id.objectId, attribute.id.attributeDef, attribute.imageValue).then(
                                function (data) {
                                    loadObjectAttributes();
                                    vm.clearBrowse = true;
                                    attribute.value.imagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();
                                    $rootScope.showSuccessMessage('Image uploaded successfully.');
                                    attribute.changeImage = false;
                                    attribute.newImageValue = null;
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    )
                }
            }

            /*----------  To show large image when click on Image  ------------------*/

            function showImage(attribute) {
                var modal = document.getElementById('myModal2');
                var modalImg = document.getElementById("img03");

                modal.style.display = "block";
                modalImg.src = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                var span = document.getElementsByClassName("closeImage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function saveAttachments(attribute) {
                if (attribute.attachmentValues.length == 0) {
                    $rootScope.showWarningMessage("No file selected");
                }
                if (attribute.attachmentValues.length > 0) {
                    vm.clearBrowse = false;
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
                                    title: 'Adding attribute attachment',
                                    message: attachmentValue.name + " : Attachment already exist! Do you want to override?",
                                    okButtonClass: 'btn-danger'
                                };
                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        $rootScope.showBusyIndicator($(".view-content"));
                                        AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'MATERIALTYPE', attachmentValue).then(
                                            function (data) {
                                                itemAttachPropertyIds.push(data[0].id);
                                                attribute.attachmentValues.remove(attachmentValue);
                                                vm.clearBrowse = true;
                                                if (itemAttachPropertyIds.length > 0) {
                                                    angular.forEach(itemAttachPropertyIds, function (revAttachId) {
                                                        attribute.value.attachmentValues.push(revAttachId);
                                                    });
                                                    ObjectAttributeService.updateObjectAttribute(vm.receivedItem.id, attribute.value).then(
                                                        function (data) {
                                                            $rootScope.hideBusyIndicator();
                                                            if (attribute.attachmentValues.length == 0) {
                                                                loadObjectAttributes();
                                                                attribute.showAttachment = false;
                                                                $rootScope.showSuccessMessage('Attachment(s) uploaded successfully');
                                                            }
                                                        }
                                                    )
                                                }
                                            }
                                        )
                                    } else {
                                        attribute.showAttachment = false;
                                        loadObjectAttributes();
                                        vm.clearBrowse = true;
                                    }
                                });
                            }
                        });
                        if (itemAttachmentExists == false) {
                            $rootScope.showBusyIndicator($(".view-content"));
                            AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'MATERIALTYPE', attachmentValue).then(
                                function (data) {
                                    itemAttachPropertyIds.push(data[0].id);
                                    attribute.attachmentValues.remove(attachmentValue);
                                    vm.clearBrowse = true;
                                    if (itemAttachPropertyIds.length > 0) {
                                        angular.forEach(itemAttachPropertyIds, function (itemAttachId) {
                                            attribute.value.attachmentValues.push(itemAttachId);
                                        });
                                        ObjectAttributeService.updateObjectAttribute(vm.receivedItem.id, attribute.value).then(
                                            function (data) {
                                                $rootScope.hideBusyIndicator();
                                                if (attribute.attachmentValues.length == 0) {
                                                    loadObjectAttributes();
                                                    attribute.showAttachment = false;
                                                    $rootScope.showSuccessMessage('Attachment(s) uploaded successfully');
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
                    title: 'Delete Attachment',
                    message: 'Are you sure you want to delete this attachment?',
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
                        if (attribute.attributeDef.objectType == 'MATERIAL') {
                            ObjectAttributeService.updateObjectAttribute(vm.materialId, attribute.value).then(
                                function (data) {
                                    AttributeAttachmentService.deleteAttributeAttachment(attachment.id).then(
                                        function (data) {
                                            loadObjectAttributes();
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.showSuccessMessage(attachment.name + ' : deleted successfully');
                                        }
                                    )

                                }
                            )
                        }
                    }
                });
            }

            /*----------  To download and open attachment  ---------------*/

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
                if ($application.homeLoaded == true) {
                    loadObjectAttributeDefs();
                }
            })();
        }
    });