define(
    [
        'app/desktop/modules/workflow/workflow.module',
        'bpmn-viewer',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/workflow/workflowDefinitionService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController'
    ],
    function (module, Viewer) {
        module.controller('WorkflowEditorController', WorkflowEditorController);

        function WorkflowEditorController($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies, $translate,
                                          httpFactory, WorkflowDefinitionService) {
            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;
            var parse = angular.element("<div></div>");
            $rootScope.viewInfo.icon = "fa flaticon-plan2";
            $rootScope.viewInfo.title = parse.html($translate.instant('WORKFLOWEDITOR_ALL_TITLE')).html();

            vm.workflow = null;

            vm.mode = 'new';
            vm.selectedElement = null;
            vm.selectedStatus = null;

            var diagramLoaded = false;

            vm.newWorkflow = newWorkflow;
            vm.openWorkflow = openWorkflow;
            vm.saveWorkflow = saveWorkflow;
            vm.showSaveAsPanel = showSaveAsPanel;
            vm.updateStatus = updateStatus;
            vm.onEditorLoad = onEditorLoad;
            vm.autoNumber = autoNumber;
            vm.back = back;

            var mapStatuses = new Hashtable();
            var mapTransitions = new Hashtable();

            function onEditorLoad() {
                var mode = $stateParams.mode;
                if (mode != null && mode != undefined && mode == 'edit') {
                    vm.mode = 'edit';
                    var workflowId = $stateParams.workflow;

                    if (workflowId != null && workflowId != undefined) {
                        openWorkflow(workflowId);
                    }
                }
                else {
                    vm.newWorkflow();
                }
            }

            function resizeCanvas() {
                var height = $("#contentpanel").height();
                $("#canvas").height(height - 105);

                if (diagramLoaded == true) {
                    window.renderer.get('canvas').zoom('fit-viewport');
                }
            }

            function back() {
                window.history.back();
            }

            function newWorkflow() {
                httpFactory.get('app/desktop/modules/workflow/editor/newWorkflow.bpmn?bust=' + (new Date()).getTime()).then(
                    function (data) {
                        vm.workflow = {
                            id: null,
                            name: null,
                            workflowType: null,
                            description: null,
                            workflowNumber: null,
                            start: null,
                            finish: null,
                            assignableTo: [],
                            statuses: [],
                            transitions: []
                        };
                        loadWorkflow(data);
                    }
                );
            }

            function openWorkflow(workflowId) {
                WorkflowDefinitionService.getWorkflowDefinition(workflowId).then(
                    function (data) {
                        vm.workflow = data;
                        loadWorkflow(vm.workflow.diagram);
                    }
                );
            }

            /*---------  To get Selected Type default Number  -------------*/

            function autoNumber() {
                /*  if (vm.workflow.workflowType != null && vm.workflow.workflowType.workflowNumberSource != null) {
                 var source = vm.workflow.workflowType.workflowNumberSource;
                 AutonumberService.getNextNumber(source.id).then(
                 function (data) {
                 vm.workflow.workflowNumber = data;
                 }, function (error) {
                 $rootScope.showErrorMessage("Problem loading itemNumber.. Please create item once")
                 }
                 )
                 }*/
            }

            function loadWorkflow(xml) {
                $rootScope.hideBusyIndicator();
                window.openDiagram(xml, function () {
                    window.renderer.get('canvas').zoom('fit-viewport');

                    var eventBus = window.renderer.get('eventBus');

                    eventBus.on('element.click', function (e) {
                        // e.element = the model element
                        // e.gfx = the graphical element
                        vm.selectedElement = e.element;
                        onSelectElement(e.element);
                    });

                    eventBus.on('shape.add', function (e) {
                        vm.selectedElement = e.element;
                        $scope.$apply();
                        onAddElement(e.element);
                    });

                    eventBus.on('shape.remove', function (e) {
                        onDeleteElement(e.element);
                    });

                    eventBus.on('element.changed', function (e) {
                        //$scope.$apply();
                        onUpdateElement(e.element);
                    });

                    eventBus.on('connection.reconnectStart', function (e) {
                        console.log("Reconnect Start: " + e.element.id);
                    });

                    eventBus.on('connection.reconnectEnd', function (e) {
                        console.log("Reconnect End: " + e.element.id);
                    });

                    vm.selectedElement = {
                        type: 'bpmn:Process'
                    };
                    $scope.$apply();

                    if (vm.mode != null && vm.mode != undefined && vm.mode == 'new') {
                        initNewWorkflow(xml);
                    }
                    else {
                        buildMaps();
                    }

                    diagramLoaded = true;

                });
            }

            function buildMaps() {
                var map = new Hashtable();

                map.put(vm.workflow.start.id, vm.workflow.start);
                map.put(vm.workflow.finish.id, vm.workflow.finish);

                mapStatuses.put(vm.workflow.start.diagramId, vm.workflow.start);
                mapStatuses.put(vm.workflow.finish.diagramId, vm.workflow.finish);

                angular.forEach(vm.workflow.statuses, function (status) {
                    map.put(status.id, status);
                    mapStatuses.put(status.diagramId, status);
                });

                angular.forEach(vm.workflow.transitions, function (transition) {
                    mapTransitions.put(transition.diagramId, transition);
                    var fromStatus = map.get(transition.fromStatus);
                    if (fromStatus != null) {
                        transition.fromStatusDiagramId = fromStatus.diagramId;
                    }

                    var toStatus = map.get(transition.toStatus);
                    if (toStatus != null) {
                        transition.toStatusDiagramId = toStatus.diagramId;
                    }
                });
            }

            function onSelectElement(element) {
                vm.newCheckListItemMode = false;

                if (element.type == 'bpmn:Task') {
                    vm.selectedStatus = mapStatuses.get(element.id);
                }
                else {
                    vm.selectedElement = {
                        type: 'bpmn:Process'
                    };
                }
                $scope.$apply();
            }

            function onAddElement(element) {
                if (element.type == 'bpmn:Task') {
                    var status = {
                        id: null,
                        type: 'PENDING',
                        name: element.businessObject.name,
                        diagramId: element.id,
                        transitins: [],
                        tasks: []
                    };
                    vm.workflow.statuses.push(status);
                    mapStatuses.put(element.id, status);
                }
                else if (element.type == 'bpmn:SequenceFlow') {
                    var fromStatus = mapStatuses.get(element.sourceRef.id);
                    var toStatus = mapStatuses.get(element.targetRef.id);

                    var transition = {
                        id: null,
                        name: element.businessObject.name,
                        diagramId: element.id,
                        fromStatus: fromStatus.id,
                        fromStatusDiagramId: fromStatus.diagramId,
                        toStatus: toStatus.id,
                        toStatusDiagramId: toStatus.diagramId
                    };
                    vm.workflow.transitions.push(transition);
                    mapTransitions.put(element.id, transition);
                }
                else if (element.type == 'label') {
                    var bo = element.businessObject;
                    if (bo.$type == 'bpmn:SequenceFlow') {
                        var fromStatus = mapStatuses.get(bo.sourceRef.id);
                        var toStatus = mapStatuses.get(bo.targetRef.id);

                        var transition = {
                            id: null,
                            name: bo.name,
                            diagramId: bo.id,
                            fromStatus: fromStatus.id,
                            fromStatusDiagramId: fromStatus.diagramId,
                            toStatus: toStatus.id,
                            toStatusDiagramId: toStatus.diagramId
                        };
                        vm.workflow.transitions.push(transition);
                        mapTransitions.put(bo.id, transition);
                    }
                }
            }

            function onDeleteElement(element) {
                if (element.type == 'bpmn:Task') {
                    var status = mapStatuses.get(element.id);
                    if (status != null) {
                        var index = vm.workflow.statuses.indexOf(status);
                        vm.workflow.statuses.splice(index, 1);
                    }
                }
                else if (element.type == 'bpmn:SequenceFlow') {
                    var transition = mapTransitions.get(element.id);
                    var fromStatus = mapStatuses.get(element.fromStatus.id);
                    if (transition != null && fromStatus != null) {
                        var index = fromStatus.transitions.indexOf(transition);
                        fromStatus.transitions.splice(index, 1);
                    }
                }
                else if (element.type == 'label') {
                    var bo = element.businessObject;
                    if (bo.$type == 'bpmn:SequenceFlow') {
                        var transition = mapTransitions.get(bo.id);
                        var fromStatus = mapStatuses.get(bo.fromStatus.id);
                        if (transition != null && fromStatus != null) {
                            var index = fromStatus.transitions.indexOf(transition);
                            fromStatus.transitions.splice(index, 1);
                        }
                    }
                }
            }

            function onUpdateElement(element) {
                if (element.type == 'bpmn:Task') {
                    var status = mapStatuses.get(element.id);
                    if (status != null) {
                        $timeout(function () {
                            status.name = element.businessObject.name;
                        });
                    }
                }
                else if (element.type == 'bpmn:SequenceFlow') {
                    var transition = mapTransitions.get(element.id);
                    if (transition != null) {
                        transition.name = element.businessObject.name;

                        var currentFromStatus = mapStatuses.get(transition.fromStatusDiagramId);
                        var currentToStatus = mapStatuses.get(transition.toStatusDiagramId);

                        var fromStatus = mapStatuses.get(element.businessObject.sourceRef.id);
                        var toStatus = mapStatuses.get(element.businessObject.targetRef.id);

                        if (currentFromStatus != fromStatus) {
                            transition.fromStatus = fromStatus.id;
                            transition.fromStatusDiagramId = fromStatus.diagramId;
                        }

                        if (currentToStatus != toStatus) {
                            transition.toStatus = toStatus.id;
                            transition.toStatusDiagramId = toStatus.diagramId;
                        }
                    }
                }
                else if (element.type == 'label') {
                    if (element.businessObject.$type == 'bpmn:SequenceFlow') {
                        var transition = mapTransitions.get(element.businessObject.id);
                        if (transition != null) {
                            transition.name = element.businessObject.name;
                        }
                    }
                }
            }

            function updateStatus() {
                if (vm.selectedStatus != null) {
                    var modeling = window.renderer.get('modeling');
                    modeling.updateProperties(vm.selectedElement, {
                        name: vm.selectedStatus.name
                    });
                }
            }

            vm.workflowCreatedMsg = parse.html($translate.instant("WORKFLOW_CREATE_MESSAGE")).html;
            vm.workflowSavedMsg = parse.html($translate.instant("WORKFLOW_SAVED_MESSAGE")).html;
            vm.select = parse.html($translate.instant('SELECT')).html();

            function saveWorkflow() {
                if (validateWorkflow()) {
                    $rootScope.showBusyIndicator($('.contentpanel'));

                    window.saveDiagram(function (err, xml) {
                        var viewer = new Viewer();

                        var moddle = viewer._createModdle({});
                        moddle.fromXML(xml, function (err, definitions) {
                            if (vm.mode == 'edit') {
                                vm.workflow.diagram = xml;
                                WorkflowDefinitionService.updateWorkflowDefinition(vm.workflow).then(
                                    function (data) {
                                        $rootScope.hideBusyIndicator();
                                        $rootScope.showSuccessMessage(vm.workflowSavedMsg);
                                        $rootScope.showSuccessMessage("Workflow saved successfully")
                                    },
                                    function (error) {
                                        $rootScope.hideBusyIndicator();
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            }
                            else {
                                vm.workflow.diagram = xml;
                                WorkflowDefinitionService.createWorkflowDefinition(vm.workflow).then(
                                    function (data) {
                                        $state.go('app.workflow.all');
                                        $rootScope.hideBusyIndicator();
                                        $rootScope.showSuccessMessage("Workflow created successfully")
                                    },
                                    function (error) {
                                        $rootScope.hideBusyIndicator();
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            }
                        });
                    });
                }
            }

            function initNewWorkflow(xml) {
                var viewer = new Viewer();
                var moddle = viewer._createModdle({});
                moddle.fromXML(xml, function (err, definitions) {
                    var rootElement = definitions.rootElements[0];
                    var flowElements = rootElement.flowElements;

                    angular.forEach(flowElements, function (element) {
                        if (element.$type == 'bpmn:StartEvent') {
                            vm.workflow.start = {
                                id: null,
                                name: element.name,
                                description: null,
                                diagramId: element.id
                            };
                            mapStatuses.put(element.id, vm.workflow.start);
                        }
                        else if (element.$type == 'bpmn:EndEvent') {
                            vm.workflow.finish = {
                                id: null,
                                name: element.name,
                                description: null,
                                diagramId: element.id
                            };
                            mapStatuses.put(element.id, vm.workflow.finish);
                        }
                        else if (element.$type == 'bpmn:Task') {
                            var status = {
                                id: null,
                                name: null,
                                description: null,
                                diagramId: element.id
                            };
                            vm.workflow.statuses.push(status);
                            mapStatuses.put(element.id, status);
                        }
                        else if (element.$type == 'bpmn:SequenceFlow') {
                            var transition = {
                                id: null,
                                name: element.name,
                                diagramId: element.id,
                                fromStatus: mapStatuses.get(element.sourceRef),
                                toStatus: mapStatuses.get(element.targetRef),
                                fromStatusDiagramId: element.sourceRef,
                                toStatusDiagramId: element.targetRef
                            };
                            vm.workflow.transitions.push(transition);
                            mapTransitions.put(element.id, transition);
                        }
                    });

                });
            }

            function validateWorkflow() {
                var workflowValid = isWorkflowValid();
                var statusesValid = (workflowValid == true ? areStatusesValid() : true);
                var transitionsValid = (statusesValid == true ? areTransitionsValid() : true);
                var pathsValid = (transitionsValid == true ? areAllPathsValid() : true);

                return (workflowValid && statusesValid && transitionsValid && pathsValid);
            }

            var workflowType = $translate.instant("WORKFLOW_TYPE_VALIDATE");
            var workflowName = $translate.instant("WORKFLOW_NAME_VALIDATE");
            var workflowStatusName = $translate.instant("WORKFLOW_STATUS_VALIDATE");
            var workflowReleased = parse.html($translate.instant("WORKFLOW_RELEASED_VALIDATE")).html();
            var saveAs = $translate.instant("WORKFLOW_SAVEAS_TITLE");

            function isWorkflowValid() {
                var valid = true;
                if (vm.workflow.name == null || vm.workflow.name.trim() == "") {
                    $rootScope.showErrorMessage(workflowName);
                    valid = false;
                }
                return valid;
            }

            var atleastOneTransition = parse.html($translate.instant("AT_LEAST_ONE_TRANSITION")).html();
            var workflowReleasedState = parse.html($translate.instant("WORKFLOW_RELEASED_STATE_CONNECTION")).html();
            var workflowFinishState = parse.html($translate.instant("WORKFLOW_FINISH_STATE_CONNECTION")).html();
            var duplicateStatusNameValidation = $translate.instant("DUPLICATE_STATUS_NAME_VALID");

            function areStatusesValid() {
                var valid = true;
                var statuses = vm.workflow.statuses;
                var uniq = [];
                angular.forEach(statuses, function (status) {
                    if (uniq.indexOf(status['name']) == -1) {
                        uniq.push(status['name']);
                    }

                    if (status.name == null || status.name.trim() == "") {
                        $rootScope.showErrorMessage(workflowStatusName);
                        valid = false;
                    }
                    else if (!hasAnyTransitions(status)) {
                        $rootScope.showErrorMessage(atleastOneTransition);
                        valid = false;
                    }

                    if (status.type != null) {
                        angular.forEach(vm.workflow.transitions, function (transition) {
                            if (status.diagramId == transition.fromStatusDiagramId && transition.toStatusDiagramId != vm.workflow.finish.diagramId) {
                                if (status.type == "RELEASED" && transition.toStatusDiagramId == vm.workflow.finish.diagramId) {
                                    $rootScope.showWarningMessage("Released state should always be link only to the Finish");
                                    valid = false;
                                }

                            } else if (status.diagramId == transition.fromStatusDiagramId && transition.toStatusDiagramId == vm.workflow.finish.diagramId) {
                                if (status.type != "RELEASED") {
                                    $rootScope.showWarningMessage("Finish should always be connected with Release state only");
                                    valid = false;
                                }
                            }
                        });
                    }

                });

                if (valid && statuses.length > uniq.length) {
                    $rootScope.showErrorMessage();
                    valid = false;
                }

                return valid;
            }

            function hasAnyTransitions(status) {
                var has = false;

                angular.forEach(vm.workflow.transitions, function (transition) {
                    if (status.diagramId == transition.fromStatusDiagramId ||
                        status.diagramId == transition.toStatusDiagramId) {
                        has = true;
                    }
                });

                return has;
            }

            var transitionNameValidation = parse.html($translate.instant("TRANSITION_NAME_VALIDATION")).html();

            function areTransitionsValid() {
                var valid = true;

                var statuses = vm.workflow.statuses;
                angular.forEach(statuses, function (status) {
                    var transitions = status.transitions;
                    angular.forEach(transitions, function (transition) {

                        if (transition.name == null || transition.name.trim() == "") {
                            $rootScope.showErrorMessage(transitionNameValidation);
                            valid = false;
                        }
                    });
                });

                return valid;
            }

            var duplicateTransitionNameValidation = parse.html($translate.instant("DUPLICATE_TRANSITION_NAME_VALIDATION")).html();
            var incomingTransitionValidation = parse.html($translate.instant("INCOMING_TRANSITION_VALIDATION")).html();

            function areAllPathsValid() {
                var valid = true;

                var statuses = vm.workflow.statuses;
                var dists = []
                angular.forEach(statuses, function (status) {
                    if (status.type != 'UNDEFINED') {
                        var transitions = getToTransitions(status);
                        if (transitions.name != null && transitions.name != "") {
                            if (dists.indexOf(transitions.name) != -1) {
                                $rootScope.showErrorMessage(duplicateTransitionNameValidation);
                                valid = false;
                            } else {
                                dists.push(transitions.name);
                            }
                        }
                        if (transitions == null) {
                            $rootScope.showErrorMessage(incomingTransitionValidation);
                            valid = false;
                        }
                    }
                });

                return valid;
            }

            function getToTransitions(status) {
                var toTransition = null;
                var transitions = vm.workflow.transitions;
                angular.forEach(transitions, function (transition) {
                    if (status.diagramId == transition.toStatusDiagramId) {
                        toTransition = transition;
                    }
                });

                return toTransition;
            }

            var saveButton = $translate.instant("SAVE");

            function showSaveAsPanel() {

                window.saveDiagram(function (err, xml) {
                    var viewer = new Viewer();

                    var moddle = viewer._createModdle({});
                    moddle.fromXML(xml, function (err, definitions) {
                        vm.workflow.diagram = xml;

                        var options = {
                            title: saveAs,
                            template: 'app/desktop/modules/workflow/all/saveAsView.jsp',
                            controller: 'SaveWorkflowAsController as saveAsVm',
                            resolve: 'app/desktop/modules/workflow/all/saveAsController',
                            showMask: true,
                            width: 400,
                            buttons: [
                                {text: saveButton, broadcast: 'app.workflow.saveas'}
                            ],
                            data: {
                                workflow: vm.workflow
                            },
                            callback: function (data) {
                                $state.go('app.workflow.editor', {mode: 'edit', workflow: data.id});
                            }
                        };
                        $rootScope.showSidePanel(options);
                    });
                });
            }

            (function () {
                resizeCanvas();
                $(window).resize(resizeCanvas);

            })();
        }
    }
);