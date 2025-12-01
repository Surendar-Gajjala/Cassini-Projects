define([
        'app/desktop/modules/pm/pm.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/shared/services/pm/project/wbsService',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/tm/taskService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/desktop/modules/pm/wbs/ganttEditor'
    ],
    function (module) {
        module.controller('WorksController', WorksController);

        function WorksController($scope, $rootScope, $timeout, $state, $window, $stateParams, $q, CommonService,
                                 AttributeAttachmentService, ObjectAttributeService, ProjectService,
                                 TaskService, DialogService, $cookies, WbsService, GanttEditor) {

            $rootScope.viewInfo.icon = "fa flaticon-plan2";
            $rootScope.viewInfo.title = "Works";

            var vm = this;

            vm.zoomed = false;
            vm.showGantt = true;
            var linkId = null;
            var links = [];
            var project = null;
            var wbsMap = new Hashtable();
            var creatingWbs = false;

            var demo_tasks = {
                data: [],
                links: [],
                persons: [],
                project: null
            };

            vm.expandAll = GanttEditor.expandAll;
            vm.collapseAll = GanttEditor.collapseAll;
            vm.toggleGantt = GanttEditor.toggleGantt;
            vm.toggleMode = toggleMode;
            vm.saveGantt = saveGantt;
            $rootScope.ids = [];


            function saveGantt() {
                $rootScope.showBusyIndicator();
                vm.wbsList = GanttEditor.saveGantt();
                var result = {data: [], links: []};
                if (vm.wbsList.data != null) {
                    vm.valid = true;
                    angular.forEach(vm.wbsList.data, function (wbs) {
                        if(vm.valid){
                            if ($rootScope.ids != null) {
                                angular.forEach($rootScope.ids, function (id) {
                                    if (id.ganttId == wbs.id) {
                                        wbs.id = id.cassiniId;
                                        wbs.cassiniId = id.cassiniId;
                                    }
                                    if (id.ganttId == parseInt(wbs.parent)) {
                                        wbs.parent = parseInt(id.cassiniId);
                                    }
                                });
                            }
                            if(wbs.type != "placeholder"){
                                var newWbs = restructureWbs(wbs);
                                validate(newWbs);
                                result.data.push(newWbs);
                            }
                        }
                    });
                    result.links.push(vm.wbsList.links);
                }
                if(vm.valid) createGantt(result);
                else $rootScope.hideBusyIndicator();
            }

            function restructureWbs(wbs) {
                var newWbs = {
                    id: wbs.id,
                    cassiniId: wbs.cassiniId == undefined ? null : wbs.cassiniId,
                    name: wbs.text,
                    plannedStartDate: wbs.start_date.slice(0, 10).split("-").join("/"),
                    plannedFinishDate: wbs.end_date.slice(0, 10).split("-").join("/"),
                    actualStartDate: wbs.actual_start_date == "" ? null : wbs.actual_start_date,
                    actualFinishDate: wbs.actual_start_date == "" ? null : wbs.actual_end_date,
                    parent: wbs.parent != 0 ? parseInt(wbs.parent) : null,
                    percentageComplete: 0,
                    duration: wbs.duration,
                    weightage: 0,
                    project: parseInt($stateParams.projectId),
                    type: wbs.type,
                    assignedTo: wbs.assignedTo != null ? parseInt(wbs.assignedTo) : wbs.assignedTo,
                    type: wbs.type != null ? wbs.type : null
                };
                return newWbs;
            }

            function createGantt(result) {
                WbsService.createGantt($stateParams.projectId, JSON.stringify(result)).then(
                    function (data) {
                        angular.forEach(data, function (ids) {
                            $rootScope.ids.push(ids);
                        });
                        $rootScope.hideBusyIndicator();
                        if (result.links[0].length > 0) {
                            angular.forEach(result.links[0], function (link) {
                                var sourceId = parseInt(link.source);
                                var targetId = parseInt(link.target);
                                if ($rootScope.ids != null) {
                                    angular.forEach($rootScope.ids, function (id) {
                                        if (sourceId == id.ganttId) link.source = id.cassiniId;
                                        if (targetId == id.ganttId) link.target = id.cassiniId;
                                    })
                                }
                            });
                            createLinks(result.links[0]);
                        } else {
                            $rootScope.showSuccessMessage("WBS Saved");
                        }
                    },
                    function (error) {
                        $rootScope.hideBusyIndicator();
                        console.log(error);
                    }
                )
            }

            function createLinks(result) {
                WbsService.createLinks($stateParams.projectId, JSON.stringify(result)).then(
                    function (data) {
                        $rootScope.showSuccessMessage("WBS Saved");
                    },
                    function (error) {
                        console.log(error);
                    }
                )
            }

            function initGantt() {
                $timeout(function () {
                    GanttEditor.initEditor('gantt_here', demo_tasks, $scope);
                    GanttEditor.expandAll();
                    $rootScope.hideBusyIndicator();
                }, 100)
            }

            function toggleMode() {
                GanttEditor.toggleMode(vm.zoomed);
                vm.zoomed = !vm.zoomed;
            }

            function loadWbs() {
                $timeout(function () {
                    vm.wbsData = [];
                    links = [];
                    var i = 0;
                    WbsService.getProjectWbs($stateParams.projectId).then(
                        function (data) {
                            if (data.length > 0) {
                                angular.forEach(data, function (wbs) {
                                    ++i;
                                    loadGanttData(wbs);
                                    if (i == data.length) {
                                        demo_tasks.data = vm.wbsData;
                                        initGantt();
                                    }
                                });
                            } else {
                                initGantt();
                            }
                        }
                    );
                }, 1000)
            }

            function loadLinks() {
                WbsService.getLink($stateParams.projectId).then(
                    function (data) {
                        if (data != null || data != undefined) {
                            demo_tasks.links = JSON.parse(data.dependency);
                        }
                    }
                );
            }


            function loadProjectPersons() {
                $rootScope.showBusyIndicator();
                ProjectService.getProjectPersons($stateParams.projectId).then(
                    function (data) {
                        vm.projectPersons = data;
                        angular.forEach(vm.projectPersons, function (obj) {
                            loadPersonsById(obj.person);
                        });
                    }
                );
            }

            function loadPersonsById(person) {
                vm.persons = [];
                CommonService.getPerson(person).then(
                    function (data) {
                        vm.persons.push(data);
                        demo_tasks.persons.push({key: data.id, label: data.firstName});
                    }
                )
            }

            function loadGanttData(wbs) {
                wbsMap.put(wbs.id, wbs);
                wbs.start_date = wbs.plannedStartDate;
                wbs.end_date = wbs.plannedFinishDate;
                wbs.actual_start_date = wbs.actualStartDate == null ? "" : wbs.actualStartDate;
                wbs.actual_end_date = wbs.actualFinishDate == null ? "" : wbs.actualFinishDate;
                wbs.text = wbs.name;
                wbs.progress = wbs.percentageComplete;
                vm.wbsData.push(wbs);
                if (wbs.predecessors != null) {
                    var predecessorsArray = wbs.predecessors.split(',');
                    angular.forEach(predecessorsArray, function (predecessor) {
                        var link = {
                            id: ++linkId,
                            source: predecessor,
                            type: 0,
                            target: wbs.id
                        };
                        links.push(link);
                    });
                }
            }

            function updateWbs(wbs) {
                creatingWbs = true;
                var projectId = $stateParams.projectId;
                var newRow = {
                    name: wbs.text,
                    plannedStartDate: moment(wbs.start_date).format('DD/MM/YYYY'),
                    plannedFinishDate: moment(wbs.end_date).format('DD/MM/YYYY'),
                    parent: wbs.parent != 0 ? wbs.parent : null,
                    percentageComplete: 0,
                    duration: wbs.duration,
                    project: projectId,
                    weightage: 0,
                    assignedTo: wbs.assignedTo
                };
                if (validate(newRow)) {
                    if (wbsMap.get(wbs.id) != null) {
                        var existingWbs = wbsMap.get(wbs.id);
                        if (!(existingWbs.plannedStartDate == newRow.plannedStartDate && existingWbs.plannedFinishDate == newRow.plannedFinishDate && (existingWbs.parent == null || existingWbs.parent == newRow.parent) && existingWbs.name == newRow.name)) {
                            newRow.id = wbs.id;
                            newRow.percentageComplete = wbs.percentageComplete;
                            WbsService.updateWbs($stateParams.projectId, newRow).then(
                                function (data) {
                                    creatingWbs = false;
                                    $rootScope.showSuccessMessage("WBS updated successfully");
                                },
                                function (error) {
                                    creatingWbs = false;
                                }
                            );
                        }
                        else {
                            creatingWbs = false;
                        }
                    }
                    else {
                        WbsService.createWbs($stateParams.projectId, newRow).then(
                            function (data) {
                                creatingWbs = false;
                                $rootScope.showSuccessMessage("WBS created successfully");
                                //wbs.id = data.id;
                                wbsMap.put(wbs.id, data);
                            },
                            function (error) {
                                creatingWbs = false;
                                //GanttEditor.deleteTask(wbs.id);
                            }
                        );
                    }
                }
                else {
                    vm.hasError = true;
                    creatingWbs = false;
                }
            }

            function validate(wbs) {
                vm.hasError = false;
                var parentPlannedStartDate = null;
                var parentPlannedFinishDate = null;
                if (wbs.parent != null && wbs.parent != 0) {
                    var wbsParent = wbs.parent;
                    parentPlannedStartDate = moment(wbsParent.plannedStartDate, 'DD/MM/YYYY');
                    parentPlannedFinishDate = moment(wbsParent.plannedFinishDate, 'DD/MM/YYYY');
                }
                if (wbs.name == null || wbs.name == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage("WBS Name cannot be empty");
                }
                else if (wbs.plannedStartDate == null) {
                    vm.valid = false;
                    $rootScope.showWarningMessage("WBS Start Date cannot be empty for task : " + wbs.name);
                }
                else if (wbs.plannedFinishDate == null) {
                    vm.valid = false;
                    $rootScope.showWarningMessage("WBS Finish Date cannot be empty for task : " + wbs.name);
                }

                else if (wbs.plannedStartDate != null) {
                    var today = moment(new Date());
                    var todayStr = today.format('DD/MM/YYYY');
                    var todayDate = moment(todayStr, 'DD/MM/YYYY');
                    var projectPlannedStartDate = moment(vm.project.plannedStartDate, 'DD/MM/YYYY');
                    var projectPlannedFinishDate = moment(vm.project.plannedFinishDate, 'DD/MM/YYYY');
                    var plannedStartDate = moment(wbs.plannedStartDate, 'DD/MM/YYYY');
                    var val1 = plannedStartDate.isSame(todayDate) || plannedStartDate.isAfter(todayDate);
                    if (plannedStartDate.isBefore(projectPlannedStartDate) || plannedStartDate.isAfter(projectPlannedFinishDate)) {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Planned Start Date should be in b/w project Planned Start & Finish Date for task : " + wbs.name);
                    }

                }
                if (wbs.plannedStartDate != null && wbs.plannedFinishDate != null) {
                    var plannedFinishDate = moment(wbs.plannedFinishDate, 'DD/MM/YYYY');
                    var plannedStartDate = moment(wbs.plannedStartDate, 'DD/MM/YYYY');
                    var projectPlannedStartDate = moment(vm.project.plannedStartDate, 'DD/MM/YYYY');
                    var projectPlannedFinishDate = moment(vm.project.plannedFinishDate, 'DD/MM/YYYY');
                    if (wbs.parent != null && wbs.parent != 0) {
                        var parentStartDate = moment(wbs.parent.plannedStartDate, 'DD/MM/YYYY');
                        var parentFinishDate = moment(wbs.parent.plannedFinishDate, 'DD/MM/YYYY');
                        if (wbs.parent.plannedStartDate != null && wbs.parent.plannedStartDate != undefined && parentStartDate.isAfter(plannedStartDate)) {
                            vm.valid = false;
                            $rootScope.showErrorMessage("Child Planned Start Date should be on or after Parent Planned Start Date for task : " + wbs.name);
                        }
                        if (wbs.parent.plannedStartDate != null && wbs.parent.plannedStartDate != undefined && plannedStartDate.isAfter(parentFinishDate)) {
                            vm.valid = false;
                            $rootScope.showErrorMessage("Child Planned Finish Date should be before Parent Planned Finish Date for task : " + wbs.name);
                        }
                    }
                    var val = plannedFinishDate.isSame(plannedStartDate) || plannedFinishDate.isAfter(plannedStartDate) || plannedFinishDate.isBefore(projectPlannedStartDate);
                    if (!val) {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Planned Finish Date should be after Planned Start Date for task : " + wbs.name);
                    }
                    if (plannedStartDate.isBefore(projectPlannedStartDate) || plannedStartDate.isAfter(projectPlannedFinishDate)) {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Planned Start Date should be in between Project Planned Start & Finish Date for task : " + wbs.name);
                    }
                    if (plannedFinishDate.isAfter(projectPlannedFinishDate)) {
                        vm.valid = false;
                        $rootScope.showErrorMessage(" Planned Finish Date should not exceed project Planned Finish Date for task : " + wbs.name);
                    }

                    if (plannedFinishDate.isAfter(parentPlannedFinishDate)) {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Planned Finish Date should not exceed Parent Planned Finish Date for task : " + wbs.name);
                    }
                    if (parentPlannedStartDate != null) {
                        var val2 = plannedStartDate.isBefore(parentPlannedStartDate) || plannedStartDate.isAfter(parentPlannedFinishDate);
                        if (val2) {
                            vm.valid = false;
                            $rootScope.showErrorMessage("Planned Start Date should be in b/w Parent Planned Start & Finish Date for task : " + wbs.name)
                        }
                    }

                } else if (wbs.actualStartDate != null) {
                    var today = moment(new Date());
                    var todayStr = today.format('DD/MM/YYYY');
                    var todayDate = moment(todayStr, 'DD/MM/YYYY');
                    var actualStartDate = moment(wbs.actualStartDate, 'DD/MM/YYYY');
                    var val1 = actualStartDate.isSame(todayDate) || actualStartDate.isAfter(todayDate);
                    if (!val1) {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Actual Start Date should be on (or) after Today's Date for task : " + wbs.name);
                    }
                }

                if (wbs.actualStartDate != null && wbs.actualFinishDate != null) {
                    var actualFinishDate = moment(wbs.actualFinishDate, 'DD/MM/YYYY');
                    var actualStartDate = moment(wbs.actualStartDate, 'DD/MM/YYYY');
                    var val = actualFinishDate.isSame(actualStartDate) || actualFinishDate.isAfter(actualStartDate);
                    if (!val) {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Actual Finish Date should be after Actual Start Date for task : " + wbs.name);
                    }
                }
                return vm.valid;
            }

            function loadProject() {
                ProjectService.getProject($stateParams.projectId).then(
                    function (data) {
                        vm.project = data;
                        demo_tasks.project = vm.project;
                    }
                )
            }

            function deleteWbs(wbsId) {
                var ganttId = null;
                vm.deleteTask = true;
                if ($rootScope.ids.length != 0 && wbsId.length > 10) {
                    angular.forEach($rootScope.ids, function (id) {
                        if (id.ganttId == wbsId) {
                            wbsId = id.cassiniId;
                            ganttId = id.ganttId;
                        }
                    });
                } else if($rootScope.ids.length == 0 && wbsId.length > 10){
                    vm.deleteTask = false;
                    GanttEditor.deleteTask(wbsId)
                }
                if (vm.deleteTask) {
                    WbsService.deleteWbs($stateParams.projectId, wbsId).then(
                        function (data) {
                            $rootScope.showSuccessMessage("WBS Item deleted successfully");
                            if (ganttId != null) GanttEditor.deleteTask(ganttId);
                            else GanttEditor.deleteTask(wbsId);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }
            }

            (function () {
                loadProjectPersons();
                loadProject();
                loadLinks();
                loadWbs();
                $scope.$on("$destroy", function () {
                    GanttEditor.clearAll();
                });
                $scope.$on('app.worksController.createWbs', function (event, args) {
                    updateWbs(args.wbs);
                });
                $scope.$on('app.worksController.deleteWbs', function (event, args) {
                    deleteWbs(args.wbsId);
                });
            })();

        }
    }
)
;


