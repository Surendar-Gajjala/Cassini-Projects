define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('BOPService', BOPService);

        function BOPService(httpFactory) {
            return {
                createBOP: createBOP,
                updateBOP: updateBOP,
                getBOP: getBOP,
                getBOPCounts: getBOPCounts,
                deleteBOP: deleteBOP,
                getBOPs: getBOPs,
                getAllBOPs: getAllBOPs,
                getMultipleBOPs: getMultipleBOPs,
                uploadImageAttribute: uploadImageAttribute,
                saveBOPAttributes: saveBOPAttributes,
                getMESTypeWorkflows: getMESTypeWorkflows,
                getBOPRevision: getBOPRevision,
                attachBOPWorkflow: attachBOPWorkflow,
                getBopPlans: getBopPlans,
                getBopPlan: getBopPlan,
                createMultipleBopPlans: createMultipleBopPlans,
                createBopPlan: createBopPlan,
                updateBopPlan: updateBopPlan,
                deleteBopPlan: deleteBopPlan,
                getBopPlanChildren: getBopPlanChildren,
                searchBopPlanOperationResources: searchBopPlanOperationResources,
                createMultipleBopPlanResources: createMultipleBopPlanResources,
                createBopPlanResource: createBopPlanResource,
                updateBopPlanResource: updateBopPlanResource,
                deleteBopPlanResource: deleteBopPlanResource,
                getBopPlanResource: getBopPlanResource,
                getBopPlanResources: getBopPlanResources,
                createBopPlanInstructions: createBopPlanInstructions,
                updateBopPlanInstructions: updateBopPlanInstructions,
                deleteBopPlanInstructions: deleteBopPlanInstructions,
                getBopPlanInstructions: getBopPlanInstructions,
                getBOPRevisionHistory: getBOPRevisionHistory,
                createMultipleBopPlanItems: createMultipleBopPlanItems,
                createBopPlanItem: createBopPlanItem,
                updateBopPlanItem: updateBopPlanItem,
                deleteBopPlanItem: deleteBopPlanItem,
                getBopPlanItem: getBopPlanItem,
                getBopPlanItems: getBopPlanItems,
                getBopPlanResourceValidate: getBopPlanResourceValidate,
                getBOPMBOMItems: getBOPMBOMItems,
                getBOPMBOMItemsByType: getBOPMBOMItemsByType,
                getBOPPlanCounts: getBOPPlanCounts,
                getBopPlanItemsByType: getBopPlanItemsByType,
                reviseBOP: reviseBOP
            };

            function createBOP(bop) {
                var url = "api/mes/bops";
                return httpFactory.post(url, bop)
            }

            function updateBOP(bop) {
                var url = "api/mes/bops/" + bop.id;
                return httpFactory.put(url, bop);
            }

            function getBOP(id) {
                var url = "api/mes/bops/" + id;
                return httpFactory.get(url)
            }

            function getBOPCounts(id) {
                var url = "api/mes/bops/" + id + "/counts";
                return httpFactory.get(url)
            }

            function getBOPPlanCounts(routeId) {
                var url = "api/mes/bops/operations/" + routeId + "/counts";
                return httpFactory.get(url)
            }

            function getBOPRevision(id) {
                var url = "api/mes/bops/revisions/" + id;
                return httpFactory.get(url)
            }

            function reviseBOP(id, revision) {
                var url = "api/mes/bops/revisions/" + id + "/revise";
                return httpFactory.put(url, revision)
            }

            function getBOPRevisionHistory(id) {
                var url = "api/mes/bops/" + id + "/revisions/history";
                return httpFactory.get(url);
            }

            function deleteBOP(bop) {
                var url = "api/mes/bops/" + bop;
                return httpFactory.delete(url);
            }

            function getBOPs() {
                var url = "api/mes/bops";
                return httpFactory.get(url);
            }

            function getAllBOPs(pageable, filters) {
                var url = "api/mes/bops/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&number={1}&name={2}&type={3}".
                    format(filters.searchQuery, filters.number, filters.name, filters.type);
                return httpFactory.get(url);
            }

            function getMultipleBOPs(bopIds) {
                var url = "api/mes/bops/multiple/[" + bopIds + "]";
                return httpFactory.get(url);
            }


            function uploadImageAttribute(objectId, attributeId, file) {
                var url = "api/mes/bops/uploadimageattribute/" + objectId + "/" + attributeId;
                return httpFactory.upload(url, file);
            }

            function saveBOPAttributes(attributes) {
                var url = "api/mes/bops/create/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function getAllBOPsByPage(pageable) {
                var url = "api/mes/bops/pageable?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getMESTypeWorkflows(typeId, type) {
                var url = "api/mes/bops/" + typeId + "/" + type + "/workflows";
                return httpFactory.get(url);
            }

            function attachBOPWorkflow(id, wfId) {
                var url = "api/mes/bops/" + id + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

            function getBopPlanResourceValidate(id) {
                var url = "api/mes/bops/" + id + "/routes/validate";
                return httpFactory.get(url);
            }

            function getBopPlans(id) {
                var url = "api/mes/bops/" + id + "/routes";
                return httpFactory.get(url);
            }

            function getBopPlan(id, routeId) {
                var url = "api/mes/bops/" + id + "/routes/" + routeId;
                return httpFactory.get(url);
            }

            function getBOPMBOMItems(routeId, operationId, mbomId, hierarchy) {
                var url = "api/mes/bops/routes/" + routeId + "/" + operationId + "/mboms/" + mbomId + "/parts?hierarchy=" + hierarchy;
                return httpFactory.get(url);
            }

            function getBOPMBOMItemsByType(routeId, operationId, mbomId, type, hierarchy) {
                var url = "api/mes/bops/routes/" + routeId + "/" + operationId + "/mboms/" + mbomId + "/parts/type/" + type + "?hierarchy=" + hierarchy;
                return httpFactory.get(url);
            }

            function getBopPlanChildren(id, routeId) {
                var url = "api/mes/bops/" + id + "/routes/" + routeId + "/children";
                return httpFactory.get(url);
            }

            function createMultipleBopPlans(id, operations) {
                var url = "api/mes/bops/" + id + "/routes/multiple";
                return httpFactory.post(url, operations);
            }

            function createBopPlan(id, plan) {
                var url = "api/mes/bops/" + id + "/routes";
                return httpFactory.post(url, plan);
            }

            function updateBopPlan(id, plan) {
                var url = "api/mes/bops/" + id + "/routes/" + plan.id;
                return httpFactory.put(url, plan);
            }

            function deleteBopPlan(id, routeId) {
                var url = "api/mes/bops/" + id + "/routes/" + routeId;
                return httpFactory.delete(url);
            }

            function searchBopPlanOperationResources(routeId, pageable, filters) {
                var url = "api/mes/bops/operations/" + routeId + "/resources/search?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&number={1}&name={2}&type={3}&resource={4}".
                    format(filters.searchQuery, filters.number, filters.name, filters.type, filters.resource);
                return httpFactory.get(url);
            }

            function createMultipleBopPlanResources(routeId, resources) {
                var url = "api/mes/bops//operations/" + routeId + "/resources/multiple";
                return httpFactory.post(url, resources);
            }

            function createBopPlanResource(routeId, resource) {
                var url = "api/mes/bops/operations/" + routeId + "/resources";
                return httpFactory.post(url, resource);
            }

            function updateBopPlanResource(routeId, resource) {
                var url = "api/mes/bops/operations/" + routeId + "/resources/" + resource.id;
                return httpFactory.put(url, resource);
            }

            function deleteBopPlanResource(routeId, resourceId) {
                var url = "api/mes/bops/operations/" + routeId + "/resources/" + resourceId;
                return httpFactory.delete(url);
            }

            function getBopPlanResource(routeId, resourceId) {
                var url = "api/mes/bops/operations/" + routeId + "/resources/" + resourceId;
                return httpFactory.get(url);
            }

            function getBopPlanResources(routeId) {
                var url = "api/mes/bops/operations/" + routeId + "/resources";
                return httpFactory.get(url);
            }

            function createMultipleBopPlanItems(routeId, parts) {
                var url = "api/mes/bops/operations/" + routeId + "/parts/multiple";
                return httpFactory.post(url, parts);
            }

            function createBopPlanItem(routeId, item) {
                var url = "api/mes/bops/operations/" + routeId + "/parts";
                return httpFactory.post(url, item);
            }

            function updateBopPlanItem(routeId, item) {
                var url = "api/mes/bops/operations/" + routeId + "/parts/" + item.id;
                return httpFactory.put(url, item);
            }

            function deleteBopPlanItem(routeId, itemId) {
                var url = "api/mes/bops/operations/" + routeId + "/parts/" + itemId;
                return httpFactory.delete(url);
            }

            function getBopPlanItem(routeId, itemId) {
                var url = "api/mes/bops/operations/" + routeId + "/parts/" + itemId;
                return httpFactory.get(url);
            }

            function getBopPlanItems(routeId) {
                var url = "api/mes/bops/operations/" + routeId + "/parts";
                return httpFactory.get(url);
            }

            function getBopPlanItemsByType(routeId, type) {
                var url = "api/mes/bops/operations/" + routeId + "/parts/type/" + type;
                return httpFactory.get(url);
            }

            function createBopPlanInstructions(routeId, instructions) {
                var url = "api/mes/bops/operations/" + routeId + "/instructions";
                return httpFactory.post(url, instructions);
            }

            function updateBopPlanInstructions(routeId, instructions) {
                var url = "api/mes/bops/operations/" + routeId + "/instructions/" + instructions.id;
                return httpFactory.put(url, instructions);
            }

            function deleteBopPlanInstructions(routeId, instructionId) {
                var url = "api/mes/bops/operations/" + routeId + "/instructions/" + instructionId;
                return httpFactory.delete(url);
            }

            function getBopPlanInstructions(routeId) {
                var url = "api/mes/bops/operations/" + routeId + "/instructions";
                return httpFactory.get(url);
            }

        }
    }
);