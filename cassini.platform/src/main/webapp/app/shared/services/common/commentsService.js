define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('CommentsService', CommentsService);

        function CommentsService(httpFactory) {
            return {
                getRootComments: getRootComments,
                getReplies: getReplies,
                getComment: getComment,
                createComment: createComment,
                updateComment: updateComment,
                getAllComments: getAllComments,
                getAllCommentsCount: getAllCommentsCount,
                uploadCommentFiles: uploadCommentFiles,
                searchComments: searchComments,
                getMessageCount: getMessageCount,
                deleteComment: deleteComment,
                getMessageCountByPerson: getMessageCountByPerson,
                getUserReadsByPersonAndComments: getUserReadsByPersonAndComments,
                updateUserReadComment: updateUserReadComment,
                updateUnReadCommentsByPerson: updateUnReadCommentsByPerson
            };


            function getRootComments(objectType, objectId, pageable) {
                var url = "api/col/comments";
                if (objectType != null && objectType != undefined &&
                    objectId != null && objectId != undefined) {
                    url += "?objectType={0}&objectId={1}&".
                        format(objectType, objectId);
                }
                else {
                    url += "?"
                }
                url += "page={0}&size={1}".format(pageable.page, pageable.size);
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

            function updateComment(comment) {
                var url = "api/col/comments/" + comment.id;
                return httpFactory.put(url, comment);
            }

            function getAllComments(objectType, objectId) {
                var url = "api/col/comments/all?objectType={0}&objectId={1}".format(objectType, objectId);
                return httpFactory.get(url);
            }

            function getAllCommentsCount(objectType, objectId) {
                var url = "api/col/comments/count?objectType={0}&objectId={1}".format(objectType, objectId);
                return httpFactory.get(url);
            }

            function uploadCommentFiles(comment, files) {
                var url = "api/col/comments/" + comment + "/uploadFiles";
                return httpFactory.uploadMultiple(url, files);
            }

            function searchComments(query, pageable) {
                var url = "api/col/comments/search?query={0}&page={1}&size={2}&sort={3}:{4}"
                    .format(query, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getMessageCount() {
                var url = "api/col/comments/all/count";
                return httpFactory.get(url);
            }

            function getMessageCountByPerson(person) {
                var url = "api/col/comments/all/" + person + "/count";
                return httpFactory.get(url);
            }

            function getUserReadsByPersonAndComments(person, commentIds) {
                var url = "api/col/comments/all/" + person + "/[" + commentIds + "]";
                return httpFactory.get(url);
            }

            function updateUserReadComment(commentId, person) {
                var url = "api/col/comments/" + commentId + "/person/" + person + "/read";
                return httpFactory.get(url);
            }

            function updateUnReadCommentsByPerson(person) {
                var url = "api/col/comments/person/" + person + "/unread/all/update";
                return httpFactory.get(url);
            }

            function deleteComment(commentId) {
                var url = "api/col/comments/" + commentId;
                return httpFactory.delete(url);
            }

        }
    }
);