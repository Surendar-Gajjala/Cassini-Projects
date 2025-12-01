define(
    [
        'app/desktop/desktop.root',
        'app/shared/services/services.module'
    ],

    function() {
        var module = angular.module('tms.main',
            [
                'app.root',
                'tms.services'
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