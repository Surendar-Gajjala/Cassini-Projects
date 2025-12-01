/**
 * Created by swapna on 21/11/18.
 */
define(['app/desktop/modules/tm/tm.module',
        'app/shared/services/tm/taskService'
    ],
    function (module) {
        module.controller('TaskInspectionController', TaskInspectionController);

        function TaskInspectionController($scope, $rootScope, $timeout, $state, $stateParams, TaskService) {

            var vm = this;
            vm.task = $scope.data.task;
            vm.task.inspectedBy = window.$application.login.person.id;
            vm.task.inspectedOn = moment(new Date()).format("DD/MM/YYYY");
            vm.task.inspectionResult = null;
            vm.task.inspectionRemarks = null;

            function accept() {
                vm.task.inspectionResult = "ACCEPTED";
                if (vm.task.percentComplete == 100) {
                    vm.task.status = "FINISHED";
                }
                if (vm.task.inspectionRemarks != null && vm.task.inspectionRemarks != "") {
                    updateTask();
                }
                else {
                    $rootScope.showErrorMessage("Please enter remarks");
                }
            }

            function reject() {
                vm.task.inspectionResult = "REJECTED";
                vm.task.status = "INPROGRESS";
                if (vm.task.inspectionRemarks != null && vm.task.inspectionRemarks != "") {
                    updateTask();
                }
                else {
                    $rootScope.showErrorMessage("Please enter remarks");
                }
            }

            function updateTask() {
                $rootScope.showBusyIndicator();
                TaskService.updateProjectTask($stateParams.projectId, vm.task).then(
                    function (data) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.hideSidePanel();
                        $rootScope.showSuccessMessage("Remarks noted for the task :" + vm.task.name);
                        $scope.callback(data);
                    });
            }

            (function () {
                $rootScope.$on('app.task.inspect.accept', accept);
                $rootScope.$on('app.task.inspect.reject', reject);
            })();
        }
    }
)
;