define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/profileService'
    ],
    function (module) {
        module.controller('AllRolesController', AllRolesController);

        function AllRolesController($scope, $rootScope, $injector, $sce, $translate, $cookieStore, $window, $timeout, $application,
                                    $state, $stateParams, $cookies, PersonGroupService, DialogService) {

            var vm = this;

            var parsed = angular.element("<div></div>");

            vm.pageable = {
                page: 0,
                size: 30,
                sort: {
                    field: "name",
                    order: "ASC"
                }
            };
            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.filters = {
                searchQuery: null
            };
            vm.roles = angular.copy(pagedResults);

            vm.freeTextSearch = freeTextSearch;
            vm.showRoleDetails = showRoleDetails;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.showRoleDetails = showRoleDetails;

            function showRoleDetails(role) {
                $state.go('app.roledetails.basic', {roleId: role.groupId})
            }

            vm.noResults = false;
            function loadGroups() {
                $rootScope.showBusyIndicator($('.view-container'));
                PersonGroupService.getFilteredGroups(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.roles = data;
                        angular.forEach(vm.roles.content, function (role) {
                            angular.forEach(role.groupMembers, function (groupMember) {
                                var firstLetter = "";
                                var lastLetter = "";
                                if (groupMember.person.firstName != null && groupMember.person.firstName != "") {
                                    firstLetter = groupMember.person.firstName.substring(0, 1).toUpperCase();
                                }
                                if (groupMember.person.lastName != null && groupMember.person.lastName != "") {
                                    lastLetter = groupMember.person.lastName.substring(0, 1).toUpperCase();
                                }
                                groupMember.imageWord = firstLetter + "" + lastLetter;
                                if (groupMember.person.hasImage) {
                                    groupMember.person.personImage = "api/common/persons/" + groupMember.person.id + "/image/download?" + new Date().getTime();
                                }
                            })
                        });
                        if (vm.searchMode == true && vm.roles.content.length == 0) {
                            vm.noResults = true;
                        }
                        resizeView();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function resizeView() {
                var contentpanel = $('.contentpanel').outerHeight();
                $('.view-content').height(contentpanel - 50);
            }

            function nextPage() {
                if (vm.roles.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    loadGroups();
                }
            }

            function previousPage() {
                if (vm.roles.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    loadGroups();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadGroups();
            }

            vm.searchMode = false;
            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.searchMode = true;
                    vm.filters.searchQuery = freeText;
                    loadGroups();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.roles = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                vm.searchMode = false;
                vm.noResults = false;
                $rootScope.showBusyIndicator($('.view-container'));
                loadGroups();
            }


            $rootScope.newRole = newRole;
            function newRole() {
                var options = {
                    title: "New Role",
                    template: 'app/desktop/modules/admin/role/new/newGroupView.jsp',
                    controller: 'NewGroupController as newGroupVm',
                    resolve: 'app/desktop/modules/admin/role/new/newGroupController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: "Create", broadcast: 'app.groups.new'}
                    ],
                    callback: function () {
                        loadGroups();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            $scope.deleteRoleTitle = parsed.html($translate.instant("DELETE_ROLE")).html();
            var deleteRoleDialogMessage = parsed.html($translate.instant("DELETE_ROLE_DIALOG_MESSAGE")).html();
            var roleDeletedMessage = parsed.html($translate.instant("ROLE_DELETED_MESSAGE")).html();
            var groupDefaultDeleteMessage = parsed.html($translate.instant("GROUP_DEFAULT_DELETE_MSG")).html();
            var roleUsedInDefaultValue = parsed.html($translate.instant("ROLE_USED_IN_DV")).html();
            $scope.clickToShowDetails = parsed.html($translate.instant("CLICK_TO_SHOW_DETAILS")).html();
            vm.deleteRole = deleteRole;
            function deleteRole(role) {
                $rootScope.showBusyIndicator($('.view-container'));
                PersonGroupService.getDefaultUsersCountByGroup(role.groupId).then(
                    function (data) {
                        if (data == 0) {
                            PersonGroupService.getDefaultValuePreferenceByGroup(role.groupId).then(
                                function (data) {
                                    var deleteDialogMsg = "";
                                    if (data == null || data == "") {
                                        deleteDialogMsg = deleteRoleDialogMessage.format(role.name);
                                    } else {
                                        var deleteValue = parsed.html($translate.instant(data.preferenceKey)).html();
                                        deleteDialogMsg = roleUsedInDefaultValue.format(role.name, deleteValue, role.name);
                                    }
                                    $rootScope.hideBusyIndicator();
                                    var options = {
                                        title: $scope.deleteRoleTitle,
                                        message: deleteDialogMsg,
                                        okButtonClass: 'btn-danger'
                                    };

                                    DialogService.confirm(options, function (yes) {
                                        if (yes == true) {
                                            $rootScope.showBusyIndicator($('.view-container'));
                                            PersonGroupService.deletePersonGroup(role.groupId).then(
                                                function (data) {
                                                    $rootScope.showSuccessMessage(roleDeletedMessage);
                                                    loadGroups();
                                                }, function (error) {
                                                    $rootScope.showErrorMessage(error.message);
                                                    $rootScope.hideBusyIndicator();
                                                }
                                            )
                                        }
                                    });
                                }
                            )
                        } else {
                            $rootScope.showWarningMessage(groupDefaultDeleteMessage.format(role.name, data));
                            $rootScope.hideBusyIndicator();
                        }
                    }
                )
            }

            (function () {
                loadGroups();
                $scope.$on('app.roles.search', function (event, data) {
                    freeTextSearch(data.searchText)
                });
            })();
        }
    }
);