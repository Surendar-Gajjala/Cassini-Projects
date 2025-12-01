/**
 * Created by swapna on 12/12/18.
 */
define(['app/desktop/modules/tm/tm.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/issue/issueService'

    ],
    function (module) {
        module.controller('TaskProblemController', TaskProblemController);
        function TaskProblemController($scope, $rootScope, $timeout, $state, $stateParams,
                                       CommonService, IssueService) {

            var vm = this;

            vm.taskId = $stateParams.taskId;
            vm.projectId = $stateParams.projectId;
            vm.taskProblems = [];
            vm.loading = false;
            var issueMap = new Hashtable();
            vm.openProblem = openProblem;

            function loadTaskProblems() {
                vm.loading = true;
                IssueService.getIssues("TASK", vm.taskId).then(
                    function (data) {
                        vm.taskProblems = data;
                        angular.forEach(vm.taskProblems, function (problem) {
                            issueMap.put(problem.title, problem);
                        });
                        CommonService.getPersonReferences(vm.taskProblems, 'createdBy');
                        CommonService.getPersonReferences(vm.taskProblems, 'assignedTo');
                        vm.loading = false;
                    });
            }

            function openProblem(issue) {
                $rootScope.issue = issue;
                $state.go('app.pm.project.issuedetails', {issueId: issue.id});
            }

            function newProblem() {
                var options = {
                    title: 'New Problem',
                    showMask: true,
                    template: 'app/desktop/modules/issues/new/newIssueDialog.jsp',
                    controller: 'NewIssueDialogController as newIssueVm',
                    resolve: 'app/desktop/modules/issues/new/newIssueDialogController',
                    width: 500,
                    data: {
                        targetObjectType: "TASK",
                        targetObjectId: $stateParams.taskId,
                        issueMap: issueMap
                    },
                    buttons: [
                        {text: 'Create', broadcast: 'app.issue.new'}
                    ],
                    callback: function () {
                        $rootScope.task.hasProblems = true;
                        loadTaskProblems();
                        $rootScope.loadDetailsCount();
                    }
                };
                $rootScope.showSidePanel(options);
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $scope.$on('app.task.tabactivated', function (event, data) {
                        if (data.tabId == 'details.problems') {
                            loadTaskProblems();
                        }
                    });
                    $scope.$on('app.task.newProblem', newProblem);
                    $rootScope.$on('app.task.problems', loadTaskProblems)
                }
            })();
        }
    });