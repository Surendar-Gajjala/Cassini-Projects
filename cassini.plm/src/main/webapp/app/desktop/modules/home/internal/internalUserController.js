define(
    [
        'app/desktop/modules/home/home.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/desktop/modules/home/widgets/recentlyVisited/recentlyVisitedWidgetController',
        'app/desktop/modules/home/widgets/savedSearches/savedSearchesWidgetController',
        'app/desktop/modules/home/widgets/conversations/conversationsWidget',
        'app/desktop/modules/home/widgets/myTasks/myTasksWidgetController',
        'app/shared/services/core/analyticsService',
        'app/shared/services/core/userTasksService',
        'app/desktop/modules/home/internal/newObjectController'
    ],
    function (module) {
        module.controller('InternalUserController', InternalUserController);

        function InternalUserController($scope, $rootScope, $timeout, $compile, $sce, $interval, $state, $cookies, $window, $translate,
                                        $application, AnalyticsService, UserTasksService) {

            $rootScope.viewInfo.icon = "fa fa-home";
            $rootScope.viewInfo.title = $translate.instant("HOME_TITLE");
            $rootScope.viewInfo.showDetails = false;

            var vm = this;

            var charts = [];
            vm.taskCountsByStatus = [];

            function addItemsChart() {
                var options = {
                    series: [{
                        name: "Count",
                        data: vm.itemsByClass
                    }],
                    xaxis: {
                        categories: [
                            'Products', 'Assemblies', 'Parts', 'Documents', 'Others'
                        ]
                    },
                    chart: {
                        type: 'bar',
                        height: '100%',
                        toolbar: {
                            show: false
                        }
                    },
                    opacity: 1.0,
                    plotOptions: {
                        bar: {
                            horizontal: true,
                            barHeight: '30%',
                            distributed: true,
                            dataLabels: {
                                position: 'top'
                            }
                        }
                    },
                    dataLabels: {
                        enabled: true,
                        style: {
                            colors: ['#fff'],
                            fontSize: '10px',
                            fontWeight: 'normal'
                        },
                        offsetX: 0,
                        offsetY: 0,
                    },
                    tooltip: {
                        enabled: false
                    },
                    legend: {
                        show: false
                    },
                    colors: [
                        '#0D47A1', '#1565C0', '#1976D2', '#1E88E5', '#2196F3',
                        '#42A5F5', '#64B5F6', '#90CAF9', '#BBDEFB', '#E3F2FD'
                    ],
                    title: {
                        text: 'Items by Class'
                    }

                };

                var chart = new ApexCharts(document.querySelector("#itemCounts"), options);
                chart.render();
                charts.push(chart);
            }

            function addChangesChart() {
                var options = {
                    series: [{
                        name: "Count",
                        data: vm.changeTypesCount
                    }],
                    xaxis: {
                        categories: [
                            'ECRs', 'ECOs', 'DCRs', 'DCOs', 'MCOs',
                            'Deviations', 'Waivers'
                        ]
                    },
                    chart: {
                        type: 'bar',
                        height: '100%',
                        toolbar: {
                            show: false
                        }
                    },
                    opacity: 1.0,
                    plotOptions: {
                        bar: {
                            horizontal: true,
                            barHeight: '40%',
                            distributed: true,
                            dataLabels: {
                                position: 'top'
                            }
                        }
                    },
                    dataLabels: {
                        enabled: true,
                        style: {
                            colors: ['#fff'],
                            fontSize: '10px',
                            fontWeight: 'normal'
                        },
                        offsetX: 0,
                        offsetY: 0,
                    },
                    tooltip: {
                        enabled: false
                    },
                    legend: {
                        show: false
                    },
                    colors: [
                        '#1B5E20', '#2E7D32', '#388E3C', '#43A047', '#4CAF50',
                        '#66BB6A', '#81C784', '#A5D6A7', '#C8E6C9', '#E8F5E9'
                    ],
                    title: {
                        text: 'Changes by Type'
                    }
                };

                var chart = new ApexCharts(document.querySelector("#changeCounts"), options);
                chart.render();
                charts.push(chart);
            }

            function addQualityChart() {
                var options = {
                    series: vm.qualityTypeCounts,
                    chart: {
                        type: 'donut',
                        height: '100%',
                        toolbar: {
                            show: false
                        }
                    },
                    opacity: 1.0,
                    labels: ['PRs', 'NCRs', 'QCRs'],
                    title: {
                        text: 'Quality by Type'
                    },
                    dataLabels: {
                        enabled: false
                    },
                    tooltip: {
                        enabled: false
                    },
                    plotOptions: {
                        pie: {
                            donut: {
                                labels: {
                                    show: true,
                                    name: {
                                        show: true
                                    },
                                    value: {
                                        offsetY: -1,
                                        show: true
                                    },
                                    total: {
                                        show: true,
                                        label: 'Total',
                                        color: '#373d3f'
                                    }
                                }
                            }
                        }
                    }
                };

                var chart = new ApexCharts(document.querySelector("#qualityTypeCounts"), options);
                chart.render();
                charts.push(chart);
            }

            function addTasksSummaryChart() {
                var options = {
                    series: vm.taskCountsByStatus,
                    chart: {
                        height: 230,
                        type: 'radialBar',
                        toolbar: {
                            show: false
                        }
                    },
                    plotOptions: {
                        radialBar: {
                            offsetY: 0,
                            startAngle: 0,
                            endAngle: 270,
                            hollow: {
                                margin: 5,
                                size: '30%',
                                background: 'transparent',
                                image: undefined
                            },
                            dataLabels: {
                                name: {
                                    show: false
                                },
                                value: {
                                    show: false
                                }
                            }
                        }
                    },
                    colors: ['#0084ff', '#39539E', '#0077B5'],
                    labels: ['Pending', 'Finished', 'Cancelled'],
                    legend: {
                        show: true,
                        floating: true,
                        fontSize: '12px',
                        position: 'left',
                        offsetX: 100,
                        offsetY: 0,
                        labels: {
                            useSeriesColors: true
                        },
                        markers: {
                            size: 0
                        },
                        formatter: function (seriesName, opts) {
                            return seriesName + ":  " + opts.w.globals.series[opts.seriesIndex]
                        },
                        itemMargin: {
                            vertical: 3
                        }
                    },
                    responsive: [{
                        breakpoint: 480,
                        options: {
                            legend: {
                                show: false
                            }
                        }
                    }]
                };

                var chart = new ApexCharts(document.querySelector("#tasksSummary"), options);
                chart.render();
                charts.push(chart);
            }

            function addProblemReportsChart() {
                var options = {
                    series: [{
                        name: 'Problem Reports',
                        data: vm.prSeverityCounts
                    }],
                    chart: {
                        height: 215,
                        type: 'bar',
                        toolbar: {
                            show: false
                        }
                    },
                    plotOptions: {
                        bar: {
                            barWidth: '30%',
                            distributed: true,
                            dataLabels: {
                                position: 'top' // top, center, bottom
                            }
                        }
                    },
                    dataLabels: {
                        enabled: true,
                        offsetY: -20,
                        style: {
                            fontSize: '12px',
                            colors: ["#304758"]
                        }
                    },
                    tooltip: {
                        enabled: false
                    },
                    xaxis: {
                        categories: vm.prSeverities,
                        position: 'top',
                        axisBorder: {
                            show: false
                        },
                        axisTicks: {
                            show: false
                        },
                        crosshairs: {
                            fill: {
                                type: 'gradient',
                                gradient: {
                                    colorFrom: '#D8E3F0',
                                    colorTo: '#BED1E6',
                                    stops: [0, 100],
                                    opacityFrom: 0.4,
                                    opacityTo: 0.5
                                }
                            }
                        },
                        tooltip: {
                            enabled: false
                        }
                    },
                    yaxis: {
                        axisBorder: {
                            show: false
                        },
                        axisTicks: {
                            show: false
                        },
                        labels: {
                            show: false
                        }

                    }
                };

                var chart = new ApexCharts(document.querySelector("#problemReportCounts"), options);
                chart.render();
                charts.push(chart);
            }

            function destroyCharts() {
                angular.forEach(charts, function (chart) {
                    chart.destroy();
                })
            }

            vm.initCharts = initCharts;
            var chartsInited = false;

            function initCharts() {
                if (!chartsInited && $rootScope.mainLoaded) {
                    $timeout(function () {
                        chartsInited = true;
                        $scope.$on("$destroy", destroyCharts);
                    }, 1000);
                }
            }

            function loadItemDashboardCounts() {
                AnalyticsService.getItemDashboardCounts().then(
                    function (data) {
                        vm.itemsByClass = data.itemsByClass;
                        $timeout(function () {
                            addItemsChart();
                            initCharts();
                        }, 1000);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadChangeCardCounts() {
                AnalyticsService.getChangeTypeCounts().then(
                    function (data) {
                        vm.changeTypesCount = data.changeTypes;
                        $timeout(function () {
                            addChangesChart();
                        }, 1000);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadQualityTypeCounts() {
                AnalyticsService.getQualityTypeCounts().then(
                    function (data) {
                        vm.qualityTypeCounts = data.qualityTypes;
                        $timeout(function () {
                            addQualityChart();
                        }, 1000);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadProblemReportsBySeverity() {
                AnalyticsService.getPrsBySeverities().then(
                    function (data) {
                        vm.prSeverities = data.prSeverities;
                        vm.prSeverityCounts = data.prSeverityCounts;
                        $timeout(function () {
                            addProblemReportsChart();
                        }, 1000);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadTaskCountsByStatus() {
                UserTasksService.getTaskCountsByStatus().then(
                    function (data) {
                        var mapCounts = new Hashtable();
                        mapCounts.put("Pending", 0);
                        mapCounts.put("Finished", 0);
                        mapCounts.put("Cancelled", 0);

                        angular.forEach(data, function (d) {
                            if (d.status === 'PENDING') {
                                mapCounts.put("Pending", d.count);
                            }
                            else if (d.status === 'FINISHED') {
                                mapCounts.put("Finished", d.count);
                            }
                            else if (d.status === 'CANCELLED') {
                                mapCounts.put("Cancelled", d.count);
                            }
                        });

                        vm.taskCountsByStatus = [mapCounts.get("Pending"),
                            mapCounts.get("Finished"), mapCounts.get("Cancelled")];

                        $timeout(function () {
                            addTasksSummaryChart();
                        }, 2000);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadItemDashboardCounts();
                loadChangeCardCounts();
                loadQualityTypeCounts();
                loadProblemReportsBySeverity();
                loadTaskCountsByStatus();
            })();

        }
    }
);