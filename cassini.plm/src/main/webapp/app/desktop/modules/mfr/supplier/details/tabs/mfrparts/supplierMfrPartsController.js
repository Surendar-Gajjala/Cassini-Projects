define(
    [
        'app/desktop/modules/mfr/mfr.module',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/supplierService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('SupplierMfrPartsController', SupplierMfrPartsController);

        function SupplierMfrPartsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window
            , MfrPartsService, DialogService, $translate, SupplierService) {
            var vm = this;

            vm.loading = true;
            $rootScope.mfrparts = [];
            vm.supplierId = $stateParams.supplierId;
            vm.showMfrPartsDetails = showMfrPartsDetails;
            $rootScope.selectMfrParts = selectMfrParts;
            vm.saveParts = saveParts;
            vm.removeParts = removeParts;
            vm.savePart = savePart;
            vm.removePart = removePart;
            vm.deleteMfrpart = deleteMfrpart;
            vm.editPart = editPart;
            vm.cancelChanges = cancelChanges;
            vm.updatePart = updatePart;
            vm.selectedSupplierParts = [];

            vm.supplierParts = [];
            var emptySupplierPart = {
                id: null,
                supplier: vm.supplierId,
                manufacturerPart: null,
                partNumber: null
            };


            var parsed = angular.element("<div></div>");

            var deleteMfrPartDialogTitle = parsed.html($translate.instant("DELETE_SUPPLIER_MFR_PART_DIALOG_TITLE")).html();
            var deleteMfrPartDialogMessage = parsed.html($translate.instant("DELETE_SUPPLIER_MFR_PART_DIALOG_MESSAGE")).html();
            var deleteMfrPartMessage = parsed.html($translate.instant("DELETE_SUPPLIER_MFR_PART_MESSAGE")).html();
            vm.addMfrParts = parsed.html($translate.instant("ADD_MFR_PARTS")).html();

            function deleteMfrpart(mfrpart) {
                var options = {
                    title: deleteMfrPartDialogTitle,
                    message: deleteMfrPartDialogMessage + " [ " + mfrpart.manufacturerPart.partNumber + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        SupplierService.deleteSupplierParts(mfrpart.id).then(
                            function (data) {
                                loadMfrItems();
                                $rootScope.loadSupplierFileCounts();
                                $rootScope.showSuccessMessage(deleteMfrPartMessage);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                });
            }


            function loadMfrItems() {
                vm.loading = true;
                vm.selectedSupplierParts = [];
                vm.supplierParts = [];
                SupplierService.getSupplierParts(vm.supplierId).then(
                    function (data) {
                        vm.supplierParts = data;
                        angular.forEach(vm.supplierParts, function (part) {
                            part.editMode = false;
                            part.isNew = false;
                        });
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                        //$rootScope.loadSupplierFileCounts();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            var mfrPartsSelectionTitle = $translate.instant("SELECT_MFR_PARTS_SELECTION");
            var partsAddedMessage = $translate.instant("MFR_PARTS_ADDED_MESSAGE");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var selectMfrPartsTitle = parsed.html($translate.instant("ITEM_DETAILS_SELECT_MFR_PARTS")).html();
            var itemManufacturerAddedSuccess = parsed.html($translate.instant("SUPPLIER_MFR_PARTS_ADDED")).html();
            var itemManufacturerUpdatedSuccess = parsed.html($translate.instant("ITEM_MFR_PARTS_UPDATED")).html();
            $scope.thisIsConfigurableItemPart = parsed.html($translate.instant("THIS_IS_CONFIGURABLE_ITEM_PART")).html();
            $scope.configurableItemPart = parsed.html($translate.instant("CONFIGURABLE_ITEM_PART")).html();

            vm.supplierParts = [];

            function selectMfrParts() {
                var options = {
                    title: mfrPartsSelectionTitle,
                    template: 'app/desktop/modules/item/details/selectMfrItemView.jsp',
                    controller: 'SelectMfrItemController as mfrItemVm',
                    resolve: 'app/desktop/modules/item/details/selectMfrItemController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedSupplierId: vm.supplierId,
                        selectMfrPartsMode: "SUPPLIER",
                        mfrParts: $rootScope.mfrparts
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.item.mfr.new'}
                    ],
                    callback: function (result) {
                        // vm.supplierParts = [];
                        /*angular.forEach(result, function (mfrPart) {
                         var obj = {
                         supplier: null,
                         manufacturerPart: null,
                         partNumber: null
                         };
                         obj.manufacturerPart = mfrPart;
                         obj.partNumber = mfrPart.partNumber;
                         obj.supplier = vm.supplierId;
                         vm.supplierParts.push(obj);
                         });
                         createSupplierMfrPart(vm.supplierParts);*/

                        angular.forEach(result, function (sPart) {
                            var part = angular.copy(emptySupplierPart);
                            part.editMode = true;
                            part.isNew = true;
                            part.manufacturerPart = sPart;
                            part.manufacturerPart.mfrName = sPart.manufacturerObject.name;
                            part.partNumber = sPart.partNumber;
                            vm.supplierParts.unshift(part);
                            vm.selectedSupplierParts.push(part);
                        });


                    }
                };

                $rootScope.showSidePanel(options);
            }


            /*function createSupplierMfrPart(mfrParts) {
             SupplierService.createSupplierParts(vm.supplierId, vm.selectedSpareParts).then(
             function (data) {
             $rootScope.showSuccessMessage(itemManufacturerAddedSuccess);
             loadMfrItems();
             // $rootScope.getItemMfrParts();
             },
             function (error) {
             $rootScope.showErrorMessage(error.message)
             }
             )
             }*/

            function saveParts() {
                if (validateParts()) {
                    $rootScope.showBusyIndicator();
                    SupplierService.createSupplierParts(vm.supplierId, vm.selectedSupplierParts).then(
                        function (data) {
                            loadMfrItems();
                            $rootScope.loadSupplierFileCounts();
                            vm.selectedSupplierParts = [];
                            $rootScope.showSuccessMessage(itemManufacturerAddedSuccess);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            function validateParts() {
                var valid = true;
                angular.forEach(vm.selectedSupplierParts, function (part) {
                    if (valid) {
                        if (part.partNumber == null || part.partNumber == "" || part.partNumber == undefined) {
                            valid = false;
                            $rootScope.showWarningMessage("Please enter supplier part number");
                        }
                    }
                })
                return valid;
            }


            function removeParts() {
                angular.forEach(vm.selectedSupplierParts, function (item) {
                    vm.supplierParts.splice(vm.supplierParts.indexOf(item), 1);
                })
                vm.selectedSupplierParts = [];
            }

            function savePart(part) {
                if (validatePart(part)) {
                    SupplierService.createSupplierPart(vm.supplierId, part).then(
                        function (data) {
                            part.id = data.id;
                            part.manufacturerPart = data.manufacturerPart;
                            part.editMode = false;
                            part.isNew = false;
                            vm.selectedSupplierParts.splice(vm.selectedSupplierParts.indexOf(part), 1);
                            $rootScope.loadSupplierFileCounts();
                            $rootScope.showSuccessMessage("Supplier part added successfully");
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validatePart(part) {
                var valid = true;

                if (part.partNumber == null || part.partNumber == "" || part.partNumber == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter supplier part number");
                }
                return valid;
            }

            function updatePart(part) {
                if (validatePart(part)) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    SupplierService.updateSupplierPart(vm.supplierId, part).then(
                        function (data) {
                            part.editMode = false;
                            loadMfrItems();
                            $rootScope.loadSupplierFileCounts();
                            $rootScope.showSuccessMessage("Supplier part updated successfully");
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function removePart(part) {
                vm.supplierParts.splice(vm.supplierParts.indexOf(part), 1);
                vm.selectedSupplierParts.splice(vm.selectedSupplierParts.indexOf(part), 1);
            }

            function editPart(part) {
                part.oldPartNumber = part.partNumber;
                part.editMode = true;
            }

            function cancelChanges(part) {
                part.partNumber = part.oldPartNumber;
                part.editMode = false;
            }


            vm.showMfrPartsDetails = showMfrPartsDetails;

            function showMfrPartsDetails(mfrpart) {
                $window.localStorage.setItem("shared-permission", $rootScope.sharedPermission);
                $state.go('app.mfr.mfrparts.details', {
                    mfrId: mfrpart.manufacturerPart.manufacturer,
                    manufacturePartId: mfrpart.manufacturerPart.id
                });
            }

            vm.showMfr = showMfr;

            function showMfr(mfrpart) {
                $window.localStorage.setItem("shared-permission", $rootScope.sharedPermission);
                $state.go('app.mfr.details', {manufacturerId: mfrpart.manufacturerPart.manufacturer});
            }


            (function () {
                $scope.$on('app.supplier.tabActivated', function (event, data) {
                    if (data.tabId == 'details.parts') {
                        vm.supplierParts = [];
                        loadMfrItems();
                        $rootScope.loadSupplierFileCounts();
                    }
                });
            })();
        }
    }
);