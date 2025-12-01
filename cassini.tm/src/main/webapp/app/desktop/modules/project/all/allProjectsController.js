/**
 * Created by Anusha on 11-07-2016.
 */
define(['app/desktop/modules/project/project.module',
        'app/desktop/modules/project/new/newProjectDialogueController',
        'app/shared/services/projectService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('AllProjectsController', AllProjectsController);

        function AllProjectsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application, $uibModal, ProjectService,
                                       DialogService) {
            var vm = this;
            $rootScope.viewInfo.icon = "fa flaticon-businessman277";
            $rootScope.viewInfo.title = "Projects"

            vm.createProject = createProject;
            vm.deleteProject = deleteProject;
            vm.loadProjects = loadProjects;
            vm.projects = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.showProjectDetails = showProjectDetails;
            vm.freeTextSearch = freeTextSearch;
            vm.loading = true;
            vm.clear = false;
            vm.clearFilter = clearFilter;
            vm.filters = {
                searchQuery: null
            }


            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
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
            vm.projects = angular.copy(pagedResults);

            function clearFilter() {
                loadProjects();
                vm.clear = false;
            }



            function createProject() {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/desktop/modules/project/new/newProjectDialogueView.jsp',
                    controller: 'NewProjectDialogueController as newProjectDialogueVm',
                    size: 'md'
                });
                modalInstance.result.then(
                    function (result) {
                        ProjectService.createProject(result).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Project Created Successfully ");
                                loadProjects();
                            }
                        )

                    }
                );
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.filters.searchQuery = freeText;
                    ProjectService.freeTextSearch(vm.filters, pageable).then(
                        function (data) {
                            vm.projects = data;
                        }
                    )
                    vm.clear = true;
                } else {
                    resetPage();
                    loadProjects();
                }
            }

            function resetPage() {
                pageable.page = 0;
            }

            function nextPage() {
                if (vm.projects.last != true) {
                    pageable.page++;
                    loadProjects();
                }
            }

            function previousPage() {
                if (vm.projects.first != true) {
                    pageable.page--;
                    loadProjects();
                }
            }

            function deleteProject(project) {
                var options = {
                    title: 'Delete Project',
                    message: 'Are you sure you want to delete this project?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ProjectService.deleteProjectItem(project.id).then(
                            function (data) {
                                var index = vm.projects.content.indexOf(project);
                                vm.projects.content.splice(index, 1);
                                $rootScope.showErrorMessage("Deleted Successfully!");
                                loadProjects();
                            }
                        )
                    }
                });
            }

            function loadProjects() {
                ProjectService.getProjects().then(
                    function (data) {
                        vm.projects = data;
                    });
                vm.loading = false;
            }

            function showProjectDetails(project) {
                $state.go('app.project.details', {projectId: project.id});
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadProjects();
                }
            })();
        }
    }
)
;
