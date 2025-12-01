define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/ecrService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/workflowService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/specificationsService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/shared/services/core/qualityTypeService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective',
        'app/desktop/modules/directives/ecrTypeDirective'
    ],
    function (module) {
        module.controller('ECRsController', ECRsController);

        function ECRsController($scope, $rootScope, $translate, $timeout, $state, $window, $stateParams, $cookies, $application,
                                $sce, ECRService, ECOService, DialogService, ObjectTypeAttributeService, WorkflowService) {

            $rootScope.viewInfo.icon = "fa fa-calendar";
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            vm.loading = true;
            //vm.selectedEcrType = null;
            var parsed = angular.element("<div></div>");
            $scope.createEcrTitle = parsed.html($translate.instant("CREATE_NEW_ECR")).html();
            var newEcrTitle = parsed.html($translate.instant("NEW_ECR")).html();
            var createButton = parsed.html($translate.instant("CREATE")).html();
            var deleteDialogTitle = parsed.html($translate.instant("DELETE_ECR")).html();
            var deleteEcrDialogMessage = parsed.html($translate.instant("DELETE_ECR_DIALOG_MESSAGE")).html();
            var ecrDeletedMessage = parsed.html($translate.instant("ECR_DELETED_MESSAGE")).html();
            $scope.cannotDeleteApprovedEcr = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_ECR")).html();
            vm.showAttributes = parsed.html($translate.instant("SHOW_ATTRIBUTES")).html();

            var currencyMap = new Hashtable();

            vm.newEcr = newEcr;

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
                crNumber: null,
                crType: '',
                descriptionofChange: null,
                searchQuery: null,
                changeAnalyst: '',
                status: '',
                urgency: '',
                originator: '',
                requestedBy: '',
                changeReasonType: ''
            };

            vm.ecrs = angular.copy(pagedResults);

            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;

            function nextPage() {
                if (vm.ecrs.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadAllEcrs();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadAllEcrs();
            }

            function previousPage() {
                if (vm.ecrs.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadAllEcrs();
                }
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadAllEcrs();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.ecrs = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadAllEcrs();
            }


            function loadAllEcrs() {
                $rootScope.showBusyIndicator();
                ECRService.getAllECRs(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.ecrs = data;
                        loadAttributeValues();
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        vm.loading = false;
                    }
                )
            }

            vm.objectIds = [];
            vm.attributeIds = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                angular.forEach(vm.ecrs.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                if (vm.selectedAttributes.length > 0) {
                    $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.ecrs.content);
                }
            }


            function newEcr() {
                var options = {
                    title: newEcrTitle,
                    template: 'app/desktop/modules/change/ecr/new/newEcrView.jsp',
                    controller: 'NewEcrController as newEcrVm',
                    resolve: 'app/desktop/modules/change/ecr/new/newEcrController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createButton, broadcast: 'app.ecrs.new'}
                    ],
                    callback: function (ecr) {
                        $timeout(function () {
                            showEcr(ecr);
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.deleteECR = deleteECR;
            function deleteECR(ecr) {
                var options = {
                    title: deleteDialogTitle,
                    message: deleteEcrDialogMessage + " [" + ecr.crNumber + "] ?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        ECRService.deleteECR(ecr.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(ecrDeletedMessage);
                                loadChangeTypeFilterObjects();
                                loadWorkflowStatus();
                                loadAllEcrs();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            vm.showEcr = showEcr;
            function showEcr(ecr) {
                $state.go('app.changes.ecr.details', {ecrId: ecr.id, tab: 'details.basic'});
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
                        selectedObjectType: "ECRTYPE",
                        selectedObject: "ECR",
                        selectedParentObjectType: "CHANGE"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.quality.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("ecrAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                        loadAllEcrs();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem("ecrAttributes", JSON.stringify(vm.selectedAttributes));
            }

            $scope.selectedPerson = null;
            $scope.onSelectChangeAnalyst = onSelectChangeAnalyst;
            function onSelectChangeAnalyst(person) {
                vm.pageable.page = 0;
                $scope.selectedPerson = person;
                vm.filters.changeAnalyst = person.id;
                loadAllEcrs();
            }

            $scope.clearChangeAnalyst = clearChangeAnalyst;
            function clearChangeAnalyst() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedPerson = null;
                vm.filters.changeAnalyst = '';
                loadAllEcrs();
                $rootScope.hideBusyIndicator();
            }


            function loadWorkflowStatus() {
                WorkflowService.getObjectWorkflowStatus("ECR").then(
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
                loadAllEcrs();
            }

            $scope.clearStatus = clearStatus;
            function clearStatus() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedStatus = null;
                vm.filters.status = '';
                loadAllEcrs();
                $rootScope.hideBusyIndicator();
            }


            $scope.selectedUrgency = null;
            $scope.onSelectUrgency = onSelectUrgency;
            function onSelectUrgency(urgency) {
                vm.pageable.page = 0;
                $scope.selectedUrgency = urgency;
                vm.filters.urgency = urgency;
                loadAllEcrs();
            }

            $scope.clearUrgency = clearUrgency;
            function clearUrgency() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedUrgency = null;
                vm.filters.urgency = '';
                loadAllEcrs();
                $rootScope.hideBusyIndicator();
            }


            $scope.selectedOriginator = null;
            $scope.onSelectOriginator = onSelectOriginator;
            function onSelectOriginator(person) {
                vm.pageable.page = 0;
                $scope.selectedOriginator = person;
                vm.filters.originator = person.id;
                loadAllEcrs();
            }

            $scope.clearOriginator = clearOriginator;
            function clearOriginator() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedOriginator = null;
                vm.filters.originator = '';
                loadAllEcrs();
                $rootScope.hideBusyIndicator();
            }


            $scope.selectedRequestedBy = null;
            $scope.onSelectRequestedBy = onSelectRequestedBy;
            function onSelectRequestedBy(person) {
                vm.pageable.page = 0;
                $scope.selectedRequestedBy = person;
                vm.filters.requestedBy = person.id;
                loadAllEcrs();
            }

            $scope.clearRequestedBy = clearRequestedBy;
            function clearRequestedBy() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedRequestedBy = null;
                vm.filters.requestedBy = '';
                loadAllEcrs();
                $rootScope.hideBusyIndicator();
            }


            $scope.selectedChangeReasonType = null;
            $scope.onSelectChangeReasonType = onSelectChangeReasonType;
            function onSelectChangeReasonType(changeReasonType) {
                vm.pageable.page = 0;
                $scope.selectedChangeReasonType = changeReasonType;
                vm.filters.changeReasonType = changeReasonType;
                loadAllEcrs();
            }

            $scope.clearChangeReasonType = clearChangeReasonType;
            function clearChangeReasonType() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedChangeReasonType = null;
                vm.filters.changeReasonType = '';
                loadAllEcrs();
                $rootScope.hideBusyIndicator();
            }

            vm.clearTypeSelection = clearTypeSelection;
            function clearTypeSelection() {
                vm.pageable.page = 0;
                vm.selectedEcrType = null;
                vm.filters.crType = '';
                loadAllEcrs();
            }

            vm.onSelectType = onSelectType;
            function onSelectType(crType) {
                vm.pageable.page = 0;
                vm.selectedEcrType = crType;
                vm.filters.crType = crType.id;
                loadAllEcrs();
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("ecrAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            vm.filterObjectTypes = null;
            $scope.originators = [];
            $scope.allUrgency = [];
            $scope.changeAnalysts = [];
            $scope.requesters = [];
            $scope.changeReasonTypes = [];
            function loadChangeTypeFilterObjects() {
                vm.filterObjectTypes = null;
                $scope.originators = [];
                $scope.allUrgency = [];
                $scope.changeAnalysts = [];
                $scope.requesters = [];
                $scope.changeReasonTypes = [];
                ECOService.getChangeTypeFilterObjects("ECR").then(
                    function (data) {
                        vm.filterObjectTypes = data;
                        $scope.originators = vm.filterObjectTypes.originators;
                        $scope.allUrgency = vm.filterObjectTypes.urgencies;
                        $scope.changeAnalysts = vm.filterObjectTypes.changeAnalysts;
                        $scope.requesters = vm.filterObjectTypes.requestedBys;
                        $scope.changeReasonTypes = vm.filterObjectTypes.changeReasonTypes;
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                });
                $rootScope.getChangeAttributes("CHANGE", "ECRTYPE", vm.attributeIds);
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("ecrAttributes"));
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
                                $window.localStorage.setItem("ecrAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadAllEcrs();
                        }
                    )
                } else {
                    loadAllEcrs();
                }
                loadChangeTypeFilterObjects();
                loadWorkflowStatus();
            })();
        }
    }
);


