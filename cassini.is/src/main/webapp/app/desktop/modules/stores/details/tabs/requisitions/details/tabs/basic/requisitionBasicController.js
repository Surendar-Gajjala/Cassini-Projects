/**
 * Created by swapna on 19/09/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/requisitionService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDetailsDirectiveController'

    ],
    function (module) {
        module.controller('RequisitionBasicController', RequisitionBasicController);

        function RequisitionBasicController($scope, $rootScope, $window, $state, $stateParams, RequisitionService) {
            var vm = this;

            vm.requisition = null;
            vm.updateRequisition = updateRequisition;

            function loadRequisition() {
                vm.loading = true;
                RequisitionService.getRequisition($rootScope.storeId, $stateParams.requisitionId).then(
                    function (data) {
                        vm.loading = false;
                        vm.requisition = data;
                        vm.requisition.customRequisitionItems = [];
                        $rootScope.viewInfo.title = "Requisition : " + vm.requisition.requisitionNumber;
                    }
                )
            }

            function updateRequisition() {
                vm.requisition.customRequisitionItems = [];
                RequisitionService.updateRequisition($rootScope.storeId, vm.requisition).then(
                    function (data) {
                        vm.requisition.status = data.status;
                    }
                )
            }

            function approveValidation() {
                var valid = true;
                if (vm.requisition.approvedBy == null || vm.requisition.approvedBy == "" || vm.requisition.approvedBy == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter ApprovedBy");
                }
                return valid;
            }

            $scope.$on('app.request.approve', function (event, data) {
                approveRequisition();
            });

            function approveRequisition() {
                if (approveValidation()) {
                    vm.requisition.status = "APPROVED";
                    RequisitionService.updateRequisition($rootScope.storeId, vm.requisition).then(
                        function (data) {
                            vm.requisition = data;
                            $rootScope.requisition = data;
                            $rootScope.showSuccessMessage(vm.requisition.requisitionNumber + ": approved successfully");
                        }
                    )
                }
            }

            (function () {
                loadRequisition();
            })();
        }
    }
)
;