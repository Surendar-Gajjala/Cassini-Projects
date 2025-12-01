/**
 * Created by swapna on 1/17/18.
 */
define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/activityService',
        'app/shared/services/core/glossaryService',
        'app/shared/services/core/specificationsService',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/desktop/modules/classification/directive/classificationTreeController',
        'app/desktop/modules/rm/directive/specificationDirective',
        'app/desktop/modules/rm/directive/requirementDirective'
    ],
    function (module) {

        module.controller('NewDeliverableController', NewDeliverableController);

        function NewDeliverableController($scope, $q, $rootScope, $window, $translate, $timeout, $state, $stateParams, GlossaryService, ProjectService, ItemService, SpecificationsService) {


            var vm = this;
            vm.select = select;
            vm.create = create;
            vm.checkAll = checkAll;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.total = 0;
            vm.onSelectType = onSelectType;
            vm.clearFilter = clearFilter;
            vm.searchItems = searchItems;
            vm.clear = false;
            vm.loadProjectDeliverables = loadProjectDeliverables;
            vm.selectedAll = false;
            vm.selectedItems = [];
            vm.selectedGlossary = [];
            vm.selectedSpecifications = [];
            vm.selectedRequirements = [];
            vm.items = [];
            vm.showItemDeliverable = true;

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
            vm.items = angular.copy(pagedResults);

            vm.filters = {
                name: null,
                objectNumber: null,
                itemType: '',
                itemNumber: null,
                description: null
            };

            vm.showItems = showItems;
            vm.showGlossary = showGlossary;
            vm.showSpecification = showSpecification;
            vm.showRequirements = showRequirements;
            vm.selectedGlossare = selectedGlossare;
            vm.checkAllGlossary = checkAllGlossary;

            function showItems() {
                vm.showItemDeliverable = true;
                vm.glossary = false;
                vm.requirement = false;
                vm.specification = false;
            }

            function showSpecification() {
                loadProjectSpecificationDeliverable();
                vm.showItemDeliverable = false;
                vm.requirement = false;
                vm.glossary = false;
                vm.specification = true;
                resizeView();
            }

            function showRequirements() {
                loadProjectRequirementDeliverable();
                vm.showItemDeliverable = false;
                vm.glossary = false;
                vm.requirement = true;
                vm.specification = false;
                resizeView();
            }

            function showGlossary() {
                vm.showItemDeliverable = false;
                vm.glossary = true;
                vm.specification = false;
                vm.requirement = false;
            }

            vm.onSelectSpecType = onSelectSpecType;
            vm.selectedType = null;
            function onSelectSpecType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.selectedType = itemType;
                    vm.itemType = itemType;
                    vm.SpecReqfilters.type = itemType.id;
                    $rootScope.freeTextQuery = itemType.name;
                    loadProjectSpecificationDeliverable();
                    vm.clear = true;
                }
            }

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.selectedType = itemType;
                    vm.itemType = itemType;
                    vm.filters.itemType = itemType.id;
                    $rootScope.freeTextQuery = itemType.name;
                    searchItems();
                    //vm.clear = true;
                }
            }

            function searchItems() {

                if (vm.filters.itemType != '' || (vm.filters.objectNumber != null && vm.filters.objectNumber != "") || (vm.filters.itemNumber != null && vm.filters.itemNumber != "") || (vm.filters.name != null && vm.filters.name != "")) {

                    if(vm.filters.objectNumber != null){
                        $rootScope.freeTextQuery = vm.filters.objectNumber;
                    }
                    if(vm.filters.itemNumber != null){
                        $rootScope.freeTextQuery = vm.filters.itemNumber;
                    }
                    if(vm.filters.name != null){
                        $rootScope.freeTextQuery = vm.filters.name;
                    }
                    vm.clear = true;
                    vm.pageable.page = 0;
                    loadProjectDeliverables();

                } else {
                   // $rootScope.showWarningMessage(pleaseEnterSearchValue);
                   clearFilter();
                   
                }

            }

            function clearFilter() {
                vm.filters = {
                    name: null,
                    itemType: '',
                    itemNumber: null,
                    itemName: null,
                    latestRevision: null,
                    status: null,
                    objectNumber: null
                };
                vm.selectedType = null;
                $rootScope.freeTextQuery = null;
                loadProjectDeliverables();
                vm.clear = false;
            }

            function nextPage() {
                if (vm.projectDeliverables.last != true) {
                    vm.pageable.page++;
                    loadProjectDeliverables();
                }
            }

            function previousPage() {
                if (vm.projectDeliverables.first != true) {
                    vm.pageable.page--;
                    loadProjectDeliverables();
                }
            }

            var parsed = angular.element("<div></div>");
            var deliverableAddedSuccesMessage = parsed.html($translate.instant("DELIVERABLE_ADD_SUCCESS")).html();
            var pleaseEnterSearchValue = parsed.html($translate.instant("SEARCH_VALUE")).html();
            var selectItem = parsed.html($translate.instant("ATLEAST_ONE_ITEM_VALIDATION")).html();
            var searchValidation = parsed.html($translate.instant("SEARCH_MESSAGE_VALIDATION")).html();

            var itemsTitle = parsed.html($translate.instant("ITEMS_ALL_TITLE")).html();
            var glossaryTitle = parsed.html($translate.instant("TERMINOLOGY")).html();
            var specificationTitle = parsed.html($translate.instant("SPECIFICATIONS")).html();
            var requirementTitle = parsed.html($translate.instant("REQUIREMENT")).html();

            function select(item) {
                var flag = true;
                if (item.selected == false) {
                    vm.selectedAll = false;
                    var index = vm.selectedItems.indexOf(item);
                    vm.selectedItems.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedItems, function (selectedItem) {
                        if (selectedItem.id == item.id) {
                            flag = false;
                            var index = vm.selectedItems.indexOf(item);
                            vm.selectedItems.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedItems.push(item);
                    }

                    if (vm.selectedItems.length == vm.projectDeliverables.content.length) {
                        vm.selectedAll = true;
                    }
                }
            }

            /*-------- Specification deliverables ---------------*/
            vm.selectedSpecAll = false;
            vm.selectedReqAll = false;

            vm.selectSpecification = selectSpecification;
            function selectSpecification(item) {
                var flag = true;
                if (item.specSelected == false) {
                    vm.selectedSpecAll = false;
                    var index = vm.selectedSpecifications.indexOf(item);
                    vm.selectedSpecifications.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedSpecifications, function (selectedItem) {
                        if (selectedItem.id == item.id) {
                            flag = false;
                            var index = vm.selectedSpecifications.indexOf(item);
                            vm.selectedSpecifications.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedSpecifications.push(item);
                    }

                    if (vm.selectedSpecifications.length == vm.projectSpecifications.content.length) {
                        vm.selectedSpecAll = true;
                    }
                }
            }

            vm.checkAllSpecifications = checkAllSpecifications;
            vm.projectSpecifications = [];
            function checkAllSpecifications() {
                if (vm.selectedSpecAll) {
                    vm.selectedSpecifications = [];
                    vm.selectedSpecAll = true;
                    angular.forEach(vm.projectSpecifications.content, function (item) {
                        item.specSelected = vm.selectedSpecAll;
                        vm.selectedSpecifications.push(item);
                    });
                } else {
                    vm.selectedSpecAll = false;
                    angular.forEach(vm.projectSpecifications.content, function (item) {
                        item.specSelected = vm.selectedSpecAll;
                        vm.selectedSpecifications = [];
                    });
                }
            }

            /*-------- Requirement deliverables ---------------*/

            vm.selectRequirement = selectRequirement;
            function selectRequirement(item) {
                var flag = true;
                if (item.reqSelected == false) {
                    vm.selectedReqAll = false;
                    var index = vm.selectedRequirements.indexOf(item);
                    vm.selectedRequirements.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedRequirements, function (selectedItem) {
                        if (selectedItem.id == item.id) {
                            flag = false;
                            var index = vm.selectedRequirements.indexOf(item);
                            vm.selectedSpecifications.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedRequirements.push(item);
                    }

                    if (vm.selectedRequirements.length == vm.projectRequirements.content.length) {
                        vm.selectedReqAll = true;
                    }
                }
            }

            vm.checkAllRequirements = checkAllRequirements;
            function checkAllRequirements() {
                if (vm.selectedReqAll) {
                    vm.selectedRequirements = [];
                    vm.selectedReqAll = true;
                    angular.forEach(vm.projectRequirements.content, function (item) {
                        item.reqSelected = vm.selectedReqAll;
                        vm.selectedRequirements.push(item);
                    });
                } else {
                    vm.selectedReqAll = false;
                    angular.forEach(vm.projectRequirements.content, function (item) {
                        item.reqSelected = vm.selectedReqAll;
                        vm.selectedRequirements = [];
                    });
                }
            }

            /*------------Show  Specification and Requirement Details*/
            vm.showSpecificationDetails = showSpecificationDetails;
            vm.showRequirementDetails = showRequirementDetails;
            function showSpecificationDetails(spec) {
                $window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify('details.deliverables'));
                $state.go('app.rm.specifications.details', {specId: spec.id});
            }

            function showRequirementDetails(req) {
                $window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify('details.deliverables'));
                $state.go('app.rm.requirements.details', {requirementId: req.id});
            }

            /*--------------- Search Specification Methods --------------*/

            vm.nextSpecificationPage = nextSpecificationPage;
            vm.previousSpecificationPage = previousSpecificationPage;
            vm.onSpecificationSelectType = onSpecificationSelectType;
            vm.clearSpecificationFilter = clearSpecificationFilter;
            vm.searchSpecifications = searchSpecifications;
            vm.clearSpecification = false;

            vm.SpecReqfilters = {
                name: null,
                objectNumber: null,
                type: '',
                itemNumber: null,
                description: null
            };

            function nextSpecificationPage() {
                if (vm.projectSpecifications.last != true) {
                    vm.pageable.page++;
                    loadProjectSpecificationDeliverable();
                }
            }

            function previousSpecificationPage() {
                if (vm.projectSpecifications.first != true) {
                    vm.pageable.page--;
                    loadProjectSpecificationDeliverable();
                }
            }

            function onSpecificationSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.selectedType = itemType;
                    vm.itemType = itemType;
                    vm.SpecReqfilters.type = itemType.id;
                    loadProjectSpecificationDeliverable();
                    vm.clearSpecification = true;
                }
            }

            function searchSpecifications() {

                if (vm.SpecReqfilters.type == '' && (vm.SpecReqfilters.name == null || vm.SpecReqfilters.name == "") && (vm.SpecReqfilters.objectNumber == null || vm.SpecReqfilters.objectNumber == "") && (vm.SpecReqfilters.description == null || vm.SpecReqfilters.description == "")) {
                    $rootScope.showWarningMessage(pleaseEnterSearchValue);
                } else {
                    vm.clearSpecification = true;
                    if(vm.SpecReqfilters.objectNumber != null){
                        $rootScope.freeTextQuery = vm.SpecReqfilters.objectNumber;
                    }
                    if(vm.SpecReqfilters.description != null){
                        $rootScope.freeTextQuery = vm.SpecReqfilters.description;
                    }
                    if(vm.SpecReqfilters.name != null){
                        $rootScope.freeTextQuery = vm.SpecReqfilters.name;
                    }
                    loadProjectSpecificationDeliverable();
                }
            }

            function clearSpecificationFilter() {
                vm.SpecReqfilters = {
                    name: null,
                    objectNumber: null,
                    type: '',
                    itemNumber: null,
                    description: null
                };

                $rootScope.freeTextQuery = null;
                vm.selectedType = null;
                loadProjectSpecificationDeliverable();
                vm.clearSpecification = false;
            }

            /*--------------- Search Requirement Methods --------------*/

            vm.nextRequirementPage = nextRequirementPage;
            vm.previousRequirementPage = previousRequirementPage;
            vm.onRequirementSelectType = onRequirementSelectType;
            vm.clearRequirementFilter = clearRequirementFilter;
            vm.searchRequirements = searchRequirements;
            vm.clearRequirement = false;
            vm.specification = false;
            vm.requirement = false;

            vm.SpecReqfilters = {
                name: null,
                objectNumber: null,
                type: '',
                itemNumber: null,
                description: null
            };

            function nextRequirementPage() {
                if (vm.projectRequirements.last != true) {
                    vm.pageable.page++;
                    loadProjectRequirementDeliverable();
                }
            }

            function previousRequirementPage() {
                if (vm.projectRequirements.first != true) {
                    vm.pageable.page--;
                    loadProjectRequirementDeliverable();
                }
            }

            function onRequirementSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.selectedType = itemType;
                    vm.itemType = itemType;
                    vm.SpecReqfilters.type = itemType.id;
                    $rootScope.freeTextQuery = itemType.name;
                    loadProjectRequirementDeliverable();
                    vm.clearRequirement = true;
                }
            }

            function searchRequirements() {
                if (vm.SpecReqfilters.type == '' && vm.SpecReqfilters.name == null && vm.SpecReqfilters.objectNumber == null && vm.SpecReqfilters.description == null) {
                    $rootScope.showWarningMessage(pleaseEnterSearchValue);
                } else {
                    vm.clearRequirement = true;
                    if(vm.SpecReqfilters.objectNumber != null){
                        $rootScope.freeTextQuery = vm.SpecReqfilters.objectNumber;
                    }
                    if(vm.SpecReqfilters.description != null){
                        $rootScope.freeTextQuery = vm.SpecReqfilters.description;
                    }
                    if(vm.SpecReqfilters.name != null){
                        $rootScope.freeTextQuery = vm.SpecReqfilters.name;
                    }
                    loadProjectRequirementDeliverable();
                }

            }

            function clearRequirementFilter() {
                vm.SpecReqfilters = {
                    name: null,
                    objectNumber: null,
                    type: '',
                    itemNumber: null,
                    description: null
                };
                $rootScope.freeTextQuery = null;
                vm.selectedType = null;
                loadProjectRequirementDeliverable();
                vm.clearRequirement = false;
            }

            function checkAll() {
                if (vm.selectedAll) {
                    vm.selectedItems = [];
                    vm.selectedAll = true;
                    angular.forEach(vm.projectDeliverables.content, function (item) {
                        item.selected = vm.selectedAll;
                        vm.selectedItems.push(item);
                    });
                } else {
                    vm.selectedAll = false;
                    angular.forEach(vm.projectDeliverables.content, function (item) {
                        item.selected = vm.selectedAll;
                        vm.selectedItems = [];
                    });
                }
            }

            function create() {
                if (vm.selectedItems.length == 0 && vm.selectedSpecifications.length == 0 && vm.selectedRequirements.length == 0 && vm.selectedGlossary.length == 0) {
                    $rootScope.showWarningMessage(selectItem);
                }
                else {
                    if (vm.selectedItems.length != 0) {
                        ProjectService.createProjectDeliverables($stateParams.projectId, vm.selectedItems).then(
                            function (data) {
                                $rootScope.hideSidePanel('right');
                                data.type = "Item";
                                $scope.callback(data);
                                vm.selectedItems = [];
                                vm.creating = false;
                                $rootScope.showSuccessMessage(deliverableAddedSuccesMessage);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )

                    }
                    if (vm.selectedGlossary.length != 0) {
                        GlossaryService.createGlossaryDeliverable($stateParams.projectId, vm.selectedGlossary).then(
                            function (data) {
                                data.type = "Terminology";
                                $rootScope.hideSidePanel('right');
                                $scope.callback(data);
                                vm.selectedGlossary = [];
                                vm.creating = false;
                                $rootScope.showSuccessMessage(deliverableAddedSuccesMessage);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            });
                    }

                    if (vm.selectedSpecifications.length != 0) {
                        SpecificationsService.createSpecificationDeliverable($stateParams.projectId, "PROJECT", vm.selectedSpecifications).then(
                            function (data) {
                                $rootScope.hideSidePanel('right');
                                data.type = "Specification";
                                $scope.callback(data);
                                vm.selectedSpecifications = [];
                                vm.creating = false;
                                $rootScope.showSuccessMessage(deliverableAddedSuccesMessage);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            });
                    }

                    if (vm.selectedRequirements.length != 0) {
                        SpecificationsService.createRequirementDeliverable($stateParams.projectId, "PROJECT", vm.selectedRequirements).then(
                            function (data) {
                                $rootScope.hideSidePanel('right');
                                data.type = "Requirement";
                                $scope.callback(data);
                                vm.selectedRequirements = [];
                                vm.creating = false;
                                $rootScope.showSuccessMessage(deliverableAddedSuccesMessage);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            });
                    }

                }

            }

            /*------------- Glossaries ------------*/

            vm.selectedGlossaryAll = false;

            function selectedGlossare(item) {
                var flag = true;
                if (item.glossarySelected == false) {
                    vm.selectedGlossaryAll = false;
                    var index = vm.selectedGlossary.indexOf(item);
                    vm.selectedGlossary.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedGlossary, function (selectedItem) {
                        if (selectedItem.id == item.id) {
                            flag = false;
                            var index = vm.selectedGlossary.indexOf(item);
                            vm.selectedGlossary.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedGlossary.push(item);
                    }

                    if (vm.selectedGlossary.length == vm.projectGlossaries.content.length) {
                        vm.selectedGlossaryAll = true;
                    }
                }
            }

            function checkAllGlossary() {
                if (vm.selectedGlossaryAll) {
                    vm.selectedGlossary = [];
                    vm.selectedGlossaryAll = true;
                    angular.forEach(vm.projectGlossaries.content, function (item) {
                        item.glossarySelected = vm.selectedGlossaryAll;
                        vm.selectedGlossary.push(item);
                    });
                } else {
                    vm.selectedGlossaryAll = false;
                    angular.forEach(vm.projectGlossaries.content, function (item) {
                        item.glossarySelected = vm.selectedGlossaryAll;
                        vm.selectedGlossary = [];
                    });
                }
            }

            function loadProjectDeliverables() {
                vm.loading = true;
                ProjectService.getProjectItemDeliverables($stateParams.projectId, vm.pageable, vm.filters).then(
                    function (data) {
                        vm.projectDeliverables = data;
                        vm.loading = false;
                        vm.selectedAll = false;
                        ItemService.getLatestRevisionReferences(vm.projectDeliverables.content, 'latestRevision');
                        vm.showItemDeliverable = true;
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.projectGlossaries = [];
            function loadProjectGlossariesDeliverable() {
                GlossaryService.getProjectGlossaryDeliverable($stateParams.projectId, vm.pageable, vm.filters).then(
                    function (data) {
                        vm.selectedGlossaryAll = false;
                        vm.projectGlossaries = data;
                        angular.forEach(vm.projectGlossaries.content, function (glossries) {
                            glossries.glossarySelected = false;
                        })
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadProjectSpecificationDeliverable() {
                SpecificationsService.getSpecificationDeliverable($stateParams.projectId, vm.pageable, vm.SpecReqfilters).then(
                    function (data) {
                        vm.projectSpecifications = data;
                        vm.selectedSpecAll = false;
                        angular.forEach(vm.projectSpecifications.content, function (specs) {
                            specs.selected = false;
                        })
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadProjectRequirementDeliverable() {
                SpecificationsService.getRequirementDeliverable($stateParams.projectId, vm.pageable, vm.SpecReqfilters).then(
                    function (data) {
                        vm.projectRequirements = data;
                        vm.selectedReqAll = false;
                        angular.forEach(vm.projectRequirements.content, function (specs) {
                            specs.selected = false;
                        })
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadProjectGlossarys() {
                GlossaryService.getAllProjectGlossaryDeliverables($stateParams.projectId).then(
                    function (data) {
                        vm.glossarys = data;
                        angular.forEach(vm.glossarys, function (glossary) {
                            glossary.selected = false;
                        })
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.openGlossaryDetails = openGlossaryDetails;
            function openGlossaryDetails(glossary) {
                $window.localStorage.setItem("lastSelectedGlossaryTab", JSON.stringify('details.entries'));
                $window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify('details.deliverables'));
                $state.go('app.rm.glossary.details', {glossaryId: glossary.id});
            }

            vm.showItem = showItem;

            function showItem(item) {
                $window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify('details.deliverables'));
                $state.go('app.items.details', {itemId: item.latestRevision});
            }

            vm.deliverableList = [itemsTitle, requirementTitle];

            $scope.switchType = switchType;
            $scope.selected = itemsTitle;
            vm.specification = false;
            vm.requirement = false;
            function switchType(selected) {
                var seletedValue = selected;

                if (seletedValue == 'Artikel') {
                    seletedValue = 'Items';
                }

                if (seletedValue == 'Terminologie') {
                    seletedValue = 'Terminology';
                }

                if (seletedValue == 'Spezifikationen') {
                    seletedValue = 'Specifications';
                }

                if (seletedValue == 'Anforderung') {
                    seletedValue = 'Requirement';
                }

                switch (seletedValue) {
                    case 'Items':
                        vm.specification = false;
                        vm.requirement = false;
                        vm.glossary = false;
                        vm.showItemDeliverable = true;
                        resizeView();
                        loadProjectDeliverables();
                        //loadProjectGlossariesDeliverable();
                        break;
                    case 'Terminology':
                        vm.specification = false;
                        vm.requirement = false;
                        vm.showItemDeliverable = false;
                        //loadProjectDeliverables();
                        vm.glossary = true;
                        resizeView();
                        loadProjectGlossariesDeliverable();
                        break;
                    case 'Specifications':
                        showSpecification();
                        break;

                    case 'Requirement':
                        showRequirements();
                        break;

                    default:
                        vm.specification = false;
                        vm.requirement = false;
                        vm.glossary = false;
                        vm.showItemDeliverable = true;
                        loadProjectDeliverables();
                    //loadProjectGlossariesDeliverable();

                }

            }

            function resizeView() {
                var sidePanelHeight = $("#rightSidePanelContent").outerHeight();
                $("#deliverablesView").height(sidePanelHeight);

                var formContent = $("#form-content").outerHeight();
                var footerContent = $("#footer-content").outerHeight();
                if (vm.glossary) {
                    formContent = 0;
                }
                $("#search-content").height(sidePanelHeight - (formContent + footerContent));
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    $(window).resize(resizeView);
                    $rootScope.freeTextQuery = null;
                    loadProjectDeliverables();
                    loadProjectGlossariesDeliverable();
                    $timeout(function () {
                        resizeView();
                    }, 500);
                    $scope.$on('app.deliverables.new', create);
                //}
            })();
        }

    }
)
;