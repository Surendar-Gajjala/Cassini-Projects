define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('ReqDocTemplateService', ReqDocTemplateService);

        function ReqDocTemplateService(httpFactory) {
            return {
                createReqDocTemplate: createReqDocTemplate,
                updateReqDocTemplate: updateReqDocTemplate,
                getReqDocTemplate: getReqDocTemplate,
                deleteReqDocTemplate: deleteReqDocTemplate,
                getAllReqDocTemplates: getAllReqDocTemplates,
                getAllReqDocumentTemplates: getAllReqDocumentTemplates,

                addReqDocTemplateReviewer: addReqDocTemplateReviewer,
                updateReqDocTemplateReviewer: updateReqDocTemplateReviewer,
                deleteReqDocTemplateReviewer: deleteReqDocTemplateReviewer,
                getDocTemplateReviewers: getDocTemplateReviewers,

                getRequirementTemplateTree: getRequirementTemplateTree,
                getRequirementTemplate: getRequirementTemplate,
                getRequirementTemplateReviewers: getRequirementTemplateReviewers,

                createRequirementTemplate: createRequirementTemplate,
                updateRequirementTemplate: updateRequirementTemplate,
                deleteRequirementTemplate: deleteRequirementTemplate,

                addRequirementTemplateReviewer: addRequirementTemplateReviewer,
                updateRequirementTemplateReviewer: updateRequirementTemplateReviewer,
                deleteRequirementTemplateReviewer: deleteRequirementTemplateReviewer,
                getReqDocumentTemplateTabCount: getReqDocumentTemplateTabCount,
                getRequirementTemplateTabCount: getRequirementTemplateTabCount
            };

            function createReqDocTemplate(reqDocTemplate) {
                var url = "api/req/documents/template";
                return httpFactory.post(url, reqDocTemplate)
            }

            function updateReqDocTemplate(reqDocTemplate) {
                var url = "api/req/documents/template/" + reqDocTemplate.id;
                return httpFactory.put(url, reqDocTemplate);
            }

            function getReqDocTemplate(id) {
                var url = "api/req/documents/template/" + id;
                return httpFactory.get(url)
            }

            function deleteReqDocTemplate(reqDocTemplate) {
                var url = "api/req/documents/template/" + reqDocTemplate;
                return httpFactory.delete(url);
            }

            function getAllReqDocTemplates(pageable, filters) {
                var url = "api/req/documents/template/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&name={0}&description{1}&searchQuery={2}".
                    format(filters.name, filters.description, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getAllReqDocumentTemplates() {
                var url = "api/req/documents/template";
                return httpFactory.get(url);
            }


            function addReqDocTemplateReviewer(reviewer) {
                var url = "api/req/documents/template/{0}/reviewer".format(reviewer.documentTemplate);
                return httpFactory.post(url, reviewer);
            }


            function addRequirementTemplateReviewer(reviewer) {
                var url = "api/req/documents/template/requirement/{0}/reviewer".format(reviewer.requirementTemplate);
                return httpFactory.post(url, reviewer);
            }

            function updateRequirementTemplateReviewer(reviewer) {
                var url = "api/req/documents/template/requirement/{0}/reviewer/{1}".format(reviewer.requirementTemplate, reviewer.id);
                return httpFactory.put(url, reviewer);
            }

            function deleteRequirementTemplateReviewer(reviewer) {
                var url = "api/req/documents/template/requirement/{0}/reviewer/{1}".format(reviewer.requirementTemplate, reviewer.id);
                return httpFactory.delete(url);
            }


            function updateReqDocTemplateReviewer(reviewer) {
                var url = "api/req/documents/template/{0}/reviewer/{1}".format(reviewer.documentTemplate, reviewer.id);
                return httpFactory.put(url, reviewer);
            }

            function deleteReqDocTemplateReviewer(reviewer) {
                var url = "api/req/documents/template/{0}/reviewer/{1}".format(reviewer.documentTemplate, reviewer.id);
                return httpFactory.delete(url);
            }

            function getDocTemplateReviewers(reqDocId) {
                var url = "api/req/documents/template/" + reqDocId + "/reqDocTemReviewer/all";
                return httpFactory.get(url);
            }

            function getRequirementTemplateTree(reqdocumentId) {
                var url = "api/req/documents/template/" + reqdocumentId + "/requirement/template/tree";
                return httpFactory.get(url);
            }

            function getRequirementTemplate(id) {
                var url = "api/req/documents/template/requirement/" + id;
                return httpFactory.get(url)
            }

            function getRequirementTemplateReviewers(reqId) {
                var url = "api/req/documents/template/requirement/" + reqId + "/reviewers";
                return httpFactory.get(url);
            }

            function createRequirementTemplate(requirement) {
                var url = "api/req/documents/template/requirement";
                return httpFactory.post(url, requirement)
            }

            function updateRequirementTemplate(requirement) {
                var url = "api/req/documents/template/requirement/" + requirement.id;
                return httpFactory.put(url, requirement);
            }

            function deleteRequirementTemplate(id) {
                var url = "api/req/documents/template/requirement/" + id;
                return httpFactory.delete(url);
            }
            function getReqDocumentTemplateTabCount(id) {
                var url = "api/req/documents/template/" + id + "/count";
                return httpFactory.get(url)
            }

            function getRequirementTemplateTabCount(id) {
                var url = "api/req/documents/template/requirement/" + id + "/count";
                return httpFactory.get(url)
            }
        }
    }
);