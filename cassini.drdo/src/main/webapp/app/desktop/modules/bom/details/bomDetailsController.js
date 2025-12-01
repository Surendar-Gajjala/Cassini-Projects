define(
    [
        'app/desktop/modules/bom/bom.module',
        'jspdf',
        'jspdf-autotable',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/drdoApplication',
        'app/shared/services/core/bomService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/bomGroupService',
        'app/shared/services/core/partTrackingService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module, jsPDF) {
        module.controller('BomDetailsController', BomDetailsController);

        function BomDetailsController($scope, $rootScope, $bom, $window, $timeout, $state, $stateParams, $cookies, $translate,
                                      BomService, ItemService, PartTrackingService, DialogService, CommonService) {
            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            var lastSelectedItem = null;

            vm.selectedBom = null;
            vm.showBomItems = false;
            vm.loading = true;
            vm.selectedWorkCenter = null;
            vm.printItems = [];
            vm.showPrintData = false;

            vm.selectItem = selectItem;
            vm.addBomItem = addBomItem;
            vm.createFailureList = createFailureList;

            $rootScope.showWorkCenterItems = showWorkCenterItems;
            $rootScope.closeWorkCentersMenu = closeWorkCentersMenu;
            vm.assignPartTracking = assignPartTracking;
            vm.showPartTracking = showPartTracking;
            vm.showFailureList = showFailureList;
            vm.getInstanceTarbDocument = getInstanceTarbDocument;
            vm.getInstanceSectionTarbDocument = getInstanceSectionTarbDocument;

            vm.showSectionRequestReport = showSectionRequestReport;
            vm.closeSectionRequestReport = closeSectionRequestReport;
            vm.getSectionRequestReport = getSectionRequestReport;
            vm.printSectionRequestReport = printSectionRequestReport;
            vm.getInstanceRequestReport = getInstanceRequestReport;
            $rootScope.showSectionsMenu = showSectionsMenu;
            $rootScope.closeSectionsMenu = closeSectionsMenu;

            vm.toggleNode = toggleNode;
            vm.addNewBomItem = addNewBomItem;
            $rootScope.addSearchItemToBom = addSearchItemToBom;
            vm.editItem = editItem;
            vm.saveItem = saveItem;
            vm.cancelChanges = cancelChanges;
            vm.removeItem = removeItem;
            vm.deleteItem = deleteItem;
            vm.createBomGroupType = createBomGroupType;
            $rootScope.addBomGroupTypeToBom = addBomGroupTypeToBom;
            vm.printSelectedType = printSelectedType;
            vm.printSelectedSections = printSelectedSections;
            vm.onSelectWorkCenter = onSelectWorkCenter;

            vm.bomItems = [];
            vm.workCenters = [];
            vm.emptyBomItem = {
                id: null,
                parent: null,
                bom: null,
                item: null,
                quantity: 0,
                fractionalQuantity: 0.0,
                bomItemType: null,
                typeRef: null,
                hierarchicalCode: null
            };

            var pageable = {
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
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };


            vm.workCenterItems = [];

            function getInstanceTarbDocument(item) {
                $rootScope.showBusyIndicator();
                BomService.getBomInstanceTarbDocument(item.id).then(
                    function (data) {
                        vm.bomInstanceTarbDocument = data;

                        $timeout(function () {
                            printBomInstanceTarbDocument("INSTANCE");
                        }, 1000);
                    }
                )
            }

            function getInstanceSectionTarbDocument(item) {
                $rootScope.showBusyIndicator();
                BomService.getBomInstanceSectionTarbDocument(vm.selectedBom.id, item.id).then(
                    function (data) {
                        vm.bomInstanceTarbDocument = data;
                        $timeout(function () {
                            printBomInstanceTarbDocument("SECTION");
                        }, 1000);
                    }
                )
            }

            function printBomInstanceTarbDocument(type) {

                var pdf = new jsPDF('l', 'pt', 'a4');

                pdf.setFont('arial narrow');
                pdf.setFontSize(13);
                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                var imgData = $rootScope.drdoImage;
                pdf.addImage(imgData, 'JPG', 365, 20, 125, 125);
                var pageNumber = 1;
                var today = moment(new Date());
                var todayStr = today.format('DD/MM/YYYY HH:mm');

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('bold');
                pdf.setFontSize(15);
                pdf.text(290, 165, "Technical Architecture Review Board Document");

                pdf.setFontSize(13);
                pdf.text(50, 185, "BOM :" + vm.bomInstanceTarbDocument.system.item.itemMaster.itemName + " / " + " Missile :" + vm.bomInstanceTarbDocument.bomInstance.item.instanceName);
                pdf.text(650, 185, "Date : " + todayStr);

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');

                var pageHeight = pdf.internal.pageSize.height;
                var pageWidth = pdf.internal.pageSize.width;

                pdf.text(pageWidth - 25, pageHeight - 30, "" + pageNumber);
                pdf.text(50, 215, "The main objective of this document is to Verify and Approve the Parts which were assembled in the Missile.")

                angular.forEach(vm.bomInstanceTarbDocument.sectionTarbDtoList, function (sectionList) {

                    var personDetail = pdf.autoTableHtmlToJson(document.getElementById("bomInstanceTarbDocument" + vm.bomInstanceTarbDocument.sectionTarbDtoList.indexOf(sectionList)));

                    var txt = "";
                    var fontSize = null;
                    var pageWidth = null;
                    var txtWidth = null;
                    var xPosition = null;

                    if (!pdf.autoTable.previous) {
                        if (sectionList.subsystem) {
                            var yPostiton = null;
                            pdf.setTextColor(7, 7, 7);
                            pdf.setFontStyle('bold');
                            if (type == "INSTANCE") {
                                pdf.text(25, 235, sectionList.section.typeRef.name + " / " + sectionList.subsystem.typeRef.name + " : ");
                                yPostiton = 255;
                            } else {
                                pdf.text(385, 240, sectionList.section.typeRef.name);
                                pdf.text(25, 255, sectionList.subsystem.typeRef.name + " : ");
                                yPostiton = 270;
                            }

                            pdf.setTextColor(7, 7, 7);
                            pdf.setFontStyle('normal');
                            pdf.autoTable(personDetail.columns, personDetail.data, {
                                startY: yPostiton,
                                margin: {horizontal: 25},
                                styles: {overflow: 'linebreak', overflowColumns: false},
                                //columnStyles: {text: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false}},
                                columnStyles: {
                                    0: {columnWidth: 280, overflow: 'linebreak', overflowColumns: false},
                                    1: {columnWidth: 150, overflow: 'linebreak', overflowColumns: false},
                                    2: {
                                        columnWidth: 60,
                                        overflow: 'linebreak',
                                        overflowColumns: false
                                    },
                                    3: {
                                        columnWidth: 150,
                                        overflow: 'linebreak',
                                        overflowColumns: false
                                    },
                                    4: {
                                        columnWidth: 80,
                                        overflow: 'linebreak',
                                        overflowColumns: false
                                    },
                                    5: {
                                        columnWidth: 80,
                                        overflow: 'linebreak',
                                        overflowColumns: false
                                    }
                                },
                                //styles: {overflow: 'linebreak', overflowColumns: false, columnWidth: 'auto'}
                            });

                            pdf.autoTable.previous.finalY = pdf.autoTable.previous.finalY + 30;

                            if ((pdf.autoTable.previous.finalY + 160) >= pageHeight) {
                                pdf.addPage();
                                pageWidth = pdf.internal.pageSize.width;
                                /*pageNumber = pageNumber + 1;
                                 pdf.text(pageWidth - 25, pageHeight - 30, "" + pageNumber);*/
                                pdf.autoTable.previous.finalY = 50;
                            }

                            pdf.setTextColor(7, 7, 7);
                            pdf.setFontStyle('bold');

                            pdf.text(385, pdf.autoTable.previous.finalY, "Signatures");

                            pdf.autoTable.previous.finalY = pdf.autoTable.previous.finalY + 20;

                            txt = "( " + sectionList.subsystem.typeRef.name + " Parts Chief of )";
                            fontSize = pdf.internal.getFontSize();

                            pageWidth = pdf.internal.pageSize.width;

                            txtWidth = pdf.getStringUnitWidth(txt) * fontSize / pdf.internal.scaleFactor;

                            xPosition = ( pageWidth - txtWidth ) / 2;

                            pdf.text(xPosition, pdf.autoTable.previous.finalY, "( " + sectionList.subsystem.typeRef.name + " Parts Chief of )");

                            pdf.autoTable.previous.finalY = pdf.autoTable.previous.finalY + 30;

                            pdf.setTextColor(7, 7, 7);
                            pdf.setFontStyle('normal');
                            pdf.text(200, pdf.autoTable.previous.finalY, "BDL");
                            pdf.text(400, pdf.autoTable.previous.finalY, "SSQAG");
                            pdf.text(600, pdf.autoTable.previous.finalY, "CAS");

                            pdf.autoTable.previous.finalY = pdf.autoTable.previous.finalY + 110;

                            //pdf.line(40, pdf.autoTable.previous.finalY, pageWidth - 20, pdf.autoTable.previous.finalY);
                        }

                    } else {
                        if (sectionList.subsystem != null) {
                            if ((pdf.autoTable.previous.finalY + 200) >= pageHeight) {
                                pdf.addPage();
                                pageWidth = pdf.internal.pageSize.width;
                                /*pageNumber = pageNumber + 1;
                                 pdf.text(pageWidth - 25, pageHeight - 30, "" + pageNumber);*/
                                pdf.autoTable.previous.finalY = 50;
                            }

                            pdf.setTextColor(7, 7, 7);
                            pdf.setFontStyle('bold');
                            if (type == "INSTANCE") {
                                pdf.text(25, pdf.autoTable.previous.finalY, sectionList.section.typeRef.name + " / " + sectionList.subsystem.typeRef.name + " : ");
                            } else {
                                pdf.text(25, pdf.autoTable.previous.finalY, sectionList.subsystem.typeRef.name + " : ");
                            }

                            pdf.setTextColor(7, 7, 7);
                            pdf.setFontStyle('normal');
                            pdf.autoTable.previous.finalY = pdf.autoTable.previous.finalY + 10;


                            pdf.autoTable(personDetail.columns, personDetail.data, {
                                startY: pdf.autoTable.previous.finalY,
                                margin: {horizontal: 25},
                                styles: {overflow: 'linebreak', overflowColumns: false},
                                //columnStyles: {text: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false}},
                                columnStyles: {
                                    0: {columnWidth: 280, overflow: 'linebreak', overflowColumns: false},
                                    1: {columnWidth: 150, overflow: 'linebreak', overflowColumns: false},
                                    2: {
                                        columnWidth: 60,
                                        overflow: 'linebreak',
                                        overflowColumns: false
                                    },
                                    3: {
                                        columnWidth: 150,
                                        overflow: 'linebreak',
                                        overflowColumns: false
                                    },
                                    4: {
                                        columnWidth: 80,
                                        overflow: 'linebreak',
                                        overflowColumns: false
                                    },
                                    5: {
                                        columnWidth: 80,
                                        overflow: 'linebreak',
                                        overflowColumns: false
                                    }
                                },
                            });

                            pdf.autoTable.previous.finalY = pdf.autoTable.previous.finalY + 30;

                            if ((pdf.autoTable.previous.finalY + 160) >= pageHeight) {
                                pdf.addPage();
                                pageWidth = pdf.internal.pageSize.width;
                                /*pageNumber = pageNumber + 1;
                                 pdf.text(pageWidth - 25, pageHeight - 30, "" + pageNumber);*/
                                pdf.autoTable.previous.finalY = 50;
                            }

                            pdf.setTextColor(7, 7, 7);
                            pdf.setFontStyle('bold');

                            pdf.text(385, pdf.autoTable.previous.finalY, "Signatures");

                            pdf.autoTable.previous.finalY = pdf.autoTable.previous.finalY + 20;

                            txt = "( " + sectionList.subsystem.typeRef.name + " Parts Chief of )";
                            fontSize = pdf.internal.getFontSize();

                            pageWidth = pdf.internal.pageSize.width;

                            txtWidth = pdf.getStringUnitWidth(txt) * fontSize / pdf.internal.scaleFactor;

                            xPosition = ( pageWidth - txtWidth ) / 2;

                            pdf.text(xPosition, pdf.autoTable.previous.finalY, "( " + sectionList.subsystem.typeRef.name + " Parts Chief of )");

                            pdf.autoTable.previous.finalY = pdf.autoTable.previous.finalY + 30;

                            pdf.setTextColor(7, 7, 7);
                            pdf.setFontStyle('normal');
                            pdf.text(200, pdf.autoTable.previous.finalY, "BDL");
                            pdf.text(400, pdf.autoTable.previous.finalY, "SSQAG");
                            pdf.text(600, pdf.autoTable.previous.finalY, "CAS");

                            pdf.autoTable.previous.finalY = pdf.autoTable.previous.finalY + 110;

                            //pdf.line(40, pdf.autoTable.previous.finalY, pageWidth - 20, pdf.autoTable.previous.finalY);
                        }
                    }


                });

                if ((pdf.autoTable.previous.finalY + 160) >= pageHeight) {
                    pdf.addPage();
                    pageWidth = pdf.internal.pageSize.width;
                    pageNumber = pageNumber + 1;
                    pdf.text(pageWidth - 25, pageHeight - 30, "" + pageNumber);
                    pdf.autoTable.previous.finalY = 50;
                }

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('bold');
                pdf.text(50, pdf.autoTable.previous.finalY, "Signature of Main Article Chief and other Associates :");
                pdf.autoTable.previous.finalY = pdf.autoTable.previous.finalY + 25;

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                if (type == "INSTANCE") {
                    var splitTitle = pdf.splitTextToSize("Once all the above approvals are done in the above document, it is clear that Missile assembled with all the necessary parts with required dimensions", 700);
                    pdf.text(50, pdf.autoTable.previous.finalY, splitTitle);
                } else {
                    var splitTitle = pdf.splitTextToSize("Once all the above approvals are done in the above document, it is clear that Section assembled with all the necessary parts with required dimensions", 700);
                    pdf.text(50, pdf.autoTable.previous.finalY, splitTitle);
                }

                pdf.autoTable.previous = false;
                //pdf.save(vm.bomInstanceTarbDocument.system.item.itemMaster.itemName + "_" + vm.bomInstanceTarbDocument.bomInstance.item.instanceName + "_" + todayStr + ".pdf");
                window.open(pdf.output('bloburl'), '_blank');
                closeSectionRequestReport();
                $rootScope.hideBusyIndicator();

                $rootScope.showSuccessMessage("Report generated successfully");

            }

            function showPartTracking(item) {
                $bom.bomObject = item;
                $state.go('app.bom.partTracking', {bomId: item.id});
            }

            function createFailureList(item, instance) {
                var options = {
                    title: 'Failure Report',
                    showMask: true,
                    side: 'left',
                    template: 'app/desktop/modules/bom/details/failureList/createFailureListView.jsp',
                    controller: "CreateFailureListController as failureListVm",
                    resolve: 'app/desktop/modules/bom/details/failureList/createFailureListController',
                    width: 700,
                    buttons: [
                        {text: 'Create', broadcast: 'create.failureList'}
                    ],
                    data: {
                        systemObject: $rootScope.selectedSystemDetails,
                        missileObject: $rootScope.selectedBomItemDetails,
                        selectedItem: item,
                        instance: instance
                    },
                    callback: function () {
                        instance.hasFailed = true;
                        if (!item.item.itemMaster.itemType.hasLots) {
                            ItemService.updateFilureItemInstance(instance).then(
                                function (data) {
                                    instance = data;
                                    showFailureList(item, instance);
                                }
                            );
                        } else if (item.item.itemMaster.itemType.hasLots) {
                            ItemService.updateFilureLotInstance(instance).then(
                                function (data) {
                                    instance = data;
                                    showFailureList(item, instance);
                                }
                            );
                        }
                        /*item.failBatchItemsIssued.push(item.acceptBatchItemsIssued[0]);
                         item.acceptBatchItemsIssued = [];
                         ItemBomService.updateBatchItemIssued(vm.item.id, item.failBatchItemsIssued[0]).then(
                         function (data) {
                         $rootScope.hideSidePanel('left');
                         showFailureList(item);
                         }
                         );
                         ItemBomService.getByItemAndParent(item.item.id, vm.item.id).then(
                         function (data1) {
                         ItemBomService.updateBomItemFailure(item.id, item).then(
                         function (data2) {

                         })
                         });*/
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function showFailureList(item, instance) {
                var options = {
                    title: 'Failure Report - ' + instance.upnNumber,
                    showMask: true,
                    side: 'left',
                    template: 'app/desktop/modules/bom/details/failureList/showFailureValueListView.jsp',
                    controller: "ShowFailureValueListController as failureValueListVm",
                    resolve: 'app/desktop/modules/bom/details/failureList/showFailureValueListController',
                    width: 700,
                    buttons: [
                        {text: 'Update', broadcast: 'update.failureList'},
                        {text: 'Print', broadcast: 'print.failureList'}
                    ],
                    data: {
                        item: item,
                        selectedItem: $rootScope.selectedBomItemDetails,
                        instance: instance
                    },
                    callback: function () {
                        ItemService.updateBomItem(item.id, item).then(
                            function (data) {
                                $rootScope.hideSidePanel('left');
                                $rootScope.showSuccessMessage("Failure report created successfully");
                            }
                        )
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function assignPartTracking(item) {
                $rootScope.checkListPdf = item;
                var partTrackings = [];
                var options = {
                    title: "Assign Part Tacking",
                    showMask: true,
                    side: 'left',
                    template: 'app/desktop/modules/bom/details/partTracking/partTrackingView.jsp',
                    controller: "SelectPartTrackingController as selectPartTrackVm",
                    resolve: 'app/desktop/modules/bom/details/partTracking/selectPartTrackingController',
                    width: 500,
                    buttons: [
                        {text: 'Add', broadcast: 'select.partTracking'}
                    ],
                    callback: function (data) {
                        var bomItem = item;
                        var partTracking = {
                            item: bomItem.id,
                            serialNo: null,
                            partTrackingStep: null,
                            status: null
                        };
                        angular.forEach(data.trackingSteps, function (step) {
                            partTracking = {
                                item: bomItem.id,
                                serialNo: step.serialNo,
                                partTrackingStep: step.id,
                                status: 'START'
                            };
                            partTrackings.push(partTracking);
                        });
                        var promise = null;
                        PartTrackingService.createPartTrackingItems(partTrackings).then(
                            function (data) {
                                item.hasPartTracking = true;
                                if (item.item != null) {
                                    promise = BomService.updateBomInstance(item.id, item);
                                } else if (item.typeRef != null) {
                                    promise = BomService.updateBomItemInstance(item.id, item);
                                }
                                promise.then(
                                    function (data) {

                                    }
                                )
                            });
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function bomTypeSelected(event, args) {
                lastSelectedItem = null;
                $rootScope.selectedSystemDetails = null;
                $rootScope.selectedBomItemDetails = null;
                vm.selectedBom = args.itemObject;
                if (vm.selectedBom == null) {
                    vm.showBomItems = false;
                }
                vm.selectAllCheck = false;
                vm.selectedSectionsToPrint = [];
                loadSections(args);
            }

            function loadSections(args) {
                var versity = false;
                if (vm.selectedBom != null && vm.selectedBom.objectType == "BOM") {
                    $rootScope.showBusyIndicator($('.view-container'));
                    $rootScope.selectedSystemDetails = vm.selectedBom.item;
                    vm.showBomItems = true;
                    versity = $rootScope.hasPermission('permission.admin.all');
                    var bomPermission = $rootScope.hasPermission('permission.bom.all');
                    if (!versity && !bomPermission) {
                        versity = $rootScope.hasPermission('permission.versity.group');
                    } else {
                        versity = false;
                    }
                    BomService.getItemBom(vm.selectedBom.item.id, versity).then(
                        function (data) {
                            vm.bomItems = data;
                            vm.bomSections = angular.copy(vm.bomItems);
                            angular.forEach(vm.bomItems, function (item) {
                                item.newQuantity = item.quantity;
                                item.newFractionalQuantity = item.fractionalQuantity;
                                item.newWorkCenter = item.workCenter;
                                item.editMode = false;
                                item.isNew = false;
                                item.expanded = false;
                                item.level = 1;
                                item.bomChildren = [];
                            });

                            angular.forEach(vm.bomSections, function (section) {
                                section.selected = false;
                            });
                            vm.loading = false;
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (vm.selectedBom != null && vm.selectedBom.objectType == "BOMINSTANCE") {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.showBomItems = true;
                    $rootScope.selectedSystemDetails = args.parentObject.item;
                    $rootScope.selectedBomItemDetails = vm.selectedBom.item;
                    versity = $rootScope.hasPermission('permission.admin.all');
                    var bomPermission = $rootScope.hasPermission('permission.bom.all');
                    if (!versity && !bomPermission) {
                        versity = $rootScope.hasPermission('permission.versity.group');
                    } else {
                        versity = false;
                    }
                    BomService.getInstanceBom(vm.selectedBom.id, versity).then(
                        function (data) {
                            vm.bomItems = data;
                            angular.forEach(vm.bomItems, function (item) {
                                item.editMode = false;
                                item.isNew = false;
                                item.expanded = false;
                                item.level = 1;
                                item.bomChildren = [];
                            });
                            vm.loading = false;
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function toggleNode(bomItem) {
                if (vm.searchMode == false) {
                    if (bomItem.expanded == null || bomItem.expanded == undefined) {
                        bomItem.expanded = false;
                    }
                    bomItem.expanded = !bomItem.expanded;
                    var index = vm.bomItems.indexOf(bomItem);
                    if (bomItem.objectType == 'BOMITEM') {
                        if (bomItem.expanded == false) {
                            removeChildren(bomItem);
                        }
                        else {
                            BomService.getBomItemChildren(bomItem.id).then(
                                function (data) {

                                    angular.forEach(data, function (item) {
                                        item.isNew = false;
                                        item.editMode = false;
                                        item.expanded = false;
                                        item.level = bomItem.level + 1;
                                        item.bomChildren = [];
                                        bomItem.bomChildren.push(item);
                                    });

                                    angular.forEach(bomItem.bomChildren, function (item) {
                                        index = index + 1;
                                        vm.bomItems.splice(index, 0, item);
                                    });

                                    /*------ To  Expand Entire Section --------------*/
                                    /*if (bomItem.bomItemType == "SECTION") {
                                     angular.forEach(data, function (item) {
                                     if (bomItem.bomChildren == undefined) {
                                     bomItem.bomChildren = [];
                                     }
                                     item.expanded = true;
                                     item.level = bomItem.level + 1;
                                     item.isNew = false;
                                     item.editMode = false;
                                     item.bomChildren = [];
                                     bomItem.bomChildren.push(item);
                                     });

                                     angular.forEach(bomItem.bomChildren, function (item) {
                                     item.bomChildren = [];
                                     index = index + 1;
                                     vm.bomItems.splice(index, 0, item);
                                     index = populateChildren(item, index)
                                     });
                                     } else {

                                     angular.forEach(data, function (item) {
                                     item.isNew = false;
                                     item.editMode = false;
                                     item.expanded = false;
                                     item.level = bomItem.level + 1;
                                     item.bomChildren = [];
                                     bomItem.bomChildren.push(item);
                                     });

                                     angular.forEach(bomItem.bomChildren, function (item) {
                                     index = index + 1;
                                     vm.bomItems.splice(index, 0, item);
                                     });
                                     }*/
                                }
                            )
                        }
                    } else if (bomItem.objectType == 'BOMINSTANCEITEM') {
                        if (bomItem.expanded == false) {
                            removeChildren(bomItem);
                        }
                        else {
                            BomService.getInstanceToggle(bomItem.id).then(
                                function (data) {
                                    angular.forEach(data, function (item) {
                                        item.isNew = false;
                                        item.editMode = false;
                                        item.expanded = false;
                                        item.level = bomItem.level + 1;
                                        item.bomChildren = [];
                                        bomItem.bomChildren.push(item);
                                    });

                                    angular.forEach(bomItem.bomChildren, function (item) {
                                        index = index + 1;
                                        vm.bomItems.splice(index, 0, item);
                                    });
                                    /*if (bomItem.bomItemType == "SECTION") {
                                     angular.forEach(data, function (item) {
                                     if (bomItem.bomChildren == undefined) {
                                     bomItem.bomChildren = [];
                                     }
                                     item.expanded = true;
                                     item.level = bomItem.level + 1;
                                     item.isNew = false;
                                     item.editMode = false;
                                     item.bomChildren = [];
                                     bomItem.bomChildren.push(item);
                                     });

                                     angular.forEach(bomItem.bomChildren, function (item) {
                                     item.bomChildren = [];
                                     index = index + 1;
                                     vm.bomItems.splice(index, 0, item);
                                     index = populateChildren(item, index)
                                     });
                                     } else {
                                     angular.forEach(data, function (item) {
                                     item.isNew = false;
                                     item.editMode = false;
                                     item.expanded = false;
                                     item.level = bomItem.level + 1;
                                     item.bomChildren = [];
                                     bomItem.bomChildren.push(item);
                                     });

                                     angular.forEach(bomItem.bomChildren, function (item) {
                                     index = index + 1;
                                     vm.bomItems.splice(index, 0, item);
                                     });
                                     }*/
                                }
                            )
                        }
                    }
                }

            }

            function removeChildren(bomItem) {
                if (bomItem != null && bomItem.bomChildren != null && bomItem.bomChildren != undefined) {
                    angular.forEach(bomItem.bomChildren, function (item) {
                        removeChildren(item);
                    });

                    var index = vm.bomItems.indexOf(bomItem);
                    vm.bomItems.splice(index + 1, bomItem.bomChildren.length);
                    bomItem.bomChildren = [];
                    bomItem.expanded = false;
                }
            }

            function populateChildren(bomItem, lastIndex) {
                angular.forEach(bomItem.children, function (item) {
                    lastIndex++;
                    item.bomChildren = [];
                    item.expanded = true;
                    item.level = bomItem.level + 1;
                    item.isNew = false;
                    item.editMode = false;
                    //var index = vm.bomItems.indexOf(bomItem);
                    vm.bomItems.splice(lastIndex, 0, item);
                    bomItem.bomChildren.push(item);
                    lastIndex = populateChildren(item, lastIndex)
                });

                return lastIndex;
            }

            function selectItem(item) {
                if (lastSelectedItem != null && lastSelectedItem != item) {
                    lastSelectedItem.selected = false;
                    lastSelectedItem = null;
                }
                item.selected = !item.selected;
                if (item.selected == true) {
                    lastSelectedItem = item;
                    $rootScope.selectedBomItemDetails = lastSelectedItem.item;
                    if (item.bomChildren == 0) {
                        vm.bomItems.itemSelect = [];
                    }
                }
                else {
                    item.selected = false;
                    lastSelectedItem = null;
                    $rootScope.selectedBomItemDetails = $rootScope.selectedSystemDetails;
                }
            }


            function addNewBomItem(bomItem) {
                lastSelectedItem = bomItem;
                $rootScope.newBomItemButton = false;
                $rootScope.addedSearchItems = [];
                var button = {text: "Close", broadcast: 'app.bomItem.new'};
                var options = {
                    title: "Add Part to : " + bomItem.typeRef.name,
                    template: 'app/desktop/modules/bom/details/newBomView.jsp',
                    controller: 'NewBomController as newBomVm',
                    resolve: 'app/desktop/modules/bom/details/newBomController',
                    width: 600,
                    showMask: true,
                    data: {
                        selectedBomItem: bomItem,
                        buttonTitle: button
                    },
                    buttons: [
                        button
                    ],
                    callback: function (item) {
                        if (item != null && item != undefined) {
                            item.editMode = false;
                            item.isNew = false;
                            var index = vm.bomItems.indexOf(bomItem);
                            if (bomItem.children == undefined) {
                                bomItem.children = [];
                            }
                            if (bomItem.children.length > 0 && !bomItem.expanded) {
                                toggleNode(bomItem);
                            } else {
                                bomItem.expanded = true;
                                if (bomItem.bomChildren == undefined) {
                                    bomItem.bomChildren = [];
                                }
                                if (bomItem.children == undefined) {
                                    bomItem.children = [];
                                }
                                bomItem.children.push(item);
                                item.level = bomItem.level + 1;
                                index = index + getIndexTopInsertNewChild(bomItem) + 1;
                                bomItem.bomChildren.push(item);

                                vm.bomItems.splice(index, 0, item);
                            }
                        } else {
                            $rootScope.newBomItemButton = false;
                        }
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function addBomItem(bom) {
                lastSelectedItem = bom;
                $rootScope.newBomItemButton = false;
                $rootScope.addedSearchItems = [];
                var button = {text: "Close", broadcast: 'app.bomItem.new'};
                var options = {
                    title: "Add Item to : " + bom.item.itemMaster.itemName,
                    template: 'app/desktop/modules/bom/details/newBomView.jsp',
                    controller: 'NewBomController as newBomVm',
                    resolve: 'app/desktop/modules/bom/details/newBomController',
                    width: 600,
                    showMask: true,
                    data: {
                        selectedBomItem: bom.item,
                        buttonTitle: button
                    },
                    buttons: [
                        button
                    ],
                    callback: function (item) {
                        if (item != null && item != undefined) {
                            item.editMode = false;
                            item.isNew = false;
                            item.level = 1;
                            item.bomChildren = [];
                            vm.bomItems.push(item);
                        } else {
                            $rootScope.newBomItemButton = false;
                        }
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getIndexTopInsertNewChild(bomItem) {
                var index = 0;

                if (bomItem.bomChildren != undefined && bomItem.bomChildren != null) {
                    index = bomItem.bomChildren.length;
                    angular.forEach(bomItem.bomChildren, function (child) {
                        var childCount = getIndexTopInsertNewChild(child);
                        index = index + childCount;
                    })
                }

                return index;
            }


            function addSearchItemToBom(item) {
                var newBomItem = angular.copy(vm.emptyBomItem);
                newBomItem.parent = lastSelectedItem.id;
                if (lastSelectedItem.bomItemType == 'SECTION' || lastSelectedItem.bomItemType == 'COMMONPART') {
                    newBomItem.hierarchicalCode = lastSelectedItem.hierarchicalCode + "" + lastSelectedItem.typeRef.code + "" + "000" + "" + item.itemCode;
                } else if (lastSelectedItem.bomItemType == 'SUBSYSTEM') {
                    newBomItem.hierarchicalCode = lastSelectedItem.hierarchicalCode + "" + lastSelectedItem.typeRef.code + "" + "00" + "" + item.itemCode;
                } else {
                    newBomItem.hierarchicalCode = lastSelectedItem.hierarchicalCode + "" + lastSelectedItem.typeRef.code + "" + item.itemCode;
                }

                newBomItem.item = item.latestRevisionObject;
                newBomItem.item.itemMaster.parentType = item.parentType;

                if (item.itemType.hasSpec) {
                    newBomItem.hierarchicalCode = newBomItem.hierarchicalCode + "" + item.partSpec.specName;
                }

                newBomItem.editMode = true;
                newBomItem.isNew = true;
                if (lastSelectedItem.objectType == "BOM") {
                    newBomItem.level = 0;
                    newBomItem.bomChildren = [];
                    vm.bomItems.push(newBomItem);
                } else {
                    newBomItem.bomItemType = "PART";
                    lastSelectedItem.expanded = true;
                    var index = vm.bomItems.indexOf(lastSelectedItem);
                    if (lastSelectedItem.bomChildren == undefined) {
                        lastSelectedItem.bomChildren = [];
                    }

                    if (lastSelectedItem.children == undefined) {
                        lastSelectedItem.children = [];
                    }
                    lastSelectedItem.children.push(newBomItem);
                    newBomItem.level = lastSelectedItem.level + 1;
                    newBomItem.bomChildren = [];
                    index = index + getIndexTopInsertNewChild(lastSelectedItem) + 1;
                    lastSelectedItem.bomChildren.push(newBomItem);

                    vm.bomItems.splice(index, 0, newBomItem);
                }
            }

            function editItem(item) {
                item.editMode = true;
                item.newQuantity = item.quantity;
                item.newWorkCenter = item.workCenter;
            }

            function saveItem(item) {
                if (item.id == null || item.id == undefined) {
                    if (validateItem(item)) {
                        if (item.item != null && item.item.itemMaster.itemType.parentNodeItemType != "Part") {
                            item.quantity = null;
                            item.fractionalQuantity = null;
                        } else {
                            item.quantity = item.newQuantity;
                            item.fractionalQuantity = item.newFractionalQuantity;
                            item.workCenter = item.newWorkCenter;
                        }
                        BomService.createBomItem(vm.selectedBom.item.id, item).then(
                            function (data) {
                                item.id = data.id;
                                item.editMode = false;
                                item.isNew = false;
                                item.objectType = data.objectType;
                                item.parentType = data.parentType;
                                $rootScope.showSuccessMessage("Bom Item created successfully");
                            }
                        )
                    }
                } else {
                    if (validateItem(item)) {
                        item.quantity = item.newQuantity;
                        item.fractionalQuantity = item.newFractionalQuantity;
                        item.workCenter = item.newWorkCenter;
                        BomService.updateBomItem(item).then(
                            function (data) {
                                item.quantity = data.quantity;
                                item.editMode = false;
                                item.isNew = false;
                                $rootScope.showSuccessMessage("Bom Item updated successfully");
                            }
                        )
                    }
                }
            }

            function validateItem(item) {
                var valid = true;
                if (item.item != null && (item.item.itemMaster.itemType.parentNodeItemType == "Part" || !item.item.hasBom)) {
                    if (!item.item.itemMaster.itemType.hasLots && (item.newQuantity == null || item.newQuantity == "" || item.newQuantity == undefined)) {
                        valid = false;
                        $rootScope.showWarningMessage("Please enter Quantity");
                    } else if (item.item.itemMaster.itemType.hasLots && (item.newFractionalQuantity == null || item.newFractionalQuantity == "" || item.newFractionalQuantity == undefined)) {
                        valid = false;
                        $rootScope.showWarningMessage("Please enter Quantity");
                    } else if (item.item.itemMaster.itemType.hasLots && item.newFractionalQuantity < 0) {
                        valid = false;
                        $rootScope.showWarningMessage("Please enter positive Quantity");
                    } else if (!item.item.itemMaster.itemType.hasLots && item.newQuantity < 0) {
                        valid = false;
                        $rootScope.showWarningMessage("Please enter positive Quantity");
                    } else if (item.newWorkCenter == null || item.newWorkCenter == "" || item.newWorkCenter == undefined) {
                        valid = false;
                        $rootScope.showWarningMessage("Please select work center");
                    }
                }

                return valid;
            }

            function cancelChanges(item) {
                item.editMode = false;
                item.newQuantity = item.quantity;
                item.newFractionalQuantity = item.fractionalQuantity;
                item.workCenter = item.newWorkCenter;
            }

            function removeItem(item) {
                var index = vm.bomItems.indexOf(item);
                if (item.bomItemType == "PART") {
                    var index1 = $rootScope.addedSearchItems.indexOf(item);
                    $rootScope.addedSearchItems.splice(index1, item);
                    vm.bomItems.splice(index, 1);
                } else {
                    var index2 = $rootScope.addedSectionTypes.indexOf(item);
                    $rootScope.addedSectionTypes.splice(index2, item);
                    vm.bomItems.splice(index, 1);
                }
            }

            function deleteItem(item) {

                var parentData = null;
                angular.forEach(vm.bomItems, function (parent) {
                    if (parent.id == item.parent) {
                        parentData = parent;
                    }
                });

                var options = {
                    title: "Remove Item",
                    message: "Please confirm to Remove this Item",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-content'));
                        BomService.deleteBomItem(item.id).then(
                            function (data) {
                                var index = vm.bomItems.indexOf(item);
                                vm.bomItems.splice(index, 1);

                                var itemIndex = parentData.bomChildren.indexOf(item);

                                parentData.bomChildren.splice(itemIndex, 1);
                                $rootScope.showWarningMessage("Bom Item removed successfully");
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            function createBomGroupType(item, type) {
                $rootScope.addedSectionTypes = [];

                lastSelectedItem = item;
                if (!item.expanded) {
                    toggleNode(item);
                }
                var title = null;
                if (type == "SECTION") {
                    title = "Add Section to : " + vm.selectedBom.item.itemMaster.itemName;
                } else if (type == "SUBSYSTEM") {
                    title = "Add Sub System to : " + item.typeRef.name;
                } else if (type == "UNIT") {
                    title = "Add Unit to :" + item.typeRef.name;
                }

                var options = {
                    title: title,
                    template: 'app/desktop/modules/bom/details/add/addBomGroupTypeView.jsp',
                    controller: 'AddBomGroupTypeController as addBomGroupTypeVm',
                    resolve: 'app/desktop/modules/bom/details/add/addBomGroupTypeController',
                    width: 600,
                    showMask: true,
                    data: {
                        selectedBomItem: item,
                        selectedBomItemType: type
                    },
                    buttons: [
                        {text: "Close", broadcast: 'app.type.bomItem.new'}
                    ],
                    callback: function (bomItem) {
                        if (bomItem != null && bomItem != undefined) {

                            item.editMode = false;
                            item.isNew = false;
                            var index = vm.bomItems.indexOf(item);
                            if (!item.expanded) {
                                toggleNode(item);
                            } else {
                                item.expanded = true;
                                if (item.bomChildren == undefined) {
                                    item.bomChildren = [];
                                }
                                item.level = item.level + 1;
                                index = index + getIndexTopInsertNewChild(bomItem) + 1;
                                item.bomChildren.push(item);

                                vm.bomItems.splice(index, 0, item);
                            }
                        }
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function addBomGroupTypeToBom(type) {
                var newBomItem = angular.copy(vm.emptyBomItem);
                if (type.type == "SECTION") {
                    newBomItem.typeRef = type;
                    newBomItem.bom = vm.selectedBom.id;
                    newBomItem.hierarchicalCode = vm.selectedBom.item.itemMaster.itemCode;
                    newBomItem.bomItemType = 'SECTION';
                } else {
                    newBomItem.typeRef = type;
                    newBomItem.parent = lastSelectedItem.id;
                    if (lastSelectedItem.bomItemType == "SECTION" && type.type == "UNIT") {
                        newBomItem.hierarchicalCode = lastSelectedItem.hierarchicalCode + "" + lastSelectedItem.typeRef.code + "0";
                    } else {
                        newBomItem.hierarchicalCode = lastSelectedItem.hierarchicalCode + "" + lastSelectedItem.typeRef.code;
                    }

                    newBomItem.bomItemType = type.type;
                }


                if (newBomItem.id == null || newBomItem.id == undefined) {
                    BomService.createBomItem(lastSelectedItem.id, newBomItem).then(
                        function (data) {
                            newBomItem.id = data.id;
                            newBomItem.editMode = false;
                            newBomItem.isNew = false;
                            newBomItem.objectType = data.objectType;
                            $rootScope.showSuccessMessage("Bom Type added successfully");

                            if (lastSelectedItem.objectType == "BOM") {
                                newBomItem.level = 1;
                                newBomItem.bomChildren = [];
                                vm.bomItems.push(newBomItem);
                            } else {
                                lastSelectedItem.expanded = true;
                                var index = vm.bomItems.indexOf(lastSelectedItem);
                                if (lastSelectedItem.bomChildren == undefined) {
                                    lastSelectedItem.bomChildren = [];
                                }

                                if (lastSelectedItem.children == undefined) {
                                    lastSelectedItem.children = [];
                                }
                                lastSelectedItem.children.push(newBomItem);
                                lastSelectedItem.expanded = true;
                                newBomItem.level = lastSelectedItem.level + 1;
                                newBomItem.bomChildren = [];
                                index = index + getIndexTopInsertNewChild(lastSelectedItem) + 1;
                                lastSelectedItem.bomChildren.push(newBomItem);

                                vm.bomItems.splice(index, 0, newBomItem);
                            }
                            $rootScope.loadSectionBomGroups();
                        }
                    )
                }
            }

            vm.issuedInstancesPopOver = {
                templateUrl: 'app/desktop/modules/bom/details/issuedInstancesPopover.jsp'
            };

            function printSelectedType(item) {
                $rootScope.showBusyIndicator();
                BomService.getSelectedItemStructure(item.id).then(
                    function (data) {
                        vm.printItems = data;
                        vm.showPrintData = true;
                        $timeout(function () {
                            printData(item, 'TYPE');
                        }, 2000);
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.selectedSectionsToPrint = [];
            function printSelectedSections() {
                if (vm.selectedSectionsToPrint.length > 0) {
                    $rootScope.showBusyIndicator();
                    BomService.getSelectedSectionsStructure(vm.selectedSectionsToPrint).then(
                        function (data) {
                            vm.printItems = data;
                            vm.showPrintData = true;
                            $timeout(function () {
                                printData(vm.selectedBom, 'SECTIONS');
                            }, 2000);
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    $rootScope.showErrorMessage("Please select at least one section");
                }
            }

            vm.selectAllCheck = false;
            vm.selectAllSection = selectAllSection;
            vm.selectSection = selectSection;

            function selectAllSection(check) {
                vm.selectedSectionsToPrint = [];
                if (check) {
                    vm.selectAllCheck = true;
                    angular.forEach(vm.bomSections, function (section) {
                        section.selected = true;
                        vm.selectedSectionsToPrint.push(section.id);
                    })
                } else {
                    vm.selectAllCheck = false;
                    angular.forEach(vm.bomSections, function (section) {
                        section.selected = false;
                    })
                }
            }

            function selectSection(selectedSection) {
                var flag = false;
                angular.forEach(vm.selectedSectionsToPrint, function (section) {
                    if (selectedSection.id == section) {
                        flag = true;
                        section.selected = false;
                        vm.selectAllCheck = false;
                        vm.selectedSectionsToPrint.splice(vm.selectedSectionsToPrint.indexOf(selectedSection.id), 1);
                    }
                });

                if (!flag) {
                    selectedSection.selected = true;
                    vm.selectedSectionsToPrint.push(selectedSection.id);
                }

                if (vm.bomSections.length == vm.selectedSectionsToPrint.length) {
                    vm.selectAllCheck = true;
                } else {
                    vm.selectAllCheck = false;
                }

            }

            function printData(item, type) {
                $("#printSelectedType").show();

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
                pdf.text(50, 170, "BOM :" + $rootScope.selectedSystemDetails.itemMaster.itemName);
                if (type == "TYPE") {
                    pdf.text(50, 185, "Section :" + item.typeRef.name);
                }
                pdf.text(675, 170, "Date : " + todayStr);
                var personDetail = pdf.autoTableHtmlToJson(document.getElementById("printSelectedType"));

                pdf.autoTable(personDetail.columns, personDetail.data, {
                    startY: 200,
                    margin: {horizontal: 25},
                    styles: {overflow: 'linebreak', overflowColumns: false},
                    //columnStyles: {text: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false}},
                    columnStyles: {
                        0: {columnWidth: 280, overflow: 'linebreak', overflowColumns: false},
                        1: {columnWidth: 150, overflow: 'linebreak', overflowColumns: false},
                        2: {
                            columnWidth: 60,
                            overflow: 'linebreak',
                            overflowColumns: false,
                            valign: 'middle',
                            halign: 'center'
                        },
                        3: {
                            columnWidth: 70,
                            overflow: 'linebreak',
                            overflowColumns: false
                        },
                        4: {
                            columnWidth: 80,
                            overflow: 'linebreak',
                            overflowColumns: false
                        },
                        5: {
                            columnWidth: 150,
                            overflow: 'linebreak',
                            overflowColumns: false
                        }
                    },
                    drawCell: function (cell, data) {
                        var rowData = vm.printItems[data.row.index];
                        if (rowData.bomItemType != "PART") {
                            pdf.setFontStyle('bold');
                            pdf.setFontSize(11);
                        } else {
                            pdf.setFontStyle('normal');
                            pdf.setFontSize(12);
                        }
                        if (cell.raw.firstElementChild != null && cell.raw.firstElementChild.className == "level1") {
                            cell.text[0] = "        " + cell.text[0];
                        } else if (cell.raw.firstElementChild != null && cell.raw.firstElementChild.className == "level2") {
                            cell.text[0] = "            " + cell.text[0];
                        } else if (cell.raw.firstElementChild != null && cell.raw.firstElementChild.className == "level3") {
                            cell.text[0] = "                " + cell.text[0];
                        } else if (cell.raw.firstElementChild != null && cell.raw.firstElementChild.className == "level4") {
                            cell.text[0] = "                    " + cell.text[0];
                        }
                    }
                });

                if (type == "SECTIONS") {
                    pdf.autoTable.previous = false;
                    //pdf.save($rootScope.selectedSystemDetails.itemMaster.itemName + "_" + todayStr + ".pdf");

                    window.open(pdf.output('bloburl'), '_blank');
                    var modal = document.getElementById("sectionsMenu");
                    modal.style.display = "none";

                    angular.forEach(vm.bomSections, function (section) {
                        section.selected = false;
                    })
                    vm.selectAllCheck = false;
                    vm.selectedSectionsToPrint = [];
                } else {
                    pdf.autoTable.previous = false;
                    //pdf.save($rootScope.selectedSystemDetails.itemMaster.itemName + "_" + item.typeRef.name + "_" + todayStr + ".pdf");
                    window.open(pdf.output('bloburl'), '_blank');
                }

                $rootScope.hideBusyIndicator();
                $("#printSelectedType").hide();
                $rootScope.showSuccessMessage("Report generated successfully");

            }

            function showSectionsMenu() {
                var modal = document.getElementById("sectionsMenu");
                modal.style.display = "block";
            }

            function closeSectionsMenu() {
                var modal = document.getElementById("sectionsMenu");
                modal.style.display = "none";

                vm.selectAllCheck = false;
                vm.selectedSectionsToPrint = [];

                angular.forEach(vm.bomSections, function (section) {
                    section.selected = false;
                })
            }


            function showWorkCenterItems() {
                var modal = document.getElementById("workCentersMenu");
                modal.style.display = "block";
            }

            function closeWorkCentersMenu() {
                var modal = document.getElementById("workCentersMenu");
                modal.style.display = "none";
                vm.selectedWorkCenter = null;
                vm.workCenterItems = [];
            }

            vm.showNewSidePanel = showNewSidePanel;
            vm.closeNewSidePanel = closeNewSidePanel;
            function showNewSidePanel() {
                var modal = document.getElementById("newRightSidePanel");
                var height = $('.contentpanel').outerHeight();
                $('.right-side').height(height);
                $('.right-side').width(600);
                $('.right-side').css({'margin-right': "-" + 600 + "px"});
                $('.right-side').animate({"margin-right": '+=' + 600}, 'slow', function () {

                });

                modal.style.display = "block";
            }

            function closeNewSidePanel() {
                var modal = document.getElementById("newRightSidePanel");
                modal.style.display = "none";
                vm.selectedWorkCenter = null;
                vm.workCenterItems = [];
            }

            function showSectionRequestReport() {
                var modal = document.getElementById("sectionReport-view");
                modal.style.display = "block";
            }

            function closeSectionRequestReport() {
                var modal = document.getElementById("sectionReport-view");
                modal.style.display = "none";
                vm.workCenterCriteria.searchQuery = null;
                vm.workCenterCriteria.bom = '';
            }

            vm.workCenterCriteria = {
                searchQuery: null,
                bom: '',
                versity: false
            };

            function onSelectWorkCenter(workCenter) {
                vm.workCenterItems = [];
                $rootScope.showBusyIndicator($(".workCenter-content"))
                vm.workCenterCriteria.bom = vm.selectedBom.id;
                vm.workCenterCriteria.searchQuery = workCenter;
                pageable.size = 10000;
                var versity = $rootScope.hasPermission('permission.admin.all');
                var bomPermission = $rootScope.hasPermission('permission.bom.all');
                if (!versity && !bomPermission) {
                    versity = $rootScope.hasPermission('permission.versity.group');
                } else {
                    versity = false;
                }
                vm.workCenterCriteria.versity = versity;
                BomService.getWorkCenterItemsByBom(pageable, vm.workCenterCriteria).then(
                    function (data) {
                        vm.workCenterItemsDto = data;

                        angular.forEach(vm.workCenterItemsDto, function (type) {
                            type.section.level = 0;
                            type.section.type = "SECTION";
                            type.section.children = [];
                            type.section.expanded = false;
                            vm.workCenterItems.push(type.section);

                            angular.forEach(type.bomItems, function (bomItem) {
                                bomItem.level = type.section.level + 1;
                                type.section.children.push(bomItem);
                                //vm.requestItems.push(requestItem);
                            })
                        });

                        $rootScope.hideBusyIndicator();
                        vm.loading = false;
                    }
                )
            }

            vm.toggleSection = toggleSection;

            function toggleSection(section) {
                section.expanded = !section.expanded;
                var index = vm.workCenterItems.indexOf(section);
                if (section.expanded) {
                    angular.forEach(section.children, function (requestItem) {
                        index = index + 1;
                        vm.workCenterItems.splice(index, 0, requestItem);
                    })
                } else {
                    vm.workCenterItems.splice(index + 1, section.children.length);
                }
            }

            function getSectionRequestReport(bomItem) {
                BomService.getRequestReportBySection($rootScope.selectedMissile.id, bomItem.id).then(
                    function (data) {
                        vm.requestReportItems = data;
                        vm.selectedSection = bomItem;
                        showSectionRequestReport();
                        vm.selectedPrintRequestType = "SECTION";
                    }
                )
            }

            function printSectionRequestReport() {

                $rootScope.showBusyIndicator();
                var pdf = new jsPDF('l', 'pt', 'a4');

                pdf.setFont('arial narrow');
                pdf.setFontSize(13);
                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                var imgData = $rootScope.drdoImage;
                pdf.addImage(imgData, 'JPG', 350, 25, 125, 125);

                var pageHeight = pdf.internal.pageSize.height;
                var pageWidth = pdf.internal.pageSize.width;

                var today = moment(new Date());
                var todayStr = today.format('DD/MM/YYYY');

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('bold');
                if (vm.selectedPrintRequestType == "SECTION") {
                    pdf.text(350, 160, $rootScope.selectedMissile.item.instanceName + "_" + vm.selectedSection.typeRef.name + " Request Report");
                } else {
                    pdf.text(350, 160, $rootScope.selectedMissile.item.instanceName + " Request Report");
                }

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                pdf.text(50, 170, "BOM :" + $rootScope.selectedSystemDetails.itemMaster.itemName);
                pdf.text(675, 170, "Date : " + todayStr);
                var personDetail = pdf.autoTableHtmlToJson(document.getElementById("printSectionRequestReport"));

                pdf.autoTable(personDetail.columns, personDetail.data, {
                    startY: 190,
                    margin: {horizontal: 25},
                    headerStyles: {
                        halign: 'center', valign: 'middle'
                    },
                    styles: {overflow: 'linebreak', overflowColumns: false},
                    columnStyles: {
                        0: {columnWidth: 300, overflow: 'linebreak', overflowColumns: false},
                        1: {columnWidth: 180, overflow: 'linebreak', overflowColumns: false},
                        2: {
                            columnWidth: 100,
                            overflow: 'linebreak',
                            overflowColumns: false,
                            valign: 'middle',
                            halign: 'center'
                        },
                        3: {
                            columnWidth: 100,
                            overflow: 'linebreak',
                            overflowColumns: false,
                            valign: 'middle',
                            halign: 'center'
                        },
                        4: {
                            columnWidth: 100,
                            overflow: 'linebreak',
                            overflowColumns: false,
                            valign: 'middle',
                            halign: 'center'
                        }
                    },
                    //columnStyles: {text: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false}},
                    drawCell: function (cell, data) {
                        var rowData = vm.requestReportItems[data.row.index];
                        if (rowData.item.bomItemType != "PART") {
                            pdf.setFontStyle('bold');
                            pdf.setFontSize(11);
                        } else {
                            pdf.setFontStyle('normal');
                            pdf.setFontSize(12);
                        }
                        if (cell.raw.firstElementChild != null && cell.raw.firstElementChild.className == "level1") {
                            cell.text[0] = "        " + cell.text[0];
                        } else if (cell.raw.firstElementChild != null && cell.raw.firstElementChild.className == "level2") {
                            cell.text[0] = "            " + cell.text[0];
                        } else if (cell.raw.firstElementChild != null && cell.raw.firstElementChild.className == "level3") {
                            cell.text[0] = "                " + cell.text[0];
                        } else if (cell.raw.firstElementChild != null && cell.raw.firstElementChild.className == "level4") {
                            cell.text[0] = "                    " + cell.text[0];
                        }
                    }
                });

                /*var blobPDF =  new Blob([ pdf.output() ], { type : 'application/pdf'});
                 var blobUrl = URL.createObjectURL(blobPDF);

                 window.open(blobUrl);*/

                if (vm.selectedPrintRequestType == "SECTION") {
                    pdf.autoTable.previous = false;
                    //pdf.save($rootScope.selectedSystemDetails.itemMaster.itemName + "_" + $rootScope.selectedMissile.item.instanceName + "_" + vm.selectedSection.typeRef.name + "_" + todayStr + ".pdf");
                    window.open(pdf.output('bloburl'), '_blank');
                } else {
                    pdf.autoTable.previous = false;
                    //pdf.save($rootScope.selectedSystemDetails.itemMaster.itemName + "_" + $rootScope.selectedMissile.item.instanceName + "_" + todayStr + ".pdf");
                    window.open(pdf.output('bloburl'), '_blank');
                }
                closeSectionRequestReport();
                $rootScope.hideBusyIndicator();

                $rootScope.showSuccessMessage("Report generated successfully");

            }

            function getInstanceRequestReport() {
                vm.requestReportItems = [];
                BomService.getRequestReportByInstance(vm.selectedBom.id).then(
                    function (data) {
                        vm.requestReportItems = data;
                        showSectionRequestReport();
                        vm.selectedPrintRequestType = "MBOM";
                    }
                )
            }

            function loadWorkStations() {
                CommonService.getLovByName("Work Centers").then(
                    function (data) {
                        if (data.values != null || data.values != undefined)
                            vm.workCenters = data.values;
                    });
            }

            vm.synchronizeSelectedSection = synchronizeSelectedSection;
            function synchronizeSelectedSection(item) {
                $rootScope.showBusyIndicator($('.view-content'));
                BomService.synchronizeBomSection(vm.selectedBom.id, item.id).then(
                    function (data) {
                        $rootScope.showSuccessMessage(item.typeRef.name + " : Section updated successfully");
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.synchronizeSelectedUnit = synchronizeSelectedUnit;
            function synchronizeSelectedUnit(item) {
                $rootScope.showBusyIndicator($('.view-content'));
                BomService.synchronizeBomUnit(vm.selectedBom.id, item.id).then(
                    function (data) {
                        $rootScope.showSuccessMessage(item.typeRef.name + " : Unit updated successfully");
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.searchBomText = null;
            $rootScope.searchBom = searchBom;
            $rootScope.resetBomSearch = resetBomSearch;
            vm.searchMode = false;
            function searchBom(freeText) {
                var versity = $rootScope.hasPermission('permission.admin.all');
                var bomPermission = $rootScope.hasPermission('permission.bom.all');
                if (!versity && !bomPermission) {
                    versity = $rootScope.hasPermission('permission.versity.group');
                } else {
                    versity = false;
                }
                if (freeText != null && freeText != "" && freeText != undefined && freeText.length >= 2) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    $rootScope.searchBomText = freeText;
                    BomService.searchBom(vm.selectedBom.id, freeText, versity).then(
                        function (data) {
                            vm.searchMode = true;
                            vm.bomItems = data;
                            angular.forEach(vm.bomItems, function (bomItem) {
                                bomItem.editMode = false;
                                bomItem.isNew = false;
                            })
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    if (freeText.length == 0) {
                        resetBomSearch();
                    }
                }
            }

            function resetBomSearch() {
                $rootScope.searchBomText = null;
                vm.searchMode = false;
                loadSections();
            }

            (function () {
                loadWorkStations();
                $scope.$on('app.bom.selected', bomTypeSelected);
            })();
        }
    }
)
;