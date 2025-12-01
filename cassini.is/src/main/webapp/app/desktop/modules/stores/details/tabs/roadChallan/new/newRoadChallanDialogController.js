define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/topStoreService',
        'app/shared/services/core/itemService',
        'app/shared/services/store/roadChallanService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('NewRoadChallanController', NewRoadChallanController);

        function NewRoadChallanController($scope, $rootScope, $q, ObjectTypeAttributeService, RoadChallanService, AttributeAttachmentService, $application, AutonumberService, $state, TopStoreService, ItemService
            , ObjectAttributeService) {

            var vm = this;
            vm.roadChallanItems = [];
            vm.addItems = addItems;
            var roadChallanItemsMap = new Hashtable();
            $rootScope.viewInfo.title = "Road Challans";

            vm.remove = remove;
            vm.create = create;
            vm.generateAutoNumber = generateAutoNumber;
            vm.back = back;
            vm.roadChallanAttributes = [];
            vm.requiredAttributes = [];
            vm.attributes = [];

            vm.newRoadChallan = {
                chalanDate: null,
                goingFrom: null,
                goingTo: null,
                meansOfTrans: null,
                vehicleDetails: null,
                issuingAuthority: null,
                customRoadChalanItems: []
            };

            vm.emptyRoadChalanItem = {
                materialItem: null,
                notes: null,
                quantity: null
            };

            function loadStore() {
                TopStoreService.getTopStore($rootScope.storeId).then(
                    function (data) {
                        vm.newRoadChallan.store = data;
                        $rootScope.viewInfo.description = "Store : " + data.storeName;
                    }
                )
            }

            function remove(item) {
                var index = vm.roadChallanItems.indexOf(item);
                vm.roadChallanItems.splice(index, 1);
                roadChallanItemsMap.remove(item.stockMovementDTO.itemDTO.itemNumber);
            }

            function generateAutoNumber() {
                var prefix = $application.config.autoNumber.prefix;
                /* var year = $application.config.autoNumber.year;*/
                var year = new Date().getFullYear();
                AutonumberService.getAutonumberByName('Default Road Challan Number Source').then(
                    function (data) {
                        AutonumberService.getNextNumber(data.id).then(
                            function (data) {
                                vm.generatedNumber = data;
                                if (prefix != "" && year != "") {
                                    vm.newRoadChallan.chalanNumber = prefix + "/ " + $rootScope.selectedStore.storeName + "/" + year + "/" + vm.generatedNumber;
                                }
                                else {
                                    vm.newRoadChallan.chalanNumber = vm.generatedNumber;
                                }
                            }
                        )
                    });
            }

            function addItems() {
                if (validate()) {
                    var options = {
                        title: 'Road Challan Items',
                        showMask: true,
                        template: 'app/desktop/modules/stores/details/tabs/roadChallan/new/newRoadChallanItemsDialogue.jsp',
                        controller: 'NewRoadChallanItemsController as roadChallanItemsVm',
                        resolve: 'app/desktop/modules/stores/details/tabs/roadChallan/new/newRoadChallanItemsDialogueController',
                        width: 700,
                        data: {
                            newRoadChallan: vm.newRoadChallan,
                            addedItemsMap: roadChallanItemsMap
                        },
                        buttons: [],
                        callback: function (roadItem) {
                            var item = roadChallanItemsMap.get(roadItem.stockMovementDTO.itemDTO.itemNumber);
                            if (item == null) {
                                roadChallanItemsMap.put(roadItem.stockMovementDTO.itemDTO.itemNumber, roadItem);
                                vm.roadChallanItems.push(roadItem);
                            }
                        }
                    };
                    $rootScope.showSidePanel(options);
                }
            }

            function validateRoadChallan() {
                var valid = true;
                vm.newRoadChallan.customRoadChalanItems = [];
                angular.forEach(vm.roadChallanItems, function (roadChallanItem) {
                    if (roadChallanItem.quantity > 0) {
                        if (roadChallanItem.storeOnHand >= roadChallanItem.quantity) {
                            vm.newRoadChallan.customRoadChalanItems.push(roadChallanItem);
                        }
                        else {
                            valid = false;
                            $rootScope.showErrorMessage("Qty cannot be greater than Store Inventory Qty");
                        }
                    } else {
                        valid = false;
                        $rootScope.showErrorMessage("Please enter qty to item's");
                    }
                });

                return valid;
            }

            function create() {
                if (validateRoadChallan()) {
                    attributesValidate().then(
                        function (success) {
                            RoadChallanService.createRoadChallan($rootScope.storeId, vm.newRoadChallan).then(
                                function (data) {
                                    vm.newRoadChallan = data;
                                    intializationForAttributesSave().then(
                                        function (success) {
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.roadchallanId = vm.newRoadChallan.id;
                                            $state.go('app.store.stock.roadChallanDetails', {roadchallanId: vm.newRoadChallan.id});
                                            $rootScope.showSuccessMessage("Road Challan (" + vm.newRoadChallan.chalanNumber + ") created successfully");
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

            function validate() {
                var valid = true;
                if (vm.newRoadChallan.chalanDate == null || vm.newRoadChallan.chalanDate == undefined) {
                    valid = false;
                    $rootScope.showErrorMessage("Field 'Challan Date' cannot be empty");
                }
                else if (vm.newRoadChallan.goingFrom == null || vm.newRoadChallan.goingFrom == "") {
                    valid = false;
                    $rootScope.showErrorMessage("Field 'Going From' cannot be empty");
                }
                else if (vm.newRoadChallan.goingTo == null || vm.newRoadChallan.goingTo == "") {
                    valid = false;
                    $rootScope.showErrorMessage("Field 'Going To' cannot be empty");
                }
                else if (vm.newRoadChallan.meansOfTrans == null || vm.newRoadChallan.meansOfTrans == "") {
                    valid = false;
                    $rootScope.showErrorMessage("Field 'MeansOfTrans' cannot be empty");
                }
                else if (vm.newRoadChallan.vehicleDetails == null || vm.newRoadChallan.vehicleDetails == "") {
                    valid = false;
                    $rootScope.showErrorMessage("Field 'Vehicle Details' cannot be empty");
                }
                else if (vm.newRoadChallan.issuingAuthority == null || vm.newRoadChallan.issuingAuthority == "") {
                    valid = false;
                    $rootScope.showErrorMessage("Field 'Issuing Authority' cannot be empty");
                }
                return valid;
            }

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

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'RC'});
            }

            function addAttachment(attribute) {
                var defered = $q.defer();
                vm.propertyAttachmentIds = [];
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'CUSTOM_ROADCHALAN', attachmentFile).then(
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

            function intializationForAttributesSave() {
                var defered = $q.defer();
                var attrCount = 0;
                vm.propertyImageAttributes = [];
                vm.propertyImages = new Hashtable();
                vm.imageAttributes = [];
                vm.images = new Hashtable();
                vm.requiredAttributes = [];

                if (vm.newRoadChallanAttributes.length == 0) {
                    defered.resolve();
                }
                else {
                    angular.forEach(vm.newRoadChallanAttributes, function (attribute) {
                        attribute.id.objectId = vm.newRoadChallan.id;
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
                                    if (attrCount == vm.newRoadChallanAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (data) {
                                                vm.newRoadChallanAttributes = [];
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
                            if (attrCount == vm.newRoadChallanAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        vm.newRoadChallanAttributes = [];
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
                if (vm.newRoadChallanAttributes != null && vm.newRoadChallanAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.newRoadChallanAttributes);
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

            function saveObjectAttributes() {
                var defered = $q.defer();
                if (vm.newRoadChallanAttributes.length > 0) {
                    angular.forEach(vm.newRoadChallanAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                    });

                    ObjectAttributeService.saveItemObjectAttributes(vm.newRoadChallan.id, vm.newRoadChallanAttributes).then(
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

            function loadObjectAttributeDefs() {
                vm.newRoadChallanAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("CUSTOM_ROADCHALAN").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newRoadChallan.id,
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

                            vm.newRoadChallanAttributes.push(att);
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

            (function () {
                resize();
                loadStore();
                loadObjectAttributeDefs();
            })();
        }
    }
)
;
