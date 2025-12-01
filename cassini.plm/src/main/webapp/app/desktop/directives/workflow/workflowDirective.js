define(
    [
        'app/desktop/modules/pdm/pdm.module',
        'bpmn-modeler',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/preferenceService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/dcoService',
        'app/shared/services/core/dcrService',
        'app/shared/services/core/ecrService',
        'app/shared/services/core/mcoService',
        'app/shared/services/core/varianceService',
        'app/shared/services/core/inspectionPlanService',
        'app/shared/services/core/inspectionService',
        'app/shared/services/core/problemReportService',
        'app/shared/services/core/ncrService',
        'app/shared/services/core/qcrService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/activityService',
        'app/shared/services/core/workOrderService',
        'app/shared/services/core/workRequestService',
        'app/shared/services/core/nprService',
        'app/shared/services/core/supplierAuditService',
        'app/shared/services/core/reqDocumentService',
        'app/shared/services/core/requirementService',
        'app/shared/services/core/mbomService',
        'app/shared/services/core/bopService',
        'app/shared/services/core/programService',
        'app/shared/services/core/projectTemplateService',
        'app/shared/services/core/programTemplateService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/groupService',
        'app/shared/services/core/templateActivityService'
    ],
    function (module, Modeler) {
        module.directive('objectWorkflow', ObjectWorkflowDirective);

        function ObjectWorkflowDirective($rootScope, $stateParams, $application, $timeout, $translate, WorkflowService, PreferenceService, GroupService,
                                         InspectionPlanService, CommonService, ECOService, ItemService, InspectionService, DialogService, ProgramService, ProjectTemplateService, ProgramTemplateService,
                                         ProblemReportService, QcrService, NcrService, DCOService, MfrPartsService, WorkRequestService, NprService, SupplierAuditService, CustomObjectService, BOPService,
                                         DCRService, ECRService, MCOService, MfrService, VarianceService, ProjectService, ActivityService, SpecificationsService, WorkOrderService, ReqDocumentService, RequirementService, MBOMService, TemplateActivityService) {

            return {
                templateUrl: 'app/desktop/directives/workflow/workflowDirective.jsp',
                restrict: 'E',
                scope: {
                    objectType: "@",
                    object: '=',
                    canStartWorkflow: "=",
                    canFinishWorkflow: "=",
                    permission: "="
                },

                link: function ($scope, element, attrs) {
                    var viewer = null;
                    var workflowLoaded = false;

                    var svgNodes = new Hashtable();
                    var mapStatuses = new Hashtable();
                    var mapTransitions = new Hashtable();
                    var parsed = angular.element("<div></div>");
                    $scope.removePerson = parsed.html($translate.instant("REMOVE_PERSON")).html();
                    $scope.personRemove = parsed.html($translate.instant("PERSON_REMOVE_MSG")).html();
                    var selectPersons = parsed.html($translate.instant("SELECT_PERSONS")).html();
                    var enterComment = parsed.html($translate.instant("PLEASE_ENTER_COMMENT")).html();
                    var noChangeApprovalPassword = parsed.html($translate.instant("NO_CHANGE_APPROVAL_PASSWORD")).html();
                    var currentStatusApproved = parsed.html($translate.instant("CURRENT_STATUS_APPROVED")).html();
                    var currentStatusRejected = parsed.html($translate.instant("CURRENT_STATUS_REJECTED")).html();
                    var currentStatusAcknowledged = parsed.html($translate.instant("CURRENT_STATUS_ACKNOWLEDGED")).html();
                    var addChecklistToStart = parsed.html($translate.instant("ADD_CHECKLIST_TO_START_WORKFLOW")).html();
                    var finishChecklistToPromote = parsed.html($translate.instant("FINISH_CHECKLIST_TO_PROMOTE_WORKFLOW")).html();
                    var addProblemItemToStartWorkflow = parsed.html($translate.instant("ADDED_P_ITEM_START_WORKFLOW")).html();
                    var capaValidation = parsed.html($translate.instant("ONE_CAPA_SHOULD_BE_PASS")).html();
                    var addAffectedItemToStartWorkflow = parsed.html($translate.instant("ADDED_AFFECTED_ITEM_START_WORKFLOW")).html();
                    var addPlanToStartWorkflow = parsed.html($translate.instant("ADDED_PLAN_START_WORKFLOW")).html();
                    var enterNotes = parsed.html($translate.instant("ENTER_NOTES_VALIDATION")).html();
                    var workflowEventSaved = parsed.html($translate.instant("WORKFLOW_EVENT_SAVED")).html();
                    var workflowEventDeleted = parsed.html($translate.instant("WORKFLOW_EVENT_DELETED")).html();
                    var workflowEventDialogMsg = parsed.html($translate.instant("W_E_DIALOG_MSG")).html();
                    var confirmation = parsed.html($translate.instant("CONFIRMATION")).html();
                    var addAffectedItemTitle = parsed.html($translate.instant("ADDED_AFFECTED_ITEM")).html();
                    var pleaseSelectLifecyclePhase = parsed.html($translate.instant("P_S_LIFECYCLE_PHASE")).html();
                    var pleaseSelectActivity = parsed.html($translate.instant("P_S_ACTIVITY")).html();

                    $scope.workflow = null;
                    $scope.workflowHistory = [];
                    $scope.currentStatus = null;
                    $scope.selectedStatus = null;
                    $scope.startWorkflow = startWorkflow;
                    $scope.promote = promote;
                    $scope.demote = demote;
                    $scope.putWorkflowOnHold = putWorkflowOnHold;
                    $scope.removeWorkflowOnHold = removeWorkflowOnHold;
                    $scope.saveAssignment = saveAssignment;
                    $scope.saveStatusAssignment = saveStatusAssignment;
                    $scope.showPersons = showPersons;
                    var selectActivity = $stateParams.workflowActivity;
                    $scope.noWorkflowMsg = parsed.html($translate.instant("NO_WORKFLOW_MSG")).html();
                    $scope.reqWorkflowMsg = parsed.html($translate.instant("REQ_WORKFLOW_MSG")).html();
                    var productInspectionPlan = parsed.html($translate.instant("PRODUCT_INSPECTION_PLAN")).html();
                    var materialInspectionPlan = parsed.html($translate.instant("MATERIAL_INSPECTION_PLAN")).html();
                    var problemReport = parsed.html($translate.instant("PROBLEM_REPORT")).html();
                    var variance = parsed.html($translate.instant("VARIANCE")).html();
                    var item = parsed.html($translate.instant("ITEM")).html();
                    var manufacturer = parsed.html($translate.instant("MANUFACTURER")).html();
                    var manufacturerPart = parsed.html($translate.instant("MANUFACTURER_PART")).html();
                    var supplierAudit = parsed.html($translate.instant("SUPPLIER_AUDIT")).html();
                    var terminology = parsed.html($translate.instant("TERMINOLOGY")).html();
                    var specification = parsed.html($translate.instant("SPECIFICATION")).html();
                    var requirement = parsed.html($translate.instant("REQUIREMENT")).html();
                    var projectTemplate = parsed.html($translate.instant("PROJECTTEMPLATE")).html();
                    var programTemplate = parsed.html($translate.instant("PROGRAMTEMPLATE")).html();
                    var bop = parsed.html($translate.instant("BOP")).html();
                    var project = parsed.html($translate.instant("PROJECT")).html();
                    var activity = parsed.html($translate.instant("ACTIVITY")).html();
                    var task = parsed.html($translate.instant("TASK")).html();
                    var program = parsed.html($translate.instant("PROGRAM")).html();
                    var reqDocument = parsed.html($translate.instant("REQDOCUMENT")).html();
                    var mbomTitle = parsed.html($translate.instant("MBOM")).html();
                    var workOrder = parsed.html($translate.instant("WORK_ORDER")).html();
                    var npr = parsed.html($translate.instant("NPR")).html();
                    var customObject = parsed.html($translate.instant("CUSTOM_OBJECT")).html();
                    var workRequest = parsed.html($translate.instant("WORK_REQUEST")).html();
                    var maintenancePlan = parsed.html($translate.instant("MAINTENANCE_PLAN")).html();

                    var objectType = null;
                    $scope.active = -1;
                    $scope.activeTab = -1;
                    $scope.workflowEventTypes = [
                        {label: 'Workflow Start', value: 'WorkflowStart'},
                        //{label: 'Workflow Finish', value: 'WorkflowFinish'},
                        //{label: 'Workflow Hold', value: 'WorkflowHold'},
                        //{label: 'Workflow Unhold', value: 'WorkflowUnHold'},
                        //{label: 'Workflow Cancelled', value: 'WorkflowCancelled'},
                        {label: 'Workflow Activity Start', value: 'WorkflowActivityStart'},
                        {label: 'Workflow Activity Finish', value: 'WorkflowActivityFinish'}
                    ];

                    $scope.workflowActionTypes = [
                        {label: 'Set Lifecycle Phase', value: 'SetLifecyclePhase'}
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
                        history: {
                            id: 'details.history',
                            heading: "History",
                            template: 'app/desktop/directives/workflow/workflowHistoryView.jsp',
                            index: 0,
                            active: true,
                            activated: true
                        },
                        attributes: {
                            id: 'details.attributes',
                            heading: 'Attributes',
                            template: 'app/desktop/directives/workflow/workflowAttributesView.jsp',
                            index: 1,
                            active: false,
                            activated: false
                        },
                        events: {
                            id: 'details.events',
                            heading: 'Events',
                            template: 'app/desktop/directives/workflow/workflowEventsView.jsp',
                            index: 2,
                            active: false,
                            activated: false
                        }
                    };

                    $scope.activityTabs = {
                        persons: {
                            id: 'details.persons',
                            heading: "Persons",
                            template: 'app/desktop/directives/workflow/workflowActivityPersonsView.jsp',
                            index: 0,
                            active: true,
                            activated: true
                        },
                        formData: {
                            id: 'details.formData',
                            heading: 'Form Data',
                            template: 'app/desktop/directives/workflow/workflowActivityFormDataView.jsp',
                            index: 1,
                            active: false,
                            activated: false
                        }
                    };

                    $scope.events = [];
                    $scope.eventsLoading = true;
                    $scope.selectWorkflowTab = selectWorkflowTab;
                    function selectWorkflowTab(id) {
                        if (id == "details.history") {
                            $scope.active = 0;
                            loadWorkflowHistory();
                        } else if (id == "details.attributes") {
                            $scope.active = 1;
                            $timeout(function () {
                                $scope.$broadcast('app.attributes.tabActivated', {});
                                var height = $('.view-content').outerHeight();
                                $('.workflow-tabs').height(height - 100);
                                $('.workflow-tabs .tab-content').height(height - 150);
                                $('.workflow-tabs .tab-content .tab-pane').height(height - 150);
                                $('#workflow-attributes-view').height($('.workflow-tabs .tab-content .tab-pane').outerHeight() - 20);
                            }, 1000);
                        } else {
                            $scope.active = 2;
                            loadEvents();
                            if ($scope.object.objectType == "CHANGE" && ($scope.object.changeType == "ECO" || $scope.object.changeType == "DCO")) {
                                loadAffectedItems();
                            } else if ($scope.object.objectType == "ITEMMCO") {
                                loadAffectedItems();
                            }
                        }
                        var height = $('.view-content').outerHeight();
                        $('.workflow-tabs').height(height - 100);
                        $('.workflow-tabs .tab-content').height(height - 150);
                        $('.workflow-tabs .tab-content .tab-pane').height(height - 150);
                    }

                    $scope.eventTypes = [];
                    function loadEvents() {
                        $scope.eventsLoading = true;
                        WorkflowService.getWorkflowEvents($scope.workflow.id).then(
                            function (data) {
                                $scope.selectedEvent.event = null;
                                $scope.events = data;
                                filterEventTypes();
                                $scope.eventsLoading = false;
                                var height = $('.view-content').outerHeight();
                                $('.workflow-tabs').height(height - 100);
                                $('.workflow-tabs .tab-content').height(height - 150);
                                $('.workflow-tabs .tab-content .tab-pane').height(height - 150);
                                $('#events-view').height($('.workflow-tabs .tab-content .tab-pane').outerHeight());
                                $scope.$evalAsync();
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
                                    if (eventType.value == "WorkflowStart") {
                                        if (!$scope.workflow.started) {
                                            if ($scope.eventTypes.indexOf(eventType) == -1) {
                                                $scope.eventTypes.push(eventType);
                                            }
                                        }
                                    } else {
                                        if ($scope.eventTypes.indexOf(eventType) == -1) {
                                            $scope.eventTypes.push(eventType);
                                        }
                                    }
                                }
                            })
                        }, 500)
                    }

                    function loadAffectedItems() {
                        if ($scope.object.changeType == "ECO" || $scope.object.changeType == "DCO") {
                            ECOService.getChangeAffectedItems($scope.object.id).then(
                                function (data) {
                                    $scope.changeAffectedItems = data;
                                }
                            )
                        } else {
                            MCOService.getProductAffectedItems($scope.object.id).then(
                                function (data) {
                                    $scope.changeAffectedItems = data;
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }

                    }

                    $scope.selectedEvent = {
                        event: null
                    };
                    $scope.onSelectEvent = onSelectEvent;
                    function onSelectEvent(newEvent) {
                        if (newEvent.value == "WorkflowStart" && $scope.workflow.started) {
                            $rootScope.showWarningMessage("Workflow already started.");
                        } else {
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
                    }

                    $scope.onSelectActivity = onSelectActivity;
                    function onSelectActivity(activity, workflowEvent) {
                        if (workflowEvent.eventType == "WorkflowActivityStart" && $scope.workflow.currentStatus == activity.id) {
                            $rootScope.showWarningMessage(activity.name + " activity already started");
                            workflowEvent.activity = null;
                        } else if (workflowEvent.eventType == "WorkflowActivityStart" && activity.flag == "COMPLETED") {
                            $rootScope.showWarningMessage(activity.name + " activity already completed");
                            workflowEvent.activity = null;
                        } else if (workflowEvent.eventType == "WorkflowActivityFinish" && activity.flag == "COMPLETED") {
                            $rootScope.showWarningMessage(activity.name + " activity already completed");
                            workflowEvent.activity = null;
                        }
                        workflowEvent.actionData = null;
                    }

                    function addEvent(newEvent) {
                        var actionType = null;
                        if ($scope.workflowActionTypes.length == 1) {
                            actionType = $scope.workflowActionTypes[0].value;
                        }
                        $scope.event = {
                            eventType: newEvent.value,
                            workflowEvents: [
                                {
                                    id: null,
                                    workflow: $scope.workflow.id,
                                    activity: null,
                                    eventType: newEvent.value,
                                    actionType: actionType,
                                    actionData: null,
                                    actionDataCopy: null
                                }
                            ]
                        };
                        $scope.events.push($scope.event);
                        filterEventTypes();
                    }

                    $scope.addWorkflowEvent = addWorkflowEvent;
                    function addWorkflowEvent(event) {
                        var actionType = null;
                        if ($scope.workflowActionTypes.length == 1) {
                            actionType = $scope.workflowActionTypes[0].value;
                        }
                        event.workflowEvents.push({
                            id: null,
                            workflow: $scope.workflow.id,
                            activity: null,
                            eventType: event.eventType,
                            actionType: actionType,
                            actionData: null,
                            actionDataCopy: null
                        })
                    }

                    $scope.saveWorkflowEvent = saveWorkflowEvent;
                    function saveWorkflowEvent(workflowEvent) {
                        if (workflowEvent.id == null || workflowEvent.id == "" || workflowEvent.id == undefined) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            WorkflowService.createWorkflowEvent($scope.workflow.id, workflowEvent).then(
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
                            WorkflowService.updateWorkflowEvent($scope.workflow.id, workflowEvent).then(
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

                    $scope.deleteWorkflowEvent = deleteWorkflowEvent;
                    function deleteWorkflowEvent(event, workflowEvent) {
                        if (workflowEvent.id == null || workflowEvent.id == "") {
                            event.workflowEvents.splice(event.workflowEvents.indexOf(workflowEvent), 1);
                            if (event.workflowEvents.length == 0) {
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
                                        WorkflowService.deleteWorkflowEvent($scope.workflow.id, workflowEvent.id).then(
                                            function (data) {
                                                event.workflowEvents.splice(event.workflowEvents.indexOf(workflowEvent), 1);
                                                if (event.workflowEvents.length == 0) {
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
                        WorkflowService.getWorkflowStatuses($scope.workflow.id).then(
                            function (data) {
                                angular.forEach(data, function (status) {
                                    if ((status.type == 'NORMAL' || status.type == 'RELEASED' || status.type == 'REJECTED') && status.flag != 'COMPLETED') {
                                        $scope.workflowStatuses.push(status);
                                    }
                                })
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }

                    $scope.showSetLifecycleDialog = showSetLifecycleDialog;
                    function showSetLifecycleDialog(workflowEvent) {
                        $scope.showApplyAllButton = false;
                        $scope.selectedWorkflowEvent = null;
                        $scope.selectedLifecyclePhase = null;
                        if (($scope.objectType == "ECO" && $rootScope.ecoAffectedItems > 0) || ($scope.objectType == "DCO" && $rootScope.affectedItems > 0) || ($scope.objectType == "MCO" && $rootScope.mcoAffectedItems > 0) || $scope.objectType == "BOPREVISION") {
                            if ((workflowEvent.eventType == "WorkflowActivityStart" || workflowEvent.eventType == "WorkflowActivityFinish") && workflowEvent.activity == null || workflowEvent.activity == "") {
                                $rootScope.showWarningMessage(pleaseSelectActivity);
                            } else {
                                if ($scope.objectType == "BOPREVISION") {
                                    if (workflowEvent.actionData != null && workflowEvent.actionData != "") {
                                        workflowEvent.actionDataList = JSON.parse(workflowEvent.actionData);
                                    } else {
                                        workflowEvent.actionDataList = [];
                                        $scope.bopDetails.type.lifecycle.normalLifecyclePhases = [];
                                        $scope.bopDetails.type.lifecycle.releasedLifecyclePhases = [];
                                        $scope.bopDetails.type.lifecycle.rejectedLifecyclePhases = [];
                                        angular.forEach($scope.bopDetails.type.lifecycle.phases, function (phase) {
                                            if (phase.phaseType == "RELEASED") {
                                                $scope.bopDetails.type.lifecycle.releasedLifecyclePhases.push(phase);
                                            } else if (phase.phaseType == "CANCELLED") {
                                                $scope.bopDetails.type.lifecycle.rejectedLifecyclePhases.push(phase);
                                            } else if (phase.phaseType != "OBSOLETE") {
                                                $scope.bopDetails.type.lifecycle.normalLifecyclePhases.push(phase);
                                            }
                                        });
                                        var emptyData = {
                                            lifecycle: $scope.bopDetails.type.lifecycle,
                                            lifecyclePhase: null
                                        };
                                        workflowEvent.actionDataList.push(emptyData);
                                    }
                                } else {
                                    $scope.affectedItems = angular.copy($scope.changeAffectedItems);
                                    if (workflowEvent.actionData != null && workflowEvent.actionData != "") {
                                        var items = JSON.parse(workflowEvent.actionData);
                                        angular.forEach(items, function (item) {
                                            angular.forEach($scope.affectedItems, function (affectedItem) {
                                                if (item.id == affectedItem.id) {
                                                    affectedItem.toLifecyclePhase = item.toLifecyclePhase;
                                                }
                                            })
                                        })
                                    }
                                    $scope.emptyToLifecyclePhaseCount = 0;
                                    angular.forEach($scope.affectedItems, function (item) {
                                        if (workflowEvent.eventType == "WorkflowActivityStart" && workflowEvent.activity != null && workflowEvent.activity.type == "NORMAL" && item.normalLifecyclePhases.length == 1) {
                                            item.toLifecyclePhase = item.normalLifecyclePhases[0];
                                        } else if (workflowEvent.eventType == "WorkflowActivityStart" && workflowEvent.activity != null && workflowEvent.activity.type == "RELEASED" && item.normalLifecyclePhases.length == 1) {
                                            item.toLifecyclePhase = item.normalLifecyclePhases[0];
                                        } else if (workflowEvent.eventType == "WorkflowActivityFinish" && workflowEvent.activity != null && workflowEvent.activity.type == "NORMAL" && item.normalLifecyclePhases.length == 1) {
                                            item.toLifecyclePhase = item.normalLifecyclePhases[0];
                                        } else if (workflowEvent.eventType == "WorkflowActivityFinish" && workflowEvent.activity != null && workflowEvent.activity.type == "RELEASED" && item.releasedLifecyclePhases.length == 1) {
                                            item.toLifecyclePhase = item.releasedLifecyclePhases[0];
                                        }

                                        if (item.toLifecyclePhase == null || item.toLifecyclePhase == "") {
                                            $scope.emptyToLifecyclePhaseCount++;
                                        }
                                    })
                                }

                                var modal = document.getElementById("workflow-event");
                                modal.style.display = "block";
                                $timeout(function () {
                                    var headerHeight = $('.workflow-event-header').outerHeight();
                                    var bomContentHeight = $('.workflow-event-content').outerHeight();
                                    $(".event-content").height(bomContentHeight - headerHeight);
                                    $scope.errorNotification = $('#configuration-error');
                                    $scope.selectedWorkflowEvent = workflowEvent
                                }, 200)
                            }
                        } else {
                            $rootScope.showWarningMessage(addAffectedItemTitle);
                        }

                    }

                    $scope.showScriptDialog = showScriptDialog;
                    function showScriptDialog(workflowEvent) {
                        $scope.selectedWorkflowEvent = null;
                        var modal = document.getElementById("workflow-event");
                        modal.style.display = "block";
                        $timeout(function () {
                            var headerHeight = $('.workflow-event-header').outerHeight();
                            var bomContentHeight = $('.workflow-event-content').outerHeight();
                            $(".event-content").height(bomContentHeight - headerHeight);
                            $scope.errorNotification = $('#configuration-error');
                            $scope.selectedWorkflowEvent = workflowEvent
                        }, 200)
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

                    $scope.onSelectLifecyclePhase = onSelectLifecyclePhase;
                    function onSelectLifecyclePhase(phase) {
                        $scope.selectedLifecyclePhase = phase;
                        $scope.showApplyAllButton = true;
                        if ($scope.emptyToLifecyclePhaseCount > 0) {
                            $scope.emptyToLifecyclePhaseCount--;
                        }
                    }

                    $scope.applyPhaseLifecyclePhase = applyPhaseLifecyclePhase;
                    function applyPhaseLifecyclePhase() {
                        angular.forEach($scope.affectedItems, function (item) {
                            if (item.toLifecyclePhase == null || item.toLifecyclePhase == "") {
                                item.toLifecyclePhase = $scope.selectedLifecyclePhase;
                            }
                        })
                        $scope.selectedLifecyclePhase = null;
                        $scope.showApplyAllButton = false;
                    }

                    $scope.addAffectedItemToEvent = addAffectedItemToEvent;
                    function addAffectedItemToEvent() {
                        if ($scope.selectedWorkflowEvent.actionType == "SetLifecyclePhase") {
                            if (validateAffectedItems()) {
                                var items = [];
                                if ($scope.objectType != "BOPREVISION") {
                                    $scope.selectedWorkflowEvent.actionData = angular.toJson($scope.affectedItems);
                                    $scope.selectedWorkflowEvent.actionDataCopy = $scope.affectedItems;
                                } else {
                                    $scope.selectedWorkflowEvent.actionData = angular.toJson($scope.selectedWorkflowEvent.actionDataList);
                                    $scope.selectedWorkflowEvent.actionDataCopy = $scope.selectedWorkflowEvent.actionDataList;
                                }
                                saveWorkflowEvent($scope.selectedWorkflowEvent);
                            }
                        } else if ($scope.selectedWorkflowEvent.actionType == "ExecuteScript") {
                            var editor = ace.edit("execute-script");
                            $scope.selectedWorkflowEvent.actionData = editor.getValue();
                            editor.session.setValue("");
                            saveWorkflowEvent($scope.selectedWorkflowEvent);
                        }
                    }

                    function validateAffectedItems() {
                        var valid = true;
                        angular.forEach($scope.affectedItems, function (affectedItem) {
                            if (valid) {
                                if (affectedItem.toLifecyclePhase == null || affectedItem.toLifecyclePhase == "" || affectedItem.toLifecyclePhase == undefined) {
                                    valid = false;
                                    $scope.errorNotification.show();
                                    $scope.errorNotification.removeClass('zoomOut');
                                    $scope.errorNotification.addClass('zoomIn');
                                    $scope.notificationClass = "fa-warning";
                                    $scope.notificationBackground = "alert-warning";
                                    $scope.message = pleaseSelectLifecyclePhase.format(affectedItem.itemNumber);
                                    $timeout(function () {
                                        closeErrorNotification();
                                    }, 3000);
                                }
                            }
                        })
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
                        if (modal != null && modal != undefined) {
                            modal.style.display = "none";
                        }
                    }


                    $scope.checkDeleteEvent = checkDeleteEvent;
                    function checkDeleteEvent(workflowEvent) {
                        var hide = false;
                        if ((workflowEvent.activity == null || workflowEvent.activity == "") && workflowEvent.eventType == "WorkflowStart" && $scope.workflow.started) {
                            hide = true;
                        } else if ((workflowEvent.activity == null || workflowEvent.activity == "") && workflowEvent.eventType == "WorkflowFinish" && $scope.workflow.finished) {
                            hide = true;
                        } else if (workflowEvent.activity != null && workflowEvent.activity.flag == "COMPLETED" && (workflowEvent.eventType == "WorkflowActivityStart" || workflowEvent.eventType == "WorkflowActivityFinish")) {
                            hide = true;
                        } else if (workflowEvent.activity != null && workflowEvent.activity.id == $scope.workflow.currentStatus && workflowEvent.eventType == "WorkflowActivityStart") {
                            hide = true;
                        } else if ($scope.workflow.finished) {
                            hide = true;
                        }
                        return hide;
                    }

                    $scope.checkDisableEvent = checkDisableEvent;
                    function checkDisableEvent() {
                        var hide = false;
                        if ($scope.selectedWorkflowEvent != null) {
                            if (($scope.selectedWorkflowEvent.activity == null || $scope.selectedWorkflowEvent.activity == "") && $scope.selectedWorkflowEvent.eventType == "WorkflowStart" && $scope.workflow.started) {
                                hide = true;
                            } else if (($scope.selectedWorkflowEvent.activity == null || $scope.selectedWorkflowEvent.activity == "") && $scope.selectedWorkflowEvent.eventType == "WorkflowFinish" && $scope.workflow.finished) {
                                hide = true;
                            } else if ($scope.selectedWorkflowEvent.activity != null && ($scope.selectedWorkflowEvent.activity.flag == "COMPLETED" || $scope.selectedWorkflowEvent.activity.flag == "INPROCESS") && ($scope.selectedWorkflowEvent.eventType == "WorkflowActivityStart" || $scope.selectedWorkflowEvent.eventType == "WorkflowActivityFinish")) {
                                hide = true;
                            } else if ($scope.selectedWorkflowEvent.activity != null && $scope.selectedWorkflowEvent.activity.id == $scope.workflow.currentStatus && $scope.selectedWorkflowEvent.eventType == "WorkflowActivityStart") {
                                hide = true;
                            } else if ($scope.workflow.finished) {
                                hide = true;
                            }
                        }
                        return hide;
                    }

                    function loadWorkflowTypes() {
                        if ($scope.objectType == "INSPECTIONPLANREVISION") {
                            var promise = null;
                            if ($scope.object.plan.objectType == "PRODUCTINSPECTIONPLAN") {
                                $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(productInspectionPlan);
                                promise = InspectionPlanService.getProductInspectionPlan($scope.object.plan.id);
                            } else {
                                $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(materialInspectionPlan);
                                promise = InspectionPlanService.getMaterialInspectionPlan($scope.object.plan.id);
                            }
                            if (promise != null) {
                                promise.then(
                                    function (data) {
                                        objectType = data.planType.id;
                                    }
                                )
                            }
                        } else if ($scope.objectType == "PROBLEMREPORT") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(problemReport);
                            objectType = $scope.object.prType.id;
                        } else if ($scope.objectType == "NCR") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format("NCR");
                            objectType = $scope.object.ncrType.id;
                        } else if ($scope.objectType == "QCR") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format("QCR");
                            objectType = $scope.object.qcrType.id;
                        } else if ($scope.objectType == "ECO") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format("ECO");
                            objectType = $scope.object.ecoType;
                        } else if ($scope.objectType == "DCO") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format("DCO");
                            objectType = $scope.object.dcoType;
                        } else if ($scope.objectType == "ECR" || $scope.objectType == "DCR") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format($scope.objectType);
                            objectType = $scope.object.crType;
                        } else if ($scope.objectType == "MCO") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format("MCO");
                            objectType = $scope.object.mcoType.id;
                        } else if ($scope.objectType == "VARIANCE") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(variance);
                            objectType = $scope.object.changeClass.id;
                        } else if ($scope.objectType == "ITEMREVISION") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(item);
                            promise = ItemService.getItem($scope.object.itemMaster);
                            if (promise != null) {
                                promise.then(
                                    function (data) {
                                        objectType = data.itemType.id;
                                    }
                                )
                            }
                        } else if ($scope.objectType == "MANUFACTURER") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(manufacturer);
                            objectType = $scope.object.mfrType.id;
                        } else if ($scope.objectType == "MANUFACTURERPART") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(manufacturerPart);
                            objectType = $scope.object.mfrPartType.id;
                        }
                        else if ($scope.objectType == "SUPPLIERAUDIT") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(supplierAudit);
                            objectType = $scope.object.type.id;
                        } else if ($scope.objectType == "SPECIFICATION") {
                            if ($scope.object.workflow == null && $scope.object.released == false) {
                                $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(specification);
                            } else {
                                $scope.noWorkflowMsg = $scope.reqWorkflowMsg.format(specification);
                            }
                            objectType = $scope.object.type.id;
                        } else if ($scope.objectType == "PROJECT") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(project);
                        } else if ($scope.objectType == "PROJECTACTIVITY" || $scope.objectType == "TEMPLATEACTIVITY") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(activity);
                        }
                        else if ($scope.objectType == "MROWORKORDER") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(workOrder);
                            objectType = $scope.object.type.id;
                        }
                        else if ($scope.objectType == "NPR") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(npr);
                        }
                        else if ($scope.objectType == "CUSTOMOBJECT") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(customObject);
                            objectType = $scope.object.type.id;
                        } else if ($scope.objectType == "PROJECTTASK" || $scope.objectType == "TEMPLATETASK") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(task);

                        } else if ($scope.objectType == "PROGRAM") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(program);

                        } else if ($scope.objectType == "MBOMREVISION") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(mbomTitle);
                            MBOMService.getMBOM($scope.object.master).then(
                                function (data) {
                                    objectType = data.type.id;
                                }
                            )
                        }
                        else if ($scope.objectType == "REQUIREMENT") {
                            objectType = $scope.object.requirementVersion.master.type.id;
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(requirement);

                        }
                        else if ($scope.objectType == "REQUIREMENTDOCUMENT") {
                            objectType = $scope.object.master.type.id;
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(requirement);
                        }
                        else if ($scope.objectType == "BOPREVISION") {
                            BOPService.getBOP($scope.object.master).then(
                                function (data) {
                                    objectType = data.type.id;
                                    $scope.bopDetails = data;
                                }
                            )
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(bop);
                        } else if ($scope.objectType == "PROJECTTEMPLATE") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(projectTemplate);
                        } else if ($scope.objectType == "PROGRAMTEMPLATE") {
                            $scope.noWorkflowMsg = $scope.noWorkflowMsg.format(programTemplate);
                        }
                    }

                    function loadWorkflow() {
                        if (!workflowLoaded) {
                            $scope.workflow = null;
                            if ($scope.object.workflow != null) {
                                WorkflowService.getWorkflow($scope.object.workflow).then(
                                    function (data) {
                                        $scope.workflow = data;

                                        buildMaps();

                                        $scope.workflow.currentStatusObject = getStatusById($scope.workflow.currentStatus);

                                        viewer = new Modeler({
                                            container: '#canvas',
                                            additionalModules: [{
                                                zoomScroll: ['value', '']
                                            }]
                                        });

                                        viewer.importXML($scope.workflow.diagram, function (err) {
                                            if (!err) {
                                                resizeCanvas();
                                                workflowLoaded = true;
                                                if ($scope.objectType == "ECO" || $scope.objectType == "DCO") {
                                                    highlightRevisionTriggerStatus();
                                                }
                                                $rootScope.hideBusyIndicator();

                                            } else {
                                                console.log('Error loading workflow file:', err);
                                                $rootScope.hideBusyIndicator();
                                            }

                                            if ($scope.objectType == "ECO" || $scope.objectType == "DCO") {
                                                if (selectActivity !== undefined && selectActivity != null) {
                                                    triggerActivitySelection();
                                                }
                                            }
                                        });
                                        selectWorkflowTab('details.history');
                                        initEvents(viewer);
                                        processWorkflowFile($scope.workflow.diagram);
                                        loadAssignments();
                                        loadWorkflowStatuses();
                                        if ($scope.objectType == "ECO" || $scope.objectType == "DCO" || ($scope.objectType == "MCO" && $scope.object.mcoType.mcoType == "ITEMMCO")) {
                                            $scope.workflowActionTypes = [
                                                {label: 'Set Lifecycle Phase', value: 'SetLifecyclePhase'},
                                                {label: 'ExecuteScript', value: 'ExecuteScript'}
                                            ];
                                        } else {
                                            $scope.workflowActionTypes = [
                                                {label: 'Set Lifecycle Phase', value: 'SetLifecyclePhase'}
                                            ];
                                        }
                                    }
                                )
                            }
                            else {
                                $rootScope.hideBusyIndicator();
                            }
                        }
                        else {
                            reIntializeWorkflow();
                        }

                    }

                    function reIntializeWorkflow() {
                        $scope.workflow = null;
                        if ($scope.object.workflow != null) {
                            WorkflowService.getWorkflow($scope.object.workflow).then(
                                function (data) {
                                    $scope.workflow = data;
                                    buildMaps();
                                    $scope.workflow.currentStatusObject = getStatusById($scope.workflow.currentStatus);
                                    viewer.importXML($scope.workflow.diagram, function (err) {
                                        if (!err) {
                                            resizeCanvas();
                                            workflowLoaded = true;
                                            if ($scope.objectType == "ECO" || $scope.objectType == "DCO") {
                                                highlightRevisionTriggerStatus();
                                            }
                                            $rootScope.hideBusyIndicator();

                                        } else {
                                            console.log('Error loading workflow file:', err);
                                            $rootScope.hideBusyIndicator();
                                        }

                                        if ($scope.objectType == "ECO" || $scope.objectType == "DCO") {
                                            if (selectActivity !== undefined && selectActivity != null) {
                                                triggerActivitySelection();
                                            }
                                        }
                                    });

                                    initEvents(viewer);
                                    processWorkflowFile($scope.workflow.diagram);
                                    loadAssignments();
                                    loadWorkflowHistory();
                                }
                            )
                        }
                        else {
                            $rootScope.hideBusyIndicator();
                        }
                    }

                    function highlightRevisionTriggerStatus() {
                        if ($scope.object.revisionCreationType === "ACTIVITY_COMPLETION") {
                            for (var i = 0; i < $scope.workflow.statuses.length; i++) {
                                var status = $scope.workflow.statuses[i];
                                if ($scope.object.workflowStatus === status.id) {
                                    var modeling = viewer.get('modeling');
                                    var registry = viewer.get('elementRegistry');
                                    var found = registry.get(status.diagramId);
                                    if (found !== null) {
                                        found.type = "bpmn:ServiceTask";
                                        modeling.updateProperties(found, {type: "bpmn:ServiceTask"});
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    function triggerActivitySelection() {
                        for (var i = 0; i < $scope.workflow.statuses.length; i++) {
                            var status = $scope.workflow.statuses[i];
                            var id = "" + status.id;
                            if (id === selectActivity) {
                                var activity = viewer.get('elementRegistry').get(status.diagramId);
                                if (activity != null) {
                                    viewer.get('selection').select(activity);
                                    selectStatus(status);
                                    break;
                                }
                            }
                        }
                    }

                    function buildMaps() {
                        var map = new Hashtable();

                        map.put($scope.workflow.start.id, $scope.workflow.start);
                        map.put($scope.workflow.finish.id, $scope.workflow.finish);

                        mapStatuses.put($scope.workflow.start.diagramId, $scope.workflow.start);
                        mapStatuses.put($scope.workflow.finish.diagramId, $scope.workflow.finish);

                        angular.forEach($scope.workflow.statuses, function (status) {
                            status.approvers = [];
                            status.observers = [];
                            status.acknowledgers = [];
                            status.transitions = getTransitionsFromStatus(status.id);

                            map.put(status.id, status);
                            mapStatuses.put(status.diagramId, status);
                        });

                        angular.forEach($scope.workflow.transitions, function (transition) {
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

                    function resizeCanvas() {
                        $timeout(function () {
                            var buttonContainerHeight = $('.button-container').outerHeight();
                            if (buttonContainerHeight == null) {
                                buttonContainerHeight = 40;
                            } else {
                                buttonContainerHeight = buttonContainerHeight + 40;
                            }
                            $('.workflow-viewer').height($('.tab-pane').outerHeight() - buttonContainerHeight);
                            if (viewer != null) {
                                viewer.get('canvas').zoom('fit-viewport', 'auto');
                            }
                            resizeWorkflowHistoryTabs();
                        });
                    }

                    function getTransitionsFromStatus(status) {
                        var transitionArr = [];
                        var transitions = $scope.workflow.transitions;

                        angular.forEach(transitions, function (t) {
                            if (t.fromStatus == status) {
                                t.fromStatusObject = getStatusById(status);
                                transitionArr.push(t);
                            }
                        });

                        return transitionArr;
                    }

                    function getStatusById(id) {
                        var found = null;

                        if ($scope.workflow.start.id == id) {
                            found = $scope.workflow.start;
                        }
                        else if ($scope.workflow.finish.id == id) {
                            found = $scope.workflow.finish;
                        }
                        else {
                            angular.forEach($scope.workflow.statuses, function (status) {
                                if (status.id == id) {
                                    found = status;
                                }
                            })
                        }

                        return found;
                    }

                    function resizeWorkflowHistoryTabs() {
                        var viewContent = $(".view-content").outerHeight();
                        $("#workflow-history-tabs .tab-content").height(viewContent - 120);
                        $("#workflow-history-tabs .tab-pane").height(viewContent - 120);

                        var height = $('.view-content').outerHeight();
                        $('.workflow-tabs').height(height - 100);
                        $('.workflow-tabs .tab-content').height(height - 150);
                        $('.workflow-tabs .tab-content .tab-pane').height(height - 150);
                        $('#events-view').height($('.workflow-tabs .tab-content .tab-pane').outerHeight());
                    }

                    function initEvents(viewer) {
                        var eventBus = viewer.get('eventBus');
                        eventBus.on('element.click', function (e) {
                            if (e.element.type === 'bpmn:Task' || e.element.type === 'bpmn:ServiceTask') {
                                var status = mapStatuses.get(e.element.id);
                                if (status != null) {
                                    selectStatus(status);
                                }
                            }
                            else {
                                $scope.selectedStatus = null;
                                loadEvents();
                            }

                            $scope.$apply();
                        });
                    }

                    function selectStatus(status) {
                        $scope.selectedStatus = status;
                        selectWorkflowActivityTab('details.persons');
                        $scope.$apply();
                    }

                    function processWorkflowFile(xml) {
                        var moddle = viewer._createModdle({});
                        moddle.fromXML(xml, function (err, definitions) {
                            var rootElement = definitions.rootElements[0];
                            var flowElements = rootElement.flowElements;
                            angular.forEach(flowElements, function (element) {
                                if (element.$type == 'bpmn:StartEvent' ||
                                    element.$type == 'bpmn:EndEvent' ||
                                    element.$type == 'bpmn:IntermediateThrowEvent' ||
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
                        setColors($scope.workflow.start);
                        setColors($scope.workflow.finish);
                        angular.forEach($scope.workflow.statuses, function (status) {
                            setColors(status);
                        })
                    }

                    function setColors(status) {
                        removeClassSVG(status.diagramId, 'inprocess-status');
                        removeClassSVG(status.diagramId, 'onhold-status');
                        removeClassSVG(status.diagramId, 'completed-status');
                        removeClassSVG(status.diagramId, 'terminated-status');
                        if (status.flag == 'INPROCESS') {
                            addClassSVG(status.diagramId, 'inprocess-status');
                        }
                        else if (status.flag == 'COMPLETED') {
                            addClassSVG(status.diagramId, 'completed-status');
                        }
                        else if (status.flag == 'TERMINATED') {
                            addClassSVG(status.diagramId, 'terminated-status');
                        }
                        else if (status.flag == 'ONHOLD') {
                            addClassSVG(status.diagramId, 'onhold-status');
                        }
                    }

                    function removeClassSVG(id, className) {
                        var node = svgNodes.get(id);
                        if (node != null) {
                            node.attr('class', function (index, existingClassNames) {
                                return existingClassNames.replace(className, '');
                            });
                        }
                    }

                    function addClassSVG(id, className) {
                        var node = svgNodes.get(id);
                        if (node != null) {
                            node.attr('class', function (index, existingClassNames) {
                                return existingClassNames + ' ' + className;
                            });
                        }
                    }

                    function loadAssignments() {
                        WorkflowService.getWorkflowAssignments($scope.workflow.id).then(
                            function (data) {
                                var assigmmentMap = data;
                                angular.forEach($scope.workflow.statuses, function (status) {
                                    var assignmentDto = assigmmentMap[status.id];
                                    status.approvers = assignmentDto.approvers;
                                    status.observers = assignmentDto.observers;
                                    status.acknowledgers = assignmentDto.acknowledgers;
                                    initAssigments(status.approvers);
                                    initAssigments(status.observers);
                                    initAssigments(status.acknowledgers);
                                    /*WorkflowService.getApprovers(status.id).then(
                                     function (data) {
                                     initAssigments(data);
                                     status.approvers = data;
                                     CommonService.getPersonReferences(status.approvers, 'person');
                                     }
                                     );

                                     WorkflowService.getObservers(status.id).then(
                                     function (data) {
                                     initAssigments(data);
                                     status.observers = data;
                                     CommonService.getPersonReferences(status.observers, 'person');
                                     }
                                     );

                                     WorkflowService.getAcknowledgers(status.id).then(
                                     function (data) {
                                     initAssigments(data);
                                     status.acknowledgers = data;
                                     CommonService.getPersonReferences(status.acknowledgers, 'person');
                                     }
                                     );*/
                                })
                            }
                        )
                    }

                    function initAssigments(assignments) {
                        angular.forEach(assignments, function (assignment) {
                            assignment.editMode = false;
                        });
                    }

                    function loadChangeApprovalPassword() {
                        var key = "APPLICATION.CHANGE_APPROVAL";
                        PreferenceService.getPreferenceByKey(key).then(
                            function (data) {
                                if (data == null || data == "") {
                                    $scope.changeApprovalPreference = {
                                        booleanValue: false
                                    }
                                } else {
                                    $scope.changeApprovalPreference = data;
                                }
                                loadPersonChangePassword();
                            }
                        )
                    }

                    function loadPersonChangePassword() {
                        PreferenceService.getUserChangeApprovalPassword($scope.personDetails.person.id).then(
                            function (data) {
                                $scope.personApprovalPassword = data;
                                $scope.personApprovalPassword.password = null;
                            }
                        )
                    }

                    function startWorkflow() {
                        if ($scope.canStartWorkflow) {
                            $rootScope.showBusyIndicator($('.view-content'));
                            WorkflowService.startWorkflow($scope.workflow.id).then(
                                function (data) {
                                    $scope.workflow.started = true;
                                    $scope.object.startWorkflow = true;
                                    $scope.workflow.start.flag = 'COMPLETED';
                                    $scope.workflow.startedOn = data.startedOn;
                                    if ($scope.workflow.currentStatus != null) {
                                        getStatusById($scope.workflow.currentStatus).flag = 'COMPLETED';
                                    }
                                    var transitions = getTransitionsFromStatus($scope.workflow.start.id);
                                    if (transitions.length == 1) {
                                        $scope.workflow.currentStatus = transitions[0].toStatus;
                                        $scope.workflow.currentStatusObject = transitions[0].toStatusObject;
                                        getStatusById($scope.workflow.currentStatus).flag = 'INPROCESS';
                                    }
                                    loadWorkflowHistory();
                                    updateStatusColors();
                                    loadObjects();
                                    loadWorkflowStatuses();
                                    loadEvents();
                                    if ($scope.objectType == "ECO") {
                                        $timeout(function () {
                                            updateItemConfigRules();
                                        }, 500);
                                    } else {
                                        $rootScope.hideBusyIndicator();
                                    }
                                },
                                function (error) {
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        } else {
                            if ($scope.objectType == "INSPECTIONPLANREVISION") {
                                $rootScope.showWarningMessage(addChecklistToStart);
                            } else if ($scope.objectType == "PROBLEMREPORT") {
                                $rootScope.showWarningMessage(addProblemItemToStartWorkflow);
                            } else if ($scope.objectType == "NCR") {
                                $rootScope.showWarningMessage(addProblemItemToStartWorkflow);
                            } else if ($scope.objectType == "QCR") {
                                if (!$rootScope.qcrDetailCount.capaPass) {
                                    $rootScope.showWarningMessage(capaValidation);
                                } else {
                                    $rootScope.showWarningMessage(addProblemItemToStartWorkflow);
                                }
                            } else if ($scope.objectType == "ECR") {
                                $rootScope.showWarningMessage(addAffectedItemToStartWorkflow);
                            } else if ($scope.objectType == "SUPPLIERAUDIT") {
                                $rootScope.showWarningMessage(addPlanToStartWorkflow);
                            }
                        }

                    }

                    $scope.holdType = null;
                    $scope.workflowHold = workflowHold;
                    function workflowHold() {
                        $scope.holdType = 'HOLD';
                        var modal = document.getElementById("hold-modal");
                        modal.style.display = "block";
                    }

                    $scope.workflowOnHold = workflowOnHold;
                    function workflowOnHold() {
                        $scope.holdType = 'ONHOLD';
                        var modal = document.getElementById("hold-modal");
                        modal.style.display = "block";
                    }

                    $scope.hideHoldDialog = hideHoldDialog;
                    function hideHoldDialog() {
                        var modal = document.getElementById("hold-modal");
                        modal.style.display = "none";
                        $scope.notes = null;
                        $scope.error = "";
                    }

                    function putWorkflowOnHold() {
                        if ($scope.notes == null || $scope.notes == '') {
                            $scope.error = enterNotes;
                        } else if ($scope.notes != null) {
                            $rootScope.showBusyIndicator($('.view-content'));
                            WorkflowService.putWorkflowOnHold($scope.workflow.id, $scope.workflow.currentStatus, $scope.notes).then(
                                function (data) {
                                    getStatusById($scope.workflow.currentStatus).flag = 'ONHOLD';
                                    $rootScope.hideBusyIndicator();
                                    updateStatusColors();
                                    $scope.workflow.holdBy = $scope.personDetails.person.id;
                                    loadWorkflowHistory();
                                    $scope.hideHoldDialog();
                                },
                                function (error) {
                                    $rootScope.hideBusyIndicator();
                                    $scope.hideHoldDialog();
                                }
                            )
                        }
                    }

                    function removeWorkflowOnHold() {
                        if ($scope.notes == null || $scope.notes == '') {
                            $scope.error = enterNotes;
                        } else if ($scope.notes != null) {
                            $rootScope.showBusyIndicator($('.view-content'));
                            WorkflowService.removeWorkflowOnHold($scope.workflow.id, $scope.workflow.currentStatus, $scope.notes).then(
                                function (data) {
                                    getStatusById($scope.workflow.currentStatus).flag = 'INPROCESS';
                                    updateStatusColors();
                                    $rootScope.hideBusyIndicator();
                                    loadWorkflowHistory();
                                    $scope.hideHoldDialog();
                                },
                                function (error) {
                                    $rootScope.hideBusyIndicator();
                                    $scope.hideHoldDialog();
                                }
                            )
                        }
                    }

                    function promote(toStatus) {
                        $rootScope.showBusyIndicator($('.view-content'));
                        if (toStatus != $scope.workflow.finish.id) {
                            WorkflowService.promoteWorkflow($scope.workflow.id, $scope.workflow.currentStatus, toStatus).then(
                                function (data) {
                                    getStatusById($scope.workflow.currentStatus).flag = 'COMPLETED';
                                    $scope.workflow.currentStatus = toStatus;
                                    $scope.workflow.currentStatus.transitionedFrom = data.transitionedFrom;
                                    var s = getStatusById($scope.workflow.currentStatus);
                                    $scope.workflow.currentStatusObject = s;
                                    $scope.workflow.currentStatusObject.transitionedFrom = data.transitionedFrom;
                                    s.flag = 'INPROCESS';
                                    if (s.type == 'TERMINATE') {
                                        s.flag = 'TERMINATED';
                                    }
                                    updateStatusColors();
                                    loadWorkflowHistory();
                                    loadAssignments();
                                    loadObjects();
                                    loadWorkflowStatuses();
                                    loadEvents();
                                    if ($scope.objectType == "ECO") {
                                        $timeout(function () {
                                            updateItemConfigRules();
                                        }, 500);
                                    } else {
                                        $rootScope.hideBusyIndicator();
                                    }
                                },
                                function (error) {
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                        else {
                            if ($scope.canFinishWorkflow) {
                                WorkflowService.finishWorkflow($scope.workflow.id).then(
                                    function (data) {
                                        $scope.workflow.finished = true;
                                        $scope.workflow.finishedOn = data.finishedOn;

                                        var s = getStatusById($scope.workflow.currentStatus);
                                        s.flag = 'COMPLETED';
                                        $scope.workflow.currentStatus = toStatus;
                                        $scope.workflow.currentStatusObject = s;
                                        s = getStatusById($scope.workflow.currentStatus);
                                        $scope.workflow.currentStatusObject = s;
                                        s.flag = 'COMPLETED';
                                        updateStatusColors();
                                        loadWorkflowHistory();
                                        loadObjects();
                                        loadWorkflowStatuses();
                                        loadEvents();
                                        if ($scope.objectType == "ECO") {
                                            $timeout(function () {
                                                updateItemConfigRules();
                                            }, 500);
                                        } else {
                                            $rootScope.hideBusyIndicator();
                                        }
                                    },
                                    function (error) {
                                        $rootScope.hideBusyIndicator();
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                );
                            } else {
                                if ($scope.objectType == "ITEMINSPECTION" || $scope.objectType == "MATERIALINSPECTION") {
                                    $rootScope.showWarningMessage(finishChecklistToPromote);
                                    $rootScope.hideBusyIndicator();
                                }
                            }
                        }
                    }

                    function demote() {
                        $rootScope.showBusyIndicator($('.view-content'));
                        WorkflowService.demoteWorkflow($scope.workflow.id).then(
                            function (demotedToStatus) {
                                if ($scope.workflow.currentStatus != null) {
                                    getStatusById($scope.workflow.currentStatus).flag = 'UNASSIGNED';
                                }
                                $scope.workflow.currentStatus = demotedToStatus.id;
                                var dStatus = getStatusById(demotedToStatus.id);
                                dStatus.flag = 'INPROCESS';
                                $scope.workflow.currentStatusObject = dStatus;

                                updateStatusColors();
                                loadAssignments();
                                loadWorkflowStatuses();
                                loadEvents();
                                loadWorkflowHistory();
                                $rootScope.hideBusyIndicator();
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        );
                    }

                    function saveStatusAssignment(assignment, status) {
                        if (assignment.assignmentType == 'APPROVER') {
                            if (status == "Approve") {
                                $scope.approveState = "Approve";
                            } else if (status == "Reject") {
                                $scope.approveState = "Reject";
                            }
                            if (assignment.comments == null || assignment.comments == "") {
                                assignment.editMode = true;
                            }
                        }
                    }

                    $scope.approveState = null;
                    function saveAssignment(assignment, status) {
                        var promise = null;
                        if (assignment.assignmentType == 'APPROVER') {
                            if (status == "Approve") {
                                $scope.approveState = "Approve";
                            } else if (status == "Reject") {
                                $scope.approveState = "Reject";
                            }
                            if (assignment.comments == null || assignment.comments == "") {
                                assignment.editMode = true;
                                $rootScope.showWarningMessage(enterComment)

                            } else {
                                if (status == "Approve") {
                                    if ($scope.changeApprovalPreference.booleanValue) {
                                        if ($scope.personApprovalPassword == null || $scope.personApprovalPassword == "" || $scope.personApprovalPassword.stringValue == "") {
                                            $rootScope.showWarningMessage(noChangeApprovalPassword);
                                        } else {
                                            $scope.selectedAssignment = assignment;
                                            $scope.workflowStatus = "APPROVE";
                                            showPasswordDialog();
                                        }
                                    } else {
                                        assignment.vote = "APPROVE";
                                        $rootScope.showBusyIndicator($('.view-content'));
                                        promise = WorkflowService.updateApprover($scope.selectedStatus.id, assignment);
                                        $scope.approveState = null;
                                    }
                                } else if (status == "Reject") {
                                    if ($scope.changeApprovalPreference.booleanValue) {
                                        if ($scope.personApprovalPassword == null || $scope.personApprovalPassword == "") {
                                            $rootScope.showWarningMessage(noChangeApprovalPassword);
                                        } else {
                                            $scope.selectedAssignment = assignment;
                                            $scope.workflowStatus = "REJECT";
                                            showPasswordDialog();
                                        }
                                    } else {
                                        assignment.vote = "REJECT";
                                        $rootScope.showBusyIndicator($('.view-content'));
                                        promise = WorkflowService.updateApprover($scope.selectedStatus.id, assignment);
                                        $scope.approveState = null;
                                    }
                                } else {
                                    $rootScope.showBusyIndicator($('.view-content'));
                                    promise = WorkflowService.updateApprover($scope.selectedStatus.id, assignment);
                                    $scope.approveState = null;
                                }
                            }
                        }
                        else if (assignment.assignmentType == 'OBSERVER') {

                            if (assignment.comments == null || assignment.comments == "") {
                                assignment.editMode = true;
                                $rootScope.showWarningMessage(enterComment)

                            } else {
                                promise = WorkflowService.updateObserver($scope.selectedStatus.id, assignment);
                            }

                        }
                        else if (assignment.assignmentType == 'ACKNOWLEDGER') {

                            if (status == "Acknowledge") {
                                assignment.acknowledged = true;
                            }
                            promise = WorkflowService.updateAcknowledger($scope.selectedStatus.id, assignment);
                            $rootScope.showSuccessMessage(currentStatusAcknowledged);

                        }

                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    assignment.editMode = false;
                                    if (status == "Approve") {
                                        $rootScope.showSuccessMessage(currentStatusApproved);
                                    } else if (status == "Reject") {
                                        $rootScope.showSuccessMessage(currentStatusRejected);
                                    }
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }

                    $scope.deleteWorkflowAssignment = deleteWorkflowAssignment;
                    function deleteWorkflowAssignment(assignment) {
                        WorkflowService.deleteWorkflowAssignment(assignment.status, assignment).then(
                            function (data) {
                                $rootScope.showSuccessMessage(assignment.assignmentType + ' : ' + $scope.personRemove);
                                loadAssignments();
                            }
                        )
                    }

                    function showPasswordDialog() {
                        var modal = document.getElementById("password-modal");
                        modal.style.display = "block";
                    }

                    $scope.hidePasswordDialog = hidePasswordDialog;
                    function hidePasswordDialog() {
                        var modal = document.getElementById("password-modal");
                        modal.style.display = "none";
                        $scope.personApprovalPassword.password = null;
                    }

                    function showPersons(type) {
                        var options = {
                            title: selectPersons,
                            template: 'app/desktop/modules/change/eco/details/tabs/workflow/personSelectionDialog.jsp',
                            controller: 'PersonSelectionController as personSelectionVm',
                            resolve: 'app/desktop/modules/change/eco/details/tabs/workflow/personSelectionController',
                            width: 600,
                            data: {
                                selectedPersonType: type,
                                selectedType: $scope.selectedStatus
                            },
                            showMask: true,
                            side: 'left',
                            buttons: [
                                {text: add, broadcast: 'app.workflow.persons'}
                            ],
                            callback: function (persons) {
                                addAssignments(type, persons);
                                $rootScope.hideSidePanel('left');
                            }
                        };
                        $rootScope.showSidePanel(options);
                    }

                    function addAssignments(type, persons) {
                        if ($scope.selectedStatus != null) {
                            var assignments = null;
                            var promise = null;

                            if (type == 'approvers') {
                                assignments = $scope.selectedStatus.approvers;
                            }
                            else if (type == 'observers') {
                                assignments = $scope.selectedStatus.observers;
                            }
                            else if (type == 'acknowledgers') {
                                assignments = $scope.selectedStatus.acknowledgers;
                            }

                            angular.forEach(persons, function (person) {
                                if (!isAlreadyInTheList(assignments, person)) {
                                    var ass = {
                                        status: $scope.selectedStatus.id,
                                        person: person.id,
                                        personName: person.fullName,
                                        editMode: false
                                    };

                                    if (type == 'approvers') {
                                        ass.assignmentType = 'APPROVER';
                                    }
                                    else if (type == 'observers') {
                                        ass.assignmentType = 'OBSERVER';
                                    }
                                    else if (type == 'acknowledgers') {
                                        ass.assignmentType = 'ACKNOWLEDGER';
                                    }

                                    assignments.push(ass);
                                }
                            });

                            if (type == 'approvers') {
                                promise = WorkflowService.addApprovers($scope.selectedStatus.id, assignments);
                            }
                            else if (type == 'observers') {
                                promise = WorkflowService.addObservers($scope.selectedStatus.id, assignments);
                            }
                            else if (type == 'acknowledgers') {
                                promise = WorkflowService.addAcknowledgers($scope.selectedStatus.id, assignments);
                            }

                            if (assignments.length > 0 && promise != null) {
                                promise.then(
                                    function (data) {
                                        if (type == 'approvers') {
                                            $scope.selectedStatus.approvers = data;
                                        }
                                        else if (type == 'observers') {
                                            $scope.selectedStatus.observers = data;
                                        }
                                        else if (type == 'acknowledgers') {
                                            $scope.selectedStatus.acknowledgers = data;
                                        }
                                        //CommonService.getPersonReferences(data, 'person');
                                    }
                                )
                            }
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

                    $scope.loadingHistory = true;
                    function loadWorkflowHistory() {
                        $scope.loadingHistory = true;
                        WorkflowService.getWorkflowHistory($scope.workflow.id).then(
                            function (data) {
                                $scope.workflowHistory = data;
                                if ($scope.workflow.started) {
                                    $scope.workflowHistory.push({
                                        statusObject: {
                                            name: "Workflow Started",
                                            createdBy: $scope.workflow.start.createdBy
                                        },
                                        timestamp: $scope.workflow.startedOn
                                    })
                                }

                                if ($scope.workflow.finished) {
                                    $scope.workflowHistory.unshift({
                                        statusObject: {
                                            name: "Workflow Finished",
                                            createdBy: $scope.workflow.finish.createdBy
                                        },
                                        timestamp: $scope.workflow.finishedOn
                                    })
                                }
                                angular.forEach($scope.workflowHistory, function (history) {
                                    var json = history.assignments;
                                    if (json != null && json.trim() !== "") {
                                        try {
                                            history.assignmentsList = JSON.parse(json);
                                        } catch (e) {
                                            history.assignmentsList = [];
                                        }
                                    }
                                    else {
                                        history.assignmentsList = [];
                                    }

                                    if (history.statusObject.createdBy != undefined) {
                                        CommonService.getPerson(history.statusObject.createdBy).then(
                                            function (data) {
                                                history.statusObject.createdByObject = data;
                                            }
                                        )
                                    }
                                })
                                $scope.loadingHistory = false;
                                loadPersonReferences();
                            }
                        );
                    }

                    function loadPersonReferences() {
                        var assignments = [];
                        angular.forEach($scope.workflowHistory, function (history) {
                            var list = history.assignmentsList;
                            angular.forEach(list, function (a) {
                                assignments.push(a);
                            });
                        });

                        CommonService.getPersonReferences(assignments, "person");
                    }

                    $scope.error = "";
                    $scope.voteApproval = voteApproval;

                    function voteApproval() {
                        $rootScope.showBusyIndicator($('#password-modal'));
                        PreferenceService.checkChangeApprovalPassword($scope.personDetails.person.id, $scope.personApprovalPassword.password).then(
                            function (data) {
                                $scope.selectedAssignment.vote = $scope.workflowStatus;
                                WorkflowService.updateApprover($scope.selectedStatus.id, $scope.selectedAssignment).then(
                                    function (data) {
                                        $scope.selectedAssignment.editMode = false;
                                        if ($scope.workflowStatus == "APPROVE") {
                                            $rootScope.showSuccessMessage(currentStatusApproved);
                                        } else if ($scope.workflowStatus == "REJECT") {
                                            $rootScope.showSuccessMessage(currentStatusRejected);
                                        }
                                        hidePasswordDialog();
                                        $scope.approveState = null;
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }, function (error) {
                                $scope.error = error.message;
                                $timeout(function () {
                                    $scope.error = "";
                                }, 1000);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }

                    var changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
                    var addWorkflowTitle = parsed.html($translate.instant("ADD_WORKFLOW")).html();
                    var change = parsed.html($translate.instant("CHANGE")).html();
                    var add = parsed.html($translate.instant("ADD")).html();
                    $scope.changeWorkflow = changeWorkflow;
                    function changeWorkflow() {
                        var options = {
                            title: changeWorkflowTitle,
                            template: 'app/desktop/directives/workflow/changeWorkflowDialog.jsp',
                            controller: 'ChangeWorkflowController as changeWorkflowVm',
                            resolve: 'app/desktop/directives/workflow/changeWorkflowController',
                            width: 600,
                            data: {
                                selectedObject: $scope.object,
                                selectedObjectType: objectType,
                                objectType: $scope.objectType
                            },
                            showMask: true,
                            buttons: [
                                {text: change, broadcast: 'app.workflow.change'}
                            ],
                            callback: function (data) {
                                $rootScope.showBusyIndicator($('.view-content'));
                                viewer.get('eventBus').fire('diagram.clear');
                                loadChangeWf();
                            }
                        };
                        $rootScope.showSidePanel(options);
                    }

                    $scope.addWorkflow = addWorkflow;
                    function addWorkflow() {
                        var options = {
                            title: addWorkflowTitle,
                            template: 'app/desktop/directives/workflow/addWorkflowDialog.jsp',
                            controller: 'AddWorkflowController as addWorkflowVm',
                            resolve: 'app/desktop/directives/workflow/addWorkflowController',
                            width: 650,
                            data: {
                                selectedObject: $scope.object,
                                selectedObjectType: objectType,
                                objectType: $scope.objectType
                            },
                            showMask: true,
                            buttons: [
                                {text: add, broadcast: 'app.workflow.add'}
                            ],
                            callback: function (data) {
                                $rootScope.showBusyIndicator($('.view-content'));
                                viewer = new Modeler({
                                    container: '#canvas',
                                    additionalModules: [{
                                        zoomScroll: ['value', '']
                                    }]
                                });
                                loadChangeWf();
                            }
                        };
                        $rootScope.showSidePanel(options);
                    }

                    function loadChangeWf() {
                        var promise = null;
                        if ($scope.objectType == "INSPECTIONPLANREVISION") {
                            promise = InspectionPlanService.getInspectionPlanRevision($scope.object.id)
                        } else if ($scope.objectType == "ITEMINSPECTION" || $scope.objectType == "MATERIALINSPECTION") {
                            promise = InspectionService.getInspection($scope.object.id)
                        } else if ($scope.objectType == "PROBLEMREPORT") {
                            promise = ProblemReportService.getProblemReport($scope.object.id)
                        } else if ($scope.objectType == "NCR") {
                            promise = NcrService.getNcr($scope.object.id)
                        } else if ($scope.objectType == "QCR") {
                            promise = QcrService.getQcr($scope.object.id)
                        } else if ($scope.objectType == "ECO") {
                            promise = ECOService.getECO($scope.object.id)
                        } else if ($scope.objectType == "DCO") {
                            promise = DCOService.getDCO($scope.object.id)
                        } else if ($scope.objectType == "ECR") {
                            promise = ECRService.getECR($scope.object.id)
                        } else if ($scope.objectType == "DCR") {
                            promise = DCRService.getDCR($scope.object.id)
                        } else if ($scope.objectType == "MCO") {
                            promise = MCOService.getMCO($scope.object.id)
                        } else if ($scope.objectType == "VARIANCE") {
                            promise = VarianceService.getVariance($scope.object.id)
                        } else if ($scope.objectType == "PROJECT") {
                            promise = ProjectService.getProject($scope.object.id)
                        } else if ($scope.objectType == "PROJECTACTIVITY") {
                            promise = ActivityService.getActivity($scope.object.id)
                        } else if ($scope.objectType == "MANUFACTURER") {
                            promise = MfrService.getManufacturer($scope.object.id)
                        } else if ($scope.objectType == "MANUFACTURERPART") {
                            promise = MfrPartsService.getManufacturepart($scope.object.id)
                        } else if ($scope.objectType == "REQUIREMENT") {
                            promise = RequirementService.getRequirementVersionObject($scope.object.id)
                        } else if ($scope.objectType == "SPECIFICATION") {
                            promise = SpecificationsService.findById($scope.object.id)
                        } else if ($scope.objectType == "ITEMREVISION") {
                            promise = ItemService.getRevisionId($scope.object.id)
                        } else if ($scope.objectType == "MROWORKORDER") {
                            promise = WorkOrderService.getWorkOrder($scope.object.id)
                        } else if ($scope.objectType == "NPR") {
                            promise = NprService.getNpr($scope.object.id)
                        } else if ($scope.objectType == "CUSTOMOBJECT") {
                            promise = CustomObjectService.getCustomObject($scope.object.id)
                        } else if ($scope.objectType == "SUPPLIERAUDIT") {
                            promise = SupplierAuditService.getSupplierAudit($scope.object.id)
                        } else if ($scope.objectType == "PROJECTTASK") {
                            promise = ActivityService.getActivityTask($scope.object.activity, $scope.object.id)
                        } else if ($scope.objectType == "PROGRAM") {
                            promise = ProgramService.getProgram($scope.object.id)
                        } else if ($scope.objectType == "REQUIREMENTDOCUMENT") {
                            promise = ReqDocumentService.getReqDocumentRevision($scope.object.id)
                        } else if ($scope.objectType == "BOPREVISION") {
                            promise = BOPService.getBOPRevision($scope.object.id)
                        } else if ($scope.objectType == "PROJECTTEMPLATE") {
                            promise = ProjectTemplateService.getProjectTemplate($scope.object.id)
                        } else if ($scope.objectType == "PROGRAMTEMPLATE") {
                            promise = ProgramTemplateService.getProgramTemplate($scope.object.id)
                        }
                        else if ($scope.objectType == "TEMPLATETASK") {
                            promise = TemplateActivityService.getTemplateTask($scope.object.id)
                        } else if ($scope.objectType == "TEMPLATEACTIVITY") {
                            promise = TemplateActivityService.getTemplateActivity($scope.object.id)
                        }
                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    $scope.object = data;
                                    $scope.object.workflow = data.workflow;
                                    if ($scope.object.workflow != null) {
                                        WorkflowService.getWorkflow($scope.object.workflow).then(
                                            function (data) {
                                                $scope.workflow = data;

                                                buildMaps();

                                                $scope.workflow.currentStatusObject = getStatusById($scope.workflow.currentStatus);

                                                viewer.importXML($scope.workflow.diagram, function (err) {
                                                    if (!err) {
                                                        resizeCanvas();
                                                        workflowLoaded = true;
                                                        if ($scope.objectType == "ECO" || $scope.objectType == "DCO") {
                                                            highlightRevisionTriggerStatus();
                                                        }
                                                        $rootScope.hideBusyIndicator();

                                                    } else {
                                                        console.log('Error loading workflow file:', err);
                                                        $rootScope.hideBusyIndicator();
                                                    }
                                                    if ($scope.objectType == "ECO" || $scope.objectType == "DCO") {
                                                        triggerActivitySelection();
                                                    }
                                                });
                                                initEvents(viewer);
                                                processWorkflowFile($scope.workflow.diagram);
                                                loadAssignments();
                                                loadObjects();
                                                loadEvents();
                                                loadWorkflowStatuses();
                                                if($scope.selectedStatus != null) {
                                                    $scope.selectedStatus.approvers = [];
                                                    $scope.selectedStatus.observers = [];
                                                    $scope.selectedStatus.acknowledgers = [];
                                                }
                                            }
                                        )
                                    }

                                })
                        }
                    }

                    function updateItemConfigRules() {
                        $rootScope.showBusyIndicator($(".view-container"));
                        ECOService.getECO($scope.object.id).then(
                            function (data) {
                                $scope.eco = data;
                                if ($scope.eco.revisionsCreated) {
                                    ECOService.getConfigurableAffectedItems($scope.object.id).then(
                                        function (data) {
                                            $scope.configurableAffectedItems = data;
                                            if ($scope.configurableAffectedItems.length > 0) {
                                                var count = 0;
                                                angular.forEach($scope.configurableAffectedItems, function (affectedItem) {
                                                    if (affectedItem.configurableItem != null && affectedItem.configurableItem != "") {
                                                        $scope.inclusionRulesJson = new Map();
                                                        $scope.itemExlusionRulesJson = new Map();
                                                        $scope.inclusionRulesJson = new Map(JSON.parse(affectedItem.configurableItem.inclusionRules));
                                                        $scope.itemExlusionRulesJson = new Map(JSON.parse(affectedItem.configurableItem.itemExclusions));
                                                        var key = getKeysFromInclusionMap($scope.inclusionRulesJson, affectedItem.configurableItem.id);
                                                        var key1 = getKeysFromItemExclusionMap($scope.itemExlusionRulesJson, affectedItem.configurableItem.id);
                                                        var i = 0;
                                                        angular.forEach($scope.keyArray, function (val) {
                                                            i++;
                                                            if ($scope.inclusionRulesJson.has(val)) {
                                                                var values = $scope.inclusionRulesJson.get(val);
                                                                angular.forEach(values, function (value) {
                                                                    if (value.itemId == affectedItem.configurableItem.id) {
                                                                        value.itemId = affectedItem.revisedItem.id;
                                                                    }
                                                                })
                                                            }
                                                        })
                                                        angular.forEach($scope.exclusionArray, function (val) {
                                                            i++;
                                                            if ($scope.itemExlusionRulesJson.has(val)) {
                                                                var values = $scope.itemExlusionRulesJson.get(val);
                                                                angular.forEach(values, function (value) {
                                                                    if (value.itemId == affectedItem.configurableItem.id) {
                                                                        value.itemId = affectedItem.revisedItem.id;
                                                                    }
                                                                })
                                                            }
                                                        })
                                                        affectedItem.revisedItem.inclusionRules = JSON.stringify(Array.from($scope.inclusionRulesJson.entries()));
                                                        affectedItem.revisedItem.itemExclusions = JSON.stringify(Array.from($scope.itemExlusionRulesJson.entries()));
                                                        affectedItem.revisedItem.attributeExclusionRules = affectedItem.configurableItem.attributeExclusionRules;
                                                        ItemService.updateItem(affectedItem.revisedItem).then(
                                                            function (itemRevision) {
                                                                count++;
                                                                if (count == $scope.configurableAffectedItems.length) {
                                                                    $rootScope.hideBusyIndicator();
                                                                }
                                                            }
                                                        )
                                                    }
                                                })

                                            } else {
                                                $rootScope.hideBusyIndicator();
                                            }
                                        }
                                    )
                                } else {
                                    $rootScope.hideBusyIndicator();
                                }
                            }
                        )
                    }

                    function getKeysFromInclusionMap(obj, value) {
                        $scope.keyArray = [];
                        angular.forEach(obj, function (values, key) {
                            angular.forEach(values, function (val) {
                                if (value == val.itemId) {
                                    $scope.keyArray.push(key);
                                }
                            })

                        });
                        return $scope.keyArray;
                    }

                    function getKeysFromItemExclusionMap(obj, value) {
                        $scope.exclusionArray = [];
                        angular.forEach(obj, function (values, key) {
                            angular.forEach(values, function (val) {
                                if (value == val.itemId) {
                                    $scope.exclusionArray.push(key);
                                }
                            })

                        });
                        return $scope.exclusionArray;
                    }

                    function loadObjects() {
                        if ($scope.objectType == "INSPECTIONPLANREVISION") {
                            $rootScope.loadPlanBasicInfo();
                        } else if ($scope.objectType == "ITEMINSPECTION" || $scope.objectType == "MATERIALINSPECTION") {
                            $rootScope.loadInspectionBasicInfo();
                        } else if ($scope.objectType == "PROBLEMREPORT") {
                            $rootScope.loadProblemReportBasicInfo();
                        } else if ($scope.objectType == "NCR") {
                            $rootScope.loadNcrBasicInfo();
                        } else if ($scope.objectType == "QCR") {
                            $rootScope.loadQcrBasicInfo();
                        } else if ($scope.objectType == "ECO") {
                            $rootScope.loadBasicEco();
                        } else if ($scope.objectType == "DCO") {
                            $rootScope.loadDco(false);
                        } else if ($scope.objectType == "ECR") {
                            $rootScope.loadBasicECR();
                        } else if ($scope.objectType == "DCR") {
                            $rootScope.loadDcr();
                        } else if ($scope.objectType == "MCO") {
                            $rootScope.loadBasicMCO();
                        } else if ($scope.objectType == "VARIANCE") {
                            $rootScope.loadVariance();
                        } else if ($scope.objectType == "PROJECT") {
                            $rootScope.loadProject();
                        } else if ($scope.objectType == "PROJECTACTIVITY") {
                            $rootScope.loadActivity();
                        } else if ($scope.objectType == "MANUFACTURER") {
                            $rootScope.loadMfr();
                        } else if ($scope.objectType == "MANUFACTURERPART") {
                            $rootScope.loadPartDetails();
                        }
                        else if ($scope.objectType == "SUPPLIERAUDIT") {
                            $rootScope.loadSupplierAudit();
                        }

                        else if ($scope.objectType == "REQUIREMENT") {
                            $rootScope.loadRequirement();
                        } else if ($scope.objectType == "SPECIFICATION") {
                            $rootScope.loadSpecification();
                        } else if ($scope.objectType == "ITEMREVISION") {
                            $rootScope.loadItem();
                        } else if ($scope.objectType == "MROWORKORDER") {
                            $rootScope.loadWorkOrderBasicDetails();
                        } else if ($scope.objectType == "NPR") {
                            $rootScope.loadNpr();
                        } else if ($scope.objectType == "CUSTOMOBJECT") {
                            $rootScope.loadObject();
                        } else if ($scope.objectType == "PROJECTTASK") {
                            $rootScope.loadTask();
                        } else if ($scope.objectType == "PROGRAM") {
                            $rootScope.loadProgram();
                        }
                        else if ($scope.objectType == "REQUIREMENTDOCUMENT") {
                            $rootScope.loadReqDocument();
                        }
                        else if ($scope.objectType == "BOPREVISION") {
                            $rootScope.loadBOP();
                        } else if ($scope.objectType == "PROJECTTEMPLATE") {
                            $rootScope.loadTemplate();
                        }
                        else if ($scope.objectType == "TEMPLATETASK") {
                            $rootScope.loadTemplateTask();
                        } else if ($scope.objectType == "TEMPLATEACTIVITY") {
                            $rootScope.loadTemplateActivity();
                        }
                        else if ($scope.objectType == "PROGRAMTEMPLATE") {
                            $rootScope.loadProgramTemplate();
                        }

                    }

                    $scope.cancelAssignment = cancelAssignment;
                    function cancelAssignment(assignment) {
                        assignment.editMode = false;
                        assignment.comments = null;
                        $scope.approveState = null;
                        assignment.vote = null
                    }

                    $scope.selectWorkflowActivityTab = selectWorkflowActivityTab;
                    function selectWorkflowActivityTab(id) {
                        if (id == "details.persons") {
                            $scope.activeTab = 0;
                        } else if (id == "details.formData") {
                            $scope.activeTab = 1;
                            $timeout(function () {
                                $scope.$broadcast('app.attributes.tabActivated', {});
                                var height = $('.view-content').outerHeight();
                                $('.workflow-tabs').height(height - 100);
                                $('.workflow-tabs .tab-content').height(height - 150);
                                $('.workflow-tabs .tab-content .tab-pane').height(height - 150);
                                $('#workflow-form-data-view').height($('.workflow-tabs .tab-content .tab-pane').outerHeight() - 20);
                            }, 1000);
                        }
                        $timeout(function () {
                            var height = $('.view-content').outerHeight();
                            $('.workflow-tabs').height(height - 100);
                            $('.workflow-tabs .tab-content').height(height - 150);
                            $('.workflow-tabs .tab-content .tab-pane').height(height - 150);
                        }, 300);
                    }

                    $scope.checkHasApprover = checkHasApprover;
                    function checkHasApprover() {
                        var hasApprover = false;
                        if ($scope.selectedStatus != null) {
                            angular.forEach($scope.selectedStatus.approvers, function (approver) {
                                if ($rootScope.loginPersonDetails.person.id == approver.person) {
                                    hasApprover = true;
                                }
                            })
                        }
                        return hasApprover;
                    }

                    $scope.checkForQualityUser = checkForQualityUser;
                    function checkForQualityUser() {
                        var valid = false;
                        if ($scope.objectType == "CUSTOMOBJECT" && $scope.qualityUserIds.indexOf($scope.personDetails.person.id) != -1) {
                            valid = true;
                        }

                        return valid;
                    }

                    function loadQualityUsers() {
                        $scope.qualityUsers = [];
                        $scope.qualityUserIds = [];
                        var preference = $application.defaultValuesPreferences.get("DEFAULT_QUALITY_ADMINISTRATOR_ROLE");
                        if (preference != null && preference.defaultValueName != null) {
                            var value = JSON.parse(preference.jsonValue);
                            GroupService.getGroupById(value.typeId).then(
                                function (data) {
                                    angular.forEach(data.groupMember, function (member) {
                                        if ($scope.qualityUserIds.indexOf(member.person.id) == -1) {
                                            $scope.qualityUserIds.push(member.person.id);
                                            $scope.qualityUsers.push(member.person);
                                        }
                                    })
                                    preference = $application.defaultValuesPreferences.get("DEFAULT_QUALITY_ANALYST_ROLE");
                                    if (preference != null && preference.defaultValueName != null) {
                                        var value = JSON.parse(preference.jsonValue);
                                        GroupService.getGroupById(value.typeId).then(
                                            function (data) {
                                                angular.forEach(data.groupMember, function (member) {
                                                    if ($scope.qualityUserIds.indexOf(member.person.id) == -1) {
                                                        $scope.qualityUserIds.push(member.person.id);
                                                        $scope.qualityUsers.push(member.person);
                                                    }
                                                })
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                            }
                                        )
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }

                    (function () {
                        $scope.$on('app.object.workflow', function (event, args) {
                            loadChangeApprovalPassword();
                            loadWorkflowTypes();
                            loadQualityUsers();
                            $scope.personDetails = $rootScope.loginPersonDetails;
                            if (workflowLoaded == false) {
                                $(window).resize(resizeCanvas);
                                loadWorkflow();
                            }
                            else {
                                $(window).resize(resizeCanvas);
                                loadWorkflow();
                            }
                        });
                        $scope.$on('app.change.workflow', function (event, data) {
                            changeWorkflow();
                        });
                        $scope.$on('app.add.workflow', function (event, data) {
                            addWorkflow();
                        });
                    })();
                }
            }
        }
    }
);
