define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/materialService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllMaterialController', AllMaterialController);

        function AllMaterialController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce,
                                       ObjectTypeAttributeService, CommonService, MaterialService, RecentlyVisitedService, ItemService, ECOService, WorkflowDefinitionService,
                                       AttributeAttachmentService, MfrService, MfrPartsService, ProjectService, SpecificationsService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newMaterial = newMaterial;
            vm.materials = [];
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

            vm.materials = angular.copy(pagedResults);

            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            $scope.newMaterial = parsed.html($translate.instant("NEW_MATERIAL")).html();
            var newMaterialHeading = parsed.html($translate.instant("NEW_MATERIAL")).html();

            function newMaterial() {
                var options = {
                    title: newMaterialHeading,
                    template: 'app/desktop/modules/mes/material/new/newMaterialView.jsp',
                    controller: 'NewMaterialController as newMaterialVm',
                    resolve: 'app/desktop/modules/mes/material/new/newMaterialController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.material.new'}
                    ],
                    callback: function (material) {
                        $timeout(function () {
                            loadMaterials();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            $scope.operationFilePopover = {
                templateUrl: 'app/desktop/modules/mes/material/all/materialFilePopoverTemplate.jsp'
            };



            function nextPage() {
                if (vm.materials.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadMaterials();
                }
            }

            function previousPage() {
                if (vm.materials.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadMaterials();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadMaterials();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadMaterials();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.materials = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadMaterials();
            }

            function loadMaterials() {
                vm.loading = true;
                MaterialService.getAllMaterials(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.materials = data;
                        angular.forEach(vm.materials.content, function (material) {
                            if (material.hasImage) {
                                material.imagePath = "api/mes/materials/" + material.id + "/image/download?" + new Date().getTime();
                            }
                        })
                        vm.loading = false;
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
                angular.forEach(vm.materials.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.materials.content);

            }

            vm.showMaterial = showMaterial;
            function showMaterial(material) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = material.id;
                vm.recentlyVisited.objectType = "MATERIAL";
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.mes.masterData.material.details', {
                            materialId: material.id,
                            tab: 'details.basic'
                        });
                    }, function (error) {
                        $state.go('app.mes.masterData.material.details', {
                            materialId: material.id,
                            tab: 'details.basic'
                        });
                    }
                )
            }

            var deleteMaterials = parsed.html($translate.instant("DELETE_MATERIAL")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var materialDeleteMsg = parsed.html($translate.instant("MATERIAL_DELETE_MSG")).html();


            vm.deleteMaterial = deleteMaterial;
            function deleteMaterial(material) {
                var options = {
                    title: deleteMaterials,
                    message: deleteDialogMessage + " [ " + material.number + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        if (material.id != null && material.id != undefined) {
                            MaterialService.deleteMaterial(material.id).then(
                                function (data) {
                                    var index = vm.materials.content.indexOf(material);
                                    vm.materials.content.splice(index, 1);
                                    vm.materials.totalElements--;
                                    $rootScope.showSuccessMessage(materialDeleteMsg);
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
                        type: "MATERIALTYPE",
                        objectType: "MATERIAL"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("materialAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadMaterials();
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
                    JSON.parse($window.localStorage.getItem("materialAttributes"));
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
                    setAttributes = JSON.parse($window.localStorage.getItem("materialAttributes"));
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
                                $window.localStorage.setItem("materialAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadMaterials();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadMaterials();
                }
            })();

        }
    }
);