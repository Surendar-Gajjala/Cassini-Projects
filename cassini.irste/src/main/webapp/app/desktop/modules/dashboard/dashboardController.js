define(
    [
        'app/desktop/modules/dashboard/dashboard.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/shared/services/core/complaintService'
    ],
    function (module) {
        module.controller('DashboardController', DashboardController);

        function DashboardController($scope, $rootScope, $timeout, $window, $state, $cookies, $uibModal,
                                     CommonService, ComplaintService) {

            $rootScope.viewInfo.icon = "fa fa-dashboard";
            $rootScope.viewInfo.title = "Dashboard";

            var vm = this;

            vm.trackComplaint = trackComplaint;
            vm.complaint = null;
            vm.errorMessage = null;
            vm.complaintNumber = "";

            function trackComplaint() {
                vm.complaint = null;
                vm.errorMessage = null;
                vm.complaintNumber = vm.complaintNumber.trim();
                if (vm.complaintNumber != null && vm.complaintNumber != "" && vm.complaintNumber != undefined) {
                    ComplaintService.getByComplaintNumber(vm.complaintNumber).then(
                        function (data) {
                            if (data != null && data != "" && data != undefined) {
                                vm.complaint = data;
                                vm.complaintNumber = data.complaintNumber;
                                CommonService.getPersonReferences([vm.complaint], 'person');
                            } else {
                                vm.errorMessage = "Complaint Number is not available";
                            }
                        }
                    )
                } else {
                    vm.errorMessage = "Please Enter Complaint Number";
                }
            }


            $scope.$on('$viewContentLoaded', function () {
                $timeout(function () {
                    $application.homeLoaded = true;
                    window.$("#preloader").hide();
                    window.$("#appview").show();
                }, 1000);
            });
        }
    }
);