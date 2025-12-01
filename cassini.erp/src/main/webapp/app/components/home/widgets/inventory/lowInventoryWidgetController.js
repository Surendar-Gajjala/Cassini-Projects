define(['app/app.modules',
        'app/components/prod/product/productFactory'],
    function(module) {
        module.controller('LowInventoryWidgetController', LowInventoryWidgetController);

        function LowInventoryWidgetController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,productFactory) {

            $scope.productLowInventory = null;
               function lowInventory (){
                  productFactory.getLowInventory().then(
                    function(data){
                        $scope.productLowInventory = data;
                    }
                )
            }
            (function () {
                lowInventory();
            })();
        }
    }
);

