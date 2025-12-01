define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('PersonGroupService', PersonGroupService);

        function PersonGroupService(httpFactory) {
            return {
                getPersonGroups: getPersonGroups,
                getAllPersonGroups: getAllPersonGroups,
                createPersonGroup: createPersonGroup,
                updatePersonGroup: updatePersonGroup,
                getSubGroups: getSubGroups,
                getHierarchy: getHierarchy,
                getPersonGrpById: getPersonGrpById,
                getPersonGroupsByPersonId: getPersonGroupsByPersonId,
                getMultiplePersonGroups: getMultiplePersonGroups,
                getPersonGroupReferences: getPersonGroupReferences,
                deletePersonGroup: deletePersonGroup,
                deletePersonGroupMember: deletePersonGroupMember,
                getAllGroupPermissions: getAllGroupPermissions,
                createGroupPermissions: createGroupPermissions,
                deleteGroupPermissions: deleteGroupPermissions,
                findGroupPermissionsByGroupId: findGroupPermissionsByGroupId,
                updatePersonGroups: updatePersonGroups,
                getPersonGroupTree: getPersonGroupTree,
                updateGroupMember: updateGroupMember,
                createGroupMember: createGroupMember,
                getGroupMember: getGroupMember,
                getLoginPersonGroupReferences: getLoginPersonGroupReferences,
                getFilteredGroups: getFilteredGroups,
                getAllPersonGroupNames: getAllPersonGroupNames,
                getDefaultUsersCountByGroup: getDefaultUsersCountByGroup,
                getDefaultValuePreferenceByGroup: getDefaultValuePreferenceByGroup
            };

            function getPersonGroups(pageable) {
                var url = "api/common/persongroups?page={0}&size={1}".format(pageable.page - 1, pageable.size);
                return httpFactory.get(url);
            }

            function getFilteredGroups(pageable, filters) {
                var url = "api/common/persongroups/search?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(filters.searchQuery);
                return httpFactory.get(url);
            }

            function getAllPersonGroups() {
                var url = "api/common/persongroups/all";
                return httpFactory.get(url);
            }

            function deletePersonGroup(personGrpId) {
                var url = "api/common/persongroups/" + personGrpId;
                return httpFactory.delete(url);
            }

            function deletePersonGroupMember(personGrpId, personId) {
                var url = "api/common/persongroups/groupmember/" + personGrpId + "/" + personId;
                return httpFactory.delete(url);
            }

            function getPersonGrpById(id) {
                var url = "api/common/persongroups/" + id;
                return httpFactory.get(url);

            }

            function getDefaultUsersCountByGroup(id) {
                var url = "api/common/persongroups/" + id + "/defaultusers/count";
                return httpFactory.get(url);

            }

            function getDefaultValuePreferenceByGroup(id) {
                var url = "api/common/persongroups/" + id + "/defaultvalue";
                return httpFactory.get(url);

            }

            function getPersonGroupsByPersonId(id) {
                var url = "api/common/persons/" + id + "/groups";
                return httpFactory.get(url);
            }

            function createPersonGroup(personGroup) {
                var url = "api/common/persongroups";
                return httpFactory.post(url, personGroup);
            }

            function updatePersonGroup(personGroup) {
                var url = "api/common/persongroups/" + personGroup.groupId;
                return httpFactory.put(url, personGroup);
            }

            function createGroupMember(personId, goupMember) {
                var url = "api/common/persongroups/groupMember/" + personId;
                return httpFactory.post(url, goupMember);
            }

            function getGroupMember(personId, groupId) {
                var url = "api/common/persongroups/groupMember/" + personId + "/group/" + groupId;
                return httpFactory.get(url);
            }


            function updateGroupMember(personId, groupMember) {
                var url = "api/common/persongroups/groupMember/" + personId;
                return httpFactory.put(url, groupMember);
            }

            function getSubGroups(parent) {
                var url = "api/common/persongroups/" + parent + "/subgroups";
                return httpFactory.get(url);
            }

            function getHierarchy() {
                var url = "api/common/persongroups/hierarchy";
                return httpFactory.get(url);
            }

            function getMultiplePersonGroups(ids) {
                var url = "api/common/persongroups/multiple/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getAllGroupPermissions() {
                var url = "api/common/persongroups/grouppermissions";
                return httpFactory.get(url);
            }

            function createGroupPermissions(personGroups) {
                var url = "api/common/persongroups/grouppermissions";
                return httpFactory.post(url, personGroups);
            }

            function deleteGroupPermissions(personGroups) {
                var url = "api/common/persongroups/grouppermissions/delete";
                return httpFactory.post(url, personGroups);
            }

            function findGroupPermissionsByGroupId(groupId) {
                var url = "api/common/persongroups/grouppermissions/group/" + groupId;
                return httpFactory.get(url);
            }

            function updatePersonGroups(personId, personGroups) {
                var url = "api/common/persongroups/person/" + personId;
                return httpFactory.post(url, personGroups);
            }

            function getPersonGroupTree() {
                var url = "api/common/persongroups/all/groupTree";
                return httpFactory.get(url);
            }

            function getAllPersonGroupNames() {
                var url = "api/common/persongroups/all/names";
                return httpFactory.get(url);
            }

            function getPersonGroupReferences(objects, property) {
                var groupIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && groupIds.indexOf(object[property]) == -1) {
                        groupIds.push(object[property]);
                    }
                });

                if (groupIds.length > 0) {
                    getMultiplePersonGroups(groupIds).then(
                        function (groups) {
                            var map = new Hashtable();
                            angular.forEach(groups, function (group) {
                                map.put(group.groupId, group);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var group = map.get(object[property]);
                                    if (group != null) {
                                        object[property + "Object"] = group;
                                    }
                                }
                            });
                        }
                    );
                }
            }

            function getLoginPersonGroupReferences(objects, property) {
                var groupIds = [];
                angular.forEach(objects, function (object) {
                    if (object.person[property] != null && groupIds.indexOf(object.person[property]) == -1) {
                        groupIds.push(object.person[property]);
                    }
                });

                if (groupIds.length > 0) {
                    getMultiplePersonGroups(groupIds).then(
                        function (groups) {
                            var map = new Hashtable();
                            angular.forEach(groups, function (group) {
                                map.put(group.groupId, group);
                            });

                            angular.forEach(objects, function (object) {
                                if (object.person[property] != null) {
                                    var group = map.get(object.person[property]);
                                    if (group != null) {
                                        object.person[property + "Object"] = group;
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