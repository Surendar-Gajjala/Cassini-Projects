define(
    [
        'app/desktop/modules/item/item.module',
        'app/desktop/directives/person-avatar/personAvatarDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/licenseService'

    ],
    function (module) {
        module.controller('AllUsersController', AllUsersController);

        function AllUsersController($scope, $rootScope, $injector, $sce, $translate, $cookieStore, $window, $timeout, $application,
                                    LicenseService, $state, $stateParams, $cookies, LoginService, PersonGroupService) {

            var vm = this;

            var parsed = angular.element("<div></div>");

            vm.pageable = {
                page: 0,
                size: 30,
                sort: {
                    field: "modifiedDate",
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
                searchQuery: null
            };
            vm.logins = angular.copy(pagedResults);

            vm.freeTextSearch = freeTextSearch;
            vm.showUserDetails = showUserDetails;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            function showUserDetails(login) {
                $state.go('app.userdetails.activity', {userId: login.person.id})
            }

            vm.noResults = false;

            function loadUsers() {
                $rootScope.showBusyIndicator();
                LoginService.getFilteredLogins(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.logins = data;
                        PersonGroupService.getLoginPersonGroupReferences(vm.logins.content, 'defaultGroup');
                        if (vm.searchMode == true && vm.logins.content.length == 0) {
                            vm.noResults = true;
                        }
                        $rootScope.hideBusyIndicator();
                        resizeView();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function resizeView() {
                var contentpanel = $('.contentpanel').outerHeight();
                $('.view-content').height(contentpanel - 50);
            }

            function nextPage() {
                if (vm.logins.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadUsers();
                }
            }

            function previousPage() {
                if (vm.logins.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadUsers();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadUsers();
            }

            vm.searchMode = false;

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.searchMode = true;
                    vm.filters.searchQuery = freeText;
                    loadUsers();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.logins = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                vm.searchMode = false;
                vm.noResults = false;
                $rootScope.showBusyIndicator($('.view-container'));
                loadUsers();
            }


            $rootScope.newUser = newUser;
            function newUser() {
                var options = {
                    title: "New User",
                    template: 'app/desktop/modules/admin/user/new/newUserView.jsp',
                    controller: 'NewUserController as newUserVm',
                    resolve: 'app/desktop/modules/admin/user/new/newUserController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: "Create", broadcast: 'app.users.new'}
                    ],
                    callback: function () {
                        loadUsers();
                        loadUsersCount();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadUsersCount() {
                LoginService.getUsersCount().then(
                    function (data) {
                        vm.userCounts = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function loadNoOfLicences() {
                LicenseService.getLicense().then(function (data) {
                    vm.licenses = data.licenses
                })
            }

            (function () {
                loadUsers();
                loadUsersCount();
                loadNoOfLicences();
                $scope.$on('app.users.search', function (event, data) {
                    freeTextSearch(data.searchText)
                });
            })();
        }
    }
);