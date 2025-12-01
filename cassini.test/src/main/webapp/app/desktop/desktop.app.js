/**
 * Created by Suresh Cassini on 03-Jul-18.
 */

define([
        'app/desktop/desktop.routes',
        'app/desktop/desktop.root',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/common.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogs.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/login.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/help/help.module',
        'app/shared/services/services.module',
        'app/desktop/modules/main/main.module',
        'app/desktop/modules/home/home.module',
        'app/desktop/modules/config/config.module',
        'app/desktop/modules/definition/definition.module',
        'app/desktop/modules/run/run.module',
        'app/desktop/modules/settings/settings.module'
    ],

    function (routeConfigs) {

        var module = angular.module('app', [
                'app.root',
                'app.common',
                'app.dialogs',
                'app.login',
                'test.services',
                'test.main',
                'test.home',
                'app.admin',
                'app.help',
                'test.definition',
                'test.config',
                'test.run',
                'test.settings'
            ]
        );

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