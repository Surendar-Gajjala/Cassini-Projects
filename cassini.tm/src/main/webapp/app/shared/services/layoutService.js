define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory',

    ],
    function (mdoule) {
        mdoule.factory('LayoutService', LayoutService);

        function LayoutService(httpFactory) {
            return {
                createLayoutDrawing: createLayoutDrawing,
                findLayoutByDate: findLayoutByDate
            };

            function findLayoutByDate(date){
                var url = "api/layoutDrawings/bydate/" +date;
                return httpFactory.get(url);

            }
            function createLayoutDrawing(layoutDrawing) {
                var url = "api/layoutDrawings";
                return httpFactory.post(url, layoutDrawing);
            }
        }
    }
);