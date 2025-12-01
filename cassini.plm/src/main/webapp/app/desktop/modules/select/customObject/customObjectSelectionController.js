define([
        'app/desktop/modules/customObject/customObject.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('CustomObjectSelectionController', CustomObjectSelectionController);

        function CustomObjectSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore, $translate,
                                                 $uibModal, CustomObjectService) {

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
                number: null,
                name: null,
                type: '',
                customType: '',
                description: null,
                searchQuery: null,
                object: '',
                bomObject: '',
                related: false
            };
            vm.selectedObj = null;
            vm.customObjects = angular.copy(pagedResults);
            var parsed = angular.element("<div></div>");
            var pleaseSelectOnePart = parsed.html($translate.instant("PLEASE_SELECT_ATLEAST_ONE_OBJECT")).html();
            $scope.clearTitleSearch = parsed.html($translate.instant("CLEAR_SEARCH")).html();
            vm.selectAttributeDef = $scope.data.selectAttDef;

            function nextPage() {
                vm.pageable.page++;
                loadCustomObjects();
            }

            function previousPage() {
                vm.pageable.page--;
                loadCustomObjects();
            }

            vm.searchTerm = null;
            function freeTextSearch() {
                if (vm.searchTerm != null && vm.searchTerm != undefined && vm.searchTerm.trim() != "") {
                    $scope.freeTextQuery = vm.searchTerm;
                    vm.filters.searchQuery = vm.searchTerm;
                    loadCustomObjects();
                } else {
                    resetPage();
                    loadCustomObjects();
                }
            }

            function clearFilter() {
                loadCustomObjects();
                vm.clear = false;
            }

            function resetPage() {
                vm.customObjects = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.searchTerm = null;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                loadCustomObjects();
            }

            function loadCustomObjects() {
                vm.customObjects = [];
                vm.loading = true;
                if (vm.selectAttributeDef.refSubType != null) {
                    vm.filters.type = vm.selectAttributeDef.refSubType;
                }
                CustomObjectService.getCustomObjects(vm.pageable, vm.filters).then(
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
                vm.customObjects = data;
                var existObjectId = false;
                angular.forEach(vm.customObjects.content, function (part) {
                    if (objectId != null && objectId != "" && objectId != undefined && objectId == part.id) {
                        vm.customObjects.content.splice(vm.customObjects.content.indexOf(part), 1);
                        existObjectId = true;
                    }
                    part.checked = false;
                });
                if (existObjectId) {
                    vm.customObjects.totalElements = vm.customObjects.totalElements - 1;
                    vm.customObjects.numberOfElements = vm.customObjects.numberOfElements - 1;
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
                    $rootScope.showWarningMessage(pleaseSelectOnePart);
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
                $rootScope.$on('app.selector.customObject', selectRadio);
                loadCustomObjects();
                //}
            })();
        }
    }
)
;