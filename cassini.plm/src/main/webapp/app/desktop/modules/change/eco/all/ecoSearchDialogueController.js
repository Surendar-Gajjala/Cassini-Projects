define(['app/desktop/modules/change/change.module'
    ],
    function (module) {
        module.controller('ECOSearchDialogueController', ECOSearchDialogueController);

        function ECOSearchDialogueController($scope, $rootScope, $timeout, $translate, $state,LoginService, $stateParams, $cookies, CommonService) {


            var vm = this;
            var parsed = angular.element("<div></div>");

            vm.selectTitle = parsed.html($translate.instant("SELECT")).html();
            vm.selectTitle1 = parsed.html($translate.instant("SELECT")).html();
            vm.descriptionTitle = parsed.html($translate.instant("DESCRIPTION")).html();
            vm.numberTitle = parsed.html($translate.instant("NUMBER")).html();
            vm.title = parsed.html($translate.instant("TITLE")).html();
            var searchValue = parsed.html($translate.instant("SEARCH_VALUE")).html();

            vm.search = search;
            vm.ecoStatuses = [];
            vm.filters = {
                ecoNumber: null,
                title: null,
                description: null,
                status: null,
                statusType: null,
                //ecoOwnerObject: $application.login.person
                ecoOwnerObject: {
                    id: null
                }
            };

            vm.Persons = [];

            function validate() {
                var valid = true;
                $rootScope.closeNotification();
                if ((vm.filters.ecoNumber != "" && vm.filters.ecoNumber != null && vm.filters.ecoNumber != undefined) ||
                    (vm.filters.statusType != null) ||
                    (vm.filters.ecoOwnerObject.id != null) ||
                    (vm.filters.title != "" && vm.filters.title != null && vm.filters.title != undefined) ||
                    (vm.filters.description != "" && vm.filters.description != null && vm.filters.description != undefined) ||
                    (vm.filters.status != "" && vm.filters.status != null && vm.filters.status != undefined)) {
                    valid = true;
                } else {
                    $rootScope.showInfoMessage(searchValue);
                    valid = false;
                }

                return valid;
            }

            function search() {
                if (validate()) {
                    $scope.callback(vm.filters);
                    $rootScope.hideSidePanel();
                }
            }

            function loadPersons() {
                vm.persons = [];
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && login.external == false) {
                                vm.persons.push(login.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    $rootScope.$on('app.change.Search', search);
                    loadPersons();

                //}
            })();
        }
    }
);
