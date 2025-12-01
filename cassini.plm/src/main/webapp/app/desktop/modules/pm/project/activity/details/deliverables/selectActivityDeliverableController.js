define(
    [
        'app/desktop/modules/pm/project/activity/activity.module',
        'app/shared/services/core/activityService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/specificationsService',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/desktop/modules/classification/directive/classificationTreeController',
        'app/desktop/modules/rm/directive/specificationDirective',
        'app/desktop/modules/rm/directive/requirementDirective'
    ],
    function (module) {
        module.controller('SelectActivityDeliverableController', SelectActivityDeliverableController);

        function SelectActivityDeliverableController($scope, $state, $window, $rootScope, $stateParams, $timeout, $translate, ActivityService,
                                                     ProjectService, ItemService, SpecificationsService) {

            var vm = this;
            var projectId = $rootScope.selectedProjectId;
            var activityId = $scope.data.selectedActivityId;
            vm.selectedItems = [];
            vm.selectItem = selectItem;
            vm.selectAllItems = selectAllItems;
            vm.selectedAllItems = false;

            var parsed = angular.element("<div></div>");
            var pleaseSelectAtleast = parsed.html($translate.instant("SELECT_FOLDERITEM")).html();
            var deliverableMessage = parsed.html($translate.instant("SELECT_DELIVERABLE")).html();

           vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };



            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            function nextPage() {
                if (vm.items.last != true) {
                    vm.pageable.page++;
                    if (vm.mode == "ACTIVITY") {
                        loadActivityDeliverables();
                    } else {
                        loadTaskDeliverables();
                    }

                }
            }

            function previousPage() {
                if (vm.items.first != true) {
                    vm.pageable.page--;
                    if (vm.mode == "ACTIVITY") {
                        loadActivityDeliverables();
                    } else {
                        loadTaskDeliverables();
                    }
                }
            }

            vm.deliverableFilter = {
                objectId: '',
                objectType: null,
                itemType: '',
                itemNumber: null,
                name: null,
                description: null
            };

            function loadActivityDeliverables() {
                vm.loading = true;
                vm.deliverableFilter.objectId = activityId;
                vm.deliverableFilter.objectType = "PROJECTACTIVITY";
                ActivityService.getFilteredItemDeliverables(vm.deliverableFilter, vm.pageable).then(
                    function (data) {
                        vm.loading = false;
                        vm.selectedAllItems = false;
                        vm.items = data;
                        ItemService.getLatestRevisionReferences(vm.items.content, 'latestRevision');
                        loadGlossaryDeliverables();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadGlossaryDeliverables() {
                ActivityService.getFilteredGlossaryDeliverables(vm.deliverableFilter, vm.pageable).then(
                    function (data) {
                        vm.loading = false;
                        vm.selectedAllItems = false;
                        vm.glossaries = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadTaskDeliverables() {
                vm.loading = true;
                vm.deliverableFilter.objectId = vm.taskId;
                vm.deliverableFilter.objectType = "PROJECTTASK";
                ActivityService.getFilteredItemDeliverables(vm.deliverableFilter, vm.pageable).then(
                    function (data) {
                        vm.loading = false;
                        vm.selectedAllItems = false;
                        vm.items = data;
                        ItemService.getLatestRevisionReferences(vm.items.content, 'latestRevision');
                        loadGlossaryDeliverables();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.selectedAllItems = false;
            function selectAllItems() {
                vm.selectedItems = [];
                if (vm.selectedAllItems == false) {
                    $scope.check = false;
                    vm.selectedAllItems = false;
                    angular.forEach(vm.items.content, function (item) {
                        item.selected = false;
                    })
                } else {
                    vm.error = "";
                    angular.forEach(vm.items.content, function (item) {
                        item.selected = true;
                        vm.selectedItems.push(item);
                    })
                    if (vm.selectedItems.length == vm.items.content.length) {
                        vm.selectedAllItems = true;
                    }
                }
            }

            function selectItem(item) {
                var flag = true;
                vm.error = "";
                angular.forEach(vm.selectedItems, function (selectedItem) {
                    if (selectedItem.id == item.id) {
                        flag = false;
                        var index = vm.selectedItems.indexOf(item);
                        vm.selectedItems.splice(index, 1);
                    }
                    if (vm.selectedItems.length == vm.items.content.length) {
                        vm.selectedAllItems = true;
                    } else {
                        vm.selectedAllItems = false;
                    }
                });
                if (flag) {
                    vm.selectedItems.push(item);
                    if (vm.selectedItems.length == vm.items.content.length) {
                        vm.selectedAllItems = true;
                    }
                }
            }

            vm.selectedAllGlossary = false;
            vm.selectedGlossaries = [];
            vm.selectGlossary = selectGlossary;
            vm.checkAllGlossary = checkAllGlossary;

            function checkAllGlossary() {
                if (vm.selectedAllGlossary) {
                    vm.selectedGlossaries = [];
                    vm.selectedAllGlossary = true;
                    angular.forEach(vm.glossaries.content, function (item) {
                        item.glossarySelected = vm.selectedAllGlossary;
                        vm.selectedGlossaries.push(item);
                    })
                } else {
                    vm.selectedAllGlossary = false;
                    angular.forEach(vm.glossaries.content, function (item) {
                        item.glossarySelected = vm.selectedAllGlossary;
                        vm.selectedGlossaries = [];
                    })

                }
            }

            function selectGlossary(item) {
                var flag = true;
                vm.error = "";
                angular.forEach(vm.selectedGlossaries, function (selectedItem) {
                    if (selectedItem.id == item.id) {
                        flag = false;
                        var index = vm.selectedGlossaries.indexOf(item);
                        vm.selectedGlossaries.splice(index, 1);
                    }
                    if (vm.selectedGlossaries.length == vm.glossaries.content.length) {
                        vm.selectedAllGlossary = true;
                    } else {
                        vm.selectedAllGlossary = false;
                    }
                });
                if (flag) {
                    vm.selectedGlossaries.push(item);
                    if (vm.selectedGlossaries.length == vm.glossaries.content.length) {
                        vm.selectedAllGlossary = true;
                    }
                }
            }

            vm.selectedType = null;
            vm.clear = false;

            vm.filters = {
                name: null,
                objectNumber: null,
                itemType: null,
                itemNumber: null,
                itemName: null,
                description: null,
                latestRevision: null,
                status: null
            };

            vm.clearFilter = clearFilter;
            vm.onSelectType = onSelectType;
            vm.searchItems = searchItems;

            function clearFilter() {
                vm.deliverableFilter = {
                    objectId: '',
                    objectType: null,
                    itemType: '',
                    itemNumber: null,
                    name: null,
                    description: null
                };
                $rootScope.freeTextQuery = null;
                vm.selectedType = null;
                vm.clear = false;
                if (vm.mode == "ACTIVITY") {
                    loadActivityDeliverables();
                }

                if (vm.mode == "TASK") {
                    loadTaskDeliverables();
                }
            }

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.selectedType = itemType;
                    vm.itemType = itemType;
                    vm.deliverableFilter.itemType = itemType.id;
                    $rootScope.freeTextQuery = itemType.name;
                    searchItems();
                }
            }

            function searchItems() {
                vm.clear = true;
                if (vm.deliverableFilter.itemNumber != null) {
                    $rootScope.freeTextQuery = vm.deliverableFilter.itemNumber;
                }
                if (vm.deliverableFilter.name != null) {
                    $rootScope.freeTextQuery = vm.deliverableFilter.name;
                }
                if (vm.mode == "ACTIVITY") {
                    loadActivityDeliverables();
                }

                if (vm.mode == "TASK") {
                    loadTaskDeliverables();
                }

            }

            function selectDeliverable() {
                if (vm.selectedItems.length == 0 && vm.selectedGlossaries.length == 0 && vm.selectedSpecifications.length == 0 && vm.selectedRequirements.length == 0) {
                    $rootScope.showWarningMessage(pleaseSelectAtleast);
                } else {
                    if (vm.mode == "ACTIVITY") {
                        if (vm.showItemDeliverable == true) {
                            vm.activityDeliverables = [];
                            angular.forEach(vm.selectedItems, function (item) {
                                vm.activityDeliverable = {
                                    id: null,
                                    activity: activityId,
                                    itemRevision: item.latestRevision,
                                    deliverableStatus: 'PENDING'
                                };
                                vm.activityDeliverables.push(vm.activityDeliverable);
                            });
                            if (vm.selectedItems.length == vm.activityDeliverables.length) {
                                ActivityService.createActivityDeliverables(activityId, vm.activityDeliverables).then(
                                    function (data) {
                                        data.type = "Item";
                                        vm.deleverables = data;
                                        $scope.callback(data);
                                        vm.activityDeliverables = [];
                                        vm.selectedItems = [];
                                        vm.activityDeliverable = {
                                            id: null,
                                            activity: null,
                                            itemRevision: null
                                        };
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        }

                        else if (vm.specification == true) {
                            SpecificationsService.createSpecificationDeliverable(activityId, "PROJECTACTIVITY", vm.selectedSpecifications).then(
                                function (data) {
                                    data.type = "Specification";
                                    $rootScope.hideSidePanel('right');
                                    $scope.callback(data);
                                    vm.selectedSpecifications = [];
                                    vm.creating = false;
                                    $rootScope.showSuccessMessage(deliverableMessage);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                });
                        }

                        else if (vm.requirement == true) {
                            SpecificationsService.createRequirementDeliverable(activityId, "PROJECTACTIVITY", vm.selectedRequirements).then(
                                function (data) {
                                    data.type = "Requirement";
                                    $rootScope.hideSidePanel('right');
                                    $scope.callback(data);
                                    vm.selectedRequirements = [];
                                    vm.creating = false;
                                    $rootScope.showSuccessMessage(deliverableMessage);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                });
                        }

                        else {
                            vm.activityDeliverables = [];
                            angular.forEach(vm.selectedGlossaries, function (glossary) {
                                vm.glossaryDeliverable = {
                                    id: null,
                                    objectId: activityId,
                                    objectType: "PROJECTACTIVITY",
                                    glossary: glossary
                                };
                                vm.activityDeliverables.push(vm.glossaryDeliverable);
                            });
                            if (vm.selectedGlossaries.length == vm.activityDeliverables.length) {
                                ActivityService.createGlossaryDeliverables(activityId, vm.activityDeliverables).then(
                                    function (data) {
                                        data.type = "Terminology";
                                        vm.deleverables = data;
                                        $scope.callback(data);
                                        vm.activityDeliverables = [];
                                        vm.selectedGlossaries = [];
                                        vm.glossaryDeliverable = {
                                            id: null,
                                            objectId: null,
                                            glossary: null
                                        };
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        }
                    }

                    if (vm.mode == "TASK") {
                        if (vm.showItemDeliverable == true) {
                            vm.taskDeliverables = [];
                            vm.taskDeliverables.deliverableStatus = null;
                            angular.forEach(vm.selectedItems, function (item) {
                                vm.taskDeliverable = {
                                    id: null,
                                    task: vm.taskId,
                                    itemRevision: item.latestRevision,
                                    deliverableStatus: 'PENDING'
                                };
                                vm.taskDeliverables.push(vm.taskDeliverable);
                            });
                            if (vm.selectedItems.length == vm.taskDeliverables.length) {
                                ActivityService.createTaskDeliverables(activityId, vm.taskId, vm.taskDeliverables).then(
                                    function (data) {
                                        data.type = "Item";
                                        vm.deleverables = data;
                                        $scope.callback(data);
                                        vm.taskDeliverables = [];
                                        vm.selectedItems = [];
                                        vm.taskDeliverable = {
                                            id: null,
                                            task: null,
                                            itemRevision: null
                                        };
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        }

                        else if (vm.specification == true) {
                            SpecificationsService.createSpecificationDeliverable(vm.taskId, "PROJECTTASK", vm.selectedSpecifications).then(
                                function (data) {
                                    data.type = "Specification";
                                    $rootScope.hideSidePanel('right');
                                    $scope.callback(data);
                                    vm.selectedSpecifications = [];
                                    vm.creating = false;
                                    $rootScope.showSuccessMessage(deliverableMessage);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                });
                        }

                        else if (vm.requirement == true) {
                            SpecificationsService.createRequirementDeliverable(vm.taskId, "PROJECTTASK", vm.selectedRequirements).then(
                                function (data) {
                                    data.type = "Requirement";
                                    $rootScope.hideSidePanel('right');
                                    $scope.callback(data);
                                    vm.selectedRequirements = [];
                                    vm.creating = false;
                                    $rootScope.showSuccessMessage(deliverableMessage);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                });
                        }

                        else {
                            vm.activityDeliverables = [];
                            angular.forEach(vm.selectedGlossaries, function (glossary) {
                                vm.glossaryDeliverable = {
                                    id: null,
                                    objectId: vm.taskId,
                                    objectType: "PROJECTTASK",
                                    glossary: glossary
                                };
                                vm.activityDeliverables.push(vm.glossaryDeliverable);
                            });
                            if (vm.selectedGlossaries.length == vm.activityDeliverables.length) {
                                ActivityService.createGlossaryDeliverables(vm.taskId, vm.activityDeliverables).then(
                                    function (data) {
                                        data.type = "Terminology";
                                        vm.deleverables = data;
                                        $scope.callback(data);
                                        vm.activityDeliverables = [];
                                        vm.selectedGlossaries = [];
                                        vm.glossaryDeliverable = {
                                            id: null,
                                            objectId: null,
                                            glossary: null
                                        };
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        }
                    }
                }

            }

            vm.showItems = showItems;
            vm.showGlossary = showGlossary;
            vm.showGlossaryDeliverable = false;
            vm.showItemDeliverable = true;

            function showItems() {
                vm.showItemDeliverable = true;
                vm.showGlossaryDeliverable = false;

                vm.requirement = false;
                vm.specification = false;
            }

            function showGlossary() {
                vm.showItemDeliverable = false;
                vm.showGlossaryDeliverable = true;
                vm.requirement = false;
                vm.specification = false;
            }

            vm.showItem = showItem;

            function showItem(item) {
                if (vm.mode == "ACTIVITY") {
                    $window.localStorage.setItem("lastSelectedActivityTab", JSON.stringify('details.deliverables'));
                    $state.go('app.items.details', {itemId: item.latestRevision});
                } else {
                    $window.localStorage.setItem("lastSelectedTaskTab", JSON.stringify('details.deliverables'));
                    $state.go('app.items.details', {itemId: item.latestRevision});
                }

            }

            vm.openGlossaryDetails = openGlossaryDetails;

            function openGlossaryDetails(glossary) {
                $window.localStorage.setItem("lastSelectedGlossaryTab", JSON.stringify('details.entries'));
                if (vm.mode == "ACTIVITY") {
                    $window.localStorage.setItem("lastSelectedActivityTab", JSON.stringify('details.deliverables'));
                    $state.go('app.rm.glossary.details', {glossaryId: glossary.id});
                } else {
                    $window.localStorage.setItem("lastSelectedTaskTab", JSON.stringify('details.deliverables'));
                    $state.go('app.rm.glossary.details', {glossaryId: glossary.id});
                }

            }

            /*------------ Specification and Requirement Activity and Task Deliverables ------------*/
            vm.selectedSpecifications = [];
            vm.selectedRequirements = [];
            vm.projectSpecifications = [];
            vm.projectRequirements = [];
            vm.showSpecification = showSpecification;
            vm.showRequirements = showRequirements;

            vm.specification = false;
            vm.requirement = false;

            function showSpecification() {

                if (vm.mode == "ACTIVITY") {
                    vm.showItemDeliverable = false;
                    vm.showGlossaryDeliverable = false;
                    vm.requirement = false;
                    vm.specification = true;
                    loadProjectSpecificationDeliverable(activityId);
                }

                if (vm.mode == "TASK") {
                    vm.showItemDeliverable = false;
                    vm.showGlossaryDeliverable = false;
                    vm.requirement = false;
                    vm.specification = true;
                    loadProjectSpecificationDeliverable(vm.taskId);
                }
                resizeView();
            }

            function showRequirements() {
                if (vm.mode == "ACTIVITY") {
                    loadProjectRequirementDeliverable(activityId);
                    vm.showItemDeliverable = false;
                    vm.showGlossaryDeliverable = false;
                    vm.requirement = true;
                    vm.specification = false;
                }
                if (vm.mode == "TASK") {
                    loadProjectRequirementDeliverable(vm.taskId);
                    vm.showItemDeliverable = false;
                    vm.showGlossaryDeliverable = false;
                    vm.requirement = true;
                    vm.specification = false;
                }
                resizeView();
            }

            function loadProjectSpecificationDeliverable(selId) {
                SpecificationsService.getSpecificationDeliverable(selId, vm.pageable, vm.SpecReqfilters).then(
                    function (data) {
                        vm.projectSpecifications = data;
                        vm.selectedAll = false;
                        angular.forEach(vm.projectSpecifications.content, function (specs) {
                            specs.selected = false;
                        })
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadProjectRequirementDeliverable(selId) {
                SpecificationsService.getRequirementDeliverable(selId, vm.pageable, vm.SpecReqfilters).then(
                    function (data) {
                        vm.projectRequirements = data;
                        vm.selectedAll = false;
                        angular.forEach(vm.projectRequirements.content, function (specs) {
                            specs.selected = false;
                        })
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
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

            vm.selectedSpecAll = false;
            function checkAllSpecifications() {
                vm.selectedSpecifications = [];
                if (vm.selectedSpecAll == false) {
                    $scope.check = false;
                    vm.selectedSpecAll = false;
                    angular.forEach(vm.projectSpecifications.content, function (item) {
                        item.specSelected = false;
                    })
                } else {
                    vm.error = "";
                    angular.forEach(vm.projectSpecifications.content, function (item) {
                        item.specSelected = true;
                        vm.selectedSpecifications.push(item);
                    })
                    if (vm.selectedSpecifications.length == vm.items.content.length) {
                        vm.selectedSpecAll = true;
                    }
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
                            vm.selectedRequirements.splice(index, 1);
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

            vm.showSpecificationDetails = showSpecificationDetails;
            vm.showRequirementDetails = showRequirementDetails;
            function showSpecificationDetails(spec) {

                if (vm.mode == "ACTIVITY") {
                    $window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify("details.deliverables"));
                    $state.go('app.rm.specifications.details', {specId: spec.id});
                } else {
                    $window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify("details.deliverables"));
                    $state.go('app.rm.specifications.details', {specId: spec.id});
                }

            }

            function showRequirementDetails(req) {
                if (vm.mode == "ACTIVITY") {
                    $window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify("details.deliverables"));
                    $state.go('app.rm.requirements.details', {requirementId: req.id});
                } else {
                    $window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify("details.deliverables"));
                    $state.go('app.rm.requirements.details', {requirementId: req.id});
                }

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
                    if (vm.mode == "ACTIVITY") {
                        vm.pageable.page++;
                        loadProjectSpecificationDeliverable(activityId);
                    }
                    if (vm.mode == "TASK") {
                        vm.pageable.page++;
                        loadProjectSpecificationDeliverable(vm.taskId);
                    }

                }
            }

            function previousSpecificationPage() {
                if (vm.projectSpecifications.first != true) {
                    if (vm.mode == "ACTIVITY") {
                        vm.pageable.page--;
                        loadProjectSpecificationDeliverable(activityId);
                    }
                    if (vm.mode == "TASK") {
                        vm.pageable.page--;
                        loadProjectSpecificationDeliverable(vm.taskId);
                    }
                }
            }

            function onSpecificationSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    if (vm.mode == "ACTIVITY") {
                        vm.selectedType = itemType;
                        vm.itemType = itemType;
                        vm.SpecReqfilters.type = itemType.id;
                        vm.clearSpecification = true;
                        $rootScope.freeTextQuery = itemType.name;
                        loadProjectSpecificationDeliverable(activityId);
                    }
                    if (vm.mode == "TASK") {
                        vm.selectedType = itemType;
                        vm.itemType = itemType;
                        vm.SpecReqfilters.type = itemType.id;
                        vm.clearSpecification = true;
                        $rootScope.freeTextQuery = itemType.name;
                        loadProjectSpecificationDeliverable(vm.taskId);
                    }

                }
            }

            function searchSpecifications() {
                if (vm.SpecReqfilters.name != null) {
                    $rootScope.freeTextQuery = vm.SpecReqfilters.name;
                }
                if (vm.SpecReqfilters.description != null) {
                    $rootScope.freeTextQuery = vm.SpecReqfilters.description;
                }
                if (vm.mode == "ACTIVITY") {
                    vm.clearSpecification = true;
                    loadProjectSpecificationDeliverable(activityId);
                }
                if (vm.mode == "TASK") {
                    vm.clearSpecification = true;
                    loadProjectSpecificationDeliverable(vm.taskId);
                }

            }

            function clearSpecificationFilter() {
                if (vm.mode == "ACTIVITY") {
                    vm.SpecReqfilters = {
                        name: null,
                        objectNumber: null,
                        type: '',
                        itemNumber: null,
                        description: null
                    };

                    vm.selectedType = null;
                    vm.clearSpecification = false;
                    $rootScope.freeTextQuery = null;
                    loadProjectSpecificationDeliverable(activityId);
                }
                if (vm.mode == "TASK") {
                    vm.SpecReqfilters = {
                        name: null,
                        objectNumber: null,
                        type: '',
                        itemNumber: null,
                        description: null
                    };
                    $rootScope.freeTextQuery = null;
                    vm.selectedType = null;
                    vm.clearSpecification = false;
                    loadProjectSpecificationDeliverable(vm.taskId);
                }

            }

            /*--------------- Search Requirement Methods --------------*/

            vm.nextRequirementPage = nextRequirementPage;
            vm.previousRequirementPage = previousRequirementPage;
            vm.onRequirementSelectType = onRequirementSelectType;
            vm.clearRequirementFilter = clearRequirementFilter;
            vm.searchRequirements = searchRequirements;
            vm.clearRequirement = false;

            vm.SpecReqfilters = {
                name: null,
                objectNumber: null,
                type: '',
                itemNumber: null,
                description: null
            };

            function nextRequirementPage() {
                if (vm.projectRequirements.last != true) {
                    if (vm.mode == "ACTIVITY") {
                        vm.pageable.page++;
                        loadProjectRequirementDeliverable(activityId);
                    }
                    if (vm.mode == "TASK") {
                        vm.pageable.page++;
                        loadProjectRequirementDeliverable(vm.taskId);
                    }

                }
            }

            function previousRequirementPage() {
                if (vm.projectRequirements.first != true) {
                    if (vm.mode == "ACTIVITY") {
                        vm.pageable.page--;
                        loadProjectRequirementDeliverable(activityId);
                    }
                    if (vm.mode == "TASK") {
                        vm.pageable.page--;
                        loadProjectRequirementDeliverable(vm.taskId);
                    }
                }
            }

            function onRequirementSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    $rootScope.freeTextQuery = itemType.name;
                    if (vm.mode == "ACTIVITY") {
                        vm.selectedType = itemType;
                        vm.itemType = itemType;
                        vm.SpecReqfilters.type = itemType.id;
                        loadProjectRequirementDeliverable(activityId);
                        vm.clearRequirement = true;
                    }
                    if (vm.mode == "TASK") {
                        vm.selectedType = itemType;
                        vm.itemType = itemType;
                        vm.SpecReqfilters.type = itemType.id;
                        loadProjectRequirementDeliverable(vm.taskId);
                        vm.clearRequirement = true;
                    }

                }

            }

            function searchRequirements() {
                if (vm.SpecReqfilters.name != null) {
                    $rootScope.freeTextQuery = vm.SpecReqfilters.name;
                }
                if (vm.SpecReqfilters.description != null) {
                    $rootScope.freeTextQuery = vm.SpecReqfilters.description;
                }
                if (vm.mode == "ACTIVITY") {
                    loadProjectRequirementDeliverable(activityId);
                    vm.clearRequirement = true;
                }
                if (vm.mode == "TASK") {
                    loadProjectRequirementDeliverable(vm.taskId);
                    vm.clearRequirement = true;
                }

            }

            function clearRequirementFilter() {
                if (vm.mode == "ACTIVITY") {
                    vm.SpecReqfilters = {
                        name: null,
                        objectNumber: null,
                        type: '',
                        itemNumber: null,
                        description: null
                    };
                    vm.selectedType = null;
                    vm.clearRequirement = false;
                    $rootScope.freeTextQuery = null;
                    loadProjectRequirementDeliverable(activityId);

                }
                if (vm.mode == "TASK") {
                    vm.SpecReqfilters = {
                        name: null,
                        objectNumber: null,
                        type: '',
                        itemNumber: null,
                        description: null
                    };

                    vm.selectedType = null;
                    vm.clearRequirement = false;
                    $rootScope.freeTextQuery = null;
                    loadProjectRequirementDeliverable(vm.taskId);

                }

            }

            var itemsTitle = parsed.html($translate.instant("ITEMS_ALL_TITLE")).html();
            var requirementTitle = parsed.html($translate.instant("REQUIREMENT")).html();
            vm.deliverableList = [itemsTitle, requirementTitle];

            $scope.switchType = switchType;
            $scope.selected = itemsTitle;
            function switchType(selected) {
                var seletedValue = selected;
                vm.pageable.page = 0;

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
                        vm.showItemDeliverable = true;
                        vm.showGlossaryDeliverable = false;
                        resizeView();
                        if (vm.mode == "ACTIVITY") {
                            loadActivityDeliverables();
                        }
                        if (vm.mode == "TASK") {
                            loadTaskDeliverables();
                        }
                        break;
                    case 'Terminology':
                        vm.specification = false;
                        vm.requirement = false;
                        vm.showItemDeliverable = false;
                        vm.showGlossaryDeliverable = true;
                        resizeView();
                        if (vm.mode == "ACTIVITY") {
                            loadActivityDeliverables();
                        }
                        if (vm.mode == "TASK") {
                            loadTaskDeliverables();
                        }
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
                        vm.showItemDeliverable = true;
                        vm.showGlossaryDeliverable = false;
                        if (vm.mode == "ACTIVITY") {
                            loadActivityDeliverables();
                        }
                        if (vm.mode == "TASK") {
                            loadTaskDeliverables();
                        }

                }

            }

            function resizeView() {
                var sidePanelHeight = $("#rightSidePanelContent").outerHeight();
                $("#deliverablesView").height(sidePanelHeight);

                var formContent = $("#form-content").outerHeight();
                var footerContent = $("#footer-content").outerHeight();
                if (vm.showGlossaryDeliverable) {
                    formContent = 0;
                }
                $("#search-content").height(sidePanelHeight - (formContent + footerContent));
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    $(window).resize(resizeView);
                    vm.activityDeliverable = {
                        id: null,
                        activity: null,
                        itemRevision: null
                    };
                    $rootScope.freeTextQuery = null;
                    vm.selectedItems = [];
                    vm.activityDeliverables = [];
                    vm.taskId = $scope.data.selectedTaskId;
                    vm.mode = $scope.data.deliverableMode;
                    if (vm.mode == "ACTIVITY") {
                        loadActivityDeliverables();
                    }

                    if (vm.mode == "TASK") {
                        loadTaskDeliverables();
                    }
                    $timeout(function () {
                        resizeView();
                    }, 500);
                    $scope.$on('app.project.activity.deliverable.select', selectDeliverable);
                //}
            })();

        }
    }
);