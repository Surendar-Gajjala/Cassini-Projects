define(
    [
        'app/desktop/modules/request/request.module',
        'jspdf',
        'jspdf-autotable',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/requestService',
        'app/shared/services/core/bomService'
    ],
    function (module, jsPDF) {
        module.controller('RequestsController', RequestsController);

        function RequestsController($scope, $rootScope, $translate, $window, $timeout, $application, $state, $filter,
                                    $stateParams, $cookies, CommonService, DialogService, ItemTypeService, ItemService,
                                    AttributeAttachmentService, AttachmentService, RequestService, BomService) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa flaticon-stamp13";
            $rootScope.viewInfo.title = "Requests";

            var vm = this;
            vm.loading = true;
            vm.clear = false;
            $rootScope.searchModeType = false;
            var currencyMap = new Hashtable();

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

            vm.requests = angular.copy(pagedResults);

            vm.requestFilter = {
                notification: false,
                status: null,
                searchQuery: null,
                issued: false,
                fromDate: null,
                toDate: null,
                requested: false,
                report: false,
                month: null
            };

            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.resetPage = resetPage;

            vm.createNewRequest = createNewRequest;
            vm.showRequest = showRequest;
            vm.freeTextSearch = freeTextSearch;
            vm.changeView = changeView;
            vm.flag = false;
            vm.addFilter = addFilter;
            vm.getFilterResults = getFilterResults;
            vm.cancelFilter = cancelFilter;
            vm.errorMessage = null;
            vm.clearDateFilter = clearDateFilter;
            vm.requestView = "Requests";
            vm.getMonthResults = getMonthResults;
            vm.loadRequestedItemsView = loadRequestedItemsView;
            vm.loadIssuedRequestsView = loadIssuedRequestsView;
            vm.showRequestReport = showRequestReport;

            function clearDateFilter() {
                vm.errorMessage = null;
                vm.requestFilter.fromDate = null;
                vm.requestFilter.toDate = null;
                vm.requestFilter.month = null;
                vm.filterMode = false;
                loadRequests();
            }

            function cancelFilter() {
                var modal = document.getElementById("add-filter");
                modal.style.display = "none";
            }

            function getFilterResults() {
                vm.errorMessage = null;
                if ((vm.requestFilter.fromDate != null && vm.requestFilter.fromDate != "" && vm.requestFilter.fromDate != undefined) &&
                    (vm.requestFilter.toDate != null && vm.requestFilter.toDate != "" && vm.requestFilter.toDate != undefined)) {
                    vm.filterMode = true;
                    vm.requestFilter.month = null;
                    loadRequests();
                    var modal = document.getElementById("add-filter");
                    modal.style.display = "none";
                } else {
                    vm.filterMode = false;
                    vm.requestFilter.month = null;
                    vm.errorMessage = "Please select From Date and To Date";
                }
            }

            vm.filterMode = false;
            function getMonthResults() {
                vm.errorMessage = null;
                if (vm.requestFilter.month != null && vm.requestFilter.month != "" && vm.requestFilter.month != undefined) {
                    vm.requestFilter.fromDate = null;
                    vm.requestFilter.toDate = null;
                    vm.filterMode = true;
                    loadRequests();
                    var modal = document.getElementById("add-filter");
                    modal.style.display = "none";
                } else {
                    vm.filterMode = false;
                    vm.errorMessage = "Please select Month";
                }
            }

            function addFilter() {
                vm.errorMessage = null;
                vm.requestFilter.fromDate = null;
                vm.requestFilter.toDate = null;
                var modal = document.getElementById("add-filter");
                modal.style.display = "block";
            }

            function resetPage() {
                vm.pageable.page = 0;
                vm.requestFilter.searchQuery = null;
            }

            function nextPage() {
                if (vm.requests.last != true) {
                    vm.pageable.page++;
                    loadRequests();
                }
            }

            function previousPage() {
                if (vm.requests.first != true) {
                    vm.pageable.page--;
                    loadRequests();
                }
            }

            function clearFilter() {
                loadRequests();
                vm.clear = false;
                $rootScope.showSearch = false;
            }

            function createNewRequest() {
                $state.go('app.requests.new');
            }

            function showRequest(request) {
                $state.go('app.requests.details', {requestId: request.id, mode: 'requests'});
            }

            function loadRequestedItemsView() {
                vm.requestFilter.issued = false;
                vm.requestFilter.report = false;
                vm.requestFilter.requested = !vm.requestFilter.requested;
                $rootScope.viewInfo.title = "Requested Items";
                vm.bomInstance = null;
                if (!vm.requestFilter.requested) {
                    vm.showSearchBox = true;
                    loadRequests();
                    $rootScope.viewInfo.title = "Requests";
                    vm.selectedBom = null;
                    vm.bomInstance = null;
                    vm.requestItems = [];
                } else {
                    vm.showSearchBox = false;
                }
                $rootScope.requestFilter = vm.requestFilter;
            }

            function loadIssuedRequestsView() {
                vm.requestFilter.requested = false;
                vm.requestFilter.issued = !vm.requestFilter.issued;
                if (vm.requestFilter.issued) {
                    $rootScope.viewInfo.title = "Issued Requests";
                } else {
                    $rootScope.viewInfo.title = "Requests";
                }
                vm.pageable.page = 0;
                loadRequests();
                vm.selectedBom = null;
                vm.bomInstance = null;
                vm.requestItems = [];
                $rootScope.requestFilter = vm.requestFilter;
            }

            function showRequestReport() {
                vm.requestFilter.requested = false;
                vm.requestFilter.issued = false;
                vm.requestFilter.report = !vm.requestFilter.report;
                vm.bomInstance = null;
                if (vm.requestFilter.report) {
                    $rootScope.viewInfo.title = "Request Report";
                    vm.showSearchBox = false;
                    vm.requestSummaries = [];
                    //loadRequestReport();
                } else {
                    $rootScope.viewInfo.title = "Requests";
                    vm.showSearchBox = true;
                    loadRequests();
                }
                $rootScope.requestFilter = vm.requestFilter;
            }

            vm.requestSummaries = [];
            function loadRequestReport() {
                vm.loading = true;
                RequestService.getRequestReportSummary().then(
                    function (data) {
                        vm.requestSummaries = data;
                        vm.loading = false;
                    }
                )
            }

            vm.showRequestSummary = showRequestSummary;
            function showRequestSummary(request) {
                $state.go('app.requests.summary', {requestId: request.id});
            }

            function changeView(type) {
                vm.requestView = type;
                if (type == 'Issued') {
                    vm.requestFilter.issued = true;
                    vm.requestFilter.requested = false;
                    $rootScope.viewInfo.title = "Issued Requests";
                    vm.pageable.page = 0;
                    loadRequests();
                } else if (type == 'RequestedItems') {
                    vm.requestFilter.issued = false;
                    vm.requestFilter.requested = true;
                    $rootScope.viewInfo.title = "Requested Items";
                } else {
                    vm.requestFilter.issued = false;
                    vm.requestFilter.requested = false;
                    $rootScope.viewInfo.title = "Requests";
                    vm.pageable.page = 0;
                    loadRequests();
                }
                $rootScope.requestFilter = vm.requestFilter;
            }

            function loadRequests() {
                $timeout(function () {
                    $rootScope.showBusyIndicator($('.view-container'));
                }, 100);
                vm.loading = true;
                if (!vm.requestFilter.requested && !vm.requestFilter.report) {
                    RequestService.getAllRequests(vm.pageable, vm.requestFilter).then(
                        function (data) {
                            vm.requests = data;
                            if (vm.requests == "" || vm.requests == null) {
                                vm.requests = angular.copy(pagedResults);
                            }
                            vm.loading = false;
                            vm.showSearchBox = true;
                            $timeout(function () {
                                $rootScope.hideBusyIndicator();
                            }, 100)
                        }
                    );
                } else if (vm.requestFilter.report) {
                    if (vm.bomInstance != null) {
                        onSelectInstanceReport(vm.bomInstance);
                    }
                }
            }

            vm.requiredRequestAttributes = [];
            function loadRequestAttributes() {
                ItemTypeService.getAttributesByObjectType("REQUEST").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            if (attribute.required) {
                                vm.requiredRequestAttributes.push(attribute);
                            }
                        });
                        loadBoms();
                        loadRequests();
                    }
                )
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != "" && freeText != undefined) {
                    $scope.freeTextQuery = freeText;
                    vm.requestFilter.searchQuery = freeText;
                    vm.pageable.page = 0;
                    if (vm.requestFilter.requested) {
                        onSelectSection(vm.instanceSection);
                    } else {
                        loadRequests();
                    }
                } else {

                    resetPage();
                    $scope.freeTextQuery = null;
                    vm.requestFilter.searchQuery = null;
                    if (vm.requestFilter.requested) {
                        onSelectSection(vm.instanceSection);
                    } else {
                        loadRequests();
                    }
                }
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

            vm.showSearchBox = true;
            vm.selectedBom = null;
            vm.bomInstance = null;
            vm.requestItems = [];
            vm.onSelectBom = onSelectBom;
            vm.onSelectInstance = onSelectInstance;

            function onSelectBom(bom) {
                vm.showSearchBox = false;
                BomService.getBomInstances(bom.id).then(
                    function (data) {
                        vm.bomInstances = data;
                        vm.bomInstances = $filter('orderBy')(vm.bomInstances, 'item.instanceName');
                        vm.requestItems = [];
                        if (vm.bomInstances.length == 0) {
                            vm.bomInstanceTitle = "No Instances";
                        } else {
                            vm.bomInstanceTitle = "Select Instance";
                        }
                        vm.bomInstance = null;
                        vm.instanceSection = null;
                        vm.requestItems = [];
                        $scope.$evalAsync();
                    }
                )
            }

            function onSelectInstance(instance) {
                vm.instanceSection = null;
                vm.requestItems = [];
                vm.showSearchBox = false;
                $rootScope.showBusyIndicator($("#requestedItemsView"));
                var admin = $rootScope.hasPermission('permission.admin.all');
                var versity = $rootScope.hasPermission('permission.versity.group');
                vm.bomInstance = instance;
                BomService.getSectionsByInstance(instance.id, admin, versity).then(
                    function (data) {
                        vm.instanceSections = [];
                        angular.forEach(data, function (item) {
                            item.selected = false;
                            vm.instanceSections.push(item);

                        });
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }
                );


                /*BomService.getRequestedItemsBySections(instance.id).then(
                 function (data) {
                 vm.sectionGroup = data;
                 vm.requestItems = [];
                 angular.forEach(vm.sectionGroup, function (group) {
                 group.section.level = 0;
                 group.section.type = "SECTION";
                 group.section.children = [];
                 group.section.expanded = false;
                 vm.requestItems.push(group.section);

                 angular.forEach(group.bomInstanceItems, function (requestItem) {
                 requestItem.level = group.section.level + 1;
                 group.section.children.push(requestItem);
                 //vm.requestItems.push(requestItem);
                 })
                 });
                 vm.loading = false;
                 $rootScope.hideBusyIndicator();
                 }, function (error) {
                 $rootScope.showErrorMessage(error.message);
                 $rootScope.hideBusyIndicator();
                 }
                 )*/
            }

            vm.instanceSection = null;
            vm.onSelectSection = onSelectSection;
            function onSelectSection(section) {
                vm.loading = true;
                vm.showSearchBox = true;
                $rootScope.showBusyIndicator($(".view-container"));
                vm.instanceSection = section;
                BomService.getRequestedItemsBySection(vm.bomInstance.id, section.id, vm.requestFilter.searchQuery).then(
                    function (data) {
                        vm.requestItems = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.onSelectInstanceReport = onSelectInstanceReport;
            function onSelectInstanceReport(instance) {
                $rootScope.showBusyIndicator($('.view-container'));
                vm.loading = true;
                RequestService.getRequestReportSummaryByInstance(instance.id).then(
                    function (data) {
                        vm.requestSummaries = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )

            }

            vm.toggleSection = toggleSection;

            function toggleSection(section) {
                section.expanded = !section.expanded;
                var index = vm.requestItems.indexOf(section);
                if (section.expanded) {
                    angular.forEach(section.children, function (requestItem) {
                        index = index + 1;
                        vm.requestItems.splice(index, 0, requestItem);
                    })
                } else {
                    vm.requestItems.splice(index + 1, section.children.length);
                }
            }

            vm.deleteRequest = deleteRequest;

            function deleteRequest(request) {
                var options = {
                    title: "Delete Request",
                    message: "Please confirm to delete this request",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            RequestService.deleteRequest(request.id).then(
                                function (data) {
                                    loadRequests();
                                    $rootScope.showSuccessMessage("Request deleted successfully");
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                )
            }

            vm.printRequestSummary = printRequestSummary;

            function printRequestSummary() {
                $rootScope.showBusyIndicator($('.view-container'));
                var pdf = new jsPDF('l', 'pt', 'a4');
                var pageWidth = pdf.internal.pageSize.width;
                pdf.setFont('arial narrow');

                var pageContent = function (data) {
                    //Header

                    pdf.setFontStyle('bold');
                    pdf.setFontSize(20);
                    pdf.text(350, 20, "Request Summary");
                    pdf.setFontSize(13);
                    pdf.setTextColor(7, 7, 7);
                    pdf.setFontStyle('normal');
                    var imgData = $rootScope.drdoImage;
                    pdf.addImage(imgData, 'JPG', pageWidth / 2.2, 40, 100, 100);

                    var today = moment(new Date());
                    var todayStr = today.format('DD/MM/YYYY HH:mm');

                    pdf.setFontSize(13);
                    pdf.setFontStyle('normal');

                    pdf.text(pageWidth - 200, 120, 'Date : ' + todayStr);

                    var str = "Page " + data.pageCount;

                    pdf.text(str, pdf.internal.pageSize.width - 50, pdf.internal.pageSize.height - 10);
                };

                var personDetail = pdf.autoTableHtmlToJson(document.getElementById("requestSummary"));

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                pdf.setFontSize(13);
                pdf.autoTable(personDetail.columns, personDetail.data, {
                    addPageContent: pageContent,
                    startY: 160,
                    margin: {horizontal: 25, bottom: 20, top: 140},
                    headerStyles: {
                        lineWidth: 0.2,
                        lineColor: [7, 7, 7],
                        fillColor: [255, 255, 255],
                        textColor: [7, 7, 7]
                    },
                    styles: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false},
                    //columnStyles: {text: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false}},
                    columnStyles: {
                        0: {
                            columnWidth: 300, overflow: 'linebreak', overflowColumns: false,
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7]
                        },
                        1: {
                            columnWidth: 100, overflow: 'linebreak', overflowColumns: false,
                            fillColor: [255, 255, 255],
                            valign: 'middle', halign: 'center',
                            textColor: [7, 7, 7]
                        },
                        2: {
                            columnWidth: 60, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle', halign: 'center',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7]
                        },
                        3: {
                            columnWidth: 70, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle', halign: 'center',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7]
                        },
                        4: {
                            columnWidth: 60, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle', halign: 'center',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7]
                        },
                        5: {
                            columnWidth: 70, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle', halign: 'center',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7]
                        },
                        6: {
                            columnWidth: 70, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle', halign: 'center',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7]
                        },
                        7: {
                            columnWidth: 60, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle', halign: 'center',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7]
                        }
                    }
                });

                window.open(pdf.output('bloburl'), '_blank');

                $rootScope.hideBusyIndicator();
                $rootScope.showSuccessMessage("Report generated successfully");
            }

            vm.requestNumber = null;
            vm.partName = null;
            vm.showRequestUpdateWindow = showRequestUpdateWindow;
            function showRequestUpdateWindow() {
                vm.requestNumber = null;
                vm.partName = null;
                var modal = document.getElementById("requestUpdate-view");
                modal.style.display = "block";
                var header = document.getElementsByClassName("header-row");
                angular.forEach(header, function (head) {
                    head.style.position = "initial";
                })
            }

            vm.updateRequest = updateRequest;
            function updateRequest() {
                if (vm.requestNumber != null && vm.requestNumber != "" && vm.partName != "" && vm.partName != null) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    RequestService.updateRequestByNumber(vm.requestNumber, vm.partName).then(
                        function (data) {
                            $rootScope.showSuccessMessage("Request Item updated successfully");
                            $rootScope.hideBusyIndicator();
                            closeWindow();
                        }, function (error) {
                            $rootScope.showSuccessMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    $rootScope.showWarningMessage("Please enter Required fields");
                }
            }

            vm.closeWindow = closeWindow;
            function closeWindow() {
                var modal = document.getElementById("requestUpdate-view");
                modal.style.display = "none";
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    vm.requestFilter = $rootScope.requestFilter;
                    loadRequestAttributes();
                });
            })();
        }
    }
);