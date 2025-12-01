define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('ToolService', ToolService);

        function ToolService(httpFactory) {
            return {
                createTool: createTool,
                updateTool: updateTool,
                getTool: getTool,
                getAllTools: getAllTools,
                deleteTool: deleteTool,
                getMultipleTools: getMultipleTools,
                uploadImageAttribute: uploadImageAttribute,
                saveToolAttributes: saveToolAttributes,
                uploadImage: uploadImage
            };

            function createTool(tool) {
                var url = "api/mes/tools";
                return httpFactory.post(url, tool)
            }

            function updateTool(tool) {
                var url = "api/mes/tools/" + tool.id;
                return httpFactory.put(url, tool);
            }

            function getTool(id) {
                var url = "api/mes/tools/" + id;
                return httpFactory.get(url)
            }

            function deleteTool(tool) {
                var url = "api/mes/tools/" + tool;
                return httpFactory.delete(url);
            }

            function getAllTools(pageable, filters) {
                var url = "api/mes/tools/all??page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&type={1}&name={2}&searchQuery={3}&workOrder={4}".
                    format(filters.number, filters.type, filters.name, filters.searchQuery, filters.workOrder);
                return httpFactory.get(url);
            }

            function getMultipleTools(toolIds) {
                var url = "api/mes/tools/multiple/[" + toolIds + "]";
                return httpFactory.get(url);
            }

            function uploadImageAttribute(objectId, attributeId, file) {
                var url = "api/mes/tools/uploadimageattribute/" + objectId + "/" + attributeId;
                return httpFactory.upload(url, file);
            }


            function saveToolAttributes(toolId, attributes) {
                var url = "api/mes/tools/" + toolId + "/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function uploadImage(toolId, file) {
                var url = "api/mes/tools/" + toolId + "/image";
                return httpFactory.upload(url, file);
            }
        }
    }
);