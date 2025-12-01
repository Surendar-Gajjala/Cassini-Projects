define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('AttributeAttachmentService', AttributeAttachmentService);

        function AttributeAttachmentService($q, $http, httpFactory) {
            return {
                getAttributeAttachments: getAttributeAttachments,
                getAttributeAttachment: getAttributeAttachment,
                deleteAttributeAttachment: deleteAttributeAttachment,
                saveAttributeAttachment: saveAttributeAttachment,
                getAttributeAttachmentsByExtension: getAttributeAttachmentsByExtension,
                getAttributeAttachmentsByExtensions: getAttributeAttachmentsByExtensions,
                getMultipleAttributeAttachments: getMultipleAttributeAttachments,
                getAttributeAttachmentsByObjectIdAndAttributeId: getAttributeAttachmentsByObjectIdAndAttributeId,
                getAttributeAttachmentsByObjectId: getAttributeAttachmentsByObjectId,
                saveMultipleAttributeAttachments: saveMultipleAttributeAttachments,
                deleteObjectAttributeAttachment: deleteObjectAttributeAttachment
            };

            function saveMultipleAttributeAttachments(objectId, attributeDef, objectType, files) {

                var url = "api/col/attributeAttachments/multiple?objectType={0}&objectId={1}&attributeDef={2}".
                    format(objectType, objectId, attributeDef);
                var fd = new FormData();
                angular.forEach(files, function (file) {
                    fd.append('file', file);
                });
                var dfd = $q.defer();
                var config = {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                };

                $http.post(url, fd, config).success(function (response) {
                    dfd.resolve(response);
                }).error(function (response) {
                    dfd.reject(response);
                });

                return dfd.promise;

            }


            function saveAttributeAttachment(objectId, attributeDef, objectType, file) {

                var url = "api/col/attributeAttachments?objectType={0}&objectId={1}&attributeDef={2}".
                    format(objectType, objectId, attributeDef);
                var fd = new FormData();
                fd.append('file', file);

                var dfd = $q.defer();
                var config = {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                };

                $http.post(url, fd, config).success(function (response) {
                    dfd.resolve(response);
                }).error(function (response) {
                    dfd.reject(response);
                });

                return dfd.promise;

            }

            function getAttributeAttachments(objectType, objectId) {
                var url = "api/col/attributeAttachments?objectType={0}&objectId={1}".
                    format(objectType, objectId);
                return httpFactory.get(url);
            }

            function getMultipleAttributeAttachments(attachmentIds) {
                var url = "api/col/attributeAttachments/multiple/[" + attachmentIds + "]";
                return httpFactory.get(url);
            }

            function getAttributeAttachment(attachmentId) {
                var url = "api/col/attributeAttachments/" + attachmentId;
                return httpFactory.get(url);
            }

            function deleteAttributeAttachment(attachmentId) {
                var url = "api/col/attributeAttachments/" + attachmentId;
                return httpFactory.delete(url);
            }

            function deleteObjectAttributeAttachment(attachmentId, objectId) {
                var url = "api/col/attributeAttachments/" + attachmentId + "/object/" + objectId;
                return httpFactory.delete(url);
            }

            function getAttributeAttachmentsByExtension(ext) {
                var url = "api/col/attributeAttachments/ext/" + ext;
                return httpFactory.get(url);
            }

            function getAttributeAttachmentsByExtensions(exts) {
                var url = "api/col/attributeAttachments/exts/[" + exts + "]";
                return httpFactory.get(url);
            }

            function getAttributeAttachmentsByObjectIdAndAttributeId(objectId, attributeDef) {
                var url = "api/col/attributeAttachments/objectId/" + objectId + "/attributeDef/" + attributeDef;
                return httpFactory.get(url);
            }

            function getAttributeAttachmentsByObjectId(objectId) {
                var url = "api/col/attributeAttachments/objectId/" + objectId;
                return httpFactory.get(url);
            }
        }
    }
);