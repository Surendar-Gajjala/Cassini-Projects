/**
 * Created by Nageshreddy on 10-12-2020.
 */
define(['app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/profileService',
        'app/shared/services/core/itemTypeService'
    ],
    function (module) {
        module.controller('ProfilesController', ProfilesController);

        function ProfilesController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                    ProfileService, CommonService, $translate, $i18n) {
            var vm = this;

            var profileDelConfirmMessage = $i18n.getValue("PROFILE_DELETE_CONFIRMATION");
            var profileUpdateMessage = $i18n.getValue("PROFILE_UPDATE_MESSAGE");
            var profileAddedToGroupMessage = $i18n.getValue("PROFILE_ADDED_TO_GROUP_MESSAGE");
            var profileSavedMessage = $i18n.getValue("PROFILE_SAVED_MESSAGE");
            var profileDeleteMessage = $i18n.getValue("PROFILE_DELETED_SUCCESSFULLY");
            var validateProfileName = $i18n.getValue("VALIDATE_PROFILE_NAME");
            var deleteProfileTitle = $i18n.getValue("DELETE_PROFILE");
            vm.removeProfileTitle = $i18n.getValue("REMOVE_PROFILE");
            vm.editProfileTitle = $i18n.getValue("EDIT_PROFILE");
            vm.deleteProfileTitle = $i18n.getValue("DELETE_PROFILE");
            vm.updateTitle = $i18n.getValue("UPDATE");
            vm.cancelTitle = $i18n.getValue("CANCEL");
            vm.newProfileTitle = $i18n.getValue("NEW_PROFILE");
            vm.saveTitle = $i18n.getValue("SAVE");
            var profileNameValidation = $i18n.getValue("PROFILE_NAME_VALIDATION");
            vm.copyProfileTitle = $i18n.getValue("COPY_PROFILE");
            vm.selectAll = false;
            vm.selectedProfile = null;
            vm.profiles = [];
            vm.navigation = [];
            vm.navigationItems = [];
            vm.navigationMap = new Hashtable();

            var newProfile = {
                id: null,
                editMode: true,
                isNew: true,
                selected: true,
                name: "New Profile",
                defaultProfile: false,
                profileData: []
            };


            function loadProfiles() {
                ProfileService.getAllProfiles().then(
                    function (data) {
                        vm.profiles = data;
                        angular.forEach(vm.profiles, function (profile) {
                            profile.isNew = false;
                        });
                        loadNavigation();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            var mapSize = 0;

            function loadNavigation() {
                var url = "app/desktop/modules/navigation.json";
                ProfileService.getNavigationMenu(url).then(
                    function (data) {
                        mapSize = 0;
                        angular.forEach(data.navigation, function (nav) {
                            nav.level = 0;
                            nav.selected = false;
                            nav.expanded = false;
                            vm.navigationItems.push(nav);

                            vm.navigationMap.put(nav.id, nav);
                            mapSize++;
                            angular.forEach(nav.children, function (child) {
                                child.level = 1;
                                child.selected = false;
                                child.expanded = false;
                                vm.navigationMap.put(child.id, child);
                                mapSize++;
                            });
                        });
                        vm.navigation = data.navigation;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            vm.toggleNode = toggleNode;
            function toggleNode(nav) {
                var index = findIndexOf(nav);
                if (nav.expanded) {
                    if (index !== -1) {
                        vm.navigationItems.splice(index + 1, nav.children.length);
                    }
                }
                else {
                    if (index !== -1) {
                        index++;
                        for (var i = 0; i < nav.children.length; i++) {
                            vm.navigationItems.splice(index, 0, nav.children[i]);
                            index++;
                        }
                    }
                }

                nav.expanded = !nav.expanded;

                updateSelections();
            }

            vm.toggleSelection = toggleSelection;
            function toggleSelection(nav) {

                if (!nav.id.includes('menu')) {
                    var nav1 = nav.id.split('.')[0];
                    nav1 = nav1 + ".menu";
                    var nav2 = vm.navigationMap.get(nav1);
                    if (!nav.selected && nav2 != null) {
                        nav2.selected = false;
                        addOrRemoveNavItem(nav2);
                    } else if (nav.selected && nav2 != null) {
                        var select1 = true;
                        angular.forEach(nav2.children, function (child) {
                            if (!child.selected && select1) {
                                select1 = false;
                            }
                        });
                        if (select1) {
                            nav2.selected = select1;
                            addOrRemoveNavItem(nav2);
                        }
                    }
                }

                addOrRemoveNavItem(nav);

                angular.forEach(nav.children, function (child) {
                    child.selected = nav.selected;
                    addOrRemoveNavItem(child);
                });
                saveProfile(vm.selectedProfile, true);
            }

            function addOrRemoveNavItem(nav) {
                var index;
                if (vm.selectedProfile != null) {
                    if (nav.selected) {
                        index = vm.selectedProfile.profileData.indexOf(nav.id);
                        if (index === -1) {
                            vm.selectedProfile.profileData.push(nav.id);
                            if (vm.selectedProfile != null && vm.selectedProfile.profileData.length == mapSize) {
                                vm.selectAll = true;
                            }
                        }

                    }
                    else {
                        index = vm.selectedProfile.profileData.indexOf(nav.id);
                        if (index !== -1) {
                            vm.selectedProfile.profileData.splice(index, 1);
                            vm.selectAll = false;
                        }
                    }

                }
            }

            function findIndexOf(nav) {
                var index = -1;
                for (var i = 0; i < vm.navigationItems.length; i++) {
                    if (nav.id === vm.navigationItems[i].id) {
                        index = i;
                        break;
                    }
                }
                return i;
            }

            vm.addNewProfile = addNewProfile;
            function addNewProfile() {
                if (vm.selectedProfile != null) {
                    vm.selectedProfile.selected = false;
                }

                var profile = angular.copy(newProfile);
                vm.profiles.push(profile);
                var index = vm.profiles.indexOf(profile);
                if (index !== -1) {
                    $timeout(function () {
                        $('#profile' + index).focus();
                        $('#profile' + index).select();
                    });
                }

                vm.selectedProfile = profile;
            }

            vm.copyProfile = copyProfile;
            function copyProfile(profile1) {
                if (vm.selectedProfile != null) {
                    vm.selectedProfile.selected = false;
                }
                var profile = angular.copy(profile1);
                profile.id = null;
                profile.editMode = true;
                profile.isNew = true;
                profile.selected = true;
                profile.defaultProfile = false;
                profile.name = profile1.name + ' Copy';
                vm.profiles.push(profile);
                var index = vm.profiles.indexOf(profile);
                if (index !== -1) {
                    $timeout(function () {
                        $('#profile' + index).focus();
                        $('#profile' + index).select();
                    });
                }

                vm.selectedProfile = profile;
            }

            vm.onEnter = onEnter;
            function onEnter(event, profile) {
                if (event.keyCode == 10 || event.keyCode == 13) {
                    saveProfile(profile);
                }
            }

            vm.updateProfile = updateProfile;
            function updateProfile(profile) {
                $timeout(function () {
                    saveProfile(profile);
                }, 300)
            }

            vm.saveProfile = saveProfile;
            function saveProfile(profile, flag) {
                if (validate(profile)) {
                    if (profile != null) {
                        var promise = null;
                        if (profile.id == null) {
                            promise = ProfileService.createProfile(profile);
                        }
                        else {
                            promise = ProfileService.updateProfile(profile);
                        }
                        promise.then(
                            function (data) {
                                profile.id = data.id;
                                profile.editMode = false;
                                profile.isNew = false;
                                profile = [];
                                if (flag) {
                                    $rootScope.showSuccessMessage(profileSavedMessage);
                                } else {
                                    $rootScope.showSuccessMessage(profileSavedMessage);
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                var index = vm.profiles.indexOf(profile);
                                if (index !== -1) {
                                    vm.profiles.splice(index, 1);
                                    vm.selectedProfile = null;
                                }
                            }
                        );
                    }
                }
            }

            function validate(profile) {
                var valid = true;
                if (profile.name == null || profile.name == '') {
                    $rootScope.showErrorMessage(profileNameValidation);
                    valid = false;
                }
                return valid;
            }

            vm.setEditMode = setEditMode;
            var profileName = null;

            function setEditMode(profile) {
                profile.editMode = true;
                profileName = profile.name;
                var index = vm.profiles.indexOf(profile);
                if (index !== -1) {
                    $timeout(function () {
                        $('#profile' + index).focus();
                        $('#profile' + index).select();
                    });
                }
            }

            vm.cancelEditProfile = cancelEditProfile;
            function cancelEditProfile(profile) {
                profile.name = profileName;
                profile.editMode = false;
            }


            vm.selectProfile = selectProfile;
            function selectProfile(profile) {
                if (vm.selectedProfile != null) {
                    vm.selectedProfile.selected = false;
                }

                profile.selected = true;
                vm.selectedProfile = profile;
                updateSelections();
            }

            vm.deleteProfile = deleteProfile;
            function deleteProfile(profile) {
                ProfileService.getProfileGroups(profile.id).then(
                    function (groups1) {
                        if (groups1 == null || groups1.length == 0) {
                            var options = {
                                title: deleteProfileTitle,
                                message: profileDelConfirmMessage,
                                okButtonClass: 'btn-danger'
                            };

                            DialogService.confirm(options, function (yes) {
                                if (yes === true) {
                                    ProfileService.deleteProfile(profile.id).then(
                                        function (data) {
                                            var index = vm.profiles.indexOf(profile);
                                            if (index !== -1) {
                                                vm.profiles.splice(index, 1);
                                                vm.selectedProfile = null;
                                                $rootScope.showSuccessMessage(profileDeleteMessage);
                                                updateSelections();
                                            }
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    );
                                }
                            });
                        } else {
                            $rootScope.showInfoMessage(profileAddedToGroupMessage);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function updateSelections() {
                vm.selectAll = false;
                angular.forEach(vm.navigation, function (nav) {
                    nav.selected = false;
                    if (vm.selectedProfile != null) {
                        var index = vm.selectedProfile.profileData.indexOf(nav.id);
                        nav.selected = index !== -1;
                    }

                    angular.forEach(nav.children, function (child) {
                        child.selected = false;
                        if (vm.selectedProfile != null) {
                            var index = vm.selectedProfile.profileData.indexOf(child.id);
                            child.selected = index !== -1;
                        }
                    });
                });
                if (vm.selectedProfile != null && vm.selectedProfile.profileData.length == mapSize) {
                    vm.selectAll = true;
                }
            }

            vm.toggleSelectAll = toggleSelectAll;
            function toggleSelectAll() {
                angular.forEach(vm.navigation, function (nav) {
                    nav.selected = vm.selectAll;
                    angular.forEach(nav.children, function (child) {
                        child.selected = vm.selectAll;
                        addOrRemoveNavItem(child);
                    });

                    addOrRemoveNavItem(nav);
                });
                saveProfile(vm.selectedProfile, true);
            }

            function selectText(containerid) {
                if (document.selection) {
                    var range = document.body.createTextRange();
                    range.moveToElementText(document.getElementById(containerid));
                    range.select();
                } else if (window.getSelection) {
                    var range = document.createRange();
                    range.selectNode(document.getElementById(containerid));
                    window.getSelection().addRange(range);
                }
            }

            vm.cancelProfile = cancelProfile;
            function cancelProfile(profile) {
                var index = vm.profiles.indexOf(profile);
                if (index !== -1) {
                    vm.profiles.splice(index, 1);
                }
                vm.selectedProfile = null;
            }

            (function () {
                loadProfiles();
            })();
        }
    }
)
;