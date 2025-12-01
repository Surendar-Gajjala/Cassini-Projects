define([
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/desktop/modules/mfr/supplier/directive/supplierDirective'
    ],
    function (module) {
        module.controller('ProjectsSelectionController', ProjectsSelectionController);

        function ProjectsSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate, ProjectService) {

            var vm = this;
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.error = "";

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

            vm.projects = angular.copy(pagedResults);

            vm.onSelectType = onSelectType;
            vm.selectCheck = selectCheck;
            vm.selectAll = selectAll;
            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.add = add;

            var parsed = angular.element("<div></div>");
            var selectOnePr = parsed.html($translate.instant("SELECT_ONE_SUPPLIER")).html();

            vm.selectedProjects = [];
            vm.filters = {
                name: null,
                description: null,
                searchQuery: null,
                program: ''
            };

            $scope.check = false;

            function selectAll(check) {
                vm.selectedProjects = [];
                if (check) {
                    $scope.check = false;
                    angular.forEach(vm.projects.content, function (item) {
                        item.selected = false;
                    })
                } else {
                    $scope.check = true;
                    vm.error = "";
                    angular.forEach(vm.projects.content, function (item) {
                        item.selected = true;
                        vm.selectedProjects.push(item);
                    })
                }
            }

            function nextPage() {
                if (vm.projects.last != true) {
                    vm.pageable.page++;
                    $scope.check = false;
                    vm.selectedProjects = [];
                    loadProjects();
                }
            }

            function previousPage() {
                if (vm.projects.first != true) {
                    vm.pageable.page--;
                    $scope.check = false;
                    vm.selectedProjects = [];
                    loadProjects();
                }
            }

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.selectedType = itemType;
                    vm.filters.type = itemType.id;
                    vm.filters.typeName = itemType.name;
                    vm.pageable.page = 0;
                    loadProjects();
                    vm.clear = true;
                }
            }


            function clearFilter() {
                vm.filters.searchQuery = null;
                vm.filters.type = '';
                vm.filters.typeName = null;
                vm.filters.city = null;
                vm.selectedType = null;
                vm.pageable.page = 0;
                $scope.check = false;
                vm.selectedProjects = [];
                vm.selectAllCheck = false;
                loadProjects();
                vm.clear = false;
            }

            function add() {
                if (vm.selectedProjects.length != 0) {
                    $rootScope.hideSidePanel();
                    $scope.callback(vm.selectedProjects);
                }

                if (vm.selectedProjects.length == 0) {
                    $rootScope.showWarningMessage(selectOnePr);
                }

            }

            vm.selectAllCheck = false;

            function selectCheck(item) {
                var flag = true;
                vm.error = "";
                angular.forEach(vm.selectedProjects, function (selectedItem) {
                    if (selectedItem.id == item.id) {
                        flag = false;
                        var index = vm.selectedProjects.indexOf(item);
                        vm.selectedProjects.splice(index, 1);
                    }
                });
                if (flag) {
                    vm.selectedProjects.push(item);
                }

                if (vm.selectedProjects.length != vm.projects.content.length) {
                    vm.selectAllCheck = false;
                } else {
                    vm.selectAllCheck = true;
                }
            }

            vm.searchProjects = searchProjects;
            function searchProjects() {
                vm.pageable.page = 0;
                loadProjects();
                vm.clear = true;
                vm.selectAllCheck = false;
                if (vm.filters.searchQuery == "" || vm.filters.searchQuery == null) {
                    vm.clear = false;
                    vm.selectAllCheck = false;
                }
            }

            function loadProjects() {
                vm.filters.program = $scope.data.selectedProgramId;
                ProjectService.getFilteredProjects(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.projects = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadProjects();
                $rootScope.$on("app.program.projects.add", add);
            })();
        }
    }
)
;