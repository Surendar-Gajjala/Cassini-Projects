define([
        'app/app.routes',
        'app/shared/services/dependencyResolverFor',
        'app/assets/libs/bootstrap/bootstrap',
        'app/assets/libs/angular-calendar/fullcalendar',
        'app/assets/libs/angular-calendar/calendar'
    ],

    function (config, dependencyResolverFor) {

        var app = angular.module('app', [
                'ngCookies',
                'ui.router',
                'ui.bootstrap',
                'treeGrid',
                'ngStorage',
                'ngAnimate',
                'ngSanitize',
                'ui.select',
                'xeditable',
                'door3.css',
                'daterangepicker',
                'treasure-overlay-spinner',
                'ui.calendar',
                'uiGmapgoogle-maps',
                'ui.ace',
                'angular-flot',
                'angular.morris-chart',
                'angularValidator'
            ]
        );
        /*
        app.factory('sessionValidationInterceptor', ['$q', '$injector', '$rootScope', function($q, $injector, $rootScope) {
            var sessionValidationInterceptor = {
                request: function(config) {
                    if(config.url.substr(0, 'api') == 'api') {
                        console.log(config.url);
                    }

                    if(config.url.substr(0, 'api') == 'api' &&
                        (app.session == null || app.session == undefined || app.session.apiKey == null)) {
                        console.log(config);
                        return $q.reject("APIInterceptor")
                    }
                    return config;
                },
                responseError: function(response) {
                    // Session has expired
                    if (response.status == 401) {
                        console.log("Unauthorized access!");
                        $rootScope.$broadcast('UnauthorizedAccess', response.data);
                    }
                    return $q.reject(response);
                }
            };
            return sessionValidationInterceptor;
        }]);
        */

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

                function ($stateProvider, $urlRouterProvider, $locationProvider,
                          $controllerProvider, $compileProvider, $filterProvider,
                          $provide, $logProvider, $httpProvider) {

                    //$logProvider.debugEnabled(true);
                    //$httpProvider.interceptors.push('sessionValidationInterceptor');

                    app.controller = $controllerProvider.register;
                    app.directive = $compileProvider.directive;
                    app.filter = $filterProvider.register;
                    app.factory = $provide.factory;
                    app.service = $provide.service;
                    app.stateProvider = $stateProvider;

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

        app.directive('ngEnter', function () {
            return function (scope, element, attrs) {
                element.bind("keydown keypress", function (event) {
                    if(event.which === 13) {
                        scope.$apply(function (){
                            scope.$eval(attrs.ngEnter);
                        });

                        event.preventDefault();
                    }
                });
            };
        });

        app.directive('ngEscape', function () {
            return function (scope, element, attrs) {
                element.bind("keydown keypress", function (event) {
                    if(event.which === 27) {
                        scope.$apply(function (){
                            scope.$eval(attrs.ngEscape);
                        });

                        event.preventDefault();
                    }
                });
            };
        });

        app.run(
            function(editableOptions, editableThemes) {
                editableOptions.theme = 'bs3'; // bootstrap3 theme. Can be also 'bs2', 'default'
                editableThemes.bs3.inputClass = 'input-sm';
                editableThemes.bs3.buttonsClass = 'btn-sm';
            }
        );

        app.backgroundLoaded = false;
        app.homeLoaded = false;
        app.navLoaded = false;
        app.navInited = false;
        app.uiInited = false;
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