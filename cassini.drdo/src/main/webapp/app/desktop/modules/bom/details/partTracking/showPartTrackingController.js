/**
 * Created by Nageshreddy on 04-01-2018.
 */
define([
        'app/desktop/modules/bom/bom.module',
        'jspdf',
        'jspdf-autotable',
        'app/shared/services/drdoApplication',
        'app/shared/services/core/partTrackingService',
        'app/shared/services/core/listService',
        'app/shared/services/core/bomService',
        'app/desktop/modules/bom/details/partTracking/scanPartTrackingController',
        'app/desktop/modules/bom/details/partTracking/showScanPartTrackingController',
        'app/desktop/modules/bom/details/partTracking/showReScanPartTrackingController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService'
    ],
    function (module, jsPDF) {
        module.controller('ShowPartTrackingController', ShowPartTrackingController);

        function ShowPartTrackingController($scope, $q, $timeout, $bom, $rootScope, $stateParams, $window, PartTrackingService,
                                            ListService, BomService, CommonService, AttachmentService) {
            if ($application.homeLoaded == false) {
                return;
            }
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-list";
            $rootScope.viewInfo.title = "Part Tracking";

            $rootScope.checkWithSystemWithMissile = false;
            $rootScope.checkWithSystem = false;
            $rootScope.checkReqSystemWithMissile = false;
            $rootScope.checkStorage = false;

            vm.saveCheckList = saveCheckList;
            vm.bomItemId = $stateParams.bomId;
            vm.back = back;
            vm.check = check;
            vm.editPartTracking = editPartTracking;
            vm.updatePartTracking = updatePartTracking;
            vm.openAttachment = openAttachment;
            vm.generateReport = generateReport;
            vm.scanItems = scanItems;
            vm.showScanItems = showScanItems;
            vm.showReScanItems = showReScanItems;
            vm.printPartTracking = printPartTracking;
            var bdlMap = new Hashtable();
            var ssqagMap = new Hashtable();
            var casMap = new Hashtable();
            vm.listSize = 0;
            vm.checks = null;
            var children = [];
            var partTracking = {
                partTracking: null,
                comment: null,
                attachment: null,
                createdBy: window.$application.login.person.id,
                checkedDate: new Date(),
                checkedDept: null,
                checked: null
            };

            function addChildren(children) {
                angular.forEach(children, function (child) {
                    if (child.item.itemType.parent == 'PART' || child.item.itemType.parent == 'GENERIC' ||
                        child.item.itemType.storeAsLot) {
                        vm.items.push(child);
                    }
                    if (child.children.length > 0) {
                        addChildren(child.children);
                    }
                });
            }

            vm.items = [];
            vm.children = null;
            vm.loadScanItems = loadScanItems;
            function loadScanItems() {
                BomService.getBomItemChildren(vm.bomItemId, $bom.bomObject.parent.id).then(
                    function (data) {
                        vm.children = data;
                        if ($bom.bomObject.item.itemType.name == 'MBOM') {
                            vm.sectionForPdf = "";
                            var children = angular.copy(vm.children);
                            vm.children = [];
                            angular.forEach(children, function (child) {
                                if (child.item.itemType.parent == "SECTION") {
                                    if (vm.sectionForPdf == "") {
                                        vm.sectionForPdf = child.item.itemName;
                                    } else {
                                        vm.sectionForPdf = vm.sectionForPdf + ', ' + child.item.itemName;
                                    }
                                    vm.children.push(child);
                                }
                            });
                            //vm.children = removeDuplicates(vm.children, 'sectionId');
                        }
                        addChildren(vm.children);
                        if ($bom.bomObject.item.itemType.parent == 'GENERIC' || $bom.bomObject.item.itemType.parent == 'PART'
                            || $bom.bomObject.item.itemType.storeAsLot) {
                            vm.items.unshift($bom.bomObject.item);
                        }
                    }
                );
            }

            function removeDuplicates(originalArray, objKey) {
                var trimmedArray = [];
                var values = [];
                var value;

                for (var i = 0; i < originalArray.length; i++) {
                    value = originalArray[i][objKey];

                    if (values.indexOf(value) === -1) {
                        trimmedArray.push(originalArray[i]);
                        values.push(value);
                    }
                }

                return trimmedArray;

            }

            function showScanItems(partTracking) {
                var options = {
                    title: "Scan Items",
                    template: 'app/desktop/modules/bom/details/partTracking/showScanPartTrackingItems.jsp',
                    controller: 'ShowScanPartTrackingController as showScanVm',
                    resolve: 'app/desktop/modules/bom/details/partTracking/showScanPartTrackingController',
                    width: 500,
                    data: {
                        bomId: vm.bomItemId,
                        chekedId: partTracking.bdl.id
                    },
                    showMask: true,
                    buttons: [
                        {text: 'Ok', broadcast: 'app.partTracking.scanned'}
                    ],
                    callback: function (result) {
                        if (result != null) {
                            partTracking.scanned = result;
                            partTracking.scan = true;
                        }
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function showReScanItems(partTracking) {
                var options = {
                    title: "Scan Items",
                    template: 'app/desktop/modules/bom/details/partTracking/showReScanPartTrackingItems.jsp',
                    controller: 'ShowReScanPartTrackingController as showReScanVm',
                    resolve: 'app/desktop/modules/bom/details/partTracking/showReScanPartTrackingController',
                    width: 500,
                    data: {
                        bomId: vm.bomItemId,
                        chekedId: partTracking.bdl.id
                    },
                    showMask: true,
                    buttons: [
                        {text: 'Verify', broadcast: 'app.partTracking.verify'}
                    ],
                    callback: function (result) {
                        if (result != null) {
                            if (partTracking.bdl != undefined) {
                                var bdl = {
                                    scanned: result,
                                    scan: true,
                                    newComment: partTracking.bdl.newComment,
                                    attachmentObject: partTracking.bdl.attachmentObject,
                                    id: partTracking.bdl.id,
                                    checked: true,
                                    edit: true,
                                    createdBy: partTracking.bdl.createdBy,
                                    createdByObject: partTracking.bdl.createdByObject,
                                    partTracking: partTracking.bdl.partTracking,
                                    checkedDept: partTracking.bdl.checkedDept,
                                    modifiedDate: partTracking.bdl.modifiedDate,
                                    createdDate: partTracking.bdl.createdDate,
                                    objectType: 'CHECKLIST'
                                };
                            } else {
                                var bdl = {
                                    scanned: result,
                                    scan: true
                                };
                            }
                            partTracking.bdl = bdl;
                        }
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function scanItems(partTracking) {
                var options = {
                    title: "Scan Items",
                    template: 'app/desktop/modules/bom/details/partTracking/scanPartTrackingItems.jsp',
                    controller: 'ScanPartTrackingController as scanVm',
                    resolve: 'app/desktop/modules/bom/details/partTracking/scanPartTrackingController',
                    width: 600,
                    data: {
                        bomId: vm.bomItemId
                    },
                    showMask: true,
                    buttons: [
                        {text: 'Verify', broadcast: 'app.partTracking.scan'}
                    ],
                    callback: function (result) {
                        if (result != null && result != "") {
                            if (partTracking.bdl != undefined) {
                                var bdl = {
                                    scanned: result,
                                    scan: true,
                                    newComment: partTracking.bdl.newComment,
                                    attachmentObject: partTracking.bdl.attachmentObject,
                                    id: partTracking.bdl.id
                                };
                            } else {
                                var bdl = {
                                    scanned: result,
                                    scan: true
                                };
                            }
                            partTracking.bdl = bdl;
                        }
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function validate(checkListItem, dept, chk) {
                var valid = true;
                if (!chk) {
                    if (dept == 'SSQAG') {
                        if (checkListItem.ssqag == undefined || checkListItem.ssqag.newComment == null || checkListItem.ssqag.newComment == "") {
                            valid = false;
                            $rootScope.showErrorMessage("Please enter reason for On hold");
                        }
                    } else if (dept == 'CAS') {
                        if (checkListItem.cas == undefined || checkListItem.cas.newComment == null || checkListItem.cas.newComment == "") {
                            valid = false;
                            $rootScope.showErrorMessage("Please enter reason for On hold");
                        }
                    }
                }
                /*else {
                 if (dept == 'BDL' && checkListItem.partTrackingStepObject.scan) {
                 if (checkListItem.bdl == undefined || checkListItem.bdl.scan == null || checkListItem.bdl.scan == "") {
                 valid = false;
                 $rootScope.showErrorMessage("Please Scan the parts");
                 }
                 }
                 }*/
                return valid;
            }

            function check(checkListItem, dept, chk, mode) {
                if (validate(checkListItem, dept, chk)) {
                    var attachment = null;
                    var scanned = null;
                    checkListItem.status = dept;
                    var copyPartTracking = angular.copy(partTracking);
                    copyPartTracking.checked = chk;

                    if (dept == 'BDL' && checkListItem.bdl != undefined) {
                        checkListItem.bdl.edit = false;
                        copyPartTracking.comment = checkListItem.bdl.newComment;
                        copyPartTracking.id = checkListItem.bdl.id;
                        copyPartTracking.scan = checkListItem.bdl.scan;
                        scanned = checkListItem.bdl.scanned;
                        attachment = checkListItem.bdl.attachmentObject;
                        if (checkListItem.bdl.scan != undefined) {
                            copyPartTracking.scan = checkListItem.bdl.scan;
                        } else if (checkListItem.scan != undefined) {
                            copyPartTracking.scan = checkListItem.scan;
                        }
                        if (checkListItem.scanned != undefined) {
                            copyPartTracking.scanned = checkListItem.scanned;
                        }
                    } else if (dept == 'SSQAG' && checkListItem.ssqag != undefined) {
                        checkListItem.ssqag.edit = false;
                        copyPartTracking.comment = checkListItem.ssqag.newComment;
                        copyPartTracking.id = checkListItem.ssqag.id;
                        attachment = checkListItem.ssqag.attachmentObject;
                    } else if (dept == 'CAS' && checkListItem.cas != undefined) {
                        checkListItem.cas.edit = false;
                        copyPartTracking.comment = checkListItem.cas.newComment;
                        copyPartTracking.id = checkListItem.cas.id;
                        attachment = checkListItem.cas.attachmentObject;
                    }
                    copyPartTracking.partTracking = checkListItem.id;
                    copyPartTracking.checkedDept = dept;

                    PartTrackingService.createPartTracking(copyPartTracking).then(
                        function (data) {
                            copyPartTracking.id = data.id;
                            if (scanned != null || scanned != undefined) {
                                saveScannedUpns(scanned, data.id);
                            }
                            if (attachment != null || attachment != undefined) {
                                AttachmentService.saveAttachment(data.id,
                                    'TRACKVALUE', attachment).then(
                                    function (data) {
                                        if (data[0] != undefined || data[0] != null) {
                                            copyPartTracking.attachment = data[0].id;
                                            PartTrackingService.updatePartTracking(copyPartTracking).then(
                                                function (data) {
                                                    AttachmentService.getAttachmentReferences([data], 'attachment');
                                                    setValues(data, checkListItem, copyPartTracking, dept, chk);
                                                }
                                            )
                                        }
                                    })
                            }
                            else {
                                setValues(data, checkListItem, copyPartTracking, dept, chk);
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                    ;


                    /*if (chk) {
                     if (dept == 'BDL' && !checkListItem.partTrackingStepObject.cas && !checkListItem.partTrackingStepObject.ssqag) {
                     updateBom(checkListItem);
                     } else if (dept == 'SSQAG' && !checkListItem.partTrackingStepObject.cas) {
                     updateBom(checkListItem);
                     } else if (dept == 'CAS') {
                     updateBom(checkListItem);
                     }
                     }*/

                    if (dept == 'BDL' && mode == 'save') {
                        updateBom(checkListItem);
                    }
                    updatePartTrackingItem(checkListItem);
                }
            }

            function saveScannedUpns(scanned, id) {
                var arrUpn = [];
                var upns = scanned.split(';');
                angular.forEach(upns, function (upn) {
                    if (upn != "" && upn != null) {
                        var upn2 = {
                            partTrackingId: id,
                            upn: upn
                        };
                        arrUpn.push(upn2);
                    }
                });

                PartTrackingService.saveScannedUpnsByPartTrackingStep(arrUpn).then(
                    function (data) {

                    }
                )

            }

            function updatePartTrackingItem(checkItem) {
                PartTrackingService.updatePartTrackingItem(checkItem).then(
                    function (data) {
                        checkItem = data;
                    }
                )
            }

            function updateBom(checkListItem) {
                var promise = null
                $bom.bomObject.status = checkListItem.partTrackingStepObject.status;
                $bom.bomObject.percentage =
                    $bom.bomObject.percentage + ((100 / vm.totalPercentage) * checkListItem.partTrackingStepObject.percentage);
                if ($bom.bomObject.percentage >= 99) {
                    $bom.bomObject.percentage = 100;
                }

                if ($bom.bomObject.objectType == 'BOMINSTANCE') {
                    promise = BomService.updateBomInstance($bom.bomObject.id, $bom.bomObject);
                } else {
                    promise = BomService.updateBomItemInstance($bom.bomObject.id, $bom.bomObject);
                }
                promise.then(
                    function (data2) {
                        if (children.length > 0) {
                            angular.forEach(children, function (item) {
                                item.status = checkListItem.partTrackingStepObject.status;
                            });

                            ItemBomService.updateBomItems($bom.bomObject.id, children).then(
                                function (data) {

                                }
                            )
                        }
                    }
                )
            }

            function setValues(data, checkListItem, copyPartTracking, dept, chk) {
                CommonService.getPersonReferences([data], 'createdBy');
                data.newComment = data.comment;
                if (data.checkedDept == 'BDL') {
                    if (chk) {
                        $rootScope.showInfoMessage("BDL accepted successfully");
                    } else if (!chk) {
                        $rootScope.showInfoMessage("BDL On holded");
                    }
                    bdlMap.put(data.partTracking, data);
                } else if (data.checkedDept == 'SSQAG') {
                    if (chk) {
                        $rootScope.showInfoMessage("SSQAG accepted successfully");
                    } else if (!chk) {
                        $rootScope.showInfoMessage("SSQAG On holded");
                    }
                    ssqagMap.put(data.partTracking, data);
                } else if (data.checkedDept == 'CAS') {
                    if (chk) {
                        $rootScope.showInfoMessage("CAS accepted successfully");
                    } else if (!chk) {
                        $rootScope.showInfoMessage("CAS On holded");
                    }
                    casMap.put(data.partTracking, data);
                }

                if (!bdlMap.isEmpty() && bdlMap.containsKey(checkListItem.id)) {
                    copyPartTracking.bdl = bdlMap.get(checkListItem.id);
                }
                if (!ssqagMap.isEmpty() && ssqagMap.containsKey(checkListItem.id)) {
                    copyPartTracking.ssqag = ssqagMap.get(checkListItem.id);
                }
                if (!casMap.isEmpty() && casMap.containsKey(checkListItem.id)) {
                    copyPartTracking.cas = casMap.get(checkListItem.id);
                }

                var valid = false;
                var anyCheck = false;
                var anyReject = false;
                angular.forEach(vm.partTrackings, function (partTracking) {
                    partTracking.reject = false;
                    if (partTracking.id == copyPartTracking.partTracking && dept == 'BDL') {
                        partTracking.bdl = copyPartTracking.bdl;
                    } else if (partTracking.id == copyPartTracking.partTracking && dept == 'SSQAG') {
                        partTracking.ssqag = copyPartTracking.ssqag;
                    } else if (partTracking.id == copyPartTracking.partTracking && dept == 'CAS') {
                        partTracking.cas = copyPartTracking.cas;
                    }
                    if (valid) {
                        partTracking.previousCompleted = true;
                        valid = false;
                    }

                    if (anyReject) {
                        partTracking.reject = true;
                    }

                    if (partTracking.bdl != undefined && partTracking.bdl.checked == true) {
                        partTracking.previousCompleted = true;
                        anyCheck = true;
                        valid = true;
                    }

                    if ((partTracking.ssqag != undefined && partTracking.ssqag.checked == false) ||
                        (partTracking.cas != undefined && partTracking.cas.checked == false)) {
                        anyReject = true;
                    }

                });
            }

            function generateReport() {
                PartTrackingService.clearancePartTrackingReport(vm.bomItemId).then(
                    function (data) {
                        var url = "{0}//{1}//api/drdo/itempartTrackings/file/".format(window.location.protocol, window.location.host);
                        url += data + "/download";
                        launchUrl(url);
                        $rootScope.showSuccessMessage("Report exported successfully");
                    }
                )
            }

            function saveCheckList(checkList) {
                //checkList.checkedDept = dept;
                if (checkList.newAttachment != null || checkList.newAttachment != undefined) {
                    AttachmentService.saveAttachment(vm.bomItemId,
                        'BOMITEM', checkList.newAttachment).then(
                        function (data) {
                            checkList.attachment = data[0].id;
                            addCheckList(checkList);
                        }
                    )
                } else {
                    addCheckList(checkList);
                }
            }

            function back() {
                $window.history.back();
            }

            function addCheckList(copyPartTracking) {
                PartTrackingService.createPartTracking(copyPartTracking).then(
                    function (data) {
                        //loadCheckList();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function saveCheckLists() {
                $rootScope.hideSidePanel('left');
            }

            function freezeData() {
                var valid = false;
                var anyCheck = false;
                var anyReject = false;
                angular.forEach(vm.partTrackings, function (partTracking) {

                    if (valid) {
                        partTracking.previousCompleted = true;
                        valid = false;
                    }

                    if (anyReject) {
                        partTracking.reject = true;
                    }

                    if (partTracking.bdl != undefined && partTracking.bdl.checked == true) {
                        partTracking.previousCompleted = true;
                        anyCheck = true;
                        valid = true;
                    }

                    if ((partTracking.ssqag != undefined && partTracking.ssqag.checked == false) ||
                        (partTracking.cas != undefined && partTracking.cas.checked == false)) {
                        anyReject = true;
                    }
                });
                if (!anyCheck) {
                    if (vm.partTrackings.length > 0) {
                        vm.partTrackings[0].previousCompleted = true;
                    }
                }
            }

            vm.whoCheckedList = [];
            vm.lists = [];
            vm.totalList = [];

            vm.checkListPdf = $rootScope.checkListPdf;
            vm.missileNumberForPdf = null;
            vm.parentOfMissileForPdf = null;
            vm.secItemNumberForPdf = null;
            vm.sectionForPdf = null;
            vm.totalPercentage = 0;
            vm.datetime = null;

            function loadPartTracking() {
                vm.countChecks = 0;
                PartTrackingService.getPartTrackingItemsByBomId(vm.bomItemId).then(
                    function (data) {
                        vm.partTrackings = data;
                        var currentdate = new Date();
                        vm.datetime = currentdate.getDate() + "/"
                            + (currentdate.getMonth() + 1) + "/"
                            + currentdate.getFullYear() + " "
                            + currentdate.getHours() + ":"
                            + currentdate.getMinutes() + ":"
                            + currentdate.getSeconds();
                        ListService.getPartTrackingStepReference(vm.partTrackings, 'partTrackingStep');
                        loadPartTrackingValues();
                        $timeout(function () {

                            angular.forEach(vm.partTrackings, function (partTracking) {
                                vm.missileNumberForPdf = partTracking.missile;
                                vm.parentOfMissileForPdf = partTracking.parentOfMissile;
                                vm.secItemNumberForPdf = partTracking.secItemNumber;
                                if (vm.sectionForPdf == null)
                                    vm.sectionForPdf = partTracking.section;
                                vm.totalPercentage = vm.totalPercentage + partTracking.partTrackingStepObject.percentage;
                                if (partTracking.partTrackingStepObject.bdl == true) {
                                    vm.countChecks++;
                                }
                                if (partTracking.partTrackingStepObject.ssqag == true) {
                                    vm.countChecks++;
                                }
                                if (partTracking.partTrackingStepObject.cas == true) {
                                    vm.countChecks++;
                                }

                            })
                        }, 500);
                    })
            }

            vm.lastRowOfCheckList = [];

            vm.bdlCreatedDateForPdf = null;
            vm.bdlCreatedByForPdf = null;
            vm.casCreatedDateForPdf = null;
            vm.casCreatedByForPdf = null;
            vm.ssqagCreatedDateForPdf = null;
            vm.ssqagCreatedByForPdf = null;
            function loadPartTrackingValues() {
                var ids = [];
                vm.checkedCount = 0;
                angular.forEach(vm.partTrackings, function (partTracking) {
                    ids.push(partTracking.id);
                });
                PartTrackingService.getPartTrackingByCheckIds(ids).then(
                    function (data) {
                        CommonService.getPersonReferences(data, 'createdBy');
                        AttachmentService.getAttachmentReferences(data, 'attachment');
                        vm.checks = data;
                        $timeout(function () {
                            angular.forEach(data, function (check) {
                                check.newComment = check.comment;
                                if (check.checkedDept == 'BDL') {
                                    bdlMap.put(check.partTracking, check);
                                } else if (check.checkedDept == 'SSQAG') {
                                    ssqagMap.put(check.partTracking, check);
                                } else if (check.checkedDept == 'CAS') {
                                    casMap.put(check.partTracking, check);
                                }
                            });

                            angular.forEach(vm.partTrackings, function (partTracking) {

                                if (!bdlMap.isEmpty() && bdlMap.containsKey(partTracking.id)) {
                                    partTracking.bdl = bdlMap.get(partTracking.id);
                                }
                                if (!ssqagMap.isEmpty() && ssqagMap.containsKey(partTracking.id)) {
                                    partTracking.ssqag = ssqagMap.get(partTracking.id);
                                }
                                if (!casMap.isEmpty() && casMap.containsKey(partTracking.id)) {
                                    partTracking.cas = casMap.get(partTracking.id);
                                }
                            });
                            freezeData();
                            vm.checkedCount = bdlMap.size() + ssqagMap.size() + casMap.size();
                            vm.lastRowOfCheckList = vm.partTrackings[vm.partTrackings.length - 1];

                            if (vm.lastRowOfCheckList.bdl != null) {

                                if (vm.lastRowOfCheckList.bdl.checkedDept == 'BDL' && vm.lastRowOfCheckList.bdl.checked == true) {
                                    vm.bdlCreatedDateForPdf = vm.lastRowOfCheckList.bdl.createdDate;
                                    vm.bdlCreatedByForPdf = vm.lastRowOfCheckList.bdl.createdByObject.firstName;
                                }
                                else {
                                    vm.bdlCreatedDateForPdf = "";
                                    vm.bdlCreatedByForPdf = "";
                                }
                            }

                            if (vm.lastRowOfCheckList.cas != null) {
                                if (vm.lastRowOfCheckList.cas.checkedDept == 'CAS' && vm.lastRowOfCheckList.cas.checked == true) {
                                    vm.casCreatedDateForPdf = vm.lastRowOfCheckList.cas.createdDate;
                                    vm.casCreatedByForPdf = vm.lastRowOfCheckList.cas.createdByObject.firstName;
                                }
                                else {
                                    vm.casCreatedDateForPdf = "";
                                    vm.casCreatedByForPdf = "";
                                }
                            }

                            if (vm.lastRowOfCheckList.ssqag != null) {
                                if (vm.lastRowOfCheckList.ssqag.checkedDept == 'SSQAG' && vm.lastRowOfCheckList.ssqag.checked == true) {
                                    vm.ssqagCreatedDateForPdf = vm.lastRowOfCheckList.ssqag.createdDate;
                                    vm.ssqagCreatedByForPdf = vm.lastRowOfCheckList.ssqag.createdByObject.firstName;
                                }
                                else {
                                    vm.ssqagCreatedDateForPdf = "";
                                    vm.ssqagCreatedByForPdf = "";
                                }
                            }
                        }, 200);
                    }
                )
            }

            function editPartTracking(partTracking, dept) {

                if (dept == 'BDL') {
                    partTracking.bdl.edit = true;
                } else if (dept == 'SSQAG') {
                    partTracking.ssqag.edit = true;
                } else if (dept == 'CAS') {
                    partTracking.cas.edit = true;
                }

            }

            function updatePartTracking(partTracking, dept) {
                var chk = null;
                if (dept == 'BDL') {
                    chk = partTracking.bdl.checked;
                } else if (dept == 'SSQAG') {
                    chk = partTracking.ssqag.checked;
                } else if (dept == 'CAS') {
                    chk = partTracking.cas.checked;
                }

                check(partTracking, dept, chk, 'update');
            }

            function openAttachment(file) {
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

            /* $scope.getAllocations = function(item){
             var list =  vm.childItemAndPartNumberMap.get(item);
             return list;
             }*/


            vm.pdfChildrenItems = [];
            vm.ListOfAllocationParts = [];
            vm.ListOfAllocationLots = [];
            function loadBomChildren() {
                BomService.getBomItemChildren($bom.bomObject.id, $bom.bomObject.parent.id).then(
                    function (data1) {
                        vm.childItemAndPartNumberMap = new Hashtable();
                        angular.forEach(data1, function (item) {
                            children.push(item);
                            if (item.children.length > 0) {
                                getChildren(item.children);
                                angular.forEach(item.children, function (childItemName) {
                                    angular.forEach(childItemName.children, function (childItemNames) {
                                        childItemNames.item.numbers = [];
                                        childItemNames.item.oemNums = [];
                                        childItemNames.item.allocatedOems = [];
                                        if (childItemNames.allocationList != null) {
                                            if (childItemNames.allocationList.length >= 0) {
                                                var noCount = 0;
                                                angular.forEach(childItemNames.allocationList, function (batch) {
                                                    childItemNames.item.numbers.push(batch);
                                                    noCount += 1;
                                                    childItemNames.item.requiredQty = null;
                                                    childItemNames.item.requiredQty = childItemNames.quantity;
                                                    if (noCount == childItemNames.allocationList.length) {
                                                        vm.pdfChildrenItems.push(childItemNames.item);
                                                    }
                                                });
                                            }

                                        }
                                        if (childItemNames.allocatedLots != null) {
                                            var noCountLot = 0;
                                            childItemNames.item.oem = [];
                                            angular.forEach(childItemNames.allocatedLots, function (lot) {
                                                childItemNames.item.numbers.push(lot);
                                                childItemNames.item.oemNums.push(lot.oemNumber);
                                                noCountLot += 1;
                                                childItemNames.item.requiredQty = null;
                                                childItemNames.item.requiredQty = childItemNames.quantity;
                                                if (noCountLot == childItemNames.allocatedLots.length) {
                                                    vm.pdfChildrenItems.push(childItemNames.item);
                                                }
                                            });
                                        }
                                    });
                                })
                            }
                        })
                    })
            }

            function getChildren(items) {
                angular.forEach(items, function (item) {
                    children.push(item.item);
                    if (item.children.length > 0) {
                        getChildren(item.children);

                    }
                })
            }

            function printPartTracking() {
                $("#checkListPdf1").show();

                var pdf = new jsPDF('l', 'pt', 'a0');
                pdf.setFont('courier');
                pdf.setFontType('bolditalic');
                pdf.setFontSize(60);
                pdf.setTextColor(107, 5, 132);
                pdf.text(1370, 500, 'Part Tracking Report');
                var today = moment(new Date());
                var todayStr = today.format('DD/MM/YYYY');
                pdf.setFontSize(40);
                pdf.setTextColor(0, 0, 255);
                var width = pdf.internal.pageSize.width;
                var height = pdf.internal.pageSize.height;
                pdf.rect(20, 20, pdf.internal.pageSize.width - 40, pdf.internal.pageSize.height - 40, 'S');
                var imgData = $rootScope.drdoImage;
                pdf.addImage(imgData, 'JPG', 1500, 40, 350, 350);
                pdf.setTextColor(7, 7, 7);
                pdf.setFontType('bold');
                if ($bom.bomObject.item != null) {
                    pdf.text(200, 600, "BOM :" + $bom.bomObject.item.item.itemMaster.itemName);
                    pdf.text(1500, 600, "Missile : " + $bom.bomObject.item.instanceName);
                } else if ($bom.bomObject.typeRef != null) {
                    pdf.text(200, 600, "Missile :" + $bom.bomObject.hierarchicalCode);
                    pdf.text(1500, 600, "Section :" + $bom.bomObject.typeRef.name);
                }

                pdf.text(2500, 600, "Date : " + todayStr);
                var res2 = pdf.autoTableHtmlToJson(document.getElementById("checkListPdf1"));
                pdf.autoTable(res2.columns, res2.data, {
                    startY: 650,
                    theme: 'grid',
                    pageBreak: 'auto',
                    headerStyles: {fillColor: [137, 244, 66], textColor: 20},
                    styles: {
                        overflow: 'linebreak',
                        fontSize: 50,
                        rowHeight: 90,
                        columnWidth: '100',
                        theme: 'grid'
                    },
                    margin: {bottom: 400},
                    columnStyles: {
                        0: {columnWidth: 150},
                        1: {columnWidth: 500},
                        2: {columnWidth: 'auto'},
                        3: {columnWidth: 500},
                        4: {columnWidth: 250},
                        5: {columnWidth: 250},
                        6: {columnWidth: 250}
                    }
                });

                pdf.setFontSize(40);
                pdf.setTextColor(0, 0, 255);
                //var width = pdf.internal.pageSize.width;
                var height = pdf.internal.pageSize.height;
                pdf.setFont('courier');
                pdf.setFontType('bolditalic');
                pdf.setFontSize(60);
                pdf.setTextColor(107, 5, 132)
                pdf.text(1390, height + 50, 'Parts As Enclosed');
                pdf.setFont('courier')
                pdf.setFontType('bolditalic')
                pdf.setFontSize(60);
                pdf.setTextColor(107, 5, 132)
                pdf.text(1490, height + 100, 'Approvals');

                $("#checkListPdf1").hide();
                //pdf.save("CheckList ClearanceReport Report.pdf");
                window.open(pdf.output('bloburl'), '_blank');
            }

            (function () {
                loadPartTracking();
                $("#checkListPdf1").hide();
                $rootScope.$on('save.partTracking', saveCheckLists);
            })();
        }
    }
)
;