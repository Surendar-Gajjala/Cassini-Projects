define([
        'app/desktop/modules/pm/pm.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],
    function (module) {
        module.factory('GanttEditor', GanttEditor);

        function GanttEditor($rootScope) {

            var projectStartDate = null;
            var projectFinishDate = null;
            var gridWidth = null;
            var ganttScope = null;
            var startDate = null;
            var duration = null;
            var ganttParentNodes = [];

            function initEditor(container, data, $scope, $rootScope) {
                ganttParentNodes = [];
                gridWidth = data.width.grid;
                ganttScope = $scope;
                var objectType = [
                    {key: "Activity", label: "Activity"},
                    {key: "Milestone", label: "Milestone"}
                ];

                var editors = {
                    over_due: {type: "text", map_to: "over_due"},
                    has_files: {type: "text", map_to: "has_files"},
                    is_shared: {type: "text", map_to: "is_shared"},
                    text: {type: "text", map_to: "text"},
                    object_type: {type: "select", map_to: "object_type", options: objectType},
                    start_date: {type: "date", map_to: "start_date"},
                    end_date: {type: "date", map_to: "end_date"},
                    actual_start_date: {type: "date", map_to: "actual_start_date"},
                    actual_end_date: {type: "date", map_to: "actual_end_date"},
                    duration: {type: "number", map_to: "duration", min: 0, max: 100},
                    assignedTo: {map_to: "assignedTo", type: "select", options: data.persons},
                    invalid_user: {type: "text", map_to: "invalid_user"}
                };

                $scope.deleteTask = deleteTask;
                $scope.viewTask = viewTask;
                $scope.finishTask = finishTask;
                $scope.finishMilestone = finishMilestone;

                gantt.config.keyboard_navigation_cells = true;
                gantt.config.keyboard_navigation = true;
                gantt.config.auto_scheduling = true;
                gantt.config.auto_scheduling_strict = true;
                gantt.config.row_height = 25;
                gantt.config.fit_tasks = true;
                gantt.config.show_unscheduled = true;
                gantt.config.auto_types = true;
                gantt.config.open_tree_initially = true;
                gantt.config.order_branch = "marker";
                gantt.config.show_errors = false;
                gantt.config.undo = true;
                gantt.config.duration_unit = "day";

                if (data.workingDays == null && data.holidays == null) {
                    gantt.config.work_time = false;
                } else if (data.workingDays != null && data.holidays.length == 0) {
                    setWeekends(data.workingDays);
                } else if (data.workingDays != null && data.holidays.length > 0) {
                    setWeekends(data.workingDays);
                    setHolidays(data.holidays);
                } else if (data.workingDays == null && data.holidays.length > 0) {
                    setHolidays(data.holidays);
                }

                projectStartDate = data.projectStartDate.split($rootScope.applicationDateSelectFormatDivider);
                projectFinishDate = data.projectFinishDate.split($rootScope.applicationDateSelectFormatDivider);
                if ($rootScope.applicationDateFormat == "MM-dd-yyyy, HH:mm:ss") {
                    gantt.config.api_date = "%m-%d-%Y %H:%i";
                    gantt.config.date_scale = "%M %d";
                    gantt.config.xml_date = "%m-%d-%Y %H:%i";
                    gantt.config.task_date = "%m %F %Y";
                    gantt.config.date_grid = "%Y-%d-%m";
                    gantt.config.start_date = new Date(parseInt(projectStartDate[2]), parseInt(projectStartDate[0] - 1), parseInt(projectStartDate[1]));
                    gantt.config.end_date = new Date(parseInt(projectFinishDate[2]), parseInt(projectFinishDate[0] - 1), parseInt(projectFinishDate[1]));
                } else {
                    gantt.config.api_date = "%d-%m-%Y %H:%i";
                    gantt.config.date_scale = "%d %M";
                    gantt.config.xml_date = "%d-%m-%Y %H:%i";
                    gantt.config.task_date = "%d %F %Y";
                    gantt.config.date_grid = "%Y-%m-%d";
                    gantt.config.start_date = new Date(parseInt(projectStartDate[2]), parseInt(projectStartDate[1] - 1), parseInt(projectStartDate[0]));
                    gantt.config.end_date = new Date(parseInt(projectFinishDate[2]), parseInt(projectFinishDate[1] - 1), parseInt(projectFinishDate[0]));
                }

                if (data.width.showGantt) {
                    gantt.config.layout = {
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
                    gantt.config.layout = {
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

                var gridDateToStr = gantt.date.date_to_str($rootScope.ganttDateFormat);
                gantt.templates.grid_date_format = function (date, column) {
                    if (column === "end_date") {
                        return gridDateToStr(date /*new Date(date.valueOf() - 1)*/);
                    } else {
                        return gridDateToStr(date);
                    }

                };

                gantt.config.columns = [
                    {
                        name: "add",
                        width: 30,
                        align: 'center',
                        resize: true,
                        hide: !data.hasPermission
                    },
                    {
                        name: "number",
                        label: "Number",
                        width: 50,
                        template: gantt.getWBSCode,
                        resize: true
                    },
                    {
                        name: "is_shared",
                        label: "",
                        width: 12,
                        align: 'center',
                        resize: true,
                        template: function (obj) {
                            if (obj.isShared == true) {
                                if (obj.object_type == 'Activity') return "<i class='fa fa-share-alt' style='cursor: pointer;color: darkgrey;' title='This activity is shared' ></i>";
                                if (obj.object_type == 'Task') return "<i class='fa fa-share-alt' style='cursor: pointer;color: darkgrey;' title='This task is shared' ></i>";
                            }
                            return '<div></div>';
                        }
                    },
                    {
                        name: "has_files",
                        label: "",
                        width: 12,
                        align: 'center',
                        resize: true,
                        template: function (obj) {
                            if (obj.hasFiles == true && obj.fileTab == true) {
                                if (obj.object_type == 'Activity') return "<i class='fa fa-paperclip' style='cursor: pointer;color: darkgrey;' onclick='angular.element(this).scope().showProjectActivityFiles(\"" + obj.id + "\")' title='This activity has files' ></i>";
                                if (obj.object_type == 'Task') return "<i class='fa fa-paperclip' style='cursor: pointer;color: darkgrey;' onclick='angular.element(this).scope().showProjectTaskFiles(\"" + obj.id + "," + obj.parent + "\")' title='This task has files' ></i>";
                            }
                            return '<div></div>';
                        }
                    },
                    {
                        name: "over_due",
                        label: "",
                        width: 12,
                        align: 'center',
                        resize: true,
                        template: function (obj) {
                            if (obj.over_due == "true" && obj.percentComplete != 100) {
                                return "<i class='fa fa-exclamation-triangle' style='cursor: pointer;color: red;' title='This task is overdue'></i>";
                            }
                            return '<div></div>';
                        }
                    },
                    {
                        name: "text",
                        label: "Name",
                        tree: true,
                        width: data.width.text,
                        editor: editors.text,
                        resize: true
                    },
                    {
                        name: "object_type",
                        label: "Type",
                        width: data.width.object_type,
                        align: "left",
                        type: "select",
                        editor: editors.object_type,
                        resize: true
                    },
                    {
                        name: "assignedTo",
                        label: "Assigned To",
                        width: data.width.assignedTo,
                        align: "center",
                        resize: true,
                        editor: editors.assignedTo,
                        template: function (item) {
                            if (item.id != null && item.assignedTo != null) {
                                var personName = "";
                                angular.forEach(data.persons, function (person) {
                                    if (person.key == item.assignedTo)
                                        personName = person.label;
                                });
                                return personName;
                            }
                            return "";
                        }
                    },
                    {
                        name: "invalid_user",
                        label: "",
                        width: 20,
                        align: 'center',
                        template: function (item) {
                            var isActive = '<div></div>';
                            if (item.id != null && item.assignedTo != null) {
                                var flag = false;
                                angular.forEach(data.persons, function (person) {
                                    if (person.key == item.assignedTo && person.active == false && !flag) {
                                        flag = true;
                                        isActive = "<i class='fa fa-user' aria-hidden='true' style='cursor: pointer;color: red;' title='Re Assign this task to another person'></i>";
                                    }
                                });
                                return isActive;
                            } else return isActive;
                        }
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
                        name: "start_date",
                        label: "Start",
                        width: data.width.start_date,
                        align: "center",
                        editor: editors.start_date,
                        resize: true
                    },
                    {
                        name: "end_date",
                        label: "Finish",
                        width: data.width.end_date,
                        align: "center",
                        editor: editors.end_date,
                        resize: true
                    },
                    {
                        name: "actual_start_date",
                        label: "Actual Start",
                        width: data.width.actual_start_date,
                        align: "center",
                        editor: editors.actual_start_date,
                        resize: true
                    },
                    {
                        name: "actual_end_date",
                        label: "Actual Finish",
                        width: data.width.actual_end_date,
                        align: "center",
                        editor: editors.actual_end_date,
                        resize: true
                    },
                    {
                        name: "actions", label: "Actions", width: 100, max_width: 100, align: "center",
                        hide: !(data.projectView && !$rootScope.loginPersonDetails.external),
                        template: function (obj) {
                            if (JSON.stringify(obj.id).length < 10) {
                                var isDelete = "";
                                var isViewTask = "";
                                var isFinishTask = "";
                                var isFinishMilestone = "";
                                if (data.hasDelete) {
                                    isDelete = "<i class='action-btn fa fa-times' style='cursor: pointer;position: absolute;right: 10px;' onclick='angular.element(this).scope().deleteTask(\"" + obj.id + "," + obj.object_type + "," + obj.parent + "," + obj.name + "," + obj.status + "\");' title='Delete'></i>"
                                }
                                if ($rootScope.hasPermission('project', 'view') && (obj.assignedTo != null) && (typeof(obj.assignedTo) != 'string') && ( $rootScope.loginPersonDetails.isAdmin || obj.assignedTo == $rootScope.loginPersonDetails.person.id || $rootScope.loginPersonDetails.person.id == data.projectManager) ) {
                                    isFinishTask = "<i class='fa fa-flag-checkered' style='cursor: pointer;color: darkgrey;position: absolute;right: 50px;' onclick='angular.element(this).scope().finishTask(\"" + obj.id + "," + obj.parent + "\");' title='Finish'></i>";
                                    isViewTask = "<i class='fa fa-info-circle' style='cursor: pointer;color: darkgrey;position: absolute;right: 30px;' onclick='angular.element(this).scope().viewTask(\"" + obj.id + "," + obj.object_type + "," + obj.parent + "\");' title='View details'></i>";
                                    isFinishMilestone = "<i class='fa fa-flag-checkered' style='cursor: pointer;color: darkgrey;position: absolute;right: 30px;' onclick='angular.element(this).scope().finishMilestone(\"" + obj.id + "," + obj.parent + "\");' title='Finish'></i>";
                                }
                                if (obj.object_type != "Phase" && obj.object_type != "Milestone") {
                                    if (obj.object_type == "Task" && obj.progress != 1) return isFinishTask + isViewTask + isDelete;
                                    else if (obj.object_type == "Activity" && obj.activityTasks.length == 0 && obj.progress != 1) return isFinishTask + isViewTask + isDelete;
                                    else return isViewTask + isDelete;
                                } else {
                                    if (obj.object_type == "Milestone" && obj.finishMilestone == true && obj.status != "FINISHED") {
                                        return isFinishMilestone + isDelete;
                                    } else if (obj.object_type == "Milestone" && obj.finishMilestone == false && obj.status != "FINISHED") {
                                        return isDelete;
                                    } else if (obj.object_type == "Phase" && (obj.hasChildren == false || obj.hasChildren == null)) {
                                        return isDelete;
                                    } else {
                                        return "\
                                <i class='action-btn fa fa-times' style='display: none;cursor: pointer;position: absolute;right: 10px;' onclick='angular.element(this).scope().deleteTask(\"" + obj.id + "," + obj.object_type + "," + obj.parent + "," + obj.name + "," + obj.status + "\");' title='Delete'></i> \
                                ";
                                    }
                                }
                            } else {
                                if (data.hasDelete) {
                                    return "<i class='action-btn fa fa-times' style='cursor: pointer;position: absolute;right: 10px;' onclick='angular.element(this).scope().deleteTask(\"" + obj.id + "," + obj.object_type + "," + obj.parent + "," + obj.name + "," + obj.status + "\");' title='Delete'></i>"
                                }
                            }
                        }
                    },
                    {
                        name: "actions", label: "Actions", width: 100, max_width: 100, align: "center",
                        hide: !($rootScope.hasPermission('project', 'edit') && $rootScope.sharedProjectPermission == 'WRITE'),
                        template: function (obj) {
                            if (JSON.stringify(obj.id).length < 10 && obj.object_type != "Phase" && obj.object_type != "Milestone") {
                                if (obj.assignedTo == $rootScope.loginPersonDetails.person.id || $rootScope.loginPersonDetails.person.id == data.projectManager) {
                                    var isFinishTask = "";
                                    var isViewTask = "<i class='fa fa-info-circle' style='cursor: pointer;color: darkgrey;position: absolute;right: 30px;' onclick='angular.element(this).scope().viewTask(\"" + obj.id + "," + obj.object_type + "," + obj.parent + "," + $rootScope.sharedProjectPermission + "\");' title='View details'></i> ";
                                    if (obj.progress != 1) {
                                        isFinishTask = "<i class='fa fa-flag-checkered' style='cursor: pointer;color: darkgrey;position: absolute;right: 50px;' onclick='angular.element(this).scope().finishTask(\"" + obj.id + "," + obj.parent + "\");' title='Finish'></i>";
                                    }
                                    return isViewTask + isFinishTask;
                                } else {
                                    return "";
                                }
                            } else {
                                return "";
                            }
                        }
                    }
                ];

                gantt.attachEvent("onBeforeTaskMove", function (id, parent, tindex) {
                    var task = gantt.getTask(id);
                    if (task.parent != parent)
                        return false;
                    return true;
                });

                gantt.attachEvent("onLinkValidation", function (link) {
                    if (link.type == 0 && checkLink(link)) {
                        gantt.addLink(link);
                        $scope.$broadcast('app.project.ganttUpdated');
                    }
                });

                gantt.attachEvent("onAfterLinkDelete", function (id, item) {
                    var sourceTask = gantt.getTask(item.source).text;
                    var targetTask = gantt.getTask(item.target).text;
                    $scope.$broadcast('app.project.linkDeleted', {source: sourceTask, target: targetTask});
                });

                gantt.attachEvent("onColumnResizeEnd", function (index, column, new_width) {
                    $scope.$broadcast('app.project.saveWidths', {columnName: column.name, newWidth: new_width});
                });

                var gridWidth = null;
                gantt.attachEvent("onGridResize", function (old_width, new_width) {
                    gridWidth = new_width;
                });


                owners = [
                    {key: 0, label: "Unassigned", textColor: "", backgroundColor: ""},
                    {key: 11, label: "Ilona", textColor: "yellow", backgroundColor: "#d96c49"}
                ];

                function compare_input(id, filter_data) {
                    var match = false;
                    // check children's text
                    if (gantt.hasChild(id)) {
                        gantt.eachTask(function (child_object) {
                            if (compare_input(child_object.id, filter_data)) match = true;
                        }, id);
                    }
                    // check task's text
                    if (gantt.getTask(id).text.toLowerCase().indexOf(filter_data.toLowerCase()) >= 0) {
                        gantt.getTask(id).text = "<span style='background-color:#a8d1ff'>" + gantt.getTask(id).text + "</span>";
                        match = true;
                    }
                    if (gantt.getTask(id).person != null && gantt.getTask(id).person.fullName.toLowerCase().indexOf(filter_data.toLowerCase()) >= 0) {

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

                gantt.attachEvent("onBeforeTaskDisplay", function (id, task) {
                    if ($rootScope.searchProjectName != null && $rootScope.searchProjectName != undefined
                        && $rootScope.searchProjectName != "") {
                        if (!compare_input(id, $rootScope.searchProjectName)) {
                            return false;
                        }
                    }
                    return true;

                });

                gantt.attachEvent("onGridResizeEnd", function (old_width, new_width) {
                    $scope.$broadcast('app.project.saveWidths', {columnName: 'grid', newWidth: gridWidth});
                });

                gantt.attachEvent("onGanttReady", function () {
                    var tooltips = gantt.ext.tooltips;
                    var inlineEditors = gantt.ext.inlineEditors;

                    tooltips.detach("[" + gantt.config.task_attribute + "]:not(.gantt_task_row)");
                    tooltips.tooltipFor({
                        selector: ".gantt_task_link",
                        html: function (event, node) {
                            var linkId = node.getAttribute(gantt.config.link_attribute);
                            if (linkId) {
                                var link = gantt.getLink(linkId);
                                var from = gantt.getTask(link.source);
                                var to = gantt.getTask(link.target);

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
                            var linkId = node.getAttribute(gantt.config.link_attribute);
                            if (linkId) {
                                var link = gantt.getLink(linkId);
                                var from = gantt.getTask(link.source);
                                var to = gantt.getTask(link.target);

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
                            if (gantt.config.scale_unit == 'day') {
                                return [
                                    "<b>Non-Working day</b> "
                                ].join("<br>");
                            }
                        }
                    });

                    inlineEditors.attachEvent("onBeforeEditStart", function (state) {
                        var task = gantt.getTask(state.id);
                        if (state.columnName == "actual_start_date" || state.columnName == "actual_end_date") {
                            return false;
                        }
                        if (task.object_type == "Phase") {
                            if (state.columnName == "assignedTo" || state.columnName == "object_type") return false;
                            else return true;
                        }
                        if (task.object_type == "Task") {
                            if (state.columnName == "object_type") return false;
                            else return true;
                        }
                        if (task.object_type == "Milestone") {
                            if (state.columnName == "duration") return false;
                        }
                        return true;
                    });
                });

                function checkLink(link) {
                    var links = gantt.getLinks();
                    var flag = true;
                    $.each(links, function (i, el) {
                        if (parseInt(el.source) == parseInt(link.source) && parseInt(el.target) == parseInt(link.target) && flag)
                            flag = false;
                    });
                    return flag;
                }

                function deleteTask(val) {
                    var obj = val.split(",");
                    if (obj[0].length > 10) {
                        gantt.deleteTask(obj[0]);
                        id = null;
                        type = null;
                        parentId = null;
                        status = null;
                    } else {
                        var id = parseInt(obj[0]);
                        var type = obj[1];
                        var parentId = parseInt(obj[2]);
                        var name = obj[3];
                        var status = obj[4];
                    }
                    if (type == "Phase") $scope.$broadcast('app.project.deleteWbs', {wbsId: id, name: name});
                    if (type == "Activity") $scope.$broadcast('app.project.deleteActivity', {
                        activityId: id,
                        name: name,
                        status: status
                    });
                    if (type == "Milestone") $scope.$broadcast('app.project.deleteMilestone', {
                        milestoneId: id,
                        parentId: parentId,
                        name: name,
                        status: status
                    });
                    if (type == "Task") $scope.$broadcast('app.project.deleteTask', {
                        taskId: id,
                        parentId: parentId,
                        name: name,
                        status: status
                    });
                }

                function finishTask(val) {
                    var obj = val.split(",");
                    var taskId = parseInt(obj[0]);
                    var activityId = parseInt(obj[1]);
                    $scope.$broadcast('app.project.finishActivityTask', {taskId: taskId, activityId: activityId});
                }

                function finishMilestone(val) {
                    var obj = val.split(",");
                    var milestoneId = parseInt(obj[0]);
                    var phaseId = parseInt(obj[1]);
                    $scope.$broadcast('app.project.finishMilestone', {milestoneId: milestoneId, phaseId: phaseId});
                }

                function viewTask(val) {
                    var obj = val.split(",");
                    var id = parseInt(obj[0]);
                    var type = obj[1];
                    var parentId = parseInt(obj[2]);
                    var permission = obj[3];
                    if (type == "Activity") $scope.$broadcast('app.project.openActivityDetails', {
                        activityId: id,
                        permission: permission
                    });
                    else if (type == "Task") $scope.$broadcast('app.project.openTaskDetails', {
                        taskId: id,
                        activityId: parentId,
                        permission: permission
                    });
                }

                function linkTypeToString(linkType) {
                    switch (linkType) {
                        case gantt.config.links.start_to_start:
                            return "Start to start";
                        case gantt.config.links.start_to_finish:
                            return "Start to finish";
                        case gantt.config.links.finish_to_start:
                            return "Finish to start";
                        case gantt.config.links.finish_to_finish:
                            return "Finish to finish";
                        default:
                            return ""
                    }
                }

                gantt.config.scale_unit = "day";
                gantt.config.subscales = [{unit: "month", step: 1, date: "%M, %Y"}];

                gantt.templates.progress_text = function (start, end, task) {
                    return "<span style='text-align:left; color:#fff;font-size: 12px;'>" + Math.round(task.progress * 100) + "% </span>";
                };

                gantt.init(container);
                gantt.parse(data);

                gantt.attachEvent("onTaskCreated", function (task) {
                    startDate = task.start_date;
                    duration = task.duration;
                    if ($rootScope.ganttPersons.length == 0 && $rootScope.ganttType == 'plan') {
                        $scope.$broadcast('app.project.addMembers');
                        return false;
                    } else {
                        $scope.$broadcast('app.project.ganttUpdated');
                        return true;
                    }
                });

                gantt.attachEvent("onAfterTaskUpdate", function (id, task) {
                    if (task.children == undefined || task.object_type == "Milestone") {
                        if (task.activityTasks != undefined && task.activityTasks.length > 0) task.object_type = "Activity";
                        $rootScope.ganttUpdated = true;
                    } else {
                        if (checkParentNodes(task)) $rootScope.ganttUpdated = true;
                        ganttParentNodes.push(task);
                    }
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

                gantt.showLightbox = function (id) {

                };

                gantt.attachEvent("onAfterTaskMove", function (id, parent, tindex) {
                    $rootScope.ganttUpdated = true;
                });
            }

            var toggle_value = 1;

            function checkParentNodes(task) {
                var flag = false;
                var uniqueList = ganttParentNodes.filter(function (item, index, inputArray) {
                    return inputArray.indexOf(item) == index;
                });
                angular.forEach(uniqueList, function (node) {
                    if (node.id == task.id && flag == false) {
                        if (node.name != task.text || node.duration != task.duration || node.assignedTo != task.assignedTo) {
                            flag = true;
                        }
                    }
                });
                return flag;
            }

            function toggleGrid() {
                if (toggle_value != 1) {
                    ganttScope.$broadcast('app.project.hideGantt');
                } else {
                    ganttScope.$broadcast('app.project.showGantt');
                }
                toggle_value = toggle_value * (-1);
            }

            gantt.templates.rightside_text = function (start, end, task) {
                if (task.type == gantt.config.types.milestone) {
                    return task.text;
                }
                return "";
            };

            gantt.templates.task_cell_class = function (task, date) {
                if (!gantt.isWorkTime(date))
                    return "week_end";
                return "";
            };

            gantt.templates.grid_row_class = function (start, end, task) {
                if (task.parent != 0) {
                    var parent = gantt.getTask(task.parent);
                    if (parent.object_type == "Activity") {
                        task.object_type = "Task";
                        task.$open = true;
                    }
                }
                if (task.object_type == "Milestone") task.type = "milestone";
                if (task.$level == 0) {
                    task.assignedTo = "";
                    task.object_type = "Phase";
                }
                if (task.object_type != "Phase" && task.object_type != "Activity") return "nested_task";
                return "";
            };

            gantt.eachTask(function (task) {
                task.$open = true;
            });


            gantt.attachEvent("onTaskCreated", function (task) {
                task.text = "Name";
                task.assignedTo = " ";
                var parent = gantt.getTask(task.parent);
                if (parent != undefined) {
                    if (parent.object_type == "Phase") task.object_type = "Activity";
                    else task.object_type = "";
                } else {
                    task.object_type = "";
                }
                task.actual_start_date = "";
                task.actual_end_date = "";
                return true;
            });

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
                    gantt.config.start_date = gantt.date.add(dates.start_date, 0, config.unit);
                    gantt.config.end_date = gantt.date.add(gantt.date[config.unit + "_start"](dates.end_date), 2, config.unit);
                } else {
                    gantt.config.start_date = config.start_date;
                    gantt.config.end_date = config.end_date;
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
                            var dateToStr = gantt.date.date_to_str($rootScope.ganttDateSelectFormat);
                            var endDate = gantt.date.add(gantt.date.add(date, 1, "week"), -1, "day");
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
                            var dateToStr = gantt.date.date_to_str("%M");
                            var endDate = gantt.date.add(gantt.date.add(date, 3, "month"), -1, "day");
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
                            var dateToStr = gantt.date.date_to_str("%Y");
                            var endDate = gantt.date.add(gantt.date.add(date, 5, "year"), -1, "day");
                            return dateToStr(date) + " - " + dateToStr(endDate);
                        }
                        }
                    ]
                },
                // decades
                {
                    name: "decade",
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

            function clearAll() {
                gantt.clearAll();
            }

            function refresh() {
                gantt.refreshData();
            }

            function deleteTask(taskId) {
                gantt.deleteTask(taskId);
            }

            function setWeekends(workingDays) {
                gantt.config.work_time = true;
                gantt.config.correct_work_time = true;
                if (workingDays == 5) {
                    gantt.setWorkTime({day: 0, hours: false});
                    gantt.setWorkTime({day: 6, hours: false});
                } else if (workingDays == 6) {
                    gantt.setWorkTime({day: 0, hours: false});
                    gantt.unsetWorkTime({day: 6, hours: false});
                } else if (workingDays == 7) {
                    gantt.unsetWorkTime({day: 0, hours: false});
                    gantt.unsetWorkTime({day: 6, hours: false});
                }
            }

            function setHolidays(holidays) {
                gantt.config.work_time = true;
                gantt.config.correct_work_time = true;
                angular.forEach(holidays, function (holiday) {
                    var parts = holiday.date.split($rootScope.applicationDateSelectFormatDivider);
                    if ($rootScope.applicationDateFormat == "MM-dd-yyyy, HH:mm:ss") {
                        gantt.setWorkTime({date: new Date(parts[2], parts[0] - 1, parts[1]), hours: false});
                    } else {
                        gantt.setWorkTime({date: new Date(parts[2], parts[1] - 1, parts[0]), hours: false});
                    }
                });
            }

            function checkPersonPresent(projectMembers,assignedToId){
                var valid = false;
                var isPresent = projectMembers.filter((projectMember) => projectMember.key == assignedToId)
                if (isPresent.length > 0) {
                    valid = true;
                }
                return valid;

            }

            return {
                initEditor: initEditor,
                toggleMode: toggleMode,
                clearAll: clearAll,
                refresh: refresh,
                deleteTask: deleteTask,
                saveGantt: saveGantt,
                toggleGrid: toggleGrid
            };

        }
    }
);