define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('ProjectTemplateService', ProjectTemplateService);

        function ProjectTemplateService(httpFactory) {
            return {
                createProjectTemplate: createProjectTemplate,
                updateProjectTemplate: updateProjectTemplate,
                deleteProjectTemplate: deleteProjectTemplate,
                getProjectTemplate: getProjectTemplate,
                getAllProjectTemplates: getAllProjectTemplates,
                getProjectTemplates: getProjectTemplates,
                getProjectTemplateWbs: getProjectTemplateWbs,
                getTemplateWbsChildren: getTemplateWbsChildren,
                getProjectTemplateByName: getProjectTemplateByName,
                getProjectTemplateMembers: getProjectTemplateMembers,
                deleteProjectTemplateMember: deleteProjectTemplateMember,
                createProjectTemplateMembers: createProjectTemplateMembers,
                createProjectTemplateMember: createProjectTemplateMember,
                updateProjectTemplateMember: updateProjectTemplateMember,
                getAllProjectTemplateMembers: getAllProjectTemplateMembers,
                getProjectTemplateDetails: getProjectTemplateDetails,
                getProjectTemplatesByProgramNull: getProjectTemplatesByProgramNull,
                attachProjectTempWorkflow: attachProjectTempWorkflow
            };

            function createProjectTemplate(template) {
                var url = "api/plm/templates";
                return httpFactory.post(url, template);
            }

            function updateProjectTemplate(template) {
                var url = "api/plm/templates/" + template.id;
                return httpFactory.put(url, template);
            }

            function deleteProjectTemplate(templateId) {
                var url = "api/plm/templates/" + templateId;
                return httpFactory.delete(url);
            }

            function getProjectTemplate(templateId) {
                var url = "api/plm/templates/" + templateId;
                return httpFactory.get(url);
            }

            function getProjectTemplateByName(templateName) {
                var url = "api/plm/templates/byName?templateName=" + templateName;
                return httpFactory.get(url);
            }

            function getAllProjectTemplates(pageable, filters) {
                var url = "api/plm/templates/paged?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&type={1}&name={2}&searchQuery={3}".
                    format(filters.number, filters.type, filters.name, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getProjectTemplates() {
                var url = "api/plm/templates";
                return httpFactory.get(url);
            }

            function getProjectTemplatesByProgramNull() {
                var url = "api/plm/templates/programNull";
                return httpFactory.get(url);
            }

            function getProjectTemplateWbs(templateId) {
                var url = "api/plm/templates/" + templateId + "/wbs";
                return httpFactory.get(url);
            }

            function getTemplateWbsChildren(templateId, wbsId) {
                var url = "api/plm/templates/" + templateId + "/wbs/children/" + wbsId;
                return httpFactory.get(url);
            }

            function getProjectTemplateMembers(templateId, pageable) {
                var url = "api/plm/templates/" + templateId + "/members?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function deleteProjectTemplateMember(templateId, personId) {
                var url = "api/plm/templates/" + templateId + "/members/" + personId;
                return httpFactory.delete(url);
            }

            function createProjectTemplateMembers(templateId, projectPersons) {
                var url = "api/plm/templates/" + templateId + "/team/multiple";
                return httpFactory.post(url, projectPersons);
            }

            function createProjectTemplateMember(templateId, projectPerson) {
                var url = "api/plm/templates/" + templateId + "/team";
                return httpFactory.post(url, projectPerson);
            }

            function updateProjectTemplateMember(templateId, projectPerson) {
                var url = "api/plm/templates/" + templateId + "/team/" + projectPerson.id;
                return httpFactory.put(url, projectPerson);
            }

            function getAllProjectTemplateMembers(templateId) {
                var url = "api/plm/templates/team/members/" + templateId;
                return httpFactory.get(url);
            }

            function getProjectTemplateDetails(templateId) {
                var url = "api/plm/templates/" + templateId + "/details";
                return httpFactory.get(url);
            }
            function attachProjectTempWorkflow(templateId, wfId) {
                var url = "api/plm/templates/" + templateId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

        }
    }
);