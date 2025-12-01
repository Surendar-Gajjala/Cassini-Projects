define(
    [
        'app/desktop/desktop.root',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogs.module'
    ],

    function() {
        var module = angular.module('app.login',
            [
                'app.root',
                'app.dialogs'
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