define(
    [
        'app/desktop/modules/dashboard/dashboard.module',
        'app/shared/services/core/analyticsService'
    ],
    function (module) {
        module.controller('QualityDashboardController', QualityDashboardController);

        function QualityDashboardController($scope, $rootScope, $timeout, $compile, $sce, $interval, $state, $cookies, $window, $translate,
                                            $application, AnalyticsService) {

            var vm = this;
            $rootScope.viewInfo.title = "Quality Dashboard";
            $rootScope.viewInfo.showDetails = false;

            var parsed = angular.element("<div></div>");
            $scope.inspectionByStatus = parsed.html($translate.instant("INSPECTION_BY_STATUS")).html();
            $scope.inspectionPlanByStatus = parsed.html($translate.instant("INSPECTION_PLAN_BY_STATUS")).html();
            $scope.problemReportByStatus = parsed.html($translate.instant("PROBLEM_REPORT_BY_STATUS")).html();
            $scope.ncrByStatus = parsed.html($translate.instant("NCR_BY_STATUS")).html();
            $scope.qcrByStatus = parsed.html($translate.instant("QCR_BY_STATUS")).html();
            $scope.supplierAuditByStatus = parsed.html($translate.instant("SUPPLIER_AUDIT_BY_STATUS")).html();
            $scope.prBySeverity = parsed.html($translate.instant("PR_BY_SEVERITY")).html();
            $scope.prByFailure = parsed.html($translate.instant("PR_BY_FAILURE")).html();
            $scope.prByDisposition = parsed.html($translate.instant("PR_BY_DISPOSITION")).html();
            $scope.ncrBySeverity = parsed.html($translate.instant("NCR_BY_SEVERITY")).html();
            $scope.ncrByFailure = parsed.html($translate.instant("NCR_BY_FAILURE")).html();
            $scope.ncrByDisposition = parsed.html($translate.instant("NCR_BY_DISPOSITION")).html();
            $scope.ppapByStatus = parsed.html($translate.instant("PPAP_BY_STATUS")).html();
            $scope.inspectionReports = parsed.html($translate.instant("INSPECTION_REPORTS_BY_STATUS")).html();
            $scope.ppapChecklistStatusTitle = parsed.html($translate.instant("PPAP_CHECKLIST_STATUS")).html();
            $scope.supplierAuditByYear = parsed.html($translate.instant("SUPPLIER_AUDIT_BY_YEAR")).html();


            var charts = [];
            vm.qualityByTypeCounts = [];
            vm.inspectionPlansByStatusCounts = [];
            vm.inspectionsByStatusCounts = [];
            vm.problemReportsByStatusCounts = [];
            vm.problemReportsBySourceCounts = [];
            vm.ncrsByStatusCounts = [];
            vm.qcrsByStatusCounts = [];
            vm.qcrsByTypeCounts = [];
            vm.ppapByStatusCounts = [];
            vm.supplierAuditsByTypeCounts = [];

            function addQualityChart() {
                var options = {
                    series: vm.qualityByTypeCounts,
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

                var chart = new ApexCharts(document.querySelector("#qualityCounts"), options);
                chart.render();
                charts.push(chart);
            }

            function addProblemReportsChart() {
                var options = {
                    series: [{
                        name: 'Problem Reports',
                        data: vm.problemReportsByStatusCounts
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
                        categories: ['Pending', 'Approved', 'Rejected'],
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

                var chart = new ApexCharts(document.querySelector("#problemReportsByStatus"), options);
                chart.render();
                charts.push(chart);
            }

            function addQualityTypesByStatusChart() {
                var options = {
                    series: [{
                        name: '',
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
                        categories: ['Pending', 'Approved', 'Rejected'],
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

                angular.forEach(vm.qualityTypeReportsByStatus, function (report) {
                    options.series[0].name = report.name;
                    options.series[0].data = report.data;
                    var chart = new ApexCharts(document.querySelector("#" + report.id), options);
                    chart.render();
                    charts.push(chart);
                })
            }

            function addProblemReportsBySourceChart() {
                var options = {
                    series: vm.problemReportsBySourceCounts,
                    chart: {
                        type: 'donut',
                        height: 290,
                        toolbar: {
                            show: false
                        }
                    },
                    opacity: 1.0,
                    labels: ['Customer', 'Internal', 'Supplier'],
                    title: {
                        text: 'Problem Reports by Type'
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

                var chart = new ApexCharts(document.querySelector("#problemReportsBySource"), options);
                chart.render();
                charts.push(chart);
            }

            function addQcrByTypeChart() {
                var options = {
                    series: vm.qcrsByTypeCounts,
                    chart: {
                        type: 'donut',
                        height: 290,
                        toolbar: {
                            show: false
                        }
                    },
                    opacity: 1.0,
                    labels: ['PR', 'NCR'],
                    title: {
                        text: 'QCRs by Type'
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

                var chart = new ApexCharts(document.querySelector("#qcrByType"), options);
                chart.render();
                charts.push(chart);
            }

            function addPpapStatusChart() {
                var options = {
                    series: vm.ppapByStatusCounts,
                    chart: {
                        type: 'pie',
                        height: 290,
                        toolbar: {
                            show: false
                        }
                    },
                    opacity: 1.0,
                    labels: ['Draft', 'Review', 'Approved'],
                    title: {
                        text: $scope.ppapByStatus
                    },
                    dataLabels: {
                        enabled: false
                    },
                    tooltip: {
                        enabled: true
                    },
                    colors: [
                        '#FBB917', '#1bc5bd', '#3399FF'
                    ],
                    plotOptions: {
                        pie: {
                            donut: {
                                labels: {
                                    show: true,
                                    name: {
                                        show: false
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
                var chart = new ApexCharts(document.querySelector("#ppapByStatus"), options);
                chart.render();
                charts.push(chart);
            }

            function addSupplierAuditsByTypeChart() {
                var options = {
                    series: vm.supplierAuditsByTypeCounts,
                    chart: {
                        type: 'pie',
                        height: 250,
                        toolbar: {
                            show: false
                        }
                    },
                    opacity: 1.0,
                    labels: ['None', 'Planned', 'Completed','Approved','Overdue'],
                    dataLabels: {
                        enabled: false
                    },
                    tooltip: {
                        enabled: true
                    },
                    colors: [

                        '#808080', '#008080', '#00E396','#3699ff', '#E55451'

                    ],
                    plotOptions: {
                        pie: {
                            donut: {
                                labels: {
                                    show: true,
                                    name: {
                                        show: false
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

                var chart = new ApexCharts(document.querySelector("#supplierAuditByStatus"), options);
                chart.render();
                charts.push(chart);
            }

            function addSeverityFailureDispositionCharts() {
                var options = {
                    series: [{
                        name: "",
                        data: []
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
                    }/*,
                     colors: [
                     '#1B5E20', '#2E7D32', '#388E3C', '#43A047', '#4CAF50',
                     '#66BB6A', '#81C784', '#A5D6A7', '#C8E6C9', '#E8F5E9'
                     ]*/,
                    title: {
                        text: ''
                    }
                };

                angular.forEach(vm.qualityReportsBySFD, function (report) {
                    options.series[0].data = report.counts;
                    options.xaxis.categories = report.data;
                    options.title.text = report.name;
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
                        addProblemReportsBySourceChart();
                        addQcrByTypeChart();
                        addPpapStatusChart();
                        addSupplierAuditsByTypeChart();
                        chartsInited = true;
                        $scope.$on("$destroy", destroyCharts);
                    }, 100);
                }
            }

            $scope.qualityTypeCardCounts = null;
            function loadQualityCardCounts() {
                AnalyticsService.getQualityCardCounts().then(
                    function (data) {
                        $scope.qualityTypeCardCounts = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadQualityDashboardCounts() {
                AnalyticsService.getQualityDashboardCounts().then(
                    function (data) {
                        vm.qualityByTypeCounts = data.qualityTypes;
                        vm.inspectionPlansByStatusCounts = data.inspectionPlansByStatus;
                        vm.inspectionsByStatusCounts = data.inspectionsByStatus;
                        vm.problemReportsByStatusCounts = data.prsByStatus;
                        vm.problemReportsBySourceCounts = data.prsBySource;
                        vm.ncrsByStatusCounts = data.ncrsByStatus;
                        vm.qcrsByTypeCounts = data.qcrsByType;
                        vm.qcrsByStatusCounts = data.qcrsByStatus;
                        vm.ppapByStatusCounts = data.ppapByStatus;
                        vm.supplierAuditsByTypeCounts = data.supplierAuditsByStatusCounts;
                        vm.qualityTypeReportsByStatus = [
                            {
                                name: $scope.inspectionPlanByStatus,
                                id: "inspectionPlanByStatus",
                                data: vm.inspectionPlansByStatusCounts
                            }, {
                                name: $scope.inspectionByStatus,
                                id: "inspectionsByStatus",
                                data: vm.inspectionsByStatusCounts
                            }, {
                                name: $scope.problemReportByStatus,
                                id: "problemReportsByStatus",
                                data: vm.problemReportsByStatusCounts
                            }, {
                                name: $scope.ncrByStatus,
                                id: "ncrByStatus",
                                data: vm.ncrsByStatusCounts
                            }, {
                                name: $scope.qcrByStatus,
                                id: "qcrByStatus",
                                data: vm.qcrsByStatusCounts
                            }
                        ];
                        $timeout(function () {
                            addQualityTypesByStatusChart();
                        }, 1000);
                        initCharts();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadPPAPChecklistStatusChart() {
                var options = {
                    series: vm.ppapCheckListStatusCounts.foldersReports,
                    chart: {
                        type: 'bar',
                        height: 225,
                        width: 350,
                        toolbar: {show: false}
                    },
                    colors: [
                        '#708090', '#EE9A4D', '#6495ED', '#6CBB3C'
                    ],

                    plotOptions: {
                        bar: {
                            horizontal: false,
                            columnWidth: '60%'
                        },
                    },

                    dataLabels: {
                        enabled: false
                    },
                    stroke: {
                        show: true,
                        width: 2,
                        colors: ['transparent']
                    },
                    xaxis: {
                        categories: vm.ppapCheckListStatusCounts.ppapFolders,
                        labels: {
                            trim: true,
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

                        },
                        fill: {
                            opacity: 1
                        },
                        tooltip: {
                            enabled: false
                        },
                    }
                };
                var chart = new ApexCharts(document.querySelector("#ppapChecklistStatus"), options);
                chart.render();
                charts.push(chart);
            }

            function loadObjectSeverityFailureDispositions() {
                AnalyticsService.getObjectSeverityFailureDispositions().then(
                    function (data) {
                        vm.prsBySeverities = data.prSeverities;
                        vm.prsBySeverityCounts = data.prSeverityCounts;
                        vm.prsByFailures = data.prFailures;
                        vm.prsByFailureCounts = data.prFailureCounts;
                        vm.prsByDispsitions = data.prDispositions;
                        vm.prsByDispositionCounts = data.prDispositionCounts;
                        vm.ncrsBySeverities = data.ncrSeverities;
                        vm.ncrsBySeverityCounts = data.ncrSeverityCounts;
                        vm.ncrsByFailures = data.ncrFailures;
                        vm.ncrsByFailureCounts = data.ncrFailureCounts;
                        vm.ncrsByDispsitions = data.ncrDispositions;
                        vm.ncrsByDispositionCounts = data.ncrDispositionCounts;
                        vm.qualityReportsBySFD = [
                            {
                                name: $scope.prBySeverity,
                                id: "problemReportsBySeverity",
                                data: vm.prsBySeverities,
                                counts: vm.prsBySeverityCounts
                            },
                            {
                                name: $scope.prByFailure,
                                id: "problemReportsByFailure",
                                data: vm.prsByFailures,
                                counts: vm.prsByFailureCounts
                            },
                            {
                                name: $scope.prByDisposition,
                                id: "problemReportsByDisposition",
                                data: vm.prsByDispsitions,
                                counts: vm.prsByDispositionCounts
                            },
                            {
                                name: $scope.ncrBySeverity,
                                id: "ncrsBySeverity",
                                data: vm.ncrsBySeverities,
                                counts: vm.ncrsBySeverityCounts
                            },
                            {
                                name: $scope.ncrByFailure,
                                id: "ncrsByFailure",
                                data: vm.ncrsByFailures,
                                counts: vm.ncrsByFailureCounts
                            },
                            {
                                name: $scope.ncrByDisposition,
                                id: "ncrsByDisposition",
                                data: vm.ncrsByDispsitions,
                                counts: vm.ncrsByDispositionCounts
                            }
                        ]
                        $timeout(function () {
                            addSeverityFailureDispositionCharts();
                        }, 1000)
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function addInspectionReportsChart() {
                var options = {
                    series: vm.mfrInspectionReportCounts.monthReports,
                    chart: {
                        type: 'bar',
                        height: 225,
                        width: 350,
                        toolbar: {show: false}
                    },
                    plotOptions: {
                        bar: {
                            columnWidth: '80%',
                            dataLabels: {
                                position: 'top' // top, center, bottom
                            }
                        }
                    },
                    colors: ['#ffa800', '#1bc5bd', '#3699ff', '#3cb371', '#ff4560'],
                    dataLabels: {
                        enabled: false
                    },
                    stroke: {
                        show: true,
                        width: 2,
                        colors: ['transparent']
                    },
                    xaxis: {
                        categories: vm.mfrInspectionReportCounts.months,
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

                        },
                        fill: {
                            opacity: 1
                        },
                        tooltip: {
                            enabled: false
                        },
                    }
                };
                var chart = new ApexCharts(document.querySelector("#inspectionReports"), options);
                chart.render();
                charts.push(chart);
            }

            function addSupplierAuditByYearChart() {
                var options = {
                    series: vm.supplierAuditReportCounts.yearReports,
                    chart: {
                        type: 'bar',
                        height: 225,
                        width: 350,
                        toolbar: {show: false}
                    },
                    plotOptions: {
                        bar: {
                            horizontal: false,
                            columnWidth: '60%'
                        },
                    },
                    colors: ['#ffa800', '#1bc5bd', '#00E396', '#3699ff'],
                    dataLabels: {
                        enabled: false
                    },
                    stroke: {
                        show: true,
                        width: 2,
                        colors: ['transparent']
                    },
                    xaxis: {
                        categories: vm.supplierAuditReportCounts.years,
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

                        },
                        fill: {
                            opacity: 1
                        },
                        tooltip: {
                            enabled: false
                        },
                    }
                };
                var chart = new ApexCharts(document.querySelector("#supplierAuditByYear"), options);
                chart.render();
                charts.push(chart);
            }

            function maximizeSupplierAuditByYearChart(windowContent) {
                var options = {
                    series: vm.supplierAuditReportCounts.yearReports,
                    chart: {
                        type: 'bar',
                        height: 225,
                        toolbar: {show: false}
                    },
                    plotOptions: {
                        bar: {
                            horizontal: false,
                            columnWidth: '20%'
                        },
                    },
                    colors: ['#ffa800', '#1bc5bd', '#00E396', '#3699ff'],
                    dataLabels: {
                        enabled: false
                    },
                    stroke: {
                        show: true,
                        width: 2,
                        colors: ['transparent']
                    },
                    xaxis: {
                        categories: vm.supplierAuditReportCounts.years,
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

                        },
                        fill: {
                            opacity: 1
                        },
                        tooltip: {
                            enabled: false
                        },

                    }
                };
                vm.windowChart = new ApexCharts(document.querySelector("#maximizeInpectionReports"), options);
                vm.windowChart.render();
                charts.push(vm.windowChart);
            }

            function maximizeaddPPAPChecklistReportsChart(windowContent) {
                var options = {
                    series: vm.ppapCheckListStatusCounts.foldersReports,
                    chart: {
                        type: 'bar',
                        height: 225,
                        toolbar: {show: false}
                    },
                    plotOptions: {
                        bar: {
                            horizontal: false,
                            columnWidth: '25%'
                        },
                    },
                    colors: ['#708090', '#EE9A4D', '#6495ED', '#6CBB3C'],
                    dataLabels: {
                        enabled: false
                    },
                    stroke: {
                        show: true,
                        width: 2,
                        colors: ['transparent']
                    },
                    xaxis: {
                        categories: vm.ppapCheckListStatusCounts.ppapFolders,
                        labels: {
                            trim: true,
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

                        },
                        fill: {
                            opacity: 1
                        },
                        tooltip: {
                            enabled: false
                        },

                    }
                };
                vm.windowChart = new ApexCharts(document.querySelector("#maximizeCheckListReports"), options);
                vm.windowChart.render();
                charts.push(vm.windowChart);
            }

            function maximizeaddInspectionReportsChart(windowContent) {
                var options = {
                    series: vm.mfrInspectionReportCounts.monthReports,
                    chart: {
                        type: 'bar',
                        height: 225,
                        toolbar: {show: false}
                    },
                    plotOptions: {
                        bar: {
                            columnWidth: '80%',
                            dataLabels: {
                                position: 'top' // top, center, bottom
                            }
                        }
                    },
                    colors: ['#ffa800', '#1bc5bd', '#3699ff', '#3cb371', '#ff4560'],
                    dataLabels: {
                        enabled: false
                    },
                    stroke: {
                        show: true,
                        width: 2,
                        colors: ['transparent']
                    },
                    xaxis: {
                        categories: vm.mfrInspectionReportCounts.months,
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

                        },
                        fill: {
                            opacity: 1
                        },
                        tooltip: {
                            enabled: false
                        },

                    }
                };
                vm.windowChart = new ApexCharts(document.querySelector("#maximizeInpectionReports"), options);
                vm.windowChart.render();
                charts.push(vm.windowChart);
            }

            $scope.inspectionReportCounts = null;
            function loadInspectionReportCounts() {
                AnalyticsService.getMfrInspectionReportCounts().then(
                    function (data) {
                        vm.mfrInspectionReportCounts = data;
                        $timeout(function () {
                            addInspectionReportsChart();
                        }, 1000)
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadPPAPChecklistCounts() {
                AnalyticsService.getPPAPCheckListStatusCounts().then(
                    function (data) {
                        vm.ppapCheckListStatusCounts = data;
                        $timeout(function () {
                            loadPPAPChecklistStatusChart();
                        }, 1000)
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $scope.maximizeWidget = maximizeWidget;
            function maximizeWidget(type) {
                var modal = document.getElementById("widget-window");
                modal.style.display = "block";
                $timeout(function () {
                    var headerHeight = $('.widget-window-header').outerHeight();
                    var bomContentHeight = $('.widget-window-content').outerHeight();
                    $(".window-content").height(bomContentHeight - headerHeight);
                    var windowContent = bomContentHeight - headerHeight;
                    $scope.maxType = type;
                    if (type == 'inspectionReports') maximizeaddInspectionReportsChart(windowContent);
                    if (type == 'supplierAuditByYear') maximizeSupplierAuditByYearChart(windowContent);
                }, 200)

            }

            $scope.hideWidgetWindow = hideWidgetWindow;
            function hideWidgetWindow() {
                var modal = document.getElementById("widget-window");
                modal.style.display = "none";
                vm.windowChart.destroy();
            }

            $scope.maximizePPAPChecklistWidget = maximizePPAPChecklistWidget;
            function maximizePPAPChecklistWidget(type) {
                var modal = document.getElementById("widget1-window");
                modal.style.display = "block";
                $timeout(function () {
                    var headerHeight = $('.widget1-window-header').outerHeight();
                    var bomContentHeight = $('.widget1-window-content').outerHeight();
                    $(".window1-content").height(bomContentHeight - headerHeight);
                    var windowContent = bomContentHeight - headerHeight;
                    $scope.maxType = type;
                    if (type == 'ppapChecklistReports') maximizeaddPPAPChecklistReportsChart(windowContent);
                }, 200)

            }

            $scope.hideWidgetPPAPWindow = hideWidgetPPAPWindow;
            function hideWidgetPPAPWindow() {
                var modal = document.getElementById("widget1-window");
                modal.style.display = "none";
                vm.windowChart.destroy();
            }


            vm.pageable = {
                page: 0,
                size: 10,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            function loadInspectionFailureProducts() {
                vm.loading = true;
                AnalyticsService.getTopInspectionFailureProducts().then(
                    function (data) {
                        vm.inspectionFailureProducts = data;
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $scope.supplierAuditReportCounts = null;
            function loadSupplierAuditReports() {
                AnalyticsService.getSupplierAuditReportCounts().then(
                    function (data) {
                        vm.supplierAuditReportCounts = data;
                        $timeout(function () {
                            addSupplierAuditByYearChart();
                        }, 1000)
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            function loadTopProductProblems() {
                AnalyticsService.getTopProductProblems().then(
                    function (data) {
                        vm.productProblems = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadInspectionFailureMaterials() {
                AnalyticsService.getTopInspectionFailureMaterials().then(
                    function (data) {
                        vm.inspectionFailureMaterials = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadTopCustomerReportingProblems() {
                AnalyticsService.getTopCustomerReportingProblems().then(
                    function (data) {
                        vm.customerReportingProblems = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadTopManufacturersForNCR() {
                AnalyticsService.getTopManufacturersForNCR().then(
                    function (data) {
                        vm.manufacturersForNCR = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadQualityCardCounts();
                loadQualityDashboardCounts();
                loadObjectSeverityFailureDispositions();
                loadInspectionFailureProducts();
                loadTopProductProblems();
                loadInspectionFailureMaterials();
                loadTopCustomerReportingProblems();
                loadTopManufacturersForNCR();
                loadInspectionReportCounts();
                loadPPAPChecklistCounts();
                loadSupplierAuditReports();
            })();

        }
    }
);