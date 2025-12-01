define([
        'app/desktop/modules/department/department.module',
        'app/shared/services/departmentService'
    ],
    function (module) {
        module.controller('ChangeDepartmentController', ChangeDepartmentController);

        function ChangeDepartmentController($scope, $rootScope, $timeout, $state,
                                            $stateParams, $cookies, $uibModalInstance, DepartmentService) {
            var vm = this;
            vm.departments = null;
            vm.cancel = cancel;
            vm.radioChange = radioChange;
            vm.onOk = onOk;
            vm.selectedDepartment = null;

            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            function loadDepartments() {
                DepartmentService.getAllDepartments().then(
                    function (data) {
                        vm.departments = data.content;
                    }
                )
            }

            function onOk() {
                $uibModalInstance.close(vm.selectedDepartment);
            }

            function radioChange(dept) {
                vm.selectedDepartment = dept;
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadDepartments();
                }
            })();
        }
    }
);