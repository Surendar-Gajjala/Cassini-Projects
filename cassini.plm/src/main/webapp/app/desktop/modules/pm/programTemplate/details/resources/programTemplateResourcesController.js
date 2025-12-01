define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/programTemplateService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'

    ],
    function (module) {
        module.controller('ProgramTemplateResourcesController', ProgramTemplateResourcesController);

        function ProgramTemplateResourcesController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, ProgramTemplateService, DialogService, CommonService) {

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
            var programPersonAddedSuccess = parsed.html($translate.instant("RESOURCE_ADD_SUCCESS")).html();
            var resourceUpdateMsg = parsed.html($translate.instant("RESOURCE_UPDATE_SUCCESS")).html();

            function loadProgramTemplateResources() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                vm.programResources = [];
                ProgramTemplateService.getProgramTemplateResources($stateParams.templateId).then(
                    function (data) {
                        // vm.programResources = data;
                        $scope.selectPersons = data;
                        angular.forEach($scope.selectPersons,  function (person) {
                            person.editMode = false;
                        });
                        CommonService.getPersonReferences($scope.selectPersons,  'person');
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
                        ProgramTemplateService.deleteProgramTemplateResource($stateParams.templateId, resource.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(deleteMessage);
                                loadProgramTemplateResources();
                                $rootScope.loadProgramTemplateCounts();
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
                    template: 'app/desktop/modules/pm/programTemplate/details/resources/personDialogueView.jsp',
                    controller: 'PersonDialogueController as personDialogueVm',
                    resolve: 'app/desktop/modules/pm/programTemplate/details/resources/personDialogueController',
                    width: 600,
                    data: {
                        programPersons: $scope.selectPersons
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.programtemplate.resource.new'}
                    ],
                    callback: function (result) {
                        vm.persons = result;
                        vm.itemFlag = true;
                        var selectPerson = {
                            template: $stateParams.templateId,
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
                    ProgramTemplateService.createProgramTemplateResource($stateParams.templateId, person).then(
                        function (data) {
                            person.id = data.id;
                            person.editMode = false;
                            vm.selectedPersons.splice(vm.selectedPersons.indexOf(person), 1);
                            $rootScope.showSuccessMessage(programPersonAddedSuccess);
                            $rootScope.loadProgramTemplateCounts();
                            $rootScope.hideBusyIndicator();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    ProgramTemplateService.updateProgramTemplateResource($stateParams.templateId, person).then(
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
                ProgramTemplateService.createProgramTemplateResources($stateParams.templateId, vm.selectedPersons).then(
                    function (data) {
                        loadProgramTemplateResources();
                        vm.selectedPersons = [];
                        $rootScope.loadProgramTemplateCounts();
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
                $scope.$on('app.programTemplate.tabActivated', function (event, data) {
                    if (data.tabId == 'details.resources') {
                        loadProgramTemplateResources();
                    }
                });
            })();

        }
    }
);

