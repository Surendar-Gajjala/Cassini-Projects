define(
    [
        'app/desktop/modules/tm/tm.module',
        'bpmn-modeler',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/shared/services/workflow/workflowDefinitionService',
        'app/shared/services/tm/taskService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/desktop/modules/tm/details/tabs/workflow/tabs/history/workflowHistoryController'

    ],
    function (module, Modeler) {
        module.controller('TaskWorkflowController', TaskWorkflowController);

        function TaskWorkflowController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal, httpFactory, CommonService, TaskService, WorkflowDefinitionService) {

            var vm = this;
            var workflowLoaded = false;
            var viewer = null;
            vm.workflow = null;
            vm.taskId = $stateParams.taskId;
            vm.projectId = $stateParams.projectId;
            vm.eco = null;
            vm.currentStatus = null;
            vm.selectedStatus = null;
            vm.workflowHistory = [];
            vm.workflowProperties = [];
            vm.workflowAttributes = [];

            vm.startWorkflow = startWorkflow;
            vm.promote = promote;
            vm.demote = demote;
            vm.saveAssignment = saveAssignment;
            vm.showPersons = showPersons;

            var currencyMap = new Hashtable();
            vm.currencies = $application.currencies;

            var svgNodes = new Hashtable();
            var mapStatuses = new Hashtable();
            var mapTransitions = new Hashtable();

            var parsed = angular.element("<div></div>");
            vm.removePerson = parsed.html($translate.instant("REMOVE_PERSON")).html();
            vm.personRemove = parsed.html($translate.instant("PERSON_REMOVE_MSG")).html();
            var select = parsed.html($translate.instant("SELECT")).html();
            var selectPersons = parsed.html($translate.instant("SELECT_PERSON")).html();
            var enterComment = parsed.html($translate.instant("PLEASE_ENTER_COMMENT")).html();

            function startWorkflow() {
                $rootScope.showBusyIndicator($('.view-content'));
                TaskService.startWorkflow(vm.taskId).then(
                    function (data) {
                        vm.workflow.started = true;
                        vm.workflow.startedOn = data.startedOn;

                        if (vm.workflow.currentStatus != null) {
                            getStatusById(vm.workflow.currentStatus).flag = 'COMPLETED';
                        }
                        var transitions = getTransitionsFromStatus(vm.workflow.start.id);
                        if (transitions.length == 1) {
                            vm.workflow.currentStatus = transitions[0].toStatus;
                            vm.workflow.currentStatusObject = transitions[0].toStatusObject;
                            getStatusById(vm.workflow.currentStatus).flag = 'INPROCESS';
                        }
                        $rootScope.loadWorkflowHistory();
                        updateStatusColors();
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function promote(toStatus) {
                $rootScope.showBusyIndicator($('.view-content'));

                if (toStatus != vm.workflow.finish.id) {
                    TaskService.promoteWorkflow(vm.taskId, vm.workflow.currentStatus, toStatus).then(
                        function (data) {
                            getStatusById(vm.workflow.currentStatus).flag = 'COMPLETED';
                            vm.workflow.currentStatus = toStatus;
                            var s = getStatusById(vm.workflow.currentStatus);
                            vm.workflow.currentStatusObject = s;
                            s.flag = 'INPROCESS';
                            updateStatusColors();
                            $rootScope.loadWorkflowHistory();
                            $rootScope.hideBusyIndicator();
                        },
                        function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }
                else {
                    TaskService.finishWorkflow(vm.taskId).then(
                        function (data) {
                            vm.workflow.finished = true;
                            vm.workflow.finishedOn = data.finishedOn;

                            var s = getStatusById(vm.workflow.currentStatus);
                            s.flag = 'COMPLETED';
                            vm.workflow.currentStatus = toStatus;
                            vm.workflow.currentStatusObject = s;
                            s = getStatusById(vm.workflow.currentStatus);
                            vm.workflow.currentStatusObject = s;
                            s.flag = 'COMPLETED';
                            updateStatusColors();
                            $rootScope.loadWorkflowHistory();
                            $rootScope.hideBusyIndicator();
                        },
                        function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }
            }

            function demote() {
                if (vm.workflow.currentStatus != null) {
                    getStatusById(vm.workflow.currentStatus).flag = 'UNASSIGNED';
                }
                var transition = getTransitionsToStatus(vm.workflow.currentStatus);
                if (transition != null) {
                    vm.workflow.currentStatus = transition.fromStatus;
                    getStatusById(vm.workflow.currentStatus).flag = 'INPROCESS';
                }
                updateStatusColors();
            }

            function buildMaps() {
                var map = new Hashtable();

                map.put(vm.workflow.start.id, vm.workflow.start);
                map.put(vm.workflow.finish.id, vm.workflow.finish);

                mapStatuses.put(vm.workflow.start.diagramId, vm.workflow.start);
                mapStatuses.put(vm.workflow.finish.diagramId, vm.workflow.finish);

                angular.forEach(vm.workflow.statuses, function (status) {
                    status.approvers = [];
                    status.observers = [];
                    status.acknowledgers = [];
                    status.transitions = getTransitionsFromStatus(status.id);

                    map.put(status.id, status);
                    mapStatuses.put(status.diagramId, status);
                });

                angular.forEach(vm.workflow.transitions, function (transition) {
                    mapTransitions.put(transition.diagramId, transition);
                    var fromStatus = map.get(transition.fromStatus);
                    if (fromStatus != null) {
                        transition.fromStatusDiagramId = fromStatus.diagramId;
                        transition.fromStatusObject = fromStatus;
                    }

                    var toStatus = map.get(transition.toStatus);
                    if (toStatus != null) {
                        transition.toStatusDiagramId = toStatus.diagramId;
                        transition.toStatusObject = toStatus;
                    }
                });
            }

            function getTransitionsFromStatus(status) {
                var transitionArr = [];
                var transitions = vm.workflow.transitions;

                angular.forEach(transitions, function (t) {
                    if (t.fromStatus == status) {
                        t.fromStatusObject = getStatusById(status);
                        transitionArr.push(t);
                    }
                });

                return transitionArr;
            }

            function getTransitionsToStatus(status) {
                var transitionArr = null;
                var transitions = vm.workflow.transitions;

                angular.forEach(transitions, function (t) {
                    if (t.toStatus == status) {
                        transitionArr(t);
                    }
                });

                return transitionArr;
            }

            function initEvents(viewer) {
                var eventBus = viewer.get('eventBus');
                eventBus.on('element.click', function (e) {
                    // e.element = the model element
                    // e.gfx = the graphical element
                    if (e.element.type == 'bpmn:Task') {
                        var status = mapStatuses.get(e.element.id);
                        if (status != null) {
                            selectStatus(status);
                        }
                    }
                    else {
                        vm.selectedStatus = null;
                    }

                    $scope.$apply();
                });
            }

            function selectStatus(status) {
                vm.selectedStatus = status;
                $scope.$apply();
            }

            function resizeCanvas() {
                $timeout(function () {
                    var height = $("#contentpanel").height();
                    $("#canvas").height(height - 300);
                    if (viewer != null) {
                        viewer.get('canvas').zoom('fit-viewport');
                        var y = $('svg')[0].getBoundingClientRect().y;
                        var s = $('.viewport')[0].getBoundingClientRect().y;
                        var diff = s - y;
                        if (diff > 0) {
                            $('.viewport').attr('transform', 'translate(0, -' + diff + 100 + ')');
                        }
                    }
                }, 1000);
            }

            function getStatusById(id) {
                var found = null;

                if (vm.workflow.start.id == id) {
                    found = vm.workflow.start;
                }
                else if (vm.workflow.finish.id == id) {
                    found = vm.workflow.finish;
                }
                else {
                    angular.forEach(vm.workflow.statuses, function (status) {
                        if (status.id == id) {
                            found = status;
                        }
                    })
                }

                return found;
            }

            function loadWorkflow() {
                WorkflowDefinitionService.getWorkflow(vm.task.workflow).then(
                    function (data) {
                        vm.workflow = data;

                        buildMaps();

                        vm.workflow.currentStatusObject = getStatusById(vm.workflow.currentStatus);

                        viewer = new Modeler({container: '#canvas'});
                        viewer.importXML(vm.workflow.diagram, function (err) {
                            if (!err) {
                                resizeCanvas();
                                workflowLoaded = true;

                                $rootScope.$broadcast('eco.details.workflow.loaded',
                                    {workflowViewer: viewer, workflow: vm.workflow});
                                $rootScope.hideBusyIndicator();

                            } else {
                                console.log('Error loading workflow file:', err);
                                $rootScope.hideBusyIndicator();
                            }
                        });

                        initEvents(viewer);
                        processWorkflowFile(vm.workflow.diagram);
                        loadAssignments();

                    }
                )
            }

            vm.tabs = {
                history: {
                    id: 'details.workflow.history',
                    index: 0,
                    heading: 'History',
                    template: 'app/desktop/modules/tm/details/tabs/workflow/tabs/history/workflowHistoryView.jsp',
                    active: true
                }/*,
                 attributes: {
                 id: 'details.workflow.attributes',
                 index: 1,
                 heading: Attributes,
                 template: 'app/desktop/modules/change/eco/details/tabs/workflow/tabs/attributes/workflowAttributesView.jsp',
                 active: false
                 }*/
            };

            function addClassSVG(id, className) {
                var node = svgNodes.get(id);
                node.attr('class', function (index, existingClassNames) {
                    return existingClassNames + ' ' + className;
                });
            }

            function removeClassSVG(id, className) {
                var node = svgNodes.get(id);
                node.attr('class', function (index, existingClassNames) {
                    return existingClassNames.replace(className, '');
                });
            }

            function processWorkflowFile(xml) {
                var moddle = viewer._createModdle({});
                moddle.fromXML(xml, function (err, definitions) {
                    var rootElement = definitions.rootElements[0];
                    var flowElements = rootElement.flowElements;
                    angular.forEach(flowElements, function (element) {
                        if (element.$type == 'bpmn:StartEvent' ||
                            element.$type == 'bpmn:EndEvent' ||
                            element.$type == 'bpmn:Task') {
                            var node = $("[data-element-id='" + element.id + "']");
                            if (node.prop('tagName') == 'g') {
                                svgNodes.put(element.id, node)
                            }
                        }
                    });
                    updateStatusColors();
                });

            }

            function updateStatusColors() {
                setColors(vm.workflow.start);
                setColors(vm.workflow.finish);
                angular.forEach(vm.workflow.statuses, function (status) {
                    setColors(status);
                })
            }

            function setColors(status) {
                removeClassSVG(status.diagramId, 'inprocess-status');
                removeClassSVG(status.diagramId, 'completed-status');
                if (status.flag == 'INPROCESS') {
                    removeClassSVG(status.diagramId, 'completed-status');
                    addClassSVG(status.diagramId, 'inprocess-status');
                }
                else if (status.flag == 'COMPLETED') {
                    removeClassSVG(status.diagramId, 'inprocess-status');
                    addClassSVG(status.diagramId, 'completed-status');
                }
            }

            function showPersons(type) {
                var options = {
                    title: 'Select Person(s)',
                    template: 'app/desktop/modules/tm/details/tabs/workflow/personSelectionDialog.jsp',
                    controller: 'PersonSelectionController as personSelectionVm',
                    resolve: 'app/desktop/modules/tm/details/tabs/workflow/personSelectionController',
                    width: 450,
                    data: {
                        selectedPersonType: type
                    },
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: select, broadcast: 'app.workflow.persons'}
                    ],
                    callback: function (persons) {
                        addAssignments(type, persons);
                        $rootScope.hideSidePanel('left');
                    }
                };

                $rootScope.showSidePanel(options);

            }

            function addAssignments(type, persons) {
                var assignments = null;
                assignments = [];
                var promise = null;
                angular.forEach(persons, function (person) {
                    if (!isAlreadyInTheList(assignments, person)) {
                        var ass = {
                            status: vm.selectedStatus.id,
                            person: person.id,
                            editMode: false
                        };
                        if (type == 'approvers') {
                            ass.assignmentType = 'APPROVER';
                        }
                        assignments.push(ass);
                    }
                });

                if (type == 'approvers') {
                    promise = TaskService.addApprovers(vm.taskId, vm.selectedStatus.id, assignments);
                }
                if (assignments.length > 0 && promise != null) {
                    promise.then(
                        function (data) {
                            vm.selectedStatus.approvers = data;
                            CommonService.getPersonReferences(data, 'person');
                        }
                    )
                }

            }

            function isAlreadyInTheList(assignments, person) {
                var exists = false;
                angular.forEach(assignments, function (assignment) {
                    if (assignment.person == person.id) {
                        exists = true;
                    }
                });
                return exists;
            }

            function loadAssignments() {
                angular.forEach(vm.workflow.statuses, function (status) {
                    TaskService.getApprovers(vm.taskId, status.id).then(
                        function (data) {
                            initAssigments(data);
                            status.approvers = data;
                            CommonService.getPersonReferences(status.approvers, 'person');
                        }
                    );
                })
            }

            function initAssigments(assignments) {
                angular.forEach(assignments, function (assignment) {
                    assignment.editMode = false;
                });
            }

            function saveAssignment(assignment) {
                var promise = null;
                if (assignment.assignmentType == 'APPROVER') {
                    if (assignment.comments == null || assignment.comments == "") {
                        assignment.editMode = true;
                        $rootScope.showWarningMessage(enterComment)

                    } else {
                        promise = TaskService.addApprovers(vm.taskId, vm.selectedStatus.id, [assignment]);
                    }
                }
                if (promise != null) {
                    promise.then(
                        function (data) {
                            assignment.editMode = false;
                        }
                    )
                }
            }

            /*         vm.deletePerson = deletePerson;
             function deletePerson(person) {
             ECOService.deletePerson(person.id).then(
             function (data) {
             $rootScope.showSuccessMessage(person.assignmentType + ' : ' + vm.personRemove);
             loadAssignments();
             }
             )
             }

             function loadWorkflowHistory() {
             ECOService.getWorkflowHistory(vm.ecoId).then(
             function (data) {
             vm.workflowHistory = data;
             if (vm.workflow.started) {
             vm.workflowHistory.push({
             statusObject: {name: "Workflow Started"},
             timestamp: vm.workflow.startedOn
             })
             }

             if (vm.workflow.finished) {
             vm.workflowHistory.unshift({
             statusObject: {name: "Workflow Finished"},
             timestamp: vm.workflow.finishedOn
             })
             }

             $rootScope.hideBusyIndicator();
             }
             );
             }*/

            function loadProjectTask() {
                TaskService.getProjectTask(vm.projectId, vm.taskId).then(
                    function (data) {
                        vm.task = data;
                        loadWorkflow();
                    }
                )
            }

            (function () {
                $scope.$on('app.task.tabactivated', function (event, args) {
                    if (args.tabId == 'details.workflow') {
                        if (workflowLoaded == false) {
                            //initViewer();
                            $(window).resize(resizeCanvas);
                            /*$rootScope.showBusyIndicator($('.view-content'));*/
                        }
                        loadProjectTask();
                    }
                });
            })();
        }
    }
)
;