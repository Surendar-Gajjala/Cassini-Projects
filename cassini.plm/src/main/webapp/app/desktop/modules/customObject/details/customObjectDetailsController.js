define(
    [
        'app/desktop/modules/customObject/customObject.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/modules/customObject/details/tabs/basic/customObjectBasicInfoController',
        'app/desktop/modules/customObject/details/tabs/bom/customObjectBomController',
        'app/desktop/modules/customObject/details/tabs/whereused/customObjectWhereUsedController',
        'app/desktop/modules/customObject/details/tabs/related/customObjectRelatedController',
        'app/desktop/modules/customObject/details/tabs/files/customObjectFilesController',
        'app/desktop/modules/customObject/details/tabs/workflow/customObjectWorkflowController',
        'app/desktop/modules/customObject/details/tabs/timeline/customObjectTimelineController',
        'app/shared/services/core/workflowService',
        'app/desktop/modules/item/details/customExtensionController',
        'app/desktop/directives/dynamic-controller/dynamicCtrl',
        'app/desktop/directives/plugin-directive/pluginTabsDirective',
        'app/shared/services/core/workflowService'

    ],
    function (module) {
        module.controller('CustomObjectDetailsController', CustomObjectDetailsController);

        function CustomObjectDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                               $translate, CommentsService, CustomObjectService, WorkflowService, CommonService) {
            var vm = this;
            $rootScope.viewInfo.icon = "fa fa flaticon-contract11";
            $rootScope.viewInfo.title = "CustomObject Details";
            $rootScope.viewInfo.showDetails = false;

            vm.customId = $stateParams.customId;
            vm.tabId = $stateParams.tab;
            vm.back = back;

            var lastSelectedTab = null;
            vm.active = 0;
            vm.customTabs = [];

            var parsed = angular.element("<div></div>");
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var bomHeading = parsed.html($translate.instant("ITEM_DETAILS_TAB_BOM")).html();
            var whereUsedHeading = parsed.html($translate.instant("DETAILS_TAB_WHERE_USED")).html();
            var relatedHeading = parsed.html($translate.instant("RELATED_DETAILS_TAB_OBJECT")).html();
            var files = parsed.html($translate.instant("FILES")).html();
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            vm.detailsShareTitle = parsed.html($translate.instant("SHARE")).html();
            $rootScope.clipBoardCustomFiles = [];
            $rootScope.clipBoardCustomFiles = $application.clipboard.files;
            $rootScope.sharedPermission = null;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/customObject/details/tabs/basic/customObjectBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                bom: {
                    id: 'details.bom',
                    heading: bomHeading,
                    template: 'app/desktop/modules/customObject/details/tabs/bom/customObjectBomView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                whereUsed: {
                    id: 'details.whereUsed',
                    heading: whereUsedHeading,
                    template: 'app/desktop/modules/customObject/details/tabs/whereused/customObjectWhereUsedView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                related: {
                    id: 'details.related',
                    heading: relatedHeading,
                    template: 'app/desktop/modules/customObject/details/tabs/related/customObjectRelatedView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },

                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/customObject/details/tabs/files/customObjectFilesView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: 'Workflow',
                    template: 'app/desktop/modules/customObject/details/tabs/workflow/customObjectWorkflowView.jsp',
                    index: 5,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/customObject/details/tabs/timeline/customObjectTimelineView.jsp',
                    index: 6,
                    active: false,
                    activated: false
                }
            };

            vm.showExternalUserSuppliers = showExternalUserSuppliers;
            function showExternalUserSuppliers() {
                $state.go('app.home');
                $rootScope.sharedSupplier();
            }

            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.customobjects.details', {
                    customId: vm.customId,
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

            $scope.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;

            function refreshDetails() {
                $scope.$broadcast('app.customObj.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                    JSON.parse($window.localStorage.getItem("lastSelectedCustomTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function tabActivated(tabId) {
                $state.transitionTo('app.customobjects.details', {
                    customId: vm.customId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                if (tab != null) {
                    vm.active = tab.index;
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.customObj.tabActivated', {tabId: tabId});

                } else {
                    $rootScope.showAll('app.customobjects.all');
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

            vm.showAllCustomObjects = showAllCustomObjects;
            function showAllCustomObjects() {
                $state.go('app.customobjects.all', {typeName: $window.localStorage.getItem("route-param")});
            }

            $rootScope.loadObject = loadObject;
            function loadObject() {
                WorkflowService.getCustomObjectWorkflowStatus(vm.customId).then(
                    function (data) {
                        $rootScope.customObject = data;
                        vm.customObject = data;
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('CUSTOMOBJECT', $rootScope.customObject.id).then(
                    function (data) {
                        $rootScope.showComments('CUSTOMOBJECT', $rootScope.customObject.id, data);
                        $rootScope.showTags('CUSTOMOBJECT', $rootScope.customObject.id, vm.customObject.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $scope.$on("app.customObj.files.count", function (evnt, args) {
                loadCustomObjectTabCount();
            });

            $rootScope.loadCustomObjectTabCount = loadCustomObjectTabCount;
            function loadCustomObjectTabCount() {
                WorkflowService.getCustomObjectTabCount(vm.customId).then(
                    function (data) {
                        vm.tabCounts = data;
                        var filesTab = document.getElementById("custom-files");
                        var bomTab = document.getElementById("custom-bom");
                        var whereUsedTab = document.getElementById("custom-whereused");
                        var relatedObjects = document.getElementById("custom-related");
                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (filesTab != null) {
                            filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                                tmplStr.format(vm.tabCounts.itemFiles);
                        }
                        if (bomTab != null) {
                            bomTab.lastElementChild.innerHTML = vm.tabs.bom.heading +
                                tmplStr.format(vm.tabCounts.bom);
                        }
                        if (whereUsedTab != null) {
                            whereUsedTab.lastElementChild.innerHTML = vm.tabs.whereUsed.heading +
                                tmplStr.format(vm.tabCounts.whereUsedItems);
                        }
                        if (relatedObjects != null) {
                            relatedObjects.lastElementChild.innerHTML = vm.tabs.related.heading +
                                tmplStr.format(vm.tabCounts.relatedItems);
                        }
                    }
                )
            }

            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            vm.addWorkflowTitle = parsed.html($translate.instant("ADD_WORKFLOW")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
            vm.addWorkflow = addWorkflow;
            function addWorkflow() {
                $scope.$broadcast('app.add.workflow');
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.customObj.tabActivated', {tabId: 'details.files'});
            }

            vm.freeTextSearch = freeTextSearch;
            $rootScope.freeTextQuerys = null;
            function freeTextSearch(freeText) {
                $rootScope.freeTextQuerys = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    $scope.$broadcast('app.customObj.files.search', {name: freeText});
                }
                else {
                    $rootScope.freeTextQuerys = null;
                    $scope.$broadcast('app.customObj.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.showObject = showObject;
            function showObject() {
                if ($rootScope.customTitle != null) {
                    $state.go('app.customobjects.all', {customMode: $rootScope.customTitle});
                }
            }

            vm.hasDisplayTab = hasDisplayTab;
            function hasDisplayTab(tabName) {
                var valid = true;

                if (vm.customObject != null && vm.customObject != undefined && vm.customObject.type.tabs != null) {
                    var index = vm.customObject.type.tabs.indexOf(tabName);
                    if (index == -1) {
                        valid = false;
                    }
                }

                return valid;
            }


            var parse = angular.element("<div></div>");
            var shareButton = parse.html($translate.instant("SHARE")).html();
            var shareCustomObjectTitle = parse.html($translate.instant("SHARE_CUSTOM_OBJECT")).html();
            vm.shareCustomObject = shareCustomObject;
            function shareCustomObject() {
                var options = {
                    title: shareCustomObjectTitle,
                    template: 'app/desktop/modules/shared/share/shareView.jsp',
                    controller: 'ShareController as shareVm',
                    resolve: 'app/desktop/modules/shared/share/shareController',
                    width: 600,
                    showMask: true,
                    data: {
                        sharedItem: $rootScope.customObject,
                        itemsSharedType: 'itemSelection',
                        objectType: "CUSTOMOBJECT"
                    },
                    buttons: [
                        {text: shareButton, broadcast: 'app.share.item'}
                    ],
                    callback: function (data) {
                        $rootScope.showSuccessMessage($rootScope.customObject.name + " : Shared successfully");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadCustomObject() {
                WorkflowService.getCustomObjectWorkflowStatus(vm.customId).then(
                    function (data) {
                        $rootScope.customObject = data;
                        vm.customObject = data;
                        $timeout(function () {
                            $scope.$broadcast('app.object.plugins.tabActivated', {});
                        }, 500);
                        $rootScope.viewInfo.title = data.number;
                        $rootScope.viewInfo.description = data.name;
                        loadCommentsCount();
                        loadCustomObjectTabCount();
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedCustomTab"));
                }
                if ($window.localStorage.getItem("shared-permission") != undefined && $window.localStorage.getItem("shared-permission") != null) {
                    var sharedPermission = $window.localStorage.getItem("shared-permission");
                    if (sharedPermission != null && sharedPermission != "") {
                        $rootScope.sharedPermission = sharedPermission;
                    }
                }
                $window.localStorage.setItem("lastSelectedCustomTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.customObj.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.customObj.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadCustomObject();
            })();

        }
    }
);
