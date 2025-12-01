define(['app/desktop/modules/mro/mro.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/mesObjectTypeService'
    ],
    function (module) {
        module.controller('MROObjectSelectionController', MROObjectSelectionController);

        function MROObjectSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore, $translate,
                                              $uibModal, MESObjectTypeService) {

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
            var objectId = $scope.data.existObjectId;
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

            vm.filters = {
                type: '',
                searchQuery: null
            };
            vm.selectedObj = null;
            vm.mroObjects = angular.copy(pagedResults);
            var parsed = angular.element("<div></div>");
            var pleaseSelectOne = parsed.html($translate.instant("PLEASE_SELECT_ATLEAST_ONE")).html();
            var objectWord = parsed.html($translate.instant("OBJECT_WORD")).html();
            $scope.clearTitleSearch = parsed.html($translate.instant("CLEAR_SEARCH")).html();
            vm.selectAttributeDef = $scope.data.selectAttDef;

            function nextPage() {
                vm.pageable.page++;
                loadMROObjects();
            }

            function previousPage() {
                vm.pageable.page--;
                loadMROObjects();
            }

            vm.searchTerm = null;
            function freeTextSearch() {
                if (vm.searchTerm != null && vm.searchTerm != undefined && vm.searchTerm.trim() != "") {
                    $scope.freeTextQuery = vm.searchTerm;
                    vm.filters.searchQuery = vm.searchTerm;
                    loadMROObjects();
                } else {
                    resetPage();
                    loadMROObjects();
                }
            }

            function clearFilter() {
                loadMROObjects();
                vm.clear = false;
            }

            function resetPage() {
                vm.mroObjects = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.searchTerm = null;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                loadMROObjects();
            }

            function loadMROObjects() {
                vm.mroObjects = [];
                vm.loading = true;
                if (vm.selectAttributeDef.refSubType != null) {
                    vm.filters.type = vm.selectAttributeDef.refSubType;
                }
                MESObjectTypeService.getMROObjects(vm.pageable, vm.filters).then(
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
                vm.mroObjects = data;
                var existObjectId = false;
                angular.forEach(vm.mroObjects.content, function (part) {
                    if (objectId != null && objectId != "" && objectId != undefined && objectId == part.id) {
                        vm.mroObjects.content.splice(vm.mroObjects.content.indexOf(part), 1);
                        existObjectId = true;
                    }
                    part.checked = false;
                });
                if (existObjectId) {
                    vm.mroObjects.totalElements = vm.mroObjects.totalElements - 1;
                    vm.mroObjects.numberOfElements = vm.mroObjects.numberOfElements - 1;
                }
            }

            function selectRadioChange(mfrPart, $event) {
                radioChange(mfrPart, $event);
                selectRadio();
            }

            function radioChange(mfrPart, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === mfrPart) {
                    mfrPart.checked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = mfrPart;
                }
            }

            function selectRadio() {
                if (vm.selectedObj != null) {
                    $rootScope.hideSidePanel('left');
                    $scope.callback(vm.selectedObj);
                }

                if (vm.selectedObj == null) {
                    if (vm.selectAttributeDef.refSubTypeName != null) {
                        $rootScope.showWarningMessage(pleaseSelectOne + " " + angular.lowercase(vm.selectAttributeDef.refSubTypeName) + " " + objectWord);
                    } else {
                        $rootScope.showWarningMessage(pleaseSelectOne + " " + angular.lowercase(vm.selectAttributeDef.refType));
                    }
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
                $rootScope.$on('app.select.mroObject', selectRadio);
                loadMROObjects();
                //}
            })();
        }
    }
)
;