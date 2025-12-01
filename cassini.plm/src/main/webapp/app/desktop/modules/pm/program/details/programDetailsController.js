define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/programService',
        'app/desktop/modules/pm/program/details/tabs/basic/programBasicController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/pm/program/details/tabs/resources/programResourcesController',
        'app/desktop/modules/pm/program/details/tabs/files/programFilesController',
        'app/desktop/modules/pm/program/details/tabs/project/programProjectsDrillDownController',
        'app/desktop/modules/pm/program/details/tabs/timeline/programTimelineController',
        'app/desktop/modules/pm/program/details/tabs/workflow/programWorkflowController',
        'app/desktop/directives/plugin-directive/pluginTabsDirective',
        'app/shared/services/core/shareService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'

    ],
    function (module) {
        module.controller('ProgramDetailsController', ProgramDetailsController);

        function ProgramDetailsController($scope, $rootScope, $timeout, $window, $translate, $state, $stateParams, $cookies, ProgramService,
                                          ShareService, CommonService, CommentsService, $application) {

            var vm = this;
            var programId = $stateParams.programId;
            $rootScope.viewInfo.showDetails = true;
            vm.programId = $stateParams.programId;

            vm.zoomed = false;
            vm.project = null;
            vm.back = back;
            vm.manager = null;
            vm.expand = false;
            $rootScope.loadProgram = loadProgram;
            vm.programDetailsTabActivated = programDetailsTabActivated;
            vm.downloadFilesAsZip = downloadFilesAsZip;
            var session = JSON.parse(localStorage.getItem('local_storage_login'));
            $rootScope.loginPersonDetails = session.login;
            $rootScope.external = $rootScope.loginPersonDetails;
            $rootScope.selectedProjectId = programId;
            $rootScope.selectedProgram = null;
            $rootScope.projectPercentComplete = 0;

            vm.tabId = $stateParams.tab;
            vm.customTabs = [];

            var lastSelectedTab = null;
            vm.active = 0;

            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            vm.detailsShareTitle = parsed.html($translate.instant("DETAILS_SHARE_TITLE")).html();
            vm.refreshTitle = parsed.html($translate.instant("CLICK_TO_REFRESH")).html();
            vm.shareProjectTitle = parsed.html($translate.instant("SHARE_PROJECT_TITLE")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            vm.activityAttribute = parsed.html($translate.instant("ACTIVITY_ATTRIBUTES")).html();
            vm.saveProjectTemplate = parsed.html($translate.instant("SAVE_PROGRAM_AS_TEMPLATE")).html();
            vm.addProjectTemplate = parsed.html($translate.instant("ADD_PROJECT_TEMPLATE")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
            vm.timeline = parsed.html($translate.instant("TIMELINE")).html();
            vm.addWorkflowTitle = parsed.html($translate.instant("ADD_WORKFLOW")).html();
            vm.sendNotificationTitle = parsed.html($translate.instant("SEND_NOTIFICATION_MAIL")).html();
            vm.notTasksNotificationTitle = parsed.html($translate.instant("NO_TASKS_TO_SEND_NOTIFICATION")).html();

            $rootScope.sharedProgramPermission = null;
            $rootScope.showCopyProjectFilesToClipBoard = false;
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;

            vm.externalPart = null;
            $rootScope.external = vm.externalPart;
            $rootScope.loadProgramCounts = loadProgramCounts;

            function loadProgramCounts() {
                ProgramService.getProgramCounts(programId).then(
                    function (data) {
                        vm.programCounts = data;
                        var filesTab = document.getElementById("program-files");
                        var resources = document.getElementById("program-resources");
                        var projects = document.getElementById("program-projects");
                        if (filesTab != undefined && filesTab != null) {
                            filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.programCounts.files);
                        }
                        if (resources != undefined && resources != null) {
                            resources.lastElementChild.innerHTML = vm.tabs.resources.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.programCounts.resourcesCount);
                        }
                        if (projects != undefined && projects != null) {
                            projects.lastElementChild.innerHTML = vm.tabs.project.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.programCounts.projectCount);
                        }
                        if (vm.programCounts.finishedTasks > 0 && vm.programCounts.tasks == 0) {
                            vm.notTasksNotificationTitle = parsed.html($translate.instant("PROGRAM_ALREADY_FINISHED")).html();
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadProjectManager() {
                CommonService.getPerson(vm.program.programManager).then(
                    function (data) {
                        vm.manager = data;
                        $rootScope.viewInfo.title = vm.program.name;
                        $rootScope.viewInfo.description = "Manager: " + vm.manager.firstName;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            vm.onClear = onClear;
            function onClear() {
                if ($rootScope.selectedTab.id == 'details.files') {
                    $scope.$broadcast('app.program.tabactivated', {tabId: 'details.files'});
                } else {
                    $rootScope.loadProgramProjects();
                }
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
                        $rootScope.loadProgramProjectsBySearchQuery(freeText);
                    }
                }
                else {
                    $rootScope.freeTextQuerys = null;
                    if ($rootScope.selectedTab.id == 'details.files') {
                        $scope.$broadcast('app.program.tabactivated', {tabId: 'details.files'});
                    } else {
                        $rootScope.loadProgramProjects();
                    }
                }
            }

            $rootScope.programResourceAccess = false;
            $rootScope.programDetailsPermission = false;


            function loadProjectResources() {
                vm.loading = true;
                vm.programResources = [];
                ProgramService.getProgramResources(programId).then(
                    function (data) {
                        angular.forEach(data, function (person) {
                            if ($rootScope.loginPersonDetails.person.id == person.person) {
                                $rootScope.programResourceAccess = true;
                            }
                        });
                        $rootScope.programDetailsPermission = $rootScope.loginPersonDetails.isAdmin || ($rootScope.loginPersonDetails.person.id == $rootScope.programInfo.programManager && $rootScope.hasPermission('program', 'edit')) || ($rootScope.programResourceAccess && $rootScope.hasPermission('program', 'edit') && !$rootScope.loginPersonDetails.external) || ($rootScope.loginPersonDetails.external && $rootScope.sharedProgramPermission == 'WRITE' && $rootScope.hasPermission('program', 'edit')) || $rootScope.hasPermission('file', 'teamCreate') ;


                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            $rootScope.loadProgram = loadProgram;
            function loadProgram() {
                vm.loading = true;
                ProgramService.getProgram(programId).then(
                    function (data) {
                        vm.program = data;
                        $rootScope.programInfo = vm.program;
                        $rootScope.selectedProgram = vm.program;
                        loadProjectResources();
                        loadCommentsCount();
                        loadProgramCounts();
                        loadProjectManager();
                        if (vm.program.percentComplete < 100 && vm.program.percentComplete > 0) {
                            vm.program.percentComplete = parseInt(vm.program.percentComplete);
                        }
                        vm.program.editMode = false;
                        vm.finishDateFlag = false;
                        $rootScope.programId = vm.program.id;
                        CommonService.getPersonReferences([vm.program], 'createdBy');
                        CommonService.getPersonReferences([vm.program], 'projectManager');
                        $rootScope.viewInfo.title = vm.program.name;
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

                if (vm.program != null && vm.program != undefined && vm.program.type != null && vm.program.type.tabs != null) {
                    var index = vm.program.type.tabs.indexOf(tabName);
                    if (index == -1) {
                        valid = false;
                    }
                }

                return valid;
            }

            $rootScope.projectShared = false;
            function loadSharedProject() {
                ShareService.getByObjectId(vm.program.id).then(
                    function (data) {
                        if (data.length > 0) $rootScope.projectShared = true;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
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
                    template: 'app/desktop/modules/pm/program/details/tabs/basic/programBasicView.jsp',
                    active: true,
                    activated: true
                },
                resources: {
                    id: 'details.resources',
                    heading: "Resources",
                    index: 1,
                    template: 'app/desktop/modules/pm/program/details/tabs/resources/programResourcesView.jsp',
                    active: false,
                    activated: false
                },
                project: {
                    id: 'details.project',
                    heading: "Projects",
                    index: 2,
                    template: 'app/desktop/modules/pm/program/details/tabs/project/programProjectsDrillDownView.jsp',
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    index: 3,
                    template: 'app/desktop/modules/pm/program/details/tabs/files/programFilesView.jsp',
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: "Workflow",
                    index: 4,
                    template: 'app/desktop/modules/pm/program/details/tabs/workflow/programWorkflowView.jsp',
                    active: false,
                    activated: false
                },
                timeline: {
                    id: 'details.timeline',
                    heading: "Timeline",
                    index: 5,
                    template: 'app/desktop/modules/pm/program/details/tabs/timeline/programTimelineView.jsp',
                    active: false,
                    activated: false
                }
            };

            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.plan';
                $state.transitionTo('app.pm.program.details', {
                    programId: programId,
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

            vm.refreshDetails = refreshDetails;

            function refreshDetails() {
                $scope.$broadcast('app.program.tabactivated', {tabId: $rootScope.selectedTab.id});
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
                    JSON.parse($window.localStorage.getItem("lastSelectedProgramTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function programDetailsTabActivated(tabId) {
                $state.transitionTo('app.pm.program.details', {
                    programId: vm.programId,
                    tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.program.tabactivated', {tabId: tabId});
                }
            }

            vm.showAllPrograms = showAllPrograms;
            function showAllPrograms() {
                $state.go('app.pm.program.all');
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

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('PROGRAM', programId).then(
                    function (data) {
                        $rootScope.showComments('PROGRAM', programId, data);
                        $rootScope.showTags('PROGRAM', programId, vm.program.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.downloadFilesAsZip = downloadFilesAsZip;
            function downloadFilesAsZip() {
                $rootScope.showBusyIndicator($('.view-container'));
                var url = "{0}//{1}/api/plm/programs/{2}/files/zip".
                    format(window.location.protocol, window.location.host, programId);

                //launchUrl(url);
                window.open(url);
                $rootScope.hideBusyIndicator();
            }

            var newTemplate = parsed.html($translate.instant("NEW_TEMPLATE")).html();
            var templateCreatedMessage = parsed.html($translate.instant("TEMPLATE_CREATE_MESSAGE")).html();
            var createTitle = parsed.html($translate.instant("CREATE")).html();
            vm.createNewTemplate = createNewTemplate;
            function createNewTemplate() {
                var options = {
                    title: newTemplate,
                    showMask: true,
                    template: 'app/desktop/modules/pm/programTemplate/new/newProgramTemplateView.jsp',
                    controller: 'NewProgramTemplateController as newProgramTemplateVm',
                    resolve: 'app/desktop/modules/pm/programTemplate/new/newProgramTemplateController',
                    width: 550,
                    data: {
                        selectedProgramTemplateId: vm.program.id
                    },
                    buttons: [
                        {text: createTitle, broadcast: 'app.program.template.new'}
                    ],
                    callback: function () {
                        $rootScope.showSuccessMessage(templateCreatedMessage);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var share = parsed.html($translate.instant("SHARE")).html();
            vm.detailsShareTitle = parsed.html($translate.instant("PROGRAM_SHARE_TITLE")).html();
            var shareProgramTitle = parsed.html($translate.instant("SHARE_PROGRAM")).html();
            $rootScope.shareProgram = shareProgram;
            function shareProgram(program) {
                var options = {
                    title: shareProgramTitle,
                    template: 'app/desktop/modules/shared/share/shareView.jsp',
                    controller: 'ShareController as shareVm',
                    resolve: 'app/desktop/modules/shared/share/shareController',
                    width: 600,
                    showMask: true,
                    data: {
                        sharedItem: program,
                        itemsSharedType: 'itemSelection',
                        objectType: "PROGRAM"
                    },
                    buttons: [
                        {text: share, broadcast: 'app.share.item'}
                    ],
                    callback: function (data) {
                        $rootScope.showSuccessMessage(program.name + " : Shared successfully");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.sendTasksNotification = sendTasksNotification;
            function sendTasksNotification() {
                $rootScope.showBusyIndicator();
                ProgramService.sendTasksNotification(vm.programId, 'PROGRAM').then(
                    function (data) {
                        $rootScope.showSuccessMessage("Notification sent successfully");
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                loadProgram();
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedProgramTab"));
                }
                if ($window.localStorage.getItem("shared-permission") != undefined && $window.localStorage.getItem("shared-permission") != null) {
                    var sharedPermission = $window.localStorage.getItem("shared-permission");
                    if (sharedPermission != null && sharedPermission != "") {
                        $rootScope.sharedProgramPermission = sharedPermission;
                    }
                }
                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    programDetailsTabActivated(lastSelectedTab);
                    $timeout(function () {
                        $scope.$broadcast('app.program.tabactivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    programDetailsTabActivated(tabId);
                    $timeout(function () {
                        $scope.$broadcast('app.program.tabactivated', {tabId: tabId});
                    }, 500)
                }

                $window.localStorage.setItem("lastSelectedProgramTab", "");
            })();
        }
    }
);

