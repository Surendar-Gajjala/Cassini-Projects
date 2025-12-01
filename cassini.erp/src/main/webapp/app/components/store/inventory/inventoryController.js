define(['app/app.modules'],
    function ($app) {
        $app.controller('InventoryController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies) {

                    $rootScope.iconClass = "fa flaticon-done2";
                    $rootScope.viewTitle = "Inventory";

                    $scope.tabs = [
                        {heading: 'Material', active: true, state: 'app.store.inventory.material', view: 'material'},
                        {heading: 'Products', active: false, state: 'app.store.inventory.products', view: 'products'}
                    ];

                    $scope.setTabActive = function (tab) {
                        if (tab.state != null) {
                            $state.go(tab.state);

                        }
                    };

                    $scope.setActiveFlag = function (index) {
                        angular.forEach($scope.tabs, function (t) {
                            t.active = false;
                        });
                        for (var i = 0; i < $scope.tabs.length; i++) {
                            if (i == index) {
                                $scope.tabs[i].active = true;
                            }
                        }
                    };
                }
            ]
        );
    }
);
