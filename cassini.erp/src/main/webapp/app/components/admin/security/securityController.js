define(['app/app.modules'
    ],
    function (app) {
        app.controller('SecurityController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams',

                function ($scope, $rootScope, $timeout, $interval, $state, $stateParams) {
                    $scope.$state = $state;


                    $rootScope.iconClass = "fa fa-lock";
                    $rootScope.viewTitle = "Security";

                    $scope.tabs = [
                        { heading: 'Logins', active: true, state: 'app.admin.security.logins', view: 'logins' },
                        { heading: 'Roles & Permissions', active: false, state: 'app.admin.security.roles', view: 'roles' },
                        { heading: 'User Sessions', active: false, state: 'app.admin.security.sessions', view: 'sessions' }
                    ];

                    $scope.setTabActive = function(tab) {
                        if(tab.state != null) {
                            $state.go(tab.state);

                        }
                    };

                    $scope.setActiveFlag = function (index) {
                        angular.forEach($scope.tabs, function(t) {
                            t.active = false;
                        });
                        for(var i=0; i<$scope.tabs.length; i++) {
                            if(i == index) {
                                $scope.tabs[i].active = true;
                            }
                        }

                    };

                }
            ]
        );
    }
);