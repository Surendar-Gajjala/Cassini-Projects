define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/bopService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService'
    ],
    function (module) {
        module.controller('BOPPlanResourcesController', BOPPlanResourcesController);

        function BOPPlanResourcesController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal,
                                            httpFactory, BOPService, AutonumberService, DialogService) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            var removeResourceTitle = parsed.html($translate.instant("REMOVE_RESOURCE_DIALOG")).html();
            var removeResourceDialogMsg = parsed.html($translate.instant("REMOVE_RESOURCE_DIALOG_MESSAGE")).html();
            var resourceRemovedMsg = parsed.html($translate.instant("BOP_PLAN_RESOURCE_REMOVED_MSG")).html();
            var resourceCreatedMsg = parsed.html($translate.instant("BOP_PLAN_RESOURCE_CREATE_MSG")).html();
            var resourceUpdatedMsg = parsed.html($translate.instant("BOP_PLAN_RESOURCE_UPDATE_MSG")).html();
            vm.bopPlanId = $stateParams.bopPlanId;
            vm.bopId = $stateParams.bopId;

            var emptyPlan = {
                id: null,
                bopOperation: vm.bopPlanId,
                operation: null,
                type: null,
                resourceType: null,
                resource: null,
                notes: null
            };

            vm.addedOperations = [];
            vm.addResources = addResources;
            function addResources() {
                var options = {
                    title: "Select Resources",
                    template: 'app/desktop/modules/mes/bop/planDetails/tabs/resources/resourcesSelectionView.jsp',
                    controller: 'ResourcesSelectionController as resourcesSelectionVm',
                    resolve: 'app/desktop/modules/mes/bop/planDetails/tabs/resources/resourcesSelectionController',
                    width: 750,
                    showMask: true,
                    buttons: [
                        {text: 'Add', broadcast: 'app.select.bop.plan.resources'}
                    ],
                    callback: function (result) {
                        loadResources();
                        $rootScope.loadBOPPlanTabCounts();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadResources() {
                vm.loading = true;
                BOPService.getBopPlanResources(vm.bopPlanId).then(
                    function (data) {
                        vm.bopPlanResources = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.editResource = editResource;
            function editResource(resource) {
                resource.oldNotes = resource.notes;
                resource.editMode = true;
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(resource) {
                resource.notes = resource.oldNotes;
                resource.editMode = false;
            }

            vm.updateResource = updateResource;
            function updateResource(resource) {
                $rootScope.showBusyIndicator($('.view-container'));
                BOPService.updateBopPlanResource(vm.bopPlanId, resource).then(
                    function (data) {
                        resource.id = data.id;
                        resource.editMode = false;
                        $rootScope.showSuccessMessage(resourceUpdatedMsg);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.removeResource = removeResource;
            function removeResource(resource) {
                var options = {
                    title: removeResourceTitle,
                    message: removeResourceDialogMsg.format(resource.number),
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        BOPService.deleteBopPlanResource(vm.bopPlanId, resource.id).then(
                            function (data) {
                                loadResources();
                                $rootScope.loadBOPPlanTabCounts();
                                $rootScope.showSuccessMessage(resourceRemovedMsg);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            (function () {
                $scope.$on('app.bop.plan.tabActivated', function (event, args) {
                    if (args.tabId == 'details.resources') {
                        loadResources();
                    }
                });
            })();
        }
    }
)
;



