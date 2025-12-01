define(['app/desktop/modules/mfr/mfrparts/mfrparts.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/supplierService',
        'app/desktop/directives/person-data/personDataDirectiveController'
    ],
    function (module) {
        module.controller('NewContactController', NewContactController);

        function NewContactController($scope, $q, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                      LoginService, ContactService, SupplierService) {

            var vm = this;
            vm.valid = true;
            var parsed = angular.element("<div></div>");
            vm.supplierId = $stateParams.supplierId;
            vm.contactMode = $scope.data.mode;
            vm.newContact = {
                id: null,
                supplier: null,
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

            function createNewContact() {
                create().then(function () {
                    vm.newContact = {
                        id: null,
                        supplier: null,
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
                if (!vm.newContact.newPerson && (vm.newContact.contact == null || vm.newContact.contact == undefined || vm.newContact.contact == "")) {
                    $rootScope.showWarningMessage(personValidation);
                    valid = false;
                } else if ( (vm.newContact.person.firstName == null || vm.newContact.person.firstName == "" || vm.newContact.person.firstName == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(firstNameValidation);
                } else if ( (vm.newContact.person.email == null || vm.newContact.person.email == "" || vm.newContact.person.email == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(emailValidation);
                } else if (vm.newContact.person.email != null && !validateEmail()) {
                    valid = false;
                    $rootScope.showWarningMessage(emailCannotEmpty);
                }
                return valid;
            }

            function validateEmail() {
                var valid = true;
                var atpos = vm.newContact.person.email.indexOf("@");
                var dotpos = vm.newContact.person.email.lastIndexOf(".");
                if (vm.newContact.person.email != null && vm.newContact.person.email != undefined && vm.newContact.person.email != "") {
                    if (atpos < 1 || ( dotpos - atpos < 2 )) {
                        valid = false;
                    }
                }
                return valid
            }

            var createContact = parsed.html($translate.instant("SUPPLIER_CREATE_CONTACT")).html();
            var updatecontact = parsed.html($translate.instant("SUPPLIER_UPDATE_CONTACT")).html();


            function create() {
                var dfd = $q.defer();
                var msg = null;
                if (validate()) {
                    vm.newContact.supplier = vm.supplierId;
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    var promise = null;
                    if ($scope.data.mode == "NEW") {
                        promise = SupplierService.createSupplierContact(vm.newContact);
                        msg = createContact;
                    } else {
                        promise = SupplierService.updateSupplierContact(vm.newContact);
                        msg = updatecontact;
                    }

                    if (promise != null) {
                        promise.then(
                            function (data) {
                                $scope.callback();
                                $rootScope.showSuccessMessage(msg);
                                $rootScope.hideBusyIndicator();
                                $rootScope.hideSidePanel();
                                vm.newContact = {
                                    id: null,
                                    supplier: null,
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
                            if (!personExist && login.isActive == true && login.external == true) {
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
                vm.newContact.person = {
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
                    vm.newContact = $scope.data.contactDetails;
                    vm.newContact.person = $scope.data.contactDetails.person;
                    $scope.callback();
                }
                loadFilteredPersons();
                $rootScope.$on('app.supplier.contact.new', createNewContact);
            })();

        }

    }
);