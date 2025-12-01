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
                    'objectId': '=',
                    'commentCount': '='
                },

                link: function (scope, elm, attr) {
                    scope.showComments = showComments;
                    var parsed = angular.element('<div></div>');
                    scope.showCommentsTitle = parsed.html($translate.instant("SHOW_CONVERSATION")).html();
                    var commentsTitle = parsed.html($translate.instant("CONVERSATION")).html();

                    function showComments() {
                        var options = {
                            title: commentsTitle,
                            template: 'app/desktop/modules/shared/comments/newCommentsView.jsp',
                            controller: 'NewCommentsController as commentsVm',
                            resolve: 'app/desktop/modules/shared/comments/newCommentsController',
                            width: 600,
                            data: {
                                objectType: scope.objectType,
                                objectId: scope.objectId,
                                commentCount: scope.commentCount
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }
                }
            }
        }
    }
);