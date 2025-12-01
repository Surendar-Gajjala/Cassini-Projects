define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/varianceService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/desktop/modules/change/variance/details/varianceDetailsController',
        'app/shared/services/core/projectService',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/workflowService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllVarianceController', AllVarianceController);

        function AllVarianceController($scope, $rootScope, $translate, DialogService, VarianceService, $timeout, ItemService, ECOService, WorkflowDefinitionService, ObjectTypeAttributeService,
                                       MfrService, MfrPartsService, AttributeAttachmentService, CommonService, $state, $window, $stateParams, $cookies, $sce, ProjectService, SpecificationsService,
                                       RecentlyVisitedService, WorkflowService) {

            $rootScope.viewInfo.icon = "fa fa-calendar";
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var parsed = angular.element("<div></div>");
            var vm = this;
            vm.loading = false;
            vm.newVariance = newVariance;
            vm.freeTextSearch = freeTextSearch;
            vm.filterSearch = false;
            var currencyMap = new Hashtable();
            vm.RemoveColumnTitle = parsed.html($translate.instant("REMOVE_ATTRIBUTE_COLUMN")).html();
            vm.showAttributes = parsed.html($translate.instant("SHOW_ATTRIBUTES")).html();
            vm.selectedWaiverFor = ['ITEMS', 'MATERIALS'];
            vm.selectedDeviationFor = ['ITEMS', 'MATERIALS'];
            vm.selectedEffictivityType = ['DURATION', 'QUANTITY'];


            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.filters = {
                varianceType: null,
                description: null,
                varianceNumber: '',
                searchQuery: null,
                originator: '',
                status: null,
                varianceFor: '',
                effectivityType:''
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

            vm.variances = angular.copy(pagedResults);

            $scope.freeTextQuery = null;
            var varianceTitle = null;
            var createButton = parsed.html($translate.instant("CREATE")).html();
            vm.recurringItem = parsed.html($translate.instant("RECURRING_ITEM")).html();
            $scope.cannotDeleteReleasedVariance = parsed.html($translate.instant("CANNOT_DELETE_RELEASED_VARIANCE")).html();


            function newVariance() {
                if ($rootScope.varianceType == "Deviation") {
                    varianceTitle = parsed.html($translate.instant("NEW_DEVIATION")).html();
                } else if ($rootScope.varianceType == "Waiver") {
                    varianceTitle = parsed.html($translate.instant("NEW_WAIVER")).html();
                }
                var options = {
                    title: varianceTitle,
                    template: 'app/desktop/modules/change/variance/new/newVarianceView.jsp',
                    controller: 'NewVarianceController as newVarianceVm',
                    resolve: 'app/desktop/modules/change/variance/new/newVarianceController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createButton, broadcast: 'app.variance.new'}
                    ],
                    callback: function (variance) {
                        $timeout(function () {
                            showVariance(variance);
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var attributesTitle = $translate.instant("ATTRIBUTES");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var selectedAttributesMessage = $translate.instant("SELECTED_ATTRIBUTES_MESSAGE");
            vm.showTypeAttributes = showTypeAttributes;
            vm.removeAttribute = removeAttribute;
            vm.selectedAttributes = [];
            var objectType = '';
            var object = '';

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
                        selectedObjectType: objectType,
                        selectedObject: object,
                        selectedParentObjectType: "CHANGE"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.quality.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("varianceAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                        loadByType();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeAttribute(changeAttribute) {
                vm.selectedAttributes.remove(changeAttribute);
                $window.localStorage.setItem("varianceAttributes", JSON.stringify(vm.selectedAttributes));
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("changeAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            vm.showVariance = showVariance;
            function showVariance(variance) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = variance.id;
                vm.recentlyVisited.objectType = "VARIANCE";
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.changes.variance.details', {varianceId: variance.id, tab: 'details.basic'});
                    }, function (error) {
                        $state.go('app.changes.variance.details', {varianceId: variance.id, tab: 'details.basic'});
                    }
                )
            }

            vm.resetPage = resetPage;
            function resetPage() {
                vm.variances = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                vm.filterSearch = false;
                $rootScope.showBusyIndicator($('.view-container'));
                loadByType();
            }

            vm.nextPage = nextPage;
            function nextPage() {
                if (vm.variances.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    loadByType();
                }
            }

            vm.previousPage = previousPage;
            function previousPage() {
                if (vm.variances.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    loadByType();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadByType();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    vm.filterSearch = true;
                    loadByType();
                } else {
                    resetPage();
                }
            }

            var deleteVarianceTitle = parsed.html($translate.instant("DELETE_VARIANCE_TITLE")).html();
            var deleteVarianceDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var varianceDeletedMessage = parsed.html($translate.instant("VARIANCE_DELETED")).html();
            vm.deleteVariance = deleteVariance;
            function deleteVariance(variance) {
                var options = {
                    title: deleteVarianceTitle,
                    message: deleteVarianceDialogMessage + " [ " + variance.title + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        VarianceService.deleteVariance(variance.id).then(
                            function (data) {
                                loadByType();
                                $rootScope.showSuccessMessage(varianceDeletedMessage);
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                });
            }

            var EditVariance = null;
            var Update = parsed.html($translate.instant("UPDATE")).html();
            vm.editVariance = editVariance;
            function editVariance(variance) {
                if ($rootScope.varianceType == "Deviation") {
                    EditVariance = parsed.html($translate.instant("EDIT_DEVIATION")).html();
                } else if ($rootScope.varianceType == "Waiver") {
                    EditVariance = parsed.html($translate.instant("EDIT_WAIVER")).html();
                }
                var options = {
                    title: EditVariance,
                    showMask: true,
                    template: 'app/desktop/modules/change/variance/new/editVarianceView.jsp',
                    controller: 'EditVarianceController as editVarianceVm',
                    resolve: 'app/desktop/modules/change/variance/new/editVarianceController',
                    width: 550,
                    data: {
                        varianceId: variance.id
                    },
                    buttons: [
                        {text: Update, broadcast: 'app.variance.edit'}
                    ],
                    callback: function () {
                        loadByType();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadDeviation() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                VarianceService.getVarianceByType(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.variances = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadWaiver() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                VarianceService.getVarianceByType(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.variances = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $scope.loadOriginators = [];
            function loadOriginators(){
                VarianceService.getOriginator('WAIVER').then(
                    function (data) {
                        $scope.originators = data;
                    }
                )
            }

            $scope.loadDeviationOriginators = [];
            function loadDeviationOriginators(){
                VarianceService.getOriginator('DEVIATION').then(
                    function (data) {
                        $scope.originators = data;
                    }
                )
            }

            $scope.selectedPerson = null;
            $scope.onSelectOriginator = onSelectOriginator;
            function onSelectOriginator(person) {
                vm.pageable.page = 0;
                $scope.selectedPerson = person;
                vm.filters.originator = person.id;
                loadWaiver();
            }

            $scope.onSelectDeviationOriginator = onSelectDeviationOriginator;
            function onSelectDeviationOriginator(person) {
                vm.pageable.page = 0;
                $scope.selectedPerson = person;
                vm.filters.originator = person.id;
                loadDeviation();
            }

            $scope.clearOriginator = clearOriginator;
            function clearOriginator() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedPerson = null;
                vm.filters.originator = '';
                loadWaiver();
                $rootScope.hideBusyIndicator();
            }

            $scope.clearDeviationOriginator = clearDeviationOriginator;
            function clearDeviationOriginator() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedPerson = null;
                vm.filters.originator = '';
                loadDeviation();
                $rootScope.hideBusyIndicator();
            }

            $scope.loadStatus = [];
            function loadStatus(){
                WorkflowService.getObjectWorkflowStatus(vm.filters.varianceType).then(
                    function (data) {
                        $scope.statuses = data;
                    }
                )
            }

            $scope.selectedStatus = null;
            $scope.onSelectStatus = onSelectStatus;
            function onSelectStatus(status) {
                $scope.selectedStatus = status;
                vm.filters.status = status;
                loadWaiver();
            }

            $scope.onSelectDeviationStatus = onSelectDeviationStatus;
            function onSelectDeviationStatus(status) {
                $scope.selectedStatus = status;
                vm.filters.status = status;
                loadDeviation();
            }

            $scope.clearStatus = clearStatus;
            function clearStatus() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedStatus = null;
                vm.filters.status = '';
                loadWaiver();
                $rootScope.hideBusyIndicator();
            }

            $scope.clearDeviationStatus = clearDeviationStatus;
            function clearDeviationStatus() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedStatus = null;
                vm.filters.status = '';
                loadDeviation();
                $rootScope.hideBusyIndicator();
            }


            $scope.selectedWaiverFor= null;
            $scope.onSelectWaiverFor = onSelectWaiverFor;
            function onSelectWaiverFor(waiver) {
                vm.pageable.page = 0;
                $scope.selectedWaiverFor = waiver;
                vm.filters.varianceFor = waiver;
                loadWaiver();
            }

            $scope.selectedEffictivityType= null;
            $scope.onSelectEffictivityType = onSelectEffictivityType;
            function onSelectEffictivityType(deviation) {
                vm.pageable.page = 0;
                $scope.selectedEffictivityType = deviation;
                vm.filters.effectivityType = deviation;
                loadDeviation();
            }

            $scope.selectedDeviationFor= null;
            $scope.onSelectDeviationFor = onSelectDeviationFor;
            function onSelectDeviationFor(deviation) {
                vm.pageable.page = 0;
                $scope.selectedDeviationFor = deviation;
                vm.filters.varianceFor = deviation;
                loadDeviation();
            }

            $scope.clearWaiver = clearWaiver;
            function clearWaiver() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedWaiverFor = null;
                vm.filters.varianceFor = '';
                loadWaiver();
                $rootScope.hideBusyIndicator();
            }

            $scope.clearEffictivity = clearEffictivity;
            function clearEffictivity() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedEffictivityType = null;
                vm.filters.effectivityType = '';
                loadDeviation();
                $rootScope.hideBusyIndicator();
            }

            $scope.clearDeviation = clearDeviation;
            function clearDeviation() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedDeviationFor = null;
                vm.filters.varianceFor = '';
                loadDeviation();
                $rootScope.hideBusyIndicator();
            }

            function loadByType() {
                if ($rootScope.varianceType == "Deviation") {
                    vm.filters.varianceType = 'DEVIATION';
                    loadDeviation();
                    loadDeviationOriginators();
                    loadStatus();
                }
                if ($rootScope.varianceType == "Waiver") {
                    vm.filters.varianceType = 'WAIVER';
                    loadWaiver();
                    loadOriginators();
                    loadStatus();
                }
                loadAttributeValues();
            }

            vm.objectIds = [];
            vm.attributeIds = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                if ($rootScope.varianceType == "Deviation") {
                    objectType = 'DEVIATIONTYPE';
                    object = 'DEVIATION';
                }
                if ($rootScope.varianceType == "Waiver") {
                    objectType = 'WAIVERTYPE';
                    object = 'WAIVER';
                }
                angular.forEach(vm.variances.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                $rootScope.getChangeAttributes('CHANGE', objectType, vm.attributeIds);
                if (vm.selectedAttributes.length > 0) {
                    $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.variances.content);
                }
            }


            $rootScope.$watch('varianceType', function () {
                vm.searchText = null;
                vm.filterSearch = false;
                vm.filters.searchQuery = null;
                loadByType();
            });

            (function () {
                if ($rootScope.varianceType == "Deviation") {
                    objectType = 'DEVIATIONTYPE';
                    object = 'DEVIATION';
                }
                if ($rootScope.varianceType == "Waiver") {
                    objectType = 'WAIVERTYPE';
                    object = 'WAIVER';
                }
                $timeout(function () {
                    $rootScope.getChangeAttributes('CHANGE', objectType, vm.attributeIds);
                }, 500);

                $scope.$on('$viewContentLoaded', function () {
                    if ($stateParams.varianceMode != null && $stateParams.varianceMode != "" && $stateParams.varianceMode != undefined) {
                        if ($stateParams.varianceMode == "deviation") {
                            $rootScope.varianceType = "Deviation";
                        }
                        if ($stateParams.varianceMode == "waiver") {
                            $rootScope.varianceType = "Waiver";
                        }
                    }
                    angular.forEach($application.currencies, function (data) {
                        currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                    })
                    var setAttributes = null;
                    if (validateJSON()) {
                        setAttributes = JSON.parse($window.localStorage.getItem("changeAttributes"));
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
                                    $window.localStorage.setItem("varianceAttributes", "");
                                    vm.selectedAttributes = setAttributes
                                } else {
                                    vm.selectedAttributes = setAttributes;
                                }
                                loadByType();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    } else {
                        loadByType();
                    }
                });
            })();
        }
    }
);


