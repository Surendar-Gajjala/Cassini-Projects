define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/itemService',
        'app/shared/services/pm/project/bomService'
    ],
    function (module) {
        module.controller('CostToProjectDialogController', CostToProjectDialogController);

        function CostToProjectDialogController($scope, $rootScope, $timeout, $state, $stateParams, $uibModalInstance, BomService, $cookies, boqItem) {
            var vm = this;

            vm.cancel = cancel;
            vm.onOk = onOk;
            vm.errorMsg = "";

            vm.rows = [];
            var costRows = [];
            vm.emptyRows = [{
                id: null,
                boqItem: boqItem.boq,
                costName: null,
                cost: null
            }];
            vm.handleClick = handleClick;
            vm.clickEventsOnEmptyrow = clickEventsOnEmptyrow;
            vm.deleteRow = deleteRow;
            var selectedRow = null;

            function onOk() {
                var valid = true;
                for (var j = 0; j < vm.emptyRows.length; j++) {
                    costRows.push(vm.emptyRows[j]);
                }
                if (valid) {
                    var actualRows = [];
                    var duplicates = [];
                    var actualCost = 0;
                    var length = costRows.length;
                    for (var i = 0; i < length; i++) {
                        var row = costRows[i];
                        row.boqItem = boqItem.id;
                        if (row.costName != null && row.cost != 0) {
                            if (!isExists(actualRows, row)) {
                                if (row.cost < 0) {
                                    valid = false;
                                }
                                actualCost += row.cost;
                                actualRows.push(row);
                            } else {
                                duplicates.push(row);
                            }
                        }

                    }
                    if (actualRows.length > 0 && duplicates.length == 0) {
                        if (valid) {
                            BomService.createCostToProject($stateParams.projectId, actualRows).then(
                                function (data) {
                                    $rootScope.showSuccessMessage("Actual cost updated successfully");
                                    $uibModalInstance.close(actualCost);
                                },
                                function (error) {
                                    $rootScope.showErrorMessage("Fill the table rows properly")
                                }
                            )
                        }
                        else {
                            vm.errorMsg = "Cost cannot be less than 0";
                        }

                    } else {
                        if (duplicates.length != 0) {
                            vm.errorMsg = "Please remove duplicate Items";
                        } else {
                            actualCost = 0;
                            vm.errorMsg = "Please enter Items then click on Calculate";
                        }
                    }
                }
                vm.emptyRows = [{
                    id: null,
                    boqItem: boqItem.boq,
                    costName: null,
                    cost: null
                }];

            }

            function isExists(actualRows, row) {
                var exists = false;
                angular.forEach(actualRows, function (acRow) {
                    if (acRow.costName == row.costName && acRow.cost == row.cost) {
                        exists = true;
                    }
                });
                return exists;
            }

            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            function deleteRow() {
                $(document).bind("#menu", function (e) {
                    return false;
                });
                BomService.deleteCostToProject($stateParams.projectId, selectedRow.id).then(
                    function (data) {
                        loadcostToProject();
                        vm.errorMsg = "(" + selectedRow.costName + ")" + "Row deleted successfully";
                        $('#menu').css({
                            'left': event.clientX - 450,
                            'top': event.clientY - 30,
                            'display': 'inline',
                            "position": "absolute"
                        }).hide();
                    }
                )
            }

            function loadcostToProject() {
                BomService.getCostToProject($stateParams.projectId, boqItem.id).then(
                    function (data) {
                        vm.rows = data;
                        costRows = data;

                    });
            }

            function clickEventsOnEmptyrow(event) {
                switch (event.which) {
                    case 1:
                        $('#menu').css({
                            'left': event.clientX - 450,
                            'top': event.clientY - 30,
                            'display': 'inline',
                            "position": "absolute"
                        }).hide();
                        break;
                }
            }

            function handleClick(event, row) {

                switch (event.which) {
                    case 1:
                        $('#menu').css({
                            'left': event.clientX - 450,
                            'top': event.clientY - 30,
                            'display': 'inline',
                            "position": "absolute"
                        }).hide();
                        break;
                    case 3:
                        selectedRow = row;
                        $(document).bind("contextmenu", function (e) {
                            return false;
                        });

                        $('#hdnCR').val(event.target.id);

                        $('#menu').css({
                            'left': event.clientX - 450,
                            'top': event.clientY - 30,
                            'display': 'inline',
                            "position": "absolute"
                        }).show();
                        break;
                    default:
                        alert("Right click to remove the Cost");
                        break;
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadcostToProject();
                }
            })();
        }
    }
);