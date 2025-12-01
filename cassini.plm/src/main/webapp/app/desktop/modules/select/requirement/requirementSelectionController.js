define(['app/desktop/modules/req/req.module',
        'app/shared/services/core/requirementService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('RequirementSelectionController', RequirementSelectionController);

        function RequirementSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore, $translate,
                                                $uibModal, RequirementService) {

            var vm = this;

            vm.loading = true;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.selectRadio = selectRadio;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
            $scope.freeTextQuery = null;

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
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

            vm.selectedObj = null;
            vm.requirements = angular.copy(pagedResults);
            var parsed = angular.element("<div></div>");
            var pleaseSelectAtleastOneItem = parsed.html($translate.instant("PLEASE_SELECT_ONE_REQ")).html();
            vm.selectAttributeDef = $scope.data.selectAttDef;
            var objectId = $scope.data.existObjectId;

            function nextPage() {
                vm.pageable.page++;
                loadRequirements();
            }

            function previousPage() {
                vm.pageable.page--;
                loadRequirements();
            }

            vm.filters = {
                type: '',
                searchQuery: null
            };

            vm.searchTerm = null;
            function freeTextSearch() {
                if (vm.searchTerm != null && vm.searchTerm != undefined && vm.searchTerm.trim() != "") {
                    $scope.freeTextQuery = vm.searchTerm;
                    vm.filters.searchQuery = vm.searchTerm;
                    loadRequirements();
                } else {
                    resetPage();
                    loadRequirements();
                }
            }

            vm.searchTerm = null;
            function clearFilter() {
                loadRequirements();
                vm.clear = false;
            }

            function resetPage() {
                vm.requirements = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.searchTerm = null;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                loadRequirements();
            }

            function loadRequirements() {
                vm.requirements = [];
                vm.loading = true;
                if (vm.selectAttributeDef.refSubType != null) {
                    vm.filters.type = vm.selectAttributeDef.refSubType;
                }
                RequirementService.requirementFreeTextSearch(vm.filters, vm.pageable).then(
                    function (data) {
                        loadSelectedObjects(data);
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )

            }

            function loadSelectedObjects(data) {
                vm.requirements = data;
                var existObjectId = false;
                angular.forEach(vm.requirements.content, function (part) {
                    if (objectId != null && objectId != "" && objectId != undefined && objectId == part.id) {
                        vm.requirements.content.splice(vm.requirements.content.indexOf(part), 1);
                        existObjectId = true;
                    }
                    part.checked = false;
                });
                if (existObjectId) {
                    vm.requirements.totalElements = vm.requirements.totalElements - 1;
                    vm.requirements.numberOfElements = vm.requirements.numberOfElements - 1;
                }
            }


            function selectRadioChange(requirement) {
                radioChange(requirement);
                selectRadio();
            }

            function radioChange(requirement) {
                if (vm.selectedObj === requirement) {
                    requirement.checked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = requirement;
                }
            }

            function selectRadio() {
                if (vm.selectedObj != null) {
                    $rootScope.hideSidePanel('left');
                    $scope.callback(vm.selectedObj);
                } else if (vm.selectedObj == null) {
                    $rootScope.showWarningMessage(pleaseSelectAtleastOneItem);
                }
            }

            module.directive('autoFocus', ['$timeout', function ($timeout) {
                return {
                    restrict: 'A',
                    link: function ($scope, $element) {
                        $timeout(function () {
                            $element[0].focus();
                        });
                    }
                }
            }]);

            (function () {
                //if ($application.homeLoaded == true) {
                $rootScope.$on('app.select.req', selectRadio);
                loadRequirements();
                //}
            })();
        }
    }
)
;

