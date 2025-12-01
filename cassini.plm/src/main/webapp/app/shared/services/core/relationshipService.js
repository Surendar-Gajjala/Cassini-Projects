define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('RelationshipService', RelationshipService);

        function RelationshipService(httpFactory) {
            return {
                createRelationship: createRelationship,
                updateRelationship: updateRelationship,
                deleteRelationship: deleteRelationship,
                getRelationship: getRelationship,
                getAllRelationships: getAllRelationships,
                getItemsByRelationshipToType: getItemsByRelationshipToType,
                getRelationshipsByFromType: getRelationshipsByFromType,
                getItemsByRelationshipToTypeAndFromItem: getItemsByRelationshipToTypeAndFromItem,
                getRelationshipByFromTypeAndToType: getRelationshipByFromTypeAndToType,
                getRelationshipByName: getRelationshipByName,
                createRelationshipAttribute: createRelationshipAttribute,
                updateRelationshipAttribute: updateRelationshipAttribute,
                deleteRelationshipAttribute: deleteRelationshipAttribute,
                getAllAttributesByRelationship: getAllAttributesByRelationship,
                getItemsByRelationshipAndFromItem: getItemsByRelationshipAndFromItem,
                getItemsByRelationship: getItemsByRelationship
            };

            function createRelationship(relationship) {
                var url = "api/plm/relationships";
                return httpFactory.post(url, relationship);
            }

            function createRelationshipAttribute(attribute) {
                var url = "api/plm/relationships/" + attribute.relationship + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateRelationshipAttribute(attribute) {
                var url = "api/plm/relationships/" + attribute.relationship + "/attributes/" + attribute.id;
                return httpFactory.put(url, attribute);
            }

            function updateRelationship(relationship) {
                var url = "api/plm/relationships/" + relationship.id;
                return httpFactory.put(url, relationship);
            }

            function deleteRelationship(relationship) {
                var url = "api/plm/relationships/" + relationship.id;
                return httpFactory.delete(url);
            }

            function deleteRelationshipAttribute(attributeId) {
                var url = "api/plm/relationships/attributes/" + attributeId;
                return httpFactory.delete(url);
            }

            function getRelationship(relationship) {
                var url = "api/plm/relationships/" + relationship.id;
                return httpFactory.get(url);
            }

            function getAllRelationships(pageable) {
                var url = "api/plm/relationships?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getItemsByRelationshipToType(toType) {
                var url = "api/plm/items/itemType/" + toType.id;
                return httpFactory.get(url);
            }

            function getRelationshipsByFromType(fromType) {
                var url = "api/plm/relationships/itemType/" + fromType.id;
                return httpFactory.get(url);
            }

            function getItemsByRelationshipToTypeAndFromItem(fromType, revisionId) {
                var url = "api/plm/relationships/itemType/" + fromType.id + "/itemRevision/" + revisionId;
                return httpFactory.get(url);
            }

            function getItemsByRelationshipAndFromItem(relationshipId, revisionId, pageable, filters) {
                var url = "api/plm/relationships/" + relationshipId + "/itemRevision/" + revisionId + "?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&itemNumber={1}&itemName={2}&description={3}".
                    format(filters.searchQuery, filters.itemNumber, filters.itemName, filters.description);
                return httpFactory.get(url);
            }

            function getRelationshipByFromTypeAndToType(fromType, toType) {
                var url = "api/plm/relationships/fromType/" + fromType.id + "/toType/" + toType.id;
                return httpFactory.get(url);
            }

            function getRelationshipByName(relationship) {
                var url = "api/plm/relationships/byRelationshipName";
                return httpFactory.post(url, relationship);
            }

            function getAllAttributesByRelationship(relationshipId) {
                var url = "api/plm/relationships/" + relationshipId + "/attributes";
                return httpFactory.get(url);
            }

            function getItemsByRelationship(id) {
                var url = "api/plm/relationships/" + id + "/items";
                return httpFactory.get(url);
            }
        }
    }
);