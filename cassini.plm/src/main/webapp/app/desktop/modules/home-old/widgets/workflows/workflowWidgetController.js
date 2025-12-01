define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/core/workflowService'
    ],
    function (module) {
        module.controller('WorkflowWidgetController', WorkflowWidgetController);

        function WorkflowWidgetController($scope, $stateParams, $state, $window, $rootScope, $translate, WorkflowService) {

            var vm = this;
            var session = JSON.parse(localStorage.getItem('local_storage_login'));
            $rootScope.loginPersonDetails = session.login;
            var parsed = angular.element("<div></div>");
            var login = $rootScope.loginPersonDetails;
            vm.loading = true;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.workflows = [];

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.pagedResults = {
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
            vm.workflows = angular.copy(vm.pagedResults);

            function nextPage() {
                vm.pageable.page++;
                loadWorkflows();
            }

            function previousPage() {
                vm.pageable.page--;
                loadWorkflows();
            }

            $rootScope.wfHistory = parsed.html($translate.instant("WORKFLOW_HISTORY")).html();
            var Items = parsed.html($translate.instant("ITEMS")).html();
            var Changes = parsed.html($translate.instant("ITEM_DETAILS_TAB_CHANGES")).html();
            var Projects = parsed.html($translate.instant("PROJECTS")).html();
            var Manufacturers = parsed.html($translate.instant("MANUFACTURERS_TITLE")).html();
            var ManufacturerParts = parsed.html($translate.instant("MANUFACTURER_PARTS")).html();
            var Specifications = parsed.html($translate.instant("SPECIFICATIONS")).html();
            var Requirements = parsed.html($translate.instant("REQUIREMENTS")).html();
            var ProjectActivities = parsed.html($translate.instant("PROJECT_ACTIVITIES")).html();

            vm.showWorkflowHistory = showWorkflowHistory;

            function showWorkflowHistory(object) {
                var options = {
                    title: $rootScope.wfHistory,
                    template: 'app/desktop/modules/home/widgets/workflows/workflowHistoryView.jsp',
                    controller: 'WorkflowHistoryController as workflowHistoryVm',
                    resolve: 'app/desktop/modules/home/widgets/workflows/workflowHistoryController',
                    width: 600,
                    data: {
                        selectedWorkflow: object,
                        selectedWorkflowObject: null
                    },
                    showMask: true,
                    callback: function (data) {

                    }
                };
                $rootScope.showSidePanel(options);
            }

            vm.showItemDetails = showItemDetails;
            function showItemDetails(item) {
                $state.go('app.items.details', {itemId: item.latestRevision});
            }

            vm.showEcoDetails = showEcoDetails;
            function showEcoDetails(eco) {
                $state.go('app.changes.eco.details', {ecoId: eco.id});
            }

            vm.showProjectDetails = showProjectDetails;
            function showProjectDetails(project) {
                $state.go('app.pm.project.details', {projectId: project.id});
            }

            vm.showSpecDetails = showSpecDetails;
            function showSpecDetails(spec) {
                $state.go('app.rm.specifications.details', {specId: spec.id});
            }

            vm.showReqDetails = showReqDetails;
            function showReqDetails(req) {
                $state.go('app.rm.requirements.details', {requirementId: req.id});
            }

            vm.showMfrDetails = showMfrDetails;
            function showMfrDetails(manufacturer) {
                $state.go('app.mfr.details', {manufacturerId: manufacturer.id});
            }

            vm.showMfrPartDetails = showMfrPartDetails;
            function showMfrPartDetails(mfrpart) {
                $state.go('app.mfr.mfrparts.details', {mfrId: mfrpart.manufacturer, manufacturePartId: mfrpart.id});
            }

            vm.showActivityDetails = showActivityDetails;
            function showActivityDetails(activity) {
                $state.go('app.pm.project.activity.details', {activityId: activity.id})
            }


            vm.active = 0;

            vm.tabs = {
                items: {
                    id: 'items',
                    heading: Items,
                    name: "ITEMS",
                    template: 'app/desktop/modules/home/widgets/workflows/itemsWorkflowsView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                changes: {
                    id: 'changes',
                    heading: Changes,
                    name: "CHANGES",
                    template: 'app/desktop/modules/home/widgets/workflows/changesWorkflowsView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                qualitys: {
                    id: 'qualitys',
                    heading: 'Quality',
                    name: "QUALITY",
                    template: 'app/desktop/modules/home/widgets/workflows/specificationWorkflowsView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                Manufacturers: {
                    id: 'Manufacturers',
                    heading: Manufacturers,
                    name: "MANUFACTURERS",
                    template: 'app/desktop/modules/home/widgets/workflows/manufacturerWorkflowView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                ManufacturerParts: {
                    id: 'ManufacturerParts',
                    heading: ManufacturerParts,
                    name: "MANUFACTURER PARTS",
                    template: 'app/desktop/modules/home/widgets/workflows/manufacturerPartsWorkflowView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                },
                projects: {
                    id: 'projects',
                    heading: Projects,
                    name: "PROJECTS",
                    template: 'app/desktop/modules/home/widgets/workflows/projectsWorkflowsView.jsp',
                    index: 5,
                    active: false,
                    activated: false
                },
                requirements: {
                    id: 'requirements',
                    heading: Requirements,
                    name: 'REQUIREMENTS',
                    template: 'app/desktop/modules/home/widgets/workflows/requirementWorkflowsView.jsp',
                    index: 6,
                    active: false,
                    activated: false
                }
            };

            vm.workflowWidTabActivated = workflowWidTabActivated;
            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            function workflowWidTabActivated(tabId) {
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    $scope.$broadcast('app.item.tabactivated', {tabId: tabId});
                }
                vm.workflows = [];
                loadWorkflows(tab.name);
                resizeWorkflowTabs();
                if (tab != null) {
                    activateTab(tab);
                }
            }

            function getTabById(tabId) {
                var tab = null;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t) && vm.tabs[t].id == tabId) {
                        tab = vm.tabs[t];
                    }
                }

                return tab;
            }

            function resizeWorkflowTabs() {
                var height1 = $(".widget-panel").outerHeight();
                $('.tab-content').height(height1 - 43);
                $('.tab-pane').height(height1 - 43);
            }

            vm.filters = {
                type: null
            };

            vm.workflows = [];
            function loadWorkflows(type) {
                vm.filters.type = type;
                vm.workflows = [];
                WorkflowService.getWorkflowByType(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.workflows = data;
                        $scope.$evalAsync();
                        vm.loading = false;
                    }
                )
            }

            function loadWorkflowCounts() {
                WorkflowService.getWorkflowTabCounts().then(
                    function (data) {
                        vm.workflowTabDetails = data;
                        var itemsTab = document.getElementById("Items");
                        var changesTab = document.getElementById("Changes");
                        var mfrsTab = document.getElementById("Manufacturers");
                        var mfrPartsTab = document.getElementById("ManufacturerParts");
                        var projectsTab = document.getElementById("Project");
                        var reqTab = document.getElementById("Requirements");
                        if (itemsTab != null) {
                            itemsTab.lastElementChild.innerHTML = vm.tabs.items.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.workflowTabDetails.items);
                        }
                        if (projectsTab != null) {
                            projectsTab.lastElementChild.innerHTML = vm.tabs.projects.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.workflowTabDetails.projects);
                        }
                        if (changesTab != null) {
                            changesTab.lastElementChild.innerHTML = vm.tabs.changes.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.workflowTabDetails.ecos);
                        }
                        if (mfrsTab != null) {
                            mfrsTab.lastElementChild.innerHTML = vm.tabs.Manufacturers.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.workflowTabDetails.manufacturers);
                        }
                        if (mfrPartsTab != null) {
                            mfrPartsTab.lastElementChild.innerHTML = vm.tabs.ManufacturerParts.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.workflowTabDetails.manufacturerParts);
                        }
                        if (reqTab != null) {
                            reqTab.lastElementChild.innerHTML = vm.tabs.requirements.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.workflowTabDetails.requirements);
                        }
                })
            }

            (function () {
                loadWorkflowCounts();
                loadWorkflows("ITEMS");
                $(window).resize(resizeWorkflowTabs);
            })();
        }
    }
)