define(['app/desktop/modules/stores/store.module',
        'app/shared/services/core/itemService',
        'app/shared/services/store/scrapItemService',
        'app/shared/services/pm/project/projectService'
    ],
    function (module) {
        module.controller('ScrapRequestItemsController', ScrapRequestItemsController);

        function ScrapRequestItemsController($scope, $rootScope, $timeout, $window, $state, $stateParams, ScrapItemService,
                                             ItemService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-shopping-cart";

            vm.back = back;
            vm.items = [];
            vm.updateScrap = updateScrap;

            function back() {
                $window.history.back();
            }

            function loadScrapItems() {
                ScrapItemService.getScrapItems($stateParams.scrapDetailsId).then(
                    function (data) {
                        vm.items = data;
                        $rootScope.scrapItems = data;
                        ItemService.getItemReferences(vm.items, 'item');
                    }
                );

            }

            function updateScrap(scrapItem) {
                ScrapItemService.updateScrapItem(scrapItem).then(
                    function (data) {
                        loadScrapItems();
                        $rootScope.showSuccessMessage("Scrap item updated successfully");
                    }
                )
            }

            function resize() {
                var height = $(window).height();
                var projectHeaderHeight = $("#project-headerbar").outerHeight();
                if (projectHeaderHeight != null) {
                    if ($application.selectedProject != undefined && $application.selectedProject.locked == true) {
                        $('#contentpanel').height(height - 297);
                    } else {
                        $('#contentpanel').height(height - 267);
                    }
                } else if (projectHeaderHeight == null) {
                    $('#contentpanel').height(height - 217);
                }
            }

            //vm.showItemDetails = showItemDetails;
            //function showItemDetails(item) {
            //    var options = {
            //        title: 'Scrap Item Details',
            //        showMask: true,
            //        template: 'app/desktop/modules/stores/details/tabs/itemBasicInfoView.jsp',
            //        controller: 'ItemBasicInfoController as itemBasicInfoVm',
            //        resolve: 'app/desktop/modules/stores/details/tabs/itemBasicInfoController',
            //        width: 600,
            //        data: {
            //            item: item
            //        },
            //        buttons: [
            //            {text: 'Update', broadcast: 'app.stores.receivedItem.info'}
            //        ],
            //        callback: function () {}
            //    };
            //
            //    $rootScope.showSidePanel(options);
            //}

            (function () {
                if ($application.homeLoaded == true) {
                    $scope.$on('app.store.scrapDetails', function (event, data) {
                        loadScrapItems();
                        resize();
                    });
                }
            })();
        }
    }
)
;