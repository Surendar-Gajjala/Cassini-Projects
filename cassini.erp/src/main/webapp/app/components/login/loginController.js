define(['app/app.modules',
        'mobile-detect',
        'app/components/login/loginFactory',
        'app/components/admin/security/authorizationFactory',
        'app/components/common/commonFactory'
    ],
    function(app, MobileDetect) {
        app.controller('LoginController',
            [
                '$scope', '$rootScope', '$timeout',
                '$interval', '$state', '$cookies',
                'loginFactory', 'authorizationFactory','commonFactory',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies,
                         loginFactory, authorizationFactory,commonFactory) {

                    initPreloader();

                    function getRandomInt(min, max) {
                        return Math.floor(Math.random() * (max - min + 1)) + min;
                    }

                    if(app.backgroundLoaded == false) {
                        var bgImages = [
                            '1.jpg', '2.jpg', '3.jpg', '4.png', '5.jpg', '6.jpg', '7.jpg', '8.jpg', '9.jpg', '10.jpg',
                            '11.jpg', '12.jpg', '13.png', '14.jpg', '16.png', '17.jpg', '18.png', '19.jpg', '20.jpg',
                            '21.jpg', '22.jpg', '23.jpg', '24.jpg', '25.jpg', '26.jpg', '27.jpg', '28.jpg', '29.jpg', '30.jpg',
                            '31.jpg', '32.jpg', '33.jpg', '34.jpg', '35.jpg', '36.jpg', '37.jpg', '38.jpg', '39.jpg', '40.jpg',
                            '41.jpg', '42.jpg', '43.jpg', '44.jpg', '45.jpg', '46.jpg', '47.jpg', '48.jpg', '49.jpg', '50.jpg',
                            '51.jpg', '52.jpg', '53.jpg', '55.jpg', '56.jpg', '57.png', '58.jpg', '59.png', '60.png'
                        ];

                        var index = getRandomInt(1, bgImages.length);
                        $('html').css('background', 'url(app/assets/images/bg/' + bgImages[index - 1] + ') no-repeat 0 0 fixed');
                        //$('html').css('background-size', 'auto');
                        $('body').css('background', 'inherit');
                        //$('body').css('background-size', 'auto');


                        app.backgroundLoaded = true;
                    }

                    $scope.hasError = false;
                    $scope.errorMessage = "Incorrect login or password";
                    $scope.loggingIn = false;

                    $scope.userName = $cookies._CASSINI_USERNAME_;
                    $scope.password = null;

                    $scope.login = function() {
                        $scope.hasError = false;
                        $scope.loggingIn = true;
                        loginFactory.login($scope.userName, $scope.password).then (
                            function(session) {
                                $scope.loggingIn = false;
                                app.session = session;
                                app.login = session.login;

                                authorizationFactory.initialize(session);
                                app.authorizationFactory = authorizationFactory;

                                commonFactory.initialize();

                                $cookies._CASSINI_USERNAME_ = $scope.userName;
                                $rootScope.$broadcast("app.loggedIn", {});

                                $state.go('app.home', {}, { reload: true });

                                /*
                                var md = new MobileDetect(window.navigator.userAgent);

                                if(md.tablet() != null) {
                                    console.log("Switching to tablet app...");
                                    $state.go('mobile.tablet');
                                }
                                else {
                                    $state.go('app.home', {}, { reload: true });
                                }
                                */

                            },

                            function (error) {
                                $scope.hasError = true;
                                $scope.loggingIn = false;
                                $scope.errorMessage = error.message;
                            }
                        );

                    };
                }
            ]
        );
    }
);