define(['app/desktop/modules/mes/mes.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/manpowerService',
        'app/desktop/directives/person-data/personDataDirectiveController'
    ],
    function (module) {
        module.controller('NewPersonController', NewPersonController);

        function NewPersonController($scope, $q, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                      LoginService, ContactService, ManpowerService) {

            var vm = this;
            vm.valid = true;
            var parsed = angular.element("<div></div>");
            vm.manpowerId = $stateParams.manpowerId;
            vm.contactMode = $scope.data.mode;
            vm.newManpower = {
                id: null,
                manpower: null,
                contact: null,
                role: null,
                active: true,
                person: {
                    id: null,
                    firstName: null,
                    lastName: null,
                    phoneMobile: null,
                    email: null,
                    personType: 1
                },
                newPerson: true
            };

            function createNewManpower() {
                create().then(function () {
                    vm.newManpower = {
                        id: null,
                        manpower: null,
                        contact: null,
                        role: null,
                        active: true,
                        person: {
                            id: null,
                            firstName: null,
                            lastName: null,
                            phoneMobile: null,
                            email: null,
                            personType: 1
                        },
                        newPerson: true
                    };
                    $scope.callback();
                    $rootScope.hideBusyIndicator();
                })

            }

            var personValidation = parsed.html($translate.instant("PERSON_CANNOT_BE_EMPTY")).html();
            var firstNameValidation = parsed.html($translate.instant("FIRST_NAME_VALIDATION")).html();
            var emailValidation = parsed.html($translate.instant("EMAIL_VALIDATION")).html();
            var emailCannotEmpty = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();

            function validate() {
                var valid = true;
                if (!vm.newManpower.newPerson && (vm.newManpower.contact == null || vm.newManpower.contact == undefined || vm.newManpower.contact == "")) {
                    $rootScope.showWarningMessage(personValidation);
                    valid = false;
                } else if ( (vm.newManpower.person.firstName == null || vm.newManpower.person.firstName == "" || vm.newManpower.person.firstName == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(firstNameValidation);
                } else if ( (vm.newManpower.person.email == null || vm.newManpower.person.email == "" || vm.newManpower.person.email == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(emailValidation);
                } else if (vm.newManpower.person.email != null && !validateEmail()) {
                    valid = false;
                    $rootScope.showWarningMessage(emailCannotEmpty);
                }
                return valid;
            }

            function validateEmail() {
                var valid = true;
                var atpos = vm.newManpower.person.email.indexOf("@");
                var dotpos = vm.newManpower.person.email.lastIndexOf(".");
                if (vm.newManpower.person.email != null && vm.newManpower.person.email != undefined && vm.newManpower.person.email != "") {
                    if (atpos < 1 || ( dotpos - atpos < 2 )) {
                        valid = false;
                    }
                }
                return valid
            }

            var createContact = parsed.html($translate.instant("MANPOWER_CREATE_CONTACT")).html();
            var updateContact = parsed.html($translate.instant("MANPOWER_UPDATE_CONTACT")).html();


            function create() {
                var dfd = $q.defer();
                var msg = null;
                if (validate()) {
                    vm.newManpower.manpower = vm.manpowerId;
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    var promise = null;
                    if ($scope.data.mode == "NEW") {
                        promise = ManpowerService.createManpowerContact(vm.manpowerId, vm.newManpower);
                        msg = createContact;
                    } else {
                        promise = ManpowerService.updateManpowerContact(vm.manpowerId, vm.newManpower);
                        msg = updateContact;
                    }

                    if (promise != null) {
                        promise.then(
                            function (data) {
                                $scope.callback();
                                $rootScope.showSuccessMessage(msg);
                                $rootScope.hideBusyIndicator();
                                $rootScope.hideSidePanel();
                                vm.newManpower = {
                                    id: null,
                                    manpower: null,
                                    contact: null,
                                    role: null,
                                    active: true,
                                    person: {
                                        id: null,
                                        firstName: null,
                                        lastName: null,
                                        phoneMobile: null,
                                        email: null,
                                        personType: 1
                                    },
                                    newPerson: true
                                };
                            }, function (error) {
                                $rootScope.hideBusyIndicator();
                                $rootScope.showErrorMessage(error.message);
                            }
                        );

                    }

                    return dfd.promise;
                }
            }

            vm.persons = [];
            function loadFilteredPersons() {
                vm.persons = [];
                LoginService.getAllLogins().then(
                    function (logins) {
                        var addedPersons = $scope.data.addedPersons;
                        vm.persons = [];
                        angular.forEach(logins, function (login) {
                            var personExist = false;
                            angular.forEach(addedPersons, function (addedPerson) {
                                if (addedPerson.person.id == login.person.id) {
                                    personExist = true;
                                }
                            });
                            if (!personExist && login.isActive == true && !login.external == true) {
                                vm.persons.push(login.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.onChangePerson = onChangePerson;
            function onChangePerson() {
                vm.newManpower.person = {
                    id: null,
                    firstName: null,
                    lastName: null,
                    phoneMobile: null,
                    email: null,
                    personType: 1
                }
            }

            (function () {
                if ($scope.data.mode == "EDIT") {
                    vm.newManpower = $scope.data.contactDetails;
                    vm.newManpower.person = $scope.data.contactDetails.person;
                    $scope.callback();
                }
                loadFilteredPersons();
                $rootScope.$on('app.manpower.person.new', createNewManpower);
            })();

        }

    }
);