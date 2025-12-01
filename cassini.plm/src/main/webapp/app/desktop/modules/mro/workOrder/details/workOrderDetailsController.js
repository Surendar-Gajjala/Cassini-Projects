define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mro/workOrder/details/tabs/basic/workOrderBasicInfoController',
        'app/desktop/modules/mro/workOrder/details/tabs/attributes/workOrderAttributesController',
        'app/desktop/modules/mro/workOrder/details/tabs/operations/workOrderOperationsController',
        'app/desktop/modules/mro/workOrder/details/tabs/sparePart/workOrderSparePartsController',
        'app/desktop/modules/mro/workOrder/details/tabs/timeline/workOrderTimelineController',
        'app/desktop/modules/mro/workOrder/details/tabs/files/workOrderFilesController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/modules/mro/workOrder/details/tabs/workflow/workOrderWorkflowController',
        'app/desktop/modules/mro/workOrder/details/tabs/resources/workOrderResourcesController',
        'app/desktop/modules/mro/workOrder/details/tabs/instructions/workOrderInstructionsController',
        'app/shared/services/core/workOrderService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('WorkOrderDetailsController', WorkOrderDetailsController);

        function WorkOrderDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                            ECOService, $translate, WorkOrderService, CommentsService, DialogService) {

            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;


            vm.workOrderId = $stateParams.workOrderId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var attributesTitle = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var instructionsTabHeading = parsed.html($translate.instant("DETAILS_TAB_INSTRUCTIONS")).html();
            var whereUsed = parsed.html($translate.instant("DETAILS_TAB_WHERE_USED")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            var workOrderUpdated = parsed.html($translate.instant("WORK_ORDER_UPDATED_MESSAGE")).html();
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/mro/workOrder/details/tabs/basic/workOrderBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: attributesTitle,
                 template: 'app/desktop/modules/mro/workOrder/details/tabs/attributes/workOrderAttributesView.jsp',
                 index: 1,
                 active: true,
                 activated: true
                 }*/,
                operations: {
                    id: 'details.operations',
                    heading: "Operations",
                    template: 'app/desktop/modules/mro/workOrder/details/tabs/operations/workOrderOperationsView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                resources: {
                    id: 'details.resources',
                    heading: "Resources",
                    template: 'app/desktop/modules/mro/workOrder/details/tabs/resources/workOrderResourcesView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                spareParts: {
                    id: 'details.spareParts',
                    heading: "Spare Parts",
                    template: 'app/desktop/modules/mro/workOrder/details/tabs/sparePart/workOrderSparePartsView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                instructions: {
                    id: 'details.instructions',
                    heading: instructionsTabHeading,
                    template: 'app/desktop/modules/mro/workOrder/details/tabs/instructions/workOrderInstructionsView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mro/workOrder/details/tabs/files/workOrderFilesView.jsp',
                    index: 5,
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: "Workflow",
                    index: 6,
                    template: 'app/desktop/modules/mro/workOrder/details/tabs/workflow/workOrderWorkflowView.jsp',
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mro/workOrder/details/tabs/timeline/workOrderTimelineView.jsp',
                    index: 7,
                    active: false,
                    activated: false
                }

            };


            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mro.workOrder.details', {
                    workOrderId: vm.workOrderId,
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
                $scope.$broadcast('app.workOrder.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                $state.transitionTo('app.mro.workOrder.details', {
                    workOrderId: vm.workOrderId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.workOrder.tabActivated', {tabId: tabId});

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

            vm.addWorkflowTitle = parsed.html($translate.instant("ADD_WORKFLOW")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();

            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            vm.addWorkflow = addWorkflow;
            function addWorkflow() {
                $scope.$broadcast('app.add.workflow');
            }

            vm.back = back;
            function back() {
                window.history.back();
            }


            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedWorkOrderTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            function loadWorkOrderBasicDetails() {
                vm.loading = true;
                if (vm.workOrderId != null && vm.workOrderId != undefined) {
                    WorkOrderService.getWorkOrder(vm.workOrderId).then(
                        function (data) {
                            vm.workOrder = data;
                            $rootScope.workOrderInfo = vm.workOrder;
                            $rootScope.workOrder = vm.workOrder;
                            vm.loading = false;
                            $rootScope.viewInfo.title = $translate.instant("WORK_ORDER_DETAILS");
                            $rootScope.viewInfo.description = vm.workOrder.number + " , " + vm.workOrder.name;
                            loadWorkOrderTabCounts();
                            loadCommentsCount();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            $rootScope.loadWorkOrderTabCounts = loadWorkOrderTabCounts;
            function loadWorkOrderTabCounts() {
                WorkOrderService.getWorkOrderTabCounts(vm.workOrderId).then(
                    function (data) {
                        vm.tabCounts = data;
                        var operationsTab = document.getElementById("operations");
                        var sparePartsTab = document.getElementById("spareParts");
                        var resourcesTab = document.getElementById("resources");
                       // var digitalWorkInstructionsTab = document.getElementById("instructions");
                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (operationsTab != null) {
                            operationsTab.lastElementChild.innerHTML = vm.tabs.operations.heading +
                                tmplStr.format(vm.tabCounts.operations);
                        }
                        if (sparePartsTab != null) {
                            sparePartsTab.lastElementChild.innerHTML = vm.tabs.spareParts.heading +
                                tmplStr.format(vm.tabCounts.spareParts);
                        }
                        if (resourcesTab != null) {
                            resourcesTab.lastElementChild.innerHTML = vm.tabs.resources.heading +
                                tmplStr.format(vm.tabCounts.resources);
                        }

                        // if (digitalWorkInstructionsTab != null) {
                        //     digitalWorkInstructionsTab.lastElementChild.innerHTML = vm.tabs.instructions.heading +
                        //         tmplStr.format(vm.tabCounts.instructions);
                        // }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadWorkOrder() {
                $rootScope.$broadcast("loadMroObjectFilesCount", {
                    objectId: vm.workOrderId,
                    objectType: "MROOBJECT",
                    heading: vm.tabs.files.heading
                });
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
                    $scope.$broadcast('app.workOrder.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.workOrder.tabActivated', {tabId: 'details.files'});
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('MROWORKORDER', vm.workOrderId).then(
                    function (data) {
                        $rootScope.showComments('MROWORKORDER', vm.workOrderId, data);
                        $rootScope.showTags('MROWORKORDER', vm.workOrderId, vm.workOrder.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            var confirmation = parsed.html($translate.instant("CONFIRMATION")).html();
            var doYouWantToStartWorkOrder = parsed.html($translate.instant("DO_YOU_WANT_TO_START_WORK_ORDER")).html();
            var doYouWantToFinishWorkOrder = parsed.html($translate.instant("DO_YOU_WANT_TO_FINISH_WORK_ORDER")).html();
            var doYouWantToHoldWorkOrder = parsed.html($translate.instant("DO_YOU_WANT_TO_HOLD_WORKORDER")).html();
            var doYouWantToRemoveHoldWorkOrder = parsed.html($translate.instant("DO_YOU_WANT_TO_UNHOLD_WORKORDER")).html();
            var yesTitle = parsed.html($translate.instant("YES")).html();
            var noTitle = parsed.html($translate.instant("NO")).html();
            vm.promoteWorkOrder = promoteWorkOrder;
            vm.holdWorkOrder = holdWorkOrder;
            vm.removeHold = removeHold;
            function promoteWorkOrder() {
                var message = doYouWantToStartWorkOrder;
                if ($rootScope.workOrder.status == "INPROGRESS") {
                    message = doYouWantToFinishWorkOrder;
                }
                var options = {
                    title: confirmation,
                    message: message,
                    okButtonClass: 'btn-danger',
                    okButtonText: yesTitle,
                    cancelButtonText: noTitle
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        WorkOrderService.promoteWorkOrder(vm.workOrderId).then(
                            function (data) {
                                $rootScope.loadWorkOrderBasicDetails();
                                $rootScope.showSuccessMessage(workOrderUpdated);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })

            }

            function holdWorkOrder() {
                var options = {
                    title: confirmation,
                    message: doYouWantToHoldWorkOrder,
                    okButtonClass: 'btn-danger',
                    okButtonText: yesTitle,
                    cancelButtonText: noTitle
                };
                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            WorkOrderService.holdWorkOrder(vm.workOrderId).then(
                                function (data) {
                                    $rootScope.loadWorkOrderBasicDetails();
                                    $rootScope.showSuccessMessage(workOrderUpdated);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                )

            }

            function removeHold() {
                var options = {
                    title: confirmation,
                    message: doYouWantToRemoveHoldWorkOrder,
                    okButtonClass: 'btn-danger',
                    okButtonText: yesTitle,
                    cancelButtonText: noTitle
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        WorkOrderService.removeOnHold(vm.workOrderId).then(
                            function (data) {
                                $rootScope.loadWorkOrderBasicDetails();
                                $rootScope.showSuccessMessage(workOrderUpdated);
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
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedWorkOrderTab"));
                }

                $window.localStorage.setItem("lastSelectedWorkOrderTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.workOrder.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.workOrder.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadWorkOrderBasicDetails();
                loadWorkOrder();
            })();

        }
    }
);
