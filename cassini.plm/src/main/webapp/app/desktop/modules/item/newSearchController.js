define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/searchService'
    ],
    function (module) {

        module.controller('NewSearchController', NewSearchController);

        function NewSearchController($scope, $q, $rootScope, $timeout, $translate, $state, $stateParams, $cookies, SearchService) {

            var vm = this;
            $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
            vm.newSearch = {
                name: null,
                description: null,
                searchType: null,
                query: null,
                objectType: 'ITEM',
                type: 'PRIVATE',
                owner: $rootScope.localStorageLogin.login.person.id
            };

            var parsed = angular.element("<div></div>");

            var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var descriptionValidation = parsed.html($translate.instant("ENTER_DESCRIPTION")).html();
            var savedMessage = parsed.html($translate.instant("SEARCH_SAVED_MESSAGE")).html();

            vm.newSearch = angular.copy($scope.data.search);

            vm.createSearch = createSearch;

            function createSearch() {
                if (validate()) {
                    create().then(
                        function () {
                            $rootScope.hideSidePanel();
                            $scope.callback();
                        }
                    );
                }
            }

            function validate() {
                var valid = true;
                if (vm.newSearch.name == null || vm.newSearch.name == "" || vm.newSearch.name == undefined) {
                    valid = false;
                    $rootScope.showErrorMessage(nameValidation);
                }

                return valid;
            }

            function create() {
                var defered = $q.defer();
                $rootScope.closeNotification();
                SearchService.createSearch(vm.newSearch).then(
                    function (data) {
                        $rootScope.showSuccessMessage(savedMessage);
                        defered.resolve();
                    }, function (error) {
                        $rootScope.showWarningMessage(error.message);
                        defered.reject();
                    }
                )

                return defered.promise;
            }

            vm.selectType = selectType;

            function selectType(value, event) {
                if (value == "Public") {
                    vm.newSearch.type = 'PUBLIC';
                }
                if (value == "Private") {
                    vm.newSearch.type = 'PRIVATE';
                }
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    $rootScope.$on('app.search.new', createSearch);
                //}
            })();
        }
    }
)
;