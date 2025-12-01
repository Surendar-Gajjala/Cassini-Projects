define(
    [
        'app/desktop/modules/users/users.module',
        'app/shared/services/core/userService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService'
    ],
    function (module) {
        module.controller('NewUserController', NewUserController);

        function NewUserController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal,
                                   UserService, CommonService, LovService) {

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;
            vm.user = $scope.data.personType;

            vm.personType = null;

            $scope.responderUtilitySettings = {
                showCheckAll: false,
                showUncheckAll: false,
                styleActive: false,
                scrollableHeight: '300', scrollable: true,
                minWidth: '262px',
                enableSearch: true,
                externalIdProp: '',
                template: '{{option}}'
            };

            vm.newUser = {
                firstName: null,
                lastName: null,
                designation: null,
                email: null,
                phoneMobile: null
            };


            vm.create = create;
            vm.showPassword = showPassword;
            vm.mode = null;
            vm.getResponders = getResponders;
            vm.newPerson = newPerson;

            function showPassword() {
                var eyeIcon = document.getElementById("showPassword");
                var newPassword = document.getElementById("newPassword");
                if (eyeIcon.attributes.class.nodeValue == "fa fa-fw fa-eye") {
                    eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye-slash";
                } else {
                    eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye";
                }

                if (newPassword.attributes.type.nodeValue == "password") {
                    newPassword.attributes.type.nodeValue = "text";
                } else {
                    newPassword.attributes.type.nodeValue = "password";
                }
            }

            function create() {
                if (validate()) {
                    vm.newUser.personType = vm.personType.id;
                    //if (checkEmailOrMobileIn(vm.assessor)) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    UserService.createResponder(vm.newUser).then(
                        function (data) {
                            var tempUtilities = vm.newUser.utilities;
                            UserService.createResponderUtility(data.id, tempUtilities).then(
                                function (data) {
                                    vm.utilities = null;
                                    tempUtilities = null;
                                    vm.newUser = null;
                                }
                            );

                            $rootScope.hideBusyIndicator();
                            $rootScope.showSuccessMessage(vm.user + " created successfully");
                            $rootScope.hideSidePanel();
                            $scope.callback(data);
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
                //}
            }

            function getResponders(mode) {
                if (vm.personType == null || vm.personType == undefined) {
                    $rootScope.showErrorMessage("Please select Utilities");
                }
                else {
                    vm.mode = 'EXIST';
                    vm.persons = [];
                    UserService.getResponders(vm.personType.id).then(
                        function (data) {
                            vm.persons = data;
                        }
                    )
                }
            }

            function newPerson(mode) {
                if (vm.personType == null || vm.personType == undefined) {
                    $rootScope.showErrorMessage("Please select ManpowerType");
                }
                else {
                    vm.mode = 'NEW';
                    vm.persons = [];
                    UserService.getFilterUsers(vm.personType.id).then(
                        function (data) {
                            vm.persons = data;
                        }
                    )
                }
            }

            /*vm.checkEmailOrMobileIn = checkEmailOrMobileIn;

             function checkEmailOrMobileIn(data1) {
             var insert = true;
             if (data1.phoneMobile != null) {
             for (var i = 0; i < vm.assessors.length; i++) {
             if (vm.assessors[i].phoneMobile == data1.phoneMobile) {
             insert = false;
             $rootScope.showWarningMessage("Given mobile number already exist");
             }
             }
             }
             if (data1.email != null) {
             for (var i = 0; i < vm.assessors.length; i++) {
             if (vm.assessors[i].email == data1.email) {
             insert = false;
             $rootScope.showWarningMessage("Given email already exist");
             }
             }
             }

             return insert;
             }*/

            function validate() {
                var valid = true;
                if (vm.newUser.firstName == null || vm.newUser.firstName == undefined || vm.newUser.firstName == "") {
                    valid = false;
                    $rootScope.showErrorMessage('First Name cannot be empty');
                }
                else if (vm.newUser.designation == null || vm.newUser.designation == undefined || vm.newUser.designation == "") {
                    valid = false;
                    $rootScope.showErrorMessage('Designation cannot be empty');
                }
                else if (vm.newUser.email == null || vm.newUser.email == undefined || vm.newUser.email == "") {
                    valid = false;
                    $rootScope.showErrorMessage(' Enter Valid Email Address ');
                }
                else if (vm.newUser.phoneMobile == null || vm.newUser.phoneMobile == undefined || vm.newUser.phoneMobile == "") {
                    valid = false;
                    $rootScope.showErrorMessage('Enter Valid Phone Number ');
                }
                else if (vm.newUser.utilities == null || vm.newUser.utilities == undefined || vm.newUser.utilities == "") {
                    valid = false;
                    $rootScope.showErrorMessage(' Select Utilities ');
                }

                return valid;
            }

            function loadUtilities() {
                LovService.getLovByName("Utilities").then(
                    function (data) {
                        vm.utilities = data.values;
                    }
                )
            }


            function loadPersonTypeByName() {
                vm.personType = CommonService.getPersonTypeByName($scope.data.personType);
            }

            (function () {
                $scope.$on('app.user.new', create);
                loadUtilities();
                loadPersonTypeByName();
            })();
        }
    }
)
;