define(['app/desktop/desktop.app',
        'app/shared/services/tm/taskService',
        'app/shared/services/pm/project/wbsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'

    ],
    function (module) {
        module.controller('ProjectNavController', ProjectNavController);

        function ProjectNavController($scope, $rootScope, $timeout, $state, $cookies, $stateParams, CommonService, WbsService, TaskService) {
            var vm = this;

            vm.loadAssignedTasks = loadAssignedTasks;
            vm.loadInProgressTasks = loadInProgressTasks;
            vm.clear = clear;
            $rootScope.assignedTasks = [];
            $rootScope.inprogressTasks = [];
            vm.assignedClick = assignedClick;
            vm.inProgressClick = inProgressClick;
            vm.setValues = setValues;
            vm.flag = false;
            vm.activateProjectTab = activateProjectTab;
            vm.activeProject = {
                name: ""
            };

            vm.emptyFilters = {
                name: null,
                description: null,
                person: null,
                wbsItem: null,
                status: null,
                percentComplete: null,
                personObject: null,
                wbsItemObject: null,
                percentCompleteObject: null,
                plannedStartDate: null,
                plannedFinishDate: null,
                actualStartDate: null,
                actualFinishDate: null,
                searchQuery: null
            };

            vm.filters = angular.copy(vm.emptyFilters);
            vm.navItems = [];
            var lastSelectedItem = null;

            var navTabss = [
                {
                    id: "project.home",
                    label: "Details",
                    state: "app.pm.project.home",
                    active: true
                },
                {
                    id: "project.admin",
                    label: "Team",
                    state: "app.pm.project.projectAdmin.team",
                    active: false
                },
                {
                    id: "project.wbs",
                    label: "WBS",
                    state: "app.pm.project.wbs",
                    active: false
                },
                {
                    id: "project.works",
                    label: "WORKS",
                    state: "app.pm.project.works",
                    active: false
                },
                {
                    id: "project.bom",
                    label: "BOQ",
                    state: "app.pm.project.bom",
                    active: false
                },
                {
                    id: "project.tasks",
                    label: "Tasks",
                    state: "app.pm.project.tasks",
                    active: false
                },
                {
                    id: "project.documents",
                    label: "Documents",
                    state: "app.pm.project.documents",
                    active: false
                },
                {
                    id: "project.media",
                    label: "Media",
                    state: "app.pm.project.media.all",
                    active: false
                },
                {
                    id: "project.issues",
                    label: "Problems",
                    state: "app.pm.project.issues",
                    active: false
                },
                {
                    id: "project.site",
                    label: "Sites",
                    state: "app.pm.project.sites.all",
                    active: false
                },

                {
                    id: "project.communication",
                    label: "Communication",
                    state: "app.pm.project.communication",
                    active: false
                },
                {
                    id: "project.meetings",
                    label: "Meetings",
                    state: "app.pm.project.meetings",
                    active: false
                }
            ];

            function tabs() {
                vm.navItems = navTabss;
                /*angular.forEach(navTabss, function (tab) {
                    if (tab.id == 'project.home') {
                        vm.navItems.push(tab);
                    }
                     if (tab.id == 'project.admin') {
                        if ($rootScope.login.person.isProjectOwner || $rootScope.hasPermission('permission.team.view') || $rootScope.hasPermission('permission.team.addPerson') || $rootScope.hasPermission('permission.team.editTeam') || $rootScope.hasPermission('permission.team.roles') || $rootScope.hasPermission('permission.team.newRole') || $rootScope.hasPermission('permission.team.deleteRole')) {
                            vm.navItems.push(tab);
                        }
                    }
                     if (tab.id == 'project.works') {
                        if ($rootScope.login.person.isProjectOwner || $rootScope.hasPermission('permission.wbs.view') || $rootScope.hasPermission('permission.wbs.create') || $rootScope.hasPermission('permission.wbs.edit')) {
                            vm.navItems.push(tab);
                        }
                    }
                     if (tab.id == 'project.bom') {
                        if ($rootScope.login.person.isProjectOwner || $rootScope.hasPermission('permission.bom.view') || $rootScope.hasPermission('permission.bom.create') || $rootScope.hasPermission('permission.bom.edit') || $rootScope.hasPermission('permission.bom.import') || $rootScope.hasPermission('permission.bom.export')) {
                            vm.navItems.push(tab);
                        }
                    }
                     if (tab.id == 'project.tasks') {
                        if ($rootScope.login.person.isProjectOwner || $rootScope.hasPermission('permission.tasks.view') || $rootScope.hasPermission('permission.tasks.add') || $rootScope.hasPermission('permission.tasks.edit') || $rootScope.hasPermission('permission.tasks.delete') || $rootScope.hasPermission('permission.tasks.uploadMedia')) {
                            $rootScope.taskPage = 0;
                            vm.navItems.push(tab);
                        }
                    }
                     if (tab.id == 'project.documents') {
                        if ($rootScope.login.person.isProjectOwner || $rootScope.hasPermission('permission.files.view') || $rootScope.hasPermission('permission.files.addFolder') || $rootScope.hasPermission('permission.files.deleteFolder')) {
                            vm.navItems.push(tab);
                        }
                    }
                     if (tab.id == 'project.issues') {
                        if ($rootScope.login.person.isProjectOwner || $rootScope.hasPermission('permission.issues.view') || $rootScope.hasPermission('permission.issues.add') || $rootScope.hasPermission('permission.issues.edit') || $rootScope.hasPermission('permission.issues.delete') || $rootScope.hasPermission('permission.issues.uploadMedia')) {
                            vm.navItems.push(tab);
                        }
                    }
                     if (tab.id == 'project.site') {
                        if ($rootScope.hasPermission('permission.sites.view') || $rootScope.hasPermission('permission.sites.new') || $rootScope.hasPermission('permission.sites.edit') || $rootScope.hasPermission('permission.sites.basic') || $rootScope.hasPermission('permission.sites.tasks') || $rootScope.hasPermission('permission.sites.resources')) {
                            vm.navItems.push(tab);
                        }
                    }
                     if (tab.id == 'project.reporting') {
                        if ($rootScope.login.person.isProjectOwner || $rootScope.hasPermission('permission.reporting.view')) {
                            vm.navItems.push(tab);
                        }
                    }
                     if (tab.id == 'project.communication') {
                        if ($rootScope.login.person.isProjectOwner || $rootScope.hasPermission('permission.communication.viewEmail') || $rootScope.hasPermission('permission.communication.viewMessages') || $rootScope.hasPermission('permission.communication.newGroup') || $rootScope.hasPermission('permission.communication.editGroup') || $rootScope.hasPermission('permission.communication.writeMessage')) {
                            vm.navItems.push(tab);
                        }
                    }
                     if (tab.id == 'project.meetings') {
                        if ($rootScope.login.person.isProjectOwner || $rootScope.hasPermission('permission.meetings.view') || $rootScope.hasPermission('permission.meetings.new') || $rootScope.hasPermission('permission.meetings.edit') || $rootScope.hasPermission('permission.meetings.delete')) {
                            vm.navItems.push(tab);
                        }
                    }
                     if (tab.id == 'project.media') {
                        if ($rootScope.login.person.isProjectOwner || $rootScope.hasPermission('permission.media.all') || $rootScope.hasPermission('permission.media.view') || $rootScope.hasPermission('permission.media.upload')) {
                            vm.navItems.push(tab);
                        }
                    }

                });*/
                if (lastSelectedItem == null && $rootScope.mode != "Inventory") {
                    if ($rootScope.hasPermission('permission.wbs.view')) {
                        vm.navItems[0].active = false;
                        lastSelectedItem = vm.navItems[2];
                        lastSelectedItem.active = true;
                    } else if ($rootScope.mode != "Inventory") {
                        lastSelectedItem = vm.navItems[0];
                    }
                } else if (lastSelectedItem == null && $rootScope.mode == "Inventory") {
                    angular.forEach(vm.navItems, function (item) {
                        if ($rootScope.tab.id == item.id) {
                            vm.navItems[0].active = false;
                            item.active = true;
                            $rootScope.mode = null;
                            lastSelectedItem = item;
                        }
                    })
                }
            }

            $rootScope.$on('app.activate.project', function (event, args) {
                vm.activeProject = args.project;
            });

            function setValues(tasks) {
                TaskService.getSiteReferences(tasks.content, 'site');
                CommonService.getPersonReferences(tasks.content, 'person');
                WbsService.getMultipleWbsWithTasks($stateParams.projectId, tasks.content, 'wbsItem');
            };

            function activateProjectTab(id) {
                $rootScope.taskPage = 0;
                var item = getTabById(id);
                if (item != null) {
                    lastSelectedItem.active = false;
                    item.active = true;
                    lastSelectedItem = item;
                    $rootScope.tab = lastSelectedItem;

                    $timeout(function () {
                        $('.project-headerbar').trigger('click');
                    }, 100);

                    if (item.id == 'project.documents') {
                        $state.go(item.state, {type: 'documents'});
                    }
                    else {
                        $state.go(item.state);
                    }
                }
            }

            function getTabById(id) {
                var found = null;
                angular.forEach(vm.navItems, function (item) {
                    if (item.id == id) {
                        found = item;
                    }
                });

                return found;
            }

            function assignedClick() {
                loadAssignedTasks();
                vm.flag = true;
            }

            function inProgressClick() {
                loadInProgressTasks();
                vm.flag = true;
            }

            function clear() {
                $rootScope.flag = false;
                vm.filters.status = ""
                TaskService.getListTasks(vm.filters).then(
                    function (data) {
                        vm.tasks = data;
                        setValues(vm.tasks);
                    });
            }

            function loadAssignedTasks() {
                vm.filters.status = "ASSIGNED";
                vm.filters.person = window.$application.login.person.id;
                TaskService.getListTasks($rootScope.selectedProject.id, vm.filters).then(
                    function (data) {
                        $rootScope.assignedTasks = data;
                        vm.tasks = data;
                        setValues(vm.tasks);
                    });
            }

            function loadInProgressTasks() {
                vm.filters.status = "INPROGRESS";
                vm.filters.person = window.$application.login.person.id;
                TaskService.getListTasks($rootScope.selectedProject.id, vm.filters).then(
                    function (data) {
                        $rootScope.inprogressTasks = data;
                        vm.tasks = data;
                        setValues(vm.tasks);
                    });
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadAssignedTasks();
                    loadInProgressTasks();
                    $rootScope.$on('mytasks.increment', function () {
                        $rootScope.assignedTasks.push({});
                    });

                    $rootScope.$on("mytasks.increment.inprogress", function () {
                        $rootScope.inprogressTasks.push({});
                    });

                    $rootScope.$on("mytasks.decrement.finished", function () {
                        $rootScope.inprogressTasks.splice($rootScope.inprogressTasks.length - 1, 1);
                    });
                    $rootScope.$on('project.tabs', tabs);
                    tabs();
                }
            })();
        }
    }
);