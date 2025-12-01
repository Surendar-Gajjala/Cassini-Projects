/**
 * Created by swapna on 31/07/18.
 */
define(
    [
        'app/desktop/modules/stores/store.module',
        'app/shared/services/store/topStockReceivedService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/pm/project/wbsService',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/supplier/supplierService',
        'app/shared/services/store/customPurchaseOrderService',
        'app/desktop/modules/stores/details/tabs/receives/directive/receiveTypeDirective',
        'app/shared/services/core/itemTypeService'
    ],
    function (module) {
        module.controller('NewStockReceiveDialogController', NewStockReceiveDialogController);

        function NewStockReceiveDialogController($scope, $rootScope, $timeout, $state, $stateParams, AutonumberService, ProjectService, CustomPurchaseOrderService,
                                                 $q, TopStockReceivedService, ItemService, $application, ObjectAttributeService, AttributeAttachmentService, SupplierService,
                                                 ItemTypeService) {

            var vm = this;
            vm.stockReceiveItems = [];
            vm.addItems = addItems;
            var stockReceiveItemsMap = new Hashtable();
            $rootScope.viewInfo.title = "Stock Receive";
            vm.remove = remove;
            vm.create = create;
            vm.autoNumber = autoNumber;
            vm.back = back;
            vm.onSelectType = onSelectType;
            vm.newStockReceiveAttributes = [];
            vm.requiredAttributes = [];
            var allReceiveTypeAttributes = [];

            vm.poList = [];
            vm.storeId = $stateParams.storeId;
            vm.supplierList = [];

            vm.newStockReceive = {
                materialReceiveType: null,
                name: null,
                store: vm.storeId,
                receiveNumberSource: null,
                notes: null,
                project: null,
                supplier: null,
                purchaseOrderNumber: null,
                stockReceiveItems: []

            };

            vm.emptyReceiveItem = {
                materialItem: null,
                notes: null,
                quantity: null
            };

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            function remove(item) {
                var index = vm.stockReceiveItems.indexOf(item);
                vm.stockReceiveItems.splice(index, 1);
                stockReceiveItemsMap.remove(item.itemNumber);
            }

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'RECEIVE'});
            }

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.newStockReceive.materialReceiveType = null;
                    vm.newStockReceive.materialReceiveType = itemType;
                    loadReceiveTypeAttributeDefs();
                }
            }

            function autoNumber() {
                var prefix = $application.config.autoNumber.prefix;
                /*var year = $application.config.autoNumber.year;*/
                var year = new Date().getFullYear();
                AutonumberService.getAutonumberByName('Default Stock Receive Number Source').then(
                    function (data) {
                        AutonumberService.getNextNumber(data.id).then(
                            function (data) {
                                vm.generatedNumber = data;
                                if (prefix != "" && year != "") {
                                    vm.newStockReceive.receiveNumberSource = prefix + "/ " + $rootScope.selectedStore.storeName + "/" + year + "/" + vm.generatedNumber;
                                }
                                else {
                                    vm.newStockReceive.receiveNumberSource = vm.generatedNumber;
                                }
                            }
                        )
                    });
            }

            function addItems() {
                if (vm.newStockReceive.receiveNumberSource == null) {
                    $rootScope.showErrorMessage("Please select Auto Number");
                }
                else {
                    var options = {
                        title: 'Stock Receive Items',
                        showMask: true,
                        template: 'app/desktop/modules/stores/details/tabs/receives/new/newStockReceiveItemsDialogView.jsp',
                        controller: 'NewStockReceiveItemsController as stockReceiveItemsVm',
                        resolve: 'app/desktop/modules/stores/details/tabs/receives/new/newStockReceiveItemsDialogController',
                        width: 700,
                        data: {
                            newStockReceive: vm.newStockReceive,
                            addedItemsMap: stockReceiveItemsMap
                        },
                        buttons: [],
                        callback: function (receiveItem) {
                            var item = stockReceiveItemsMap.get(receiveItem.itemNumber);
                            if (item == null) {
                                stockReceiveItemsMap.put(receiveItem.itemNumber, receiveItem);
                                vm.stockReceiveItems.push(receiveItem);
                            }
                        }
                    };

                    $rootScope.showSidePanel(options);
                }
            }

            function loadObjectAttributeDefs() {
                vm.newStockReceiveAttributes = [];
                vm.attributes = [];
                ItemService.getAllTypeAttributes("RECEIVE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newStockReceive.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                listValue: null,
                                stringValue: null,
                                newListValue: null,
                                mListValue: [],
                                timeValue: null,
                                timestampValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                attachmentValues: [],
                                currencyType: null,
                                longTextValue: null,
                                richTextValue: null
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.required == false) {
                                vm.attributes.push(att);
                            } else {
                                vm.requiredAttributes.push(att);
                            }

                            vm.newStockReceiveAttributes.push(att);
                        });
                    }, function (error) {

                    });
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
                if (vm.newStockReceiveAttributes != null && vm.newStockReceiveAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.newStockReceiveAttributes);
                }
                if (allReceiveTypeAttributes != null && allReceiveTypeAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(allReceiveTypeAttributes);
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

            function checkAttribute(attribute) {
                if ((attribute.stringValue != null && attribute.stringValue != undefined && attribute.stringValue != "") ||
                    (attribute.integerValue != null && attribute.integerValue != undefined && attribute.integerValue != "") ||
                    (attribute.doubleValue != null && attribute.doubleValue != undefined && attribute.doubleValue != "") ||
                    (attribute.dateValue != null && attribute.dateValue != undefined && attribute.dateValue != "") ||
                    (attribute.imageValue != null && attribute.imageValue != undefined && attribute.imageValue != "") ||
                    (attribute.currencyValue != null && attribute.currencyValue != undefined && attribute.currencyValue != "") ||
                    (attribute.timeValue != null && attribute.timeValue != undefined && attribute.timeValue != "") ||
                    (attribute.attachmentValues != undefined && attribute.attachmentValues.length != 0) ||
                    (attribute.refValue != null && attribute.refValue != undefined && attribute.refValue != "") ||
                    (attribute.listValue != null && attribute.listValue != undefined && attribute.listValue != "")) {
                    return true;
                } else {
                    return false;
                }
            }

            function addAttachment(attribute) {
                var defered = $q.defer();
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'RECEIVE', attachmentFile).then(
                        function (data) {
                            attribute.propertyAttachmentIds.push(data[0].id);
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
                vm.receiveTypeImageAttributes = [];
                vm.receiveTypeImages = new Hashtable();
                vm.imageAttributes = [];
                if (allReceiveTypeAttributes != null && allReceiveTypeAttributes != undefined) {
                    vm.newStockReceiveAttributes = vm.newStockReceiveAttributes.concat(allReceiveTypeAttributes);
                }
                if (vm.newStockReceiveAttributes.length > 0) {
                    angular.forEach(vm.newStockReceiveAttributes, function (attribute) {
                        attribute.propertyAttachmentIds = [];
                        attribute.id.objectId = vm.newStockReceive.id;
                        if (attribute.attributeDef.dataType == "IMAGE" && attribute.imageValue != null) {
                            vm.propertyImages.put(attribute.attributeDef.id, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues != undefined && attribute.attachmentValues.length > 0) {
                            addAttachment(attribute).then(
                                function (data) {
                                    attribute.attachmentValues = attribute.propertyAttachmentIds;
                                    attrCount++;
                                    if (attrCount == vm.newStockReceiveAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (data) {
                                                vm.newStockReceiveAttributes = [];
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
                            if (attrCount == vm.newStockReceiveAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        vm.newStockReceiveAttributes = [];
                                        loadObjectAttributeDefs();
                                        defered.resolve();
                                    }, function (error) {
                                        defered.reject();
                                    }
                                )
                            }
                        }
                    });
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function validateReceive() {
                var valid = false;
                var counter = 0;
                var stockrecItems = [];
                angular.forEach(vm.stockReceiveItems, function (stockReceiveItem) {
                    if (stockReceiveItem.quantity > 0) {
                        var copiedstockReceiveItem = angular.copy(vm.emptyReceiveItem);
                        copiedstockReceiveItem.item = stockReceiveItem.id;
                        copiedstockReceiveItem.notes = stockReceiveItem.notes;
                        copiedstockReceiveItem.quantity = stockReceiveItem.quantity;
                        copiedstockReceiveItem.recordedBy = window.$application.login.person.id;
                        copiedstockReceiveItem.movementType = "RECEIVED";
                        copiedstockReceiveItem.receivedBy = window.$application.login.person.id;
                        stockrecItems.push(copiedstockReceiveItem);
                        counter++;
                        if (counter == vm.stockReceiveItems.length) {
                            valid = true;
                            vm.newStockReceive.stockReceiveItems = stockrecItems;
                        }
                    } else {
                        valid = false;
                        $rootScope.showErrorMessage("Please enter qty to item's");
                    }
                });

                return valid;
            }

            function create() {
                $rootScope.closeNotification();
                if (validateReceive()) {
                    attributesValidate().then(
                        function (success) {
                            if (vm.newStockReceive.project != null || vm.newStockReceive.project != undefined) {
                                vm.newStockReceive.project = vm.newStockReceive.project.id;
                            }
                            if (vm.newStockReceive.supplier != null || vm.newStockReceive.supplier != undefined) {
                                vm.newStockReceive.supplier = vm.newStockReceive.supplier.id;
                            }
                            TopStockReceivedService.createTopStockReceive($stateParams.storeId, vm.newStockReceive).then(
                                function (data) {
                                    vm.newStockReceive = data;
                                    intializationForAttributesSave().then(
                                        function (success) {
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.hideSidePanel();
                                            $rootScope.receiveId = vm.newStockReceive.id;
                                            $state.go('app.store.stock.receiveDetails', {receiveId: vm.newStockReceive.id});
                                            $rootScope.showSuccessMessage("Stock Receive (" + vm.newStockReceive.receiveNumberSource + ") created successfully");
                                        }, function (error) {

                                        }
                                    )

                                }, function (error) {
                                    $rootScope.showErrorMessage("{0} Number already exists".format(vm.newStockReceive.receiveNumberSource));
                                }
                            )
                        }, function (error) {

                        });
                }

            }

            function saveObjectAttributes() {
                var defered = $q.defer();
                if (vm.newStockReceiveAttributes.length > 0) {
                    angular.forEach(vm.newStockReceiveAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                    });
                    ObjectAttributeService.saveItemObjectAttributes(vm.newStockReceive.id, vm.newStockReceiveAttributes).then(
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
                }
                return defered.promise;
            }

            function validate() {
                var flag = true;
                if (vm.newStockReceive.receiveNumberSource == null || vm.newStockReceive.receiveNumberSource == "") {
                    flag = false;
                    $rootScope.showErrorMessage("Please select Auto Number");
                }
                return flag;
            }

            function loadProjects() {
                ProjectService.getAllProjects().then(
                    function (data) {
                        vm.projects = data;
                    }
                );
            }

            function loadSuppliers() {
                SupplierService.getSuppliers(vm.pageable).then(
                    function (data) {
                        vm.supplierList = data.content;
                    }
                );
            }

            function loadReceiveTypeAttributeDefs() {
                vm.receiveTypeAttributes = [];
                vm.requiredReceiveTypeAttributes = [];
                allReceiveTypeAttributes = [];
                ItemTypeService.getReceiveTypeAttributesWithHierarchy(vm.newStockReceive.materialReceiveType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newStockReceive.id,
                                    attributeDef: attribute.id
                                },
                                value: {
                                    id: {
                                        objectId: vm.newStockReceive.id,
                                        attributeDef: attribute.id
                                    }
                                },
                                attributeDef: attribute,
                                listValue: null,
                                stringValue: null,
                                newListValue: null,
                                mListValue: [],
                                timeValue: null,
                                timestampValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                attachmentValues: [],
                                currencyType: null,
                                longTextValue: null,
                                richTextValue: null,
                                attachmentIds: []
                            };

                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.required == false) {
                                vm.receiveTypeAttributes.push(att);
                            } else {
                                vm.requiredReceiveTypeAttributes.push(att);
                            }
                            allReceiveTypeAttributes.push(att);
                        });
                    });
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $scope.$on('app.storeItems.receive', function () {
                        create();
                    });
                    loadProjects();
                    loadObjectAttributeDefs();
                    loadSuppliers();
                }
            })();
        }
    }
);