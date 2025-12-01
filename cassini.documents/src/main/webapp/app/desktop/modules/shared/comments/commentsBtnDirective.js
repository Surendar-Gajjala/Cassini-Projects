define (
    [
        'app/desktop/modules/shared/shared.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commentsService'
    ],

    function(module) {
        module.directive('commentsBtn', CommentsButtonDirective);

        function CommentsButtonDirective($rootScope, $compile, $timeout, CommentsService) {
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
                    $scope.commentsCount = 0;

                    function showComments() {
                        var options = {
                            title: 'Conversation',
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

                    function loadComments() {
                        var pageable = {
                            page: 0,
                            size: 1
                        };
                        CommentsService.getRootComments($scope.objectType, $scope.objectId, pageable).then(
                            function(data) {
                                $scope.commentsCount = data.totalElements;
                            }
                        )
                    }

                    (function() {
                        loadComments();
                        $rootScope.$on('comment.added', function() {
                            loadComments();
                        })
                    })();
                }
            }
        }
    }
);