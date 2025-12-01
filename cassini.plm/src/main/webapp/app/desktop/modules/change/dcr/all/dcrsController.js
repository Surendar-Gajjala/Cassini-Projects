define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/dcrService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/workflowService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective',
        'app/desktop/modules/directives/changeRequestItems/dcrTypeDirective'

    ],
    function (module) {
        module.controller('DCRsController', DCRsController);

        function DCRsController($scope, $rootScope, $translate, $timeout, $state, $window, $stateParams, $application, DialogService, $cookies, $sce, DCRService, ObjectTypeAttributeService,
                                ItemService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService, AttributeAttachmentService, CommonService, ProjectService,
                                SpecificationsService, RecentlyVisitedService, QualityTypeService, WorkflowService) {

            $rootScope.viewInfo.icon = "fa fa-calendar";
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            vm.loading = false;
            var parsed = angular.element("<div></div>");
            var newDcr = parsed.html($translate.instant("NEW_DCR")).html();
            var create = parsed.html($translate.instant("CREATE")).html();
            $scope.cannotDeleteApprovedDcr = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_DCR")).html();
            $scope.noDcr = parsed.html($translate.instant("NO_DCRS")).html();
            vm.showAttributes = parsed.html($translate.instant("SHOW_ATTRIBUTES")).html();
            var currencyMap = new Hashtable();
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.showDCR = showDCR;
            vm.deleteDcr = deleteDcr;
            vm.searchText = null;
            vm.filterSearch = null;
            vm.selectedDcrType = null;

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
                status: '',
                urgency: '',
                changeAnalyst: '',
                requestedBy: '',
                originator: '',
                changeReasonType: '',
                descriptionofChange: null,
                searchQuery: null
            };
            $scope.freeTextQuery = null;

            vm.dcrs = angular.copy(pagedResults);


            vm.newDCR = newDCR;

            function newDCR() {
                var options = {
                    title: newDcr,
                    template: 'app/desktop/modules/change/dcr/new/newDCRView.jsp',
                    controller: 'NewDCRController as newDcrVm',
                    resolve: 'app/desktop/modules/change/dcr/new/newDCRController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.dcr.new'}
                    ],
                    callback: function (dcr) {
                        $timeout(function () {
                            showDCR(dcr);
                        }, 2000);
                    }
                };

                $rootScope.showSidePanel(options);
            }


            function loadDCRS() {
                $rootScope.showBusyIndicator()
                vm.loading = true;
                DCRService.getAllDCRs(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.dcrs = data;
                        vm.loading = false;
                        loadAttributeValues();
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
                angular.forEach(vm.dcrs.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                if (vm.selectedAttributes.length > 0) {
                    $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.dcrs.content);
                }
            }

            function nextPage() {
                if (vm.dcrs.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadDCRS();
                }
            }

            function previousPage() {
                if (vm.dcrs.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadDCRS();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadDCRS();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadDCRS();
                } else {
                    resetPage();
                }
            }

            $scope.statuses = [];
            function loadStatus() {
                $scope.statuses = [];
                WorkflowService.getObjectWorkflowStatus("DCR").then(
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
                loadDCRS();
            }

            $scope.clearStatus = clearStatus;
            function clearStatus() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedStatus = null;
                vm.filters.status = '';
                loadDCRS();
                $rootScope.hideBusyIndicator();
            }

            vm.selectedChangeReasonType = null;
            $scope.onSelectChangeReasonType = onSelectChangeReasonType;
            function onSelectChangeReasonType(changeReasonType) {
                vm.pageable.page = 0;
                $scope.selectedChangeReasonType = changeReasonType;
                vm.filters.changeReasonType = changeReasonType;
                loadDCRS();
            }

            $scope.clearChangeReasonType = clearChangeReasonType;
            function clearChangeReasonType() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedChangeReasonType = null;
                vm.filters.changeReasonType = '';
                loadDCRS();
                $rootScope.hideBusyIndicator();
            }


            vm.urgency = [];
            $scope.selectedUrgency = null;
            $scope.onSelectUrgency = onSelectUrgency;
            function onSelectUrgency(urgency) {
                vm.pageable.page = 0;
                $scope.selectedUrgency = urgency;
                vm.filters.urgency = urgency;
                loadDCRS();
            }

            $scope.clearUrgency = clearUrgency;
            function clearUrgency() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedUrgency = null;
                vm.filters.urgency = '';
                loadDCRS();
                $rootScope.hideBusyIndicator();
            }


            $scope.selectedPerson = null;
            $scope.onSelectChangeAnalyst = onSelectChangeAnalyst;
            function onSelectChangeAnalyst(person) {
                vm.pageable.page = 0;
                $scope.selectedPerson = person;
                vm.filters.changeAnalyst = person.id;
                loadDCRS();
            }

            $scope.clearChangeAnalyst = clearChangeAnalyst;
            function clearChangeAnalyst() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedPerson = null;
                vm.filters.changeAnalyst = '';
                loadDCRS();
                $rootScope.hideBusyIndicator();
            }


            $scope.selectedOriginator = null;
            $scope.onSelectOriginator = onSelectOriginator;
            function onSelectOriginator(person) {
                vm.pageable.page = 0;
                $scope.selectedOriginator = person;
                vm.filters.originator = person.id;
                loadDCRS();
            }

            $scope.clearOriginator = clearOriginator;
            function clearOriginator() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedOriginator = null;
                vm.filters.originator = '';
                loadDCRS();
                $rootScope.hideBusyIndicator();
            }


            $scope.selectedRequestedBy = null;
            $scope.onSelectRequestedBy = onSelectRequestedBy;
            function onSelectRequestedBy(person) {
                vm.pageable.page = 0;
                $scope.selectedRequestedBy = person;
                vm.filters.requestedBy = person.id;
                loadDCRS();
            }

            $scope.clearRequestedBy = clearRequestedBy;
            function clearRequestedBy() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedRequestedBy = null;
                vm.filters.requestedBy = '';
                loadDCRS();
                $rootScope.hideBusyIndicator();
            }


            // vm.clearTypeSelection = clearTypeSelection;
            // function clearTypeSelection() {
            //     $('.view-content').click();
            //     vm.pageable.page = 0;
            //     vm.selectedDcrType = null;
            //     vm.filters.crType = '';
            //     loadDCRS();
            // }

            // vm.onSelectType = onSelectType;
            // function onSelectType(crType) {
            //     vm.pageable.page = 0; 
            //     vm.selectedDcrType = crType;
            //     vm.filters.crType = crType.id;
            //    // vm.filters.freeTextSearch = false;
            //     loadDCRS();
            // }

            vm.clearTypeSelection = clearTypeSelection;
            function clearTypeSelection() {
                vm.pageable.page = 0;
                vm.selectedDcrType = null;
                vm.filters.crType = '';
                loadDCRS();
            }

            vm.onSelectType = onSelectType;
            function onSelectType(crType) {
                vm.pageable.page = 0;
                vm.selectedDcrType = crType;
                vm.filters.crType = crType.id;
                loadDCRS();
            }


            function resetPage() {
                vm.dcrs = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadDCRS();
            }

            function showDCR(dcr) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = dcr.id;
                vm.recentlyVisited.objectType = "DCR";
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.changes.dcr.details', {dcrId: dcr.id, tab: 'details.basic'});
                    }, function (error) {
                        $state.go('app.changes.dcr.details', {dcrId: dcr.id, tab: 'details.basic'});
                    }
                )
            }

            var deleteDCR = parsed.html($translate.instant("DELETE_DCR")).html();
            var DeleteDcrTitle = parsed.html($translate.instant("DELETE_DCR_TITLE_MSG")).html();
            var DCRDeleteMsg = parsed.html($translate.instant("DCR_DELETE_MSG")).html();

            function deleteDcr(dcr) {
                var options = {
                    title: deleteDCR,
                    message: DeleteDcrTitle + " [ " + dcr.crNumber + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        if (dcr.id != null && dcr.id != undefined) {
                            DCRService.deleteDCR(dcr.id).then(
                                function (data) {
                                    loadChangeTypeFilterObjects();
                                    loadStatus();
                                    var index = vm.dcrs.content.indexOf(dcr);
                                    vm.dcrs.content.splice(index, 1);
                                    vm.dcrs.totalElements--;
                                    $rootScope.showSuccessMessage(DCRDeleteMsg);
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
                        selectedObjectType: "DCRTYPE",
                        selectedObject: "DCR",
                        selectedParentObjectType: "CHANGE"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.quality.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("dcrAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                        loadDCRS();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem("dcrAttributes", JSON.stringify(vm.selectedAttributes));
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("dcrAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            vm.filterObjectTypes = null;
            $scope.originators = [];
            $scope.urgencyes = [];
            $scope.changeAnalysts = [];
            $scope.requestedBy = [];
            $scope.changeReasonTypes = [];
            function loadChangeTypeFilterObjects() {
                vm.filterObjectTypes = null;
                $scope.originators = [];
                $scope.urgencyes = [];
                $scope.changeAnalysts = [];
                $scope.requestedBy = [];
                $scope.changeReasonTypes = [];
                ECOService.getChangeTypeFilterObjects("DCR").then(
                    function (data) {
                        vm.filterObjectTypes = data;
                        $scope.originators = vm.filterObjectTypes.originators;
                        $scope.urgencyes = vm.filterObjectTypes.urgencies;
                        $scope.changeAnalysts = vm.filterObjectTypes.changeAnalysts;
                        $scope.requestedBy = vm.filterObjectTypes.requestedBys;
                        $scope.changeReasonTypes = vm.filterObjectTypes.changeReasonTypes;
                    }
                )
            }


            (function () {
                //if ($application.homeLoaded == true) {
                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                });
                $rootScope.getChangeAttributes("CHANGE", "DCRTYPE", vm.attributeIds);
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("dcrAttributes"));
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
                                $window.localStorage.setItem("dcrAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadDCRS();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadDCRS();
                    loadChangeTypeFilterObjects();
                    loadStatus();
                }
                //}
            })();
        }
    }
);


