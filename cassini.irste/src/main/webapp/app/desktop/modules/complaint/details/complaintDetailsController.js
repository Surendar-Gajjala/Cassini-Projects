/**
 * Created by Nageshreddy on 16-11-2018.
 */
define(
    [
        'app/desktop/modules/complaint/complaint.module',
        'app/shared/services/core/complaintService',
        'app/shared/services/core/userService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('ComplaintDetailsController', ComplaintDetailsController);

        function ComplaintDetailsController($scope, $rootScope, $timeout, $window, $state, $stateParams, $uibModal,
                                            ComplaintService, UserService, CommonService) {
            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;
            vm.loadComplaint = loadComplaint;
            vm.complaint = null;
            vm.statusUpdate = statusUpdate;
            //var complaintId = $stateParams.complaintId;
            var complaintId = $scope.data.complaintData.id;
            vm.personType = null;
            $scope.personType = $scope.data.personType;
            vm.assistors = null;
            vm.facilitators = null;
            vm.assignToAssistor = assignToAssistor;
            vm.assignToFacilitator = assignToFacilitator;
            vm.utility = null;

            function assignToAssistor() {
                vm.complaint.status = 'AT_ASSISTOR';
                updateComplaint();
            }

            function assignToFacilitator() {
                vm.complaint.status = 'AT_FACILITATOR';
                updateComplaint();
            }

            function loadPersonTypes() {
                CommonService.getAllPersonTypes().then(
                    function (data) {
                        vm.personTypeMap = {};
                        angular.forEach(data, function (type) {
                            vm.personTypeMap[type.id] = type;
                        })
                    }
                )

            }

            vm.loadComplaintsBasedOnLogin = loadComplaintsBasedOnLogin;
            function loadComplaintsBasedOnLogin() {
                vm.filters = angular.copy(vm.emptyFilters);
                if ($rootScope.loginPersonDetails.person.id == 1) {
                    CommonService.getPersonType($rootScope.loginPersonDetails.person.personType).then(
                        function (data) {
                            $scope.personType = data;
                        });
                    loadComplaint()
                } else {
                    CommonService.getPersonType($rootScope.loginPersonDetails.person.personType).then(
                        function (data) {
                            $scope.personType = data;
                            if (data.name.toLowerCase() == ('Responder').toLowerCase()) {
                                vm.filters.responder = $rootScope.loginPersonDetails.person.id;
                            } else if (data.name.toLowerCase() == ('Assistor').toLowerCase()) {
                                vm.filters.assistor = $rootScope.loginPersonDetails.person.id;
                            } else if (data.name.toLowerCase() == ('Facilitator').toLowerCase()) {
                                vm.filters.facilitator = $rootScope.loginPersonDetails.person.id;
                            }
                            loadUserComplaints();
                        }
                    )
                }
            }

            var pageable = {
                page: 0,
                size: 15,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            function loadUserComplaints() {
                vm.loading = true;
                ComplaintService.getComplaintsByFilter(pageable, vm.filters).then(
                    function (data) {
                        vm.complaints = data;
                        assignValues();
                        vm.loading = false;
                    }
                )
            }
            function assignValues() {
                CommonService.getPersonReferences(vm.complaints.content, 'person');
            }



            function updateComplaint() {
                ComplaintService.updateComplaint(vm.complaint).then(
                    function (data) {
                        vm.complaint.status = data.status;
                        CommonService.getPersonReferences([vm.complaint], 'person');
                        $scope.callback(vm.complaint);
                        $rootScope.showSuccessMessage(" Updated Successfully!");
                    })
            }

            function statusUpdate(status) {
                if ($scope.personType != null) {
                    if ($scope.personType.name.toLowerCase() == ('Responder').toLowerCase() ||
                        $scope.personType.name.toLowerCase() == ('Administrator').toLowerCase()) {
                        if (status == 'start') {
                            vm.complaint.status = 'INPROGRESS';
                        } else if (status == 'resolved') {
                            vm.complaint.status = 'COMPLETED';
                        }
                    } else if ($scope.personType.name.toLowerCase() == ('Assistor').toLowerCase() && status == 'resolved') {
                        vm.complaint.status = 'ASSISTED';
                    } else if ($scope.personType.name.toLowerCase() == ('Facilitator').toLowerCase() && status == 'resolved') {
                        vm.complaint.status = 'FACILITATED';
                    }
                    updateComplaint();
                }

            }

            function loadComplaint() {
                vm.complaint = null;
                if (complaintId != null && complaintId != "" && complaintId != undefined) {
                    ComplaintService.getOne(complaintId).then(
                        function (data) {
                            if (data != null && data != "" && data != undefined) {
                                vm.complaint = data;
                                vm.utility = vm.complaint.utility.replace("/", "%2F");
                                vm.utility = vm.utility.replace("/", "%2F");
                                vm.utility = vm.utility.replace("/", "%2F");
                                CommonService.getPersonReferences([vm.complaint], 'person');
                                loadFacilitators();
                                loadAssistors();
                            }
                        }
                    )
                }
            }

            function loadFacilitators() {
                vm.personType = CommonService.getPersonTypeByName('Facilitator');
                UserService.getByUtilityAndPersonType(vm.personType.id, vm.utility).then(
                    function (data) {
                        CommonService.getPersons(data).then(
                            function (data2) {
                                vm.facilitators = data2;
                            }
                        )
                    }
                )
            }

            function loadAssistors() {
                vm.personType = CommonService.getPersonTypeByName('Assistor');
                UserService.getByUtilityAndPersonType(vm.personType.id, vm.utility).then(
                    function (data) {
                        CommonService.getPersons(data).then(
                            function (data1) {
                                vm.assistors = data1;
                            }
                        )
                    }
                )
            }

            function close() {
                $rootScope.hideSidePanel();
            }

            (function () {
                loadPersonTypes();
                loadComplaint();
                $scope.$on('app.details.close', close);
            })();
        }
    }
);
