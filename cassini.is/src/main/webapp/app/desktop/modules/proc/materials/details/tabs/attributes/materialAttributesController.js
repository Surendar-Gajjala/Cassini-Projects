define(['app/desktop/modules/proc/proc.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemTypeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('MaterialAttributesController', MaterialAttributesController);

        function MaterialAttributesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $sce, DialogService, ObjectAttributeService, AttachmentService, ItemTypeService, CommonService, AttributeAttachmentService, ItemService, ItemTypeService) {
            var vm = this;

            var currencyMap = new Hashtable();
            vm.materialId = $stateParams.materialId;
            vm.material = null;
            vm.clearBrowse = true;
            vm.saveMaterialAttribute = saveMaterialAttribute;
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
            vm.addAttachment = addAttachment;
            vm.cancelAttachment = cancelAttachment;
            vm.saveAttachments = saveAttachments;
            vm.changeTimestamp = changeTimestamp;
            vm.openAttachment = openAttachment;
            vm.showRefValueDetails = showRefValueDetails;
            vm.saveObject = saveObject;
            vm.deleteAttachments = deleteAttachments;
            vm.changeCurrencyValue = changeCurrencyValue;
            vm.cancelChanges = cancelChanges;
            vm.loading = true;
            vm.changeAttribute = changeAttribute;
            vm.addToListValue = addToListValue;
            vm.cancelToListValue = cancelToListValue;

            vm.flags = [{
                name: "True",
                value: true
            },
                {
                    name: "False",
                    value: false
                }
            ];

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

            function changeAttribute(attribute) {
                attribute.editMode = true;
            }

            function cancelToListValue(attribute) {
                attribute.listValueEditMode = false;
            }

            function change(attribute) {
                attribute.changeImage = true;
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
            }

            function cancelAttachment(attribute) {
                document.getElementById("file").value = null;
                attribute.showAttachment = false;
                attribute.attachmentValues = [];
                attribute.revAttachmentValues = [];
            }

            function cancelChanges(attribute) {
                attribute.editMode = false;
                attribute.value.refValue = vm.previousValue;
                attribute.changeCurrency = false;
            }

            function changeCurrencyValue(attribute) {
                attribute.changeCurrency = true;
            }

            function addToListValue(attribute) {
                attribute.value.listValue = attribute.newListValue;
            }

            function cancelToListValue(attribute) {
                attribute.listValueEditMode = false;
            }

            function loadMaterial() {
                vm.loading = true;
                $rootScope.showBusyIndicator($('.view-content'));
                ItemService.getMaterialItem(vm.materialId).then(
                    function (data) {
                        vm.material = data;
                        vm.loading = false;
                        CommonService.getPersonReferences([vm.material], 'createdBy');
                        CommonService.getPersonReferences([vm.material], 'modifiedBy');
                        loadAttributeDefs();
                    }
                )
            }

            /*------------ To get Attribute Names By Type ID --------------*/

            function loadAttributeDefs() {
                vm.attributes = [];
                ItemTypeService.getMaterialAttributesWithHierarchy(vm.material.itemType.id).then(
                    function (data) {
                        vm.loading = false;
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.material.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                value: {
                                    id: {
                                        objectId: vm.material.id,
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
                                changeCurrency: false,
                                editTimeValue: null,
                                editTimestampValue: null
                            };

                            vm.attributes.push(att);
                        });
                        loadAttributes();
                    });
            }

            /*----- To get Attribute Values with materialId -------*/

            function loadAttributes() {
                ItemService.getMaterialItemAttributes(vm.materialId).then(
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
                                        });
                                        AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                            function (data) {
                                                vm.attachments = data;
                                                attribute.value.attachmentValues = vm.attachments;
                                            }
                                        )
                                    }
                                    if (value.refValue != null) {
                                        addRefValue(attribute, value);
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
                                    attribute.value.currencyType = value.currencyType;
                                    attribute.editTimeValue = moment(value.timeValue, "hh:mm:ss A");
                                    attribute.editTimestampValue = moment(value.timestampValue, "hh:mm:ss A");
                                    if (value.currencyType != null) {
                                        attribute.value.encodedCurrencyType = currencyMap.get(value.currencyType);
                                    }
                                    attribute.value.imagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                }
                            }
                        );
                        $rootScope.hideBusyIndicator();
                    })
            }

            function addRefValue(attribute, value) {
                if (attribute.attributeDef.refType == "MATERIAL") {
                    ItemService.getMaterialItem(value.refValue).then(
                        function (materialValue) {
                            attribute.value.refValue = materialValue;
                        }
                    )
                }
                if (attribute.attributeDef.refType == "MACHINE") {
                    ItemService.getMachineItem(value.refValue).then(
                        function (machineValue) {
                            attribute.value.refValue = machineValue;
                        }
                    )
                }
                if (attribute.attributeDef.refType == "MANPOWER") {
                    ItemService.getManpowerItem(value.refValue).then(
                        function (manpowerValue) {
                            attribute.value.refValue = manpowerValue;
                        }
                    )
                }
            }

            function isInteger(x) {
                return x % 1 === 0;
            }

            /*---- To check given value is Integer or not -----*/

            function validateAttribute(value) {
                if (isInteger(value)) {
                    return true;
                } else {
                    return "This Attribute accept only integer value";
                }
            }

            /*------ To update String, Integer, Boolean, Date, Double Attributes ------*/

            function saveMaterialAttribute(attribute) {
                if (attribute.value.id == undefined || attribute.value.id == null) {
                    ItemService.saveMaterialItemAttributes(vm.material.id, attribute.value).then(
                        function (data) {
                            attribute.value.id = data.id;
                            attribute.listValueEditMode = false;
                            attribute.editMode = false;
                            $rootScope.showSuccessMessage(attribute.attributeDef.name + " : Attribute created successfully");
                            loadAttributes();
                        }
                    )
                }
                else {
                    ItemService.updateMaterialItemAttributes(vm.material.id, attribute.value).then(
                        function (data) {
                            attribute.listValueEditMode = false;
                            attribute.changeCurrency = false;
                            attribute.editMode = false;
                            loadAttributes();
                            $rootScope.showSuccessMessage(attribute.attributeDef.name + " : Attribute updated successfully");

                        }
                    )
                }

            }

            function saveTime(attribute) {
                if (attribute.editTimeValue != null && attribute.attributeDef.dataType == 'TIME') {
                    attribute.value.timeValue = moment(attribute.editTimeValue).format("HH:mm:ss");
                    if (attribute.attributeDef.objectType == "MATERIALTYPE") {
                        ItemService.updateMaterialItemAttributes(vm.material.id, attribute.value).then(
                            function (data) {
                                attribute.showTimeAttribute = false;
                                $rootScope.showSuccessMessage(attribute.attributeDef.name + " : Time Attribute updated successfully");
                                loadAttributes();
                            }
                        )
                    }
                }
                else if (attribute.editTimestampValue != null) {
                    attribute.value.timestampValue = moment(attribute.editTimestampValue).format('DD/MM/YYYY, HH:mm:ss');
                    if (attribute.attributeDef.objectType == 'MATERIALTYPE') {
                        ItemService.updateMaterialItemAttributes(vm.material.id, attribute.value).then(
                            function (data) {
                                attribute.showTimestamp = false;
                                $rootScope.showSuccessMessage(attribute.attributeDef.name + " : TimeStamp Attribute updated successfully");
                                loadAttributes();
                            }
                        )
                    }
                }
            }

            /*-----------------  To Save IMAGE Attribute Values  ----------------*/

            function saveImage(attribute) {
                if (attribute.newImageValue == null) {
                    $rootScope.showWarningMessage('No Image file selected');
                }
                if (attribute.newImageValue != null) {
                    vm.clearBrowse = false;
                    $rootScope.showBusyIndicator($('.view-content'));
                    attribute.imageValue = attribute.newImageValue;
                    ItemService.updateMaterialItemAttributes(vm.materialId, attribute.value).then(
                        function (data) {
                            ObjectAttributeService.uploadObjectAttributeImage(attribute.id.objectId, attribute.id.attributeDef, attribute.imageValue).then(
                                function (data) {
                                    loadAttributes();
                                    vm.clearBrowse = true;
                                    attribute.value.imagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();
                                    $rootScope.showSuccessMessage('Image uploaded successfully');
                                    attribute.changeImage = false;
                                    attribute.newImageValue = null;
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    )
                }

            }

            /*------------ To Save ITEM ATTACHMENT Attribute Values --------------*/

            function saveAttachments(attribute) {
                if (attribute.attachmentValues.length == 0) {
                    $rootScope.showWarningMessage('No file selected');
                }
                if (attribute.attachmentValues.length > 0) {
                    vm.clearBrowse = false;
                    var attachmentIds = [];
                    var attachments = attribute.value.attachmentValues;
                    attribute.value.attachmentValues = [];
                    angular.forEach(attachments, function (attachment) {
                        attachmentIds.push(attachment.id);
                    });
                    angular.forEach(attribute.attachmentValues, function (attachmentValue) {
                        var materialAttachmentExists = false;
                        angular.forEach(attachments, function (materialAttachment) {
                            if ((attribute.id.objectId == materialAttachment.objectId) && (attribute.id.attributeDef == materialAttachment.attributeDef) && (attachmentValue.name == materialAttachment.name)) {
                                materialAttachmentExists = true;
                                attachmentIds.remove(materialAttachment.id);

                                var options = {
                                    title: 'Adding Attachment Attribute',
                                    message: attachmentValue.name + " : Attachment already exists! Do you want to override?",
                                    okButtonClass: 'btn-danger'
                                };

                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        $rootScope.showBusyIndicator($(".view-content"));
                                        AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'MATERIAL', attachmentValue).then(
                                            function (data) {
                                                attachmentIds.push(data[0].id);
                                                attribute.attachmentValues.remove(attachmentValue);
                                                vm.clearBrowse = true;
                                                if (attachmentIds.length > 0) {
                                                    angular.forEach(attachmentIds, function (attachId) {
                                                        attribute.value.attachmentValues.push(attachId);
                                                    });
                                                    ItemService.updateMaterialItemAttributes(vm.material.id, attribute.value).then(
                                                        function (data) {
                                                            $rootScope.hideBusyIndicator();
                                                            if (attribute.attachmentValues.length == 0) {
                                                                loadAttributes();
                                                                attribute.showAttachment = false;
                                                                $rootScope.showSuccessMessage(' Attachment(s) uploaded successfully');
                                                            }
                                                        }
                                                    )
                                                }
                                            }
                                        )
                                    } else {
                                        attribute.showAttachment = false;
                                        loadAttributes();
                                        vm.clearBrowse = true;
                                    }
                                });
                            }
                        });
                        if (materialAttachmentExists == false) {
                            $rootScope.showBusyIndicator($(".view-content"));
                            AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'MATERIAL', attachmentValue).then(
                                function (data) {
                                    attachmentIds.push(data[0].id);
                                    attribute.attachmentValues.remove(attachmentValue);
                                    vm.clearBrowse = true;
                                    if (attachmentIds.length > 0) {
                                        angular.forEach(attachmentIds, function (attachId) {
                                            attribute.value.attachmentValues.push(attachId);
                                        });
                                        ItemService.updateMaterialItemAttributes(vm.material.id, attribute.value).then(
                                            function (data) {
                                                $rootScope.hideBusyIndicator();
                                                if (attribute.attachmentValues.length == 0) {
                                                    loadAttributes();
                                                    attribute.showAttachment = false;
                                                    $rootScope.showSuccessMessage('Attachment(s) uploaded successfully');
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
                ItemService.updateMaterialItemAttributes(vm.material.id, attribute.value).then(
                    function (data) {
                        AttributeAttachmentService.deleteAttributeAttachment(attachment.id).then(
                            function (data) {
                                loadAttributes();
                                $rootScope.hideBusyIndicator();
                                $rootScope.showSuccessMessage(attachment.name + ' : deleted successfully ');
                            }
                        )

                    }
                )

            }

            /*-------------- To Display Large Material Attribute Image -----------------*/

            function showImage(attribute) {
                var modal = document.getElementById('myModal4');
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

            /*--------- It Goes to the Specified Details Page When Click on Ref Value  ----------*/

            function showRefValueDetails(attribute) {
                if (attribute.value.refValue.objectType == 'MATERIALTYPE') {
                    $state.go('app.proc.materials.details', {materialId: attribute.value.refValue.id});
                } else if (attribute.value.refValue.objectType == 'MACHINETYPE') {
                    $state.go('app.proc.machines.details', {machineId: attribute.value.refValue.id});
                } else if (attribute.value.refValue.objectType == 'MANPOWERTYPE') {
                    $state.go('app.proc.manpower.details', {manpowerId: attribute.value.refValue.id});
                }
            }

            /*--------------------  To save selected OBJECT Attribute values  ---------------------*/

            function saveObject(attribute) {
                $rootScope.showBusyIndicator($('.view-content'));
                attribute.value.refValue = attribute.value.refValue.id;
                if (attribute.value.id == undefined || attribute.value.id == null) {
                    ItemService.saveMaterialItemAttributes(vm.material.id, attribute.value).then(
                        function (data) {
                            $rootScope.showSuccessMessage("Attribute created successfully");
                            attribute.editMode = false;
                            loadAttributes();
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    ItemService.updateMaterialItemAttributes(vm.material.id, attribute.value).then(
                        function (data) {
                            $rootScope.showSuccessMessage(attribute.attributeDef.name + " : Attribute updated successfully");
                            attribute.editMode = false;
                            loadAttributes();
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            (function () {
                $scope.$on('app.material.tabactivated', function (event, data) {
                    ObjectAttributeService.getCurrencies().then(
                        function (data) {
                            vm.currencies = data;
                            angular.forEach(vm.currencies, function (currency) {
                                currencyMap.put(currency.id, $sce.trustAsHtml(currency.symbol));
                            });
                        }
                    );
                    loadMaterial();

                });
            })();
        }
    }
);