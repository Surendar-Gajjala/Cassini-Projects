define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/shared/services/core/inspectionService',
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
        module.controller('AllInspectionsController', AllInspectionsController);

        function AllInspectionsController($scope, $rootScope, $translate, $timeout, $state, $window, $application, $stateParams, $cookies, $sce, ItemService,
                                          DialogService, InspectionService, ECOService, WorkflowDefinitionService, ObjectTypeAttributeService,
                                          MfrService, MfrPartsService, AttributeAttachmentService, CommonService, ProjectService, SpecificationsService,
                                          RecentlyVisitedService) {

            $rootScope.viewInfo.icon = "fa fa-calendar";
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            vm.loading = false;
            var currencyMap = new Hashtable();

            var parsed = angular.element("<div></div>");
            $scope.createInspectionTitle = parsed.html($translate.instant("CREATE_NEW_INSPECTION")).html();
            var inspectionTitle = parsed.html($translate.instant("NEW_INSPECTION")).html();
            var itemInspectionTitle = parsed.html($translate.instant("NEW_ITEM_INSPECTION")).html();
            var materialInspectionTitle = parsed.html($translate.instant("NEW_MATERIAL_INSPECTION")).html();
            var createButton = parsed.html($translate.instant("CREATE")).html();

            var deleteDialogTitle = parsed.html($translate.instant("DELETE_INSPECTION")).html();
            var deleteInspectionDialogMessage = parsed.html($translate.instant("DELETE_INSPECTION_DIALOG_MESSAGE")).html();
            var inspectionDeletedMessage = parsed.html($translate.instant("INSPECTION_DELETED_MESSAGE")).html();
            $scope.cannotDeleteApprovedInspection = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_INSPECTION")).html();
            vm.RemoveColumnTitle = parsed.html($translate.instant("REMOVE_ATTRIBUTE_COLUMN")).html();
            vm.showAttributes = parsed.html($translate.instant("SHOW_ATTRIBUTES")).html();

            vm.newInspection = newInspection;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;

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
                inspectionNumber: null,
                description: null,
                searchQuery: null
            };
            vm.inspections = angular.copy(pagedResults);

            function nextPage() {
                if (vm.inspections.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadInspections();
                }
            }

            function previousPage() {
                if (vm.inspections.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadInspections();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadInspections();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadInspections();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.inspections = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadInspections();
            }


            vm.showInspection = showInspection;
            function showInspection(inspection) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = inspection.id;
                vm.recentlyVisited.objectType = inspection.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.pqm.inspection.details', {inspectionId: inspection.id, tab: 'details.basic'})
                    }, function (error) {
                        $state.go('app.pqm.inspection.details', {inspectionId: inspection.id, tab: 'details.basic'})
                    }
                )
            }


            function loadInspections() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                var promise = null;
                if ($rootScope.allInspectionType == "ITEMINSPECTION") {
                    promise = InspectionService.getAllItemInspections(vm.pageable, vm.filters);
                } else {
                    promise = InspectionService.getAllMaterialInspections(vm.pageable, vm.filters);
                }
                if (promise != null) {
                    promise.then(
                        function (data) {
                            vm.inspections = data;
                            loadAttributeValues();
                            vm.loading = false;
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.objectIds = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.inspections.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.inspections.content);
            }

            vm.openAttachment = openAttachment;
            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                $rootScope.downloadFileFromIframe(url);
            }


            function newInspection() {
                var title = "";
                if ($rootScope.allInspectionType == "ITEMINSPECTION") {
                    title = itemInspectionTitle;
                } else {
                    title = materialInspectionTitle;
                }
                var options = {
                    title: title,
                    template: 'app/desktop/modules/pqm/inspection/new/newInspectionView.jsp',
                    controller: 'NewInspectionController as newInspectionVm',
                    resolve: 'app/desktop/modules/pqm/inspection/new/newInspectionController',
                    width: 700,
                    showMask: true,
                    data: {
                        inspectionType: $rootScope.allInspectionType,
                        actionType: "all"
                    },
                    buttons: [
                        {text: createButton, broadcast: 'app.inspections.new'}
                    ],
                    callback: function (inspection) {
                        $timeout(function () {
                            showInspection(inspection)
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.deleteInspection = deleteInspection;
            function deleteInspection(inspection) {
                var options = {
                    title: deleteDialogTitle,
                    message: deleteInspectionDialogMessage + " [" + inspection.inspectionNumber + "] ?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        var promise = null;
                        if ($rootScope.allInspectionType == "ITEMINSPECTION") {
                            promise = InspectionService.deleteItemInspection(inspection.id);
                        } else {
                            promise = InspectionService.deleteMaterialInspection(inspection.id);
                        }
                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    $rootScope.showSuccessMessage(inspectionDeletedMessage);
                                    $rootScope.hideBusyIndicator();
                                    loadInspections();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                });
            }

            var attributesTitle = $translate.instant("ATTRIBUTES");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var selectedAttributesMessage = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();

            vm.showTypeAttributes = showTypeAttributes;
            vm.removeAttribute = removeAttribute;
            vm.selectedAttributes = [];
            function showTypeAttributes() {
                var options = {
                    title: attributesTitle,
                    template: 'app/desktop/modules/pqm/attributes/qualityTypeAttributeSelectionView.jsp',
                    resolve: 'app/desktop/modules/pqm/attributes/qualityTypeAttributeSelectionController',
                    controller: 'QualityTypeAttributeSelectionController as qualityTypeAttributeSelectionVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: vm.selectedAttributes,
                        selectedObjectType: "ITEMINSPECTION",
                        selectedObject: "ITEMINSPECTION",
                        selectedParentObjectType: "QUALITY"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.quality.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("inspectionAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                        loadInspections();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem("inspectionAttributes", JSON.stringify(vm.selectedAttributes));
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("inspectionAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            vm.showAttributeDetails = showAttributeDetails;
            function showAttributeDetails(attribute) {
                if (attribute.objectType == 'ITEM') {
                    $state.go('app.items.details', {itemId: attribute.id});
                } else if (attribute.objectType == 'ITEMREVISION') {
                    $state.go('app.items.details', {itemId: attribute.id});
                } else if (attribute.objectType == 'CHANGE') {
                    $state.go('app.changes.ecos.details', {ecoId: attribute.id});
                } else if (attribute.objectType == 'PLMWORKFLOWDEFINITION') {
                    $state.go('app.workflow.editor', {mode: 'edit', workflow: attribute.id})
                } else if (attribute.objectType == 'MANUFACTURER') {
                    $state.go('app.mfr.details', {manufacturerId: attribute.id});
                } else if (attribute.objectType == 'MANUFACTURERPART') {
                    $state.go('app.mfr.mfrparts.details', {
                        mfrId: attribute.manufacturer,
                        manufacturePartId: attribute.id
                    });
                } else if (attribute.objectType == 'REQUIREMENT') {
                    $state.go('app.rm.requirements.details', {requirementId: attribute.id});
                } else if (attribute.objectType == 'PROJECT') {
                    $state.go('app.pm.project.details', {projectId: attribute.id});
                }
            }

            vm.selectInspectionType = selectInspectionType;
            function selectInspectionType(type) {
                $rootScope.showBusyIndicator($('.view-container'));
                $rootScope.allInspectionType = type;
                vm.pageable.page = 0;
                loadInspections();
            }

            vm.showThumbnailImage = showThumbnailImage;
            function showThumbnailImage(item) {
                var modal = document.getElementById('item-thumbnail' + item.id);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close" + item.id);
                $("#thumbnail-image" + item.id).width($('#thumbnail-view' + item.id).outerWidth());
                $("#thumbnail-image" + item.id).height($('#thumbnail-view' + item.id).outerHeight());

                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            (function () {
                if ($rootScope.allInspectionType == null || $rootScope.allInspectionType == "" || $rootScope.allInspectionType == undefined) {
                    $rootScope.allInspectionType = "ITEMINSPECTION";
                }
                if ($rootScope.allInspectionType == "MATERIALINSPECTION") {
                    document.getElementById("productType").checked = false;
                    document.getElementById("materialType").checked = true;
                }

                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                });
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("inspectionAttributes"));
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
                                $window.localStorage.setItem("inspectionAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadInspections();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadInspections();
                }
            })();
        }
    }
);


