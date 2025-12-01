define(['app/app.modules',
        'app/components/admin/security/session/sessionFactory'
    ],
    function (app) {
        app.controller('SessionDetailsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams',
                'sessionFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $stateParams,
                          sessionFactory) {

                    $rootScope.iconClass = "fa fa-lock";
                    $rootScope.viewTitle = "Session (" + $stateParams.sessionId + ")";

                    $scope.session = {
                        loginTime: "&nbsp;",
                        logoutTime: "&nbsp;"
                    };
                    $scope.history = [];

                    sessionFactory.getSession($stateParams.sessionId).then (
                        function(session) {
                            if(session.logoutTime == null || session.logoutTime == "") {
                                session.logoutTime = "-";
                            }
                            $scope.session = session;
                            return sessionFactory.getSession($stateParams.sessionId);
                        },

                        function(error) {
                            console.error(error);
                        }
                    ).then (
                        function(history) {
                            $scope.history = history;
                        },

                        function(error) {
                            console.error(error);
                        }
                    );
                }
            ]
        );
    }
);