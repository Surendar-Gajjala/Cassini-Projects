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
        'app/desktop/modules/classification/classification.module',
        'app/desktop/modules/item/item.module',
        'app/desktop/modules/bom/bom.module',
        'app/desktop/modules/storage/storage.module',
        'app/desktop/modules/inventory/inventory.module',
        'app/desktop/modules/gatePass/gatePass.module',
        'app/desktop/modules/inward/inward.module',
        'app/desktop/modules/request/request.module',
        'app/desktop/modules/issue/issue.module',
        'app/desktop/modules/dispatch/dispatch.module',
        'app/desktop/modules/settings/settings.module',
        'app/desktop/modules/shared/shared.module',
        'app/desktop/modules/planning/planning.module',
        'app/desktop/modules/procurement/procurement.module',
        'app/desktop/modules/reports/reports.module',
        'app/desktop/modules/summary/summary.module'

    ],

    function (routeConfigs) {

        var module = angular.module('app', [
                'app.root',
                'app.common',
                'app.dialogs',
                'app.login',
                'app.admin',
                'app.help',
                'drdo.services',
                'drdo.main',
                'drdo.home',
                'drdo.classification',
                'drdo.item',
                'drdo.bom',
                'drdo.storage',
                'drdo.inventory',
                'drdo.gatePass',
                'drdo.inward',
                'drdo.request',
                'drdo.issue',
                'drdo.issue',
                'drdo.dispatch',
                'drdo.settings',
                'drdo.shared',
                'drdo.procurement',
                'drdo.planning',
                'drdo.reports',
                'drdo.summary'
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
                    files: [{
                        prefix: 'app/assets/bower_components/cassini-platform/app/desktop/modules/i18n/',
                        suffix: '.json'
                    }, {
                        prefix: 'app/desktop/modules/i18n/',
                        suffix: '.json'
                    }]
                })
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
)
;