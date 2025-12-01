define(
    [
        'app/desktop/modules/department/department.module',
        'app/shared/services/departmentService'

    ],
    function (module) {
        module.controller('DepartmentBasicInfoController', DepartmentBasicInfoController);

        function DepartmentBasicInfoController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                               DepartmentService) {
            var vm = this;

            vm.loading = true;
            vm.deptId = $stateParams.departmentId;
            vm.department = null;
            vm.updateDepartment = updateDepartment;

            function loadDepartment() {
                vm.loading = true;
                DepartmentService.getDepartmentById(vm.deptId).then(
                    function (data) {
                        vm.department = data;
                        vm.loading = false;
                        $rootScope.viewInfo.title = "Department Details (" + vm.department.name + ")";
                    }
                )
            }

            function updateDepartment() {
                DepartmentService.updateDepartment(vm.department).then(
                    function (data) {
                        $rootScope.showSuccessMessage("Department Updated Successfully!");
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadDepartment();
                    $scope.$on('app.department.update',  function() {
                        updateDepartment();
                    })
                }
            })();
        }
    }
);