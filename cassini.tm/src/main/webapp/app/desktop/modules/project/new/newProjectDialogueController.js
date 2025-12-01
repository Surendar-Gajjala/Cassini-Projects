/**
 * Created by Anusha on 11-07-2016.
 */
define(['app/desktop/modules/project/project.module',
        'app/shared/services/projectService'
    ],
    function (module) {
        module.controller('NewProjectDialogueController', NewProjectDialogueController);

        function NewProjectDialogueController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application, $uibModalInstance, ProjectService) {
            var vm = this;
            vm.cancel = cancel;
            vm.create = create;
            vm.nameError = null;
            vm.descriptionError = null;

            vm.newProject = {
                name: null,
                description: null

            };

            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            function create() {
                if (projectValidation()) {
                    $uibModalInstance.close(vm.newProject);
                }

            }

            function projectValidation() {
                var valid = true;
                vm.nameError = null;
                vm.descriptionError = null;
                if (vm.newProject.name == null || vm.newProject.name == "") {
                    vm.nameError = "Enter Project Name";
                    valid = false;
                }
                else if (vm.newProject.description == null || vm.newProject.description == "") {
                    vm.descriptionError = "Enter Project Description";
                    valid = false;
                } else {
                    valid = true;
                }
                return valid;
            }


            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);

