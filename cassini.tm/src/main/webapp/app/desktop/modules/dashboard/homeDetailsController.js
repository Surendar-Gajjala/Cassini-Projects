define(
    [
        'app/desktop/modules/dashboard/home.module',
        'app/shared/services/taskService',
        'app/shared/services/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('HomeDetailsController', HomeDetailsController);

        function HomeDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModalInstance,
                        location, statuses, statusLabel, TaskService, ProjectService, CommonService) {

            var vm = this;

            vm.statusLabel = statusLabel;
            vm.tasks = [];
            vm.users = [];
            vm.cancel = cancel;


            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            function loadTasks() {
                TaskService.getTasksByLocationAndStatus(location, statuses).then(
                    function(data) {
                        vm.tasks = data;
                        var map = new Hashtable();

                        angular.forEach(vm.tasks, function(task) {
                            var assignedTo = task.assignedTo;
                            var user = map.get(assignedTo);
                            if(user == null) {
                                user = {
                                    showTasks: false,
                                    assignedTo: assignedTo,
                                    tasks: []
                                };
                                map.put(assignedTo, user);
                                vm.users.push(user);
                            }

                            user.tasks.push(task);
                        });

                        CommonService.getPersonReferences(vm.tasks, 'assignedTo');
                        CommonService.getPersonReferences(vm.tasks, 'verifiedBy');
                        CommonService.getPersonReferences(vm.tasks, 'approvedBy');
                        CommonService.getPersonReferences(vm.users, 'assignedTo');
                    }
                )
            }


            (function () {
                loadTasks();
            })();
        }
    }
);