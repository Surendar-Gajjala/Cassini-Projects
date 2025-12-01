define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('WorkOrderService', WorkOrderService);

        function WorkOrderService(httpFactory) {
            return {
                createWorkOrder: createWorkOrder,
                updateWorkOrder: updateWorkOrder,
                getWorkOrder: getWorkOrder,
                deleteWorkOrder: deleteWorkOrder,
                getWorkOrders: getWorkOrders,
                getAllWorkOrders: getAllWorkOrders,
                getMultipleWorkOrders: getMultipleWorkOrders,
                uploadImageAttribute: uploadImageAttribute,
                saveWorkOrderAttributes: saveWorkOrderAttributes,
                getObjectAttributesWithHierarchy: getObjectAttributesWithHierarchy,
                getWorkOrderSpareParts: getWorkOrderSpareParts,
                createWorkOrderSparePart: createWorkOrderSparePart,
                updateWorkOrderSparePart: updateWorkOrderSparePart,
                deleteWorkOrderSparePart: deleteWorkOrderSparePart,
                createMultipleWorkOrderSpareParts: createMultipleWorkOrderSpareParts,
                updateWorkOrderOperation: updateWorkOrderOperation,
                getWorkOrderOperations: getWorkOrderOperations,
                getWorkOrderTabCounts: getWorkOrderTabCounts,
                promoteWorkOrder: promoteWorkOrder,
                holdWorkOrder: holdWorkOrder,
                removeOnHold: removeOnHold,
                attachWorkOrderWorkflow: attachWorkOrderWorkflow,
                getWorkOrderWorkflows: getWorkOrderWorkflows,
                getWorkOrderResources: getWorkOrderResources,
                createMultipleWorkOrderResources: createMultipleWorkOrderResources,
                deleteWorkOrderResource: deleteWorkOrderResource,
                getWorkOrderInstructions: getWorkOrderInstructions,
                createWorkOrderInstructions: createWorkOrderInstructions
            };

            function createWorkOrder(workOrder) {
                var url = "api/mro/workorders";
                return httpFactory.post(url, workOrder)
            }


            function updateWorkOrder(workOrder) {
                var url = "api/mro/workorders/" + workOrder.id;
                return httpFactory.put(url, workOrder);
            }

            function getWorkOrder(id) {
                var url = "api/mro/workorders/" + id;
                return httpFactory.get(url)
            }

            function deleteWorkOrder(workOrder) {
                var url = "api/mro/workorders/" + workOrder;
                return httpFactory.delete(url);
            }


            function getWorkOrders() {
                var url = "api/mro/workorders";
                return httpFactory.get(url);
            }

            function getAllWorkOrders(pageable, filters) {
                var url = "api/mro/workorders/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(filters.searchQuery);
                return httpFactory.get(url);
            }

            function getMultipleWorkOrders(workOrderIds) {
                var url = "api/mro/workorders/multiple/[" + workOrderIds + "]";
                return httpFactory.get(url);
            }

            function uploadImageAttribute(objectId, attributeId, file) {
                var url = "api/mro/workorders/uploadimageattribute/" + objectId + "/" + attributeId;
                return httpFactory.upload(url, file);
            }

            function saveWorkOrderAttributes(attributes) {
                var url = "api/mro/workorders/create/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function getObjectAttributesWithHierarchy(typeId) {
                var url = "api/mro/objecttypes/type/" + typeId + "/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function getWorkOrderSpareParts(workOrderId) {
                var url = "api/mro/workorders/" + workOrderId + "/parts";
                return httpFactory.get(url);
            }

            function createWorkOrderSparePart(workOrderId, spareParts) {
                var url = "api/mro/workorders/" + workOrderId + "/parts";
                return httpFactory.post(url, spareParts);
            }

            function updateWorkOrderSparePart(workOrderId, sparePart) {
                var url = "api/mro/workorders/" + workOrderId + "/parts/" + sparePart.id;
                return httpFactory.put(url, sparePart);
            }

            function createMultipleWorkOrderSpareParts(workOrderId, spareParts) {
                var url = "api/mro/workorders/" + workOrderId + "/parts/multiple";
                return httpFactory.post(url, spareParts);
            }

            function deleteWorkOrderSparePart(workOrderId, sparePartId) {
                var url = "api/mro/workorders/" + workOrderId + "/parts/" + sparePartId;
                return httpFactory.delete(url);
            }

            function updateWorkOrderOperation(workOrderId, operation) {
                var url = "api/mro/workorders/" + workOrderId + "/operations/" + operation.id;
                return httpFactory.put(url, operation);
            }

            function getWorkOrderOperations(workOrderId) {
                var url = "api/mro/workorders/" + workOrderId + "/operations";
                return httpFactory.get(url);
            }

            function getWorkOrderTabCounts(workOrderId) {
                var url = "api/mro/workorders/" + workOrderId + "/count";
                return httpFactory.get(url);
            }

            function promoteWorkOrder(workOrderId) {
                var url = "api/mro/workorders/" + workOrderId + "/promoteworkorder";
                return httpFactory.get(url);
            }

            function holdWorkOrder(workOrderId) {
                var url = "api/mro/workorders/" + workOrderId + "/holdworkorder";
                return httpFactory.get(url);
            }

            function removeOnHold(workOrderId) {
                var url = "api/mro/workorders/" + workOrderId + "/removeonhold";
                return httpFactory.get(url);
            }

            function attachWorkOrderWorkflow(workOrderId, wfId) {
                var url = "api/mro/workorders/" + workOrderId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

            function getWorkOrderWorkflows(typeId, type) {
                var url = "api/mro/workorders/workflow/" + typeId + "/mroType/" + type;
                return httpFactory.get(url);
            }

            function getWorkOrderResources(workOrderId) {
                var url = "api/mro/workorders/" + workOrderId + "/resources";
                return httpFactory.get(url);
            }

            function createMultipleWorkOrderResources(workOrderId, spareParts) {
                var url = "api/mro/workorders/" + workOrderId + "/resources/multiple";
                return httpFactory.post(url, spareParts);
            }

            function deleteWorkOrderResource(workOrderId, sparePartId) {
                var url = "api/mro/workorders/" + workOrderId + "/resources/" + sparePartId;
                return httpFactory.delete(url);
            }

            function getWorkOrderInstructions(workOrderId) {
                var url = "api/mro/workorders/" + workOrderId + "/instructions";
                return httpFactory.get(url);
            }

            function createWorkOrderInstructions(workOrderId, instructions) {
                var url = "api/mro/workorders/" + workOrderId + "/instructions";
                return httpFactory.post(url, instructions);
            }
        }
    }
);