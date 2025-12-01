define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/mbomService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/desktop/directives/all-view-icons/allViewIconsDirective',
    ],
    function (module) {
        module.controller('AllMBOMController', AllMBOMController);

        function AllMBOMController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application,
                                   $stateParams, $cookies, $sce, MBOMService, CommonService, ObjectTypeAttributeService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newMBOM = newMBOM;
            vm.mboms = [];
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
                mco: ''
            };
            $scope.freeTextQuery = null;

            vm.mboms = angular.copy(pagedResults);


            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newMBOMHeading = parsed.html($translate.instant("NEW_MBOM")).html();

            function newMBOM() {
                var options = {
                    title: newMBOMHeading,
                    template: 'app/desktop/modules/mes/mbom/new/newMBOMView.jsp',
                    controller: 'NewMBOMController as newMBOMVm',
                    resolve: 'app/desktop/modules/mes/mbom/new/newMBOMController',
                    width: 700,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.mbom.new'}
                    ],
                    callback: function (mbom) {
                        $timeout(function () {
                            loadMBOMS();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

        var mbomAttributeTitle = parsed.html($translate.instant("ATTRIBUTES")).html();
        var selectedAttributesAdded = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();
        var addButton = parsed.html($translate.instant("ADD")).html();

        vm.showTypeAttributes = showTypeAttributes;
        function showTypeAttributes() {
            var options = {
                title: mbomAttributeTitle,
                template: 'app/desktop/modules/shared/attributes/attributesView.jsp',
                resolve: 'app/desktop/modules/shared/attributes/attributesController',
                controller: 'AttributesController as attributesVm',
                width: 500,
                showMask: true,
                data: {
                    selectedAttributes: vm.selectedMbomAttributes,
                    type: "MBOMTYPE",
                    objectType: "MESOBJECT"
                },
                buttons: [
                    {text: addButton, broadcast: 'add.select.attributes'}
                ],
                callback: function (result) {
                    vm.selectedMbomAttributes = result;
                    $window.localStorage.setItem("mbomAttributes", JSON.stringify(vm.selectedMbomAttributes));
                    if (result.length > 0) {
                        $rootScope.showSuccessMessage(selectedAttributesAdded);
                    }
                    loadMBOMS();
                }
            };

            $rootScope.showSidePanel(options);
        }

            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.selectedMbomAttributes.remove(att);
                $window.localStorage.setItem("mbomAttributes", JSON.stringify(vm.selectedMbomAttributes));
            }

            function nextPage() {
                if (vm.mboms.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadMBOMS();
                }
            }

            function previousPage() {
                if (vm.mboms.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadMBOMS();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadMBOMS();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadMBOMS();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.mboms = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadMBOMS();
            }

            function loadMBOMS() {
                MBOMService.getAllMBOMs(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.mboms = data;
                        loadMbomAttributeValues();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }
            vm.objectIds = [];
            vm.attributeIds = [];
            vm.selectedMbomAttributes = [];
            function loadMbomAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.mboms.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                if (vm.selectedMbomAttributes != null && vm.selectedMbomAttributes.length > 0) {
                    
                    angular.forEach(vm.selectedMbomAttributes, function (selectedAttribute) {
                        if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                            vm.attributeIds.push(selectedAttribute.id);
                        }
                    });
                    $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedMbomAttributes, vm.mboms.content);
                }
            }

            vm.showMBOM = showMBOM;
            function showMBOM(mbom) {
                $state.go('app.mes.mbom.details', {mbomId: mbom.latestRevision, tab: 'details.basic'});

            }
            vm.showItem = showItem;
            function showItem(mbom) {
                $state.go('app.items.details', {itemId: mbom.itemRevisionId, tab: 'details.basic'});
            }

            vm.showMBOMRevisionHistory = showMBOMRevisionHistory;
            var revisionHistoryTitle = $translate.instant("REVISION_HISTORY_TITLE");

            function showMBOMRevisionHistory(mbom) {
                var options = {
                    title: mbom.number + " - " + revisionHistoryTitle,
                    template: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryView.jsp',
                    controller: 'ItemRevisionHistoryController as revHistoryVm',
                    resolve: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryController',
                    data: {
                        itemId: mbom.id,
                        revisionHistoryType: "MBOM"
                    },
                    width: 700,
                    showMask: true,
                };

                $rootScope.showSidePanel(options);
            }


            var deleteDialogTitle = parsed.html($translate.instant("DELETE_MBOM")).html();
            var deletePlantDialogMessage = parsed.html($translate.instant("DELETE_MBOM_DIALOG_MESSAGE")).html();
            var mbomDeletedSuccessMessage = parsed.html($translate.instant("MBOM_DELETED_MESSAGE")).html();
            $scope.cannotDeleteApprovedMbom = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_MBOM")).html();
            $scope.cannotDeleteRejectedMbom = parsed.html($translate.instant("CANNOT_DELETE_REJECTED_MBOM")).html();

            vm.deleteMBOM = deleteMBOM;
            function deleteMBOM(mbom) {
                var options = {
                    title: deleteDialogTitle,
                    message: deletePlantDialogMessage.format(mbom.name),
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        MBOMService.deleteMBOM(mbom.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(mbomDeletedSuccessMessage);
                                loadMBOMS();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }


            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("mbomAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            (function () {
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("mbomAttributes"));
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
                                $window.localStorage.setItem("mbomAttributes", "");
                                vm.selectedMbomAttributes = setAttributes
                            } else {
                                vm.selectedMbomAttributes = setAttributes;
                            }
                            loadMBOMS()
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadMBOMS()
                }
            })();  

        }
    }
);