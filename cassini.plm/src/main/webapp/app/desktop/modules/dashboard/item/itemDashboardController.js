define(
    [
        'app/desktop/modules/dashboard/dashboard.module',
        'app/shared/services/core/analyticsService'
    ],
    function (module) {
        module.controller('ItemDashboardController', ItemDashboardController);

        function ItemDashboardController($scope, $rootScope, $timeout, $compile, $sce, $interval, $state, $cookies, $window, $translate,
                                         $application, AnalyticsService) {

            var vm = this;
            $rootScope.viewInfo.title = "Items Dashboard";
            $rootScope.viewInfo.showDetails = false;

            var parsed = angular.element("<div></div>");
            $scope.productsByStatus = parsed.html($translate.instant("P_ITEM_BY_STATUS")).html();
            $scope.assembliesByStatus = parsed.html($translate.instant("A_ITEM_BY_STATUS")).html();
            $scope.partsByStatus = parsed.html($translate.instant("PT_ITEM_BY_STATUS")).html();
            $scope.documentsByStatus = parsed.html($translate.instant("D_ITEM_BY_STATUS")).html();
            $scope.othersByStatus = parsed.html($translate.instant("O_ITEM_BY_STATUS")).html();

            $scope.productsByPhase = parsed.html($translate.instant("P_BY_PHASE")).html();
            $scope.assembliesByPhase = parsed.html($translate.instant("A_BY_PHASE")).html();
            $scope.partsByPhase = parsed.html($translate.instant("PT_BY_PHASE")).html();
            $scope.documentsByPhase = parsed.html($translate.instant("D_BY_PHASE")).html();
            $scope.othersByPhase = parsed.html($translate.instant("O_BY_PHASE")).html();
            $scope.itemByConfiguration = parsed.html($translate.instant("ITEM_BY_CONFIGURATION")).html();

            var charts = [];
            vm.problemItems = [];
            vm.loading = true;
            function addChangeTypeReportChart() {
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
                        categories: ['Pending', 'Released', 'Cancelled'],
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

                angular.forEach(vm.itemClassItemsByStatusReports, function (report) {
                    options.series[0].name = report.name;
                    options.series[0].data = report.data;
                    var chart = new ApexCharts(document.querySelector("#" + report.id), options);
                    chart.render();
                    charts.push(chart);
                })

            }

            function addItemsByLifecyclePhaseReportChart() {
                var options = {
                    series: [{
                        name: "Count",
                        data: vm.itemsByClass
                    }],
                    xaxis: {
                        categories: []
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

                angular.forEach(vm.itemsByLifecyclePhasesCounts, function (report) {
                    options.title.text = report.name;
                    options.series[0].data = report.counts;
                    options.xaxis.categories = report.data;
                    var chart = new ApexCharts(document.querySelector("#" + report.id), options);
                    chart.render();
                    charts.push(chart);
                })
            }

            function addItemsByConfigurationChart() {
                var options = {
                    series: vm.configurationCounts,
                    chart: {
                        type: 'donut',
                        height: 290,
                        toolbar: {
                            show: false
                        }
                    },
                    opacity: 1.0,
                    labels: ['Normal', 'Configurable', 'Configured'],
                    title: {
                        text: $scope.itemByConfiguration
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
                                        color: '#fff'
                                    }
                                }
                            }
                        }
                    },
                    legend: {
                        position: 'bottom'
                    }
                };
                var chart = new ApexCharts(document.querySelector("#configurationCounts"), options);
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
                        addChangeTypeReportChart();
                        addItemsByConfigurationChart();
                        chartsInited = true;
                        $scope.$on("$destroy", destroyCharts);
                    }, 100);
                }
            }

            function loadItemDashboardCounts() {
                AnalyticsService.getItemDashboardCounts().then(
                    function (data) {
                        vm.itemClassItemsByStatusReports = [
                            {
                                name: "Product Items by Status",
                                id: "productItemsByStatusCounts",
                                data: data.productItemsByStatus
                            },
                            {
                                name: "Assembly Items by Status",
                                id: "assemblyItemsByStatusCounts",
                                data: data.assemblyItemsByStatus
                            },
                            {name: "Part Items by Status", id: "partItemsByStatusCounts", data: data.partItemsByStatus},
                            {
                                name: "Document Items by Status",
                                id: "documentItemsByStatusCounts",
                                data: data.documentItemsByStatus
                            },
                            {
                                name: "Other Items by Status",
                                id: "otherItemsByStatusCounts",
                                data: data.otherItemsByStatus
                            }
                        ]
                        vm.configurationCounts = data.itemsByConfigurations;
                        initCharts();
                        vm.loading = false;
                    }, function (error) {
                          $rootScope.showErrorMessage(error.message);
                          $rootScope.hideBusyIndicator();
                     }
                )
            }

            $scope.itemConfigurations = null;
            function loadItemClassCardCounts() {
                AnalyticsService.getItemClassCardCounts().then(
                    function (data) {
                        $scope.itemClassCardCounts = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadItemsByLifecyclePhases() {
                AnalyticsService.getItemsByLifeCyclePhases().then(
                    function (data) {
                        vm.itemsByLifecyclePhasesCounts = [
                            {
                                name: $scope.productsByPhase,
                                id: "productItemsByLifecycleCounts",
                                data: data.productLifeCycles,
                                counts: data.productLifeCycleCounts
                            },
                            {
                                name: $scope.assembliesByPhase,
                                id: "assemblyItemsByLifecycleCounts",
                                data: data.assemblyLifeCycles,
                                counts: data.assemblyLifeCycleCounts
                            },
                            {
                                name: $scope.partsByPhase,
                                id: "partItemsByLifecycleCounts",
                                data: data.partLifeCycles,
                                counts: data.partLifeCycleCounts
                            },
                            {
                                name: $scope.documentsByPhase,
                                id: "documentItemsByLifecycleCounts",
                                data: data.documentLifeCycles,
                                counts: data.documentLifeCycleCounts
                            },
                            {
                                name: $scope.othersByPhase,
                                id: "otherItemsByLifecycleCounts",
                                data: data.otherLifeCycles,
                                counts: data.otherLifeCycleCounts
                            }
                        ]
                        $timeout(function () {
                            addItemsByLifecyclePhaseReportChart();
                        }, 1000);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadTopProblemItems() {
                AnalyticsService.getTopProblemItems().then(
                    function (data) {
                        vm.problemItems = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadTopProblemItemTypes() {
                AnalyticsService.getTopProblemItemTypes().then(
                    function (data) {
                        vm.problemItemTypes = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadFrequentlyChangingItemTypes() {
                AnalyticsService.getTopChangingItemTypes().then(
                    function (data) {
                        vm.changingItemTypes = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadItemDashboardCounts();
                loadItemClassCardCounts();
                loadItemsByLifecyclePhases();
                loadTopProblemItems();
                loadTopProblemItemTypes();
                loadFrequentlyChangingItemTypes();
            })();

        }
    }
);