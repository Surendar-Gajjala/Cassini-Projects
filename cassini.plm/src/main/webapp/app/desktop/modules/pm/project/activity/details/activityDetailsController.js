define(
    [
        'app/desktop/modules/pm/project/activity/activity.module',
        'app/desktop/modules/pm/project/activity/details/basic/activityBasicInfoController',
        'app/desktop/modules/pm/project/activity/details/files/activityFilesController',
        'app/desktop/modules/pm/project/activity/details/deliverables/activityDeliverablesController',
        'app/desktop/modules/pm/project/activity/details/referenceItems/activityReferenceItemsController',
        'app/desktop/modules/pm/project/activity/details/workflow/activityWorkflowController',
        'app/desktop/modules/pm/project/activity/details/history/activityHistoryController',
        'app/shared/services/core/activityService'

    ],
    function (module) {
        module.controller('ActivityDetailsController', ActivityDetailsController);

        function ActivityDetailsController($scope, $stateParams, $rootScope, $sce, $translate, $timeout, $state, $application,
                                           $window, ActivityService, CommentsService) {

            var vm = this;
            var activityId = $stateParams.activityId;
            $rootScope.viewInfo.showDetails = true;
            var wbsId = $stateParams.wbsId;
            vm.activityDetailsTabActivated = activityDetailsTabActivated;
            vm.downloadFilesAsZip = downloadFilesAsZip;
            vm.back = back;
            vm.activityPercentage = 0;
            $rootScope.activityPercent = 0;
            var lastSelectedTab = null;
            $rootScope.sharedProjectActivityPermission = null;
            $rootScope.activityWritePermission = $rootScope.hasPermission('projectactivity', 'edit') && (!$rootScope.loginPersonDetails.external || $rootScope.sharedProjectActivityPermission == 'WRITE');
            $rootScope.activityReadPermission = !$rootScope.hasPermission('projectactivity', 'edit') || ($rootScope.loginPersonDetails.external && $rootScope.sharedProjectActivityPermission != 'WRITE');

            $rootScope.showCopyActivityFilesToClipBoard = false;
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;

            $rootScope.showCopyDeliverablesToClipBoard = false;
            $rootScope.clipBoardActivityDeliverables = null;
            $rootScope.clipBoardActivityDeliverables = $application.clipboard.deliverables;

            function back() {
                window.history.back();
                $rootScope.breadCrumb.project = "";
            }

            vm.active = 0;

            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var task = parsed.html($translate.instant("TASKS")).html();
            var files = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var deliverable = parsed.html($translate.instant("DETAILS_TAB_DELIVERABLES")).html();
            var assignedTo = parsed.html($translate.instant("ASSIGNED_TO")).html();
            var referenceItems = parsed.html($translate.instant("REFERENCE_ITEMS")).html();
            vm.showTaskAttributes = parsed.html($translate.instant("SHOW_TASK_ATTRIBUTES")).html();
            vm.addTask = $sce.trustAsHtml(parsed.html($translate.instant("ADD_TASK")).html());
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
            vm.addWorkflowTitle = parsed.html($translate.instant("ADD_WORKFLOW")).html();
            vm.showAll = parsed.html($translate.instant("SHOW_ALL")).html();


            vm.onClear = onClear;
            function onClear() {
                $scope.$broadcast('app.activity.tabActivated', {tabId: 'details.files'});
            }

            vm.activityOpenFrom = null;
            vm.showProject = showProject;
            function showProject() {
                if (vm.activityOpenFrom != null && vm.activityOpenFrom != "null") {
                    $state.go('app.pm.program.details', {
                        programId: vm.activityOpenFrom,
                        tab: 'details.project'
                    });
                } else {
                    $state.go('app.pm.project.details', {projectId: $rootScope.projectInfo.id, tab: 'details.plan'});
                }
            }


            vm.freeTextSearch = freeTextSearch;
            var searchMode = null;
            var freeTextQuery = null;
            $rootScope.freeTextQuerys = null;
            function freeTextSearch(freeText) {
                searchMode = "freetext";
                $rootScope.freeTextQuerys = freeText;
                freeTextQuery = null;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    $scope.$broadcast('app.details.files.search', {name: freeText});
                }
                else {
                    $rootScope.freeTextQuerys = null;
                    $scope.$broadcast('app.activity.tabActivated', {tabId: 'details.files'});
                }
            }


            var share = parsed.html($translate.instant("SHARE")).html();
            vm.detailsShareTitle = parsed.html($translate.instant("PROJECT_SHARE_TITLE")).html();
            vm.shareProjectActivity = shareProjectActivity;
            function shareProjectActivity() {
                var options = {
                    title: share,
                    template: 'app/desktop/modules/shared/share/shareView.jsp',
                    controller: 'ShareController as shareVm',
                    resolve: 'app/desktop/modules/shared/share/shareController',
                    width: 600,
                    showMask: true,
                    data: {
                        sharedItem: vm.activity,
                        itemsSharedType: 'projectActivitySelection',
                        objectType: "PROJECTACTIVITY"
                    },
                    buttons: [
                        {text: share, broadcast: 'app.share.item'}
                    ],
                    callback: function (data) {
                        $rootScope.showSuccessMessage(vm.activity.name + " : Shared successfully");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    index: 0,
                    template: 'app/desktop/modules/pm/project/activity/details/basic/activityBasicInfoView.jsp',
                    active: true,
                    activated: true
                },
                files: {
                    id: 'details.files',
                    heading: files,
                    index: 1,
                    template: 'app/desktop/modules/pm/project/activity/details/files/activityFilesView.jsp',
                    active: false,
                    activated: false
                },
                deliverables: {
                    id: 'details.deliverables',
                    heading: deliverable,
                    index: 2,
                    template: 'app/desktop/modules/pm/project/activity/details/deliverables/activityDeliverablesView.jsp',
                    active: false,
                    activated: false
                },
                itemReferences: {
                    id: 'details.itemReferences',
                    heading: referenceItems,
                    index: 3,
                    template: 'app/desktop/modules/pm/project/activity/details/referenceItems/activityReferenceItemsView.jsp',
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: "Workflow",
                    index: 4,
                    template: 'app/desktop/modules/pm/project/activity/details/workflow/activityWorkflowView.jsp',
                    active: false,
                    activated: false
                },
                activityHistory: {
                    id: 'details.activityHistory',
                    heading: "Timeline",
                    index: 5,
                    template: 'app/desktop/modules/pm/project/activity/details/history/activityHistoryView.jsp',
                    active: false,
                    activated: false
                }
            };

            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.pm.project.activity.details', {
                    activityId: activityId,
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

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            function activityDetailsTabActivated(tabId) {
                $state.transitionTo('app.pm.project.activity.details', {
                    activityId: activityId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.activity.tabActivated', {tabId: tabId});

                }
                /* if (tab != null) {
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

                return tab;
            }

            vm.hasDisplayTab = hasDisplayTab;
            function hasDisplayTab(tabName) {
                var valid = true;

                if (vm.activity != null && vm.activity != undefined && vm.activity.type != null && vm.activity.type.tabs != null) {
                    var index = vm.activity.type.tabs.indexOf(tabName);
                    if (index == -1) {
                        valid = false;
                    }
                }

                return valid;
            }

            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            vm.addWorkflow = addWorkflow;
            function addWorkflow() {
                $scope.$broadcast('app.add.workflow');
            }

            $rootScope.loadActivity = loadActivity;
            function loadActivity() {
                ActivityService.getActivity(activityId).then(
                    function (data) {
                        vm.activity = data;
                        $rootScope.activityInfo = vm.activity;
                        $rootScope.viewInfo.title = vm.activity.name;
                        $rootScope.breadCrumb.project = $rootScope.projectInfo;
                        $rootScope.breadCrumb.activity = $rootScope.activityInfo;
                        $rootScope.breadCrumb.task = "";

                        if (vm.activity.person != null) {
                            $rootScope.viewInfo.description = assignedTo + " : " + vm.activity.person.firstName;
                        }
                        loadActivityTasks();
                        loadActivityCount();
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.finishedTasks = [];
            function loadActivityTasks() {
                ActivityService.getActivityTasks(activityId).then(
                    function (data) {
                        vm.activityTasks = data;
                        vm.finishedTasks = [];
                        angular.forEach(vm.activityTasks, function (task) {
                            if (task.status == "FINISHED") {
                                vm.finishedTasks.push(task);
                            }
                        });
                        vm.loading = false;
                        if (vm.activityTasks.length == vm.finishedTasks.length && vm.activityTasks.length != 0) {
                            vm.activity.status = "FINISHED";
                            ActivityService.updateActivity(vm.activity).then(
                                function (data) {

                                }, function (error) {
                                    //$rootScope.showErrorMessage(error.message);
                                }
                            )
                        } else if (vm.finishedTasks.length > 0 && vm.activityTasks.length > vm.finishedTasks.length) {
                            vm.activity.status = "INPROGRESS";
                            ActivityService.updateActivity(vm.activity).then(
                                function (data) {

                                }, function (error) {
                                    //$rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.showFinishButton = false;
            $rootScope.loadActivityPercentComplete = loadActivityPercentComplete;
            function loadActivityPercentComplete() {
                ActivityService.getActivityPercentComplete(activityId).then(
                    function (data) {
                        vm.activityPercentage = data.percentComplete;
                        if (vm.activityPercentage < 100 && vm.activityPercentage > 0) {
                            vm.activityPercentage = parseInt(vm.activityPercentage);
                        }
                        $rootScope.activityPercent = vm.activityPercentage;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.loadActivityCount = loadActivityCount;
            function loadActivityCount() {
                ActivityService.getActivityCount(activityId).then(
                    function (data) {
                        vm.activityCount = data;
                        var filesTab = document.getElementById("files");
                        var tasks = document.getElementById("tasks");
                        var deliverables = document.getElementById("deliverables");
                        var referenceItems = document.getElementById("referenceItems");

                        filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.activityCount.files);
                        //tasks.lastElementChild.innerHTML = vm.tabs.tasks.heading +
                        //  "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.activityCount.tasks);
                        deliverables.lastElementChild.innerHTML = vm.tabs.deliverables.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.activityCount.deliverables);
                        referenceItems.lastElementChild.innerHTML = vm.tabs.itemReferences.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.activityCount.referenceItems);

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedActivityTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('PROJECTACTIVITY', activityId).then(
                    function (data) {
                        $rootScope.showComments('PROJECTACTIVITY', activityId, data);
                        $rootScope.showTags('PROJECTACTIVITY', activityId, vm.activity.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function downloadFilesAsZip() {
                $rootScope.showBusyIndicator($('.view-container'));
                var url = "{0}//{1}/api/plm/projects/wbs/activities/{2}/files/zip".
                    format(window.location.protocol, window.location.host, activityId);

                //launchUrl(url);
                window.open(url);
                $rootScope.hideBusyIndicator();
            }

            (function () {
                //if ($application.homeLoaded == true) {
                /* if (validateJSON()) {
                 lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedActivityTab"));
                 }

                 $window.localStorage.setItem("lastSelectedActivityTab", "");

                 if (lastSelectedTab != null && lastSelectedTab != undefined) {
                 activityDetailsTabActivated(lastSelectedTab);

                 $timeout(function () {
                 $scope.$broadcast('app.activity.tabActivated', {tabId: lastSelectedTab});
                 }, 1000)
                 }*/
                loadActivity();
                loadActivityPercentComplete();
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedActivityTab"));
                }
                if ($window.localStorage.getItem("shared-permission") != undefined && $window.localStorage.getItem("shared-permission") != null) {
                    var sharedPermission = $window.localStorage.getItem("shared-permission");
                    if (sharedPermission != null && sharedPermission != "") {
                        $rootScope.sharedProjectActivityPermission = sharedPermission;
                    }
                }
                if (tabId != null && tabId != undefined) {
                    activityDetailsTabActivated(tabId);
                    $timeout(function () {
                        $scope.$broadcast('app.activity.tabActivated', {tabId: tabId});
                    }, 1000)
                } else if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    activityDetailsTabActivated(lastSelectedTab);
                    $timeout(function () {
                        $scope.$broadcast('app.activity.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                }
                if ($window.localStorage.getItem("activity_open_from") != undefined && $window.localStorage.getItem("activity_open_from") != null) {
                    vm.activityOpenFrom = $window.localStorage.getItem("activity_open_from");
                }
                $window.localStorage.setItem("lastSelectedActivityTab", "");
                //}
            })();
        }
    }
);