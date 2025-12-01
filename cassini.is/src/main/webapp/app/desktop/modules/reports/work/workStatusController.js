define(['app/desktop/modules/reports/reports.module',
        'app/shared/services/tm/taskService',
        'app/shared/services/pm/project/projectService'
    ],
    function (module) {
        module.controller('WorkStatusController', WorkStatusController);

        function WorkStatusController($scope, $rootScope, $timeout, $state, $cookies, TaskService, ProjectService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-area-chart";
            $rootScope.viewInfo.title = "Work Status Report";

            vm.exportReport = exportReport;
            vm.searchReport = searchReport;
            vm.tasks = [];
            vm.openItemDetails = openItemDetails;
            vm.loadTasks = loadTasks;
            $scope.selectedRow = 0;
            vm.projects = [];
            vm.searchMode = false;
            vm.openProjectDetails = openProjectDetails;
            vm.loadProjects = loadProjects;
            $scope.selectedRow = 0;
            vm.startDate = null;
            vm.endDate = moment(new Date()).format("DD/MM/YYYY");
            vm.loading = false;
            vm.addRemark = addRemark;

            vm.projFilters = {
                searchQuery: ""
            };
            vm.selectedProjectId = null;
            vm.filters = {
                searchQuery: ""
            };
            vm.selectedTask = null;
            vm.reportRows = [];

            var remarkMap = {};

            function addRemark(row) {
                if (row.remarks != null && row.remarks != undefined && $.trim(row.remarks) != "") {
                    remarkMap["" + row.serialNo] = row.remarks;
                } else {
                    remarkMap["" + row.serialNo] = "";
                }
            }

            function openItemDetails(task) {
                $('#search-results-container').hide();
                vm.selectedTask = task;
                vm.filters.searchQuery = task.name;
            }

            function loadTasks() {
                $scope.selectedRow = 0;
                $('#search-results-container1').hide();
                $('#search-results-container').show();
                TaskService.searchTasks(vm.selectedProjectId, vm.filters).then(
                    function (data) {
                        vm.tasks = data;
                    }
                )
            }

            function openProjectDetails(project) {
                $('#search-results-container1').hide();
                vm.selectedProjectId = project.id;
                vm.projFilters.searchQuery = project.name;
            }

            function loadProjects() {
                vm.searchMode = false;
                $scope.selectedRow = 0;
                $('#search-results-container1').show();
                $('#search-results-container').hide();
                ProjectService.searchProjects(vm.projFilters).then(
                    function (data) {
                        vm.projects = data;
                    }
                )
            }

            function exportReport() {
                $rootScope.showExportMessage("Task Report, Exporting in progress");
                TaskService.exportTasksReport(vm.selectedProjectId, vm.startDate, vm.endDate).then(
                    function (data) {
                        var url = "{0}//{1}//api/common/exports/file/".format(window.location.protocol, window.location.host);
                        url += data + "/download";
                        var c = document.cookie.split("; ");
                        for (i in c)
                            document.cookie = /^[^=]+/.exec(c[i])[0] + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
                        window.open(url, '_self');
                        $rootScope.showSuccessMessage("Report exported successfully");
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function searchReport() {
                vm.searchMode = true;
                remarkMap = {};
                vm.reportRows = [];
                if (vm.endDate != "") {
                    vm.loading = true;
                    TaskService.getReportByDates(vm.selectedProjectId, vm.startDate, vm.endDate).then(
                        function (data) {
                            vm.reportRows = data;
                            vm.loading = false;
                        }
                    )
                } else {
                    $rootScope.showErrorMessage("Please select ToDate");
                }

            }

            (function () {
                if ($application.homeLoaded == true) {
                    $(document).click(function (e) {
                        /* if ($(e.target).is('.search-element input')) {
                         loadTasks();
                         } else */
                        if ($(e.target).is('.search-element1 input')) {
                            loadProjects();
                        } else
                            $('#search-results-container').hide();
                    });
                    $(document).on('keydown', function (evt) {
                        if (evt.keyCode == 27) {
                            /*$('#search-results-container').hide();*/
                            $('#search-results-container1').hide();
                        }
                    });
                    $(document).on('keydown', function (e) {
                        if ($scope.selectedRow == null) {
                            $scope.selectedRow = 0;
                        }
                        if (/*$('#search-results-container').is(':visible') || */$('#search-results-container1').is(':visible')) {
                            if (e.keyCode == 38) {
                                if ($scope.selectedRow == 0) {
                                    return;
                                }
                                $scope.selectedRow--;
                                $scope.$apply();
                                e.preventDefault();
                            }
                            else if (e.keyCode == 40) {
                                if ($scope.selectedRow == vm.tasks.length - 1) {
                                    return;
                                }
                                $scope.selectedRow++;
                                $scope.$apply();
                                e.preventDefault();
                            }
                            else if (e.keyCode == 13) {
                                /*if ($('#search-results-container').is(':visible'))
                                 openItemDetails(vm.tasks[$scope.selectedRow]);*/
                                if ($('#search-results-container1').is(':visible'))
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