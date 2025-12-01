define(['app/desktop/modules/mes/mes.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/reqDocumentService'
    ],
    function (module) {
        module.controller('ReqDocumentSelectionController', ReqDocumentSelectionController);

        function ReqDocumentSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore, $translate,
                                                $uibModal, ReqDocumentService) {

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
                project: '',
                searchQuery: null
            };
            vm.selectedObj = null;
            vm.reqDocuments = angular.copy(pagedResults);
            var parsed = angular.element("<div></div>");
            var pleaseSelectOnePart = parsed.html($translate.instant("PLEASE_SELECT_ONE_REQ_DOC")).html();
            $scope.clearTitleSearch = parsed.html($translate.instant("CLEAR_SEARCH")).html();
            vm.selectAttributeDef = $scope.data.selectAttDef;

            function nextPage() {
                vm.pageable.page++;
                loadReqDocuments();
            }

            function previousPage() {
                vm.pageable.page--;
                loadReqDocuments();
            }

            vm.searchTerm = null;
            function freeTextSearch() {
                if (vm.searchTerm != null && vm.searchTerm != undefined && vm.searchTerm.trim() != "") {
                    $scope.freeTextQuery = vm.searchTerm;
                    vm.filters.searchQuery = vm.searchTerm;
                    loadReqDocuments();
                } else {
                    resetPage();
                    loadReqDocuments();
                }
            }

            function clearFilter() {
                loadReqDocuments();
                vm.clear = false;
            }

            function resetPage() {
                vm.reqDocuments = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.searchTerm = null;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                loadReqDocuments();
            }

            function loadReqDocuments() {
                vm.reqDocuments = [];
                vm.loading = true;
                if (vm.selectAttributeDef.refSubType != null) {
                    vm.filters.type = vm.selectAttributeDef.refSubType;
                }
                ReqDocumentService.getAllReqDocuments(vm.pageable, vm.filters).then(
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
                vm.reqDocuments = data;
                var existObjectId = false;
                angular.forEach(vm.reqDocuments.content, function (part) {
                    if (objectId != null && objectId != "" && objectId != undefined && objectId == part.id) {
                        vm.reqDocuments.content.splice(vm.reqDocuments.content.indexOf(part), 1);
                        existObjectId = true;
                    }
                    part.checked = false;
                });
                if (existObjectId) {
                    vm.reqDocuments.totalElements = vm.reqDocuments.totalElements - 1;
                    vm.reqDocuments.numberOfElements = vm.reqDocuments.numberOfElements - 1;
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
                $rootScope.$on('app.select.reqDocument', selectRadio);
                loadReqDocuments();
                //}
            })();
        }
    }
)
;