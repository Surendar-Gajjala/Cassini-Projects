define(
    [
        'app/app.root'
    ],

    function () {
        var module = angular.module('is.tm',
            [
                'app.root'
            ]
        );

        module.config(
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
                'dependencyProvider',

                function ($stateProvider, $urlRouterProvider, $locationProvider,
                          $controllerProvider, $compileProvider, $filterProvider,
                          $provide, $logProvider, $httpProvider, dependencyProvider) {

                    module.controller = $controllerProvider.register;
                    module.directive = $compileProvider.directive;
                    module.filter = $filterProvider.register;
                    module.factory = $provide.factory;
                    module.service = $provide.service;
                    module.stateProvider = $stateProvider;
                }
            ]
        );

        return module;
    }
);