define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/core/complaintService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('HomeController', HomeController);

        function HomeController($scope, $rootScope, $timeout, $window, $state, $cookies,
                                ComplaintService, CommonService) {

            $rootScope.viewInfo.icon = "fa fa-home";
            $rootScope.viewInfo.title = "Home";

            var vm = this;
            vm.newComplaint = newComplaint;
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

            function newComplaint() {

                var options = {
                    title: 'New Complaint',
                    template: 'app/desktop/modules/main/new/newComplaintView.jsp',
                    controller: 'NewComplaintController as newCompVm',
                    resolve: 'app/desktop/modules/main/new/newComplaintController',
                    width: 500,
                    showMask: true,
                    buttons: [
                        {text: "Create", broadcast: 'app.comps.new'}
                    ],
                    callback: function (complaint) {
                        $rootScope.showSuccessMessage(complaint.complaintNumber + " Complaint Created Successfully");
                        $rootScope.hideSidePanel();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadComplaints() {


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