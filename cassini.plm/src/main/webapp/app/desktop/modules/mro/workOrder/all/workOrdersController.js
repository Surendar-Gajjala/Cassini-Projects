define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/shared/services/core/workOrderService',
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
        module.controller('WorkOrdersController', WorkOrdersController);

        function WorkOrdersController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce, WorkOrderService, ObjectTypeAttributeService,
                                      ItemService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService, AttributeAttachmentService, CommonService, ProjectService, SpecificationsService,
                                      RecentlyVisitedService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newWorkOrder = newWorkOrder;
            vm.deleteWorkOrder = deleteWorkOrder;
            vm.workOrder = [];
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
                searchQuery: null
            };
            $scope.freeTextQuery = null;

            vm.workOrders = angular.copy(pagedResults);


            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newWorkOrderHeading = parsed.html($translate.instant("NEW_WORK_ORDER_TYPE")).html();
            $scope.cannotDeleteFinishedWorkOrder = parsed.html($translate.instant("CANNOT_DELETE_FINISHED_WORK_ORDER")).html();

            function newWorkOrder() {
                var options = {
                    title: newWorkOrderHeading,
                    template: 'app/desktop/modules/mro/workOrder/new/newWorkOrderView.jsp',
                    controller: 'NewWorkOrderController as newWorkOrderVm',
                    resolve: 'app/desktop/modules/mro/workOrder/new/newWorkOrderController',
                    width: 700,
                    showMask: true,
                    data: {
                        workOrderMode: "WORKORDER"
                    },
                    buttons: [
                        {text: create, broadcast: 'app.workOrder.new'}
                    ],
                    callback: function (workOrder) {
                        $timeout(function () {
                            loadWorkOrders();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function nextPage() {
                if (vm.workOrders.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadWorkOrders();
                }
            }

            function previousPage() {
                if (vm.workOrders.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadWorkOrders();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadWorkOrders();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadWorkOrders();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.workOrders = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadWorkOrders();
            }

            function loadWorkOrders() {
                vm.workOrders = [];
                vm.loading = true;
                WorkOrderService.getAllWorkOrders(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.workOrders = data;
                        CommonService.getPersonReferences(vm.workOrders.content, 'modifiedBy');
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
                angular.forEach(vm.workOrders.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.workOrders.content);

            }


            vm.showWorkOrder = showWorkOrder;
            function showWorkOrder(workOrder) {
                $state.go('app.mro.workOrder.details', {workOrderId: workOrder.id, tab: 'details.basic'});
                /* vm.recentlyVisited = {};
                 vm.recentlyVisited.objectId = workOrder.id;
                 vm.recentlyVisited.objectType = workOrder.objectType;
                 vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                 RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                 function (data) {
                 $state.go('app.mes.workOrder.details', {workOrderId: workOrder.id, tab: 'details.basic'});
                 }, function (error) {
                 $state.go('app.mes.workOrder.details', {workOrderId: workOrder.id, tab: 'details.basic'});
                 }
                 )*/
            }

            var deleteWorkOrderTitle = parsed.html($translate.instant("DELETE_WORK_ORDER")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var workOrderDeleteMsg = parsed.html($translate.instant("WORK_ORDER_DELETE_MSG")).html();


            function deleteWorkOrder(workOrder) {
                var options = {
                    title: deleteWorkOrderTitle,
                    message: deleteDialogMessage + " [ " + workOrder.number + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        if (workOrder.id != null && workOrder.id != undefined) {
                            WorkOrderService.deleteWorkOrder(workOrder.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(workOrderDeleteMsg);
                                    loadWorkOrders();
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
                        type: "WORKORDERTYPE",
                        objectType: "MROWORKORDER"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("workOrderAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadWorkOrders();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
            }

            vm.showMaintenancePlan = showMaintenancePlan;
            function showMaintenancePlan(workOrder) {
                $state.go('app.mro.maintenancePlan.details', {
                    maintenancePlanId: workOrder.plan,
                    tab: 'details.basic'
                });
            }

            vm.showWorkRequest = showWorkRequest;
            function showWorkRequest(workOrder) {
                $state.go('app.mro.workRequest.details', {workRequestId: workOrder.request, tab: 'details.basic'});
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("workOrderAttributes"));
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
                    setAttributes = JSON.parse($window.localStorage.getItem("workOrderAttributes"));
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
                                $window.localStorage.setItem("workOrderAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadWorkOrders();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadWorkOrders();
                }
            })();

        }
    }
);