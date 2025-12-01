define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('reportFactory',
            [
                '$rootScope', '$http', '$q', 'httpFactory',

                function ($rootScope, $http, $q, httpFactory) {
                    var reportGroups = [];
                    var reportGroupMap = new Hashtable();
                    var reportsMap = new Hashtable();
                    var reportsLoaded = false;

                    function initializeReports(groups) {
                        reportGroups = groups;
                        angular.forEach(groups, function(group) {
                            reportGroups.push(group);
                            reportGroupMap.put(group.id, group);

                            var reports = group.reports;
                            angular.forEach(reports, function(report) {
                               reportsMap.put(report.id, report);
                            });
                        });
                        console.log("Reports loaded and initialized");

                        $rootScope.$broadcast("reportsLoaded");
                        reportsLoaded = true;
                    }

                    return {
                        areReportsLoaded: function() {
                            return reportsLoaded;
                        },
                        loadReports: function() {
                            var dfd = $q.defer(),
                                url = "api/reporting";
                            httpFactory.get(url).then(
                                function(data) {
                                    initializeReports(data);
                                }
                            );
                        },
                        getReportById: function(reportId) {
                            return reportsMap.get(reportId);
                        },
                        getReports: function() {
                            var dfd = $q.defer(),
                                url = "api/reporting";
                            return httpFactory.get(url);
                        },
                        executeReport: function(report, dateRange) {
                            var dfd = $q.defer(),
                                url = "api/reporting/execute";
                            if(dateRange != null && dateRange != undefined) {
                                url += "?dateRange=" + dateRange.startDate + "-" + dateRange.endDate;
                            }
                            return httpFactory.post(url, report);
                        },
                        executeScript: function(script) {
                            var dfd = $q.defer(),
                                url = "api/reporting/editor";
                            return httpFactory.post(url, script);
                        }
                    }
                }
            ]
        );
    }
);