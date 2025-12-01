define(
    [
        'app/desktop/modules/workflow/workflow.module',
        'bpmn-viewer',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/workflowDefinitionService',
        'app/desktop/modules/workflow/directive/workflowtypeDirective',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/mesObjectTypeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/desktop/modules/directives/classificationAttributesDirective',
        'app/shared/services/core/classificationService'
    ],
    function (module, Viewer) {
        module.controller('WorkflowEditorController', WorkflowEditorController);

        function WorkflowEditorController($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies, $translate, MESObjectTypeService,
                                          httpFactory, WorkflowDefinitionService, AutonumberService, ItemTypeService, DialogService, ClassificationService) {

            var vm = this;
            var parse = angular.element("<div></div>");
            var message1 = parse.html($translate.instant("WORKFLOW_VALIDATION_ONLY_REJECTED_CAN_CONNECT_TO_CANCEL")).html();
            var message2 = parse.html($translate.instant("WORKFLOW_VALIDATION_ONLY_RELEASED_CAN_CONNECT_TO_FINISH")).html();
            var message3 = parse.html($translate.instant("WORKFLOW_SAVED_SUCCESSFULLY")).html();
            var message4 = parse.html($translate.instant("WORKFLOW_VALIDATION_FINISH_MUST_BE_CONNECTED_TO_RELEASED")).html();
            var message5 = parse.html($translate.instant("WORKFLOW_VALIDATION_CANCEL_MUST_BE_CONNECTED_TO_REJECTED")).html();
            var message6 = parse.html($translate.instant("WORKFLOW_VALIDATION_ACTIVITY_MUST_HAVE_ONE_INCOMING_TRANSITION")).html();
            var message7 = parse.html($translate.instant("WORKFLOW_VALIDATION_ACTIVITY_MUST_HAVE_ONE_OUTGOING_TRANSITION")).html();

            var workflowEventSaved = parse.html($translate.instant("WORKFLOW_EVENT_SAVED")).html();
            var workflowEventDeleted = parse.html($translate.instant("WORKFLOW_EVENT_DELETED")).html();
            var workflowEventDialogMsg = parse.html($translate.instant("W_E_DIALOG_MSG")).html();
            var pleaseSelectLifecyclePhase = parse.html($translate.instant("P_S_LIFECYCLE_PHASE")).html();
            var addOneLifecycle = parse.html($translate.instant("ADD_ATLEAST_ONE_LIFECYCLE")).html();
            $rootScope.viewInfo.icon = "fa flaticon-plan2";
            /*$rootScope.viewInfo.title = parse.html($translate.instant('WORKFLOWEDITOR_ALL_TITLE')).html();*/
            var pleaseSelectActivity = parse.html($translate.instant("P_S_ACTIVITY")).html();

            vm.workflow = null;

            vm.mode = 'new';
            vm.selectedElement = null;
            vm.selectedStatus = null;

            var diagramLoaded = false;
            var selectedBpmnElement = null;

            vm.newWorkflow = newWorkflow;
            vm.openWorkflow = openWorkflow;
            vm.saveWorkflow = saveWorkflow;
            vm.showSaveAsPanel = showSaveAsPanel;
            vm.updateStatus = updateStatus;
            vm.onEditorLoad = onEditorLoad;
            vm.autoNumber = autoNumber;
            vm.gotoAllWorkflows = gotoAllWorkflows;
            vm.onSelectType = onSelectType;
            vm.onSelectStatusType = onSelectStatusType;

            var mapStatuses = new Hashtable();
            var mapTransitions = new Hashtable();

            var wfNameChangeListener = null;
            var wfDescriptionChangeListener = null;
            var nameChangeListener = null;
            var typeChangeListener = null;

            var unsavedChanges = false;

            function onEditorLoad() {
                var mode = $stateParams.mode;
                if (mode != null && mode != undefined && mode == 'edit') {
                    vm.mode = 'edit';
                    $rootScope.viewInfo.showDetails = true;
                    var workflowId = $stateParams.workflow;

                    if (workflowId != null && workflowId != undefined) {
                        resizeContainer(function () {
                            openWorkflow(workflowId);
                        });
                    }
                }
                else {
                    vm.newWorkflow();
                    selectWorkflowTab("details.basic");
                    $rootScope.viewInfo.showDetails = false;
                }
            }

            function onSelectType(workflowType) {
                vm.attributes = [];
                vm.requiredAttributes = [];
                if (workflowType != null && workflowType != undefined) {
                    vm.workflow.workflowType = workflowType;
                    WorkflowDefinitionService.getWorkflowAttributesWithHierarchy(vm.workflow.workflowType.id).then(
                        function (data) {
                            angular.forEach(data, function (attribute) {
                                var att = {
                                    id: {
                                        objectId: vm.workflow.id,
                                        attributeDef: attribute.id

                                    },
                                    attributeDef: attribute,
                                    listValue: null,
                                    newListValue: null,
                                    listValueEditMode: false,
                                    booleanValue: false,
                                    integerValue: null,
                                    stringValue: null,
                                    doubleValue: null,
                                    imageValue: null,
                                    refValue: null,
                                    timestampValue: moment(new Date()).format("DD/MM/YYYY, HH:mm:ss"),
                                    ref: null,
                                    attachmentValues: []
                                };
                                if (attribute.lov != null) {
                                    att.lovValues = attribute.lov.values;
                                }
                                if (attribute.required == false) {
                                    vm.attributes.push(att);
                                } else {
                                    vm.requiredAttributes.push(att);
                                }
                            });
                            autoNumber();
                            loadWfType();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }

            }

            function loadWfType() {
                if (vm.workflow.workflowType.assignable == 'REQUIREMENT DOCUMENTS' || vm.workflow.workflowType.assignable == 'REQUIREMENT') {
                    if (vm.workflow.workflowType.assignedType != null && vm.workflow.workflowType.assignedType != "" && vm.workflow.workflowType.assignedType != undefined) {
                        ClassificationService.getAssignedObjectType(vm.workflow.workflowType.id, vm.workflow.workflowType.assignedType, vm.workflow.workflowType.assignable).then(
                            function (data) {
                                $scope.reqTypeName = data.name;
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                }
            }

            function resizeCanvas() {
                var height = $("#contentpanel").height();
                $("#canvas").height(height - 73);

                if (diagramLoaded === true) {
                    if (vm.workflow.released) {
                        readonlyViewer.get('canvas').zoom('fit-viewport', 'auto');
                    }
                    else {
                        window.renderer.get('canvas').zoom('fit-viewport', 'auto');
                    }
                }

                var height1 = $(window).height();
                $('.workflow-tabs').height(height1 - 210);
                $('.workflow-tabs .tab-content').height(height1 - 260);
                $('.workflow-tabs .tab-content .tab-pane').height(height1 - 260);
                $('#workflow-details').height($('.workflow-tabs .tab-content .tab-pane').outerHeight());
                $('#events-view').height($('.workflow-tabs .tab-content .tab-pane').outerHeight());
            }

            function resizeContainer(callback) {
                $timeout(function () {
                    var height = $("#contentpanel").height();
                    $("#canvas").height(height - 73);
                    if (callback != undefined) {
                        callback();
                    }
                }, 1000)
            }

            function gotoAllWorkflows() {
                if (unsavedChanges) {
                    var options = {
                        title: saveWorkflowTitle,
                        message: workflowModifiedSaveMsg,
                        okButtonText: "Yes",
                        cancelButtonText: "No"
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes) {
                            saveWorkflow(function () {
                                showAllView();
                            });
                        }
                        else {
                            showAllView();
                        }
                    });
                }
                else {
                    showAllView();
                }
            }

            function showAllView() {
                if ($rootScope.allWorkflowType == "instances") {
                    $state.go('app.workflow.instances');
                } else {
                    $state.go('app.workflow.all');
                }
            }

            function newWorkflow() {
                httpFactory.get('app/desktop/modules/workflow/editor/newWorkflow.bpmn?bust=' + (new Date()).getTime()).then(
                    function (data) {
                        vm.workflow = {
                            id: null,
                            name: null,
                            workflowType: null,
                            description: null,
                            number: null,
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
                        vm.workflow.number = vm.workflow.master.number;
                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                            "{0}</div> <span class='item-rev'>Rev {1}</span>".
                                format(vm.workflow.master.number,
                                vm.workflow.revision);
                        setLifecycles();

                        if (vm.workflow.released) {
                            loadWorkflowInReadOnlyMode();
                        }
                        else {
                            loadWorkflow(vm.workflow.diagram);
                        }
                        loadAssignableType();
                        loadItemTypeLifecycles();
                        loadMBOMTypeLifecycles();
                        loadWorkflowStatuses();
                        loadWfType();
                        selectWorkflowTab("details.basic");
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            vm.workflowAssignableType = null;
            function loadAssignableType() {
                if (vm.workflow != null && vm.workflow.workflowType.assignable != null && vm.workflow.workflowType.assignedType != null && vm.workflow.workflowType.assignedType != "") {
                    WorkflowDefinitionService.getWorkflowAssignableTypeObjectType(vm.workflow.workflowType.assignedType).then(
                        function (data) {
                            vm.workflowAssignableType = data;
                            if (vm.workflowAssignableType.objectType == 'ECOTYPE' || vm.workflowAssignableType.objectType == 'DCOTYPE'
                                || vm.workflowAssignableType.objectType == 'MCOTYPE' || vm.workflowAssignableType.objectType == 'BOPTYPE') {
                                $scope.workflowActionTypes = [
                                    {label: 'Set Lifecycle Phase', value: 'SetLifecyclePhase'},
                                    {label: 'Execute Script', value: 'ExecuteScript'}
                                ];
                                if (vm.workflowAssignableType.objectType == 'BOPTYPE') {
                                    vm.workflowAssignableType.lifecycle.normalLifecyclePhases = [];
                                    vm.workflowAssignableType.lifecycle.releasedLifecyclePhases = [];
                                    vm.workflowAssignableType.lifecycle.rejectedLifecyclePhases = [];
                                    angular.forEach(vm.workflowAssignableType.lifecycle.phases, function (phase) {
                                        if (phase.phaseType == "RELEASED") {
                                            vm.workflowAssignableType.lifecycle.releasedLifecyclePhases.push(phase);
                                        } else if (phase.phaseType == "CANCELLED") {
                                            vm.workflowAssignableType.lifecycle.rejectedLifecyclePhases.push(phase);
                                        } else if (phase.phaseType != "OBSOLETE") {
                                            vm.workflowAssignableType.lifecycle.normalLifecyclePhases.push(phase);
                                        }
                                    })
                                }
                            } else {
                                $scope.workflowActionTypes = [
                                    {label: 'Execute Script', value: 'ExecuteScript'}
                                ];
                            }
                        }
                    )
                }
            }

            /*---------  To get Selected Type default Number  -------------*/

            function autoNumber() {
                if (vm.workflow.workflowType != null && vm.workflow.workflowType.numberSource != null) {
                    var source = vm.workflow.workflowType.numberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.workflow.number = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message)
                        }
                    )
                }
            }

            var readonlyViewer = null;

            function loadWorkflowInReadOnlyMode() {
                readonlyViewer = new Viewer({
                    container: '#canvas',
                    additionalModules: [{
                        zoomScroll: ['value', '']
                    }]
                });
                readonlyViewer.importXML(vm.workflow.diagram, function (err) {
                    if (!err) {
                        diagramLoaded = true;
                        $timeout(function () {
                            resizeCanvas();
                        });
                        buildMaps();
                        var eventBus = readonlyViewer.get('eventBus');

                        eventBus.on('element.click', function (e) {
                            // e.element = the model element
                            // e.gfx = the graphical element
                            vm.selectedElement = e.element;
                            onSelectElement(e.element);
                        });

                        vm.selectedElement = {
                            type: 'bpmn:Process'
                        };
                        $scope.$apply();

                        $rootScope.hideBusyIndicator();

                    } else {
                        console.log('Error loading workflow file:', err);
                        $rootScope.hideBusyIndicator();
                    }
                });
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
                    $scope.workflow = vm.workflow;

                    wfNameChangeListener = $scope.$watch('workflow.name', function (newval, oldval) {
                        if (newval !== oldval) {
                            unsavedChanges = true;
                        }
                    });

                    wfDescriptionChangeListener = $scope.$watch('workflow.description', function (newval, oldval) {
                        if (newval !== oldval) {
                            unsavedChanges = true;
                        }
                    });

                    resizeCanvas();
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

            $scope.selectedStatus = null;
            function onSelectElement(element) {
                selectedBpmnElement = element;
                vm.newCheckListItemMode = false;

                if (element.type === 'bpmn:StartEvent') {
                    if (element.outgoing.length > 0) {
                        $("#workflowFrame").contents().find(".djs-overlays-" + element.id).addClass("hide-all");
                    }
                    else {
                        $("#workflowFrame").contents().find(".djs-overlays-" + element.id).removeClass("hide-all");
                    }
                }
                else if (element.type === 'bpmn:Task') {
                    vm.selectedStatus = mapStatuses.get(element.id);
                    $scope.selectedStatus = vm.selectedStatus;
                    $rootScope.selectedClassificationType = vm.selectedStatus;
                    element.activity = mapStatuses.get(element.id);

                    var type = vm.selectedStatus.type;

                    onSelectStatusType(type);

                    if (type === 'REJECTED') {
                        if (isConnectedToCancel(vm.selectedStatus)) {
                            $("#workflowFrame").contents().find(".djs-overlays-" + element.id).addClass("hide-all");
                        }
                        else {
                            $("#workflowFrame").contents().find(".djs-overlays-" + element.id).removeClass("hide-all");
                        }
                    }
                    else if (type === 'RELEASED') {
                        if (isConnectedToFinish(vm.selectedStatus)) {
                            $("#workflowFrame").contents().find(".djs-overlays-" + element.id).addClass("hide-all");
                        }
                        else {
                            $("#workflowFrame").contents().find(".djs-overlays-" + element.id).removeClass("hide-all");
                        }
                    }

                    if (nameChangeListener != null) {
                        nameChangeListener();
                    }

                    if (typeChangeListener != null) {
                        typeChangeListener();
                    }

                    nameChangeListener = $scope.$watch('selectedStatus.name', function (newval, oldval) {
                        if (oldval !== newval) {
                            unsavedChanges = true;
                        }
                        element.name = newval;
                    });

                    typeChangeListener = $scope.$watch('selectedStatus.type', function (newval, oldval) {
                        if (oldval !== newval) {
                            var activity = $scope.selectedStatus;
                            if (oldval === 'REJECTED') {
                                if (isConnectedToCancel(activity)) {
                                    $rootScope.showWarningMessage(message1);
                                    activity.type = oldval;
                                    $timeout(function () {
                                        onSelectStatusType('REJECTED')
                                    }, 500);
                                }
                            }
                            else if (oldval === 'RELEASED') {
                                if (isConnectedToFinish(activity)) {
                                    $rootScope.showWarningMessage(message2);
                                    activity.type = oldval;
                                    $timeout(function () {
                                        onSelectStatusType('RELEASED')
                                    }, 500);
                                }
                                else if (newval === 'NORMAL') {
                                    $timeout(function () {
                                        onSelectStatusType('NORMAL')
                                    }, 500);
                                }
                            }
                            else if (oldval === 'NORMAL' && newval === 'REJECTED' && isConnectedToReleased($scope.selectedStatus)) {
                                $rootScope.showWarningMessage('A rejected activity cannot be connected to a released activity');
                                activity.type = oldval;
                                $timeout(function () {
                                    onSelectStatusType('NORMAL')
                                }, 500);
                            }
                            else if (oldval === 'NORMAL' && newval === 'REJECTED' && isConnectedToNormal($scope.selectedStatus)) {
                                $rootScope.showWarningMessage('A rejected activity can only be connected to cancel');
                                activity.type = oldval;
                                $timeout(function () {
                                    onSelectStatusType('NORMAL')
                                }, 500);
                            }
                            else if (oldval === 'NORMAL' && newval === 'RELEASED' && isConnectedToReleased($scope.selectedStatus)) {
                                $rootScope.showWarningMessage('A released activity cannot be connected to another released activity');
                                activity.type = oldval;
                                $timeout(function () {
                                    onSelectStatusType('NORMAL')
                                }, 500);
                            }
                            else if (oldval === 'NORMAL' && newval === 'RELEASED' && isConnectedToNormal($scope.selectedStatus)) {
                                $rootScope.showWarningMessage('A released activity can only be connected to finish');
                                activity.type = oldval;
                                $timeout(function () {
                                    onSelectStatusType('NORMAL')
                                }, 500);
                            }
                            else if (oldval === 'NORMAL' && newval === 'REJECTED' && isConnectedToRejected($scope.selectedStatus)) {
                                $rootScope.showWarningMessage('A rejected activity cannot be connected to another rejected activity');
                                activity.type = oldval;
                                $timeout(function () {
                                    onSelectStatusType('NORMAL')
                                }, 500);
                            }
                            else {
                                unsavedChanges = true;
                            }
                        }
                    });
                }
                else {
                    vm.selectedElement = {
                        type: 'bpmn:Process'
                    };

                    if (nameChangeListener != null) {
                        nameChangeListener();
                    }

                    if (typeChangeListener != null) {
                        typeChangeListener();
                    }
                    if (vm.workflow != null && vm.workflow.id != null) {
                        loadEvents();
                    }
                }
                $scope.$apply();
            }

            function isConnectedToCancel(status) {
                var transitions = vm.workflow.transitions;
                for (var i = 0; i < transitions.length; i++) {
                    var fromStatusId = transitions[i].fromStatusDiagramId;

                    var toStatusId = transitions[i].toStatusDiagramId;
                    var toStatus = mapStatuses.get(toStatusId);
                    if (status.diagramId === fromStatusId && toStatus.type === "TERMINATE") {
                        return true;
                    }
                }
                return false;
            }

            function isConnectedToFinish(status) {
                var transitions = vm.workflow.transitions;
                for (var i = 0; i < transitions.length; i++) {
                    var fromStatusId = transitions[i].fromStatusDiagramId;

                    var toStatusId = transitions[i].toStatusDiagramId;
                    var toStatus = mapStatuses.get(toStatusId);
                    if (status.diagramId === fromStatusId && toStatus['@type'] === "PLMWorkflowDefinitionFinish") {
                        return true;
                    }
                }
                return false;
            }

            function isConnectedToNormal(status) {
                var transitions = vm.workflow.transitions;
                for (var i = 0; i < transitions.length; i++) {
                    var fromStatusId = transitions[i].fromStatusDiagramId;

                    var toStatusId = transitions[i].toStatusDiagramId;
                    var toStatus = mapStatuses.get(toStatusId);
                    if (status.diagramId === fromStatusId && toStatus.type === "NORMAL") {
                        return true;
                    }
                }
                return false;
            }

            function isConnectedToReleased(status) {
                var transitions = vm.workflow.transitions;
                for (var i = 0; i < transitions.length; i++) {
                    var fromStatusId = transitions[i].fromStatusDiagramId;

                    var toStatusId = transitions[i].toStatusDiagramId;
                    var toStatus = mapStatuses.get(toStatusId);
                    if (status.diagramId === fromStatusId && toStatus.type === "RELEASED") {
                        return true;
                    }
                }
                return false;
            }

            function isConnectedToRejected(status) {
                var transitions = vm.workflow.transitions;
                for (var i = 0; i < transitions.length; i++) {
                    var fromStatusId = transitions[i].fromStatusDiagramId;

                    var toStatusId = transitions[i].toStatusDiagramId;
                    var toStatus = mapStatuses.get(toStatusId);
                    if (status.diagramId === fromStatusId && toStatus.type === "REJECTED") {
                        return true;
                    }
                }
                return false;
            }

            function onAddElement(element) {
                if (element.type === 'bpmn:Task') {
                    var status = {
                        id: null,
                        type: 'NORMAL',
                        "@type": 'PLMWorkflowDefinitionStatus',
                        name: element.businessObject.name,
                        diagramId: element.id,
                        transitions: [],
                        tasks: []
                    };
                    element.activity = status;
                    vm.workflow.statuses.push(status);
                    mapStatuses.put(element.id, status);

                    selectedBpmnElement = element;
                    $timeout(function () {
                        onSelectStatusType('NORMAL');
                    });
                }
                else if (element.type === 'bpmn:IntermediateThrowEvent') {
                    element.businessObject.name = "Cancel";
                    var terminate = {
                        id: null,
                        type: 'TERMINATE',
                        "@type": 'PLMWorkflowDefinitionTerminate',
                        name: element.businessObject.name,
                        diagramId: element.id,
                        transitions: [],
                        tasks: []
                    };
                    vm.workflow.statuses.push(terminate);
                    mapStatuses.put(element.id, terminate);
                }
                else if (element.type === 'bpmn:SequenceFlow') {
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

                    if (fromStatus.type === 'REJECTED' && toStatus.type === 'TERMINATE') {
                        $("#workflowFrame").contents().find(".djs-overlays-" + fromStatus.diagramId).addClass("hide-all");
                    }
                    else if (fromStatus.type === 'START') {
                        $("#workflowFrame").contents().find(".djs-overlays-" + fromStatus.diagramId).addClass("hide-all");
                    }
                }
                else if (element.type === 'label') {
                    var bo = element.businessObject;
                    if (bo.$type === 'bpmn:SequenceFlow') {
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

                        if (fromStatus.type === 'REJECTED' && toStatus.type === 'TERMINATE') {
                            $("#workflowFrame").contents().find(".djs-overlays-" + fromStatus.diagramId).addClass("hide-all");
                        }
                        else if (fromStatus['@type'] === 'PLMWorkflowDefinitionStart') {
                            $("#workflowFrame").contents().find(".djs-overlays-" + fromStatus.diagramId).addClass("hide-all");
                        }
                    }
                }

                unsavedChanges = true;
            }

            function onDeleteElement(element) {
                if (element.type === 'bpmn:Task' || element.type === 'bpmn:IntermediateThrowEvent') {
                    var status = mapStatuses.get(element.id);
                    if (status != null) {
                        var index = vm.workflow.statuses.indexOf(status);
                        vm.workflow.statuses.splice(index, 1);
                        if (status.id != null) {
                            deleteWfDefStatus(status)
                        }
                    }

                }
                else if (element.type === 'bpmn:SequenceFlow') {
                    var bo = element.businessObject;
                    var transition = mapTransitions.get(element.id);
                    if (transition != null) {
                        index = vm.workflow.transitions.indexOf(transition);
                        vm.workflow.transitions.splice(index, 1);
                        if (transition.id != null) {
                            deleteWfDefTransition(transition)
                        }
                    }

                    var fromStatus = mapStatuses.get(bo.sourceRef.id);
                    if (fromStatus.type === 'REJECTED' && !isConnectedToCancel(fromStatus)) {
                        $("#workflowFrame").contents().find(".djs-overlays-" + fromStatus.diagramId).removeClass("hide-all");
                    }
                }
                else if (element.type === 'label') {
                    var bo = element.businessObject;
                    if (bo.$type === 'bpmn:SequenceFlow') {
                        var transition = mapTransitions.get(bo.id);
                        if (transition != null) {
                            index = vm.workflow.transitions.indexOf(transition);
                            vm.workflow.transitions.splice(index, 1);
                            if (transition.id != null) {
                                deleteWfDefTransition(transition)
                            }
                        }

                        var fromStatus = mapStatuses.get(bo.sourceRef.id);
                        if (fromStatus.type === 'REJECTED' && !isConnectedToCancel(fromStatus)) {
                            $("#workflowFrame").contents().find(".djs-overlays-" + fromStatus.diagramId).removeClass("hide-all");
                        }
                        else if (fromStatus['@type'] === 'PLMWorkflowDefinitionStart' && !isConnectedToNormal(fromStatus)) {
                            $("#workflowFrame").contents().find(".djs-overlays-" + fromStatus.diagramId).removeClass("hide-all");
                        }
                    }
                }
                unsavedChanges = true;
            }

            function deleteWfDefTransition(transition) {
                WorkflowDefinitionService.deleteWorkflowDefinitionTransition(vm.workflow.id, transition.id).then(
                    function (data) {

                    }
                )
            }

            function deleteWfDefStatus(status) {
                WorkflowDefinitionService.deleteWorkflowDefinitionStatus(vm.workflow.id, status.id).then(
                    function (data) {

                    }
                )
            }

            function onUpdateElement(element) {
                if (element.type === 'bpmn:Task') {
                    var status = mapStatuses.get(element.id);
                    if (status != null) {
                        $timeout(function () {
                            status.name = element.businessObject.name;
                        });
                    }
                }
                else if (element.type === 'bpmn:SequenceFlow') {
                    var transition = mapTransitions.get(element.id);
                    if (transition != null) {
                        transition.name = element.businessObject.name;

                        var currentFromStatus = mapStatuses.get(transition.fromStatusDiagramId);
                        var currentToStatus = mapStatuses.get(transition.toStatusDiagramId);

                        var fromStatus = mapStatuses.get(element.businessObject.sourceRef.id);
                        var toStatus = mapStatuses.get(element.businessObject.targetRef.id);

                        if (currentFromStatus !== fromStatus) {
                            transition.fromStatus = fromStatus.id;
                            transition.fromStatusDiagramId = fromStatus.diagramId;
                        }

                        if (currentToStatus !== toStatus) {
                            transition.toStatus = toStatus.id;
                            transition.toStatusDiagramId = toStatus.diagramId;
                        }
                    }
                }
                else if (element.type === 'label') {
                    if (element.businessObject.$type === 'bpmn:SequenceFlow') {
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

            function saveWorkflow(callback) {
                if (validateWorkflow()) {
                    $rootScope.showBusyIndicator($('.contentpanel'));

                    angular.forEach(vm.workflow.statuses, function (status) {
                        if (status.diagramId.startsWith("StartEvent_")) {
                            status['@type'] = 'PLMWorkflowDefinitionStart';
                        }
                        else if (status.diagramId.startsWith("EndEvent_")) {
                            status['@type'] = 'PLMWorkflowDefinitionFinish';
                        }
                    });

                    window.saveDiagram(function (err, xml) {
                        var viewer = new Viewer();

                        var moddle = viewer._createModdle({});
                        moddle.fromXML(xml, function (err, definitions) {
                            if (vm.mode == 'edit') {
                                vm.workflow.diagram = xml;
                                WorkflowDefinitionService.updateWorkflowDefinition(vm.workflow).then(
                                    function (data) {
                                        vm.workflow.id = data.id;
                                        $rootScope.hideBusyIndicator();
                                        $rootScope.showSuccessMessage(message3);
                                        unsavedChanges = false;

                                        if (callback != undefined) {
                                            callback();
                                        }
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
                                        vm.workflow.id = data.id;
                                        $state.go('app.workflow.all');
                                        $rootScope.hideBusyIndicator();
                                        $rootScope.showSuccessMessage(message3);
                                        unsavedChanges = false;

                                        if (callback != undefined) {
                                            callback();
                                        }
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
                        if (element.$type === 'bpmn:StartEvent') {
                            vm.workflow.start = {
                                id: null,
                                '@type': 'PLMWorkflowDefinitionStart',
                                name: element.name,
                                description: null,
                                diagramId: element.id
                            };
                            mapStatuses.put(element.id, vm.workflow.start);
                        }
                        else if (element.$type === 'bpmn:EndEvent') {
                            vm.workflow.finish = {
                                id: null,
                                '@type': 'PLMWorkflowDefinitionFinish',
                                name: element.name,
                                description: null,
                                diagramId: element.id
                            };
                            mapStatuses.put(element.id, vm.workflow.finish);
                        }
                        else if (element.$type === 'bpmn:Task') {
                            var status = {
                                id: null,
                                name: null,
                                description: null,
                                diagramId: element.id
                            };
                            vm.workflow.statuses.push(status);
                            mapStatuses.put(element.id, status);
                        }

                        else if (element.$type === 'bpmn:IntermediateThrowEvent') {
                            var status = {
                                id: null,
                                '@type': 'PLMWorkflowDefinitionTerminate',
                                name: "Cancel",
                                description: null,
                                diagramId: element.id
                            };
                            vm.workflow.statuses.push(status);
                            mapStatuses.put(element.id, status);
                        }
                        else if (element.$type === 'bpmn:SequenceFlow') {
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

                    $timeout(function () {
                        resizeCanvas();
                    });

                });
            }

            function validateWorkflow() {
                return (isWorkflowValid() && areStatusesValid() && areTransitionsValid() && areAllPathsValid());
            }

            var workflowType = $translate.instant("WORKFLOW_TYPE_VALIDATE");
            var workflowName = $translate.instant("WORKFLOW_NAME_VALIDATE");
            var workflowStatusName = $translate.instant("WORKFLOW_STATUS_VALIDATE");
            var workflowReleased = parse.html($translate.instant("WORKFLOW_RELEASED_VALIDATE")).html();
            var saveAs = $translate.instant("WORKFLOW_SAVEAS_TITLE");

            function isWorkflowValid() {
                var valid = true;
                if (vm.workflow.workflowType == null || vm.workflow.workflowType.undefined == "") {
                    $rootScope.showErrorMessage(workflowType);
                    valid = false;
                }
                else if (vm.workflow.name == null || vm.workflow.name.trim() === "") {
                    $rootScope.showErrorMessage(workflowName);
                    valid = false;
                }
                return valid;
            }

            var atleastOneTransition = parse.html($translate.instant("AT_LEAST_ONE_TRANSITION")).html();
            var workflowReleasedState = parse.html($translate.instant("WORKFLOW_RELEASED_STATE_CONNECTION")).html();
            var workflowFinishState = parse.html($translate.instant("WORKFLOW_FINISH_STATE_CONNECTION")).html();
            var duplicateStatusNameValidation = $translate.instant("DUPLICATE_STATUS_NAME_VALID");
            var workflowPendingConnection = $translate.instant("WORKFLOW_PENDING_CONNECTION");
            var workflowReleasedConnection = $translate.instant("WORKFLOW_RELEASED_CONNECTION");
            var workflowConnection = $translate.instant("WORKFLOW_CONNECTION");
            var workflowStatusMsg = $translate.instant("WORKFLOW_NO_STATUS");
            var workflowActivityNameEmptyMsg = $translate.instant("WORKFLOW_ACTIVITY_NAME_EMPTY");
            var workflowDuplicateActivityNameMsg = $translate.instant("WORKFLOW_DUPLICATE_ACT_NAME_MSG");

            function areStatusesValid() {
                var valid = true;
                var statuses = vm.workflow.statuses;
                if (statuses.length === 0) {
                    $rootScope.showErrorMessage(workflowStatusMsg);
                    valid = false;
                }

                var nameMap = new Hashtable();

                for (var i = 0; i < statuses.length; i++) {
                    var status = statuses[i];
                    if (status.name == null || status.name.trim() === "") {
                        $rootScope.showErrorMessage(workflowActivityNameEmptyMsg);
                        valid = false;
                        break;
                    }
                    if (nameMap.get(status.name) === null) {
                        if (status.type !== 'TERMINATE') {
                            nameMap.put(status.name, status);
                        }
                    }
                    else {
                        $rootScope.showErrorMessage(workflowDuplicateActivityNameMsg);
                        valid = false;
                        break;
                    }

                    if (status.type != null) {
                        angular.forEach(vm.workflow.transitions, function (transition) {
                            if (status.diagramId == transition.fromStatusDiagramId && transition.toStatusDiagramId != vm.workflow.finish.diagramId) {
                                if (status.type == "RELEASED" && transition.toStatusDiagramId == vm.workflow.finish.diagramId) {
                                    $rootScope.showWarningMessage(status.name + " - " + workflowReleasedState);
                                    valid = false;
                                }

                            }
                            if (status.diagramId == transition.fromStatusDiagramId && transition.toStatusDiagramId == vm.workflow.finish.diagramId) {
                                if (status.type != "RELEASED") {
                                    $rootScope.showWarningMessage(status.name + " -" + workflowFinishState);
                                    valid = false;
                                }
                            }
                            if (status.diagramId == transition.fromStatusDiagramId && transition.toStatusDiagramId != vm.workflow.finish.diagramId) {
                                if (status.type == "RELEASED") {
                                    $rootScope.showWarningMessage(status.name + " - " + workflowConnection);
                                    valid = false;
                                }

                            }
                        });
                    }

                    if (status.name == null || status.name.trim() == "") {
                        $rootScope.showErrorMessage(workflowStatusName);
                        valid = false;
                    }
                    else if (!hasAnyTransitions(status)) {
                        $rootScope.showErrorMessage(atleastOneTransition);
                        valid = false;
                    }

                    if (status.type != null && status.type != 'FINISH') {
                        var transtion1 = true;
                        angular.forEach(vm.workflow.transitions, function (transition) {
                            if (status.diagramId == transition.fromStatusDiagramId) {
                                transtion1 = false;
                            }
                        });
                        if (transtion1 == true) {
                            if (status.type != "RELEASED" && status.type != "REJECTED" && status.type != "TERMINATE") {
                                $rootScope.showWarningMessage(status.name + " - " + workflowPendingConnection);
                                valid = false;
                            } else if (status.type == "RELEASED") {
                                $rootScope.showWarningMessage(status.name + " - " + workflowReleasedConnection);
                                valid = false;
                            }
                        }
                    }

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
                var statuses = vm.workflow.statuses;

                var transitions = getToTransitions(vm.workflow.finish);
                if (transitions === undefined || transitions === null || transitions.length === 0) {
                    $rootScope.showErrorMessage(message4);
                    return false;
                }

                for (var i = 0; i < statuses.length; i++) {
                    var status = statuses[i];

                    if (status.type === 'TERMINATE') {
                        var transitions = getToTransitions(status);
                        if (transitions === undefined || transitions === null || transitions.length === 0) {
                            $rootScope.showErrorMessage(message5);
                            return false;
                        }
                    }
                    else if (status.type === 'NORMAL' ||
                        status.type === 'RELEASED' || status.type === 'REJECTED') {
                        var transitions = getToTransitions(status);
                        if (transitions === undefined || transitions === null || transitions.length === 0) {
                            $rootScope.showErrorMessage(message6);
                            return false;
                        }

                        var transitions = getFromTransitions(status);
                        if (transitions === undefined || transitions === null || transitions.length === 0) {
                            $rootScope.showErrorMessage(message7);
                            return false;
                        }
                    }

                }

                return true;
            }

            function getToTransitions(status) {
                var toTransition = null;
                var transitions = vm.workflow.transitions;
                angular.forEach(transitions, function (transition) {
                    if (status.diagramId === transition.toStatusDiagramId) {
                        toTransition = transition;
                    }
                });

                return toTransition;
            }

            function getFromTransitions(status) {
                var toTransition = null;
                var transitions = vm.workflow.transitions;
                angular.forEach(transitions, function (transition) {
                    if (status.diagramId === transition.fromStatusDiagramId) {
                        toTransition = transition;
                    }
                });

                return toTransition;
            }

            function loadWorkflowAttributeDefs() {
                vm.objectAttributes = [];
                vm.reqObjectAttributes = [];
                ItemTypeService.getAllTypeAttributes("WORKFLOW").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                                var att = {
                                    id: {
                                        objectId: vm.id,
                                        attributeDef: attribute.id
                                    },
                                    attributeDef: attribute,
                                    listValue: null,
                                    newListValue: null,
                                    listValueEditMode: false,
                                    booleanValue: false,
                                    integerValue: null,
                                    stringValue: null,
                                    doubleValue: null,
                                    imageValue: null,
                                    refValue: null,
                                    timestampValue: moment(new Date()).format("DD/MM/YYYY, HH:mm:ss"),
                                    ref: null,
                                    attachmentValues: []
                                };
                                if (attribute.lov != null) {
                                    att.lovValues = attribute.lov.values;
                                }
                                if (attribute.required === false) {
                                    vm.objectAttributes.push(att);
                                } else {
                                    vm.reqObjectAttributes.push(att);
                                }
                            }
                        )
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    })
            }

            var saveButton = $translate.instant("SAVE");
            var saveAsMessage = parse.html($translate.instant('WORKFLOW_SAVED_MESSAGE')).html();

            function promptSaveAs(flag, xml) {
                var viewer = new Viewer();
                var moddle = viewer._createModdle({});
                moddle.fromXML(xml, function (err, definitions) {

                    regenerateIds(definitions);

                    moddle.toXML(definitions, {}, function (err, xml) {
                        vm.workflow.diagram = xml;
                        $rootScope.revWorkflow = vm.workflow;
                        if (flag === undefined || flag == null || flag === true) {
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
                                    $rootScope.showSuccessMessage(saveAsMessage);
                                }
                            };
                            $rootScope.showSidePanel(options);
                        }
                    });
                });
            }

            function showSaveAsPanel(flag) {
                if (vm.workflow.released) {
                    promptSaveAs(flag, vm.workflow.diagram);
                }
                else {
                    if (validateWorkflow()) {
                        window.saveDiagram(function (err, xml) {
                            promptSaveAs(flag, xml);
                        });
                    }
                }
            }

            function regenerateIds(definitions) {
                //console.log(definitions);

                var map = new Hashtable();

                definitions.id = "Definitions_" + Math.random().toString(36).slice(2);
                var elements = definitions.rootElements[0].flowElements;
                angular.forEach(elements, function (element) {
                    var newId = null;
                    var oldId = element.id;
                    if (element.$type === "bpmn:StartEvent") {
                        newId = "StartEvent_" + Math.random().toString(36).slice(2);
                        vm.workflow.start.diagramId = newId;
                    }
                    else if (element.$type === "bpmn:EndEvent") {
                        newId = "EndEvent_" + Math.random().toString(36).slice(2);
                        vm.workflow.finish.diagramId = newId;
                    }
                    else if (element.$type === "bpmn:Task") {
                        newId = "Task_" + Math.random().toString(36).slice(2);
                    }
                    else if (element.$type === "bpmn:SequenceFlow") {
                        newId = "SequenceFlow_" + Math.random().toString(36).slice(2);
                    }
                    else if (element.$type === "bpmn:IntermediateThrowEvent") {
                        newId = "IntermediateThrowEvent_" + Math.random().toString(36).slice(2);
                    }
                    else if (element.$type === "bpmn:Process") {
                        newId = "Process_" + Math.random().toString(36).slice(2);
                    }

                    if (newId != null) {
                        map.put(oldId, newId);
                        element.id = newId;
                        var activity = mapStatuses.get(oldId);
                        if (activity !== null && activity !== undefined) {
                            activity.diagramId = newId;
                        }

                        var transition = mapTransitions.get(oldId);
                        if (transition !== null && transition !== undefined) {
                            transition.diagramId = newId;
                        }
                    }
                });

                var transitions = vm.workflow.transitions;
                angular.forEach(transitions, function (transition) {
                    var id = transition.fromStatusDiagramId;
                    var mappedId = map.get(id);
                    if (mappedId !== null && mappedId !== undefined) {
                        transition.fromStatusDiagramId = mappedId;
                    }

                    id = transition.toStatusDiagramId;
                    mappedId = map.get(id);
                    if (mappedId !== null && mappedId !== undefined) {
                        transition.toStatusDiagramId = mappedId;
                    }
                });

                //console.log(definitions);
            }

            function onSelectStatusType(item) {
                if (selectedBpmnElement != null && selectedBpmnElement.type === "bpmn:Task") {
                    $("#workflowFrame").contents().find(".djs-overlays-" + selectedBpmnElement.id).removeClass("workflow-status-type-normal");
                    $("#workflowFrame").contents().find(".djs-overlays-" + selectedBpmnElement.id).removeClass("workflow-status-type-rejected");
                    $("#workflowFrame").contents().find(".djs-overlays-" + selectedBpmnElement.id).removeClass("workflow-status-type-released");

                    if (item === "RELEASED") {
                        $("#workflowFrame").contents().find(".djs-overlays-" + selectedBpmnElement.id).addClass("workflow-status-type-released");
                    }
                    else if (item === "NORMAL") {
                        $("#workflowFrame").contents().find(".djs-overlays-" + selectedBpmnElement.id).addClass("workflow-status-type-normal");
                    }
                    else if (item === "REJECTED") {
                        $("#workflowFrame").contents().find(".djs-overlays-" + selectedBpmnElement.id).addClass("workflow-status-type-rejected");
                    }
                }
            }

            var confirmation = parse.html($translate.instant("CONFIRMATION")).html();
            var doYouWantToPromote = parse.html($translate.instant("DO_YOU_WANT_TO_WORKFLOW_PROMOTE_STATUS")).html();
            var doYouWantToDemote = parse.html($translate.instant("DO_YOU_WANT_TO_WORKFLOW_DEMOTE_STATUS")).html();
            var yesTitle = parse.html($translate.instant("YES")).html();
            var noTitle = parse.html($translate.instant("NO")).html();
            var itemLifecycleStatusPromoted = parse.html($translate.instant("WORKFLOW_PROMOTED")).html();
            var itemLifecycleStatusDemoted = parse.html($translate.instant("WORKFLOW_DEMOTED")).html();

            vm.demoteDefinition = demoteDefinition;
            vm.promoteDefinition = promoteDefinition;
            function demoteDefinition() {
                var options = {
                    title: confirmation,
                    message: doYouWantToDemote,
                    okButtonClass: 'btn-danger',
                    okButtonText: yesTitle,
                    cancelButtonText: noTitle
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        WorkflowDefinitionService.demoteDefinition($stateParams.workflow).then(
                            function (data) {
                                vm.workflow.lifeCyclePhase = data.lifeCyclePhase;
                                setLifecycles();
                                $rootScope.showSuccessMessage(itemLifecycleStatusDemoted);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            var saveWorkflowTitle = parse.html($translate.instant("SAVE_WORKFLOW")).html();
            var workflowModifiedSaveMsg = parse.html($translate.instant("WORKFLOW_MODIFIED_CHANGES_MSG")).html();
            var workflowPromoteUnSaveChanges = parse.html($translate.instant("WORKFLOW_PROMOTE_UN_SAVE_CHANGES_MSG")).html();

            function promoteDefinition() {
                if (unsavedChanges) {
                    var options = {
                        title: saveWorkflowTitle,
                        message: workflowModifiedSaveMsg,
                        okButtonText: "Yes",
                        cancelButtonText: "No"
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes) {
                            saveWorkflow(function () {
                                promoteWorkflow();
                            });
                        } else {
                            $rootScope.showWarningMessage(workflowPromoteUnSaveChanges)
                        }
                    });
                } else {
                    var options = {
                        title: confirmation,
                        message: doYouWantToPromote,
                        okButtonClass: 'btn-danger',
                        okButtonText: yesTitle,
                        cancelButtonText: noTitle
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes) {
                            promoteWorkflow();
                        }
                    })
                }
            }

            function promoteWorkflow() {
                $rootScope.showBusyIndicator($('.view-container'));
                WorkflowDefinitionService.promoteDefinition($stateParams.workflow).then(
                    function (data) {
                        vm.workflow.lifeCyclePhase = data.lifeCyclePhase;
                        vm.workflow.released = data.released;
                        setLifecycles();
                        if (vm.workflow.released) {
                            vm.workflow.revision = data.revision;
                            $rootScope.viewInfo.title = "<div class='item-number'>" +
                                "{0}</div> <span class='item-rev'>Rev {1}</span>".
                                    format(vm.workflow.master.number,
                                    vm.workflow.revision);
                            unsavedChanges = false;
                            loadWorkflowInReadOnlyMode();
                        }
                        $rootScope.showSuccessMessage(itemLifecycleStatusPromoted);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function setLifecycles() {
                var phases = [];
                var currentPhase = vm.workflow.lifeCyclePhase.phase;
                $rootScope.lifeCycleStatus = vm.workflow.lifeCyclePhase.phase;
                var defs = vm.workflow.workflowType.lifecycle.phases;
                defs.sort(function (a, b) {
                    return a.id - b.id;
                });
                var lastPhase = defs[defs.length - 1].phase;
                angular.forEach(defs, function (def) {
                    if (def.phase == currentPhase && lastPhase == def.phase) {
                        phases.push({
                            name: def.phase,
                            finished: true,
                            current: (def.phase == currentPhase)
                        })
                    } else {
                        phases.push({
                            name: def.phase,
                            finished: false,
                            current: (def.phase == currentPhase)
                        })
                    }
                });

                var index = -1;
                for (var i = 0; i < phases.length; i++) {
                    if (phases[i].current == true) {
                        index = i;
                    }
                }

                if (index > 0) {
                    for (i = 0; i < index; i++) {
                        phases[i].finished = true;
                    }
                }
                vm.lastLifecyclePhase = vm.workflow.workflowType.lifecycle.phases[vm.workflow.workflowType.lifecycle.phases.length - 1];
                vm.firstLifecyclePhase = vm.workflow.workflowType.lifecycle.phases[0];
                $rootScope.setLifecyclePhases(phases);
            }

            vm.reviseDefinition = reviseDefinition;
            function reviseDefinition() {
                $rootScope.showBusyIndicator($('#appView')[0]);
                showSaveAsPanel(false);
                $timeout(function () {
                    WorkflowDefinitionService.reviseWorkflow($stateParams.workflow, $rootScope.revWorkflow).then(
                        function (data) {
                            WorkflowDefinitionService.copyWorkflowDefinitionEvents($stateParams.workflow, data).then(
                                function (data) {
                                    $rootScope.hideBusyIndicator();
                                    $state.go('app.workflow.editor', {mode: 'edit', workflow: data.id});
                                }
                            )
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }, 2000)
            }

            var revisionHistoryTitle = $translate.instant("REVISION_HISTORY_TITLE");
            vm.showWorkflowRevisionHistory = showWorkflowRevisionHistory;
            function showWorkflowRevisionHistory() {
                var options = {
                    title: vm.workflow.number + " - " + revisionHistoryTitle,
                    template: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryView.jsp',
                    controller: 'ItemRevisionHistoryController as revHistoryVm',
                    resolve: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryController',
                    data: {
                        itemId: vm.workflow.master.id,
                        revisionHistoryType: "WORKFLOW"
                    },
                    width: 700,
                    showMask: true,
                };

                $rootScope.showSidePanel(options);
            }

            vm.workflowInstances = workflowInstances;
            var workflowInstanceTitle = parse.html($translate.instant("WORKFLOW_INSTANCES_TITLE")).html();

            function workflowInstances() {
                var options = {
                    title: workflowInstanceTitle,
                    template: 'app/desktop/modules/workflow/all/workflowInstances.jsp',
                    controller: 'WorkflowInstanceController as wfInstanceVm',
                    resolve: 'app/desktop/modules/workflow/all/workflowInstancesController',
                    showMask: true,
                    width: 725,
                    data: {
                        workflow: vm.workflow,
                        mode: "Revision"
                    },
                    callback: function () {
                    }
                };

                $rootScope.showSidePanel(options);
            }


            /*---------------------------  Events -------------------------------------------*/

            $scope.active = -1;
            $scope.workflowEventTypes = [
                {label: 'Workflow Start', value: 'WorkflowStart'},
                //{label: 'Workflow Finish', value: 'WorkflowFinish'},
                //{label: 'Workflow Hold', value: 'WorkflowHold'},
                //{label: 'Workflow Unhold', value: 'WorkflowUnHold'},
                //{label: 'Workflow Cancelled', value: 'WorkflowCancelled'},
                {label: 'Workflow Activity Start', value: 'WorkflowActivityStart'},
                {label: 'Workflow Activity Finish', value: 'WorkflowActivityFinish'}
            ];

            $scope.getEventTypeLabel = getEventTypeLabel;
            function getEventTypeLabel(event) {
                var eventTypeName = "";
                angular.forEach($scope.workflowEventTypes, function (eventType) {
                    if (eventType.value == event.eventType) {
                        eventTypeName = eventType.label;
                    }
                })
                return eventTypeName;
            }

            $scope.tabs = {
                details: {
                    id: 'details.basic',
                    heading: "Details",
                    template: 'app/desktop/modules/workflow/editor/workflowDetailsView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                events: {
                    id: 'details.events',
                    heading: 'Events',
                    template: 'app/desktop/modules/workflow/editor/workflowEventsView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                }
            };

            $scope.events = [];
            $scope.eventsLoading = true;
            $scope.selectWorkflowTab = selectWorkflowTab;
            function selectWorkflowTab(id) {
                if (id == "details.basic") {
                    $scope.active = 0;
                } else {
                    $scope.active = 1;
                    loadEvents();
                }
                $timeout(function () {
                    var height = $(window).height();
                    $('.workflow-tabs').height(height - 210);
                    $('.workflow-tabs .tab-content').height(height - 260);
                    $('.workflow-tabs .tab-content .tab-pane').height(height - 260);
                    $('#workflow-details').height($('.workflow-tabs .tab-content .tab-pane').outerHeight());
                }, 1000)
            }

            $scope.eventTypes = [];
            function loadEvents() {
                $scope.eventsLoading = true;
                WorkflowDefinitionService.getWorkflowDefinitionEvents(vm.workflow.id).then(
                    function (data) {
                        $scope.events = data;
                        var height = $(window).height();
                        $('.workflow-tabs').height(height - 210);
                        $('.workflow-tabs .tab-content').height(height - 260);
                        $('.workflow-tabs .tab-content .tab-pane').height(height - 260);
                        $('#events-view').height($('.workflow-tabs .tab-content .tab-pane').outerHeight());
                        filterEventTypes();
                        loadWorkflowStatuses();
                        $scope.eventsLoading = false;
                    }
                )
            }

            function filterEventTypes() {
                var eventMap = new Hashtable();
                $scope.eventTypes = [];
                angular.forEach($scope.events, function (event) {
                    eventMap.put(event.eventType, event);
                })
                $timeout(function () {
                    angular.forEach($scope.workflowEventTypes, function (eventType) {
                        var event = eventMap.get(eventType.value);
                        if (event == null || event == "" || event == undefined) {
                            if ($scope.eventTypes.indexOf(eventType) == -1) {
                                $scope.eventTypes.push(eventType);
                            }
                        }
                    })
                }, 500)
            }

            vm.selectedEvent = null;
            $scope.onSelectEvent = onSelectEvent;
            function onSelectEvent(newEvent) {
                if ($scope.events.length == 0) {
                    addEvent(newEvent);
                } else {
                    $scope.event = null;
                    angular.forEach($scope.events, function (event) {
                        if (event.eventType == newEvent.value) {
                            $scope.event = event;
                        }
                    })

                    if ($scope.event == null) {
                        addEvent(newEvent);
                    }
                }
            }

            $scope.onSelectActivity = onSelectActivity;
            function onSelectActivity(activity, workflowEvent) {
                workflowEvent.actionData = null;
                workflowEvent.actionDataList = [];
            }

            function addEvent(newEvent) {
                var actionType = null;
                if ($scope.workflowActionTypes.length == 1) {
                    actionType = $scope.workflowActionTypes[0].value;
                }
                $scope.event = {
                    eventType: newEvent.value,
                    workflowDefinitionEvents: [
                        {
                            id: null,
                            workflow: vm.workflow.id,
                            activity: null,
                            eventType: newEvent.value,
                            actionType: actionType,
                            actionData: null,
                            actionDataCopy: null
                        }
                    ]
                };
                $scope.events.push($scope.event);
                vm.selectedEvent = null;
                filterEventTypes();
            }

            $scope.addWorkflowEvent = addWorkflowEvent;
            function addWorkflowEvent(event) {
                var actionType = null;
                if ($scope.workflowActionTypes.length == 1) {
                    actionType = $scope.workflowActionTypes[0].value;
                }
                event.workflowDefinitionEvents.push({
                    id: null,
                    workflow: vm.workflow.id,
                    activity: null,
                    eventType: event.eventType,
                    actionType: actionType,
                    actionData: null,
                    actionDataCopy: null
                })
            }

            $scope.saveWorkflowEvent = saveWorkflowEvent;
            function saveWorkflowEvent(workflowEvent) {
                if (validateWorkflowEvent(workflowEvent)) {
                    if (workflowEvent.id == null || workflowEvent.id == "" || workflowEvent.id == undefined) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        WorkflowDefinitionService.createWorkflowDefinitionEvent(vm.workflow.id, workflowEvent).then(
                            function (data) {
                                workflowEvent.id = data.id;
                                $rootScope.showSuccessMessage(workflowEventSaved);
                                $rootScope.hideBusyIndicator();
                                hideWorkflowEvent();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else {
                        $rootScope.showBusyIndicator($('.view-container'));
                        WorkflowDefinitionService.updateWorkflowDefinitionEvent(vm.workflow.id, workflowEvent).then(
                            function (data) {
                                workflowEvent.id = data.id;
                                $rootScope.showSuccessMessage(workflowEventSaved);
                                $rootScope.hideBusyIndicator();
                                hideWorkflowEvent();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function validateWorkflowEvent(workflowEvent) {
                var valid = true;
                if (workflowEvent.actionType == "SetLifecyclePhase" && !validateLifecyclePhases(workflowEvent, "save")) {
                    valid = false;
                }
                return valid;
            }

            $scope.deleteWorkflowEvent = deleteWorkflowEvent;
            function deleteWorkflowEvent(event, workflowEvent) {
                if (workflowEvent.id == null || workflowEvent.id == "") {
                    event.workflowDefinitionEvents.splice(event.workflowDefinitionEvents.indexOf(workflowEvent), 1);
                    if (event.workflowDefinitionEvents.length == 0) {
                        $scope.events.splice($scope.events.indexOf(event), 1);
                    }
                    filterEventTypes();
                } else {
                    var options = {
                        title: confirmation,
                        message: workflowEventDialogMsg,
                        okButtonClass: 'btn-danger'
                    };

                    DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                $rootScope.showBusyIndicator($('.view-container'));
                                WorkflowDefinitionService.deleteWorkflowDefinitionEvent(vm.workflow.id, workflowEvent.id).then(
                                    function (data) {
                                        event.workflowDefinitionEvents.splice(event.workflowDefinitionEvents.indexOf(workflowEvent), 1);
                                        if (event.workflowDefinitionEvents.length == 0) {
                                            $scope.events.splice($scope.events.indexOf(event), 1);
                                        }
                                        filterEventTypes();
                                        $rootScope.showSuccessMessage(workflowEventDeleted);
                                        $rootScope.hideBusyIndicator();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        }
                    )
                }
            }

            $scope.workflowStatuses = [];
            function loadWorkflowStatuses() {
                $scope.workflowStatuses = [];
                WorkflowDefinitionService.getWorkflowDefinitionStatuses(vm.workflow.id).then(
                    function (data) {
                        angular.forEach(data, function (status) {
                            if ((status.type == 'NORMAL' || status.type == 'RELEASED' || status.type == 'REJECTED')) {
                                $scope.workflowStatuses.push(status);
                            }
                        })
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.selectedLifecycle = null;
            vm.dialogHeader = null;
            $scope.showSetLifecycleDialog = showSetLifecycleDialog;
            function showSetLifecycleDialog(workflowEvent) {
                if (vm.workflowAssignableType.objectType == "ECOTYPE" || vm.workflowAssignableType.objectType == "DCOTYPE" || vm.workflowAssignableType.objectType == "BOPTYPE" || (vm.workflowAssignableType.objectType == "MCOTYPE" || vm.workflowAssignableType.objectType == "ITEMMCO")) {
                    if ((workflowEvent.eventType == "WorkflowActivityStart" || workflowEvent.eventType == "WorkflowActivityFinish") && workflowEvent.activity == null || workflowEvent.activity == "") {
                        $rootScope.showWarningMessage(pleaseSelectActivity);
                    } else {
                        $scope.selectedWorkflowEvent = null;
                        var modal = document.getElementById("workflow-event");
                        modal.style.display = "block";
                        vm.dialogHeader = "Set Lifecycle Phases";
                        $timeout(function () {
                            var headerHeight = $('.workflow-event-header').outerHeight();
                            var bomContentHeight = $('.workflow-event-content').outerHeight();
                            $(".event-content").height(bomContentHeight - headerHeight);
                            $scope.errorNotification = $('#configuration-error');
                            vm.selectedLifecycle = null;
                            $scope.selectedWorkflowEvent = workflowEvent;
                            if ($scope.selectedWorkflowEvent.actionData != null && $scope.selectedWorkflowEvent.actionData != "") {
                                $scope.selectedWorkflowEvent.actionDataList = JSON.parse($scope.selectedWorkflowEvent.actionData);
                                filterLifecycles();
                            } else {
                                $scope.selectedWorkflowEvent.actionDataList = [];
                                if (vm.workflowAssignableType.objectType == "MCOTYPE") {
                                    $scope.lifecycles = angular.copy(vm.mbomTypeLifecycles);
                                } else if (vm.workflowAssignableType.objectType == "BOPTYPE") {
                                    $scope.lifecycles = [];
                                    $scope.lifecycles.push(angular.copy(vm.workflowAssignableType.lifecycle));
                                } else {
                                    $scope.lifecycles = angular.copy(vm.itemTypeLifecycles);
                                }
                            }
                        }, 200)
                    }
                }

            }

            $scope.showScriptDialog = showScriptDialog;
            function showScriptDialog(workflowEvent) {
                $scope.selectedWorkflowEvent = null;
                var modal = document.getElementById("workflow-event");
                modal.style.display = "block";
                vm.dialogHeader = "Event Script";
                $timeout(function () {
                    var headerHeight = $('.workflow-event-header').outerHeight();
                    var bomContentHeight = $('.workflow-event-content').outerHeight();
                    $(".event-content").height(bomContentHeight - headerHeight);
                    $scope.errorNotification = $('#configuration-error');
                    $scope.selectedWorkflowEvent = workflowEvent;
                }, 200);
                $timeout(function () {
                    var editor = ace.edit("execute-script");
                    editor.setTheme("ace/theme/xcode");
                    editor.getSession().setMode("ace/mode/groovy");
                    editor.setFontSize("14px");
                    editor.setPrintMarginColumn(false);
                    if ($scope.selectedWorkflowEvent.actionData == null || $scope.selectedWorkflowEvent.actionData == "") {
                        $scope.selectedWorkflowEvent.actionData = "";
                    }
                    editor.session.setValue($scope.selectedWorkflowEvent.actionData);
                }, 500);

            }

            $scope.updateEventData = updateEventData;
            function updateEventData() {
                if ($scope.selectedWorkflowEvent.actionType == "SetLifecyclePhase") {
                    if (validateLifecyclePhases($scope.selectedWorkflowEvent, "add")) {
                        $scope.selectedWorkflowEvent.actionData = angular.toJson($scope.selectedWorkflowEvent.actionDataList);
                        saveWorkflowEvent($scope.selectedWorkflowEvent);
                    }
                } else if ($scope.selectedWorkflowEvent.actionType == "ExecuteScript") {
                    var editor = ace.edit("execute-script");
                    $scope.selectedWorkflowEvent.actionData = editor.getValue();
                    editor.session.setValue("");
                    saveWorkflowEvent($scope.selectedWorkflowEvent);
                }
            }

            $scope.removeLifecycle = removeLifecycle;
            function removeLifecycle(lifecycleData) {
                $scope.selectedWorkflowEvent.actionDataList.splice($scope.selectedWorkflowEvent.actionDataList.indexOf(lifecycleData), 1);
                filterLifecycles();
            }

            function validateLifecyclePhases(workflowEvent, type) {
                var valid = true;
                if (workflowEvent.actionDataList.length == 0) {
                    valid = false;
                    if (type == "add") {
                        $scope.errorNotification.show();
                        $scope.errorNotification.removeClass('zoomOut');
                        $scope.errorNotification.addClass('zoomIn');
                        $scope.notificationClass = "fa-warning";
                        $scope.notificationBackground = "alert-warning";
                        $scope.message = addOneLifecycle;
                        $timeout(function () {
                            closeErrorNotification();
                        }, 3000);
                    } else {
                        $rootScope.showWarningMessage(addOneLifecycle);
                    }
                } else {
                    angular.forEach(workflowEvent.actionDataList, function (lifecycleData) {
                        if (valid) {
                            if (lifecycleData.lifecyclePhase == null || lifecycleData.lifecyclePhase == "" || lifecycleData.lifecyclePhase == undefined) {
                                valid = false;
                                if (type == "add") {
                                    $scope.errorNotification.show();
                                    $scope.errorNotification.removeClass('zoomOut');
                                    $scope.errorNotification.addClass('zoomIn');
                                    $scope.notificationClass = "fa-warning";
                                    $scope.notificationBackground = "alert-warning";
                                    $scope.message = pleaseSelectLifecyclePhase.format(lifecycleData.lifecycle.name);
                                    $timeout(function () {
                                        closeErrorNotification();
                                    }, 3000);
                                } else {
                                    $rootScope.showWarningMessage(pleaseSelectLifecyclePhase.format(lifecycleData.lifecycle.name));
                                }
                            }
                        }
                    })
                }
                return valid;
            }

            $scope.closeErrorNotification = closeErrorNotification;
            function closeErrorNotification() {
                $scope.errorNotification.removeClass('zoomIn');
                $scope.errorNotification.addClass('zoomOut');
                $scope.errorNotification.hide();
            }

            $scope.hideWorkflowEvent = hideWorkflowEvent;
            function hideWorkflowEvent() {
                var modal = document.getElementById("workflow-event");
                modal.style.display = "none";
            }

            $scope.addLifecycle = addLifecycle;
            function addLifecycle(lifecycle) {
                var lifecycleExist = false;
                angular.forEach($scope.selectedWorkflowEvent.actionDataList, function (data) {
                    if (data.lifecycle.id == lifecycle.id) {
                        lifecycleExist = true;
                    }
                })

                if (!lifecycleExist) {
                    var emptyData = {
                        lifecycle: lifecycle,
                        lifecyclePhase: null
                    }
                    $scope.selectedWorkflowEvent.actionDataList.push(emptyData);
                    filterLifecycles();
                    vm.selectedLifecycle = null;
                }
            }

            function filterLifecycles() {
                var lifecycleMap = new Hashtable();
                $scope.lifecycles = [];
                angular.forEach($scope.selectedWorkflowEvent.actionDataList, function (data) {
                    lifecycleMap.put(data.lifecycle.id, data.lifecycle);
                })
                $timeout(function () {
                    if (vm.workflowAssignableType.objectType == "MCOTYPE") {
                        angular.forEach(vm.mbomTypeLifecycles, function (lifecycle) {
                            var lifecycleData = lifecycleMap.get(lifecycle.id);
                            if (lifecycleData == null || lifecycleData == "" || lifecycleData == undefined) {
                                if ($scope.lifecycles.indexOf(lifecycle) == -1) {
                                    $scope.lifecycles.push(lifecycle);
                                }
                            }
                        })
                    } else if (vm.workflowAssignableType.objectType == "BOPTYPE") {
                        var lifecycleData = lifecycleMap.get(vm.workflowAssignableType.lifecycle.id);
                        if (lifecycleData == null || lifecycleData == "" || lifecycleData == undefined) {
                            if ($scope.lifecycles.indexOf(lifecycle) == -1) {
                                $scope.lifecycles.push(vm.workflowAssignableType.lifecycle);
                            }
                        }
                    } else {
                        angular.forEach(vm.itemTypeLifecycles, function (lifecycle) {
                            var lifecycleData = lifecycleMap.get(lifecycle.id);
                            if (lifecycleData == null || lifecycleData == "" || lifecycleData == undefined) {
                                if ($scope.lifecycles.indexOf(lifecycle) == -1) {
                                    $scope.lifecycles.push(lifecycle);
                                }
                            }
                        })
                    }
                }, 500)
            }

            function loadItemTypeLifecycles() {
                ItemTypeService.getItemTypeLifecycles().then(
                    function (data) {
                        vm.itemTypeLifecycles = data;
                        angular.forEach(vm.itemTypeLifecycles, function (lifecycle) {
                            lifecycle.normalLifecyclePhases = [];
                            lifecycle.releasedLifecyclePhases = [];
                            lifecycle.rejectedLifecyclePhases = [];
                            angular.forEach(lifecycle.phases, function (phase) {
                                if (phase.phaseType == "RELEASED") {
                                    lifecycle.releasedLifecyclePhases.push(phase);
                                } else if (phase.phaseType == "CANCELLED") {
                                    lifecycle.rejectedLifecyclePhases.push(phase);
                                } else if (phase.phaseType != "OBSOLETE") {
                                    lifecycle.normalLifecyclePhases.push(phase);
                                }
                            })
                        })
                    }
                )
            }

            function loadMBOMTypeLifecycles() {
                MESObjectTypeService.getMBOMTypeLifecycles().then(
                    function (data) {
                        vm.mbomTypeLifecycles = data;
                        angular.forEach(vm.mbomTypeLifecycles, function (lifecycle) {
                            lifecycle.normalLifecyclePhases = [];
                            lifecycle.releasedLifecyclePhases = [];
                            lifecycle.rejectedLifecyclePhases = [];
                            angular.forEach(lifecycle.phases, function (phase) {
                                if (phase.phaseType == "RELEASED") {
                                    lifecycle.releasedLifecyclePhases.push(phase);
                                } else if (phase.phaseType == "CANCELLED") {
                                    lifecycle.rejectedLifecyclePhases.push(phase);
                                } else if (phase.phaseType != "OBSOLETE") {
                                    lifecycle.normalLifecyclePhases.push(phase);
                                }
                            })
                        })
                    }
                )
            }

            vm.showActivityFormData = showActivityFormData;
            function showActivityFormData() {
                var modal = document.getElementById("workflow-form-data");
                modal.style.display = "block";
                $timeout(function () {
                    var headerHeight = $('.workflow-form-data-header').outerHeight();
                    var bomContentHeight = $('.workflow-form-data-content').outerHeight();
                    $(".form-data-content").height(bomContentHeight - (headerHeight + 20));
                    $rootScope.formDataErrorNotification = $('#configuration-error');
                    $scope.$broadcast('app.classification.attribute', {});
                }, 200)
            }

            $scope.hideActivityFormData = hideActivityFormData;
            function hideActivityFormData() {
                var modal = document.getElementById("workflow-form-data");
                if (modal != null && modal != undefined) {
                    modal.style.display = "none";
                }
            }

            (function () {
                $rootScope.showBusyIndicator($('#appView')[0]);
                $scope.$on('$viewContentLoaded', function () {
                    $(window).resize(resizeCanvas);
                    loadWorkflowAttributeDefs();

                    $scope.$on('$destroy', function () {
                        if (wfNameChangeListener != null) {
                            wfNameChangeListener();
                        }
                        if (wfDescriptionChangeListener != null) {
                            wfDescriptionChangeListener();
                        }
                        if (nameChangeListener != null) {
                            nameChangeListener();
                        }
                        if (typeChangeListener != null) {
                            typeChangeListener();
                        }
                    });
                });
            })();
        }
    }
);