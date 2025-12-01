define(['app/desktop/modules/tm/tm.module',
        'dropzone',
        'app/desktop/modules/tm/details/tabs/basic/taskBasicDetailsController',
        'app/desktop/modules/tm/details/tabs/manpower/manpowerTaskController',
        'app/desktop/modules/tm/details/tabs/material/materialTaskController',
        'app/desktop/modules/tm/details/tabs/machine/machineTaskController',
        'app/desktop/modules/tm/details/tabs/workflow/taskWorkflowController',
        'app/desktop/modules/tm/details/tabs/attachments/taskAttachmentsController',
        'app/desktop/modules/tm/details/tabs/documents/taskDocumentsController',
        'app/desktop/modules/tm/details/tabs/drawings/taskDrawingsController',
        'app/desktop/modules/tm/details/tabs/media/taskMediaController',
        'app/shared/services/tm/taskService',
        'app/desktop/modules/tm/details/tabs/inspect/taskInspectionController',
        'app/desktop/modules/tm/details/tabs/problems/taskProblemsController'

    ],
    function (module) {
        module.controller('TaskDetailsController', TaskDetailsController);

        function TaskDetailsController($scope, $rootScope, $timeout, $state, $stateParams, TaskService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa flaticon-deadlines";

            vm.loading = true;
            vm.back = back;
            vm.activeTab = 0;
            var taskId = $stateParams.taskId;
            vm.projectId = $stateParams.projectId;
            vm.showStartButton = false;
            $scope.task = null;
            vm.taskDetailsTabActivated = taskDetailsTabActivated;
            vm.selectedPerson = null;
            vm.startTask = startTask;
            vm.addManpower = addManpower;
            vm.finishTask = finishTask;
            vm.addMachine = addMachine;
            vm.addMaterial = addMaterial;
            vm.addTaskFiles = addTaskFiles;
            vm.assignToTask = assignToTask;
            vm.addTaskDrawings = addTaskDrawings;
            vm.addTaskDocuments = addTaskDocuments;
            vm.inspectTask = inspectTask;
            vm.newProblem = newProblem;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/tm/details/tabs/basic/taskBasicDetailsView.jsp',
                    active: true
                },

                manpower: {
                    id: 'details.manpower',
                    heading: 'Manpower',
                    template: 'app/desktop/modules/tm/details/tabs/manpower/manpowerTaskView.jsp',
                    active: false
                },

                material: {
                    id: 'details.material',
                    heading: 'Material',
                    template: 'app/desktop/modules/tm/details/tabs/material/materialTaskView.jsp',
                    active: false
                },

                machine: {
                    id: 'details.machine',
                    heading: 'Machine',
                    template: 'app/desktop/modules/tm/details/tabs/machine/machineTaskView.jsp',
                    active: false
                },

                workflow: {
                    id: 'details.workflow',
                    heading: 'Workflow',
                    template: 'app/desktop/modules/tm/details/tabs/workflow/taskWorkflowView.jsp',
                    active: false
                },
                attachments: {
                    id: 'details.attachments',
                    heading: 'Files',
                    template: 'app/desktop/modules/tm/details/tabs/attachments/taskAttachmentsView.jsp',
                    active: false
                },
                media: {
                    id: 'details.media',
                    heading: 'Media',
                    template: 'app/desktop/modules/tm/details/tabs/media/taskMediaView.jsp',
                    active: false
                },
                problems: {
                    id: 'details.problems',
                    heading: 'Problems',
                    template: 'app/desktop/modules/tm/details/tabs/problems/taskProblemsView.jsp',
                    active: false
                }
            };

            function addMaterial() {
                $rootScope.$broadcast('app.task.addMaterial');
            }

            function addTaskDrawings() {
                $rootScope.$broadcast('app.task.addDrawing');
            }

            function addTaskDocuments() {
                $rootScope.$broadcast('app.task.addDocument');
            }

            function addTaskFiles() {
                $rootScope.$broadcast('app.taskAttachments.addFile');
            }

            function addMachine() {
                $rootScope.$broadcast('app.task.addMachine');
            }

            function addManpower() {
                $rootScope.$broadcast('app.task.addManpower');
            }

            function assignToTask() {
                $rootScope.$broadcast('app.task.assignedTo');
            }

            function startTask() {
                $rootScope.$broadcast('app.task.start');

            }

            function finishTask() {
                $rootScope.$broadcast('app.task.finish');

            }

            function newProblem() {
                $rootScope.$broadcast('app.task.newProblem');
            }

            function back() {
                window.history.back();
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t)) {
                        vm.tabs[t].active = (t != undefined && t == tab);
                    }
                }
            }

            function taskDetailsTabActivated(tabId) {
                var tab = getTabById(tabId);
                if (tab != null) {
                    activateTab(tab);
                    $scope.$broadcast('app.task.tabactivated', {tabId: tabId});
                }
            }

            function getTabById(tabId) {
                var tab = null;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t) && vm.tabs[t].id == tabId) {
                        tab = t;
                    }
                }

                return tab;
            }

            function inspectTask() {
                $scope.$broadcast('app.task.inspect');
            }

            $rootScope.loadDetailsCount = loadDetailsCount;
            function loadDetailsCount() {
                TaskService.getTaskDetailsCount(vm.projectId, taskId).then(
                    function (data) {
                        vm.taskCount = data;
                        var materialTab = document.getElementById("material");
                        var machineTab = document.getElementById("machine");
                        var manpowerTab = document.getElementById("manpower");
                        var filesTab = document.getElementById("files");
                        var mediaTab = document.getElementById("media");
                        var problemsTab = document.getElementById("problems");

                        materialTab.lastElementChild.innerHTML = vm.tabs.material.heading +
                            "<span class='label label-default' style='margin-top:20px;color: black;background-color: #e4dddd;height: 20px;margin-left: 5px;padding: 3px 7px;'>{0}</span>".format(vm.taskCount.material);
                        machineTab.lastElementChild.innerHTML = vm.tabs.machine.heading +
                            "<span class='label label-default' style='margin-top:20px;color: black;background-color: #e4dddd;height: 20px;margin-left: 5px;padding: 3px 7px;'>{0}</span>".format(vm.taskCount.machine);
                        manpowerTab.lastElementChild.innerHTML = vm.tabs.manpower.heading +
                            "<span class='label label-default' style='margin-top:20px;color: black;background-color: #e4dddd;height: 20px;margin-left: 5px;padding: 3px 7px;'>{0}</span>".format(vm.taskCount.manpower);
                        filesTab.lastElementChild.innerHTML = vm.tabs.attachments.heading +
                            "<span class='label label-default' style='margin-top:20px;color: black;background-color: #e4dddd;height: 20px;margin-left: 5px;padding: 3px 7px;'>{0}</span>".format(vm.taskCount.files);
                        mediaTab.lastElementChild.innerHTML = vm.tabs.media.heading +
                            "<span class='label label-default' style='margin-top:20px;color: black;background-color: #e4dddd;height: 20px;margin-left: 5px;padding: 3px 7px;'>{0}</span>".format(vm.taskCount.media);
                        problemsTab.lastElementChild.innerHTML = vm.tabs.problems.heading +
                            "<span class='label label-default' style='margin-top:20px;color: black;background-color: #e4dddd;height: 20px;margin-left: 5px;padding: 3px 7px;'>{0}</span>".format(vm.taskCount.problems);
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadDetailsCount();
                    $rootScope.showComments('TASK', taskId);
                }
            })();
        }
    }
);