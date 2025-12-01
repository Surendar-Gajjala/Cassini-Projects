define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/shared/services/core/glossaryService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('GlossaryPermissionsController', GlossaryPermissionsController);

        function GlossaryPermissionsController($scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies, $translate,
                                               CommonService, GlossaryService) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            $rootScope.addGlossaryPersons = addGlossaryPersons;
            vm.selectPermission = selectPermission;

            var addPerson = parsed.html($translate.instant("ADD_PERSON")).html();
            var permissionMessage = parsed.html($translate.instant("PERMISSION_MESSAGE")).html();
            var personAddedSuccessfully = parsed.html($translate.instant("PERSON_PERMISSION_MESSAGE")).html();
            var add = parsed.html($translate.instant("ADD")).html();
            vm.deletePermission = parsed.html($translate.instant("DELETE_PERMISSION")).html();

            function addGlossaryPersons() {
                var options = {
                    title: addPerson,
                    template: 'app/desktop/modules/rm/glossary/details/tabs/permissions/personsSelectionView.jsp',
                    controller: 'PersonsSelectionController as personVm',
                    resolve: 'app/desktop/modules/rm/glossary/details/tabs/permissions/personsSelectionController',
                    data: {},
                    buttons: [
                        {text: add, broadcast: 'app.add.person'}
                    ],
                    width: 500,
                    showMask : true,
                    callback: function (result) {
                        $rootScope.showSuccessMessage(personAddedSuccessfully);
                        loadGlossaryPersons()

                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadGlossaryPersons() {
                GlossaryService.getAllGlossaryPersons($stateParams.glossaryId).then(
                    function (data) {
                        vm.glossaryPersons = data;
                        angular.forEach(vm.glossaryPersons, function (person) {
                            if (person.editPermission == true && person.deletePermission == true && person.acceptRejectPermission == true && person.statusChangePermission == true && person.importPermission == true && person.exportPermission == true) {
                                person.all = true;
                            } else {
                                person.all = false;
                            }
                        });
                        CommonService.getPersonReferences(vm.glossaryPersons, 'glossaryUser');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function selectPermission(glossaryPerson) {
                GlossaryService.createGlossaryPermission(glossaryPerson).then(
                    function (data) {
                        glossaryPerson.editPermission = data.editPermission;
                        glossaryPerson.deletePermission = data.deletePermission;
                        glossaryPerson.acceptRejectPermission = data.acceptRejectPermission;
                        glossaryPerson.statusChangePermission = data.statusChangePermission;
                        glossaryPerson.importPermission = data.importPermission;
                        glossaryPerson.exportPermission = data.exportPermission;
                        if (glossaryPerson.editPermission == true && glossaryPerson.deletePermission == true && glossaryPerson.acceptRejectPermission == true && glossaryPerson.statusChangePermission == true && glossaryPerson.importPermission == true && glossaryPerson.exportPermission == true) {
                            glossaryPerson.all = true;
                        } else {
                            glossaryPerson.all = false;
                        }
                        $rootScope.showSuccessMessage(permissionMessage);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.selectAllPermission = selectAllPermission;
            function selectAllPermission(glossaryPerson) {
                if (glossaryPerson.all == true) {
                    glossaryPerson.editPermission = true;
                    glossaryPerson.deletePermission = true;
                    glossaryPerson.acceptRejectPermission = true;
                    glossaryPerson.statusChangePermission = true;
                    glossaryPerson.importPermission = true;
                    glossaryPerson.exportPermission = true;
                }
                if (glossaryPerson.all == false) {
                    glossaryPerson.editPermission = false;
                    glossaryPerson.deletePermission = false;
                    glossaryPerson.acceptRejectPermission = false;
                    glossaryPerson.statusChangePermission = false;
                    glossaryPerson.importPermission = false;
                    glossaryPerson.exportPermission = false;
                }
                GlossaryService.createGlossaryPermission(glossaryPerson).then(
                    function (data) {
                        glossaryPerson = data;
                        $rootScope.showSuccessMessage(permissionMessage)
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.deleteGlossaryPerson = deleteGlossaryPerson;
            var deleteSpecPersonDialogTitle = parsed.html($translate.instant("DELETE_SPECPERSON")).html();
            var deleteSpecPersonDialogMessage = parsed.html($translate.instant("DELETE_SPECPERSON_DIALOG_MESSAGE")).html();
            var specPersonDeletedMessage = parsed.html($translate.instant("SPECPERSON_DELETE_MSG")).html();

            function deleteGlossaryPerson(glossary) {
                var options = {
                    title: deleteSpecPersonDialogTitle,
                    message: deleteSpecPersonDialogMessage + " [ " + glossary.glossaryUserObject.fullName + " ] " + " ?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        GlossaryService.deleteGlossaryPerson(glossary.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(specPersonDeletedMessage);
                                loadGlossaryPersons();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            (function () {
            })();
            $scope.$on('app.glossary.tabactivated', function (event, data) {
                if (data.tabId == 'details.permissions') {
                    loadGlossaryPersons();
                }
            });
        }
    }
)
;