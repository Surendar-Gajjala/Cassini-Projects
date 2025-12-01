define(
    [
        'app/desktop/modules/shared/shared.module'
    ],

    function (module) {
        module.directive('commentsBtn', CommentsButtonDirective);

        function CommentsButtonDirective($rootScope, $compile, $timeout, $translate) {
            return {
                templateUrl: 'app/desktop/modules/shared/comments/commentsBtn.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'objectType': '=',
                    'objectId': '=',
                    'commentCount': '='
                },

                link: function (scope, elm, attr) {

                    var parsed = angular.element("<div></div>");
                    scope.showComments = showComments;
                    scope.addCommentTitle = parsed.html($translate.instant("SHOW_CONVERSATION")).html();
                    var commentsTitle = parsed.html($translate.instant("CONVERSATION")).html();

                    function showComments() {
                        var options = {
                            title: commentsTitle,
                            template: 'app/desktop/modules/shared/comments/newCommentsView.jsp',
                            controller: 'NewCommentsController as commentsVm',
                            resolve: 'app/desktop/modules/shared/comments/newCommentsController',
                            width: 600,
                            showMask: true,
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
)
;