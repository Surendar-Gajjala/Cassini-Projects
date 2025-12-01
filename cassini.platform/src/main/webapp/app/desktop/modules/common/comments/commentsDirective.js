define(['app/assets/bower_components/cassini-platform/app/desktop/modules/common/common.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commentsService'
    ],
    function(module) {
        module.directive('comments',
            ['CommentsService',
                function(CommentsService) {
                    return {
                        templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/comments.jsp',
                        restrict: 'E',
                        scope: {
                            'objectType': '@',
                            'objectId': '='
                        },
                        link: function($scope, $elem, attrs){
                            $scope.comments = [];
                            $scope.commentError = false;
                            $scope.commentErrorMessage = "";

                            $scope.newComment = {
                                objectType: $scope.objectType,
                                objectId: $scope.objectId,
                                comment: null
                            };

                            $scope.addComment = createComment;

                            function loadComments() {
                                var pageable = {
                                    page: 0,
                                    size: 100
                                };
                                CommentsService.getRootComments($scope.objectType, $scope.objectId, pageable).then(
                                    function(data) {
                                        $scope.comments = data.content;
                                    }
                                )
                            }

                            function createComment() {
                                if(validateComment()) {
                                    CommentsService.createComment($scope.newComment).then(
                                        function(data) {
                                            $scope.comments.unshift(data);
                                            $scope.newComment.comment = null;
                                        }
                                    )
                                }
                            }

                            function validateComment() {
                                var valid = true;

                                $scope.commentError = false;
                                if($scope.newComment.comment == null || $scope.newComment.comment.trim() == "") {
                                    $scope.commentError = true;
                                    $scope.commentErrorMessage = "Comment must contain at least three characters";
                                    valid = false;
                                }
                                return valid;
                            }


                            (function() {
                                loadComments();
                            })();
                        }
                    }
                }
            ]
        );

        module.directive('comment',
            ['$compile', '$sce', 'CommentsService',
                function($compile, $sce, CommentsService) {
                    return {
                        templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/comment.jsp',
                        restrict: 'E',
                        replace: true,
                        scope: {
                            'commentId': '='
                        },
                        link: function($scope, $elem, attrs){
                            $scope.comment = null;
                            $scope.showReply = false;
                            $scope.replyText = null;
                            $scope.commentError = false;
                            $scope.commentErrorMessage = "";

                            $scope.createReply = createReply;

                            var repliesTpl = '<comment comment-id="reply.id" ng-repeat="reply in comment.replies" ng-if="comment.replies.length > 0"></comment>';

                            CommentsService.getComment($scope.commentId).then(
                                function(data) {
                                    data.comment = data.comment.replace(/\n/g, '<br/>');
                                    data.comment = $sce.trustAsHtml(data.comment);
                                    $scope.comment = data;
                                    $scope.comment.replies = [];
                                    var pageable = {
                                        page: 0,
                                        size: 100
                                    };
                                    CommentsService.getReplies($scope.comment.id, pageable).then(
                                        function(data) {
                                            if(data.content.length > 0) {
                                                angular.forEach(data.content, function(reply) {
                                                    reply.comment = reply.comment.replace(/\n/g, '<br/>');
                                                    reply.comment = $sce.trustAsHtml(reply.comment);
                                                });

                                                $scope.comment.replies = data.content;
                                                $compile(repliesTpl)($scope, function(cloned, scope)   {
                                                    $elem.append(cloned);
                                                });
                                            }
                                        }
                                    )
                                }
                            );

                            function createReply() {
                                $scope.commentError = false;

                                if($scope.replyText != null && $scope.replyText.trim() != "" && $scope.replyText.length >= 2) {
                                    var reply = {
                                        objectType: $scope.comment.objectType,
                                        objectId: $scope.comment.objectId,
                                        replyTo: $scope.comment.id,
                                        comment: $scope.replyText
                                    };

                                    CommentsService.createComment(reply).then(
                                        function(data) {
                                            $scope.comment.replies.unshift(data);
                                            $scope.showReply = false;

                                            if($scope.comment.replies.length == 1) {
                                                $compile(repliesTpl)($scope, function(cloned, scope)   {
                                                    $elem.append(cloned);
                                                });
                                            }
                                        }
                                    )
                                }
                                else {
                                    $scope.commentError = true;
                                    $scope.commentErrorMessage = "Comment must contain at least two characters";
                                }
                            }
                        }
                    }
                }
            ]
        );
    }
);