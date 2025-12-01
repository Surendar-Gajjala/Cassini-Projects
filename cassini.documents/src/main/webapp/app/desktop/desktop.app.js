define([
        'app/desktop/desktop.routes',
        'app/desktop/desktop.root',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/common.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogs.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/login.module',
        'app/shared/services/services.module',
        'app/desktop/modules/main/main.module',
        'app/desktop/modules/home/home.module',
        'app/desktop/modules/shared/shared.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module'
        //'app/desktop/modules/documents/document.module'

    ],

    function (routeConfigs) {

        var module = angular.module('app', [
                'app.root',
                'app.common',
                'app.dialogs',
                'app.login',
                'dm.services',
                'dm.main',
                'dm.home',
                'dm.shared',
                'app.admin'
                //'dm.document'
            ]
        );

        module.factory('urlEncoderInterceptor', ['$location',function($location) {
            return {
                request: function(config) {
                    config.url = encodeURI(config.url);
                    return config;
                }
            };
        }]);

        module.config(configFunc);

        function configFunc($stateProvider, $urlRouterProvider, $locationProvider,
                            $controllerProvider, $compileProvider, $filterProvider,
                            $provide, $logProvider, $httpProvider, dependencyProvider, $translateProvider) {

            module.controller = $controllerProvider.register;
            module.directive = $compileProvider.directive;
            module.filter = $filterProvider.register;
            module.factory = $provide.factory;
            module.service = $provide.service;
            module.stateProvider = $stateProvider;

            $httpProvider.interceptors.push('urlEncoderInterceptor');

            $translateProvider
                .addInterpolation('$translateMessageFormatInterpolation')
                .preferredLanguage('en')
                .useCookieStorage()
                .fallbackLanguage('en')//if any KEY will not find in json language files finally it checks in English.
                .useStaticFilesLoader({
                    prefix: 'app/desktop/modules/i18n/',
                    suffix: '.json'
                })
                //.useMessageFormatInterpolation()
                .useSanitizeValueStrategy('sanitize');

            angular.forEach(routeConfigs, function (config) {
                dependencyProvider.configRoutes($urlRouterProvider, $stateProvider, config);
            });

        }

        module.run(
            function (editableOptions, editableThemes) {
                editableOptions.theme = 'bs3'; // bootstrap3 theme. Can be also 'bs2', 'default'
                editableThemes.bs3.inputClass = 'input-sm';
                editableThemes.bs3.buttonsClass = 'btn-sm';
            }
        );

        return module;
    }
);