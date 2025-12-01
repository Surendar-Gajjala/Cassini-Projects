define(['app/app.modules'],
    function (app) {
        app.directive('authorization', ['$compile', '$timeout',
            function ($compile, $timeout) {
                return {
                    restrict: 'A',
                    scope: {
                        permission: '@'
                    },
                    link: function ($scope, elm, attr) {
                        if(app.authorizationFactory != null &&
                            $scope.permission != null &&
                            $scope.permission != undefined) {
                            if(!app.authorizationFactory.hasPermission($scope.permission)) {
                                $(elm).hide();
                            }
                        }
                    }
                };
            }]
        );
    }
);
