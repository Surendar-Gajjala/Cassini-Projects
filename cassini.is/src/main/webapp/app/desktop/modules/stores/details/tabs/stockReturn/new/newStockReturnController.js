/**
 * Created by swapna on 05/12/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/store/stockReturnService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/common/commonService'

    ],
    function (module) {
        module.controller('NewStockReturnController', NewStockReturnController);

        function NewStockReturnController($scope, $rootScope, $stateParams, $state, ProjectService, StockReturnService, AttributeAttachmentService, ObjectAttributeService, $q, ObjectTypeAttributeService, AutonumberService, CommonService) {
            var vm = this;
            $rootScope.viewInfo.title = "New Stock Return";

            vm.showAddRequestsButton = false;
            vm.showCreateRequisitionButton = false;
            vm.loading = false;
            vm.projects = [];
            vm.stockReturnItemList = [];
            vm.persons = [];
            vm.addItems = addItems;
            var itemsMap = new Hashtable();
            vm.create = create;
            vm.removeFromItemList = removeFromItemList;
            vm.generateAutoNumber = generateAutoNumber;

            vm.back = back;
            vm.requisitionAttributes = [];
            vm.requiredAttributes = [];
            vm.attributes = [];
            vm.newStockReturn = {
                project: null,
                notes: null,
                store: $rootScope.storeId,
                returnedTo: null,
                stockReturnItemList: []

            };

            vm.emptyItem = {
                item: null,
                notes: null,
                quantity: null
            };

            function loadProjects() {
                ProjectService.getAllProjects().then(
                    function (data) {
                        vm.projects = data;
                    }
                );
            }

            function loadPersons() {
                CommonService.getAllPersons().then(
                    function (data) {
                        vm.persons = data;
                    }
                )
            }

            function removeFromItemList(item) {
                var index = vm.stockReturnItemList.indexOf(item);
                vm.stockReturnItemList.splice(index, 1);
                itemsMap.remove(item.id);
            }

            function generateAutoNumber() {
                if ($application.config != undefined) {
                    var prefix = $application.config.autoNumber.prefix;
                    var year = $application.config.autoNumber.year;
                }
                AutonumberService.getAutonumberByName('Default Stock Return Number Source').then(
                    function (data) {
                        AutonumberService.getNextNumber(data.id).then(
                            function (data) {
                                vm.generatedNumber = data;
                                if (prefix != "" && year != "" && prefix != undefined && year != undefined) {
                                    vm.newStockReturn.returnNumberSource = prefix + "/ " + $rootScope.selectedStore.storeName + "/" + year + "/" + vm.generatedNumber;
                                }
                                else {
                                    vm.newStockReturn.returnNumberSource = vm.generatedNumber;
                                }
                            }
                        )
                    });
            }

            function addItems() {
                if (validate()) {
                    var options = {
                        title: 'Stock Return Items',
                        showMask: true,
                        template: 'app/desktop/modules/stores/details/tabs/stockReturn/new/newStockReturnItemsDialogView.jsp',
                        controller: 'NewStockReturnItemsController as newStockReturnItemsVm',
                        resolve: 'app/desktop/modules/stores/details/tabs/stockReturn/new/newStockReturnItemsDialogController.js',
                        width: 700,
                        data: {
                            projctObj: vm.newStockReturn.project,
                            newStockReturn: vm.newStockReturn,
                            requisitionItems: vm.stockReturnItemList,
                            addedItemsMap: itemsMap
                        },
                        buttons: [],
                        callback: function (reqItem) {
                            var item = itemsMap.get(reqItem.id);
                            if (item == null) {
                                itemsMap.put(reqItem.id, reqItem);
                                vm.stockReturnItemList.push(reqItem);
                            }
                        }
                    };

                    $rootScope.showSidePanel(options);
                }
            }

            function validateItems() {
                var valid = false;
                var returnItems = [];
                var counter = 0;
                angular.forEach(vm.stockReturnItemList, function (reqItem) {
                    if (reqItem.quantity > 0) {
                        if ((reqItem.quantity + reqItem.itemReturnQuantity) <= reqItem.itemIssueQuantity) {
                            var item = angular.copy(vm.emptyItem);
                            item.item = reqItem.id;
                            item.notes = reqItem.notes;
                            item.quantity = reqItem.quantity;
                            if (vm.newStockReturn.project != null) {
                                item.project = vm.newStockReturn.project.id;
                            }
                            item.recordedBy = window.$application.login.person.id;
                            returnItems.push(item);
                            counter++;
                            if (counter == vm.stockReturnItemList.length) {
                                valid = true;
                                vm.newStockReturn.stockReturnItemList = returnItems;
                            }
                        }
                        else {
                            valid = false;
                            $rootScope.showErrorMessage("Return qty cannot be greater than Issued qty");
                        }
                    }
                    else {
                        valid = false;
                        $rootScope.showErrorMessage("Please enter qty to items");
                    }

                });

                return valid;
            }

            function create() {
                if (validateItems()) {
                    attributesValidate().then(
                        function (success) {
                            if (vm.newStockReturn.project != null) {
                                vm.newStockReturn.project = vm.newStockReturn.project.id;
                            }
                            vm.newStockReturn.returnedTo = vm.newStockReturn.returnedTo.id;
                            if (vm.newStockReturn.returnNumberSource == null || vm.newStockReturn.returnNumberSource == undefined) {
                                generateAutoNumber();
                            }
                            StockReturnService.createStockReturn($rootScope.storeId, vm.newStockReturn).then(
                                function (data) {
                                    vm.newStockReturn = data;
                                    intializationForAttributesSave().then(
                                        function (sucess) {
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.stockReturnId = vm.newStockReturn.id;
                                            $state.go('app.store.stock.stockReturnDetails', {stockReturnId: vm.newStockReturn.id});
                                            $rootScope.showSuccessMessage("Stock Return (" + vm.newStockReturn.returnNumberSource + ") created successfully");
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        });

                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        });
                }
            }

            function validate() {
                var valid = true;
                if (vm.newStockReturn.returnNumberSource == null || vm.newStockReturn.returnNumberSource == undefined) {
                    valid = false;
                    $rootScope.showErrorMessage("Please select 'Auto Number'");
                }
                else if (vm.newStockReturn.returnedTo == null || vm.newStockReturn.returnedTo == "") {
                    valid = false;
                    $rootScope.showErrorMessage("Field 'Returned To' cannot be empty");
                }
                return valid;
            }

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'RET'});
            }

            /* ............. start attributes methods block ............. */

            function addAttachment(attribute) {
                var defered = $q.defer();
                vm.propertyAttachmentIds = [];
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'CUSTOM_REQUISITION', attachmentFile).then(
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

            function saveObjectAttributes() {
                var defered = $q.defer();
                if (vm.newStockReturnAttributes.length > 0) {
                    angular.forEach(vm.newStockReturnAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                    });
                    ObjectAttributeService.saveItemObjectAttributes(vm.newStockReturn.id, vm.newStockReturnAttributes).then(
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
                if (vm.newStockReturnAttributes != null && vm.newStockReturnAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.newStockReturnAttributes);
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

            function loadObjectAttributeDefs() {
                vm.newStockReturnAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("STOCKRETURN").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newStockReturn.id,
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

                            vm.newStockReturnAttributes.push(att);
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

            function intializationForAttributesSave() {
                var defered = $q.defer();
                var attrCount = 0;
                vm.propertyImageAttributes = [];
                vm.propertyImages = new Hashtable();
                vm.imageAttributes = [];
                vm.images = new Hashtable();
                vm.requiredAttributes = [];

                if (vm.newStockReturnAttributes.length == 0) {
                    defered.resolve();
                }
                else {
                    angular.forEach(vm.newStockReturnAttributes, function (attribute) {
                        attribute.id.objectId = vm.newStockReturn.id;
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
                                    if (attrCount == vm.newStockReturnAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (data) {
                                                vm.newStockReturnAttributes = [];
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
                            if (attrCount == vm.newStockReturnAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        vm.newStockReturnAttributes = [];
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

            /* ............. attributes methods end block ............. */

            (function () {
                loadProjects();
                loadPersons();
                loadObjectAttributeDefs();
            })();
        }
    }
)
;