define(['app/desktop/modules/person/person.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/personService',
        'app/shared/services/departmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'
    ],
    function (module) {
        module.controller('NewPersonController', NewPersonController);

        function NewPersonController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application,
                                     CommonService, PersonService, DepartmentService, LoginService) {

            var vm = this;

            vm.createPersonInfo = createPersonInfo;
            vm.getPersonRoles = getPersonRoles;
            vm.getDepartments = getDepartments;
            vm.createButton = false;
            vm.personRoles = null;
            vm.valid = false;
            vm.departments = null;

            vm.person = {
                "personType": 1,
                "title": null,
                "firstName": null,
                "middleName": null,
                "lastName": null,
                "phoneOffice": null,
                "phoneMobile": null,
                "email": null
            };

            vm.emergencyContact = {
                "personType": 5,
                "firstName": null,
                "lastName": null,
                "phoneMobile": null,
                "relation": null
            };

            vm.otherInfo = {
                "role": null,
                "person": null,
                "department": null,
                "departmentObject": null,
                "designation": null,
                "devision": null,
                "parentUnit": null,
                "controllingOfficer": null,
                "controllingOfficerContact": null,
                "bloodGroup": null,
                "medicalProblems": null
            };

            vm.login = {
                loginName: null,
                password: null,
                person: null

            };

            function validatePerson() {
                vm.errorMessage = null;

                vm.valid = false;

                if (vm.person.firstName == null || vm.person.firstName == undefined || vm.person.firstName == "") {
                    vm.errorMessage = "First Name Is Required"
                } else if (vm.person.lastName == null || vm.person.lastName == undefined || vm.person.lastName == "") {
                    vm.errorMessage = "Last Name Is Required"
                } else if (vm.person.phoneMobile == null || vm.person.phoneMobile == undefined || vm.person.phoneMobile == "") {
                    vm.errorMessage = "Phone Number Is Mandatory"
                } else if (vm.person.email == null || vm.person.email == undefined || vm.person.email == "") {
                    vm.errorMessage = "Email Is Required"
                } else if (vm.login.loginName == null || vm.login.loginName == undefined || vm.login.loginName == "") {
                    vm.errorMessage = "Login Name Is Required"
                } else if (vm.emergencyContact.firstName == null || vm.emergencyContact.firstName == undefined || vm.emergencyContact.firstName == "") {
                    vm.errorMessage = "First Name Is Required"
                } else if (vm.otherInfo.parentUnit == null || vm.otherInfo.parentUnit == undefined || vm.otherInfo.parentUnit == "") {
                    vm.errorMessage = "Parent Unit Is Required"
                } else if (vm.otherInfo.role == null || vm.otherInfo.role == undefined || vm.otherInfo.role == "") {
                    vm.errorMessage = "Person Role is Mandatory"
                } else if (vm.otherInfo.departmentObject == null || vm.otherInfo.departmentObject == undefined || vm.otherInfo.departmentObject == "") {
                    vm.errorMessage = "Department is Mandatory"
                } else if (vm.otherInfo.controllingOfficer == null || vm.otherInfo.controllingOfficer == undefined || vm.otherInfo.controllingOfficer == "") {
                    vm.errorMessage = "Please Enter Officer Name"
                } else if (vm.otherInfo.controllingOfficerContact == null || vm.otherInfo.controllingOfficerContact == undefined || vm.otherInfo.controllingOfficerContact == "") {
                    vm.errorMessage = "Please Enter ControllingOfficerContact"
                } else if (vm.otherInfo.bloodGroup == null || vm.otherInfo.bloodGroup == undefined || vm.otherInfo.bloodGroup == "") {
                    vm.errorMessage = "Blood Group Should Be Enter"
                } else if (vm.otherInfo.medicalProblems == null || vm.otherInfo.medicalProblems == undefined || vm.otherInfo.medicalProblems == "") {
                    vm.errorMessage = "Enter Medical Problems";
                } else if (vm.emergencyContact.relation == null || vm.emergencyContact.relation == undefined || vm.emergencyContact.relation == "") {
                    vm.errorMessage = "Enter Relationship";
                } else {
                    vm.valid = true;
                }
                return vm.valid;
            }

            function getPersonRoles() {
                CommonService.getLovByName('Default Person Roles').then(
                    function (data) {
                        vm.personRoles = data.values;
                    }
                )
            }

            function getDepartments() {
                DepartmentService.getAllDepartments().then(
                    function (data) {
                        vm.departments = data.content;
                    }
                )
            }

            function createPersonInfo() {
                vm.createButton = true;

                /* if (validatePerson()) {*/
                CommonService.createPerson(vm.person).then(
                    function (data) {
                        vm.emergencyContact.person = data.id;
                        vm.login.person = data;
                        PersonService.createEmergencyContact(vm.emergencyContact).then(
                            function (data) {
                                vm.otherInfo.person = vm.emergencyContact.person;
                                vm.otherInfo.department = vm.otherInfo.departmentObject.id;
                                DepartmentService.createPersonsByDepartment(vm.otherInfo.department, vm.otherInfo).then(
                                    function (data) {
                                        PersonService.createPersonOtherInfo(vm.otherInfo).then(
                                            function (data) {
                                                LoginService.createLogin(vm.login, vm.person.phoneMobile, vm.person.email).then(
                                                    function (data) {
                                                        vm.createButton = false;
                                                        $state.go('app.person.all');
                                                        $rootScope.showSuccessMessage("Person Created Successfully ");
                                                    });
                                            });
                                    }
                                );
                            }
                        );
                    }
                )

                /* }*/
               /* $timeout(function () {
                    vm.createButton = false;
                }, 2000);*/


            }

            (function () {
                if ($application.homeLoaded == true) {
                    getPersonRoles();
                    getDepartments();
                }
            })();
        }
    }
);