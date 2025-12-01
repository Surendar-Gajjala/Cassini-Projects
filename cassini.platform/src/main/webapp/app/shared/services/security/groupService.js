define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('GroupService', GroupService);

        function GroupService(httpFactory) {
            return {
                getGroupsGrid: getGroupsGrid,
                getAllGroups: getAllGroups,
                createGroup: createGroup,
                saveGroup: saveGroup,
                saveGroups: saveGroups,
                getLoginGroups: getLoginGroups,
                saveLoginGroups: saveLoginGroups,
                getAllPermissionsByGroupId: getAllPermissionsByGroupId,
                getAllLeafGroups: getAllLeafGroups,
                getGroupById: getGroupById,
                getGroupPermissionsByGroupId: getGroupPermissionsByGroupId
            };

            function getGroupsGrid(groupPermGridUrl) {

                return httpFactory.get(groupPermGridUrl);
            }

            function getAllGroups() {
                var url = "api/security/group";
                return httpFactory.get(url);
            }

            function getAllPermissionsByGroupId(group) {
                var url = "api/security/group/" + group.groupId + "/permissions";
                return httpFactory.get(url);
            }

            function createGroup(group) {
                var url = "api/security/group/new";
                return httpFactory.post(url, group);
            }

            function saveGroup(group) {
                var url = "api/security/group/" + group.groupId;
                return httpFactory.put(url, group);
            }

            function saveGroups(groups) {

                var url = "api/security/group";
                return httpFactory.post(url, groups);
            }

            function getLoginGroups(login) {

                var url = "api/security/login/{0}/groups".format(login.id);
                return httpFactory.get(url);

            }

            function saveLoginGroups(login, groups) {
                var url = "api/security/login/{0}/groups".format(login.id);
                return httpFactory.post(url, groups);
            }

            function getAllLeafGroups() {
                var url = "api/security/group/leafNodes";
                return httpFactory.get(url);
            }

            function getGroupById(groupId) {
                var url = "api/security/group/" + groupId;
                return httpFactory.get(url);
            }

            function getGroupPermissionsByGroupId(group) {
                var url = "api/security/login/permissions/" + group;
                return httpFactory.get(url);
            }
        }
    }
);