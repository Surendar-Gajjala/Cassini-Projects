define(
    [
        'app/desktop/modules/issue/issue.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/issueService',
        'app/shared/services/core/bomService',
        'app/shared/services/core/requestService'
    ],
    function (module) {
        module.controller('IssuesController', IssuesController);

        function IssuesController($scope, $rootScope, $translate, $window, $filter, $timeout, $application, $state,
                                  $stateParams, $cookies, CommonService, DialogService, ItemTypeService, ItemService,
                                  AttributeAttachmentService, AttachmentService, IssueService, BomService, RequestService) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa fa-sign-out";
            $rootScope.viewInfo.title = "Issues";

            var vm = this;

            vm.loading = true;
            vm.clear = false;
            $rootScope.searchModeType = false;
            var currencyMap = new Hashtable();
            vm.showIssue = showIssue;

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

            vm.issues = angular.copy(pagedResults);

            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.resetPage = resetPage;
            vm.createNewIssue = createNewIssue;
            vm.issueFilter = {
                notification: false,
                searchQuery: null,
                fromDate: null,
                toDate: null,
                month: null,
                issues: true,
                report: false,
                missile: ''
            };
            vm.flag = false;
            vm.addFilter = addFilter;
            vm.getFilterResults = getFilterResults;
            vm.cancelFilter = cancelFilter;
            vm.errorMessage = null;
            vm.clearDateFilter = clearDateFilter;
            vm.freeTextSearch = freeTextSearch;
            vm.getMonthResults = getMonthResults;
            vm.filterMode = false;

            function clearDateFilter() {
                vm.errorMessage = null;
                vm.issueFilter.fromDate = null;
                vm.issueFilter.toDate = null;
                vm.issueFilter.month = null;
                vm.filterMode = false;
                loadIssues();
            }

            function cancelFilter() {
                var modal = document.getElementById("add-filter");
                modal.style.display = "none";
            }

            function getFilterResults() {
                vm.errorMessage = null;
                if ((vm.issueFilter.fromDate != null && vm.issueFilter.fromDate != "" && vm.issueFilter.fromDate != undefined) &&
                    (vm.issueFilter.toDate != null && vm.issueFilter.toDate != "" && vm.issueFilter.toDate != undefined)) {
                    vm.issueFilter.month = null;
                    vm.filterMode = true;
                    loadIssues();
                    var modal = document.getElementById("add-filter");
                    modal.style.display = "none";
                } else {
                    vm.filterMode = false;
                    vm.issueFilter.month = null;
                    vm.errorMessage = "Please select From Date and To Date";
                }
            }

            function getMonthResults() {
                vm.errorMessage = null;
                if (vm.issueFilter.month != null && vm.issueFilter.month != "" && vm.issueFilter.month != undefined) {
                    vm.issueFilter.fromDate = null;
                    vm.issueFilter.toDate = null;
                    loadIssues();
                    vm.filterMode = true;
                    var modal = document.getElementById("add-filter");
                    modal.style.display = "none";
                } else {
                    vm.filterMode = false;
                    vm.errorMessage = "Please select Month";
                }
            }

            function addFilter() {
                vm.errorMessage = null;
                vm.issueFilter.fromDate = null;
                vm.issueFilter.toDate = null;
                var modal = document.getElementById("add-filter");
                modal.style.display = "block";
            }

            function resetPage() {
                vm.pageable.page = 0;
            }

            function nextPage() {
                if (vm.issues.last != true) {
                    vm.pageable.page++;
                    loadIssues();
                }
            }

            function previousPage() {
                if (vm.issues.first != true) {
                    vm.pageable.page--;
                    loadIssues();
                }
            }

            function clearFilter() {
                loadIssues();
                vm.clear = false;
                $rootScope.showSearch = false;
            }

            vm.showSearchBox = true;
            function loadIssues() {
                vm.loading = true;
                vm.showSearchBox = true;
                vm.issueFilter.adminPermission = $rootScope.hasPermission('permission.admin.all');
                vm.issueFilter.storeApprove = $rootScope.hasPermission('permission.inward.storeApprove');
                vm.issueFilter.bdlQcApprove = $rootScope.hasPermission('permission.issued.BDLQCApprove');
                vm.issueFilter.bdlPpcReceive = $rootScope.hasPermission('permission.bdlPcc.receive');
                vm.issueFilter.bdlApprove = $rootScope.hasPermission('permission.requests.bdlApprove');
                vm.issueFilter.casApprove = $rootScope.hasPermission('permission.requests.casApprove');
                vm.issueFilter.versity = $rootScope.hasPermission('permission.versity.group');
                vm.issueFilter.versityApprove = $rootScope.hasPermission('permission.requests.versityApprove');
                vm.issueFilter.versityQc = $rootScope.hasPermission('permission.issued.versityQCApprove');
                vm.issueFilter.versityPpc = $rootScope.hasPermission('permission.versityPpc.receive');
                IssueService.getAllIssues(vm.pageable, vm.issueFilter).then(
                    function (data) {
                        vm.issues = data;
                        CommonService.getPersonReferences(vm.issues.content, 'modifiedBy');
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;
                    }
                )
            }

            function showIssue(issue) {
                $state.go('app.issues.details', {issueId: issue.id, mode: 'issues'});
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != "" && freeText != undefined) {
                    $scope.freeTextQuery = freeText;
                    vm.issueFilter.searchQuery = freeText;
                    if (vm.issueFilter.report) {
                        selectSection(vm.selectedSection);
                    } else {
                        loadIssues();
                    }
                } else {
                    $scope.freeTextQuery = null;
                    vm.issueFilter.searchQuery = null;
                    resetPage();
                    if (vm.issueFilter.report) {
                        selectSection(vm.selectedSection);
                    } else {
                        loadIssues();
                    }
                }
            }

            function createNewIssue() {
                $state.go('app.issues.new', {issueMode: "ISSUE"});
            }

            vm.showSearchBox = true;
            vm.requiredIssueAttributes = [];
            function loadIssueAttributes() {
                ItemTypeService.getAttributesByObjectType("ISSUE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            if (attribute.required) {
                                vm.requiredIssueAttributes.push(attribute);
                            }
                        });
                        loadIssues();
                    }
                )
            }


            function loadBoms() {
                BomService.getAllBoms().then(
                    function (data) {
                        vm.boms = data;
                        if (vm.boms.length == 0) {
                            vm.selectBomTitle = "No BOM";
                        } else {
                            if (vm.boms.length == 1) {
                                vm.selectedBom = vm.boms[0];
                                onSelectBom(vm.selectedBom);
                            }
                            vm.selectBomTitle = "Select BOM";
                        }
                    }
                )
            }

            vm.bomInstances = [];
            vm.bomInstanceTitle = "Select Instance";
            vm.selectedBom = null;
            vm.onSelectBom = onSelectBom;
            function onSelectBom(bom) {
                vm.selectedBomInstance = null;
                vm.showSearchBox = false;
                BomService.getBomInstances(bom.id).then(
                    function (data) {
                        vm.selectedBom = bom;
                        vm.bomInstances = data;
                        vm.bomInstances = $filter('orderBy')(vm.bomInstances, 'item.instanceName');
                        vm.requestItems = [];
                        if (vm.bomInstances.length == 0) {
                            vm.bomInstanceTitle = "No Instances";
                        } else {
                            vm.bomInstanceTitle = "Select Instance";
                        }
                        vm.bomInstance = null;
                        $scope.$evalAsync();
                    }
                )
            }

            vm.instanceSections = [];
            vm.selectedBomInstance = null;
            vm.onSelectInstance = onSelectInstance;
            vm.onSelectInstanceSearch = onSelectInstanceSearch;
            function onSelectInstanceSearch(instance) {
                vm.issueFilter.missile = instance.id;
                vm.pageable.page = 0;
                vm.loading = true;
                vm.showSearchBox = true;
                IssueService.getAllIssues(vm.pageable, vm.issueFilter).then(
                    function (data) {
                        vm.issues = data;
                        CommonService.getPersonReferences(vm.issues.content, 'modifiedBy');
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;
                    }
                )
            }

            function onSelectInstance(instance) {
                vm.selectedSection = null;
                vm.issueReport = [];
                vm.showSearchBox = false;
                $rootScope.showBusyIndicator($('.view-container'));
                vm.selectedBomInstance = instance;
                var versity = false;
                var admin = $rootScope.hasPermission('permission.admin.all');
                var bomPermission = $rootScope.hasPermission('permission.bom.all');
                if (!bomPermission) {
                    versity = $rootScope.hasPermission('permission.versity.group');
                }

                BomService.getSectionsByInstance(vm.selectedBomInstance.id, admin, versity).then(
                    function (data) {
                        vm.instanceSections = [];
                        var allSection = {
                            id: 0,
                            typeRef: {
                                name: "All"
                            }
                        };
                        vm.instanceSections.push(allSection);
                        angular.forEach(data, function (item) {
                            item.selected = false;
                            vm.instanceSections.push(item);

                        });
                        vm.showSearchBox = false;
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.selectSection = selectSection;
            function selectSection(section) {
                vm.selectedSection = section;
                vm.showSearchBox = true;
                vm.loading = true;
                IssueService.getIssueReportByMissile(vm.selectedBomInstance.id, section.id, vm.issueFilter.searchQuery).then(
                    function (data) {
                        vm.issueReport = [];
                        angular.forEach(data, function (report) {
                            report.section.level = 0;
                            vm.issueReport.push(report.section);
                            angular.forEach(report.subsystemReport, function (subsystem) {
                                if (subsystem.subsystem != null) {
                                    subsystem.subsystem.level = 1;
                                    vm.issueReport.push(subsystem.subsystem);
                                }
                                angular.forEach(subsystem.issuedParts, function (issuedPart) {
                                    issuedPart.level = 2;
                                    vm.issueReport.push(issuedPart);
                                })
                            })
                        })
                        vm.loading = false;
                    }
                )
            }


            vm.resetMissileSearch = resetMissileSearch;
            function resetMissileSearch() {
                $('#missileDropDown').hide();
                $rootScope.showBusyIndicator($('.view-content'));
                vm.issueFilter.missile = '';
                vm.selectedMissile = null;
                vm.selectedBom = null;
                vm.pageable.page = 0;
                loadIssues();
            }

            vm.showIssueReport = showIssueReport;
            function showIssueReport() {
                vm.issueFilter.report = !vm.issueFilter.report;
                if (vm.issueFilter.report) {
                    vm.issueFilter.issues = false;
                    vm.showSearchBox = false;
                    vm.issueFilter.searchQuery = null;
                    $rootScope.freeTextQuery = null;
                    if (vm.selectedSection != null) {
                        vm.showSearchBox = true;
                        selectSection(vm.selectedSection);
                    }
                } else {
                    vm.issueFilter.issues = true;
                    loadIssues();
                }
            }

            vm.showInstances = showInstances;
            function showInstances() {
                vm.showUnitsDropDown = !vm.showUnitsDropDown;
                if (this.showUnitsDropDown) {
                    $('#missileDropDown').show();
                } else {
                    $('#missileDropDown').hide();
                }
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    loadBoms();
                    loadIssueAttributes();
                });
            })();
        }
    }
);