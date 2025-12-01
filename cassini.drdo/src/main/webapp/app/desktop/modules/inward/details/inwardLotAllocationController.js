define(
    [
        'app/desktop/modules/inward/inward.module',
        'moment',
        'moment-timezone-with-data',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/inwardService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('InwardLotAllocationController', InwardLotAllocationController);

        function InwardLotAllocationController($scope, $q, $sce, $stateParams, $rootScope, $timeout, $interval, $state, $cookies, ItemTypeService, InwardService,
                                               ObjectAttributeService, AttachmentService, AttributeAttachmentService, DialogService) {

            var vm = this;

            vm.inwardItem = $scope.data.inwardItem;
            vm.lotMode = $scope.data.inwardLotMode;

            vm.mode = 'single';
            vm.numberOfLots = 0;
            vm.lots = [];
            vm.multipleLotDto = {
                lots: []
            };

            vm.emptySingleLot = {
                id: null,
                item: {
                    id: null,
                    lotSize: null
                }
            };

            var currencyMap = new Hashtable();
            vm.setMode = setMode;
            vm.createLots = createLots;

            function createLots() {
                vm.lots = [];
                if (vm.numberOfLots > 1) {
                    for (var i = 0; i < vm.numberOfLots; i++) {
                        vm.lots.push(
                            {
                                name: 'LOT' + (i + 1),
                                qty: 0
                            }
                        )
                    }
                }
            }

            function setMode(mode) {
                vm.mode = mode;
            }

            function createLot() {
                if (vm.mode === "single" && vm.lotMode === "NEW") {
                    if (validateAttributes()) {
                        var singleLot = angular.copy(vm.emptySingleLot);
                        singleLot.item.lotSize = vm.inwardItem.fractionalQuantity;
                        vm.inwardItem.instances.push(singleLot);

                        InwardService.createItemLot($stateParams.inwardId, vm.inwardItem).then(
                            function (data) {
                                saveObjectAttributes(data);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }

                } else if (vm.mode === "multiple" && vm.lotMode === "NEW") {
                    if (validateLots() && validateQuantities() && validateAttributes()) {
                        vm.inwardItem.instances = [];
                        angular.forEach(vm.lots, function (lot) {
                            var singleLot = angular.copy(vm.emptySingleLot);
                            singleLot.item.lotSize = lot.qty;
                            vm.inwardItem.instances.push(singleLot);
                        })
                        InwardService.createItemLot($stateParams.inwardId, vm.inwardItem).then(
                            function (data) {
                                var instanceIds = [];
                                angular.forEach(data.instances, function (instance) {
                                    instanceIds.push(instance.item.id);
                                });
                                if (data.instances.length == instanceIds.length) {
                                    saveMultipleObjectAttributes(instanceIds, data);
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                } else {
                    $rootScope.hideBusyIndicator();
                    $rootScope.hideSidePanel();
                }
            }

            function validateLots() {
                var valid = true;
                if (vm.lots.length == 0) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Number of Lots");
                } else {
                    angular.forEach(vm.lots, function (lot) {
                        if (valid) {
                            if (lot.qty == 0 || lot.qty == "" || lot.qty == null || lot.qty == undefined) {
                                valid = false;
                                $rootScope.showWarningMessage("Please enter quantity for : " + lot.name);
                            }
                        }
                    });
                }


                return valid;
            }

            function validateQuantities() {
                var valid = true;
                var totalQuantity = 0;
                angular.forEach(vm.lots, function (lot) {
                    totalQuantity = totalQuantity + lot.qty;
                });

                if (totalQuantity > vm.inwardItem.fractionalQuantity || totalQuantity < vm.inwardItem.fractionalQuantity) {
                    valid = false;
                    $rootScope.showWarningMessage("Total Lots quantity and Inward Quantity should be same");
                }

                return valid;
            }

            function validateAttributes() {
                var valid = true;
                angular.forEach(vm.newItemInstanceAttributes, function (attribute) {
                    if (valid) {
                        if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                            attribute.attributeDef.dataType != 'TIMESTAMP') {
                            if ($rootScope.checkAttribute(attribute)) {
                                valid = true;
                            }
                            else {
                                valid = false;
                                $rootScope.showErrorMessage(attribute.attributeDef.name + " is required");
                                $rootScope.hideBusyIndicator();
                            }
                        }
                    }
                });

                return valid;
            }

            function loadItemInstanceAttributes() {
                vm.newItemInstanceAttributes = [];
                ItemTypeService.getAttributesByObjectType("ITEMINSTANCE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: null,
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
                            vm.newItemInstanceAttributes.push(att);
                        });
                    }
                )
            }

            vm.instanceProperties = [];

            function loadInstanceAttributeDefs() {
                vm.instanceProperties = [];
                ItemTypeService.getAttributesByObjectType("ITEMINSTANCE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.inwardItem.instances[0].item.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                value: {
                                    id: {
                                        objectId: vm.inwardItem.instances[0].item.id,
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
                            vm.instanceProperties.push(att);
                        });

                        loadInstanceAttributeValues();
                    }
                )
            }

            function loadInstanceAttributeValues() {
                ObjectAttributeService.getAllObjectAttributes(vm.inwardItem.instances[0].item.id).then(
                    function (data) {
                        var map = new Hashtable();

                        angular.forEach(data, function (attribute) {
                            map.put(attribute.id.attributeDef, attribute);
                        });

                        angular.forEach(vm.instanceProperties, function (attribute) {
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

            function saveObjectAttributes(data) {
                var defered = $q.defer();
                if (vm.newItemInstanceAttributes.length > 0) {
                    vm.propertyImageAttributes = [];
                    var propertyImages = new Hashtable();
                    angular.forEach(vm.newItemInstanceAttributes, function (attribute) {
                        attribute.id.objectId = data.instances[0].item.id;
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
                        ObjectAttributeService.saveItemObjectAttributes(data.instances[0].item.id, vm.newItemInstanceAttributes).then(
                            function (attributes) {
                                if (vm.propertyImageAttributes.length > 0) {
                                    angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                        ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, propertyImages.get(propImgAtt.id.attributeDef)).then(
                                            function (image) {
                                                defered.resolve();
                                            }
                                        )
                                    })
                                } else {
                                    defered.resolve();
                                }

                                $scope.callback(data);
                                vm.inwardItem = null;
                                vm.numberOfLots = 0;
                                vm.lots = [];
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage("Lot created successfully");
                            },
                            function (error) {
                                $rootScope.hideBusyIndicator();
                                defered.reject();
                            }
                        )
                    }, 2000);
                } else {
                    $scope.callback(data);
                    vm.inwardItem = null;
                    vm.numberOfLots = 0;
                    vm.lots = [];
                    $rootScope.hideSidePanel();
                    $rootScope.showSuccessMessage("Lot created successfully");
                }

                return defered.promise;
            }

            function saveMultipleObjectAttributes(instanceIds, data) {
                var defered = $q.defer();
                if (vm.newItemInstanceAttributes.length > 0) {
                    vm.propertyImageAttributes = [];
                    var propertyImages = new Hashtable();
                    var allAttributes = [];
                    var attachmentAttributes = [];
                    angular.forEach(vm.newItemInstanceAttributes, function (attribute) {
                        attribute.id.objectId = instanceIds[0];
                        if (attribute.timeValue != null) {
                            attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");

                        }
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            propertyImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            var attachments = attribute.attachmentValues;
                            var attributeAttach = angular.copy(attribute);
                            attributeAttach.attachmentValues = attachments;
                            attachmentAttributes.push(attributeAttach);
                            attribute.attachmentValues = [];
                        }
                        allAttributes.push(attribute);
                    });

                    if (vm.newItemInstanceAttributes.length == allAttributes.length) {
                        InwardService.saveMultipleLotAttributes(instanceIds, vm.newItemInstanceAttributes).then(
                            function (attributes) {
                                if (vm.propertyImageAttributes.length > 0) {
                                    angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                        InwardService.uploadMultipleLotImageAttribute(instanceIds, propImgAtt.id.attributeDef, propertyImages.get(propImgAtt.id.attributeDef)).then(
                                            function (image) {
                                                defered.resolve();
                                            }
                                        )
                                    })
                                }

                                if (attachmentAttributes.length > 0) {
                                    angular.forEach(attachmentAttributes, function (attribute) {
                                        InwardService.uploadMultipleLotAttachment(instanceIds, attribute.id.attributeDef, attribute.attachmentValues).then(
                                            function (attachments) {

                                            }
                                        )

                                    })
                                }

                                vm.inwardItem = null;
                                vm.numberOfLots = 0;
                                vm.lots = [];
                                $scope.callback(data);
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage("Multiple Lot's created successfully");
                            },
                            function (error) {
                                $rootScope.hideBusyIndicator();
                                defered.reject();
                            }
                        )
                    }
                } else {
                    vm.inwardItem = null;
                    vm.numberOfLots = 0;
                    vm.lots = [];
                    $scope.callback(data);
                    $rootScope.hideSidePanel();
                    $rootScope.showSuccessMessage("Multiple Lot's created successfully");
                }

                return defered.promise;
            }

            function addItemPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'ITEMINSTANCE', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }
                    )
                })
                return propertyAttachmentIds;
            }

            vm.change = change;
            vm.cancel = cancel;
            vm.saveImage = saveImage;
            vm.showImageProperty = showImageProperty;
            vm.addAttachment = addAttachment;
            vm.cancelAttachment = cancelAttachment;
            vm.saveAttachments = saveAttachments;
            vm.changeTime = changeTime;
            vm.cancelTime = cancelTime;
            vm.saveTimeProperty = saveTimeProperty;
            vm.changeTimestamp = changeTimestamp;
            vm.openPropertyAttachment = openPropertyAttachment;
            vm.deleteAttachments = deleteAttachments;
            vm.changeCurrencyValue = changeCurrencyValue;
            vm.thumbnail = null;
            vm.addImage = false;
            vm.cancelChanges = cancelChanges;
            vm.changeAttribute = changeAttribute;
            //vm.saveItemProperties = saveItemProperties;

            function changeTime(attribute) {
                attribute.timeValue = attribute.value.timeValue;
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
                var instanceFile = document.getElementById("instanceFile");
                if (instanceFile != null && instanceFile != undefined) {
                    document.getElementById("instanceFile").value = "";
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

            function saveImage(attribute) {
                if (attribute.newImageValue != null) {
                    $rootScope.showBusyIndicator($(".view-content"));
                    attribute.imageValue = attribute.newImageValue;
                    if (attribute.attributeDef.objectType == 'ITEMINSTANCE') {
                        ObjectAttributeService.updateObjectAttribute(vm.inwardItem.instances[0].id, attribute.value).then(
                            function (data) {
                                ObjectAttributeService.uploadObjectAttributeImage(attribute.id.objectId, attribute.id.attributeDef, attribute.imageValue).then(
                                    function (data) {
                                        attribute.changeImage = false;
                                        attribute.newImageValue = null;
                                        attribute.value.itemImagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();
                                        $rootScope.showSuccessMessage("Image saved successfully");
                                        loadInstanceAttributeValues();
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
                    var instanceIds = [];
                    angular.forEach(vm.inwardItem.instances, function (instance) {
                        instanceIds.push(instance.item.id);
                    })

                    if (instanceIds.length > 0) {
                        InwardService.uploadMultipleLotAttachment(instanceIds, attribute.attributeDef.id, attribute.attachmentValues).then(
                            function (data) {
                                attribute.showAttachment = false;
                                $scope.callback();
                                loadInstanceAttributeValues();
                                $rootScope.showSuccessMessage("Attachments saved successfully");
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                }
            }


            function deleteAttachments(attribute, attachment) {

                var instanceIds = [];
                angular.forEach(vm.inwardItem.instances, function (instance) {
                    instanceIds.push(instance.item.id);
                })
                if (instanceIds.length > 0) {
                    InwardService.deleteInstanceAttributeAttachment(instanceIds, attribute.attributeDef.id, attachment.id).then(
                        function (data) {
                            $scope.callback();
                            loadInstanceAttributeValues();
                            $rootScope.showSuccessMessage("Attachment deleted successfully");
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
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

            function openPropertyAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }

            function saveTimeProperty(attribute) {
                if (attribute.timeValue != null) {
                    attribute.value.timeValue = attribute.timeValue;
                    if (attribute.attributeDef.objectType == 'ITEMINSTANCE') {
                        ObjectAttributeService.updateObjectAttribute(vm.inwardItem.instances[0].id, attribute.value).then(
                            function (data) {
                                attribute.showTimeAttribute = false;
                                $rootScope.showSuccessMessage("Details saved successfully");
                                loadInstanceAttributeValues();
                                $scope.callback();
                            }
                        )
                    }

                } else if (attribute.timestampValue != null) {
                    attribute.timestampValue = moment(attribute.timestampValue).format('DD/MM/YYYY, HH:mm:ss');
                    attribute.value.timestampValue = attribute.timestampValue;
                    if (attribute.attributeDef.objectType == 'ITEMINSTANCE') {
                        ObjectAttributeService.updateObjectAttribute(vm.inwardItem.instances[0].id, attribute.value).then(
                            function (data) {
                                attribute.showTimestamp = false;
                                $rootScope.showSuccessMessage("Value updated successfully");
                                loadInstanceAttributeValues();
                            }
                        )
                    }
                }
            }

            vm.saveInstanceProperties = saveInstanceProperties;
            function saveInstanceProperties(attribute) {
                InwardService.saveAttributeToAllInstances(vm.inwardItem.instances[0].id, attribute.value).then(
                    function (data) {
                        attribute.editMode = false;
                        $rootScope.showSuccessMessage("Details saved successfully");
                        loadInstanceAttributeValues();
                        $scope.callback();
                    }
                )
            }


            (function () {
                if (vm.lotMode === "NEW") {
                    loadItemInstanceAttributes();
                }

                if (vm.lotMode === "EDIT") {
                    loadInstanceAttributeDefs();
                }
                ObjectAttributeService.getCurrencies().then(
                    function (data) {
                        vm.currencies = data;
                        angular.forEach(vm.currencies, function (currency) {
                            currencyMap.put(currency.id, $sce.trustAsHtml(currency.symbol));
                        });
                    }
                );
                $scope.$on('app.inward.lotAllocation.create', createLot)
            })();
        }
    }
);