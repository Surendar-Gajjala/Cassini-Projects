/**
 * Created by Nageshreddy on 02-01-2019.
 */
define([
        'app/desktop/modules/bom/bom.module',
        'jspdf',
        'jspdf-autotable',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/shared/services/core/failureListService',
        'app/shared/services/core/failureValueListService'

    ],
    function (module, jsPDF) {
        module.controller('ShowFailureValueListController', ShowFailureValueListController);

        function ShowFailureValueListController($scope, $timeout, $rootScope, CommonService, AttachmentService,
                                                FailureListService, FailureValueListService) {
            if ($application.homeLoaded == false) {
                return;
            }
            var vm = this;
            vm.failureLists = null;
            vm.failureValueList = [];
            //vm.item = $scope.data.selectedItem;
            vm.bomItem = $scope.data.item;
            var instance = $scope.data.instance;

            if (!vm.bomItem.item.itemMaster.itemType.hasLots) {
                vm.serialNo = /*instance.manufacturer.mfrCode + '-' + */instance.oemNumber;
                vm.certificateNo = instance.certificateNumber;
                vm.upn = instance.upnNumber;
            } else if (vm.bomItem.item.itemMaster.itemType.hasLots) {
                vm.serialNo = /*instance.itemInstance.manufacturer.mfrCode + '-' + */instance.itemInstance.oemNumber;
                vm.certificateNo = instance.itemInstance.certificateNumber;
                vm.upn = instance.itemInstance.upnNumber;
            }
            var valueMap = new Hashtable();
            vm.downloadFile = downloadFile;
            vm.bugs = [
                'Major',
                'Minor'
            ];

            vm.activities = [
                'Phase 2',
                'Vibration',
                'Phase 3',
                'Phase 4'
            ];

            function loadValues() {
                $("#failureListPdf").hide();
                FailureListService.getLists().then(
                    function (data) {
                        vm.failureLists = data;
                        angular.forEach(vm.failureLists[0].failureSteps, function (step) {
                            valueMap.put(step.serialNo, step);
                        });
                        loadFailureValues();
                    }
                )
            }

            vm.systemForPdf = null;
            vm.missileForPdf = null;
            vm.partItemName = null;
            vm.datetime = null;
            vm.check = check;

            function check(step) {
                FailureValueListService.createFailListValue(instance.id, step).then(
                    function (data) {
                        step.checkedDate = data.checkedDate;
                        step.checkedBy = data.checkedBy;
                        CommonService.getPersonReferences([step], 'checkedBy');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        step.value = false;
                    }
                )
            }

            vm.bdlModifiedName = null;
            vm.bdlCreatedDate = null;
            vm.casModifiedName = null;
            vm.casCreatedDate = null;
            vm.storeModifiedName = null;
            vm.storeCreatedDate = null;
            vm.ssqagModifiedName = null;
            vm.ssqagCreatedDate = null;
            vm.attachments = [];
            vm.newAttachments = [];

            function downloadFile(file) {
                var url = "{0}//{1}/api/col/attachments/{2}/preview".
                    format(window.location.protocol, window.location.host,
                    file.id);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = file.name;
                });
                /*window.open(url);
                 $timeout(function () {
                 window.close();
                 }, 2000);*/
            }

            function loadFailureValues() {

                vm.failureValueList1 = [];
                vm.failureValueList2 = [];
                vm.countValue = 1;
                var promise;
                if (!vm.bomItem.item.itemMaster.itemType.hasLots) {
                    promise = FailureValueListService.getByItemAndInstanceFailListValues(vm.bomItem.id, instance.id);
                } else if (vm.bomItem.item.itemMaster.itemType.hasLots) {
                    promise = FailureValueListService.getByItemAndLotInstanceFailListValues(vm.bomItem.id, instance.id);
                }
                promise.then
                (function (list) {
                        vm.failureValueList = list;
                        angular.forEach(vm.failureValueList, function (value) {
                            if (value.sno == 10) {
                                if (value.value != "" && value.value != null && value.value != undefined) {
                                    var attachmentsIds = value.value.split(", ");
                                    AttachmentService.getMultipleAttachments(attachmentsIds).then(
                                        function (data) {
                                            vm.attachments = data;
                                        })
                                }
                            }
                            vm.countValue++;
                            if (value.sno >= 11) {
                                vm.failureValueList1.push(value);
                            } else {
                                vm.failureValueList2.push(value);
                            }
                        });

                        CommonService.getPersonReferences(vm.failureValueList1, 'checkedBy');

                        $timeout(function () {

                            if (vm.failureValueList1 != null) {

                                if (vm.failureValueList1[0].sno != null) {
                                    if (vm.failureValueList1[0].sno == 11 && vm.failureValueList1[0].checkedByObject != undefined) {
                                        vm.bdlModifiedName = vm.failureValueList1[0].checkedByObject.firstName;
                                        vm.bdlCreatedDate = vm.failureValueList1[0].checkedDate;
                                    }
                                    else {
                                        vm.bdlModifiedName = '';
                                        vm.bdlCreatedDate = '';
                                    }
                                }
                                if (vm.failureValueList1[1].sno != null) {
                                    if (vm.failureValueList1[1].sno == 12 && vm.failureValueList1[1].checkedByObject != undefined) {
                                        vm.ssqagModifiedName = vm.failureValueList1[1].checkedByObject.firstName;
                                        vm.ssqagCreatedDate = vm.failureValueList1[1].checkedDate;
                                    }
                                    else {
                                        vm.ssqagModifiedName = '';
                                        vm.ssqagCreatedDate = '';
                                    }
                                }
                                if (vm.failureValueList1[2].sno != null) {
                                    if (vm.failureValueList1[2].sno == 13 && vm.failureValueList1[2].checkedByObject != undefined) {
                                        vm.casModifiedName = vm.failureValueList1[2].checkedByObject.firstName;
                                        vm.casCreatedDate = vm.failureValueList1[2].checkedDate;
                                    }
                                    else {
                                        vm.casModifiedName = '';
                                        vm.casCreatedDate = '';
                                    }
                                }
                                if (vm.failureValueList1[3].sno != null) {
                                    if (vm.failureValueList1[3].sno == 14 && vm.failureValueList1[3].checkedByObject != undefined) {
                                        vm.storeModifiedName = vm.failureValueList1[3].checkedByObject.firstName;
                                        vm.storeCreatedDate = vm.failureValueList1[3].checkedDate;
                                    }
                                    else {
                                        vm.storeModifiedName = '';
                                        vm.storeCreatedDate = '';
                                    }
                                }
                            }
                        }, 1000);

                        vm.sec = vm.failureValueList2[0].value;
                        var str_array = vm.sec.split('-');
                        var str_array1 = (vm.failureValueList2[3].value).split('-');
                        vm.systemForPdf = str_array[0];
                        vm.missileForPdf = str_array[1];
                        vm.partItemName = str_array1[1];
                        var currentdate = new Date();
                        vm.datetime = currentdate.getDate() + "/"
                            + (currentdate.getMonth() + 1) + "/"
                            + currentdate.getFullYear() + " "
                            + currentdate.getHours() + ":"
                            + currentdate.getMinutes() + ":"
                            + currentdate.getSeconds();


                        angular.forEach(vm.failureValueList, function (value) {
                            value.dataType = (valueMap.get(value.sno)).dataType;
                            value.status = (valueMap.get(value.sno)).status;
                        })
                        //})
                    }
                )
            }

            function updateFailureValueList() {
                FailureValueListService.updateFailListValues(vm.failureValueList2).then(
                    function (data) {
                        if (vm.newAttachments.length > 0) {
                            var attachmentStep = data[9];
                            if (attachmentStep.sno == 10) {
                                var ids = [];
                                angular.forEach(vm.newAttachments, function (at) {
                                    ids.push(attachmentStep.id);
                                });
                                AttachmentService.saveMultipleAttachments('FAILUREVALUELIST', ids, vm.newAttachments).then(
                                    function (data) {
                                        angular.forEach(data, function (att, $index) {
                                            if ($index == 0) {
                                                attachmentStep.value = att.id;
                                            } else {
                                                attachmentStep.value = attachmentStep.value + ", " + att.id;
                                            }
                                        });
                                        FailureValueListService.createFailListValue(attachmentStep.id, attachmentStep).then(
                                            function (data) {

                                            }
                                        )
                                    });

                            }
                        }
                        loadValues();
                        $rootScope.showSuccessMessage("Failure value list updated successfully");
                    });
            }

            function printFailureValueList() {
                $("#failureListPdf").show();

                var pdf = new jsPDF('l', 'pt', 'a4');
                pdf.setFont("courier");
                pdf.setFontType('bolditalic');
                pdf.setFontSize(16);
                pdf.setTextColor(107, 5, 132);
                pdf.text(365, 150, 'Failure Report');

                pdf.setFontSize(40);
                pdf.setTextColor(0, 0, 255);
                pdf.rect(20, 20, pdf.internal.pageSize.width - 40, pdf.internal.pageSize.height - 40, 'S');
                var imgData = $rootScope.drdoImage;
                pdf.addImage(imgData, 'JPG', 365, 10, 125, 125);
                var res1 = pdf.autoTableHtmlToJson(document.getElementById("failureTablepdfForSection"));
                pdf.autoTable(res1.columns, res1.data, {
                    startY: 170,
                    margin: {horizontal: 50},
                    styles: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false}
                });

                var res = pdf.autoTableHtmlToJson(document.getElementById("failureTablepdf"));
                pdf.autoTable(res.columns, res.data, {
                    startY: pdf.autoTable.previous.finalY + 20,
                    margin: {horizontal: 50},
                    styles: {overflow: 'linebreak', overflowColumns: false},
                    columnStyles: {
                        0: {columnWidth: 70},
                        1: {columnWidth: 220},
                        2: {columnWidth: 'auto'}
                    }
                });

                var pageHeight = pdf.internal.pageSize.height;
                if ((pdf.autoTable.previous.finalY + 100) >= pageHeight) {
                    pdf.addPage();
                    pdf.rect(20, 20, pdf.internal.pageSize.width - 40, pdf.internal.pageSize.height - 40, 'S');
                    pdf.autoTable.previous.finalY = 50;
                }

                pdf.autoTable.previous.finalY = pdf.autoTable.previous.finalY + 20;
                pdf.setFont('courier');
                pdf.setFontType('bolditalic');
                pdf.setFontSize(16);
                pdf.setTextColor(66, 134, 244);
                pdf.text(365, pdf.autoTable.previous.finalY, 'Approvals');
                pdf.setTextColor(172, 65, 244);

                if (vm.bdlModifiedName != null) {

                    pdf.setFontSize(15);
                    pdf.text(70, pdf.autoTable.previous.finalY + 20, 'BDL');
                    pdf.setFontSize(13);
                    pdf.setTextColor(24, 25, 24);
                    pdf.text(70, pdf.autoTable.previous.finalY + 40, 'Name: ' + vm.bdlModifiedName);

                    pdf.text(70, pdf.autoTable.previous.finalY + 60, vm.bdlCreatedDate);
                }
                else {

                    pdf.setFontSize(15);
                    pdf.text(70, pdf.autoTable.previous.finalY + 20, 'BDL');
                    pdf.setFontSize(13);
                    pdf.setTextColor(24, 25, 24);
                    pdf.text(70, pdf.autoTable.previous.finalY + 40, 'Name: ');
                    //pdf.text(50, pdf.autoTable.previous.finalY + 60, 'Date: ');
                }

                if (vm.ssqagModifiedName != null) {
                    pdf.setFontSize(15);
                    pdf.setTextColor(172, 65, 244);
                    pdf.text(250, pdf.autoTable.previous.finalY + 20, 'SSQAG');
                    pdf.setFontSize(13);
                    pdf.setTextColor(24, 25, 24);

                    pdf.text(250, pdf.autoTable.previous.finalY + 40, 'Name: ' + vm.ssqagModifiedName);
                    pdf.text(250, pdf.autoTable.previous.finalY + 60, vm.ssqagCreatedDate);
                }
                else {
                    pdf.setFontSize(15);
                    pdf.setTextColor(172, 65, 244);
                    pdf.text(250, pdf.autoTable.previous.finalY + 20, 'SSQAG');
                    pdf.setFontSize(13);
                    pdf.setTextColor(24, 25, 24);

                    pdf.text(250, pdf.autoTable.previous.finalY + 40, 'Name: ');
                    //pdf.text(50, pdf.autoTable.previous.finalY + 60, 'Date: ');
                }

                if (vm.casModifiedName != null) {

                    pdf.setFontSize(15);
                    pdf.setTextColor(172, 65, 244);
                    pdf.text(430, pdf.autoTable.previous.finalY + 20, 'CAS');
                    pdf.setFontSize(13);
                    pdf.setTextColor(24, 25, 24);
                    pdf.text(430, pdf.autoTable.previous.finalY + 40, 'Name: ' + vm.casModifiedName);
                    pdf.text(430, pdf.autoTable.previous.finalY + 60, vm.casCreatedDate);
                }
                else {

                    pdf.setFontSize(15);
                    pdf.setTextColor(172, 65, 244);
                    pdf.text(430, pdf.autoTable.previous.finalY + 20, 'CAS');
                    pdf.setFontSize(13);
                    pdf.setTextColor(24, 25, 24);
                    pdf.text(430, pdf.autoTable.previous.finalY + 40, 'Name: ');
                    //pdf.text(400, pdf.autoTable.previous.finalY + 60, 'Date: ');
                }

                if (vm.storeModifiedName != null) {
                    pdf.setFontSize(15);
                    pdf.setTextColor(172, 65, 244);
                    pdf.text(615, pdf.autoTable.previous.finalY + 20, 'STORES');
                    pdf.setFontSize(13);
                    pdf.setTextColor(24, 25, 24);
                    pdf.text(615, pdf.autoTable.previous.finalY + 40, 'Name: ' + vm.storeModifiedName);
                    pdf.text(615, pdf.autoTable.previous.finalY + 60, vm.storeCreatedDate);
                }
                else {

                    pdf.setFontSize(15);
                    pdf.setFontSize(15);
                    pdf.setTextColor(172, 65, 244);
                    pdf.text(615, pdf.autoTable.previous.finalY + 20, 'STORES');
                    pdf.setFontSize(13);
                    pdf.setTextColor(24, 25, 24);
                    pdf.text(615, pdf.autoTable.previous.finalY + 40, 'Name: ');
                    //pdf.text(625, pdf.autoTable.previous.finalY + 60, 'Date: ');
                }

                //pdf.save("Failure Report.pdf");
                window.open(pdf.output('bloburl'), '_blank');
                $("#failureListPdf").hide();
            }

            function loadFailureActivities() {
                CommonService.getLovByName("Failure Activities").then(
                    function (data) {
                        if (data.values != null || data.values != undefined)
                            vm.activities = data.values;
                    });
            }

            (function () {
                loadValues();
                loadFailureActivities();
                $("#failureListPdf").hide();
                $rootScope.$on('update.failureList', updateFailureValueList);
                $rootScope.$on('print.failureList', printFailureValueList);
            })();
        }
    }
);
