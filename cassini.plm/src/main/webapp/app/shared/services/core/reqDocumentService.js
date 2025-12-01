define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('ReqDocumentService', ReqDocumentService);

        function ReqDocumentService(httpFactory) {
            return {
                createReqDocument: createReqDocument,
                updateReqDocument: updateReqDocument,
                getReqDocument: getReqDocument,
                deleteReqDocument: deleteReqDocument,
                getAllReqDocuments: getAllReqDocuments,
                getMultipleReqDocuments: getMultipleReqDocuments,
                getReqDocLatestRevisionReferences: getReqDocLatestRevisionReferences,
                getReqDocumentRevision: getReqDocumentRevision,
                updateReqDocumentOwner: updateReqDocumentOwner,
                submitReqDocument: submitReqDocument,
                releaseReqDocument: releaseReqDocument,
                getReqDocumentTabCount: getReqDocumentTabCount,
                addReqDocumentReviewer: addReqDocumentReviewer,
                updateReqDocumentReviewer: updateReqDocumentReviewer,
                deleteReqDocumentReviewer: deleteReqDocumentReviewer,
                getDocumentReviewersAndApprovers: getDocumentReviewersAndApprovers,
                approveRequirementDocument: approveRequirementDocument,
                rejectRequirementDocument: rejectRequirementDocument,
                reviewRequirementDocument: reviewRequirementDocument,
                approveAllRequirements: approveAllRequirements,
                saveAsRequirementDocumentTemplate: saveAsRequirementDocumentTemplate,
                getReqDocumentRevisionHistory: getReqDocumentRevisionHistory,
                reviseRequirementDocument: reviseRequirementDocument,
                getReqDocWorkflows: getReqDocWorkflows,
                attachReqDocWorkflow: attachReqDocWorkflow,
                attachReqWorkflow: attachReqWorkflow,
                deleteWorkflow: deleteWorkflow,
                getUsedRequirementTypeAttributeValues: getUsedRequirementTypeAttributeValues,
                getReqDocRevisionIds: getReqDocRevisionIds,
                getOwners: getOwners,
                getReqDocTypeLifecycles: getReqDocTypeLifecycles
            };

            function createReqDocument(reqdocument) {
                var url = "api/req/documents";
                return httpFactory.post(url, reqdocument)
            }

            function updateReqDocument(reqdocument) {
                var url = "api/req/documents/" + reqdocument.id;
                return httpFactory.put(url, reqdocument);
            }

            function getReqDocument(id) {
                var url = "api/req/documents/" + id;
                return httpFactory.get(url)
            }

            function deleteReqDocument(reqdocument) {
                var url = "api/req/documents/" + reqdocument;
                return httpFactory.delete(url);
            }

            function getAllReqDocuments(pageable, filters) {
                var url = "api/req/documents/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&type={1}&name={2}&description{3}&searchQuery={4}&project={5}&phase={6}&owner={7}".
                    format(filters.number, filters.type, filters.name, filters.description, filters.searchQuery, filters.project, filters.phase, filters.owner);
                return httpFactory.get(url);
            }

            function getMultipleReqDocuments(reqdocumentIds) {
                var url = "api/req/documents/multiple/[" + reqdocumentIds + "]";
                return httpFactory.get(url);
            }

            function getDocumentRevisionsByIds(revisionIds) {
                var url = "api/req/documents/revisions/multiple/[" + revisionIds + "]";
                return httpFactory.get(url);
            }

            function getReqDocumentRevision(revisionId) {
                var url = "api/req/documents/revision/" + revisionId;
                return httpFactory.get(url);
            }

            function getReqDocLatestRevisionReferences(objects, property) {
                var revisionIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && revisionIds.indexOf(object[property]) == -1) {
                        revisionIds.push(object[property]);
                    }
                });

                if (revisionIds.length > 0) {
                    getDocumentRevisionsByIds(revisionIds).then(
                        function (revisions) {
                            var map = new Hashtable();
                            angular.forEach(revisions, function (revision) {
                                map.put(revision.id, revision);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var revision = map.get(object[property]);
                                    if (revision != null) {
                                        object[property + "Object"] = revision;
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function updateReqDocumentOwner(reqdocument) {
                var url = "api/req/documents/revision/" + reqdocument.id;
                return httpFactory.put(url, reqdocument);
            }

            function submitReqDocument(reqDocId) {
                var url = "api/req/documents/" + reqDocId + "/submit";
                return httpFactory.get(url);
            }

            function releaseReqDocument(reqDocId) {
                var url = "api/req/documents/" + reqDocId + "/release";
                return httpFactory.get(url);
            }

            function getReqDocumentTabCount(id) {
                var url = "api/req/documents/" + id + "/count";
                return httpFactory.get(url)
            }

            function addReqDocumentReviewer(id, reviewer) {
                var url = "api/req/documents/{0}/reviewers".format(id);
                return httpFactory.post(url, reviewer);
            }

            function updateReqDocumentReviewer(id, reviewer) {
                var url = "api/req/documents/{0}/reviewers/{1}".format(id, reviewer.id);
                return httpFactory.put(url, reviewer);
            }

            function deleteReqDocumentReviewer(id, reviewer) {
                var url = "api/req/documents/{0}/reviewers/{1}".format(id, reviewer.id);
                return httpFactory.delete(url);
            }

            function getDocumentReviewersAndApprovers(reqId) {
                var url = "api/req/documents/" + reqId + "/reviewers";
                return httpFactory.get(url)
            }

            function approveRequirementDocument(reqId, reviewer) {
                var url = "api/req/documents/" + reqId + "/approve";
                return httpFactory.put(url, reviewer)
            }

            function rejectRequirementDocument(reqId, reviewer) {
                var url = "api/req/documents/" + reqId + "/reject";
                return httpFactory.put(url, reviewer)
            }

            function reviewRequirementDocument(reqId, reviewer) {
                var url = "api/req/documents/" + reqId + "/review";
                return httpFactory.put(url, reviewer)
            }

            function approveAllRequirements(reqId) {
                var url = "api/req/documents/" + reqId + "/requirements/all/approve";
                return httpFactory.put(url)
            }

            function saveAsRequirementDocumentTemplate(template, reqDocId) {
                var url = "api/req/documents/" + reqDocId + "/template";
                return httpFactory.post(url, template);
            }

            function getReqDocumentRevisionHistory(reqId) {
                var url = "api/req/documents/{0}/revisions/history".format(reqId);
                return httpFactory.get(url);
            }

            function reviseRequirementDocument(revison, reqId) {
                var url = "api/req/documents/{0}/revise".format(reqId);
                return httpFactory.post(url, revison);
            }

            function getReqDocWorkflows(typeId, type) {
                var url = "api/req/documents/type/" + typeId + "/workflow/" + type;
                return httpFactory.get(url);
            }

            function attachReqDocWorkflow(reqDoc, wfId) {
                var url = "api/req/documents/" + reqDoc + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

            function attachReqWorkflow(reqDoc, wfId) {
                var url = "api/req/documents/requirements/" + reqDoc + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

            function deleteWorkflow(docId) {
                var url = 'api/req/documents/' + docId + "/workflow/delete";
                return httpFactory.delete(url);
            }

            function getUsedRequirementTypeAttributeValues(attributeId) {
                var url = "api/req/documents/attributes/" + attributeId;
                return httpFactory.get(url);
            }

            function getReqDocRevisionIds(reqDocId) {
                var url = "api/req/documents/" + reqDocId + "/revisions/ids";
                return httpFactory.get(url);
            }

            function getOwners() {
                var url = "api/req/documents/owners" ;
                return httpFactory.get(url);
            }

            function getReqDocTypeLifecycles() {
                var url = "api/req/documents/types/lifecycles";
                return httpFactory.get(url);
            }
        }
    }
);