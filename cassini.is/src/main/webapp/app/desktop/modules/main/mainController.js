define(
    [
        'app/desktop/modules/main/main.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/shared/filters/filters',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/forgeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/desktop/modules/shared/comments/commentsBtnDirective',
        'app/desktop/modules/navigation/app/appNavController',
        'app/desktop/modules/navigation/procurement/procurementNavController',
        'app/desktop/modules/navigation/project/projectNavController',
        'app/desktop/common/constants',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/groupService',
        'app/desktop/modules/select/objectSelectionController'

    ],
    function (module, moment) {
        module.controller('MainController', MainController);

        function MainController($scope, $rootScope, $timeout, $state, $cookies, $http, GroupService, $translate,
                                $application, LoginService, ForgeService, $interval, Constants, DialogService, ObjectAttributeService, CommonService, PersonGroupService) {
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
            vm.resize = resize;
            vm.user = {
                name: ""
            };

            vm.rightSidePanelOptions = {
                title: "",
                visible: false,
                buttons: []
            };
            vm.leftSidePanelOptions = {
                title: "",
                visible: false,
                buttons: []
            };
            var sessionCheckPromise = null;
            var sessionCheckErrorCount = 0;
            var permissionsMap = new Hashtable();
            vm.changePermissionsByGroup = changePermissionsByGroup;
            vm.defaultGroup = null;
            vm.groups = [];

            vm.broadcastButtonCallback = broadcastButtonCallback;
            $rootScope.showSidePanel = showSidePanel;
            $rootScope.hideSidePanel = vm.hideSidePanel = hideSidePanel;
            vm.logout = logout;
            vm.feedback = feedback;
            vm.showProfile = showProfile;
            vm.help = help;
            $rootScope.setViewType = setViewType;
            vm.viewInfo = $rootScope.viewInfo;
            $application.config = null;

            $rootScope.showTaskNotification = false;
            $rootScope.helpDefaultView = "app/help/OVERVIEW/OVERVIEW.htm";
            function clearBroadcastListeners(buttons) {
                angular.forEach(buttons, function (button) {
                    $rootScope.$$listeners[button.broadcast] = [];
                });
            }

            function broadcastButtonCallback(button) {
                $rootScope.$broadcast(button.broadcast);
            }

            function showSidePanel(options) {
                clearBroadcastListeners(vm.rightSidePanelOptions.buttons);
                clearBroadcastListeners(vm.leftSidePanelOptions.buttons);

                if (options.side == null || options.side == undefined || options.side == 'right') {
                    showRightSidePanel(options);
                }
                else if (options.side == 'left') {
                    showLeftSidePanel(options);
                }
            }

            function showLeftSidePanel(options) {
                require([options.resolve], function () {
                    vm.leftSidePanelOptions = options;

                    if (options.showMask != null && options.showMask != undefined &&
                        options.showMask == true) {
                        $('#contentpanel-mask').show();
                    }

                    var html = "<div ng-include=\"'" + options.template + "'\"";
                    html += "ng-controller='" + options.controller + "'></div>";

                    $('#leftSidePanelContent').empty();
                    $('#leftSidePanelContent').append(html);

                    var el = angular.element($("#leftSidePanelContent"));
                    var $localScope = el.scope();
                    $localScope.data = options.data;
                    $localScope.callback = options.callback;
                    var $injector = el.injector();

                    $injector.invoke(function ($compile) {
                        $compile(el)($localScope);
                    });

                    if (options.width == null && options.width == undefined) {
                        options.width = 500;
                    }

                    var slider_width = $('#leftSidePanel').width();

                    if (!vm.leftSidePanelOptions.visible || options.width != slider_width) {
                        $('#leftSidePanel').width(options.width);
                        $('#leftSidePanel').css({'margin-left': "-" + options.width + "px"});
                        $('#leftSidePanel').animate({"margin-left": '+=' + options.width}, 'slow', function () {
                            $('#leftSidePanelButtonsPanel').css({'width': options.width + 'px'});
                            if (options.buttons != undefined && options.buttons.length == 2) {
                                $('#leftSidePanelButtonsPanel').css({'display': 'inline-flex'});
                            } else {
                                $('#leftSidePanelButtonsPanel').css({'display': 'inline'});
                            }
                            $('#leftSidePanelButtonsPanel').show();
                        });
                        vm.leftSidePanelOptions.visible = true;
                    }
                    $scope.$apply();
                });
            }

            $scope.toggleAdmin = function () {
                $('.uib-dropdown-menu1').toggle();
                $('.uib-dropdown-menu').hide();
                $rootScope.hideSidePanel('right');
            };
            $scope.toggleAdmin1 = function () {
                $('.uib-dropdown-menu').toggle();
                $('.uib-dropdown-menu1').hide();
                $rootScope.hideSidePanel('right');
            };
            function showRightSidePanel(options) {
                require([options.resolve], function () {
                    vm.rightSidePanelOptions = options;

                    $('.uib-dropdown-menu').hide();
                    $('.uib-dropdown-menu1').hide();

                    if (options.showMask != null && options.showMask != undefined &&
                        options.showMask == true) {
                        $('#contentpanel-mask').show();
                    }

                    var html = "<div ng-include=\"'" + options.template + "'\"";
                    html += "ng-controller='" + options.controller + "'></div>";

                    $('#rightSidePanelContent').empty();
                    $('#rightSidePanelContent').append(html);

                    var el = angular.element($("#rightSidePanelContent"));
                    var $localScope = el.scope();
                    $localScope.data = options.data;
                    $localScope.callback = options.callback;
                    var $injector = el.injector();

                    $injector.invoke(function ($compile) {
                        $compile(el)($localScope);
                    });

                    if (options.width == null && options.width == undefined) {
                        options.width = 500;
                    }

                    var slider_width = $('#rightSidePanel').width();

                    if (!vm.rightSidePanelOptions.visible || options.width != slider_width) {
                        $('#rightSidePanel').width(options.width);
                        $('#rightSidePanel').css({'margin-right': "-" + options.width + "px"});
                        $('#rightSidePanel').animate({"margin-right": '+=' + options.width}, 'slow', function () {
                            $('#rightSidePanelButtonsPanel').css({'width': options.width + 'px'});
                            if (options.buttons != undefined && options.buttons.length == 2) {
                                $('#rightSidePanelButtonsPanel').css({'display': 'inline-flex'});
                            } else {
                                $('#rightSidePanelButtonsPanel').css({'display': 'inline'});
                            }
                            $('#rightSidePanelButtonsPanel').show();
                        });
                        vm.rightSidePanelOptions.visible = true;
                    }
                    $scope.$apply();
                });
            }

            function hideSidePanel(side) {
                if (side == null || side == undefined || side == 'right') {
                    hideRightSidePanel();
                }
                else if (side == 'left') {
                    hideLeftSidePanel();
                }
            }

            function hideRightSidePanel() {
                if (vm.rightSidePanelOptions.visible) {
                    var slider_width = $('#rightSidePanel').width();
                    $('#rightSidePanel').animate({"margin-right": '-=' + slider_width}, 'slow',
                        function () {
                            if (vm.rightSidePanelOptions.showMask != null &&
                                vm.rightSidePanelOptions.showMask != undefined &&
                                vm.rightSidePanelOptions.showMask == true) {
                                $('#contentpanel-mask').hide();
                            }
                        }
                    );
                    vm.rightSidePanelOptions.visible = false;
                    $('#rightSidePanelButtonsPanel').hide();
                }
            }

            function hideLeftSidePanel() {
                if (vm.leftSidePanelOptions.visible) {
                    var slider_width = $('#leftSidePanel').width();
                    $('#leftSidePanel').animate({"margin-left": '-=' + slider_width}, 'slow',
                        function () {
                            if (vm.leftSidePanelOptions.showMask != null &&
                                vm.leftSidePanelOptions.showMask != undefined &&
                                vm.leftSidePanelOptions.showMask == true) {
                                $('#contentpanel-mask').hide();
                            }
                        }
                    );
                    vm.leftSidePanelOptions.visible = false;
                    $('#leftSidePanelButtonsPanel').hide();
                }
            }

            $rootScope.setToolbarTemplate = function (tbTemplate) {
                $scope.toolbarTemplate = tbTemplate;
            };

            function setViewType(type) {
                vm.viewType = type;
                $application.viewType = Constants.App.ViewType.fromName(type);
                $rootScope.$broadcast('app.viewtype.changed');
            }

            vm.userPreferences = null;
            function loadUserTheme(loginId) {
                LoginService.getUserTheme(loginId).then(
                    function (data) {
                        vm.userPreferences = data;
                    }
                );
            }

            function checkSession() {
                LoginService.current().then(
                    function (session) {
                        loadPreferences();
                        if (session == null || session == "") {
                            $state.go('login');
                        }
                        else {
                            loadUserTheme(session.login.id);
                            window.$("#preloader").hide();
                            buildMaps(session);
                            if ($application.config != null && $application.config.forgeViewableEnable != null && $application.config.forgeViewableEnable)
                                forgeIntialization();
                            window.$application.session = session;
                            window.$application.login = session.login;
                            vm.user = session.login;
                            $rootScope.login = vm.user;

                            CommonService.initialize();
                            if (vm.user.person.email == null || vm.user.person.email == "") {
                                LoginService.logout().then(
                                    function (success) {
                                        $state.go('login', {}, {reload: true});
                                    },
                                    function (error) {
                                    }
                                );
                            }
                            //Person has Email Redirect to HomePage
                            else {

                                if ($application.homeLoaded == false) {
                                    $timeout(function () {
                                        if (vm.userPreferences == null) {
                                            if (vm.userPreferences.userTheme == null || vm.userPreferences.userTheme == "") {
                                                changeTheme(vm.systemTheme);

                                            }
                                            else {
                                                changeTheme(vm.userPreferences.userTheme);

                                            }
                                        } else {
                                            if (vm.userPreferences.userTheme == null || vm.userPreferences.userTheme == "") {
                                                changeTheme(vm.systemTheme);

                                            }
                                            else {
                                                changeTheme(vm.userPreferences.userTheme);
                                            }
                                        }
                                        $state.go('app.home');
                                        $rootScope.sessionStatus = true;
                                    }, 1000)

                                }

                            }
                            $timeout(function () {
                                /*resizeHeader();*/
                            }, 500)
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

            $rootScope.permissionTaskMap = new Hashtable();

            function buildPermissionsMap(permissions) {
                angular.forEach(permissions, function (permission) {
                    permissionsMap.put(permission.id, permission);
                    $rootScope.permissionTaskMap.put(permission.id, permission);
                });
            }

            $rootScope.hasPermission = function (permission) {
                var has = true;
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
                    message: "Are you sure you want to change Group permissions?",
                    okButtonClass: 'btn-danger'
                };

                if (currentState.name != 'app.home') {
                    options.message = options.message + ' If yes,  It redirects to HOME page';
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

            vm.systemTheme = null;
            function loadPreferences() {
                CommonService.getPreferences().then(
                    function (data) {
                        angular.forEach(data, function (item) {
                            if (item.preferenceKey == "SYSTEM.LOGO") {
                                if (item.customLogo != null) {
                                    $rootScope.companyImage = "api/common/persons/" + item.id + "/customImageAttribute/download?" + new Date().getTime();
                                }
                            }
                            if (item.preferenceKey == "SYSTEM.THEME") {
                                vm.systemTheme = item.stringValue;
                                //changeTheme(item.stringValue);
                            }
                        })

                    }
                )
            }

            function changeTheme(el) {
                var theme = el;
                if (theme != null) {
                    if (theme == "Default") {
                        $('#' + el).css('display', 'none');
                        $("#footer").css('background-color', "#00253f");
                        $(".headerbar").css('background-color', "#00253f");
                    } else {
                        $('#' + el).css('display', 'block');
                        $("#footer").css('background-color', el);
                        $(".headerbar").css('background-color', el);
                    }
                } else {
                    $('#' + el).css('display', 'none');
                    $("#footer").css('background-color', "#00253f");
                    $(".headerbar").css('background-color', "#00253f");
                }
            }

            /*-----------  To check Required Attributes empty or not ----------*/

            $rootScope.checkAttribute = function (attribute) {
                if ((attribute.stringValue != null && attribute.stringValue != undefined && attribute.stringValue != "") ||
                    (attribute.integerValue != null && attribute.integerValue != undefined && attribute.integerValue != "") ||
                    (attribute.doubleValue != null && attribute.doubleValue != undefined && attribute.doubleValue != "") ||
                    (attribute.dateValue != null && attribute.dateValue != undefined && attribute.dateValue != "") ||
                    (attribute.imageValue != null && attribute.imageValue != undefined && attribute.imageValue != "") ||
                    (attribute.currencyValue != null && attribute.currencyValue != undefined && attribute.currencyValue != "") ||
                    (attribute.timeValue != null && attribute.timeValue != undefined && attribute.timeValue != "") ||
                    (attribute.attachmentValues.length != 0) ||
                    (attribute.refValue != null && attribute.refValue != undefined && attribute.refValue != "") ||
                    (attribute.listValue != null && attribute.listValue != undefined && attribute.listValue != "")) {
                    return true;
                } else {
                    return false;
                }
            };

            $rootScope.closeNotification = function () {
                hideNotificationPanel();
            };

            $rootScope.showNotification = function (message, type) {
                $rootScope.notification.type = type;
                $rootScope.notification.message = message;
                //$rootScope.$apply();
                showNotificationPanel();
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

            function updateSidePanel(toState) {
                var name = toState.name;

                if (name == 'app.home' || name == 'app.store.all' || name == 'app.store.stock.issued' || name == 'app.store.stock.received' ||
                    name == 'app.store.stock.receiveDetails' || name == 'app.store.details' || name == 'app.store.newRequisition' || 'app.store.stock.issueDetails' ||
                    name == 'app.contracts.contractors' || name == 'app.contracts.workOrders' || name == 'app.contracts.workOrderDetails'
                    || name == 'app.store.requisitionDetails') {
                    $('#leftSidePanel').css({'top': '125px'});
                    $('#rightSidePanel').css({'top': '125px'});
                    $('#contentpanel-mask').css({'top': '125px'});
                }
                else {
                    $('#leftSidePanel').css({'top': '175px'});
                    $('#rightSidePanel').css({'top': '175px'});
                    $('#contentpanel-mask').css({'top': '175px'});
                }
            }

            $rootScope.toggleSideNav = function (event) {
                $rootScope.hideSidePanel('left');
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

            function resize() {
                var height = $(window).height();
                var projectHeaderHeight = $("#project-headerbar").outerHeight();
                if (projectHeaderHeight != null) {
                    if ($application.selectedProject != undefined && $application.selectedProject.locked == true) {
                        $('#contentpanel').height(height - 297);
                    } else {
                        $('#contentpanel').height(height - 267);
                    }
                } else if (projectHeaderHeight == null) {
                    $('#contentpanel').height(height - 217);
                }
            }

            /*   function resizeHeader() {
             var width = $(window).width();
             var headerLeft = $("#header-left").width();
             var headerRight = $("#header-right").width();
             $('#project-heading').width(width - (headerLeft + headerRight) - 20);
             }*/

            function forgeIntialization() {
                if ($application.forgeToken == null) {
                    ForgeService.getForgeAuthentication().then(
                        function (data) {
                            $application.forgeToken = data;
                        }, function (error) {
                            console.log("error");
                        }
                    );
                }
            }

            $rootScope.getIcon = function (fileName) {
                var fileExtensionPattern = /\.([0-9a-z]+)(?=[?#])|(\.)(?:[\w]+)$/gmi;
                if (fileName.includes(".")) {
                    var extension = fileName.toLowerCase().match(fileExtensionPattern)[0];
                    if ([".html", ".csv", ".ttf", ".exe", ".log", ".bat", ".css", ".jsp", ".js", ".sql", ".java", ".class", ".c", ".net", ".py", ".ipynb"].indexOf(extension) > -1) {
                        return "fa fa-file-code-o";
                    }
                    else if ([".png", ".jpeg", ".jpg", ".bmp", ".eps", ".gif", ".pict", ".esd", ".tif"].indexOf(extension) > -1) {
                        return "fa fa-file-photo-o";
                    }
                    else if ([".xlsx", ".xls"].indexOf(extension) > -1) {
                        return "fa fa-file-excel-o";
                    }
                    else if ([".mp4", ".avi", ".flv", ".mpg", ".mpeg", ".mov", ".wmv"].indexOf(extension) > -1) {
                        return "fa fa-file-movie-o";
                    }
                    else if ([".mp3", ".wav", ".mid", ".mkv"].indexOf(extension) > -1) {
                        return "fa fa-file-audio-o";
                    }
                    else if ([".docx", ".doc"].indexOf(extension) > -1) {
                        return "fa fa-file-word-o";
                    }
                    else if ([".pdf"].indexOf(extension) > -1) {
                        return "fa fa-file-pdf-o";
                    }
                    else if ([".ppt"].indexOf(extension) > -1) {
                        return "fa fa-file-powerpoint-o";
                    }
                    else if ([".txt", ".rtf", ".wps", ".wpd"].indexOf(extension) > -1) {
                        return "fa fa-file-text-o";
                    }
                    else if ([".zip", ".war", ".jar", ".arc", ".arj", ".gz", ".hqx", ".sit", ".tar", ".z"].indexOf(extension) > -1) {
                        return "fa fa-file-zip-o";
                    }
                    else {
                        return "fa fa-file-o";
                    }
                }
            };

            $rootScope.getIconColor = function (fileName) {
                var fileExtensionPattern = /\.([0-9a-z]+)(?=[?#])|(\.)(?:[\w]+)$/gmi;
                if (fileName.includes(".")) {
                    var extension = fileName.toLowerCase().match(fileExtensionPattern)[0];
                    if ([".html", ".csv", ".ttf", ".exe", ".log", ".bat", ".css", ".jsp", ".js", ".sql", ".java", ".class", ".c", ".net", ".py", ".ipynb"].indexOf(extension) > -1) {
                        return "{'color': '#e699ff'}";
                    }
                    else if ([".png", ".jpeg", ".jpg", ".bmp", ".eps", ".gif", ".pict", ".esd", ".tif"].indexOf(extension) > -1) {
                        return "{'color': '#9999ff'}";
                    }
                    else if ([".xlsx", ".xls"].indexOf(extension) > -1) {
                        return "{'color': '#00b377'}";
                    }
                    else if ([".mp4", ".avi", ".flv", ".mpg", ".mpeg", ".mov", ".wmv"].indexOf(extension) > -1) {
                        return "{'color': '#ff6600'}";
                    }
                    else if ([".mp3", ".wav", ".mid", ".mkv"].indexOf(extension) > -1) {
                        return "{'color': '#ff6600'}";
                    }
                    else if ([".docx", ".doc"].indexOf(extension) > -1) {
                        return "{'color': '#4d79ff'}";
                    }
                    else if ([".pdf"].indexOf(extension) > -1) {
                        return "{'color': '#ff0000'}";
                    }
                    else if ([".ppt"].indexOf(extension) > -1) {
                        return "{'color': '#ff531a'}";
                    }
                    else if ([".txt", ".rtf", ".wps", ".wpd"].indexOf(extension) > -1) {
                        return "{'color': '#5b1f07'}";
                    }
                    else if ([".zip", ".war", ".jar", ".arc", ".arj", ".gz", ".hqx", ".sit", ".tar", ".z"].indexOf(extension) > -1) {
                        return "{'color': '#ffb84d'}";
                    }
                    else {
                        return "{'color': '#ff6600'}";
                    }
                }
            };



            var parsed = angular.element("<div></div>");


            var passwordStrength = /^[\s\S]{8,32}$/,
                upper = /[A-Z]/,
                lower = /[a-z]/,
                number = /[0-9]/,
                special = /[ !"#$%&'()*+,\-./:;<=>?@[\\\]^_`{|}~]/;

            function loadPasswordStrength() {
                var context = 'SYSTEM';
                CommonService.getPreferenceByContext(context).then(
                    function (data) {
                        angular.forEach(data, function (prop) {
                            if (prop.preferenceKey == 'SYSTEM.PASSWORD') {
                                $rootScope.passwordProperties = JSON.parse(prop.jsonValue);
                            }
                        });
                    }, function (error) {
                        console.log(error);
                    }
                )
            }


            vm.passwordMinLength = parsed.html($translate.instant("PASSWORD_MINIMUM_LENGTH")).html();
            vm.passwordNumbersOnly = parsed.html($translate.instant("PASSWORD_NUMBERS_ONLY")).html();
            vm.passwordNumbersAndSpecialChar = parsed.html($translate.instant("PASSWORD_NUMBERS_AND_SPECIAL_CHARACTERS")).html();
            vm.passwordCaseSensitivie = parsed.html($translate.instant("PASSWORD_CASE_SENSITIVIE")).html();
            vm.password = parsed.html($translate.instant("PASSWORD")).html();
            var characters = parsed.html($translate.instant("CHARACTERS")).html();


            vm.loadPasswordMessage = loadPasswordMessage;
            function loadPasswordMessage() {
                $rootScope.passwordInformation = "";
                if ($rootScope.passwordProperties.minLen != null) {
                    $rootScope.passwordInformation += "<br>";
                    $rootScope.passwordInformation += "\u2022 " + vm.passwordMinLength + $rootScope.passwordProperties.minLen + characters;
                }
                if ($rootScope.passwordProperties.specialChar != null) {
                    if ($rootScope.passwordProperties.specialChar == 'Nb') {
                        $rootScope.passwordInformation += "<br>";
                        $rootScope.passwordInformation += "\u2022 " + vm.passwordNumbersOnly;
                    }
                    if ($rootScope.passwordProperties.specialChar == 'Nbs') {
                        $rootScope.passwordInformation += "<br>";
                        $rootScope.passwordInformation += "\u2022 " + vm.passwordNumbersAndSpecialChar;
                    }
                }
                if ($rootScope.passwordProperties.cases != null) {
                    if ($rootScope.passwordProperties.cases == 'Yes') {
                        $rootScope.passwordInformation += "<br>";
                        $rootScope.passwordInformation += "\u2022 " + vm.passwordCaseSensitivie;
                    }
                }
            }

            $rootScope.currentLang = $translate.proposedLanguage() || $translate.use();

            vm.passwordMinLength = parsed.html($translate.instant("PASSWORD_MINIMUM_LENGTH")).html();
            vm.passwordNumbersOnly = parsed.html($translate.instant("PASSWORD_NUMBERS_ONLY")).html();
            vm.passwordNumbersAndSpecialChar = parsed.html($translate.instant("PASSWORD_NUMBERS_AND_SPECIAL_CHARACTERS")).html();
            vm.passwordCaseSensitivie = parsed.html($translate.instant("PASSWORD_CASE_SENSITIVIE")).html();
            vm.password = parsed.html($translate.instant("PASSWORD")).html();
            $rootScope.dragAndDropFilesTitle = parsed.html($translate.instant("DRAG_DROP_FILE")).html();
            $rootScope.clickToAddFilesTitle = parsed.html($translate.instant("CLICK_TO_ADD_FILES")).html();
            var characters = parsed.html($translate.instant("CHARACTERS")).html();


            vm.loadPasswordMessage = loadPasswordMessage;
            function loadPasswordMessage() {
                $rootScope.passwordInformation = "";
                if ($rootScope.passwordProperties.minLen != null) {
                    $rootScope.passwordInformation += "<br>";
                    $rootScope.passwordInformation += "\u2022 " + vm.passwordMinLength + $rootScope.passwordProperties.minLen + characters;
                }
                if ($rootScope.passwordProperties.specialChar != null) {
                    if ($rootScope.passwordProperties.specialChar == 'Nb') {
                        $rootScope.passwordInformation += "<br>";
                        $rootScope.passwordInformation += "\u2022 " + vm.passwordNumbersOnly;
                    }
                    if ($rootScope.passwordProperties.specialChar == 'Nbs') {
                        $rootScope.passwordInformation += "<br>";
                        $rootScope.passwordInformation += "\u2022 " + vm.passwordNumbersAndSpecialChar;
                    }
                }
                if ($rootScope.passwordProperties.cases != null) {
                    if ($rootScope.passwordProperties.cases == 'Yes') {
                        $rootScope.passwordInformation += "<br>";
                        $rootScope.passwordInformation += "\u2022 " + vm.passwordCaseSensitivie;
                    }
                }
            }

            $rootScope.passwordStrengthValid = passwordStrengthValid;
            function passwordStrengthValid() {
                $rootScope.validPassword = false;
                var password = document.getElementsByName('password')[0].value;
                var meter = document.getElementById('password-strength-meter');
                var score = null;
                if ($rootScope.passwordProperties != null) {
                    loadPasswordMessage();
                    var minLength = parseInt($rootScope.passwordProperties.minLen);
                    var specialChar = $rootScope.passwordProperties.specialChar;
                    var cases = $rootScope.passwordProperties.cases;
                    score = evalPasswordScore(minLength, specialChar, cases, password);
                } else {
                    score = 4;
                    $rootScope.passwordInformation = vm.password;
                }
                if (score == 4) {
                    $rootScope.validPassword = true;
                }
                meter.value = score;
            }

            vm.passwordScore = null;
            function evalPasswordScore(minLength, specialChar, cases, password) {
                var len = password.length;
                var score = 0;
                if (minLength == null && (specialChar == null || specialChar == 'No') && (cases == null || cases == 'No')) {
                    score = 4;
                } else if (minLength != null && (specialChar == 'No' || specialChar == null) && (cases == 'No' || cases == null)) {
                    if (len >= minLength) score = 4;
                } else if (minLength != null && specialChar == 'Nb' && (cases == 'No' || cases == null)) {
                    if (len >= minLength) {
                        score = 2;
                    }
                    if (number.test(password)) {
                        score += 2;
                    }
                } else if (minLength != null && specialChar == 'Nbs' && (cases == 'No' || cases == null)) {
                    if (len >= minLength) {
                        score = 2;
                    }
                    if (number.test(password) && special.test(password)) {
                        score += 2;
                    }
                } else if (minLength != null && (specialChar == 'No' || specialChar == null) && (cases == 'Yes' || cases == null)) {
                    if (len >= minLength) {
                        score = 2;
                    }
                    if (upper.test(password) && lower.test(password)) {
                        score += 2;
                    }
                } else if (minLength != null && specialChar == 'Nb' && (cases == 'Yes' || cases == null)) {
                    if (len >= minLength) {
                        score = 2;
                    }
                    if (number.test(password)) {
                        score += 1;
                    }
                    if (upper.test(password) && lower.test(password)) {
                        score += 1;
                    }
                } else if (minLength != null && specialChar == 'Nbs' && (cases == 'Yes' || cases == null)) {
                    if (len >= minLength) {
                        score = 2;
                    }
                    if (number.test(password) && special.test(password)) {
                        score += 1;
                    }
                    if (upper.test(password) && lower.test(password)) {
                        score += 1;
                    }
                } else if (minLength == null && specialChar == 'Nb' && (cases == 'No' || cases == null)) {
                    if (number.test(password)) {
                        score = 4;
                    }
                } else if (minLength == null && specialChar == 'Nbs' && (cases == 'No' || cases == null)) {
                    if (number.test(password) && special.test(password)) {
                        score = 4;
                    }
                } else if (minLength == null && (specialChar == 'No' || specialChar == null) && (cases == 'Yes' || cases == null)) {
                    if (upper.test(password) && lower.test(password)) {
                        score = 4;
                    }
                } else if (minLength == null && specialChar == 'Nb' && (cases == 'Yes' || cases == null)) {
                    if (number.test(password)) {
                        score = 2;
                    }
                    if (upper.test(password) && lower.test(password)) {
                        score += 2;
                    }
                } else if (minLength == null && specialChar == 'Nbs' && (cases == 'Yes' || cases == null)) {
                    if (number.test(password) && special.test(password)) {
                        score = 2;
                    }
                    if (upper.test(password) && lower.test(password)) {
                        score += 2;
                    }
                }
                vm.passwordScore = score;
                return score;
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
                $timeout(function () {
                    positionNotificationPanel();
                    $http.get('application.json').success(function (data) {
                        vm.language = data.showLanguage;
                        $application.config = data;
                        /*resizeHeader();*/
                    });
                }, 500);

                $rootScope.$on("app.selectionItems.title", function (event, args) {
                    vm.leftSidePanelOptions.title = args.title;
                });
                $rootScope.$on("app.main.groups", buildMaps);
                $scope.$on('$viewContentLoaded', function () {
                    initNotificationPanel();
                    initEvents();
                    $(document).on('keydown', function (evt) {
                        if (evt.keyCode == 27) {
                            hideSidePanel('right');
                            hideSidePanel('left');
                        }
                    });
                    $(window).resize(function () {
                        resize();
                        /*resizeHeader();*/
                    });

                    $rootScope.$on('$stateChangeStart',
                        function (event, toState, toParams, fromState, fromParams, elm) {
                            hideSideNavbar();
                            vm.comments.show = false;
                            vm.viewInfo.description = "";
                            updateSidePanel(toState);
                            hideRightSidePanel();
                            hideNotificationPanel();
                            $rootScope.hideSidePanel('left');
                            vm.comments.show = false;

                            if (toState.name == "app.pm.project.tasks") {
                                $rootScope.showTaskNotification = true;
                            } else {
                                $rootScope.showTaskNotification = false;
                            }

                            if (toState.name == 'app.home' || toState.name == 'app.admin.usersettings' || toState.name == 'app.admin.security.sessions' || toState.name == 'app.settings'
                                || toState.name == 'app.help.feedback' || toState.name == 'app.help.main' || toState.name == 'app.admin.logindetails' || toState.name == 'app.document.all' || toState.name == 'app.workflow.all'
                                || toState.name == 'app.reports' || toState.name == 'app.store.inventory' || toState.name == 'app.store.supplier' || toState.name == 'app.store.newRequisition' || toState.name == 'app.store.requisitionDetails'
                                || toState.name == 'app.store.all' || (toState.name).indexOf('app.reports') != -1 || toState.name == 'app.store.details' || toState.name == 'app.issues'
                                || toState.name == 'app.contracts.contractors' || toState.name == 'app.contracts.workOrders' || toState.name == 'app.contracts.workOrderDetails') {
                                setViewType('APP');
                                var height = $(window).height();
                                $rootScope.selectedProject = null;
                                $rootScope.masterDataTab = false;
                                /*if (toState.name == "app.store.details") {
                                 $('#contentpanel').height(height - 247);
                                 } else {
                                 $('#contentpanel').height(height - 217);
                                 }*/

                            } else {
                                var height = $(window).height();
                                if (toState.name == 'app.proc.classification' || toState.name == 'app.proc.materials.all' || toState.name == 'app.proc.machines.all'
                                    || toState.name == 'app.proc.manpower.all') {
                                    $application.selectedProject = null;
                                    $rootScope.selectedProject = null;
                                    $rootScope.masterDataTab = true;
                                    //$('#contentpanel').height(height - 267);
                                } else {
                                    $rootScope.masterDataTab = false;
                                    /*if ($rootScope.selectedProject != null && $rootScope.selectedProject.locked) {
                                     $('#contentpanel').height(height - 297);
                                     } else {
                                     if ($rootScope.selectedProject != null) {
                                     $('#contentpanel').height(height - 267);
                                     } else {
                                     $('#contentpanel').height(height - 217);
                                     }

                                     }*/

                                }
                            }

                            if (sessionCheckPromise == null) {
                                initSessionCheck();
                            }
                        }
                    );
                });
                checkSession();
                loadPasswordStrength();
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
                                var adminRightSideView = $(elm).find("#admin-rightView").length > 0;
                                var tabContent = $(elm).find('.tab-content').length > 0;
                                var barHeight = $('#admin-rightView-bar').outerHeight();
                                var detailsFooter = $(elm).find("#detailsFooter").length > 0;
                                var navTabs = $(elm).find(".nav-tabs").height();

                                var subNav = $("#subNavigation").height();
                                var subNav1 = $("#subNavigation1").height();
                                var appHeight = $(window).height();
                                if (subNav) {
                                    $("#contentpanel").height(appHeight - 130);
                                } else if (subNav1) {
                                    $("#contentpanel").height(appHeight - 130);
                                } else {
                                    $("#contentpanel").height(appHeight - 80);
                                }

                                var height = $("#contentpanel").outerHeight();

                                if (hasToolbar) {
                                    if (detailsFooter) {
                                        $(elm).find('.view-content').height(height - (50 + navTabs));
                                    } else {
                                        $(elm).find('.view-content').height(height - 50);
                                        if (adminRightSideView) {
                                            if (barHeight != null) {
                                                $("#admin-rightView").height(height - barHeight);
                                            } else {
                                                $("#admin-rightView").height(height - 30);
                                            }
                                        }
                                    }
                                } else {
                                    $(elm).find('.view-content').height(height);
                                    if (adminRightSideView) {
                                        if (barHeight != null) {
                                            $("#admin-rightView").height(height - barHeight);
                                        } else {
                                            $("#admin-rightView").height(height - 30);
                                        }
                                    }
                                }

                                if (tabContent) {
                                    var height1 = $(".view-content").height();

                                    if (detailsFooter) {
                                        $('.tab-content').height(height1 - 40);
                                        $('.tab-pane').height(height1 - 40);
                                    } else {
                                        if (hasToolbar) {
                                            $('.tab-content').height(height1 - 70);
                                            $('.tab-pane').height(height1 - 70);
                                        } else {
                                            $('.tab-content').height(height1 - 20);
                                            $('.tab-pane').height(height1 - 20);
                                        }
                                    }
                                }

                                /*if (hasToolbar) {
                                 if (detailsFooter) {
                                 if (tabContent && subNav1 != null) {
                                 $(elm).find('.view-content').height(height - 204);
                                 } else if (tabContent && subNav != null) {
                                 $(elm).find('.view-content').height(height - 154);
                                 } else {
                                 $(elm).find('.view-content').height(height - 154);
                                 }
                                 } else {
                                 if (tabContent && subNav1 != null) {
                                 $(elm).find('.view-content').height(height - 164);
                                 } else if (tabContent && subNav != null) {
                                 $(elm).find('.view-content').height(height - 114);
                                 } else {
                                 $(elm).find('.view-content').height(height - 52);
                                 }
                                 }

                                 }
                                 else {
                                 if (detailsFooter) {
                                 $(elm).find('.view-content').height(height - 42);
                                 } else {
                                 $(elm).find('.view-content').height(height - 2);
                                 if (adminRightSideView) {
                                 $("#admin-rightView").height(height - 130);
                                 }
                                 }
                                 }


                                 if (tabContent) {
                                 var height1 = $("#contentpanel").height();

                                 if (detailsFooter) {
                                 if (subNav1 != null) {
                                 $('.tab-content').height(height1 - 204);
                                 $('.tab-pane').height(height1 - 204);
                                 } else {
                                 $('.tab-content').height(height1 - 137);
                                 $('.tab-pane').height(height1 - 137);
                                 }
                                 } else {
                                 if (subNav1 != null) {
                                 $('.tab-content').height(height1 - 174);
                                 $('.tab-pane').height(height1 - 174);
                                 } else {
                                 if (hasToolbar) {
                                 $('.tab-content').height(height1 - 124);
                                 $('.tab-pane').height(height1 - 124);
                                 } else {
                                 $('.tab-content').height(height1 - 64);
                                 $('.tab-pane').height(height1 - 64);
                                 }
                                 }

                                 }
                                 }*/
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