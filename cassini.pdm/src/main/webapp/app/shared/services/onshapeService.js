define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('OnshapeService', OnshapeService);

        function OnshapeService(httpFactory) {
            return {
                getOnshapeIds: getOnshapeIds,
                translateFile: translateFile
            };

            function getOnshapeIds(ids) {
                var url = "api/pdm/onshape/ids/[" + ids + "]";
                return httpFactory.get(url);
            }

            function translateFile(id) {
                var url = "api/pdm/onshape/translate/" + id;
                return httpFactory.post(url, {});
            }
        }
    }
);