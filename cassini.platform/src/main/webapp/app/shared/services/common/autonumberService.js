define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('AutonumberService', AutonumberService);

        function AutonumberService($q, httpFactory) {
            return {
                createAutonumber: createAutonumber,
                getAutonumbers: getAutonumbers,
                getAllAutonumbers: getAllAutonumbers,
                getAutonumberByName: getAutonumberByName,
                saveAutonumbers: saveAutonumbers,
                updateAutonumber: updateAutonumber,
                getNextNumber: getNextNumber,
                getNextNumbers: getNextNumbers,
                deleteAutonumber: deleteAutonumber,
                getAutonumber: getAutonumber,
                getAutonumberName: getAutonumberName,
                getAutonumberPrefix: getAutonumberPrefix,
                getNextNumberByName: getNextNumberByName
            };

            function getAutonumbers() {
                var url = "api/core/autonumbers";
                return httpFactory.get(url);
            }

            function getAllAutonumbers(pageable, filters) {
                var url = "api/core/autonumbers/search?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size,pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".format(filters.searchQuery);
                return httpFactory.get(url);
            }

            function createAutonumber(autonumber) {
                var url = "api/core/autonumbers";
                return httpFactory.post(url, autonumber);
            }

            function saveAutonumbers(autonumbers) {
                var url = "api/core/autonumbers/multiple";
                return httpFactory.post(url, autonumbers);
            }

            function getNextNumber(autonumberId) {
                var url = "api/core/autonumbers/" + autonumberId + "/next";
                return httpFactory.get(url);
            }

            function deleteAutonumber(autonumberId) {
                var url = "api/core/autonumbers/" + autonumberId;
                return httpFactory.delete(url);
            }

            function updateAutonumber(autonumberId, autonumber) {
                var url = "api/core/autonumbers/" + autonumberId;
                return httpFactory.put(url, autonumber);
            }

            function getNextNumbers(autonumberId, count) {
                var url = "api/core/autonumbers/" + autonumberId + "/next/" + count;
                return httpFactory.get(url);
            }

            function getAutonumber(autoNumberId) {
                var url = "api/core/autonumbers/" + autoNumberId;
                return httpFactory.get(url);
            }

            function getAutonumberName(name) {
                var url = "api/core/autonumbers/ByName/" + name;
                return httpFactory.get(url);
            }

            function getAutonumberPrefix(prefix) {
                var url = "api/core/autonumbers/ByPrefix/" + prefix;
                return httpFactory.get(url);
            }

            function getNextNumberByName(name) {
                var url = "api/core/autonumbers/" + name + "/name/next";
                return httpFactory.get(url);
            }

            function getAutonumberByName(name) {
                return $q(function (resolve, reject) {
                    getAutonumbers().then(
                        function (data) {
                            var found = null;
                            angular.forEach(data, function (item) {
                                if (item.name == name) {
                                    found = item;
                                }
                            });

                            resolve(found);
                        }
                    );
                });
            }
        }
    }
);