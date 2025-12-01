define(
    [
        'app/desktop/modules/stores/store.module',
        'app/shared/services/store/scrapService',
        'app/shared/services/store/scrapItemService',
        'app/desktop/modules/stores/scrapRequest/tabs/basic/scrapBasicController',
        'app/desktop/modules/stores/scrapRequest/tabs/items/scrapItemController',
        'app/shared/services/store/topStoreService'
    ],
    function (module) {
        module.controller('ScrapDetailsController', ScrapDetailsController);

        function ScrapDetailsController($scope, $rootScope, $timeout, $state, $stateParams, ScrapService, ScrapItemService, TopStoreService) {

            var vm = this;

            vm.back = back;
            vm.detailsTabActivated = detailsTabActivated;
            vm.stockReceive = null;
            vm.loading = true;
            vm.addItem = false;
            vm.addItems = addItems;
            vm.store = null;
            vm.activeTab = 0;

            vm.empty = {
                quantity: null,
                itemNumber: null,
                scrapRequest: null
            }

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/stores/scrapRequest/tabs/basic/scrapBasicView.jsp',
                    active: true,
                    activated: true,
                    index: 0
                },
                items: {
                    id: 'details.items',
                    heading: 'Items',
                    template: 'app/desktop/modules/stores/scrapRequest/tabs/items/scrapItemView.jsp',
                    active: false,
                    activated: false,
                    index: 1
                }
            };

            function back() {
                window.history.back();
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
                    $scope.$broadcast('app.store.scrapDetails', {tabId: tabId});

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
                    title: 'Add Items',
                    showMask: true,
                    template: 'app/desktop/modules/stores/scrapRequest/tabs/items/scrapRequestDialogView.jsp',
                    controller: 'ScrapRequestDialogController as scrapRequestDialogVm',
                    resolve: 'app/desktop/modules/stores/scrapRequest/tabs/items/scrapRequestDialogController',
                    width: 700,
                    data: {
                        scrapObj: vm.scrap
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.scrap.reqItem'}
                    ],
                    callback: function (selectedItems) {
                        var scrapReqItems = [];
                        var counter = 0;
                        angular.forEach(selectedItems, function (item) {
                            var copiedReqItem = angular.copy(vm.empty);
                            copiedReqItem.quantity = item.quantity;
                            copiedReqItem.description = item.description;
                            copiedReqItem.item = item.id;
                            copiedReqItem.scrapRequest = $stateParams.scrapDetailsId;
                            scrapReqItems.push(copiedReqItem);
                            counter += 1;
                            if (counter == selectedItems.length) {
                                ScrapItemService.createScrapReqItem(scrapReqItems).then(
                                    function (data) {
                                        $rootScope.showSuccessMessage("Scrap request item's created successfully");
                                        $rootScope.hideSidePanel();
                                        $rootScope.$broadcast('app.store.scrapDetails');
                                    }, function (error) {
                                        $rootScope.showErrorMessage("Please enter qty");

                                    }
                                );
                            }
                        });
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.printScrapChallan = printScrapChallan;
            function printScrapChallan() {
                TopStoreService.printScrapChallan($stateParams.scrapDetailsId).then(
                    function (data) {
                        var url = "{0}//{1}/api/is/stores/file/".format(window.location.protocol, window.location.host);
                        url += data + "/download";

                        window.open(url, '_self');
                    }
                )
            }

            //function validateQuantity(item) {
            //    var valid = false;
            //    if (item.Qty > 0) {
            //        valid = true;
            //    }
            //    return valid;
            //}

            function loadScrap() {
                ScrapService.get($stateParams.scrapDetailsId).then(
                    function (data) {
                        vm.scrap = data;
                        $rootScope.viewInfo.title = "Scrap : " + vm.scrap.scrapNumber;
                    }
                )
            }

            (function () {
                loadScrap();
            })();
        }
    }
);