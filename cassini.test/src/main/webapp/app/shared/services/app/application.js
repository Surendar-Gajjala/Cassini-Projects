/**
 * Created by Suresh Cassini on 03-Jul-18.
 */

define(['app/shared/services/services.module'],
    function (mdoule) {
        mdoule.factory('$application', application);

        function application() {
            return {
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

        mdoule.factory('$bom', bom);

        function bom() {
            return {
                bomObject: null
            }
        }

        mdoule.factory('compare', compare);
        function compare() {
            return {
                id: function (a, b) {
                    if (a.id > b.id)
                        return 1;
                    if (a.id < b.id)
                        return -1;
                    return 0;
                },

                itemNumber: function (a, b) {
                    if (a.itemNumber > b.itemNumber)
                        return 1;
                    if (a.itemNumber < b.itemNumber)
                        return -1;
                    return 0;
                },

                priority: function (a, b) {
                    if (a.priority < b.priority)
                        return -1;
                    if (a.priority > b.priority)
                        return 1;
                    return 0;
                }
            }

        }
    }
)
;

