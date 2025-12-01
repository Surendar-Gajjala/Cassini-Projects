define(['app/desktop/modules/tm/tm.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/tm/taskService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemTypeService'

    ],
    function (module) {
        module.controller('ManpowerDialogueController', ManpowerDialogueController);
        function ManpowerDialogueController($scope, $rootScope, $timeout, $state, $stateParams, ProjectService,
                                            CommonService, TaskService, ItemService) {
            var vm = this;

            vm.select = select;
            vm.create = create;
            vm.checkAll = checkAll;
            vm.selectedAll = false;
            var persons = $scope.data.personResources;

            vm.projectPersons = [];
            vm.selectedPersons = [];
            // vm.applyFilters = applyFilters;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilters = clearFilters;

            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.resetPage = resetPage;
            vm.showSearchMode = false;
            $scope.freeTextQuery = null;
            vm.personView = true;
            vm.personRoles = false;

            vm.emptyFilters = {
                itemType: null,
                itemNumber: null,
                active: null,
                unitPrice: null,
                unitCost: null,
                description: null,
                createdDate: null,
                modifiedDate: null
            };

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: 'modifiedBy',
                    order: 'desc'
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                first: true,
                numberOfElements: 0
            };

            vm.projectPersons = angular.copy(pagedResults);

            vm.emptyFilters = {
                firstName: null,
                phoneMobile: null,
                email: null
            };

            function clearFilters() {
                vm.emptyFilters.firstName = null;
                vm.emptyFilters.phoneMobile = null;
                vm.selectedPersons = [];
                loadProjectPersons();
                vm.clear = false;
                vm.selectedAll = false;
            }

            function freeTextSearch(freeText) {
                if (!vm.showSearchMode) {
                    vm.pageable.page = 0;
                }
                vm.showSearchMode = true;
                $scope.freeTextQuery = freeText;
                vm.emptyFilters.searchQuery = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.selectedAll = false;
                    ProjectService.getFilterdProjectPersons($stateParams.projectId, vm.pageable, freeText).then(
                        function (data) {
                            vm.projectPersons = data;
                            vm.loading = false;
                            var count = 0;
                            vm.projectPersons.numberOfElements = (vm.pageable.page + 1) * vm.pageable.size;
                            if (vm.projectPersons.numberOfElements > vm.projectPersons.totalElements) {
                                vm.projectPersons.numberOfElements = vm.projectPersons.totalElements;
                            }
                            CommonService.getPersonReferences(vm.projectPersons.content, 'person');
                            vm.clear = true;
                            angular.forEach(vm.projectPersons.content, function (person) {
                                angular.forEach(vm.selectedPersons, function (selectedPerson) {
                                    if (person.itemNumber == selectedPerson.itemNumber && person.sheet == selectedPerson.sheet) {
                                        person.selected = true;
                                        count++;
                                    }
                                })
                            });
                            if (vm.projectPersons.content.length > 0 && count == vm.projectPersons.content.length) {
                                vm.selectedAll = true;
                            }
                        }, function (error) {
                            vm.loading = false;
                        }
                    );
                } else {
                    resetPage();
                    loadProjectPersons();
                }
            }

            function nextPage() {
                if (vm.projectPersons.last != true) {
                    vm.pageable.page++;
                    vm.selectedAll = false;
                    if (vm.showSearchMode) {
                        freeTextSearch($scope.freeTextQuery);
                    }
                    else {
                        loadProjectPersons();
                    }
                }
            }

            function previousPage() {
                if (vm.projectPersons.first != true) {
                    vm.pageable.page--;
                    vm.selectedAll = false;
                    if (vm.showSearchMode) {
                        freeTextSearch($scope.freeTextQuery);
                    }
                    else {
                        loadProjectPersons();
                    }
                }
            }

            function resetPage() {
                vm.pageable.page = 0;
                vm.showSearchMode = false;
            }

            function select(projectPerson) {
                var pr = projectPerson.personObject;
                var flag = true;
                if (projectPerson.selected == false) {
                    vm.selectedAll = false;
                    var index = vm.selectedPersons.indexOf(projectPerson);
                    vm.selectedPersons.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedPersons, function (selectPerson) {
                        if (selectPerson.id == pr.id) {
                            flag = false;
                            var index = vm.selectedPersons.indexOf(projectPerson);
                            vm.selectedPersons.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedPersons.push(projectPerson);
                    }
                }
                if (vm.selectedPersons.length == vm.projectPersons.content.length) {
                    vm.selectedAll = true;
                }
            }

            vm.selectRole = selectRole;
            vm.selectedRoles = [];
            function selectRole(item) {
                var flag = true;
                vm.error = "";
                angular.forEach(vm.selectedRoles, function (role) {
                    vm.selectedAll = false;
                    if (role.id == item.id) {
                        flag = false;
                        var index = vm.selectedRoles.indexOf(item);
                        vm.selectedRoles.splice(index, 1);
                    }
                });
                if (flag) {
                    vm.selectedRoles.push(item);
                }
                if (vm.selectedRoles.length == vm.projectRoles.content.length) {
                    vm.selectedAll = true;
                }
            }

            function checkAll() {
                if (vm.selectedAll) {
                    angular.forEach(vm.projectPersons.content, function (person) {
                        if (!person.selected) {
                            person.selected = vm.selectedAll;
                            vm.selectedPersons.push(person);
                        }
                    });
                } else {
                    angular.forEach(vm.projectPersons.content, function (person) {
                        var index = vm.selectedPersons.indexOf(person);
                        vm.selectedPersons.splice(index, 1);
                        person.selected = vm.selectedAll;
                    });
                }
            }

            vm.checkAllRoles = checkAllRoles;
            function checkAllRoles() {
                if (vm.selectedAll) {
                    angular.forEach(vm.projectRoles.content, function (person) {
                        if (!person.selected) {
                            person.selected = vm.selectedAll;
                            vm.selectedRoles.push(person);
                        }
                    });
                } else {
                    angular.forEach(vm.projectRoles.content, function (person) {
                        var index = vm.selectedRoles.indexOf(person);
                        vm.selectedRoles.splice(index, 1);
                        person.selected = vm.selectedAll;
                    });
                }
            }

            function create() {
                if (vm.selectedPersons.length == 0 && vm.selectedRoles.length == 0) {
                    $rootScope.showErrorMessage("Please add  atleast one manpower");
                }
                if (vm.selectedPersons.length > 0) {
                    if (vm.selectedPersons.length != 0) {
                        vm.resources = [];
                        angular.forEach(vm.selectedPersons, function (projectPerson) {
                            var resource = {
                                referenceId: projectPerson.person,
                                project: $stateParams.projectId,
                                task: $stateParams.taskId,
                                quantity: 1,
                                units: 1,
                                resourceType: "MANPOWERTYPE"
                            };
                            vm.resources.push(resource);
                        });
                        TaskService.createProjectResource($stateParams.projectId, vm.resources).then(
                            function (data) {
                                $rootScope.hideSidePanel('left');
                                $scope.callback(data);
                                vm.creating = false;
                                vm.selectedPersons = [];
                                $rootScope.showSuccessMessage("Person(s) added successfully");
                            });
                    } else {
                        $rootScope.showErrorMessage("Please add  atleast one manpower");
                    }
                } else if (vm.selectedRoles.length > 0) {
                    vm.resources = [];
                    angular.forEach(vm.selectedRoles, function (role) {
                        var resource = {
                            referenceId: role.id,
                            project: $stateParams.projectId,
                            task: $stateParams.taskId,
                            units: 1,
                            resourceType: "ROLE"
                        };
                        vm.resources.push(resource);
                    });
                    TaskService.createProjectResource($stateParams.projectId, vm.resources).then(
                        function (data) {
                            $rootScope.hideSidePanel('left');
                            $scope.callback(data);
                            vm.creating = false;
                            vm.selectedPersons = [];
                            $rootScope.showSuccessMessage("Role(s) added successfully");
                        });
                }
            }

            function loadProjectPersons() {
                vm.loading = true;
                ProjectService.getProjectPagedPersons($stateParams.projectId, vm.pageable).then(
                    function (data) {
                        vm.projectPersons = data;
                        angular.forEach(persons, function (person) {
                                angular.forEach(vm.projectPersons.content, function (projectPerson) {
                                    if (projectPerson.person == person.referenceIdObject.id) {
                                        var index = vm.projectPersons.content.indexOf(projectPerson);
                                        vm.projectPersons.content.splice(index, 1);
                                        vm.projectPersons.numberOfElements--;
                                    }
                                })
                            }
                        );
                        CommonService.getPersonReferences(vm.projectPersons.content, 'person');
                    }
                )

            }

            function loadProjectRoles() {
                ProjectService.getProjectPagedRoles($stateParams.projectId, vm.pageable).then(
                    function (data) {
                        vm.projectRoles = [];
                        vm.loading = false;
                        vm.projectRoles = data;
                        angular.forEach(persons, function (person) {
                                angular.forEach(vm.projectRoles.content, function (projectPerson) {
                                    if (projectPerson.id == person.referenceIdObject.id) {
                                        var index = vm.projectRoles.content.indexOf(projectPerson);
                                        vm.projectRoles.content.splice(index, 1);
                                        vm.projectRoles.numberOfElements--;
                                    }
                                })
                            }
                        );

                    }
                );
            }

            vm.changeView = changeView;
            function changeView(changeValue) {
                if (changeValue == "roles") {
                    vm.personRoles = true;
                    vm.personView = false;
                    vm.selectedAll = false;
                    vm.selectedPersons = [];
                    loadProjectRoles();
                } else if (changeValue == "manPower") {
                    loadProjectPersons();
                    vm.personRoles = false;
                    vm.personView = true;
                    vm.selectedAll = false;
                    vm.selectedRoles = [];
                }

            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.manpower.new', create);
                    loadProjectPersons();
                }
            })();
        }
    }
);