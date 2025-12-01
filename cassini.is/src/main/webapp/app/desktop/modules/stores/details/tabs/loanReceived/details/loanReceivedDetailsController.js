/**
 * Created by swapna on 12/08/18.
 */
define(
    [
        'app/desktop/modules/stores/store.module',
        'app/shared/services/store/topStoreService',
        'app/desktop/modules/stores/details/tabs/loanReceived/details/tabs/basic/loanReceivedBasicController',
        'app/desktop/modules/stores/details/tabs/loanReceived/details/tabs/items/loanReceivedItemsController',
        'app/shared/services/store/loanService'
    ],
    function (module) {
        module.controller('LoanReceivedDetailsController', LoanReceivedDetailsController);

        function LoanReceivedDetailsController($scope, $rootScope, $timeout, $state, $stateParams, ItemService, TopStoreService, LoanService) {

            var vm = this;

            vm.back = back;
            vm.detailsTabActivated = detailsTabActivated;
            vm.printLoanReceiveChallan = printLoanReceiveChallan;
            vm.stockReceive = null;
            vm.loading = true;
            vm.return = false;
            vm.returnItems = returnItems;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.store = null;
            vm.activeTab = 0;
            $rootScope.selectedLoanReceiveDetailsTab = null;

            vm.tabs = {
                basic: {
                    id: 'received.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/stores/details/tabs/loanReceived/details/tabs/basic/loanReceivedBasicView.jsp',
                    active: true,
                    activated: true,
                    index: 0
                },
                items: {
                    id: 'received.items',
                    heading: 'Items',
                    template: 'app/desktop/modules/stores/details/tabs/loanReceived/details/tabs/items/loanReceivedItemsView.jsp',
                    active: false,
                    activated: false,
                    index: 1
                }
            };

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'LOANRECEIVE'});
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            function detailsTabActivated(tabId) {
                var tab = getTabById(tabId);
                $rootScope.selectedLoanReceiveDetailsTab = tabId;
                if (tab != null && !tab.activated) {
                    tab.activated = true;
                    $scope.$broadcast('app.stock.loanReceivedItems', {tabId: tabId});

                    if (tab == "items") {
                        vm.return = true;
                    }
                    else {
                        vm.return = false;
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

            function returnItems() {
                vm.selectedItems = [];
                var options = {
                    title: 'Select Item(s)',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/loanReceived/details/tabs/items/loanReturnItemsDialogView.jsp',
                    controller: 'LoanReturnItemsDialogController as returnItemsDialogVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/loanReceived/details/tabs/items/loanReturnItemsDialogController',
                    width: 800,
                    buttons: [
                        {text: 'Return', broadcast: 'app.stores.return'}
                    ],
                    callback: function (data) {
                        vm.selectedItems = data;
                        update();
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function update() {
                var stock = [];
                vm.hasError = false;
                angular.forEach(vm.selectedItems, function (item) {
                    if (item.Qty > 0) {
                        if (validateQuantity(item)) {
                            stock.push({
                                movementType: "LOANRETURNITEMISSUED",
                                quantity: item.Qty,
                                recordedBy: window.$application.login.person.id,
                                store: vm.store,
                                project: item.project,
                                item: item.id,
                                loan: $stateParams.loanId,
                                timeStamp: moment(new Date()).format("DD/MM/YYYY, HH:mm:ss"),
                                date: moment(new Date()).format("DD/MM/YYYY, HH:mm:ss")

                            })
                        }
                    }
                });

                if (!vm.hasError) {
                    if (stock.length > 0) {
                        LoanService.createLoanReturnItems($rootScope.storeId, $stateParams.loanId, stock).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Items returned successfully");
                                $rootScope.hideSidePanel('right');
                                $scope.$broadcast('app.stock.loanReceivedItems');
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );

                    } else {
                        $rootScope.showWarningMessage("Please enter return qty");
                    }
                }
            }

            function validateQuantity(item) {
                var valid = true;

                if (item.Qty > item.balancedQuantity) {
                    valid = false;
                    vm.hasError = true;
                    $rootScope.showErrorMessage("Return qty cannot be greater than Loan Balance qty");
                }
                else if (item.Qty <= 0) {
                    valid = false;
                    vm.hasError = true;
                    $rootScope.showErrorMessage("Please enter +ve number for qty");
                }
                else if (item.Qty > item.storeInventory) {
                    valid = false;
                    vm.hasError = true;
                    $rootScope.showErrorMessage("Return qty cannot be greater than Store Inventory");
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

            function loanLoan() {
                LoanService.getLoanById($rootScope.storeId, $stateParams.loanId).then(
                    function (data) {
                        vm.loan = data;
                    })
            }

            function printLoanReceiveChallan() {
                LoanService.printLoanChallan($rootScope.storeId, $stateParams.loanId).then(
                    function (data) {
                        var url = "{0}//{1}/api/is/stores/".format(window.location.protocol, window.location.host);
                        url += $rootScope.storeId + "/loans/file/" + data + "/download";

                        window.open(url, '_self');
                    }
                )
            }

            function nextPage() {
                $scope.$broadcast('app.loanReceived.items.nextPageDetails');
            }

            function previousPage() {
                $scope.$broadcast('app.loanReceived.items.previousPageDetails');
            }

            (function () {
                loadStore();
                loanLoan();
            })();
        }
    }
);