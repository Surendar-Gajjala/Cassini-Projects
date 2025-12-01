define(
    [
        'app/desktop/modules/dashboard/dashboard.module',
        'app/shared/services/core/analyticsService'
    ],
    function (module) {
        module.controller('ProjectDashboardController', ProjectDashboardController);

        function ProjectDashboardController($scope, $rootScope, $timeout, $compile, $sce, $interval, $state, $cookies, $window, $translate,
                                            $application, AnalyticsService) {

            var vm = this;
            $rootScope.viewInfo.title = "Projects Dashboard";
            $rootScope.viewInfo.showDetails = false;

            var charts = [];

            function addProjectsStatusChart() {
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
                        categories: ['Not Started', 'In Progress', 'Finished', 'Overdue'],
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

                angular.forEach(vm.projectsByStatusReports, function (report) {
                    options.series[0].name = report.name;
                    options.series[0].data = report.data;
                    var chart = new ApexCharts(document.querySelector("#" + report.id), options);
                    chart.render();
                    charts.push(chart);
                })

            }

            function addProjectStatusByProgramChart(type) {
                var options = {
                    series: vm.projectStatusByProgram.projectStatusCounts,
                    chart: {
                        type: 'bar',
                        height: 230,
                        toolbar: {
                            show: false
                        }
                    },
                    plotOptions: {
                        bar: {
                            columnWidth: '75%',
                            dataLabels: {
                                position: 'top' // top, center, bottom
                            }
                        }
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
                        categories: vm.projectStatusByProgram.programs,
                        position: 'bottom',
                        axisBorder: {
                            show: false
                        },
                        labels: {
                            trim: true,
                            rotate: 0
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
                        }
                    }
                };
                if (type == null) {
                    var chart = new ApexCharts(document.querySelector("#projectStatusByProgram"), options);
                    chart.render();
                    charts.push(chart);
                } else {
                    options.chart.height = type;
                    vm.windowChart = new ApexCharts(document.querySelector("#maximizeOpenTasks"), options);
                    vm.windowChart.render();
                    charts.push(vm.windowChart);
                }

            }

            function addDeliverableStatusChart() {
                var options = {
                    series: [],
                    chart: {
                        type: 'donut',
                        height: '100%',
                        toolbar: {
                            show: false
                        }
                    },
                    opacity: 1.0,
                    labels: ['Pending', 'Finished'],
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

                angular.forEach(vm.deliverableByStatus, function (report) {
                    //options.title.text = report.name;
                    options.series = report.data;
                    var chart = new ApexCharts(document.querySelector("#" + report.id), options);
                    chart.render();
                    charts.push(chart);
                })
            }

            function addReqTypeChart() {
                var options = {
                    series: [{
                        name: "Count",
                        data: vm.reqCounts
                    }],
                    xaxis: {
                        categories: vm.reqByTypes
                    },
                    chart: {
                        height: 215,
                        type: 'bar',
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
                    }
                };

                var chart = new ApexCharts(document.querySelector("#reqByType"), options);
                chart.render();
                charts.push(chart);
            }

            function addOpenTasksChart() {
                var options = {
                    series: [{
                        name: "Count",
                        data: vm.openTasks
                    }],
                    xaxis: {
                        categories: vm.openProjects
                    },
                    chart: {
                        height: 215,
                        type: 'bar',
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
                            colors: ['#636e7b'],
                            fontSize: '10px',
                            fontWeight: 'normal'
                        },
                        offsetX: 20
                    },
                    tooltip: {
                        enabled: true
                    },
                    legend: {
                        show: false
                    }
                };

                var chart = new ApexCharts(document.querySelector("#openTasks"), options);
                chart.render();
                charts.push(chart);
            }

            function addProjectActivityStatusChart() {
                var options = {
                    series: [{
                        name: 'Pending',
                        data: vm.activityStatusCount.pendingCounts
                    }, {
                        name: 'Inprogress',
                        data: vm.activityStatusCount.inProgressCounts
                    }, {
                        name: 'Finished',
                        data: vm.activityStatusCount.finishedCounts
                    }, {
                        name: 'Overdue',
                        data: vm.activityStatusCount.overDueCounts
                    }],
                    chart: {
                        type: 'bar',
                        height: 195,
                        width: 400,
                        toolbar: {show: false}
                    },
                    plotOptions: {
                        bar: {
                            horizontal: false,
                            columnWidth: '65%'

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
                        categories: vm.projects,
                        position: 'bottom',
                        labels: {
                            trim: true,
                            rotate: 0
                        },
                        axisBorder: {
                            show: false
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
                var chart = new ApexCharts(document.querySelector("#projectActivityStatus"), options);
                chart.render();
                charts.push(chart);
            }

            function addReqStatusChart() {
                var options = {
                    series: [{
                        name: "",
                        data: vm.reqByStatus
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
                        categories: ['Draft', 'Review', 'Approved'],
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

                var chart = new ApexCharts(document.querySelector("#reqByStatus"), options);
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
                        addProjectsStatusChart();
                        addDeliverableStatusChart();
                        addReqStatusChart();
                        addOpenTasksChart();
                        addReqTypeChart();
                        addProjectActivityStatusChart();
                        chartsInited = true;
                        $scope.$on("$destroy", destroyCharts);
                    }, 100);
                }
            }

            function loadProjectStatusByProgramDashboardCounts() {
                AnalyticsService.getProjectStatusByProgramDashboardCounts().then(
                    function (data) {
                        vm.projectStatusByProgram = data;
                        addProjectStatusByProgramChart(null);
                    }
                )
            }

            function loadProjectDashboardCounts() {
                AnalyticsService.getProjectStatusCounts().then(
                    function (data) {
                        vm.projectsByStatusReports = [
                            {
                                name: "Project By Status",
                                id: "projectByStatus",
                                data: data.projectByStatus
                            },
                            {
                                name: "Activity By Status",
                                id: "activityByStatus",
                                data: data.activityByStatus
                            },
                            {
                                name: "Task By Status",
                                id: "taskByStatus",
                                data: data.taskByStatus
                            }
                        ];
                        vm.deliverableByStatus = [
                            {
                                name: "Project Deliverables By Status",
                                id: "projectDeliverableByStatus",
                                data: data.projectDeliverableByStatus
                            },
                            {
                                name: "Activity Deliverables By Status",
                                id: "activityDeliverableByStatus",
                                data: data.activityDeliverableByStatus
                            },
                            {
                                name: "Task Deliverables By Status",
                                id: "taskDeliverableByStatus",
                                data: data.taskDeliverableByStatus
                            }
                        ];
                        vm.reqByStatus = data.reqByStatus;
                        vm.openTasks = data.openTasks;
                        vm.openProjects = data.openProjects;
                        vm.reqByTypes = data.reqByTypes;
                        vm.reqCounts = data.reqCounts;
                        vm.activityStatusCount = data.activityStatusCount;
                        vm.projects = data.projects;
                        vm.totalProjectsCounts = data.totalProjectsCounts;
                        vm.totalActivityStatusCounts = data.totalActivityStatusCounts;
                        $timeout(function () {
                            initCharts();
                        }, 200)
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
                    if (type == 'openTask') maximizeOpenTasksChart(windowContent);
                    if (type == 'projectActivity') maximizeProjectActivityChart(windowContent);
                    if (type == 'projectStatusByProgram') addProjectStatusByProgramChart(windowContent);
                    if (type == 'graph-container') loadDrillDownChart(windowContent, 'maximizeOpenTasks');
                }, 200)

            }

            function maximizeOpenTasksChart(windowContent) {
                var options = {
                    series: [{
                        name: "Count",
                        data: vm.openTasks
                    }],
                    xaxis: {
                        categories: vm.openProjects
                    },
                    chart: {
                        height: windowContent,
                        type: 'bar',
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
                            colors: ['#636e7b'],
                            fontSize: '10px',
                            fontWeight: 'normal'
                        },
                        offsetX: 20
                    },
                    tooltip: {
                        enabled: false
                    },
                    legend: {
                        show: false
                    }
                };

                vm.windowChart = new ApexCharts(document.querySelector("#maximizeOpenTasks"), options);
                vm.windowChart.render();
                charts.push(vm.windowChart);
            }

            function maximizeProjectActivityChart(windowContent) {
                var options = {
                    series: [{
                        name: 'Pending',
                        data: vm.activityStatusCount.pendingCounts
                    }, {
                        name: 'Inprogress',
                        data: vm.activityStatusCount.inProgressCounts
                    }, {
                        name: 'Finished',
                        data: vm.activityStatusCount.finishedCounts
                    }, {
                        name: 'Overdue',
                        data: vm.activityStatusCount.overDueCounts
                    }],
                    chart: {
                        type: 'bar',
                        height: windowContent - 100,
                        toolbar: {show: false}
                    },
                    plotOptions: {
                        bar: {
                            horizontal: false,
                            columnWidth: '40%'

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
                        categories: vm.projects,
                        position: 'bottom',
                        labels:{
                            trim: true
                        },
                        axisBorder: {
                            show: false
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
                vm.windowChart = new ApexCharts(document.querySelector("#maximizeOpenTasks"), options);
                vm.windowChart.render();
                charts.push(vm.windowChart);
            }

            $scope.hideWidgetWindow = hideWidgetWindow;
            function hideWidgetWindow() {
                var modal = document.getElementById("widget-window");
                modal.style.display = "none";
                vm.windowChart.destroy();
            }


            /*
             * Drill down Charts
             * */


            function initDrillDownChart(allDataGroupsValues, myChart) {

                var option;
                var allOptionsWithItemGroupId = {};

                angular.forEach(allDataGroupsValues, function (dataGroup, index) {
                    var dataGroupId = dataGroup.dataGroupId;
                    var finalLevel = dataGroup.finalLevel;
                    var color = dataGroup.colorByLevel;
                    var legend = dataGroup.legend;
                    var colorBy = "series";
                    var stage = dataGroup.stage;

                    var data = dataGroup.data;
                    var optionWithItemGroupId = {
                        title: {
                            text: legend,
                            left: 'center',
                            rich: {},
                            textStyle: {
                                color: color,
                                fontSize: 14
                            }
                        },
                        tooltip: {
                            show: true,
                            showContent: true,
                            triggerOn: 'mousemove',
                            trigger: 'axis'


                        },
                        dataZoom: [
                            {
                                show: true,
                                realtime: true,
                            },
                            {
                                type: 'inside',
                                realtime: true,
                            }
                        ],
                        xAxis: {
                            type: 'category',
                            axisLabel: {
                                // force to display all labels
                                interval: 0,
                                rotate: 30,
                                formatter: function (d, index) {
                                    if (d.length >= 13) {
                                        d.substring(0, 3);
                                        d = d.substring(0, 3) + "...";
                                    }
                                    return "" + d;
                                }
                            }
                        },
                        grid: {containLabel: true},
                        yAxis: {},
                        // dataGroupId: dataGroupId,
                        animationDurationUpdate: 500,
                        //color: ['#008ffb'],
                        series: {
                            type: 'bar',
                            barWidth: 15,
                            itemStyle: {normal: {color: color}},
                            dataGroupId: dataGroupId,
                            final: finalLevel,
                            stage: stage,
                            colorBy: colorBy,
                            silent: stage,
                            encode: {
                                x: 0,
                                y: 1,
                                itemGroupId: 2
                            },
                            data: data,
                            universalTransition: {
                                enabled: true,
                                divideShape: 'clone'
                            }
                        },
                        /*graphic: [
                         {
                         type: 'text',
                         right: 50,
                         top: 20,
                         style: {
                         text: 'Back',
                         fontSize: 14,
                         },
                         onclick: function () {
                         goBack();
                         }
                         }
                         ]*/
                        graphic: [
                            {
                                id: 'img',
                                type: 'image',
                                right: 50,
                                top: 1,
                                style: {
                                    image: 'app/assets/images/back.png',
                                    width: 20,
                                    height: 20,
                                    opacity: 0.5
                                },
                                onclick: function () {
                                    goBack();
                                }
                            }
                        ]
                    };
                    allOptionsWithItemGroupId[dataGroupId] = optionWithItemGroupId;
                });
                myChart.hideLoading();


                // A stack to remember previous dataGroupsId
                var dataGroupIdStack = [];

                function goForward(dataGroupId) {
                    dataGroupIdStack.push(myChart.getOption().series[0].dataGroupId); // push current dataGroupId into stack.
                    if (myChart.getOption().series[0].final != undefined && myChart.getOption().series[0].final == "Final") {
                        allOptionsWithItemGroupId[dataGroupId].series.itemStyle.normal.color = null;
                        allOptionsWithItemGroupId[dataGroupId].series.colorBy = "data";
                    }

                    myChart.setOption(allOptionsWithItemGroupId[dataGroupId], false);  // setOption twice? Yeah, it is dirty.
                    myChart.setOption(allOptionsWithItemGroupId[dataGroupId], true); // Note: the parameter notMerge is set true
                    myChart.hideLoading();
                };


                function goBack() {
                    if (dataGroupIdStack.length === 0) {
                        console.log('Already in root dataGroup!');
                    } else {
                        console.log('Go back to previous level');
                        if (dataGroupIdStack.length === 1) {
                            allOptionsWithItemGroupId[dataGroupIdStack].graphic[0] = null;
                        }
                        myChart.setOption(allOptionsWithItemGroupId[dataGroupIdStack.pop()], true); // Note: the parameter notMerge is set true

                    }
                };


                option = allOptionsWithItemGroupId['1'];  // The initial option is the root data option

                myChart.on('click', 'series.bar', function (event) {
                    myChart.showLoading({
                        text: 'Get the data',
                        effect: 'whirling'
                    });

                    if (event.data[2]) {  // If current params is not belong to the "childest" data, then it has data[2]
                        const dataGroupId = event.data[2];
                        goForward(dataGroupId);
                    }
                });

                window.addEventListener('resize', function () {
                    myChart.resize();
                })

                option.graphic[0] = null;
                option && myChart.setOption(option);

            }


            function loadDrillDownChart(height, type) {
                var allDataGroups = [];
                var dom = document.getElementById(type);
                var myChart;
                $timeout(function () {
                    myChart = echarts.init(dom, null, {
                        renderer: 'canvas',
                        useDirtyRect: false,
                        height: height
                    });
                    myChart.showLoading({
                        text: 'Loading data ...',
                        effect: 'whirling'
                    });
                    AnalyticsService.getProgramDrillDownReport().then(
                        function (data) {
                            allDataGroups = data;
                            $timeout(function () {

                                initDrillDownChart(allDataGroups, myChart)
                            }, 200)
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }, 1000)


            }


            (function () {
                loadDrillDownChart(280, 'graph-container');
                loadProjectStatusByProgramDashboardCounts();
                loadProjectDashboardCounts();
            })();

        }
    }
)
;