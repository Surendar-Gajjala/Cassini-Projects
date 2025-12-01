define(['app/app.modules', 'app/components/crm/salesrep/salesRepFactory'],
    function($app) {
        $app.controller('SalesRepDetailsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams',
                '$cookies', 'salesRepFactory',

                function($scope, $rootScope, $timeout, $interval, $state, $stateParams,
                         $cookies, salesRepFactory) {

                    $rootScope.iconClass = "fa flaticon-businessman277";
                    $rootScope.viewTitle = "Sales Executive";

                    if($rootScope.hasRole('Sales Executive')) {
                        $rootScope.viewTitle = "Home";
                    }

                    $scope.tabs = [
                        /*{ heading: 'Basic Info', active: true, state: 'app.crm.salesrep.basicinfo', view: 'basicinfo' },*/
                        { heading: 'Customers', active: true, state: 'app.crm.salesrep.customers', view: 'customers' },
                        { heading: 'Orders', active: false, state: 'app.crm.salesrep.orders', view: 'orders' },
                        { heading: 'Performance', active: false, state: 'app.crm.salesrep.performance', view: 'performance' },
                        { heading: 'Customer Reports', active: false, state: 'app.crm.salesrep.fieldreports', view: 'fieldreports' }
                    ];

                    $scope.salesRep = {};

                    $scope.showProfile = function() {
                        $state.go('app.hrm.employee', {employeeId: $scope.salesRep.id});
                    };

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

                    $scope.loadSalesRep = function() {
                        salesRepFactory.getSalesRep($stateParams.salesRepId).then(
                            function(data) {
                                $scope.salesRep = data;
                                $scope.$broadcast("salesRepLoaded");
                            }
                        )
                    };

                    (function() {
                        $scope.loadSalesRep();
                    })();
                }
            ]
        );
    }
);