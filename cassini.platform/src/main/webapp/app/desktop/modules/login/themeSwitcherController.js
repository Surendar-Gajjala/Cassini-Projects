define(
    [
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/login.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'
    ],

    function (module) {
        module.controller('ThemeSwitcherController', ThemeSwitcherController);

        function ThemeSwitcherController($scope, $rootScope, $timeout, $interval, $state, $location, $application, $translate, $window, LoginService) {

            var vm = this;
            var root = document.documentElement;
            $rootScope.updatePreference = updatePreference;
            function updatePreference(preferences) {
                LoginService.updateUserPreference(preferences).then(
                    function (data) {

                    }, function (error) {

                    }
                );
            }


            function setDarkBlueTheme() {
                root.style.setProperty('--cassini-theme-dark-color', "rgb(0, 37, 63)");
                root.style.setProperty('--cassini-theme-light-color', "rgb(7, 55, 88)");
                root.style.setProperty('--cassini-theme-lighter-color', "rgb(27, 78, 113)");
                root.style.setProperty('--cassini-theme-more-lighter-color', "#157d9c");
                root.style.setProperty('--cassini-theme-widget-hover-color', "rgba(17, 116, 155, 0.5)");
            }

            function setSlateGreyTheme() {
                root.style.setProperty('--cassini-theme-dark-color', "#20262E");
                root.style.setProperty('--cassini-theme-light-color', "#2B333E");
                root.style.setProperty('--cassini-theme-lighter-color', "#383E45");
                root.style.setProperty('--cassini-theme-more-lighter-color', "#495563");
                root.style.setProperty('--cassini-theme-widget-hover-color', "#4e5157");
            }

            function updateUserPreferences(theme) {
                if ($rootScope.userPreferences != null) {
                    if ($rootScope.userPreferences.userTheme != theme) {
                        $rootScope.userPreferences.userTheme = theme;
                        $rootScope.updatePreference($rootScope.userPreferences);
                    }

                } else {

                    if ($rootScope.userPreferences != null) {
                        $rootScope.updatePreference($rootScope.userPreferences);
                    }
                    else {
                        var userPreferences = {
                            id: null,
                            login: $rootScope.loginPersonDetails.id,
                            userTheme: theme
                        };
                        $rootScope.updatePreference(userPreferences);
                    }
                }
            }

            $rootScope.switchTheme = switchTheme;
            function switchTheme(theme, mode) {
                if (mode == "switching") {
                    if (theme === 'darkblue') {
                        updateUserPreferences(theme);
                        setDarkBlueTheme();
                    }
                    else if (theme === 'slategrey') {
                        updateUserPreferences(theme);
                        setSlateGreyTheme();
                    }
                }
                else if (mode == "refresh") {

                    if (theme === 'darkblue') {
                        setDarkBlueTheme();
                    }
                    else if (theme === 'slategrey') {
                        setSlateGreyTheme();
                    }
                }

            }


            $rootScope.loadUserPreference = loadUserPreference;
            function loadUserPreference(loginId) {
                LoginService.getUserPreference(loginId).then(
                    function (data) {
                        $rootScope.userPreferences = data;

                    }
                );
            }


            (function () {
                $(document).click(function (e) {
                    if ($('#themeSwitcher').css('display') !== 'none' && !$(e.target).hasClass('theme-switcher-btn') && !$(e.target).hasClass('la-desktop') &&
                        (!$(e.target).hasClass('theme-switcher') ||
                        $(e.target).parents("#themeSwitcher").length === 1)) {
                        $('#themeSwitcher').slideUp();
                    }
                });

                //ESC key press event
                $(document).on('keyup', function (evt) {
                    if (evt.keyCode === 27) {
                        $('#themeSwitcher').slideUp();
                    }
                });
            })();
        }
    }
);