define(
    [
        'app/desktop/modules/issue/issue.module',
        'jspdf',
        'jspdf-autotable',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/requestService',
        'app/shared/services/core/issueService',
        'app/shared/services/core/inwardService',
        'app/shared/services/core/bomService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module, jsPDF) {
        module.controller('IssueDetailsController', IssueDetailsController);

        function IssueDetailsController($scope, $window, $rootScope, $timeout, $state, $stateParams, $cookies, ItemTypeService,
                                        ItemService, $uibModal, $interval, AttachmentService, RequestService, IssueService, InwardService,
                                        CommonService, DialogService, BomService, ObjectAttributeService, AttributeAttachmentService) {

            $rootScope.viewInfo.icon = "fa flaticon-stamp13";
            $rootScope.viewInfo.title = "Issue Details";

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.issueId = $stateParams.issueId;

            vm.back = back;
            vm.mode = $stateParams.mode;
            vm.generateReport = generateReport;

            function back() {
                if (vm.mode == 'home') {
                    $state.go('app.home');
                } else {
                    $state.go('app.issues.all');
                }
            }

            vm.system = null;
            vm.bdlQc = null;
            vm.hideApprove = false;
            function loadIssue() {
                $timeout(function () {
                    $rootScope.showBusyIndicator($('.view-container'));
                }, 100)
                IssueService.getIssue(vm.issueId).then(
                    function (data) {
                        vm.issuedBomItems = [];
                        vm.issue = data;
                        $rootScope.viewInfo.title = "Issue Details : " + vm.issue.number;
                        $rootScope.viewInfo.description = "Status :" + vm.issue.status;
                        vm.system = vm.issue.request.reqNumber.substring(0, 2);
                        loadIssuedPersonPermission();
                        IssueService.getIssueDetails(vm.issue.id).then(
                            function (data) {

                                vm.sectionGroup = data.issuedItemsDtos;
                                vm.receives = data.receiveDtoList;
                                vm.issueHistories = data.issueHistories;
                                vm.issuedItems = [];

                                var attributeDefs = [];
                                var instanceIds = [];
                                angular.forEach(vm.itemInstanceAttributes, function (attribute) {
                                    if (attribute.attributeDef.id != null && attribute.attributeDef.id != "" && attribute.attributeDef.id != 0) {
                                        attributeDefs.push(attribute.attributeDef.id);
                                    }
                                });
                                var count = 0;
                                var approved = 0;
                                angular.forEach(vm.sectionGroup, function (group) {
                                        group.section.level = 0;
                                        group.section.children = [];
                                        group.section.type = "SECTION";
                                        vm.issuedItems.push(group.section);

                                        angular.forEach(group.issuedItems, function (issueItem) {
                                            count++;
                                            issueItem.level = group.section.level + 1;
                                            issueItem.type = "ISSUEITEM";
                                            group.section.children.push(issueItem);
                                            vm.issuedItems.push(issueItem);
                                            if (issueItem.issueItem.status == "P_APPROVED" || issueItem.issueItem.status == "APPROVED") {
                                                vm.issuedBomItems.push(issueItem);
                                            }
                                            instanceIds.push(issueItem.itemInstance.id);

                                            if (issueItem.issueItem.approved) {
                                                approved++;
                                            }

                                            if (issueItem.issueItem.approvedBy != null && vm.bdlQc == null) {
                                                vm.bdlQc = issueItem.issueItem.approvedBy;
                                            }
                                        })

                                        if (count == approved) {
                                            vm.hideApprove = true;
                                        }

                                        if (count == instanceIds.length) {
                                            InwardService.getAttributesByItemIdAndAttributeId(instanceIds, attributeDefs).then(
                                                function (data) {
                                                    vm.selectedObjectAttributes = data;

                                                    var map = new Hashtable();
                                                    angular.forEach(vm.itemInstanceAttributes, function (att) {
                                                        if (att.attributeDef.id != null && att.attributeDef.id != "" && att.attributeDef.id != 0) {
                                                            map.put(att.attributeDef.id, att);
                                                        }
                                                    });

                                                    angular.forEach(vm.issuedItems, function (issueItem) {
                                                        if (issueItem.type == "ISSUEITEM") {
                                                            var attributes = [];
                                                            attributes = vm.selectedObjectAttributes[issueItem.itemInstance.id];

                                                            angular.forEach(attributes, function (attribute) {
                                                                var selectAtt = map.get(attribute.id.attributeDef);
                                                                if (selectAtt != null) {
                                                                    if (selectAtt.attributeDef.dataType == 'ATTACHMENT' && selectAtt.attributeDef.name == "Certificate") {
                                                                        var revisionAttachmentIds = [];
                                                                        if (attribute.attachmentValues.length > 0) {
                                                                            angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                                                revisionAttachmentIds.push(attachmentId);
                                                                            });
                                                                            AttributeAttachmentService.getMultipleAttributeAttachments(revisionAttachmentIds).then(
                                                                                function (data) {
                                                                                    vm.revisionAttachments = data;
                                                                                    issueItem.certificates = vm.revisionAttachments;
                                                                                }
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    })

                                                }
                                            )
                                        }
                                    }
                                );

                                angular.forEach(vm.receives, function (recieve) {
                                    angular.forEach(recieve.receiveItems, function (receiveItem) {
                                        receiveItem.itemInstanceText = "";
                                        angular.forEach(receiveItem.itemInstances, function (instance) {
                                            if (receiveItem.itemInstanceText == "") {
                                                receiveItem.itemInstanceText = instance.oemNumber;
                                            } else {
                                                receiveItem.itemInstanceText = receiveItem.itemInstanceText + ' , ' + instance.oemNumber;
                                            }
                                        });
                                        angular.forEach(receiveItem.lotInstances, function (instance) {
                                            if (receiveItem.itemInstanceText == "") {
                                                receiveItem.itemInstanceText = instance.itemInstance.oemNumber + '/' + instance.sequence;
                                            } else {
                                                receiveItem.itemInstanceText = receiveItem.itemInstanceText + ' , ' + instance.itemInstance.oemNumber + '/' + instance.sequence;
                                            }
                                        });
                                    })
                                });

                                //vm.issuedItems = data;
                                CommonService.getPerson(vm.issue.createdBy).then(
                                    function (data) {
                                        vm.issue.createdByObject = data;
                                    }
                                )
                                CommonService.getPerson(vm.issue.modifiedBy).then(
                                    function (data) {
                                        vm.issue.modifiedByObject = data;
                                    }
                                )

                                $timeout(function () {
                                    $rootScope.hideBusyIndicator();
                                }, 100);
                            }
                        )
                    }
                )
            }

            function generateReport() {
                $("#issueReport").show();

                var pdf = new jsPDF('l', 'pt', 'a4');
                var pageWidth = pdf.internal.pageSize.width;
                pdf.setFont('arial narrow');


                /*var requestReport = pdf.autoTableHtmlToJson(document.getElementById("issueReport"));

                 pdf.setTextColor(7, 7, 7);
                 pdf.setFontStyle('normal');
                 pdf.setFontSize(13);
                 pdf.autoTable(requestReport.columns, requestReport.data, {
                 startY: 220,
                 margin: {horizontal: 50},
                 styles: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false},
                 columnStyles: {text: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false}},
                 });*/


                var pageContent = function (data) {
                    //Header

                    pdf.setFontStyle('bold');
                    pdf.setFontSize(20);
                    pdf.text(350, 50, "ISSUE REPORT");
                    pdf.setFontSize(13);
                    pdf.setTextColor(7, 7, 7);
                    pdf.setFontStyle('normal');
                    var imgData = $rootScope.drdoImage;
                    pdf.addImage(imgData, 'JPG', pageWidth - 160, 30, 125, 125);

                    var today = moment(new Date());
                    var todayStr = today.format('DD/MM/YYYY HH:mm');

                    pdf.setFontSize(13);
                    pdf.setFontStyle('normal');

                    pdf.text(25, 70, 'Issue Number : ' + vm.issue.number);
                    pdf.text(25, 90, 'Request Number : ' + vm.issue.request.reqNumber);
                    pdf.text(25, 110, 'Section : ' + vm.issuedItems[0].name);
                    pdf.text(25, 130, 'Issue Date : ' + vm.issue.createdDate);

                    // FOOTER
                    pdf.setFontSize(16);
                    //pdf.setTextColor(66, 134, 244);

                    pdf.text(25, pdf.internal.pageSize.height - 115, "Issued By");
                    pdf.text(pageWidth / 2.5, pdf.internal.pageSize.height - 115, "Verified By");
                    pdf.text(pageWidth - 250, pdf.internal.pageSize.height - 115, "Received By");

                    pdf.setFontSize(13);
                    //pdf.setTextColor(66, 244, 113);

                    if (vm.bondageStorePerson != null && vm.bondageStorePerson != "") {
                        pdf.text(25, pdf.internal.pageSize.height - 95, "Bonded Store In-charge : " + vm.issue.createdByObject.fullName);
                    } else {
                        pdf.text(25, pdf.internal.pageSize.height - 95, vm.issue.createdByObject.fullName);
                    }
                    pdf.text(pageWidth / 2.5, pdf.internal.pageSize.height - 95, "BDL QC : " + vm.bdlQc.fullName);
                    pdf.text(pageWidth - 250, pdf.internal.pageSize.height - 95, "BDL PPC");


                    pdf.setFontSize(12);
                    //pdf.setTextColor(24, 25, 24);
                    pdf.text(25, pdf.internal.pageSize.height - 75, "Name : ");
                    pdf.text(pageWidth / 2.5, pdf.internal.pageSize.height - 75, "Name : ");
                    pdf.text(pageWidth - 250, pdf.internal.pageSize.height - 75, "Name : ");

                    pdf.text(25, pdf.internal.pageSize.height - 50, "Date : ");
                    pdf.text(pageWidth / 2.5, pdf.internal.pageSize.height - 50, "Date : ");
                    pdf.text(pageWidth - 250, pdf.internal.pageSize.height - 50, "Date : ");

                    var str = "Page " + data.pageCount;

                    pdf.text(str, pdf.internal.pageSize.width - 50, pdf.internal.pageSize.height - 10);
                };

                var personDetail = pdf.autoTableHtmlToJson(document.getElementById("issueDetailsReport"));

                /*pdf.setFontSize(20);
                 pdf.setFontStyle('bold');
                 pdf.text(380, 130, "Parts");*/

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                pdf.setFontSize(13);
                pdf.autoTable(personDetail.columns, personDetail.data, {
                    addPageContent: pageContent,
                    startY: 160,
                    margin: {horizontal: 25, bottom: 160, top: 145},
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
                            columnWidth: 50, overflow: 'linebreak', overflowColumns: false,
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7]
                        },
                        1: {
                            columnWidth: 100, overflow: 'linebreak', overflowColumns: false,
                            fillColor: [255, 255, 255],
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
                        },
                        8: {
                            columnWidth: 70, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle', halign: 'center',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7]
                        },
                        9: {
                            columnWidth: 70, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle', halign: 'center',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7]
                        }
                    }
                });

                /*pdf.setTextColor(107, 5, 132);
                 pdf.setFontSize(20);
                 pdf.text(370, pdf.autoTable.previous.finalY + 40, "Approvals");*/


                //pdf.save(vm.issue.number + ".pdf");

                window.open(pdf.output('bloburl'), '_blank');

                $rootScope.hideBusyIndicator();
                $("#issueReport").hide();
                $rootScope.showSuccessMessage("Report generated successfully");

            }


            vm.printReceive = printReceive;
            function printReceive(receive) {
                $("#issueReport").show();

                var pdf = new jsPDF('l', 'pt', 'a4');
                var pageWidth = pdf.internal.pageSize.width;
                pdf.setFont('arial narrow');

                var pageContent = function (data) {
                    //Header

                    pdf.setFontStyle('bold');
                    pdf.setFontSize(20);
                    pdf.text(350, 50, "ISSUE REPORT");
                    pdf.setFontSize(13);
                    pdf.setTextColor(7, 7, 7);
                    pdf.setFontStyle('normal');
                    var imgData = $rootScope.drdoImage;
                    pdf.addImage(imgData, 'JPG', pageWidth - 160, 30, 100, 100);

                    var today = moment(new Date());

                    pdf.setFontSize(13);
                    pdf.setFontStyle('normal');

                    pdf.text(25, 70, 'Issue Number : ' + vm.issue.number + " / " + receive.receive.receiveSequence);
                    pdf.text(25, 90, 'Request Number : ' + vm.issue.request.reqNumber);
                    if (vm.issue.versity) {
                        pdf.text(25, 110, 'Section : ' + vm.issuedItems[0].name + " ( VSPL )");
                    } else {
                        pdf.text(25, 110, 'Section : ' + vm.issuedItems[0].name);
                    }

                    pdf.text(25, 130, 'Issue Date : ' + receive.receive.createdDate);

                    // FOOTER
                    pdf.setFontSize(16);
                    //pdf.setTextColor(66, 134, 244);

                    pdf.text(25, pdf.internal.pageSize.height - 115, "Issued By");
                    pdf.text(pageWidth / 2.5, pdf.internal.pageSize.height - 115, "Verified By");
                    pdf.text(pageWidth - 250, pdf.internal.pageSize.height - 115, "Received By");

                    pdf.setFontSize(13);
                    //pdf.setTextColor(66, 244, 113);

                    if (vm.bondageStorePerson != null && vm.bondageStorePerson != "") {
                        pdf.text(25, pdf.internal.pageSize.height - 95, "Bonded Store In-charge : " + vm.issue.createdByObject.fullName);
                    } else {
                        pdf.text(25, pdf.internal.pageSize.height - 95, vm.issue.createdByObject.fullName);
                    }
                    pdf.text(pageWidth / 2.5, pdf.internal.pageSize.height - 95, "BDL QC : " + receive.issueItems[0].issueItem.approvedBy.fullName);
                    pdf.text(pageWidth - 250, pdf.internal.pageSize.height - 95, "BDL PPC : " + receive.receivedBy.fullName);


                    pdf.setFontSize(12);
                    //pdf.setTextColor(24, 25, 24);
                    pdf.text(25, pdf.internal.pageSize.height - 75, "Name : ");
                    pdf.text(pageWidth / 2.5, pdf.internal.pageSize.height - 75, "Name : ");
                    pdf.text(pageWidth - 250, pdf.internal.pageSize.height - 75, "Name : ");

                    pdf.text(25, pdf.internal.pageSize.height - 50, "Date : ");
                    pdf.text(pageWidth / 2.5, pdf.internal.pageSize.height - 50, "Date : ");
                    pdf.text(pageWidth - 250, pdf.internal.pageSize.height - 50, "Date : ");

                    var str = "Page " + data.pageCount;

                    pdf.text(str, pdf.internal.pageSize.width - 50, pdf.internal.pageSize.height - 10);
                };

                var personDetail = pdf.autoTableHtmlToJson(document.getElementById("receive" + receive.receive.id));

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                pdf.setFontSize(13);
                pdf.autoTable(personDetail.columns, personDetail.data, {
                    addPageContent: pageContent,
                    startY: 160,
                    margin: {horizontal: 25, bottom: 160, top: 145},
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
                            columnWidth: 35, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle', halign: 'center',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7],
                            lineWidth: 0.2,
                            lineColor: [110, 107, 107]
                        },
                        1: {
                            columnWidth: 150, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle', halign: 'left',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7],
                            lineWidth: 0.1,
                            lineColor: [110, 107, 107]
                        },
                        2: {
                            columnWidth: 50, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7],
                            lineWidth: 0.1,
                            lineColor: [110, 107, 107]
                        },
                        3: {
                            columnWidth: 55, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle', halign: 'center',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7],
                            lineWidth: 0.1,
                            lineColor: [110, 107, 107]
                        },
                        4: {
                            columnWidth: 60, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle', halign: 'center',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7],
                            lineWidth: 0.1,
                            lineColor: [110, 107, 107]
                        },
                        5: {
                            columnWidth: 85, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7],
                            lineWidth: 0.1,
                            lineColor: [110, 107, 107]
                        },
                        6: {
                            columnWidth: 120, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7],
                            lineWidth: 0.1,
                            lineColor: [110, 107, 107]
                        },
                        7: {
                            columnWidth: 120, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7],
                            lineWidth: 0.1,
                            lineColor: [110, 107, 107]
                        },
                        8: {
                            columnWidth: 110, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7],
                            lineWidth: 0.1,
                            lineColor: [110, 107, 107]
                        }
                    }
                });

                //pdf.save(vm.issue.number + ".pdf");

                window.open(pdf.output('bloburl'), '_blank')

                $rootScope.hideBusyIndicator();
                $("#issueReport").hide();
                $rootScope.showSuccessMessage("Report generated successfully");

            }

            vm.approveIssue = approveIssue;
            vm.approveIssueItem = approveIssueItem;
            vm.issueItems = issueItems;

            function approveIssue() {
                var options = {
                    title: "Approve Issue",
                    message: "Please confirm to Approve this Issue",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($(".view-container"));
                            IssueService.approveIssue(vm.issue).then(
                                function (data) {
                                    loadIssue();
                                    $rootScope.showSuccessMessage("Issue Approved successfully");
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

            function approveIssueItem(issueItem) {
                var options = {
                    title: "Approve Issue Item",
                    message: "Please confirm to Approve this Issue Item",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($(".view-container"));
                            IssueService.approveIssueItem(vm.issueId, issueItem).then(
                                function (data) {
                                    loadIssue();
                                    $rootScope.showSuccessMessage("Issue Item Approved successfully");
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

            vm.approveItem = approveItem;
            function approveItem(issueItem) {
                var options = {
                    title: "Approve Issue Item",
                    message: "Please confirm to Approve this Issue Item",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($(".view-container"));
                            IssueService.approveItem(vm.issueId, issueItem).then(
                                function (data) {
                                    loadIssue();
                                    $rootScope.showSuccessMessage("Issue Item Approved successfully");
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

            function issueItems() {
                var options = {
                    title: "Issue Items",
                    message: "Please confirm to Issue this Items",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($(".view-container"));
                            IssueService.issueItems(vm.issue).then(
                                function (data) {
                                    loadIssue();
                                    $rootScope.showSuccessMessage("Items issued successfully");
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

            function loadItemInstanceAttributes() {
                vm.itemInstanceAttributes = [];
                ItemTypeService.getAttributesByObjectType("ITEMINSTANCE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: null,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                value: {
                                    id: {
                                        objectId: null,
                                        attributeDef: attribute.id
                                    },
                                    stringValue: null,
                                    integerValue: null,
                                    doubleValue: null,
                                    booleanValue: null,
                                    dateValue: null,
                                    listValue: null,
                                    newListValue: null,
                                    listValueEditMode: false,
                                    timeValue: null,
                                    timestampValue: null,
                                    refValue: null,
                                    imageValue: null,
                                    attachmentValues: [],
                                    currencyValue: null,
                                    currencyType: null
                                },
                                stringValue: null,
                                integerValue: null,
                                doubleValue: null,
                                booleanValue: null,
                                dateValue: null,
                                listValue: null,
                                newListValue: null,
                                listValueEditMode: false,
                                timeValue: null,
                                timestampValue: null,
                                refValue: null,
                                imageValue: null,
                                attachmentValues: [],
                                currencyValue: null,
                                currencyType: null
                            };
                            vm.itemInstanceAttributes.push(att);
                        });
                    }
                )
            }

            vm.openPropertyAttachment = openPropertyAttachment;
            function openPropertyAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/preview".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = attachment.name;
                });
                /*window.open(url);
                $timeout(function () {
                    window.close();
                 }, 2000);*/
                //launchUrl(url);
            }

            vm.sidePanel = false;
            vm.showIssueInfoPanel = showIssueInfoPanel;
            function showIssueInfoPanel(show) {
                if (show) {
                    $('#issueInfoPanel').show('slide', {direction: 'left'}, 600);
                }
                else {
                    $('#issueInfoPanel').hide('slide', {direction: 'left'}, 600);
                }
            }

            vm.showProvisionalApproveDialog = showProvisionalApproveDialog;
            vm.closeProvisionalApproveDialog = closeProvisionalApproveDialog;

            function showProvisionalApproveDialog(issuedItem) {
                vm.selectedIssueItem = issuedItem;
                vm.selectedIssueItem.itemInstance.reason = null;
                var modal = document.getElementById("pApprove-view");
                modal.style.display = "block";
            }

            function closeProvisionalApproveDialog() {
                var modal = document.getElementById("pApprove-view");
                modal.style.display = "none";
                vm.selectedIssueItem = null;
                vm.errorMessage = null;
            }

            vm.showRejectDialog = showRejectDialog;
            vm.closeRejectDialog = closeRejectDialog;

            function showRejectDialog(issuedItem) {
                vm.selectedIssueItem = issuedItem;
                vm.selectedIssueItem.itemInstance.reason = null;
                var modal = document.getElementById("reject-view");
                modal.style.display = "block";
            }

            function closeRejectDialog() {
                var modal = document.getElementById("reject-view");
                modal.style.display = "none";
                vm.selectedIssueItem = null;
                vm.errorMessage = null;
            }


            vm.provisionalApproveIssueItem = provisionalApproveIssueItem;
            function provisionalApproveIssueItem(issueItem) {
                if (vm.selectedIssueItem.itemInstance.reason == null || vm.selectedIssueItem.itemInstance.reason == "" || vm.selectedIssueItem.itemInstance.reason == undefined) {
                    vm.errorMessage = "Please enter reason";
                } else {
                    $rootScope.showBusyIndicator($(".view-container"));
                    IssueService.partiallyApproveIssueItem(vm.issueId, vm.selectedIssueItem).then(
                        function (data) {
                            loadIssue();
                            vm.selectedIssueItem = null;
                            vm.errorMessage = null;
                            closeProvisionalApproveDialog();
                            $rootScope.showSuccessMessage("Item Provisionally Approved successfully");
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.rejectIssueItem = rejectIssueItem;
            function rejectIssueItem() {
                if (vm.selectedIssueItem.itemInstance.reason == null || vm.selectedIssueItem.itemInstance.reason == "" || vm.selectedIssueItem.itemInstance.reason == undefined) {
                    vm.errorMessage = "Please enter reason";
                } else {
                    $rootScope.showBusyIndicator($(".view-container"));
                    IssueService.rejectIssueItem(vm.issueId, vm.selectedIssueItem).then(
                        function (data) {
                            loadIssue();
                            vm.selectedIssueItem = null;
                            vm.errorMessage = null;
                            closeRejectDialog();
                            $rootScope.showSuccessMessage("Item Rejected successfully");
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.resetReturnItem = resetReturnItem;
            function resetReturnItem(issueItem) {
                var options;
                IssueService.checkRejectedItemAlreadyIssued(vm.issueId, issueItem).then(
                    function (data) {
                        if (data == null || data == "") {
                            var message = null;
                            if (vm.issue.versity) {
                                message = "Please confirm to reset returned Item by Versity QC";
                            } else {
                                message = "Please confirm to reset returned Item by BDL QC";
                            }

                            options = {
                                title: "Reset Returned Item",
                                message: message,
                                okButtonClass: 'btn-danger'
                            };

                            DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        $rootScope.showBusyIndicator($(".view-container"))
                                        IssueService.resetReturnedItem(vm.issueId, issueItem).then(
                                            function (data) {
                                                loadIssue();
                                                $rootScope.showSuccessMessage("Reset done successfully");
                                                //$rootScope.hideBusyIndicator();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        )
                                    }
                                }
                            )
                        } else {
                            options = {
                                title: "Please Confirm",
                                message: "Issue happened on this part. And this part will added to Inventory and will be removed from this issue.",
                                okButtonClass: 'btn-danger'
                            };

                            DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        $rootScope.showBusyIndicator($(".view-container"));
                                        IssueService.addRejectedItemToInventory(vm.issueId, issueItem).then(
                                            function (data) {
                                                loadIssue();
                                                $rootScope.showSuccessMessage("Item Updated successfully");
                                                //$rootScope.hideBusyIndicator();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        )
                                    }
                                }
                            )
                        }
                    }
                )
            }

            vm.selectedReceiveItems = [];
            vm.selectReceiveItem = selectReceiveItem;
            function selectReceiveItem(issuedItem) {
                if (issuedItem.receive) {
                    vm.selectedReceiveItems.push(issuedItem.issueItem);
                } else {
                    vm.selectedReceiveItems.splice(vm.selectedReceiveItems.indexOf(issuedItem.issueItem), 1);
                }
            }

            vm.receiveItems = receiveItems;

            function receiveItems() {
                if (vm.selectedReceiveItems.length == 0) {
                    $rootScope.showErrorMessage("Please select at least one item");
                } else {
                    var options = {
                        title: "Receive Items",
                        message: "Please confirm to receive selected Items",
                        okButtonClass: 'btn-danger'
                    };

                    DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                $rootScope.showBusyIndicator($(".view-container"));
                                IssueService.receiveItems(vm.issue.id, vm.selectedReceiveItems).then(
                                    function (data) {
                                        loadIssue();
                                        $rootScope.showSuccessMessage("Items received successfully");
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
            }

            vm.bondageStorePerson = null;
            function loadIssuedPersonPermission() {
                IssueService.getIssuedPersonPermission(vm.issue.createdBy).then(
                    function (data) {
                        vm.bondageStorePerson = data;
                    }
                )
            }

            (function () {
                loadItemInstanceAttributes();
                loadIssue();
            })();
        }
    }
)
;