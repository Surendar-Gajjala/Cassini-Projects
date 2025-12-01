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
                    'objectId': '='
                },


                link: function ($scope, elm, attr) {

                    var parsed = angular.element("<div></div>");
                    $scope.showComments = showComments;
                    $scope.addCommentTitle = parsed.html($translate.instant("ADD_COMMENT")).html();
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
)
;