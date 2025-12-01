define(['app/app.modules',
        'moment',
        'app/components/common/commonFactory',
        'app/components/login/loginFactory',
        'app/components/admin/security/session/sessionFactory',
        'app/components/admin/security/authorizationFactory',
        'app/components/crm/order/orderFactory'
    ],
    function ($app, moment) {
        $app.controller('MainController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                '$mdDialog', '$mdSidenav', 'commonFactory',
                'loginFactory', 'sessionFactory', 'authorizationFactory', 'orderFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          $mdDialog, $mdSidenav, commonFactory,
                          loginFactory, sessionFactory, authorizationFactory, orderFactory) {
                    window.moment = moment;

                    $rootScope.viewName = "Cassini.ERP";
                    $rootScope.backgroundColor = "#1565c0";


                    $scope.login = null;
                    $scope.loggedIn = false;
                    $scope.mobileDevice = null;

                    $rootScope.lapsedTime = function(timestamp) {
                        return moment(timestamp, "DD/MM/YYYY, HH:mm:ss").fromNow();
                    };

                    $rootScope.dateOnly = function (timestamp) {
                        return moment(timestamp, "DD/MM/YYYY, HH:mm:ss").format('DD/MM/YYYY');
                    };

                    $scope.toggleSidenav = function(menuId) {
                        $mdSidenav(menuId).toggle();
                    };

                    $scope.showHome = function() {
                        $state.go('app.home');
                    };

                    $scope.showOrders = function() {
                        $timeout(function() {
                            $state.go('app.crm.orders');
                        }, 500);
                    };

                    $scope.showProducts = function() {
                        $timeout(function() {
                            $state.go('app.crm.products');
                        }, 500);
                    };

                    $scope.showCustomers = function() {
                        $timeout(function() {
                            $state.go('app.crm.customers');
                        }, 500);
                    };

                    $rootScope.showOrderDetails = function(order) {
                        $state.go('app.crm.order', {orderId: order.id});
                    };

                    $rootScope.callPhoneNumber = function(phoneNumber) {
                        App2AndroidBridge.callPhoneNumber(phoneNumber);
                    };

                    $rootScope.addToContacts = function(person) {
                        App2AndroidBridge.addToContacts(person);
                    };

                    $rootScope.isPhoneAvailable = function() {
                        return App2AndroidBridge.isPhoneAvailable();
                    };

                    $scope.logout = function() {
                        var confirm = $mdDialog.confirm()
                            .title('Logout')
                            .content('Are you sure you want to logout?')
                            .ariaLabel('Logout')
                            .ok('Yes')
                            .cancel('No');

                        $mdDialog.show(confirm).then(
                            function() {
                                loginFactory.logout().then(
                                    function() {
                                        App2AndroidBridge.logout();
                                        $state.go('login', {fromapp: 'true'}, { reload: true });
                                    }
                                )
                            },
                            function() {

                            }
                        );
                    };


                    $scope.showAlert = function(event, message) {
                        // Appending dialog to document.body to cover sidenav in docs app
                        // Modal dialogs should fully cover application
                        // to prevent interaction outside of dialog
                        $mdDialog.show(
                            $mdDialog.alert()
                                .parent(angular.element(document.querySelector('#loginContainer')))
                                .clickOutsideToClose(true)
                                .title('Cassini.ERP')
                                .content(message)
                                .ariaLabel('Cassini.ERP')
                                .ok('OK')
                                .targetEvent(event)
                        );
                    };

                    $rootScope.approveOrder = function(order) {
                        orderFactory.approveOrder(order.id).then(
                            function(data) {
                                order.status = data.status;
                                $scope.showAlert(event, "Order is approved");
                            }
                        );
                    };

                    $rootScope.cancelOrder = function(order) {
                        orderFactory.cancelOrder(order.id).then(
                            function(data) {
                                order.status = data.status;
                                $scope.showAlert(event, "Order is canceled");
                            }
                        );
                    };

                    $rootScope.removeHold = function(order) {
                        order.onhold = false;
                        orderFactory.updateOrder(order).then (
                            function(data) {
                                order.status = data.status;
                                $scope.showAlert(event, "Removed hold on order");
                            }
                        );
                    };

                    $rootScope.holdOrder = function(order) {
                        orderFactory.getOrder(order.id).then(
                            function(data) {
                                order = data;
                                order.onhold = true;
                                orderFactory.updateOrder(order).then (
                                    function(data) {
                                        order.status = data.status;
                                        $scope.showAlert(event, "Order put on hold");
                                    }
                                );
                            }
                        );
                    };

                    $scope.disableNotification = function() {
                        loginFactory.disablePushNotification($app.session.id).then(
                            function(data) {
                                $scope.showAlert(event, "Push notification is disabled");
                            }
                        )
                    };

                    $scope.enableNotification = function() {
                        loginFactory.enablePushNotification($app.session.id).then(
                            function(data) {
                                $scope.showAlert(event, "Push notification is enable");
                            }
                        )
                    };

                    (function() {
                        loginFactory.current().then (
                            function(session) {
                                if(session == null || session == "") {
                                    $state.go('login');
                                }
                                else {
                                    $app.session = session;
                                    $app.login = session.login;
                                    $scope.login = session.login;
                                    $scope.mobileDevice = session.mobileDevice;

                                    authorizationFactory.initialize(session);
                                    $app.authorizationFactory = authorizationFactory;

                                    commonFactory.initialize();

                                    if(!$app.homeLoaded) {
                                        $state.go('app.home');
                                    }
                                    else {
                                        console.log("Showing app view");
                                        window.$("#appview").show();
                                    }

                                    Android2App.approveOrder = function(json) {
                                        $rootScope.approveOrder(JSON.parse(json));
                                    };
                                    Android2App.cancelOrder = function(json) {
                                        $rootScope.cancelOrder(JSON.parse(json))
                                    };
                                    Android2App.holdOrder = function(json) {
                                        $rootScope.holdOrder(JSON.parse(json));
                                    };
                                    Android2App.showOrder = function(json) {
                                        $rootScope.showOrderDetails(JSON.parse(json));
                                    };

                                    $scope.loggedIn = true;
                                }
                            }
                        );
                    })();
                }
            ]
        );
    }
);