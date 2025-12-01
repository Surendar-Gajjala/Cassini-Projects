define(
    [
        'app/desktop/modules/dashboard/dashboard.module',
        'app/shared/services/core/analyticsService'
    ],
    function (module) {
        module.controller('OEMDashboardController', OEMDashboardController);

        function OEMDashboardController($scope, $rootScope, $timeout, $compile, $sce, $interval, $state, $cookies, $window, $translate,
                                        $application, AnalyticsService) {

            var vm = this;
            $rootScope.viewInfo.title = "OEM Dashboard";
            $rootScope.viewInfo.showDetails = false;

            var charts = [];
            vm.mfrPartByStatusCounts = [];
            vm.mfrByStatusCounts = [];


            function addMfrPartStatusChart() {
                var options = {
                    series: [{
                        name: 'Manufacturer Parts By Status',
                        data: vm.mfrPartByStatusCounts
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
                        categories: ['Unqualified', 'Qualified', 'Disqualified', 'Obsolete'],
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

                var chart = new ApexCharts(document.querySelector("#mfrPartByStatus"), options);
                chart.render();
                charts.push(chart);
            }

            function addManufacturerByStatusChart() {
                var options = {
                    series: [{
                        name: 'Manufacturer By Status',
                        data: vm.mfrByStatusCounts
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
                        categories: ['Unqualified', 'Review', 'Approved', 'Disqualified'],
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

                var chart = new ApexCharts(document.querySelector("#mfrByStatus"), options);
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
                if (!chartsInited) {
                    $timeout(function () {
                        addMfrPartStatusChart();
                        addManufacturerByStatusChart();
                        chartsInited = true;
                        $scope.$on("$destroy", destroyCharts);
                    }, 100);
                }
            }

            function loadOEMDashboardCounts() {
                vm.loading=true;
                AnalyticsService.getOEMDashboardCounts().then(
                    function (data) {
                        vm.mfrPartByStatusCounts = data.partsByStatus;
                        vm.mfrByStatusCounts = data.mfrByStatus;
                        initCharts();
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadTopManufacturerParts() {
                vm.loading=true;
                AnalyticsService.getTopMfrParts().then(
                    function (data) {
                        vm.topMfrParts = data;
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    })
            }

            function loadTopProblemParts() {
                vm.loading=true;
                AnalyticsService.getTopProblemMfrParts().then(
                    function (data) {
                        vm.topProblemParts = data;
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    })
            }

            function loadTopProblemMfrs() {
                vm.loading=true;
                AnalyticsService.getTopProblemMfrs().then(
                    function (data) {
                        vm.topProblemMfrs = data;
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    })
            }

            function loadTopRecurringParts() {
                vm.loading=true;
                AnalyticsService.getTopRecurringParts().then(
                    function (data) {
                        vm.topRecurringParts = data;
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    })
            }

            (function () {
                loadOEMDashboardCounts();
                loadTopManufacturerParts();
                loadTopProblemParts();
                loadTopProblemMfrs();
                loadTopRecurringParts();
            })();

        }
    }
);