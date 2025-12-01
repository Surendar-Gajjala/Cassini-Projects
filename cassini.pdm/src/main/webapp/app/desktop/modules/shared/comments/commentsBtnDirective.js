define (
    [
        'app/desktop/modules/shared/shared.module'
    ],

    function(module) {
        module.directive('commentsBtn', CommentsButtonDirective);

        function CommentsButtonDirective($rootScope, $compile, $timeout) {
            return {
                templateUrl: 'app/desktop/modules/shared/comments/commentsBtn.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'objectType': '=',
                    'objectId': '='
                },


                link: function ($scope, elm, attr) {
                    $scope.showComments = showComments;

                    function showComments() {
                        var options = {
                            title: 'Comments',
                            template: 'app/desktop/modules/shared/comments/commentsView.jsp',
                            controller: 'CommentsController as commentsVm',
                            resolve: 'app/desktop/modules/shared/comments/commentsController',
                            data: {
                                objectType: $scope.objectType,
                                objectId: $scope.objectId
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }
                }
            }
        }
    }
);