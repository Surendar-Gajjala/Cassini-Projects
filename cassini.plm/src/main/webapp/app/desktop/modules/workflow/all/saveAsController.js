define(
    [
        'app/desktop/modules/workflow/workflow.module',
        'bpmn-viewer',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/desktop/modules/workflow/directive/workflowtypeDirective',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/classificationService'
    ],
    function (module, Viewer) {
        module.controller('SaveWorkflowAsController', SaveWorkflowAsController);

        function SaveWorkflowAsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                          CommonService, WorkflowDefinitionService, AutonumberService, ClassificationService) {

            var vm = this;

            var parsed = angular.element("<div></div>");

            vm.newWorkflow = angular.copy($scope.data.workflow);
            vm.oldWorkflowId = vm.newWorkflow.id;

            var mapStatuses = new Hashtable();
            var mapTransitions = new Hashtable();

            var saveAsMessage = parsed.html($translate.instant('WORKFLOW_SAVED_MESSAGE')).html();
            vm.saveWorkflowAs = saveWorkflowAs;
            var workflowType = $translate.instant("WORKFLOW_TYPE_VALIDATE");
            var workflowName = $translate.instant("WORKFLOW_NAME_VALIDATE");
            var workflowNumber = $translate.instant("WORKFLOW_NUMBER_VALIDATE");

            function copyWorkflow() {
                buildMaps();
                var viewer = new Viewer();
                var moddle = viewer._createModdle({});
                moddle.fromXML(vm.newWorkflow.diagram, function (err, definitions) {
                    regenerateIds(definitions);
                    moddle.toXML(definitions, {}, function (err, xml) {
                        vm.newWorkflow.diagram = xml;

                        //vm.newWorkflow = angular.copy(vm.newWorkflow);
                        vm.newWorkflow.id = null;
                        vm.newWorkflow.start.id = null;
                        vm.newWorkflow.finish.id = null;

                        angular.forEach(vm.newWorkflow.statuses, function (status) {
                            status.id = null;
                        });

                        angular.forEach(vm.newWorkflow.transitions, function (transition) {
                            transition.id = null;
                        });

                        autoNumber();
                        loadWfType();
                    });
                });
            }

            function regenerateIds(definitions) {
                var map = new Hashtable();

                definitions.id = "Definitions_" + Math.random().toString(36).slice(2);
                var elements = definitions.rootElements[0].flowElements;
                angular.forEach(elements, function (element) {
                    var newId = null;
                    var oldId = element.id;
                    if (element.$type === "bpmn:StartEvent") {
                        newId = "StartEvent_" + Math.random().toString(36).slice(2);
                        vm.newWorkflow.start.diagramId = newId;
                    }
                    else if (element.$type === "bpmn:EndEvent") {
                        newId = "EndEvent_" + Math.random().toString(36).slice(2);
                        vm.newWorkflow.finish.diagramId = newId;
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

                var transitions = vm.newWorkflow.transitions;
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

            }

            function buildMaps() {
                mapStatuses.put(vm.newWorkflow.start.diagramId, vm.newWorkflow.start);
                mapStatuses.put(vm.newWorkflow.finish.diagramId, vm.newWorkflow.finish);

                angular.forEach(vm.newWorkflow.statuses, function (status) {
                    mapStatuses.put(status.diagramId, status);
                });

                angular.forEach(vm.newWorkflow.transitions, function (transition) {
                    mapTransitions.put(transition.diagramId, transition);

                    var found = getStatusById(transition.fromStatus);
                    if (found != null) {
                        transition.fromStatusDiagramId = found.diagramId;
                    }

                    found = getStatusById(transition.toStatus);
                    if (found != null) {
                        transition.toStatusDiagramId = found.diagramId;
                    }
                });
            }

            function getStatusById(id) {
                if (id != null) {
                    if (vm.newWorkflow.start.id === id) {
                        return vm.newWorkflow.start;
                    }
                    else if (vm.newWorkflow.finish.id === id) {
                        return vm.newWorkflow.finish;
                    }

                    for (var i = 0; i < vm.newWorkflow.statuses.length; i++) {
                        var status = vm.newWorkflow.statuses[i];
                        if (status.id === id) {
                            return status;
                        }
                    }

                    return status;
                }
            }

            function saveWorkflowAs() {
                if (isWorkflowValid()) {
                    $scope.showBusy(true);
                    vm.newWorkflow.instances = 0;
                    WorkflowDefinitionService.createWorkflowDefinition(vm.newWorkflow).then(
                        function (data) {
                            WorkflowDefinitionService.copyWorkflowDefinitionEvents(vm.oldWorkflowId, data).then(
                                function (data) {
                                    $scope.showBusy(false);
                                    $rootScope.hideSidePanel();
                                    $rootScope.showSuccessMessage(saveAsMessage);
                                    $scope.callback(data);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $scope.showBusy(false);
                                }
                            )
                        },
                        function (error) {
                            $rootScope.showWarningMessage(error.message);
                            $scope.showBusy(false);
                        }
                    );
                }
                // else {
                //     $rootScope.showWarningMessage("Workflow name cannot be empty");
                // }
            }

            function isWorkflowValid() {
                var valid = true;
                if (vm.newWorkflow.workflowType == null || vm.newWorkflow.workflowType.undefined == "") {
                    $rootScope.showWarningMessage(workflowType);
                    valid = false;
                }
                else if (vm.newWorkflow.name == null || vm.newWorkflow.name.trim() === "") {
                    $rootScope.showWarningMessage(workflowName);
                    valid = false;
                }
                else if (vm.newWorkflow.number == null || vm.newWorkflow.number.trim() === "") {
                    $rootScope.showWarningMessage(workflowNumber);
                    valid = false;
                }
                return valid;
            }

            vm.onSelectType = onSelectType;
            function onSelectType(workflowType) {
                if (workflowType != null && workflowType !== undefined) {
                    vm.newWorkflow.workflowType = workflowType;
                    autoNumber();
                    loadWfType();
                }
            }

            function loadWfType() {
                if(vm.newWorkflow.workflowType.assignable == 'REQUIREMENT DOCUMENTS' || vm.newWorkflow.workflowType.assignable == 'REQUIREMENT'){
                if (vm.newWorkflow.workflowType.assignedType != null && vm.newWorkflow.workflowType.assignedType != "" && vm.newWorkflow.workflowType.assignedType != undefined) {
                    ClassificationService.getAssignedObjectType(vm.newWorkflow.workflowType.id, vm.newWorkflow.workflowType.assignedType, vm.newWorkflow.workflowType.assignable).then(
                        function (data) {
                            $scope.reqTypeName = data.name;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }
            }

            function autoNumber() {
                if (vm.newWorkflow.workflowType != null && vm.newWorkflow.workflowType.numberSource != null) {
                    var source = vm.newWorkflow.workflowType.numberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newWorkflow.number = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message)
                        }
                    )
                }
            }

            (function () {
                copyWorkflow();
                $rootScope.$on('app.workflow.saveas', function () {
                    saveWorkflowAs();
                })
            })();
        }
    }
);