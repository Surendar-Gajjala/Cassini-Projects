define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('RequirementService', RequirementService);

        function RequirementService(httpFactory) {
            return {
                createRequirement: createRequirement,
                updateRequirement: updateRequirement,
                updateRequirementVersion: updateRequirementVersion,
                getRequirementVersion: getRequirementVersion,
                getRequirement: getRequirement,
                deleteRequirement: deleteRequirement,
                getMultipleRequirements: getMultipleRequirements,
                getRequirementTree: getRequirementTree,
                searchRequirements: searchRequirements,
                addReviewer: addReviewer,
                updateReviewer: updateReviewer,
                deleteReviewer: deleteReviewer,
                approveRequirement: approveRequirement,
                rejectRequirement: rejectRequirement,
                getReviewersAndApprovers: getReviewersAndApprovers,
                getRequirements: getRequirements,
                requirementFreeTextSearch: requirementFreeTextSearch,
                getRequirementTabCount: getRequirementTabCount,
                reviewRequirement: reviewRequirement,
                createRequirementItems: createRequirementItems,
                createRequirementItem: createRequirementItem,
                updateRequirementItem: updateRequirementItem,
                deleteRequirementItem: deleteRequirementItem,
                getRequirementItems: getRequirementItems,
                getItemRequirements: getItemRequirements,
                reviseRequirement: reviseRequirement,
                getReqVersionHistory: getReqVersionHistory,
                getRequirementVersionObject: getRequirementVersionObject,
                getRequirementChildrenTree: getRequirementChildrenTree,
                searchRequirementObjects: searchRequirementObjects,
                deleteRequirementDocChildren: deleteRequirementDocChildren,
                submitReqVersion: submitReqVersion,
                getReqWorkflows: getReqWorkflows,
                getUsedRequirementTypeAttributeValues: getUsedRequirementTypeAttributeValues,
                getReqVersionIds: getReqVersionIds,
                getAssignedTo: getAssignedTo,
                getPriorities: getPriorities,
                getLifecyclePhases: getLifecyclePhases
            };

            function createRequirement(requirement) {
                var url = "api/req/documents/requirements";
                return httpFactory.post(url, requirement)
            }

            function updateRequirement(req) {
                var url = "api/req/documents/requirements/" + req.id;
                return httpFactory.put(url, req);
            }

            function updateRequirementVersion(reqId, reqVer) {
                var url = "api/req/documents/requirements/versions/" + reqId;
                return httpFactory.put(url, reqVer);
            }

            function getRequirement(id) {
                var url = "api/req/documents/requirements/" + id;
                return httpFactory.get(url)
            }

            function deleteRequirement(reqId) {
                var url = "api/req/documents/requirements/" + reqId;
                return httpFactory.delete(url);
            }

            function getMultipleRequirements(reqIds) {
                var url = "api/req/documents/requirements/multiple/[" + reqIds + "]";
                return httpFactory.get(url);
            }

            function getRequirementTree(reqdocumentId) {
                var url = "api/req/documents/revision/" + reqdocumentId + "/requirements/tree";
                return httpFactory.get(url);
            }

            function getRequirementVersion(versionId) {
                var url = "api/req/documents/requirements/version/" + versionId;
                return httpFactory.get(url);
            }

            function searchRequirements(criteria, pageable) {
                var url = "api/req/documents/requirements/search?searchQuery={0}&requirementDocument={1}"
                    .format(criteria.searchQuery, criteria.requirementDocument);
                return httpFactory.get(url);
            }

            function addReviewer(id, reviewer) {
                var url = "api/req/documents/requirements/{0}/reviewers".format(id);
                return httpFactory.post(url, reviewer);
            }

            function updateReviewer(id, reviewer) {
                var url = "api/req/documents/requirements/{0}/reviewers/{1}".format(id, reviewer.id);
                return httpFactory.put(url, reviewer);
            }

            function deleteReviewer(id, reviewer) {
                var url = "api/req/documents/requirements/{0}/reviewers/{1}".format(id, reviewer.id);
                return httpFactory.delete(url);
            }

            function approveRequirement(reqId, reviewer) {
                var url = "api/req/documents/requirements/" + reqId + "/approve";
                return httpFactory.put(url, reviewer)
            }

            function rejectRequirement(reqId, reviewer) {
                var url = "api/req/documents/requirements/" + reqId + "/reject";
                return httpFactory.put(url, reviewer)
            }

            function getReviewersAndApprovers(reqId) {
                var url = "api/req/documents/requirements/" + reqId + "/reviewers";
                return httpFactory.get(url)
            }

            function getRequirements(pageable) {
                var url = "api/req/documents/requirements/all/?page={0}&size={1}&sort{2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function requirementFreeTextSearch(filters, pageable) {
                var url = "api/req/documents/requirements/freeTextSearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&type={1}".
                    format(filters.searchQuery, filters.type);
                return httpFactory.get(url);
            }

            function getRequirementTabCount(id) {
                var url = "api/req/documents/requirements/" + id + "/count";
                return httpFactory.get(url)
            }

            function reviewRequirement(reqId, reviewer) {
                var url = "api/req/documents/requirements/" + reqId + "/review";
                return httpFactory.put(url, reviewer)
            }

            function createRequirementItems(reqId, reqItem) {
                var url = "api/req/documents/requirements/" + reqId + "/items/multiple";
                return httpFactory.post(url, reqItem)
            }

            function createRequirementItem(reqId, reqItem) {
                var url = "api/req/documents/requirements/" + reqId + "/items";
                return httpFactory.post(url, reqItem)
            }

            function updateRequirementItem(reqId, reqItem) {
                var url = "api/req/documents/requirements/" + reqId + "/items/" + reqItem.id;
                return httpFactory.put(url, reqItem)
            }

            function deleteRequirementItem(reqId, reqItemId) {
                var url = "api/req/documents/requirements/" + reqId + "/items/" + reqItemId;
                return httpFactory.delete(url)
            }

            function getRequirementItems(reqId) {
                var url = "api/req/documents/requirements/" + reqId + "/items";
                return httpFactory.get(url)
            }


            function getItemRequirements(itemId) {
                var url = "api/req/documents/item/" + itemId + "/requirements";
                return httpFactory.get(url)
            }

            function getReqVersionHistory(reqId) {
                var url = "api/req/documents/requirement/{0}/versions/history".format(reqId);
                return httpFactory.get(url);
            }

            function getRequirementVersionObject(versionId) {
                var url = "api/req/documents/requirements/children/version/" + versionId;
                return httpFactory.get(url);
            }

            function getRequirementChildrenTree(reqdocumentId) {
                var url = "api/req/documents/revision/" + reqdocumentId + "/requirements/children/tree";
                return httpFactory.get(url);
            }

            function searchRequirementObjects(criteria) {
                var url = "api/req/documents/requirement/objects/search?searchQuery={0}&requirementDocument={1}&type={2}&assignedTo={3}&priority={4}&phase={5}"
                    .format(criteria.searchQuery, criteria.requirementDocument, criteria.type, criteria.assignedTo, criteria.priority, criteria.phase);
                return httpFactory.get(url);
            }

            function deleteRequirementDocChildren(reqId) {
                var url = "api/req/documents/requirements/children/" + reqId;
                return httpFactory.delete(url);
            }

            function reviseRequirement(reqVersion, reqId) {
                var url = "api/req/documents/requirement/{0}/revise".format(reqId);
                return httpFactory.post(url, reqVersion);
            }

            function submitReqVersion(reqId) {
                var url = "api/req/documents/requirement/" + reqId + "/submit";
                return httpFactory.get(url);
            }
            function getReqWorkflows(typeId, type) {
                var url = "api/req/documents/requirement/type/" + typeId + "/workflow/" + type;
                return httpFactory.get(url);
            }

            function getUsedRequirementTypeAttributeValues(attributeId) {
                var url = "api/req/documents/attributes/" + attributeId;
                return httpFactory.get(url);
            }
            function getReqVersionIds(reqId) {
                var url = "api/req/documents/requirements/" + reqId + "/versions/ids";
                return httpFactory.get(url);
            }

            function getAssignedTo(reqId) {
                var url = "api/req/documents/assignedTo/"+reqId ;
                return httpFactory.get(url);
            }

            function getPriorities(reqId) {
                var url = "api/req/documents/priority/"+reqId ;
                return httpFactory.get(url);
            }

            function getLifecyclePhases(reqId) {
                var url = "api/req/documents/lifecyclePhases/"+reqId ;
                return httpFactory.get(url);
            }

        }
    }
);