define(['app/shared/services/services.module'], function (app) {
    app.factory('httpFactory',
        [
            '$http', '$q',

            function ($http, $q) {
                return {
                    get: function (url) {
                        var dfd = $q.defer();
                        var config = {};

                        if (app.session != null && app.session.apiKey != null) {
                            config.headers = {'CASSINI-API-KEY': app.session.apiKey};
                        }

                        $http.get(url, config).success(function (response) {
                            dfd.resolve(response);
                        }).error(function (response) {
                            dfd.reject(response);
                        });

                        return dfd.promise;
                    },

                    post: function (url, obj) {
                        var dfd = $q.defer();

                        var config = {};

                        if (app.session != null && app.session.apiKey != null) {
                            config.headers = {'CASSINI-API-KEY': app.session.apiKey};
                        }

                        $http.post(url, obj, config).success(function (response) {
                            dfd.resolve(response);
                        }).error(function (response) {
                            dfd.reject(response);
                        });

                        return dfd.promise;
                    },

                    submittingFormData: function (url, formData) {
                        var dfd = $q.defer();
                        var config = {
                            transformRequest: angular.identity,
                            headers: {'Content-Type': undefined}
                        };

                        $http.post(url, formData, config).success(function (response) {
                            dfd.resolve(response);
                        }).error(function (response) {
                            dfd.reject(response);
                        });

                        return dfd.promise;
                    },


                    put: function (url, obj) {
                        var dfd = $q.defer();

                        var config = {};

                        if (app.session != null && app.session.apiKey != null) {
                            config.headers = {'CASSINI-API-KEY': app.session.apiKey};
                        }

                        $http.put(url, obj, config).success(function (response) {
                            dfd.resolve(response);
                        }).error(function (response) {
                            dfd.reject(response);
                        });

                        return dfd.promise;
                    },
                    delete: function (url, obj) {
                        var dfd = $q.defer();

                        var config = {};

                        if (app.session != null && app.session.apiKey != null) {
                            config.headers = {'CASSINI-API-KEY': app.session.apiKey};
                        }

                        $http.delete(url, obj, config).success(function (response) {
                            dfd.resolve(response);
                        }).error(function (response) {
                            dfd.reject(response);
                        });

                        return dfd.promise;
                    },


                    all: function (urlList) {
                        var dfd = $q.defer();
                        var config = {};
                        var arr = [];

                        if (app.session != null && app.session.apiKey != null) {
                            config.headers = {'CASSINI-API-KEY': app.session.apiKey};
                        }

                        for (var i = 0; i < urlList.length; i++) {
                            arr.push($http.get(urlList[i], config));
                        }

                        $q.all(arr).then(
                            function (response) {
                                dfd.resolve(response);
                            },
                            function (response) {
                                dfd.reject(response);
                            }
                        );

                        return dfd.promise;
                    },
                    upload: function (url, file) {
                        var fd = new FormData();
                        fd.append('file', file);

                        var dfd = $q.defer();
                        var config = {
                            transformRequest: angular.identity,
                            headers: {'Content-Type': undefined}
                        };

                        $http.post(url, fd, config).success(function (response) {
                            dfd.resolve(response);
                        }).error(function (response) {
                            dfd.reject(response);
                        });

                        return dfd.promise;
                    },

                    uploadMultiple: function (url, files) {
                        var fd = new FormData();

                        for (var i = 0; i < files.length; i++) {
                            fd.append('file' + i, files[i]);
                        }

                        var dfd = $q.defer();
                        var config = {
                            transformRequest: angular.identity,
                            headers: {'Content-Type': undefined}
                        };

                        $http.post(url, fd, config).success(function (response) {
                            dfd.resolve(response);
                        }).error(function (response) {
                            dfd.reject(response);
                        });

                        return dfd.promise;
                    },

                    refs: function (objects, property, multipeObjectsFunc) {
                        var ids = [];
                        angular.forEach(objects, function (object) {
                            if (object[property] != null && ids.indexOf(object[property]) == -1) {
                                ids.push(object[property]);
                            }
                        });

                        if (ids.length > 0) {
                            multipeObjectsFunc(ids).then(
                                function (items) {
                                    var map = new Hashtable();
                                    angular.forEach(items, function (item) {
                                        map.put(item.id, item);
                                    });

                                    angular.forEach(objects, function (object) {
                                        if (object[property] != null) {
                                            var item = map.get(object[property]);
                                            if (item != null) {
                                                object[property + "Object"] = item;
                                            }
                                        }
                                    });
                                }
                            );
                        }
                    }
                }
            }
        ]);
});