define([
        'app/desktop/modules/template/template.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],
    function (module) {
        module.factory('TemplateGanttEditor', TemplateGanttEditor);

        function TemplateGanttEditor() {
            gantt.config.keyboard_navigation_cells = true;
            gantt.config.keyboard_navigation = true;
            var templateGantt = gantt;

            function initEditor(container, data, $scope, $rootScope) {

                var objectType = [
                    {key: "Activity", label: "Activity"},
                    {key: "Milestone", label: "Milestone"}
                ];

                var editors = {
                    text: {type: "text", map_to: "text"},
                    object_type: {type: "select", map_to: "object_type", options: objectType},
                    assignedTo: {map_to: "assignedTo", type: "select", options: data.persons},
                };

                $scope.deleteTask = deleteTask;
                templateGantt.config.auto_scheduling = true;
                templateGantt.config.auto_scheduling_strict = true;
                templateGantt.config.row_height = 25;
                templateGantt.config.fit_tasks = true;
                templateGantt.config.show_unscheduled = true;
                templateGantt.config.auto_types = true;
                templateGantt.config.open_tree_initially = true;
                templateGantt.config.order_branch = "marker";
                templateGantt.config.show_errors = false;
                templateGantt.undo = true;
                templateGantt.config.layout = {
                    css: 'gantt_container',
                    cols: [
                        {
                            rows: [
                                {view: 'grid', scrollX: 'gridScrollX', scrollY: 'scrollVer'},
                                {view: 'scrollbar', id: 'gridScrollX', group: 'horizontal'},
                            ]
                        },
                        {view: "scrollbar", id: "scrollVer", height: 800}
                    ]
                };


                templateGantt.config.columns = [
                    {name: "add", width: 30, align: 'center'},
                    {
                        name: "text",
                        label: "Name",
                        tree: true,
                        width: 200,
                        editor: editors.text,
                        resize: true
                    },
                    {
                        name: "object_type",
                        label: "Type",
                        width: 300,
                        align: "left",
                        type: "select",
                        editor: editors.object_type,
                        resize: true
                    }, {
                        name: "assignedTo",
                        label: "Assigned To",
                        width: 200,
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
                    }, /*{
                     name: "invalid_user",
                     label: "",
                     width: 20,
                     align: 'center',
                     template: function (item) {
                     if (item.id != null && item.assignedTo != null) {
                     var flag = false;
                     var isActive = '<div></div>';
                     angular.forEach(data.persons, function (person) {
                     if (person.key == item.assignedTo && person.active == false && !flag) {
                     flag = true;
                     isActive = "<i class='fa fa-user' aria-hidden='true' style='cursor: pointer;color: red;' title='Re Assign this task to another person'></i>";
                     }
                     });
                     return isActive;
                     }
                     }
                     },*/
                    {
                        name: "actions", label: "Actions", width: 100, max_width: 100, align: "center",
                        template: function (obj) {
                            var isDelete = "<i class='action-btn fa fa-times' style='cursor: pointer;position: absolute;right: 10px;' onclick='angular.element(this).scope().deleteTask(\"" + obj.id + "\");' title='Delete'></i>";
                            var isViewTask = "<i class='fa fa-info-circle' style='cursor: pointer;color: darkgrey;position: absolute;right: 30px;' onclick='angular.element(this).scope().viewTask(\"" + obj.id + "," + obj.object_type + "," + obj.parent + "\");' title='View details'></i>";
                            if (obj.object_type != "Phase" && obj.object_type != "Milestone") {
                                if (obj.object_type == "Task") return isViewTask + isDelete;
                                else return isViewTask + isDelete;
                            } else {
                                if (obj.object_type == "Milestone") {
                                    return isDelete;
                                } else if (obj.object_type == "Phase" && (obj.hasChildren == false || obj.hasChildren == null)) {
                                    return isDelete;
                                } else {
                                    return "\
                                <i class='action-btn fa fa-times' style='cursor: pointer;position: absolute;right: 10px;' onclick='angular.element(this).scope().deleteTask(\"" + obj.id + "\");' title='Delete'></i> \
                                ";
                                }
                            }
                        }
                    }
                ];

                templateGantt.attachEvent("onBeforeTaskMove", function (id, parent, tindex) {
                    var task = templateGantt.getTask(id);
                    if (task.parent != parent)
                        return false;
                    return true;
                });


                templateGantt.attachEvent("onAfterTaskUpdate", function (id, task) {
                    if (task.children == undefined || task.object_type == "Milestone") {
                        if (task.templateActivityTasks != undefined) {
                            if (task.templateActivityTasks.length > 0) task.object_type = "Activity";
                        }

                    }

                    return true;
                });

                templateGantt.attachEvent("onGanttReady", function () {
                    var inlineEditors = templateGantt.ext.inlineEditors;
                    inlineEditors.attachEvent("onBeforeEditStart", function (state) {
                        var task = templateGantt.getTask(state.id);
                        if (task.object_type == "Phase") {
                            if (state.columnName == "assignedTo" || state.columnName == "object_type") return false;
                            else return true;
                        }
                        if (task.object_type == "Task") {
                            if (state.columnName == "object_type") return false;
                            else return true;
                        }
                        return true;
                    });
                });


                templateGantt.templates.tooltip_text = function (start, end, task) {
                    return false;
                }

                function deleteTask(id) {
                    if (id.length > 10) {
                        templateGantt.deleteTask(id);
                    } else {
                        $scope.$broadcast('app.template.deleteWbs', {Id: id});
                    }
                }

                $scope.viewTask = viewTask;
                function viewTask(val) {
                    var obj = val.split(",");
                    var id = parseInt(obj[0]);
                    var type = obj[1];
                    var parentId = parseInt(obj[2]);
                    //var permission = obj[3];
                    if (type == "Activity") $scope.$broadcast('app.projecttemplate.openActivityDetails', {activityId: id});
                    else if (type == "Task") $scope.$broadcast('app.projecttemplate.openTaskDetails', {
                        taskId: id,
                        activityId: parentId
                    });
                }

                templateGantt.init(container);
                templateGantt.parse(data);

                templateGantt.attachEvent("onTaskCreated", function (task) {
                    $scope.$broadcast('app.template.ganttUpdated');
                    return true;
                });

                templateGantt.attachEvent("onMouseMove", function (e, i) {
                    $('.gantt_tooltip').hide();
                    if (i.clientX > templateGantt.config.grid_width + 100) {
                        $('.gantt_tooltip').show();
                    }
                    return true;
                });

                templateGantt.showLightbox = function (id) {

                };

            }

            var toggle_value = -1;

            templateGantt.templates.rightside_text = function (start, end, task) {
                if (task.type == templateGantt.config.types.milestone) {
                    return task.text;
                }
                return "";
            };

            templateGantt.templates.grid_row_class = function (start, end, task) {
                if (task.parent != 0) {
                    var parent = templateGantt.getTask(task.parent);
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

            templateGantt.eachTask(function (task) {
                task.$open = true;
            });


            templateGantt.attachEvent("onTaskCreated", function (task) {
                task.text = "Name";
                task.assignedTo = " ";
                task.object_type = "";
                task.actual_start_date = "";
                task.actual_end_date = "";
                return true;
            });

            function saveGantt() {
                return templateGantt.serialize();
            }

            function clearAll() {
                templateGantt.clearAll();
            }

            function refresh() {
                templateGantt.refreshData();
            }

            function deleteTask(taskId) {
                templateGantt.deleteTask(taskId);
            }

            function expandAll() {
                templateGantt.eachTask(function (task) {
                    task.$open = true;
                });
                templateGantt.render();
            }

            function collapseAll() {
                templateGantt.eachTask(function (task) {
                    task.$open = false;
                });
                templateGantt.render();
            }

            function getGanttInstance() {
                return templateGantt;
            }

            return {
                initEditor: initEditor,
                clearAll: clearAll,
                refresh: refresh,
                deleteTask: deleteTask,
                saveGantt: saveGantt,
                expandAll: expandAll,
                collapseAll: collapseAll,
                getGanttInstance: getGanttInstance
            };

        }
    }
);