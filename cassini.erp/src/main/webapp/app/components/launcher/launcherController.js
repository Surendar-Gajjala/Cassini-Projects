define(['app/app.modules',
        'mobile-detect',
        'app/components/login/loginFactory',
        'app/components/common/commonFactory',
        'app/components/admin/security/session/sessionFactory',
        'app/components/admin/security/authorizationFactory'
    ],
    function ($app, MobileDetect) {
        $app.controller('LauncherController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'loginFactory', 'commonFactory', 'sessionFactory', 'authorizationFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          loginFactory, commonFactory, sessionFactory, authorizationFactory) {

                    loginFactory.current().then (
                        function(session) {
                            if(session == null || session == "") {
                                $state.go('login');
                            }
                            else {
                                $app.session = session;
                                $app.login = session.login;

                                authorizationFactory.initialize(session);
                                $app.authorizationFactory = authorizationFactory;

                                commonFactory.initialize();

                                $rootScope.$broadcast("app.loggedIn", {});

                                var md = new MobileDetect(window.navigator.userAgent);

                                if(md.tablet() != null) {
                                    console.log("Switching to tablet app...");
                                    $state.go('mobile.tablet');
                                }
                                else if(md.phone() != null) {
                                    console.log("Switching to phone app...");
                                    $state.go('mobile.phone');
                                }
                                else {
                                    console.log("Switching to web app...");
                                    $state.go('app.home', {}, { reload: true });
                                }
                            }
                        }
                    );
                }
            ]
        );
    }
);