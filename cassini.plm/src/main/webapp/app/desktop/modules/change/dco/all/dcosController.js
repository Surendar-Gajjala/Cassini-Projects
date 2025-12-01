define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/dcoService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/specificationsService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/workflowService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective',
        'app/desktop/modules/directives/dcoTypeDirective'
    ],
    function (module) {
        module.controller('DCOsController', DCOsController);

        function DCOsController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce, DCOService, ObjectTypeAttributeService,
                                ItemService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService, AttributeAttachmentService, CommonService, ProjectService, SpecificationsService,
                                RecentlyVisitedService, QualityTypeService, WorkflowService) {

            $rootScope.viewInfo.icon = "fa fa-calendar";
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;

            var parsed = angular.element("<div></div>");
            var newDco = parsed.html($translate.instant("NEW_DCO")).html();
            var create = parsed.html($translate.instant("CREATE")).html();
            $scope.cannotDeleteApprovedDco = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_DCO")).html();
            vm.showAttributes = parsed.html($translate.instant("SHOW_ATTRIBUTES")).html();
            $scope.noDcos = parsed.html($translate.instant("NO_DCOS")).html();

            var currencyMap = new Hashtable();
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.showDCO = showDCO;
            vm.deleteDco = deleteDco;
            vm.searchText = null;
            vm.filterSearch = null;
            vm.loading = false;

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
                dcoNumber: null,
                description: null,
                dcoType: '',
                searchQuery: null,
                changeAnalyst:'',
                status: ''
            };
            $scope.freeTextQuery = null;

            vm.dcos = angular.copy(pagedResults);


            vm.newDCO = newDCO;

            function newDCO() {
                var options = {
                    title: newDco,
                    template: 'app/desktop/modules/change/dco/new/newDCOView.jsp',
                    controller: 'NewDCOController as newDcoVm',
                    resolve: 'app/desktop/modules/change/dco/new/newDCOController',
                    width: 700,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.dco.new'}
                    ],
                    callback: function (dco) {
                        $timeout(function () {
                            showDCO(dco);
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadDCOS() {
                $rootScope.showBusyIndicator()
                vm.loading = true;
                DCOService.getAllDCOs(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.dcos = data;
                        vm.loading = false;
                        loadAttributeValues();
                        loadChangeAnalysts();
                        loadStatus();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }

                )
            }

            vm.objectIds = [];
            vm.attributeIds = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                angular.forEach(vm.dcos.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                if (vm.selectedAttributes.length > 0) {
                    $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.dcos.content);
                }
            }


            function nextPage() {
                if (vm.dcos.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadDCOS();
                }
            }

            function previousPage() {
                if (vm.dcos.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadDCOS();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadDCOS();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadDCOS();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.dcos = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadDCOS();
            }

            function showDCO(dco) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = dco.id;
                vm.recentlyVisited.objectType = "DCO";
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.changes.dco.details', {dcoId: dco.id, tab: 'details.basic'});
                    }, function (error) {
                        $state.go('app.changes.dco.details', {dcoId: dco.id, tab: 'details.basic'});
                    }
                )
            }


            var deleteDCO = parsed.html($translate.instant("DELETE_DCO")).html();
            var DeleteDcoTitle = parsed.html($translate.instant("DELETE_DCO_TITLE_MSG")).html();
            var DCODeleteMsg = parsed.html($translate.instant("DCO_DELETE_MSG")).html();

            function deleteDco(dco) {
                var options = {
                    title: deleteDCO,
                    message: DeleteDcoTitle + " [ " + dco.dcoNumber + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        if (dco.id != null && dco.id != undefined) {
                            DCOService.deleteDCO(dco.id).then(
                                function (data) {
                                    var index = vm.dcos.content.indexOf(dco);
                                    vm.dcos.content.splice(index, 1);
                                    vm.dcos.totalElements--;
                                    $rootScope.showSuccessMessage(DCODeleteMsg);
                                    loadChangeAnalysts();
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }
                });
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
                        selectedObjectType: "DCOTYPE",
                        selectedObject: "DCO",
                        selectedParentObjectType: "CHANGE"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.quality.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("dcoAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                        loadDCOS();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            $scope.changeAnalysts = [];
            function loadChangeAnalysts(){
                DCOService.getChangeAnalysts().then(
                    function (data) {
                        $scope.changeAnalysts = data;
                    }
                )
            }

            $scope.statuses = [];
            function loadStatus(){
                WorkflowService.getObjectWorkflowStatus("DCO").then(
                    function (data) {
                        $scope.statuses = data;
                    }
                )
            }

            $scope.clearChangeAnalyst = clearChangeAnalyst;
            function clearChangeAnalyst() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedPerson = null;
                vm.filters.changeAnalyst = '';
                loadDCOS();
                $rootScope.hideBusyIndicator();
            }

            $scope.selectedPerson = null;
            $scope.onSelectChangeAnalyst = onSelectChangeAnalyst;
            function onSelectChangeAnalyst(person) {
                vm.pageable.page = 0;
                $scope.selectedPerson = person;
                vm.filters.changeAnalyst = person.id;
                loadDCOS();
            }

            $scope.selectedStatus = null;
            $scope.onSelectStatus = onSelectStatus;
            function onSelectStatus(status) {
                vm.pageable.page = 0;
                $scope.selectedStatus = status;
                vm.filters.status = status;
                loadDCOS();
            }

            $scope.clearStatus = clearStatus;
            function clearStatus() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedStatus = null;
                vm.filters.status = '';
                loadDCOS();
                $rootScope.hideBusyIndicator();
            }


            vm.clearTypeSelection = clearTypeSelection;
            function clearTypeSelection() {
                vm.pageable.page = 0;
                vm.selectedDcoType = null;
                vm.filters.dcoType = '';
                loadDCOS();
            }

            vm.onSelectType = onSelectType;
            function onSelectType(dcoType) {
                vm.pageable.page = 0; 
                vm.selectedDcoType = dcoType;
                vm.filters.dcoType = dcoType.id;
                loadDCOS();
            }

            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem("dcoAttributes", JSON.stringify(vm.selectedAttributes));
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("dcoAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            (function () {
                //if ($application.homeLoaded == true) {
                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                });
                $rootScope.getChangeAttributes("CHANGE", "DCOTYPE", vm.attributeIds);
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("dcoAttributes"));
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
                                $window.localStorage.setItem("dcoAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadDCOS();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadDCOS();
                    loadStatus();
                    loadChangeAnalysts();
                }
                //}
            })();
        }
    }
);


