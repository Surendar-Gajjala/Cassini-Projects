define(['app/shared/services/services.module'],
    function (mdoule) {
        mdoule.factory('$application', application);

        function application() {
            return {
                viewType: null,
                homeLoaded: false,
                session: {
                    apiKey: null
                },
                login: {
                    person: {
                        firstName: null
                    }
                }
            }
        }
    }
);