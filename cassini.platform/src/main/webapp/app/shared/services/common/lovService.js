define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('LovService', LovService);

        function LovService(httpFactory) {
            var baseUrl = "api/core/lovs";
            return {
                createLov: createLov,
                updateLov: updateLov,
                getAllLovs: getAllLovs,
                getLov: getLov,
                deleteLov: deleteLov,
                getLovsByType: getLovsByType,
                getLovByName:getLovByName,
                getLovMapWithNameAsKey: getLovMapWithNameAsKey,
                getLovById: getLovById
            };

            function createLov(lov) {
                return httpFactory.post(baseUrl, lov);
            }

            function updateLov(lov) {
                var url = baseUrl + "/" + lov.id;
                return httpFactory.put(url, lov);
            }

            function getAllLovs() {
                return httpFactory.get(baseUrl + "/all");
            }

            function getLov(id) {
                return httpFactory.get(baseUrl + "/" + id);
            }

            function deleteLov(id) {
                return httpFactory.delete(baseUrl + "/" + id);
            }

            function getLovById(id) {
                return httpFactory.get(baseUrl + "/getLovById/" + id);
            }

            function getLovsByType(type) {
                return httpFactory.get(baseUrl + "/type/" + type);
            }

            function getLovByName(name){
                return httpFactory.get(baseUrl + "/lovByName/" + name);
            }

            function getLovMapWithNameAsKey(){
                return httpFactory.get(baseUrl +"/map/name");
            }
        }
    }
);