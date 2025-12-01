define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/shared/services/core/specificationsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('PermissionsController', PermissionsController);

        function PermissionsController($scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies, $translate,
                                       CommonService, SpecificationsService) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            $rootScope.addPerson = addPerson;
            vm.selectPermission = selectPermission;

            var addPerson = parsed.html($translate.instant("ADD_PERSON")).html();
            vm.personAdd = parsed.html($translate.instant("ADD_PERSON")).html();
            var permissionMessage = parsed.html($translate.instant("PERMISSION_MESSAGE")).html();
            var personAddedSuccessfully = parsed.html($translate.instant("PERSON_PERMISSION_MESSAGE")).html();
            var add = parsed.html($translate.instant("ADD")).html();
            vm.deletePermission = parsed.html($translate.instant("DELETE_PERMISSION")).html();

            function addPerson() {
                var options = {
                    title: addPerson,
                    template: 'app/desktop/modules/rm/specification/details/tabs/permissions/personsSelectionView.jsp',
                    controller: 'PersonsSelectionController as personVm',
                    resolve: 'app/desktop/modules/rm/specification/details/tabs/permissions/personsSelectionController',
                    data: {},
                    buttons: [
                        {text: add, broadcast: 'app.add.person'}
                    ],
                    width: 500,
                    showMask : true,
                    callback: function (result) {
                        $rootScope.showSuccessMessage(personAddedSuccessfully);
                        loadSpecPersons()

                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadSpecPersons() {
                SpecificationsService.getAllSpecPersons($stateParams.specId).then(
                    function (data) {
                        vm.specPersons = data;
                        angular.forEach(vm.specPersons, function (person) {
                            if (person.editPermission == true && person.deletePermission == true && person.acceptRejectPermission == true && person.statusChangePermission == true && person.importPermission == true && person.exportPermission == true) {
                                person.all = true;
                            } else {
                                person.all = false;
                            }
                        });
                        CommonService.getPersonReferences(vm.specPersons, 'specUser');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function selectPermission(spec) {
                SpecificationsService.createSpecPermission(spec).then(
                    function (data) {
                        spec.editPermission = data.editPermission;
                        spec.deletePermission = data.deletePermission;
                        spec.acceptRejectPermission = data.acceptRejectPermission;
                        spec.statusChangePermission = data.statusChangePermission;
                        spec.importPermission = data.importPermission;
                        spec.exportPermission = data.exportPermission;
                        if (spec.editPermission == true && spec.deletePermission == true && spec.acceptRejectPermission == true && spec.statusChangePermission == true && spec.importPermission == true && spec.exportPermission == true) {
                            spec.all = true;
                        } else {
                            spec.all = false;
                        }
                        $rootScope.showSuccessMessage(permissionMessage);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.selectAllPermission = selectAllPermission;
            function selectAllPermission(spec) {
                if (spec.all == true) {
                    spec.editPermission = true;
                    spec.deletePermission = true;
                    spec.acceptRejectPermission = true;
                    spec.statusChangePermission = true;
                    spec.importPermission = true;
                    spec.exportPermission = true;
                }
                if (spec.all == false) {
                    spec.editPermission = false;
                    spec.deletePermission = false;
                    spec.acceptRejectPermission = false;
                    spec.statusChangePermission = false;
                    spec.importPermission = false;
                    spec.exportPermission = false;
                }
                SpecificationsService.createSpecPermission(spec).then(
                    function (data) {
                        spec = data;
                        $rootScope.showSuccessMessage(permissionMessage)
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            vm.deleteSpecPerson = deleteSpecPerson;
            var deleteSpecPersonDialogTitle = parsed.html($translate.instant("DELETE_SPECPERSON")).html();
            var deleteSpecPersonDialogMessage = parsed.html($translate.instant("DELETE_SPECPERSON_DIALOG_MESSAGE")).html();
            var specPersonDeletedMessage = parsed.html($translate.instant("SPECPERSON_DELETE_MSG")).html();

            function deleteSpecPerson(spec) {
                var options = {
                    title: deleteSpecPersonDialogTitle,
                    message: deleteSpecPersonDialogMessage + " [ " + spec.specUserObject.fullName + " ] " + " ?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        SpecificationsService.deleteSpecPerson(spec.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(specPersonDeletedMessage);
                                loadSpecPersons();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                             }
                        )
                    }
                });
            }

            (function () {
            })();
            $scope.$on('app.spec.tabactivated', function (event, data) {
                if (data.tabId == 'details.permissions') {
                    loadSpecPersons();
                }
            });
        }
    }
)
;