define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('ProfileService', ProfileService);

        function ProfileService(httpFactory) {
            return {
                getAllProfiles: getAllProfiles,
                getNavigationMenu: getNavigationMenu,
                createProfile: createProfile,
                updateProfile: updateProfile,
                deleteProfile: deleteProfile,
                getGroupProfiles: getGroupProfiles,
                deleteGroupProfile: deleteGroupProfile,
                getProfileGroups: getProfileGroups
            };

            function createProfile(profile) {
                var url = "api/common/profiles";
                return httpFactory.post(url, profile);
            }

            function getAllProfiles() {
                var url = "api/common/profiles/all";
                return httpFactory.get(url);
            }

            function getNavigationMenu(url) {
                return httpFactory.get(url);
            }

            function updateProfile(profile) {
                var url = "api/common/profiles/" + profile.id;
                return httpFactory.put(url, profile);
            }

            function deleteProfile(profileId) {
                var url = "api/common/profiles/" + profileId;
                return httpFactory.delete(url);
            }

            function getGroupProfiles(groupId) {
                var url = "api/common/profiles/by/group/" + groupId;
                return httpFactory.get(url);
            }

            function getProfileGroups(profileId) {
                var url = "api/common/profiles/by/profile/" + profileId;
                return httpFactory.get(url);
            }

            function deleteGroupProfile(groupId, profileId) {
                var url = "api/common/profiles/group/" + groupId + "/" + profileId;
                return httpFactory.delete(url);
            }
        }
    }
);