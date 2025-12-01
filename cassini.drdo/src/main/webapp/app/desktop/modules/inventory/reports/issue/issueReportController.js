define(
    [
        'app/desktop/modules/inward/inward.module',
        'jspdf',
        'jspdf-autotable',
        'split-pane',
        'jquery.easyui',
        'app/shared/services/core/issueService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module, jsPDF) {
        module.controller('IssueReportController', IssueReportController);

        function IssueReportController($scope, $stateParams, $rootScope, $timeout, $interval, $state, $cookies, IssueService, CommonService) {

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;
            vm.loading = true;

            vm.toggleSection = toggleSection;
            vm.getIssueReport = getIssueReport;
            vm.back = back;

            function back() {
                window.history.back();
            }

            vm.sections = {
                basic: true,
                items: false
            };

            function toggleSection(section) {
                if (section == 'basic') {
                    vm.sections.basic = !vm.sections.basic;
                } else if (section == 'items') {
                    vm.sections.items = !vm.sections.items;
                }
            }

            function loadIssues() {
                IssueService.getIssuesByInstance($stateParams.referenceId).then(
                    function (data) {
                        vm.issues = data;
                        CommonService.getPersonReferences(vm.issues, 'createdBy');
                        CommonService.getPersonReferences(vm.issues, 'modifiedBy');
                        vm.loading = false;
                        getIssueReport(vm.issues[0]);
                    }
                )
            }

            function getIssueReport(issue) {
                vm.issue = issue;
                IssueService.getIssueDetails(issue.id).then(
                    function (data) {
                        vm.system = issue.request.reqNumber.substring(0, 2);
                        vm.sectionGroup = data;
                        vm.issuedItems = [];
                        vm.issuedItemsLength = 0;
                        angular.forEach(vm.sectionGroup, function (group) {
                            group.section.level = 0;
                            group.section.children = [];
                            group.section.type = "SECTION";
                            vm.issuedItems.push(group.section);

                            angular.forEach(group.issuedItems, function (issueItem) {
                                vm.issuedItemsLength++;
                                issueItem.level = group.section.level + 1;
                                issueItem.type = "ISSUEITEM";
                                group.section.children.push(issueItem);
                                vm.issuedItems.push(issueItem);
                            })
                        });
                    }
                )
            }

            vm.generateReport = generateReport;
            function generateReport() {
                $("#issueReport").show();

                var pdf = new jsPDF('l', 'pt', 'a4');

                pdf.setFont('arial narrow');
                pdf.setFontSize(13);
                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                var imgData = $rootScope.drdoImage;
                pdf.addImage(imgData, 'JPG', 350, 25, 125, 125);

                var today = moment(new Date());
                var todayStr = today.format('DD/MM/YYYY HH:mm');

                pdf.setTextColor(107, 5, 132);
                pdf.setFontSize(20);
                pdf.setFontStyle('bold');
                pdf.text(360, 180, "Issue Report");

                pdf.setTextColor(7, 7, 7);
                pdf.setFontSize(13);
                pdf.setFontStyle('normal');
                pdf.text(50, 200, "Issue Number :" + vm.issue.number);
                pdf.text(660, 200, "Date : " + todayStr);

                var pageHeight = pdf.internal.pageSize.height;

                var requestReport = pdf.autoTableHtmlToJson(document.getElementById("issueReport"));

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                pdf.setFontSize(13);
                pdf.autoTable(requestReport.columns, requestReport.data, {
                    startY: 220,
                    margin: {horizontal: 50},
                    styles: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false},
                    columnStyles: {text: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false}},
                });

                var personDetail = pdf.autoTableHtmlToJson(document.getElementById("issueDetailsReport"));
                pdf.setTextColor(107, 5, 132);
                pdf.setFontSize(20);

                pdf.text(380, pdf.autoTable.previous.finalY + 50, "Parts");

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                pdf.setFontSize(13);
                pdf.autoTable(personDetail.columns, personDetail.data, {
                    startY: pdf.autoTable.previous.finalY + 75,
                    margin: {horizontal: 50},
                    styles: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false},
                    columnStyles: {text: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false}},
                });

                pdf.setTextColor(107, 5, 132);
                pdf.setFontSize(20);

                if ((pdf.autoTable.previous.finalY + 150) >= pageHeight) {
                    pdf.addPage();
                    pdf.autoTable.previous.finalY = 50;
                }

                pdf.text(360, pdf.autoTable.previous.finalY + 40, "Approvals");

                pdf.setFontSize(16);
                pdf.setTextColor(66, 134, 244);
                pdf.text(200, pdf.autoTable.previous.finalY + 75, "Issued By");
                pdf.text(450, pdf.autoTable.previous.finalY + 75, "Received By");

                pdf.setFontSize(14);
                pdf.setTextColor(66, 244, 113);
                pdf.text(200, pdf.autoTable.previous.finalY + 95, "Bondage Store In-charge");
                pdf.text(450, pdf.autoTable.previous.finalY + 95, "BDL PPC");


                pdf.setFontSize(12);
                pdf.setTextColor(24, 25, 24);
                pdf.text(200, pdf.autoTable.previous.finalY + 115, "Name : ");
                pdf.text(450, pdf.autoTable.previous.finalY + 115, "Name : ");

                pdf.text(200, pdf.autoTable.previous.finalY + 140, "Date : ");
                pdf.text(450, pdf.autoTable.previous.finalY + 140, "Date : ");

                //pdf.save(vm.issue.number + ".pdf");
                window.open(pdf.output('bloburl'), '_blank');
                $rootScope.hideBusyIndicator();
                $("#issueReport").hide();
                $rootScope.showSuccessMessage("Report generated successfully");

            }

            (function () {
                loadIssues();
            })();
        }
    }
);