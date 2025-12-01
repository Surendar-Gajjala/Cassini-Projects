define(
    [
        'app/desktop/desktop.root'
    ],

    function() {
        var module = angular.module('tm.report',
            [
                'app.root',
                'tm.services'
            ]
        );

        module.config(configFunc);

        function configFunc($stateProvider, $urlRouterProvider, $locationProvider,
                            $controllerProvider, $compileProvider, $filterProvider,
                            $provide, $logProvider, $httpProvider, dependencyProvider) {

            module.controller = $controllerProvider.register;
            module.directive = $compileProvider.directive;
            module.filter = $filterProvider.register;
            module.factory = $provide.factory;
            module.service = $provide.service;
            module.stateProvider = $stateProvider;
        }

        return module;
    }
);