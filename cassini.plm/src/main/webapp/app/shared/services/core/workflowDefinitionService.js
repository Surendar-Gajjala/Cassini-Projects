define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('WorkflowDefinitionService', WorkflowDefinitionService);

        function WorkflowDefinitionService($q, httpFactory) {
            return {
                createWorkflowDefinition: createWorkflowDefinition,
                createWorkflowDefinitionStatus: createWorkflowDefinitionStatus,
                createWorkflowDefinitionTransition: createWorkflowDefinitionTransition,

                updateWorkflowDefinition: updateWorkflowDefinition,
                updateWorkflowDefinitionStatus: updateWorkflowDefinitionStatus,
                updateWorkflowDefinitionTransition: updateWorkflowDefinitionTransition,

                getWorkflowDefinition: getWorkflowDefinition,
                getWorkflowDefinitionByName: getWorkflowDefinitionByName,
                getWorkflowDefinitionStatus: getWorkflowDefinitionStatus,
                getWorkflowDefinitionTransition: getWorkflowDefinitionTransition,

                createWorkflowAttribute: createWorkflowAttribute,
                updateWorkflowAttribute: updateWorkflowAttribute,
                getWorkflowAttributesWithHierarchy: getWorkflowAttributesWithHierarchy,
                getAllWorkflowDefinitions: getAllWorkflowDefinitions,
                getWorkflowDefs: getWorkflowDefs,
                saveWorkflowTypeAttributes: saveWorkflowTypeAttributes,
                getWorkflowAttributes: getWorkflowAttributes,
                freeTextSearch: freeTextSearch,
                updateWorkflowImageValue: updateWorkflowImageValue,
                deleteWorkflow: deleteWorkflow,
                getNormalWorkflowStatuses: getNormalWorkflowStatuses,
                deleteWorkflowDefinition: deleteWorkflowDefinition,
                promoteDefinition: promoteDefinition,
                demoteDefinition: demoteDefinition,
                getAllWorkflowRevisionDefinitions: getAllWorkflowRevisionDefinitions,
                reviseWorkflow: reviseWorkflow,
                getWorkflowRevisionHistory: getWorkflowRevisionHistory,

                deleteWorkflowDefinitionStatus: deleteWorkflowDefinitionStatus,
                deleteWorkflowDefinitionTransition: deleteWorkflowDefinitionTransition,
                getWorkflowDefsByTypeId: getWorkflowDefsByTypeId,
                getAllWorkflowDefs: getAllWorkflowDefs,
                getWorkflowAssignableTypeObjectType: getWorkflowAssignableTypeObjectType,
                getWorkflowDefinitionEvents: getWorkflowDefinitionEvents,
                createWorkflowDefinitionEvent: createWorkflowDefinitionEvent,
                updateWorkflowDefinitionEvent: updateWorkflowDefinitionEvent,
                deleteWorkflowDefinitionEvent: deleteWorkflowDefinitionEvent,
                getWorkflowDefinitionStatuses: getWorkflowDefinitionStatuses,
                copyWorkflowDefinitionEvents: copyWorkflowDefinitionEvents,
                getWorkflowStatusAttribute: getWorkflowStatusAttribute,
                deleteWorkflowStatusAttribute: deleteWorkflowStatusAttribute,
                getWorkflowStatusAttributes: getWorkflowStatusAttributes,
                createWorkflowStatusAttribute: createWorkflowStatusAttribute,
                updateWorkflowStatusAttribute: updateWorkflowStatusAttribute,
                getWorkflowStatusAttributeByName: getWorkflowStatusAttributeByName
            };

            function getWorkflowAttributesWithHierarchy(type) {
                var url = "api/plm/workflows/workflowTypes/" + type + "/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function createWorkflowDefinition(workflowDefinition) {
                var url = "api/plm/workflows/definitions";
                return httpFactory.post(url, workflowDefinition);
            }

            function createWorkflowDefinitionStatus(definitionId, definitionStatus) {
                var url = "api/plm/workflows/definitions/" + definitionId + "/statuses";
                return httpFactory.post(url, definitionStatus);
            }

            function createWorkflowDefinitionTransition(definitionId, statusId, definitionTransition) {
                var url = "api/plm/workflows//instances/" + definitionId + "/statuses/" + statusId + "/transitions";
                return httpFactory.post(url, definitionTransition);
            }

            function updateWorkflowDefinition(workflowDefinition) {
                var url = "api/plm/workflows/definitions/" + workflowDefinition.id;
                return httpFactory.put(url, workflowDefinition);
            }

            function deleteWorkflowDefinition(workflowDefinition) {
                var url = "api/plm/workflows/definitions/" + workflowDefinition.id;
                return httpFactory.delete(url, workflowDefinition);
            }

            function updateWorkflowDefinitionStatus(definitionId, definitionStatus) {
                var url = "api/plm/workflows/definitions/" + definitionId + "/statuses/" + definitionStatus.id;
                return httpFactory.put(url, definitionStatus);
            }

            function updateWorkflowDefinitionTransition(definitionId, statusId, definitionTransition) {
                var url = "api/plm/workflows/definitions/" + definitionId + "/statuses/" + statusId + "/transitions/" + definitionTransition.id;
                return httpFactory.put(url, definitionTransition);
            }

            function getWorkflowDefinition(definitionId) {
                var url = "api/plm/workflows/definitions/" + definitionId;
                return httpFactory.get(url);
            }

            function getWorkflowDefinitionByName(workflowName) {
                var url = "api/plm/workflows/definitions/workflowName/" + workflowName;
                return httpFactory.get(url);
            }

            function getWorkflowDefinitionStatus(definitionId, statusId) {
                var url = "api/plm/workflows/definitions/" + definitionId + "/statuses/" + statusId;
                return httpFactory.get(url);
            }

            function getWorkflowDefinitionTransition(definitionId, statusId, workflowTransition) {
                var url = "api/plm/workflows/definitions/" + definitionId + "/statuses/" + statusId + "/transitions/" + workflowTransition;
                return httpFactory.get(url);
            }

            function getAllWorkflowDefinitions(pageable) {
                var url = "api/plm/workflows/definitions?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getWorkflowDefs() {
                var url = "api/plm/workflows/definitions/all";
                return httpFactory.get(url);
            }

            function createWorkflowAttribute(workflowId, attribute) {
                var url = "api/plm/workflows/definitions/" + workflowId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateWorkflowAttribute(workflowId, attribute) {
                var url = "api/plm/workflows/definitions/" + workflowId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function saveWorkflowTypeAttributes(workflowId, attributes) {
                var url = "api/plm/workflows/definitions/" + workflowId + "/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function getWorkflowAttributes(workflowId) {
                var url = "api/plm/workflows/definitions/" + workflowId + "/attributes";
                return httpFactory.get(url);
            }

            function freeTextSearch(pageable, freetext) {
                var url = "api/plm/workflows/definitions/freetextsearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);

                url += "&searchQuery={0}".
                    format(freetext);
                return httpFactory.get(url);
            }

            function updateWorkflowImageValue(objectId, attributeId, file) {
                var url = "api/plm/workflows/updateImageValue/" + objectId + "/" + attributeId;
                return httpFactory.upload(url, file);
            }

            function deleteWorkflow(workflowId) {
                var url = "api/plm/workflows/definitions/workflow/" + workflowId;
                return httpFactory.delete(url);
            }

            function getNormalWorkflowStatuses(workflowId) {
                var url = "api/plm/workflows/definitions/" + workflowId + "/statuses";
                return httpFactory.get(url);
            }

            function promoteDefinition(defId) {
                var url = "api/plm/workflows/definitions/" + defId + "/promote";
                return httpFactory.put(url);
            }

            function demoteDefinition(defId) {
                var url = "api/plm/workflows/definitions/" + defId + "/demote";
                return httpFactory.put(url);
            }

            function getAllWorkflowRevisionDefinitions(pageable, filters) {
                var url = "api/plm/workflows/definitions/allRevisions?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(filters.searchQuery);
                return httpFactory.get(url);
            }

            function reviseWorkflow(defId, revWorkflow) {
                var url = "api/plm/workflows/definitions/" + defId + "/revise";
                return httpFactory.post(url, revWorkflow);
            }

            function getWorkflowRevisionHistory(workflow) {
                var url = "api/plm/workflows/revision/history/" + workflow;
                return httpFactory.get(url);
            }

            function deleteWorkflowDefinitionStatus(workflowId, statusId) {
                var url = "api/plm/workflows/definitions/" + workflowId + "/status/" + statusId + "/delete";
                return httpFactory.delete(url, status);
            }

            function deleteWorkflowDefinitionTransition(workflowId, transitionId) {
                var url = "api/plm/workflows/definitions/" + workflowId + "/transition/" + transitionId + "/delete";
                return httpFactory.delete(url);
            }

            function getWorkflowDefsByTypeId(pageable, filters) {
                var url = "api/plm/workflows/definitions/type?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&type={1}".format(filters.searchQuery, filters.type);
                return httpFactory.get(url);
            }

            function getAllWorkflowDefs(pageable, filters) {
                var url = "api/plm/workflows/master/defs?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(filters.searchQuery);
                return httpFactory.get(url);
            }

            function getWorkflowAssignableTypeObjectType(id) {
                var url = "api/plm/workflows/definitions/" + id + "/assignabletype";
                return httpFactory.get(url);
            }

            function getWorkflowDefinitionEvents(id) {
                var url = "api/plm/workflows/definitions/" + id + "/events";
                return httpFactory.get(url);
            }

            function createWorkflowDefinitionEvent(id, event) {
                var url = "api/plm/workflows/definitions/" + id + "/events";
                return httpFactory.post(url, event);
            }

            function updateWorkflowDefinitionEvent(id, event) {
                var url = "api/plm/workflows/definitions/" + id + "/events/" + event.id;
                return httpFactory.put(url, event);
            }

            function deleteWorkflowDefinitionEvent(id, eventId) {
                var url = "api/plm/workflows/definitions/" + id + "/events/" + eventId;
                return httpFactory.delete(url);
            }

            function copyWorkflowDefinitionEvents(id, workflow) {
                var url = "api/plm/workflows/definitions/" + id + "/events/copy";
                return httpFactory.post(url, workflow);
            }

            function getWorkflowDefinitionStatuses(workflowId) {
                var url = "api/plm/workflows/definitions/" + workflowId + "/statuses/all";
                return httpFactory.get(url);
            }

            function getWorkflowStatusAttribute(status, attribute) {
                var url = "api/plm/workflows/definitions/status/" + status + "/attributes/" + attribute;
                return httpFactory.get(url);
            }

            function deleteWorkflowStatusAttribute(status, attribute) {
                var url = "api/plm/workflows/definitions/status/" + status + "/attributes/" + attribute;
                return httpFactory.delete(url);
            }

            function getWorkflowStatusAttributes(status) {
                var url = "api/plm/workflows/definitions/status/" + status + "/attributes";
                return httpFactory.get(url);
            }

            function createWorkflowStatusAttribute(status, attribute) {
                var url = "api/plm/workflows/definitions/status/" + status + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateWorkflowStatusAttribute(status, attribute) {
                var url = "api/plm/workflows/definitions/status/" + status + "/attributes/" + attribute.id;
                return httpFactory.put(url, attribute);
            }

            function getWorkflowStatusAttributeByName(status, name) {
                var url = "api/plm/workflows/definitions/status/" + status + "/name/" + name;
                return httpFactory.get(url);
            }

        }
    }
);