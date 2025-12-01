define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/shared/services/core/manpowerService',
        'app/shared/services/core/contactService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'


    ],
    function (module) {
        module.controller('ManpowerPersonsController', ManpowerPersonsController);

        function ManpowerPersonsController($scope, $rootScope, $timeout, $state, $stateParams, $translate, $cookies, $window,
                                           LoginService, ManpowerService, DialogService) {
            var vm = this;
            vm.loading = true;
            vm.manpowerId = $stateParams.manpowerId;
            vm.manpower = null;
            vm.persons = [];
            vm.manpowersContacts = [];

            var parsed = angular.element("<div></div>");

            function loadManpowerContactDetails() {
                vm.manpowersContacts = [];
                vm.loading = true;
                if (vm.manpowerId != null && vm.manpowerId != undefined) {
                    ManpowerService.getManpowerContacts(vm.manpowerId).then(
                        function (data) {
                            vm.manpowersContacts = data;
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
                    title: 'New Person',
                    template: 'app/desktop/modules/mes/manpower/newPerson/newPersonView.jsp',
                    controller: 'NewPersonController as newPersonVm',
                    resolve: 'app/desktop/modules/mes/manpower/newPerson/newPersonController',
                    width: 600,
                    showMask: true,
                    data: {
                        addedPersons: vm.manpowersContacts,
                        mode: "NEW",
                        contactDetails: null
                    },
                    buttons: [
                        {text: create, broadcast: 'app.manpower.person.new'}
                    ],
                    callback: function (contact) {
                        $timeout(function () {
                            loadManpowerContactDetails();


                        }, 500);
                    }
                };
                $rootScope.showSidePanel(options);
            }

            vm.editContact = editContact;
            function editContact(manpowerContact) {
                var options = {
                    title: 'Edit Person',
                    template: 'app/desktop/modules/mes/manpower/newPerson/newPersonView.jsp',
                    controller: 'NewPersonController as newPersonVm',
                    resolve: 'app/desktop/modules/mes/manpower/newPerson/newPersonController',
                    width: 600,
                    showMask: true,
                    data: {
                        mode: "EDIT",
                        contactDetails: manpowerContact
                    },
                    buttons: [
                        {text: update, broadcast: 'app.manpower.person.new'}
                    ],
                    callback: function () {
                        $timeout(function () {
                            loadManpowerContactDetails();


                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var deleteContactHeading = parsed.html($translate.instant("DELETE_MANPOWER_CONTACT_TITLE")).html();
            var manpowerContactDeleteTitle = parsed.html($translate.instant("MANPOWER_CONTACT_PERSON_DELETE_MSG")).html();
            var deleteDialogContact = parsed.html($translate.instant("DELETE_CONTACT_DIALOG")).html();
            var statusUpdateSuccess = parsed.html($translate.instant("STATUS_UPDATED_MSG")).html();
            var manpowerRemoveSuccess = parsed.html($translate.instant("MANPOWER_CONTACT_REMOVE_MSG")).html();

            vm.deleteManpowerContact = deleteManpowerContact;
            function deleteManpowerContact(manpowersContact) {
                ManpowerService.getManpowerContactExitOrNot(manpowersContact.id).then(
                    function (data) {
                        vm.contactExitOrNot = data;
                        if (vm.contactExitOrNot > 0) {
                            var options = {
                                title: deleteContactHeading,
                                message: manpowerContactDeleteTitle,
                                okButtonClass: 'btn-danger'
                            };

                        } else {
                            var options = {
                                title: deleteContactHeading,
                                message: deleteDialogContact + " [ " + manpowersContact.person.fullName + " ] " + "?",
                                okButtonClass: 'btn-danger'
                            };
                        }
                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                $rootScope.showBusyIndicator($('.view-container'));
                                ManpowerService.deleteManpowerContact(vm.manpowerId, manpowersContact.id).then(
                                    function (data) {
                                        $rootScope.showSuccessMessage(manpowerRemoveSuccess);
                                        loadManpowerContactDetails();
                                        $rootScope.hideBusyIndicator();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);

                                    }
                                )
                            }
                        });

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }


            function loadPersons() {
                vm.persons = [];
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && !login.external == true) {
                                vm.persons.push(login.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.updateManpowerContact = updateManpowerContact;
            function updateManpowerContact(manpower) {
                ManpowerService.updateManpowerContact(vm.manpowerId, manpower).then(
                    function (data) {
                        manpower = data;
                        $rootScope.showSuccessMessage(statusUpdateSuccess)
                    }
                )
            }

            vm.status = ['YES', 'NO'];

            (function () {
                $scope.$on('app.manpower.tabActivated', function (event, data) {
                    if (data.tabId == 'details.persons') {
                        loadPersons();
                        loadManpowerContactDetails();
                    }
                });

            })();
        }
    }
);