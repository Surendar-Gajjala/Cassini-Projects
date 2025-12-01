define(
    [
        'app/desktop/modules/dashboard/dashboard.module',
        'app/shared/services/core/analyticsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectService'
    ],
    function (module) {
        module.controller('WorkflowDashboardController', WorkflowDashboardController);

        function WorkflowDashboardController($scope, $rootScope, $timeout, $compile, $sce, $interval, $state, $cookies, $window, $translate,
                                             $application, AnalyticsService, ObjectService) {

            var vm = this;
            $rootScope.viewInfo.title = "Workflow Dashboard";
            $rootScope.viewInfo.showDetails = false;

            var charts = [];

            var parsed = angular.element("<div></div>");
            $scope.workflowsByStatus = parsed.html($translate.instant("WORKFLOW_BY_STATUS")).html();
            $scope.itemWfByStatus = parsed.html($translate.instant("ITEM_WF_BY_STATUS")).html();
            $scope.changeWfByStatus = parsed.html($translate.instant("CHANGE_WF_BY_STATUS")).html();
            $scope.qualityWfByStatus = parsed.html($translate.instant("QUALITY_WF_BY_STATUS")).html();
            $scope.mfrWfByStatus = parsed.html($translate.instant("MFR_WF_BY_STATUS")).html();
            $scope.nprWfByStatus = parsed.html($translate.instant("NPR_WF_BY_STATUS")).html();
            $scope.workOrderWfByStatus = parsed.html($translate.instant("WORK_ORDER_WF_BY_STATUS")).html();
            $scope.mfrPartWfByStatus = parsed.html($translate.instant("MFR_PART_WF_BY_STATUS")).html();
            $scope.projectWfByStatus = parsed.html($translate.instant("PROJECT_WF_BY_STATUS")).html();
            $scope.requirementWfByStatus = parsed.html($translate.instant("REQUIREMENT_WF_BY_STATUS")).html();


            vm.workflowStatusCounts = [];
            vm.workflowChangeObjectTypeCounts = [];
            vm.workflowItemObjectTypeCounts = [];
            vm.workflowQualityObjectTypeCounts = [];
            vm.workflowManufacturerObjectTypeCounts = [];
            vm.workflowMfrPartObjectTypeCounts = [];
            vm.workflowWorkOrderObjectTypeCounts = [];
            vm.workflowProjectObjectTypeCounts = [];
            vm.workflowNPRObjectTypeCounts = [];

            function addItemObjectTypeChart() {
                var options = {
                    series: [{
                        name: 'Item Type Workflow By Status',
                        data: vm.workflowItemObjectTypeCounts
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
                        categories: ['Not Started', 'Inprogress', 'Released', 'Rejected'],
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

                var chart = new ApexCharts(document.querySelector("#ItemWorkflowByStatus"), options);
                chart.render();
                charts.push(chart);
            }

            function addChangeObjectTypeChart() {
                var options = {
                    series: [{
                        name: 'Change Type Workflow By Status',
                        data: vm.workflowChangeObjectTypeCounts
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
                        categories: ['Not Started', 'Inprogress', 'Released', 'Rejected'],
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

                var chart = new ApexCharts(document.querySelector("#ChangeWorkflowByStatus"), options);
                chart.render();
                charts.push(chart);
            }

            function addQualityObjectTypeChart() {
                var options = {
                    series: [{
                        name: 'Quality Type Workflow By Status',
                        data: vm.workflowQualityObjectTypeCounts
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
                        categories: ['Not Started', 'Inprogress', 'Released', 'Rejected'],
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
                var chart = new ApexCharts(document.querySelector("#QualityWorkflowByStatus"), options);
                chart.render();
                charts.push(chart);
            }

            function addMfrPartObjectTypeChart() {
                var options = {
                    series: [{
                        name: 'Manufacturer Part Type Workflow By Status',
                        data: vm.workflowMfrPartObjectTypeCounts
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
                        categories: ['Not Started', 'Inprogress', 'Released', 'Rejected'],
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

                var chart = new ApexCharts(document.querySelector("#ManufacturerPartWorkflowByStatus"), options);
                chart.render();
                charts.push(chart);
            }

            function addWorkOrderObjectTypeChart() {
                var options = {
                    series: [{
                        name: 'Work Order Workflow By Status',
                        data: vm.workflowWorkOrderObjectTypeCounts
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
                        categories: ['Not Started', 'Inprogress', 'Released', 'Rejected'],
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

                var chart = new ApexCharts(document.querySelector("#WorkOrderWorkflowByStatus"), options);
                chart.render();
                charts.push(chart);
            }

            function addWorkflowStatusChart() {
                var options = {
                    series: vm.workflowStatusCounts,
                    chart: {
                        type: 'donut',
                        height: 290,
                        toolbar: {
                            show: false
                        }
                    },
                    opacity: 1.0,
                    labels: ['Not Started', 'Inprogress', 'Released', 'Rejected'],
                    title: {
                        text: $scope.workflowsByStatus
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
                var chart = new ApexCharts(document.querySelector("#workflowByStatus"), options);
                chart.render();
                charts.push(chart);
            }

            function addManufacturerObjectTypeChart() {
                var options = {
                    series: [{
                        name: "Count",
                        data: vm.workflowManufacturerObjectTypeCounts
                    }],
                    xaxis: {
                        categories: [
                            'Not Started', 'Inprogress', 'Released', 'Rejected'
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
                        text: $scope.mfrWfByStatus
                    }

                };

                var chart = new ApexCharts(document.querySelector("#ManufacturerWorkflowByStatus"), options);
                chart.render();
                charts.push(chart);
            }

            function addProjectObjectTypeChart() {
                var options = {
                    series: [{
                        name: "Count",
                        data: vm.workflowProjectObjectTypeCounts
                    }],
                    xaxis: {
                        categories: [
                            'Not Started', 'Inprogress', 'Released', 'Rejected'
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
                        offsetX: -10
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
                        text: $scope.projectWfByStatus
                    }

                };

                var chart = new ApexCharts(document.querySelector("#ProjectWorkflowByStatus"), options);
                chart.render();
                charts.push(chart);
            }


            function addNPRObjectTypeChart() {
                var options = {
                    series: [{
                        name: "Count",
                        data: vm.workflowNPRObjectTypeCounts
                    }],
                    xaxis: {
                        categories: [
                            'Not Started', 'Inprogress', 'Released', 'Rejected'
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
                        offsetX: -10
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
                        text: $scope.nprWfByStatus
                    }

                };

                var chart = new ApexCharts(document.querySelector("#NPRWorkflowByStatus"), options);
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
                        addWorkflowStatusChart();
                        addChangeObjectTypeChart();
                        addItemObjectTypeChart();
                        addQualityObjectTypeChart();
                        addManufacturerObjectTypeChart();
                        addMfrPartObjectTypeChart();
                        addProjectObjectTypeChart();
                        addNPRObjectTypeChart();
                        addWorkOrderObjectTypeChart();
                        chartsInited = true;
                        $scope.$on("$destroy", destroyCharts);
                    }, 100);
                }
            }

            function loadWorkflowDashboardCounts() {
                AnalyticsService.getWorkflowDashboardCounts().then(
                    function (data) {
                        vm.workflowStatusCounts = data.workflowTypes;
                        vm.workflowObjectTypeCounts = data.workflowObjectTypes;
                        vm.workflowChangeObjectTypeCounts = data.changeTypes;
                        vm.workflowItemObjectTypeCounts = data.itemTypes;
                        vm.workflowQualityObjectTypeCounts = data.qualityTypes;
                        vm.workflowManufacturerObjectTypeCounts = data.mfrTypes;
                        vm.workflowMfrPartObjectTypeCounts = data.mfrPartTypes;
                        vm.workflowProjectObjectTypeCounts = data.projectTypes;
                        vm.workflowNPRObjectTypeCounts = data.nprTypes;
                        vm.workflowWorkOrderObjectTypeCounts = data.workOrderTypes;
                        initCharts();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadWorkflowTypeCardCounts() {
                AnalyticsService.getWorkflowTypeCardCounts().then(
                    function (data) {
                        $scope.workflowTypeCard = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadWorkflowDashboardCounts();
                loadWorkflowTypeCardCounts();
            })();

        }
    }
);