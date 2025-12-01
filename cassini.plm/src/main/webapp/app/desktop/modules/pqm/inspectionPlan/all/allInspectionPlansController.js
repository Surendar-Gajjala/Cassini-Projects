define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/shared/services/core/inspectionPlanService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/projectService',
        'app/shared/services/core/workflowService',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/recentlyVisitedService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective',
        'app/desktop/modules/pqm/directives/qualityObjectTypeDirective',
        'app/desktop/modules/pqm/directives/alternateQualityObjectTypeDirective'
    ],
    function (module) {
        module.controller('AllInspectionPlansController', AllInspectionPlansController);

        function AllInspectionPlansController($scope, $rootScope, $translate, $timeout, $state, $window, $application, $stateParams, $cookies, $sce, ItemService,
                                              InspectionPlanService, DialogService, ObjectTypeAttributeService, ECOService, WorkflowDefinitionService,
                                              MfrService, MfrPartsService, AttributeAttachmentService, CommonService, ProjectService, SpecificationsService,
                                              RecentlyVisitedService, WorkflowService) {

            $rootScope.viewInfo.icon = "fa fa-calendar";
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            var parsed = angular.element("<div></div>");
            vm.loading = false;

            $scope.createPlanTitle = parsed.html($translate.instant("CREATE_NEW_INSPECTION_PLAN")).html();
            var inspectionPlanTitle = parsed.html($translate.instant("NEW_INSPECTION_PLAN")).html();
            var productInspectionPlanTitle = parsed.html($translate.instant("NEW_PRODUCT_INSPECTION_PLAN")).html();
            var materialInspectionPlanTitle = parsed.html($translate.instant("NEW_MATERIAL_INSPECTION_PLAN")).html();
            var createButton = parsed.html($translate.instant("CREATE")).html();
            var deleteDialogTitle = parsed.html($translate.instant("DELETE_INSPECTION_PLAN")).html();
            var deleteInspectionPlanDialogMessage = parsed.html($translate.instant("DELETE_INSPECTION_PLAN_DIALOG_MESSAGE")).html();
            var inspectionPlanDeletedMessage = parsed.html($translate.instant("INSPECTION_PLAN_DELETED_MESSAGE")).html();
            $scope.cannotDeleteApprovedPlan = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_PLAN")).html();
            var currencyMap = new Hashtable();
            vm.RemoveColumnTitle = parsed.html($translate.instant("REMOVE_ATTRIBUTE_COLUMN")).html();
            vm.showAttributes = parsed.html($translate.instant("SHOW_ATTRIBUTES")).html();
            $scope.noPlans = parsed.html($translate.instant("NO_PLANS")).html();


            vm.newInspectionPlan = newInspectionPlan;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.showPlan = showPlan;
            vm.showTypeAttributes = showTypeAttributes;
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

            vm.inspectionPlans = angular.copy(pagedResults);

            vm.filters = {
                name: null,
                planType: '',
                status: '',
                number: null,
                phase: null,
                product: '',
                material: '',
                description: null,
                searchQuery: null
            };
            $scope.freeTextQuery = null;
            vm.selectInspectionPlanType = selectInspectionPlanType;
            function selectInspectionPlanType(type) {
                $rootScope.showBusyIndicator($('.view-container'));
                $rootScope.allInspectionPlanType = type;
                vm.pageable.page = 0;
                vm.inspectionPlans = [];
                loadInspectionPlans();
            }

            function loadInspectionPlans() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                var promise = null;
                if ($rootScope.allInspectionPlanType == "PRODUCTINSPECTIONPLAN") {
                    promise = InspectionPlanService.getAllProductInspectionPlans(vm.pageable, vm.filters);
                } else if ($rootScope.allInspectionPlanType == "MATERIALINSPECTIONPLAN") {
                    promise = InspectionPlanService.getAllMaterialInspectionPlans(vm.pageable, vm.filters);
                }
                if (promise != null) {
                    promise.then(
                        function (data) {
                            vm.inspectionPlans = data;
                            vm.loading = false;
                            loadStatus();
                            loadInspectionPlanTypeLifecycles();
                            loadAttributeValues();
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.objectIds = [];
            vm.attributeIds = [];
            function loadAttributeValues() {
                vm.objectIds = [];

                angular.forEach(vm.inspectionPlans.content, function (item) {
                    vm.objectIds.push(item.id);
                    vm.objectIds.push(item.latestRevision);
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.inspectionPlans.content);
            }

            vm.openAttachment = openAttachment;
            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                $rootScope.downloadFileFromIframe(url);
            }

            function newInspectionPlan() {
                var title = "";
                if ($rootScope.allInspectionPlanType == "PRODUCTINSPECTIONPLAN") {
                    title = productInspectionPlanTitle;
                } else {
                    title = materialInspectionPlanTitle;
                }
                var options = {
                    title: title,
                    template: 'app/desktop/modules/pqm/inspectionPlan/new/newInspectionPlanView.jsp',
                    controller: 'NewInspectionPlanController as newInspectionPlanVm',
                    resolve: 'app/desktop/modules/pqm/inspectionPlan/new/newInspectionPlanController',
                    width: 600,
                    showMask: true,
                    data: {
                        inspectionPlanType: $rootScope.allInspectionPlanType,
                        actionType: "all"
                    },
                    buttons: [
                        {text: createButton, broadcast: 'app.inspectionPlans.new'}
                    ],
                    callback: function (plan) {
                        $timeout(function () {
                            showPlan(plan)
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.lifecyclePhases = [];
            function loadInspectionPlanTypeLifecycles() {
                vm.lifecyclePhases = [];
                InspectionPlanService.getInspectionTypeLifecycles().then(
                    function (data) {
                        vm.docTypeLifecycles = data;
                        angular.forEach(vm.docTypeLifecycles, function (lifecycle) {
                            angular.forEach(lifecycle.phases, function (phase) {
                                if (vm.lifecyclePhases.indexOf(phase.phase) == -1) {
                                    vm.lifecyclePhases.push(phase.phase);
                                }
                            })
                        })
                    }
                )
            }

            vm.selectedPhase = null;
            vm.onSelectPhase = onSelectPhase;
            function onSelectPhase(phase) {
                vm.pageable.page = 0;
                vm.selectedPhase = phase;
                vm.filters.phase = phase;
                loadInspectionPlans();
            }


            vm.clearPhase = clearPhase;
            function clearPhase() {
                vm.pageable.page = 0;
                vm.selectedPhase = null;
                vm.filters.phase = null;
                loadInspectionPlans();
            }

            vm.selectedProduct = null;
            vm.onSelectProduct = onSelectProduct;
            function onSelectProduct(product) {
                vm.pageable.page = 0;
                vm.selectedProduct = product.productName;
                vm.filters.product = product.product;
                loadInspectionPlans();
            }


            vm.clearProduct = clearProduct;
            function clearProduct() {
                vm.pageable.page = 0;
                vm.selectedProduct = null;
                vm.filters.product = '';
                loadInspectionPlans();
            }

            vm.selectedMaterial = null;
            vm.onSelectMaterial = onSelectMaterial;
            function onSelectMaterial(material) {
                vm.pageable.page = 0;
                vm.selectedMaterial = material.materialName;
                vm.filters.material = material.material;
                loadInspectionPlans();
            }


            vm.clearMaterial = clearMaterial;
            function clearMaterial() {
                vm.pageable.page = 0;
                vm.selectedMaterial = null;
                vm.filters.material = '';
                loadInspectionPlans();
            }

            function nextPage() {
                if (vm.inspectionPlans.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadInspectionPlans();
                }
            }

            function previousPage() {
                if (vm.inspectionPlans.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadInspectionPlans();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadInspectionPlans();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadInspectionPlans();
                } else {
                    resetPage();
                }
            }

            vm.clearTypeSelection = clearTypeSelection;
            function clearTypeSelection() {
                vm.pageable.page = 0;
                vm.selectedProductType = null;
                vm.filters.planType = '';
                loadInspectionPlans();
            }

            vm.onSelectType = onSelectType;
            function onSelectType(productType) {
                vm.pageable.page = 0;
                vm.selectedProductType = productType;
                vm.filters.planType = productType.id;
                vm.filters.freeTextSearch = false;
                loadInspectionPlans();
            }

            $scope.statuses = [];
            function loadStatus() {
                WorkflowService.getObjectWorkflowStatus($rootScope.allInspectionPlanType).then(
                    function (data) {
                        $scope.statuses = data;
                    }
                )
            }

            $scope.selectedStatus = null;
            $scope.onSelectStatus = onSelectStatus;
            function onSelectStatus(status) {
                vm.pageable.page = 0;
                $scope.selectedStatus = status;
                vm.filters.status = status;
                loadInspectionPlans();
            }

            $scope.clearStatus = clearStatus;
            function clearStatus() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedStatus = null;
                vm.filters.status = '';
                loadInspectionPlans();
                $rootScope.hideBusyIndicator();
            }

            vm.onSelectedType = onSelectedType;
            function onSelectedType(inspectionPlanType) {
                if (inspectionPlanType != null && inspectionPlanType != undefined) {
                    vm.inspectionPlans.inspectionPlanType = inspectionPlanType;
                    vm.inspectionPlans.material = inspectionPlanType;
                }
            }


            function resetPage() {
                vm.inspectionPlans = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadInspectionPlans();
            }

            function showPlan(plan) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = plan.latestRevision;
                vm.recentlyVisited.objectType = plan.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.pqm.inspectionPlan.details', {
                            planId: plan.latestRevision,
                            tab: 'details.basic'
                        });
                    }, function (error) {
                        $state.go('app.pqm.inspectionPlan.details', {
                            planId: plan.latestRevision,
                            tab: 'details.basic'
                        });
                    }
                )
            }

            vm.deletePlan = deletePlan;
            function deletePlan(plan) {
                var options = {
                    title: deleteDialogTitle,
                    message: deleteInspectionPlanDialogMessage + " [" + plan.name + "] ?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        InspectionPlanService.deleteInspectionPlan(plan.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(inspectionPlanDeletedMessage);
                                loadInspectionPlans();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            var attributesTitle = $translate.instant("ATTRIBUTES");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var selectedAttributesMessage = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();

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
                        selectedObjectType: "PRODUCTINSPECTIONPLANTYPE",
                        selectedObject: "PRODUCTINSPECTIONPLAN",
                        selectedParentObjectType: "QUALITY"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.quality.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("inspectionPlanAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                        loadInspectionPlans();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem("inspectionPlanAttributes", JSON.stringify(vm.selectedAttributes));
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("inspectionPlanAttributes"));
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

            var revisionHistoryTitle = $translate.instant("REVISION_HISTORY_TITLE");
            vm.showPlanRevisionHistory = showPlanRevisionHistory;

            function showPlanRevisionHistory(inspectionPlan) {
                var options = {
                    title: inspectionPlan.number + " - " + revisionHistoryTitle,
                    template: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryView.jsp',
                    controller: 'ItemRevisionHistoryController as revHistoryVm',
                    resolve: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryController',
                    data: {
                        itemId: inspectionPlan.id,
                        revisionHistoryType: "INSPECTIONPLAN"
                    },
                    width: 700,
                    showMask: true,
                };

                $rootScope.showSidePanel(options);
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
                $rootScope.getQualityAttributes("QUALITY", "PRODUCTINSPECTIONPLANTYPE", vm.attributeIds);
                if ($rootScope.allInspectionPlanType == null || $rootScope.allInspectionPlanType == "" || $rootScope.allInspectionPlanType == undefined) {
                    $rootScope.allInspectionPlanType = "PRODUCTINSPECTIONPLAN";
                }
                if ($rootScope.allInspectionPlanType == "MATERIALINSPECTIONPLAN") {
                    document.getElementById("productType").checked = false;
                    document.getElementById("materialType").checked = true;
                }
                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                })
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("inspectionPlanAttributes"));
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
                                $window.localStorage.setItem("inspectionPlanAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadInspectionPlans();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadInspectionPlans();
                }
            })();
        }
    }
);


