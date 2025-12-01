define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/shared/services/core/plantService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/projectService',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/recentlyVisitedService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllPlantController', AllPlantController);

        function AllPlantController($scope, $rootScope, $translate, $timeout, $state, $window, $application,
                                    $stateParams, $cookies, $sce, PlantService, ItemService,
                                    ECOService, WorkflowDefinitionService, RecentlyVisitedService, ObjectTypeAttributeService,
                                    MfrService, MfrPartsService, AttributeAttachmentService, CommonService,
                                    ProjectService, SpecificationsService, DialogService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newPlant = newPlant;
            vm.plants = [];
            vm.objectIds = [];
            vm.selectedAttributes = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.deletePlant = deletePlant;

            vm.searchText = null;
            vm.filterSearch = null;
            var currencyMap = new Hashtable();

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
                searchQuery: null
            };
            $scope.freeTextQuery = null;

            vm.plants = angular.copy(pagedResults);


            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newPlantTitle = parsed.html($translate.instant("NEW_PLANT")).html();
            var deleteDialogTitle = parsed.html($translate.instant("DELETE_PLANT")).html();
            var deletePlantDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var plantDeletedSuccessMessage = parsed.html($translate.instant("PLANT_DELETED_MESSAGE")).html();
            $scope.plantAssignedToWorkCenterCannotDelete = parsed.html($translate.instant("PLANT_ASSIGNED_WORK_CENTER_CANNOT_DELETE")).html();


            function newPlant() {
                var options = {
                    title: newPlantTitle,
                    template: 'app/desktop/modules/mes/plant/new/newPlantView.jsp',
                    controller: 'NewPlantController as newPlantVm',
                    resolve: 'app/desktop/modules/mes/plant/new/newPlantController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.plant.new'}
                    ],
                    callback: function (plant) {
                        $timeout(function () {
                            loadPlants();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function nextPage() {
                if (vm.plants.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadPlants();
                }
            }

            $scope.plantFilePopover = {
                templateUrl: 'app/desktop/modules/mes/plant/all/plantFilePopoverTempalate.jsp'
            };


            function previousPage() {
                if (vm.plants.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadPlants();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadPlants();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadPlants();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.plants = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadPlants();
            }

            function loadPlants() {
                vm.loading = true;
                PlantService.getAllPlants(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.plants = data;
                        angular.forEach(vm.plants.content, function (plant) {
                            plant.modifiedDatede = null;
                            if (plant.modifiedDate != null) {
                                plant.modifiedDatede = moment(plant.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                            }
                        });
                        CommonService.getPersonReferences(vm.plants.content, 'modifiedBy');
                        loadAttributeValues();
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var plantsAttributeTitle = parsed.html($translate.instant("ATTRIBUTES")).html();
            var selectedAttributesAdded = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();

            vm.showTypeAttributes = showTypeAttributes;
            function showTypeAttributes() {
                var options = {
                    title: plantsAttributeTitle,
                    template: 'app/desktop/modules/shared/attributes/attributesView.jsp',
                    resolve: 'app/desktop/modules/shared/attributes/attributesController',
                    controller: 'AttributesController as attributesVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: vm.selectedAttributes,
                        type: "PLANTTYPE",
                        objectType: "PLANT"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("plantAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadPlants();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem("plantAttributes", JSON.stringify(vm.selectedAttributes));

            }


            vm.objectIds = [];
            vm.attributeIds = [];
            vm.selectedAttributes = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.plants.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.plants.content);

            }


            function deletePlant(plant) {
                var options = {
                    title: deleteDialogTitle,
                    message: deletePlantDialogMessage + " [" + plant.name + "] ?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        PlantService.deletePlant(plant.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(plantDeletedSuccessMessage);
                                loadPlants();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            vm.showPlant = showPlant;
            function showPlant(plant) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = plant.id;
                vm.recentlyVisited.objectType = plant.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.mes.masterData.plant.details', {plantId: plant.id, tab: 'details.basic'});
                    }, function (error) {
                        $state.go('app.mes.masterData.plant.details', {plantId: plant.id, tab: 'details.basic'});
                    }
                )
            }


            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("plantAttributes"));
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
                    setAttributes = JSON.parse($window.localStorage.getItem("plantAttributes"));
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
                                $window.localStorage.setItem("plantAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadPlants();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadPlants();
                }
            })();

        }
    }
);