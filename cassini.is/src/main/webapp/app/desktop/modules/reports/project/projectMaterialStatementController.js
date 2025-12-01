define(['app/desktop/modules/reports/reports.module',
        'app/shared/services/pm/project/projectService'
    ],
    function (module) {
        module.controller('ProjectMaterialStatementController', ProjectMaterialStatementController);

        function ProjectMaterialStatementController($scope, $rootScope, $timeout, $state, $cookies, ProjectService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-file-image-o";
            $rootScope.viewInfo.title = "Project Material Statement Report";

            vm.exportReport = exportReport;
            vm.projects = [];
            vm.openProjectDetails = openProjectDetails;
            vm.loadProjects = loadProjects;
            $scope.selectedRow = 0;
            vm.searchReport = searchReport;
            vm.exportReport = exportReport;
            vm.toggleRow = toggleRow;
            vm.showDetails = false;
            vm.selectedProject = null;
            vm.loading = false;
            vm.filters = {
                searchQuery: ""
            };
            vm.reportRows = [];

            function searchReport() {
                vm.reportRows = [];
                vm.loading = true;
                vm.maprows = [];
                ProjectService.getReportByDates(vm.selectedProject.id).then(
                    function (data) {
                        vm.reportRows = data;
                        vm.loading = false;
                    }
                )
            }

            function exportReport() {
                $rootScope.showExportMessage("Project Material Report, Exporting in progress");
                ProjectService.exportProjectMaterialReport(vm.selectedProject.id).then(
                    function (data) {
                        var url = "{0}//{1}//api/common/exports/file/".format(window.location.protocol, window.location.host);
                        url += data + "/download";
                        var c = document.cookie.split("; ");
                        for (i in c)
                            document.cookie = /^[^=]+/.exec(c[i])[0] + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
                        window.open(url, '_self');
                        $rootScope.showSuccessMessage("Report exported successfully");
                    }
                )
            }

            function openProjectDetails(project) {
                $('#search-results-container').hide();
                vm.selectedProject = project;
                vm.filters.searchQuery = project.name;
                if (project != null) {
                    searchReport();
                }
            }

            function loadProjects() {
                $scope.selectedRow = 0;
                $('#search-results-container').show();
                ProjectService.searchProjects(vm.filters).then(
                    function (data) {
                        vm.projects = data;
                    }
                )
            }

            function toggleRow(row) {
                if (row.showDetails == undefined) {
                    row.showDetails = false;
                }
                row.showDetails = !row.showDetails;
                var index = vm.reportRows.indexOf(row);
                for (var i = 0; i <= row.noOfRows; i++) {
                    index++;
                    var newRow = vm.reportRows[index];
                    newRow.hideRow = !newRow.hideRow;
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $(document).click(function (e) {
                        if ($(e.target).is('.search-element1 input')) {
                            loadProjects();
                        } else
                            $('#search-results-container').hide();
                    });
                    $(document).on('keydown', function (evt) {
                        if (evt.keyCode == 27) {
                            $('#search-results-container').hide();
                        }
                    });
                    $(document).on('keydown', function (e) {
                        if ($scope.selectedRow == null) {
                            $scope.selectedRow = 0;
                        }
                        if ($('#search-results-container').is(':visible')) {
                            if (e.keyCode == 38) {
                                if ($scope.selectedRow == 0) {
                                    return;
                                }
                                $scope.selectedRow--;
                                $scope.$apply();
                                e.preventDefault();
                            }
                            else if (e.keyCode == 40) {
                                if ($scope.selectedRow == vm.projects.length - 1) {
                                    return;
                                }
                                $scope.selectedRow++;
                                $scope.$apply();
                                e.preventDefault();
                            }
                            else if (e.keyCode == 13) {
                                openProjectDetails(vm.projects[$scope.selectedRow]);
                                $scope.$apply();
                                e.preventDefault();
                            }
                        }
                    });

                }
            })();
        }
    }
);