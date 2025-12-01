define(['app/app.modules',
        'moment',
        'hopscotch',
        'app/components/main/notificationController',
        'app/components/home/widgets/orders/ordersWidgetController',
        'app/shared/directives/commonDirectives',
        'app/components/login/loginFactory',
        'app/components/admin/security/session/sessionFactory',
        'app/components/admin/security/authorizationFactory',
        'app/components/reporting/reportFactory',
        'app/shared/ui/modal/modalService',
        'app/shared/directives/windowResized',
        'app/components/common/commonFactory',
        'app/components/help/contextHelpController'
    ],
    function (app, moment, hopscotch) {
        app.controller('MainController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state',
                'sessionFactory', 'authorizationFactory', 'loginFactory', 'reportFactory',
                'modalService', 'commonFactory',

                function ($scope, $rootScope, $timeout, $interval, $state,
                          sessionFactory, authorizationFactory, loginFactory,
                          reportFactory, modalService, commonFactory) {


                    window.$("#appview").hide();

                    window.moment = moment;
                    window.hopscotch = hopscotch;

                    $rootScope.uiInited = false;
                    $scope.showInsideLogo = false;


                    $rootScope.animationType = "";
                    $rootScope.showMessage = false;

                    $rootScope.iconClass = 'fa fa-home';

                    $rootScope.viewTitle = "";
                    $scope.toolbarTemplate = "main-view-tb";

                    $scope.sessionCheckPromise = null;
                    $scope.sessionExpiredDialogOpen = false;
                    $scope.fullScreen = false;
                    $scope.showHelpIcon = true;

                    $scope.templates = {
                        newOrders: "app/components/main/newOrdersNotification.jsp",
                        newReturns: "app/components/main/newReturnsNotification.jsp",
                        help: 'app/components/help/contextHelpView.jsp',
                        topnav: 'app/components/main/topNavigation.jsp'
                    };

                    $rootScope.hasRole = function (name) {
                        var yes = false;

                        if (app.login != null) {
                            angular.forEach(app.login.roles, function (role) {
                                if (role.name == name) {
                                    yes = true;
                                }
                            });
                        }
                        return yes;
                    };

                    $rootScope.hasPermission = function (permission) {
                        var has = false;
                        if (app.authorizationFactory != null &&
                            permission != null &&
                            permission != undefined) {
                            if (app.authorizationFactory.hasPermission(permission)) {
                                has = true;
                            }
                        }
                        return has;
                    };

                    $rootScope.lapsedTime = function (timestamp) {
                        return moment(timestamp, "DD/MM/YYYY, HH:mm:ss").fromNow();
                    };

                    $rootScope.setToolbarTemplate = function (tbTemplate) {
                        $scope.toolbarTemplate = tbTemplate;
                    };

                    $scope.toggleSidePanel = function () {
                        $scope.fullScreen = !$scope.fullScreen;
                        $scope.showInsideLogo = !$scope.showInsideLogo;
                        window.toggleMainPanelFullScreen();
                        $rootScope.$broadcast("windowResize");
                    };

                    $scope.hideHelpIcon = function () {
                        $scope.showHelpIcon = false;
                    };

                    $scope.toggleWorkspace = function () {
                        $scope.fullScreen = !$scope.fullScreen;
                        window.toggleWorkspaceFullScreen();
                        $rootScope.$broadcast("windowResize");
                    };

                    $scope.showCustomerDetails = function (customer) {
                        $state.go('app.crm.customer', {customerId: customer.id})
                    };

                    $scope.showSalesRep = function (salesRep) {
                        $state.go('app.crm.salesrep', {salesRepId: salesRep.id});
                    };

                    loginFactory.current().then(
                        function (session) {
                            if (session == null || session == "") {
                                $state.go('login');
                            }
                            else {
                                app.session = session;
                                app.login = session.login;

                                authorizationFactory.initialize(session);
                                app.authorizationFactory = authorizationFactory;

                                commonFactory.initialize();


                                $rootScope.$broadcast("app.loggedIn", {});
                                $scope.initializeApp();
                            }
                        }
                    );

                    $scope.hasShoppingCart = function () {
                        return (app.shoppingCart != null);
                    };

                    $scope.getShoppingCartSize = function () {
                        if (app.shoppingCart != null)
                            return app.shoppingCart.items.length;
                        else
                            return 0;
                    };

                    $scope.showShoppingCart = function () {
                        if (app.shoppingCart != null) {
                            if (app.shoppingCart.orderNumber != null && app.shoppingCart.orderNumber != undefined) {
                                app.shoppingCart.details = angular.copy(app.shoppingCart.items);
                                $state.go('app.crm.orders.details', {orderId: app.shoppingCart.id});
                            }
                            else {
                                $state.go('app.crm.orders.cart');
                            }
                        }
                        else {
                            $state.go('app.crm.orders.cart');
                        }
                    };

                    $scope.hasReturnCart = function () {
                        return (app.returnCart != null);
                    };

                    $scope.getReturnCartSize = function () {
                        if (app.returnCart != null)
                            return app.returnCart.items.length;
                        else
                            return 0;
                    };

                    $scope.createOrder = function (customer, type) {
                        if (customer == null || customer.blacklisted == false) {
                            app.shoppingCart = {
                                items: [],
                                poNumber: null,
                                billingAddress: null,
                                shippingAddress: null,
                                shipTo: null,
                                notes: null,
                                contactPhone: null,
                                customer: null,
                                orderTotal: null,
                                netTotal: null

                            };

                            if (customer != null && customer != undefined) {
                                app.shoppingCart.customer = customer;
                            }

                            $state.go('app.crm.orders.cart');

                            if (type != null && type != undefined &&
                                (type == 'PRODUCT' || type == 'SAMPLE')) {
                                app.shoppingCart.orderType = type;
                            }
                            else {
                                app.shoppingCart.orderType = "PRODUCT";
                            }
                        }
                        else {
                            $rootScope.showErrorMessage("This customer has been blacklisted");
                        }
                    };

                    $scope.showReturnCart = function () {
                        $state.go('app.crm.returns.new');
                    };

                    $scope.toggleHelp = function () {
                        window.toggleHelp();
                    };

                    $scope.initializeApp = function () {
                        $scope.userName = app.login.person.firstName;


                        if (app.login.person.lastName != null &&
                            app.login.person.lastName != "") {
                            $scope.userName += " " + app.login.person.lastName;
                        }


                        $rootScope.notification = {
                            class: 'fa-check',
                            type: "alert-success",
                            message: ""
                        };

                        $rootScope.$on("$locationChangeStart", function (event, next, current) {
                            //console.log('Current: ' + current + '  Next: ' + next);
                        });

                        $rootScope.$on('$stateChangeStart', function (event, toState) {
                            $scope.toolbarTemplate = "main-view-tb";
                            //event.preventDefault();
                            //console.log('Stopped...');
                        });


                        function initUI() {
                            app.uiInited = true;

                            initPreloader();
                            initPanels();
                            //initLeftPanel();
                            initRightPanel();
                            initToolTipAndPopover();
                            initToggles();
                            initModals();

                            repositionTopNav();
                            repositionSearchForm();
                            initResizeEvent();
                            initBody();
                        }

                        $scope.logout = function () {
                            var modalDefaults = {
                                templateUrl: 'app/shared/ui/modal/modalTemplate.html?nd=' + Date.now()
                            };

                            var modalOptions = {
                                closeButtonText: 'No',
                                actionButtonText: 'Yes',
                                headerText: 'Logout',
                                bodyText: 'Are you sure you want to logout?'
                            };

                            modalService.showModal(modalDefaults, modalOptions).then(function (result) {
                                loginFactory.logout().then(
                                    function (success) {
                                        $rootScope.performLogout();
                                    },

                                    function (error) {
                                        console.error(error);
                                    }
                                );
                            });
                        };

                        $rootScope.performLogout = function () {
                            app.loggedIn = false;
                            app.navLoaded = false;
                            app.uiInited = false;
                            if ($scope.sessionCheckPromise != null) {
                                $interval.cancel($scope.sessionCheckPromise);
                            }
                            $rootScope.$broadcast("app.logout");
                            $state.go('login', {}, {reload: true});
                        };

                        function loadCountriesAndStates() {

                        }

                        $scope.showTour = function () {
                            var tour = {
                                id: "cassini-tour",
                                steps: [
                                    {
                                        title: "Navigation",
                                        content: "This is the navigation bar, where you will interact to move from one function to the other.",
                                        target: "headerbar",
                                        xOffset: 'center',
                                        arrowOffset: 'center',
                                        placement: "bottom"
                                    },
                                    {
                                        title: "Home",
                                        content: "This is the home buttom. You can come back to the home view from anywhere in the app by clicking this button.",
                                        target: "nav-home",
                                        xOffset: 'center',
                                        arrowOffset: 'center',
                                        placement: "bottom"
                                    },
                                    {
                                        title: "Header",
                                        content: "This is the header bar where you will see the current view name.",
                                        target: "viewTitleContainer",
                                        xOffset: 'center',
                                        arrowOffset: 'center',
                                        placement: "bottom"
                                    },
                                    {
                                        title: "Content",
                                        content: "This is the application content area where most of the interaction happens.",
                                        target: "appcontent",
                                        xOffset: 'center',
                                        arrowOffset: 'center',
                                        placement: "top"
                                    }
                                ]
                            };

                            window.hopscotch.startTour(tour);
                        };

                        $scope.showProfile = function () {
                            $state.go('app.hrm.employee', {employeeId: app.session.login.person.id});
                        };

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

                        $rootScope.$on('$stateChangeStart',
                            function (event, toState, toParams, fromState, fromParams) {
                                hideNotification();

                                if (toState.name == 'app.help') {
                                    $scope.showHelpIcon = false;
                                    window.hideHelp();
                                }
                                else {
                                    $scope.showHelpIcon = true;
                                }

                                /*
                                 Permission check here

                                 if(toState.name == 'app.crm.customers') {
                                 $timeout(function() {
                                 $rootScope.showWarningMessage("You do not have permission to view this information");
                                 }, 100);
                                 event.preventDefault();
                                 }
                                 */
                            }
                        );

                        $scope.initSessionCheck = function () {
                            $scope.sessionCheckPromise = $interval(function () {
                                $scope.checkSession();
                            }, 1 * 60 * 1000);
                        };

                        $scope.checkSession = function () {
                            if ($scope.sessionExpiredDialogOpen == false) {
                                sessionFactory.isSesstionActive().then(
                                    function (session) {
                                        if (session.active == false) {
                                            if ($scope.sessionCheckPromise != null) {
                                                $interval.cancel($scope.sessionCheckPromise);
                                            }

                                            $rootScope.$broadcast('cassini.session.expired');

                                            var modalDefaults = {
                                                templateUrl: 'app/shared/ui/modal/modalTemplate.html?nd=' + Date.now()
                                            };

                                            var modalOptions = {
                                                closeButtonText: 'Cancel',
                                                hideCloseButton: true,
                                                actionButtonText: 'OK',
                                                headerText: 'Logout',
                                                bodyText: 'Your session has expired. Please login again.'
                                            };
                                            $scope.sessionExpiredDialogOpen = true;

                                            modalService.showModal(modalDefaults, modalOptions).then(function (result) {
                                                app.loggedIn = false;
                                                app.navLoaded = false;
                                                app.uiInited = false;

                                                $scope.sessionExpiredDialogOpen = false;
                                                $state.go('login', {}, {reload: true});
                                            });
                                        }
                                    }
                                );
                            }
                        };

                        if (app.uiInited == false) {
                            console.log("UI initializing...");
                            initUI();

                            $scope.initSessionCheck();

                            $rootScope.$broadcast("uiInited");
                            reportFactory.loadReports();

                            if (!app.homeLoaded) {
                                $state.go('app.home');
                            }
                            else {
                                console.log("Showing app view");
                                window.$("#appview").show();
                            }
                        }
                    };
                }
            ]
        );

    }
);