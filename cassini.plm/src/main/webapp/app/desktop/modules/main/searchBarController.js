define(
    [
        'app/desktop/modules/main/main.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/workflowService',
        'app/shared/services/core/recentlyVisitedService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/directives/table-footer/tableFooterDirective',
        'app/shared/services/core/reqDocumentService'
    ],
    function (module) {
        module.controller('SearchBarController', SearchBarController);

        function SearchBarController($scope, $rootScope, $timeout, $interval, $translate, $state, $cookies, $application,
                                     ItemService, WorkflowService, CommonService, RecentlyVisitedService, ReqDocumentService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            vm.searchTitle = parsed.html($translate.instant("SEARCH")).html();
            $scope.declarations = parsed.html($translate.instant("DECLARATIONS")).html();
            $scope.workCenters = parsed.html($translate.instant("WORKCENTERS")).html();
            $scope.equipments = parsed.html($translate.instant("EQUIPMENTS")).html();
            $scope.instrumnets = parsed.html($translate.instant("INSTRUMENTS")).html();
            $scope.jigs = parsed.html($translate.instant("JIGS")).html();
            $scope.fixtures = parsed.html($translate.instant("FIXTURES")).html();
            $scope.manpowers = parsed.html($translate.instant("MANPOWER")).html();
            $scope.operations = parsed.html($translate.instant("OPERATIONS")).html();
            $scope.meters = parsed.html($translate.instant("METERS")).html();
            $scope.maintenancePlan = parsed.html($translate.instant("MAINTENANCE_PLANS")).html();
            $scope.manufacturers = parsed.html($translate.instant("MANUFACTURERS_TITLE")).html();

            vm.pageable = {
                page: 0,
                size: 10,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };
            $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
            vm.search = {
                name: null,
                description: null,
                searchType: null,
                query: null,
                objectType: 'ITEM',
                owner: $rootScope.loginPersonDetails.person.id
            };
            vm.pageSize = pageSize;
            vm.searchResults = {};

            $scope.allTitle = parsed.html($translate.instant("ALL")).html();
            $scope.itemTitle = parsed.html($translate.instant("ITEMS_ALL_TITLE")).html();
            $scope.designDataTitle = parsed.html($translate.instant("DESIGN_DATA")).html();
            $scope.assembliesTitle = parsed.html($translate.instant("ASSEMBLIES")).html();
            $scope.vaultsTitle = parsed.html($translate.instant("VAULTS")).html();
            $scope.partsTitle = parsed.html($translate.instant("PARTS")).html();
            $scope.drawingsTitle = parsed.html($translate.instant("DRAWINGS")).html();
            $scope.projectTitle = parsed.html($translate.instant("NO_OF_PROJECTS")).html();
            $scope.programTitle = parsed.html($translate.instant("PROGRAMS")).html();
            $scope.changesTitle = parsed.html($translate.instant("ITEM_DETAILS_TAB_CHANGES")).html();
            $scope.ecoTitle = parsed.html($translate.instant("ECOS")).html();
            $scope.requirementTitle = parsed.html($translate.instant("REQUIREMENTS")).html();
            $scope.requirementDocumentTitle = parsed.html($translate.instant("REQ_DOCUMENTS")).html();
            $scope.workflowTitle = parsed.html($translate.instant("WORKFLOWS")).html();
            $scope.fileTitle = parsed.html($translate.instant("FILES")).html();
            $scope.documentTitle = parsed.html($translate.instant("DOCUMENTS")).html();
            $scope.qualityTitle = parsed.html($translate.instant("QUALITY")).html();
            $scope.manufacturingTitle = parsed.html($translate.instant("MANUFACTURING")).html();
            $scope.maintenanceTitle = parsed.html($translate.instant("MAINTENANCEANDREPAIRS")).html();
            $scope.maintenanceTitle = $scope.maintenanceTitle.replace("amp;","");
            $scope.oemTitle = parsed.html($translate.instant("OEM")).html();
            $scope.manufacturerTitle = parsed.html($translate.instant("MANUFACTURER")).html();
            $scope.manufacturerPartTitle = parsed.html($translate.instant("MANUFACTURERPART")).html();
            $scope.supplierTitle = parsed.html($translate.instant("SUPPLIER")).html();
            $scope.productInspectionPlanTitle = parsed.html($translate.instant("PRODUCTINSPECTIONPLANS")).html();
            $scope.materialInspectionPlanTitle = parsed.html($translate.instant("MATERIALINSPECTIONPLANS")).html();
            $scope.itemInspectionTitle = parsed.html($translate.instant("ITEMINSPECTIONS")).html();
            $scope.materialInspectionTitle = parsed.html($translate.instant("MATERIALINSPECTIONS")).html();
            $scope.problemReportTitle = parsed.html($translate.instant("PROBLEM_REPORTS")).html();
            $scope.ncrTitle = parsed.html($translate.instant("NCRS")).html();
            $scope.qcrTitle = parsed.html($translate.instant("QCRS")).html();
            $scope.ppapTitle = parsed.html($translate.instant("PPAPS")).html();
            $scope.supplierAuditTitle = parsed.html($translate.instant("SUPPLIER_AUDITS")).html();
            $scope.ecrTitle = parsed.html($translate.instant("ECRS")).html();
            $scope.dcoTitle = parsed.html($translate.instant("DCOS")).html();
            $scope.dcrTitle = parsed.html($translate.instant("DCRS")).html();
            $scope.mcoTitle = parsed.html($translate.instant("MCOS")).html();
            $scope.deviationTitle = parsed.html($translate.instant("DEVIATION")).html();
            $scope.waiverTitle = parsed.html($translate.instant("WAIVER")).html();

            $scope.plantsTitle = parsed.html($translate.instant("PLANTS")).html();
            $scope.workCentersTitle = parsed.html($translate.instant("WORKCENTERS")).html();
            $scope.machinesTitle = parsed.html($translate.instant("MACHINES")).html();
            $scope.equipmentsTitle = parsed.html($translate.instant("EQUIPMENTS")).html();
            $scope.instrumentsTitle = parsed.html($translate.instant("INSTRUMENTS")).html();
            $scope.toolsTitle = parsed.html($translate.instant("TOOLS")).html();
            $scope.jigsTitle = parsed.html($translate.instant("JIGS")).html();
            $scope.fixturesTitle = parsed.html($translate.instant("FIXTURES")).html();
            $scope.materialsTitle = parsed.html($translate.instant("MATERIALS")).html();
            $scope.manpowersTitle = parsed.html($translate.instant("MANPOWER")).html();
            $scope.shiftsTitle = parsed.html($translate.instant("SHIFTS")).html();
            $scope.operationsTitle = parsed.html($translate.instant("OPERATIONS")).html();

            $scope.assetTitle = parsed.html($translate.instant("ASSETS")).html();
            $scope.meterTitle = parsed.html($translate.instant("METERS")).html();
            $scope.sparePartTitle = parsed.html($translate.instant("SPARE_PARTS")).html();
            $scope.maintenancePlanTitle = parsed.html($translate.instant("MAINTENANCE_PLANS")).html();
            $scope.workRequestTitle = parsed.html($translate.instant("WORK_REQUESTS")).html();
            $scope.workOrderTitle = parsed.html($translate.instant("WORK_ORDERS")).html();

            $scope.complianceTitle = parsed.html($translate.instant("COMPLIANCE")).html();
            $scope.substanceTitle = parsed.html($translate.instant("SUBSTANCES")).html();
            $scope.pgcSpecificationTitle = parsed.html($translate.instant("SPECIFICATIONS")).html();
            $scope.pgcDeclarationTitle = parsed.html($translate.instant("DECLARATIONS")).html();
            $scope.assemblyLineTitle = parsed.html($translate.instant("ASSEMBLY_LINES")).html();
            $scope.nprTitle = parsed.html($translate.instant("NEW_PART_REQUESTS")).html();
            $scope.tagsTitle = parsed.html($translate.instant("TAGS")).html();
            $scope.customObjectTitle = parsed.html($translate.instant("CUSTOM_OBJECT_TITLE")).html();

            vm.change = parsed.html($translate.instant("ITEM_DETAILS_TAB_CHANGES")).html();

            vm.searchText = "";
            vm.filter = {
                label: $scope.allTitle,
                value: 'ALL'
            };
            vm.active = $scope.itemTitle;
            vm.filesDto = null;

            vm.performSearch = performSearch;
            vm.openItemDetails = openItemDetails;
            vm.openEcoDetails = openEcoDetails;
            vm.openGlossaryDetails = openGlossaryDetails;
            vm.openProjectDetails = openProjectDetails;
            vm.openFileDetails = openFileDetails;
            vm.openReqDetails = openReqDetails;
            vm.openWorkflowDetails = openWorkflowDetails;
            vm.openCustomObjectDetails = openCustomObjectDetails;

            vm.setFilter = setFilter;
            vm.preventClick = preventClick;
            vm.showFiltersMenu = showFiltersMenu;

            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.detailsFound = true;
            vm.filesDto = [];

            vm.searching = false;

            function pageSize(size) {
                vm.pageable.size = size;
                vm.pageable.page = 0;
                pageSizeResults();
            }


            function pageSizeResults() {
                vm.searching = true;
                $rootScope.showBusyIndicator();
                RecentlyVisitedService.getTopSearchResults(vm.pageable, vm.filters, vm.filter.value).then(
                    function (data) {
                        vm.searchResults = data;
                        if (vm.active == $scope.itemTitle) {
                            if (vm.searchResults.items != null) {
                                ItemService.getLatestRevisionReferences(vm.searchResults.items.content, 'latestRevision');
                            }
                        } else if (vm.active == $scope.ecoTitle) {
                            if (vm.searchResults.ecos != null) {
                                WorkflowService.getWorkflowReferences(vm.searchResults.ecos.content, 'workflow');
                                CommonService.getPersonReferences(vm.searchResults.ecos.content, 'ecoOwner');
                            }
                        } else if (vm.active == $scope.projectTitle) {
                            if (vm.searchResults.projects != null) {
                                CommonService.getPersonReferences(vm.searchResults.projects.content, 'projectManager');
                            }
                        } else if (vm.active == $scope.workflowTitle) {
                            if (vm.searchResults.workflows != null) {
                                CommonService.getPersonReferences(vm.searchResults.workflows.content, 'createdBy');
                            }
                        } else if (vm.active == $scope.mcoTitle) {
                            if (vm.searchResults.mcos != null) {
                                CommonService.getPersonReferences(vm.searchResults.mcos.content, 'changeAnalyst');
                            }
                        } else if (vm.active == $scope.sparePartTitle) {
                            if (vm.searchResults.spareParts != null) {
                                CommonService.getPersonReferences(vm.searchResults.spareParts.content, 'modifiedBy');
                            }
                        } else if (vm.active == $scope.requirementDocumentTitle) {
                            if (vm.searchResults.requirementDocuments != null) {
                                ReqDocumentService.getReqDocLatestRevisionReferences(vm.searchResults.requirementDocuments.content, 'latestRevision');
                            }
                        } else if (vm.active == $scope.requirementTitle) {
                            if (vm.searchResults.requirements != null) {
                                CommonService.getPersonReferences(vm.searchResults.requirements.content, "assignedTo");
                            }
                        }
                        else if (vm.active == $scope.assemblyLineTitle) {
                            if (vm.searchResults.assemblyLines != null) {
                                CommonService.getPersonReferences(vm.searchResults.assemblyLines.content, "modifiedBy");
                            }
                        } else if (vm.active == $scope.nprTitle) {
                            if (vm.searchResults.nprs != null) {
                                CommonService.getPersonReferences(vm.searchResults.nprs.content, 'requester');
                            }
                        }
                        vm.searching = false;
                        $scope.$evalAsync();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            vm.searchResponse = null;
            function performSearch() {
                vm.searchResponse = null;
                var searchStart = new Date();

                vm.searching = true;
                vm.detailsFound = true;
                vm.searchText = vm.searchText.trim();
                $rootScope.hideSidePanel();
                if (vm.searchText == "") {
                    $('#search-results-mask-panel').hide();
                    $('#search-results-container').slideUp(200);
                }
                if (vm.searchText != "") {
                    vm.pageable.page = 0;
                    if (vm.filter.label == $scope.allTitle) {
                        vm.filter.value = "ALL";
                    } else if (vm.filter.label == $scope.designDataTitle) {
                        vm.filter.value = "DESIGN_DATA";
                    } else if (vm.filter.label == $scope.qualityTitle) {
                        vm.filter.value = "QUALITY";
                    } else if (vm.filter.label == $scope.manufacturingTitle) {
                        vm.filter.value = "MANUFACTURING";
                    } else if (vm.filter.label == $scope.maintenanceTitle) {
                        vm.filter.value = "MAINTENANCE";
                    } else if (vm.filter.label == $scope.changesTitle) {
                        vm.filter.value = "CHANGES";
                    } else if (vm.filter.label == $scope.oemTitle) {
                        vm.filter.value = "OEM";
                    } else if (vm.filter.label == $scope.complianceTitle) {
                        vm.filter.value = "COMPLIANCE";
                    }
                    vm.filters.searchQuery = vm.searchText;
                    $('#search-results-mask-panel').show();
                    $('#search-results-container').slideDown(200);
                    $rootScope.showBusyIndicator();
                    RecentlyVisitedService.getTopSearchResults(vm.pageable, vm.filters, vm.filter.value).then(
                        function (data) {
                            vm.searchResults = data;

                            var searchFinish = new Date();
                            var duration = (searchFinish.getTime() - searchStart.getTime()) / 1000;
                            vm.searchResponse = duration.toFixed(2) + " seconds";

                            $('.search-input-container .dropdown-menu').hide();
                            if (vm.filter.label == $scope.allTitle) {
                                if (vm.searchResults.itemsCount > 0) {
                                    vm.active = $scope.itemTitle;
                                } else if (vm.searchResults.pdmVaultCount > 0) {
                                    vm.active = $scope.vaultsTitle;
                                } else if (vm.searchResults.pdmAssemblyCount > 0) {
                                    vm.active = $scope.assembliesTitle;
                                } else if (vm.searchResults.pdmPartCount > 0) {
                                    vm.active = $scope.partsTitle;
                                } else if (vm.searchResults.pdmDrawingCount > 0) {
                                    vm.active = $scope.drawingsTitle;
                                } else if (vm.searchResults.ecrCount > 0) {
                                    vm.active = $scope.ecrTitle;
                                } else if (vm.searchResults.ecoCount > 0) {
                                    vm.active = $scope.ecoTitle;
                                } else if (vm.searchResults.dcoCount > 0) {
                                    vm.active = $scope.dcoTitle;
                                } else if (vm.searchResults.dcrCount > 0) {
                                    vm.active = $scope.dcrTitle;
                                } else if (vm.searchResults.mcoCount > 0) {
                                    vm.active = $scope.mcoTitle;
                                } else if (vm.searchResults.deviationCount > 0) {
                                    vm.active = $scope.deviationTitle;
                                } else if (vm.searchResults.waiverCount > 0) {
                                    vm.active = $scope.waiverTitle;
                                } else if (vm.searchResults.productInspectionPlanCount > 0) {
                                    vm.active = $scope.productInspectionPlanTitle;
                                } else if (vm.searchResults.materialInspectionPlanCount > 0) {
                                    vm.active = $scope.materialInspectionPlanTitle;
                                } else if (vm.searchResults.itemInspectionCount > 0) {
                                    vm.active = $scope.itemInspectionTitle;
                                } else if (vm.searchResults.materialInspectionCount > 0) {
                                    vm.active = $scope.materialInspectionTitle;
                                } else if (vm.searchResults.problemReportCount > 0) {
                                    vm.active = $scope.problemReportTitle;
                                } else if (vm.searchResults.ncrCount > 0) {
                                    vm.active = $scope.ncrTitle;
                                } else if (vm.searchResults.qcrCount > 0) {
                                    vm.active = $scope.qcrTitle;
                                } else if (vm.searchResults.ppapCount > 0) {
                                    vm.active = $scope.ppapTitle;
                                } else if (vm.searchResults.supplierAuditCount > 0) {
                                    vm.active = $scope.supplierAuditTitle;
                                } else if (vm.searchResults.plantCount > 0) {
                                    vm.active = $scope.plantsTitle;
                                } else if (vm.searchResults.assemblyLineCount > 0) {
                                    vm.active = $scope.assemblyLineTitle;
                                } else if (vm.searchResults.workCenterCount > 0) {
                                    vm.active = $scope.workCentersTitle;
                                } else if (vm.searchResults.machineCount > 0) {
                                    vm.active = $scope.machinesTitle;
                                } else if (vm.searchResults.equipmentCount > 0) {
                                    vm.active = $scope.equipmentsTitle;
                                } else if (vm.searchResults.instrumentCount > 0) {
                                    vm.active = $scope.instrumentsTitle;
                                } else if (vm.searchResults.toolCount > 0) {
                                    vm.active = $scope.toolsTitle;
                                } else if (vm.searchResults.jigCount > 0) {
                                    vm.active = $scope.jigsTitle;
                                } else if (vm.searchResults.fixtureCount > 0) {
                                    vm.active = $scope.fixturesTitle;
                                } else if (vm.searchResults.materialCount > 0) {
                                    vm.active = $scope.materialsTitle;
                                } else if (vm.searchResults.manPowerCount > 0) {
                                    vm.active = $scope.manpowersTitle;
                                } else if (vm.searchResults.shiftCount > 0) {
                                    vm.active = $scope.shiftsTitle;
                                } else if (vm.searchResults.operationCount > 0) {
                                    vm.active = $scope.operationsTitle;
                                } else if (vm.searchResults.assetCount > 0) {
                                    vm.active = $scope.assetTitle;
                                } else if (vm.searchResults.meterCount > 0) {
                                    vm.active = $scope.meterTitle;
                                } else if (vm.searchResults.sparePartCount > 0) {
                                    vm.active = $scope.sparePartTitle;
                                } else if (vm.searchResults.maintenancePlanCount > 0) {
                                    vm.active = $scope.maintenancePlanTitle;
                                } else if (vm.searchResults.workRequestCount > 0) {
                                    vm.active = $scope.workRequestTitle;
                                } else if (vm.searchResults.workOrderCount > 0) {
                                    vm.active = $scope.workOrderTitle;
                                } else if (vm.searchResults.mfrCount > 0) {
                                    vm.active = $scope.manufacturerTitle;
                                } else if (vm.searchResults.mfrPartCount > 0) {
                                    vm.active = $scope.manufacturerPartTitle;
                                } else if (vm.searchResults.supplierCount > 0) {
                                    vm.active = $scope.supplierTitle;
                                } else if (vm.searchResults.pgcSubstanceCount > 0) {
                                    vm.active = $scope.substanceTitle;
                                } else if (vm.searchResults.pgcSpecificationCount > 0) {
                                    vm.active = $scope.pgcSpecificationTitle;
                                } else if (vm.searchResults.pgcDeclarationCount > 0) {
                                    vm.active = $scope.pgcDeclarationTitle;
                                } else if (vm.searchResults.projectsCount > 0) {
                                    vm.active = $scope.projectTitle;
                                } else if (vm.searchResults.programCount > 0) {
                                    vm.active = $scope.programTitle;
                                } else if (vm.searchResults.requirementDocumentsCount > 0) {
                                    vm.active = $scope.requirementDocumentTitle;
                                } else if (vm.searchResults.requirementsCount > 0) {
                                    vm.active = $scope.requirementTitle;
                                } else if (vm.searchResults.workflowCount > 0) {
                                    vm.active = $scope.workflowTitle;
                                } else if (vm.searchResults.filesCount > 0) {
                                    vm.active = $scope.fileTitle;
                                } else if (vm.searchResults.documentCount > 0) {
                                    vm.active = $scope.documentTitle;
                                } else if (vm.searchResults.customObjectCount > 0) {
                                    vm.active = $scope.customObjectTitle;
                                } else if (vm.searchResults.nprCount > 0) {
                                    vm.active = $scope.nprTitle;
                                } else if (vm.searchResults.tagsCount > 0) {
                                    vm.active = $scope.tagsTitle;
                                } else {
                                    vm.detailsFound = false;
                                    vm.active = "";
                                }
                            } else if (vm.filter.label == $scope.changesTitle) {
                                if (vm.searchResults.ecrCount > 0) {
                                    vm.active = $scope.ecrTitle;
                                } else if (vm.searchResults.ecoCount > 0) {
                                    vm.active = $scope.ecoTitle;
                                } else if (vm.searchResults.dcoCount > 0) {
                                    vm.active = $scope.dcoTitle;
                                } else if (vm.searchResults.dcrCount > 0) {
                                    vm.active = $scope.dcrTitle;
                                } else if (vm.searchResults.mcoCount > 0) {
                                    vm.active = $scope.mcoTitle;
                                } else if (vm.searchResults.deviationCount > 0) {
                                    vm.active = $scope.deviationTitle;
                                } else if (vm.searchResults.waiverCount > 0) {
                                    vm.active = $scope.waiverTitle;
                                } else {
                                    vm.detailsFound = false;
                                    vm.active = "";
                                }
                            } else if (vm.filter.label == $scope.oemTitle) {
                                if (vm.searchResults.mfrCount > 0) {
                                    vm.active = $scope.manufacturerTitle;
                                } else if (vm.searchResults.mfrPartCount > 0) {
                                    vm.active = $scope.manufacturerPartTitle;
                                } else if (vm.searchResults.supplierCount > 0) {
                                    vm.active = $scope.supplierTitle;
                                } else {
                                    vm.detailsFound = false;
                                    vm.active = "";
                                }
                            } else if (vm.filter.label == $scope.designDataTitle) {
                                if (vm.searchResults.pdmVaultCount > 0) {
                                    vm.active = $scope.vaultsTitle;
                                } else if (vm.searchResults.pdmAssemblyCount > 0) {
                                    vm.active = $scope.assembliesTitle;
                                } else if (vm.searchResults.pdmPartCount > 0) {
                                    vm.active = $scope.partsTitle;
                                } else if (vm.searchResults.pdmDrawingCount > 0) {
                                    vm.active = $scope.drawingsTitle;
                                } else {
                                    vm.detailsFound = false;
                                    vm.active = "";
                                }
                            } else if (vm.filter.label == $scope.qualityTitle) {
                                if (vm.searchResults.productInspectionPlanCount > 0) {
                                    vm.active = $scope.productInspectionPlanTitle;
                                } else if (vm.searchResults.materialInspectionPlanCount > 0) {
                                    vm.active = $scope.materialInspectionPlanTitle;
                                } else if (vm.searchResults.itemInspectionCount > 0) {
                                    vm.active = $scope.itemInspectionTitle;
                                } else if (vm.searchResults.materialInspectionCount > 0) {
                                    vm.active = $scope.materialInspectionTitle;
                                } else if (vm.searchResults.problemReportCount > 0) {
                                    vm.active = $scope.problemReportTitle;
                                } else if (vm.searchResults.ncrCount > 0) {
                                    vm.active = $scope.ncrTitle;
                                } else if (vm.searchResults.qcrCount > 0) {
                                    vm.active = $scope.qcrTitle;
                                } else if (vm.searchResults.ppapCount > 0) {
                                    vm.active = $scope.ppapTitle;
                                } else if (vm.searchResults.supplierAuditCount > 0) {
                                    vm.active = $scope.supplierAuditTitle;
                                } else {
                                    vm.detailsFound = false;
                                    vm.active = "";
                                }
                            } else if (vm.filter.label == $scope.manufacturingTitle) {
                                if (vm.searchResults.plantCount > 0) {
                                    vm.active = $scope.plantsTitle;
                                } else if (vm.searchResults.assemblyLineCount > 0) {
                                    vm.active = $scope.assemblyLineTitle;
                                } else if (vm.searchResults.workCenterCount > 0) {
                                    vm.active = $scope.workCentersTitle;
                                } else if (vm.searchResults.machineCount > 0) {
                                    vm.active = $scope.machinesTitle;
                                } else if (vm.searchResults.equipmentCount > 0) {
                                    vm.active = $scope.equipmentsTitle;
                                } else if (vm.searchResults.instrumentCount > 0) {
                                    vm.active = $scope.instrumentsTitle;
                                } else if (vm.searchResults.toolCount > 0) {
                                    vm.active = $scope.toolsTitle;
                                } else if (vm.searchResults.jigCount > 0) {
                                    vm.active = $scope.jigsTitle;
                                } else if (vm.searchResults.fixtureCount > 0) {
                                    vm.active = $scope.fixturesTitle;
                                } else if (vm.searchResults.materialCount > 0) {
                                    vm.active = $scope.materialsTitle;
                                } else if (vm.searchResults.manPowerCount > 0) {
                                    vm.active = $scope.manpowersTitle;
                                } else if (vm.searchResults.shiftCount > 0) {
                                    vm.active = $scope.shiftsTitle;
                                } else if (vm.searchResults.operationCount > 0) {
                                    vm.active = $scope.operationsTitle;
                                } else {
                                    vm.detailsFound = false;
                                    vm.active = "";
                                }
                            } else if (vm.filter.label == $scope.maintenanceTitle) {
                                if (vm.searchResults.assetCount > 0) {
                                    vm.active = $scope.assetTitle;
                                } else if (vm.searchResults.meterCount > 0) {
                                    vm.active = $scope.meterTitle;
                                } else if (vm.searchResults.sparePartCount > 0) {
                                    vm.active = $scope.sparePartTitle;
                                } else if (vm.searchResults.maintenancePlanCount > 0) {
                                    vm.active = $scope.maintenancePlanTitle;
                                } else if (vm.searchResults.workRequestCount > 0) {
                                    vm.active = $scope.workRequestTitle;
                                } else if (vm.searchResults.workOrderCount > 0) {
                                    vm.active = $scope.workOrderTitle;
                                } else {
                                    vm.detailsFound = false;
                                    vm.active = "";
                                }
                            } else if (vm.filter.label == $scope.complianceTitle) {
                                if (vm.searchResults.pgcSubstanceCount > 0) {
                                    vm.active = $scope.substanceTitle;
                                } else if (vm.searchResults.pgcSpecificationCount > 0) {
                                    vm.active = $scope.pgcSpecificationTitle;
                                } else if (vm.searchResults.pgcDeclarationCount > 0) {
                                    vm.active = $scope.pgcDeclarationTitle;
                                } else {
                                    vm.detailsFound = false;
                                    vm.active = "";
                                }
                            } else if (vm.filter.label == $scope.itemTitle && vm.searchResults.itemsCount == 0) {
                                vm.detailsFound = false;
                            } else if (vm.filter.label == $scope.nprTitle && vm.searchResults.nprCount == 0) {
                                vm.detailsFound = false;
                            } else if (vm.filter.label == $scope.tagsTitle && vm.searchResults.tagsCount == 0) {
                                vm.detailsFound = false;
                            } else if (vm.filter.label == $scope.projectTitle && vm.searchResults.projectsCount == 0) {
                                vm.detailsFound = false;
                            } else if (vm.filter.label == $scope.programTitle && vm.searchResults.programCount == 0) {
                                vm.detailsFound = false;
                            } else if (vm.filter.label == $scope.requirementDocumentTitle && vm.searchResults.requirementDocumentsCount == 0) {
                                vm.detailsFound = false;
                            } else if (vm.filter.label == $scope.requirementTitle && vm.searchResults.requirementsCount == 0) {
                                vm.detailsFound = false;
                            } else if (vm.filter.label == $scope.workflowTitle && vm.searchResults.workflowCount == 0) {
                                vm.detailsFound = false;
                            } else if (vm.filter.label == $scope.fileTitle && vm.searchResults.filesCount == 0) {
                                vm.detailsFound = false;
                            } else if (vm.filter.label == $scope.documentTitle && vm.searchResults.documentCount == 0) {
                                vm.detailsFound = false;
                            } else if (vm.filter.label == $scope.customObjectTitle && vm.searchResults.customObjectCount == 0) {
                                vm.detailsFound = false;
                            }

                            if (vm.active == $scope.itemTitle) {
                                if (vm.searchResults.items != null) {
                                    ItemService.getLatestRevisionReferences(vm.searchResults.items.content, 'latestRevision');
                                }
                            } else if (vm.active == $scope.ecoTitle) {
                                if (vm.searchResults.ecos != null) {
                                    WorkflowService.getWorkflowReferences(vm.searchResults.ecos.content, 'workflow');
                                    CommonService.getPersonReferences(vm.searchResults.ecos.content, 'ecoOwner');
                                }
                            } else if (vm.active == $scope.projectTitle) {
                                if (vm.searchResults.projects != null) {
                                    CommonService.getPersonReferences(vm.searchResults.projects.content, 'projectManager');
                                }
                            } else if (vm.active == $scope.workflowTitle) {
                                if (vm.searchResults.workflows != null) {
                                    CommonService.getPersonReferences(vm.searchResults.workflows.content, 'createdBy');
                                }
                            } else if (vm.active == $scope.mcoTitle) {
                                if (vm.searchResults.mcos != null) {
                                    CommonService.getPersonReferences(vm.searchResults.mcos.content, 'changeAnalyst');
                                }
                            } else if (vm.active == $scope.sparePartTitle) {
                                if (vm.searchResults.spareParts != null) {
                                    CommonService.getPersonReferences(vm.searchResults.spareParts.content, 'modifiedBy');
                                }
                            } else if (vm.active == $scope.requirementDocumentTitle) {
                                if (vm.searchResults.requirementDocuments != null) {
                                    ReqDocumentService.getReqDocLatestRevisionReferences(vm.searchResults.requirementDocuments.content, 'latestRevision');
                                }
                            } else if (vm.active == $scope.requirementTitle) {
                                if (vm.searchResults.requirements != null) {
                                    CommonService.getPersonReferences(vm.searchResults.requirements.content, "assignedTo");
                                }
                            } else if (vm.active == $scope.assemblyLineTitle) {
                                if (vm.searchResults.assemblyLines != null) {
                                    CommonService.getPersonReferences(vm.searchResults.assemblyLines.content, "modifiedBy");
                                }
                            } else if (vm.active == $scope.nprTitle) {
                                if (vm.searchResults.nprs != null) {
                                    CommonService.getPersonReferences(vm.searchResults.nprs.content, 'requester');
                                }
                            }

                            vm.searching = false;
                            $scope.$evalAsync();
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }
            }

            function setFilter(filter) {
                vm.filter.label = filter;
                if (vm.filter.label == $scope.itemTitle) {
                    vm.filter.value = "ITEM";
                    vm.active = $scope.itemTitle;
                } else if (vm.filter.label == $scope.designDataTitle) {
                    vm.filter.value = "DESIGN_DATA";
                    vm.active = $scope.designDataTitle;
                } else if (vm.filter.label == $scope.projectTitle) {
                    vm.filter.value = "PROJECT";
                    vm.active = $scope.projectTitle;
                } else if (vm.filter.label == $scope.programTitle) {
                    vm.filter.value = "PROGRAM";
                    vm.active = $scope.programTitle;
                } else if (vm.filter.label == $scope.requirementTitle) {
                    vm.filter.value = "REQUIREMENT";
                    vm.active = $scope.requirementTitle;
                } else if (vm.filter.label == $scope.requirementDocumentTitle) {
                    vm.filter.value = "REQUIREMENTDOCUMENT";
                    vm.active = $scope.requirementDocumentTitle;
                } else if (vm.filter.label == $scope.workflowTitle) {
                    vm.filter.value = "WORKFLOW";
                    vm.active = $scope.workflowTitle;
                } else if (vm.filter.label == $scope.fileTitle) {
                    vm.filter.value = "FILE";
                    vm.active = $scope.fileTitle;
                } else if (vm.filter.label == $scope.documentTitle) {
                    vm.filter.value = "DOCUMENT";
                    vm.active = $scope.documentTitle;
                } else if (vm.filter.label == $scope.changesTitle) {
                    vm.filter.value = "CHANGES";
                } else if (vm.filter.label == $scope.qualityTitle) {
                    vm.filter.value = "QUALITY";
                } else if (vm.filter.label == $scope.manufacturingTitle) {
                    vm.filter.value = "MANUFACTURING";
                } else if (vm.filter.label == $scope.maintenanceTitle) {
                    vm.filter.value = "MAINTENANCE";
                } else if (vm.filter.label == $scope.complianceTitle) {
                    vm.filter.value = "COMPLIANCE";
                } else if (vm.filter.label == $scope.customObjectTitle) {
                    vm.filter.value = "CUSTOMOBJECT";
                    vm.active = $scope.customObjectTitle;
                } else if (vm.filter.label == $scope.nprTitle) {
                    vm.filter.value = "NPR";
                    vm.active = $scope.nprTitle;
                } else if (vm.filter.label == $scope.tagsTitle) {
                    vm.filter.value = "TAG";
                    vm.active = $scope.tagsTitle;
                } else {
                    vm.active = $scope.allTitle;
                    vm.filter.value = "ALL";
                }

                $('.search-input-container .dropdown-menu').hide();
                performSearch();
            }

            vm.filters = {
                searchQuery: null,
                objectType: ''
            }

            vm.activateTab = activateTab;
            function activateTab(tab) {
                $rootScope.showBusyIndicator();
                vm.searchResponse = null;
                var searchStart = new Date();

                vm.filter.value = tab;
                vm.filters.searchQuery = vm.searchText;
                vm.pageable.page = 0;
                RecentlyVisitedService.getTopSearchResults(vm.pageable, vm.filters, vm.filter.value).then(
                    function (data) {
                        vm.searchResults = data;

                        var searchFinish = new Date();
                        var duration = (searchFinish.getTime() - searchStart.getTime()) / 1000;
                        vm.searchResponse = duration.toFixed(2) + " seconds";

                        if (vm.active == $scope.itemTitle) {
                            ItemService.getLatestRevisionReferences(vm.searchResults.items.content, 'latestRevision');
                        } else if (vm.active == $scope.ecoTitle) {
                            CommonService.getPersonReferences(vm.searchResults.ecos.content, 'ecoOwner');
                            WorkflowService.getWorkflowReferences(vm.searchResults.ecos.content, 'workflow');
                        } else if (vm.active == $scope.projectTitle) {
                            CommonService.getPersonReferences(vm.searchResults.projects.content, 'projectManager');
                        } else if (vm.active == $scope.workflowTitle) {
                            CommonService.getPersonReferences(vm.searchResults.workflows.content, 'createdBy');
                        } else if (vm.active == $scope.mcoTitle) {
                            CommonService.getPersonReferences(vm.searchResults.mcos.content, 'changeAnalyst');
                        } else if (vm.active == $scope.sparePartTitle) {
                            if (vm.searchResults.spareParts != null) {
                                CommonService.getPersonReferences(vm.searchResults.spareParts.content, 'modifiedBy');
                            }
                        } else if (vm.active == $scope.requirementDocumentTitle) {
                            if (vm.searchResults.requirementDocuments != null) {
                                ReqDocumentService.getReqDocLatestRevisionReferences(vm.searchResults.requirementDocuments.content, 'latestRevision');
                            }
                        } else if (vm.active == $scope.requirementTitle) {
                            if (vm.searchResults.requirements != null) {
                                CommonService.getPersonReferences(vm.searchResults.requirements.content, "assignedTo");
                            }
                        } else if (vm.active == $scope.assemblyLineTitle) {
                            if (vm.searchResults.assemblyLines != null) {
                                CommonService.getPersonReferences(vm.searchResults.assemblyLines.content, "modifiedBy");
                            }
                        } else if (vm.active == $scope.nprTitle) {
                            if (vm.searchResults.nprs != null) {
                                CommonService.getPersonReferences(vm.searchResults.nprs.content, 'requester');
                            }
                        }
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function nextPage() {
                if (!vm.searchResults.last) {
                    vm.pageable.page++;
                    $rootScope.showBusyIndicator();
                    RecentlyVisitedService.getTopSearchResults(vm.pageable, vm.filters, vm.filter.value).then(
                        function (data) {
                            vm.searchResults = data;

                            if (vm.active == $scope.itemTitle) {
                                ItemService.getLatestRevisionReferences(vm.searchResults.items.content, 'latestRevision');
                            } else if (vm.active == $scope.ecoTitle) {
                                CommonService.getPersonReferences(vm.searchResults.ecos.content, 'ecoOwner');
                                WorkflowService.getWorkflowReferences(vm.searchResults.ecos.content, 'workflow');
                            } else if (vm.active == $scope.projectTitle) {
                                CommonService.getPersonReferences(vm.searchResults.projects.content, 'projectManager');
                            } else if (vm.active == $scope.workflowTitle) {
                                CommonService.getPersonReferences(vm.searchResults.workflows.content, 'createdBy');
                            } else if (vm.active == $scope.mcoTitle) {
                                CommonService.getPersonReferences(vm.searchResults.mcos.content, 'changeAnalyst');
                            } else if (vm.active == $scope.sparePartTitle) {
                                if (vm.searchResults.spareParts != null) {
                                    CommonService.getPersonReferences(vm.searchResults.spareParts.content, 'modifiedBy');
                                }
                            } else if (vm.active == $scope.requirementDocumentTitle) {
                                if (vm.searchResults.requirementDocuments != null) {
                                    ReqDocumentService.getReqDocLatestRevisionReferences(vm.searchResults.requirementDocuments.content, 'latestRevision');
                                }
                            } else if (vm.active == $scope.requirementTitle) {
                                if (vm.searchResults.requirements != null) {
                                    CommonService.getPersonReferences(vm.searchResults.requirements.content, "assignedTo");
                                }
                            } else if (vm.active == $scope.assemblyLineTitle) {
                                if (vm.searchResults.assemblyLines != null) {
                                    CommonService.getPersonReferences(vm.searchResults.assemblyLines.content, "modifiedBy");
                                }
                            } else if (vm.active == $scope.nprTitle) {
                                if (vm.searchResults.nprs != null) {
                                    CommonService.getPersonReferences(vm.searchResults.nprs.content, 'requester');
                                }
                            }
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }
            }

            function previousPage() {
                if (!vm.searchResults.first) {
                    vm.pageable.page--;
                    $rootScope.showBusyIndicator();
                    RecentlyVisitedService.getTopSearchResults(vm.pageable, vm.filters, vm.filter.value).then(
                        function (data) {
                            vm.searchResults = data;
                            if (vm.active == $scope.itemTitle) {
                                ItemService.getLatestRevisionReferences(vm.searchResults.items.content, 'latestRevision');
                            } else if (vm.active == $scope.ecoTitle) {
                                CommonService.getPersonReferences(vm.searchResults.ecos.content, 'ecoOwner');
                                WorkflowService.getWorkflowReferences(vm.searchResults.ecos.content, 'workflow');
                            } else if (vm.active == $scope.projectTitle) {
                                CommonService.getPersonReferences(vm.searchResults.projects.content, 'projectManager');
                            } else if (vm.active == $scope.workflowTitle) {
                                CommonService.getPersonReferences(vm.searchResults.workflows.content, 'createdBy');
                            } else if (vm.active == $scope.mcoTitle) {
                                CommonService.getPersonReferences(vm.searchResults.mcos.content, 'changeAnalyst');
                            } else if (vm.active == $scope.sparePartTitle) {
                                if (vm.searchResults.spareParts != null) {
                                    CommonService.getPersonReferences(vm.searchResults.spareParts.content, 'modifiedBy');
                                }
                            } else if (vm.active == $scope.requirementDocumentTitle) {
                                if (vm.searchResults.requirementDocuments != null) {
                                    ReqDocumentService.getReqDocLatestRevisionReferences(vm.searchResults.requirementDocuments.content, 'latestRevision');
                                }
                            } else if (vm.active == $scope.requirementTitle) {
                                if (vm.searchResults.requirements != null) {
                                    CommonService.getPersonReferences(vm.searchResults.requirements.content, "assignedTo");
                                }
                            } else if (vm.active == $scope.assemblyLineTitle) {
                                if (vm.searchResults.assemblyLines != null) {
                                    CommonService.getPersonReferences(vm.searchResults.assemblyLines.content, "modifiedBy");
                                }
                            } else if (vm.active == $scope.nprTitle) {
                                if (vm.searchResults.nprs != null) {
                                    CommonService.getPersonReferences(vm.searchResults.nprs.content, 'requester');
                                }
                            }
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }
            }

            vm.recentlyVisited = {
                id: null,
                objectId: null,
                objectType: null,
                person: null,
                visitedDate: null
            };
            var session = JSON.parse(localStorage.getItem('local_storage_login'));

            vm.showPpap = showPpap;
            function showPpap(ppap) {
                $state.go('app.pqm.ppap.details', {
                    ppapId: ppap.id,
                    tab: 'details.basic'
                });
            }

            function openItemDetails(item) {
                $('#search-results-container').hide();
                $state.go('app.items.details', {itemId: item.latestRevision});
                vm.recentlyVisited.objectId = item.id;
                vm.recentlyVisited.objectType = item.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function openEcoDetails(eco) {
                $('#search-results-container').hide();
                $state.go('app.changes.eco.details', {ecoId: eco.id});
                vm.recentlyVisited.objectId = eco.id;
                vm.recentlyVisited.objectType = eco.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function openGlossaryDetails(glossary) {
                $('#search-results-container').hide();
                $state.go('app.rm.glossary.details', {glossaryId: glossary.id});
                vm.recentlyVisited.objectId = glossary.id;
                vm.recentlyVisited.objectType = glossary.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function openCustomObjectDetails(customObject) {
                $('#search-results-container').hide();
                $state.go('app.customobjects.details', {customId: customObject.id, tab: 'details.basic'});
            }

            function openProjectDetails(project) {
                $('#search-results-container').hide();
                $state.go('app.pm.project.details', {projectId: project.id})
                vm.recentlyVisited.objectId = project.id;
                vm.recentlyVisited.objectType = project.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function openReqDetails(requirement) {
                $('#search-results-container').hide();
                $state.go('app.rm.requirements.details', {requirementId: requirement.id})
                vm.recentlyVisited.objectId = requirement.id;
                vm.recentlyVisited.objectType = requirement.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function openWorkflowDetails(workflow) {
                $('#search-results-container').hide();
                $state.go('app.workflow.editor', {mode: 'edit', workflow: workflow.id});
                vm.recentlyVisited.objectId = workflow.id;
                vm.recentlyVisited.objectType = workflow.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $scope.showEcrDetails = showEcrDetails;
            $scope.showDcoDetails = showDcoDetails;
            $scope.showDcrDetails = showDcrDetails;
            $scope.showMcoDetails = showMcoDetails;
            $scope.showVarianceDetails = showVarianceDetails;
            $scope.showPlanDetails = showPlanDetails;
            $scope.showInspectionDetails = showInspectionDetails;
            $scope.showPrDetails = showPrDetails;
            $scope.showNcrDetails = showNcrDetails;
            $scope.showQcrDetails = showQcrDetails;
            function showEcrDetails(ecr) {
                $state.go('app.changes.ecr.details', {ecrId: ecr.id});
                $('#search-results-container').hide();
            }

            function showDcoDetails(dco) {
                $state.go('app.changes.dco.details', {dcoId: dco.id});
                $('#search-results-container').hide();
            }

            function showDcrDetails(dcr) {
                $state.go('app.changes.dcr.details', {dcrId: dcr.id});
                $('#search-results-container').hide();
            }

            function showMcoDetails(mco) {
                $state.go('app.changes.mco.details', {mcoId: mco.id});
                $('#search-results-container').hide();
            }

            function showVarianceDetails(variance) {
                $state.go('app.changes.variance.details', {varianceId: variance.id});
                $('#search-results-container').hide();
            }

            function showInspectionDetails(inspection) {
                $state.go('app.pqm.inspection.details', {inspectionId: inspection.id});
                $('#search-results-container').hide();
            }

            function showPrDetails(report) {
                $state.go('app.pqm.pr.details', {problemReportId: report.id});
                $('#search-results-container').hide();
            }

            function showNcrDetails(ncr) {
                $state.go('app.pqm.ncr.details', {ncrId: ncr.id});
                $('#search-results-container').hide();
            }

            function showQcrDetails(qcr) {
                $state.go('app.pqm.qcr.details', {qcrId: qcr.id});
                $('#search-results-container').hide();
            }

            function showPlanDetails(plant) {
                $state.go('app.mes.masterData.plant.details', {plantId: plant.id, tab: 'details.basic'});
                $('#search-results-container').hide();
            }

            vm.showWorkCenter = showWorkCenter;
            function showWorkCenter(workCenter) {
                $state.go('app.mes.masterData.workcenter.details', {workcenterId: workCenter.id, tab: 'details.basic'});
                $('#search-results-container').hide();
            }

            vm.showMachine = showMachine;
            function showMachine(machine) {
                $state.go('app.mes.masterData.machine.details', {machineId: machine.id, tab: 'details.basic'});
                $('#search-results-container').hide();
            }

            vm.showInstrument = showInstrument;
            function showInstrument(instrument) {
                $state.go('app.mes.masterData.instrument.details', {instrumentId: instrument.id, tab: 'details.basic'});
                $('#search-results-container').hide();
            }

            vm.showEquipment = showEquipment;
            function showEquipment(equipment) {
                $state.go('app.mes.masterData.equipment.details', {equipmentId: equipment.id, tab: 'details.basic'});
                $('#search-results-container').hide();
            }

            vm.showTool = showTool;
            function showTool(tool) {
                $state.go('app.mes.masterData.tool.details', {toolId: tool.id, tab: 'details.basic'});
                $('#search-results-container').hide();
            }

            vm.showJigsFixture = showJigsFixture;
            function showJigsFixture(jigFixture) {
                $state.go('app.mes.masterData.jigsAndFixtures.details', {
                    jigsFixId: jigFixture.id,
                    tab: 'details.basic'
                });
                $('#search-results-container').hide();
            }

            vm.showMaterial = showMaterial;
            function showMaterial(material) {
                $state.go('app.mes.masterData.material.details', {materialId: material.id, tab: 'details.basic'});
                $('#search-results-container').hide();
            }

            vm.showManpower = showManpower;
            function showManpower(manpower) {
                $state.go('app.mes.masterData.manpower.details', {manpowerId: manpower.id, tab: 'details.basic'});
                $('#search-results-container').hide();
            }

            vm.showShift = showShift;
            function showShift(shift) {
                $state.go('app.mes.masterData.shift.details', {shiftId: shift.id, tab: 'details.basic'});
                $('#search-results-container').hide();
            }

            vm.showOperation = showOperation;
            function showOperation(operation) {
                $state.go('app.mes.masterData.operation.details', {operationId: operation.id, tab: 'details.basic'});
                $('#search-results-container').hide();
            }

            vm.showMfrDetails = showMfrDetails;
            function showMfrDetails(manufacturerId) {
                $state.go('app.mfr.details', {manufacturerId: manufacturerId, tab: 'details.basic'});
                $('#search-results-container').hide();
            }

            vm.showMfrPartDetails = showMfrPartDetails;
            function showMfrPartDetails(part) {
                $state.go('app.mfr.mfrparts.details', {mfrId: part.manufacturer, manufacturePartId: part.id});
                $('#search-results-container').hide();
            }

            vm.showSupplier = showSupplier;
            function showSupplier(supplier) {
                $state.go('app.mfr.supplier.details', {supplierId: supplier.id, tab: 'details.basic'});
                $('#search-results-container').hide();
            }

            vm.showAsset = showAsset;
            function showAsset(asset) {
                $state.go('app.mro.asset.details', {assetId: asset.id, tab: 'details.basic'});
                $('#search-results-container').hide();
            }

            vm.showMeter = showMeter;
            function showMeter(meter) {
                $state.go('app.mro.meter.details', {meterId: meter.id, tab: 'details.basic'});
                $('#search-results-container').hide();
            }

            vm.showSparePart = showSparePart;
            function showSparePart(sparePart) {
                $state.go('app.mro.sparePart.details', {sparePartId: sparePart.id, tab: 'details.basic'});
                $('#search-results-container').hide();
            }

            vm.showMaintenancePlan = showMaintenancePlan;
            function showMaintenancePlan(maintenancePlan) {
                $state.go('app.mro.maintenancePlan.details', {
                    maintenancePlanId: maintenancePlan.id,
                    tab: 'details.basic'
                });
                $('#search-results-container').hide();
            }

            vm.showWorkRequest = showWorkRequest;
            function showWorkRequest(workRequest) {
                $state.go('app.mro.workRequest.details', {workRequestId: workRequest.id, tab: 'details.basic'});
                $('#search-results-container').hide();
            }

            vm.showWorkOrder = showWorkOrder;
            function showWorkOrder(workOrder) {
                $state.go('app.mro.workOrder.details', {workOrderId: workOrder.id, tab: 'details.basic'});
                $('#search-results-container').hide();
            }

            vm.showSubstance = showSubstance;
            function showSubstance(substance) {
                $state.go('app.compliance.substance.details', {
                    substanceId: substance.id,
                    tab: 'details.basic'
                });
                $('#search-results-container').hide();
            }

            vm.showSpecification = showSpecification;
            function showSpecification(specification) {
                $state.go('app.compliance.specification.details', {
                    specificationId: specification.id,
                    tab: 'details.basic'
                });
                $('#search-results-container').hide();
            }

            vm.showDeclaration = showDeclaration;
            function showDeclaration(declaration) {
                $state.go('app.compliance.declaration.details', {
                    declarationId: declaration.id,
                    tab: 'details.basic'
                });
                $('#search-results-container').hide();
            }

            vm.showSupplierAudit = showSupplierAudit;
            function showSupplierAudit(supplierAudit) {
                $state.go('app.pqm.supplierAudit.details', {
                    supplierAuditId: supplierAudit.id,
                    tab: 'details.basic'
                });
            }

            function openFileDetails(file) {
                if (file.type == "ECO") {
                    $state.go('app.changes.eco.details', {ecoId: file.objectId, tab: "details.files"});
                } else if (file.type == "ECR") {
                    $state.go('app.changes.ecr.details', {ecrId: file.objectId, tab: "details.files"});
                } else if (file.type == "DCO") {
                    $state.go('app.changes.dco.details', {dcoId: file.objectId, tab: "details.files"});
                } else if (file.type == "DCR") {
                    $state.go('app.changes.dcr.details', {dcrId: file.objectId, tab: "details.files"});
                } else if (file.type == "MCO") {
                    $state.go('app.changes.mco.details', {mcoId: file.objectId, tab: "details.files"});
                } else if (file.type == "DEVIATION" || file.type == "WAIVER") {
                    $state.go('app.changes.variance.details', {varianceId: file.objectId, tab: "details.files"});
                } else if (file.type == "ITEM") {
                    $state.go('app.items.details', {itemId: file.parentObjectId, tab: "details.files"});
                } else if (file.type == "PROJECT") {
                    $state.go('app.pm.project.details', {projectId: file.objectId, tab: "details.files"})
                } else if (file.type == "PROJECTACTIVITY") {
                    $state.go('app.pm.project.activity.details', {activityId: file.objectId, tab: 'details.files'});
                } else if (file.type == "PROJECTTASK") {
                    $state.go('app.pm.project.activity.task.details', {
                        activityId: file.parentObjectId,
                        taskId: file.objectId,
                        tab: 'details.basic'
                    });
                } else if (file.type == "GLOSSARY") {
                    $state.go('app.rm.glossary.details', {glossaryId: file.objectId, tab: "details.files"});
                } else if (file.type == "MANUFACTURER") {
                    $state.go('app.mfr.details', {manufacturerId: file.objectId, tab: "details.files"});
                } else if (file.type == "PRODUCTINSPECTIONPLAN" || file.type == "MATERIALINSPECTIONPLAN") {
                    $state.go('app.mfr.details', {planId: file.objectId, tab: "details.files"});
                } else if (file.type == "ITEMINSPECTION" || file.type == "MATERIALINSPECTION") {
                    $state.go('app.mfr.details', {inspectionId: file.objectId, tab: "details.files"});
                } else if (file.type == "PROBLEMREPORT") {
                    $state.go('app.mfr.details', {problemReportId: file.objectId, tab: "details.files"});
                } else if (file.type == "NCR") {
                    $state.go('app.mfr.details', {ncrId: file.objectId, tab: "details.files"});
                } else if (file.type == "QCR") {
                    $state.go('app.mfr.details', {qcrId: file.objectId, tab: "details.files"});
                } else if (file.type == "SPECIFICATION") {
                    $state.go('app.rm.specifications.details', {specId: file.objectId, tab: "details.files"});
                } else if (file.type == "REQUIREMENT") {
                    $state.go('app.rm.requirements.details', {requirementId: file.objectId, tab: "details.files"});
                } else if (file.type == "MANUFACTURERPART") {
                    $state.go('app.mfr.mfrparts.details', {
                        mfrId: file.parentObjectId,
                        manufacturePartId: file.objectId
                    });
                }
                else if (file.type == "MFRSUPPLIER") {
                    $state.go('app.mfr.supplier.details', {supplierId: file.objectId, tab: "details.files"});
                }
                else if (file.type == "PLANT") {
                    $state.go('app.mes.masterData.plant.details', {plantId: file.objectId, tab: "details.files"});
                }
                else if (file.type == "WORKCENTER") {
                    $state.go('app.mes.masterData.workcenter.details', {
                        workcenterId: file.objectId,
                        tab: "details.files"
                    });
                }
                else if (file.type == "MACHINE") {
                    $state.go('app.mes.masterData.machine.details', {machineId: file.objectId, tab: "details.files"});
                }
                else if (file.type == "TOOL") {
                    $state.go('app.mes.masterData.tool.details', {toolId: file.objectId, tab: "details.files"});
                }
                else if (file.type == "OPERATION") {
                    $state.go('app.mes.masterData.operation.details', {
                        operationId: file.objectId,
                        tab: "details.files"
                    });
                }
                else if (file.type == "MATERIAL") {
                    $state.go('app.mes.masterData.material.details', {materialId: file.objectId, tab: "details.files"});
                }
                else if (file.type == "JIGFIXTURE") {
                    $state.go('app.mes.masterData.jigsAndFixtures.details', {
                        jigsFixId: file.objectId,
                        tab: "details.files"
                    });
                }
                else if (file.type == "MANPOWER") {
                    $state.go('app.mes.masterData.manpower.details', {manpowerId: file.objectId, tab: "details.files"});
                }
                else if (file.type == "EQUIPMENT") {
                    $state.go('app.mes.masterData.equipment.details', {
                        equipmentId: file.objectId,
                        tab: "details.files"
                    });
                }
                else if (file.type == "INSTRUMENT") {
                    $state.go('app.mes.masterData.instrument.details', {
                        instrumentId: file.objectId,
                        tab: "details.files"
                    });
                }
                else if (file.type == "SHIFT") {
                    $state.go('app.mes.masterData.shift.details', {shiftId: file.objectId, tab: "details.files"});
                }
                else if (file.type == "MROASSET") {
                    $state.go('app.mro.asset.details', {assetId: file.objectId, tab: "details.files"});
                }
                else if (file.type == "MROMETER") {
                    $state.go('app.mro.meter.details', {meterId: file.objectId, tab: "details.files"});
                }
                else if (file.type == "MROSPAREPART") {
                    $state.go('app.mro.sparePart.details', {sparePartId: file.objectId, tab: "details.files"});
                }
                else if (file.type == "MROMAINTENANCEPLAN") {
                    $state.go('app.mro.maintenancePlan.details', {
                        maintenancePlanId: file.objectId,
                        tab: "details.files"
                    });
                }
                else if (file.type == "MROWORKREQUEST") {
                    $state.go('app.mro.workRequest.details', {workRequestId: file.objectId, tab: "details.files"});
                }
                else if (file.type == "MROWORKORDER") {
                    $state.go('app.mro.workOrder.details', {workOrderId: file.objectId, tab: "details.files"});
                }
                else if (file.type == "PGCSUBSTANCE") {
                    $state.go('app.compliance.substance.details', {substanceId: file.objectId, tab: "details.files"});
                }
                else if (file.type == "PGCSPECIFICATION") {
                    $state.go('app.compliance.specification.details', {
                        specificationId: file.objectId,
                        tab: "details.files"
                    });
                }
                else if (file.type == "PGCDECLARATION") {
                    $state.go('app.compliance.declaration.details', {
                        declarationId: file.objectId,
                        tab: "details.files"
                    });
                }
                else if (file.type == "ASSEMBLYLINE") {
                    $state.go('app.mes.masterData.assemblyline.details', {
                        assemblyLineId: file.objectId,
                        tab: 'details.files'
                    });
                } else if (file.type == "PLMNPR") {
                    $state.go('app.nprs.details', {nprId: file.objectId, tab: 'details.files'});
                } else if (file.type == "CUSTOMOBJECT") {
                    $state.go('app.customobjects.details', {customId: file.objectId, tab: 'details.files'});
                } else if (file.type == "PPAP") {
                    $state.go('app.pqm.ppap.details', {
                        ppapId: file.objectId,
                        tab: 'details.basic'
                    });
                }
                if (file.type == "ITEM") {
                    vm.recentlyVisited.objectId = file.parentObjectId;
                } else {
                    vm.recentlyVisited.objectId = file.objectId;
                }
                vm.recentlyVisited.objectType = file.parentObjectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.showWoMaintenancePlan = showWoMaintenancePlan;
            function showWoMaintenancePlan(workOrder) {
                $state.go('app.mro.maintenancePlan.details', {
                    maintenancePlanId: workOrder.plan,
                    tab: 'details.basic'
                });
            }

            vm.showAssemblyLine = showAssemblyLine;
            function showAssemblyLine(assemblyLine) {
                $state.go('app.mes.masterData.assemblyline.details', {
                    assemblyLineId: assemblyLine.id,
                    tab: 'details.basic'
                });
            }

            vm.showWoWorkRequest = showWoWorkRequest;
            function showWoWorkRequest(workOrder) {
                $state.go('app.mro.workRequest.details', {workRequestId: workOrder.request, tab: 'details.basic'});
            }

            vm.showNpr = showNpr;
            function showNpr(npr) {
                $state.go('app.nprs.details', {nprId: npr.id, tab: 'details.basic'});
            }

            $scope.previewFile = parsed.html($translate.instant("FILE_PREVIEW")).html();
            $scope.filePreview = filePreview;
            function filePreview(file) {
                var itemId = file.objectId;
                var fileId = file.file.id;
                var url = "{0}//{1}/api/plm/objects/{2}/{3}/files/{4}/preview".
                    format(window.location.protocol, window.location.host, itemId, file.parentObjectType, fileId);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = file.name;
                });
                $timeout(function () {
                    window.close();
                }, 2000);
            }

            $scope.documentPreview = documentPreview;
            function documentPreview(file) {
                var url = "{0}//{1}/api/plm/objects/{2}/{3}/files/{4}/preview".
                    format(window.location.protocol, window.location.host, 0, "DOCUMENT", file.id);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = file.name;
                });
                $timeout(function () {
                    window.close();
                }, 2000);
            }

            function preventClick(event) {
                event.stopPropagation();
                event.preventDefault();

            }

            function showFiltersMenu() {
                $('.search-input-container .dropdown-menu').toggle();
                $rootScope.hideSidePanel('right');
            }

            vm.showReqDocument = showReqDocument;
            function showReqDocument(req) {
                $state.go('app.req.document.details', {reqId: req.latestRevision, tab: 'details.basic'});
            }

            vm.showRequirementDetails = showRequirementDetails;
            function showRequirementDetails(req) {
                $state.go('app.req.requirements.details', {requirementId: req.id, tab: 'details.basic'});
            }

            vm.clearSearch = clearSearch;
            function clearSearch() {
                $rootScope.hideSidePanel();
                vm.searchText = '';
                //performSearch();

                $('#search-results-mask-panel').hide();
                $('.search-input-container .dropdown-menu').hide();
                $('#search-results-container').slideUp(200);
            }

            vm.onFocus = onFocus;
            function onFocus() {
                $rootScope.hideSidePanel();
                if (vm.searchText != "") {
                    $('#search-results-mask-panel').show();
                    $('#search-results-container').slideDown(200);
                }
            }

            vm.selectedTagObjectType = null;
            vm.clearTagObjectType = clearTagObjectType;
            function clearTagObjectType() {
                vm.filters.objectType = '';
                activateTab('TAG');
            }

            vm.onSelectTagObjectType = onSelectTagObjectType;
            function onSelectTagObjectType(objectType) {
                vm.filters.objectType = objectType;
                $('#tag-dropdown-menu').hide();
                activateTab('TAG');
                vm.active = $scope.tagsTitle;
            }

            vm.showTagFiltersMenu = showTagFiltersMenu;
            function showTagFiltersMenu() {
                $('#tag-dropdown-menu').toggle();
            }

            vm.showDetails = showDetails;
            function showDetails(tag) {
                if (tag.objectType == "ITEM") {
                    $state.go('app.items.details', {itemId: tag.revisionId});
                } else if (tag.objectType == "ECO") {
                    $state.go('app.changes.eco.details', {ecoId: tag.object});
                } else if (tag.objectType == "CUSTOMOBJECT") {
                    $state.go('app.customobjects.details', {customId: tag.object, tab: 'details.basic'});
                } else if (tag.objectType == "PROJECT") {
                    $state.go('app.pm.project.details', {projectId: tag.object});
                } else if (tag.objectType == "PROJECTACTIVITY") {
                    $state.go('app.pm.project.activity.details', {activityId: tag.object, tab: 'details.basic'});
                } else if (tag.objectType == "PROJECTTASK") {
                    $state.go('app.pm.project.activity.task.details', {
                        activityId: tag.revisionId,
                        taskId: tag.object,
                        tab: 'details.basic'
                    });
                } else if (tag.objectType == "REQUIREMENT") {
                    $state.go('app.rm.requirements.details', {requirementId: tag.object});
                } else if (tag.objectType == "") {
                    $state.go('app.workflow.editor', {mode: 'edit', workflow: tag.object});
                } else if (tag.objectType == "ECR") {
                    $state.go('app.changes.ecr.details', {ecrId: tag.object});
                } else if (tag.objectType == "DCO") {
                    $state.go('app.changes.dco.details', {dcoId: tag.object});
                } else if (tag.objectType == "DCR") {
                    $state.go('app.changes.dcr.details', {dcrId: tag.object});
                } else if (tag.objectType == "MCO") {
                    $state.go('app.changes.mco.details', {mcoId: tag.object});
                } else if (tag.objectType == "DEVIATION" || tag.objectType == "WAIVER") {
                    $state.go('app.changes.variance.details', {varianceId: tag.object});
                } else if (tag.objectType == "ITEMINSPECTION" || tag.objectType == "MATERIALINSPECTION") {
                    $state.go('app.pqm.inspection.details', {inspectionId: tag.object});
                } else if (tag.objectType == "PROBLEMREPORT") {
                    $state.go('app.pqm.pr.details', {problemReportId: tag.object});
                } else if (tag.objectType == "NCR") {
                    $state.go('app.pqm.ncr.details', {ncrId: tag.object});
                } else if (tag.objectType == "QCR") {
                    $state.go('app.pqm.qcr.details', {qcrId: tag.object});
                } else if (tag.objectType == "PLANT") {
                    $state.go('app.mes.masterData.plant.details', {plantId: tag.object, tab: 'details.basic'});
                } else if (tag.objectType == "ASSEMBLYLINE") {
                    $state.go('app.mes.masterData.assemblyline.details', {
                        assemblyLineId: tag.object,
                        tab: 'details.basic'
                    });
                } else if (tag.objectType == "WORKCENTER") {
                    $state.go('app.mes.masterData.workcenter.details', {
                        workcenterId: tag.object,
                        tab: 'details.basic'
                    });
                } else if (tag.objectType == "MACHINE") {
                    $state.go('app.mes.masterData.machine.details', {machineId: tag.object, tab: 'details.basic'});
                } else if (tag.objectType == "INSTRUMENT") {
                    $state.go('app.mes.masterData.instrument.details', {
                        instrumentId: tag.object,
                        tab: 'details.basic'
                    });
                } else if (tag.objectType == "EQUIPMENT") {
                    $state.go('app.mes.masterData.equipment.details', {equipmentId: tag.object, tab: 'details.basic'});
                } else if (tag.objectType == "TOOL") {
                    $state.go('app.mes.masterData.tool.details', {toolId: tag.object, tab: 'details.basic'});
                } else if (tag.objectType == "JIGFIXTURE") {
                    $state.go('app.mes.masterData.jigsAndFixtures.details', {
                        jigsFixId: tag.object,
                        tab: 'details.basic'
                    });
                } else if (tag.objectType == "MATERIAL") {
                    $state.go('app.mes.masterData.material.details', {materialId: tag.object, tab: 'details.basic'});
                } else if (tag.objectType == "MANPOWER") {
                    $state.go('app.mes.masterData.manpower.details', {manpowerId: tag.object, tab: 'details.basic'});
                } else if (tag.objectType == "SHIFT") {
                    $state.go('app.mes.masterData.shift.details', {shiftId: tag.object, tab: 'details.basic'});
                } else if (tag.objectType == "OPERATION") {
                    $state.go('app.mes.masterData.operation.details', {operationId: tag.object, tab: 'details.basic'});
                } else if (tag.objectType == "MANUFACTURER") {
                    $state.go('app.mfr.details', {manufacturerId: tag.object, tab: 'details.basic'});
                } else if (tag.objectType == "MANUFACTURERPART") {
                    $state.go('app.mfr.mfrparts.details', {mfrId: tag.revisionId, manufacturePartId: tag.object});
                } else if (tag.objectType == "MFRSUPPLIER") {
                    $state.go('app.mfr.supplier.details', {supplierId: tag.object, tab: 'details.basic'});
                } else if (tag.objectType == "MROASSET") {
                    $state.go('app.mro.asset.details', {assetId: tag.object, tab: 'details.basic'});
                } else if (tag.objectType == "MROMETER") {
                    $state.go('app.mro.meter.details', {meterId: tag.object, tab: 'details.basic'});
                } else if (tag.objectType == "MROSPAREPART") {
                    $state.go('app.mro.sparePart.details', {sparePartId: tag.object, tab: 'details.basic'});
                } else if (tag.objectType == "MROMAINTENANCEPLAN") {
                    $state.go('app.mro.maintenancePlan.details', {
                        maintenancePlanId: tag.object,
                        tab: 'details.basic'
                    });
                } else if (tag.objectType == "MROWORKREQUEST") {
                    $state.go('app.mro.workRequest.details', {workRequestId: tag.object, tab: 'details.basic'});
                } else if (tag.objectType == "MROWORKORDER") {
                    $state.go('app.mro.workOrder.details', {workOrderId: tag.object, tab: 'details.basic'});
                } else if (tag.objectType == "PGCSUBSTANCE") {
                    $state.go('app.compliance.substance.details', {
                        substanceId: tag.object,
                        tab: 'details.basic'
                    });
                } else if (tag.objectType == "PGCSPECIFICATION") {
                    $state.go('app.compliance.specification.details', {
                        specificationId: tag.object,
                        tab: 'details.basic'
                    });
                } else if (tag.objectType == "PGCDECLARATION") {
                    $state.go('app.compliance.declaration.details', {
                        declarationId: tag.object,
                        tab: 'details.basic'
                    });
                } else if (tag.objectType == "REQUIREMENTDOCUMENT") {
                    $state.go('app.req.document.details', {reqId: tag.revisionId, tab: 'details.basic'});
                }
            }

            function resizeDropdown() {
                $("#filtersMenu").height($(window).height() - 90);
            }

            (function () {
                $(document).click(function () {
                    $('#search-results-mask-panel').hide();
                    $('#search-results-container').slideUp(200);
                    $('.search-input-container .dropdown-menu').hide();
                });

                $(document).on('keydown', function (evt) {
                    if (evt.keyCode == 27) {
                        $('#search-results-mask-panel').hide();
                        $('#search-results-container').slideUp(200);
                        $('.search-input-container .dropdown-menu').hide();
                    }
                });

                $timeout(function () {
                    $('#search-results-container').click(function (event) {
                        event.stopPropagation();
                        event.preventDefault();
                    });

                    $('#search-results-mask-panel').click(function () {
                        $('#search-results-mask-panel').hide();
                        $('#search-results-container').slideUp(200);
                        $('.search-input-container .dropdown-menu').hide();
                    });
                    $("#filtersMenu").height($(window).height() - 90);
                }, 2000);
                $(window).resize(resizeDropdown);
            })();
        }
    }
);