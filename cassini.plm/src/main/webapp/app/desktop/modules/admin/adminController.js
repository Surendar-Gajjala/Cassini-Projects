define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/profileService',
        'app/desktop/modules/securityPermission/securityPermissionService'
    ],
    function (module) {
        module.controller('AdminController', AdminController);

        function AdminController($scope, $rootScope, $injector, $sce, $translate, $cookieStore, $window, $timeout, $application,
                                 LoginService, $state, $http, SecurityPermissionService, $cookies) {

            var vm = this;

            $rootScope.viewInfo.showDetails = false;

            var parsed = angular.element("<div></div>");
            var entityPropertiesPath = '../entityProperties.json';
            var typeClassNamePath = 'app/assets/json/typeClassName.json';
            var subTypePropertiesPath = 'app/assets/json/subTypeProperties.json';

            vm.view = 'users';
            vm.module = 'ALL';
            vm.viewType = "cards";

            vm.showUsers = showUsers;
            function showUsers() {
                vm.view = 'users';
                var adminUserView = $window.localStorage.getItem("admin-user-view");
                if (adminUserView != null && adminUserView != "" && adminUserView != undefined) {
                    vm.viewType = adminUserView;
                } else {
                    vm.viewType = "cards";
                }
                $state.go('app.newadmin.users')
            }

            vm.showRoles = showRoles;
            function showRoles() {
                vm.view = 'roles';
                var adminRoleView = $window.localStorage.getItem("admin-role-view");
                if (adminRoleView != null && adminRoleView != "" && adminRoleView != undefined) {
                    vm.viewType = adminRoleView;
                } else {
                    vm.viewType = "cards";
                }
                $state.go('app.newadmin.roles')
            }

            vm.showPermissions = showPermissions;
            function showPermissions() {
                vm.view = 'permissions';
                $state.go('app.newadmin.permissions')
            }

            vm.searchText = null;
            vm.freeTextSearch = freeTextSearch;
            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    if (vm.view === 'users') {
                        $scope.$broadcast('app.users.search', {searchText: freeText});
                    } else if (vm.view === 'roles') {
                        $scope.$broadcast('app.roles.search', {searchText: freeText});
                    } else if (vm.view == 'permissions') {
                        $scope.$broadcast('app.permissions.search', {searchText: freeText});
                    }
                } else {
                    if (vm.view === 'users') {
                        $scope.$broadcast('app.users.search', {searchText: null});
                    } else if (vm.view === 'roles') {
                        $scope.$broadcast('app.roles.search', {searchText: null});
                    } else if (vm.view == 'permissions') {
                        $scope.$broadcast('app.permissions.search', {searchText: null});
                    }
                }
            }

            vm.resetPage = resetPage;
            function resetPage() {
                vm.searchText = null;
                if (vm.view === 'users') {
                    $scope.$broadcast('app.users.search', {searchText: null});
                } else if (vm.view === 'roles') {
                    $scope.$broadcast('app.roles.search', {searchText: null});
                } else if (vm.view == 'permissions') {
                    $scope.$broadcast('app.permissions.search', {searchText: null});
                }
            }

            vm.loadProperties = loadProperties;
            function loadProperties() {
                $rootScope.entityProerties = new Map();
                $rootScope.enums = new Map();
                $http.get(entityPropertiesPath).success(function (data) {
                    angular.forEach(data.classes, function (value) {
                        $rootScope.entityProerties.set(value.className, value.properties);
                    });
                    angular.forEach(data.enums, function (value) {
                        $rootScope.enums.set(value.objectType, value.values);
                    });
                });
                loadClassNames();
            }

            function loadPersons() {
                $rootScope.permissionPersons = [];
                LoginService.getAllLogins().then(
                    function (data) {
                        $rootScope.permissionPersons.push({id: '$loginuser', name: '$loginuser'});
                        angular.forEach(data, function (value) {
                            $rootScope.permissionPersons.push({id: value.person.id, name: value.loginName});
                        });
                    }
                );
            }

            $rootScope.loadObjectTypeAndModuleTypes = loadObjectTypeAndModuleTypes;
            function loadObjectTypeAndModuleTypes() {
                SecurityPermissionService.getObjectTypes().then(
                    function (data) {
                        $rootScope.objectTypes = data.objectTypes;
                        $rootScope.privileges = data.privileges;
                        $rootScope.moduleTypes = data.moduleTypes;
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error);
                    }
                )
            }

            function loadClassNames() {
                $http.get(typeClassNamePath).success(function (data) {
                    $rootScope.typeNames = data;
                });
                loadSubTypeProperites();
            }

            function loadSubTypeProperites() {
                $http.get(subTypePropertiesPath).success(function (data) {
                    $rootScope.subTypeNames = data;
                });
            }

            $rootScope.loadSubTypes = loadSubTypes;
            function loadSubTypes(objectType) {
                vm.typeValue = null;
                var typeValue = objectType.toUpperCase();
                vm.typeValue = $rootScope.subTypeNames[typeValue];
                return vm.typeValue;
            }

            vm.clearModule = clearModule;
            function clearModule() {
                $rootScope.moduleSelected = false;
                $rootScope.module = 'ALL';
                vm.module = 'ALL';
                $rootScope.loadPermissions();
            }

            vm.setViewType = setViewType;
            function setViewType(type) {
                if (vm.view == "roles") {
                    $window.localStorage.setItem('admin-role-view', type);
                } else {
                    $window.localStorage.setItem('admin-user-view', type);
                }
            }

            (function () {
                loadProperties();
                loadPersons();
                loadObjectTypeAndModuleTypes();
                var url = $state.current.url;

                if (url === '/users') {
                    vm.view = 'users';
                }
                else if (url === '/roles') {
                    vm.view = 'roles';
                }
                else if (url === '/permissions') {
                    vm.view = 'permissions';
                }
                var adminRoleView = $window.localStorage.getItem("admin-role-view");
                var adminUserView = $window.localStorage.getItem("admin-user-view");
                if (vm.view == "users" && adminUserView != null && adminUserView != "" && adminUserView != undefined) {
                    vm.viewType = adminUserView;
                } else if (vm.view == "roles" && adminUserView != null && adminUserView != "" && adminUserView != undefined) {
                    vm.viewType = adminRoleView;
                }
            })();
        }
    }
);