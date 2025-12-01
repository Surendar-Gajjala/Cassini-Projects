define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mes/productionOrder/details/tabs/basic/productionOrderBasicInfoController',
        'app/desktop/modules/mes/productionOrder/details/tabs/items/productionOrderItemsController',
        'app/desktop/modules/mes/productionOrder/details/tabs/timeline/productionOrderTimelineController',
        'app/desktop/modules/mes/productionOrder/details/tabs/files/productionOrderFilesController',
        'app/desktop/modules/mes/productionOrder/details/tabs/workflow/productionOrderWorkflowController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/productionOrderService'

    ],
    function (module) {
        module.controller('ProductionOrderDetailsController', ProductionOrderDetailsController);

        function ProductionOrderDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application, $translate,
                                                  ProductionOrderService, DialogService) {
            var vm = this;
            $rootScope.viewInfo.showDetails = true;
            vm.productionOrderId = $stateParams.productionOrderId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            var lastSelectedTab = null;
            vm.active = 0;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/mes/productionOrder/details/tabs/basic/productionOrderBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                items: {
                    id: 'details.items',
                    heading: "Items",
                    template: 'app/desktop/modules/mes/productionOrder/details/tabs/items/productionOrderItemsView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mes/productionOrder/details/tabs/files/productionOrderFilesView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: 'Workflow',
                    template: 'app/desktop/modules/mes/productionOrder/details/tabs/workflow/productionOrderWorkflowView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mes/productionOrder/details/tabs/timeline/productionOrderTimelineView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                }

            };
            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mes.productionOrder.details', {
                    productionOrderId: vm.productionOrderId,
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

            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;

            function refreshDetails() {
                $scope.$broadcast('app.productionOrder.tabActivated', {tabId: $rootScope.selectedTab.id});
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedProductionOrderTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function tabActivated(tabId) {
                $state.transitionTo('app.mes.productionOrder.details', {
                    productionOrderId: vm.productionOrderId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.productionOrder.tabActivated', {tabId: tabId});

                }
            }

            vm.freeTextSearch = freeTextSearch;
            $rootScope.freeTextQuerys = null;
            function freeTextSearch(freeText) {
                $rootScope.freeTextQuerys = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    $scope.$broadcast('app.details.files.search', {name: freeText});
                }
                else {
                    $rootScope.freeTextQuerys = null;
                    $scope.$broadcast('app.productionOrder.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.productionOrder.tabActivated', {tabId: 'details.files'});
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

            vm.back = back;
            function back() {
                window.history.back();
            }

            function loadProductionOrder() {
                ProductionOrderService.getProductionOrder(vm.productionOrderId).then(
                    function (data) {
                        vm.productionOrder = data;
                        $rootScope.productionOrder = vm.productionOrder;
                        $scope.name = vm.productionOrder.name;
                        $rootScope.viewInfo.title = $translate.instant("PRODUCTION_ORDER_DETAILS");
                        $rootScope.viewInfo.description = vm.productionOrder.number + " , " + vm.productionOrder.name;
                        setLifecycles();
                        loadProductionOrderTabCounts();
                        vm.lastLifecyclePhase = vm.productionOrder.type.lifecycle.phases[vm.productionOrder.type.lifecycle.phases.length - 1];
                        vm.firstLifecyclePhase = vm.productionOrder.type.lifecycle.phases[0];
                        vm.loading = false;
                        $scope.$evalAsync();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function setLifecycles() {
                var phases = [];
                var currentPhase = vm.productionOrder.lifeCyclePhase.phase;
                var currentLifeCyclePhase = vm.productionOrder.lifeCyclePhase;
                $rootScope.lifeCycleStatus = vm.productionOrder.lifeCyclePhase.phase;
                var defs = vm.productionOrder.type.lifecycle.phases;
                defs.sort(function (a, b) {
                    return a.id - b.id;
                });
                var lastPhase = defs[defs.length - 1].phase;
                var phaseMap = new Hashtable();
                angular.forEach(defs, function (def) {
                    if (def.phaseType === 'OBSOLETE' && currentLifeCyclePhase.phaseType != 'OBSOLETE') return;

                    if (def.phase == currentPhase && lastPhase == def.phase) {
                        if (phaseMap.get(def.phase) == null) {
                            phases.push({
                                name: def.phase,
                                finished: true,
                                rejected: (def.phase == currentPhase && vm.productionOrder.rejected),
                                current: (def.phase == currentPhase)
                            })
                            phaseMap.put(def.phase, def);
                        }
                    } else {
                        if (phaseMap.get(def.phase) == null) {
                            phases.push({
                                name: def.phase,
                                finished: false,
                                rejected: (def.phase == currentPhase && vm.productionOrder.rejected),
                                current: (def.phase == currentPhase)
                            })
                            phaseMap.put(def.phase, def);
                        }
                    }
                });

                var index = -1;
                for (var i = 0; i < phases.length; i++) {
                    if (phases[i].current == true) {
                        index = i;
                    }
                }

                if (index > 0) {
                    for (i = 0; i < index; i++) {
                        phases[i].finished = true;
                    }
                }

                $rootScope.setLifecyclePhases(phases);
            }

            $rootScope.loadProductionOrderTabCounts = loadProductionOrderTabCounts;
            function loadProductionOrderTabCounts() {
                ProductionOrderService.getProductionOrderCounts(vm.productionOrderId).then(
                    function (data) {
                        $rootScope.bopCounts = data;
                        var files = document.getElementById("files");
                        var items = document.getElementById("po-items");

                        if (files != null) {
                            files.lastElementChild.innerHTML = vm.tabs.files.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.bopCounts.itemFiles);
                        }
                        if (items != null) {
                            items.lastElementChild.innerHTML = vm.tabs.items.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.bopCounts.items);
                        }
                    }
                )
            }


            vm.demotePOTitle = parsed.html($translate.instant("DEMOTE_PO_PHASE")).html();
            vm.promotePOTitle = parsed.html($translate.instant("PROMOTE_PO_PHASE")).html();
            var itemLifecycleStatusPromoted = parsed.html($translate.instant("PO_PROMOTED")).html();
            var itemLifecycleStatusDemoted = parsed.html($translate.instant("PO_DEMOTED")).html();
            var confirmation = parsed.html($translate.instant("CONFIRMATION")).html();
            var doYouWantToPromote = parsed.html($translate.instant("DO_YOU_WANT_TO_PROMOTE_PO_STATUS")).html();
            var doYouWantToDemote = parsed.html($translate.instant("DO_YOU_WANT_TO_DEMOTE_PO_STATUS")).html();
            var yesTitle = parsed.html($translate.instant("YES")).html();
            var noTitle = parsed.html($translate.instant("NO")).html();

            vm.promoteProductionOrder = promoteProductionOrder;
            function promoteProductionOrder() {
                var options = {
                    title: confirmation,
                    message: doYouWantToPromote,
                    okButtonClass: 'btn-danger',
                    okButtonText: yesTitle,
                    cancelButtonText: noTitle
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        ProductionOrderService.promoteProductionOrder(vm.productionOrderId, vm.productionOrder).then(
                            function (data) {
                                loadProductionOrder();
                                $rootScope.loadBasicProductionOrder();
                                $rootScope.showSuccessMessage(itemLifecycleStatusPromoted);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            vm.demoteProductionOrder = demoteProductionOrder;
            function demoteProductionOrder() {
                var options = {
                    title: confirmation,
                    message: doYouWantToDemote,
                    okButtonClass: 'btn-danger',
                    okButtonText: yesTitle,
                    cancelButtonText: noTitle
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        ProductionOrderService.demoteProductionOrder(vm.productionOrderId, vm.productionOrder).then(
                            function (data) {
                                loadProductionOrder();
                                $rootScope.loadBasicProductionOrder();
                                $rootScope.showSuccessMessage(itemLifecycleStatusDemoted);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            (function () {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedProductionOrderTab"));
                }

                $window.localStorage.setItem("lastSelectedProductionOrderTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.productionOrder.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.productionOrder.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadProductionOrder();
            })();

        }
    }
);
