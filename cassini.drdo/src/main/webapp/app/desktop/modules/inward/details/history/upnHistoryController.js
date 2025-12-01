define(
    [
        'app/desktop/modules/inward/inward.module',
        'app/shared/services/core/inwardService',
        'app/shared/services/core/itemTypeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('UpnHistoryController', UpnHistoryController);

        function UpnHistoryController($scope, $stateParams, $rootScope, $timeout, $interval, $state, $cookies, InwardService, ItemTypeService, AttributeAttachmentService) {

            var vm = this;

            vm.barcodeBust = (new Date()).getTime();
            vm.itemInstance = $scope.data.statusItemInstance;

            function loadUpnHistory() {
                InwardService.getUpnDetails(vm.itemInstance.id).then(
                    function (data) {
                        vm.upnDetails = data;
                        var attributeDefs = [];
                        var instanceIds = [];

                        angular.forEach(vm.itemInstanceAttributes, function (attribute) {
                            if (attribute.attributeDef.id != null && attribute.attributeDef.id != "" && attribute.attributeDef.id != 0) {
                                attributeDefs.push(attribute.attributeDef.id);
                            }
                        });

                        instanceIds.push(vm.upnDetails.itemInstance.id);

                        if (attributeDefs.length > 0 && instanceIds.length > 0) {
                            InwardService.getAttributesByItemIdAndAttributeId(instanceIds, attributeDefs).then(
                                function (data) {
                                    vm.selectedObjectAttributes = data;

                                    var map = new Hashtable();
                                    angular.forEach(vm.itemInstanceAttributes, function (att) {
                                        if (att.attributeDef.id != null && att.attributeDef.id != "" && att.attributeDef.id != 0) {
                                            map.put(att.attributeDef.id, att);
                                        }
                                    });

                                    var attributes = [];
                                    attributes = vm.selectedObjectAttributes[vm.upnDetails.itemInstance.id];

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
                                                            vm.upnDetails.certificates = vm.revisionAttachments;
                                                        }
                                                    )
                                                }
                                            }

                                            if (selectAtt.attributeDef.dataType == "DATE" && selectAtt.attributeDef.name == "Manufacture Date") {
                                                vm.upnDetails.manufacturerDate = attribute.dateValue;
                                            }
                                        }
                                    });

                                }
                            )
                        }
                    }
                )
            }

            vm.printInwardUpn = printInwardUpn;
            vm.printIssuedUpn = printIssuedUpn;
            vm.openIssue = openIssue;

            function openIssue(issue) {
                $state.go('app.issues.details', {issueId: issue.id});
            }

            function printInwardUpn(print) {
                var divElements = document.getElementById(print);

                var printData = window.open("", "", "left=0,top=0,width=800,height=900,toolbar=0,scrollbars=0,status=0");
                printData.document.write(divElements.innerHTML);
                printData.document.close();
                printData.focus();
                $timeout(
                    function () {
                        printData.print();
                        printData.close();
                    }, 1000
                );
            }

            function printIssuedUpn(print) {
                var divElements = document.getElementById(print);

                var printData = window.open("", "", "left=0,top=0,width=800,height=900,toolbar=0,scrollbars=0,status=0");
                printData.document.write(divElements.innerHTML);
                printData.document.close();
                printData.focus();
                $timeout(
                    function () {
                        printData.print();
                        printData.close();
                    }, 1000
                );
            }

            vm.showCode = false;
            vm.showInfo = false;
            vm.showCodification = showCodification;
            vm.showBasicInformation = showBasicInformation;
            function showCodification() {
                vm.showCode = !vm.showCode;
            }

            function showBasicInformation() {
                vm.showInfo = !vm.showInfo;
            }

            vm.downloadGatePass = downloadGatePass;
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

            vm.showInward = showInward;

            function showInward(inward) {
                $rootScope.hideSidePanel();
                $state.go('app.inwards.details', {inwardId: inward.id, mode: 'inwards'});
            }

            (function () {
                loadItemInstanceAttributes();
                loadUpnHistory();
            })();
        }
    }
);