/**
 * Created by swapna on 19/09/18.
 */
define(
    [
        'app/desktop/modules/stores/store.module',
        'app/desktop/modules/stores/details/tabs/indents/details/tabs/basic/indentBasicDetailsController',
        'app/desktop/modules/stores/details/tabs/indents/details/tabs/items/indentRequisitionItemsController',
        'app/shared/services/store/customIndentService',
        'app/shared/services/store/topStoreService'
    ],
    function (module) {
        module.controller('IndentDetailsController', IndentDetailsController);

        function IndentDetailsController($scope, $rootScope, $application, $timeout, $state, $http, TopStoreService, $stateParams, CustomIndentService) {

            $rootScope.viewInfo.icon = "fa fa-indent";
            $rootScope.viewInfo.title = "Indent Details";

            var vm = this;

            vm.addItems = addItems;
            vm.nextPage = nextPage;
            vm.printIndentChallan = printIndentChallan;
            vm.previousPage = previousPage;
            vm.updateIndent = updateIndent;
            vm.approveIndent = approveIndent;

            vm.back = back;
            vm.detailsTabActivated = detailsTabActivated;
            vm.loading = true;
            vm.activeTab = 0;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/stores/details/tabs/indents/details/tabs/basic/indentBasicDetailsView.jsp',
                    active: true,
                    activated: true,
                    index: 0
                },
                items: {
                    id: 'details.items',
                    heading: 'Items',
                    template: 'app/desktop/modules/stores/details/tabs/indents/details/tabs/items/indentRequisitionItemsView.jsp',
                    active: false,
                    activated: false,
                    index: 1
                }
            };

            function addItems() {
                $scope.$broadcast('app.indent.addItems', {indent: $rootScope.indent});
            }

            function approveIndent() {
                $scope.$broadcast('app.indent.approve', {indent: $rootScope.indent});
            }

            function updateIndent() {
                $scope.$broadcast('app.indent.update', {indent: $rootScope.indent});
            }

            function nextPage() {
                if (vm.tabActive == 2) {
                    $scope.$broadcast('app.indent.items.nextPageDetails');
                } else if (vm.tabActive == 3) {
                    $scope.$broadcast('app.indent.items.nextPageDetails');
                }
            }

            function previousPage() {
                if (vm.tabActive == 2) {
                    $scope.$broadcast('app.indent.items.previousPageDetails');
                } else if (vm.tabActive == 3) {
                    $scope.$broadcast('app.indent.items.previousPageDetails');
                }
            }

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'IND'});
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            function detailsTabActivated(tabId) {
                var tab = getTabById(tabId);
                if (tab != null && !tab.activated) {
                    tab.activated = true;
                    $scope.$broadcast('app.stock.requisitionItems', {tabId: tabId});

                    if (tab == "items") {
                        vm.addItem = true;
                        vm.showApproveButton = false;
                        $scope.$broadcast('app.indents.itemsTabActivated', {tabId: tabId});
                    }
                    else {
                        vm.addItem = false;
                        vm.showApproveButton = true;
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

            function loadIndent() {
                CustomIndentService.getIndent($stateParams.indentId).then(
                    function (data) {
                        $rootScope.indent = data;
                        $rootScope.indentId = $rootScope.indent.id;
                        $rootScope.viewInfo.title = "Indent Details (" + $rootScope.indent.indentNumber + ")";
                    }
                )
            }

            function printIndentChallan() {
                TopStoreService.printIndentChallan($stateParams.indentId).then(
                    function (data) {
                        var url = "{0}//{1}/api/is/stores/file/".format(window.location.protocol, window.location.host);
                        url += data + "/download";
                        window.open(url, '_self');
                    }
                )
            }

            (function () {
                loadIndent();
            })();
        }
    }
);