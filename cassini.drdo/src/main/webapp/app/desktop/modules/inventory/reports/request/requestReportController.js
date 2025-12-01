define(
    [
        'app/desktop/modules/inventory/inventory.module',
        'jspdf',
        'jspdf-autotable',
        'split-pane',
        'jquery.easyui',
        'app/shared/services/core/requestService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module, jsPDF) {
        module.controller('RequestReportController', RequestReportController);

        function RequestReportController($scope, $rootScope, $stateParams, $timeout, $interval, $state, $cookies,
                                         RequestService, CommonService) {

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.back = back;
            vm.toggleSection = toggleSection;
            vm.loadRequestReport = loadRequestReport;

            function back() {
                window.history.back();
            }

            vm.sections = {
                basic: true,
                items: false,
                relatedIssues: false
            };

            function toggleSection(section) {
                if (section == 'basic') {
                    vm.sections.basic = !vm.sections.basic;
                } else if (section == 'items') {
                    vm.sections.items = !vm.sections.items;
                } else if (section == 'relatedIssues') {
                    vm.sections.relatedIssues = !vm.sections.relatedIssues;
                }
            }

            function loadRequests() {
                vm.loading = true;
                RequestService.getRequestsByBomInstance($stateParams.referenceId).then(
                    function (data) {
                        vm.requests = data;
                        CommonService.getPersonReferences(vm.requests, 'createdBy');
                        vm.loading = false;
                        getRequestReport(vm.requests[0]);
                    }
                )
            }

            function loadRequestReport(request) {
                $rootScope.showBusyIndicator($("#requestReportView"));
                getRequestReport(request)
            }

            vm.bdlemployee = "";
            vm.bdlManager = "";
            vm.casManager = "";
            vm.bdlemployeeTimestamp = "";
            vm.bdlManagerTimestamp = "";
            vm.casManagerTimestamp = "";
            function getRequestReport(request) {
                vm.requestItems = [];
                vm.request = request;
                vm.system = vm.request.reqNumber.substring(0, 2);
                $rootScope.viewInfo.title = vm.request.bomInstance.item.instanceName + " - Request Report";
                RequestService.getRequestReport(request.id).then(
                    function (data) {
                        vm.requestItems = data.requestItems;
                        vm.issuedItems = data.issuedItems;
                        angular.forEach(data.statusHistories, function (status) {
                            if (status.newStatus == "BDL_EMPLOYEE") {
                                vm.bdlemployee = status.user.fullName;
                                vm.bdlemployeeTimestamp = status.timestamp;
                            } else if (status.newStatus == "BDL_MANAGER") {
                                vm.bdlManager = status.user.fullName;
                                vm.bdlManagerTimestamp = status.timestamp;
                            } else if (status.newStatus == "CAS_MANAGER") {
                                vm.casManager = status.user.fullName;
                                vm.casManagerTimestamp = status.timestamp;
                            }
                        })
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.generateReport = generateReport;
            function generateReport() {
                $("#requestReport").show();

                var pdf = new jsPDF('l', 'pt', 'a4');

                pdf.setFont('arial narrow');
                pdf.setFontSize(13);
                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                var imgData = $rootScope.drdoImage;
                pdf.addImage(imgData, 'JPG', 350, 25, 150, 150);

                var today = moment(new Date());
                var todayStr = today.format('DD/MM/YYYY HH:mm');

                var pageHeight = pdf.internal.pageSize.height;

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                //pdf.text(50, 180, "Request Number :" + vm.request.reqNumber);
                pdf.text(660, 190, "Date : " + todayStr);
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

                if ((pdf.autoTable.previous.finalY + 200) >= pageHeight) {
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
                pdf.text(100, pdf.autoTable.previous.finalY + 100, "Name : " + vm.bdlemployee);
                pdf.text(300, pdf.autoTable.previous.finalY + 100, "Name : " + vm.bdlManager);
                pdf.text(500, pdf.autoTable.previous.finalY + 100, "Name : " + vm.casManager);

                pdf.text(100, pdf.autoTable.previous.finalY + 125, "Date : " + vm.bdlemployeeTimestamp);
                pdf.text(300, pdf.autoTable.previous.finalY + 125, "Date : " + vm.bdlManagerTimestamp);
                pdf.text(500, pdf.autoTable.previous.finalY + 125, "Date : " + vm.casManagerTimestamp);

                var personDetail = pdf.autoTableHtmlToJson(document.getElementById("relatedIssuesReport"));
                pdf.setTextColor(107, 5, 132);
                pdf.setFontSize(20);

                pdf.autoTable.previous.finalY = pdf.autoTable.previous.finalY + 125;

                if ((pdf.autoTable.previous.finalY + 200) >= pageHeight) {
                    pdf.addPage();
                    pdf.autoTable.previous.finalY = 50;
                }

                pdf.text(365, pdf.autoTable.previous.finalY, "Issued Parts");

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                pdf.setFontSize(13);
                pdf.autoTable(personDetail.columns, personDetail.data, {
                    startY: pdf.autoTable.previous.finalY + 20,
                    margin: {horizontal: 50},
                    styles: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false},
                    columnStyles: {text: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false}},
                });


                //pdf.save(vm.request.reqNumber + ".pdf");
                window.open(pdf.output('bloburl'), '_blank');
                $rootScope.hideBusyIndicator();
                $("#requestReport").hide();
                $rootScope.showSuccessMessage("Report generated successfully");

            }

            (function () {
                loadRequests();
            })();
        }
    }
);