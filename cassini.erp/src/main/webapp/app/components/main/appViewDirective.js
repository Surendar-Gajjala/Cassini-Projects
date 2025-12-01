define(['app/app.modules'], function(app)
{
    app.directive('appView',
    [
        function()
        {
            return {
               restrict: 'A',
               link: function($scope, $elem, attrs){
                    var adjuestViewHeight = function() {
                        var windowHeight = $(window).height();
                        $('#appview').height(windowHeight);
                        $('#workspace').height(windowHeight-50);
                    };

                    angular.element(window).bind('resize', function() {
                        adjuestViewHeight();
                    });

                   adjuestViewHeight();
               }
           }
        }
    ])
});