/**
 * Created by swapna on 23/11/18.
 */
define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('MediaService', MediaService);

        function MediaService(httpFactory) {
            return {
                uploadMedia: uploadMedia,
                getMediaByObjectId: getMediaByObjectId,
                deleteMediaById: deleteMediaById
            };

            function uploadMedia(objectId, media, location) {
                var url = "api/col/media/object/" + objectId + "?latitude={0}&longitude={1}&uploadFrom={2}".format(location.latitude, location.longitude, location.uploadFrom);
                return httpFactory.uploadMultiple(url, media);
            }

            function getMediaByObjectId(ObjectId) {
                var url = "api/col/media/object/" + ObjectId;
                return httpFactory.get(url);
            }

            function deleteMediaById(mediaId) {
                var url = "api/col/media/" + mediaId;
                return httpFactory.delete(url);
            }
        }
    }
);