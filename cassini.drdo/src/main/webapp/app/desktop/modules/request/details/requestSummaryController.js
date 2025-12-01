define(
    [
        'app/desktop/modules/request/request.module',
        'jspdf',
        'jspdf-autotable',
        'app/shared/services/core/requestService'
    ],
    function (module, jsPDF) {
        module.controller('RequestSummaryController', RequestSummaryController);

        function RequestSummaryController($scope, $window, $rootScope, $timeout, $state, $stateParams, $cookies,
                                          $interval, RequestService) {

            $rootScope.viewInfo.icon = "fa flaticon-stamp13";
            $rootScope.viewInfo.title = "Request Report";

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;
            vm.back = back;
            vm.reqId = $stateParams.requestId;

            function back() {
                $state.go('app.requests.all');
            }

            function loadRequestSummary() {
                vm.loading = true;
                RequestService.getSummaryByRequest(vm.reqId).then(
                    function (data) {
                        vm.requestSummary = data;
                        $rootScope.viewInfo.title = vm.requestSummary[0].request.reqNumber + " Request Report";
                        vm.loading = false;
                    }
                )
            }

            vm.printRequestSummary = printRequestSummary;

            function printRequestSummary() {
                $rootScope.showBusyIndicator($(".view-content"));
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

                    pdf.text(25, 150, 'Request : ' + vm.requestSummary[0].request.reqNumber);
                    pdf.text(pageWidth - 200, 150, 'Date : ' + todayStr);

                    var str = "Page " + data.pageCount;

                    pdf.text(str, pdf.internal.pageSize.width - 50, pdf.internal.pageSize.height - 10);
                };

                var personDetail = pdf.autoTableHtmlToJson(document.getElementById("requestSummary"));

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                pdf.setFontSize(13);
                pdf.autoTable(personDetail.columns, personDetail.data, {
                    addPageContent: pageContent,
                    startY: 170,
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
                            columnWidth: 200, overflow: 'linebreak', overflowColumns: false,
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7]
                        },
                        1: {
                            columnWidth: 120, overflow: 'linebreak', overflowColumns: false,
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7]
                        },
                        2: {
                            columnWidth: 80, overflow: 'linebreak', overflowColumns: false,
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
                        },
                        8: {
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

            (function () {
                loadRequestSummary();
            })();
        }
    }
);