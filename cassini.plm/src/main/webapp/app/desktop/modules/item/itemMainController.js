define(['app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/autodeskForge/showCADFileDirective'
    ],
    function (module) {
        module.controller('ItemMainController', ItemMainController);

        function ItemMainController($scope, $rootScope, $timeout, $interval, $state, $cookies) {


            $scope.registerCallBack = function (callback) {
                $scope.conCallBack = callback;
            };

            $rootScope.showAutoDeskFile = function (file) {
                $scope.conCallBack(file);
            };

            (function () {

            })();
        }
    }
);