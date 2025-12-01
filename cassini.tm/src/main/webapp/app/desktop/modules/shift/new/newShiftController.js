define([
        'app/desktop/modules/shift/shift.module',
        'app/assets/template/js/bootstrap-timepicker.min'
    ],
    function(module) {
        module.controller('NewShiftController', NewShiftController);

        function NewShiftController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,$application,CommonService, $uibModalInstance){
            var vm = this;
            vm.persons = [];
            vm.cancel = cancel;
            vm.create = create;

            vm.newShift = {
                name: null,
                startTime: null,
                endTime: null
            };
            function create(){
                $uibModalInstance.close(vm.newShift);
            }
            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            (function() {
                if($application.homeLoaded == true) {
                    $(function () {

                        $('#timepicker1').timepicker({
                            template: false,
                            showInputs: false,
                            minuteStep: 5
                        });

                        $('#timepicker2').timepicker({
                            template: false,
                            showInputs: false,
                            minuteStep: 5
                        });
                    });

                }
            })();
        }
    }
);
