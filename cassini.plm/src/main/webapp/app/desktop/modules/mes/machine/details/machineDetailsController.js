define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mes/machine/details/tabs/basic/machineBasicInfoController',
        'app/desktop/modules/mes/machine/details/tabs/attributes/machineAttributesController',
        'app/desktop/modules/mes/machine/details/tabs/timeline/machineTimelineController',
        'app/desktop/modules/mes/machine/details/tabs/files/machineFilesController',
        //'app/desktop/modules/mes/machine/details/tabs/relatedItem/machineRelatedItemController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/machineService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('MachineDetailsController', MachineDetailsController);

        function MachineDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                          $translate, CommentsService, MachineService) {
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;

            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.machineId = $stateParams.machineId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var attributesTitle = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var relatedItem = parsed.html($translate.instant("RELATED_ITEMS")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];


            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/mes/machine/details/tabs/basic/machineBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: attributesTitle,
                 template: 'app/desktop/modules/mes/machine/details/tabs/attributes/machineAttributesView.jsp',
                 index: 1,
                 active: true,
                 activated: true
                 }*/,
                /* maintenance:{
                 id: 'details.maintenance',
                 heading: 'maintenance',
                 template: 'app/desktop/modules/mes/machine/details/tabs/maintenance/machineMaintenanceView.jsp',
                 index: 2,
                 active: true,
                 activated: true
                 },*/
                // relatedItem: {
                //     id: 'details.relatedItem',
                //     heading: relatedItem,
                //     template: 'app/desktop/modules/mes/machine/details/tabs/relatedItem/machineRelatedItemView.jsp',
                //     index: 1,
                //     active: true,
                //     activated: true
                // },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mes/machine/details/tabs/files/machineFilesView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mes/machine/details/tabs/timeline/machineTimelineView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                }

            };


            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mes.masterData.machine.details', {
                    machineId: vm.machineId,
                    tab: 'details.basic'
                }, {notify: false});
                vm.active = 0;
            }
            else {
                var tab = getTabById(tabId);
                if (tab != null) {
                    //vm.active = tab.index;
                }
            }

            function refreshDetails() {
                $scope.$broadcast('app.machine.tabActivated', {tabId: $rootScope.selectedTab.id});
            }


            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
                for (var t in vm.customTabs) {
                    vm.customTabs[t].active = (vm.customTabs[t].id == tab.id);
                }
            }

            function tabActivated(tabId) {
                $state.transitionTo('app.mes.masterData.machine.details', {
                    machineId: vm.machineId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.machine.tabActivated', {tabId: tabId});

                }
            }


            function getTabById(tabId) {
                var tab = null;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t) && vm.tabs[t].id == tabId) {
                        tab = vm.tabs[t];
                    }
                }
                if (tab == null) {
                    angular.forEach(vm.customTabs, function (customTab) {
                        if (customTab.id === tabId) {
                            tab = customTab;
                        }
                    });
                }

                return tab;
            }

            vm.back = back;
            function back() {
                window.history.back();
            }


            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedMachineTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            function loadMachine() {
                $rootScope.$broadcast("loadMesObjectFilesCount", {
                    objectId: vm.machineId,
                    objectType: "MESOBJECT",
                    heading: vm.tabs.files.heading
                });

                MachineService.getMachine(vm.machineId).then(
                    function (data) {
                        vm.machine = data;
                        $rootScope.machine = data;
                        $rootScope.viewInfo.title = $translate.instant("EQUIPMENT_DETAILS");
                        $rootScope.viewInfo.description = vm.machine.number + " , " + vm.machine.name;
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('MACHINE', vm.machineId).then(
                    function (data) {
                        $rootScope.showComments('MACHINE', vm.machineId, data);
                        $rootScope.showTags('MACHINE', vm.machineId, vm.machine.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {

                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedMachineTab"));
                }

                $window.localStorage.setItem("lastSelectedMachineTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.machine.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.machine.tabActivated', {tabId: tabId});
                    }, 1000)
                }

                loadMachine();

            })();

        }
    }
);
