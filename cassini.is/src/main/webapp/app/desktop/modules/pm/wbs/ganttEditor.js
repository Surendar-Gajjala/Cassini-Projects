define([
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/pm/project/wbsService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],
    function (module) {
        module.factory('GanttEditor', GanttEditor);

        function GanttEditor() {


            function initEditor(container, data, $scope) {
                //gantt = Gantt.getGanttInstance();

                var editors = {
                    text: {type: "text", map_to: "text"},
                    start_date: {type: "date", map_to: "start_date"},
                    end_date: {type: "date", map_to: "end_date"},
                    actual_start_date: {type: "text", map_to: "actual_start_date"},
                    actual_end_date: {type: "text", map_to: "actual_end_date"},
                    duration: {type: "number", map_to: "duration", min: 0, max: 100},
                    assignedTo:{ map_to:"assignedTo",type: "select", options: data.persons}
                };

                $scope.deleteTask = deleteTask;

                gantt.config.keyboard_navigation_cells = true;
                gantt.config.keyboard_navigation = true;
                gantt.config.auto_scheduling = true;
                gantt.config.auto_scheduling_strict = true;
                gantt.config.row_height = 25;
                gantt.config.fit_tasks = true;
                gantt.config.show_unscheduled = true;
                gantt.config.placeholder_task = true;
                gantt.config.auto_types = true;
                gantt.config.open_tree_initially = true;
                gantt.config.order_branch = "marker";

                var plannedStartDate = data.project.plannedStartDate.split("/");
                gantt.config.start_date = new Date(parseInt(plannedStartDate[2]), parseInt(plannedStartDate[1] - 1), parseInt(plannedStartDate[0]));
                var plannedFinishDate = data.project.plannedFinishDate.split("/");
                gantt.config.end_date = new Date(parseInt(plannedFinishDate[2]), parseInt(plannedFinishDate[1] - 1), parseInt(plannedFinishDate[0]));

              /*  gantt.config.layout = {
                    css: "gantt_container",
                    cols: [
                        {
                            width:800,
                            min_width: 300,
                            rows:[
                                {view: "grid", scrollX: "gridScroll", scrollable: true, scrollY: "scrollVer"},
                                {view: "scrollbar", id: "gridScroll"}
                            ]
                        },
                        {resizer: true, width: 1},
                        {
                            rows:[
                                {view: "timeline", scrollX: "scrollHor", scrollY: "scrollVer"},
                                {view: "scrollbar", id: "scrollHor"}
                            ]
                        },
                        {view: "scrollbar", id: "scrollVer"}
                    ]
                };*/

                gantt.config.columns = [
                    {name: "add", width: 30, align: 'center'},
                    //{name: "wbs", label: "#", width: 60, max_width: 60, align: "left", template: gantt.getWBSCode},
                    {name: "text", label: "Name", tree: true, width: 200, editor: editors.text, resize: true},
                    {
                        name: "duration",
                        label: "Duration",
                        width: 80,
                        align: "center",
                        editor: editors.duration,
                        resize: true
                    },
                    {
                        name: "start_date",
                        label: "Start",
                        width: 100,
                        align: "center",
                        editor: editors.start_date,
                        resize: true
                    },
                    {
                        name: "end_date",
                        label: "Finish",
                        width: 100,
                        align: "center",
                        editor: editors.end_date,
                        resize: true
                    },
                    {
                        name: "actual_start_date",
                        label: "Actual Start",
                        width: 100,
                        align: "center",
                        editor: editors.actual_start_date,
                        resize: true
                    },
                    {
                        name: "actual_end_date",
                        label: "Actual Finish",
                        width: 100,
                        align: "center",
                        editor: editors.actual_end_date,
                        resize: true
                    },
                    {
                        name: "assignedTo",
                        label: "AssignedTo",
                        width: 100,
                        align: "center",
                        resize: true,
                        editor: editors.assignedTo,
                        template: function (item) {
                            if(item.id != null && item.assignedTo != null){
                                var personName = "";
                                angular.forEach(data.persons, function(person){
                                    if(person.key == item.assignedTo)
                                        personName = person.label;
                                });
                                return personName;
                            }
                            return "";
                        }
                    },
                    {
                        name: "actions", label: "Actions", width: 100, max_width: 100, align: "center",
                        template: function (obj) {
                            return "\
                                <i class='action-btn fa fa-times' onclick='angular.element(this).scope().deleteTask(\"" + obj.id + "\");' title='Delete this task'></i> \
                                ";
                        }
                    }
                ];

                function deleteTask(id) {
                    $scope.$broadcast('app.worksController.deleteWbs', {wbsId: id});
                }

                gantt.config.scale_unit = "day";
                gantt.config.subscales = [{unit: "month", step: 1, date: "%M, %Y"}];

                gantt.templates.progress_text = function (start, end, task) {
                    return "<span style='text-align:left; color:#fff;font-size: 12px;'>" + Math.round(task.progress * 100) + "% </span>";
                };


                //gantt.config.readonly = true;
                gantt.init(container);
                gantt.parse(data);

                gantt.attachEvent("onMouseMove", function (e, i) {
                    $('.gantt_tooltip').hide();
                    if (i.clientX > gantt.config.grid_width + 100) {
                        $('.gantt_tooltip').show();
                    }
                    return true;
                });

                gantt.showLightbox = function (id) {

                };
            }

            gantt.attachEvent("onTaskCreated", function(task){
                task.text = "Create task";
                task.assignedTo = "";
                task.actual_start_date = "";
                task.actual_end_date = "";
                return true;
            });


            function expandAll() {
                gantt.eachTask(function (task) {
                    task.$open = true;
                });
                gantt.render();
            }

            function collapseAll() {
                gantt.eachTask(function (task) {
                    task.$open = false;
                });
                gantt.render();
            }

            function saveGantt() {
                return gantt.serialize();
            }

            var cachedSettings = {};

            function saveConfig() {
                var config = gantt.config;
                cachedSettings = {};
                cachedSettings.scale_unit = config.scale_unit;
                cachedSettings.date_scale = config.date_scale;
                cachedSettings.step = config.step;
                cachedSettings.subscales = config.subscales;
                cachedSettings.template = gantt.templates.date_scale;
                cachedSettings.start_date = config.start_date;
                cachedSettings.end_date = config.end_date;
            }

            function restoreConfig() {
                applyConfig(cachedSettings);
            }

            function applyConfig(config, dates) {
                gantt.config.scale_unit = config.scale_unit;
                if (config.date_scale) {
                    gantt.config.date_scale = config.date_scale;
                    gantt.templates.date_scale = null;
                }
                else {
                    gantt.templates.date_scale = config.template;
                }

                gantt.config.step = config.step;
                gantt.config.subscales = config.subscales;

                if (dates) {
                    gantt.config.start_date = gantt.date.add(dates.start_date, -1, config.unit);
                    gantt.config.end_date = gantt.date.add(gantt.date[config.unit + "_start"](dates.end_date), 2, config.unit);
                } else {
                    gantt.config.start_date = gantt.config.end_date = null;
                }
            }

            function zoomToFit() {
                var project = gantt.getSubtaskDates(),
                    areaWidth = gantt.$task.offsetWidth;

                for (var i = 0; i < scaleConfigs.length; i++) {
                    var columnCount = getUnitsBetween(project.start_date, project.end_date, scaleConfigs[i].unit, scaleConfigs[i].step);
                    if ((columnCount + 2) * gantt.config.min_column_width <= areaWidth) {
                        break;
                    }
                }

                if (i === scaleConfigs.length) {
                    i--;
                }

                applyConfig(scaleConfigs[i], project);
                gantt.render();
            }

            // get number of columns in timeline
            function getUnitsBetween(from, to, unit, step) {
                var start = new Date(from),
                    end = new Date(to);
                var units = 0;
                while (start.valueOf() < end.valueOf()) {
                    units++;
                    start = gantt.date.add(start, step, unit);
                }
                return units;
            }

            //Setting available scales
            var scaleConfigs = [
                // minutes
                {
                    unit: "minute", step: 1, scale_unit: "hour", date_scale: "%H", subscales: [
                    {unit: "minute", step: 1, date: "%H:%i"}
                ]
                },
                // hours
                {
                    unit: "hour", step: 1, scale_unit: "day", date_scale: "%j %M",
                    subscales: [
                        {unit: "hour", step: 1, date: "%H:%i"}
                    ]
                },
                // days
                {
                    unit: "day", step: 1, scale_unit: "month", date_scale: "%F",
                    subscales: [
                        {unit: "day", step: 1, date: "%j"}
                    ]
                },
                // weeks
                {
                    unit: "week", step: 1, scale_unit: "month", date_scale: "%F",
                    subscales: [
                        {
                            unit: "week", step: 1, template: function (date) {
                            var dateToStr = gantt.date.date_to_str("%d %M");
                            var endDate = gantt.date.add(gantt.date.add(date, 1, "week"), -1, "day");
                            return dateToStr(date) + " - " + dateToStr(endDate);
                        }
                        }
                    ]
                },
                // months
                {
                    unit: "month", step: 1, scale_unit: "year", date_scale: "%Y",
                    subscales: [
                        {unit: "month", step: 1, date: "%M"}
                    ]
                },
                // quarters
                {
                    unit: "month", step: 3, scale_unit: "year", date_scale: "%Y",
                    subscales: [
                        {
                            unit: "month", step: 3, template: function (date) {
                            var dateToStr = gantt.date.date_to_str("%M");
                            var endDate = gantt.date.add(gantt.date.add(date, 3, "month"), -1, "day");
                            return dateToStr(date) + " - " + dateToStr(endDate);
                        }
                        }
                    ]
                },
                // years
                {
                    unit: "year", step: 1, scale_unit: "year", date_scale: "%Y",
                    subscales: [
                        {
                            unit: "year", step: 5, template: function (date) {
                            var dateToStr = gantt.date.date_to_str("%Y");
                            var endDate = gantt.date.add(gantt.date.add(date, 5, "year"), -1, "day");
                            return dateToStr(date) + " - " + dateToStr(endDate);
                        }
                        }
                    ]
                },
                // decades
                {
                    unit: "year", step: 10, scale_unit: "year", template: function (date) {
                    var dateToStr = gantt.date.date_to_str("%Y");
                    var endDate = gantt.date.add(gantt.date.add(date, 10, "year"), -1, "day");
                    return dateToStr(date) + " - " + dateToStr(endDate);
                },
                    subscales: [
                        {
                            unit: "year", step: 100, template: function (date) {
                            var dateToStr = gantt.date.date_to_str("%Y");
                            var endDate = gantt.date.add(gantt.date.add(date, 100, "year"), -1, "day");
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
                    gantt.render();
                }
            }

            function toggleGantt(showGantt) {
                gantt.config.show_chart = showGantt;
                gantt.render();
            }

            function clearAll() {
                gantt.clearAll();
            }

            function refresh() {
                gantt.refreshData();
            }

            function deleteTask(taskId) {
                gantt.deleteTask(taskId);
            }

            return {
                initEditor: initEditor,
                expandAll: expandAll,
                collapseAll: collapseAll,
                toggleMode: toggleMode,
                toggleGantt: toggleGantt,
                clearAll: clearAll,
                refresh: refresh,
                deleteTask: deleteTask,
                saveGantt: saveGantt
            };

        }
    }
);