define(
    [
        'app/desktop/modules/storage/storage.module',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/storageService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/bomService',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('StorageDetailsController', StorageDetailsController);

        function StorageDetailsController($scope, $rootScope, $timeout, $state, $stateParams, DialogService, $cookies,
                                          StorageService, BomService, ItemService, ItemTypeService, CommonService) {

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;
            $rootScope.viewInfo.icon = "fa fa-bank";
            $rootScope.viewInfo.title = "Storage";
            vm.object = null;
            vm.attributesShow = false;

            vm.addAttribute = addAttribute;
            vm.deleteAttribute = deleteAttribute;
            vm.applyChanges = applyChanges;
            vm.cancelChanges = cancelChanges;
            vm.editAttribute = editAttribute;
            vm.toggleSection = toggleSection;
            vm.selectStorageParts = selectStorageParts;
            vm.deleteStorageItem = deleteStorageItem;
            $rootScope.showSaveButton = false;
            vm.barcodeBust = (new Date()).getTime();
            vm.itemCodes = [];

            var newAttribute = {
                id: null,
                name: null,
                description: null,
                dataType: null,
                required: false,
                newRequired: false,
                newDataType: null,
                newName: null,
                newDescription: null,
                objectType: 'STORAGETYPE',
                editMode: true,
                showValues: false
            };
            vm.sections = {
                basic: false,
                items: true,
                inventory: true
            };
            vm.barcodeImageLoaded = barcodeImageLoaded;
            vm.barcodeImageLoading = true;

            vm.onChangeName = onChangeName;
            function onChangeName(storage) {
                $rootScope.storeType = (storage.type) + " [ " + (storage.name) + " ]";
            }

            vm.changeOnHoldType = changeOnHoldType;
            vm.changeReturnedType = changeReturnedType;

            function changeOnHoldType(storage) {
                if (storage.onHold == true) {
                    storage.returned = false;
                }
            }

            function changeReturnedType(storage) {
                if (storage.returned == true) {
                    storage.onHold = false;
                }
            }

            function barcodeImageLoaded() {
                vm.barcodeImageLoading = false;
            }

            function toggleSection(section) {
                if (section == 'basic') {
                    vm.sections.basic = !vm.sections.basic;
                }
                else if (section == 'items') {
                    vm.sections.items = !vm.sections.items;
                } else if (section == 'inventory') {
                    vm.sections.inventory = !vm.sections.inventory;
                }
            }

            function cancelChanges(attribute) {
                if (attribute.id == null || attribute.id == undefined) {
                    vm.object.attributes.splice(vm.object.attributes.indexOf(attribute), 1);
                }
                else {
                    attribute.newName = attribute.name;
                    attribute.newDescription = attribute.description;
                    attribute.newDataType = attribute.dataType;
                    attribute.newRequired = attribute.required;
                    attribute.editMode = false;
                    $timeout(function () {
                        attribute.showValues = true;
                    }, 500);
                }
            }

            function deleteAttribute(attribute) {
                var options = {
                    title: 'Delete Attribute',
                    message: 'Are you sure you want to delete (' + attribute.name + ') attribute?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        if (attribute.id != null && attribute.id != undefined) {
                            StorageService.deleteAttribute(vm.object.id, attribute.id).then(
                                function (data) {
                                    vm.object.attributes.splice(vm.object.attributes.indexOf(attribute), 1);
                                    $rootScope.showSuccessMessage(attribute.name + " : Attribute deleted successfully");
                                }
                            )
                        }
                    }
                });
            }

            function editAttribute(attribute) {
                attribute.showValues = false;
                attribute.newName = attribute.name;
                attribute.newDescription = attribute.description;
                attribute.newDataType = attribute.dataType;
                attribute.newRequired = attribute.required;
                attribute.dataType = attribute.newDataType;
                $timeout(function () {
                    attribute.editMode = true;
                }, 500);
            }

            function applyChanges(attribute) {
                var promise = null;
                if (validateAttribute(attribute, 'applyChanges')) {
                    attribute.name = attribute.newName;
                    attribute.description = attribute.newDescription;
                    attribute.dataType = attribute.newDataType;
                    attribute.required = attribute.newRequired;
                    if (attribute.id == null || attribute.id == undefined) {
                        attribute.storageType = vm.object.id;
                        attribute.storageObjectType = vm.object.type;
                        promise = StorageService.createAttribute(vm.object.id, attribute);
                    }
                    else {
                        promise = StorageService.updateAttribute(vm.object.id, attribute);
                    }
                }
                if (promise != null) {
                    promise.then(
                        function (data) {
                            $rootScope.showSuccessMessage(data.name + " : Attribute saved successfully");
                            $timeout(function () {
                                $rootScope.closeNotification();
                            }, 3000);
                            attribute.id = data.id;
                            attribute.editMode = false;
                            $timeout(function () {
                                attribute.showValues = true;
                            }, 500);
                        }
                    )
                }
            }

            function addAttribute() {
                var att = angular.copy(newAttribute);
                att.object = vm.object.id;
                if (vm.object.attributes != undefined) {
                    vm.object.attributes.unshift(att);
                } else {
                    vm.object.attributes = [];
                    vm.object.attributes.unshift(att);
                }
            }

            function validateAttribute(att) {
                var valid = true;
                if (att.newName == undefined || att.newName == null || att.newName == "") {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Attribute Name");
                } else if (att.newDataType == undefined && att.newDataType == null || att.newDataType == "") {
                    valid = false;
                    $rootScope.showWarningMessage("Please select Data Type");
                } else {
                    angular.forEach(vm.object.attributes, function (attr) {
                        if (attr.name == att.newName && attr.id != att.id) {
                            $rootScope.showWarningMessage("Attribute Name already exists! Please enter new Name");
                            valid = false;
                        }
                    })
                }
                return valid;
            }

            vm.printDiv = printDiv;

            function printDiv(print) {
                var divElements = document.getElementById(print);

                var printData = window.open("", "", "left=0,top=0,width=800,height=900,toolbar=0,scrollbars=0,status=0");
                printData.document.write(divElements.innerHTML);
                printData.document.close();
                printData.focus();
                $timeout(
                    function () {
                        printData.print();
                        printData.close();
                    }, 1000
                );
            }


            /*---------------------------------   Drop Down settings for Each Level ----------------------------------*/

            $scope.itemTypeSettings = {
                showCheckAll: true,
                showUncheckAll: true,
                styleActive: true,
                idProperty: "id",
                scrollableHeight: '300', scrollable: true,
                minWidth: '262px', searchField: 'itemName',
                enableSearch: true,
                template: '{{option.subTypeCode}} - {{option.itemName}} ' +
                "<span ng-if='option.parentPath != null'>" + " ( {{option.parentPath}} )</span>"
            };

            /*---------- Loading All Systems created in BOM Structure ----------------------------*/


            /*--------------------------  Loading Storage Details With Type   --------------------*/
            vm.disableSystem = false;
            $rootScope.selectedStorageParts = [];
            var nodeId = null;

            function itemTypeSelected(event, args) {
                nodeId = args.nodeId;
                $rootScope.selectedStorageParts = [];
                vm.storageInventories = [];
                vm.storageParts = [];
                vm.storageItems = [];
                vm.object = args.object;
                if (vm.object != null) {
                    if (vm.object.id != null && vm.object.id != undefined) {
                        StorageService.getStorageDetails(vm.object.id).then(
                            function (data) {
                                vm.object = data.storage;
                                vm.object.parentData = data.parentData;
                                vm.previousCapacity = vm.object.capacity;
                                vm.storageParts = data.storageParts;
                                $rootScope.storagePartsCount = data.storageParts.length;
                                vm.storageItems = data.storageItems;
                                vm.storageInventory = data.storageInventory;
                            }
                        );
                    }
                }
                $scope.$evalAsync();
            }

            vm.selectedBom = null;
            vm.validateStorageByBom = validateStorageByBom;
            vm.selectedStorageItems = [];

            function validateStorageByBom(bom) {
                $rootScope.showBusyIndicator($("#validateStorage-view"))
                if (vm.selectedBom != null) {
                    BomService.validateStorageByBom(vm.selectedBom.id).then(
                        function (data) {
                            vm.selectedStorageItems = data;
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    $rootScope.showWarningMessage("Please Select BOM");
                }
            }

            vm.onHoldLocationsPopover = {
                templateUrl: 'app/desktop/modules/storage/details/onHoldLocations.jsp'
            };

            vm.returnLocationsPopover = {
                templateUrl: 'app/desktop/modules/storage/details/returnLocations.jsp'
            };

            vm.inventoryLocationsPopover = {
                templateUrl: 'app/desktop/modules/storage/details/inventoryLocations.jsp'
            };

            $rootScope.showValidateStorageView = showValidateStorageView;
            function showValidateStorageView() {
                var modal = document.getElementById("validateStorage-view");
                modal.style.display = "block";
            }

            vm.closeValidateStorageView = closeValidateStorageView;

            function closeValidateStorageView() {
                var modal = document.getElementById("validateStorage-view");
                modal.style.display = "none";
            }

            /*-----------------------   Validation for Storage -------------------------------*/

            function validateStorage() {
                var valid = true;
                if (vm.object.name == null || vm.object.name == "" || vm.object.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Name");
                }
                /*else if (vm.object.bom == null || vm.object.bom == "" || vm.object.bom == undefined) {
                 valid = false;
                 $rootScope.showWarningMessage("Please select BOM");
                 }*/

                return valid;
            }

            /*------------------------   To Save Storage with Type  ------------------------------*/

            function onSave() {
                if (validateStorage()) {
                    if (vm.object.id == null || vm.object.id == undefined) {
                        vm.object.remainingCapacity = vm.object.capacity;
                        $rootScope.showBusyIndicator($('.view-container'));
                        StorageService.createStorage(vm.object).then(
                            function (data) {
                                vm.object = data;
                                vm.previousCapacity = vm.object.capacity;
                                StorageService.saveStorageParts(vm.object.id, $rootScope.selectedStorageParts).then(
                                    function (parts) {
                                        vm.storageItems = parts;
                                        $rootScope.$broadcast('app.storage.update', {
                                            object: vm.object,
                                            nodeId: nodeId
                                        });
                                        $rootScope.storeType = (vm.object.type) + " [ " + vm.object.name + " ]";
                                        $rootScope.showSuccessMessage("Storage created successfully");
                                        $rootScope.hideBusyIndicator();
                                    }
                                )

                            }, function (error) {
                                $rootScope.showWarningMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else {
                        if (vm.object.capacity < vm.storageParts.length) {
                            vm.object.capacity = vm.previousCapacity;
                            $rootScope.showErrorMessage("Storage Capacity should be greater than " + vm.storageParts.length);
                        } else {
                            if (vm.object.capacity > vm.previousCapacity) {
                                vm.object.remainingCapacity = vm.object.remainingCapacity + (vm.object.capacity - vm.previousCapacity);
                            } else if (vm.object.capacity < vm.previousCapacity) {
                                vm.object.remainingCapacity = vm.object.remainingCapacity - (vm.previousCapacity - vm.object.capacity)
                            }
                            $rootScope.showBusyIndicator($('.view-container'));
                            StorageService.updateStorage(vm.object).then(
                                function (data) {
                                    vm.object = data;
                                    vm.previousCapacity = vm.object.capacity;
                                    StorageService.saveStorageParts(vm.object.id, $rootScope.selectedStorageParts).then(
                                        function (parts) {
                                            vm.storageItems = parts;
                                            $rootScope.$broadcast('app.storage.update', {
                                                object: vm.object,
                                                nodeId: nodeId
                                            });
                                            $rootScope.storeType = (vm.object.type) + " [ " + vm.object.name + " ]";
                                            $rootScope.showSuccessMessage("Storage updated successfully");
                                            $rootScope.hideBusyIndicator();
                                        }
                                    )

                                }, function (error) {
                                    $rootScope.showWarningMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                }
            }

            vm.upnPopOver = {
                templateUrl: 'app/desktop/modules/storage/details/upnPopover.jsp'
            };

            function selectStorageParts() {
                $rootScope.selectedStorageParts = [];
                var options = {
                    title: "Select Parts",
                    template: 'app/desktop/modules/storage/details/selection/storagePartSelectionView.jsp',
                    controller: 'StoragePartSelectionController as storagePartSelectionVm',
                    resolve: 'app/desktop/modules/storage/details/selection/storagePartSelectionController',
                    width: 700,
                    showMask: true,
                    side: 'left',
                    data: {
                        bomDetails: vm.object.bom,
                        storageDetails: vm.object
                    },
                    buttons: [
                        {text: "Close", broadcast: 'app.storage.details.parts'}
                    ],
                    callback: function () {

                    }
                };

                $rootScope.showSidePanel(options);

            }

            $rootScope.addSelectPartToStorage = addSelectPartToStorage;
            function addSelectPartToStorage(item) {
                var emptyItem = {
                    id: null,
                    item: null,
                    section: null
                };
                emptyItem.item = item;
                emptyItem.section = item.defaultSection;
                vm.storageItems.push(emptyItem);
                $rootScope.selectedStorageParts.push(item);
            }


            function deleteStorageItem(storageItem) {
                if (storageItem.id == null) {
                    vm.storageItems.splice(vm.storageItems.indexOf(storageItem), 1);
                    $rootScope.selectedStorageParts.splice(vm.storageItems.indexOf(storageItem), 1);
                } else {
                    StorageService.deleteStorageItem(storageItem.id).then(
                        function (data) {
                            vm.storageItems.splice(vm.storageItems.indexOf(storageItem), 1);
                            $rootScope.selectedStorageParts.splice(vm.storageItems.indexOf(storageItem), 1);
                        }
                    )
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $scope.$on('app.storage.selected', itemTypeSelected);
                    $scope.$on('app.storage.save', onSave);
                }
            })();
        }
    }
)
;