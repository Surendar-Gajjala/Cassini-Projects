define(
    [
        'app/desktop/modules/shared/shared.module'
    ],

    function (module) {
        module.directive('commentsButton', CommentsButtonDirective);

        function CommentsButtonDirective($rootScope, $compile, $timeout, $translate) {
            return {
                templateUrl: 'app/desktop/modules/shared/comments/commentsButton.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'objectType': '@',
                    'objectId': '='
                },


                link: function ($scope, elm, attr) {
                    $scope.showComments = showComments;
                    var parsed = angular.element('<div></div>');
                    $scope.showCommentsTitle = parsed.html($translate.instant("SHOW_COMMENTS")).html();
                    var commentsTitle = parsed.html($translate.instant("COMMENTS")).html();

                    function showComments() {
                        var options = {
                            title: commentsTitle,
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