define(
    [
        'app/desktop/modules/main/main.module',
        'moment',
        'moment-timezone-with-data',
        'app/desktop/modules/main/searchBarController',
        'app/assets/bower_components/cassini-platform/app/shared/filters/filters',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/main/sidePanelsController',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application',
        'app/desktop/modules/shared/comments/commentsButtonDirective',
        'app/desktop/modules/shared/comments/commentsBtnDirective',
        'app/desktop/modules/shared/comments/commentsBtnDirective',
        'app/shared/services/core/procurementService',
        'app/shared/services/core/bomService'
    ],
    function (module, moment) {
        module.controller('MainController', MainController);

        function MainController($scope, $rootScope, $timeout, $interval, $state, $location, $application, $translate, $window,
                                LoginService, DialogService, CommonService, ProcurementService, BomService) {
            $rootScope.viewInfo = {
                icon: 'fa-home',
                title: 'Home',
                description: ""
            };

            $rootScope.$application = $application;

            $application.urlParams = getQueryParams();

            moment.tz.setDefault("Asia/Kolkata");
            window.moment = moment;

            var vm = this;

            vm.viewInfo = $rootScope.viewInfo;
            vm.selectedWidgets = [];
            vm.showHomePage = showHomePage;

            vm.currentVariant = {
                weight: null,
                price: null,
                deliveryDate: null
            };

            vm.comments = {
                show: false,
                objectType: null,
                objectId: null
            };

            vm.statuses = [];

            function showHomePage() {
                $state.go('app.home');
            }


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
            var permissionsMap = new Hashtable();


            vm.logout = logout;
            vm.feedback = feedback;
            vm.showProfile = showProfile;
            vm.stopPropagation = stopPropagation;
            $rootScope.hideToggleNode = false;
            vm.currentState = $state.current.name;
            $rootScope.hideToggleNode = false;
            vm.lifecyclePhases = [];
            $rootScope.personUnreadMessages = 0;

            var parsed = angular.element("<div></div>");

            function stopPropagation(event) {
                event.stopPropagation();
            }

            $rootScope.setStatuses = function (statuses) {
                vm.statuses = statuses;
            };

            function checkSession() {
                window.$application.session = null;
                window.$application.login = null;
                window.$application.foldersData = null;
                $rootScope.loginPersonDetails = null;

                LoginService.current().then(
                    function (session) {
                        if (session == null || session == "") {
                            $state.go('login');
                        }
                        else {
                            loadDrdoImage();
                            $rootScope.personPermissions = [];
                            permissionsMap = new Hashtable();
                            angular.forEach(session.login.groupPermissions, function (groupPermission) {
                                $rootScope.personPermissions.push(groupPermission);
                                permissionsMap.put(groupPermission.id, groupPermission);
                            });
                            hidePreloader();
                            //buildMaps(session);
                            window.$application.session = session;
                            window.$application.login = session.login;
                            vm.user = session.login;
                            $rootScope.loginPersonDetails = session.login;

                            CommonService.initialize();

                            if ($application.homeLoaded == false) {
                                $state.go('app.home');
                            }
                            if ($rootScope.loginPersonDetails.isSuperUser) {
                                $timeout(function () {
                                    BomService.updateUniqueCodes().then(
                                        function (data) {

                                        }
                                    )
                                }, 5000)
                            }
                        }
                    }
                );
            }

            $rootScope.drdoImage = null;
            function loadDrdoImage() {
                BomService.getDrdoImage().then(
                    function (data) {
                        $rootScope.drdoImage = data;
                    }
                )
            }

            $scope.toggleAdmin = function () {
                $('.uib-dropdown-menu').toggle();
            };

            function feedback() {
                $state.go('app.help.feedback');
            }

            vm.help = help;
            function help() {
                $state.go('app.help.main')
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
                });
            }

            $rootScope.personPermissions = [];
            function buildMaps(session) {
                $rootScope.personPermissions = [];
                permissionsMap = new Hashtable();
                var groups = session.login.groups;
                angular.forEach(groups, function (group) {
                    //groupsMap.put(group.name, group);
                    buildPermissionsMap(group.permissions);
                });
            }

            function buildPermissionsMap(permissions) {
                angular.forEach(permissions, function (permission) {
                    permissionsMap.put(permission.id, permission);
                    $rootScope.personPermissions.push(permission);
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

            $rootScope.hasPermission = function (permission) {
                var has = false;
                if (permission != null && permission != undefined) {
                    if (getPermission(permission)) {
                        has = true;
                    }
                }
                return has;
            };

            $rootScope.showExportMessage = function (message) {
                $rootScope.notification.class = 'fa fa-spinner fa-spin fa-2x';
                $rootScope.showNotification(message, 'alert-info');
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

            $rootScope.showBusyIndicator = function (parent) {
                var w = null;
                var h = null;
                if (parent != null && parent != undefined) {
                    var pos = $(parent).offset();
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

            function getQueryParams() {
                var search = $window.location.search.substring(1);

                if (search != null && search != "")
                    return JSON.parse('{"' + decodeURI(search).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g, '":"') + '"}');
                else
                    return {};
            }

            // Toggle Left Menu
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


                //Double Shift key press event
                $('body').dbKeypress(16, {
                    eventType: 'keyup',
                    callback: function () {
                        $scope.toggleSideNav()
                    }
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

            $rootScope.showUpnHistory = showUpnHistory;
            function showUpnHistory(itemInstance, side) {
                $rootScope.hideSidePanel();
                var options = {
                    title: itemInstance.upnNumber + " Details",
                    template: 'app/desktop/modules/inward/details/history/upnHistoryView.jsp',
                    controller: 'UpnHistoryController as upnHistoryVm',
                    resolve: 'app/desktop/modules/inward/details/history/upnHistoryController',
                    width: 600,
                    side: side,
                    data: {
                        statusItemInstance: itemInstance
                    },
                    buttons: [],
                    callback: function (data) {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            $rootScope.showLotUpnHistory = showLotUpnHistory;
            function showLotUpnHistory(itemInstance, mode) {
                $rootScope.hideSidePanel();
                var options = {
                    title: "Details",
                    template: 'app/desktop/modules/inward/details/history/lotUpnHistoryView.jsp',
                    controller: 'LotUpnHistoryController as lotUpnHistoryVm',
                    resolve: 'app/desktop/modules/inward/details/history/lotUpnHistoryController',
                    width: 600,
                    showMask: true,
                    data: {
                        statusItemInstance: itemInstance,
                        lotUpnHistoryMode: mode
                    },
                    buttons: [],
                    callback: function (data) {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            $rootScope.showUpdates = showUpdates;
            function showUpdates() {
                if ($rootScope.loginPersonDetails != null) {
                    ProcurementService.getUpdatesByPerson($rootScope.loginPersonDetails.person.id).then(
                        function (data) {
                            vm.drdoUpdates = data;
                            var options = {
                                width: 500
                            };

                            var workspace = $('#workspace');
                            var sidePanel = $('#updatesPanel');
                            sidePanel.show();
                            var sidePanelContent = $('#updates-content');
                            sidePanelContent.height($("#workspace").height() - 30);

                            var sidePanelContent1 = $('#updates-content').outerHeight();

                            var content = $('#updates');
                            content.height(sidePanelContent1 - 50);

                            $('#updates-content').width(options.width);
                            $('#updates-content').css({'margin-right': "-" + options.width + "px"});
                            $('#updates-content').animate({"margin-right": '+=' + options.width}, 'slow', function () {

                            });
                            $("#updates").hide().fadeIn('fast');
                        }
                    )
                }
            }

            $rootScope.unreadMessages = unreadMessages;

            function unreadMessages() {
                var slider_width = $('#updates-content').width();
                $('#updates-content').animate({"margin-right": '-=' + slider_width}, 'slow',
                    function () {
                        var model = document.getElementById("updatesPanel");
                        model.style.display = "none";
                    }
                );
            }

            $rootScope.deleteUpdates = deleteUpdates;
            function deleteUpdates() {
                var slider_width = $('#updates-content').width();
                $('#updates-content').animate({"margin-right": '-=' + slider_width}, 'slow',
                    function () {
                        var model = document.getElementById("updatesPanel");
                        model.style.display = "none";
                    }
                );

                ProcurementService.deleteUpdates($rootScope.loginPersonDetails.person.id).then(
                    function (data) {
                        vm.drdoUpdates = [];
                    }
                )
            }

            vm.updateMessage = updateMessage;

            function updateMessage(update) {
                update.read = true;
                ProcurementService.updateMessage(update).then(
                    function (data) {
                        $rootScope.personUnreadMessages = $rootScope.personUnreadMessages - 1;
                    }
                )
            }

            $rootScope.requestFilter = {
                notification: false,
                status: null,
                searchQuery: null,
                issued: false,
                fromDate: null,
                toDate: null,
                requested: false,
                report: false,
                month: null
            };

            (function () {
                if (window.$application == null || window.$application == undefined) {
                    window.$application = $application;
                }

                initEvents();

                $scope.$on('$viewContentLoaded', function () {
                    initNotificationPanel();

                    $rootScope.$on('$stateChangeStart',
                        function (event, toState, toParams, fromState, fromParams) {
                            vm.statuses = [];
                            hideSideNavbar();

                            vm.comments.show = false;
                            vm.viewInfo.description = "";
                            vm.currentState = toState.name;
                            vm.lifecyclePhases = [];
                            $rootScope.searchModeType = false;
                            hideNotification();
                            $rootScope.hideBusyIndicator();

                            $timeout(function () {
                                positionNotification();
                            }, 500);
                            var height = $(window).height();
                            $('#contentpanel').height(height - 155);

                        }
                    );

                    if (sessionCheckPromise == null) {
                        initSessionCheck();
                    }

                    $timeout(function () {
                        $('#headerbar').click(function (event) {
                            event.stopPropagation();
                            event.preventDefault();
                        });
                    }, 1000);
                });

                checkSession();
                $scope.$on('$viewContentLoaded', function () {
                    $timeout(function () {
                        //loadWidgets();
                        $application.homeLoaded = true;
                        window.$("#appview").show();
                    }, 500);
                });
            })();
        }

        /*module.directive('requestStatus', ['$compile', '$timeout', function ($compile, $timeout) {
         return {
         template: "<span class='text-upper label' " +
         "ng-class=\"{'label-warning': request.status == 'BDL_MANAGER' || request.newStatus == 'BDL_MANAGER' || request.status == 'VERSITY_MANAGER' || request.newStatus == 'VERSITY_MANAGER', " +
         "'label-primary': request.status == 'CAS_MANAGER' || request.newStatus == 'CAS_MANAGER' || request.newStatus == 'CORRECTION'," +
         "'label-success': request.status == 'APPROVED' || request.newStatus == 'APPROVED' || request.status == 'PARTIALLY_APPROVED' || request.newStatus == 'PARTIALLY_APPROVED'," +
         "'label-default': request.status == 'BDL_EMPLOYEE' || request.newStatus == 'BDL_EMPLOYEE' || request.status == 'VERSITY_EMPLOYEE' || request.newStatus == 'VERSITY_EMPLOYEE'," +
         "'label-info': request.status == 'PARTIALLY_ACCEPTED' || request.newStatus == 'PARTIALLY_ACCEPTED'," +
         "'label-danger': request.status == 'REJECTED' || request.newStatus == 'REJECTED'}\">" +
         "{{request.status || request.newStatus}}" +
         "</span>",
         restrict: 'E',
         replace: true,
         scope: {
         'request': '='
         },
         link: function (scope, elem, attrs) {

         }
         };
         }]);*/

        module.directive('requestStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning': request.status == 'REQUESTED' || request.newStatus == 'REQUESTED', " +
                "'label-success': request.status == 'APPROVED' || request.newStatus == 'APPROVED'}\">" +
                "{{request.status || request.newStatus}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'request': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('issueStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning': issue.status == 'BDL_QC' || issue.status == 'BDL_PPC' || issue.status == 'VERSITY_QC' || issue.status == 'VERSITY_PPC', " +
                "'label-success': issue.status == 'RECEIVED' || issue.status == 'PARTIALLY_RECEIVED'," +
                "'label-danger': issue.status == 'REJECTED' || issue.status == 'PARTIALLY_REJECTED'," +
                "'label-info': issue.status == 'ITEM_RESET'," +
                "'label-primary': issue.status == 'PARTIALLY_APPROVED'}\">" +
                "{{issue.status}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'issue': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);


        module.directive('inwardStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning': inward.status == 'STORE' || inward.newStatus == 'STORE', " +
                "'label-primary': inward.status == 'SECURITY' || inward.newStatus == 'SECURITY'," +
                "'label-success': inward.status == 'SSQAG' || inward.newStatus == 'SSQAG'," +
                "'label-info': inward.status == 'INVENTORY' || inward.newStatus == 'INVENTORY'," +
                "'label-danger': inward.status == 'FINISH' || inward.newStatus == 'FINISH'}\">" +
                "{{inward.status || inward.newStatus}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'inward': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('itemInstanceStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning': object.status == 'P_ACCEPT' || object.status == 'P_APPROVED' || object.status == 'APPROVED', " +
                "'label-default': object.status == 'REVIEW' || object.status == 'NEW'," +
                "'label-lightblue': object.status == 'VERIFIED'," +
                "'label-info': object.status == 'TESTED' || object.status == 'INVENTORY'," +
                "'label-success': object.status == 'ACCEPT' || object.status == 'DISPATCHED' || object.status == 'ISSUE'," +
                "'label-primary': object.status == 'STORE_SUBMITTED' || object.status == 'REVIEWED' || object.status == 'DISPATCH' || object.status == 'RECEIVED'," +
                "'label-danger': object.status == 'FAILURE' || object.status == 'FAILURE_PROCESS' || object.status == 'REJECTED'}\">" +
                "{{object.presentStatus}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'object': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('bomGroupType', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning': object.type == 'SUBSYSTEM', " +
                "'label-success': object.type == 'UNIT'," +
                "'label-danger': object.type == 'COMMONPART'," +
                "'label-primary': object.type == 'SECTION'}\">" +
                "{{object.type}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'object': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);


        module.directive('issueItemStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-warning': item.status == 'P_APPROVED' || item.status == 'APPROVED', " +
                "'label-success': item.status == 'RECEIVED'," +
                "'label-danger': item.status == 'REJECTED'," +
                "'label-primary': item.status == 'PENDING'}\">" +
                "{{item.status}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'item': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('requestItemStatus', ['$compile', '$timeout', function ($compile, $timeout) {
            return {
                template: "<span class='text-upper label' " +
                "ng-class=\"{'label-success': item.status == 'APPROVED', " +
                "'label-warning': item.status == 'ACCEPTED'," +
                "'label-primary': item.status == 'PENDING'," +
                "'label-danger': item.status == 'REJECTED'}\">" +
                "{{item.status}}" +
                "</span>",
                restrict: 'E',
                replace: true,
                scope: {
                    'item': '='
                },
                link: function (scope, elem, attrs) {

                }
            };
        }]);

        module.directive('fitcontent', ['$compile', '$timeout',
                function ($compile, $timeout) {
                    return {
                        restrict: 'A',
                        link: function ($scope, elm, attr) {
                            $(window).resize(function () {
                                adjustHeight()
                            });


                            function adjustHeight() {
                                var height = $(window).height();
                                $("#contentpanel").height(height - 155);
                                var hasToolbar = $(elm).find('.view-toolbar').length > 0;

                                var tabContent = $(elm).find('.tab-content').length > 0;
                                var adminRightSideView = $(elm).find("#admin-rightView").length > 0;
                                var barHeight = $('#admin-rightView-bar').outerHeight();
                                var detailsFooter = $(elm).find("#detailsFooter").length > 0;

                                var contentPanel = $("#contentpanel").outerHeight();
                                //$(elm).height(height-5);
                                if (hasToolbar) {
                                    if (detailsFooter) {
                                        $(elm).find('.view-content').height(contentPanel - 104);
                                    } else {
                                        $(elm).find('.view-content').height(contentPanel - 50);
                                    }

                                }
                                else {
                                    if (detailsFooter) {
                                        $(elm).find('.view-content').height(contentPanel - 54);
                                    } else {
                                        $(elm).find('.view-content').height(contentPanel);
                                        if (adminRightSideView) {
                                            if (barHeight != null) {
                                                $("#admin-rightView").height(contentPanel - 130);
                                            } else {
                                                $("#admin-rightView").height(contentPanel - 80);
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
