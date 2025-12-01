/**
 * Created by swapna on 1/30/18.
 */
define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService'
    ],
    function (module) {
        module.controller('ProjectSelectionController', ProjectSelectionController);

        function ProjectSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore, $translate,
                                            $uibModal, ProjectService) {

            var vm = this;

            vm.loading = true;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.selectRadio = selectRadio;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
            $scope.freeTextQuery = null;
            var objectId = $scope.data.existObjectId;
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };
            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.selectedObj = null;
            vm.projects = angular.copy(pagedResults);
            var parsed = angular.element("<div></div>");
            var selectOneProject = parsed.html($translate.instant("PLEASE_SELECT_ATLEAST_ONE_PROJECT")).html();

            function nextPage() {
                if (!vm.projects.last) {
                    vm.pageable.page++;
                    loadProjects();
                }
            }

            function previousPage() {
                if (!vm.projects.first) {
                    vm.pageable.page--;
                    loadProjects();
                }
            }

            function freeTextSearch() {
                if (vm.searchTerm != null && vm.searchTerm != undefined && vm.searchTerm.trim() != "") {
                    $scope.freeTextQuery = vm.searchTerm;
                    ProjectService.freeTextSearch(vm.searchTerm, vm.pageable).then(
                        function (data) {
                            vm.projects = data;
                            var existObjectId = false;
                            angular.forEach(vm.projects.content, function (project) {
                                if (objectId != null && objectId != "" && objectId != undefined && objectId == project.id) {
                                    vm.projects.content.splice(vm.projects.content.indexOf(project), 1);
                                    existObjectId = true;
                                }
                                project.checked = false;
                            });
                            if (existObjectId) {
                                vm.projects.totalElements = vm.projects.totalElements - 1;
                                vm.projects.numberOfElements = vm.projects.numberOfElements - 1;
                            }
                            vm.clear = true;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    resetPage();
                    loadProjects();
                }
            }

            function clearFilter() {
                loadProjects();
                vm.clear = false;
            }

            function resetPage() {
                vm.projects = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.searchTerm = null;
                $scope.freeTextQuery = null;
                loadProjects();
            }

            function loadProjects() {
                vm.clear = false;
                vm.loading = true;
                ProjectService.getProjects(vm.pageable).then(
                    function (data) {
                        vm.projects = data;
                        var existObjectId = false;
                        angular.forEach(vm.projects.content, function (project) {
                            if (objectId != null && objectId != "" && objectId != undefined && objectId == project.id) {
                                vm.projects.content.splice(vm.projects.content.indexOf(project), 1);
                                existObjectId = true;
                            }
                            project.checked = false;
                        });
                        if (existObjectId) {
                            vm.projects.totalElements = vm.projects.totalElements - 1;
                            vm.projects.numberOfElements = vm.projects.numberOfElements - 1;
                        }
                        vm.loading = false;
                        $scope.$evalAsync();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function selectRadioChange(project, $event) {
                radioChange(project, $event);
                selectRadio();
            }

            function radioChange(project, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === project) {
                    project.checked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = project;
                }
            }

            function selectRadio() {
                if (vm.selectedObj != null) {
                    $rootScope.hideSidePanel('left');
                    $scope.callback(vm.selectedObj);
                }

                if (vm.selectedObj == null) {
                    $rootScope.showWarningMessage(selectOneProject);
                }
            }

            (function () {
                loadProjects();
                $rootScope.$on('app.select.project', selectRadio);
            })();
        }
    }
)
;