define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('LatestRevUpdateItemsController', LatestRevUpdateItemsController);

        function LatestRevUpdateItemsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                                ECOService, ItemService) {
            var vm = this;

            vm.items = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.showItem = showItem;
            vm.loading = true;

            var pageable = {
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
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.items = angular.copy(vm.pagedResults);

            function nextPage() {
                vm.pageable.page++;
                loadItems();
            }

            function previousPage() {
                vm.pageable.page--;
                loadItems();
            }

            function showItem(item) {
                //  vm.compareTitle = item.itemName + " (" + item.itemNumber + " Rev -" + item.fromRevision + " To - " + fromRev.toRevision + ")";

                $rootScope.seletedItemId = item.id;
                $state.go('app.items.details', {itemId: item.id});
            }

            function loadItems() {
                vm.clear = false;
                vm.loading = true;
                ECOService.getLatestReleasedRevisions(pageable).then(
                    function (data) {
                        vm.items = data;
                        ItemService.getItemReferences(vm.items.content, 'itemMaster');
                        vm.loading = false;
                    })
            }

            (function () {
                loadItems();
            })();
        }

    });
