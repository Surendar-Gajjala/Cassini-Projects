define(['app/shared/services/services.module', 'app/shared/factories/httpFactory'],
    function (mdoule) {
        mdoule.factory('CommentsService', CommentsService);

        function CommentsService(httpFactory) {
            return {
                getRootComments: getRootComments,
                getReplies: getReplies,
                getComment: getComment,
                createComment: createComment
            };


            function getRootComments(objectType, objectId, pageable) {
                var url = "api/col/comments?objectType={0}&objectId={1}".
                        format(objectType, objectId);
                url += "&page={0}&size={1}".format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function getReplies(commentId, pageable) {
                var url = "api/col/comments/{0}/replies?page={1}&size={2}".
                                format(commentId, pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function getComment(commentId) {
                var url = "api/col/comments/" + commentId;
                return httpFactory.get(url);
            }

            function createComment(comment) {
                var url = "api/col/comments";
                return httpFactory.post(url, comment);
            }
        }
    }
);