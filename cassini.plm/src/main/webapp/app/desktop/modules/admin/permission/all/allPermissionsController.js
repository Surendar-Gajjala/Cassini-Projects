define(
    [
        'app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/modules/securityPermission/securityPermissionService',
        'app/desktop/modules/admin/permission/criteria/newCriteriaController'
    ],
    function (module) {
        module.controller('AllPermissionsController', AllPermissionsController);

        function AllPermissionsController($scope, $rootScope, $sce, $translate, $timeout, DialogService, $i18n, SecurityPermissionService) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            vm.editTitle = $sce.trustAsHtml(parsed.html($translate.instant("EDIT")).html());
            vm.selectTitle = $sce.trustAsHtml(parsed.html($translate.instant("SELECT")).html());
            vm.enterValueTitle = $sce.trustAsHtml(parsed.html($translate.instant("ENTER_VALUE_TITLE")).html());
            vm.enterNameTitle = $sce.trustAsHtml(parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html());
            vm.enterObjectTypeeTitle = $sce.trustAsHtml(parsed.html($translate.instant("OBJECT_TYPE_VALIDATION")).html());
            vm.enterPrivilegeTitle = $sce.trustAsHtml(parsed.html($translate.instant("PRIVILEGE_VALIDATION")).html());
            var saveButton = $sce.trustAsHtml(parsed.html($translate.instant("SAVE")).html());
            var createButton = $sce.trustAsHtml(parsed.html($translate.instant("CREATE")).html());
            var updateButton = $sce.trustAsHtml(parsed.html($translate.instant("UPDATE")).html());
            $rootScope.newSecurityPermission = newSecurityPermission;
            vm.editSecurityPermission = editSecurityPermission;

            vm.loading = false;
            $scope.freeTextQuery = null;

            vm.pageable = {
                page: 0,
                size: 30,
                sort: {
                    field: "id",
                    order: "DESC"
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
                id: null,
                name: null,
                description: null,
                objectType: null,
                subType: null,
                privilege: null,
                attribute: null,
                criteria: null,
                module: 'ALL',
                searchQuery: null
            };
            $rootScope.moduleSelected = false;
            $rootScope.module = 'ALL';
            vm.securityPermissions = angular.copy(pagedResults);

            vm.freeTextSearch = freeTextSearch;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.pageSize = pageSize;
            vm.deleteSecurityPermission = deleteSecurityPermission;
            vm.viewCriteria = viewCriteria;
            $rootScope.loadPermissions = loadPermissions;

            $rootScope.loadModulePermissions = loadModulePermissions;
            function loadModulePermissions(type) {
                $rootScope.moduleSelected = true;
                $rootScope.module = type;
                loadPermissions();
            }

            function loadPermissions() {
                vm.filters.module = $rootScope.module;
                $rootScope.showBusyIndicator();
                SecurityPermissionService.getAllSecurityPermissions(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.securityPermissions = data;
                        angular.forEach(vm.securityPermissions.content, function (item) {
                            item.editMode = false;
                            item.isNew = false;
                        });
                        vm.loading = false;
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
                if (vm.securityPermissions.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    loadPermissions();
                }
            }

            function previousPage() {
                if (vm.securityPermissions.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    loadPermissions();
                }
            }

            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadPermissions();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadPermissions();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.securityPermissions = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadPermissions();
            }

            var DeletePermissionTitle = parsed.html($translate.instant("DELETE_PERMISSION_TITLE_MSG")).html();
            var deletePermissionMessage = parsed.html($translate.instant("DELETE_PERMISSION_MESSAGE")).html();
            var PermissionDeleteSuccessMsg = parsed.html($translate.instant("PERMISSION_DELETE_SUCCESS_MSG")).html();
            var editPermissionTitle = parsed.html($translate.instant("EDIT_PERMISSION")).html();
            var newPermissionTitle = parsed.html($translate.instant("NEW_PERMISSION")).html();
            var permissionCriteriaTitle = parsed.html($translate.instant("PERMISSION_CRITERIA")).html();
            var quotation = '"';

            function deleteSecurityPermission(item) {
                var options = {
                    title: DeletePermissionTitle,
                    message: quotation + item.name + quotation + deletePermissionMessage,
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        SecurityPermissionService.deletePermission(item.id).then(
                            function (data) {
                                $rootScope.showBusyIndicator();
                                var index = vm.securityPermissions.content.indexOf(item);
                                vm.securityPermissions.content.splice(index, 1);
                                vm.securityPermissions.totalElements--;
                                $rootScope.hideBusyIndicator();
                                $rootScope.showSuccessMessage(PermissionDeleteSuccessMsg);
                            }, function (error) {
                                $rootScope.showErrorMessage(error);
                                $rootScope.hideBusyIndicator();
                            }
                        );
                    }
                });
            }

            function viewCriteria(permission) {
                var options = {
                    title: permissionCriteriaTitle,
                    template: 'app/desktop/modules/admin/permission/criteria/newCriteriaView.jsp',
                    controller: 'NewCriteriaController as newCriteriaVm',
                    resolve: 'app/desktop/modules/admin/permission/criteria/newCriteriaController',
                    width: 1000,
                    side: "left",
                    showMask: true,
                    data: {
                        mode: "view",
                        permission: permission,
                        className: $rootScope.typeNames[permission.objectType]
                    },
                    callback: function () {
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function newSecurityPermission() {
                var options = {
                    title: newPermissionTitle,
                    template: 'app/desktop/modules/admin/permission/new/newPermissionView.jsp',
                    controller: 'NewPermissionController as newPermissionsVm',
                    resolve: 'app/desktop/modules/admin/permission/new/newPermissionController',
                    width: 1000,
                    showMask: true,
                    data: {
                        mode: "add"
                    },
                    buttons: [
                        {text: createButton, broadcast: 'app.securityPermission.new'}
                    ],
                    callback: function () {
                        $timeout(function () {
                            $rootScope.hideSidePanel();
                            loadPermissions();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function editSecurityPermission(permission) {
                var editPermission = angular.copy(permission);
                var options = {
                    title: editPermissionTitle,
                    template: 'app/desktop/modules/admin/permission/new/newPermissionView.jsp',
                    controller: 'NewPermissionController as newPermissionsVm',
                    resolve: 'app/desktop/modules/admin/permission/new/newPermissionController',
                    width: 1000,
                    showMask: true,
                    data: {
                        mode: "edit",
                        permission: editPermission
                    },
                    buttons: [
                        {text: updateButton, broadcast: 'app.securityPermission.update'}
                    ],
                    callback: function () {
                        $timeout(function () {
                            $rootScope.hideSidePanel();
                            loadPermissions();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            (function () {
                loadPermissions();
                $scope.$on('app.permissions.search', function (event, data) {
                    freeTextSearch(data.searchText)
                });
            })();
        }
    }
);