define(['app/app.modules', 'app/components/hrm/attendance/timeoffs/timeoffsFactory', 'app/shared/constants/constants'],
    function ($app) {
        $app.controller('TimeoffsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', 'timeoffsFactory', 'CONSTANTS',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies, timeoffsFactory, CONSTANTS) {
                    
                        
                        init = function () {
                    	getAllTimeOffs();
                            
                        },
                        getAllTimeOffs = function () {
                        	timeoffsFactory.allTimeoffs($scope.pageable).then(
                                function (employees) {
                                    $scope.employees = employees;
                                },

                                function (error) {

                                }
                            );
                        }

                    $scope.$on('$viewContentLoaded', function () {

                    });

                    $rootScope.iconClass = "fa flaticon-businessman276";
                    $rootScope.viewTitle = "Timeoffs";

                    $scope.constants = CONSTANTS;

                    

                    $scope.pageable = {
                        page: 1,
                        size: 15,
                        sort: {}
                    };

                    
                    $scope.employees = {};
                    $scope.pageChanged = function () {
                    	getAllTimeOffs();
                    };
                    init();
                }
            ]
        );
    }
);