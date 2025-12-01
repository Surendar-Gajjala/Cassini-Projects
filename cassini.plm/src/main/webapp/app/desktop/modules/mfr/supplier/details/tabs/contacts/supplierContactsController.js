define(
    [
        'app/desktop/modules/mfr/mfr.module',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/shared/services/core/supplierService',
        'app/shared/services/core/contactService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('SupplierContactsController', SupplierContactsController);

        function SupplierContactsController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, CommonService,
                                            $translate, SupplierService, LoginService, ContactService, DialogService) {
            var vm = this;
            vm.loading = true;
            vm.supplierId = $stateParams.supplierId;
            vm.supplier = null;
            vm.persons = [];
            vm.suppliersContacts = [];

            var parsed = angular.element("<div></div>");

            function loadSupplierContactDetails() {
                vm.suppliersContacts = [];
                vm.loading = true;
                if (vm.supplierId != null && vm.supplierId != undefined) {
                    SupplierService.getSupplierContacts(vm.supplierId).then(
                        function (data) {
                            vm.suppliersContacts = data;
                            vm.loading = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        });
                }

            }

            var create = parsed.html($translate.instant("CREATE")).html();
            var update = parsed.html($translate.instant("UPDATE")).html();

            vm.addContact = addContact;
            function addContact() {
                var options = {
                    title: 'New Contact',
                    template: 'app/desktop/modules/mfr/supplier/newContact/newContactView.jsp',
                    controller: 'NewContactController as newContactVm',
                    resolve: 'app/desktop/modules/mfr/supplier/newContact/newContactController',
                    width: 600,
                    showMask: true,
                    data: {
                        addedPersons: vm.suppliersContacts,
                        mode: "NEW",
                        contactDetails: null
                    },
                    buttons: [
                        {text: create, broadcast: 'app.supplier.contact.new'}
                    ],
                    callback: function (contact) {
                        $timeout(function () {
                            loadSupplierContactDetails();
                            $rootScope.loadSupplierFileCounts();

                        }, 500);
                    }
                };
                $rootScope.showSidePanel(options);
            }

            vm.editContact = editContact;
            function editContact(supplierContact) {
                var options = {
                    title: 'Edit Contact',
                    template: 'app/desktop/modules/mfr/supplier/newContact/newContactView.jsp',
                    controller: 'NewContactController as newContactVm',
                    resolve: 'app/desktop/modules/mfr/supplier/newContact/newContactController',
                    width: 600,
                    showMask: true,
                    data: {
                        mode: "EDIT",
                        contactDetails: supplierContact
                    },
                    buttons: [
                        {text: update, broadcast: 'app.supplier.contact.new'}
                    ],
                    callback: function () {
                        $timeout(function () {
                            loadSupplierContactDetails();
                            $rootScope.loadSupplierFileCounts();

                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var deleteContactHeading = parsed.html($translate.instant("DELETE_CONTACT_TITLE")).html();
            var deleteDialogContact = parsed.html($translate.instant("DELETE_CONTACT_DIALOG")).html();

            vm.deleteSupplierContact = deleteSupplierContact;
            function deleteSupplierContact(suppliersContact) {
                var options = {
                    title: deleteContactHeading,
                    message: deleteDialogContact + " [ " + suppliersContact.person.fullName + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        SupplierService.deleteSupplierContact(suppliersContact.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Deleted Successfully");
                                loadSupplierContactDetails();
                                $rootScope.loadSupplierFileCounts();
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);

                            }
                        )
                    }
                });


            }


            function loadPersons() {
                vm.persons = [];
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && login.external == true) {
                                vm.persons.push(login.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                $scope.$on('app.supplier.tabActivated', function (event, data) {
                    if (data.tabId == 'details.contacts') {
                        loadPersons();
                        loadSupplierContactDetails();
                    }
                });

            })();

        }
    }
);