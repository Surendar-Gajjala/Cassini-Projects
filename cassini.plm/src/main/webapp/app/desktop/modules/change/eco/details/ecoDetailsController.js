define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/ecoService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/desktop/modules/item/all/itemsController',
        'app/shared/services/core/workflowService',
        'app/desktop/modules/change/eco/details/tabs/basic/ecoBasicInfoController',
        'app/desktop/modules/change/eco/details/tabs/attributes/ecoAttributesController',
        'app/desktop/modules/change/eco/details/tabs/workflow/ecoWorkflowController',
        'app/desktop/modules/change/eco/details/tabs/items/affected/ecoAffectedItemsController',
        'app/desktop/modules/change/eco/details/tabs/history/ecoHistoryController',
        'app/desktop/modules/change/eco/details/tabs/files/ecoFilesController',
        'app/desktop/modules/change/eco/details/tabs/changeRequests/ecoChangeRequestController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/modules/item/details/customExtensionController',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'
    ],
    function (module) {
        module.controller('ECODetailsController', ECODetailsController);

        function ECODetailsController($scope, $rootScope, $timeout, $state, $stateParams, $injector, $window, $application,
                                      ECOService, $translate, CommentsService, WorkflowService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-exchange";
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.title = "ECO Details";
            $rootScope.viewInfo.showDetails = true;

            vm.ecoId = $stateParams.ecoId;
            vm.tabId = $stateParams.tab;
            vm.back = back;
            vm.onAddFiles = onAddFiles;
            vm.showMultiple = showMultiple;
            vm.showSingle = showSingle;
            $rootScope.loadEco = loadEco;
            vm.downloadFilesAsZip = downloadFilesAsZip;
            var searchMode = null;
            var freeTextQuery = null;
            vm.customActions = [];
            vm.customActionGroups = [];
            vm.customTabs = [];

            var lastSelectedTab = null;
            vm.active = 0;
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;

            var parsed = angular.element("<div></div>");
            vm.detailsShareTitle = parsed.html($translate.instant("DETAILS_SHARE_TITLE")).html();
            vm.refreshTitle = parsed.html($translate.instant("CLICK_TO_REFRESH")).html();
            var AffectedItems = parsed.html($translate.instant("ECO_AFFECTED_ITEMS")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            vm.addItems = parsed.html($translate.instant("ADD_ITEM")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
            var changeRequests = parsed.html($translate.instant("CHANGE_REQUESTS")).html();

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    index: 0,
                    heading: 'Basic',
                    template: 'app/desktop/modules/change/eco/details/tabs/basic/ecoBasicInfoView.jsp',
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attribute',
                 index: 1,
                 heading: 'Attributes',
                 template: 'app/desktop/modules/change/eco/details/tabs/attributes/ecoAttributesView.jsp',
                 active: false,
                 activated: false
                 }*/,
                changeRequests: {
                    id: 'details.changeRequests',
                    index: 1,
                    heading: changeRequests,
                    template: 'app/desktop/modules/change/eco/details/tabs/changeRequests/ecoChangeRequestView.jsp',
                    active: false,
                    activated: false
                },
                affecteditems: {
                    id: 'details.affecteditems',
                    index: 2,
                    heading: AffectedItems,
                    template: 'app/desktop/modules/change/eco/details/tabs/' +
                    'items/affected/ecoAffectedItemsView.jsp',
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    index: 3,
                    heading: 'Workflow',
                    template: 'app/desktop/modules/change/eco/details/tabs/workflow/ecoWorkflowView.jsp',
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    index: 4,
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/change/eco/details/tabs/files/ecoFilesView.jsp',
                    active: false,
                    activated: false
                },
                history: {
                    id: 'details.history',
                    index: 5,
                    heading: "History",
                    template: 'app/desktop/modules/change/eco/details/tabs/history/ecoHistoryView.jsp',
                    active: false,
                    activated: false
                }
            };

            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.changes.eco.details', {
                    ecoId: vm.ecoId,
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

            vm.broadcast = broadcast;
            $scope.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;

            function refreshDetails() {
                $scope.$broadcast('app.eco.tabactivated', {tabId: $rootScope.selectedTab.id});
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.eco.tabactivated', {tabId: 'details.files'});
            }

            vm.freeTextSearch = freeTextSearch;
            $rootScope.freeTextQuerys = null;
            function freeTextSearch(freeText) {
                $rootScope.freeTextQuerys = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    $scope.$broadcast('app.changeFile.files.search', {name: freeText});
                }
                else {
                    $rootScope.freeTextQuerys = null;
                    $scope.$broadcast('app.eco.tabactivated', {tabId: 'details.files'});
                }
            }

            vm.changeWorkflow = changeWorkflow;

            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            function broadcast(event) {
                $scope.$broadcast(event);
            }

            function onAddFiles() {
                $scope.$broadcast('app.eco.details.onaddfiles');
            }

            function showMultiple() {
                $scope.$broadcast('app.eco.details.multiple');
            }

            function showSingle() {
                $scope.$broadcast('app.eco.details.single');
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }

                for (var t in vm.customTabs) {
                    vm.customTabs[t].active = (vm.customTabs[t].id == tab.id);
                }
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedEcoTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function tabActivated(tabId) {
                $state.transitionTo('app.changes.eco.details', {
                    ecoId: vm.ecoId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                if (tab != null) {
                    vm.active = tab.index;
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.eco.tabactivated', {tabId: tabId});
                } else {
                    $rootScope.showAll('app.changes.eco.all');
                }
                /*if (tab != null) {
                 activateTab(tab);
                 }*/
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

            function back() {
                window.history.back();
            }

            function loadEco() {
                ECOService.getECO(vm.ecoId).then(
                    function (data) {
                        vm.eco = data;
                        $rootScope.eco = data;
                        vm.ecoStatus = vm.eco.released;
                        if (vm.eco.statusType == 'REJECTED') {
                            vm.ecoStatus = true;
                        }
                        $rootScope.status = vm.ecoStatus;
                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                         "{0}</div>".
                             format(vm.eco.ecoNumber);
                        $rootScope.viewInfo.description = vm.eco.titlep;
                        loadECOCounts();
                        loadWorkflow();
                        loadCommentsCount();
                        loadECODetailsExtensions();
                        $timeout(function () {
                            $scope.$broadcast('app.object.plugins.tabActivated', {});
                        }, 500);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $rootScope.ecoWorkflowStarted = false;
            function loadWorkflow() {
                if (vm.eco.workflow != null) {
                    WorkflowService.getWorkflow(vm.eco.workflow).then(
                        function (data) {
                            vm.workflow = data;
                            if (vm.workflow.started) {
                                $rootScope.ecoWorkflowStarted = true;
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            vm.performCustomAction = performCustomAction;
            function performCustomAction(action) {
                var service = $injector.get(action.service);
                if (service != null && service !== undefined) {
                    var method = service[action.method];
                    if (method != null && method !== undefined && typeof method === "function") {
                        if ($rootScope.validCMRFormData()) {
                            method(vm.eco.id, $rootScope.cmrFormData).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(action.successMessage);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }
                }
            }

            $rootScope.loadECOCounts = loadECOCounts;
            function loadECOCounts() {
                ECOService.getECOCounts(vm.ecoId).then(
                    function (data) {
                        $rootScope.ecoCounts = data;
                        $rootScope.ecoAffectedItems = $rootScope.ecoCounts.affectedItems;
                        var filesTab = document.getElementById("files");
                        var affectedItems = document.getElementById("affectedItems");
                        var changeRequests = document.getElementById("changeRequests");

                        filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.ecoCounts.files);

                        affectedItems.lastElementChild.innerHTML = vm.tabs.affecteditems.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.ecoCounts.affectedItems);

                        changeRequests.lastElementChild.innerHTML = vm.tabs.changeRequests.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.ecoCounts.changeRequests);

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('CHANGE', vm.ecoId).then(
                    function (data) {
                        $rootScope.showComments('CHANGE', vm.ecoId, data);
                        $rootScope.showTags('CHANGE', vm.ecoId, vm.eco.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function downloadFilesAsZip() {
                $rootScope.showBusyIndicator($('.view-container'));
                var url = "{0}//{1}/api/cm/ecos/{2}/files/zip".
                    format(window.location.protocol, window.location.host, vm.ecoId);

                //launchUrl(url);
                window.open(url);
                $rootScope.hideBusyIndicator();
            }

            function getLastTabIndexOfStandardTabs() {
                var index = 0;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t)) {
                        index = vm.tabs[t].index;
                    }
                }

                return index;
            }

            function loadECODetailsExtensions() {
                vm.customActions = [];
                vm.customActionGroups = [];

                var context = {
                    object: vm.eco
                };
                var plugins = $application.plugins;
                angular.forEach(plugins, function (plugin) {
                    var extensions = plugin.extensions;
                    if (extensions != null && extensions !== undefined) {
                        var ecodetails = extensions.objectDetails;
                        if (ecodetails != null && ecodetails !== undefined) {
                            var actionGroups = ecodetails.actiongroups;
                            if (actionGroups != null && actionGroups !== undefined && actionGroups.length > 0) {
                                angular.forEach(actionGroups, function (group) {
                                    var show = true;

                                    if (group.filter != null && group.filter !== undefined) {
                                        show = jexl.evalSync(group.filter, context);
                                    }
                                    if (show) {
                                        vm.customActionGroups.push(group);
                                    }
                                })
                            }
                            var actions = ecodetails.actions;
                            if (actions != null && actions !== undefined && actions.length > 0) {
                                angular.forEach(actions, function (action) {
                                    var show = true;

                                    if (action.filter != null && action.filter !== undefined) {
                                        show = jexl.evalSync(action.filter, context);
                                    }
                                    if (show) {
                                        vm.customActions.push(action);
                                    }
                                })
                            }
                        }
                    }
                });
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadEco();
                $rootScope.$on("app.plugins.loaded", function () {
                    loadECODetailsExtensions();
                });
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedEcoTab"));
                }

                $window.localStorage.setItem("lastSelectedEcoTab", "");

                if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);
                    $timeout(function () {
                        $scope.$broadcast('app.eco.tabactivated', {tabId: tabId});
                    }, 1000)
                }else if(lastSelectedTab != null && lastSelectedTab !== undefined){
                    tabActivated(lastSelectedTab);
                    $timeout(function () {
                        $scope.$broadcast('app.eco.tabactivated', {tabId: lastSelectedTab});
                    }, 1000)
                }
                else {
                    $scope.$broadcast('app.eco.tabactivated', {tabId: 'details.basic'});
                }


                //}
            })();
        }
    }
);