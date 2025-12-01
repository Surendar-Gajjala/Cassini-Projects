define([
        'app/desktop/desktop.routes',
        'app/desktop/desktop.root',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/common.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogs.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/login.module',
        'app/desktop/modules/main/main.module',
        'app/desktop/modules/home/home.module',
        'app/desktop/modules/dashboard/dashboard.module',
        'app/desktop/modules/complaint/complaint.module',
        'app/desktop/modules/users/users.module',
        'app/desktop/modules/settings/settings.module',
        'app/desktop/modules/irsteLogin/irsteLogin.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/help/help.module',
        'app/shared/services/services.module'
    ],

    function (routeConfigs) {

        var module = angular.module('app', [
                'app.root',
                'app.common',
                'app.dialogs',
                'app.login',
                'app.main',
                'app.home',
                'app.dashboard',
                'app.complaint',
                'app.users',
                'app.settings',
                'irste.login',
                'app.admin',
                'app.help',
                'app.services'
            ]
        );

        module.factory('urlEncoderInterceptor', ['$location', function ($location) {
            return {
                request: function (config) {
                    config.url = encodeURI(config.url);
                    return config;
                }
            };
        }]);

        module.config(configFunc);

        function configFunc($stateProvider, $urlRouterProvider, $locationProvider,
                            $controllerProvider, $compileProvider, $filterProvider,
                            $provide, $logProvider, $httpProvider, dependencyProvider,
                            $translateProvider) {

            module.controller = $controllerProvider.register;
            module.directive = $compileProvider.directive;
            module.filter = $filterProvider.register;
            module.factory = $provide.factory;
            module.service = $provide.service;
            module.stateProvider = $stateProvider;

            $httpProvider.interceptors.push('urlEncoderInterceptor');

            //$locationProvider.html5Mode(true);

            $translateProvider
                .addInterpolation('$translateMessageFormatInterpolation')
                //.determinePreferredLanguage()//try to find out preferred language from system or browser
                //.preferredLanguage(window.navigator.language || window.navigator.userLanguage)//to get language option from browser
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

        module.run(
            function ($rootScope) {
                $rootScope.$off = function (name, listener) {
                    var namedListeners = this.$$listeners[name];
                    if (namedListeners) {
                        // Loop through the array of named listeners and remove them from the array.
                        for (var i = 0; i < namedListeners.length; i++) {
                            if (namedListeners[i] === listener) {
                                return namedListeners.splice(i, 1);
                            }
                        }
                    }
                }
            }
        );

        module.filter('unsafe', function ($sce) {
            return $sce.trustAsHtml;
        });

        return module;
    }
);