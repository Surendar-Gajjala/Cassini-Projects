define([
        'app/phone/phone.routes',
        'app/phone/phone.root',
        'app/phone/modules/main/main.module',
        'app/phone/modules/home/home.module',
        'app/phone/modules/person/person.module',
        'app/phone/modules/dept/dept.module',
        'app/phone/modules/task/task.module',
        'app/phone/modules/accomm/accomm.module',
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/phone/modules/login/login.module'
    ],

    function (routeConfigs) {

        var module = angular.module('app', [
                'app.root',
                'app.login',
                'tm.main',
                'tm.home',
                'tm.services',
                'tm.person',
                'tm.dept',
                'tm.task',
                'tm.accomm'
            ]
        );

        module.config(configFunc);

        function configFunc($stateProvider, $urlRouterProvider, $locationProvider,
                            $controllerProvider, $compileProvider, $filterProvider,
                            $provide, $logProvider, $httpProvider, dependencyProvider,
                            $mdThemingProvider) {

            module.controller = $controllerProvider.register;
            module.directive = $compileProvider.directive;
            module.filter = $filterProvider.register;
            module.factory = $provide.factory;
            module.service = $provide.service;
            module.stateProvider = $stateProvider;

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

            angular.forEach(routeConfigs, function(config) {
                dependencyProvider.configRoutes($urlRouterProvider, $stateProvider, config);
            });

        }
        return module;
    }
);