define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/bopService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/desktop/directives/all-view-icons/allViewIconsDirective'
    ],
    function (module) {
        module.controller('AllBOPController', AllBOPController);

        function AllBOPController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application,
                                  $stateParams, $cookies, $sce, BOPService, CommonService, ObjectTypeAttributeService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newBOP = newBOP;
            vm.bops = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;

            vm.searchText = null;
            vm.filterSearch = null;


            vm.pageable = {
                page: 0,
                size: 20,
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
                searchQuery: null,
                number: null,
                name: null,
                type: '',
                bop: ''
            };
            $scope.freeTextQuery = null;

            vm.bops = angular.copy(pagedResults);


            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newBOPHeading = parsed.html($translate.instant("New BOP")).html();

            function newBOP() {
                var options = {
                    title: newBOPHeading,
                    template: 'app/desktop/modules/mes/bop/new/newBOPView.jsp',
                    controller: 'NewBOPController as newBOPVm',
                    resolve: 'app/desktop/modules/mes/bop/new/newBOPController',
                    width: 700,
                    showMask: true,
                    data: {
                        bopCreationType: "BOP",
                        mbom: null,
                        mbomRevision: null
                    },
                    buttons: [
                        {text: create, broadcast: 'app.bop.new'}
                    ],
                    callback: function (bop) {
                        $timeout(function () {
                            loadBOPS();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.showMBOM = showMBOM;
            function showMBOM(bop) {
                $state.go('app.mes.mbom.details', {mbomId: bop.mbomRevisionId, tab: 'details.basic'});
            }

            var deleteDialogTitle = parsed.html($translate.instant("DELETE_BOP")).html();
            var deleteBopDialogMessage = parsed.html($translate.instant("DELETE_BOP_DIALOG_MESSAGE")).html();
            var bopDeletedSuccessMessage = parsed.html($translate.instant("BOP_DELETED_MESSAGE")).html();

            vm.deleteBOP = deleteBOP;
            function deleteBOP(bop) {
                var options = {
                    title: deleteDialogTitle,
                    message: deleteBopDialogMessage.format(bop.name),
                    okButtonClass: 'btn-danger'
                };


                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        BOPService.deleteBOP(bop.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(bopDeletedSuccessMessage);
                                loadBOPS();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            $scope.bopFilePopover = {
                templateUrl: 'app/desktop/modules/mes/bop/all/bopFileTemplatePopover.jsp'

            };

            function nextPage() {
                if (vm.bops.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadBOPS();
                }
            }

            function previousPage() {
                if (vm.bops.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadBOPS();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadBOPS();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadBOPS();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.bops = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadBOPS();
            }

            function loadBOPS() {
                BOPService.getAllBOPs(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.bops = data;
                        CommonService.getMultiplePersonReferences(vm.bops.content, ['createdBy', 'modifiedBy']);
                        loadBopAttributeValues();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.showBOP = showBOP;
            function showBOP(bop) {
                $state.go('app.mes.bop.details', {bopId: bop.latestRevision, tab: 'details.basic'});

            }

            var bopsAttributeTitle = parsed.html($translate.instant("ATTRIBUTES")).html();
            var selectedAttributesAdded = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();

            vm.showTypeAttributes = showTypeAttributes;
            function showTypeAttributes() {
                var options = {
                    title: bopsAttributeTitle,
                    template: 'app/desktop/modules/shared/attributes/attributesView.jsp',
                    resolve: 'app/desktop/modules/shared/attributes/attributesController',
                    controller: 'AttributesController as attributesVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: vm.selectedBopAttributes,
                        type: "BOPTYPE",
                        objectType: "MESOBJECT"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedBopAttributes = result;
                        $window.localStorage.setItem("bopAttributes", JSON.stringify(vm.selectedBopAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadBOPS();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.showBOPRevisionHistory = showBOPRevisionHistory;
            var revisionHistoryTitle = $translate.instant("REVISION_HISTORY_TITLE");

            function showBOPRevisionHistory(bop) {
                var options = {
                    title: bop.number + " - " + revisionHistoryTitle,
                    template: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryView.jsp',
                    controller: 'ItemRevisionHistoryController as revHistoryVm',
                    resolve: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryController',
                    data: {
                        itemId: bop.id,
                        revisionHistoryType: "BOP"
                    },
                    width: 700,
                    showMask: true
                };

                $rootScope.showSidePanel(options);
            }

            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.selectedBopAttributes.remove(att);
                $window.localStorage.setItem("bopAttributes", JSON.stringify(vm.selectedBopAttributes));

            }

            vm.objectIds = [];
            vm.attributeIds = [];
            vm.selectedBopAttributes = [];
            function loadBopAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.bops.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                if (vm.selectedBopAttributes != null && vm.selectedBopAttributes.length > 0) {

                    angular.forEach(vm.selectedBopAttributes, function (selectedAttribute) {
                        if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                            vm.attributeIds.push(selectedAttribute.id);
                        }
                    });
                    $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedBopAttributes, vm.bops.content);
                }
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("bopAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            (function () {
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("bopAttributes"));
                } else {
                    setAttributes = null;
                }
                if (setAttributes != null && setAttributes != undefined) {
                    angular.forEach(setAttributes, function (setAtt) {
                        if (setAtt.id != null && setAtt.id != "" && setAtt.id != 0) {
                            vm.objectIds.push(setAtt.id);
                        }
                    });
                    ObjectTypeAttributeService.getObjectTypeAttributesByIds(vm.objectIds).then(
                        function (data) {
                            if (data.length == 0) {

                                setAttributes = null;
                                $window.localStorage.setItem("bopAttributes", "");
                                vm.selectedBopAttributes = setAttributes
                            } else {
                                vm.selectedBopAttributes = setAttributes;
                            }
                            loadBOPS()
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadBOPS()
                }
            })();

        }
    }
);