define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/shared/services/core/assetService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/projectService',
        'app/shared/services/core/specificationsService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllAssetsController', AllAssetsController);

        function AllAssetsController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce, AssetService, ObjectTypeAttributeService,
                                     ItemService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService, AttributeAttachmentService, CommonService, ProjectService, SpecificationsService,
                                     RecentlyVisitedService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newAsset = newAsset;
            vm.deleteAsset = deleteAsset;
            vm.Asset = [];
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
                searchQuery: null
            };
            $scope.freeTextQuery = null;

            vm.assets = angular.copy(pagedResults);


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
                        type: "ASSETTYPE",
                        objectType: "MROASSET"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("assetAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadAssets();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newAssetHeading = parsed.html($translate.instant("NEW_ASSETS_TYPE")).html();

            function newAsset() {
                var options = {
                    title: newAssetHeading,
                    template: 'app/desktop/modules/mro/assets/new/newAssetView.jsp',
                    controller: 'NewAssetController as newAssetVm',
                    resolve: 'app/desktop/modules/mro/assets/new/newAssetController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.asset.new'}
                    ],
                    callback: function (asset) {
                        $timeout(function () {
                            loadAssets();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function nextPage() {
                if (vm.assets.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadAssets();
                }
            }

            function previousPage() {
                if (vm.assets.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadAssets();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadAssets();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadAssets();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.assets = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadAssets();
            }

            function loadAssets() {
                vm.assets = [];
                vm.loading = true;
                AssetService.getAllAssets(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.assets = data;
                        angular.forEach(vm.assets.content, function (asset) {
                            asset.hasImage = false;
                            if (asset.resourceObject.objectType == 'PLANT' || asset.resourceObject.objectType == 'WORKCENTER') {
                                asset.showImage = false;
                            } else {
                                asset.showImage = true;
                            }
                            if (asset.resourceObject.objectType == 'MACHINE' && asset.resourceObject.image != null) {
                                asset.hasImage = true;
                                asset.imagePath = "api/mes/machines/" + asset.resourceObject.id + "/image/download?" + new Date().getTime();
                            } else if (asset.resourceObject.objectType == 'TOOL' && asset.resourceObject.image != null) {
                                asset.hasImage = true;
                                asset.imagePath = "api/mes/tools/" + asset.resourceObject.id + "/image/download?" + new Date().getTime();
                            } else if (asset.resourceObject.objectType == 'EQUIPMENT' && asset.resourceObject.image != null) {
                                asset.hasImage = true;
                                asset.imagePath = "api/mes/equipments/" + asset.resourceObject.id + "/image/download?" + new Date().getTime();
                            } else if (asset.resourceObject.objectType == 'INSTRUMENT' && asset.resourceObject.image != null) {
                                asset.hasImage = true;
                                asset.imagePath = "api/mes/instruments/" + asset.resourceObject.id + "/image/download?" + new Date().getTime();
                            } else if (asset.resourceObject.objectType == 'JIGFIXTURE' && asset.resourceObject.image != null) {
                                asset.hasImage = true;
                                asset.imagePath = "api/mes/jigsfixs/" + asset.resourceObject.id + "/image/download?" + new Date().getTime();
                            } else if (asset.resourceObject.objectType == 'MATERIAL' && asset.resourceObject.image != null) {
                                asset.hasImage = true;
                                asset.imagePath = "api/mes/materials/" + asset.resourceObject.id + "/image/download?" + new Date().getTime();
                            }
                        })
                        CommonService.getPersonReferences(vm.assets.content, 'modifiedBy');
                        loadAttributeValues();
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;
                    },
                    function (error) {
                        vm.loading = false;
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }
            vm.objectIds = [];
            vm.selectedAttributes = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.assets.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.assets.content);

            }

            vm.showAsset = showAsset;
            function showAsset(asset) {
                $state.go('app.mro.asset.details', {assetId: asset.id, tab: 'details.basic'});
                /* vm.recentlyVisited = {};
                 vm.recentlyVisited.objectId = asset.id;
                 vm.recentlyVisited.objectType = asset.objectType;
                 vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                 RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                 function (data) {
                 $state.go('app.mes.asset.details', {assetId: asset.id, tab: 'details.basic'});
                 }, function (error) {
                 $state.go('app.mes.asset.details', {assetId: asset.id, tab: 'details.basic'});
                 }
                 )*/
            }

            var deleteAsset = parsed.html($translate.instant("DELETE_ASSET")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var assetDeleteMsg = parsed.html($translate.instant("ASSET_DELETE_MSG")).html();


            function deleteAsset(asset) {
                var options = {
                    title: deleteAsset,
                    message: deleteDialogMessage + " [ " + asset.number + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        if (asset.id != null && asset.id != undefined) {
                            AssetService.deleteAsset(asset.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(assetDeleteMsg);
                                    loadAssets();
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


            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("assetAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            vm.showImage = showImage;
            function showImage(asset) {
                var modal = document.getElementById('item-thumbnail' + asset.id);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close" + asset.id);
                $("#thumbnail-image" + asset.id).width($('#thumbnail-view' + asset.id).outerWidth());
                $("#thumbnail-image" + asset.id).height($('#thumbnail-view' + asset.id).outerHeight());

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
                    setAttributes = JSON.parse($window.localStorage.getItem("assetAttributes"));
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
                                $window.localStorage.setItem("assetAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadAssets();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadAssets();
                }
            })();

        }
    }
);