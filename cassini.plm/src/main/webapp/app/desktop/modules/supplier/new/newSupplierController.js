define(
    [
        'app/desktop/modules/customer/customer.module'
    ],
    function (module) {

        module.controller('NewSupplierController', NewSupplierController);

        function NewSupplierController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, CustomerSupplierService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var firstNameValidation = parsed.html($translate.instant("FIRST_NAME_VALIDATION")).html();
            var emailValidation = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();
            var enterValidEmail = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();
            var nameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var contactPerson = parsed.html($translate.instant("CONTACT_PERSON")).html();
            var supplierCreated = parsed.html($translate.instant("SUPPLIER_CREATED")).html();

            vm.newSupplier = {
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
                        promise = CustomerSupplierService.createSupplier(vm.newSupplier);
                    } else {
                        promise = CustomerSupplierService.updateSupplier(vm.newSupplier);
                    }
                    if (promise != null) {
                        promise.then(
                            function (data) {
                                $scope.callback();
                                $rootScope.showSuccessMessage(supplierCreated);
                                $rootScope.hideBusyIndicator();
                                $rootScope.hideSidePanel();
                                vm.newSupplier = {
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
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                }
            }

            function validate() {
                var valid = true;

                if (vm.newSupplier.name == null || vm.newSupplier.name == "" || vm.newSupplier.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                } else if (vm.newSupplier.email != null && !validateEmail(vm.newSupplier.email)) {
                    valid = false;
                    $rootScope.showWarningMessage(enterValidEmail);
                } else if (vm.newSupplier.person.firstName == "" || vm.newSupplier.person.firstName == null || vm.newSupplier.person.firstName == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(firstNameValidation);
                } else if (vm.newSupplier.person.email == "" || vm.newSupplier.person.email == null || vm.newSupplier.person.email == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(emailValidation + " For " + contactPerson);
                } else if (vm.newSupplier.person.email != null && !validateEmail(vm.newSupplier.person.email)) {
                    valid = false;
                    $rootScope.showWarningMessage(enterValidEmail + " For " + contactPerson);
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
                        vm.newSupplier = $scope.data.supplierDetails;
                        vm.newSupplier.person = $scope.data.supplierDetails.contactPersonObject;
                    }
                    $rootScope.$on('app.suppliers.new', create);
                //}
            })();
        }
    }
)
;