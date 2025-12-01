define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('SecurityPermissionService', SecurityPermissionService);

        function SecurityPermissionService(httpFactory) {
            return {
                getGroupSecurityPermissionsByGroupId: getGroupSecurityPermissionsByGroupId,
                getObjectTypes: getObjectTypes,
                createSecutiryPermission: createSecutiryPermission,
                getAllSecurityPermissions: getAllSecurityPermissions,
                getSecurityPermissionById: getSecurityPermissionById,
                updateSecutiryPermission: updateSecutiryPermission,
                deletePermission: deletePermission,
                saveCriteria: saveCriteria,
                getFilteredSecurityPermissions: getFilteredSecurityPermissions,
                createMultiplePermissions: createMultiplePermissions,
                deleteGroupSecurityPermission: deleteGroupSecurityPermission,
                getTypeAttributes: getTypeAttributes,
                getObjectType: getObjectType,
                getModuleType: getModuleType,
                getAllSecurityPermissionsByObjectType: getAllSecurityPermissionsByObjectType,
                saveDMPermissions: saveDMPermissions,
                getAllDMPermissions: getAllDMPermissions,
                getDMPermissionByIdAndType: getDMPermissionByIdAndType
            };

            function getGroupSecurityPermissionsByGroupId(group) {
                var url = "api/securityPermission/permissions/" + group;
                return httpFactory.get(url);
            }

            function getObjectTypes() {
                var url = "api/securityPermission/objecttypes";
                return httpFactory.get(url);
            }

            function getObjectType(id) {
                var url = "api/securityPermission/objecttype/" + id;
                return httpFactory.get(url);
            }

            function getAllSecurityPermissionsByObjectType(objectType) {
                var url = "api/securityPermission/objecttype/" + objectType + "/all";
                return httpFactory.get(url);
            }

            function getTypeAttributes(typeId, type) {
                var url = "api/securityPermission/gettypeattributes/" + typeId + "/type/" + type;
                return httpFactory.get(url);
            }

            function getSecurityPermissionById(permissionId) {
                var url = "api/securityPermission/" + permissionId;
                return httpFactory.get(url);
            }

            function createSecutiryPermission(securityPermission) {
                var url = "api/securityPermission";
                return httpFactory.post(url, securityPermission);
            }

            function updateSecutiryPermission(securityPermission) {
                var url = "api/securityPermission";
                return httpFactory.put(url, securityPermission);
            }

            function deletePermission(id) {
                var url = "api/securityPermission/" + id;
                return httpFactory.delete(url);
            }

            function getAllSecurityPermissions(pageable, filters) {
                var url = "api/securityPermission/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&name={0}&description={1}&objectType={2}&subType={3}&privilege={4}&attribute={5}&criteria={6}&searchQuery={7}&module={8}".
                    format(filters.name, filters.description, filters.objectType, filters.subType
                    , filters.privilege, filters.attribute, filters.criteria, filters.searchQuery, filters.module);
                return httpFactory.get(url);
            }

            function getFilteredSecurityPermissions(pageable, filters) {
                var url = "api/securityPermission/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&id={0}&name={1}&description={2}&objectType={3}&subType={4}&privilege={5}&attribute={6}&criteria={7}&searchQuery={8}&privilegeType={9}&attributeGroup={10}&module={11}".
                    format(filters.id, filters.name, filters.description, filters.objectType, filters.subType
                    , filters.privilege, filters.attribute, filters.criteria, filters.searchQuery, filters.privilegeType, filters.attributeGroup, filters.module);
                return httpFactory.get(url);
            }

            function saveCriteria(permissionId, criteria) {
                var url = "api/securityPermission/criteria/" + permissionId;
                return httpFactory.post(url, criteria);
            }

            function createMultiplePermissions(groupId, securityPermissions) {
                var url = "api/securityPermission/create/groupSecurityPermission/" + groupId;
                return httpFactory.post(url, securityPermissions);
            }

            function deleteGroupSecurityPermission(permissionId, groupId) {
                var url = "api/securityPermission/delete/groupSecurityPermission/" + permissionId + "/group/" + groupId;
                return httpFactory.delete(url);
            }

            function getModuleType(objectType) {
                var url = "api/securityPermission/getModuleType/objectType/" + objectType;
                return httpFactory.get(url);
            }

            function saveDMPermissions(dmPermissions) {
                var url = "api/securityPermission/saveDMPermissions";
                return httpFactory.post(url, dmPermissions);
            }

            function getAllDMPermissions() {
                var url = "api/securityPermission/getAllDMPermissions";
                return httpFactory.get(url);
            }

            function getDMPermissionByIdAndType(id, type){
                var url = "api/securityPermission/getDMPermissionByIdAndType/" + id + "/type/" + type;
                return httpFactory.get(url);

            }
        }
    }
);