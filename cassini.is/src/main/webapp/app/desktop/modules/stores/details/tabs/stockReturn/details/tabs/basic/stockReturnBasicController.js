/**
 * Created by swapna on 05/12/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/stockReturnService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDetailsDirectiveController',
        'app/shared/services/pm/project/projectService'

    ],
    function (module) {
        module.controller('StockReturnBasicController', StockReturnBasicController);

        function StockReturnBasicController($scope, $rootScope, CommonService, $state, $stateParams, StockReturnService, ProjectService) {
            var vm = this;

            vm.stockReturn = null;
            vm.updateStockReturn = updateStockReturn;

            function loadStockReturn() {
                vm.loading = true;
                StockReturnService.getStockReturn($rootScope.storeId, $stateParams.stockReturnId).then(
                    function (data) {
                        vm.loading = false;
                        vm.stockReturn = data;
                        $rootScope.stockReturn = data;
                        $rootScope.viewInfo.title = "Stock Return : " + vm.stockReturn.returnNumberSource;
                        if (vm.stockReturn.project != null) {
                            ProjectService.getProject(vm.stockReturn.project).then(
                                function (project) {
                                    vm.stockReturn.projectName = project.name;
                                }
                            )
                        }
                        if (vm.stockReturn.returnedTo != null) {
                            CommonService.getPerson(vm.stockReturn.returnedTo).then(
                                function (person) {
                                    vm.stockReturn.returnedToPerson = person;
                                }
                            )
                        }
                    }
                )
            }

            function updateStockReturn() {
                StockReturnService.updateStockReturn($rootScope.storeId, vm.stockReturn).then(
                    function (data) {
                        $rootScope.stockReturn.status = data.status;
                        $rootScope.showSuccessMessage("Stock Return : " + vm.stockReturn.returnNumberSource + "updated successfully");
                    }
                )
            }

            function approveValidation() {
                var valid = true;
                if (vm.stockReturn.approvedBy == null || vm.stockReturn.approvedBy == "" || vm.stockReturn.approvedBy == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter ApprovedBy");
                }
                return valid;
            }

            $scope.$on('app.stockReturn.approve', function (event, data) {
                approveStockReturn();
            });

            function approveStockReturn() {
                if (approveValidation()) {
                    vm.stockReturn.status = "APPROVED";
                    StockReturnService.updateStockReturn($rootScope.storeId, vm.stockReturn).then(
                        function (data) {
                            $rootScope.stockReturn = data;
                            $rootScope.showSuccessMessage(vm.stockReturn.returnNumberSource + ": approved successfully");
                        }
                    )
                }
            }

            (function () {
                loadStockReturn();
            })();
        }
    }
)
;