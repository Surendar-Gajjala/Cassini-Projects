define(['app/desktop/modules/main/main.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/authorizationService',
        'app/shared/services/taskService',
        'app/shared/services/projectService',
        'app/shared/services/personService'
    ],
    function (module, moment) {
        module.controller('MainController', MainController);

        function MainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application,
                                LoginService, CommonService, DialogService, TaskService, ProjectService, authorizationFactory, PersonService) {

            moment.tz.setDefault("Asia/Kolkata");
            window.moment = moment;

            $rootScope.viewInfo = {
                icon: 'fa-home',
                title: 'Home'
            };


            $rootScope.notification = {
                class: 'fa-check',
                type: "alert-success",
                message: ""
            };


            var vm = this;

            vm.tasks = tasks;
            vm.headerText = "ASSIGNED TASKS";
            vm.assignedTasks = [];
            vm.approvedTasks = [];
            vm.personRole = null;

            vm.loginRoles = ["Administrator", "Staff", "Supervisor", "Officer"];

            vm.user = {
                name: ""
            };

            vm.feedback = feedback;
            vm.showProfile = showProfile;
            vm.logout = logout;


            vm.filters = {
                project: null,
                name: null,
                description: null,
                status: null,
                assignedTo: null,
                verifiedBy: null,
                approvedBy: null,
                searchQuery: null,
                assignedDate: null
            };

            vm.pageable = {
                page: 1,
                size: 10,
                sort: {
                    label: "modifiedDate",
                    field: "modifiedDate",
                    order: "desc"
                }
            };

            vm.pagedResults = {
                content: [],
                last: false,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: false,
                numberOfElements: 0
            };

            function feedback() {
                $state.go('app.help.feedback');
            }

            function showProfile(login) {
                $state.go('app.admin.logindetails', {loginId: login.id});
            }

            $rootScope.hasRole = function (name) {
                var yes = false;
                if ($application.login != null) {
                    if (vm.personRole == name) {
                        yes = true;
                    }
                }
                return yes;
            };

            $rootScope.isAdmin = function () {
                return $application.login.loginName == 'admin';
            };

            function getPersonRoles(person) {
                PersonService.getRoleByPerson(person.id).then(
                    function (data) {
                        vm.personRole = data.role;
                        if (vm.personRole == "Staff" || vm.personRole == "Supervisor" || vm.personRole == "Officer") {
                            $state.go('app.task.all');
                        } else if (vm.personRole == "Administrator") {
                            $state.go('app.home');
                        }
                    }
                )
            }

            $rootScope.hasPermission = function (permission) {
                var has = false;
                if (app.authorizationFactory != null && permission != null && permission != undefined) {
                    if (app.authorizationFactory.hasPermission(permission)) {
                        has = true;
                    }
                }

                return has;
            };

            function checkSession() {
                LoginService.current().then(
                    function (session) {
                        if (session == null || session == "") {
                            $state.go('login');
                        }
                        else {
                            window.$application.session = session;
                            window.$application.login = session.login;
                            vm.user = session.login;
                            CommonService.initialize();
                            getPersonRoles(session.login.person);
                            if ($application.homeLoaded == false) {
                                $state.go('app.home');
                            }
                        }
                    }
                );
            }

            function loadAssignedTasks() {
                vm.filters.status = "ASSIGNED";
                TaskService.getListTasks(vm.filters).then(
                    function (data) {
                        vm.assignedTasks = data;
                    });
            }

            function tasks() {
                $state.get('app.task.all').data.status = "ASSIGNED";
            }


            function loadPendingTasks() {
                vm.filters.status = "APPROVED";
                TaskService.getListTasks(vm.filters).then(
                    function (data) {
                        vm.approvedTasks = data;
                    });
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

            $rootScope.closeNotification = function () {
                hideNotification();
            };

            $rootScope.showNotification = function (message, type) {
                $rootScope.notification.type = type;
                $rootScope.notification.message = message;
                showNotification();
            };

            $rootScope.showSuccessMessage = function (message) {
                $rootScope.notification.check = 'fa-check';
                $rootScope.showNotification(message, 'alert-success');
            };

            $rootScope.showErrorMessage = function (message) {
                $rootScope.notification.class = 'fa-ban';
                $rootScope.showNotification(message, 'alert-danger');
            };

            $rootScope.showWarningMessage = function (message) {
                $rootScope.notification.class = 'fa-warning';
                $rootScope.showNotification(message, 'alert-warning');
            };

            $rootScope.showInfoMessage = function (message) {
                $rootScope.notification.class = 'fa-info-circle';
                $rootScope.showNotification(message, 'alert-info');
            };

            (function () {
                if (window.$application == null || window.$application == undefined) {
                    window.$application = $application;
                }

                $scope.$on('$viewContentLoaded', function () {
                    initNotificationPanel();

                    $rootScope.$on('$stateChangeStart',
                        function (event, toState, toParams, fromState, fromParams) {
                            hideNotification();

                            $timeout(function () {
                                positionNotification();
                            }, 500);
                        }
                    );
                });
                checkSession();
                loadAssignedTasks();
                loadPendingTasks();
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
