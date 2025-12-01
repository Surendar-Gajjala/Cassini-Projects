define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        // 'app/shared/services/core/mbomService',
        // 'app/shared/services/core/bopService',
        'app/shared/services/core/productionOrderService',
        'app/desktop/modules/mes/productionOrder/all/productionOrderGanttEditor'
    ],
    function (module) {
        module.controller('AllProductionOrderController', AllProductionOrderController);

        function AllProductionOrderController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $compile, uiCalendarConfig,
                                              $stateParams, $cookies, $sce, ProductionOrderService, PrGanttEditor) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.showPrGantt = false;

            var productionGantt = PrGanttEditor.getGanttInstance();
            vm.newProductionOrder = newProductionOrder;
            vm.productionOrders = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;

            vm.searchText = null;
            vm.filterSearch = null;

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.filters = {
                searchQuery: null,
                schedule: false,
                unSchedule: false
            };
            $scope.freeTextQuery = null;

            vm.productionOrders = angular.copy(pagedResults);

            vm.uiConfig = {
                calendar: {
                    //height: auto,
                    editable: true,
                    header: {
                        left: '',
                        center: 'title',
                        right: 'today prev,next'
                    },
                    eventLimit: true,
                    eventClick: alertOnEventClick,
                    eventDrop: alertOnDrop,
                    eventResize: alertOnResize,
                    eventRender: function (event, element, view) {
                        element.attr({
                            'title': event.productionOrder.plannedStartDate + " - " + event.productionOrder.plannedFinishDate + ' ( ' + event.productionOrder.lifeCyclePhase.phase + ' )'
                        });
                        $compile(element)($scope);
                    }
                }
            };
            vm.eventSources = [];
            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            $scope.newProductionOrder = parsed.html($translate.instant("NEW_PRODUCTION_ORDER")).html();
            var newProductionOrderHeading = parsed.html($translate.instant("NEW_PRODUCTION_ORDER")).html();


            function newProductionOrder() {
                var options = {
                    title: newProductionOrderHeading,
                    template: 'app/desktop/modules/mes/productionOrder/new/newProductionOrderView.jsp',
                    controller: 'NewProductionOrderController as newProductionOrderVm',
                    resolve: 'app/desktop/modules/mes/productionOrder/new/newProductionOrderController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: 'create', broadcast: 'app.productionOrder.new'}
                    ],
                    callback: function (productionOrder) {
                        $timeout(function () {
                            loadProductionOrder();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }


            function nextPage() {
                if (vm.productionOrders.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadProductionOrder();
                }
            }

            function previousPage() {
                if (vm.productionOrders.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadProductionOrder();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadProductionOrder();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadProductionOrder();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.productionOrders = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadProductionOrder();
            }

            vm.orders = [];
            function loadProductionOrder() {
                $rootScope.showBusyIndicator($('.view-container'));
                vm.loading = true;
                ProductionOrderService.getAllProductionOrders(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.productionOrders = data;
                        loadProductionOrderDates();
                        prGantt_data.data = [];
                        vm.orders = [];
                        angular.forEach(vm.productionOrders.content, function (pr) {
                            pr.duration = 0;
                            pr.start_date = "";
                            pr.end_date = "";
                            pr.text = pr.number;
                            if (pr.plannedFinishDate != null && pr.plannedStartDate != null) {
                                pr.start_date = pr.plannedStartDate;
                                pr.end_date = pr.plannedFinishDate;
                                var startDate = moment(pr.plannedStartDate, $rootScope.applicationDateSelectFormat);
                                var finishDate = moment(pr.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                                var diffTime = Math.abs(finishDate - startDate);
                                var diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                                pr.duration = diffDays;
                                pr.unscheduled = false;
                            } else {
                                pr.start_date = null;
                                pr.end_date = null;
                                pr.unscheduled = true;
                                pr.duration = 0;
                            }
                            prGantt_data.data.push(pr);
                        });
                        //vm.uiConfig.calendar.height = $('.view-content').outerHeight() - 20;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }
                )

            }

            vm.renderView = false;
            vm.showCalender = false;
            vm.showCalenderView = showCalenderView;
            function showCalenderView() {
                vm.orders = [];
                ProductionOrderService.getAllCalenderProductionOrders().then(
                    function (data) {
                        angular.forEach(data, function (pr) {
                            var date = null;
                            var plannedFinishDate = null;
                            if (pr.plannedStartDate != null && pr.plannedFinishDate != null) {
                                date = moment(pr.plannedStartDate, $rootScope.applicationDateSelectFormat);
                                plannedFinishDate = moment(pr.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                            } else if (pr.plannedStartDate != null && pr.plannedFinishDate == null) {
                                date = moment(pr.plannedStartDate, $rootScope.applicationDateSelectFormat);
                                plannedFinishDate = moment(pr.plannedStartDate, $rootScope.applicationDateSelectFormat);
                            } else if (pr.plannedStartDate == null && pr.plannedFinishDate != null) {
                                date = moment(pr.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                                plannedFinishDate = moment(pr.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                            }
                            var color = "#f0ad4e";
                            var background = "#fff";
                            if (pr.lifeCyclePhase.phaseType == 'REVIEW') {
                                background = "#c9f7f5";
                                color = "#1bc5bd";
                            } else if (pr.lifeCyclePhase.phaseType == 'RELEASED') {
                                background = "#e1f0ff";
                                color = "#3699ff";
                            }
                            var order = {
                                title: pr.name,
                                start: new Date(date._d.getFullYear(), date._d.getMonth(), date._d.getDate()),
                                end: new Date(plannedFinishDate._d.getFullYear(), plannedFinishDate._d.getMonth(), plannedFinishDate._d.getDate()),
                                productionOrder: pr,
                                color: color,
                                textColor: background
                            };
                            vm.orders.push(order);
                        });
                        vm.eventSources = [vm.orders];
                        $scope.$evalAsync();
                        vm.showCalender = true;
                        vm.renderView = true;
                        $timeout(function () {
                            $(".fc-today-button")[0].click();
                        }, 200);
                    }
                );
            }

            vm.showProductionOrder = showProductionOrder;

            function showProductionOrder(productionOrder) {
                $state.go('app.mes.productionOrder.details', {
                    productionOrderId: productionOrder.id,
                    tab: 'details.basic'
                });
            }

            vm.showMBOM = showMBOM;
            function showMBOM(order) {
                $state.go('app.mes.mbom.details', {mbomId: order.mbomRevision, tab: 'details.basic'});
            }

            vm.showBOP = showBOP;
            function showBOP(order) {
                $state.go('app.mes.bop.details', {bopId: order.bopRevision, tab: 'details.basic'});
            }


            vm.showShift = showShift;
            function showShift(productionOrder) {
                $state.go('app.mes.masterData.shift.details', {
                    shiftId: productionOrder.shift,
                    tab: 'details.basic'
                });
            }

            var deleteDialogTitle = parsed.html($translate.instant("DELETE_PRODUCTION_ORDER")).html();
            var deleteOrderDialogMessage = parsed.html($translate.instant("DELETE_PRODUCTION_ORDER_DIALOG_MESSAGE")).html();
            var orderDeletedSuccessMessage = parsed.html($translate.instant("PRODUCTION_ORDER_DELETED_MESSAGE")).html();

            vm.deleteProductionOrder = deleteProductionOrder;
            function deleteProductionOrder(order) {
                var options = {
                    title: deleteDialogTitle,
                    message: deleteOrderDialogMessage.format(order.name),
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        ProductionOrderService.deleteProductionOrder(order.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(orderDeletedSuccessMessage);
                                loadProductionOrder();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            var width_settings = {};

            vm.showProductionGantt = showProductionGantt;

            function showProductionGantt() {
                loadWidths();
                addContextMenu();
                initGantt();
            }

            vm.showTableView = showTableView;
            function showTableView() {
                vm.ganttEnable = false;
                $rootScope.showPrGantt = false;
                vm.filters.schedule = false;
                vm.filters.unSchedule = false;
                loadProductionOrder();
            }

            var prGantt_data = {
                data: [],
                links: [],
                persons: [],
                prStartDate: "",
                prFinishDate: "",
                width: {},
                workingDays: null,
                holidays: [],
                hasPermission: false
            };

            var prDefault_width = {
                text: 150,
                name: 150,
                typeName: 150,
                duration: 80,
                description: 150,
                start_date: 200,
                end_date: 200,
                grid: 800,
                showPrGantt: false
            };

            function loadProductionOrderDates() {
                ProductionOrderService.getProductionOrdersMinAndMaxDates().then(
                    function (data) {
                        prGantt_data.prStartDate = data.minDate;
                        var finishDate = moment(data.maxDate, $rootScope.applicationDateSelectFormat);
                        var addedPeriod = finishDate.add(1, 'days').format($rootScope.applicationDateSelectFormat);
                        prGantt_data.prFinishDate = addedPeriod;
                    }
                )
            }

            vm.ganttEnable = false;
            function initGantt() {
                $timeout(function () {
                    vm.ganttEnable = true;
                    PrGanttEditor.initPrEditor('production_gantt_here', prGantt_data, $scope, $rootScope);
                    //PrGanttEditor.expandAll();
                    $rootScope.hideBusyIndicator();
                }, 1000)
            }

            vm.toggleGantt = toggleGantt;
            vm.toggleGrid = PrGanttEditor.toggleGrid;

            function toggleGantt() {
                $rootScope.showPrGantt = !$rootScope.showPrGantt;
                productionGantt.config.show_chart = $rootScope.showPrGantt;
                productionGantt.render();
            }

            vm.setScales = setScales;
            function setScales(scale) {
                vm.scale = scale;
                productionGantt.config.scale_unit = scale;
                productionGantt.render();
            }

            $scope.$on('app.pr.hidePrGantt', function () {
                $timeout(function () {
                    $rootScope.showBusyIndicator();
                    $rootScope.showPrGantt = false;
                    width_settings.showPrGantt = $rootScope.showPrGantt;
                    $window.localStorage.setItem("prWidthSettings", JSON.stringify(width_settings));
                    prGantt_data.width.showPrGantt = $rootScope.showPrGantt;
                    initGantt();
                }, 200);
            });

            $scope.$on('app.pr.showPrGantt', function () {
                $timeout(function () {
                    $rootScope.showBusyIndicator();
                    $rootScope.showPrGantt = true;
                    width_settings.showPrGantt = $rootScope.showPrGantt;
                    $window.localStorage.setItem("prWidthSettings", JSON.stringify(width_settings));
                    prGantt_data.width.showPrGantt = $rootScope.showPrGantt;
                    initGantt();
                }, 200);
            });

            $scope.$on('app.pr.saveWidths', saveWidths);
            function saveWidths(event, args) {
                var columnName = args.columnName;
                var newWidth = args.newWidth;
                if (columnName == "number") width_settings.number = newWidth;
                if (columnName == "name") width_settings.name = newWidth;
                if (columnName == "typeName") width_settings.typeName = newWidth;
                if (columnName == "description") width_settings.description = newWidth;
                if (columnName == "assignedTo") width_settings.assignedTo = newWidth;
                if (columnName == "plannedStartDate") width_settings.plannedStartDate = newWidth;
                if (columnName == "plannedFinishDate") width_settings.plannedFinishDate = newWidth;
                if (columnName == "grid") width_settings.grid = newWidth;
                $window.localStorage.setItem("prWidthSettings", JSON.stringify(width_settings));
            }

            function loadWidths() {
                vm.width = {};
                var widthSetting = JSON.parse($window.localStorage.getItem("prWidthSettings"));
                if (widthSetting != null) {
                    vm.width.number = widthSetting.number != null ? widthSetting.number : prDefault_width.number;
                    vm.width.name = widthSetting.name != null ? widthSetting.name : prDefault_width.name;
                    vm.width.typeName = widthSetting.typeName != null ? widthSetting.typeName : prDefault_width.typeName;
                    vm.width.description = widthSetting.description != null ? widthSetting.description : prDefault_width.description;
                    vm.width.assignedTo = widthSetting.assignedTo != null ? widthSetting.assignedTo : prDefault_width.assignedTo;
                    vm.width.plannedStartDate = widthSetting.plannedStartDate != null ? widthSetting.plannedStartDate : prDefault_width.plannedStartDate;
                    vm.width.plannedFinishDate = widthSetting.plannedFinishDate != null ? widthSetting.plannedFinishDate : prDefault_width.plannedFinishDate;
                    vm.width.grid = widthSetting.grid != null ? widthSetting.grid : prDefault_width.grid;
                    prGantt_data.width = vm.width;
                } else {
                    prGantt_data.width = prDefault_width;
                }
            }

            function addContextMenu() {
                // enables context menu
                var $contextMenu = $("#contextMenu");
                $("#production_gantt_here").on("contextmenu", function (e) {
                    var tasks = productionGantt.getSelectedTasks();
                    if (tasks.length > 0) {
                        $contextMenu.css({
                            display: "block",
                            left: e.pageX,
                            top: e.pageY - 210
                        });
                    }
                    return false;
                });

                // disables context menu
                $contextMenu.on("click", "a", function () {
                    $contextMenu.hide();
                });
                $("#production_gantt_here").on("click", function (e) {
                    $contextMenu.hide();
                });
            }

            vm.savePrGantt = savePrGantt;
            function savePrGantt() {
                $rootScope.showBusyIndicator($('.view-container'));
                vm.productionList = PrGanttEditor.saveGantt();
                angular.forEach(vm.productionList.data, function (data) {
                    data.plannedStartDate = data.start_date.slice(0, 10).split("-").join($rootScope.applicationDateSelectFormatDivider);
                    data.plannedFinishDate = data.end_date.slice(0, 10).split("-").join($rootScope.applicationDateSelectFormatDivider);
                })
                ProductionOrderService.savePrGanttObjects(vm.productionList.data).then(
                    function (data) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showSuccessMessage("Gantt data save successfully");
                        loadProductionOrder();
                        initGantt();
                    }
                )
            }

            vm.selectScheduleType = selectScheduleType;

            function selectScheduleType(type) {
                $rootScope.showBusyIndicator($('.view-container'));
                if (type == "ALL") {
                    document.getElementById("all").checked = true;
                    document.getElementById("schedule").checked = false;
                    document.getElementById("unSchedule").checked = false;
                    vm.filters.schedule = false;
                    vm.filters.unSchedule = false;
                    loadProductionOrder();
                    initGantt();
                }
                else if (type == "SCHEDULE") {
                    document.getElementById("all").checked = false;
                    document.getElementById("schedule").checked = true;
                    document.getElementById("unSchedule").checked = false;
                    vm.filters.schedule = true;
                    vm.filters.unSchedule = false;
                    loadProductionOrder();
                    initGantt();
                } else if (type == "UNSCHEDULE") {
                    document.getElementById("all").checked = false;
                    document.getElementById("schedule").checked = false;
                    document.getElementById("unSchedule").checked = true;
                    vm.filters.unSchedule = true;
                    vm.filters.schedule = false;
                    loadProductionOrder();
                    initGantt();
                }
                $rootScope.hideBusyIndicator();
            }

            /*-------------------------------------------------------------------------------------*/
            /* alert on eventClick */
            function alertOnEventClick(date, jsEvent, view) {
                $scope.alertMessage = (date.title + ' was clicked ');
            }

            /* alert on Drop */
            function alertOnDrop(event, delta, revertFunc, jsEvent, ui, view) {
                $scope.alertMessage = ('Event Droped to make dayDelta ' + delta);
            }

            /* alert on Resize */
            function alertOnResize(event, delta, revertFunc, jsEvent, ui, view) {
                $scope.alertMessage = ('Event Resized to make dayDelta ' + delta);
            }

            /* Render Tooltip */
            $scope.eventRender = function (event, element, view) {
                element.attr({
                    'title': event.title,
                    'tooltip-append-to-body': true
                });
                $compile(element)($scope);
            };

            function resizeView() {
                vm.showCalender = false;
                vm.renderView = false;
                $timeout(function () {
                    vm.uiConfig = {
                        calendar: {
                            height: $('.view-content').outerHeight() - 20,
                            editable: true,
                            header: {
                                left: '',
                                center: 'title',
                                right: 'today prev,next'
                            },
                            eventLimit: true,
                            eventClick: alertOnEventClick,
                            eventDrop: alertOnDrop,
                            eventResize: alertOnResize
                        }
                    };
                    vm.eventSources = [vm.orders];
                    showCalenderView();
                }, 1000);
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    loadProductionOrder();
                    /*$(window).resize(function () {
                     resizeView();
                     });*/
                });
            })();

        }
    }
);