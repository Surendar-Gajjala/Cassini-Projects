define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/assemblyLineService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllAssemblyLineController', AllAssemblyLineController);

        function AllAssemblyLineController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce,
                                           ObjectTypeAttributeService, CommonService, AssemblyLineService, RecentlyVisitedService, ItemService, ECOService, WorkflowDefinitionService,
                                           AttributeAttachmentService, MfrService, MfrPartsService, ProjectService, SpecificationsService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newAssemblyLine = newAssemblyLine;
            vm.assemblyLines = [];
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
                searchQuery: null,
                workOrder: ''
            };
            $scope.freeTextQuery = null;

            vm.assemblyLines = angular.copy(pagedResults);

            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            $scope.newAssemblyLineTitle = parsed.html($translate.instant("NEW_ASSEMBLYLINE")).html();

            function newAssemblyLine() {
                var options = {
                    title: $scope.newAssemblyLineTitle,
                    template: 'app/desktop/modules/mes/assemblyLine/new/newAssemblyLineView.jsp',
                    controller: 'NewAssemblyLineController as newAssemblyLineVm',
                    resolve: 'app/desktop/modules/mes/assemblyLine/new/newAssemblyLineController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.assemblyLine.new'}
                    ],
                    callback: function (assemblyLine) {
                        $timeout(function () {
                            loadAssemblyLine();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }


            function nextPage() {
                if (vm.assemblyLines.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadAssemblyLine();
                }
            }

            function previousPage() {
                if (vm.assemblyLines.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadAssemblyLine();
                }
            }

            $scope.assemblyLineFilePopover = {
                templateUrl: 'app/desktop/modules/mes/assemblyLine/all/assemblyLineFilePopoverTemplate.jsp'
            };

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadAssemblyLine();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadAssemblyLine();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.assemblyLines = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadAssemblyLine();
            }

            function loadAssemblyLine() {
                vm.loading = true;
                AssemblyLineService.getAllAssemblyLines(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.assemblyLines = data;
                        vm.loading = false;
                        CommonService.getPersonReferences(vm.assemblyLines.content, "modifiedBy");
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
                angular.forEach(vm.assemblyLines.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.assemblyLines.content);
            }

            vm.showAssemblyLine = showAssemblyLine;
            function showAssemblyLine(assemblyLine) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = assemblyLine.id;
                vm.recentlyVisited.objectType = "ASSEMBLYLINE";
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.mes.masterData.assemblyline.details', {
                            assemblyLineId: assemblyLine.id,
                            tab: 'details.basic'
                        });
                    }, function (error) {
                        $state.go('app.mes.masterData.assemblyline.details', {
                            assemblyLineId: assemblyLine.id,
                            tab: 'details.basic'
                        });
                    }
                )
            }

            var deleteAssemblyLineTitle = parsed.html($translate.instant("DELETE_ASSENBLYLINE")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var assemblyLineDeleteMsg = parsed.html($translate.instant("ASSEMBLYLINE_DELETE_MSG")).html();


            vm.deleteAssemblyLine = deleteAssemblyLine;
            function deleteAssemblyLine(assemblyLine) {
                var options = {
                    title: deleteAssemblyLineTitle,
                    message: deleteDialogMessage + " [ " + assemblyLine.number + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        if (assemblyLine.id != null && assemblyLine.id != undefined) {
                            AssemblyLineService.deleteAssemblyLine(assemblyLine.id).then(
                                function (data) {
                                    var index = vm.assemblyLines.content.indexOf(assemblyLine);
                                    vm.assemblyLines.content.splice(index, 1);
                                    vm.assemblyLines.totalElements--;
                                    $rootScope.showSuccessMessage(assemblyLineDeleteMsg);
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }
                });
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
                        type: "ASSEMBLYLINETYPE",
                        objectType: "ASSEMBLYLINE"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("assemblyLineAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadAssemblyLine();
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
                    JSON.parse($window.localStorage.getItem("assemblyLineAttributes"));
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
                    setAttributes = JSON.parse($window.localStorage.getItem("assemblyLineAttributes"));
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
                                $window.localStorage.setItem("assemblyLineAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadAssemblyLine();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadAssemblyLine();
                }
            })();

        }
    }
);