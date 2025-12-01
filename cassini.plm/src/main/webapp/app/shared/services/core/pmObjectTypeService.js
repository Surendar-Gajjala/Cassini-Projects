define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('PMObjectTypeService', PMObjectTypeService);

        function PMObjectTypeService($q, httpFactory) {
            return {

                createReqDocType: createReqDocType,
                updateReqDocType: updateReqDocType,
                deleteReqDocType: deleteReqDocType,
                deleteObjectType: deleteObjectType,
                getReqDocType: getReqDocType,
                getAllReqDocTypes: getAllReqDocTypes,
                getMultipleReqDocTypes: getMultipleReqDocTypes,
                getReqDocTypeTree: getReqDocTypeTree,

                getPMObjectTypeByType: getPMObjectTypeByType,
                getObjectsCountByType: getObjectsCountByType,
                getMesObjectsTree: getMesObjectsTree,
                getAllPMObjectTypeTree: getAllPMObjectTypeTree,
                updatePmObjectAttributes: updatePmObjectAttributes,

                createReqType: createReqType,
                updateReqType: updateReqType,
                deleteReqType: deleteReqType,
                getReqType: getReqType,
                getAllReqTypes: getAllReqTypes,
                getMultipleReqTypes: getMultipleReqTypes,
                getReqTypeTree: getReqTypeTree,
                getProjectTypeTree: getProjectTypeTree,

                createPmType: createPmType,
                updatePmType: updatePmType,
                deletePmType: deletePmType,
                getPmType: getPmType,
                getAllPmTypes: getAllPmTypes,
                getMultiplePmTypes: getMultiplePmTypes,
                getPmTypeTree: getPmTypeTree,
                getTypeObjectCount: getTypeObjectCount,

                getReqObjectAttributesWithHierarchy: getReqObjectAttributesWithHierarchy,
                getPMObjectAttributesWithHierarchy: getPMObjectAttributesWithHierarchy,
                createReqObjectAttribute: createReqObjectAttribute,
                updateReqObjectAttribute: updateReqObjectAttribute,

                getReqDocsByType: getReqDocsByType,
                getRequirementsByType: getRequirementsByType

            };

            function getPMObjectTypeByType(id, type) {
                var url = "api/plm/requirementtypes/" + id + "/" + type;
                return httpFactory.get(url);
            }

            function getObjectsCountByType(id, type) {
                var url = "api/plm/requirementtypes/" + id + "/" + type + "/count";
                return httpFactory.get(url);
            }


            /* --------------------------------ReqDocType-------------------------------------------*/

            function createPmType(planType) {
                var url = "api/plm/requirementtypes/pmtype";
                return httpFactory.post(url, planType);
            }

            function updatePmType(planType) {
                var url = "api/plm/requirementtypes/pmtype/" + planType.id;
                return httpFactory.put(url, planType);
            }

            function deletePmType(id) {
                var url = "api/plm/requirementtypes/pmtype/" + id;
                return httpFactory.delete(url);
            }

            function getPmType(id) {
                var url = "api/plm/requirementtypes/pmtype/" + id;
                return httpFactory.get(url);
            }

            function getAllPmTypes() {
                var url = "api/plm/requirementtypes/pmtype";
                return httpFactory.get(url);
            }

            function getMultiplePmTypes(ids) {
                var url = "api/plm/requirementtypes/pmtype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getPmTypeTree() {
                var url = "api/plm/requirementtypes/pmtype/tree";
                return httpFactory.get(url);
            }

            function getTypeObjectCount(id) {
                var url = "api/plm/requirementtypes/pmtype/" + id + "/count";
                return httpFactory.get(url);
            }

            function updatePmObjectAttributes(pmObjectId,pmObject) {
                var url = "api/plm/requirementtypes/pmtype/object/" + pmObjectId + "/attributes";
                return httpFactory.put(url, pmObject);
            }


            /* --------------------------------ReqDocType-------------------------------------------*/

            function createReqDocType(planType) {
                var url = "api/plm/requirementtypes/documenttype";
                return httpFactory.post(url, planType);
            }

            function updateReqDocType(planType) {
                var url = "api/plm/requirementtypes/documenttype/" + planType.id;
                return httpFactory.put(url, planType);
            }

            function deleteReqDocType(id) {
                var url = "api/plm/requirementtypes/documenttype/" + id;
                return httpFactory.delete(url);
            }

            function deleteObjectType(id) {
                var url = "api/plm/requirementtypes/documenttype" + id;
                return httpFactory.delete(url);
            }

            function getReqDocType(id) {
                var url = "api/plm/requirementtypes/reqdoctype/" + id;
                return httpFactory.get(url);
            }

            function getAllReqDocTypes() {
                var url = "api/plm/requirementtypes/documenttype";
                return httpFactory.get(url);
            }

            function getMultipleReqDocTypes(ids) {
                var url = "api/plm/requirementtypes/documenttype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getReqDocTypeTree() {
                var url = "api/plm/requirementtypes/documenttype/tree";
                return httpFactory.get(url);
            }

            function createReqObjectAttribute(objectId, attribute) {
                var url = "api/plm/requirementtypes/" + objectId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateReqObjectAttribute(objectId, attribute) {
                var url = "api/plm/requirementtypes/" + objectId + "/attributes";
                return httpFactory.put(url, attribute);
            }


            /*--------------------------------ReqType--------------------------------------------*/

            function createReqType(planType) {
                var url = "api/plm/requirementtypes/reqtype";
                return httpFactory.post(url, planType);
            }

            function updateReqType(planType) {
                var url = "api/plm/requirementtypes/reqtype/" + planType.id;
                return httpFactory.put(url, planType);
            }

            function deleteReqType(id) {
                var url = "api/plm/requirementtypes/reqtype/" + id;
                return httpFactory.delete(url);
            }

            function getReqType(id) {
                var url = "api/plm/requirementtypes/reqtype/" + id;
                return httpFactory.get(url);
            }

            function getAllReqTypes() {
                var url = "api/plm/requirementtypes/reqtype";
                return httpFactory.get(url);
            }

            function getMultipleReqTypes(ids) {
                var url = "api/plm/requirementtypes/reqtype/multiple/[{ids}]";
                return httpFactory.get(url);
            }

            function getReqTypeTree() {
                var url = "api/plm/requirementtypes/reqtype/tree";
                return httpFactory.get(url);
            }

            function getProjectTypeTree(type) {
                var url = "api/plm/projects/projecttype/" + type + "/tree";
                return httpFactory.get(url);
            }

            function getMesObjectsTree() {
                var url = "api/plm/requirementtypes/tree";
                return httpFactory.get(url);
            }

            function getAllPMObjectTypeTree() {
                var url = "api/plm/requirementtypes/all/tree";
                return httpFactory.get(url);
            }

            function getReqObjectAttributesWithHierarchy(typeId) {
                var url = "api/plm/requirementtypes/type/" + typeId + "/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function getPMObjectAttributesWithHierarchy(typeId) {
                var url = "api/plm/requirementtypes/pmtype/type/" + typeId + "/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function getReqDocsByType(typeId, pageable) {
                var url = "api/req/documents/type/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getRequirementsByType(typeId, pageable) {
                var url = "api/req/documents/reqtype/{0}?page={1}&size={2}&sort={3}:{4}".
                    format(typeId, pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }


        }
    }
);