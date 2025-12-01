define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('UserTasksService', UserTasksService);

        function UserTasksService(httpFactory) {
            return {
                getUserTasks: getUserTasks,
                getTaskCountsByStatus: getTaskCountsByStatus,
                getUserTaskCountsByStatus: getUserTaskCountsByStatus,
                getObjectCounts: getObjectCounts,
                getAllComments: getAllComments,
                searchComments: searchComments,
                getUnreadMessagesByPerson: getUnreadMessagesByPerson,
                getConversationCountByPerson: getConversationCountByPerson
            };

            function getUserTasks(personId, status) {
                var url = "api/plm/usertasks/" + personId;
                if (status !== null && status != undefined) {
                    url += "?status=" + status;
                }
                return httpFactory.get(url);
            }

            function getTaskCountsByStatus() {
                var url = "api/plm/usertasks/countsbystatus";
                return httpFactory.get(url);
            }

            function getUserTaskCountsByStatus() {
                var url = "api/plm/usertasks/counts/status";
                return httpFactory.get(url);
            }

            function getObjectCounts() {
                var url = "api/plm/usertasks/object/counts";
                return httpFactory.get(url);
            }

            function getAllComments(objectType, objectId, pageable) {
                var url = "api/plm/usertasks/comments";
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

            function getUnreadMessagesByPerson(personId, pageable) {
                var url = "api/plm/usertasks/comments/person/" + personId + "/unread/all?page={0}&size={1}&sort={2}:{3}"
                        .format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function searchComments(query, pageable) {
                var url = "api/plm/usertasks/comments/search?query={0}&page={1}&size={2}&sort={3}:{4}"
                    .format(query, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getConversationCountByPerson(person) {
                var url = "api/plm/usertasks/comments/all/" + person + "/count";
                return httpFactory.get(url);
            }
        }
    }
);