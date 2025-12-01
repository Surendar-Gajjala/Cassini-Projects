define(['app/shared/services/services.module'],
    function (mdoule) {
        mdoule.factory('$application', application);

        function application() {
            var objectSelectors = new Hashtable();

            return {
                homeLoaded: false,
                session: {
                    apiKey: null
                },
                login: {
                    person: {
                        firstName: null
                    }
                },
                plugins: [],
                forgeToken: null,
                customizations: null,
                sessionDataStorage: new Hashtable(),
                registerObjectSelector: function(objectType, selector) {
                    objectSelectors.put(objectType, selector);
                },
                getObjectSelector: function(objectType) {
                    return objectSelectors.get(objectType);
                }
            }
        }
    }
);