define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/shared/services/core/programService',
        'app/shared/services/core/activityService',
        'app/desktop/modules/pm/project/details/tabs/basic/projectBasicController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/pm/project/details/tabs/team/all/teamController',
        'app/desktop/modules/pm/project/details/tabs/files/projectFilesController',
        'app/desktop/modules/pm/project/details/tabs/reqDocuments/projectReqDocumentsController',
        'app/desktop/modules/pm/project/details/tabs/deliverables/all/deliverablesController',
        'app/desktop/modules/pm/project/details/tabs/plan/projectPlanController',
        'app/desktop/modules/pm/project/details/tabs/plan/oldProjectPlanController',
        'app/desktop/modules/pm/project/details/tabs/history/projectHistoryController',
        'app/desktop/modules/pm/project/details/tabs/itemReferences/projectItemReferenceController',
        'app/desktop/modules/pm/project/details/tabs/workflow/projectWorkflowController',
        'app/shared/services/core/projectTemplateService',
        'app/desktop/modules/pm/project/details/tabs/plan/ganttEditor',
        'app/desktop/directives/plugin-directive/pluginTabsDirective',
        'app/shared/services/core/shareService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'

    ],
    function (module) {
        module.controller('ProjectDetailsController', ProjectDetailsController);

        function ProjectDetailsController($scope, $rootScope, $timeout, $window, $translate, $state, $stateParams, $cookies, ProjectService, ProgramService,
                                          ShareService, ProjectTemplateService, DialogService, CommonService, CommentsService, $application, GanttEditor) {
            //$rootScope.viewInfo.icon =

            var vm = this;
            var projectId = $stateParams.projectId;
            $rootScope.viewInfo.showDetails = true;
            vm.projectId = $stateParams.projectId;

            vm.zoomed = false;
            vm.project = null;
            vm.back = back;
            vm.manager = null;
            vm.expand = false;
            $rootScope.loadProject = loadProject;
            vm.projectDetailsTabActivated = projectDetailsTabActivated;
            vm.newDeliverable = newDeliverable;
            vm.downloadFilesAsZip = downloadFilesAsZip;
            var session = JSON.parse(localStorage.getItem('local_storage_login'));
            $rootScope.loginPersonDetails = session.login;
            $rootScope.external = $rootScope.loginPersonDetails;
            vm.onAddItemFiles = onAddItemFiles;
            $rootScope.selectedProjectId = projectId;
            $rootScope.selectedProject = null;
            $rootScope.objectDeliverables = false;
            $rootScope.projectPercentComplete = 0;
            $rootScope.searchProjectName = null;

            vm.tabId = $stateParams.tab;
            vm.customTabs = [];

            var lastSelectedTab = null;
            vm.projectOpenFrom = null;
            vm.active = 0;

            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var addMember = parsed.html($translate.instant("ADD_MEMBER")).html();
            var deliverable = parsed.html($translate.instant("DETAILS_TAB_DELIVERABLES")).html();
            var requirementDocumentsTitle = parsed.html($translate.instant("REQUIREMENTS")).html();
            var ganttPlan = parsed.html($translate.instant("GANTT_PLAN")).html();
            var plan = parsed.html($translate.instant("PLAN")).html();
            var itemReferences = parsed.html($translate.instant("ITEM_REFERENCES")).html();
            var referenceItems = parsed.html($translate.instant("REFERENCE_ITEMS")).html();
            vm.detailsShareTitle = parsed.html($translate.instant("DETAILS_SHARE_TITLE")).html();
            vm.refreshTitle = parsed.html($translate.instant("CLICK_TO_REFRESH")).html();
            vm.shareProjectTitle = parsed.html($translate.instant("SHARE_PROJECT_TITLE")).html();
            var projectShareTitle = parsed.html($translate.instant("SHARE_PROJECT")).html();
            var share = parsed.html($translate.instant("SHARE")).html();
            var teamTitle = parsed.html($translate.instant("TEAM")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            vm.activityAttribute = parsed.html($translate.instant("ACTIVITY_ATTRIBUTES")).html();
            vm.saveProjectTemplate = parsed.html($translate.instant("PROJECT_TEMPLATE")).html();
            vm.addProjectTemplate = parsed.html($translate.instant("ADD_PROJECT_TEMPLATE")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
            vm.timeline = parsed.html($translate.instant("TIMELINE")).html();
            vm.addWorkflowTitle = parsed.html($translate.instant("ADD_WORKFLOW")).html();
            var savePlan = parsed.html($translate.instant("SAVE_PLAN")).html();
            var planModified = parsed.html($translate.instant("PLAN_MODIFIED")).html();
            var yes = parsed.html($translate.instant("YES")).html();
            var no = parsed.html($translate.instant("NO")).html();
            vm.exportGanttMessage = parsed.html($translate.instant("EXPORT_GANTT")).html();
            vm.detailsShareTitle = parsed.html($translate.instant("PROJECT_SHARE_TITLE")).html();
            vm.notTasksNotificationTitle = parsed.html($translate.instant("NO_TASKS_TO_SEND_NOTIFICATION")).html();
            vm.sendNotificationTitle = parsed.html($translate.instant("SEND_NOTIFICATION_MAIL")).html();

            $rootScope.sharedProjectPermission = null;
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "rowId",
                    order: "DESC"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            vm.projectPersons = angular.copy(pagedResults);

            $rootScope.showCopyProjectFilesToClipBoard = false;
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;

            $rootScope.showCopyDeliverablesToClipBoard = false;
            $rootScope.clipBoardProjectDeliverables = null;
            $rootScope.clipBoardProjectDeliverables = $application.clipboard.deliverables;

            vm.externalPart = null;
            $rootScope.external = vm.externalPart;
            $rootScope.loadProjectCounts = loadProjectCounts;

            function loadProjectCounts() {
                ProjectService.getProjectDetails(projectId).then(
                    function (data) {
                        vm.projectCounts = data;
                        var filesTab = document.getElementById("files");
                        var team = document.getElementById("team");
                        var deliverables = document.getElementById("deliverables");
                        var referenceItems = document.getElementById("referenceItems");
                        var reqDocuments = document.getElementById("project-reqDocuments");
                        if (vm.projectCounts.finishedTasks > 0 && vm.projectCounts.tasks == 0) {
                            vm.notTasksNotificationTitle = parsed.html($translate.instant("PROJECT_ALREADY_FINISHED")).html();
                        }
                        filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.projectCounts.files);
                        team.lastElementChild.innerHTML = teamTitle +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.projectCounts.team);
                        deliverables.lastElementChild.innerHTML = vm.tabs.deliverables.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.projectCounts.deliverables);
                        referenceItems.lastElementChild.innerHTML = vm.tabs.itemReferences.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.projectCounts.referenceItems);
                        reqDocuments.lastElementChild.innerHTML = vm.tabs.reqDocuments.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.projectCounts.requirementDocuments);

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadProjectManager() {
                if (vm.project.projectManager != null && vm.project.projectManager != "") {
                    CommonService.getPerson(vm.project.projectManager).then(
                        function (data) {
                            vm.manager = data;
                            $rootScope.viewInfo.title = vm.project.name;
                            $rootScope.viewInfo.description = "Manager: " + vm.manager.firstName;
                            if (vm.project.program != null && vm.project.program != "") {
                                ProgramService.getProgram(vm.project.program).then(
                                    function (data) {
                                        $rootScope.viewInfo.description = "Program:" + data.name + ", Manager: " + vm.manager.firstName;
                                    }
                                )
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $scope.$broadcast('app.project.tabactivated', {tabId: 'details.files'});
            }

            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            vm.addWorkflow = addWorkflow;
            function addWorkflow() {
                $scope.$broadcast('app.add.workflow');
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
                    if ($rootScope.selectedTab.id == 'details.files') {
                        $scope.$broadcast('app.details.files.search', {name: freeText});
                    } else {
                        $rootScope.onSearchProjectPlan(freeText);
                    }
                }
                else {
                    $rootScope.freeTextQuerys = null;
                    if ($rootScope.selectedTab.id == 'details.files') {
                        $scope.$broadcast('app.project.tabactivated', {tabId: 'details.files'});
                    } else {
                        $rootScope.searchProjectName = null;
                        $scope.$broadcast('app.project.tabactivated', {tabId: 'details.plan'});
                    }
                }
            }

            $rootScope.loadProjectPercentageComplete = loadProjectPercentageComplete;
            function loadProjectPercentageComplete() {
                ProjectService.getProjectPercentageComplete(projectId).then(
                    function (data) {
                        vm.projectPercentage = data.percentComplete;
                        if (vm.projectPercentage < 100 && vm.projectPercentage > 0) {
                            vm.projectPercentage = parseInt(vm.projectPercentage);
                        }
                        $rootScope.projectPercentComplete = vm.projectPercentage;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadTemplates() {
                if (vm.project.program == null || vm.project.program == "") {
                    ProjectTemplateService.getProjectTemplatesByProgramNull().then(
                        function (data) {
                            $rootScope.projectTemplates = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            $rootScope.projectTeamAccess = false;
            $rootScope.projectDetailsPermission = false;
            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    order: "DESC"
                }
            };

            function loadProjectPagedPersons() {
                vm.loading = true;
                ProjectService.getProjectMembers(projectId, pageable).then(
                    function (data) {
                        angular.forEach(data.content, function (person) {
                            if ($rootScope.loginPersonDetails.person.id == person.person) {
                                $rootScope.projectTeamAccess = true;
                            }
                        });
                        $rootScope.projectDetailsPermission = $rootScope.loginPersonDetails.isAdmin || ($rootScope.loginPersonDetails.person.id == $rootScope.projectInfo.projectManager && $rootScope.hasPermission('project', 'edit')) || ($rootScope.projectTeamAccess && $rootScope.hasPermission('project', 'edit') && !$rootScope.loginPersonDetails.external) || ($rootScope.loginPersonDetails.external && $rootScope.sharedProjectPermission == 'WRITE' && $rootScope.hasPermission('project', 'edit')) || $rootScope.hasPermission('file', 'teamCreate');


                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadProject() {
                vm.loading = true;
                ProjectService.getProject(projectId).then(
                    function (data) {
                        vm.project = data;
                        $rootScope.projectInfo = vm.project;
                        $rootScope.selectedProject = vm.project;
                        $rootScope.breadCrumb.activity = "";
                        $rootScope.breadCrumb.project = "";
                        if ($rootScope.projectInfo.makeConversationPrivate == true && $rootScope.projectInfo.teamMember == true) {
                            loadCommentsCount();
                        } else if ($rootScope.projectInfo.makeConversationPrivate == false) {
                            loadCommentsCount();
                        }
                        loadProjectPagedPersons();
                        loadProjectCounts();
                        loadProjectManager();
                        loadProjectPercentageComplete();
                        loadTemplates();

                        vm.project.editMode = false;
                        vm.finishDateFlag = false;
                        $rootScope.projectId = vm.project.id;
                        if (vm.project.plannedStartDate) {
                            vm.project.plannedStartDatede = moment(vm.project.plannedStartDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                        }
                        if (vm.project.plannedFinishDate) {
                            vm.project.plannedFinishDatede = moment(vm.project.plannedFinishDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                        }
                        if (vm.project.actualStartDate) {
                            vm.project.actualStartDatede = moment(vm.project.actualStartDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                        }
                        if (vm.project.actualFinishDate) {
                            vm.project.actualFinishDatede = moment(vm.project.actualFinishDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                        }
                        if (vm.project.minDate) {
                            vm.project.minDate = moment(vm.project.minDate, "DD/MM/YYYY, HH:mm:ss").format("DD/MM/YYYY");
                        }
                        if (vm.project.maxDate) {
                            vm.project.maxDate = moment(vm.project.maxDate, "DD/MM/YYYY, HH:mm:ss").format("DD/MM/YYYY");
                        }
                        CommonService.getPersonReferences([vm.project], 'createdBy');
                        CommonService.getPersonReferences([vm.project], 'projectManager');
                        $rootScope.viewInfo.title = vm.project.name;
                        vm.projectName = $rootScope.viewInfo.title;
                        loadSharedProject();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.hasDisplayTab = hasDisplayTab;
            function hasDisplayTab(tabName) {
                var valid = true;

                if (vm.project != null && vm.project != undefined && vm.project.type != null && vm.project.type.tabs != null) {
                    var index = vm.project.type.tabs.indexOf(tabName);
                    if (index == -1) {
                        valid = false;
                    }
                }

                return valid;
            }

            $rootScope.projectShared = false;
            function loadSharedProject() {
                ShareService.getByObjectId(vm.project.id).then(
                    function (data) {
                        if (data.length > 0) $rootScope.projectShared = true;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.sendProjectTasksNotification = sendProjectTasksNotification;
            function sendProjectTasksNotification() {
                $rootScope.showBusyIndicator();
                ProgramService.sendTasksNotification(vm.project.id, 'PROJECT').then(
                    function (data) {
                        $rootScope.showSuccessMessage("Notification sent successfully");
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function back() {
                window.history.back();
            }

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    index: 0,
                    template: 'app/desktop/modules/pm/project/details/tabs/basic/projectBasicView.jsp',
                    active: true,
                    activated: true
                },
                members: {
                    id: 'details.members',
                    heading: addMember,
                    index: 1,
                    template: 'app/desktop/modules/pm/project/details/tabs/team/all/teamView1.jsp',
                    active: false,
                    activated: false
                },
                plan: {
                    id: 'details.plan',
                    heading: plan,
                    index: 2,
                    template: 'app/desktop/modules/pm/project/details/tabs/plan/projectPlanView.jsp',
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    index: 3,
                    template: 'app/desktop/modules/pm/project/details/tabs/files/projectFilesView.jsp',
                    active: false,
                    activated: false
                },
                reqDocuments: {
                    id: 'details.reqDocuments',
                    heading: requirementDocumentsTitle,
                    index: 4,
                    template: 'app/desktop/modules/pm/project/details/tabs/reqDocuments/projectReqDocumentsView.jsp',
                    active: false,
                    activated: false
                },
                deliverables: {
                    id: 'details.deliverables',
                    heading: deliverable,
                    index: 5,
                    template: 'app/desktop/modules/pm/project/details/tabs/deliverables/all/deliverablesView.jsp',
                    active: false,
                    activated: false
                },
                itemReferences: {
                    id: 'details.itemReferences',
                    heading: referenceItems,
                    index: 6,
                    template: 'app/desktop/modules/pm/project/details/tabs/itemReferences/projectItemReferenceView.jsp',
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: "Workflow",
                    index: 7,
                    template: 'app/desktop/modules/pm/project/details/tabs/workflow/projectWorkflowView.jsp',
                    active: false,
                    activated: false
                },
                projectHistory: {
                    id: 'details.projectHistory',
                    heading: "Timeline",
                    index: 8,
                    template: 'app/desktop/modules/pm/project/details/tabs/history/projectHistoryView.jsp',
                    active: false,
                    activated: false
                },
                /*oldPlan: {
                 id: 'details.oldPlan',
                 heading: plan,
                 index: 8,
                 template: 'app/desktop/modules/pm/project/details/tabs/plan/oldProjectPlanView.jsp',
                 active: false,
                 activated: false
                 }*/
            };

            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.plan';
                $state.transitionTo('app.pm.project.details', {
                    projectId: projectId,
                    tab: 'details.plan'
                }, {notify: false});
                vm.active = 2;
            }
            else {
                var tab = getTabById(tabId);
                if (tab != null) {
                    //vm.active = tab.index;
                }
            }

            vm.expandAll = expandAll;
            vm.collapseAll = collapseAll;
            vm.toggleGantt = toggleGantt;
            vm.toggleGrid = GanttEditor.toggleGrid;
            vm.toggleMode = toggleMode;
            vm.saveGantt = saveGantt;
            vm.setScales = setScales;
            vm.exportGantt = exportGantt;
            function setScales(scale) {
                vm.scale = scale;
                gantt.config.scale_unit = scale;
                gantt.render();
            }

            function toggleGantt() {
                $rootScope.showGantt = !$rootScope.showGantt;
                gantt.config.show_chart = $rootScope.showGantt;
                gantt.render();
            }

            function exportGantt() {
                gantt.exportToPDF({
                    name: vm.projectName + ".pdf",
                    header: "<style>#gantt_here{left:-${i}px;position: absolute;} .week_end{background-color: #EFF5FD !important;}</style>",
                    skin: 'meadow', //skyblue, meadow, broadway, terrace
                    locale: "en",
                    raw: true
                });
            }

            function expandAll() {
                gantt.eachTask(function (task) {
                    task.$open = true;
                });
                gantt.render();
                vm.expand = false;
            }

            function collapseAll() {
                gantt.eachTask(function (task) {
                    task.$open = false;
                });
                gantt.render();
                vm.expand = true;
            }

            function saveGantt() {
                $scope.$broadcast('app.project.createSaveGantt');
            }


            function toggleMode() {
                GanttEditor.toggleMode(vm.zoomed);
                vm.zoomed = !vm.zoomed;
            }

            vm.refreshDetails = refreshDetails;

            function refreshDetails() {
                $scope.$broadcast('app.project.tabactivated', {tabId: $rootScope.selectedTab.id});
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
                    JSON.parse($window.localStorage.getItem("lastSelectedProjectTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            $rootScope.arrangeFreeTextSearch = arrangeFreeTextSearch;
            function arrangeFreeTextSearch(tab) {
                if ($rootScope.loginPersonDetails.external == false) {
                    $timeout(function () {
                        var freeTextSearch = document.getElementById("freeTextSearchDirective");
                        if (freeTextSearch != null) {
                            if (tab.id == "details.plan") {
                                freeTextSearch.style.right = 270 + "px";
                            } else {
                                freeTextSearch.style.right = 20 + "px";
                            }
                        }
                    }, 1000)
                }
            }

            function projectDetailsTabActivated(tabId) {
                if ($rootScope.ganttUpdated && !$rootScope.loginPersonDetails.external) {
                    var options = {
                        title: savePlan,
                        message: planModified,
                        okButtonText: yes,
                        cancelButtonText: no
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes) {
                            $rootScope.saveGantt();
                            $rootScope.ganttUpdated = false;
                            navigateTab(tabId);
                        }
                        else {
                            $rootScope.ganttUpdated = false;
                            navigateTab(tabId);
                        }
                    });
                } else {
                    $rootScope.ganttUpdated = false;
                    navigateTab(tabId);
                }
                /* if (tab != null) {
                 activateTab(tab);
                 }*/
            }

            vm.showAllProjects = showAllProjects;
            function showAllProjects() {
                if ($rootScope.ganttUpdated) {
                    var options = {
                        title: savePlan,
                        message: planModified,
                        okButtonText: yes,
                        cancelButtonText: no
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes) {
                            $rootScope.saveGantt();
                            $rootScope.ganttUpdated = false;
                            //tabActive("details.plan");
                        }
                        else {
                            $rootScope.ganttUpdated = false;
                            if (vm.projectOpenFrom != null && vm.projectOpenFrom != "null") {
                                $state.go('app.pm.program.details', {
                                    programId: vm.projectOpenFrom,
                                    tab: 'details.project'
                                });
                            } else {
                                $state.go('app.pm.project.all');
                            }
                        }
                    });
                } else {
                    if (vm.projectOpenFrom != null && vm.projectOpenFrom != "null") {
                        $state.go('app.pm.program.details', {
                            programId: vm.projectOpenFrom,
                            tab: 'details.project'
                        });
                    } else {
                        $state.go('app.pm.project.all');
                    }
                }
            }


            function tabActive(tabId) {
                if (tabId != "details.basic") {
                    if (vm.project != null && vm.project != undefined) {
                        if (vm.project.plannedStartDate != null && vm.project.plannedStartDate != "" && vm.project.plannedFinishDate != null && vm.project.plannedFinishDate != "") {
                            navigateTab(tabId);
                        } else {
                            $rootScope.showWarningMessage("Please add project planned start and finish dates");
                            $timeout(function () {
                                tabId = "details.basic";
                                tabActive(tabId);
                            }, 100);
                        }
                    }
                } else {
                    navigateTab(tabId);
                }
            }

            $rootScope.navigateProjectTab = navigateTab;
            function navigateTab(tabId) {
                $state.transitionTo('app.pm.project.details', {
                    projectId: projectId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.project.tabactivated', {tabId: tabId});
                    if (tab.id == "details.plan" || tab.id == "details.files") {
                        arrangeFreeTextSearch(tab);
                    }

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

            function newDeliverable() {
                $scope.$broadcast('app.pm.deliverables');
            }

            function onAddItemFiles() {
                $scope.$broadcast('app.project.addfiles');
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('PROJECT', projectId).then(
                    function (data) {
                        $rootScope.showComments('PROJECT', projectId, data);
                        $rootScope.showTags('PROJECT', projectId, vm.project.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.downloadFilesAsZip = downloadFilesAsZip;
            function downloadFilesAsZip() {
                $rootScope.showBusyIndicator($('.view-container'));
                var url = "{0}//{1}/api/plm/projects/{2}/files/zip".
                    format(window.location.protocol, window.location.host, projectId);

                //launchUrl(url);
                window.open(url);
                $rootScope.hideBusyIndicator();
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadProject();
                $rootScope.ganttUpdated = false;
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedProjectTab"));
                }
                if ($window.localStorage.getItem("shared-permission") != undefined && $window.localStorage.getItem("shared-permission") != null) {
                    var sharedPermission = $window.localStorage.getItem("shared-permission");
                    if (sharedPermission != null && sharedPermission != "") {
                        $rootScope.sharedProjectPermission = sharedPermission;
                    }
                }
                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    projectDetailsTabActivated(lastSelectedTab);
                    $timeout(function () {
                        $scope.$broadcast('app.project.tabactivated', {tabId: lastSelectedTab});
                    }, 200)
                } else if (tabId != null && tabId != undefined) {
                    projectDetailsTabActivated(tabId);
                    $timeout(function () {
                        $scope.$broadcast('app.project.tabactivated', {tabId: tabId});
                    }, 200)
                }
                if ($window.localStorage.getItem("project_open_from") != undefined && $window.localStorage.getItem("project_open_from") != null) {
                    vm.projectOpenFrom = $window.localStorage.getItem("project_open_from");
                }
                $window.localStorage.setItem("lastSelectedProjectTab", "");
                $window.localStorage.setItem("lastSelectedActivityTab", "");
                //}

            })();
        }
    }
);

