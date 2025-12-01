define(['app/desktop/modules/col/col.module',
        'app/shared/services/tm/taskService',
        'app/desktop/modules/reporting/reports/unfinishedTasksController'
    ],
    function (module) {
        module.controller('ReportsController', ReportsController);

        function ReportsController($scope, $rootScope, $timeout, $interval, $state, $cookies,
                                   TaskService) {
            $rootScope.viewInfo.icon = "fa fa-bar-chart-o";
            $rootScope.viewInfo.title = "Reports";

            $scope.reports = [];
            $scope.selectedModule = null;
            $scope.selectedReport = null;
            var reportGroups = [];
            var reportGroupMap = new Hashtable();
            $rootScope.reportsMap = new Hashtable();

            function loadReports() {
                TaskService.getReports().then(
                    function (data) {
                        $scope.reports = data;
                        if (data.length > 0) {
                            addBuiltinReports();

                            angular.forEach($scope.reports, function (module) {
                                reportGroups.push(module);
                                reportGroupMap.put(module.id, module);
                                angular.forEach(module.reports, function (report) {
                                    report.selected = false;
                                    $rootScope.reportsMap.put(report.id, report);

                                })
                            });
                            $scope.selectedModule = data[0];
                            $scope.selectedReport = $scope.selectedModule.reports[0];
                            $scope.selectedReport.selected = true;
                        }
                    }
                );
            }

            $scope.onSelectModule = function (item, model) {
                if ($scope.selectedReport != null) {
                    $scope.selectedReport.selected = false;
                }

                if ($scope.selectedModule != model) {
                    $scope.selectedModule = model;
                    $scope.selectedReport = $scope.selectedModule.reports[0];
                    $scope.selectedReport.selected = true;
                }
            };

            $scope.selectReport = function (report) {
                if ($scope.selectedReport != null) {
                    $scope.selectedReport.selected = false;
                }

                report.selected = true;
                $scope.selectedReport = report;
            };

            function addBuiltinReports() {
                var reportModule = null;
                angular.forEach($scope.reports, function (rm) {
                    if (rm.id == 'tasks') {
                        reportModule = rm;
                    }
                });

                if (reportModule != null) {
                    reportModule.reports.unshift({
                        id: 'builtin.reports.tasks',
                        name: "unfinishedTasks",
                        description: "Tasks that are not finished in Planned Time",
                        viewType: 'custom',
                        template: 'app/desktop/modules/reporting/reports/unfinishedTasksView.jsp',
                        controller: 'UnfinishedTasksController'
                    });
                }
            }

            (function () {
                loadReports();
            })();
        }
    }
);