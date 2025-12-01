define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('WorkflowService', WorkflowService);

        function WorkflowService($q, httpFactory) {
            return {
                createWorkflow: createWorkflow,
                createWorkflowStatus: createWorkflowStatus,
                createWorkflowTransition: createWorkflowTransition,

                updateWorkflow: updateWorkflow,
                updateWorkflowStatus: updateWorkflowStatus,
                updateWorkflowTransition: updateWorkflowTransition,

                getWorkflow: getWorkflow,
                getWorkflowStatus: getWorkflowStatus,
                getWorkflowTransition: getWorkflowTransition,

                getAllWorkflows: getAllWorkflows,
                getStatusesByWorkflow: getStatusesByWorkflow,
                getTransitionsByWorkflow: getTransitionsByWorkflow,

                getMultipleWorkflows: getMultipleWorkflows,
                getMultipleStatusesByWorkflow: getMultipleStatusesByWorkflow,
                getMultipleTransitionsByWorkflow: getMultipleTransitionsByWorkflow,

                getWorkflowReferences: getWorkflowReferences,
                getWorkflowStatusReferences: getWorkflowStatusReferences,
                getWorkflowTransitionReferences: getWorkflowTransitionReferences,

                getWorkflowsByType: getWorkflowsByType,
                getAllWorkFlowTypesNames: getAllWorkFlowTypesNames,

                getWorkflowUsedAttributes: getWorkflowUsedAttributes,
                getAssignedWorkflow: getAssignedWorkflow,
                getAssignedType: getAssignedType,
                getWorkflowStatusRepo: getWorkflowStatusRepo,
                getWorkflowHistory: getWorkflowHistory,
                getWorkflowsByDefName: getWorkflowsByDefName,
                getWorkflowAssigments: getWorkflowAssigments,
                getUsedWorkflows: getUsedWorkflows,
                getWorkflowByType: getWorkflowByType,
                getWorkflowTabCounts: getWorkflowTabCounts,
                getActiveWorkflowTasks: getActiveWorkflowTasks,
                getAllWorkflowInstances: getAllWorkflowInstances,
                getFilterWorkflowInstances: getFilterWorkflowInstances,

                deleteActivities: deleteActivities,
                deleteTransitions: deleteTransitions,
                getApprovers: getApprovers,
                addApprovers: addApprovers,
                getObservers: getObservers,
                addObservers: addObservers,
                getAcknowledgers: getAcknowledgers,
                addAcknowledgers: addAcknowledgers,
                updateApprover: updateApprover,
                updateAcknowledger: updateAcknowledger,
                updateObserver: updateObserver,
                deleteWorkflowAssignment: deleteWorkflowAssignment,

                startWorkflow: startWorkflow,
                promoteWorkflow: promoteWorkflow,
                demoteWorkflow: demoteWorkflow,
                finishWorkflow: finishWorkflow,
                putWorkflowOnHold: putWorkflowOnHold,
                removeWorkflowOnHold: removeWorkflowOnHold,
                getMasterWorkflows: getMasterWorkflows,
                getAssignedPersons: getAssignedPersons,
                getWorkflowTypeTree: getWorkflowTypeTree,
                getWorkflowEvents: getWorkflowEvents,
                createWorkflowEvent: createWorkflowEvent,
                updateWorkflowEvent: updateWorkflowEvent,
                deleteWorkflowEvent: deleteWorkflowEvent,
                getWorkflowStatuses: getWorkflowStatuses,
                attachCustomObjectWorkflow: attachCustomObjectWorkflow,
                getCustomObjectWorkflows: getCustomObjectWorkflows,
                getCustomObjectTabCount: getCustomObjectTabCount,
                getWorkflowAssignments: getWorkflowAssignments,
                getStatusAssigmentPersons: getStatusAssigmentPersons,
                createWorkflowStatusAttribute: createWorkflowStatusAttribute,
                updateWorkflowStatusAttribute: updateWorkflowStatusAttribute,
                getCustomObjectWorkflowStatus: getCustomObjectWorkflowStatus,
                getObjectWorkflowStatus: getObjectWorkflowStatus
            };

            function getAllWorkFlowTypesNames(name) {
                var url = "api/plm/workflows/ByName/" + name;
                return httpFactory.get(url);
            }

            function createWorkflow(workflow) {
                var url = "api/plm/workflows/instances";
                return httpFactory.post(url, workflow);
            }

            function createWorkflowStatus(workflowId, workflowStatus) {
                var url = "api/plm/workflows/instances/" + workflowId + "/statuses";
                return httpFactory.post(url, workflowStatus);
            }

            function createWorkflowTransition(workflowId, statusId, workflowTransition) {
                var url = "api/plm/workflows//instances/" + workflowId + "/statuses/" + statusId + "/transitions";
                return httpFactory.post(url, workflowTransition);
            }

            function updateWorkflow(workflow) {
                var url = "api/plm/workflows/instances/" + workflow.id;
                return httpFactory.put(url, workflow);
            }

            function updateWorkflowStatus(workflowId, workflowStatus) {
                var url = "api/plm/workflows/instances/" + workflowId + "/statuses/" + workflowStatus.id;
                return httpFactory.put(url, workflowStatus);
            }

            function updateWorkflowTransition(workflowId, statusId, workflowTransition) {
                var url = "api/plm/workflows//instances/" + workflowId + "/statuses/" + statusId + "/transitions/" + workflowTransition.id;
                return httpFactory.put(url, workflowTransition);
            }

            function getWorkflow(workflowId) {
                var url = "api/plm/workflows/instances/" + workflowId;
                return httpFactory.get(url);
            }

            function getWorkflowStatus(workflowId, statusId) {
                var url = "api/plm/workflows/instances/" + workflowId + "/statuses/" + statusId;
                return httpFactory.get(url);
            }

            function getWorkflowTransition(workflowId, statusId, workflowTransition) {
                var url = "api/plm/workflows/instances/" + workflowId + "/statuses/" + statusId + "/transitions/" + workflowTransition;
                return httpFactory.get(url);
            }

            function getAllWorkflows() {
                var url = "api/plm/workflows/instances";
                return httpFactory.get(url);
            }

            function getMultipleWorkflows(workflowIds) {
                var url = "api/plm/workflows/instances/multiple/[" + workflowIds + "]";
                return httpFactory.get(url);
            }

            function getMultipleStatusesByWorkflow(workflowId, statusIds) {
                var url = "api/plm/workflows/instances/" + workflowId + "/statuses/multiple/[" + statusIds + "]";
                return httpFactory.get(url);
            }

            function getStatusesByWorkflow(workflowId) {
                var url = "api/plm/workflows/instances/" + workflowId + "/statuses/multiple";
                return httpFactory.get(url);
            }

            function getMultipleTransitionsByWorkflow(workflowId, transitionIds) {
                var url = "api/plm/workflows/instances/" + workflowId + "/transitions/multiple/[" + transitionIds + "]";
                return httpFactory.get(url);
            }

            function getTransitionsByWorkflow(workflowId) {
                var url = "api/plm/workflows/instances/" + workflowId + "/transitions/multiple";
                return httpFactory.get(url);
            }

            function getWorkflowsByType(typeId, pageable) {
                var url = "api/plm/workflows/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getWorkflowReferences(objects, property) {
                var workflowIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && workflowIds.indexOf(object[property]) == -1) {
                        workflowIds.push(object[property]);
                    }
                });

                if (workflowIds.length > 0) {
                    getMultipleWorkflows(workflowIds).then(
                        function (workflows) {
                            var map = new Hashtable();
                            angular.forEach(workflows, function (workflow) {
                                map.put(workflow.id, workflow);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var workflow = map.get(object[property]);
                                    if (workflow != null) {
                                        object[property + "Object"] = workflow;
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function getWorkflowStatusReferences(objects, property) {
                var statusIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && statusIds.indexOf(object[property]) == -1) {
                        statusIds.push(object[property]);
                    }
                });

                if (statusIds.length > 0) {
                    getMultipleStatusesByWorkflow(statusIds).then(
                        function (statuses) {
                            var map = new Hashtable();
                            angular.forEach(statuses, function (status) {
                                map.put(status.id, status);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var status = map.get(object[property]);
                                    if (status != null) {
                                        object[property + "Object"] = status;
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function getWorkflowTransitionReferences(objects, property) {
                var transitionIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && transitionIds.indexOf(object[property]) == -1) {
                        transitionIds.push(object[property]);
                    }
                });

                if (transitionIds.length > 0) {
                    getMultipleStatusesByWorkflow(transitionIds).then(
                        function (transitions) {
                            var map = new Hashtable();
                            angular.forEach(transitions, function (transition) {
                                map.put(transition.id, transition);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var transition = map.get(object[property]);
                                    if (transition != null) {
                                        object[property + "Object"] = transition;
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function getWorkflowUsedAttributes(attribute) {
                var url = "api/plm/workflows/attributes/" + attribute;
                return httpFactory.get(url);
            }


            function getAssignedWorkflow(type) {
                var url = "api/plm/workflows/definitions/assigned/" + type;
                return httpFactory.get(url);
            }

            function getAssignedType(type) {
                var url = "api/plm/workflows/definitions/assignedType/" + type;
                return httpFactory.get(url);
            }

            function getWorkflowStatusRepo() {
                var url = "api/plm/workflows/status/report";
                return httpFactory.get(url);
            }

            function getWorkflowHistory(workflowId) {
                var url = "api/plm/workflows/" + workflowId + "/history";
                return httpFactory.get(url);
            }

            function getWorkflowsByDefName(defId) {
                var url = "api/plm/workflows/definitions/" + defId + "/workflows";
                return httpFactory.get(url);
            }

            function getMasterWorkflows(id) {
                var url = "api/plm/workflows/definitions/master/" + id;
                return httpFactory.get(url);
            }

            function getWorkflowAssigments(wfId) {
                var url = "api/plm/workflows/" + wfId + "/assigments";
                return httpFactory.get(url);
            }

            function getUsedWorkflows(wfId) {
                var url = "api/plm/workflows/workflowType/" + wfId;
                return httpFactory.get(url);
            }

            function getWorkflowByType(pageable, filter) {
                var url = "api/plm/workflows/status/type/report?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&type={0}"
                    .format(filter.type);
                return httpFactory.get(url);

            }

            function getActiveWorkflowTasks(pageable, filter) {
                var url = "api/plm/workflows/status/active/report?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&personId={0}".format(filter.personId);
                return httpFactory.get(url);
            }

            function getWorkflowTabCounts() {
                var url = "api/plm/workflows/tabs/count";
                return httpFactory.get(url);
            }

            function getAllWorkflowInstances(pageable, filters) {
                var url = "api/plm/workflows/definitions/all/instances?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&name={0}&searchQuery={1}".
                    format(filters.name, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getFilterWorkflowInstances(obj, pageable, filters) {
                var url = "api/plm/workflows/filter/" + obj + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&name={0}&searchQuery={1}".
                    format(filters.name, filters.searchQuery);
                return httpFactory.get(url);
            }


            function deleteTransitions(transitions) {
                var url = "api/plm/workflows/instances/0/transitions/delete";
                return httpFactory.post(url, transitions);
            }

            function deleteActivities(activities) {
                var url = "api/plm/workflows/instances/0/activities/delete";
                return httpFactory.post(url, activities);
            }

            function getWorkflowAssignments(statusId) {
                var url = "api/plm/workflows/" + statusId + "/assignments";
                return httpFactory.get(url);
            }

            function getApprovers(statusId) {
                var url = "api/plm/workflows/statuses/" + statusId + "/approvers";
                return httpFactory.get(url);
            }

            function addApprovers(statusId, approvers) {
                var url = "api/plm/workflows/statuses/" + statusId + "/approvers";
                return httpFactory.post(url, approvers);
            }

            function getObservers(statusId) {
                var url = "api/plm/workflows/statuses/" + statusId + "/observers";
                return httpFactory.get(url);
            }

            function addObservers(statusId, observers) {
                var url = "api/plm/workflows/statuses/" + statusId + "/observers";
                return httpFactory.post(url, observers);
            }

            function getAcknowledgers(statusId) {
                var url = "api/plm/workflows/statuses/" + statusId + "/acknowledgers";
                return httpFactory.get(url);
            }

            function addAcknowledgers(statusId, acknowledgers) {
                var url = "api/plm/workflows/statuses/" + statusId + "/acknowledgers";
                return httpFactory.post(url, acknowledgers);
            }

            function updateApprover(statusId, approver) {
                var url = "api/plm/workflows/statuses/" + statusId + "/approvers/" + approver.id;
                return httpFactory.put(url, approver);
            }

            function deleteWorkflowAssignment(statusId, assignment) {
                var url = "api/plm/workflows/statuses/" + statusId + "/assignments/" + assignment.id;
                return httpFactory.delete(url);
            }

            function updateAcknowledger(statusId, acknowledger) {
                var url = "api/plm/workflows/statuses/" + statusId + "/acknowledgers/" + acknowledger.id;
                return httpFactory.put(url, acknowledger);
            }

            function updateObserver(statusId, observer) {
                var url = "api/plm/workflows/statuses/" + statusId + "/observers/" + observer.id;
                return httpFactory.put(url, observer);
            }

            function startWorkflow(wfId) {
                var url = "api/plm/workflows/{0}/start".format(wfId);
                return httpFactory.get(url);
            }

            function finishWorkflow(wfId) {
                var url = "api/plm/workflows/{0}/finish".format(wfId);
                return httpFactory.get(url);
            }

            function promoteWorkflow(wfId, fromStatus, toStatus) {
                var url = "api/plm/workflows/{0}/promote?fromStatus={1}&toStatus={2}".format(wfId, fromStatus, toStatus);
                return httpFactory.get(url);
            }

            function demoteWorkflow(wfId) {
                var url = "api/plm/workflows/{0}/demote".format(wfId);
                return httpFactory.get(url);
            }

            function putWorkflowOnHold(wfId, currentStatus, notes) {
                var url = "api/plm/workflows/{0}/onhold?currentStatus={1}&notes={2}".format(wfId, currentStatus, notes);
                return httpFactory.get(url);
            }

            function removeWorkflowOnHold(wfId, currentStatus, notes) {
                var url = "api/plm/workflows/{0}/removeonhold?currentStatus={1}&notes={2}".format(wfId, currentStatus, notes);
                return httpFactory.get(url);
            }

            function getAssignedPersons(status, type) {
                var url = "api/plm/workflows/status/{0}/persons?type={1}".format(status, type);
                return httpFactory.get(url);
            }

            function getWorkflowTypeTree() {
                var url = "api/plm/workflows/type/tree";
                return httpFactory.get(url);
            }

            function getWorkflowEvents(id) {
                var url = "api/plm/workflows/" + id + "/events";
                return httpFactory.get(url);
            }

            function createWorkflowEvent(id, event) {
                var url = "api/plm/workflows/" + id + "/events";
                return httpFactory.post(url, event);
            }

            function updateWorkflowEvent(id, event) {
                var url = "api/plm/workflows/" + id + "/events/" + event.id;
                return httpFactory.put(url, event);
            }

            function deleteWorkflowEvent(id, eventId) {
                var url = "api/plm/workflows/" + id + "/events/" + eventId;
                return httpFactory.delete(url);
            }

            function getWorkflowStatuses(workflowId) {
                var url = "api/plm/workflows/" + workflowId + "/statuses";
                return httpFactory.get(url);
            }


            function getCustomObjectWorkflows(typeId, type) {
                var url = "api/plm/customobjects/type/" + typeId + "/assignedtype/" + type;
                return httpFactory.get(url);
            }

            function attachCustomObjectWorkflow(customObjectId, wfId) {
                var url = "api/plm/customobjects/" + customObjectId + "/attachworkflow/" + wfId;
                return httpFactory.get(url);
            }

            function getCustomObjectTabCount(objectId) {
                var url = "api/plm/customobjects/" + objectId + "/counts";
                return httpFactory.get(url);
            }

            function getStatusAssigmentPersons(pageable, filters) {
                var url = "api/plm/workflows/status/assigment/persons?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&typeId={1}".format(filters.searchQuery, filters.typeId);
                return httpFactory.get(url);
            }


            function createWorkflowStatusAttribute(statusId, attribute) {
                var url = "api/plm/workflows/status/" + statusId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateWorkflowStatusAttribute(statusId, attribute) {
                var url = "api/plm/workflows/status/" + statusId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function getCustomObjectWorkflowStatus(id) {
                var url = "api/plm/customobjects" + "/" + id;
                return httpFactory.get(url);
            }

            function getObjectWorkflowStatus(objectType) {
                var url = "api/plm/workflows/object/status/" + objectType;
                return httpFactory.get(url);
            }
        }
    }
);