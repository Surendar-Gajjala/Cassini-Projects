/**
 * Created by Nageshreddy on 04-12-2018.
 */
define(['app/shared/services/services.module'],
    function (module) {
        module.factory('$drdoApplication', drdoApplication);

        function drdoApplication() {
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

        module.factory('$bom', bom);

        function bom() {
            return {
                bomObject: null
            }
        }

        module.factory('compare', compare);
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

