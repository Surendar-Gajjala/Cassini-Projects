define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/core/searchService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('SavedSearchesWidgetController', SavedSearchesWidgetController);

        function SavedSearchesWidgetController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                       SearchService, CommonService) {
            var vm = this;

            vm.savedSearches = [];
            vm.loading = true;
            vm.pageable = {
                page: 0,
                size: 25,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            var parsed = angular.element("<div></div>");
            vm.public = parsed.html($translate.instant("PUBLIC")).html();
            vm.private = parsed.html($translate.instant("PRIVATE")).html();

            var owner = null;

            function loadSavedSearches() {
                vm.clear = false;
                vm.loading = true;
                SearchService.getSavedSearch(owner, vm.pageable).then(
                    function (data) {
                        vm.savedSearches = data.content;
                        vm.loading = false;
                        CommonService.getPersonReferences(vm.savedSearches.content, "owner");
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $rootScope.searchQuery = null;
            $rootScope.searchType = null;
            vm.showSavedSearchResults = showSavedSearchResults;
            function showSavedSearchResults(savedSearch) {
                if (savedSearch.objectType == 'ITEM') {
                    $rootScope.searchQuery = savedSearch.query;
                    $rootScope.searchType = savedSearch.searchType;
                    $rootScope.allItemsLoad = false;
                    $state.go("app.items.all");
                } else if (savedSearch.objectType == 'CHANGE') {
                    $rootScope.searchQuery = savedSearch.query;
                    $rootScope.searchType = savedSearch.searchType;
                    $rootScope.allEcosLoad = false;
                    $state.go("app.changes.eco.all");
                } else if (savedSearch.objectType == 'MANUFACTURER') {
                    $rootScope.searchQuery = savedSearch.query;
                    $rootScope.searchType = savedSearch.searchType;
                    $state.go("app.mfr.all");
                } else if (savedSearch.objectType == 'MANUFACTURERPART') {
                    $rootScope.searchQuery = savedSearch.query;
                    $rootScope.searchType = savedSearch.searchType;
                    $state.go("app.mfr.mfrparts.all");
                }
            }

            (function () {
                $timeout(function () {
                    $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
                    if ($rootScope.localStorageLogin != null) {
                        owner = $rootScope.localStorageLogin.login.person.id;
                    }
                    loadSavedSearches();
                }, 1000);
            })();
        }
    }
);