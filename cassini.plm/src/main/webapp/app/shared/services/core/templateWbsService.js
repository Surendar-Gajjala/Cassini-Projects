define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('TemplateWbsService', TemplateWbsService);

        function TemplateWbsService(httpFactory) {
            return {
                createTemplateWbs: createTemplateWbs,
                updateTemplateWbs: updateTemplateWbs,
                deleteTemplateWbs: deleteTemplateWbs,
                getTemplateWbsById: getTemplateWbsById,
                getAllTemplateWbs: getAllTemplateWbs,
                getTemplateWbsByName: getTemplateWbsByName,
                createWBSElements: createWBSElements
            };

            function createWBSElements(templateId, wbsList) {
                var url = "api/plm/templates/wbs/" + templateId + "/wbsList";
                return httpFactory.post(url, wbsList);
            }

            function createTemplateWbs(wbs) {
                var url = "api/plm/templates/wbs";
                return httpFactory.post(url, wbs);
            }

            function updateTemplateWbs(wbs) {
                var url = "api/plm/templates/wbs/" + wbs.id;
                return httpFactory.put(url, wbs);
            }

            function deleteTemplateWbs(wbsId) {
                var url = "api/plm/templates/wbs/" + wbsId;
                return httpFactory.delete(url);
            }

            function getTemplateWbsById(wbsId) {
                var url = "api/plm/templates/wbs/" + wbsId;
                return httpFactory.get(url);
            }

            function getAllTemplateWbs() {
                var url = "api/plm/templates/wbs";
                return httpFactory.get(url);
            }

            function getTemplateWbsByName(template, wbsName) {
                var url = "api/plm/templates/wbs/" + template + "/byName?templateWbsName=" + wbsName;
                return httpFactory.get(url);
            }
        }
    }
);