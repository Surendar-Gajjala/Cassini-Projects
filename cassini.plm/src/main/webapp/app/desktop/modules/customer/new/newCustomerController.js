define(
    [
        'app/desktop/modules/customer/customer.module',
        'app/shared/services/core/customerSupplierService'
    ],
    function (module) {

        module.controller('NewCustomerController', NewCustomerController);

        function NewCustomerController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, CustomerSupplierService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var firstNameValidation = parsed.html($translate.instant("FIRST_NAME_VALIDATION")).html();
            var emailValidation = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();
            var enterValidEmail = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();
            var nameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var contactPerson = parsed.html($translate.instant("CONTACT_PERSON")).html();
            var customerInfo = parsed.html($translate.instant("CUSTOMER_INFORMATION")).html();
            var customerCreated = parsed.html($translate.instant("CUSTOMER_CREATED")).html();

            vm.newCustomer = {
                id: null,
                name: null,
                phone: null,
                contactPerson: null,
                person: {
                    firstName: null,
                    lastName: null,
                    phoneMobile: null,
                    email: null,
                    personType: 1
                },
                email: null,
                address: null,
                notes: null
            };

            function create() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    var promise = null;
                    if ($scope.data.mode == "NEW") {
                        promise = CustomerSupplierService.createCustomer(vm.newCustomer);
                    } else {
                        promise = CustomerSupplierService.updateCustomer(vm.newCustomer);
                    }
                    if (promise != null) {
                        promise.then(
                            function (data) {
                                $scope.callback();
                                $rootScope.showSuccessMessage(customerCreated);
                                $rootScope.hideBusyIndicator();
                                $rootScope.hideSidePanel();
                                vm.newCustomer = {
                                    id: null,
                                    name: null,
                                    phone: null,
                                    contactPerson: null,
                                    person: {
                                        firstName: null,
                                        lastName: null,
                                        phoneMobile: null,
                                        email: null
                                    },
                                    email: null,
                                    address: null,
                                    notes: null
                                };
                            }, function (error) {
                                $rootScope.hideBusyIndicator();
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                }
            }

            function validate() {
                var valid = true;

                if (vm.newCustomer.name == null || vm.newCustomer.name == "" || vm.newCustomer.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                } else if (vm.newCustomer.email != null && !validateEmail(vm.newCustomer.email)) {
                    valid = false;
                    $rootScope.showWarningMessage(enterValidEmail + " for " + customerInfo);
                } else if (vm.newCustomer.person.firstName == "" || vm.newCustomer.person.firstName == null || vm.newCustomer.person.firstName == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(firstNameValidation);
                } else if (vm.newCustomer.person.email == "" || vm.newCustomer.person.email == null || vm.newCustomer.person.email == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(emailValidation + " for " + contactPerson);
                } else if (vm.newCustomer.person.email != null && !validateEmail(vm.newCustomer.person.email)) {
                    valid = false;
                    $rootScope.showWarningMessage(enterValidEmail + " for " + contactPerson);
                }


                return valid;
            }

            function validateEmail(email) {
                var valid = true;
                var atpos = email.indexOf("@");
                var dotpos = email.lastIndexOf(".");
                if (email != null && email != undefined && email != "") {
                    if (atpos < 1 || ( dotpos - atpos < 2 )) {
                        valid = false;
                    }
                }
                return valid
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    if ($scope.data.mode == "EDIT") {
                        vm.newCustomer = $scope.data.customerDetails;
                        vm.newCustomer.person = $scope.data.customerDetails.contactPersonObject;
                    }
                    $rootScope.$on('app.customers.new', create);
                //}
            })();
        }
    }
)
;