define(['app/shared/services/services.module', 'app/shared/factories/httpFactory'],
    function (mdoule) {
        mdoule.factory('MessageService', MessageService);

        function MessageService(httpFactory) {
            return {
                getGroups: getGroups,
                createGroup: createGroup,
                createMessage: createMessage,
                getAllMessage: getAllMessage,
                updateGroup: updateGroup,
                deleteGroup: deleteGroup,
                uploadGroupIcon: uploadGroupIcon,
                getGroupIcon: getGroupIcon,
                getGroup: getGroup,
                getLatestMessages: getLatestMessages,
                createDiscussionGroup: createDiscussionGroup,
                createDiscussionMessage: createDiscussionMessage,
                getDiscussionGroups: getDiscussionGroups,
                getAllDiscussionGrpMessages: getAllDiscussionGrpMessages,
                getDiscussionGrpMessagesAll: getDiscussionGrpMessagesAll,
                getDiscussion: getDiscussion,
                getReplies: getReplies,
                deleteGrpMember: deleteGrpMember,
                createGrpMember: createGrpMember,
                createGroupMessageAttachment: createGroupMessageAttachment,
                downloadGroupMessageAttachment: downloadGroupMessageAttachment,
                messageGroupFreeTextSearch: messageGroupFreeTextSearch
            };

            function getGroup(object, groupId, objectId) {
                var url = "api/col/messaging/groups/" + groupId + "/?ctxObjectType={0}&ctxObjectId={1}".format(object, objectId);
                return httpFactory.get(url);
            }

            function getGroups(object, objectId) {
                var url = "api/col/messaging/groups?ctxObjectType={0}&ctxObjectId={1}".
                    format(object, objectId);
                return httpFactory.get(url);
            }

            function createGroup(data) {
                var url = "api/col/messaging/groups";
                return httpFactory.post(url, data);
            }

            function updateGroup(data, id) {
                var url = "api/col/messaging/groups/" + id;
                return httpFactory.put(url, data);
            }

            function deleteGroup(id) {
                var url = "api/col/messaging/groups/" + id;
                return httpFactory.delete(url);
            }

            function uploadGroupIcon(id, file) {
                var url = "api/col/messaging/groups/" + id + "/icon";
                return httpFactory.upload(url, file);
            }

            function getGroupIcon(id) {
                var url = "api/col/messaging/groups/" + id + "/icon";
                return httpFactory.get(url)
            }

            function deleteGrpMember(id) {
                var url = "api/col/messaging/groups/members/" + id;
                return httpFactory.delete(url);
            }

            function createGrpMember(grpMember) {
                var url = "api/col/messaging/groups/members";
                return httpFactory.post(url, grpMember);
            }

            function createMessage(data) {
                var url = "api/col/messaging/groups/messages";
                return httpFactory.post(url, data);
            }

            function createDiscussionMessage(data) {
                var url = "api/col/discussions/messages";
                return httpFactory.post(url, data);
            }

            function getAllDiscussionGrpMessages(object, objectId, groupid, pageable) {
                var url = " api/col/discussions/messages/" + groupid;
                url += "?ctxObjectType={0}&ctxObjectId={1}&page={2}&size={3}&sort={4}:{5}".
                    format(object, objectId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }


            function getDiscussionGrpMessagesAll(object, objectId, groupid, pageable) {
                var url = " api/col/discussions/messages/" + groupid + "/getAll";
                url += "?ctxObjectType={0}&ctxObjectId={1}&page={2}&size={3}&sort={4}:{5}".
                    format(object, objectId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }


            function getAllMessage(object, objectId, groupid, pageable, criteria) {
                var url = "api/col/messaging/groups/messages/" + groupid;
                url += "?ctxObjectType={0}&ctxObjectId={1}&page={2}&size={3}&sort={4}:{5}".
                    format(object, objectId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(criteria.searchQuery);
                return httpFactory.get(url);
            }

            function getLatestMessages(object, objectId, groupId, messageId) {
                var url = "api/col/messaging/groups/messages/recent/" + groupId + "/" + messageId;
                url += "?ctxObjectType={0}&ctxObjectId={1}".
                    format(object, objectId);

                return httpFactory.get(url);
            }

            function createDiscussionGroup(data) {
                var url = "api/col/discussions";
                return httpFactory.post(url, data);
            }

            function getDiscussionGroups(object, objectId, pageable) {
                var url = "api/col/discussions";
                url += "?ctxObjectType={0}&ctxObjectId={1}&page={2}&size={3}&sort={4}:{5}".
                    format(object, objectId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getDiscussion(discussionmsgid) {
                var url = "api/col/discussions/messages/discussion/" + discussionmsgid;
                return httpFactory.get(url);
            }

            function getReplies(discussionmsgid, pageable) {
                var url = "api/col/discussions/messages/{0}/replies?page={1}&size={2}&sort={3}:{4}".
                    format(discussionmsgid, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function createGroupMessageAttachment(groupId, messageId, objectId, objectType, file) {
                var url = "api/col/messaging/groups/" + groupId + "/messages/" + messageId + "/attachments?objectType={0}&objectId={1}".format(objectId, objectType);
                return httpFactory.uploadMultiple(url, file);
            }

            function downloadGroupMessageAttachment(groupId, fileId) {
                var url = "api/col/messaging/groups/" + groupId + "/messages/attachments/" + fileId;
                return httpFactory.get(url);
            }

            function messageGroupFreeTextSearch(object, objectId, pageable, criteria) {
                var url = "api/col/messaging/groups/freesearch?ctxObjectType={0}&ctxObjectId={1}".format(object, objectId);
                url += "&page={0}&size={1}".format(pageable.page, pageable.size);
                url += "&searchQuery={0}".
                    format(criteria.searchQuery);
                return httpFactory.get(url);
            }

        }
    }
);


