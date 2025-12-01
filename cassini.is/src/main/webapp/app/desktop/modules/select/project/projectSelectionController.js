/**
 * Created by swapna on 24/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/pm/project/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('ProjectSelectionController', ProjectSelectionController);

        function ProjectSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore,
                                            ProjectService, CommonService) {

            var vm = this;

            vm.loading = true;
            vm.selectedObj = null;
            vm.selectRadio = selectRadio;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;
            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                first: true,
                numberOfElements: 0
            };

            vm.projects = angular.copy(pagedResults);

            function loadProjects() {
                vm.clear = false;
                vm.loading = true;
                ProjectService.getPageableProjects(pageable).then(
                    function (data) {
                        vm.projects = data;
                        angular.forEach(vm.projects.content, function (project) {
                            project.checked = false;
                        });
                        CommonService.getPersonReferences(vm.projects.content, 'projectOwner');
                        vm.loading = false;
                    }
                );
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.projects = [];
                    ProjectService.freeSearch(pageable, freeText).then(
                        function (data) {
                            vm.projects = data;
                            vm.clear = true;
                            angular.forEach(vm.projects.content, function (project) {
                                project.checked = false;
                            });
                            CommonService.getPersonReferences(vm.projects.content, 'projectOwner');
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
                pageable.page = 0;
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
                    $rootScope.showWarningMessage("Please select project");
                }
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

            (function () {
                $rootScope.$on('app.project.selected', selectRadio);
                loadProjects();
            })();
        }
    }
)
;

