/**
 * Created by Anusha on 04-08-2016.
 */
define(['app/desktop/modules/department/department.module'
    ],
    function(module) {
        module.controller('NewDepartmentController', NewDepartmentController);

        function NewDepartmentController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application, $uibModalInstance) {
            var vm = this;

            vm.cancel = cancel;
            vm.create = create;

            vm.newDepartment = {
                name: null,
                description:null
            }

            vm.errorMessage = ({
                name: null
            })

            function validateDepartment(result) {
                vm.valid = false;

                if (result.name == null || result.name == undefined || result.name == "") {
                    vm.errorMessage.name = "Name Is Required"
                } else {
                    vm.valid = true;
                }
                return vm.valid;
            }

            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            function create() {
                if(validateDepartment(vm.newDepartment)){
                    $uibModalInstance.close(vm.newDepartment);
                }
            }

            (function() {
                if($application.homeLoaded == true) {

                }
            })();
        }
    }
);

