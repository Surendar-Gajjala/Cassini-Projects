/**
 * Created by swapna on 12/08/18.
 */
define(
    [
        'app/desktop/modules/stores/store.module',
        'app/shared/services/store/loanService',
        'app/desktop/modules/stores/details/tabs/loanIssued/details/tabs/basic/loanIssuedBasicController',
        'app/desktop/modules/stores/details/tabs/loanIssued/details/tabs/items/loanIssuedItemsController'
    ],
    function (module) {
        module.controller('LoanDetailsController', LoanDetailsController);

        function LoanDetailsController($scope, $rootScope, $timeout, $state, $stateParams, TopStoreService, LoanService) {

            var vm = this;

            vm.stockReceive = null;
            vm.loading = true;
            vm.addItem = false;
            vm.store = null;
            vm.addItem = false;
            var valid = true;
            vm.loan = null;
            vm.activeTab = 0;
            $rootScope.selectedLoanIssuedDetailsTab = null;
            vm.addItems = addItems;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.detailsTabActivated = detailsTabActivated;
            vm.printLoanIssueChallan = printLoanIssueChallan;
            vm.back = back;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/stores/details/tabs/loanIssued/details/tabs/basic/loanIssuedBasicView.jsp',
                    active: true,
                    activated: true,
                    index: 0
                },
                items: {
                    id: 'details.items',
                    heading: 'Items',
                    template: 'app/desktop/modules/stores/details/tabs/loanIssued/details/tabs/items/loanIssuedItemsView.jsp',
                    active: false,
                    activated: false,
                    index: 1
                }
            };

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'LOANISSUE'});
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            function detailsTabActivated(tabId) {
                var tab = getTabById(tabId);
                if (tab != null && !tab.activated) {
                    $rootScope.selectedLoanIssuedDetailsTab = tabId;
                    tab.activated = true;
                    $scope.$broadcast('app.stock.loanItems', {tabId: tabId});

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
                    template: 'app/desktop/modules/stores/details/tabs/loanIssued/details/tabs/items/loanIssueItemsDialogView.jsp',
                    controller: 'LoanItemsDialogController as loanItemsDialogVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/loanIssued/details/tabs/items/loanIssueItemsDialogController',
                    width: 700,
                    data: {
                        store: $stateParams.storeId
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.stores.addLoanItems'}
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
                    if (item.Qty != undefined) {
                        if (validate(item) && valid) {
                            stock.push({
                                type: 'CONSUME',
                                issuedTo: window.$application.login.person.id,
                                receivedBy: window.$application.login.person.id,
                                movementType: "LOANISSUED",
                                quantity: item.Qty,
                                recordedBy: window.$application.login.person.id,
                                store: vm.store,
                                item: item.item,
                                loan: vm.loan.id,
                                project: vm.loan.fromProject,
                                timeStamp: moment(new Date()).format("DD/MM/YYYY, HH:mm:ss")
                            })
                        }
                    } else {
                        $rootScope.hideBusyIndicator();
                    }

                });
                if (stock.length > 0 && valid) {
                    LoanService.createLoanIssueItems(vm.loan.fromStore, vm.loan.toStore, vm.loan.toProject, stock).then(
                        function (data) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.hideSidePanel();
                            $scope.$broadcast('app.stock.loanItems');
                        },
                        function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    );

                }
            }

            function validate(item) {

                if (item.storeOnHand < item.Qty) {
                    valid = false;
                    $rootScope.showErrorMessage("Loan qty cannot be greater than Inventory qty");
                }
                else if (item.Qty <= 0) {
                    valid = false;
                    $rootScope.showErrorMessage("Please enter +ve number for qty");
                }
                return valid;
            }

            function loadLoan() {
                LoanService.getLoanById($rootScope.storeId, $stateParams.loanId).then(
                    function (data) {
                        vm.loan = data;
                    });
            }

            function loadStore() {
                TopStoreService.getTopStore($rootScope.storeId).then(
                    function (data) {
                        vm.store = data;
                        $rootScope.viewInfo.description = "Store : " + vm.store.storeName;
                    }
                )
            }

            function printLoanIssueChallan() {
                LoanService.printLoanChallan($rootScope.storeId, $stateParams.loanId).then(
                    function (data) {
                        var url = "{0}//{1}/api/is/stores/".format(window.location.protocol, window.location.host);
                        url += $rootScope.storeId + "/loans/file/" + data + "/download";

                        window.open(url, '_self');
                    }
                )
            }

            function nextPage() {
                $scope.$broadcast('app.loanIssued.items.nextPageDetails');
            }

            function previousPage() {
                $scope.$broadcast('app.loanIssued.items.previousPageDetails');
            }

            (function () {
                loadStore();
                loadLoan();
            })();
        }
    }
);