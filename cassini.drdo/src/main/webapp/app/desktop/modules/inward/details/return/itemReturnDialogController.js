define(
    [
        'app/desktop/modules/inward/inward.module',
        'app/shared/services/core/inwardService'
    ],
    function (module) {
        module.controller('ItemReturnDialogController', ItemReturnDialogController);

        function ItemReturnDialogController($scope, $stateParams, $rootScope, $timeout, $interval, $state, $cookies, InwardService) {

            var vm = this;

            var inwardId = $stateParams.inwardId;
            vm.inwardItemInstance = $scope.data.inwardItemInstance;


            vm.mode = $scope.data.itemInstanceMode;
            vm.provisionalAcceptReason = null;

            function returnItemInstance() {
                if (validateItem()) {
                    if (vm.mode == "REJECTED") {
                        InwardService.returnInwardItemInstance(inwardId, vm.inwardItemInstance).then(
                            function (data) {
                                $scope.callback(data);
                                vm.inwardItemInstance = null;
                                vm.mode = null;
                                $rootScope.showSuccessMessage("Item Rejected successfully");
                                $rootScope.hideSidePanel();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    } else if (vm.mode == "REVIEW") {
                        InwardService.reviewInwardItemInstance(inwardId, vm.inwardItemInstance).then(
                            function (data) {
                                $scope.callback(data);
                                vm.inwardItemInstance = null;
                                vm.mode = null;
                                $rootScope.showSuccessMessage("Item updated successfully");
                                $rootScope.hideSidePanel();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    } else if (vm.mode == "P_ACCEPT") {
                        InwardService.provAcceptItemInstance(inwardId, vm.inwardItemInstance.id, vm.inwardItemInstance).then(
                            function (data) {
                                $scope.callback(data);
                                vm.inwardItemInstance = null;
                                vm.mode = null;
                                $rootScope.hideSidePanel();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                }
            }

            function validateItem() {
                var valid = true;
                if (vm.inwardItemInstance.item.reason == "" || vm.inwardItemInstance.item.reason == null || vm.inwardItemInstance.item.reason == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter reason");
                }
                return valid;
            }

            function provisionalAcceptAll() {
                if (validateReason()) {
                    InwardService.provisionalAcceptItem(inwardId, vm.inwardItemInstance, vm.provisionalAcceptReason).then(
                        function (data) {
                            $scope.callback(data);
                            vm.mode = null;
                            vm.provisonalAcceptReason = null;
                            $rootScope.hideSidePanel();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function validateReason() {
                var valid = true;
                if (vm.provisionalAcceptReason == null || vm.provisionalAcceptReason == "" || vm.provisionalAcceptReason == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Reason");
                }

                return valid;
            }

            function closeRightPanel() {
                vm.inwardItemInstance = null;
                vm.mode = null;
                vm.provisonalAcceptReason = null;
            }

            (function () {
                if ($application.homeLoaded == true) {
                    if (vm.mode != "P_ACCEPTALL") {
                        vm.inwardItemInstance.item.reason = null;
                    }
                    $scope.$on('app.inward.itemInstance.return', returnItemInstance);
                    $scope.$on('app.inward.provisionalAcceptAll', provisionalAcceptAll);
                    $rootScope.$on('app.rightside.panel.closing', closeRightPanel);
                }
            })();
        }
    }
);