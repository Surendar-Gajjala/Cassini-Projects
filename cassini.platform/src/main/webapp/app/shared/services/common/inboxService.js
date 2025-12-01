define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],

    function (mdoule) {
        mdoule.factory('InboxService', InboxService);

        function InboxService(httpFactory) {

            function getUserAllMessages(userId) {
                var url = "col/users/"+ userId +"/inbox";
                return httpFactory.get(url);
            }

            function getUserInbox(userId) {
                var url = "col/users/"+ userId +"/inbox";
                return httpFactory.get(url);
            }

            function getUserInboxMessages(userId,inboxId) {
                var url = "col/users/"+ userId +"/inbox/"+ inboxId;
                return httpFactory.get(url);
            }

            function updateUserInbox(userId) {
                var url = "col/users/"+ userId +"/inbox";
                return httpFactory.put(url);
            }

            function deleteUserInbox(userId,inboxId) {
                var url = "col/users/"+ userId +"/inbox"+inboxId;
                return httpFactory.delete(url);
            }

            function getUnread(userId) {
                var url = "col/users/"+ userId +"/inbox/unread";
                return httpFactory.get(url);
            }

            return {
                deleteUserInbox:deleteUserInbox,
                updateUserInbox:updateUserInbox,
                getUserAllMessages: getUserAllMessages,
                getUserInboxMessages:getUserInboxMessages,
                getUserInbox:getUserInbox,
                getUnread: getUnread
            };

        }
    }
);
