/**
 * Created by Nageshreddy on 08-10-2018.
 */
define([
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('ListService', ListService);
        function ListService(httpFactory) {
            var url = "api/drdo/lists";
            return {
                getLists: getLists,
                createList: createList,
                deleteList: deleteList,
                updateList: updateList,
                getPartTrackingStepReference: getPartTrackingStepReference
            };


            function getLists() {
                return httpFactory.get(url);
            }

            function deleteList(id) {
                return httpFactory.delete(url + "/" + id);
            }

            function createList(partTracking) {
                return httpFactory.post(url, partTracking);
            }

            function updateList(partTracking) {
                return httpFactory.put(url + "/" + partTracking.id, partTracking);
            }

            function getMultipleCheckSteps(ids) {
                return httpFactory.get(url + "/steps/multiple/[" + ids + "]");
            }

            function getPartTrackingStepReference(objects, property) {
                var stepIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && stepIds.indexOf(object[property]) == -1) {
                        stepIds.push(object[property]);
                    }
                });

                if (stepIds.length > 0) {
                    getMultipleCheckSteps(stepIds).then(
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
