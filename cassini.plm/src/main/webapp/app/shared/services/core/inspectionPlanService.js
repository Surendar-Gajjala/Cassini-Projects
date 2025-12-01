define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('InspectionPlanService', InspectionPlanService);

        function InspectionPlanService(httpFactory) {
            return {
                createInspectionPlan: createInspectionPlan,
                updateInspectionPlan: updateInspectionPlan,
                deleteInspectionPlan: deleteInspectionPlan,
                getInspectionPlan: getInspectionPlan,
                getAllProductInspectionPlans: getAllProductInspectionPlans,
                getAllMaterialInspectionPlans: getAllMaterialInspectionPlans,
                getMultipleInspectionPlans: getMultipleInspectionPlans,
                getInspectionPlanRevision: getInspectionPlanRevision,
                getInspectionPlanFiles: getInspectionPlanFiles,
                createPlanAttribute: createPlanAttribute,
                createPlanRevisionAttribute: createPlanRevisionAttribute,
                updatePlanAttribute: updatePlanAttribute,
                updatePlanRevisionAttribute: updatePlanRevisionAttribute,
                getInspectionPlanChecklists: getInspectionPlanChecklists,
                createInspectionPlanChecklist: createInspectionPlanChecklist,
                updateInspectionPlanChecklist: updateInspectionPlanChecklist,
                getChecklistChildren: getChecklistChildren,
                deleteInspectionPlanChecklist: deleteInspectionPlanChecklist,
                getChecklistParams: getChecklistParams,
                createPlanChecklistParams: createPlanChecklistParams,
                updatePlanChecklistParams: updatePlanChecklistParams,
                getPlanDetailsCount: getPlanDetailsCount,
                getInspectionPlanList: getInspectionPlanList,
                getReleasedInspectionPlanList: getReleasedInspectionPlanList,
                getReleasedInspectionPlanByProduct: getReleasedInspectionPlanByProduct,
                deletePlanChecklistParams: deletePlanChecklistParams,
                getInspectionPlanTasks: getInspectionPlanTasks,
                updateProductInspectionPlan: updateProductInspectionPlan,
                deleteProductInspectionPlan: deleteProductInspectionPlan,
                getProductInspectionPlan: getProductInspectionPlan,
                updateMaterialInspectionPlan: updateMaterialInspectionPlan,
                deleteMaterialInspectionPlan: deleteMaterialInspectionPlan,
                getMaterialInspectionPlan: getMaterialInspectionPlan,
                reviseInspectionPlan: reviseInspectionPlan,
                getReleasedInspectionPlanByPart: getReleasedInspectionPlanByPart,
                getPlanRevisionHistory: getPlanRevisionHistory,
                getInspectionPlanStatus: getInspectionPlanStatus,
                getInspectionTypeLifecycles: getInspectionTypeLifecycles
            };

            function createInspectionPlan(plan) {
                var url = "api/pqm/inspectionplans";
                return httpFactory.post(url, plan);
            }

            function updateInspectionPlan(id, plan) {
                var url = "api/pqm/inspectionplans/" + id;
                return httpFactory.put(url, plan);
            }

            function deleteInspectionPlan(planId) {
                var url = "api/pqm/inspectionplans/" + planId;
                return httpFactory.delete(url);
            }

            function getInspectionPlan(planId) {
                var url = "api/pqm/inspectionplans/" + planId;
                return httpFactory.get(url);
            }

            function updateProductInspectionPlan(id, plan) {
                var url = "api/pqm/inspectionplans/products/" + id;
                return httpFactory.put(url, plan);
            }

            function deleteProductInspectionPlan(planId) {
                var url = "api/pqm/inspectionplans/products/" + planId;
                return httpFactory.delete(url);
            }

            function getProductInspectionPlan(planId) {
                var url = "api/pqm/inspectionplans/products/" + planId;
                return httpFactory.get(url);
            }

            function updateMaterialInspectionPlan(id, plan) {
                var url = "api/pqm/inspectionplans/materials/" + id;
                return httpFactory.put(url, plan);
            }

            function deleteMaterialInspectionPlan(planId) {
                var url = "api/pqm/inspectionplans/materials/" + planId;
                return httpFactory.delete(url);
            }

            function getMaterialInspectionPlan(planId) {
                var url = "api/pqm/inspectionplans/materials/" + planId;
                return httpFactory.get(url);
            }

            function reviseInspectionPlan(planId) {
                var url = "api/pqm/inspectionplans/revise/" + planId;
                return httpFactory.get(url);
            }

            function getInspectionPlanStatus() {
                var url = "api/pqm/inspectionplans/status";
                return httpFactory.get(url);
            }

            function getInspectionTypeLifecycles() {
                var url = "api/pqm/inspectionplans/types/lifecycles";
                return httpFactory.get(url);
            }

            function getAllProductInspectionPlans(pageable, filters) {
                var url = "api/pqm/inspectionplans/products/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&name={0}&number={1}&planType={2}&description={3}&searchQuery={4}&status={5}&phase={6}&product={7}".
                    format(filters.name, filters.number, filters.planType, filters.description, filters.searchQuery, filters.status, filters.phase, filters.product);
                return httpFactory.get(url);
            }

            function getAllMaterialInspectionPlans(pageable, filters) {
                var url = "api/pqm/inspectionplans/materials/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&name={0}&number={1}&planType={2}&description={3}&searchQuery={4}&status={5}&phase={6}&material={7}".
                    format(filters.name, filters.number, filters.planType, filters.description, filters.searchQuery, filters.status, filters.phase, filters.material);
                return httpFactory.get(url);
            }

            function getMultipleInspectionPlans(planIds) {
                var url = "api/pqm/inspectionplans/multiple/[" + planIds + "]";
                return httpFactory.get(url);
            }

            function getInspectionPlanRevision(planId) {
                var url = "api/pqm/inspectionplans/revisions/" + planId;
                return httpFactory.get(url);
            }

            function getPlanRevisionHistory(planId) {
                var url = "api/pqm/inspectionplans/revision/history/" + planId;
                return httpFactory.get(url);
            }

            function getInspectionPlanFiles(planId) {
                var url = "api/pqm/inspectionplans/" + planId + "/files";
                return httpFactory.get(url);
            }

            function createPlanAttribute(planId, attribute) {
                var url = "api/pqm/inspectionplans/" + planId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function createPlanRevisionAttribute(planId, attribute) {
                var url = "api/pqm/inspectionplans/" + planId + "/revisionAttributes";
                return httpFactory.post(url, attribute);
            }

            function updatePlanAttribute(planId, attribute) {
                var url = "api/pqm/inspectionplans/" + planId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function updatePlanRevisionAttribute(planId, attribute) {
                var url = "api/pqm/inspectionplans/" + planId + "/revisionAttributes";
                return httpFactory.put(url, attribute);
            }

            function getInspectionPlanChecklists(planId) {
                var url = "api/pqm/inspectionplans/" + planId + "/checklists";
                return httpFactory.get(url);
            }

            function createInspectionPlanChecklist(planId, checklist) {
                var url = "api/pqm/inspectionplans/" + planId + "/checklists";
                return httpFactory.post(url, checklist);
            }

            function updateInspectionPlanChecklist(planId, checklist) {
                var url = "api/pqm/inspectionplans/" + planId + "/checklists";
                return httpFactory.put(url, checklist);
            }

            function deleteInspectionPlanChecklist(planId, checklistId) {
                var url = "api/pqm/inspectionplans/" + planId + "/checklists/" + checklistId;
                return httpFactory.delete(url);
            }

            function getChecklistChildren(planId, sectionId) {
                var url = "api/pqm/inspectionplans/" + planId + "/checklists/" + sectionId + "/children";
                return httpFactory.get(url);
            }

            function getChecklistParams(planId, checklistId) {
                var url = "api/pqm/inspectionplans/" + planId + "/checklists/" + checklistId + "/params";
                return httpFactory.get(url);
            }

            function createPlanChecklistParams(planId, checklistId, params) {
                var url = "api/pqm/inspectionplans/" + planId + "/checklists/" + checklistId + "/params";
                return httpFactory.post(url, params);
            }

            function updatePlanChecklistParams(planId, checklistId, params) {
                var url = "api/pqm/inspectionplans/" + planId + "/checklists/" + checklistId + "/params";
                return httpFactory.put(url, params);
            }

            function deletePlanChecklistParams(planId, checklistId, paramId) {
                var url = "api/pqm/inspectionplans/" + planId + "/checklists/" + checklistId + "/params/" + paramId;
                return httpFactory.delete(url);
            }

            function getPlanDetailsCount(planId) {
                var url = "api/pqm/inspectionplans/" + planId + "/details/count";
                return httpFactory.get(url);
            }

            function getReleasedInspectionPlanList() {
                var url = "api/pqm/inspectionplans/list/released";
                return httpFactory.get(url);
            }

            function getReleasedInspectionPlanByProduct(product) {
                var url = "api/pqm/inspectionplans/products/" + product + "/released";
                return httpFactory.get(url);
            }

            function getReleasedInspectionPlanByPart(product) {
                var url = "api/pqm/inspectionplans/materials/" + product + "/released";
                return httpFactory.get(url);
            }

            function getInspectionPlanList() {
                var url = "api/pqm/inspectionplans/list";
                return httpFactory.get(url);
            }

            function getInspectionPlanTasks(personId) {
                var url = "api/pqm/inspectionplans/tasks/" + personId;
                return httpFactory.get(url);
            }
        }
    }
);