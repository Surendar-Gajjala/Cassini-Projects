/**
 * Created by SRAVAN on 8/20/2019.
 */
define(
    [
        'app/desktop/modules/rm/requirements/requirement.module',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('RequirementsDeliverablesController', RequirementsDeliverablesController);

        function RequirementsDeliverablesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $translate,
                                                    CommonService, DialogService, SpecificationsService, ItemService) {
            var vm = this;
            var parsed = angular.element("<div></div>");

            vm.loading = true;
            vm.loading = true;
            var reqId = $stateParams.requirementId;

            var addButton = parsed.html($translate.instant("ADD")).html();
            var deliverable = parsed.html($translate.instant("ADD_DELIVERABLE")).html();
            var deleteMessage = parsed.html($translate.instant("DELETE_DELIVERABLE_SUCCESS")).html();
            var deleteDialogueMsg = parsed.html($translate.instant("DELETE_DELIVERABLE_VALIDATE")).html();
            var deleteDialogueTitle = parsed.html($translate.instant("DELETE_DELIVERABLE")).html();
            var fileDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            vm.addItems = parsed.html($translate.instant("ADD_ITEM")).html();

            $rootScope.showDeliverables = showDeliverables;

            function showDeliverables() {
                var options = {
                    title: deliverable,
                    template: 'app/desktop/modules/rm/requirements/details/tabs/deliverables/requirementsItemDeliverableView.jsp',
                    controller: 'RequirementsItemDeliverablesController as requirementsItemDeliverableVm',
                    resolve: 'app/desktop/modules/rm/requirements/details/tabs/deliverables/requirementsItemDeliverableController',
                    width: 800,
                    showMask: true,
                    buttons: [
                        {text: addButton, broadcast: 'app.requirement.items.new'}
                    ],
                    callback: function (data) {
                        loadRequirementDeliverable();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadRequirementDeliverable() {
                vm.requirementDeliverables = [];
                SpecificationsService.getRequirementDeliverableItems(reqId).then(
                    function (data) {
                        vm.requirementDeliverables = data;
                        ItemService.getLatestRevisionReferences(vm.requirementDeliverables, 'latestRevision');
                        vm.loading = false;
                        $rootScope.loadRequirementCounts();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.showItem = showItem;
            function showItem(item) {
                $window.localStorage.setItem("lastSelectedTab", JSON.stringify('details.deliverables'));
                $state.go('app.items.details', {itemId: item.latestRevision});
            }

            vm.deleteDeliverable = deleteDeliverable;
            function deleteDeliverable(item) {
                var options = {
                    title: deleteDialogueTitle,
                    message: deleteDialogueMsg + " [ " + item.itemName + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        SpecificationsService.deleteDeliverable(reqId, item.latestRevision).then(
                            function (data) {
                                var index = vm.requirementDeliverables.indexOf(item);
                                vm.requirementDeliverables.slice(index, 1);
                                $rootScope.showSuccessMessage(deleteMessage);
                                loadRequirementDeliverable();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            (function () {
                $scope.$on('app.requirement.tabActivated', function (event, data) {
                    if (data.tabId == 'details.deliverables') {
                        loadRequirementDeliverable();

                    }

                });
            })();
        }

    });

