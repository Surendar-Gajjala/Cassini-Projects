define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/programService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'

    ],
    function (module) {
        module.controller('ProgramResourcesController', ProgramResourcesController);

        function ProgramResourcesController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, ProgramService, DialogService, CommonService) {

            var vm = this;

            vm.loading = true;
            $rootScope.showFlag = false;
            vm.showValues = true;
            vm.personRole = null;
            vm.person = null;

            vm.programResources = [];
            vm.persons = [];
            vm.projectRoles = [];
            vm.selectedPersons = [];
            $scope.selectPersons = [];


            vm.addPerson = addPerson;
            vm.deleteProgramResource = deleteProgramResource;

            var parsed = angular.element("<div></div>");
            var selectPerson = parsed.html($translate.instant("SELECT_PERSON")).html();
            var deleteMessage = parsed.html($translate.instant("RESOURCE_REMOVED_MSG")).html();
            var deleteDialogueMsg = parsed.html($translate.instant("RESOURCE_REMOVE_DIALOG_MSG")).html();
            var deleteDialogueTitle = parsed.html($translate.instant("REMOVE_RESOURCE")).html();
            vm.addMember = parsed.html($translate.instant("ADD_MEMBER")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();
            $scope.cannotDeletePerson = parsed.html($translate.instant("CANNOT_DELETE_PERSON")).html();
            var resourceUpdatedMsg = parsed.html($translate.instant("RESOURCE_UPDATED_MSG")).html();
            var programPersonAddedSuccess = parsed.html($translate.instant("RESOURCE_ADD_SUCCESS")).html();
            var resourceUpdateMsg = parsed.html($translate.instant("RESOURCE_UPDATE_SUCCESS")).html();
            var programPersonUpdatedSuccess = parsed.html($translate.instant("PROGRAM_PERSON_UPDATED_SUCESS")).html();


            function loadProjectResources() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                vm.programResources = [];
                ProgramService.getProgramResources($stateParams.programId).then(
                    function (data) {
                        // vm.programResources = data;
                        $scope.selectPersons = data;
                        angular.forEach($scope.selectPersons, function (person) {
                            person.editMode = false;
                        });
                        CommonService.getPersonReferences($scope.selectPersons, 'person');
                        $rootScope.loadProgramCounts();
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function deleteProgramResource(resource) {
                var options = {
                    title: deleteDialogueTitle,
                    message: deleteDialogueMsg.format(resource.personObject.fullName),
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ProgramService.deleteProgramResource($stateParams.programId, resource.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(deleteMessage);
                                loadProjectResources();
                                $rootScope.loadProgramCounts();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            function addPerson() {
                var options = {
                    title: selectPerson,
                    showMask: true,
                    template: 'app/desktop/modules/pm/program/details/tabs/resources/personDialogueView.jsp',
                    controller: 'PersonDialogueController as personDialogueVm',
                    resolve: 'app/desktop/modules/pm/program/details/tabs/resources/personDialogueController',
                    width: 600,
                    data: {
                        programPersons: $scope.selectPersons
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.program.resource.new'}
                    ],
                    callback: function (result) {
                        vm.persons = result;
                        vm.itemFlag = true;
                        var selectPerson = {
                            program: $stateParams.programId,
                            person: null,
                            personObject: null,
                            role: null,
                        };
                        angular.forEach(vm.persons, function (person) {
                            var specPerson = angular.copy(selectPerson);
                            specPerson.editMode = true;
                            specPerson.isNew = true;
                            specPerson.person = person.id;
                            specPerson.personObject = person;
                            vm.selectedPersons.unshift(specPerson);
                            $scope.selectPersons.unshift(specPerson);
                        });
                    }
                };

                $rootScope.showSidePanel(options);
            }


            vm.saveProgramResource = saveProgramResource;
            function saveProgramResource(person) {
                $rootScope.showBusyIndicator($(".view-container"));
                if (person.id == null) {
                    ProgramService.createProgramResource($stateParams.programId, person).then(
                        function (data) {
                            person.id = data.id;
                            person.editMode = false;
                            vm.selectedPersons.splice(vm.selectedPersons.indexOf(person), 1);
                            $rootScope.showSuccessMessage(programPersonAddedSuccess);
                            $rootScope.loadProgramCounts();
                            $rootScope.hideBusyIndicator();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    ProgramService.updateProgramResource($stateParams.programId, person).then(
                        function (data) {
                            person.id = data.id;
                            person.editMode = false;
                            $rootScope.showSuccessMessage(resourceUpdateMsg);
                            $rootScope.hideBusyIndicator();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }

            }

            vm.editProgramResource = editProgramResource;
            function editProgramResource(resource) {
                resource.isNew = false;
                resource.editMode = true;
                resource.oldRole = resource.role;
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(person) {
                vm.selectedPersons.splice(vm.selectedPersons.indexOf(person), 1);
                $scope.selectPersons.splice($scope.selectPersons.indexOf(person), 1);
            }

            vm.cancel = cancel;
            function cancel(person) {
                person.editMode= false;

            }

            vm.saveAll = saveAll;
            function saveAll() {
                $rootScope.showBusyIndicator($('.view-container'));
                ProgramService.createProgramResources($stateParams.programId, vm.selectedPersons).then(
                    function (data) {
                        loadProjectResources();
                        vm.selectedPersons = [];
                        $rootScope.showSuccessMessage(programPersonAddedSuccess);
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
                angular.forEach(vm.selectedPersons, function (persons) {
                    $scope.selectPersons.splice($scope.selectPersons.indexOf(persons), 1);
                });
                vm.selectedPersons = [];
            }

            (function () {
                $scope.$on('app.program.tabactivated', function (event, data) {
                    if (data.tabId == 'details.resources') {
                        loadProjectResources();
                    }
                });
            })();

        }
    }
);

