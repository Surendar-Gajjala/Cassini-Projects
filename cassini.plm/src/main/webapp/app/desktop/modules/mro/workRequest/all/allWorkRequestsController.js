define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/shared/services/core/workRequestService',
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
        module.controller('AllWorkRequestsController', AllWorkRequestsController);

        function AllWorkRequestsController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce, WorkRequestService, ObjectTypeAttributeService,
                                           ItemService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService, AttributeAttachmentService, CommonService, ProjectService, SpecificationsService,
                                           RecentlyVisitedService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newWorkRequest = newWorkRequest;
            vm.deleteWorkRequest = deleteWorkRequest;
            vm.WorkRequest = [];
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
                number: null,
                type: '',
                name: null,
                description: null,
                searchQuery: null
            };
            $scope.freeTextQuery = null;

            vm.workRequests = angular.copy(pagedResults);

            function loadWorkRequests() {
                vm.loading = true;
                WorkRequestService.getAllWorkRequests(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.workRequests = data;
                        vm.loading = false;
                        loadAttributeValues();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newWorkRequestHeading = parsed.html($translate.instant("NEW_WORK_REQUEST_TYPE")).html();
            $scope.cannotDeleteFinishedWorkRequest = parsed.html($translate.instant("CANNOT_DELETE_FINISHED_WORK_REQUEST")).html();

            function newWorkRequest() {
                var options = {
                    title: newWorkRequestHeading,
                    template: 'app/desktop/modules/mro/workRequest/new/newWorkRequestView.jsp',
                    controller: 'NewWorkRequestController as newWorkRequestVm',
                    resolve: 'app/desktop/modules/mro/workRequest/new/newWorkRequestController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.workRequest.new'}
                    ],
                    callback: function (workRequest) {
                        $timeout(function () {
                            loadWorkRequests();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function nextPage() {
                if (vm.workRequests.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadWorkRequests();
                }
            }

            function previousPage() {
                if (vm.workRequests.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadWorkRequests();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadWorkRequests();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadWorkRequests();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.workRequests = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadWorkRequests();
            }


            vm.objectIds = [];
            vm.selectedAttributes = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.workRequests.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.workRequests.content);
               
            }


            vm.showWorkRequest = showWorkRequest;
            function showWorkRequest(workRequest) {
                $state.go('app.mro.workRequest.details', {workRequestId: workRequest.id, tab: 'details.basic'});
                /* vm.recentlyVisited = {};
                 vm.recentlyVisited.objectId = workRequest.id;
                 vm.recentlyVisited.objectType = workRequest.objectType;
                 vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                 RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                 function (data) {
                 $state.go('app.mes.workRequest.details', {workRequestId: workRequest.id, tab: 'details.basic'});
                 }, function (error) {
                 $state.go('app.mes.workRequest.details', {workRequestId: workRequest.id, tab: 'details.basic'});
                 }
                 )*/
            }

            var deleteWorkRequestTitle = parsed.html($translate.instant("DELETE_WORK_REQUEST")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var workRequestDeleteMsg = parsed.html($translate.instant("WORK_REQUEST_DELETE_MSG")).html();


            function deleteWorkRequest(workRequest) {
                var options = {
                    title: deleteWorkRequestTitle,
                    message: deleteDialogMessage + " [ " + workRequest.number + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        if (workRequest.id != null && workRequest.id != undefined) {
                            WorkRequestService.deleteWorkRequest(workRequest.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(workRequestDeleteMsg);
                                    loadWorkRequests();
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
                        type: "WORKREQUESTTYPE",
                        objectType: "MROWORKREQUEST"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("workRequestAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadWorkRequests();
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
                    JSON.parse($window.localStorage.getItem("workRequestAttributes"));
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
                    setAttributes = JSON.parse($window.localStorage.getItem("workRequestAttributes"));
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
                                $window.localStorage.setItem("workRequestAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadWorkRequests();
                        }, function (error) {
                              $rootScope.showErrorMessage(error.message);
                              $rootScope.hideBusyIndicator();
                         }
                    )
                } else {
                    loadWorkRequests();
                }
            })();

        }
    }
);