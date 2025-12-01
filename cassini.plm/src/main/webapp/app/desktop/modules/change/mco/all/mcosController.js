define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/mcoService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/workflowService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/specificationsService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective',
        'app/desktop/modules/directives/mcoTypeDirective',
        'app/desktop/modules/directives/changeTypeDirective',
        'app/desktop/modules/directives/changeObjectTypeDirective'

    ],
    function (module) {
        module.controller('MCOsController', MCOsController);

        function MCOsController($scope, $rootScope, $translate, $timeout, $state, $window, $stateParams, $cookies, $application, $sce, MCOService, DialogService, ObjectTypeAttributeService,
                                ItemService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService, AttributeAttachmentService, CommonService, ProjectService, WorkflowService,
                                SpecificationsService, RecentlyVisitedService) {

            $rootScope.viewInfo.icon = "fa fa-calendar";
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            vm.loading = false;
            var parsed = angular.element("<div></div>");
            $scope.createMcoTitle = parsed.html($translate.instant("CREATE_NEW_MCO")).html();
            var newMcoTitle = parsed.html($translate.instant("NEW_MCO")).html();
            var createButton = parsed.html($translate.instant("CREATE")).html();
            var deleteDialogTitle = parsed.html($translate.instant("DELETE_MCO")).html();
            var deleteMcoDialogMessage = parsed.html($translate.instant("DELETE_MCO_DIALOG_MESSAGE")).html();
            var mcoDeletedMessage = parsed.html($translate.instant("MCO_DELETED_MESSAGE")).html();
            $scope.cannotDeleteApprovedMco = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_MCO")).html();
            vm.showAttributes = parsed.html($translate.instant("SHOW_ATTRIBUTES")).html();
            var currencyMap = new Hashtable();
            vm.selectedMcoType = null;

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

            vm.mcos = angular.copy(pagedResults);

            vm.filters = {
                mcoNumber: null,
                mcoType: '',
                title: null,
                changeAnalyst: '',
                status: '',
                searchQuery: null
            };

            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            function nextPage() {
                if (vm.inspectionPlans.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadAllMcos();
                }
            }

            function previousPage() {
                if (vm.inspectionPlans.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadAllMcos();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadAllMcos();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadAllMcos();
                } else {
                    resetPage();
                }
            }


            vm.clearTypeSelection = clearTypeSelection;
            function clearTypeSelection() {
                $('.view-content').click();
                vm.pageable.page = 0;
                vm.selectedMcoType = null;
                vm.filters.mcoType = '';
                loadAllMcos();
            }

            vm.onSelectType = onSelectType;
            function onSelectType(mcoType) {
                vm.pageable.page = 0;
                vm.selectedMcoType = mcoType;
                vm.filters.mcoType = mcoType.id;
                vm.filters.freeTextSearch = false;
                loadAllMcos();
            }


            $scope.changeAnalysts = [];
            function loadChangeAnalysts() {
                MCOService.getChangeAnalysts($rootScope.allMCOType).then(
                    function (data) {
                        $scope.changeAnalysts = data;
                    }
                )
            }

            $scope.selectedPerson = null;
            $scope.onSelectChangeAnalyst = onSelectChangeAnalyst;
            function onSelectChangeAnalyst(person) {
                vm.pageable.page = 0;
                $scope.selectedPerson = person;
                vm.filters.changeAnalyst = person.id;
                loadAllMcos();
            }

            $scope.clearChangeAnalyst = clearChangeAnalyst;
            function clearChangeAnalyst() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedPerson = null;
                vm.filters.changeAnalyst = '';
                loadAllMcos();
                $rootScope.hideBusyIndicator();
            }


            vm.status = [];
            vm.selectedStatus = null;
            $scope.statuses = [];
            function loadStatus() {
                MCOService.getStatus($rootScope.allMCOType).then(
                    function (data) {
                        $scope.statuses = data;
                    }
                )
            }

            function loadWorkflowStatus() {
                WorkflowService.getObjectWorkflowStatus($rootScope.allMCOType).then(
                    function (data) {
                        $scope.statuses = data;
                    }
                )
            }

            $scope.selectedStatus = null;
            $scope.onSelectStatus = onSelectStatus;
            function onSelectStatus(status) {
                vm.pageable.page = 0;
                $scope.selectedStatus = status;
                vm.filters.status = status;
                loadAllMcos();
            }

            $scope.clearStatus = clearStatus;
            function clearStatus() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedStatus = null;
                vm.filters.status = '';
                loadAllMcos();
                $rootScope.hideBusyIndicator();
            }

            vm.onSelectedType = onSelectedType;
            function onSelectedType(mcoType) {
                if (mcoType != null && mcoType != undefined) {
                    vm.mco.mcoType = mcoType;
                    vm.mco.changeClass = mcoType;
                }
            }

            function resetPage() {
                vm.mcos = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadAllMcos();
            }

            vm.selectMCOType = selectMCOType;
            function selectMCOType(type) {
                $rootScope.showBusyIndicator($('.view-container'));
                $rootScope.allMCOType = type;
                vm.pageable.page = 0;
                loadAllMcos();
            }

            function loadAllMcos() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                var promise = null;
                if ($rootScope.allMCOType == "ITEMMCO") {
                    document.getElementById("itemMcoType").checked = true;
                    document.getElementById("manufacturerMcoType").checked = false;
                    promise = MCOService.getAllItemMCOs(vm.pageable, vm.filters);
                } else if ($rootScope.allMCOType == "OEMPARTMCO") {
                    document.getElementById("itemMcoType").checked = false;
                    document.getElementById("manufacturerMcoType").checked = true;
                    promise = MCOService.getAllManufacturerMCOs(vm.pageable, vm.filters);
                }
                if (promise != null) {
                    promise.then(
                        function (data) {
                            vm.mcos = data;
                            loadAttributeValues();
                            CommonService.getPersonReferences(vm.mcos.content, 'modifiedBy');
                            CommonService.getPersonReferences(vm.mcos.content, 'changeAnalyst');
                            CommonService.getPersonReferences(vm.mcos.content, 'createdBy');
                            vm.loading = false;
                            $rootScope.hideBusyIndicator();
                            loadChangeAnalysts();
                            loadWorkflowStatus();
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            vm.objectIds = [];
            vm.attributeIds = [];
            function loadAttributeValues() {
                vm.objectIds = [];

                angular.forEach(vm.mcos.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                if (vm.selectedAttributes.length > 0) {
                    $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.mcos.content);
                }
            }


            vm.newMco = newMco;
            function newMco() {
                var options = {
                    title: newMcoTitle,
                    template: 'app/desktop/modules/change/mco/new/newMcoView.jsp',
                    controller: 'NewMCOController as newMcoVm',
                    resolve: 'app/desktop/modules/change/mco/new/newMcoController',
                    width: 600,
                    data: {
                        mcoType: $rootScope.allMCOType
                    },
                    showMask: true,
                    buttons: [
                        {text: createButton, broadcast: 'app.mcos.new'}
                    ],
                    callback: function (mco) {
                        $timeout(function () {
                            showMco(mco);
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.deleteMCO = deleteMCO;
            function deleteMCO(ecr) {
                var options = {
                    title: deleteDialogTitle,
                    message: deleteMcoDialogMessage + " [" + ecr.mcoNumber + "] ?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        MCOService.deleteMCO(ecr.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(mcoDeletedMessage);
                                loadAllMcos();
                                loadStatus();
                                loadChangeAnalysts();
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                });
            }

            vm.showMco = showMco;
            function showMco(mco) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = mco.id;
                vm.recentlyVisited.objectType = "MCO";
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.changes.mco.details', {mcoId: mco.id, tab: 'details.basic'});
                    }, function (error) {
                        $state.go('app.changes.mco.details', {mcoId: mco.id, tab: 'details.basic'});
                    }
                )
            }

            var attributesTitle = $translate.instant("ATTRIBUTES");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var selectedAttributesMessage = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();

            vm.showTypeAttributes = showTypeAttributes;
            vm.removeAttribute = removeAttribute;
            vm.selectedAttributes = [];
            function showTypeAttributes() {
                var options = {
                    title: attributesTitle,
                    template: 'app/desktop/modules/pqm/attributes/qualityTypeAttributeSelectionView.jsp',
                    resolve: 'app/desktop/modules/pqm/attributes/qualityTypeAttributeSelectionController',
                    controller: 'QualityTypeAttributeSelectionController as qualityTypeAttributeSelectionVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: vm.selectedAttributes,
                        selectedObjectType: "MCOTYPE",
                        selectedObject: "MCO",
                        selectedParentObjectType: "CHANGE"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.quality.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("mcoAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                        loadAllMcos();
                        //loadStatus();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem("mcoAttributes", JSON.stringify(vm.selectedAttributes));
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("mcoAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            (function () {
                $rootScope.getChangeAttributes("CHANGE", "MCOTYPE", vm.attributeIds);
                if ($rootScope.allMCOType == null || $rootScope.allMCOType == "" || $rootScope.allMCOType == undefined) {
                    $rootScope.allMCOType = "ITEMMCO";
                }
                if ($rootScope.allMCOType == "OEMPARTMCO") {
                    document.getElementById("itemMcoType").checked = false;
                    document.getElementById("manufacturerMcoType").checked = true;
                }
                //if ($application.homeLoaded == true) {
                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                })
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("mcoAttributes"));
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
                                $window.localStorage.setItem("mcoAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadAllMcos();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    loadAllMcos();
                    loadChangeAnalysts();

                }
                //}
            })();
        }
    }
);


