define(['app/desktop/modules/task/task.module',
        'app/shared/services/personService',
        'app/shared/services/taskService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('PrintDialogueController', PrintDialogueController);

        function PrintDialogueController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModalInstance,
                                         PersonService, CommonService,TaskService) {

            var vm = this;

            vm.staffs = [];
            vm.cancel = cancel;
            vm.getTasksByPersonAndDate = getTasksByPersonAndDate;
            vm.printTask = {
                name: null,
                date: null

            };

            vm.filters = angular.copy(vm.printTask);


            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            function getAssignedPersonsByRole() {
                PersonService.getPersonsByRole("Staff").then(
                    function (data) {
                        angular.forEach(data, function (obj) {
                            loadStaffPersons(obj);
                        });
                    });
                getAdminstratorByRole();
            }

            function loadStaffPersons(obj) {
                CommonService.getPerson(obj.person).then(
                    function (data) {
                        var staff = data;
                        vm.staffs.push(staff);
                    });
            }


            function getTasksByPersonAndDate(){
                $uibModalInstance.dismiss('cancel');
                var url = "{0}//{1}/api/projects/null/tasks/print?personId={2}&date={3}".
                    format(window.location.protocol, window.location.host,
                    vm.printTask.name.id, vm.printTask.date);
                var printWindow = window.open(url, '',
                    'scrollbars=yes,menubar=no,width=500, resizable=yes,toolbar=no,location=no,status=no');
            }

            function getAdminstratorByRole() {
                PersonService.getPersonsByRole("Administrator").then(
                    function (data) {
                        angular.forEach(data, function (obj) {
                            loadAdministrator(obj);
                        });
                    });
            }

            function loadAdministrator(obj) {
                CommonService.getPerson(obj.person).then(
                    function (data) {
                        var admin = data;
                        vm.staffs.push(admin);
                    });
            }

            (function () {
                if ($application.homeLoaded == true) {
                    getAssignedPersonsByRole();
                }
            })();
        }
    }
);

