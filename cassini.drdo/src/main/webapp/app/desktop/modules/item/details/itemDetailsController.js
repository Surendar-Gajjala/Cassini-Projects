define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/item/details/tabs/basic/itemBasicInfoController',
        'app/desktop/modules/item/details/tabs/attributes/itemAttributesController',
        'app/desktop/modules/item/details/tabs/instances/itemInstanceController',
        'app/desktop/modules/item/details/tabs/files/itemFilesController',
        'app/desktop/modules/item/details/tabs/report/itemReportController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/bomService'
    ],
    function (module) {
        module.controller('ItemDetailsController', ItemDetailsController);

        function ItemDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, DialogService,
                                       ItemService, BomService) {

            if ($application.homeLoaded == false) {
                return;
            }
            var vm = this;

            vm.itemDetailsTabActivated = itemDetailsTabActivated;
            vm.back = back;

            function back() {
                window.history.back();
            }

            vm.active = 0;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic Info',
                    template: 'app/desktop/modules/item/details/tabs/basic/itemBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                attributes: {
                    id: 'details.attributes',
                    heading: 'Attributes',
                    template: 'app/desktop/modules/item/details/tabs/attributes/itemAttributesView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: 'Files',
                    template: 'app/desktop/modules/item/details/tabs/files/itemFilesView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                instances: {
                    id: 'details.instances',
                    heading: 'Instances',
                    template: 'app/desktop/modules/item/details/tabs/instances/itemInstanceView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                report: {
                    id: 'details.report',
                    heading: 'Report',
                    template: 'app/desktop/modules/item/details/tabs/report/itemReportView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                }
            };

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            function itemDetailsTabActivated(tabId) {
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    $scope.$broadcast('app.item.tabActivated', {tabId: tabId});

                    if ($rootScope.selectedItemRevisionDetails != undefined) {
                        if ($rootScope.selectedItemRevisionDetails.partSpec != null) {
                            $rootScope.viewInfo.title = $rootScope.selectedItemRevisionDetails.itemMaster.itemName + " " + $rootScope.selectedItemRevisionDetails.partSpec.specName
                                + " - " + $rootScope.selectedTab.heading;
                        } else {
                            if ($rootScope.selectedItemRevisionDetails.itemMaster.itemType.hasBom) {
                                $rootScope.viewInfo.title = $rootScope.selectedItemRevisionDetails.itemMaster.itemName
                                    + " - Missiles"
                            } else {
                                $rootScope.viewInfo.title = $rootScope.selectedItemRevisionDetails.itemMaster.itemName
                                    + " - " + $rootScope.selectedTab.heading;
                            }
                        }

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
                        tab = vm.tabs[t];
                    }
                }

                return tab;
            }

            function loadItem() {
                vm.loading = true;
                ItemService.getRevisionId($stateParams.itemId).then(
                    function (data) {
                        vm.itemRevision = data;
                        vm.item = vm.itemRevision.itemMaster;
                        if (vm.item.thumbnail != null) {
                            vm.item.thumbnailImage = "api/plm/items/" + vm.item.id + "/itemImageAttribute/download?" + new Date().getTime();
                        }

                        $rootScope.selectedItemDetails = vm.itemRevision;
                        $rootScope.selectedItemRevisionDetails = vm.itemRevision;

                        if (vm.itemRevision.partSpec != null) {
                            $rootScope.viewInfo.title = $rootScope.selectedItemRevisionDetails.itemMaster.itemName + " " + vm.itemRevision.partSpec.specName
                                + " - " + $rootScope.selectedTab.heading;
                        } else {
                            if ($rootScope.selectedItemRevisionDetails.itemMaster.itemType.hasBom) {
                                $rootScope.viewInfo.title = $rootScope.selectedItemRevisionDetails.itemMaster.itemName
                                    + " - Missiles"
                            } else {
                                $rootScope.viewInfo.title = $rootScope.selectedItemRevisionDetails.itemMaster.itemName
                                    + " - " + $rootScope.selectedTab.heading;
                            }
                        }

                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadItem();
                }
            })();
        }
    }
);