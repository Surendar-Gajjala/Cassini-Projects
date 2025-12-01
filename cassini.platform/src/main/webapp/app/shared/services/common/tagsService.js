define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('TagsService', TagsService);

        function TagsService(httpFactory) {
            return {
                getAllTags: getAllTags,
                getTag: getTag,
                createTag: createTag,
                updateTag: updateTag,
                getAllObjectTags: getAllObjectTags,
                deleteTag: deleteTag
            };

            function getAllTags(pageable) {
                var url = "api/common/tags?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getTag(tagId) {
                var url = "api/common/tags/" + tagId;
                return httpFactory.get(url);
            }

            function createTag(tag) {
                var url = "api/common/tags";
                return httpFactory.post(url, tag);
            }

            function updateTag(tag) {
                var url = "api/common/tags/" + tag.id;
                return httpFactory.put(url, tag);
            }

            function getAllObjectTags(objectId) {
                var url = "api/common/tags/object/" + objectId + "/all";
                return httpFactory.get(url);
            }

            function deleteTag(tagId) {
                var url = "api/common/tags/" + tagId;
                return httpFactory.delete(url);
            }
        }
    }
);