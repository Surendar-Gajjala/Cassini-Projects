/**
 * Created by Nageshreddy on 08-11-2018.
 */
define(
    [
        'app/desktop/modules/main/main.module',
        'app/shared/services/core/complaintService',
        'app/shared/services/core/userService',
        'app/shared/services/core/groupLocationService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('NewComplaintController', NewComplaintController);

        function NewComplaintController($scope, $rootScope, $timeout, $window, $state, $cookies,
                                        ComplaintService, UserService, GroupLocationService, AutonumberService, LovService, CommonService) {

            var vm = this;
            vm.newComplaint = {
                complaintNumber: null,
                utility: null,
                location: null,
                details: null,
                person: null,
                group: null
            };

            vm.person = {
                email: null,
                firstName: null,
                lastName: null,
                designation: null,
                phoneMobile: null,
                personType: null,
                traineeId: null
            };

            vm.isPersonNew = isPersonNew;

            vm.complaintNumber = "";
            var valid = true;
            var lovsMap = null;

            function isPersonNew() {
                email}

            function validateComplaint() {
                valid = true;
                if (vm.complaintNumber == "" || vm.complaintNumber == null) {
                    AutonumberService.getNextNumberByName('Default Complaint Number').then(
                        function (data) {
                            vm.complaintNumber = data;
                        })
                }
                else if (vm.newComplaint.group == null) {
                    $rootScope.showErrorMessage("Please Select Group");
                    valid = false;
                }
                else if (vm.person.email == null || vm.person.email == undefined || vm.person.email == "") {
                    valid = false;
                    $rootScope.showErrorMessage('Email cannot be empty');
                }
               else if (vm.person.personType == null || vm.person.personType == undefined || vm.person.personType == "") {
                    valid = false;
                    $rootScope.showErrorMessage('Designation cannot be empty');
                }
                else if (vm.person.firstName == null || vm.person.firstName == undefined || vm.person.firstName == "") {
                    valid = false;
                    $rootScope.showErrorMessage('FirstName cannot be empty');
                }
                else if (vm.person.phoneMobile == null || vm.person.phoneMobile == undefined || vm.person.phoneMobile == "") {
                    valid = false;
                    $rootScope.showErrorMessage('Please Enter Valid Phone Number ');
                }
                else if (vm.newComplaint.location == null || vm.newComplaint.location == undefined || vm.newComplaint.location == "") {
                    valid = false;
                    $rootScope.showErrorMessage('Location cannot be empty');
                }
                else if (vm.newComplaint.utility == null || vm.newComplaint.utility == undefined || vm.newComplaint.utility == "") {
                    valid = false;
                    $rootScope.showErrorMessage('Utility cannot be empty');
                }
                else if (vm.newComplaint.details == null || vm.newComplaint.details == undefined || vm.newComplaint.details == "") {
                    valid = false;
                    $rootScope.showErrorMessage('Details cannot be empty');
                }
                return valid;
            }

            function newComplaint() {
                if (validateComplaint()) {
                    saveComplaint();
                }
            }

            function saveComplaint() {
                if (vm.person.personType != null && vm.person.personType != undefined) {
                    vm.person.personType = vm.person.personType.id;
                    UserService.createComplainant(vm.person).then(
                        function (data) {
                            vm.newComplaint.person = data.id;
                            if (vm.newComplaint.location != null) {
                                var complaint = {
                                    complaintNumber: vm.complaintNumber,
                                    utility: vm.newComplaint.utility,
                                    location: vm.newComplaint.location.name,
                                    details: vm.newComplaint.details,
                                    person: vm.newComplaint.person
                                };
                            }
                            ComplaintService.createComplaint(complaint).then(
                                function (data) {
                                    $scope.callback(data);
                                    vm.newComplaint = {
                                        complaintNumber: null,
                                        utility: null,
                                        location: null,
                                        details: null,
                                        person: null,
                                        group: null
                                    };
                                    vm.person = {
                                        email: null,
                                        firstName: null,
                                        lastName: null,
                                        phoneMobile: null,
                                        personType: null,
                                        designation: null,
                                        traineeId: null
                                    };
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    )
                }

            }

            function loadNextNumber() {
                AutonumberService.getNextNumberByName('Default Complaint Number').then(
                    function (data) {
                        vm.complaintNumber = data;
                    }
                )
            }

            /*function loadLovValues() {
                LovService.getLovMapWithNameAsKey().then(
                    function (data) {
                        lovsMap = data;
                        vm.utilities = lovsMap['Utilities'].values;
                    }
                )
             }*/

            function loadGroupLocations() {
                GroupLocationService.getGroups().then(
                    function (data) {
                        vm.groupLocations = data;
                    }
                )
            }

            function loadPersonTypes() {
                CommonService.getAllPersonTypes().then(
                    function (data) {
                        if (data.length > 0) {
                            vm.personTypes = [];
                            angular.forEach(data, function (type) {
                                if (type.name != "Administrator" && type.name != "Responder" &&
                                    type.name != "Facilitator" && type.name != "Assistor") {
                                    vm.personTypes.push(type);
                                }
                            });
                        }
                    }
                )
            }

            (function () {
                loadPersonTypes();
                loadNextNumber();
                //loadLovValues();
                loadGroupLocations();
                $scope.$on('app.comps.new', newComplaint);
            })();
        }
    }
);