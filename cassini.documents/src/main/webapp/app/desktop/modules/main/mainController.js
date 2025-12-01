define(
    [
        'app/desktop/modules/main/main.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/shared/filters/filters',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/desktop/modules/shared/comments/commentsBtnDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/groupService',
        'app/desktop/modules/main/appNavController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/main/sidePanelsController'

    ],
    function (module, moment) {
        module.controller('MainController', MainController);

        function MainController($scope, $rootScope, $timeout, $state, $cookies, $http, GroupService,
                                $application, LoginService,$interval, DialogService, CommonService, PersonGroupService) {
            moment.tz.setDefault("Asia/Kolkata");
            window.moment = moment;
            var vm = this;

            $rootScope.viewInfo = {
                icon: 'fa-home',
                title: 'Home',
                description: ""
            };
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

            vm.viewType = 'APP';
            vm.user = {
                name: ""
            };

            var sessionCheckPromise = null;
            var sessionCheckErrorCount = 0;
            var permissionsMap = new Hashtable();
            vm.changePermissionsByGroup = changePermissionsByGroup;
            vm.defaultGroup = null;
            vm.groups = [];

            vm.logout = logout;
            vm.feedback = feedback;
            vm.showProfile = showProfile;
            vm.help = help;
            vm.viewInfo = $rootScope.viewInfo;
            $application.config = null;


            $rootScope.setToolbarTemplate = function (tbTemplate) {
                $scope.toolbarTemplate = tbTemplate;
            };

            function checkSession() {
                LoginService.current().then(
                    function (session) {
                        if (session == null || session == "") {
                            $state.go('login');
                        }
                        else {
                            window.$("#preloader").hide();
                            buildMaps(session);
                            window.$application.session = session;
                            window.$application.login = session.login;
                            vm.user = session.login;
                            $rootScope.login = vm.user;

                            CommonService.initialize();

                            if ($application.homeLoaded == false) {
                                $state.go('app.home');
                            }
                        }
                    }
                );
            }

            vm.userName = null;
            vm.group = null;
            function buildMaps() {
                LoginService.current().then(
                    function (session) {
                        if (session != null && session != undefined && session != "") {
                            vm.groups = session.login.groups;
                            vm.userName = session.login.loginName;
                            vm.group = session.login.groups[0];
                            vm.user = session.login;
                            $rootScope.login = vm.user;

                            /*angular.forEach(vm.groups, function (group) {
                             //groupsMap.put(group.name, group);
                             buildPermissionsMap(group.permissions);
                             });*/
                            if ($rootScope.login.person.defaultGroup != null) {
                                GroupService.getGroupById($rootScope.login.person.defaultGroup).then(
                                    function (data) {
                                        vm.defaultGroup = data;
                                        window.$application.login.loginGroup = data;
                                        angular.forEach(vm.groups, function (group) {
                                            var count = -1;
                                            if (group.groupId == data.groupId) {
                                                count++;
                                                var x = vm.groups[0];
                                                vm.groups[0] = group;
                                                vm.groups[count] = x;
                                            }
                                        });
                                    }
                                );
                            }
                            vm.defaultGroup = vm.groups[0];
                            window.$application.login.loginGroup = vm.groups[0];
                            buildPermissionsMap(vm.defaultGroup.permissions);
                        }
                    })
            }

            function buildPermissionsMap(permissions) {
                angular.forEach(permissions, function (permission) {
                    permissionsMap.put(permission.id, permission);
                });
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

            function changePermissionsByGroup(group) {
                var currentState = $state.current;
                var options = {
                    title: 'Group Change',
                    message: "Are you sure do you want change Group Permissions",
                    okButtonClass: 'btn-danger'
                };

                if (currentState.name != 'app.home') {
                    options.message = options.message + ' If yes,  It Redirects to HOME page';
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
                                    window.$application.login.loginGroup = group;
                                    permissionsMap = new Hashtable();
                                    buildPermissionsMap(group.permissions);
                                    $state.go('app.home', {}, {reload: 'app.home'});
                                })
                        }
                    }
                )
            }


            function help() {
                $state.go('app.help.main');
            }

            function feedback() {
                $state.go('app.help.feedback');
            }

            $rootScope.showBusyIndicator = function (parent) {
                if (parent != null && parent != undefined) {
                    var pos = $(parent).offset();
                    var w = $(parent).outerWidth();
                    var h = $(parent).outerHeight();

                    $('#busy-indicator').css({top: pos.top, left: pos.left, width: w, height: h})
                }
                else {
                    var w = $(window).outerWidth();
                    var h = $(window).outerHeight();
                    $('#busy-indicator').css({top: 0, left: 0, width: w, height: h})
                }
                $('#busy-indicator').show();
            };

            $rootScope.hideBusyIndicator = function () {
                $('#busy-indicator').hide();
            };

            function showProfile(login) {
                $state.go('app.admin.logindetails', {loginId: login.id});
            }


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
                hideNotification();
            };

            $rootScope.showNotification = function (message, type) {
                $rootScope.notification.type = type;
                $rootScope.notification.message = message;
                //$rootScope.$apply();
                showNotification();
            };

            $rootScope.showSuccessMessage = function (message) {
                $rootScope.notification.class = 'fa-check';
                $rootScope.showNotification(message, 'alert-success');
                //$scope.$evalAsync();
                $timeout(function () {
                    $rootScope.closeNotification();
                }, 5000)
            };

            $rootScope.showExportMessage = function (message) {
                $rootScope.notification.class = 'fa fa-spinner fa-spin fa-2x';
                $rootScope.showNotification(message, 'alert-info');
            };

            $rootScope.showErrorMessage = function (message) {
                $rootScope.notification.class = 'fa-ban';
                $rootScope.showNotification(message, 'alert-danger');
                //$scope.$evalAsync();
                $timeout(function () {
                    $rootScope.closeNotification();
                }, 5000)
            };

            $rootScope.showWarningMessage = function (message) {
                $rootScope.notification.class = 'fa-warning';
                $rootScope.showNotification(message, 'alert-warning');
                $timeout(function () {
                    $rootScope.closeNotification();
                }, 5000)
            };

            $rootScope.showInfoMessage = function (message) {
                $rootScope.notification.class = 'fa-info-circle';
                $rootScope.showNotification(message, 'alert-info');
                $timeout(function () {
                    $rootScope.closeNotification();
                }, 5000)
            };

            $rootScope.showComments = function (objectType, objectId) {
                vm.comments.show = true;
                vm.comments.objectType = objectType;
                vm.comments.objectId = objectId;
            };

            $rootScope.toggleSideNav = function (event) {

                var sideNavigation = $('#sideNavigation');
                if (sideNavigation.hasClass('visible')) {
                    sideNavigation.animate({"left": "-250px"}, "500").removeClass('visible');
                } else {
                    sideNavigation.animate({"left": "0px"}, "500").addClass('visible');
                }

                if (event != null && event != undefined) {
                    event.stopPropagation();
                }
            };

            $('#sideNavigation .nav-parent > a').on('click', function () {
                var parent = jQuery(this).parent();
                var sub = parent.find('> ul');

                // Dropdown works only when leftpanel is not collapsed
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

            function hideSideNavbar() {
                var sideNavigation = $('#sideNavigation');
                if (sideNavigation.hasClass('visible')) {
                    sideNavigation.animate({"left": "-250px"}, "500").removeClass('visible');
                }
            }

            function initEvents() {
                $('body').on('click', function () {
                    hideSideNavbar();
                });

                $('#sideNavigation .nav-parent  a').on('click', function (event) {
                    event.stopPropagation();
                });

                //ESC key press event
                $(document).on('keyup', function (evt) {
                    if (evt.keyCode == 27) {
                        hideSideNavbar();
                    }
                });
            }

            (function () {
                if (window.$application == null || window.$application == undefined) {
                    window.$application = $application;
                }
                $timeout(function () {
                    positionNotification();
                }, 500);

                $rootScope.$on("app.main.groups", buildMaps);
                $scope.$on('$viewContentLoaded', function () {
                    initNotificationPanel();
                    initEvents();

                    $rootScope.$on('$stateChangeStart',
                        function (event, toState, toParams, fromState, fromParams, elm) {
                            hideSideNavbar();
                            vm.comments.show = false;
                            vm.viewInfo.description = "";
                            hideNotification();
                            vm.comments.show = false;
                            var height = $(window).height();
                            $('#contentpanel').height(height - 217);

                            if (sessionCheckPromise == null) {
                                initSessionCheck();
                            }
                        }
                    );
                });
                checkSession();
                $scope.$on('$viewContentLoaded', function(){
                    $timeout(function(){
                        $application.homeLoaded = true;
                        window.$('#appview').show();
                    },500)
                })
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