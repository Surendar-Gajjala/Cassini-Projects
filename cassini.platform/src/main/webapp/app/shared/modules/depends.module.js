define(
    [

    ],

    function() {
        var module = angular.module('app.depends', [
                'ui.router',
                'door3.css'
            ]
        );

        module.provider('dependency', function() {
            var factory = {};

            factory.resolve = function(dependencies){
                var definition = {
                    resolver: ['$q','$rootScope', function($q, $rootScope){
                        var deferred = $q.defer();

                        require(dependencies, function(){
                            $rootScope.$apply(function(){
                                deferred.resolve();
                            });
                        });

                        return deferred.promise;
                    }]
                };

                return definition;
            };
            factory.configRoutes = function($urlRouterProvider, $stateProvider, routesConfig) {
                // For any unmatched url, send to /
                if(routesConfig.otherwise != undefined && routesConfig.otherwise != null) {
                    $urlRouterProvider.otherwise(routesConfig.otherwise);
                }

                if(routesConfig.routes !== undefined) {
                    angular.forEach(routesConfig.routes, function(route, path) {
                        var obj = {};
                        obj.url = route.url;
                        obj.templateUrl = route.templateUrl;

                        if(route.css){
                            obj.css = route.css;
                        }
                        if(route.controller) {
                            obj.controller = route.controller;
                        }
                        if(route.resolve) {
                            obj.resolve = factory.resolve(route.resolve);
                        }
                        if(route.abstract) {
                            obj.abstract = true;
                        }


                        if(route.views) {
                            obj.views = {};

                            angular.forEach(route.views, function(view, name) {
                                var viewObj = {};

                                if(view.templateUrl) {
                                    viewObj.templateUrl = view.templateUrl;
                                }
                                if(view.css){
                                    viewObj.css = view.css;
                                }
                                if(view.controller) {
                                    viewObj.controller = view.controller;
                                }
                                if(view.resolve) {
                                    viewObj.resolve = factory.resolve(view.resolve);
                                }

                                obj.views[name] = viewObj;
                            });
                        }


                        $stateProvider.state(path, obj);
                    });
                }
            };
            factory.$get = function () {
                return {

                };
            };
            return factory;
        });

        return module;
    }
);