define(['app/desktop/modules/im/im.module',
        'app/shared/services/core/storeService',
        'app/desktop/modules/im/stores/stock/issued/stockIssuedDialogController',
        'app/shared/services/tm/taskService',
        'app/shared/services/pm/project/bomService',
        'app/shared/services/pm/project/projectSiteService'

    ],
    function (module) {
        module.controller('StockIssuedController', StockIssuedController);

        function StockIssuedController($scope, $rootScope, $timeout, $window, $state, $cookies, $stateParams, BomService, TaskService, StoreService, ProjectSiteService) {
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
                        $rootScope.viewInfo.title = "Store Details - " + vm.store.name + " ( Issuing Items )";
                    }
                )
            }

            function update() {
                var stock = [];
                angular.forEach(vm.selectedItems, function (item) {
                    if (item.Qty > 0) {
                        stock.push({
                            type: 'CONSUME',
                            issuedTo: window.$application.login.person.id,
                            receivedBy: window.$application.login.person.id,
                            boqItem: item.referenceIdObject.id,
                            project: $stateParams.projectId,
                            store: $stateParams.storeId,
                            movementType: "ISSUED",
                            quantity: item.Qty,
                            recordedBy: window.$application.login.person.id,
                            notes: item.notes
                        })
                    } else {
                        item.Qty = 0;
                        $rootScope.showErrorMessage("Please enter positive value");
                    }

                });
                if (stock.length > 0) {
                    StoreService.updateStock($stateParams.projectId, stock, $stateParams.storeId, 'stocksIssued').then(
                        function (data) {
                            if (data.length > 0) {
                                $rootScope.showSuccessMessage("Item(s) issued successfully");
                            } else {
                                $rootScope.showSuccessMessage("Item issued successfully");
                            }
                            vm.taskflag = false;
                            loadStore();
                            $timeout(function () {
                                $state.go('app.pm.project.stores.details', {
                                    storeId: $stateParams.storeId,
                                    mode: 'ISSUED'
                                });
                            }, 2000);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        });
                }
            }

            function select() {
                var options = {
                    title: 'Select Item(s)',
                    showMask: true,
                    template: 'app/desktop/modules/im/stores/stock/issued/stockIssuedDialog.jsp',
                    controller: 'StockIssuedDialogController as stockIssuedVm',
                    resolve: 'app/desktop/modules/im/stores/stock/issued/stockIssuedDialogController',
                    width: 700,
                    data: {
                        store: $stateParams.storeId,
                        project: $stateParams.projectId
                    },
                    buttons: [
                        {text: 'Select', broadcast: 'app.item.issued'}
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