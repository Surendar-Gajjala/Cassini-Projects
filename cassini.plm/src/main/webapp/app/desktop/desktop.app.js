define([
        'app/desktop/desktop.routes',
        'app/desktop/desktop.root',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/common.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogs.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/login.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/help/help.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/about/about.module',
        'app/shared/services/services.module',
        'app/desktop/modules/main/main.module',
        'app/desktop/modules/home/home.module',
        'app/desktop/modules/item/item.module',
        'app/desktop/modules/change/change.module',
        'app/desktop/modules/workflow/workflow.module',
        'app/desktop/modules/classification/classification.module',
        'app/desktop/modules/mfr/mfr.module',
        'app/desktop/modules/mfr/mfrparts/mfrparts.module',
        'app/desktop/modules/settings/settings.module',
        'app/desktop/modules/shared/shared.module',
        'app/desktop/modules/sharing/sharing.module',
        'app/desktop/modules/pdm/pdm.module',
        'app/desktop/modules/pm/pm.module',
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/req/req.module',
        'app/desktop/modules/pm/project/activity/activity.module',
        'app/desktop/modules/pm/project/task/task.module',
        'app/desktop/modules/template/template.module',
        'app/desktop/modules/exim/exim.module',
        'app/desktop/modules/activity/activity.module',
        'app/desktop/modules/supplier/supplier.module',
        'app/desktop/modules/customer/customer.module',
        'app/desktop/modules/dashboard/dashboard.module',
        'app/desktop/modules/template/templateActivity/templateActivity.module',
        'app/desktop/modules/template/templateTask/templateTask.module',
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/mro/mro.module',
        'app/desktop/modules/req/reqdocs/requirement/requirement.module',
        'app/desktop/modules/compliance/compliance.module',
        'app/desktop/modules/npr/npr.module',
        'app/desktop/modules/admin/admin.module',
        'app/desktop/modules/customObject/customObject.module',
        'app/desktop/modules/reqTemplate/reqDocTemplate.module',
        'app/desktop/modules/reqTemplate/requirementTemplate/requirementTemplate.module',
        'app/desktop/modules/dm/dm.module'

    ],

    function (routeConfigs) {

        var module = angular.module('app', [
                'app.root',
                'app.common',
                'app.dialogs',
                'app.login',
                'app.admin',
                'app.help',
                'app.about',
                'plm.services',
                'plm.main',
                'plm.home',
                'plm.pdm',
                'plm.item',
                'plm.change',
                'plm.workflow',
                'plm.classification',
                'plm.mfr',
                'plm.mfrparts',
                'plm.settings',
                'plm.shared',
                'plm.sharing',
                'plm.pm',
                'plm.pqm',
                'plm.req',
                'plm.activity',
                'plm.template',
                'plm.task',
                'plm.exim',
                'plm.events',
                'plm.templateActivity',
                'plm.templateTask',
                'plm.supplier',
                'plm.customer',
                'plm.dashboard',
                'plm.mes',
                'plm.mro',
                'plm.requirement',
                'plm.compliance',
                'plm.npr',
                'plm.admin',
                'plm.customObject',
                'plm.reqTemplate',
                'plm.requirementTemplate',
                'plm.dm'
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
        module.filter('startsWith', function () {
            return function (array, search) {
                if (array != undefined && search != undefined) {
                    var matches = [];
                    for (var i = 0; i < array.length; i++) {
                        if (array[i].name.toUpperCase().indexOf(search.toUpperCase()) > -1) {
                            matches.push(array[i]);
                        }
                    }
                    return matches;
                }
            };
        });

        var startTimer = null;

        module.factory('SessionInterceptor', ['$rootScope', function ($rootScope) {
            var count = 0;
            return {
                request: function (config) {
                    if ($rootScope.sessionStatus) {
                        if ($rootScope.sessionTime == null || $rootScope.sessionTime == 0
                            || $rootScope.sessionTime == undefined || $rootScope.sessionTime == "") {
                            $rootScope.sessionTime = 30;
                        }
                        if (count == 0) {
                            var timer = $rootScope.sessionTime * 60, minutes, seconds;
                            startTimer = setInterval(function () {
                                minutes = parseInt(timer / 60, 10);
                                seconds = parseInt(timer % 60, 10);
                                minutes = minutes < 10 ? "0" + minutes : minutes;
                                seconds = seconds < 10 ? "0" + seconds : seconds;
                                //console.log(timer);
                                if (--timer < 0) {
                                    clearInterval(startTimer);
                                    $rootScope.sessionTimeout();
                                }
                            }, 1000);
                            count++;
                        } else {
                            clearInterval(startTimer);
                            var timer = $rootScope.sessionTime * 60, minutes, seconds;
                            startTimer = setInterval(function () {
                                minutes = parseInt(timer / 60, 10);
                                seconds = parseInt(timer % 60, 10);
                                minutes = minutes < 10 ? "0" + minutes : minutes;
                                seconds = seconds < 10 ? "0" + seconds : seconds;
                                //console.log(timer);
                                if (--timer < 0) {
                                    clearInterval(startTimer);
                                    $rootScope.sessionTimeout();
                                }
                            }, 1000);
                        }
                    } else {
                        clearInterval(startTimer);
                    }
                    return config;
                },
            };
        }]);

        module.factory('AuthInterceptor', function ($window, $q, $injector) {
            var url = "api/security/logout";
            return {
                request: function (config) {
                    config.headers = config.headers || {};
                    var token = $window.localStorage.getItem('token');
                    var refreshToken = $window.localStorage.getItem('refreshToken');
                    if (token != "" && token != null && token != undefined) {
                        config.headers['Authorization'] = 'Bearer ' + token;
                    }
                    if (refreshToken != "" && refreshToken != null && refreshToken != undefined) {
                        config.headers['X-Refresh-Token'] = refreshToken;
                    }
                    return config || $q.when(config);
                },
                response: function (response) {
                    if (response.config.url.startsWith('api/')) {
                        var headers = response.headers();
                        var jwtToken = headers['x-jwt-token'];
                        var refreshToken = headers['x-refresh-token'];
                        if (jwtToken != "" && jwtToken != null && jwtToken != undefined) {
                            $window.localStorage.setItem('token', jwtToken);
                        }
                        if (refreshToken != "" && refreshToken != null && refreshToken != undefined) {
                            $window.localStorage.setItem('refreshToken', refreshToken);
                        }
                        if (response.config.url != 'api/security/logout' && response.config.url != 'api/security/checkportal'
                            && response.config.url != 'api/app/details' && !response.config.url.includes('api/security/login/validate')
                            && response.config.url != 'api/security/login/resetpwd/verify' && response.config.url != 'api/security/login/newpassword'
                            && response.config.url != 'api/security/language/de' && response.config.url != 'api/security/language/en'
                            && !response.config.url.includes('api/security/session/current') && response.config.url != 'api/core/currencies'
                            && response.config.url != 'api/security/session/isactive' && response.config.url != 'api/common/persons/preferences'
                            && response.config.url != 'api/common/persons/getSessionTime' && response.config.url != 'api/common/persons/getPreferenceByContext/SYSTEM'
                            && response.config.url != 'api/core/currencies' && response.config.url != 'api/security/login/resetpwd') {
                            if (jwtToken === null || jwtToken === undefined) {
                                $window.localStorage.removeItem('token');
                                $window.localStorage.removeItem('refreshToken');
                                var dfd = $q.defer();
                                dfd.resolve(response);
                                var $state = $injector.get('$state');
                                var $http = $injector.get('$http');
                                var config = {};
                                $http.get(url, config).success(function (response) {
                                    dfd.resolve(response);
                                    $state.go('login', {}, {reload: true});
                                }).error(function (response) {
                                    dfd.reject(response);
                                });
                            }
                        }

                    }

                    return response || $q.when(response);
                },
                responseError: function (rejection) {
                    if (rejection.status === 401) {
                        var $http = $injector.get('$http');
                        var $state = $injector.get('$state');
                        var dfd = $q.defer();
                        var config = {};
                        rejection.config.headers['Authorization'] = undefined;
                        rejection.config.headers['X-Refresh-Token'] = undefined;
                        $window.localStorage.removeItem('token');
                        $window.localStorage.removeItem('refreshToken');
                        $http.get(url, config).success(function (response) {
                            dfd.resolve(response);
                            $state.go('login', {}, {reload: true});
                        }).error(function (response) {
                            dfd.reject(response);
                        });
                        return dfd.promise;
                    }
                    return $q.reject(rejection);
                }
            };
        });


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
            module.urlRouterProvider = $urlRouterProvider;
            module.dependencyProvider = dependencyProvider;

            $httpProvider.interceptors.push('urlEncoderInterceptor');
            $httpProvider.interceptors.push('SessionInterceptor');
            $httpProvider.interceptors.push('AuthInterceptor');

            //$locationProvider.html5Mode(true);

            //initialize get if not there
            if (!$httpProvider.defaults.headers.get) {
                $httpProvider.defaults.headers.get = {};
            }

            // Answer edited to include suggestions from comments
            // because previous version of code introduced browser-related errors

            //disable IE ajax request caching
            $httpProvider.defaults.headers.get['If-Modified-Since'] = 'Mon, 26 Jul 1997 05:00:00 GMT';
            // extra
            $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
            $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';

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

        function syncTableWidth(e) {
            var parent = e.currentTarget;

            $("table").filter(function () {
                return $(this).attr("id") != $(parent).attr("id")
            }).each(function () {
                //Match the width
                $("tr th", this).each(function (index) {
                    $(this).css("width", $("tr th:nth-of-type(" + (index + 1) + ")", parent).css("width"))
                });
                //Match the grip's position
                $(this).prev().find(".JCLRgrip").each(function (index) {
                    $(this).css("left", $(parent).prev().find(".JCLRgrip:nth-of-type(" + (index + 1) + ")").css("left"));
                });
            });
        }

        function onSlide(e) {
            var columns = $(e.currentTarget).find("td");
            var ranges = [], total = 0, i, s = "Ranges: ", w;
            for (i = 0; i < columns.length; i++) {
                w = columns.eq(i).width() - 10 - (i == 0 ? 1 : 0);
                ranges.push(w);
                total += w;
            }
            for (i = 0; i < columns.length; i++) {
                ranges[i] = 100 * ranges[i] / total;
                carriage = ranges[i] - w
                s += " " + Math.round(ranges[i]) + "%,";
            }
            s = s.slice(0, -1);
            //$("#text").html(s);
        }

        module.directive('colResizeable', function ($timeout) {
            return {
                restrict: 'A',
                link: function (scope, elem) {
                    if (elem[0].id == "specItemsTable") {
                        $(elem).colResizable({
                            liveDrag: true,
                            fixed: true,
                            resizeMode: 'overflow',
                            gripInnerHtml: "<div class='grip'></div>",
                            draggingClass: "dragging",
                            postbackSafe: false,
                            partialRefresh: true,
                            disabledColumns: [0, 1],
                            removePadding: true,
                            flush: true,
                            onDrag: syncTableWidth,
                            onResize: onSlide,

                        });
                        scope.$on('reInitializeColResizable', function (event, data) {
                            $(elem).colResizable({
                                liveDrag: true,
                                fixed: true,
                                resizeMode: 'overflow',
                                gripInnerHtml: "<div class='grip'></div>",
                                draggingClass: "dragging",
                                postbackSafe: false,
                                partialRefresh: true,
                                disabledColumns: [0, 1],
                                removePadding: true,
                                flush: true,
                                onDrag: syncTableWidth,
                                onResize: onSlide,

                            });
                        })
                    }

                    else if (elem[0].id == "specifications") {
                        $(elem).colResizable({
                            liveDrag: true,
                            fixed: true,
                            resizeMode: 'overflow',
                            gripInnerHtml: "<div class='grip'></div>",
                            draggingClass: "dragging",
                            //postbackSafe: true,
                            partialRefresh: true,
                            disabledColumns: [0],
                            removePadding: true,
                            flush: true,
                            onDrag: syncTableWidth,
                            onResize: onSlide,

                        });
                        scope.$on('reInitializeColResizable', function (event, data) {
                            $(elem).colResizable({
                                liveDrag: true,
                                fixed: true,
                                resizeMode: 'overflow',
                                gripInnerHtml: "<div class='grip'></div>",
                                draggingClass: "dragging",
                                //postbackSafe: true,
                                disabledColumns: [0],
                                partialRefresh: true,
                                removePadding: true,
                                flush: true,
                                onDrag: syncTableWidth,
                                onResize: onSlide,

                            });
                        })
                    }

                    else {
                        $(elem).colResizable({
                            liveDrag: true,
                            fixed: true,
                            resizeMode: 'overflow',
                            gripInnerHtml: "<div class='grip'></div>",
                            draggingClass: "dragging",
                            //postbackSafe: true,
                            partialRefresh: true,
                            removePadding: true,
                            flush: true,
                            onDrag: syncTableWidth,
                            onResize: onSlide,

                        });
                        scope.$on('reInitializeColResizable', function (event, data) {
                            $(elem).colResizable({
                                liveDrag: true,
                                fixed: true,
                                resizeMode: 'overflow',
                                gripInnerHtml: "<div class='grip'></div>",
                                draggingClass: "dragging",
                                //postbackSafe: true,
                                partialRefresh: true,
                                removePadding: true,
                                flush: true,
                                onDrag: syncTableWidth,
                                onResize: onSlide,

                            });
                        })
                    }

                }
            };
        });

        return module;
    }
);