define(
    [
        'app/desktop/modules/stores/store.module',
        'app/shared/services/store/topStockReceivedService',
        'app/desktop/modules/stores/details/tabs/issues/details/tabs/basic/stockIssueBasicController',
        'app/desktop/modules/stores/details/tabs/issues/details/tabs/items/stockIssuedController',
        'app/desktop/modules/stores/details/tabs/issues/all/stockIssuesController',
        'app/shared/services/store/topStoreService'
    ],
    function (module) {
        module.controller('StockIssueDetailsController', StockIssueDetailsController);

        function StockIssueDetailsController($scope, $rootScope, $timeout, $state, $stateParams, TopStoreService) {

            var vm = this;

            vm.back = back;
            vm.detailsTabActivated = detailsTabActivated;
            vm.printIssuChallan = printIssuChallan;
            vm.showAttributes = showAttributes;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.stockReceive = null;
            vm.loading = true;
            vm.addItem = false;
            vm.addItems = addItems;
            vm.store = null;
            vm.activeTab = 0;
            var valid = true;
            $rootScope.selectedstockIssueDetailsTab = null;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/stores/details/tabs/issues/details/tabs/basic/stockIssueBasicView.jsp',
                    active: true,
                    activated: true,
                    index: 0
                },
                items: {
                    id: 'details.items',
                    heading: 'Items',
                    template: 'app/desktop/modules/stores/details/tabs/issues/details/tabs/items/stockIssuedView.jsp',
                    active: false,
                    activated: false,
                    index: 1
                }
            };

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'ISSUE'});
            }

            function showAttributes() {
                $scope.$broadcast('app.issue.items.attributes');
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            function detailsTabActivated(tabId) {
                var tab = getTabById(tabId);
                $rootScope.selectedstockIssueDetailsTab = tabId;
                if (tab != null && !tab.activated) {
                    tab.activated = true;
                    $rootScope.$broadcast('app.stock.issueItems', {tabId: tabId});

                    if (tab == "items") {
                        vm.addItem = true;
                    }
                    else {
                        vm.addItem = false;
                    }
                }
                if (tab != null) {
                    activateTab(tab);
                }
            }

            function getTabById(tabId) {
                var tab = null;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t) && vm.tabs[t].id == tabId) {
                        tab = t;
                    }
                }

                return tab;
            }

            function addItems() {
                var options = {
                    title: 'Select Item(s)',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/issues/details/tabs/items/stockIssuedDialog.jsp',
                    controller: 'StoreStockIssuedDialogController as stockIssuedDialogVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/issues/details/tabs/items/stockIssuedDialogController',
                    width: 900,
                    data: {
                        store: $stateParams.storeId
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.stores.issued'}
                    ],
                    callback: function (data) {
                        vm.selectedItems = data;
                        angular.forEach(vm.selectedItems, function (selected) {
                            selected.properties = [];
                            selected.properties = angular.copy(vm.issueItemsProperties);
                            angular.forEach(selected.properties, function (prop) {
                                prop.value.id.objectId = selected.referenceId;
                            });
                        });
                        update();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function update() {
                var stock = [];
                valid = true;
                angular.forEach(vm.selectedItems, function (item) {
                    if (valid) {
                        if (item.Qty != undefined && validateQuantity(item)) {
                            stock.push({
                                type: 'CONSUME',
                                issuedTo: window.$application.login.person.id,
                                receivedBy: window.$application.login.person.id,
                                movementType: "ISSUED",
                                quantity: item.Qty,
                                recordedBy: window.$application.login.person.id,
                                issue: item.issue,
                                store: vm.store,
                                project: item.project,
                                task: item.task,
                                item: item.id,
                                boqReference: item.boqReference

                            })
                        }
                    }

                });
                if (stock.length > 0 && valid) {
                    TopStoreService.updateTopStock(stock, $rootScope.storeId, 'stocksIssued').then(
                        function (data) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.hideSidePanel();
                            $rootScope.showSuccessMessage("Items issued successfully");
                            $scope.$broadcast('app.stock.issueItems', {items: vm.selectedItems});
                        },
                        function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    );

                }
            }

            function validateQuantity(item) {

                if (item.Qty <= 0) {
                    valid = false;
                    $rootScope.hideBusyIndicator();
                    $rootScope.showErrorMessage("Issuing qty cannot be less than 1");
                }

                if (item.Qty > item.storeInventory) {
                    valid = false;
                    $rootScope.hideBusyIndicator();
                    $rootScope.showErrorMessage("Issuing qty cannot be greater than Inventory qty");
                }
                else if ((item.Qty + item.itemIssueQuantity) > item.resourceQuantity) {
                    valid = false;
                    $rootScope.hideBusyIndicator();
                    $rootScope.showErrorMessage("Total Issued qty cannot be greater than Resource qty");
                }
                return valid;
            }

            function loadStore() {
                TopStoreService.getTopStore($rootScope.storeId).then(
                    function (data) {
                        vm.store = data;
                        $rootScope.viewInfo.description = "Store : " + vm.store.storeName;
                    }
                )
            }

            function printIssuChallan() {
                TopStoreService.printIssueChallan($stateParams.issueId).then(
                    function (data) {
                        var url = "{0}//{1}/api/is/stores/file/".format(window.location.protocol, window.location.host);
                        url += data + "/download";
                        window.open(url, '_self');
                    }
                )
            }

            function nextPage() {
                $scope.$broadcast('app.stores.issueItems.nextPageDetails');
            }

            function previousPage() {
                $scope.$broadcast('app.stores.issueItems.previousPageDetails');
            }

            (function () {
                loadStore();
            })();
        }
    }
);