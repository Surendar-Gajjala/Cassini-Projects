define(
    [
        'app/desktop/modules/dispatch/dispatch.module',
        'app/shared/services/core/dispatchService',
        'app/shared/services/core/bomService'
    ],
    function (module) {

        module.controller('DispatchDetailsController', DispatchDetailsController);

        function DispatchDetailsController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies,
                                           DispatchService, BomService) {

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.dispatchId = $scope.data.dispatchDetails.id;

            function updateDispatch() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));

                    DispatchService.updateDispatch(vm.dispatch).then(
                        function (data) {
                            $rootScope.showSuccessMessage("Dispatch updated successfully");
                            $rootScope.hideBusyIndicator();
                            $rootScope.hideSidePanel();
                            $scope.callback(data);
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }

            }

            function validate() {
                var valid = true;

                if (vm.dispatch.gatePassNumber == null || vm.dispatch.gatePassNumber == "" || vm.dispatch.gatePassNumber == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Gate Pass Number");
                }

                return valid;
            }


            function loadDispatch() {
                DispatchService.getDispatch(vm.dispatchId).then(
                    function (data) {
                        vm.dispatch = data;
                    }
                )
            }

            function closeDispatch() {
                $rootScope.hideBusyIndicator();
                $rootScope.hideSidePanel();
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadDispatch();
                    $rootScope.$on('app.dispatch.details', updateDispatch);
                    $rootScope.$on('app.dispatch.close', closeDispatch);
                }
            })();
        }
    }
)
;