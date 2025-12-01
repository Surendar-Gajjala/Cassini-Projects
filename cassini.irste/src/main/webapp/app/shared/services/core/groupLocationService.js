/**
 * Created by Nageshreddy on 13-11-2018.
 */
define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('GroupLocationService', GroupLocationService);

        function GroupLocationService(httpFactory) {
            var url = "api/irste/groups";
            return {
                getGroups: getGroups,
                createGroup: createGroup,
                deleteGroup: deleteGroup,
                updateGroup: updateGroup,
                getLocationReference: getLocationReference,
                getLocationUtilities: getLocationUtilities,
                addLocationUtility: addLocationUtility,
                deleteLocationUtility: deleteLocationUtility
            };

            function addLocationUtility(utility, location) {
                return httpFactory.post(url + "/locationUtilities/" + utility + "/" + location);
            }

            function deleteLocationUtility(utility, location) {
                return httpFactory.delete(url + "/locationUtilities/" + utility + "/" + location);
            }

            function getGroups() {
                return httpFactory.get(url);
            }

            function deleteGroup(id) {
                return httpFactory.delete(url + "/" + id);
            }

            function createGroup(partTracking) {
                return httpFactory.post(url, partTracking);
            }

            function updateGroup(partTracking) {
                return httpFactory.put(url + "/" + partTracking.id, partTracking);
            }

            function getMultipleLocations(ids) {
                return httpFactory.get(url + "/locations/multiple/[" + ids + "]");
            }

            function getLocationUtilities() {
                return httpFactory.get(url + "/locationUtilities");
            }

            function getLocationReference(objects, property) {
                var stepIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && stepIds.indexOf(object[property]) == -1) {
                        stepIds.push(object[property]);
                    }
                });

                if (stepIds.length > 0) {
                    getMultipleLocations(stepIds).then(
                        function (steps) {
                            var map = new Hashtable();
                            angular.forEach(steps, function (step) {
                                map.put(step.id, step);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var step = map.get(object[property]);
                                    if (step != null) {
                                        object[property + "Object"] = step;
                                    }
                                }
                            });
                        }
                    );
                }
            }
        }
    }
);