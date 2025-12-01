define(['app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'],
    function (mdoule) {
        mdoule.factory('SupplierAuditService', SupplierAuditService);

        function SupplierAuditService(httpFactory) {
            return {
                createSupplierAudit: createSupplierAudit,
                updateSupplierAudit: updateSupplierAudit,
                deleteSupplierAudit: deleteSupplierAudit,
                getAllSupplierAudits: getAllSupplierAudits,
                getSupplierAudit: getSupplierAudit,
                getSupplierAuditTabCounts: getSupplierAuditTabCounts,
                getSupplierAuditPlan: getSupplierAuditPlan,
                getSupplierAuditPlans: getSupplierAuditPlans,
                createMultipleSupplierAuditPlans: createMultipleSupplierAuditPlans,
                createSupplierAuditPlan: createSupplierAuditPlan,
                updateSupplierAuditPlan: updateSupplierAuditPlan,
                deleteSupplierAuditPlan: deleteSupplierAuditPlan,
                addPlanReviewer: addPlanReviewer,
                updatePlanReviewer: updatePlanReviewer,
                deletePlanReviewer: deletePlanReviewer,
                getPlanReviewersAndApprovers: getPlanReviewersAndApprovers,
                attachWorkflow: attachWorkflow,
                deleteWorkflow: deleteWorkflow,
                getWorkflows: getWorkflows,
                submitPlanReview: submitPlanReview,
                getAlbonairInternalAudit: getAlbonairInternalAudit,
                createAlbonairInternalAudit: createAlbonairInternalAudit,
                createSupplierAuditAttribute: createSupplierAuditAttribute,
                updateSupplierAuditAttribute: updateSupplierAuditAttribute
            };

            function createSupplierAudit(obj) {
                var url = "api/pqm/supplieraudits";
                return httpFactory.post(url, obj);
            }

            function updateSupplierAudit(supplieraudits) {
                var url = "api/pqm/supplieraudits/" + supplieraudits.id;
                return httpFactory.put(url, supplieraudits);
            }

            function deleteSupplierAudit(supplierAuditId) {
                var url = "api/pqm/supplieraudits/" + supplierAuditId;
                return httpFactory.delete(url);
            }

            function getSupplierAudit(supplierAuditId) {
                var url = "api/pqm/supplieraudits/" + supplierAuditId;
                return httpFactory.get(url);
            }

            function getAllSupplierAudits(pageable, filters) {
                var url = "api/pqm/supplieraudits/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&name={1}&description={2}&searchQuery={3}".
                    format(filters.number, filters.name, filters.description, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getSupplierAuditTabCounts(id) {
                var url = "api/pqm/supplieraudits/" + id + "/counts";
                return httpFactory.get(url);
            }

            function getSupplierAuditPlans(id) {
                var url = "api/pqm/supplieraudits/" + id + "/plan";
                return httpFactory.get(url);
            }

            function createMultipleSupplierAuditPlans(id, plans) {
                var url = "api/pqm/supplieraudits/" + id + "/plan/multiple";
                return httpFactory.post(url, plans);
            }

            function createSupplierAuditPlan(id, plan) {
                var url = "api/pqm/supplieraudits/" + id + "/plan";
                return httpFactory.post(url, plan);
            }

            function updateSupplierAuditPlan(id, plan) {
                var url = "api/pqm/supplieraudits/" + id + "/plan/" + plan.id;
                return httpFactory.put(url, plan);
            }

            function deleteSupplierAuditPlan(id, plan) {
                var url = "api/pqm/supplieraudits/" + id + "/plan/" + plan.id;
                return httpFactory.delete(url);
            }

            function getSupplierAuditPlan(id, plan) {
                var url = "api/pqm/supplieraudits/" + id + "/plan/" + plan.id;
                return httpFactory.get(url);
            }


            function addPlanReviewer(id, reviewer) {
                var url = "api/pqm/supplieraudits/plan/{0}/reviewers".format(id);
                return httpFactory.post(url, reviewer);
            }

            function updatePlanReviewer(id, reviewer) {
                var url = "api/pqm/supplieraudits/plan/{0}/reviewers/{1}".format(id, reviewer.id);
                return httpFactory.put(url, reviewer);
            }

            function deletePlanReviewer(id, reviewer) {
                var url = "api/pqm/supplieraudits/plan/{0}/reviewers/{1}".format(id, reviewer.id);
                return httpFactory.delete(url);
            }

            function getPlanReviewersAndApprovers(planId) {
                var url = "api/pqm/supplieraudits/plan/" + planId + "/reviewers";
                return httpFactory.get(url)
            }

            function attachWorkflow(partId, wfId) {
                var url = "api/pqm/supplieraudits/" + partId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

            function deleteWorkflow(partId) {
                var url = 'api/pqm/supplieraudits/' + partId + "/workflow/delete";
                return httpFactory.delete(url);
            }


            function getWorkflows(typeId, type) {
                var url = "api/pqm/supplieraudits/workflow/" + typeId + "/supplieraudittype/" + type;
                return httpFactory.get(url);
            }

            function submitPlanReview(planId, reviewer) {
                var url = "api/pqm/supplieraudits/plan/" + planId + "/reviewers/submit";
                return httpFactory.put(url, reviewer)
            }

            function getAlbonairInternalAudit(id) {
                var url = "api/plugins/albonair/audits/" + id;
                return httpFactory.get(url);
            }

            function createAlbonairInternalAudit(id, dto) {
                var url = "api/plugins/albonair/audits/" + id;
                return httpFactory.post(url, dto);
            }

            function createSupplierAuditAttribute(id, attribute) {
                var url = "api/pqm/supplieraudits/" + id + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateSupplierAuditAttribute(id, attribute) {
                var url = "api/pqm/supplieraudits/" + id + "/attributes";
                return httpFactory.put(url, attribute);
            }
        }
    }
);
