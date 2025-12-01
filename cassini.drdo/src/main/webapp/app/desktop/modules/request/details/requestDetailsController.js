define(
    [
        'app/desktop/modules/request/request.module',
        'jspdf',
        'jspdf-autotable',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/requestService',
        'app/shared/services/core/bomService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module, jsPDF) {
        module.controller('RequestDetailsController', RequestDetailsController);

        function RequestDetailsController($scope, $window, $rootScope, $timeout, $state, $stateParams, $cookies, ItemTypeService,
                                          ItemService, $uibModal, $interval, AttachmentService, RequestService,
                                          CommonService, DialogService, BomService, ObjectAttributeService, AttributeAttachmentService) {

            $rootScope.viewInfo.icon = "fa flaticon-stamp13";
            $rootScope.viewInfo.title = "Request Details";

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.reqId = $stateParams.requestId;

            vm.requestHistory = [];
            vm.bdlemployeeName = null;
            vm.bdlManagerName = null;
            vm.casManagerName = null;
            vm.bdlemployeeTime = null;
            vm.bdlManagerTime = null;
            vm.casManagerTime = null;
            vm.bdlManager = null;
            vm.casManager = null;
            vm.system = null;

            vm.approve = approve;
            vm.acceptAll = acceptAll;
            vm.reject = reject;
            vm.back = back;
            vm.mode = $stateParams.mode;
            vm.issueRequest = issueRequest;
            vm.generateReport = generateReport;
            vm.acceptItem = acceptItem;
            vm.approveItem = approveItem;

            function back() {
                if (vm.mode == 'home') {
                    $state.go('app.home');
                } else if (vm.mode == 'requests') {
                    $state.go('app.requests.all');
                } else {
                    $state.go('app.requests.all');
                }
            }


            function approve() {

                var options = {
                    title: "Approve Request",
                    message: "Please confirm to Approve this Request",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($("#requestDetailsView"));
                        RequestService.approveRequest(vm.request).then(
                            function (data) {
                                loadRequest();
                                $rootScope.showSuccessMessage(data.message);
                                //$rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            function acceptAll() {

                var options = {
                    title: "Accept Request",
                    message: "Please confirm to Accept this Request",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($("#requestDetailsView"));
                        RequestService.acceptRequest(vm.request).then(
                            function (data) {
                                loadRequest();
                                $rootScope.showSuccessMessage(data.message);
                                //$rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }


            function approveItem(reqItem) {

                var options = {
                    title: "Approve Item",
                    message: "Please confirm to Approve this Item",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($("#requestDetailsView"));
                        RequestService.approveRequestItem(reqItem).then(
                            function (data) {
                                loadRequest();
                                $rootScope.showSuccessMessage("Request Item approved successfully");
                                //$rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            function acceptItem(reqItem) {

                var options = {
                    title: "Accept Item",
                    message: "Please confirm to Accept this Item",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($("#requestDetailsView"));
                        RequestService.acceptRequestItem(reqItem).then(
                            function (data) {
                                loadRequest();
                                $rootScope.showSuccessMessage("Request Item Accepted successfully");
                                //$rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            vm.rejectItem = rejectItem;
            function rejectItem() {
                if (vm.selectedItemToReject.reason == null || vm.selectedItemToReject.reason == "" || vm.selectedItemToReject.reason == undefined) {
                    $rootScope.showWarningMessage("Please enter reason");
                } else {
                    $rootScope.showBusyIndicator($("#requestDetailsView"));
                    RequestService.rejectRequestItem(vm.request.id, vm.selectedItemToReject).then(
                        function (data) {
                            closeRejectItemView();
                            loadRequest();
                            $rootScope.showSuccessMessage("Request Item rejected successfully");
                            //$rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function reject() {

                if (vm.request.reason != null && vm.request.reason != "" && vm.request.reason != undefined) {
                    $rootScope.showBusyIndicator($("#requestDetailsView"));
                    RequestService.rejectRequest(vm.request).then(
                        function (data) {
                            loadRequest();
                            closeRejectView();
                            $rootScope.showSuccessMessage("Request rejected successfully");
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    $rootScope.showWarningMessage("Please enter reason");
                }
            }

            vm.showRejectView = showRejectView;
            vm.closeRejectView = closeRejectView;

            function showRejectView() {
                var modal = document.getElementById("rejectReason-view");
                modal.style.display = "block";
            }

            function closeRejectView() {
                var modal = document.getElementById("rejectReason-view");
                modal.style.display = "none";
                vm.request.reason = null;
            }

            vm.showRejectItemView = showRejectItemView;
            vm.closeRejectItemView = closeRejectItemView;
            vm.selectedItemToReject = null;
            vm.rejectItemReason = null;
            function showRejectItemView(reqItem) {
                vm.selectedItemToReject = reqItem;
                vm.rejectItemReason = null;
                var modal = document.getElementById("rejectItemReason-view");
                modal.style.display = "block";
            }

            function closeRejectItemView() {
                var modal = document.getElementById("rejectItemReason-view");
                modal.style.display = "none";
                vm.selectedItemToReject = null;
                vm.rejectItemReason = null;
            }

            function loadRequest() {
                RequestService.getRequest(vm.reqId).then(
                    function (data) {
                        vm.request = data;
                        vm.system = vm.request.reqNumber.substring(0, 2);
                        RequestService.getRequestItems(vm.request.id).then(
                            function (data) {
                                vm.sectionGroup = data;
                                vm.requestItems = [];
                                angular.forEach(vm.sectionGroup, function (group) {
                                    group.section.level = 0;
                                    group.section.children = [];
                                    group.section.type = "SECTION";
                                    vm.requestItems.push(group.section);

                                    angular.forEach(group.requestItems, function (requestItem) {
                                        requestItem.level = group.section.level + 1;
                                        requestItem.type = "REQUESTITEM";
                                        group.section.children.push(requestItem);
                                        vm.requestItems.push(requestItem);
                                    })
                                });


                                vm.hideRejectButton = true;
                                vm.showIssueButton = false;
                                var count = 0;
                                var requestItemsCount = 0;
                                var approvedCount = 0;
                                angular.forEach(vm.requestItems, function (item) {
                                    if (item.type == "REQUESTITEM") {
                                        requestItemsCount++;
                                        if ($rootScope.hasPermission('permission.requests.bdlApprove') || $rootScope.hasPermission('permission.requests.versityApprove')) {
                                            if (item.status == "PENDING") {
                                                count++;
                                                vm.hideRejectButton = false;
                                            }
                                        } else if ($rootScope.hasPermission('permission.requests.casApprove')) {
                                            if (item.status == "ACCEPTED") {
                                                approvedCount++;
                                                vm.hideRejectButton = false;
                                            }
                                        } else if ($rootScope.hasPermission('permission.inward.storeApprove')) {
                                            if (item.approved) {
                                                approvedCount++;
                                                vm.hideRejectButton = true;
                                                vm.showIssueButton = true;
                                            }
                                        }
                                    }
                                })
                                angular.forEach(vm.request.statusHistories, function (status) {
                                    if (status.newStatus == "BDL_EMPLOYEE") {
                                        vm.bdlemployeeName = status.user.fullName;
                                        vm.bdlemployeeTime = status.timestamp;
                                    } else if (status.newStatus == "BDL_MANAGER" || status.newStatus == "VERSITY_MANAGER") {
                                        vm.bdlManagerName = status.user.fullName;
                                        vm.bdlManagerTime = status.timestamp;
                                    } else if (status.newStatus == "CAS_MANAGER") {
                                        vm.casManagerName = status.user.fullName;
                                        vm.casManagerTime = status.timestamp;
                                    }
                                })

                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                )
            }

            function issueRequest(request) {
                $state.go('app.issues.new', {issueMode: 'REQ', requestId: request.id});
            }

            function generateReport() {
                $("#requestReport").show();

                var pdf = new jsPDF('l', 'pt', 'a4');

                pdf.setFont('arial narrow');
                pdf.setFontSize(13);
                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                var imgData = $rootScope.drdoImage;
                pdf.addImage(imgData, 'JPG', 350, 25, 125, 125);

                var today = moment(new Date());
                var todayStr = today.format('DD/MM/YYYY HH:mm');

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                //pdf.text(50, 180, "Request Number :" + vm.request.reqNumber);
                pdf.text(670, 190, "Date : " + todayStr);
                var pageHeight = pdf.internal.pageSize.height;
                var requestReport = pdf.autoTableHtmlToJson(document.getElementById("requestReport"));

                pdf.setTextColor(107, 5, 132);
                pdf.setFontSize(20);
                pdf.text(365, 190, "Request Report");

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                pdf.setFontSize(13);
                pdf.autoTable(requestReport.columns, requestReport.data, {
                    startY: 210,
                    margin: {horizontal: 50},
                    styles: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false},
                    columnStyles: {text: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false}},
                });

                if (vm.request.status == "REJECTED") {
                    pdf.text(50, pdf.autoTable.previous.finalY + 15, "Reason : " + vm.request.reason);
                }

                var personDetail = pdf.autoTableHtmlToJson(document.getElementById("requestDetailsReport"));
                pdf.setTextColor(107, 5, 132);
                pdf.setFontSize(20);

                pdf.text(365, pdf.autoTable.previous.finalY + 50, "Parts");

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                pdf.setFontSize(13);
                pdf.autoTable(personDetail.columns, personDetail.data, {
                    startY: pdf.autoTable.previous.finalY + 75,
                    margin: {horizontal: 50},
                    styles: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false},
                    columnStyles: {text: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false}},
                });

                if ((pdf.autoTable.previous.finalY + 130) >= pageHeight) {
                    pdf.addPage();
                    pdf.autoTable.previous.finalY = 50;
                }

                pdf.setTextColor(107, 5, 132);
                pdf.setFontSize(20);
                pdf.text(365, pdf.autoTable.previous.finalY + 40, "Approvals");

                pdf.setFontSize(16);
                pdf.setTextColor(172, 65, 244);
                pdf.text(100, pdf.autoTable.previous.finalY + 75, "BDL");
                pdf.text(300, pdf.autoTable.previous.finalY + 75, "BDL Manager");
                pdf.text(500, pdf.autoTable.previous.finalY + 75, "CAS Manager");

                pdf.setFontSize(12);
                pdf.setTextColor(24, 25, 24);
                pdf.text(100, pdf.autoTable.previous.finalY + 100, "Name : " + vm.bdlemployeeName);
                pdf.text(300, pdf.autoTable.previous.finalY + 100, "Name : " + vm.bdlManagerName);
                pdf.text(500, pdf.autoTable.previous.finalY + 100, "Name : " + vm.casManagerName);

                pdf.text(100, pdf.autoTable.previous.finalY + 125, "Date : " + vm.bdlemployeeTime);
                pdf.text(300, pdf.autoTable.previous.finalY + 125, "Date : " + vm.bdlManagerTime);
                pdf.text(500, pdf.autoTable.previous.finalY + 125, "Date : " + vm.casManagerTime);

                //pdf.save(vm.request.reqNumber + ".pdf");
                window.open(pdf.output('bloburl'), '_blank');
                $rootScope.hideBusyIndicator();
                $("#requestReport").hide();
                $rootScope.showSuccessMessage("Report generated successfully");

            }

            (function () {
                loadRequest();
            })();
        }
    }
);