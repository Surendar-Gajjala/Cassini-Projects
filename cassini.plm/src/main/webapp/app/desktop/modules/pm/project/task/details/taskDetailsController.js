define(
    [
        'app/desktop/modules/pm/project/task/task.module',
        'app/desktop/modules/pm/project/task/details/basic/taskBasicInfoController',
        'app/desktop/modules/pm/project/task/details/files/taskFilesController',
        'app/desktop/modules/pm/project/task/details/deliverables/taskDeliverablesController',
        'app/desktop/modules/pm/project/task/details/referenceItems/taskReferenceItemsController',
        'app/desktop/modules/pm/project/task/details/workflow/taskWorkflowController',
        'app/desktop/modules/pm/project/task/details/history/taskHistoryController',
        'app/shared/services/core/activityService'

    ],
    function (module) {
        module.controller('TaskDetailsController', TaskDetailsController);

        function TaskDetailsController($scope, $application, $stateParams, $rootScope, $window, $translate, $timeout, $state,
                                       ActivityService, CommentsService) {

            var vm = this;
            $rootScope.viewInfo.showDetails = true;
            var activityId = $stateParams.activityId;
            var taskId = $stateParams.taskId;
            vm.fromMyTaskWidget = $rootScope.fromMyTaskWidget;
            vm.taskDetailsTabActivated = taskDetailsTabActivated;
            vm.downloadFilesAsZip = downloadFilesAsZip;
            vm.back = back;
            vm.taskPercentage = 0;
            $rootScope.taskPercent = 0;
            var lastSelectedTab = null;
            vm.active = 0;

            function back() {
                window.history.back();
            }

            $rootScope.showCopyTaskFilesToClipBoard = false;
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;

            $rootScope.showCopyDeliverablesToClipBoard = false;
            $rootScope.clipBoardTaskDeliverables = null;
            $rootScope.clipBoardTaskDeliverables = $application.clipboard.deliverables;

            $rootScope.sharedProjectTaskPermission = null;

            $rootScope.taskWritePermission = $rootScope.hasPermission('projecttask', 'edit') && (!$rootScope.loginPersonDetails.external || $rootScope.sharedProjectTaskPermission == 'WRITE');
            $rootScope.taskReadPermission = !$rootScope.hasPermission('projecttask', 'edit') || ($rootScope.loginPersonDetails.external && $rootScope.sharedProjectTaskPermission != 'WRITE');

            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var task = parsed.html($translate.instant("TASKS")).html();
            var files = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var deliverable = parsed.html($translate.instant("DETAILS_TAB_DELIVERABLES")).html();
            var assignedTo = parsed.html($translate.instant("ASSIGNED_TO")).html();
            var referenceItems = parsed.html($translate.instant("REFERENCE_ITEMS")).html();
            $scope.finishTask = parsed.html($translate.instant("FINISH_TASK_TITLE")).html();
            vm.showAll = parsed.html($translate.instant("SHOW_ALL")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
            vm.addWorkflowTitle = parsed.html($translate.instant("ADD_WORKFLOW")).html();

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/pm/project/task/details/basic/taskBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                files: {
                    id: 'details.files',
                    heading: files,
                    template: 'app/desktop/modules/pm/project/task/details/files/taskFilesView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                deliverables: {
                    id: 'details.deliverables',
                    heading: deliverable,
                    template: 'app/desktop/modules/pm/project/task/details/deliverables/taskDeliverablesView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                itemReferences: {
                    id: 'details.itemReferences',
                    heading: referenceItems,
                    template: 'app/desktop/modules/pm/project/task/details/referenceItems/taskReferenceItemsView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: 'Workflow',
                    template: 'app/desktop/modules/pm/project/task/details/workflow/taskWorkflowView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                },
                taskHistory: {
                    id: 'details.taskHistory',
                    heading: "Timeline",
                    index: 5,
                    template: 'app/desktop/modules/pm/project/task/details/history/taskHistoryView.jsp',
                    active: false,
                    activated: false
                }
            };

            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.pm.project.activity.task.details', {
                    activityId: activityId,
                    taskId: taskId,
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

            vm.hasDisplayTab = hasDisplayTab;
            function hasDisplayTab(tabName) {
                var valid = true;

                if (vm.task != null && vm.task != undefined && vm.task.type != null && vm.task.type.tabs != null) {
                    var index = vm.task.type.tabs.indexOf(tabName);
                    if (index == -1) {
                        valid = false;
                    }
                }

                return valid;
            }


            vm.onClear = onClear;
            function onClear() {
                $scope.$broadcast('app.activity.tabActivated', {tabId: 'details.files'});
            }

            vm.taskOpenFrom = null;
            vm.showProject = showProject;
            function showProject() {
                if (vm.taskOpenFrom != null && vm.taskOpenFrom != "null") {
                    $state.go('app.pm.program.details', {
                        programId: vm.taskOpenFrom,
                        tab: 'details.project'
                    });
                } else {
                    $state.go('app.pm.project.details', {projectId: $rootScope.projectId, tab: 'details.plan'});
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
                    $scope.$broadcast('app.activity.tasks.tabActivated', {tabId: 'details.files'});
                }
            }

            var share = parsed.html($translate.instant("SHARE")).html();
            vm.detailsShareTitle = parsed.html($translate.instant("PROJECT_SHARE_TITLE")).html();
            vm.shareProjectTask = shareProjectTask;
            function shareProjectTask() {
                var options = {
                    title: share,
                    template: 'app/desktop/modules/shared/share/shareView.jsp',
                    controller: 'ShareController as shareVm',
                    resolve: 'app/desktop/modules/shared/share/shareController',
                    width: 600,
                    showMask: true,
                    data: {
                        sharedItem: vm.task,
                        itemsSharedType: 'projectTaskSelection',
                        objectType: "PROJECTTASK"
                    },
                    buttons: [
                        {text: share, broadcast: 'app.share.item'}
                    ],
                    callback: function (data) {
                        $rootScope.showSuccessMessage(vm.task.name + " : Shared successfully");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            function taskDetailsTabActivated(tabId) {
                $state.transitionTo('app.pm.project.activity.task.details', {
                    activityId: activityId, taskId: taskId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.activity.tasks.tabActivated', {tabId: tabId});

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

            vm.complateTask = complateTask;
            function complateTask() {
                var args = {};
                args.taskId = taskId;
                args.activityId = activityId;
                args.fromTaskDetails = true;
                $rootScope.finishActivityTask(null, args);
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedTaskTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            $rootScope.loadTaskDelivarables = loadTaskDelivarables;
            function loadTaskDelivarables() {
                ActivityService.getAllTaskDeliverables(activityId, taskId).then(
                    function (data) {
                        var taskPending = false;
                        vm.itemDeliverables = data.itemDeliverables;
                        if (vm.itemDeliverables.length > 0) {
                            angular.forEach(vm.itemDeliverables, function (val) {
                                if (val.deliverableStatus == "PENDING") {
                                    taskPending = true;
                                } else if (!taskPending && val.deliverableStatus == "FINISHED") {
                                    $rootScope.taskComplete = true;
                                }
                            });
                        }
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.loadTask = loadTask;
            function loadTask() {
                ActivityService.getActivityTask(activityId, taskId).then(
                    function (data) {
                        vm.task = data;
                        $rootScope.taskPercentage = vm.task.percentComplete;
                        $rootScope.viewInfo.title = vm.task.name;
                        $rootScope.viewInfo.description = vm.task.description;
                        $rootScope.breadCrumb.project = $rootScope.projectInfo;
                        $rootScope.breadCrumb.activity = $rootScope.activityInfo;
                        $rootScope.breadCrumb.task = vm.task;
                        $rootScope.task = vm.task;
                        loadTaskCount();
                        loadCommentsCount();
                        $rootScope.loadTaskDelivarables();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            $rootScope.loadTaskCount = loadTaskCount;
            function loadTaskCount() {
                ActivityService.getTaskCount(taskId).then(
                    function (data) {
                        vm.taskCount = data;
                        var filesTab = document.getElementById("files");
                        var deliverables = document.getElementById("deliverables");
                        var referenceItems = document.getElementById("referenceItems");

                        filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.taskCount.files);
                        deliverables.lastElementChild.innerHTML = vm.tabs.deliverables.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.taskCount.deliverables);
                        referenceItems.lastElementChild.innerHTML = vm.tabs.itemReferences.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.taskCount.referenceItems);

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('PROJECTTASK', taskId).then(
                    function (data) {
                        $rootScope.showComments('PROJECTTASK', taskId, data);
                        $rootScope.showTags('PROJECTTASK', taskId, vm.task.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function downloadFilesAsZip() {
                $rootScope.showBusyIndicator($('.view-container'));
                var url = "{0}//{1}/api/plm/projects/wbs/activities/tasks/{2}/files/zip".
                    format(window.location.protocol, window.location.host, taskId);

                //launchUrl(url);
                window.open(url);
                $rootScope.hideBusyIndicator();
            }


            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            vm.addWorkflow = addWorkflow;
            function addWorkflow() {
                $scope.$broadcast('app.add.workflow');
            }

            (function () {
                //if ($application.homeLoaded == true) {

                loadTask();
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedTaskTab"));
                }
                if ($window.localStorage.getItem("shared-permission") != undefined && $window.localStorage.getItem("shared-permission") != null) {
                    var sharedPermission = $window.localStorage.getItem("shared-permission");
                    if (sharedPermission != null && sharedPermission != "") {
                        $rootScope.sharedProjectTaskPermission = sharedPermission;
                    }
                }
                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    taskDetailsTabActivated(lastSelectedTab);
                    $timeout(function () {
                        $scope.$broadcast('app.activity.tasks.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    taskDetailsTabActivated(tabId);
                    $timeout(function () {
                        $scope.$broadcast('app.activity.tasks.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                if ($window.localStorage.getItem("task_open_from") != undefined && $window.localStorage.getItem("task_open_from") != null) {
                    vm.taskOpenFrom = $window.localStorage.getItem("task_open_from");
                }
                $window.localStorage.setItem("lastSelectedTaskTab", "");

                //}
            })();
        }
    }
);