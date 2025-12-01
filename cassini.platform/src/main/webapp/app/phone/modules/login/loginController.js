define(
    [
        'app/assets/bower_components/cassini-platform/app/phone/modules/login/login.module',
        'app/shared/services/app/application',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'
    ],
    function (module) {
        module.controller('LoginController', LoginController);

        function LoginController ($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies,
                                  $mdDialog, $application, LoginService) {
            var vm = this;

            window.$("#appview").hide();

            var bgImages = [
                'phone-bg-1.png','phone-bg-2.jpg','phone-bg-3.jpg','phone-bg-4.jpg',
                'phone-bg-5.png','phone-bg-6.jpg','phone-bg-7.jpg', 'phone-bg-8.jpg',
                'phone-bg-9.jpg','phone-bg-10.jpg','phone-bg-11.jpg', 'phone-bg-12.jpg',
                'phone-bg-13.jpg','phone-bg-14.jpg','phone-bg-15.jpg', 'phone-bg-16.jpg',
                'phone-bg-17.jpg','phone-bg-18.jpg','phone-bg-19.jpg', 'phone-bg-20.jpg',
                'phone-bg-21.jpg','phone-bg-22.jpg','phone-bg-23.jpg', 'phone-bg-24.jpg'
            ];

            var storedUser = $cookies.get('_CASSINI_USERNAME_');
            var storedPassword = $cookies.get('_CASSINI_PASSWORD_');
            var rememberMe = $cookies.get('_CASSINI_REMEMBERME_');

            vm.login = {
                username: storedUser != undefined ? storedUser : null,
                password: storedPassword != undefined ? storedPassword : null,
                remember: true
            };

            vm.onSwipeRight = onSwipeRight;
            vm.onSwipeLeft = onSwipeLeft;
            vm.bringFormToView = bringFormToView;
            vm.performLogin = performLogin;


            function getCurrentBgIndex() {
                var bgIndex = $cookies.get('_BG_INDEX_');
                if(bgIndex == null || bgIndex == undefined || bgIndex > bgImages.length-1) {
                    bgIndex = 0;
                }
                else {
                    bgIndex = parseInt(bgIndex);
                }

                return bgIndex;
            }

            function getBgIndex() {
                var bgIndex = getCurrentBgIndex();
                if(bgIndex == undefined) {
                    bgIndex = 0;
                }
                else {
                    bgIndex = bgIndex + 1;
                }
                $cookies.put('_BG_INDEX_', bgIndex);
                return bgIndex;
            }

            function setBgImage(index) {
                var image = bgImages[0];
                if(bgImages[index] != null && bgImages[index] != undefined) {
                    image = bgImages[index];
                }

                $('html').css('background-image', 'url(app/assets/bower_components/cassini-platform/images/bg/phone/' + image + ')');
                $('html').css('background-size', '100% 100%');
                $('html').css('background-repeat', 'no-repeat');
                $('body').css('background', 'inherit');
                $('body').css('background-size', '100% 100%');
                $('body').css('background-repeat', 'no-repeat');
            }
            setBgImage(getBgIndex());

            function onSwipeRight() {
                var bgIndex = getCurrentBgIndex();
                if(bgIndex == bgImages.length-1) {
                    bgIndex = 0;
                }
                else {
                    bgIndex = bgIndex + 1;
                }
                $cookies.put('_BG_INDEX_', bgIndex);
                $('html').css('background-image', 'url(app/assets/bower_components/cassini-platform/images/bg/phone/' + bgImages[bgIndex] + ')');
            }

            function onSwipeLeft() {
                var bgIndex = getCurrentBgIndex();
                if(bgIndex == 0) {
                    bgIndex = bgImages.length-1;
                }
                else {
                    bgIndex = bgIndex - 1;
                }
                $cookies.put('_BG_INDEX_', bgIndex);
                $('html').css('background-image', 'url(app/assets/bower_components/cassini-platform/images/bg/phone/' + bgImages[bgIndex] + ')');
            }

            function bringFormToView() {
                var loginFormHeight = $('#loginForm').height();
                $('#loginContainer').animate({scrollTop: $('#loginContainer').scrollTop() + $('#loginForm').position().top+150},'slow');
            }

            function showAlert(event, message) {
                // Appending dialog to document.body to cover sidenav in docs app
                // Modal dialogs should fully cover application
                // to prevent interaction outside of dialog
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#loginContainer')))
                        .clickOutsideToClose(true)
                        .title('Cassini.ERP Login')
                        .content(message)
                        .ariaLabel('Alert Dialog Demo')
                        .ok('Got it!')
                        .targetEvent(event)
                );
            }

            function performLogin(event) {
                if(vm.login != null && vm.login != undefined) {
                    LoginService.login(vm.login.username, vm.login.password).then(
                        function(session) {
                            vm.loggingIn = false;
                            window.$application.session = session;
                            window.$application.login = session.login;

                            $cookies.put('_CASSINI_USERNAME_', vm.login.username);
                            $cookies.put('_CASSINI_PASSWORD_', vm.login.password);
                            $cookies.put('_CASSINI_REMEMBERME_', vm.login.remember);

                            $rootScope.$broadcast("app.notification.login", {});

                            if(App2AndroidBridge.isPhoneAvailable()) {
                                App2AndroidBridge.setAuthentication(vm.login.username, vm.login.password);
                                setDeviceInfo(event);
                            }
                            else {
                                $state.go('app.home', {}, { reload: true });
                            }
                        },
                        function (error) {
                            vm.loggingIn = false;
                            showAlert(event, error.message);
                        }
                    )
                }
            }

            function setDeviceInfo(event) {
                App2AndroidBridge.getDeviceInfo(function(json) {
                    var device = JSON.parse(json);
                    LoginService.setMobileDevice($application.session.sessionId, device).then(
                        function(data) {
                            //showAlert(event, JSON.stringify(data));
                            $state.go('app.home', {}, { reload: true });
                        }
                    );
                });
            }

            (function() {
                if(window.$application == null || window.$application == undefined) {
                    window.$application = $application;
                }
                LoginService.current().then (
                    function(session) {
                        if(session != null && session != "") {
                            $application.session = session;
                            $application.login = session.login;
                            $state.go('app.home');
                        }
                        else {
                            $timeout(function() {
                                window.$("#loginContainer").show();
                                window.$("#appview").show();
                                if(storedUser != null && storedUser != "" &&
                                    storedPassword != null && storedPassword != "" &&
                                    ($stateParams.fromapp == null ||
                                    $stateParams.fromapp == undefined ||
                                    $stateParams.fromapp != 'true')) {
                                    performLogin(null);
                                }
                            }, 10);
                        }
                    }
                );
            })();

        }
    }
);