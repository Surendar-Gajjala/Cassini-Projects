define([
        'app/app.phone.routes',
        'app/shared/services/dependencyResolverFor'
    ],

    function (config, dependencyResolverFor) {
        var app = angular.module('app', [
                'ngMaterial',
                'ngAnimate',
                'ngMdIcons',
                'ngCookies',
                'ui.router',
                'door3.css'
            ]
        );

        app.config(
            [
                '$stateProvider',
                '$urlRouterProvider',
                '$locationProvider',
                '$controllerProvider',
                '$compileProvider',
                '$filterProvider',
                '$provide',
                '$logProvider',
                '$httpProvider',
                '$mdThemingProvider',

                function ($stateProvider, $urlRouterProvider, $locationProvider,
                          $controllerProvider, $compileProvider, $filterProvider,
                          $provide, $logProvider, $httpProvider,
                          $mdThemingProvider) {

                    app.controller = $controllerProvider.register;
                    app.directive = $compileProvider.directive;
                    app.filter = $filterProvider.register;
                    app.factory = $provide.factory;
                    app.service = $provide.service;
                    app.stateProvider = $stateProvider;

                    var customBlueMap = $mdThemingProvider.extendPalette('light-blue', {
                        'contrastDefaultColor': 'light',
                        'contrastDarkColors': ['50'],
                        '50': 'ffffff'
                    });
                    $mdThemingProvider.definePalette('customBlue', customBlueMap);
                    $mdThemingProvider.theme('default')
                        .primaryPalette('customBlue', {
                            'default': '500',
                            'hue-1': '50'
                        })
                        .accentPalette('pink');
                    $mdThemingProvider.theme('input', 'default')
                        .primaryPalette('grey');


                    // For any unmatched url, send to /
                    $urlRouterProvider.otherwise(config.otherwise);

                    if(config.routes !== undefined) {
                        angular.forEach(config.routes, function(route, path) {
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
                                obj.resolve = dependencyResolverFor(route.resolve);
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
                                        viewObj.resolve = dependencyResolverFor(view.resolve);
                                    }

                                    obj.views[name] = viewObj;
                                });
                            }


                            $stateProvider.state(path, obj);
                        });
                    }
                }
            ]
        );
        app.backgroundLoaded = false;
        app.homeLoaded = false;
        app.session = {
            apiKey: null
        };
        app.login = {
            person: {}
        };
        app.authorizationFactory = null;

        return app;
    }
);