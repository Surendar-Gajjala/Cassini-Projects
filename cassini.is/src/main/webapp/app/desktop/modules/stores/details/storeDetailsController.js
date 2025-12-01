define(['app/desktop/modules/stores/store.module',
        'app/desktop/modules/stores/details/tabs/basic/storeBasicController',
        'app/shared/services/store/topStoreService',
        'app/desktop/modules/stores/details/tabs/inventory/storeInventoryController',
        'app/desktop/modules/stores/details/tabs/stockMovement/stockMovementController',
        'app/desktop/modules/stores/details/tabs/receives/all/stockReceivedController',
        'app/desktop/modules/stores/details/tabs/receives/details/tabs/basic/stockReceiveBasicDetailsController',
        'app/desktop/modules/stores/details/tabs/issues/all/stockIssuesController',
        'app/desktop/modules/stores/details/tabs/loanIssued/all/loanIssuedController',
        'app/desktop/modules/stores/details/tabs/loanReceived/all/loanReceivedController',
        'app/desktop/modules/stores/details/tabs/requisitions/all/requisitionsController',
        'app/desktop/modules/stores/details/tabs/indents/all/indentsController',
        'app/desktop/modules/stores/details/tabs/purchaseOrders/all/purchaseOrdersController',
        'app/desktop/modules/stores/details/tabs/roadChallan/all/roadChallansController',
        'app/desktop/modules/stores/details/tabs/stockReturn/all/allStockReturnsController',
        'app/desktop/modules/stores/details/tabs/issues/directive/issueTypeDirective'

    ],
    function (module) {
        module.controller('StoreDetailController', StoreDetailController);

        function StoreDetailController($scope, $rootScope, $timeout, $state, $cookies, $stateParams, TopStoreService) {

            var vm = this;
            $rootScope.viewInfo.title = "Store Details ";
            $rootScope.viewInfo.icon = "fa fa-shopping-cart";
            $rootScope.title = "";
            vm.activeTab = 0;
            vm.tabActive = 0;
            vm.materialReceiveType = null;
            vm.materialIssueType = null;
            vm.storeDetailsTabActivated = storeDetailsTabActivated;
            vm.updateStore = updateStore;
            vm.stockIssued = stockIssued;
            vm.stockReceived = stockReceived;
            vm.back = back;
            vm.onSearch = onSearch;
            vm.clear = clear;
            vm.searchTerm = null;
            vm.showReceiveBsic = false;
            if ($stateParams.storeId == null || $stateParams.storeId == "") {
                vm.storeId = $rootScope.selectedStore;
            }
            vm.storeId = $stateParams.storeId;
            vm.newIssue = newIssue;
            vm.newLoan = newLoan;
            vm.newRequest = newRequest;
            vm.newIndent = newIndent;
            vm.newPurchaseOrder = newPurchaseOrder;
            vm.newRoadChallan = newRoadChallan;
            vm.newReceiveChallan = newReceiveChallan;
            vm.newIssueChallan = newIssueChallan;
            vm.showLoanIssuedAttributes = showLoanIssuedAttributes;
            vm.showLoanReceivedAttributes = showLoanReceivedAttributes;
            vm.showRoadChallanAttributes = showRoadChallanAttributes;
            vm.allocateItems = allocateItems;
            vm.showInventoryAttributes = showInventoryAttributes;
            vm.loanAttributes = [];
            vm.showIndentAttributes = showIndentAttributes;
            vm.showPurchaseOrderAttributes = showPurchaseOrderAttributes;
            vm.showRequisitionAttributes = showRequisitionAttributes;
            vm.showReceiveAttributes = showReceiveAttributes;
            vm.showIssueAttributes = showIssueAttributes;
            vm.newStockReturn = newStockReturn;
            vm.showStockReturnAttributes = showStockReturnAttributes;
            vm.showStockMovementAttributes = showStockMovementAttributes;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            $rootScope.selectedStoreDetailsTab = null;

            function onSearch() {
                if (vm.activeTab == 1) {
                    $scope.$broadcast('app.stores.inventory.freeText', {search: vm.searchTerm});
                } else if (vm.activeTab == 2) {
                    $scope.$broadcast('app.stores.movement.freeText', {search: vm.searchTerm});
                } else if (vm.activeTab == 3) {
                    $scope.$broadcast('app.stores.received.freeText', {search: vm.searchTerm});
                } else if (vm.activeTab == 4) {
                    $scope.$broadcast('app.stores.issued.freeText', {search: vm.searchTerm});
                }
            }

            function clear() {
                vm.searchTerm = null;
                if (vm.activeTab == 1) {
                    $scope.$broadcast('app.stores.inventory.reset', {search: vm.searchTerm});
                } else if (vm.activeTab == 2) {
                    $scope.$broadcast('app.stores.movement.reset', {search: vm.searchTerm});
                } else if (vm.activeTab == 3) {
                    $scope.$broadcast('app.stores.received.reset', {search: vm.searchTerm});
                } else if (vm.activeTab == 4) {
                    $scope.$broadcast('app.stores.issued.reset', {search: vm.searchTerm});
                }
            }

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/stores/details/tabs/basic/storeBasicView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                inventory: {
                    id: 'details.inventory',
                    heading: 'Inventory',
                    template: 'app/desktop/modules/stores/details/tabs/inventory/storeInventoryView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                stockMovement: {
                    id: 'details.stockMovement',
                    heading: 'Stock Movement',
                    template: 'app/desktop/modules/stores/details/tabs/stockMovement/stockMovementView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                requests: {
                    id: 'details.requests',
                    heading: 'Requisitions',
                    template: 'app/desktop/modules/stores/details/tabs/requisitions/all/requisitionsView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                indent: {
                    id: 'details.indent',
                    heading: 'Indents',
                    template: 'app/desktop/modules/stores/details/tabs/indents/all/indentsView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                },
                purchaseOrders: {
                    id: 'details.purchaseOrders',
                    heading: 'Purchase Orders',
                    template: 'app/desktop/modules/stores/details/tabs/purchaseOrders/all/purchaseOrdersView.jsp',
                    index: 5,
                    active: false,
                    activated: false
                },
                roadChallan: {
                    id: 'details.roadChallan',
                    heading: 'Road Challans',
                    template: 'app/desktop/modules/stores/details/tabs/roadChallan/all/roadChallansView.jsp',
                    index: 6,
                    active: false,
                    activated: false
                },
                stockReceived: {
                    id: 'details.stockReceives',
                    heading: 'Receives',
                    template: 'app/desktop/modules/stores/details/tabs/receives/all/stockReceivedView.jsp',
                    index: 7,
                    active: false,
                    activated: false
                },
                stockIssued: {
                    id: 'details.stockIssues',
                    heading: 'Issues',
                    template: 'app/desktop/modules/stores/details/tabs/issues/all/stockIssuesView.jsp',
                    index: 6,
                    active: false,
                    activated: false
                },
                loanIssued: {
                    id: 'details.loanIssued',
                    heading: 'Loan Issued',
                    template: 'app/desktop/modules/stores/details/tabs/loanIssued/all/loansIssuedView.jsp',
                    index: 9,
                    active: false,
                    activated: false
                },
                loanReceived: {
                    id: 'details.loanReceived',
                    heading: 'Loan Received',
                    template: 'app/desktop/modules/stores/details/tabs/loanReceived/all/loanReceivedView.jsp',
                    index: 10,
                    active: false,
                    activated: false
                },
                stockReturn: {
                    id: 'details.stockReturn',
                    heading: 'Stock Returns',
                    template: 'app/desktop/modules/stores/details/tabs/stockReturn/all/allStockReturnsView.jsp',
                    index: 11,
                    active: false,
                    activated: false
                }
            };

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t)) {
                        vm.tabs[t].active = (t != undefined && t == tab);
                    }
                }
            }

            function storeDetailsTabActivated(tabId) {
                $scope.$broadcast('app.store.tabactivated', {tabId: tabId});
                $rootScope.selectedStoreDetailsTab = tabId;
                var tab = getTabById(tabId);
                if (tab != null) {

                    if (tab == 'basic') {
                        $rootScope.viewInfo.title = "Store Details";
                    }
                    else if (tab == 'inventory') {
                        $rootScope.viewInfo.title = "Inventory";
                    }
                    else if (tab == 'stockMovement') {
                        $rootScope.viewInfo.title = "Stock Movements";
                    }
                    else if (tab == 'requests') {
                        $rootScope.viewInfo.title = "Requisitions";
                    }
                    else if (tab == 'stockReceived') {
                        vm.materialReceiveType = null;
                        $rootScope.viewInfo.title = "Receives";
                    }
                    else if (tab == 'stockIssued') {
                        vm.materialIssueType = null;
                        $rootScope.viewInfo.title = "Issues";
                    }
                    else if (tab == 'indent') {
                        $rootScope.viewInfo.title = "Indents";
                    }
                    else if (tab == 'purchaseOrders') {
                        $rootScope.viewInfo.title = "Purchase Orders";
                    }
                    else if (tab == 'roadChallan') {
                        $rootScope.viewInfo.title = "Road Challans";
                    }
                    else if (tab == 'loanIssued') {
                        $rootScope.viewInfo.title = "Loan Issues";
                    }
                    else if (tab == 'loanReceived') {
                        $rootScope.viewInfo.title = "Loan Receives";
                    }
                    else if (tab == 'stockReturn') {
                        $rootScope.viewInfo.title = "Stock Returns";
                    }
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

            function back() {
                if (vm.tabActive > 0) {
                    vm.tabActive = 0;
                }
                else if (vm.tabActive == 0) {
                    $state.go('app.store.all');
                }
            }

            function updateStore() {
                $scope.$broadcast('app.Store.update');
            }

            function stockIssued() {
                $state.go('app.store.stock.issued', {
                    storeId: $stateParams.storeId
                });
            }

            function stockReceived() {
                $state.go('app.store.newStockReceive', {storeId: $rootScope.storeId});
            }

            function newLoan() {
                $state.go('app.store.newLoan', {storeId: $rootScope.storeId});
            }

            function newScrap() {
                $state.go('app.store.newScrap');
            }

            function newIssue() {
                $state.go('app.store.newStockIssue', {storeId: $rootScope.storeId})
            }

            function newRequest() {
                $state.go('app.store.newRequisition', {storeId: $rootScope.storeId});
            }

            function newIndent() {
                $state.go('app.store.indent', {storeId: $stateParams.storeId, newIndent: 'newIndent'});
            }

            function newPurchaseOrder() {
                $state.go("app.store.purchaseOrder", {
                    storeId: $stateParams.storeId,
                    purchaseOrders: 'purchaseOrders',
                    purchaseOrderId: null,
                    mode: 'new'
                });
            }

            function newRoadChallan() {
                $state.go('app.store.newRoadChallan', {storeId: $rootScope.storeId});
            }

            function newReceiveChallan() {
                var options = {
                    title: 'New ReceiveChallan',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/receiveChallans/new/newReceiveChallanDialogView.jsp',
                    controller: 'NewReceiveChallanController as newReceiveChallanVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/receiveChallans/new/newReceiveChallanDialogController.js',
                    width: 500,
                    data: {},
                    buttons: [
                        {text: 'Add', broadcast: 'app.storeItems.newReceiveChallan'}
                    ]
                };

                $rootScope.showSidePanel(options);
            }

            function newIssueChallan() {
                var options = {
                    title: 'New IssueChallan',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/issueChallans/new/newIssueChallanDialogView.jsp',
                    controller: 'NewIssueChallanController as newIssueChallanVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/issueChallans/new/newIssueChallanDialogController.js',
                    width: 500,
                    data: {},
                    buttons: [
                        {text: 'Add', broadcast: 'app.storeItems.newIssueChallan'}
                    ]
                };

                $rootScope.showSidePanel(options);
            }

            function newStockReturn() {
                $state.go('app.store.newStockReturn', {storeId: $rootScope.storeId});
            }

            function nextPage() {
                if (vm.tabActive == 1) {
                    $scope.$broadcast('app.stores.inventory.nextPageDetails');
                } else if (vm.tabActive == 6) {
                    $scope.$broadcast('app.stores.movement.nextPageDetails');
                } else if (vm.tabActive == 7) {
                    $scope.$broadcast('app.stores.requisitions.nextPageDetails');
                } else if (vm.tabActive == 8) {
                    $scope.$broadcast('app.stores.indents.nextPageDetails');
                } else if (vm.tabActive == 9) {
                    $scope.$broadcast('app.stores.purchaseOrders.nextPageDetails');
                } else if (vm.tabActive == 10) {
                    $scope.$broadcast('app.stores.roadChallans.nextPageDetails');
                } else if (vm.tabActive == 2) {
                    $scope.$broadcast('app.stores.receives.nextPageDetails');
                } else if (vm.tabActive == 3) {
                    $scope.$broadcast('app.stores.issues.nextPageDetails');
                } else if (vm.tabActive == 5) {
                    $scope.$broadcast('app.stores.loanIssue.nextPageDetails');
                } else if (vm.tabActive == 4) {
                    $scope.$broadcast('app.stores.loanReceive.nextPageDetails');
                } else if (vm.tabActive == 11) {
                    $scope.$broadcast('app.stores.stockReturns.nextPageDetails');
                }
            }

            function previousPage() {
                if (vm.tabActive == 1) {
                    $scope.$broadcast('app.stores.inventory.previousPageDetails');
                } else if (vm.tabActive == 6) {
                    $scope.$broadcast('app.stores.movement.previousPageDetails');
                } else if (vm.tabActive == 7) {
                    $scope.$broadcast('app.stores.requisitions.previousPageDetails');
                } else if (vm.tabActive == 8) {
                    $scope.$broadcast('app.stores.indents.previousPageDetails');
                } else if (vm.tabActive == 9) {
                    $scope.$broadcast('app.stores.purchaseOrders.previousPageDetails');
                } else if (vm.tabActive == 10) {
                    $scope.$broadcast('app.stores.roadChallans.previousPageDetails');
                } else if (vm.tabActive == 2) {
                    $scope.$broadcast('app.stores.receives.previousPageDetails');
                } else if (vm.tabActive == 3) {
                    $scope.$broadcast('app.stores.issues.previousPageDetails');
                } else if (vm.tabActive == 5) {
                    $scope.$broadcast('app.stores.loanIssue.previousPageDetails');
                } else if (vm.tabActive == 4) {
                    $scope.$broadcast('app.stores.loanReceive.previousPageDetails');
                } else if (vm.tabActive == 11) {
                    $scope.$broadcast('app.stores.stockReturns.previousPageDetails');
                }
            }

            function showLoanIssuedAttributes() {
                $scope.$broadcast('app.Store.loan.attributes');
            }

            function showLoanReceivedAttributes() {
                $scope.$broadcast('app.Store.loanReceived.attributes');
            }

            function showRoadChallanAttributes() {
                $scope.$broadcast('app.Store.roadChallan.attributes');
            }

            function showIndentAttributes() {
                $scope.$broadcast('app.Store.indent.attributes');
            }

            function showPurchaseOrderAttributes() {
                $scope.$broadcast('app.Store.purchaseOrder.attributes');
            }

            function showRequisitionAttributes() {
                $scope.$broadcast('app.Store.requisition.attributes');
            }

            function showReceiveAttributes() {
                $scope.$broadcast('app.Store.receive.attributes');
            }

            function showIssueAttributes() {
                $scope.$broadcast('app.Store.issue.attributes');
            }

            function showStockReturnAttributes() {
                $scope.$broadcast('app.Store.stockReturn.attributes');
            }

            function showInventoryAttributes() {
                $scope.$broadcast('app.store.inventory.attributes');
            }

            function showStockMovementAttributes() {
                $scope.$broadcast('app.store.stockMovement.attributes');
            }

            function allocateItems() {
                var options = {
                    title: 'Allocate Items',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/inventory/allocateItemsToProjectDialogView.jsp',
                    controller: 'AllocateItemsDialogController as allocateItemsVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/inventory/allocateItemsToProjectDialogController.js',
                    width: 700,
                    data: {},
                    buttons: [
                        {text: 'Add', broadcast: 'app.storeItems.allocateItems'}
                    ]
                };

                $rootScope.showSidePanel(options);
            }

            function loadStore() {
                TopStoreService.getTopStore($stateParams.storeId).then(
                    function (data) {
                        vm.store = data;
                        $rootScope.title = vm.store.storeName;
                        $rootScope.selectedStore = data;
                        if ($stateParams.mode == null) {
                            $rootScope.viewInfo.title = "Store Details : " + vm.store.storeName;
                        }
                        $rootScope.viewInfo.description = "Location : " + vm.store.locationName;
                    }
                )
            }

            (function () {
                loadStore();
                if ($application.homeLoaded == true) {
                    $rootScope.showComments('STORE', $stateParams.storeId);
                    if ($stateParams.mode == 'REQ') {
                        vm.tabActive = 7;
                    }
                    else if ($stateParams.mode == 'IND') {
                        vm.tabActive = 8;
                    }
                    else if ($stateParams.mode == 'PO') {
                        vm.tabActive = 9;
                    }
                    else if ($stateParams.mode == 'RC') {
                        vm.tabActive = 10;
                    }
                    else if ($stateParams.mode == 'RECEIVE') {
                        vm.tabActive = 2;
                    }
                    else if ($stateParams.mode == 'ISSUE') {
                        vm.tabActive = 3;
                    }
                    else if ($stateParams.mode == 'LOANISSUE') {
                        vm.tabActive = 5;
                    }
                    else if ($stateParams.mode == 'LOANRECEIVE') {
                        vm.tabActive = 4;
                    }
                    else if ($stateParams.mode == 'RET') {
                        vm.tabActive = 11;
                    }
                    else {
                        vm.tabActive = 0;
                    }
                }
            })();
        }
    }
);