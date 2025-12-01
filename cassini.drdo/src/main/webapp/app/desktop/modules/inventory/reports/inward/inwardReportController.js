define(
    [
        'app/desktop/modules/inventory/inventory.module',
        'jspdf',
        'jspdf-autotable',
        'split-pane',
        'jquery.easyui',
        'app/shared/services/core/inwardService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module, jsPDF) {
        module.controller('InwardReportController', InwardReportController);

        function InwardReportController($scope, $rootScope, $stateParams, $timeout, $interval, $state, $cookies, InwardService, CommonService) {

            $rootScope.viewInfo.icon = "fa fa-bar-chart";
            $rootScope.viewInfo.title = "Inventory";

            var vm = this;

            vm.back = back;
            vm.toggleSection = toggleSection;
            vm.downloadGatePass = downloadGatePass;
            vm.loadInwardReport = loadInwardReport;

            function back() {
                window.history.back();
            }

            vm.sections = {
                basic: true,
                items: false,
                accepted: false,
                returns: false,
                onHold: false,
                failures: false
            };

            function toggleSection(section) {
                if (section == 'basic') {
                    vm.sections.basic = !vm.sections.basic;
                }
                else if (section == 'items') {
                    vm.sections.items = !vm.sections.items;
                }
                else if (section == 'returns') {
                    vm.sections.returns = !vm.sections.returns;
                }
                else if (section == 'onHold') {
                    vm.sections.onHold = !vm.sections.onHold;
                }
                else if (section == 'failures') {
                    vm.sections.failures = !vm.sections.failures;
                } else if (section == 'accepted') {
                    vm.sections.accepted = !vm.sections.accepted;
                }
            }

            function loadInwards() {
                vm.loading = true;
                InwardService.getInwardsByBom($stateParams.referenceId).then(
                    function (data) {
                        vm.inwards = data;
                        CommonService.getPersonReferences(vm.inwards, 'createdBy');
                        vm.loading = false;
                        getInwardReport(vm.inwards[0]);
                    }
                )
            }

            function loadInwardReport(inward) {
                $rootScope.showBusyIndicator($("#inwardReportView"));
                getInwardReport(inward);
            }


            function getInwardReport(inward) {
                vm.inward = inward;
                $rootScope.viewInfo.title = vm.inward.bom.item.itemMaster.itemName + " - Inward Report";
                InwardService.getInwardReport(inward.id).then(
                    function (data) {
                        vm.inwardItems = data.inwardItems;
                        vm.acceptedInstances = data.acceptedInstances;
                        vm.onHoldInstances = data.onHoldInstances;
                        vm.returnInstances = data.returnInstances;
                        vm.failedInstances = data.failedInstances;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function downloadGatePass(gatePass) {
                var url = "{0}//{1}/api/drdo/inwards/gatePass/{2}/{3}/preview".
                    format(window.location.protocol, window.location.host,
                    gatePass.id, gatePass.gatePass.id);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = gatePass.gatePass.name;
                });
                /*window.open(url);
                 $timeout(function () {
                 window.close();
                 }, 2000);*/
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
                var todayStr = today.format('DD/MM/YYYY');

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                //pdf.text(50, 180, "Request Number :" + vm.request.reqNumber);
                pdf.text(675, 190, "Date : " + todayStr);
                var inwardReport = pdf.autoTableHtmlToJson(document.getElementById("inwardReport"));

                pdf.setTextColor(107, 5, 132);
                pdf.setFontSize(20);
                pdf.text(365, 190, "Inward Report");

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                pdf.setFontSize(13);
                pdf.autoTable(inwardReport.columns, inwardReport.data, {
                    startY: 210,
                    margin: {horizontal: 50},
                    styles: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false},
                    columnStyles: {text: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false}},
                });

                var personDetail = pdf.autoTableHtmlToJson(document.getElementById("inwardItemReport"));
                pdf.setTextColor(107, 5, 132);
                pdf.setFontSize(20);

                pdf.text(365, pdf.autoTable.previous.finalY + 50, "Inward Items");

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
                pdf.text(365, pdf.autoTable.previous.finalY + 40, "Accepted Parts");

                var accepted = pdf.autoTableHtmlToJson(document.getElementById("acceptedItemReport"));
                pdf.setTextColor(107, 5, 132);
                pdf.setFontSize(20);

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                pdf.setFontSize(13);
                pdf.autoTable(accepted.columns, accepted.data, {
                    startY: pdf.autoTable.previous.finalY + 75,
                    margin: {horizontal: 50},
                    styles: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false},
                    columnStyles: {text: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false}},
                });


                pdf.setTextColor(107, 5, 132);
                pdf.setFontSize(20);
                pdf.text(365, pdf.autoTable.previous.finalY + 40, "On Hold Parts");

                var onHold = pdf.autoTableHtmlToJson(document.getElementById("onHoldItemReport"));

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                pdf.setFontSize(13);
                pdf.autoTable(onHold.columns, onHold.data, {
                    startY: pdf.autoTable.previous.finalY + 75,
                    margin: {horizontal: 50},
                    styles: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false},
                    columnStyles: {text: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false}},
                });

                pdf.setTextColor(107, 5, 132);
                pdf.setFontSize(20);
                pdf.text(365, pdf.autoTable.previous.finalY + 40, "Reject Parts");

                var returned = pdf.autoTableHtmlToJson(document.getElementById("returnItemReport"));

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                pdf.setFontSize(13);
                pdf.autoTable(returned.columns, returned.data, {
                    startY: pdf.autoTable.previous.finalY + 75,
                    margin: {horizontal: 50},
                    styles: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false},
                    columnStyles: {text: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false}},
                });

                pdf.setTextColor(107, 5, 132);
                pdf.setFontSize(20);
                pdf.text(365, pdf.autoTable.previous.finalY + 40, "Failure Parts");

                var failure = pdf.autoTableHtmlToJson(document.getElementById("failureItemReport"));

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                pdf.setFontSize(13);
                pdf.autoTable(failure.columns, failure.data, {
                    startY: pdf.autoTable.previous.finalY + 75,
                    margin: {horizontal: 50},
                    styles: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false},
                    columnStyles: {text: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false}},
                });


                //pdf.save(vm.inward.number + ".pdf");
                window.open(pdf.output('bloburl'), '_blank');
                $rootScope.hideBusyIndicator();
                $("#requestReport").hide();
                $rootScope.showSuccessMessage("Report generated successfully");

            }

            (function () {
                loadInwards();
            })();
        }
    }
);