define(
    [
        'app/desktop/modules/dashboard/dashboard.module',
        'app/shared/services/core/analyticsService'
    ],
    function (module) {
        module.controller('ChangeDashboardController', ChangeDashboardController);

        function ChangeDashboardController($scope, $rootScope, $timeout, $compile, $sce, $interval, $state, $cookies, $window, $translate,
                                           $application, AnalyticsService) {

            var vm = this;
            $rootScope.viewInfo.title = "Changes Dashboard";
            $rootScope.viewInfo.showDetails = false;

            var parsed = angular.element("<div></div>");
            $scope.ecrsByStatus = parsed.html($translate.instant("ECR_BY_STATUS")).html();
            $scope.dcrsByStatus = parsed.html($translate.instant("DCR_BY_STATUS")).html();
            $scope.ecosByStatus = parsed.html($translate.instant("ECO_BY_STATUS")).html();
            $scope.dcosByStatus = parsed.html($translate.instant("DCO_BY_STATUS")).html();
            $scope.mcosByStatus = parsed.html($translate.instant("MCO_BY_STATUS")).html();
            $scope.deviationsByStatus = parsed.html($translate.instant("DEVIATION_BY_STATUS")).html();
            $scope.waiversByStatus = parsed.html($translate.instant("WAIVER_BY_STATUS")).html();
            var charts = [];

            function addChangeRequestChart() {
                var options = {
                    series: [{
                        name: "",
                        data: []
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
                        categories: ['Not Started', 'In progress', 'Approved', 'Cancelled', 'On Hold'],
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

                angular.forEach(vm.changeRequestReports, function (report) {
                    options.series[0].name = report.name;
                    options.series[0].data = report.data;
                    var chart = new ApexCharts(document.querySelector("#" + report.id), options);
                    chart.render();
                    charts.push(chart);
                })

            }

            function addChangeOrderChart() {
                var options = {
                    series: [{
                        name: "Count",
                        data: []
                    }],
                    xaxis: {
                        categories: ['Not Started', 'In progress', 'Approved', 'Cancelled', 'On Hold']
                    },
                    chart: {
                        type: 'bar',
                        height: 250,
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
                        text: ''
                    }

                };

                angular.forEach(vm.changeOrderReports, function (report) {
                    options.title.text = report.name;
                    options.series[0].data = report.data;
                    var chart = new ApexCharts(document.querySelector("#" + report.id), options);
                    chart.render();
                    charts.push(chart);
                })
            }

            function addVarianceCharts() {
                var options = {
                    series: [],
                    chart: {
                        type: 'donut',
                        height: 290,
                        toolbar: {
                            show: false
                        }
                    },
                    opacity: 1.0,
                    labels: ['Not Started', 'In progress', 'Approved', 'Cancelled', 'On Hold'],
                    title: {
                        text: ''
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
                    },
                    legend: {
                        position: 'bottom'
                    }
                };
                angular.forEach(vm.varianceReports, function (report) {
                    options.title.text = report.name;
                    options.series = report.data;
                    var chart = new ApexCharts(document.querySelector("#" + report.id), options);
                    chart.render();
                    charts.push(chart);
                })
            }

            function destroyCharts() {
                angular.forEach(charts, function (chart) {
                    chart.destroy();
                })
            }

            vm.initCharts = initCharts;
            var chartsInited = false;

            function initCharts() {
                if (!chartsInited) {
                    $timeout(function () {
                        addChangeRequestChart();
                        addChangeOrderChart();
                        addVarianceCharts();
                        chartsInited = true;
                        $scope.$on("$destroy", destroyCharts);
                    }, 100);
                }
            }

            function loadChangeDashboardCounts() {
                AnalyticsService.getChangeDashboardCounts().then(
                    function (data) {
                        vm.changeTypes = data.changeTypes;
                        vm.ecrCounts = data.ecrCounts;
                        vm.ecoCounts = data.ecoCounts;
                        vm.dcrCounts = data.dcrCounts;
                        vm.dcoCounts = data.dcoCounts;
                        vm.mcoCounts = data.mcoCounts;
                        vm.deviationCounts = data.deviationCounts;
                        vm.waiverCounts = data.waiverCounts;
                        vm.changeRequestReports = [
                            {name: $scope.ecrsByStatus, id: "ecrReports", data: vm.ecrCounts},
                            {name: $scope.dcrsByStatus, id: "dcrReports", data: vm.dcrCounts}
                        ];
                        vm.changeOrderReports = [
                            {name: $scope.ecosByStatus, id: "ecoReports", data: vm.ecoCounts},
                            {name: $scope.dcosByStatus, id: "dcoReports", data: vm.dcoCounts},
                            {name: $scope.mcosByStatus, id: "mcoReports", data: vm.mcoCounts}
                        ];
                        vm.varianceReports = [
                            {name: $scope.deviationsByStatus, id: "deviationReports", data: vm.deviationCounts},
                            {name: $scope.waiversByStatus, id: "waiverReports", data: vm.waiverCounts}
                        ];
                        initCharts();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $scope.changeTypeCardCounts = null;
            function loadChangeCardCounts() {
                AnalyticsService.getChangeCardCounts().then(
                    function (data) {
                        $scope.changeTypeCardCounts = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadChangeCardCounts();
                loadChangeDashboardCounts();
            })();

        }
    }
);