define(
    [
        'app/desktop/modules/inward/inward.module',
        'moment',
        'moment-timezone-with-data',
        'app/shared/services/core/inwardService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {

        module.controller('NewGatePassController', NewGatePassController);

        function NewGatePassController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies,
                                       InwardService, AttachmentService, AttributeAttachmentService) {
            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.newGatePass = {
                id: null,
                gatePass: null,
                gatePassNumber: null,
                gatePassDate: null
            };


            function create() {
                $rootScope.closeNotification();
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    InwardService.createGatePass(vm.newGatePass.gatePassNumber, vm.newGatePass.gatePassDate, vm.gatePass).then(
                        function (data) {
                            vm.newGatePass = data;
                            vm.newGatePass = {
                                id: null,
                                gatePass: null,
                                gatePassNumber: null,
                                gatePassDate: null
                            };
                            $scope.callback();
                            $rootScope.hideSidePanel();
                            $rootScope.hideBusyIndicator();
                            $rootScope.showSuccessMessage("GatePass created successfully.")
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
            }

            function validate() {
                var valid = true;

                if (vm.gatePass == null || vm.gatePass == "" || vm.gatePass == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select GatePass");
                } else if (vm.gatePass != null && !validGatePass()) {
                    valid = false;
                }
                else if (vm.newGatePass.gatePassNumber == null || vm.newGatePass.gatePassNumber == "" || vm.newGatePass.gatePassNumber == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter GatePass Number");
                } else if (vm.newGatePass.gatePassDate == null || vm.newGatePass.gatePassDate == "" || vm.newGatePass.gatePassDate == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select GatePass Date");
                }

                return valid;
            }

            function validGatePass() {
                var valid = true;
                var fileExtension = vm.gatePass.name.substring(vm.gatePass.name.lastIndexOf('.') + 1);
                if (fileExtension.toLowerCase() != "pdf") {
                    valid = false;
                    $rootScope.showErrorMessage("Please upload pdf format file only");
                }

                return valid;
            }

            $timeout(function () {
                document.getElementById("inwardGatePassFile").onchange = function () {
                    var file = document.getElementById("inwardGatePassFile");
                    vm.gatePass = file.files[0];

                    var fileNameSplit = vm.gatePass.name.split(".");
                    vm.newGatePass.gatePassNumber = fileNameSplit[0];
                    $scope.$apply();
                };
            }, 1000);


            (function () {
                if ($application.homeLoaded == true) {

                    $rootScope.$on('app.inwards.gatePass.new', create);
                }
            })();
        }
    }
)
;