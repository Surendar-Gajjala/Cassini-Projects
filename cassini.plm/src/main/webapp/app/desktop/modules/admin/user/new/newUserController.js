define(
    [
        'app/desktop/modules/admin/admin.module',
        'app/shared/services/core/recentlyVisitedService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/licenseService'
    ],
    function (module) {

        module.controller('NewUserController', NewItemController);

        function NewItemController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce,
                                   CommonService, LoginService, PersonGroupService, RecentlyVisitedService, LicenseService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var invalidUsername = parsed.html($translate.instant("INVALID_USERNAME")).html();
            var firstNameValidation = parsed.html($translate.instant("FIRST_NAME_VALIDATION")).html();
            var userNameValidation = parsed.html($translate.instant("USER_NAME_VALIDATION")).html();
            var passwordValidation = parsed.html($translate.instant("PASSWORD_VALIDATION_FORMAT")).html();
            var emailValidation = parsed.html($translate.instant("EMAIL_VALIDATION")).html();
            var emailCannotEmpty = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();
            var externalValidation = parsed.html($translate.instant("EXTERNAL_CHECK_BOX_VALIDATION")).html();
            var defaultGroupValidation = parsed.html($translate.instant("DEFAULT_ROLE_VALIDATION")).html();
            var userCreatedMessage = parsed.html($translate.instant("USER_CREATED_MESSAGE")).html();
            var usernameExists = parsed.html($translate.instant("USERNAME_EXISTS")).html();
            $scope.selectCustomerTitle = parsed.html($translate.instant("SELECT_CUSTOMER")).html();
            $scope.selectSupplierContactTitle = parsed.html($translate.instant("SELECT_SUPPLIER_CONTACT")).html();
            $scope.selectManPowerTitle = parsed.html($translate.instant("SELECT_MANPOWER")).html();
            var pleaseSelectSupplierContact = parsed.html($translate.instant("PLEASE_SELECT_SUPPLIER_CONTACT")).html();
            var pleaseSelectCustumer = parsed.html($translate.instant("PLEASE_SELECT_CUSTOMER")).html();
            var pleaseSelectManpowerContact = parsed.html($translate.instant("PLEASE_SELECT_MANPOWER")).html();
            var licenceWarningMessage = parsed.html($translate.instant("LICENCE_WARNING_MESSAGE")).html();
            $scope.select = parsed.html($translate.instant("SELECT")).html();
            vm.licenses = 1;
            vm.activeLicenses = 0;
            vm.selectedPersonType = "Customers";
            vm.selectedPerson = null;

            vm.newPerson = {
                id: null,
                firstName: null,
                lastName: null,
                phoneMobile: null,
                email: null,
                personType: 1,
                defaultGroup: null,
                emailVerified: false
            };

            vm.newLogin = {
                loginName: null,
                password: null,
                person: null,
                id: null,
                isActive: true,
                external: false,
                isSuperUser: false,
                isAdmin: false,
                existPerson: false
            };
            vm.personWithoutLoginCount = 0;

            vm.selectPersonType = selectPersonType;
            function selectPersonType(value, event) {
                vm.selectedPerson = null;
                vm.selectedPersonType = value;
                vm.newLogin.loginName = null;
                vm.newPerson = {
                    id: null,
                    firstName: null,
                    lastName: null,
                    phoneMobile: null,
                    email: null,
                    personType: 1,
                    defaultGroup: null,
                    emailVerified: false
                };
            }

            vm.onChangeLoginMode = onChangeLoginMode;
            function onChangeLoginMode() {
                vm.selectedPerson = null;
                vm.newLogin.loginName = null;
                vm.newPerson = {
                    id: null,
                    firstName: null,
                    lastName: null,
                    phoneMobile: null,
                    email: null,
                    personType: 1,
                    defaultGroup: null,
                    emailVerified: false
                };
            }

            vm.changePerson = changePerson;
            function changePerson(person) {
                var fullName = "";
                vm.newPerson = person;
                if (person != null && person.lastName != null && person.lastName != "") {
                    fullName = (person.firstName + "." + person.lastName).toLowerCase();
                }
                else {
                    fullName = (person.firstName).toLowerCase();
                }

                vm.newLogin.loginName = fullName;
            }

            function createUser() {
                if (validateUser()) {
                    $rootScope.showBusyIndicator("#rightSidePanel");
                    vm.newLogin.person = vm.newPerson;
                    vm.newLogin.isAdmin = vm.newLogin.isSuperUser;
                    LoginService.createLoginPerson(vm.newLogin).then(
                        function (data) {
                            if (vm.newLogin.existPerson) {
                                vm.personWithoutLoginCount--;
                                if (vm.selectedPersonType == "Customers") {
                                    vm.customers.splice(vm.customers.indexOf(vm.selectedPerson), 1);
                                } else if (vm.selectedPersonType == "SupplierContacts") {
                                    vm.supplierContacts.splice(vm.supplierContacts.indexOf(vm.selectedPerson), 1);
                                } else if (vm.selectedPersonType == "ManpowerContacts") {
                                    vm.manpowerContacts.splice(vm.manpowerContacts.indexOf(vm.selectedPerson), 1);
                                }
                                loadSelectPlaceholders();
                            }
                            vm.newPerson = {
                                id: null,
                                firstName: null,
                                lastName: null,
                                phoneMobile: null,
                                email: null,
                                personType: 1,
                                defaultGroup: null,
                                emailVerified: false
                            };

                            vm.newLogin = {
                                loginName: null,
                                password: null,
                                newPerson: null,
                                id: null,
                                isActive: true,
                                external: false,
                                isSuperUser: false,
                                existPerson: false
                            };
                            $scope.$off('app.users.new', createUser);
                            $rootScope.showSuccessMessage(userCreatedMessage);
                            $rootScope.hideBusyIndicator();
                            $scope.callback(data);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validateUser() {
                vm.valid = true;
                if (vm.newLogin.existPerson && (vm.selectedPerson == null || vm.selectedPerson == "" || vm.selectedPerson == undefined)) {
                    vm.valid = false;
                    if (vm.selectedPersonType == "Customers") {
                        $rootScope.showWarningMessage(pleaseSelectCustumer);
                    } else if (vm.selectedPersonType == "SupplierContacts") {
                        $rootScope.showWarningMessage(pleaseSelectSupplierContact);
                    } else if (vm.selectedPersonType == "ManpowerContacts") {
                        $rootScope.showWarningMessage(pleaseSelectManpowerContact);
                    }
                }
                else if (vm.newPerson.firstName == null || vm.newPerson.firstName == "" || vm.newPerson.firstName == undefined) {
                    vm.valid = false;
                    $rootScope.showWarningMessage(firstNameValidation);
                }
                else if (vm.newLogin.loginName == null || vm.newLogin.loginName == "" || vm.newLogin.loginName == undefined) {
                    vm.valid = false;
                    $rootScope.showWarningMessage(userNameValidation);
                }
                else if (vm.newPerson.defaultGroup == null || vm.newPerson.defaultGroup == "" || vm.newPerson.defaultGroup == undefined) {
                    vm.valid = false;
                    $rootScope.showWarningMessage(defaultGroupValidation);
                }
                else if (vm.newPerson.email == null || vm.newPerson.email == "" || vm.newPerson.email == undefined) {
                    vm.valid = false;
                    $rootScope.showWarningMessage(emailValidation);
                }
                else if (!validateEmail()) {
                    vm.valid = false;
                    $rootScope.showWarningMessage(emailCannotEmpty);
                }

                if (vm.newLogin.loginName != null) {
                    if (safeCharsUserNameCheck() == false) {
                        $rootScope.showWarningMessage(invalidUsername);
                        vm.valid = false;
                    }
                }

                return vm.valid;

            }

            vm.onSelectGroup = onSelectGroup;
            function onSelectGroup(group) {
                if (group.external) {
                    vm.newLogin.external = true;
                    vm.newLogin.isSuperUser = false;
                } else {
                    vm.newLogin.external = false;
                }
            }

            function validateEmail() {
                var valid = true;
                var atpos = vm.newPerson.email.indexOf("@");
                var dotpos = vm.newPerson.email.lastIndexOf(".");
                if (vm.newPerson.email != null && vm.newPerson.email != undefined && vm.newPerson.email != "") {
                    if (atpos < 1 || ( dotpos - atpos < 2 )) {
                        valid = false;
                    }
                }
                return valid
            }

            function safeCharsUserNameCheck() {
                if (vm.newLogin.loginName.includes("@")) {
                    return false;
                }
                else {
                    $scope.safeCharsUserName = false;
                    return true;
                }

            }

            function loadGroups() {
                PersonGroupService.getAllPersonGroups().then(
                    function (data) {
                        vm.groups = [];
                        angular.forEach(data, function (group) {
                            if (group.isActive) {
                                vm.groups.push(group);
                            }
                        })
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function loadPersonsWithoutLoginCount() {
                CommonService.getPersonsCountWithoutLogin().then(
                    function (data) {
                        vm.personWithoutLoginCount = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadPersonsWithoutLogin() {
                vm.customers = [];
                vm.supplierContacts = [];
                vm.manpowerContacts = [];
                RecentlyVisitedService.getPersonsWithoutLogin().then(
                    function (data) {
                        vm.customers = data.customers;
                        vm.supplierContacts = data.supplierContacts;
                        vm.manpowerContacts = data.manpowerContacts;
                        loadSelectPlaceholders();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadSelectPlaceholders() {
                if (vm.customers.length == 0) {
                    $scope.selectCustomerTitle = parsed.html($translate.instant("NO_CUSTOMERS")).html();
                } else {
                    $scope.selectCustomerTitle = parsed.html($translate.instant("SELECT_CUSTOMER")).html();
                }
                if (vm.supplierContacts.length == 0) {
                    $scope.selectSupplierContactTitle = parsed.html($translate.instant("NO_SUPPLIER_CONTACTS")).html();
                } else {
                    $scope.selectSupplierContactTitle = parsed.html($translate.instant("SELECT_SUPPLIER_CONTACT")).html();
                }
                if (vm.manpowerContacts.length == 0) {
                    $scope.selectManpowerContactTitle = parsed.html($translate.instant("NO_MANPOWERS")).html();
                } else {
                    $scope.selectManpowerContactTitle = parsed.html($translate.instant("SELECT_MANPOWER")).html();
                }
            }

            function activeLicenses() {
                LoginService.getIsActiveAndExternalLoginsCount(true, false).then(
                    function (data) {
                        vm.activeLicenses = data;
                    })
            }

            function loadNoOfLicences() {
                LicenseService.getLicense().then(function (data) {
                    vm.licenses = data.licenses
                })
            }

            vm.setUserName = setUserName;
            function setUserName() {
                if (vm.newPerson.firstName != null && vm.newPerson.firstName != "" && vm.newPerson.firstName != undefined) vm.newLogin.loginName = vm.newPerson.firstName.split(' ')[0].toLowerCase();
                if (vm.newPerson.firstName != null && vm.newPerson.firstName != "" && vm.newPerson.firstName != undefined && vm.newPerson.lastName != null && vm.newPerson.lastName != "" && vm.newPerson.lastName != undefined) vm.newLogin.loginName += '.' + vm.newPerson.lastName.toLowerCase();
            }

            vm.userActivation = userActivation;
            function userActivation() {
                if (!vm.newLogin.external) {
                    if (vm.newLogin.isActive) {
                        if (vm.licenses <= vm.activeLicenses) {
                            vm.newLogin.isActive = false;
                            $rootScope.showWarningMessage(licenceWarningMessage);
                        } else {
                            vm.activeLicenses++;
                        }
                    } else {
                        vm.activeLicenses--;
                    }
                }
            }

            (function () {
                $rootScope.hideBusyIndicator();
                loadGroups();
                loadPersonsWithoutLoginCount();
                loadPersonsWithoutLogin();
                activeLicenses();
                loadNoOfLicences();
                $rootScope.$on('app.users.new', createUser);
            })();
        }
    }
)
;