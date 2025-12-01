/**
 * Created by swapna on 12/19/17.
 */
define(['app/desktop/modules/col/col.module',
        'app/shared/services/tm/taskService'
    ],
    function (module) {
        module.controller('UnfinishedTasksController', UnfinishedTasksController);

        function UnfinishedTasksController($scope, $rootScope, $timeout, $interval, $state, $cookies, $filter,
                                           TaskService) {

            var reportInProgress = false;
            $scope.reportData = [];

            var loaded = false;

            $scope.spinner = {
                active: true
            };

            $scope.filters = {
                orderedDate: {
                    startDate: null,
                    endDate: null
                }
            };

            $scope.$watch('filters.orderedDate', function (date) {
                loadReport();
            });

            function loadReport() {

                if (!reportInProgress) {

                    var report = $rootScope.reportsMap.get('reports.tasks.unfinishedTasks');
                    if (report != null) {
                        var dr = null;

                        reportInProgress = true;
                        $scope.reportData = [];

                        TaskService.executeReport(report, dr).then(
                            function (data) {
                                if (data != "" && data.length > 0) {
                                    $scope.reportData = data;
                                }
                                $scope.spinner.active = false;
                                reportInProgress = false;
                            },
                            function (error) {
                                $scope.spinner.active = false;
                                reportInProgress = false;
                            }
                        )
                    }
                    else {
                        $scope.spinner.active = false;
                    }
                }
            }

            (function () {
                $timeout(function () {
                    loadReport();
                }, 5000);
            })();

        }
    }
);