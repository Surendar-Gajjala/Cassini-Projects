define(['app/desktop/modules/mfr/mfrparts/mfrparts.module',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/itemService',
        'app/desktop/modules/mfr/mfrparts/details/tabs/basic/mfrPartsBasicController',
        'app/desktop/modules/mfr/mfrparts/details/tabs/attributes/mfrPartsAttributesController',
        'app/desktop/modules/mfr/mfrparts/details/tabs/files/mfrPartsFilesController',
        'app/desktop/modules/mfr/mfrparts/details/tabs/inspectionReports/mfrPartInspectionReportsController',
        'app/desktop/modules/mfr/mfrparts/details/tabs/changes/mfrPartsChangesController',
        'app/desktop/modules/mfr/mfrparts/details/tabs/quality/mfrPartsQualityController',
        'app/desktop/modules/mfr/mfrparts/details/tabs/whereused/mfrPartsWhereUsedController',
        'app/desktop/modules/mfr/mfrparts/details/tabs/workflow/manufacturerPartWorkflowController',
        'app/desktop/modules/mfr/mfrparts/details/tabs/timelineHistory/mfrPartTimelineHistoryController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('MfrpartDetailsConroller', MfrpartDetailsConroller);

        function MfrpartDetailsConroller($scope, $rootScope, $timeout, $state, DialogService, $stateParams, $window, $cookies, MfrPartsService, ItemService, $translate, CommentsService, $application) {

            $rootScope.viewInfo.icon = "fa fa-cubes";
            $rootScope.viewInfo.title = $translate.instant("MFR_PART_DETAILS_TITLE");
            $rootScope.viewInfo.showDetails = true;

            var vm = this;
            var mfId = $stateParams.manufacturePartId;
            vm.mfrPartId = $stateParams.manufacturePartId;
            var mfrId = $stateParams.mfrId;
            vm.freeTextSearch = freeTextSearch;
            vm.downloadFilesAsZip = downloadFilesAsZip;
            vm.mfrparts = [];
            vm.onAddMfrPartFiles = onAddMfrPartFiles;
            vm.back = back;
            vm.active = 0;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];

            vm.broadcast = broadcast;
            vm.resetPage = resetPage;
            var session = JSON.parse(localStorage.getItem('local_storage_login'));
            $rootScope.loginPersonDetails = session.login;
            vm.external = $rootScope.loginPersonDetails;
            $rootScope.external = $rootScope.loginPersonDetails;
            $rootScope.sharedPermission = null;
            var parse = angular.element("<div></div>");

            $rootScope.showCopyMfrPartFilesToClipBoard = false;
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            var timelineHeading = parse.html($translate.instant("TIMELINE")).html();
            var whereUsed = parse.html($translate.instant("DETAILS_TAB_WHERE_USED")).html();
            var files = parse.html($translate.instant("FILES")).html();
            var workFlow = parse.html($translate.instant("WORKFLOW")).html();
            var quality = parse.html($translate.instant("QUALITY")).html();
            var changes = parse.html($translate.instant("ITEM_DETAILS_TAB_CHANGES")).html();
            vm.inspectionReportsTitle = parse.html($translate.instant("INSPECTION_REPORTS")).html();

            var subscribeMsg = parse.html($translate.instant("SUBSCRIBE_MSG")).html();
            var subscribeTitle = parse.html($translate.instant("SUBSCRIBE_TITLE")).html();
            var unsubscribeTitle = parse.html($translate.instant("UN_SUBSCRIBE_TITLE")).html();
            var unsubscribeMsg = parse.html($translate.instant("UN_SUBSCRIBE_MSG")).html();
            var partTitle = parse.html($translate.instant("MANUFACTURER_PART")).html();

            function broadcast(event) {
                $scope.$broadcast(event);
            }

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    index: 0,
                    template: 'app/desktop/modules/mfr/mfrparts/details/tabs/basic/mfrPartsBasicView.jsp',
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: 'Attributes',
                 index: 1,
                 template: 'app/desktop/modules/mfr/mfrparts/details/tabs/attributes/mfrPartsAttributesView.jsp',
                 active: false,
                 activated: false
                 }*/,
                whereUsed: {
                    id: 'details.whereUsed',
                    heading: whereUsed,
                    index: 1,
                    template: 'app/desktop/modules/mfr/mfrparts/details/tabs/whereused/mfrPartsWhereUsedView.jsp',
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: files,
                    index: 2,
                    template: 'app/desktop/modules/mfr/mfrparts/details/tabs/files/mfrPartsFilesView.jsp',
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: workFlow,
                    index: 3,
                    template: 'app/desktop/modules/mfr/mfrparts/details/tabs/workflow/manufacturerPartWorkflowView.jsp',
                    active: false,
                    activated: false
                },
                inspectionReports: {
                    id: 'details.inspectionReports',
                    heading: vm.inspectionReportsTitle,
                    index: 4,
                    template: 'app/desktop/modules/mfr/mfrparts/details/tabs/inspectionReports/mfrPartInspectionReportsView.jsp',
                    active: false,
                    activated: false
                },
                changes: {
                    id: 'details.changes',
                    heading: changes,
                    index: 5,
                    template: 'app/desktop/modules/mfr/mfrparts/details/tabs/changes/mfrPartsChangesView.jsp',
                    active: false,
                    activated: false
                },
                quality: {
                    id: 'details.quality',
                    heading: quality,
                    index: 6,
                    template: 'app/desktop/modules/mfr/mfrparts/details/tabs/quality/mfrPartsQualityView.jsp',
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mfr/mfrparts/details/tabs/timelineHistory/mfrPartTimelineHistoryView.jsp',
                    index: 7,
                    active: false,
                    activated: false
                }
            };

            vm.refreshDetails = refreshDetails;

            function refreshDetails() {
                $scope.$broadcast('app.mfrPart.tabactivated', {tabId: $rootScope.selectedTab.id});
            }

            vm.activateTab = activateTab;
            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
                for (var t in vm.customTabs) {
                    vm.customTabs[t].active = (vm.customTabs[t].id == tab.id);
                }
            }

            vm.showExternalUserMfrParts = showExternalUserMfrParts;
            function showExternalUserMfrParts() {
                $state.go('app.home');
                $rootScope.sharedMfrPart();

            }

            function back() {
                window.history.back();
            }

            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            vm.addPartWorkflow = addPartWorkflow;
            function addPartWorkflow() {
                $scope.$broadcast('app.add.workflow');
            }

            vm.mfrDetailsTabActivated = mfrDetailsTabActivated;

            function freeTextSearch(freeText) {
                $rootScope.freeTextQuerys = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    $scope.$broadcast('app.mfrpart.addfile', {name: freeText});
                    $scope.$broadcast('app.details.files.search', {name: freeText});
                }
                else {
                    $rootScope.freeTextQuerys = freeText;
                    $scope.$broadcast('app.mfrPart.tabactivated', {tabId: 'details.files'});
                    $rootScope.searchModeType = false;
                }
            }

            function resetPage() {
                $rootScope.searchModeType = false;
            }

            var tabId = $stateParams.tab;

            function mfrDetailsTabActivated(tabId) {
                $state.transitionTo('app.mfr.mfrparts.details', {
                    mfrId: mfrId,
                    manufacturePartId: mfId,
                    tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    tab.activated = true;
                    $rootScope.selectedTab = tab;
                    $scope.$broadcast('app.mfrPart.tabactivated', {tabId: tabId});

                }
                if (tab != null) {
                    activateTab(tab);
                }
            }

            function getTabById(tabId) {
                var tab = null;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t) && vm.tabs[t].id == tabId) {
                        tab = vm.tabs[t];
                    }
                }
                if (tab == null) {
                    angular.forEach(vm.customTabs, function (customTab) {
                        if (customTab.id === tabId) {
                            tab = customTab;
                        }
                    });
                }
                return tab;
            }

            function onAddMfrPartFiles() {
                $scope.$broadcast('app.mfr.mfrparts.onAddMfrPartFiles');
            }

            var number = $translate.instant("NUMBER");

            $rootScope.loadPartDetails = loadPartDetails;
            function loadPartDetails() {
                MfrPartsService.getManufacturepart(mfId).then(
                    function (data) {
                        vm.mfrPart = data;
                        $rootScope.mfrPart = data;
                        $rootScope.viewInfo.title = $translate.instant("MFR_PART_DETAILS_TITLE");
                        $rootScope.viewInfo.description = number + " : {0}, Status: {1}".
                                format(data.partNumber, data.status);
                        loadMfrPartCounts();
                        loadSubscribe();
                        setLifecycles();
                        loadCommentsCount();
                        vm.lastLifecyclePhase = $rootScope.mfrPart.mfrPartType.lifecycle.phases[$rootScope.mfrPart.mfrPartType.lifecycle.phases.length - 1];
                        vm.firstLifecyclePhase = $rootScope.mfrPart.mfrPartType.lifecycle.phases[0];
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.loadMfrPartCounts = loadMfrPartCounts;
            function loadMfrPartCounts() {
                MfrPartsService.getMfrPartCount(mfId).then(
                    function (data) {
                        vm.mfrCounts = data;

                        var filesTab = document.getElementById("files");
                        var whereUsed = document.getElementById("whereUsed");
                        var changes = document.getElementById("changesTab");
                        var quality = document.getElementById("qualityTab");
                        var inspectionReports = document.getElementById("inspectionReportTab");

                        filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.mfrCounts.files);
                        whereUsed.lastElementChild.innerHTML = vm.tabs.whereUsed.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.mfrCounts.whereUsed);
                        changes.lastElementChild.innerHTML = vm.tabs.changes.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.mfrCounts.changes);
                        quality.lastElementChild.innerHTML = vm.tabs.quality.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.mfrCounts.quality);
                        inspectionReports.lastElementChild.innerHTML = vm.tabs.inspectionReports.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.mfrCounts.inspectionReports);

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('MANUFACTURERPART', mfId).then(
                    function (data) {
                        $rootScope.showComments('MANUFACTURERPART', mfId, data);
                        $rootScope.showTags('MANUFACTURERPART', mfId, vm.mfrPart.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function downloadFilesAsZip() {
                $rootScope.showBusyIndicator($('.view-container'));
                var url = "{0}//{1}/api/plm/mfr/{2}/mfrParts/{3}/files/zip".
                    format(window.location.protocol, window.location.host, mfrId, mfId);

                //launchUrl(url);
                window.open(url);
                $rootScope.hideBusyIndicator();
            }

            var confirmation = parse.html($translate.instant("CONFIRMATION")).html();
            var doYouWantToPromotePart = parse.html($translate.instant("DO_YOU_WANT_TO_PROMOTE_MFRPART_STATUS")).html();
            var doYouWantToDemotePart = parse.html($translate.instant("DO_YOU_WANT_TO_DEMOTE_MFRPART_STATUS")).html();
            var thisparthasUsedInItemsWantToDemotePart = parse.html($translate.instant("CONFIRM_DEMOTE_MFRPART")).html();
            var yesTitle = parse.html($translate.instant("YES")).html();
            var noTitle = parse.html($translate.instant("NO")).html();
            var promoteSuccessMsg = parse.html($translate.instant("MFR_PART_PROMOTE_SUCCESS_MSG")).html();
            var demoteSuccessMsg = parse.html($translate.instant("MFR_PART_DEMOTE_SUCCESS_MSG")).html();
            vm.addWorkflowTitle = parse.html($translate.instant("ADD_WORKFLOW")).html();
            vm.changeWorkflowTitle = parse.html($translate.instant("CHANGE_WORKFLOW")).html();
            vm.shareTooltip = parse.html($translate.instant("SHARE")).html();
            vm.showItemMfrParts = false;

            $rootScope.promoteMfrPart = promoteMfrPart;
            function promoteMfrPart() {
                if ($rootScope.mfrPart.lifeCyclePhase.phaseType != 'RELEASED') {
                    var options = {
                        title: confirmation,
                        message: doYouWantToPromotePart,
                        okButtonClass: 'btn-danger',
                        okButtonText: yesTitle,
                        cancelButtonText: noTitle
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            MfrPartsService.promotePart(mfId, $rootScope.mfrPart).then(
                                function (data) {
                                    loadPartDetails();
                                    $rootScope.showSuccessMessage(promoteSuccessMsg);
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.loadManufacturerParts();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    })
                } else {
                    MfrPartsService.getItemMfrParts(mfId).then(
                        function (data) {
                            vm.itemMfrparts = data;
                            if (vm.itemMfrparts.length > 0) {
                                showMfrItemParts();
                                vm.showItemMfrParts = true;
                            } else {
                                var options = {
                                    title: confirmation,
                                    message: doYouWantToPromotePart,
                                    okButtonClass: 'btn-danger',
                                    okButtonText: yesTitle,
                                    cancelButtonText: noTitle
                                };
                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        $rootScope.showBusyIndicator($('.view-container'));
                                        MfrPartsService.promotePart(mfId, $rootScope.mfrPart).then(
                                            function (data) {
                                                loadPartDetails();
                                                $rootScope.showSuccessMessage(promoteSuccessMsg);
                                                $rootScope.hideBusyIndicator();
                                                $rootScope.loadManufacturerParts();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        )
                                    }
                                })
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }

            }

            $rootScope.demoteMfrPart = demoteMfrPart;
            function demoteMfrPart() {
                if ($rootScope.mfrPart.lifeCyclePhase.phaseType != 'RELEASED') {
                    var options = {
                        title: confirmation,
                        message: doYouWantToDemotePart,
                        okButtonClass: 'btn-danger',
                        okButtonText: yesTitle,
                        cancelButtonText: noTitle
                    };

                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            MfrPartsService.demotePart(mfId, $rootScope.mfrPart).then(
                                function (data) {
                                    loadPartDetails();
                                    $rootScope.showSuccessMessage(demoteSuccessMsg);
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.loadManufacturerParts();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    })
                } else {
                    MfrPartsService.getItemMfrParts(mfId).then(
                        function (data) {
                            vm.itemMfrparts = data;
                            if (vm.itemMfrparts.length > 0) {
                                var options = {
                                    title: confirmation,
                                    message: thisparthasUsedInItemsWantToDemotePart,
                                    okButtonClass: 'btn-danger',
                                    okButtonText: yesTitle,
                                    cancelButtonText: noTitle
                                };
                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        $rootScope.showBusyIndicator($('.view-container'));
                                        MfrPartsService.updateItemMfrParts(mfId, $rootScope.mfrPart).then(
                                            function (data) {
                                                loadPartDetails();
                                                $rootScope.showSuccessMessage(demoteSuccessMsg);
                                                $rootScope.hideBusyIndicator();
                                                $rootScope.loadManufacturerParts();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        )
                                    }
                                })
                            } else {
                                var options = {
                                    title: confirmation,
                                    message: doYouWantToDemotePart,
                                    okButtonClass: 'btn-danger',
                                    okButtonText: yesTitle,
                                    cancelButtonText: noTitle
                                };

                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        $rootScope.showBusyIndicator($('.view-container'));
                                        MfrPartsService.demotePart(mfId, $rootScope.mfrPart).then(
                                            function (data) {
                                                loadPartDetails();
                                                $rootScope.showSuccessMessage(demoteSuccessMsg);
                                                $rootScope.hideBusyIndicator();
                                                $rootScope.loadManufacturerParts();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        )
                                    }
                                })
                            }

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function setLifecycles() {
                var phases = [];
                var currentPhase = $rootScope.mfrPart.lifeCyclePhase.phase;
                $rootScope.lifeCycleStatus = $rootScope.mfrPart.lifeCyclePhase.phase;
                var defs = $rootScope.mfrPart.mfrPartType.lifecycle.phases;
                defs.sort(function (a, b) {
                    return a.id - b.id;
                });
                var lastPhase = defs[defs.length - 1].phase;
                angular.forEach(defs, function (def) {
                    if (def.phase == currentPhase && lastPhase == def.phase) {
                        phases.push({
                            name: def.phase,
                            finished: true,
                            current: (def.phase == currentPhase)
                        })
                    } else {
                        phases.push({
                            name: def.phase,
                            finished: false,
                            current: (def.phase == currentPhase)
                        })
                    }
                });

                var index = -1;
                for (var i = 0; i < phases.length; i++) {
                    if (phases[i].current == true) {
                        index = i;
                    }
                }

                if (index > 0) {
                    for (i = 0; i < index; i++) {
                        phases[i].finished = true;
                    }
                }

                $rootScope.setLifecyclePhases(phases);
            }

            function showMfrItemParts() {
                var modal = document.getElementById("mfr-part");
                modal.style.display = "block";
                $timeout(function () {
                    var headerHeight = $('.mfr-header').outerHeight();
                    var footerHeight = $('.mfr-footer').outerHeight();
                    var bomContentHeight = $('.mfrPart-content').outerHeight();
                    $(".mfr-content").height(bomContentHeight - headerHeight - footerHeight);
                }, 200)

            }

            vm.hideMfrPartItem = hideMfrPartItem;
            function hideMfrPartItem() {
                var modal = document.getElementById("mfr-part");
                modal.style.display = "none";
            }

            vm.saveItemMfrPart = saveItemMfrPart;

            function saveItemMfrPart() {
                MfrPartsService.saveItemMfrparts(mfId, vm.itemMfrparts).then(
                    function (data) {
                        $rootScope.loadManufacturerParts();
                        var modal = document.getElementById("mfr-part");
                        modal.style.display = "none";
                        loadPartDetails();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var shareItem = parse.html($translate.instant("SHARE_MFR_PART")).html();
            var shareButton = parse.html($translate.instant("SHARE")).html();

            vm.shareMfrPart = shareMfrPart;
            function shareMfrPart() {
                var options = {
                    title: shareItem,
                    template: 'app/desktop/modules/shared/share/shareView.jsp',
                    controller: 'ShareController as shareVm',
                    resolve: 'app/desktop/modules/shared/share/shareController',
                    width: 600,
                    showMask: true,
                    data: {
                        sharedItem: $rootScope.mfrPart,
                        itemsSharedType: 'itemSelection',
                        objectType: "MANUFACTURERPART"
                    },
                    buttons: [
                        {text: shareButton, broadcast: 'app.share.item'}
                    ],
                    callback: function (data) {
                        $rootScope.showSuccessMessage($rootScope.mfrPart.partNumber + " : Shared successfully");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadSubscribe() {
                ItemService.getSubscribeByPerson(mfId, $rootScope.loginPersonDetails.person.id).then(
                    function (data) {
                        vm.subscribe = data;
                        if (vm.subscribe == null || vm.subscribe == "") {
                            $scope.subscribeButtonTitle = subscribeTitle;
                        } else {
                            if (vm.subscribe.subscribe) {
                                $scope.subscribeButtonTitle = unsubscribeTitle;
                            } else {
                                $scope.subscribeButtonTitle = subscribeTitle;
                            }
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.subscribeMfrPart = subscribeMfrPart;

            function subscribeMfrPart() {
                MfrPartsService.subscribePart(mfId).then(
                    function (data) {
                        vm.subscribe = data;
                        if (vm.subscribe.subscribe) {
                            $rootScope.showSuccessMessage(subscribeMsg.format(partTitle));
                            $scope.subscribeButtonTitle = unsubscribeTitle;
                        } else {
                            $rootScope.showSuccessMessage(unsubscribeMsg.format(partTitle));
                            $scope.subscribeButtonTitle = subscribeTitle;
                        }

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadPartDetails();
                if ($window.localStorage.getItem("shared-permission") != undefined && $window.localStorage.getItem("shared-permission") != null) {
                    var sharedPermission = $window.localStorage.getItem("shared-permission");
                    if (sharedPermission != null && sharedPermission != "") {
                        $rootScope.sharedPermission = sharedPermission;
                    }
                }
                if (tabId != null && tabId != undefined) {
                    mfrDetailsTabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.mfrPart.tabactivated', {tabId: tabId});
                    }, 1000)
                }
                //}
            })();
        }
    }
);
