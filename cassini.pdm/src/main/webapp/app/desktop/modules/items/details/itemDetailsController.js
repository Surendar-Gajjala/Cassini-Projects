define(
    [
        'app/desktop/modules/items/item.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/itemTypeService',
        'app/shared/services/itemService',
        'app/desktop/modules/items/details/tabs/basic/itemBasicInfoController',
        'app/desktop/modules/items/details/tabs/attributes/itemAttributesController',
        'app/desktop/modules/items/details/tabs/files/itemFilesController'

    ],
    function (module) {
        module.controller('ItemDetailsController', ItemDetailsController);

        function ItemDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                       CommonService, ItemTypeService, ItemService, $uibModal) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa fa-th";
            $rootScope.viewInfo.title = "Item Details";
            $rootScope.viewInfo.showDetails = true;
            $rootScope.loadItem = loadItem;

            var vm = this;

            vm.back = back;
            vm.itemId = $stateParams.itemId;

            function back() {
                window.history.back();
            }

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/items/details/tabs/basic/itemBasicInfoView.jsp',
                    active: true
                },
                attributes: {
                    id: 'details.attributes',
                    heading: 'Attributes',
                    template: 'app/desktop/modules/items/details/tabs/attributes/itemAttributesView.jsp',
                    active: false
                },
                files: {
                    id: 'details.files',
                    heading: 'Files',
                    template: 'app/desktop/modules/items/details/tabs/files/itemFilesView.jsp',
                    active: false
                }
            };

            vm.itemDetailsTabActivated = itemDetailsTabActivated;
            vm.onAddItemFiles = onAddItemFiles;
            vm.tabLoaded = tabLoaded;
            vm.broadcast = broadcast;


            function broadcast(event) {
                $scope.$broadcast(event);
            }

            function editItem() {
                $state.go('app.items.edit', {itemId: vm.item.id});
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t)) {
                        vm.tabs[t].active = (t != undefined && t == tab);
                    }
                }
            }

            function itemDetailsTabActivated(tabId) {
                $scope.$broadcast('app.item.tabactivated', {tabId: tabId})

                var tab = getTabById(tabId);
                if (tab != null) {
                    if (tab == "bom") {
                        loadItem();
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

            function onAddItemFiles() {
                $scope.$broadcast('app.item.addfiles');
            }


            function loadItem() {
                vm.loading = true;
                ItemService.getItem(vm.itemId).then(
                    function (data) {
                        vm.item = data;
                        $rootScope.viewInfo.title = "<div style='display: inline-block'>" +
                            "{0} : {1} </div>".
                                format(vm.item.itemNumber,
                                vm.item.revision);
                        $rootScope.viewInfo.description = vm.item.description;
                    }
                )
            }

            function tabLoaded(tabId) {
                $scope.$broadcast('app.item.tabloaded', {tabId: tabId});
            }

            (function () {
                $rootScope.showComments('ITEM', vm.itemId);
                loadItem();
            })();
        }
    }
);