define(['app/desktop/modules/issues/issues.module',
        'app/shared/services/issue/issueService'
    ],
    function (module) {
        module.controller('NewIssueController', NewIssueController);

        function NewIssueController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, IssueService) {
            var vm = this;

            vm.creating = true;
            vm.valid = true;
            vm.error = "";

            vm.create = create;
            vm.cancel = cancel;

            $rootScope.viewInfo.icon = "fa flaticon-marketing8";
            $rootScope.viewInfo.title = "New Issue";

            function validate() {
                vm.valid = true;

                if (vm.issue.title === null) {
                    vm.valid = false;
                    vm.error = "Issue Name cannot be empty";
                }

                return vm.valid;
            }

            function create() {
                if (validate() == true) {
                    vm.creating = true;
                    vm.issue.targetObjectId = $stateParams.projectId;
                    vm.issue.targetObjectType = "PERSON";//static temp

                    IssueService.createIssue(vm.issue).then(
                        function (data) {
                            vm.creating = false;
                            $state.go('app.pm.project.issues');
                        }
                    )
                }
            }

            function cancel() {
                $state.go('app.pm.project.issues');
            }

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);