define(
    [
        'app/desktop/modules/main/main.module',
        'moment',
        'app/shared/services/app/application',
        'app/assets/bower_components/cassini-platform/app/shared/filters/filters',
        'app/desktop/modules/navigation/app/appNavController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/main/sidePanelsController',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/groupService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],
    function (module, moment) {
        module.controller('MainController', MainController);

        function MainController($scope, $rootScope, $timeout, $state, $cookies, $window,
                                $application, LoginService, GroupService, CommonService, PersonGroupService, DialogService) {

            $rootScope.viewInfo = {
                icon: 'fa-home',
                title: 'Home'
            };
            window.moment = moment;
            var vm = this;

            vm.viewInfo = $rootScope.viewInfo;
            $rootScope.showWelcomePage = true;

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
            $rootScope.loginPersonDetails = null;

            var permissionsMap = new Hashtable();
            vm.newComplaint = newComplaint;
            function newComplaint() {
                var options = {
                    title: 'New Complaint',
                    template: 'app/desktop/modules/main/new/newComplaintView.jsp',
                    controller: 'NewComplaintController as newCompVm',
                    resolve: 'app/desktop/modules/main/new/newComplaintController',
                    width: 500,
                    showMask: true,
                    buttons: [
                        {text: "Create", broadcast: 'app.comps.new'}
                    ],
                    callback: function (complaint) {
                        $rootScope.showSuccessMessage(complaint.complaintNumber + " Complaint Created Successfully");
                        $rootScope.hideSidePanel();
                    }
                };

                $rootScope.showSidePanel(options);
            }

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
                                $rootScope.$broadcast("app.notifications.logout");
                                $rootScope.loginPersonDetails = null;
                                $rootScope.personTypeDetails = null;
                                $rootScope.showWelcomePage = true;
                                $state.go('app.home');
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

                if (currentState.name != 'app.dashboard') {
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
                                    $state.go('app.dashboard', {}, {reload: 'app.dashboard'});
                                })
                        }
                    }
                )
            }

            $rootScope.checkSession = checkSession;

            function checkSession() {
                LoginService.current().then(
                    function (session) {
                        if (session == null || session == "") {
                            $state.go('app.home');
                            $rootScope.showWelcomePage = true;
                            vm.currentState = "app.home";
                        }
                        else {
                            CommonService.getPersonType(session.login.person.personType).then(
                                function (data) {
                                    $rootScope.personTypeDetails = data.name;
                                    $rootScope.loginTypeDetails = data.description;
                                    if ($rootScope.personTypeDetails == "Administrator") {
                                        $rootScope.showUsersTab = true;
                                        $rootScope.showAssessorsTab = true;
                                    } else {
                                        $rootScope.showUsersTab = false;
                                        $rootScope.showAssessorsTab = false;
                                    }
                                }
                            );

                            buildMaps(session);
                            window.$application.session = session;
                            window.$application.login = session.login;
                            $rootScope.loginPersonDetails = session.login;
                            vm.user.name = session.login.person.firstName;
                            vm.user.id = session.login.id;
                            CommonService.initialize();
                            vm.showWelcomeMessage = false;
                            if ($application.homeLoaded == false) {
                                $state.go('app.dashboard');
                            }
                            vm.currentState = "app.dashboard";
                            $rootScope.showWelcomePage = false;
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

            function showNotification() {
                notificationPosition();
                appNotification.show();
                appNotification.removeClass('zoomOut');
                appNotification.addClass('zoomIn');
            }

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

            $rootScope.showExportMessage = function (message) {
                $rootScope.notification.class = 'fa fa-spinner fa-spin fa-2x';
                $rootScope.showNotification(message, 'alert-info');
            };

            $rootScope.showNotification = function (message, type) {
                $rootScope.notification.type = type;
                $rootScope.notification.message = message;
                //$rootScope.$apply();
                showNotification();
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

                            /*angular.forEach(vm.groups, function (group) {
                             //groupsMap.put(group.name, group);
                             buildPermissionsMap(group.permissions);
                             });*/
                            if (vm.defaultGroup == null) {
                                vm.defaultGroup = vm.groups[0];
                            }
                            /* buildPermissionsMap(vm.defaultGroup.permissions);*/
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
                /*else {
                 if (permission.endsWith(".all")) {
                 var sub = permission.remove(".all");
                 var index = sub.lastIndexOf(".");
                 if (index != -1) {
                 sub = sub.substring(0, index) + ".all";
                 has = getPermission(sub);
                 }
                 }
                 else {
                 var index = permission.lastIndexOf(".");
                 if (index != -1) {
                 permission = permission.substring(0, index);
                 }
                 permission += ".all";
                 has = getPermission(permission);
                 }
                 }*/
                return has;
            }

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

            $scope.toggleSideNav = function (event) {

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


            function notificationPosition() {
                var pos = $('#headerbar').position();
                if (pos != null) {
                    var height = $('#headerbar').outerHeight();
                    var width = $('#workspace').outerWidth();
                    var h = $('#viewTitleContainer').outerHeight();

                    var left = pos.left;
                    var top = pos.top + height;

                    appNotification.css({top: 50, left: left, width: width, height: h, position: 'absolute'});
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
                            vm.currentState = toState.name;
                            var height = $(window).height();
                            if ($rootScope.loginPersonDetails == null) {
                                $rootScope.showWelcomePage = true;
                            } else {
                                $rootScope.showWelcomePage = false;
                            }
                            var imageHeight = $("#irsteHeaderImage").outerHeight();
                            var headerHeight = $("#headerbar").outerHeight();
                            var contentHeight = $('#contentpanel').height(height - (imageHeight + headerHeight));
                            $('#applicationfitcontent').height(contentHeight - 3);
                            vm.showWelcomeMessage = false;
                            hideNotification();
                            vm.comments.show = false;
                            $timeout(function () {
                                notificationPosition();
                            }, 500);
                        }
                    );
                });

                $rootScope.checkSession();

            })();
        }
    }
)
;
