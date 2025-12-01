define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('ToolTypeService', ToolTypeService);

        function ToolTypeService($q, httpFactory) {
            return {
                createToolType: createToolType,
                updateToolType: updateToolType,
                deleteToolType: deleteToolType,
                getToolType: getToolType,
                getAllToolTypes: getAllToolTypes,
                getMultipleToolTypes: getMultipleToolTypes

            };

            function createToolType(toolType) {
                var url = "api/plm/toolTypes";
                return httpFactory.post(url, toolType);
            }

            function updateToolType(toolType) {
                var url = "api/plm/toolTypes/" + toolType.id;
                return httpFactory.put(url, toolType);
            }

            function deleteToolType(id) {
                var url = "api/plm/toolTypes/" + id;
                return httpFactory.delete(url);
            }

            function getToolType(id) {
                var url = "api/plm/toolTypes/" + id;
                return httpFactory.get(url);
            }

            function getAllToolTypes() {
                var url = "api/plm/toolTypes";
                return httpFactory.get(url);
            }

            function getMultipleToolTypes(ids) {
                var url = "api/plm/toolTypes/multiple/[{ids}]";
                return httpFactory.get(url);
            }

        }
    }
);