define(['app/desktop/modules/col/col.module',
        'app/shared/services/communication/communicationService'
    ],

    function (module) {
        module.directive('discussions',
            ['CommunicationService',
                function (CommunicationService) {
                    return {
                        templateUrl: 'app/desktop/modules/col/communication/discussion/discussions.jsp',
                        restrict: 'E',
                        replace: true,
                        scope: {
                            group: '=',
                            idd: '='
                        },
                        link: function ($scope, $elem, attrs) {
                            $scope.discussions = [];
                            $scope.discussionError = false;
                            $scope.discussionErrorMessage = "";

                            $scope.$watch('group', function (newGroup, oldGroup) {
                                if (newGroup) {

                                    loadDiscussions($scope.group);
                                }

                            }, true);

                            $scope.emptyDiscussionMessage = {
                                "commentedDate": null,
                                "commentedBy": window.$application.login.person,
                                "comment": null,
                                "replyTo": null,
                                "discussionGroupId": null,
                                "ctxObjectType": "PROJECT",
                                "ctxObjectId": $scope.idd,
                                "messageAttachments": []
                            };
                            $scope.newDiscussionMessage = angular.copy($scope.emptyDiscussionMessage);

                            $scope.addDiscussion = createDiscussion;

                            function loadDiscussions(group) {
                                var pageable = {
                                    page: 0,
                                    size: 10,
                                    sort: {
                                        field: "commentedDate",
                                        order: "DESC"
                                    }
                                };
                                CommunicationService.getDiscussionGrpMessagesAll(group.id, pageable, $scope.idd).then(
                                    function (data) {
                                        $scope.discussions = data.content;
                                    }
                                )
                            }

                            function createDiscussion() {
                                if (validateDiscussion()) {

                                    $scope.newDiscussionMessage.discussionGroupId = $scope.group.id;

                                    CommunicationService.createDiscussionMessage($scope.newDiscussionMessage).then(
                                        function (data) {
                                            $scope.discussions.unshift(data);
                                            $scope.newDiscussionMessage = angular.copy($scope.emptyDiscussionMessage);
                                        }
                                    )
                                }
                            }

                            function validateDiscussion() {
                                var valid = true;

                                $scope.discussionError = false;
                                if ($scope.newDiscussionMessage.comment == null || $scope.newDiscussionMessage.comment.trim() == "") {
                                    $scope.discussionError = true;
                                    $scope.discussionErrorMessage = "Discussion should contain atleast 3 characters";
                                    valid = false;
                                }
                                return valid;
                            }

                            (function () {
                                loadDiscussions($scope.group);
                            })();
                        }
                    }
                }
            ]
        );

        module.directive('discussion',
            ['$compile', '$sce', 'CommunicationService',
                function ($compile, $sce, CommunicationService) {
                    return {
                        templateUrl: 'app/desktop/modules/col/communication/discussion/discussion.jsp',
                        restrict: 'E',
                        replace: true,
                        scope: {
                            'discussionId': '='
                        },
                        link: function ($scope, $elem, attrs) {
                            $scope.discussion = null;
                            $scope.showReply = false;
                            $scope.replyText = null;
                            $scope.discussionError = false;
                            $scope.discussionErrorMessage = "";

                            $scope.createReply = createReply;

                            var repliesTpl = '<discussion discussion-id="reply.id" ng-repeat="reply in discussion.children" ng-if="discussion.children.length > 0"></discussion>';

                            CommunicationService.getDiscussion($scope.discussionId).then(
                                function (data) {
                                    data.comment = data.comment.replace(/\n/g, '<br/>');
                                    data.comment = $sce.trustAsHtml(data.comment);
                                    $scope.discussion = data;
                                    $scope.discussion.children = [];
                                    var pageable = {
                                        page: 0,
                                        size: 10,
                                        sort: {
                                            field: "commentedDate",
                                            order: "DESC"
                                        }
                                    };
                                    CommunicationService.getReplies($scope.discussion.id, pageable).then(
                                        function (data) {
                                            if (data.content.length > 0) {
                                                angular.forEach(data.content, function (reply) {
                                                    reply.comment = reply.comment.replace(/\n/g, '<br/>');
                                                    reply.comment = $sce.trustAsHtml(reply.comment);
                                                });

                                                $scope.discussion.children = data.content;
                                                $compile(repliesTpl)($scope, function (cloned, scope) {
                                                    $elem.append(cloned);
                                                });
                                            }
                                        }
                                    )
                                }
                            );

                            function createReply() {
                                $scope.discussionError = false;

                                if ($scope.replyText != null && $scope.replyText.trim() != "" && $scope.replyText.length >= 2) {

                                    var reply = {
                                        "commentedDate": null,
                                        "commentedBy": window.$application.login.person,
                                        "comment": $scope.replyText,
                                        "replyTo": $scope.discussion.id,
                                        "discussionGroupId": $scope.discussion.discussionGroupId,
                                        "ctxObjectType": "PROJECT",
                                        "ctxObjectId": $scope.idd,
                                        "messageAttachments": []

                                    };

                                    CommunicationService.createDiscussionMessage(reply).then(
                                        function (data) {
                                            $scope.discussion.children.unshift(data);
                                            $scope.showReply = false;

                                            if ($scope.discussion.children.length == 1) {
                                                $compile(repliesTpl)($scope, function (cloned, scope) {
                                                    $elem.append(cloned);
                                                });
                                            }
                                        }
                                    )
                                }
                                else {
                                    $scope.discussionError = true;
                                    $scope.discussionErrorMessage = "Discussion should contain atleast 2 characters";
                                }
                            }
                        }
                    }
                }
            ]
        );
    }
);