/**
 * Created by Nageshreddy on 16-11-2018.
 */
define(
    [
        'app/desktop/modules/complaint/complaint.module',
        'app/shared/services/core/complaintService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('ComplaintHistoryController', ComplaintHistoryController);

        function ComplaintHistoryController($scope, $rootScope, $timeout, $window, $state, $stateParams, $uibModal,
                                            ComplaintService, CommonService) {
            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;
            vm.loadCompliantHistory = loadCompliantHistory;
            vm.statusUpdate = statusUpdate;
            vm.complaintHistory = null;
            var complaintId = $scope.data.complaintData.id;

            function loadCompliantHistory() {
                vm.complaintHistory = null;
                if (complaintId != null && complaintId != "" && complaintId != undefined) {
                    ComplaintService.getCompliantHistory(complaintId).then(
                        function (data) {
                            if (data != null && data != "" && data != undefined) {
                                vm.complaintHistory = data;
                                CommonService.getPersonReferences(vm.complaintHistory, 'assignedTo');
                                CommonService.getPersonReferences(vm.complaintHistory, 'submittedBy');
                            }
                        }
                    )
                }
            }

            function close() {
                $rootScope.hideSidePanel();
            }

            function statusUpdate() {
                $scope.callback('update');
                $timeout(
                    function () {
                        loadCompliantHistory();
                    }, 1000
                )

            }


            (function () {
                loadCompliantHistory();
                $scope.$on('app.history.close', close);
            })();
        }
    }
);
