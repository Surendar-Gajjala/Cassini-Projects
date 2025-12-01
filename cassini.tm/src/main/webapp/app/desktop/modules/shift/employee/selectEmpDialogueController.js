define([
        'app/desktop/modules/shift/shift.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('SelectEmpDialogueController', SelectEmpDialogueController);

        function SelectEmpDialogueController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application, CommonService, $uibModalInstance) {
            var vm = this;
            vm.persons = [];
            vm.selectedPersons = [];
            vm.cancel = cancel;
            vm.selectCheckBox = selectCheckBox;
            vm.onOk = onOk;

            function onOk() {
                $uibModalInstance.close(vm.selectedPersons);
            }

            function loadPersons() {
                CommonService.getAllPersons().then(
                    function (data) {
                        vm.persons = data;
                    });
            }

            function selectCheckBox(person) {
                var flag = true;
                angular.forEach(vm.selectedPersons, function (selectedPerson) {
                    if (selectedPerson.firstName == person.firstName) {
                        flag = false;
                        var index = vm.selectedPersons.indexOf(person);
                        vm.selectedPersons.splice(index,1);
                    }
                })
                if (flag) {
                    vm.selectedPersons.push(person);
                }
            }

            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadPersons();
                }
            })();
        }
    }
);
