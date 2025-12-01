define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/shared/services/core/activityService',
        'app/shared/services/core/shiftService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'

    ],
    function (module) {
        module.controller('ShiftPersonsController', ShiftPersonsController);

        function ShiftPersonsController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, ProjectService, DialogService, CommonService, ShiftService) {

            var vm = this;

            vm.loading = true;
            $rootScope.showFlag = false;
            vm.showValues = true;
            vm.personRole = null;
            vm.person = null;

            vm.projectMembers = [];
            vm.persons = [];
            vm.projectRoles = [];


            vm.addShiftPerson = addShiftPerson;
            vm.deleteShiftPerson = deleteShiftPerson;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.resetPage = resetPage;

            var parsed = angular.element("<div></div>");
            vm.selectedPersons = [];
            $scope.selectPersons = [];
            var selectPerson = parsed.html($translate.instant("SELECT_PERSON")).html();
            var deleteMessage = parsed.html($translate.instant("DELETE_SHIFT_PERSON_SUCCESS")).html();
            var deleteDialogueMsg = parsed.html($translate.instant("DELETE_PERSON_VALIDATE")).html();
            var deleteDialogueValidateMsg = parsed.html($translate.instant("SHIFT_DELETE_DIALOG")).html();
            var deleteDialogueTitle = parsed.html($translate.instant("SHIFT_DELETE_PERSON")).html();
            vm.addMember = parsed.html($translate.instant("ADD_MEMBER")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();
            $scope.cannotDeletePerson = parsed.html($translate.instant("CANNOT_DELETE_PERSON")).html();

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    order: "DESC"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            vm.projectMembers = angular.copy(pagedResults);

            function resetPage() {
                vm.projectMembers = angular.copy(pagedResults);
                vm.pageable.page = 0;
            }

            function nextPage() {
                if (vm.projectMembers.last != true) {
                    vm.pageable.page++;
                    loadShiftPersons();
                }
            }

            function previousPage() {
                if (vm.projectMembers.first != true) {
                    vm.pageable.page--;
                    loadShiftPersons();
                }
            }

            function loadShiftPersons() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                ShiftService.getShiftPersons($stateParams.shiftId, pageable).then(
                    function (data) {
                        $scope.selectPersons = data;
                        angular.forEach($scope.selectPersons.content, function (person) {
                            person.editMode = false;
                        });
                        CommonService.getPersonReferences($scope.selectPersons.content, 'person');
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var projectPersonDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            var shiftPersonAddedSuccess = parsed.html($translate.instant("SHIFT_PERSON_ADDED_SUCESS")).html();
            var shiftPersonUpdatedSuccess = parsed.html($translate.instant("SHIFT_PERSON_UPDATED_SUCESS")).html();

            function deleteShiftPerson(projectPerson) {
                var options = {
                    title: deleteDialogueTitle,
                    message: deleteDialogueValidateMsg.format(projectPerson.personObject.fullName),
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        ShiftService.deleteShiftPerson($stateParams.shiftId, projectPerson.person).then(
                            function (data) {
                                $rootScope.showSuccessMessage(deleteMessage);
                                loadShiftPersons();
                                $rootScope.loadShiftObjectCounts();
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            function addShiftPerson() {
                var options = {
                    title: selectPerson,
                    showMask: true,
                    template: 'app/desktop/modules/mes/shift/details/tabs/persons/selectManpowerPersonsView.jsp',
                    controller: 'SelectManpowerPersonsController as selectManpowerPersonsVm',
                    resolve: 'app/desktop/modules/mes/shift/details/tabs/persons/selectManpowerPersonsController',
                    width: 600,
                    data: {},
                    buttons: [
                        {text: addButton, broadcast: 'app.select.manpower.person.add'}
                    ],
                    callback: function (result) {
                        vm.persons = result;
                        vm.itemFlag = true;
                        var selectPerson = {
                            shift: $stateParams.shiftId,
                            person: null,
                            personObject: null,
                            notes: null
                        };
                        angular.forEach(vm.persons, function (person) {
                            var manpowerPerson = angular.copy(selectPerson);
                            manpowerPerson.editMode = true;
                            manpowerPerson.person = person.id;
                            manpowerPerson.personObject = person;
                            vm.selectedPersons.unshift(manpowerPerson);
                            $scope.selectPersons.content.unshift(manpowerPerson);
                        });

                    }
                };

                $rootScope.showSidePanel(options);
            }


            vm.saveShiftPerson = saveShiftPerson;
            function saveShiftPerson(person) {
                $rootScope.showBusyIndicator($('.view-container'));
                if (person.id == null) {
                    ShiftService.createShiftPerson($stateParams.shiftId, person).then(
                        function (data) {
                            person.id = data.id;
                            person.editMode = false;
                            $rootScope.loadShiftObjectCounts();
                            vm.selectedPersons.splice(vm.selectedPersons.indexOf(person), 1);
                            $rootScope.showSuccessMessage(shiftPersonAddedSuccess);
                            $rootScope.hideBusyIndicator();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    ShiftService.updateShiftPerson($stateParams.shiftId, person).then(
                        function (data) {
                            person.id = data.id;
                            person.editMode = false;
                            $rootScope.showSuccessMessage(shiftPersonUpdatedSuccess);
                            $rootScope.hideBusyIndicator();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.onCancel = onCancel;
            function onCancel(person) {
                vm.selectedPersons.splice(vm.selectedPersons.indexOf(person), 1);
                $scope.selectPersons.content.splice($scope.selectPersons.content.indexOf(person), 1);
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(person) {
                person.editMode = false;
            }


            vm.editShiftPerson = editShiftPerson;
            function editShiftPerson(shiftPerson) {
                vm.itemFlag = false;
                shiftPerson.editMode = true;

            }

            vm.saveAll = saveAll;
            function saveAll() {
                $rootScope.showBusyIndicator($('.view-container'));
                ShiftService.createShiftMultiplePersons($stateParams.shiftId, vm.selectedPersons).then(
                    function (data) {
                        loadShiftPersons();
                        vm.selectedPersons = [];
                        $rootScope.loadShiftObjectCounts();
                        $rootScope.showSuccessMessage(shiftPersonAddedSuccess);
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )

            }

            vm.removeAll = removeAll;
            function removeAll() {
                angular.forEach(vm.selectedPersons, function (person) {
                    $scope.selectPersons.content.splice($scope.selectPersons.content.indexOf(person), 1);
                });
                vm.selectedPersons = [];
            }

            (function () {
                $scope.$on('app.shift.tabActivated', function (event, data) {
                    if (data.tabId == 'details.persons') {
                        loadShiftPersons();
                    }
                });
            })();

        }
    }
)
;