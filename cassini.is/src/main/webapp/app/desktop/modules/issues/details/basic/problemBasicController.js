/**
 * Created by swapna on 15/11/18.
 */
define(['app/desktop/modules/tm/tm.module',
        'app/shared/services/issue/issueService',
        'app/shared/services/common/attachmentService',
        'app/shared/services/tm/taskService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDetailsDirectiveController'
    ],
    function (module) {
        module.controller('ProblemBasicController', ProblemBasicController);

        function ProblemBasicController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $sce,
                                        IssueService, AttachmentService, CommonService, TaskService) {

            $rootScope.viewInfo.icon = "fa flaticon-marketing8";

            var vm = this;

            vm.loading = true;
            vm.issue = null;
            vm.issueId = $stateParams.issueId;
            vm.back = back;
            vm.status = ["NEW", "ASSIGNED", "INPROGRESS", "CLOSED"];
            vm.updateIssue = updateIssue;
            vm.newTask = newTask;
            var issueMap = new Hashtable();
            vm.isAssignedPerson = false;
            vm.task = null;

            var pageable = {
                page: 0,
                size: 10,
                sort: {
                    field: "modifiedDate"
                }
            };

            function back() {
                window.history.back();
            }

            function loadIssue() {
                vm.loading = true;
                var issueId = $stateParams.issueId;
                IssueService.getIssue(issueId).then(
                    function (data) {
                        data.resolution = $sce.trustAsHtml(data.resolution);
                        vm.issue = data;
                        if (vm.issue.type == 1) {
                            vm.issue.typeName = "ENGINEERING";
                        }
                        else if (vm.issue.type == 2) {
                            vm.issue.typeName = "MECHANICAL";
                        }
                        else if (vm.issue.type == 3) {
                            vm.issue.typeName = "ELECTRICAL";
                        }
                        return CommonService.getPerson(data.createdBy);
                    }
                ).then(
                    function (data) {
                        vm.issue.createdByPerson = data;
                        return AttachmentService.getAttachments('ISSUE', vm.issueId);
                    }
                ).then(
                    function (data) {
                        vm.loading = false;
                        vm.issue.attachments = data;
                        $rootScope.viewInfo.title = vm.issue.title;
                    }
                ).then(
                    function () {
                        if (vm.issue.task != null) {
                            taskName();
                        }
                    }
                ).then(
                    function () {
                        if (vm.issue.assignedTo == $rootScope.login.person.id) {
                            vm.isAssignedPerson = true;
                        }
                    }
                );

            }

            function validate() {
                vm.valid = true;

                if (vm.issue.type == null) {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Problem Type cannot be empty");
                } else if (vm.issue.title == null || vm.issue.title.trim() == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Problem Title cannot be empty");
                } else if (issueMap.get(vm.issue.title) != null && issueMap.get(vm.issue.title) == vm.issue.title) {
                    vm.valid = false;
                    $rootScope.showErrorMessage("{0} Title already exists".format(vm.issue.title));
                }
                else if (vm.issue.priority == null || vm.issue.priority == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Problem Priority cannot be empty");
                } else if (vm.issue.assignedTo == null) {
                    vm.valid = false;
                    $rootScope.showErrorMessage("AssignedTo cannot be empty");
                }

                if (vm.valid == false) {
                    loadIssue();
                }
                return vm.valid;
            }

            function updateIssue() {
                if (validate()) {
                    IssueService.updateExistedIssue(vm.issue).then(
                        function (data) {
                            $rootScope.showSuccessMessage("Problem updated successfully");
                        }
                    )
                }
            }

            function loadIssues() {
                vm.loading = true;
                IssueService.getPageableIssues('PROJECT', $stateParams.projectId, pageable).then(
                    function (data) {
                        vm.issues = data;
                        angular.forEach(vm.issues.content, function (issue) {
                            issueMap.put(issue.title, issue);
                        })
                    });
            }

            function newTask() {
                var options = {
                    title: 'New Task',
                    showMask: true,
                    template: 'app/desktop/modules/tm/new/newTaskView.jsp',
                    controller: 'NewTaskController as newTaskVm',
                    resolve: 'app/desktop/modules/tm/new/newTaskController',
                    width: 600,
                    data: {projectTask: false},
                    buttons: [
                        {text: 'Create', broadcast: 'app.task.new'}
                    ],
                    callback: function (result) {
                        vm.task = result;
                        vm.issue.task = vm.task.id;
                        vm.issue.status = "ASSIGNED";
                        vm.issue.taskName = vm.task.name;
                        updateIssue();
                        $state.go("app.pm.project.issuedetails");
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function taskName() {
                TaskService.getProjectTask($stateParams.projectId, vm.issue.task).then(
                    function (data) {
                        vm.issue.taskName = data.name;
                    });
            }

            function updateProblemTask(event, args) {
                vm.task = args.task;
                vm.issue.task = vm.task.id;
                vm.issue.status = "ASSIGNED";
                vm.issue.taskName = vm.task.name;
                updateIssue();
                $state.go("app.pm.project.issuedetails");
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadIssue();
                    loadIssues();
                    $rootScope.$on('app.project.problem.newTask', newTask);
                    $rootScope.$on('app.project.problem.update', updateProblemTask);
                    $rootScope.showComments('ISSUE', vm.issueId);
                }
            })();
        }
    }
);