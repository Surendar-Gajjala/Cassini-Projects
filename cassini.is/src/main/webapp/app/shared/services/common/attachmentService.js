define(['app/shared/services/services.module', 'app/shared/factories/httpFactory'],
    function (mdoule) {
        mdoule.factory('AttachmentService', AttachmentService);

        function AttachmentService(httpFactory) {
            return {
                getAttachments: getAttachments,
                getAttachment: getAttachment,
                deleteAttachment: deleteAttachment
            };

            function getAttachments(objectType, objectId) {
                var url = "api/col/attachments?objectType={0}&objectId={1}".
                            format(objectType, objectId);
                return httpFactory.get(url);
            }

            function getAttachment(attachmentId) {
                var url = "api/col/attachments" + attachmentId;
                return httpFactory.get(url);
            }

            function deleteAttachment(attachmentId) {
                var url = "api/col/attachments" + attachmentId;
                return httpFactory.delete(url);
            }
        }
    }
);