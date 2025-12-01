define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('AttachmentService', AttachmentService);

        function AttachmentService($q, $http, httpFactory) {
            return {
                getAttachments: getAttachments,
                getAttachment: getAttachment,
                deleteAttachment: deleteAttachment,
                saveAttachment: saveAttachment,
                getAttachmentsByExtension: getAttachmentsByExtension,
                getAttachmentsByExtensions: getAttachmentsByExtensions,
                getMultipleAttachments: getMultipleAttachments,
                getAttachmentReferences: getAttachmentReferences,
                saveMultipleAttachments: saveMultipleAttachments
            };

            function saveMultipleAttachments(objectType, objectIds, files) {
                var url = "api/col/attachments/multipleFilesPost?objectType={0}&objectIds={1}".format(objectType, objectIds);
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

            function saveAttachment(objectId, objectType, file) {

                var url = "api/col/attachments?objectType={0}&objectId={1}".
                    format(objectType, objectId);
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

            function getAttachments(objectType, objectId) {
                var url = "api/col/attachments?objectType={0}&objectId={1}".
                    format(objectType, objectId);
                return httpFactory.get(url);
            }

            function getMultipleAttachments(attachmentIds) {
                var url = "api/col/attachments/multiple/[" + attachmentIds + "]";
                return httpFactory.get(url);
            }

            function getAttachment(attachmentId) {
                var url = "api/col/attachments/" + attachmentId;
                return httpFactory.get(url);
            }

            function deleteAttachment(attachmentId) {
                var url = "api/col/attachments/" + attachmentId;
                return httpFactory.delete(url);
            }

            function getAttachmentsByExtension(ext) {
                var url = "api/col/attachments/ext/" + ext;
                return httpFactory.get(url);
            }

            function getAttachmentsByExtensions(exts) {
                var url = "api/col/attachments/exts/[" + exts + "]";
                return httpFactory.get(url);
            }

            function getAttachmentReferences(objects, property) {
                var attachmentIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && attachmentIds.indexOf(object[property]) == -1) {
                        attachmentIds.push(object[property]);
                    }
                });

                if (attachmentIds.length > 0) {
                    getMultipleAttachments(attachmentIds).then(
                        function (attachments) {
                            var map = new Hashtable();
                            angular.forEach(attachments, function (attachment) {
                                map.put(attachment.id, attachment);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var attachment = map.get(object[property]);
                                    if (attachment != null) {
                                        object[property + "Object"] = attachment;
                                    }
                                }
                            });
                        }
                    );
                }
            }
        }
    }
);