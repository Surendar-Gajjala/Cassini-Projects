define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/toolService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/specificationsService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllToolController', AllToolController);

        function AllToolController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce, ToolService, ObjectTypeAttributeService,
                                   ItemService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService, AttributeAttachmentService, CommonService, ProjectService, SpecificationsService,
                                   RecentlyVisitedService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newTool = newTool;
            vm.tools = [];
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
                type: null,
                name: null,
                searchQuery: null,
                workOrder: ''
            };
            $scope.freeTextQuery = null;

            vm.tools = angular.copy(pagedResults);
            vm.tools = [];

            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newToolHeading = parsed.html($translate.instant("NEW_TOOL")).html();

            function newTool() {
                var options = {
                    title: newToolHeading,
                    template: 'app/desktop/modules/mes/tool/new/newToolView.jsp',
                    controller: 'NewToolController as newToolVm',
                    resolve: 'app/desktop/modules/mes/tool/new/newToolController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.tool.new'}
                    ],
                    callback: function (tool) {
                        $timeout(function () {
                            loadTools();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var deleteToolTitle = parsed.html($translate.instant("DELETE_TOOL_TITLE")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var toolDeletedMessage = parsed.html($translate.instant("TOOL_DELETED")).html();
            vm.deleteTool = deleteTool;
            function deleteTool(tool) {
                var options = {
                    title: deleteToolTitle,
                    message: deleteDialogMessage + " [ " + tool.name + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ToolService.deleteTool(tool.id).then(
                            function (data) {
                                loadTools();
                                $rootScope.showSuccessMessage(toolDeletedMessage);
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                });
            }

            $scope.operationFilePopover = {
                templateUrl: 'app/desktop/modules/mes/tool/all/toolFilePopoverTemplate.jsp'
            };

            function nextPage() {
                if (vm.tools.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadTools();
                }
            }

            function previousPage() {
                if (vm.tools.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadTools();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadTools();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadTools();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.tools = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadTools();
            }

            function loadTools() {
                vm.loading = true;
                ToolService.getAllTools(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.tools = data;
                        angular.forEach(vm.tools.content, function (tool) {
                            tool.modifiedDatede = null;
                            if (tool.modifiedDate != null) {
                                tool.modifiedDatede = moment(tool.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                            }
                            if (tool.image != null) {
                                tool.imagePath = "api/mes/tools/" + tool.id + "/image/download?" + new Date().getTime();
                            }
                        });
                        loadAttributeValues();
                        CommonService.getPersonReferences(vm.tools.content, 'modifiedBy');
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;
                    }, function (error) {
                        vm.loading = false;
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
                angular.forEach(vm.tools.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.tools.content);

            }

            vm.showTool = showTool;
            function showTool(tool) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = tool.id;
                vm.recentlyVisited.objectType = tool.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.mes.masterData.tool.details', {toolId: tool.id, tab: 'details.basic'});
                    }, function (error) {
                        $state.go('app.mes.masterData.tool.details', {toolId: tool.id, tab: 'details.basic'});
                    }
                )
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
                        type: "TOOLTYPE",
                        objectType: "TOOL"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("toolAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadTools();
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
                    JSON.parse($window.localStorage.getItem("toolAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            vm.showImage = showImage;
            function showImage(machine) {
                var modal = document.getElementById('item-thumbnail' + machine.id);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close" + machine.id);
                $("#thumbnail-image" + machine.id).width($('#thumbnail-view' + machine.id).outerWidth());
                $("#thumbnail-image" + machine.id).height($('#thumbnail-view' + machine.id).outerHeight());

                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }


            (function () {
                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                })
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("toolAttributes"));
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
                                $window.localStorage.setItem("toolAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadTools();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadTools();
                }
            })();

        }
    }
);