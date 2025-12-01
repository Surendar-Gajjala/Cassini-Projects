define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/shared/services/core/workCenterService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllWorkCenterController', AllWorkCenterController);

        function AllWorkCenterController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce,
                                         WorkCenterService, CommonService, RecentlyVisitedService, ObjectTypeAttributeService, ItemService,
                                         ECOService, WorkflowDefinitionService, MfrService, MfrPartsService, AttributeAttachmentService,
                                         ProjectService, SpecificationsService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newWorkCenter = newWorkCenter;
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
                type: '',
                assemblyLine: false
            };
            $scope.freeTextQuery = null;

            vm.workCenters = angular.copy(pagedResults);

            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newWorkCenterHeading = parsed.html($translate.instant("NEW_WORKCENTER")).html();
            var workCenterDeleted = parsed.html($translate.instant("WORKCENTER_DELETED")).html();
            var deleteWorkCenterTitle = parsed.html($translate.instant("DELETE_WORKCENTER")).html();
            var deleteWorkCenterMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            $scope.noWorkCenters = parsed.html($translate.instant("NO_WORK_CENTER")).html();

            function newWorkCenter() {
                var options = {
                    title: newWorkCenterHeading,
                    template: 'app/desktop/modules/mes/workCenter/new/newWorkCenterView.jsp',
                    controller: 'NewWorkCenterController as newWorkCenterVm',
                    resolve: 'app/desktop/modules/mes/workCenter/new/newWorkCenterController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.workCenter.new'}
                    ],
                    callback: function (workCenter) {
                        $timeout(function () {
                            loadWorkCenters();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            $scope.workCenterFilePopover = {
                templateUrl: 'app/desktop/modules/mes/workCenter/all/workcenterFilePopoverTemplate.jsp'
            };


            function nextPage() {
                if (vm.workCenters.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    loadWorkCenters();
                }
            }

            function previousPage() {
                if (vm.workCenters.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    loadWorkCenters();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadWorkCenters();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadWorkCenters();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.workCenters = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadWorkCenters();
            }

            function loadWorkCenters() {
                vm.loading = true;
                WorkCenterService.getAllWorkCenters(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.workCenters = data;
                        CommonService.getPersonReferences(vm.workCenters.content, 'modifiedBy');
                        vm.loading = false;
                        loadAttributeValues();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.objectIds = [];
            vm.attributeIds = [];
            vm.selectedAttributes = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.workCenters.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.workCenters.content);

            }

            vm.showWorkCenter = showWorkCenter;
            function showWorkCenter(workCenter) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = workCenter.id;
                vm.recentlyVisited.objectType = workCenter.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.mes.masterData.workcenter.details', {
                            workcenterId: workCenter.id,
                            tab: 'details.basic'
                        });
                    }, function (error) {
                        $state.go('app.mes.masterData.workcenter.details', {
                            workcenterId: workCenter.id,
                            tab: 'details.basic'
                        });
                    }
                )
            }

            vm.deleteWorkCenter = deleteWorkCenter;
            function deleteWorkCenter(workCenter) {
                var options = {
                    title: deleteWorkCenterTitle,
                    message: deleteWorkCenterMessage + " [" + workCenter.name + "] ?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        WorkCenterService.deleteWorkCenter(workCenter.id).then(
                            function (data) {
                                loadWorkCenters();
                                $rootScope.hideBusyIndicator();
                                $rootScope.showSuccessMessage(workCenterDeleted);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            var currencyMap = new Hashtable();
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
                        type: "WORKCENTERTYPE",
                        objectType: "WORKCENTER"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("workCenterAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadWorkCenters();
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
                    JSON.parse($window.localStorage.getItem("workCenterAttributes"));
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
                    setAttributes = JSON.parse($window.localStorage.getItem("workCenterAttributes"));
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
                                $window.localStorage.setItem("workCenterAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadWorkCenters();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadWorkCenters();
                }
            })();

        }
    }
);