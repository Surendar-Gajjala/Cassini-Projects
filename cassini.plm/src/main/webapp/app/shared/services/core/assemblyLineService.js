define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('AssemblyLineService', AssemblyLineService);

        function AssemblyLineService(httpFactory) {
            return {
                createAssemblyLine: createAssemblyLine,
                updateAssemblyLine: updateAssemblyLine,
                getAssemblyLine: getAssemblyLine,
                deleteAssemblyLine: deleteAssemblyLine,
                getAssemblyLines: getAssemblyLines,
                getAllAssemblyLines: getAllAssemblyLines,
                getMultipleAssemblyLines: getMultipleAssemblyLines,
                getAssemblyLineWorkCenters: getAssemblyLineWorkCenters,
                getAssemblyLineTabCount: getAssemblyLineTabCount
            };

            function createAssemblyLine(assemblyLine) {
                var url = "api/mes/assemblylines";
                return httpFactory.post(url, assemblyLine)
            }

            function updateAssemblyLine(assemblyLine) {
                var url = "api/mes/assemblylines/" + assemblyLine.id;
                return httpFactory.put(url, assemblyLine);
            }

            function getAssemblyLine(id) {
                var url = "api/mes/assemblylines/" + id;
                return httpFactory.get(url)
            }

            function deleteAssemblyLine(assemblyLine) {
                var url = "api/mes/assemblylines/" + assemblyLine;
                return httpFactory.delete(url);
            }

            function getAssemblyLines() {
                var url = "api/mes/assemblylines";
                return httpFactory.get(url);
            }

            function getAllAssemblyLines(pageable, filters) {
                var url = "api/mes/assemblylines/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&type={1}&name={2}&description{3}&searchQuery={4}".
                    format(filters.number, filters.type, filters.name, filters.description, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getMultipleAssemblyLines(assemblyLineIds) {
                var url = "api/mes/assemblylines/multiple/[" + assemblyLineIds + "]";
                return httpFactory.get(url);
            }

            function getAssemblyLineWorkCenters(assemblyLineId) {
                var url = "api/mes/assemblylines/" + assemblyLineId + "/workcenters";
                return httpFactory.get(url);
            }

            function getAssemblyLineTabCount(id) {
                var url = "api/mes/assemblylines/" + id + "/count";
                return httpFactory.get(url)
            }

        }
    }
);