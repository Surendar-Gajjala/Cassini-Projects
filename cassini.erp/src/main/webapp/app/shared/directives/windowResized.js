define(['app/app.modules'],
    function (app) {
        app.directive('windowResized', ['$compile', '$timeout',
            function ($compile, $timeout) {
                return {
                    restrict: 'A',
                    link: function ($scope, elm, attr) {
                        $(window).resize(function() {
                            if($scope.$root != null) {
                                $scope.$root.$broadcast("windowResize");
                            }
                        });
                    }
                };
            }]
        );
    }
);
