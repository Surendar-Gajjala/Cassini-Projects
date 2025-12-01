define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('PGCObjectTypeService', PGCObjectTypeService);

        function PGCObjectTypeService($q, httpFactory) {
            return {

                createSubstanceType: createSubstanceType,
                updateSubstanceType: updateSubstanceType,
                deleteSubstanceType: deleteSubstanceType,
                deleteObjectType: deleteObjectType,
                getSubstanceType: getSubstanceType,
                getAllSubstanceTypes: getAllSubstanceTypes,
                getMultipleSubstanceTypes: getMultipleSubstanceTypes,
                getSubstanceTypeTree: getSubstanceTypeTree,

                getPgcObjectTypeByType: getPgcObjectTypeByType,
                getObjectsCountByType: getObjectsCountByType,
                getPgcObjectsTree: getPgcObjectsTree,
                getAllObjectTypeTree: getAllObjectTypeTree,

                createSubstanceGroupType: createSubstanceGroupType,
                updateSubstanceGroupType: updateSubstanceGroupType,
                deleteSubstanceGroupType: deleteSubstanceGroupType,
                getSubstanceGroupType: getSubstanceGroupType,
                getAllSubstanceGroupTypes: getAllSubstanceGroupTypes,
                getMultipleSubstanceGroupTypes: getMultipleSubstanceGroupTypes,
                getSubstanceGroupTypeTree: getSubstanceGroupTypeTree,

                createSpecificationType: createSpecificationType,
                updateSpecificationType: updateSpecificationType,
                deleteSpecificationType: deleteSpecificationType,
                getSpecificationType: getSpecificationType,
                getAllSpecificationTypes: getAllSpecificationTypes,
                getMultipleSpecificationTypes: getMultipleSpecificationTypes,
                getSpecificationTypeTree: getSpecificationTypeTree,

                createDeclarationType: createDeclarationType,
                updateDeclarationType: updateDeclarationType,
                deleteDeclarationType: deleteDeclarationType,
                getDeclarationType: getDeclarationType,
                getAllDeclarationTypes: getAllDeclarationTypes,
                getMultipleDeclarationTypes: getMultipleDeclarationTypes,
                getDeclarationTypeTree: getDeclarationTypeTree,


                getPgcObjectAttributesWithHierarchy: getPgcObjectAttributesWithHierarchy,
                createPGCObjectAttribute: createPGCObjectAttribute,
                updatePGCObjectAttribute: updatePGCObjectAttribute,

                getSubstanceByType: getSubstanceByType,
                getSubstanceGroupByType: getSubstanceGroupByType,
                getSpecificationsByType: getSpecificationsByType,
                getDeclarationsByType: getDeclarationsByType,
                getUsedPgcObjectAttributesValues: getUsedPgcObjectAttributesValues

            };

            function getPgcObjectTypeByType(id, type) {
                var url = "api/pgc/objecttypes/" + id + "/" + type;
                return httpFactory.get(url);
            }

            function getObjectsCountByType(id, type) {
                var url = "api/pgc/objecttypes/" + id + "/" + type + "/count";
                return httpFactory.get(url);
            }


            /* --------------------------------SubstanceType-------------------------------------------*/

            function createSubstanceType(planType) {
                var url = "api/pgc/objecttypes/substancetype";
                return httpFactory.post(url, planType);
            }

            function updateSubstanceType(planType) {
                var url = "api/pgc/objecttypes/substancetype/" + planType.id;
                return httpFactory.put(url, planType);
            }

            function deleteSubstanceType(id) {
                var url = "api/pgc/objecttypes/substancetype/" + id;
                return httpFactory.delete(url);
            }

            function deleteObjectType(id) {
                var url = "api/pgc/objecttypes/" + id;
                return httpFactory.delete(url);
            }

            function getSubstanceType(id) {
                var url = "api/pgc/objecttypes/substancetype/" + id;
                return httpFactory.get(url);
            }

            function getAllSubstanceTypes() {
                var url = "api/pgc/objecttypes/substancetype";
                return httpFactory.get(url);
            }

            function getMultipleSubstanceTypes(ids) {
                var url = "api/pgc/objecttypes/substancetype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getSubstanceTypeTree() {
                var url = "api/pgc/objecttypes/substancetype/tree";
                return httpFactory.get(url);
            }


            /*--------------------------------SubstanceGroupType--------------------------------------------*/

            function createSubstanceGroupType(planType) {
                var url = "api/pgc/objecttypes/substancegrouptype";
                return httpFactory.post(url, planType);
            }

            function updateSubstanceGroupType(planType) {
                var url = "api/pgc/objecttypes/substancegrouptype/" + planType.id;
                return httpFactory.put(url, planType);
            }

            function deleteSubstanceGroupType(id) {
                var url = "api/pgc/objecttypes/substancegrouptype/" + id;
                return httpFactory.delete(url);
            }

            function getSubstanceGroupType(id) {
                var url = "api/pgc/objecttypes/substancegrouptype/" + id;
                return httpFactory.get(url);
            }

            function getAllSubstanceGroupTypes() {
                var url = "api/pgc/objecttypes/substancegrouptype";
                return httpFactory.get(url);
            }

            function getMultipleSubstanceGroupTypes(ids) {
                var url = "api/pgc/objecttypes/substancegrouptype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getSubstanceGroupTypeTree() {
                var url = "api/pgc/objecttypes/substancegrouptype/tree";
                return httpFactory.get(url);
            }

            /*    ---------------------------------SpecificationType-----------------------------------------*/

            function createSpecificationType(planType) {
                var url = "api/pgc/objecttypes/specificationtype";
                return httpFactory.post(url, planType);
            }

            function updateSpecificationType(planType) {
                var url = "api/pgc/objecttypes/specificationtype/" + planType.id;
                return httpFactory.put(url, planType);
            }

            function deleteSpecificationType(id) {
                var url = "api/pgc/objecttypes/specificationtype/" + id;
                return httpFactory.delete(url);
            }

            function getSpecificationType(id) {
                var url = "api/pgc/objecttypes/specificationtype/" + id;
                return httpFactory.get(url);
            }

            function getAllSpecificationTypes() {
                var url = "api/pgc/objecttypes/specificationtype";
                return httpFactory.get(url);
            }

            function getMultipleSpecificationTypes(ids) {
                var url = "api/pgc/objecttypes/specificationtype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getSpecificationTypeTree() {
                var url = "api/pgc/objecttypes/specificationtype/tree";
                return httpFactory.get(url);
            }

            /*-------------------------------DeclarationType----------------------------------*/

            function createDeclarationType(declarationtype) {
                var url = "api/pgc/objecttypes/declarationtype";
                return httpFactory.post(url, declarationtype);
            }

            function updateDeclarationType(declarationtype) {
                var url = "api/pgc/objecttypes/declarationtype/" + declarationtype.id;
                return httpFactory.put(url, declarationtype);
            }

            function deleteDeclarationType(id) {
                var url = "api/pgc/objecttypes/declarationtype/" + id;
                return httpFactory.delete(url);
            }

            function getDeclarationType(id) {
                var url = "api/pgc/objecttypes/declarationtype/" + id;
                return httpFactory.get(url);
            }

            function getAllDeclarationTypes() {
                var url = "api/pgc/objecttypes/declarationtype";
                return httpFactory.get(url);
            }

            function getMultipleDeclarationTypes(ids) {
                var url = "api/pgc/objecttypes/declarationtype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getPgcObjectsTree() {
                var url = "api/pgc/objecttypes/tree";
                return httpFactory.get(url);
            }

            function getAllObjectTypeTree() {
                var url = "api/pgc/objecttypes/all/tree";
                return httpFactory.get(url);
            }

            function getDeclarationTypeTree() {
                var url = "api/pgc/objecttypes/declarationtype/tree";
                return httpFactory.get(url);
            }

            function createPGCObjectAttribute(objectId, attribute) {
                var url = "api/pgc/objecttypes/" + objectId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updatePGCObjectAttribute(objectId, attribute) {
                var url = "api/pgc/objecttypes/" + objectId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function getPgcObjectAttributesWithHierarchy(typeId) {
                var url = "api/pgc/objecttypes/type/" + typeId + "/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function getSubstanceByType(typeId, pageable) {
                var url = "api/pgc/substances/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getSubstanceGroupByType(typeId, pageable) {
                var url = "api/pgc/substancegroups/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getSpecificationsByType(typeId, pageable) {
                var url = "api/pgc/specifications/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getDeclarationsByType(typeId, pageable) {
                var url = "api/pgc/declarations/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getUsedPgcObjectAttributesValues(attributeId){
                var url = "api/pgc/objecttypes/attributes/" + attributeId;
                return httpFactory.get(url);
            }

        }
    }
);