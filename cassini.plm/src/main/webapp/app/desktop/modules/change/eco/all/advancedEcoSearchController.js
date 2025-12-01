/**
 * Created by Anusha on 28-06-2016.
 */
define(['app/desktop/modules/change/change.module'
    ],
    function (module) {
        module.controller('AdvancedEcoSearchController', AdvancedEcoSearchController);

        function AdvancedEcoSearchController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate) {

            var vm = this;

            vm.onAddeco = onAddeco;
            vm.deleteeco = deleteeco;
            vm.search = search;
            vm.show = false;
            vm.ecos = [];

            var parsed = angular.element("<div></div>");

            var containsTitle = parsed.html($translate.instant("CONTAINS_TITLE")).html();
            var startsWithTitle = parsed.html($translate.instant("STARTS_WITH_TITLE")).html();
            var endsWithTitle = parsed.html($translate.instant("ENDS_WITH_TITLE")).html();
            var equalsTitle = parsed.html($translate.instant("EQUALS_TITLE")).html();
            var lessthanTitle = parsed.html($translate.instant("LESS_THAN_TITLE")).html();
            var greaterthanTitle = parsed.html($translate.instant("GREATER_THAN_TITLE")).html();
            var lessthanorequalsTitle = parsed.html($translate.instant("LESSTHAN_OR_EQUALS_TITLE")).html();
            var greaterthanorequalsTitle = parsed.html($translate.instant("GREATERTHAN_OR_EQUALS_TITLE")).html();
            var ecoNumberTitle = parsed.html($translate.instant("ECO_NUMBER_TITLE")).html();
            var ecoDescriptionTitle = parsed.html($translate.instant("ECO_DESCRIPTION_TITLE")).html();
            var ecoStatusTitle = parsed.html($translate.instant("ECO_STATUS_TITLE")).html();
            var ecoTitle = parsed.html($translate.instant("ECO_TITLE")).html();
            var ecoFileNameTitle = parsed.html($translate.instant("ECO_FILE_NAME_TITLE")).html();
            var ecoFileVersionTitle = parsed.html($translate.instant("ECO_FILE_VERSION_TITLE")).html();
            vm.selectTitle = parsed.html($translate.instant("SELECT")).html();
            vm.enterValueTitle = parsed.html($translate.instant("ENTER_VALUE_TITLE")).html();
            var advancedSearchMessage = parsed.html($translate.instant("ADVANCED_SEARCH_MESSAGE")).html();

            vm.dataTypeOperators = {
                string: [
                    {name: 'contains', label: containsTitle},
                    {name: 'startswith', label: startsWithTitle},
                    {name: 'endswith', label: endsWithTitle},
                    {name: 'equals', label: equalsTitle}
                ],
                integer: [
                    {name: 'equals', label: equalsTitle},
                    {name: 'lessthan', label: lessthanTitle},
                    {name: 'greaterthan', label: greaterthanTitle},
                    {name: 'lessthanorequals', label: lessthanorequalsTitle},
                    {name: 'greaterthanorequals', label: greaterthanorequalsTitle}
                ]
            }

            vm.ecoNames = [
                {field: 'eco.number', label: ecoNumberTitle, operator: vm.dataTypeOperators.string},
                {field: 'eco.description', label: ecoStatusTitle, operator: vm.dataTypeOperators.string},
                {field: 'eco.status', label: ecoDescriptionTitle, operator: vm.dataTypeOperators.string},
                {field: 'eco.title', label: ecoTitle, operator: vm.dataTypeOperators.string},
                //{field: 'eco.owner', operator: vm.dataTypeOperators.string},
                {field: 'ecoFile.name', label: ecoFileNameTitle, operator: vm.dataTypeOperators.string},
                {field: 'ecoFile.version', label: ecoFileVersionTitle, operator: vm.dataTypeOperators.integer}].sort();

            var searchEcos = {
                ecoType: null,
                operator: null,
                value: null
            };

            vm.selectedType = {
                name: null
            };
            vm.filters = [];

            function onAddeco() {
                var newRow = angular.copy(searchEcos);
                newRow.parent = vm.row;
                vm.ecos.push(newRow);
            }

            function onAddEcos() {
                angular.forEach($scope.data.ecoFilters, function (filter) {
                    vm.ecos.push(filter);
                })
            }

            function search() {
                if (validate()) {
                    $scope.callback(vm.ecos);
                    $rootScope.hideSidePanel();
                }

            }

            function validate() {
                var valid = true;
                var valid1 = true;
                var count = vm.ecos.length;
                if (vm.ecos.length >= 1) {
                    angular.forEach(vm.ecos, function (item) {
                        if (item.field == undefined) {
                            valid1 = false;
                        }
                        else if (item.operator == null || item.operator == "" || item.operator == undefined) {
                            valid1 = false;
                        }
                        else if (item.value == null || item.value == "" || item.value == undefined) {
                            valid1 = false;
                        }
                        if (valid1 == false && vm.ecos.length > 1) {
                            var index = vm.ecos.indexOf(item);
                            vm.ecos.splice(index, 1);
                        }
                    })
                }

                if (vm.ecos.length >= 1 && valid1 == false && vm.ecos.length >= count) {
                    valid = false;
                    $rootScope.showInfoMessage(advancedSearchMessage);
                } else {
                    $rootScope.closeNotification();
                }

                return valid;
            }

            function deleteeco(eco) {
                var index = vm.ecos.indexOf(eco);
                vm.ecos.splice(index, 1);
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    if ($scope.data.ecoFilters != null) {
                        onAddEcos();
                    } else {
                        onAddeco();
                    }
                    $rootScope.$on('app.change.advance', search);
                //}
            })();
        }
    }
);

