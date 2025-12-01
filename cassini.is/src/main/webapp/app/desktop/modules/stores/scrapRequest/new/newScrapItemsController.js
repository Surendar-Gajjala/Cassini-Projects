/**
 * Created by swapna on 13/09/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/store/topStockIssuedService',
        'app/shared/services/store/scrapItemService'

    ],
    function (module) {
        module.controller('NewScrapItemsController', NewScrapItemsController);

        function NewScrapItemsController($scope, $rootScope, $window, $state, $stateParams, ItemService,
                                         AttributeAttachmentService, ObjectAttributeService, TopStockIssuedService, $q, ScrapItemService, $timeout) {
            var vm = this;

            vm.itemList = [];
            vm.newScrap = $scope.data.newScrap;
            var project = vm.newScrap.project;
            var projectId = 0;
            var addedItemsMap = $scope.data.addedItemsMap;
            vm.newScrap.scrapItems = [];
            var searchQuery = null;
            $scope.freeTextQuery = null;
            vm.addToScrapItems = addToScrapItems;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            var pageable = {
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
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            vm.itemList = pagedResults;

            function addToScrapItems(scrapitem) {
                if (!scrapitem.isAdded) {
                    scrapitem.isNew = true;
                    scrapitem.editMode = true;
                    var index = vm.itemList.content.indexOf(scrapitem);
                    vm.itemList.content.splice(index, 1);
                    $scope.callback(scrapitem);
                }
            }

            function loadScrapItems() {
                vm.loading = true;
                if (project == null || project == 0) {
                    projectId = 0;
                }
                else {
                    projectId = project.id
                }
                TopStockIssuedService.getProjectItems(vm.newScrap.store.id, projectId, pageable).then(
                    function (data) {
                        vm.itemList = data;
                        vm.loading = false;
                        angular.forEach(vm.itemList.content, function (item) {
                            var itemObj = addedItemsMap.get(item.id);
                            if (itemObj != null) {
                                item.isAdded = true;
                            }
                            else {
                                item.isAdded = false;
                            }
                        });
                    })
            }

            function freeTextSearch(freeText) {
                $scope.freeTextQuery = freeText;
                if (searchQuery == null) {
                    pageable.page = 0;
                }
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    searchQuery = freeText;
                    vm.loading = true;
                    if (project == null || project == 0) {
                        searchMasterDataItems(freeText);
                    }
                    else {
                        searchProjectMaterials(freeText);
                    }
                }
                else {
                    resetPage();
                }
            }

            function searchProjectMaterials(freeText) {
                ItemService.searchProjectMaterials(projectId, pageable, freeText).then(
                    function (data) {
                        vm.itemList = data;
                        vm.clear = true;
                        angular.forEach(vm.itemList.content, function (item) {
                            item.itemType = item.itemType.name;
                            var itemObj = addedItemsMap.get(item.id);
                            if (itemObj != null) {
                                item.isAdded = true;
                            }
                            else {
                                item.isAdded = false;
                            }
                        });
                        vm.loading = false;
                    }
                )
            }

            function searchMasterDataItems(freeText) {
                ItemService.materialFreeTextSearch(pageable, freeText).then(
                    function (data) {
                        vm.itemList = data;
                        vm.clear = true;
                        angular.forEach(vm.itemList.content, function (item) {
                            item.itemType = item.itemType.name;
                            var itemObj = addedItemsMap.get(item.id);
                            if (itemObj != null) {
                                item.isAdded = true;
                            }
                            else {
                                item.isAdded = false;
                            }
                        });
                        vm.loading = false;
                    }
                )
            }

            function resetPage() {
                pageable.page = 0;
                searchQuery = null;
                $scope.freeTextQuery = null;
                loadScrapItems();
            }

            function nextPage() {
                if (vm.itemList.last != true && !vm.loading) {
                    pageable.page++;
                    if (searchQuery == null) {
                        loadScrapItems();
                    }
                    else {
                        freeTextSearch(searchQuery);
                    }
                }
            }

            function previousPage() {
                if (vm.itemList.first != true && !vm.loading) {
                    pageable.page--;
                    if (searchQuery == null) {
                        loadScrapItems();
                    }
                    else {
                        freeTextSearch(searchQuery);
                    }
                }
            }

            (function () {
                loadScrapItems();
            })();
        }
    }
)
;