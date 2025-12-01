define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('PDMService', PDMVaultService);

        function PDMVaultService($q, httpFactory) {
            var baseUrl = "api/pdm/core";

            return {
                getRevisionedObject: getRevisionedObject,
                getAssembly: getAssembly,
                getAssemblyBom: getAssemblyBom,
                assignPartNumbersToAssembly: assignPartNumbersToAssembly,
                createEBOM: createEBOM,

                getPart: getPart,

                getFileVersionForRevisionedObject: getFileVersionForRevisionedObject,
                getAssemblyChildren: getAssemblyChildren,
                getWhereUsed: getWhereUsed,
                createDrawingTemplate: createDrawingTemplate,
                updateDrawingTemplate: updateDrawingTemplate,
                getDrawingTemplate: getDrawingTemplate,
                deleteDrawingTemplate: deleteDrawingTemplate,
                getDrawingTemplates: getDrawingTemplates
            };

            function getRevisionedObject(revId) {
                var url = baseUrl + "/revisioned/" + revId;
                return httpFactory.get(url);
            }

            function getAssembly(assemblyId) {
                var url = baseUrl + "/assemblies/" + assemblyId;
                return httpFactory.get(url);
            }

            function getAssemblyBom(assemblyId) {
                var url = baseUrl + "/assemblies/" + assemblyId + "/bom";
                return httpFactory.get(url);
            }

            function createEBOM(assemblyId) {
                var url = baseUrl + "/assemblies/" + assemblyId + "/ebom";
                return httpFactory.post(url);
            }

            function assignPartNumbersToAssembly(assemblyId) {
                var url = baseUrl + "/assemblies/" + assemblyId + "/assignpartnumbers";
                return httpFactory.get(url);
            }

            function getPart(partId) {
                var url = baseUrl + "/parts/" + partId;
                return httpFactory.get(url);
            }

            function getFileVersionForRevisionedObject(id) {
                var url = baseUrl + "/revisioned/" + id + "/file";
                return httpFactory.get(url);
            }

            function getAssemblyChildren(id) {
                var url = baseUrl + "/assemblies/" + id + "/children";
                return httpFactory.get(url);
            }

            function getWhereUsed(id) {
                var url = baseUrl + "/revisioned/" + id + "/whereused";
                return httpFactory.get(url);
            }

            function createDrawingTemplate(template) {
                var url = baseUrl + "/drawings/templates";
                return httpFactory.post(url, template);
            }

            function updateDrawingTemplate(template) {
                var url = baseUrl + "/drawings/templates/" + template.id;
                return httpFactory.put(url, template);
            }

            function getDrawingTemplate(templateId) {
                var url = baseUrl + "/drawings/templates/" + templateId;
                return httpFactory.get(url);
            }

            function deleteDrawingTemplate(templateId) {
                var url = baseUrl + "/drawings/templates/" + templateId;
                return httpFactory.delete(url);
            }

            function getDrawingTemplates() {
                var url = baseUrl + "/drawings/templates";
                return httpFactory.get(url);
            }

        }
    }
);