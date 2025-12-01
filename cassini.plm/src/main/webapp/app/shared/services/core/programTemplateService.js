define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('ProgramTemplateService', ProgramTemplateService);

        function ProgramTemplateService(httpFactory) {
            return {
                createProgramTemplate: createProgramTemplate,
                updateProgramTemplate: updateProgramTemplate,
                deleteProgramTemplate: deleteProgramTemplate,
                getProgramTemplate: getProgramTemplate,
                getAllProgramTemplates: getAllProgramTemplates,
                getProgramTemplates: getProgramTemplates,
                getProgramTemplateByName: getProgramTemplateByName,
                getProgramTemplateResources: getProgramTemplateResources,
                deleteProgramTemplateResource: deleteProgramTemplateResource,
                createProgramTemplateResources: createProgramTemplateResources,
                getProgramTemplateDetails: getProgramTemplateDetails,
                createProgramTemplateProject: createProgramTemplateProject,
                getProgramTemplateProject: getProgramTemplateProject,
                updateProgramTemplateProject: updateProgramTemplateProject,
                deleteProgramTemplateProject: deleteProgramTemplateProject,
                updateProgramTemplateResource: updateProgramTemplateResource,
                createProgramTemplateResource: createProgramTemplateResource,
                getProgramTemplateProjects: getProgramTemplateProjects,
                attachProgramTempWorkflow: attachProgramTempWorkflow
            };

            function createProgramTemplate(template) {
                var url = "api/plm/programtemplates";
                return httpFactory.post(url, template);
            }

            function updateProgramTemplate(template) {
                var url = "api/plm/programtemplates/" + template.id;
                return httpFactory.put(url, template);
            }

            function deleteProgramTemplate(templateId) {
                var url = "api/plm/programtemplates/" + templateId;
                return httpFactory.delete(url);
            }

            function getProgramTemplate(templateId) {
                var url = "api/plm/programtemplates/" + templateId;
                return httpFactory.get(url);
            }

            function getProgramTemplateByName(templateName) {
                var url = "api/plm/programtemplates/byName?templateName=" + templateName;
                return httpFactory.get(url);
            }

            function getAllProgramTemplates(pageable, filters) {
                var url = "api/plm/programtemplates/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(filters.searchQuery);
                return httpFactory.get(url);
            }

            function getProgramTemplates() {
                var url = "api/plm/programtemplates";
                return httpFactory.get(url);
            }

            function getProgramTemplateResources(templateId) {
                var url = "api/plm/programtemplates/" + templateId + "/resources";
                return httpFactory.get(url);
            }

            function deleteProgramTemplateResource(templateId, personId) {
                var url = "api/plm/programtemplates/" + templateId + "/resources/" + personId;
                return httpFactory.delete(url);
            }

            function createProgramTemplateResources(templateId, templateResources) {
                var url = "api/plm/programtemplates/" + templateId + "/resources/multiple";
                return httpFactory.post(url, templateResources);
            }

            function createProgramTemplateResource(templateId, templateResource) {
                var url = "api/plm/programtemplates/" + templateId + "/resources";
                return httpFactory.post(url, templateResource);
            }

            function updateProgramTemplateResource(templateId, projectPerson) {
                var url = "api/plm/programtemplates/" + templateId + "/resources/" + projectPerson.id;
                return httpFactory.put(url, projectPerson);
            }

            function getProgramTemplateDetails(templateId) {
                var url = "api/plm/programtemplates/" + templateId + "/details";
                return httpFactory.get(url);
            }

            function createProgramTemplateProject(programId, project) {
                var url = "api/plm/programtemplates/" + programId + "/projects";
                return httpFactory.post(url, project);
            }

            function getProgramTemplateProject(programId, projectId) {
                var url = "api/plm/programtemplates/" + programId + "/projects/" + projectId;
                return httpFactory.get(url);
            }

            function getProgramTemplateProjects(programId) {
                var url = "api/plm/programtemplates/" + programId + "/projects";
                return httpFactory.get(url);
            }

            function updateProgramTemplateProject(programId, project) {
                var url = "api/plm/programtemplates/" + programId + "/projects/" + project.id;
                return httpFactory.put(url, project);
            }

            function deleteProgramTemplateProject(programId, projectId) {
                var url = "api/plm/programtemplates/" + programId + "/projects/" + projectId;
                return httpFactory.delete(url);
            }

            function attachProgramTempWorkflow(templateId, wfId) {
                var url = "api/plm/programtemplates/" + templateId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

        }
    }
);