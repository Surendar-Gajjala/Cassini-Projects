define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/dcrService',
        'app/shared/services/core/ecrService',
        'app/shared/services/core/mcoService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/specificationsService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/change/mco/details/tabs/basic/mcoBasicInfoController',
        'app/desktop/modules/change/mco/details/tabs/attributes/mcoAttributesController',
        'app/desktop/modules/change/mco/details/tabs/workflow/mcoWorkflowController',
        'app/desktop/modules/change/mco/details/tabs/affectedItem/mcoAffectedItemsController',
        'app/desktop/modules/change/mco/details/tabs/relatedItem/mcoRelatedItemsController',
        'app/desktop/modules/change/mco/details/tabs/files/mcoFilesController',
        'app/desktop/modules/change/mco/details/tabs/timeLine/mcoTimeLineController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('MCODetailsController', MCODetailsController);

        function MCODetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                      ECOService, $translate, CommentsService, MCOService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-exchange";
            $rootScope.viewInfo.title = "MCO Details";
            $rootScope.viewInfo.showDetails = true;

            vm.mcoId = $stateParams.mcoId;
            vm.back = back;

            var lastSelectedTab = null;
            vm.active = 0;

            var parsed = angular.element("<div></div>");
            vm.detailsShareTitle = parsed.html($translate.instant("DETAILS_SHARE_TITLE")).html();
            vm.refreshTitle = parsed.html($translate.instant("CLICK_TO_REFRESH")).html();
            var affectedItems = parsed.html($translate.instant("ECO_AFFECTED_ITEMS")).html();
            var affectedMBOMs = parsed.html($translate.instant("AFFECTED_MBOMS")).html();
            var relatedItems = parsed.html($translate.instant("RELATED_ITEMS")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var files = parsed.html($translate.instant("FILES")).html();
            var workflow = parsed.html($translate.instant("WORKFLOW")).html();
            var timeline = parsed.html($translate.instant("TIMELINE")).html();
            var attributes = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            vm.addItems = parsed.html($translate.instant("ADD_ITEM")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
            vm.amlRedLine = parsed.html($translate.instant("AML_RED_LINE")).html();
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];
            var affectedTabName = affectedItems;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    index: 0,
                    heading: basic,
                    template: 'app/desktop/modules/change/mco/details/tabs/basic/mcoBasicInfoView.jsp',
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attribute',
                 index: 1,
                 heading: attributes,
                 template: 'app/desktop/modules/change/mco/details/tabs/attributes/mcoAttributesView.jsp',
                 active: false,
                 activated: false
                 }*/,
                affectedItems: {
                    id: 'details.affectedItems',
                    index: 1,
                    heading: affectedTabName,
                    template: 'app/desktop/modules/change/mco/details/tabs/affectedItem/mcoAffectedItemsView.jsp',
                    active: false,
                    activated: false
                },
                relatedItems: {
                    id: 'details.relatedItems',
                    index: 2,
                    heading: relatedItems,
                    template: 'app/desktop/modules/change/mco/details/tabs/relatedItem/mcoRelatedItemsView.jsp',
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    index: 3,
                    heading: workflow,
                    template: 'app/desktop/modules/change/mco/details/tabs/workflow/mcoWorkflowView.jsp',
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    index: 4,
                    heading: files,
                    template: 'app/desktop/modules/change/mco/details/tabs/files/mcoFilesView.jsp',
                    active: false,
                    activated: false
                },
                timeLine: {
                    id: 'details.timeLine',
                    index: 5,
                    heading: timeline,
                    template: 'app/desktop/modules/change/mco/details/tabs/timeLine/mcoTimeLineView.jsp',
                    active: false,
                    activated: false
                }
            };

            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.changes.mco.details', {
                    mcoId: vm.mcoId,
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
                $scope.$broadcast('app.mco.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                    JSON.parse($window.localStorage.getItem("lastSelectedMcoTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function tabActivated(tabId) {
                $state.transitionTo('app.changes.mco.details', {
                    mcoId: vm.mcoId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.mco.tabActivated', {tabId: tabId});

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

            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            $rootScope.loadMcoDetails = loadMcoDetails;
            function loadMcoDetails() {
                MCOService.getMCO(vm.mcoId).then(
                    function (data) {
                        vm.mco = data;
                        if (vm.mco.mcoType.mcoType == "ITEMMCO") {
                            affectedTabName = affectedMBOMs;
                        }
                        $rootScope.mco = data;
                        // $rootScope.viewInfo.title = "MCO Details";
                        // $rootScope.viewInfo.description = "Number: {0}, Status: {1}".
                        //     format(vm.mco.mcoNumber, vm.mco.status);
                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                        "{0}</div>".
                            format(vm.mco.mcoNumber);
                       $rootScope.viewInfo.description = vm.mco.title;
                        loadMCOCounts();
                        loadCommentsCount();
                        $timeout(function () {
                            $scope.$broadcast('app.object.plugins.tabActivated', {});
                        }, 500);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );


            }

            function loadMCOCounts() {
                MCOService.getMcoDetailsCount(vm.mcoId).then(
                    function (data) {
                        $rootScope.mcoDetailsCount = data;
                        $rootScope.mcoAffectedItems = $rootScope.mcoDetailsCount.affectedItems;
                        var affectedItems = document.getElementById("affectedItems");
                        var relatedItems = document.getElementById("relatedItems");
                        var files = document.getElementById("files");

                        if (affectedItems != null) {
                            affectedItems.lastElementChild.innerHTML = affectedTabName +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.mcoDetailsCount.affectedItems);
                        }

                        if (relatedItems != null) {
                            relatedItems.lastElementChild.innerHTML = vm.tabs.relatedItems.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.mcoDetailsCount.relatedItems);
                        }
                        if (files != null) {
                            files.lastElementChild.innerHTML = vm.tabs.files.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.mcoDetailsCount.itemFiles);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
                loadMcoDetails();
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.mco.tabActivated', {tabId: 'details.files'});
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
                    $scope.$broadcast('app.mco.tabActivated', {tabId: 'details.files'});
                }
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('CHANGE', vm.mcoId).then(
                    function (data) {
                        $rootScope.showComments('CHANGE', vm.mcoId, data);
                        $rootScope.showTags('CHANGE', vm.mcoId, vm.mco.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $rootScope.showAmlRedLine = showAmlRedLine;
            function showAmlRedLine() {
                var modal = document.getElementById("mco-rollup");
                modal.style.display = "block";
                $timeout(function () {
                    var headerHeight = $('.mco-header').outerHeight();
                    var workflowContentHeight = $('.mcoRollup-content').outerHeight();
                    $(".mco-content").height(workflowContentHeight - headerHeight);
                }, 200)
                loadAmlRedLineItems();
            }

            $scope.hideMcoPreview = hideMcoPreview;
            function hideMcoPreview() {
                var modal = document.getElementById("mco-rollup");
                modal.style.display = "none";
            }

            function loadAmlRedLineItems() {
                MCOService.getAmlParts(vm.mcoId).then(
                    function (data) {
                        vm.parts = data;
                        angular.forEach(vm.parts, function (part) {
                            part.expanded = false;
                        });
                        vm.parts[0].expanded = true;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.showAmlItems = showAmlItems;

            function showAmlItems(part) {
                part.expanded = !part.expanded;
            }

            vm.showMfrPartsDetails = showMfrPartsDetails;
            function showMfrPartsDetails(mfrpart) {
                $state.go('app.mfr.mfrparts.details', {
                    mfrId: mfrpart.mfrId,
                    manufacturePartId: mfrpart.material
                });
            }

            (function () {
                //if ($application.homeLoaded == true) {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedMcoTab"));
                }

                $window.localStorage.setItem("lastSelectedMcoTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.mco.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.mco.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadMcoDetails();
                //}
            })();
        }
    }
);