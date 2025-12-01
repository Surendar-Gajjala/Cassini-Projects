define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function (app) {
        app.factory('autonumberFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        getAutonumbers: function () {
                            var dfd = $q.defer(),
                                url = "api/common/autonumbers";
                            return httpFactory.get(url);
                        },

                        saveAutonumbers: function (autonumbers) {
                            var dfd = $q.defer(),
                                url = "api/common/autonumbers";
                            return httpFactory.post(url, autonumbers);
                        },

                        getNextNumber: function (autonumberId) {
                            var dfd = $q.defer(),
                                url = "api/common/autonumbers/" + autonumberId + "/next";
                            return httpFactory.get(url);
                        }
                    }
                }
            ]);
});