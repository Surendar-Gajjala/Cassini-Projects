define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('ObjectTypeAttributeService', ObjectTypeAttributeService);

        function ObjectTypeAttributeService($q, httpFactory) {
            return {
                createObjectTypeAttribute: createObjectTypeAttribute,
                updateObjectTypeAttribute: updateObjectTypeAttribute,
                deleteObjectTypeAttribute: deleteObjectTypeAttribute,
                getObjectTypeAttributeById: getObjectTypeAttributeById,
                getObjectTypeAttributesByType: getObjectTypeAttributesByType,
                getObjectTypeAttributesByIds: getObjectTypeAttributesByIds,
                getObjectTypeAttributesByObjectIdAndObjectType: getObjectTypeAttributesByObjectIdAndObjectType,
                getAttributeGroups: getAttributeGroups,
                getAttributeGroupsByName: getAttributeGroupsByName,
                updateAttributeGroupName: updateAttributeGroupName
            };

            function createObjectTypeAttribute(type, attribute) {
                var url = "api/core/objects/types/" + type + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateObjectTypeAttribute(type, attribute) {
                var url = "api/core/objects/types/" + type + "/attributes/" + attribute.id;
                return httpFactory.put(url, attribute);
            }

            function deleteObjectTypeAttribute(type, attributeId) {
                var url = "api/core/objects/types/" + type + "/attributes/" + attributeId;
                return httpFactory.delete(url);
            }

            function getObjectTypeAttributeById(type, attributeId) {
                var url = "api/core/objects/types/" + type + "/attributes/" + attributeId;
                return httpFactory.get(url);
            }

            function getObjectTypeAttributesByObjectIdAndObjectType(objectId, objectType) {
                var url = "api/core/objects/types/" + objectType + "/attributes/objectId/" + objectId;
                return httpFactory.get(url);
            }

            function getObjectTypeAttributesByType(type) {
                var url = "api/core/objects/types/" + type + "/attributes";
                return httpFactory.get(url);
            }

            function getObjectTypeAttributesByIds(ids) {
                var url = "api/core/objects/types/" + null + "/attributes/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getAttributeGroups() {
                var url = "api/core/objects/types/" + null + "/attributes/groups";
                return httpFactory.get(url);
            }

            function getAttributeGroupsByName(groupName) {
                var url = "api/core/objects/types/" + null + "/attributes/groups/byName?groupName=" + groupName;
                return httpFactory.get(url);
            }

            function updateAttributeGroupName(attributeId, groupName) {
                var url = "api/core/objects/types/" + null + "/attributes/" + attributeId + "/groups/update?groupName=" + groupName;
                return httpFactory.get(url);
            }
        }
    }
);
