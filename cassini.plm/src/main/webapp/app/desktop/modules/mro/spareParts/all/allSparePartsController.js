define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/shared/services/core/sparePartsService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/specificationsService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllSparePartController', AllSparePartController);

        function AllSparePartController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce, SparePartService, ObjectTypeAttributeService,
                                        ItemService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService, AttributeAttachmentService, CommonService, ProjectService, SpecificationsService,
                                        RecentlyVisitedService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newSparePart = newSparePart;
            vm.deleteSparePart = deleteSparePart;
            vm.sparePart = [];
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
                name: null,
                number: null,
                partType: '',
                workOrder: '',
                asset: ''
            };
            $scope.freeTextQuery = null;

            vm.spareParts = angular.copy(pagedResults);


            var parsed = angular.element("<div></div>");
            $scope.newSparePart = parsed.html($translate.instant("NEW_SPARE_PART_TYPE")).html();
            var create = parsed.html($translate.instant("CREATE")).html();
            var newSparePartHeading = parsed.html($translate.instant("NEW_SPARE_PART_TYPE")).html();

            function newSparePart() {
                var options = {
                    title: newSparePartHeading,
                    template: 'app/desktop/modules/mro/spareParts/new/newSparePartView.jsp',
                    controller: 'NewSparePartController as newSparePartVm',
                    resolve: 'app/desktop/modules/mro/spareParts/new/newSparePartController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.sparePart.new'}
                    ],
                    callback: function (sparePart) {
                        $timeout(function () {
                            loadSpareParts();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function nextPage() {
                if (vm.spareParts.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadSpareParts();
                }
            }

            function previousPage() {
                if (vm.spareParts.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadSpareParts();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadSpareParts();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadSpareParts();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.spareParts = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadSpareParts();
            }

            function loadSpareParts() {
                vm.spareParts = [];
                vm.loading = true;
                SparePartService.getAllSpareParts(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.spareParts = data;
                        CommonService.getPersonReferences(vm.spareParts.content, 'modifiedBy');
                        loadAttributeValues();
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.objectIds = [];
            vm.selectedAttributes = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.spareParts.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.spareParts.content);

            }


            vm.showSparePart = showSparePart;
            function showSparePart(sparePart) {
                $state.go('app.mro.sparePart.details', {sparePartId: sparePart.id, tab: 'details.basic'});
                /* vm.recentlyVisited = {};
                 vm.recentlyVisited.objectId = sparePart.id;
                 vm.recentlyVisited.objectType = sparePart.objectType;
                 vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                 RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                 function (data) {
                 $state.go('app.mes.sparePart.details', {sparePartId: sparePart.id, tab: 'details.basic'});
                 }, function (error) {
                 $state.go('app.mes.sparePart.details', {sparePartId: sparePart.id, tab: 'details.basic'});
                 }
                 )*/
            }

            var deleteSparePart = parsed.html($translate.instant("DELETE_SPARE_PART")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var sparePartDeleteMsg = parsed.html($translate.instant("SPARE_PART_DELETE_MSG")).html();


            function deleteSparePart(sparePart) {
                var options = {
                    title: deleteSparePart,
                    message: deleteDialogMessage + " [ " + sparePart.name + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        if (sparePart.id != null && sparePart.id != undefined) {
                            SparePartService.deleteSparePart(sparePart.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(sparePartDeleteMsg);
                                    loadSpareParts();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                });
            }

            var currencyMap = new Hashtable();
            var attributeTitle = parsed.html($translate.instant("ATTRIBUTES")).html();
            var selectedAttributesAdded = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();

            vm.showTypeAttributes = showTypeAttributes;
            function showTypeAttributes() {
                var options = {
                    title: attributeTitle,
                    template: 'app/desktop/modules/shared/attributes/attributesView.jsp',
                    resolve: 'app/desktop/modules/shared/attributes/attributesController',
                    controller: 'AttributesController as attributesVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: vm.selectedAttributes,
                        type: "SPAREPARTTYPE",
                        objectType: "MROSPAREPART"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("sparePartAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadSpareParts();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("sparePartAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            (function () {
                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                })
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("sparePartAttributes"));
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
                                $window.localStorage.setItem("sparePartAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadSpareParts();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    loadSpareParts();
                }
            })();

        }
    }
);