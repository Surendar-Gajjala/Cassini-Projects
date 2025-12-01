define([
        'app/desktop/modules/mes/mes.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],
    function (module) {
        module.factory('PrGanttEditor', PrGanttEditor);

        function PrGanttEditor($rootScope) {

            var ganttScope = null;
            var prStartDate = null;
            var prFinishDate = null;
            gantt.config.keyboard_navigation_cells = true;
            gantt.config.keyboard_navigation = true;
            var productionGantt = gantt;

            function initPrEditor(container, data, $scope, $rootScope) {
                gridWidth = data.width.grid;
                ganttScope = $scope;
                $scope.viewTask = viewTask;
                var editors = {
                    text: {type: "text", map_to: "text"},
                    number: {type: "text", map_to: "name"},
                    typeName: {type: "text", map_to: "typeName"},
                    description: {type: "text", map_to: "description"},
                    assignedTo: {type: "text", map_to: "assignedTo"},
                    start_date: {type: "date", map_to: "start_date"},
                    end_date: {type: "date", map_to: "end_date"},
                    duration: {type: "number", map_to: "duration", min: 0, max: 100},
                    //assignedTo: {map_to: "assignedTo", type: "select", options: data.persons},
                };

                productionGantt.config.keyboard_navigation_cells = true;
                productionGantt.config.keyboard_navigation = true;
                productionGantt.config.auto_scheduling = true;
                productionGantt.config.auto_scheduling_strict = true;
                productionGantt.config.row_height = 25;
                productionGantt.config.fit_tasks = true;
                productionGantt.config.show_unscheduled = true;
                productionGantt.config.auto_types = true;
                productionGantt.config.open_tree_initially = true;
                productionGantt.config.order_branch = "marker";
                productionGantt.config.show_errors = false;
                productionGantt.config.undo = true;
                productionGantt.config.duration_unit = "day";

                if (data.workingDays == null && data.holidays == null) {
                    productionGantt.config.work_time = false;
                } else if (data.workingDays != null && data.holidays.length == 0) {
                    setWeekends(data.workingDays);
                } else if (data.workingDays != null && data.holidays.length > 0) {
                    setWeekends(data.workingDays);
                    setHolidays(data.holidays);
                } else if (data.workingDays == null && data.holidays.length > 0) {
                    setHolidays(data.holidays);
                }

                prStartDate = data.prStartDate.split($rootScope.applicationDateSelectFormatDivider);
                prFinishDate = data.prFinishDate.split($rootScope.applicationDateSelectFormatDivider);
                if ($rootScope.applicationDateFormat == "MM-dd-yyyy, HH:mm:ss") {
                    productionGantt.config.api_date = "%m-%d-%Y %H:%i";
                    productionGantt.config.date_scale = "%M %d";
                    productionGantt.config.xml_date = "%m-%d-%Y %H:%i";
                    productionGantt.config.task_date = "%m %F %Y";
                    productionGantt.config.date_grid = "%Y-%d-%m";
                    productionGantt.config.start_date = new Date(parseInt(prStartDate[2]), parseInt(prStartDate[0] - 1), parseInt(prStartDate[1]));
                    productionGantt.config.end_date = new Date(parseInt(prFinishDate[2]), parseInt(prFinishDate[0] - 1), parseInt(prFinishDate[1]));
                } else {
                    productionGantt.config.api_date = "%d-%m-%Y %H:%i";
                    productionGantt.config.date_scale = "%d %M";
                    productionGantt.config.xml_date = "%d-%m-%Y %H:%i";
                    productionGantt.config.task_date = "%d %F %Y";
                    productionGantt.config.date_grid = "%Y-%m-%d";
                    productionGantt.config.start_date = new Date(parseInt(prStartDate[2]), parseInt(prStartDate[1] - 1), parseInt(prStartDate[0]));
                    productionGantt.config.end_date = new Date(parseInt(prFinishDate[2]), parseInt(prFinishDate[1] - 1), parseInt(prFinishDate[0]));
                }

                if (data.width.showPrGantt) {
                    productionGantt.config.layout = {
                        css: "gantt_container",
                        cols: [
                            {
                                width: gridWidth,
                                min_width: gridWidth,
                                rows: [
                                    {view: "grid", scrollX: "gridScroll", scrollable: true, scrollY: "scrollVer"},
                                    {view: "scrollbar", id: "gridScroll", group: "horizontal"}
                                ]
                            },
                            {resizer: true, width: 1},
                            {
                                rows: [
                                    {view: "timeline", scrollX: "scrollHor", scrollY: "scrollVer"},
                                    {view: "scrollbar", id: "scrollHor", group: "horizontal"}
                                ]
                            },
                            {view: "scrollbar", id: "scrollVer"}
                        ]
                    };
                } else {
                    productionGantt.config.layout = {
                        css: 'gantt_container',
                        cols: [
                            {
                                rows: [
                                    {view: 'grid', scrollX: 'gridScrollX', scrollY: 'scrollVer'},
                                    {view: 'scrollbar', id: 'gridScrollX', group: 'horizontal'},
                                ]
                            },
                            {
                                width: 1,
                                rows: [
                                    {view: 'timeline', scrollX: 'scrollHor', scrollY: 'scrollVer'},
                                    {view: 'scrollbar', id: 'scrollHor', group: 'horizontal'},
                                ]
                            },
                            {view: 'scrollbar', id: 'scrollVer'}
                        ]
                    };
                }

                var gridDateToStr = productionGantt.date.date_to_str($rootScope.ganttDateFormat);
                productionGantt.templates.grid_date_format = function (date, column) {
                    if (column === "end_date") {
                        return gridDateToStr(date /*new Date(date.valueOf() - 1)*/);
                    } else {
                        return gridDateToStr(date);
                    }

                };

                productionGantt.config.columns = [

                    {
                        name: "text",
                        label: "Number",
                        width: data.width.text,
                        align: "left",
                        editor: editors.text,
                        resize: true
                    },
                    {
                        name: "name",
                        label: "Name",
                        tree: true,
                        width: data.width.name,
                        align: "center",
                        editor: editors.name,
                        resize: true
                    },
                    {
                        name: "typeName",
                        label: "Type",
                        width: data.width.typeName,
                        align: "center",
                        editor: editors.typeName,
                        resize: true
                    },
                    {
                        name: "description",
                        label: "Description",
                        width: data.width.description,
                        align: "center",
                        editor: editors.description,
                        resize: true
                    },
                    {
                        name: "assignedTo",
                        label: "Assigned To",
                        width: data.width.assignedTo,
                        align: "center",
                        editor: editors.assignedTo,
                        resize: true,
                        template: function (item) {
                            if (item.id != null && item.assignedTo != null) {
                                return item.assignedToName;
                            }
                            return "";
                        }
                    },
                    {
                        name: "start_date",
                        label: "PlannedStartDate",
                        width: data.width.start_date,
                        align: "center",
                        editor: editors.start_date,
                        resize: true
                    },
                    {
                        name: "end_date",
                        label: "PlannedFinishDate",
                        width: data.width.end_date,
                        align: "center",
                        editor: editors.end_date,
                        resize: true
                    },
                    {
                        name: "duration",
                        label: "Duration",
                        width: data.width.duration,
                        align: "center",
                        editor: editors.duration,
                        resize: true
                    },


                    {
                        name: "actions", label: "Actions", width: 100, max_width: 100, align: "center",
                        template: function (obj) {
                            if (obj.start_date != null && obj.end_date != null) {
                                var isViewTask = "<i class='fa fa-info-circle' style='cursor: pointer;color: darkgrey;position: absolute;right: 30px;' onclick='angular.element(this).scope().viewTask(\"" + obj.id + "," + obj.number + "\");' title='View details'></i> ";
                                return isViewTask;
                            } else {
                                return "";
                            }
                        }
                    }


                ];

                productionGantt.attachEvent("onBeforeTaskMove", function (id, parent, tindex) {
                    var task = productionGantt.getTask(id);
                    if (task.parent != parent)
                        return false;
                    return true;
                });

                productionGantt.attachEvent("onLinkValidation", function (link) {
                    if (link.type == 0 && checkLink(link)) {
                        productionGantt.addLink(link);
                        $scope.$broadcast('app.pr.ganttUpdated');
                    }
                });

                productionGantt.attachEvent("onAfterLinkDelete", function (id, item) {
                    var sourceTask = productionGantt.getTask(item.source).text;
                    var targetTask = productionGantt.getTask(item.target).text;
                    $scope.$broadcast('app.pr.linkDeleted', {source: sourceTask, target: targetTask});
                });

                productionGantt.attachEvent("onColumnResizeEnd", function (index, column, new_width) {
                    $scope.$broadcast('app.pr.saveWidths', {columnName: column.name, newWidth: new_width});
                });

                var gridWidth = null;
                productionGantt.attachEvent("onGridResize", function (old_width, new_width) {
                    gridWidth = new_width;
                });

                function compare_input(id, filter_data) {
                    var match = false;
                    // check children's text
                    if (productionGantt.hasChild(id)) {
                        productionGantt.eachTask(function (child_object) {
                            if (compare_input(child_object.id, filter_data)) match = true;
                        }, id);
                    }
                    // check task's text
                    if (productionGantt.getTask(id).text.toLowerCase().indexOf(filter_data.toLowerCase()) >= 0) {
                        productionGantt.getTask(id).text = "<span style='background-color:#a8d1ff'>" + productionGantt.getTask(id).text + "</span>";
                        match = true;
                    }
                    if (productionGantt.getTask(id).person != null && productionGantt.getTask(id).person.fullName.toLowerCase().indexOf(filter_data.toLowerCase()) >= 0) {

                        match = true;
                    }


                    return match;
                }


                function updateFilterHTMLValue(value, filterText) {
                    if (value == null) {
                        return value;
                    }

                    var filterIndex = value.toLowerCase().indexOf(filterText);
                    if (filterIndex < 0) {
                        return null;
                    }
                    return value.substr(0, filterIndex)
                        + "<span class='updColor'>"
                        + value.substr(filterIndex, filterText.length)
                        + "</span>"
                        + value.substr(filterIndex + filterText.length, value.length - (filterIndex + filterText.length));
                }

                productionGantt.attachEvent("onBeforeTaskDisplay", function (id, task) {
                    return true;

                });

                productionGantt.attachEvent("onGridResizeEnd", function (old_width, new_width) {
                    $scope.$broadcast('app.pr.saveWidths', {columnName: 'grid', newWidth: gridWidth});
                });

                productionGantt.attachEvent("onGanttReady", function () {
                    var tooltips = productionGantt.ext.tooltips;
                    var inlineEditors = productionGantt.ext.inlineEditors;

                    tooltips.detach("[" + productionGantt.config.task_attribute + "]:not(.gantt_task_row)");
                    tooltips.tooltipFor({
                        selector: ".gantt_task_link",
                        html: function (event, node) {
                            var linkId = node.getAttribute(productionGantt.config.link_attribute);
                            if (linkId) {
                                var link = productionGantt.getLink(linkId);
                                var from = productionGantt.getTask(link.source);
                                var to = productionGantt.getTask(link.target);

                                return [
                                    "<b>Link:</b> " + linkTypeToString(link.type),
                                    "<b>From: </b> " + from.text,
                                    "<b>To: </b> " + to.text
                                ].join("<br>");
                            }
                        }
                    });

                    tooltips.tooltipFor({
                        selector: ".gantt_task_link",
                        html: function (event, node) {
                            var linkId = node.getAttribute(productionGantt.config.link_attribute);
                            if (linkId) {
                                var link = productionGantt.getLink(linkId);
                                var from = productionGantt.getTask(link.source);
                                var to = productionGantt.getTask(link.target);

                                return [
                                    "<b>Link:</b> " + linkTypeToString(link.type),
                                    "<b>From: </b> " + from.text,
                                    "<b>To: </b> " + to.text
                                ].join("<br>");
                            }
                        }
                    });

                    tooltips.tooltipFor({
                        selector: ".gantt_task_drag",
                        html: function (event, node) {
                            return [
                                "<b>Drag to adjust the dates</b> "
                            ].join("<br>");
                        }
                    });

                    tooltips.tooltipFor({
                        selector: ".gantt_task_cell.week_end",
                        html: function (event, node) {
                            if (productionGantt.config.scale_unit == 'day') {
                                return [
                                    "<b>Non-Working day</b> "
                                ].join("<br>");
                            }
                        }
                    });

                    inlineEditors.attachEvent("onBeforeEditStart", function (state) {
                        var task = productionGantt.getTask(state.id);
                        return true;
                    });
                });

                function checkLink(link) {
                    var links = productionGantt.getLinks();
                    var flag = true;
                    $.each(links, function (i, el) {
                        if (parseInt(el.source) == parseInt(link.source) && parseInt(el.target) == parseInt(link.target) && flag)
                            flag = false;
                    });
                    return flag;
                }

                function linkTypeToString(linkType) {
                    switch (linkType) {
                        case productionGantt.config.links.start_to_start:
                            return "Start to start";
                        case productionGantt.config.links.start_to_finish:
                            return "Start to finish";
                        case productionGantt.config.links.finish_to_start:
                            return "Finish to start";
                        case productionGantt.config.links.finish_to_finish:
                            return "Finish to finish";
                        default:
                            return ""
                    }
                }

                productionGantt.config.scale_unit = "day";
                productionGantt.config.subscales = [{unit: "month", step: 1, date: "%M, %Y"}];

                productionGantt.templates.progress_text = function (start, end, task) {
                    return "<span style='text-align:left; color:#fff;font-size: 12px;'>" + Math.round(task.progress * 100) + "% </span>";
                };

                productionGantt.init(container);
                productionGantt.parse(data);

                productionGantt.attachEvent("onTaskCreated", function (task) {

                });

                productionGantt.attachEvent("onAfterTaskUpdate", function (id, task) {

                    var taskStart = task.start_date;
                    var taskEnd = task.end_date;
                    var scaleStart = gantt.config.start_date;
                    var scaleEnd = gantt.config.end_date;
                    if (task.unscheduled && taskStart != null && taskStart != undefined) {
                        task.unscheduled = false;
                    }
                    if (scaleStart > taskEnd || scaleEnd < taskStart) {
                        task.start_date = startDate;
                        task.end_date = gantt.date.add(startDate, task.duration, 'day');
                    }

                    return true;
                });

                productionGantt.showLightbox = function (id) {

                };

                productionGantt.attachEvent("onAfterTaskMove", function (id, parent, tindex) {
                    $rootScope.ganttUpdated = true;
                });
            }

            var toggle_value = 1;

            function toggleGrid() {
                if (toggle_value != 1) {
                    ganttScope.$broadcast('app.pr.hidePrGantt');
                } else {
                    ganttScope.$broadcast('app.pr.showPrGantt');
                }
                toggle_value = toggle_value * (-1);
            }

            productionGantt.templates.rightside_text = function (start, end, task) {
                if (task.type == productionGantt.config.types.milestone) {
                    return task.text;
                }
                return "";
            };

            productionGantt.templates.task_cell_class = function (task, date) {
                if (!productionGantt.isWorkTime(date))
                    return "week_end";
                return "";
            };

            productionGantt.templates.grid_row_class = function (start, end, task) {
                return "";
            };

            productionGantt.eachTask(function (task) {
                task.$open = true;
            });


            productionGantt.attachEvent("onTaskCreated", function (task) {
                return true;
            });

            function saveGantt() {
                return productionGantt.serialize();
            }

            var cachedSettings = {};

            function saveConfig() {
                var config = productionGantt.config;
                cachedSettings = {};
                cachedSettings.scale_unit = config.scale_unit;
                cachedSettings.date_scale = config.date_scale;
                cachedSettings.step = config.step;
                cachedSettings.subscales = config.subscales;
                cachedSettings.template = productionGantt.templates.date_scale;
                cachedSettings.start_date = config.start_date;
                cachedSettings.end_date = config.end_date;
            }

            function restoreConfig() {
                applyConfig(cachedSettings);
            }

            function applyConfig(config, dates) {
                productionGantt.config.scale_unit = config.scale_unit;
                if (config.date_scale) {
                    productionGantt.config.date_scale = config.date_scale;
                    productionGantt.templates.date_scale = null;
                }
                else {
                    productionGantt.templates.date_scale = config.template;
                }

                productionGantt.config.step = config.step;
                productionGantt.config.subscales = config.subscales;

                if (dates) {
                    productionGantt.config.start_date = productionGantt.date.add(dates.start_date, 0, config.unit);
                    productionGantt.config.end_date = productionGantt.date.add(productionGantt.date[config.unit + "_start"](dates.end_date), 2, config.unit);
                } else {
                    productionGantt.config.start_date = config.start_date;
                    productionGantt.config.end_date = config.end_date;
                }
            }

            function zoomToFit() {
                var project = productionGantt.getSubtaskDates(),
                    areaWidth = productionGantt.$task.offsetWidth;
                for (var i = 0; i < scaleConfigs.length; i++) {
                    var columnCount = getUnitsBetween(project.start_date, project.end_date, scaleConfigs[i].unit, scaleConfigs[i].step);
                    if ((columnCount + 2) * productionGantt.config.min_column_width <= areaWidth) {
                        break;
                    }
                }
                if (i === scaleConfigs.length) {
                    i--;
                }
                applyConfig(scaleConfigs[i], project);
                productionGantt.render();
            }

            // get number of columns in timeline
            function getUnitsBetween(from, to, unit, step) {
                var start = new Date(from),
                    end = new Date(to);
                var units = 0;
                while (start.valueOf() < end.valueOf()) {
                    units++;
                    start = productionGantt.date.add(start, step, unit);
                }
                return units;
            }

            //Setting available scales
            var scaleConfigs = [
                // minutes
                {
                    name: "minute",
                    unit: "minute", step: 1, scale_unit: "hour", date_scale: "%H", subscales: [
                    {unit: "minute", step: 1, date: "%H:%i"}
                ]
                },
                // hours
                {
                    name: "hour",
                    unit: "hour", step: 1, scale_unit: "day", date_scale: "%j %M",
                    subscales: [
                        {unit: "hour", step: 1, date: "%H:%i"}
                    ]
                },
                // days
                {
                    name: "day",
                    unit: "day", step: 1, scale_unit: "month", date_scale: "%F",
                    subscales: [
                        {unit: "day", step: 1, date: "%j"}
                    ]
                },
                // weeks
                {
                    name: "week",
                    unit: "week", step: 1, scale_unit: "month", date_scale: "%F",
                    subscales: [
                        {
                            unit: "week", step: 1, template: function (date) {
                            var dateToStr = productionGantt.date.date_to_str($rootScope.ganttDateSelectFormat);
                            var endDate = productionGantt.date.add(productionGantt.date.add(date, 1, "week"), -1, "day");
                            return dateToStr(date) + " - " + dateToStr(endDate);
                        }
                        }
                    ]
                },
                // months
                {
                    name: "month",
                    unit: "month", step: 1, scale_unit: "year", date_scale: "%Y",
                    subscales: [
                        {unit: "month", step: 1, date: "%M"}
                    ]
                },
                // quarters
                {
                    name: "quarter",
                    unit: "month", step: 3, scale_unit: "year", date_scale: "%Y",
                    subscales: [
                        {
                            unit: "month", step: 3, template: function (date) {
                            var dateToStr = productionGantt.date.date_to_str("%M");
                            var endDate = productionGantt.date.add(productionGantt.date.add(date, 3, "month"), -1, "day");
                            return dateToStr(date) + " - " + dateToStr(endDate);
                        }
                        }
                    ]
                },
                // years
                {
                    name: "year",
                    unit: "year", step: 1, scale_unit: "year", date_scale: "%Y",
                    subscales: [
                        {
                            unit: "year", step: 5, template: function (date) {
                            var dateToStr = productionGantt.date.date_to_str("%Y");
                            var endDate = productionGantt.date.add(productionGantt.date.add(date, 5, "year"), -1, "day");
                            return dateToStr(date) + " - " + dateToStr(endDate);
                        }
                        }
                    ]
                },
                // decades
                {
                    name: "decade",
                    unit: "year", step: 10, scale_unit: "year", template: function (date) {
                    var dateToStr = productionGantt.date.date_to_str("%Y");
                    var endDate = productionGantt.date.add(productionGantt.date.add(date, 10, "year"), -1, "day");
                    return dateToStr(date) + " - " + dateToStr(endDate);
                },
                    subscales: [
                        {
                            unit: "year", step: 100, template: function (date) {
                            var dateToStr = productionGantt.date.date_to_str("%Y");
                            var endDate = productionGantt.date.add(productionGantt.date.add(date, 100, "year"), -1, "day");
                            return dateToStr(date) + " - " + dateToStr(endDate);
                        }
                        }
                    ]
                }
            ];

            function toggleMode(zoomed) {
                if (!zoomed) {
                    //Saving previous scale state for future restore
                    saveConfig();
                    zoomToFit();
                } else {
                    restoreConfig();
                    productionGantt.render();
                }
            }

            function clearAll() {
                productionGantt.clearAll();
            }

            function refresh() {
                productionGantt.refreshData();
            }

            function deleteTask(taskId) {
                productionGantt.deleteTask(taskId);
            }

            function setWeekends(workingDays) {
                productionGantt.config.work_time = true;
                productionGantt.config.correct_work_time = true;
                if (workingDays == 5) {
                    productionGantt.setWorkTime({day: 0, hours: false});
                    productionGantt.setWorkTime({day: 6, hours: false});
                } else if (workingDays == 6) {
                    productionGantt.setWorkTime({day: 0, hours: false});
                    productionGantt.unsetWorkTime({day: 6, hours: false});
                } else if (workingDays == 7) {
                    productionGantt.unsetWorkTime({day: 0, hours: false});
                    productionGantt.unsetWorkTime({day: 6, hours: false});
                }
            }

            function setHolidays(holidays) {
                productionGantt.config.work_time = true;
                productionGantt.config.correct_work_time = true;
                angular.forEach(holidays, function (holiday) {
                    var parts = holiday.date.split($rootScope.applicationDateSelectFormatDivider);
                    if ($rootScope.applicationDateFormat == "MM-dd-yyyy, HH:mm:ss") {
                        productionGantt.setWorkTime({date: new Date(parts[2], parts[0] - 1, parts[1]), hours: false});
                    } else {
                        productionGantt.setWorkTime({date: new Date(parts[2], parts[1] - 1, parts[0]), hours: false});
                    }
                });
            }

            function getGanttInstance() {
                return productionGantt;
            }

            function viewTask(val) {
                var obj = val.split(",");
                var productionOrderId = parseInt(obj[0]);
                var number = obj[1];
                var options = {
                    title: "Assigned Person " + number,
                    template: 'app/desktop/modules/mes/productionOrder/all/productionOrderAssignedPersonView.jsp',
                    controller: 'ProductionOrderAssignedPersonController as productionOrderAssignedPersonVm',
                    resolve: 'app/desktop/modules/mes/productionOrder/all/productionOrderAssignedPersonController',
                    width: 600,
                    showMask: true,
                    data: {
                        productionOrder: productionOrderId
                    },
                    buttons: [
                        {text: 'update', broadcast: 'app.productionOrder.update'}
                    ],
                    callback: function (productionOrder) {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            return {
                initPrEditor: initPrEditor,
                toggleMode: toggleMode,
                clearAll: clearAll,
                refresh: refresh,
                deleteTask: deleteTask,
                saveGantt: saveGantt,
                toggleGrid: toggleGrid,
                getGanttInstance: getGanttInstance
            };

        }
    }
);