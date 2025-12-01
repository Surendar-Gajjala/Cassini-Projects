define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('ObjectService', ObjectService);

        function ObjectService(httpFactory) {
            return {
                getObject: getObject,
                getObjectByTypeAndId: getObjectByTypeAndId,
                getMultipleObjectsByType: getMultipleObjectsByType,
                getObjectReferences: getObjectReferences
            };

            function getObjectByTypeAndId(type, id) {
                var url = "api/core/objects?type={0}&id={1}".format(type, id);
                return httpFactory.get(url);
            }

            function getObject(id) {
                var url = "api/core/objects/id/" + id;
                return httpFactory.get(url);
            }

            function getMultipleObjectsByType(ids, type) {
                var url = "api/core/objects/multiple/[" + ids + "]?type=" + type;
                return httpFactory.get(url);
            }

            function getObjectReferences(objects, type, property) {
                var objectIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && objectIds.indexOf(object[property]) === -1) {
                        objectIds.push(object[property]);
                    }
                });

                if (objectIds.length > 0) {
                    getMultipleObjectsByType(objectIds, type).then(
                        function (results) {
                            var map = new Hashtable();
                            angular.forEach(results, function (result) {
                                map.put(result.id, result);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var o = map.get(object[property]);
                                    if (o != null) {
                                        object[property + "Object"] = o;
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