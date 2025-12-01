define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/shared/services/core/workOrderService'
    ],
    function (module) {
        module.controller('WorkOrderSparePartsController', WorkOrderSparePartsController);

        function WorkOrderSparePartsController($scope, $rootScope, $sce, $timeout, $translate, $state, $stateParams, DialogService, WorkOrderService) {
            var vm = this;
            vm.workOrderId = $stateParams.workOrderId;

            vm.addSpareParts = addSpareParts;
            vm.saveParts = saveParts;
            vm.removeParts = removeParts;
            vm.savePart = savePart;
            vm.removePart = removePart;
            vm.deletePart = deletePart;
            vm.editPart = editPart;
            vm.cancelChanges = cancelChanges;
            vm.showSparePart = showSparePart;
            vm.updatePart = updatePart;

            var parsed = angular.element("<div></div>");
            var sparePartAdded = parsed.html($translate.instant("SPARE_PART_ADDED")).html();
            var sparePartsAdded = parsed.html($translate.instant("SPARE_PARTS_ADDED")).html();
            var sparePartRemoved = parsed.html($translate.instant("SPARE_PART_REMOVED")).html();
            var sparePartUpdated = parsed.html($translate.instant("SPARE_PART_UPDATED")).html();
            var addSparePartsTitle = parsed.html($translate.instant("ADD_SPARE_PARTS")).html();
            var removeSparePartTitle = parsed.html($translate.instant("REMOVE_SPARE_PART")).html();
            var removeSparePartMsg = parsed.html($translate.instant("REMOVE_SPARE_PART_MSG")).html();
            var pleaseEnterQty = parsed.html($translate.instant("PLEASE_ENTER_QTY")).html();
            var pleaseEnterSerialNumbers = parsed.html($translate.instant("PLEASE_ENTER_SERIAL_NUMBERS")).html();
            var qtyAndSerialShouldSame = parsed.html($translate.instant("QTY_SERIAL_SHOULD_SAME")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();

            vm.selectedSpareParts = [];

            var emptySparePart = {
                id: null,
                workOrder: vm.workOrderId,
                sparePart: null,
                quantity: null,
                serialNumbers: [],
                disposition: "REPLACE",
                notes: null,
                newSerialNumbers: null
            }

            vm.dispositionTypes = [
                {label: "REPLACE", value: "REPLACE"},
                {label: "REPAIR", value: "REPAIR"}
            ];

            function loadWorkOrderSpareParts() {
                vm.workOrderSpareParts = [];
                vm.selectedSpareParts = [];
                WorkOrderService.getWorkOrderSpareParts(vm.workOrderId).then(
                    function (data) {
                        vm.workOrderSpareParts = data;
                        vm.selectedSpareParts = [];
                        angular.forEach(vm.workOrderSpareParts, function (part) {
                            part.editMode = false;
                            part.isNew = false;
                            angular.forEach(part.serialNumbers, function (number) {
                                if (part.newSerialNumbers == null) {
                                    part.newSerialNumbers = number;
                                } else {
                                    part.newSerialNumbers = part.newSerialNumbers + ", " + number;
                                }
                            })
                        })
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function addSpareParts() {
                var options = {
                    title: addSparePartsTitle,
                    template: 'app/desktop/modules/mro/workOrder/details/tabs/sparePart/selectSparePartView.jsp',
                    controller: 'SelectSparePartController as selectSparePartsVm',
                    resolve: 'app/desktop/modules/mro/workOrder/details/tabs/sparePart/selectSparePartController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedWorkOrderId: vm.workOrderId,
                        mode: "WORKORDER"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.workorder.spareparts.add'}
                    ],
                    callback: function (result) {
                        angular.forEach(result, function (sparePart) {
                            var part = angular.copy(emptySparePart);
                            part.editMode = true;
                            part.isNew = true;
                            part.sparePart = sparePart;
                            vm.workOrderSpareParts.unshift(part);
                            vm.selectedSpareParts.unshift(part);
                        });

                    }
                };

                $rootScope.showSidePanel(options);
            }

            function saveParts() {
                if (validateParts()) {
                    WorkOrderService.createMultipleWorkOrderSpareParts(vm.workOrderId, vm.selectedSpareParts).then(
                        function (data) {
                            loadWorkOrderSpareParts();
                            $rootScope.loadWorkOrderTabCounts();
                            vm.selectedSpareParts = [];
                            $rootScope.showSuccessMessage(sparePartsAdded);
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
                angular.forEach(vm.selectedSpareParts, function (part) {
                    if (valid) {
                        part.serialNumbers = [];
                        if (part.newSerialNumbers != null && part.newSerialNumbers != "") {
                            part.serialNumbers = part.newSerialNumbers.split(",");
                        }
                        if (part.quantity == null || part.quantity == "" || part.quantity == undefined) {
                            valid = false;
                            $rootScope.showWarningMessage(pleaseEnterQty);
                        } else if (part.disposition == "REPLACE" && (part.newSerialNumbers == null || part.newSerialNumbers == "" || part.newSerialNumbers == undefined)) {
                            valid = false;
                            $rootScope.showWarningMessage(pleaseEnterSerialNumbers);
                        } else if (part.disposition == "REPLACE" && (part.quantity != part.serialNumbers.length)) {
                            valid = false;
                            $rootScope.showWarningMessage(qtyAndSerialShouldSame);
                        }
                    }
                })
                return valid;
            }

            function removeParts() {
                angular.forEach(vm.selectedSpareParts, function (item) {
                    vm.workOrderSpareParts.splice(vm.workOrderSpareParts.indexOf(item), 1);
                })
                vm.selectedSpareParts = [];
            }

            function savePart(part) {
                if (validatePart(part)) {
                    WorkOrderService.createWorkOrderSparePart(vm.workOrderId, part).then(
                        function (data) {
                            part.id = data.id;
                            part.editMode = false;
                            part.isNew = false;
                            vm.selectedSpareParts.splice(vm.selectedSpareParts.indexOf(part), 1);
                            if (vm.selectedSpareParts.length == 0) {
                                loadWorkOrderSpareParts();
                            }
                            $rootScope.loadWorkOrderTabCounts();
                            $rootScope.showSuccessMessage(sparePartAdded);
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
                part.serialNumbers = [];
                if (part.newSerialNumbers != null && part.newSerialNumbers != "") {
                    part.serialNumbers = part.newSerialNumbers.split(",");
                }
                if (part.quantity == null || part.quantity == "" || part.quantity == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterQty);
                } else if (part.disposition == "REPLACE" && (part.newSerialNumbers == null || part.newSerialNumbers == "" || part.newSerialNumbers == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterSerialNumbers);
                } else if (part.disposition == "REPLACE" && (part.quantity != part.serialNumbers.length)) {
                    valid = false;
                    $rootScope.showWarningMessage(qtyAndSerialShouldSame);
                }
                return valid;
            }

            function updatePart(part) {
                if (validatePart(part)) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    WorkOrderService.updateWorkOrderSparePart(vm.workOrderId, part).then(
                        function (data) {
                            part.editMode = false;
                            $rootScope.showSuccessMessage(sparePartUpdated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function removePart(part) {
                vm.workOrderSpareParts.splice(vm.workOrderSpareParts.indexOf(part), 1);
                vm.selectedSpareParts.splice(vm.selectedSpareParts.indexOf(part), 1);
            }

            function deletePart(part) {
                var options = {
                    title: removeSparePartTitle,
                    message: removeSparePartMsg,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            WorkOrderService.deleteWorkOrderSparePart(vm.workOrderId, part.id).then(
                                function (data) {
                                    loadWorkOrderSpareParts();
                                    $rootScope.loadWorkOrderTabCounts();
                                    $rootScope.showSuccessMessage(sparePartRemoved);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                )
            }

            function editPart(part) {
                part.oldNotes = part.notes;
                part.oldPartDispositionType = part.partDispositionType;
                part.oldQuantity = part.quantity;
                part.oldSerialNumbers = part.serialNumbers;
                part.editMode = true;
            }

            function cancelChanges(part) {
                part.notes = part.oldNotes;
                part.partDispositionType = part.oldPartDispositionType;
                part.quantity = part.oldQuantity;
                part.serialNumbers = part.oldSerialNumbers;
                part.editMode = false;
            }

            function showSparePart(part) {
                $state.go('app.mro.sparePart.details', {sparePartId: part.sparePart.id, tab: 'details.basic'});
            }

            (function () {
                $scope.$on('app.workOrder.tabActivated', function (event, data) {
                    if (data.tabId == 'details.spareParts') {
                        loadWorkOrderSpareParts();
                    }
                })
            })();
        }
    }
);
