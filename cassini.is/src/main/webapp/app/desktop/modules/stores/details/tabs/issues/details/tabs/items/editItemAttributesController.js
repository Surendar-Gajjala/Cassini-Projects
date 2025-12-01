define(['app/desktop/modules/stores/store.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDetailsDirectiveController'
    ],
    function (module) {
        module.controller('EditItemAttributesController', EditItemAttributesController);

        function EditItemAttributesController($scope, $rootScope, $stateParams, $timeout, $state, $cookies) {
            var vm = this;
            vm.hasPermission = $scope.data.hasPermission;
            var objType = $scope.data.objType;

            function updateItemAttributes() {
                $rootScope.showSuccessMessage("Item attributes updated successfully");
                $rootScope.hideSidePanel();
                if (objType == 'RECEIVEITEM') {
                    $rootScope.$broadcast('app.stock.receiveItems');
                }
                else if (objType == 'ISSUEITEM') {
                    $rootScope.$broadcast('app.stock.issueItems');
                }
                else if (objType == 'ROADCHALLANITEM') {
                    $rootScope.$broadcast('app.roadChallan.items');
                }
                else if (objType == 'WORKORDERITEM') {
                    $rootScope.$broadcast('app.workOrder.items');
                }
            }

            $scope.$on('app.stores.item.update.attributes', function addAttributes() {
                updateItemAttributes();
            });

            (function () {
                if ($application.homeLoaded == true) {
                }
            })();
        }
    });