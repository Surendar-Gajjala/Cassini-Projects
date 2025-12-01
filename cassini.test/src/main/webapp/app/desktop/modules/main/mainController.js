/**
 * Created by Suresh Cassini on 03-Jul-18.
 */
define(
    [
        'app/desktop/modules/main/main.module',
        'moment',
        'app/shared/services/app/application',
        'app/assets/bower_components/cassini-platform/app/shared/filters/filters',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/groupService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/main/sidePanelsController'
    ],
    function (module, moment) {
        module.controller('MainController', MainController);

        function MainController($scope, $rootScope, $timeout, $state, $cookies, $window,
                                $application, LoginService, GroupService, DialogService, CommonService, PersonGroupService) {
            $rootScope.viewInfo = {
                icon: 'fa-home',
                title: 'Home'
            };
            window.moment = moment;
            var vm = this;
            vm.viewInfo = $rootScope.viewInfo;

            $rootScope.notification = {
                class: 'fa-check',
                type: "alert-success",
                message: ""
            };

            vm.comments = {
                show: false,
                objectType: null,
                objectId: null
            };

            vm.user = {
                name: ""
            };
            vm.logout = logout;
            vm.feedback = feedback;
            vm.showProfile = showProfile;
            vm.help = help;

            vm.addAndRemoveWidgets = addAndRemoveWidgets;
            vm.groups = [];
            vm.changePermissionsByGroup = changePermissionsByGroup;
            vm.defaultGroup = null;


            var permissionsMap = new Hashtable();

            function help() {
                $state.go('app.help.main');
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
                                $rootScope.$broadcast("app..notifications.logout");
                                $state.go('login', {}, {reload: true});
                            },

                            function (error) {
                                console.error(error);
                            }
                        );
                    }
                })
            }

            function changePermissionsByGroup(group) {
                var currentState = $state.current;
                var options = {
                    title: 'Group Change',
                    message: "Are you sure you want to change group permissions",
                    okButtonClass: 'btn-danger'
                };

                if (currentState.name != 'app.home') {
                    options.message = options.message + ' If yes,it redirects to HOME page';
                }

                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            PersonGroupService.findGroupPermissionsByGroupId(group.groupId).then(
                                function (data) {
                                    group.permissions = [];
                                    angular.forEach(data, function (grpPer) {
                                        group.permissions.push(grpPer.id.permission);
                                    });
                                    vm.defaultGroup = group;
                                    permissionsMap = new Hashtable();
                                    buildPermissionsMap(group.permissions);
                                    $state.go('app.home', {}, {reload: 'app.home'});
                                })
                        }
                    }
                )
            }

            function checkSession() {
                LoginService.current().then(
                    function (session) {
                        if (session == null || session == "") {
                            $state.go('login');
                        }
                        else {
                            buildMaps(session);
                            window.$application.session = session;
                            window.$application.login = session.login;
                            vm.user.name = session.login.person.firstName;
                            vm.user.id = session.login.id;
                            CommonService.initialize();
                            vm.showWelcomeMessage = true;
                            if ($application.homeLoaded == false) {
                                $state.go('app.home');
                            }
                        }

                    }
                );
            }

            $rootScope.hideBusyIndicator = function () {
                $('#busy-indicator').hide();
            };

            $rootScope.showBusyIndicator = function (parent) {
                var w = null;
                var h = null;
                var pos = null;
                if (parent != null && parent != undefined) {
                    pos = $(parent).offset();
                    w = $(parent).outerWidth();
                    h = $(parent).outerHeight();

                    $('#busy-indicator').css({top: pos.top, left: pos.left, width: w, height: h})
                }
                else {
                    w = $(window).outerWidth();
                    h = $(window).outerHeight();
                    $('#busy-indicator').css({top: 0, left: 0, width: w, height: h})
                }
                $('#busy-indicator').show();
            };

            $rootScope.closeNotification = function () {
                hideNotification();
                $rootScope.notification.type = null;
                $rootScope.notification.message = null;
            };

            $rootScope.showNotification = function (message, type) {
                $rootScope.notification.type = type;
                $rootScope.notification.message = message;
                showNotification();
            };

            $rootScope.showSuccessMessage = function (message) {
                $rootScope.notification.class = 'fa-check';
                $rootScope.showNotification(message, 'alert-success');
                $timeout(function () {
                    $rootScope.closeNotification();
                }, 6000);
            };

            $rootScope.showErrorMessage = function (message) {
                $rootScope.notification.class = 'fa-ban';
                $rootScope.showNotification(message, 'alert-danger');
                $timeout(function () {
                    $rootScope.closeNotification();
                }, 6000);
            };

            $rootScope.showWarningMessage = function (message) {
                $rootScope.notification.class = 'fa-warning';
                $rootScope.showNotification(message, 'alert-warning');
                $timeout(function () {
                    $rootScope.closeNotification();
                }, 6000);
            };

            $rootScope.showInfoMessage = function (message) {
                $rootScope.notification.class = 'fa-info-circle';
                $rootScope.showNotification(message, 'alert-info');
                $timeout(function () {
                    $rootScope.closeNotification();
                }, 4000);
            };

            $rootScope.showComments = function (objectType, objectId) {
                vm.comments.show = true;
                vm.comments.objectType = objectType;
                vm.comments.objectId = objectId;
            };

            function addAndRemoveWidgets() {
                $scope.$broadcast('add.remove.widget');
            }


            $rootScope.hasPermission = function (permission) {
                var has = false;
                if (permission != null && permission != undefined) {
                    if (getPermission(permission)) {
                        has = true;
                    }
                }
                return has;
            };


            vm.userName = null;
            vm.group = null;
            function buildMaps() {
                LoginService.current().then(
                    function (session) {
                        if (session != null && session != undefined && session != "") {
                            vm.groups = session.login.groups;
                            vm.userName = session.login.loginName;
                            vm.group = session.login.groups[0];
                            if (vm.defaultGroup == null) {
                                vm.defaultGroup = vm.groups[0];
                            }
                        }
                    })
            }

            function buildPermissionsMap(permissions) {
                angular.forEach(permissions, function (permission) {
                    permissionsMap.put(permission.id, permission);
                });
            }

            function getPermission(permission) {
                var has = false;
                if (permissionsMap.get('permission.admin.all') != null) {
                    has = true;
                }
                var p = permissionsMap.get(permission);
                if (p != null && p != undefined) {
                    has = true;
                }
                return has;
            }

            $('#sideNavigation .nav-parent > a').on('click', function () {
                var parent = jQuery(this).parent();
                var sub = parent.find('> ul');

                if (!jQuery('body').hasClass('leftpanel-collapsed')) {
                    if (sub.is(':visible')) {
                        sub.slideUp(200, function () {
                            parent.removeClass('nav-active');
                            jQuery('.mainpanel').css({height: ''});
                        });
                    } else {
                        closeVisibleSubMenu();
                        parent.addClass('nav-active');
                        sub.slideDown(200, function () {
                        });
                    }
                }
                return false;
            });

            function closeVisibleSubMenu() {
                jQuery('.leftpanel .nav-parent').each(function () {
                    var t = jQuery(this);
                    if (t.hasClass('nav-active')) {
                        t.find('> ul').slideUp(200, function () {
                            t.removeClass('nav-active');
                        });
                    }
                });
            }

            $scope.toggleSideNav = function (event) {
                $rootScope.hideSidePanel('left');

                var sideNavigation = $('#sideNavigation');
                if (sideNavigation.hasClass('visible')) {
                    sideNavigation.animate({"left": "-250px"}, "500").removeClass('visible');
                } else {
                    sideNavigation.animate({"left": "0px"}, "500").addClass('visible');
                }

                event.stopPropagation();
            };

            function hideSideNavbar() {
                var sideNavigation = $('#sideNavigation');
                if (sideNavigation.hasClass('visible')) {
                    sideNavigation.animate({"left": "-250px"}, "500").removeClass('visible');
                }
            }

            (function () {
                if (window.$application == null || window.$application == undefined) {
                    window.$application = $application;
                }

                $('body').on('click', function () {
                    hideSideNavbar();
                });

                $('#sideNavigation .nav-parent  a').on('click', function (event) {
                    event.stopPropagation();
                });

                $(document).on('keydown', function (evt) {
                    if (evt.keyCode == 27) {
                        hideSideNavbar();
                    }
                });
                $scope.$on('$viewContentLoaded', function () {
                    initNotificationPanel();

                    $rootScope.$on('$stateChangeStart',
                        function (event, toState, toParams, fromState, fromParams) {
                            var height = $(window).height();
                            $('#contentpanel').height(height - 217);
                            vm.showWelcomeMessage = false;

                            $rootScope.viewInfo.runScenario = null;
                            $rootScope.viewInfo.total = null;
                            $rootScope.viewInfo.passed = null;
                            $rootScope.viewInfo.failed = null;
                            $rootScope.viewInfo.testrunDate = null;
                            $rootScope.viewInfo.testRunConfigurationName = null;


                            hideNotification();
                            vm.comments.show = false;
                            $timeout(function () {
                                positionNotification();
                            }, 500);
                        }
                    );
                });
                checkSession();
            })();
        }
	
	module.directive('fitcontent', ['$compile', '$timeout',
                function ($compile, $timeout) {
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

                                var height = $("#contentpanel").outerHeight();
                                //$(elm).height(height-5);
                                if (hasToolbar) {
                                    if (detailsFooter) {
                                        $(elm).find('.view-content').height(height - 154);
                                    } else {
                                        $(elm).find('.view-content').height(height - 114);
                                    }

                                }
                                else {
                                    if (detailsFooter) {
                                        $(elm).find('.view-content').height(height - 104);
                                    } else {
                                        $(elm).find('.view-content').height(height - 64);
                                        if (adminRightSideView) {
                                            if (barHeight != null) {
                                                $("#admin-rightView").height(height - 130);
                                            } else {
                                                $("#admin-rightView").height(height - 80);
                                            }
                                        }
                                    }
                                }


                                if (tabContent) {

                                    var height1 = $("#contentpanel").height();
                                    if (detailsFooter) {
                                        $('.tab-content').height(height1 - 164);
                                        $('.tab-pane').height(height1 - 164);
                                    } else {
                                        $('.tab-content').height(height1 - 124);
                                        $('.tab-pane').height(height1 - 124);
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

    }
);
