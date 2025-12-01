define(['app/desktop/modules/im/im.module',
        'app/shared/services/core/storeService',
        'app/desktop/modules/im/stores/stock/received/stockReceivedDialogController',
        'app/shared/services/tm/taskService',
        'app/shared/services/pm/project/projectSiteService'

    ],
    function (module) {
        module.controller('StockReceivedController', StockReceivedController);

        function StockReceivedController($scope, $rootScope, $timeout, $window, $state, $cookies, $stateParams, TaskService, StoreService, ProjectSiteService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-shopping-cart";
            $rootScope.viewInfo.title = "Stock Details";

            vm.select = select;
            vm.back = back;
            vm.update = update;
            vm.storeId = $stateParams.storeId;

            vm.store = null;
            vm.siteStores = null;
            vm.taskList = null;
            vm.taskflag = false;

            vm.selectedItems = [];
            vm.tasks = [];

            function back() {
                $window.history.back();
            }

            function loadStore() {
                StoreService.getStore($stateParams.projectId, $stateParams.storeId).then(
                    function (data) {
                        vm.store = data;
                        $rootScope.viewInfo.title = "Store Details - " + vm.store.name + " ( Receiving Items )";
                    }
                )
            }

            function validateQuantity(item) {
                var valid = false;
                if (item.balanceQty >= item.Qty) {
                    valid = true;
                }
                return valid;
            }

            function update() {
                var stock = [];
                angular.forEach(vm.selectedItems, function (item) {
                    if (item.Qty > 0) {
                        if (validateQuantity(item)) {
                            stock.push({
                                type: 'PURCHASE',
                                issuedTo: window.$application.login.person.id,
                                receivedBy: window.$application.login.person.id,
                                boqItem: item.id,
                                project: $stateParams.projectId,
                                store: $stateParams.storeId,
                                movementType: "RECEIVED",
                                quantity: item.Qty,
                                recordedBy: window.$application.login.person.id,
                                notes: item.notes
                            })
                        } else {
                            item.Qty = 0;
                            if (item.itemType == "MATERIAL") {
                                $rootScope.showErrorMessage("Material quantity cannot be greater than Balance quantity");
                            } else {
                                $rootScope.showErrorMessage("Machine quantity cannot be greater than Balance quantity");

                            }
                        }
                    } else {
                        item.Qty = 0;
                        $rootScope.showErrorMessage("Please enter positive value")
                    }

                });
                if (stock.length > 0) {
                    StoreService.updateStock($stateParams.projectId, stock, $stateParams.storeId, 'stocksReceived').then(
                        function (data) {
                            if (data.length > 0) {
                                $rootScope.showSuccessMessage("Item(s) received successfully");
                            } else {
                                $rootScope.showSuccessMessage("Item received successfully");
                            }
                            $timeout(function () {
                                $state.go('app.pm.project.stores.details', {
                                    storeId: $stateParams.storeId,
                                    mode: 'RECEIVED'
                                });
                            }, 2000)
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );

                }
            }

            function select() {
                var options = {
                    title: 'Select Item(s)',
                    showMask: true,
                    template: 'app/desktop/modules/im/stores/stock/received/stockReceivedDialog.jsp',
                    controller: 'StockReceivedDialogController as stockReceivedVm',
                    resolve: 'app/desktop/modules/im/stores/stock/received/stockReceivedDialogController',
                    width: 750,
                    data: {},
                    buttons: [
                        {text: 'Select', broadcast: 'app.item.received'}
                    ],
                    callback: function (data) {
                        vm.selectedItems = data;
                    }
                };

                $rootScope.showSidePanel(options);
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadStore();
                }
            })();
        }
    }
)
;