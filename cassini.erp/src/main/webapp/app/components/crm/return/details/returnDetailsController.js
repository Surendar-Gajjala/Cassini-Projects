define(['app/app.modules', 'app/components/crm/return/returnFactory'],
    function($app) {
        $app.controller('ReturnDetailsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams',
                '$cookies', 'returnFactory',

                function($scope, $rootScope, $timeout, $interval, $state, $stateParams,
                         $cookies, returnFactory) {

                    $rootScope.iconClass = "fa flaticon-refresh2";
                    $rootScope.viewTitle = "Return Details";

                    $scope.return = null;

                    $scope.loadReturn = function() {
                        returnFactory.getReturn($stateParams.returnId).then(
                            function(data) {
                                $scope.return = data;
                            }
                        );
                    };

                    (function() {
                        $scope.loadReturn();
                    })();
                }
            ]
        );
    }
);