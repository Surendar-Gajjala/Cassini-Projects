define(
    [
        'app/desktop/modules/main/main.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/main/sidePanelsController',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application',
        'app/desktop/modules/shared/comments/commentsButtonDirective',
        'app/desktop/modules/shared/comments/commentsBtnDirective'
    ],
    function (module, moment) {
        module.controller('MainController', MainController);

        function MainController($scope, $rootScope, $timeout, $interval, $state, $sce, $cookies,
                                $application, LoginService, DialogService, CommonService) {
            $rootScope.viewInfo = {
                icon: 'fa-home',
                title: 'Home',
                description: "",
                showDetails: true
            };

            moment.tz.setDefault("Asia/Kolkata");
            window.moment = moment;

            var vm = this;

            vm.viewInfo = $rootScope.viewInfo;

            vm.comments = {
                show: false,
                objectType: null,
                objectId: null
            };


            $rootScope.notification = {
                class: 'fa-check',
                type: "alert-success",
                message: ""
            };

            vm.user = {
                name: ""
            };

            var sessionCheckPromise = null;
            var sessionCheckErrorCount = 0;


            vm.logout = logout;
            vm.feedback = feedback;
            vm.showProfile = showProfile;

            function checkSession() {
                window.$application.session = null;
                window.$application.login = null;
                window.$application.foldersData = null;

                LoginService.current().then(
                    function (session) {
                        if (session == null || session == "") {
                            $state.go('login');
                        }
                        else {
                            hidePreloader();

                            window.$application.session = session;
                            window.$application.login = session.login;
                            vm.user = session.login;


                            CommonService.initialize();

                            if ($application.homeLoaded == false) {
                                $state.go('app.home');
                            }
                        }
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function feedback() {
                $state.go('app.help.feedback');
            }

            function showProfile(login) {
                $state.go('app.admin.logindetails', {loginId: login.id});
            }

            function logout() {
                DialogService.confirmLogout(function (yes) {
                    if (yes == true) {
                        LoginService.logout().then(
                            function (success) {
                                $interval.cancel(sessionCheckPromise);
                                $rootScope.$broadcast("app..notifications.logout");
                                $state.go('login', {}, {reload: true});
                            },

                            function (error) {
                                $interval.cancel(sessionCheckPromise);
                                console.error(error);
                            }
                        );
                    }
                })
            }

            $rootScope.closeNotification = function () {
                hideNotificationPanel();
                $rootScope.notification.type = null;
                $rootScope.notification.message = null;
            };


            $rootScope.showNotification = function (message, type) {
                $rootScope.notification.type = type;
                $rootScope.notification.message = message;
                showNotificationPanel();
            };

            $rootScope.showSuccessMessage = function (message) {
                $rootScope.notification.class = 'fa-check';
                $rootScope.showNotification(message, 'alert-success');
                $timeout(function () {
                    $rootScope.closeNotification();
                }, 5000);
            };

            $rootScope.showErrorMessage = function (message) {
                $rootScope.notification.class = 'fa-ban';
                $rootScope.showNotification(message, 'alert-danger');
                $timeout(function () {
                    $rootScope.closeNotification();
                }, 5000);
            };

            $rootScope.showWarningMessage = function (message) {
                $rootScope.notification.class = 'fa-warning';
                $rootScope.showNotification(message, 'alert-warning');
                $timeout(function () {
                    $rootScope.closeNotification();
                }, 5000);
            };

            $rootScope.showInfoMessage = function (message) {
                $rootScope.notification.class = 'fa-info-circle';
                $rootScope.showNotification(message, 'alert-info');
                $timeout(function () {
                    $rootScope.closeNotification();
                }, 5000);
            };

            $rootScope.showBusyIndicator = function () {
                $('#busy-indicator').show();
            };

            $rootScope.hideBusyIndicator = function () {
                $('#busy-indicator').hide();
            };

            $rootScope.showComments = function (objectType, objectId) {
                vm.comments.show = true;
                vm.comments.objectType = objectType;
                vm.comments.objectId = objectId;
            };

            function initSessionCheck() {
                sessionCheckPromise = $interval(function () {
                    LoginService.current().then(
                        function (session) {
                            sessionCheckErrorCount = 0;
                            if (session == null || session == "") {
                                if (sessionCheckPromise != null) {
                                    $interval.cancel(sessionCheckPromise);
                                }
                                $state.go('login', {expired: true});
                            }
                        },
                        function (error) {
                            sessionCheckErrorCount++;
                            if (sessionCheckErrorCount == 3 && sessionCheckPromise != null) {
                                $interval.cancel(sessionCheckPromise);
                                $state.go('login', {expired: true});
                            }
                        }
                    );
                }, 10 * 60 * 1000);
            }

            var appNotification = null;

            function initNotificationPanel() {
                appNotification = $('#appNotification');
                $(window).resize(positionNotificationPanel);

                $(document).on('keydown', function (evt) {
                    if (evt.keyCode == 27) {
                        hideNotificationPanel();
                    }
                });
            }

            function showNotificationPanel() {
                positionNotificationPanel();
                appNotification.show();
                appNotification.removeClass('zoomOut');
                appNotification.addClass('zoomIn');
            }

            function positionNotificationPanel() {
                var pos = $('#headerbar').position();
                if (pos != null) {
                    var height = $('#headerbar').outerHeight();
                    var width = $('#workspace').outerWidth();

                    var left = pos.left;
                    var top = pos.top + height;
                    appNotification.css({top: 0, left: left, width: width, height: 50, position: 'absolute'});
                }
            }

            function hideNotificationPanel() {
                positionNotificationPanel();
                appNotification.removeClass('zoomIn');
                appNotification.addClass('zoomOut');
                appNotification.hide();
            }


            (function () {
                if (window.$application == null || window.$application == undefined) {
                    window.$application = $application;
                }
                $scope.$on('$viewContentLoaded', function () {
                    initNotificationPanel();

                    $rootScope.$on('$stateChangeStart',
                        function (event, toState, toParams, fromState, fromParams) {
                            vm.comments.show = false;
                            vm.viewInfo.description = "";

                            hideNotificationPanel();
                            $rootScope.hideBusyIndicator();

                            $timeout(function () {
                                positionNotificationPanel();
                            }, 500);
                            var height = $(window).height();
                            $('#contentpanel').height(height - 80);
                        }
                    );

                    initSessionCheck();
                });
                checkSession();
            })();
        }

        module.directive('fitcontent', ['$compile', '$timeout', '$rootScope',
                function ($compile, $timeout, $rootScope) {
                    return {
                        restrict: 'A',
                        link: function ($scope, elm, attr) {
                            $(window).resize(function () {
                                adjustHeight()
                            });


                            function adjustHeight() {
                                var hasToolbar = $(elm).find('.view-toolbar').length > 0;

                                var tabContent = $(elm).find('.tab-content').length > 0;
                                var adminRightSideView = $(elm).find("#admin-rightView").length > 0;
                                var barHeight = $('#admin-rightView-bar').outerHeight();
                                var detailsFooter = $(elm).find("#detailsFooter").length > 0;
                                //var detailsHeader = $(elm).find('#viewTitleContainer').outerHeight();

                                var height = $(window).height();
                                $("#contentpanel").height(height - 80);
                                height = $("#contentpanel").outerHeight();
                                if ($rootScope.viewInfo.showDetails) {
                                    $("#contentpanel").height(height - 60);
                                }
                                height = $("#contentpanel").outerHeight();
                                //$(elm).height(height-5);
                                if (hasToolbar) {
                                    if (detailsFooter) {
                                        $(elm).find('.view-content').height(height - 92);
                                    } else {
                                        $(elm).find('.view-content').height(height - 52);
                                    }

                                }
                                else {
                                    if (detailsFooter) {
                                        $(elm).find('.view-content').height(height - 42);
                                    } else {
                                        $(elm).find('.view-content').height(height - 2);
                                        if (adminRightSideView) {
                                            if (barHeight != null) {
                                                $("#admin-rightView").height(height - barHeight);
                                            } else {
                                                $("#admin-rightView").height(height - 30);
                                            }
                                        }
                                    }
                                }

                                if (tabContent) {

                                    var height1 = $("#contentpanel").height();
                                    if (detailsFooter) {
                                        $('.tab-content').height(height1 - 155);
                                        $('.tab-pane').height(height1 - 155);
                                    } else {
                                        $('.tab-content').height(height1 - 115);
                                        $('.tab-pane').height(height1 - 115);

                                    }
                                }
                            }

                            $timeout(function () {
                                adjustHeight();
                            });
                        }
                    };
                }
            ]
        );

        module.directive('pdmcontentpanel', ['$compile', '$timeout',
                function ($compile, $timeout) {
                    return {
                        restrict: 'A',
                        link: function ($scope, elm, attr) {
                            $(window).resize(function () {
                                adjustContentPanelHeight()
                            });

                            function adjustContentPanelHeight() {
                                var height = $(window).height();
                                $(elm).height(height - 80);
                            }

                            $timeout(function () {
                                adjustContentPanelHeight();
                            }, 1000);

                            adjustContentPanelHeight();
                        }
                    };
                }
            ]
        );

    }
);
