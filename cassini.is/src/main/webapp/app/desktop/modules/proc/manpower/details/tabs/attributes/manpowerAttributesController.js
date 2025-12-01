define(
    [
        'app/desktop/modules/proc/proc.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
    ],
    function (module) {
        module.controller('ManpowerAttributesController', ManpowerAttributesController);

        function ManpowerAttributesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $sce,
                                              CommonService, ItemTypeService,
                                              ItemService, DialogService, AttributeAttachmentService, ObjectAttributeService) {

            var vm = this;

            vm.manpowerId = $stateParams.manpowerId;
            vm.prjId = $stateParams.projectId;
            vm.manpower = null;
            vm.attributes = [];
            vm.clearBrowse = true;
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
            vm.changeCurrencyValue = changeCurrencyValue;
            vm.cancel = cancel;
            vm.changeTime = changeTime;
            vm.cancelTime = cancelTime;
            vm.changeTimestamp = changeTimestamp;
            vm.cancelChanges = cancelChanges;
            vm.addAttachment = addAttachment;
            vm.cancelAttachment = cancelAttachment;
            vm.saveManpowerAttribute = saveManpowerAttribute;
            vm.validateAttribute = validateAttribute;
            vm.saveTimeProperty = saveTimeProperty;
            vm.saveObject = saveObject;
            vm.showRefValueDetails = showRefValueDetails;
            vm.saveImage = saveImage;
            vm.showImage = showImage;
            vm.saveAttachments = saveAttachments;
            vm.deleteAttachments = deleteAttachments;
            vm.openAttachment = openAttachment;
            vm.changeAttribute = changeAttribute;
            vm.addToListValue = addToListValue;
            vm.cancelToListValue = cancelToListValue;

            var currencyMap = new Hashtable();
            vm.loading = true;

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
                loadAttributes();
            }

            function addAttachment(attribute) {
                attribute.showAttachment = true;
            }

            function cancelAttachment(attribute) {
                document.getElementById("file").value = null;
                attribute.showAttachment = false;
                attribute.attachmentValues = [];
            }

            function addToListValue(attribute) {
                attribute.value.listValue = attribute.newListValue;
            }

            function cancelToListValue(attribute) {
                attribute.listValueEditMode = false;
            }

            function loadManpower() {
                vm.loading = true;
                $rootScope.showBusyIndicator($('.view-content'));
                ItemService.getManpowerItem(vm.manpowerId).then(
                    function (data) {
                        vm.manpower = data;
                        vm.loading = false;
                        CommonService.getPersonReferences([vm.manpower], 'createdBy');
                        CommonService.getPersonReferences([vm.manpower], 'modifiedBy');
                        loadAttributeDefs();

                    }
                );
            }

            /*------------ To get Attribute Names By Type ID --------------*/

            function loadAttributeDefs() {
                vm.attributes = [];
                ItemTypeService.getManpowerAttributesWithHierarchy(vm.manpower.itemType.id).then(
                    function (data) {
                        vm.loading = false;
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.manpower.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                value: {
                                    id: {
                                        objectId: vm.manpower.id,
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

            /*----- To get Attribute Values with machineId -------*/

            function loadAttributes() {
                ItemService.getManpowerItemAttributes(vm.manpowerId).then(
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

            function validateAttribute(value) {
                if (isInteger(value)) {
                    return true;
                } else {
                    return "This Attribute accepts only integer value";
                }
            }

            /*-----------------  To save INTEGER,STRING,DOUBLE,BOOLEAN & LIST Attribute values  --------------*/

            function saveManpowerAttribute(attribute) {
                if (attribute.value.id == undefined || attribute.value.id == null) {
                    ItemService.saveManpowerItemAttributes(vm.manpower.id, attribute.value).then(
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
                    ItemService.updateManpowerItemAttributes(vm.manpower.id, attribute.value).then(
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

            /*-----------------  To save TIME & TIMESTAMP Attribute values  --------------*/

            function saveTimeProperty(attribute) {
                if (attribute.editTimeValue != null && attribute.attributeDef.dataType == 'TIME') {
                    attribute.value.timeValue = moment(attribute.editTimeValue).format("HH:mm:ss");
                    if (attribute.attributeDef.objectType == "MANPOWERTYPE") {
                        ItemService.updateManpowerItemAttributes(vm.manpowerId, attribute.value).then(
                            function (data) {
                                attribute.showTimeAttribute = false;
                                $rootScope.showSuccessMessage(attribute.attributeDef.name + " : Attribute updated successfully");
                                loadAttributes();
                            }
                        )
                    }
                }
                else if (attribute.editTimestampValue != null) {
                    attribute.value.timestampValue = moment(attribute.editTimestampValue).format('DD/MM/YYYY, HH:mm:ss');
                    if (attribute.attributeDef.objectType == 'MANPOWERTYPE') {
                        ItemService.updateManpowerItemAttributes(vm.manpower.id, attribute.value).then(
                            function (data) {
                                attribute.showTimestamp = false;
                                $rootScope.showSuccessMessage(attribute.attributeDef.name + " : TimeStamp Attribute updated successfully");
                                loadAttributes();
                            }
                        )
                    }
                }
            }

            /*- It goes to specified MATERIALTYPE, materialTYPE and MANPOWERTYPE details page ------*/

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
                    ItemService.saveManpowerItemAttributes(vm.manpower.id, attribute.value).then(
                        function (data) {
                            $rootScope.showSuccessMessage("Attribute created successfully");
                            attribute.editMode = false;
                            loadAttributes();
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    ItemService.updateManpowerItemAttributes(vm.manpower.id, attribute.value).then(
                        function (data) {
                            $rootScope.showSuccessMessage(attribute.attributeDef.name + " : Attribute updated successfully");
                            attribute.editMode = false;
                            loadAttributes();
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
                    ItemService.updateManpowerItemAttributes(vm.manpowerId, attribute.value).then(
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
                                },
                                function (err) {
                                    vm.clearBrowse = true;
                                }
                            )
                        }
                    )
                }
            }

            /*----------  To show large image when click on Image  ------------------*/

            function showImage(attribute) {
                var modal = document.getElementById('myModal1');
                var modalImg = document.getElementById("img01");

                modal.style.display = "block";
                modalImg.src = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                var span = document.getElementsByClassName("closeImage1")[0];

                span.onclick = function () {
                    modal.style.display = "none";
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
                        var machineAttachmentExists = false;
                        angular.forEach(attachments, function (manpowerAttachment) {
                            if ((attribute.id.objectId == manpowerAttachment.objectId) && (attribute.id.attributeDef == manpowerAttachment.attributeDef) && (attachmentValue.name == manpowerAttachment.name)) {
                                machineAttachmentExists = true;
                                attachmentIds.remove(manpowerAttachment.id);

                                var options = {
                                    title: 'Adding Attachment Attribute',
                                    message: attachmentValue.name + " : Attachment already exists! Do you want to override?",
                                    okButtonClass: 'btn-danger'
                                };

                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        $rootScope.showBusyIndicator($(".view-content"));
                                        AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'MANPOWER', attachmentValue).then(
                                            function (data) {
                                                attachmentIds.push(data[0].id);
                                                attribute.attachmentValues.remove(attachmentValue);
                                                if (attachmentIds.length > 0) {
                                                    angular.forEach(attachmentIds, function (attachId) {
                                                        attribute.value.attachmentValues.push(attachId);
                                                    });
                                                    ItemService.updateManpowerItemAttributes(vm.manpower.id, attribute.value).then(
                                                        function (data) {
                                                            $rootScope.hideBusyIndicator();
                                                            if (attribute.attachmentValues.length == 0) {
                                                                vm.clearBrowse = true;
                                                                loadAttributes();
                                                                attribute.showAttachment = false;
                                                                $rootScope.showSuccessMessage(' Attachment(s) uploaded successfully');
                                                            }
                                                        }
                                                    )
                                                }
                                            }, function (err) {
                                                vm.clearBrowse = true;
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
                        if (machineAttachmentExists == false) {
                            $rootScope.showBusyIndicator($(".view-content"));
                            AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'MANPOWER', attachmentValue).then(
                                function (data) {
                                    attachmentIds.push(data[0].id);
                                    attribute.attachmentValues.remove(attachmentValue);
                                    vm.clearBrowse = true;
                                    if (attachmentIds.length > 0) {
                                        angular.forEach(attachmentIds, function (attachId) {
                                            attribute.value.attachmentValues.push(attachId);
                                        });
                                        ItemService.updateManpowerItemAttributes(vm.manpower.id, attribute.value).then(
                                            function (data) {
                                                $rootScope.hideBusyIndicator();
                                                if (attribute.attachmentValues.length == 0) {
                                                    loadAttributes();
                                                    attribute.showAttachment = false;
                                                    $rootScope.showSuccessMessage('Attachment (s) uploaded successfully');
                                                }

                                            }
                                        )
                                    }
                                }, function (err) {
                                    vm.clearBrowse = true;
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
                ItemService.updateManpowerItemAttributes(vm.manpower.id, attribute.value).then(
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

            /*----------  To download and open attachment  ---------------*/

            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }

            (function () {
                $scope.$on('app.manpower.tabactivated', function (event, data) {
                    ObjectAttributeService.getCurrencies().then(
                        function (data) {
                            vm.currencies = data;
                            angular.forEach(vm.currencies, function (currency) {
                                currencyMap.put(currency.id, $sce.trustAsHtml(currency.symbol));
                            });
                        }
                    );
                    loadManpower();
                });
            })();
        }
    }
);