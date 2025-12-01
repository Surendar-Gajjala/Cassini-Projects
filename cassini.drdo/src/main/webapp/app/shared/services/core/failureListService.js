/**
 * Created by Nageshreddy on 07-01-2019.
 */
define([
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('FailureListService', FailureListService);
        function FailureListService(httpFactory) {
            var url = "api/drdo/failures";
            return {
                getLists: getLists,
                createList: createList,
                deleteList: deleteList,
                updateList: updateList,
                getFailureStepReference: getFailureStepReference,
                getFailProcessItems: getFailProcessItems
            };


            function getLists() {
                return httpFactory.get(url);
            }

            function deleteList(id) {
                return httpFactory.delete(url + "/" + id);
            }

            function createList(failurelist) {
                return httpFactory.post(url, failurelist);
            }

            function updateList(failurelist) {
                return httpFactory.put(url + "/" + failurelist.id, failurelist);
            }

            function getMultipleFailureSteps(ids) {
                return httpFactory.get(url + "/steps/multiple/[" + ids + "]");
            }

            function getFailProcessItems(pageable) {
                var url = "api/drdo/failures/failureProcess?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getFailureStepReference(objects, property) {
                var stepIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && stepIds.indexOf(object[property]) == -1) {
                        stepIds.push(object[property]);
                    }
                });

                if (stepIds.length > 0) {
                    getMultipleFailureSteps(stepIds).then(
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
