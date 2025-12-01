define(
    [
        'app/desktop/modules/inward/inward.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/shared/services/core/inwardService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/bomService'
    ],
    function (module) {
        module.controller('InwardsController', InwardsController);

        function InwardsController($scope, $rootScope, $translate, $window, $timeout, $application, $state,
                                   $stateParams, $cookies, CommonService, DialogService, InwardService, ItemTypeService, ItemService,
                                   AttributeAttachmentService, AttachmentService, BomService) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa fa-sign-in";
            $rootScope.viewInfo.title = "Inwards";

            var vm = this;

            vm.loading = true;
            vm.clear = false;
            $rootScope.searchModeType = false;
            var currencyMap = new Hashtable();

            vm.filterMode = false;

            vm.pageable = {
                page: 0,
                size: 30,
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
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.inwardFilter = {
                notification: false,
                status: null,
                searchQuery: null,
                finishedPage: false,
                gatePassView: false,
                gatePassPermission: false,
                fromDate: null,
                toDate: null,
                finish: false,
                month: null
            };

            vm.inwards = angular.copy(pagedResults);
            vm.inwardReport = angular.copy(pagedResults);

            vm.showNewInward = showNewInward;
            vm.showNewGatePass = showNewGatePass;

            vm.showInward = showInward;

            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.resetPage = resetPage;
            vm.flag = false;
            vm.finishedInwardsView = false;
            vm.gatePassView = false;
            vm.inwardReportView = false;

            vm.changeView = changeView;
            vm.addFilter = addFilter;
            vm.getFilterResults = getFilterResults;
            vm.cancelFilter = cancelFilter;
            vm.clearDateFilter = clearDateFilter;
            vm.errorMessage = null;

            function clearDateFilter() {
                vm.errorMessage = null;
                vm.inwardFilter.fromDate = null;
                vm.inwardFilter.toDate = null;
                vm.inwardFilter.month = null;
                vm.filterMode = false;
                loadInwards();
            }

            function cancelFilter() {
                var modal = document.getElementById("add-filter");
                modal.style.display = "none";
                vm.inwardFilter.fromDate = null;
                vm.inwardFilter.toDate = null;
                vm.inwardFilter.month = null;
                vm.filterMode = false;
            }

            function getFilterResults() {
                vm.errorMessage = null;
                if ((vm.inwardFilter.fromDate != null && vm.inwardFilter.fromDate != "" && vm.inwardFilter.fromDate != undefined) &&
                    (vm.inwardFilter.toDate != null && vm.inwardFilter.toDate != "" && vm.inwardFilter.toDate != undefined)) {
                    vm.inwardFilter.month = null;
                    loadInwards();
                    var modal = document.getElementById("add-filter");
                    modal.style.display = "none";
                    vm.filterMode = true;
                } else {
                    vm.errorMessage = "Please select From Date and To Date";
                }
            }

            vm.getMonthResults = getMonthResults;
            function getMonthResults() {
                vm.errorMessage = null;
                if ((vm.inwardFilter.month != null && vm.inwardFilter.month != "" && vm.inwardFilter.month != undefined)) {
                    vm.inwardFilter.fromDate = null;
                    vm.inwardFilter.toDate = null;
                    loadInwards();
                    var modal = document.getElementById("add-filter");
                    modal.style.display = "none";
                    vm.filterMode = true;
                } else {
                    vm.errorMessage = "Please select month";
                }
            }

            function addFilter() {
                vm.errorMessage = null;
                vm.inwardFilter.fromDate = null;
                vm.inwardFilter.toDate = null;
                var modal = document.getElementById("add-filter");
                modal.style.display = "block";
            }

            function changeView(mode) {
                vm.errorMessage = null;
                vm.inwardFilter.fromDate = null;
                vm.inwardFilter.toDate = null;
                if (mode == "finish") {
                    vm.finishedInwardsView = !vm.finishedInwardsView;
                    vm.inwardFilter.gatePassView = false;
                    vm.inwardReportView = false;
                    vm.gatePassView = false;
                    vm.pageable = {
                        page: 0,
                        size: 20,
                        sort: {
                            field: "modifiedDate",
                            order: "DESC"
                        }
                    };
                    if (vm.finishedInwardsView) {
                        vm.inwardFilter.finishedPage = true;
                        vm.inwardReportView = false;
                        $rootScope.viewInfo.title = "Finished Inwards";
                        loadInwardAttributes();
                    } else {
                        vm.inwardFilter.finishedPage = false;
                        vm.inwardReportView = false;
                        $rootScope.viewInfo.title = "Inwards";
                        loadInwardAttributes();
                    }
                } else if (mode == "gatePass") {
                    vm.gatePassView = !vm.gatePassView;
                    vm.inwardFilter.finishedPage = false;
                    vm.inwardReportView = false;
                    vm.finishedInwardsView = false;
                    vm.pageable = {
                        page: 0,
                        size: 20,
                        sort: {
                            field: "modifiedDate",
                            order: "DESC"
                        }
                    };
                    if (vm.gatePassView) {
                        vm.inwardFilter.gatePassView = true;
                        vm.inwardFilter.finishedPage = false;
                        vm.inwardReportView = false;
                        $rootScope.viewInfo.title = "Gate Passes";
                        loadInwardAttributes();
                    } else {
                        vm.inwardFilter.gatePassView = false;
                        vm.inwardFilter.finishedPage = false;
                        vm.inwardReportView = false;
                        $rootScope.viewInfo.title = "Inwards";
                        loadInwardAttributes();
                    }
                } else if (mode == "inwardReport") {
                    vm.inwardReportView = !vm.inwardReportView;
                    if (vm.inwardReportView) {
                        vm.inwardFilter.fromDate = null;
                        vm.inwardFilter.toDate = null;
                        vm.inwardFilter.gatePassView = false;
                        vm.inwardFilter.finishedPage = false;
                        vm.inwardDto.gatePassView = false;
                        vm.inwardDto.inwardsPage = false;
                        vm.searchText = null;
                        vm.inwardFilter.searchQuery = null;
                        $rootScope.viewInfo.title = "Inward Report";
                        loadInwardReport();
                    } else {
                        vm.inwardFilter.gatePassView = false;
                        vm.inwardFilter.finishedPage = false;
                        $rootScope.viewInfo.title = "Inwards";
                        vm.searchText = null;
                        vm.inwardFilter.searchQuery = null;
                        loadInwardAttributes();
                    }
                }
            }

            function resetPage() {
                vm.pageable.page = 0;
                vm.inwardFilter.searchQuery = null;
            }

            function nextPage() {
                if (vm.inwardReportView) {
                    if (vm.inwardReport.last != true) {
                        vm.pageable.page++;
                        loadInwardReport();
                    }

                } else {
                    if (vm.inwardFilter.gatePassView) {
                        if (vm.gatePasses.last != true) {
                            vm.pageable.page++;
                            loadInwards();
                        }
                    } else {
                        if (vm.inwards.last != true) {
                            vm.pageable.page++;
                            loadInwards();
                        }
                    }
                }

            }

            function previousPage() {
                if (vm.inwardReportView) {
                    if (vm.inwardReport.first != true) {
                        vm.pageable.page--;
                        loadInwardReport();
                    }

                } else {
                    if (vm.inwardFilter.gatePassView) {
                        if (vm.gatePasses.first != true) {
                            vm.pageable.page--;
                            loadInwards();
                        }
                    } else {
                        if (vm.inwards.first != true) {
                            vm.pageable.page--;
                            loadInwards();
                        }
                    }
                }

            }

            function clearFilter() {
                loadInwards();
                vm.clear = false;
                $rootScope.showSearch = false;
            }

            function showNewInward() {
                var options = {
                    title: "New Inward",
                    template: 'app/desktop/modules/inward/new/newInwardView.jsp',
                    controller: 'NewInwardController as newInwardVm',
                    resolve: 'app/desktop/modules/inward/new/newInwardController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: "Create", broadcast: 'app.inwards.new'}
                    ],
                    callback: function () {
                        $timeout(function () {
                            loadInwards();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);

            }

            function showNewGatePass() {
                var options = {
                    title: "New Gate Pass",
                    template: 'app/desktop/modules/inward/new/newGatePassView.jsp',
                    controller: 'NewGatePassController as newGatePassVm',
                    resolve: 'app/desktop/modules/inward/new/newGatePassController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: "Create", broadcast: 'app.inwards.gatePass.new'}
                    ],
                    callback: function () {
                        $timeout(function () {
                            loadInwards();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);

            }

            function showInward(inward) {
                $state.go('app.inwards.details', {inwardId: inward.id, mode: 'inwards'});
            }

            vm.deleteInward = deleteInward;
            function deleteInward(inward) {
                var options = {
                    title: "Delete Inward",
                    message: "Please confirm to delete this Inward",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-content'));
                        InwardService.deleteInward(inward.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Inward deleted successfully");
                                vm.loading = true;
                                loadInwards();
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            function freeTextSearch(freeText) {
                if (vm.inwardReportView) {
                    if (freeText != null && freeText != "" && freeText != undefined) {
                        $scope.freeTextQuery = freeText;
                        vm.searchText = freeText;
                        loadInwardReport();
                    } else {
                        vm.searchText = null;
                        $scope.freeTextQuery = null;
                        resetPage();
                        loadInwardReport();
                    }
                } else {
                    if (freeText != null && freeText != "" && freeText != undefined) {
                        $scope.freeTextQuery = freeText;
                        vm.inwardFilter.searchQuery = freeText;
                        loadInwards();
                    } else {
                        $scope.freeTextQuery = null;
                        vm.inwardFilter.searchQuery = null;
                        resetPage();
                        loadInwards();
                    }
                }
            }


            function loadInwards() {
                vm.loading = true;
                vm.inwardFilter.adminPermission = $rootScope.hasPermission('permission.admin.all');
                vm.inwardFilter.storeApprove = $rootScope.hasPermission('permission.inward.storeApprove');
                vm.inwardFilter.ssqagApprove = $rootScope.hasPermission('permission.inward.SSQAGApprove');
                vm.inwardFilter.bdlApprove = $rootScope.hasPermission('permission.requests.bdlApprove');
                vm.inwardFilter.casApprove = $rootScope.hasPermission('permission.requests.casApprove');
                InwardService.getAllInwardsByPermission(vm.pageable, vm.inwardFilter).then(
                    function (data) {
                        vm.inwardDto = data;
                        vm.inwards = data.inwards;
                        if (vm.inwards == null) {
                            vm.inwards = angular.copy(pagedResults);
                        }
                        vm.gatePasses = data.gatePasses;
                        vm.loading = false;
                        if (vm.inwardDto.inwardsPage) {
                            CommonService.getPersonReferences(vm.inwards.content, 'createdBy');
                            CommonService.getPersonReferences(vm.inwards.content, 'modifiedBy');
                        } else {
                            CommonService.getPersonReferences(vm.gatePasses.content, 'createdBy');
                            $rootScope.viewInfo.title = "Gate Passes";
                            angular.forEach(vm.gatePasses.content, function (gatePass) {
                                var today = moment(new Date());
                                var todayStr = today.format('DD/MM/YYYY');
                                var todayDate = moment(todayStr, 'DD/MM/YYYY');

                                var startDate = moment(gatePass.createdDate, 'DD/MM/YYYY');

                                if (todayDate.isSame(startDate)) {
                                    gatePass.showNew = true;
                                } else {
                                    gatePass.showNew = false;
                                }
                            })
                        }

                        var inwardIds = [];
                        var attributeIds = [];

                        if (vm.inwardDto.inwardsPage) {
                            angular.forEach(vm.inwards.content, function (inward) {
                                var today = moment(new Date());
                                var todayStr = today.format('DD/MM/YYYY');
                                var todayDate = moment(todayStr, 'DD/MM/YYYY');

                                var startDate = moment(inward.createdDate, 'DD/MM/YYYY');

                                if (todayDate.isSame(startDate)) {
                                    inward.showNew = true;
                                } else {
                                    inward.showNew = false;
                                }

                                inwardIds.push(inward.id);
                            });

                            angular.forEach(vm.requiredInwardAttributes, function (attribute) {
                                attributeIds.push(attribute.id)
                            });

                            if (inwardIds.length > 0 && attributeIds.length > 0) {
                                ItemService.getAttributesByObjectIdAndAttributeId(inwardIds, attributeIds).then(
                                    function (data) {

                                        vm.selectedObjectAttributes = data;

                                        var map = new Hashtable();
                                        angular.forEach(vm.requiredInwardAttributes, function (att) {
                                            if (att.id != null && att.id != "" && att.id != 0) {
                                                map.put(att.id, att);
                                            }
                                        });

                                        angular.forEach(vm.inwards.content, function (inward) {
                                            var attributes = [];
                                            var itemAttributes = vm.selectedObjectAttributes[inward.id];
                                            if (itemAttributes != null && itemAttributes != undefined) {
                                                attributes = attributes.concat(itemAttributes);
                                            }
                                            angular.forEach(attributes, function (attribute) {
                                                var selectAtt = map.get(attribute.id.attributeDef);
                                                if (selectAtt != null) {
                                                    var attributeName = selectAtt.name;
                                                    if (selectAtt.dataType == 'TEXT') {
                                                        inward[attributeName] = attribute.stringValue;
                                                    } else if (selectAtt.dataType == 'INTEGER') {
                                                        inward[attributeName] = attribute.integerValue;
                                                    } else if (selectAtt.dataType == 'BOOLEAN') {
                                                        inward[attributeName] = attribute.booleanValue;
                                                    } else if (selectAtt.dataType == 'DOUBLE') {
                                                        inward[attributeName] = attribute.doubleValue;
                                                    } else if (selectAtt.dataType == 'DATE') {
                                                        inward[attributeName] = attribute.dateValue;
                                                    } else if (selectAtt.dataType == 'LIST') {
                                                        inward[attributeName] = attribute.listValue;
                                                    } else if (selectAtt.dataType == 'TIME') {
                                                        inward[attributeName] = attribute.timeValue;
                                                    } else if (selectAtt.dataType == 'TIMESTAMP') {
                                                        inward[attributeName] = attribute.timestampValue;
                                                    } else if (selectAtt.dataType == 'CURRENCY') {
                                                        inward[attributeName] = attribute.currencyValue;
                                                        if (attribute.currencyType != null) {
                                                            inward[attributeName + 'type'] = currencyMap.get(attribute.currencyType);
                                                        }
                                                    } else if (selectAtt.dataType == 'ATTACHMENT') {
                                                        var revisionAttachmentIds = [];
                                                        if (attribute.attachmentValues.length > 0) {
                                                            angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                                revisionAttachmentIds.push(attachmentId);
                                                            });
                                                            AttributeAttachmentService.getMultipleAttributeAttachments(revisionAttachmentIds).then(
                                                                function (data) {
                                                                    vm.revisionAttachments = data;
                                                                    inward[attributeName] = vm.revisionAttachments;
                                                                }
                                                            )
                                                        }
                                                    } else if (selectAtt.dataType == 'IMAGE') {
                                                        if (attribute.imageValue != null) {
                                                            inward[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                                        }
                                                    }
                                                }
                                            })
                                        })

                                    }
                                )
                            }
                        }
                    }
                )
            }

            vm.requiredInwardAttributes = [];
            function loadInwardAttributes() {
                vm.requiredInwardAttributes = [];
                ItemTypeService.getAttributesByObjectType("INWARD").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            if (attribute.required) {
                                vm.requiredInwardAttributes.push(attribute);
                            }
                        });
                        loadInwards();
                    }
                )
            }

            vm.selectedSystem = null;
            function loadInwardReport() {
                vm.loading = true;
                InwardService.getInwardReportBySystem(vm.selectedSystem.id, vm.searchText, vm.pageable).then(
                    function (data) {
                        vm.inwardReport = data;
                        vm.loading = false;
                    }
                )
            }

            vm.onSelectBom = onSelectBom;
            function onSelectBom(bom) {
                vm.selectedSystem = bom;
                loadInwardReport();
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

            vm.openAttachment = openAttachment;
            function openAttachment(attachment) {
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

            vm.showGatePassItems = showGatePassItems;

            function showGatePassItems(gatePass) {
                var options = {
                    title: gatePass.gatePassNumber + " details",
                    template: 'app/desktop/modules/gatePass/all/gatePassDetailsView.jsp',
                    controller: 'GatePassDetailsController as gatePassDetailsVm',
                    resolve: 'app/desktop/modules/gatePass/all/gatePassDetailsController',
                    width: 700,
                    data: {
                        gatePassId: gatePass.id
                    },
                    buttons: [
                        {text: "Close", broadcast: 'app.gatePass.details'}
                    ],
                    callback: function (result) {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadBoms() {
                BomService.getAllBoms().then(
                    function (data) {
                        vm.boms = data;
                        vm.selectedSystem = vm.boms[0];
                    }
                )
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    loadInwardAttributes();
                    loadBoms();
                });
            })();
        }
    }
);