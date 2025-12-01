define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/searchService'
    ],
    function (module) {
        module.controller('SavedSearchesController', SavedSearchesController);

        function SavedSearchesController($scope, $rootScope, $timeout, $state, $stateParams, $translate, CommonService, $cookies, DialogService, SearchService) {

            var vm = this;
            vm.loading = true;
            vm.savedSearches = null;
            vm.showSearch = showSearch;
            vm.deleteSavedSearch = deleteSavedSearch;
            var type = $scope.data.type;
            var parsed = angular.element("<div></div>");

            $scope.deleteSavedSearchs = parsed.html($translate.instant("DELETE_SAVED_SEARCH")).html();
            $scope.SavedSearchtitle = parsed.html($translate.instant("SAVED_SEARCH_TITLE")).html();
            $scope.SavedSearchmessage = parsed.html($translate.instant("SAVED_SEARCH_MESSAGE")).html();
            $scope.SavedSearchDelete = parsed.html($translate.instant("SAVED_SEARCH_DELETE")).html();
            $scope.showSavedSearchResultsTitle = parsed.html($translate.instant("CLICK_TO_SAVED_RESULTS")).html();
            $scope.publicSearch = parsed.html($translate.instant("SAVED_SEARCH_PUBLIC")).html();
            $scope.privateSearch = parsed.html($translate.instant("SAVED_SEARCH_PRIVATE")).html();

            function showSearch(search) {
                $scope.callback(search);
                $rootScope.hideSidePanel();
            }

            function loadSearches(type) {
                SearchService.getSavedSearches(type).then(
                    function (data) {
                        vm.savedSearches = data;
                        vm.loading = false;
                        CommonService.getPersonReferences(vm.savedSearches, 'owner');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function deleteSavedSearch(search) {
                var options = {
                    title: $scope.deleteSavedSearchs,
                    message: $scope.SavedSearchmessage + " [" + search.name + "] ?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        SearchService.deleteSavedSearch(search.id).then(
                            function (data) {
                                $rootScope.showSearch = false;
                                $rootScope.searchModeType = false;
                                if ($rootScope.savedSearchesCount > 0) {
                                    $rootScope.savedSearchesCount--;
                                }
                                loadSearches(type);
                                $rootScope.showSuccessMessage($scope.SavedSearchDelete);
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                });
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadSearches(type);
                //}
            })();
        }
    }
);