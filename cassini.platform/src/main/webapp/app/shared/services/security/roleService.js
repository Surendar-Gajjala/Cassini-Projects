define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('RoleService',RoleService);

        function RoleService (httpFactory) {
            return {
                getRolesGrid: getRolesGrid,
                getAllRoles: getAllRoles,
                getRolesAll:getRolesAll,
                getRoleById: getRoleById,
                getRolesByPersonId:getRolesByPersonId,
                deleteRoleById: deleteRoleById,
                createRole: createRole,
                saveRole: saveRole,
                updateRole:updateRole,
                saveRoles:saveRoles,
                getLoginRoles:getLoginRoles,
                getPersonRoles:getPersonRoles,
                saveLoginRoles: saveLoginRoles,
                deletePersonRole:deletePersonRole,
                deletePersonGroupRole:deletePersonGroupRole
            };

            function getRolesGrid(groupPermGridUrl) {

                var url = "app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/security/role/rolesGrid.json";
                if(groupPermGridUrl!=null && groupPermGridUrl!=undefined && groupPermGridUrl.length>0){
                    url=groupPermGridUrl;
                }
                return httpFactory.get(url);
            }

            function getAllRoles(pageable) {
                var url = "api/security/role?page={0}&size={1}".format(pageable.page - 1, pageable.size);
                return httpFactory.get(url);
            }

            function getRolesAll() {
                var url = "api/security/role/all";
                return httpFactory.get(url);
            }

            function getRoleById(roleId) {
                var url = "api/security/role/"+ roleId;
                return httpFactory.get(url);
            }

            function getRolesByPersonId(personId) {
                var url = "api/security/role/" + personId + "/users";
                return httpFactory.get(url);
            }

            function deleteRoleById(roleId) {
                var url = "api/security/role/"+ roleId;
                return httpFactory.delete(url);
            }

            function deletePersonRole(personId,roleId) {
                var url = "api/security/role/personrole/"+ personId+"/"+roleId;
                return httpFactory.delete(url);
            }

            function deletePersonGroupRole(groupId,roleId) {
                var url = "api/security/role/persongrouprole/"+ groupId+"/"+roleId;
                return httpFactory.delete(url);
            }


            function createRole(role) {
                var  url = "api/security/role/new";
                return httpFactory.post(url, role);
            }

            function saveRole(role) {
                var url = "api/security/role/" + role.id;
                return httpFactory.put(url, role);
            }

            function updateRole(role) {
                var url = "api/security/role/" + role.id;
                return httpFactory.put(url, role);
            }


            function saveRoles(roles){

                var  url = "api/security/role";
                return httpFactory.post(url, roles);
            }

            function getLoginRoles(login){

                var url = "api/security/login/{0}/roles".format(login.id);
                return httpFactory.get(url);

            }

            function getPersonRoles(personId){

                var url = "api/security/person/{0}/roles".format(personId);
                return httpFactory.get(url);

            }

            function saveLoginRoles(login,roles) {
                var url = "api/security/login/{0}/roles".format(login.id);
                return httpFactory.post(url, roles);
            }

        }
    }
);