define(['app/app.modules'], function (app) {
    app.factory('httpFactory',
        [
            '$http', '$q',

            function ($http, $q) {
                return {
                    get: function (url) {
                        var dfd = $q.defer();
                        var config = {
                        };

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

                        var config = {
                        };

                        if(app.session != null && app.session.apiKey != null) {
                            config.headers = { 'CASSINI-API-KEY': app.session.apiKey };
                        }

                        $http.post(url, obj, config).success(function (response) {
                            dfd.resolve(response);
                        }).error(function (response) {
                            dfd.reject(response);
                        });

                        return dfd.promise;
                    },


                  put: function (url, obj) {
                        var dfd = $q.defer();

                        var config = {
                        };

                        if(app.session != null && app.session.apiKey != null) {
                            config.headers = { 'CASSINI-API-KEY': app.session.apiKey };
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

                        var config = {
                        };

                        if(app.session != null && app.session.apiKey != null) {
                            config.headers = { 'CASSINI-API-KEY': app.session.apiKey };
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
                        var config = {
                        };
                        var arr = [];

                        if (app.session != null && app.session.apiKey != null) {
                            config.headers = {'CASSINI-API-KEY': app.session.apiKey};
                        }

                        for(var i = 0; i < urlList.length; i++){
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
                    }
                }
            }
        ]);
});