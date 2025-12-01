define(['app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mfr/details/tabs/basic/mfrBasicController',
        'app/desktop/modules/mfr/details/tabs/parts/mfrPartsController',
        'app/desktop/modules/mfr/details/tabs/attributes/mfrAttributesController',
        'app/desktop/modules/mfr/details/tabs/files/mfrFilesController',
        'app/desktop/modules/mfr/details/tabs/workflow/manufacturerWorkflowController',
        'app/desktop/modules/mfr/details/tabs/timelineHistory/mfrTimelineHistoryController',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'
    ],
    function (module) {
        module.controller('MfrDetailsController', MfrDetailsController);

        function MfrDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                      CommonService, MfrService, MfrPartsService, DialogService, $uibModal, $translate, CommentsService) {

            $rootScope.viewInfo.icon = "fa fa-industry";
            $rootScope.viewInfo.title = $translate.instant("MFR_DETAILS_TITLE");
            $rootScope.viewInfo.showDetails = true;

            var vm = this;
            vm.mfrId = $stateParams.manufacturerId;
            vm.mfrDetailsTabActivated = mfrDetailsTabActivated;
            vm.showNewManufacturePart = showNewManufacturePart;
            vm.manufactureparts = [];
            vm.downloadFilesAsZip = downloadFilesAsZip;
            vm.onAddMfrFiles = onAddMfrFiles;
            vm.updateManufacture = updateManufacture;
            vm.freeTextSearch = freeTextSearch;
            vm.freeTextSearchFile = freeTextSearchFile;
            vm.resetPage = resetPage;
            $rootScope.showSearch = false;
            $rootScope.searchModeType = false;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];

            var parsed = angular.element("<div></div>");
            vm.detailsShareTitle = parsed.html($translate.instant("DETAILS_SHARE_TITLE")).html();
            vm.refreshTitle = parsed.html($translate.instant("CLICK_TO_REFRESH")).html();
            var parts = parsed.html($translate.instant("MANUFACTURER_DETAILS_TAB_PARTS")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            vm.crateParts = parsed.html($translate.instant("CREATE_PART")).html();
            vm.savedSearchItems = parsed.html($translate.instant("ALL_VIEW_SAVED_SEARCHES")).html();
            vm.saveSearchMfrPart = parsed.html($translate.instant("SAVED_SEARCH_MFR_PART")).html();
            vm.partAttributesTitle = parsed.html($translate.instant("MFR_PART_ATTRIBUTES")).html();
            vm.addWorkflowTitle = parsed.html($translate.instant("ADD_WORKFLOW")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();

            $rootScope.showCopyMfrFilesToClipBoard = false;
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            var session = JSON.parse(localStorage.getItem('local_storage_login'));
            $rootScope.loginPersonDetails = session.login;
            vm.external = $rootScope.loginPersonDetails;
            $rootScope.external = $rootScope.loginPersonDetails;
            $rootScope.sharedPermission = null;

            var lastSelectedTab = null;
            vm.active = 0;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/mfr/details/tabs/basic/mfrBasicView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: 'Attributes',
                 template: 'app/desktop/modules/mfr/details/tabs/attributes/mfrAttributesView.jsp',
                 index: 1,
                 active: false,
                 activated: false
                 }*/,
                parts: {
                    id: 'details.parts',
                    heading: parts,
                    template: 'app/desktop/modules/mfr/details/tabs/parts/mfrPartsView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mfr/details/tabs/files/mfrFilesView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: 'Workflow',
                    template: 'app/desktop/modules/mfr/details/tabs/workflow/manufacturerWorkflowView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mfr/details/tabs/timelineHistory/mfrTimelineHistoryView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                }
            };

            vm.refreshDetails = refreshDetails;

            function refreshDetails() {
                $scope.$broadcast('app.mfr.tabactivated', {tabId: $rootScope.selectedTab.id});
            }


            vm.showExternalUserMfrs = showExternalUserMfrs;
            function showExternalUserMfrs() {
                $state.go('app.home');
                $rootScope.sharedMfr();
            }

            vm.mfrDetailsTabActivated = mfrDetailsTabActivated;
            vm.onAddMfrParts = onAddMfrParts;
            vm.back = back;

            function back() {
                window.history.back();
            }

            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            vm.addWorkflow = addWorkflow;
            function addWorkflow() {
                $scope.$broadcast('app.add.workflow');
            }

            function resetPage() {
                $rootScope.searchModeType = false;
            }

            function freeTextSearch(freeText) {
                $scope.$broadcast('app.mfr.freetextsearchpart', freeText);
            }

            $rootScope.freeTextQuerys = null;
            function freeTextSearchFile(freeText) {
                $rootScope.freeTextQuerys = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    $scope.$broadcast('app.details.files.search', {name: freeText});
                }
                else {
                    $rootScope.freeTextQuerys = null;
                    $scope.$broadcast('app.mfr.tabactivated', {tabId: 'details.files'});
                    $rootScope.searchModeType = false;
                }
            }

            function updateManufacture() {
                $scope.$broadcast('app.Manufacturer.update');
            }

            function showNewManufacturePart() {
                $rootScope.$broadcast('app.mfr.addpart')
            }

            function onAddMfrFiles() {
                $scope.$broadcast('app.mfr.addfiles');
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
                for (var t in vm.customTabs) {
                    vm.customTabs[t].active = (vm.customTabs[t].id == tab.id);
                }
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedMfrTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            var tabId = $stateParams.tab;

            function mfrDetailsTabActivated(tabId) {
                $state.transitionTo('app.mfr.details', {
                    manufacturerId: vm.mfrId,
                    tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    tab.activated = true;
                    $rootScope.selectedTab = tab;
                    $scope.$broadcast('app.mfr.tabactivated', {tabId: tabId});

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

            function onAddMfrParts() {
                $scope.$broadcast('app.mfr.addparts');

            }

            $rootScope.loadMfr = loadMfr;
            function loadMfr() {
                vm.loading = true;
                MfrService.getManufacturer(vm.mfrId).then(
                    function (data) {
                        vm.mfr = data;
                        $rootScope.mfr = data;
                        $rootScope.viewInfo.title = $translate.instant("MFR_DETAILS_TITLE");
                        $rootScope.viewInfo.description = "Name: " + data.name;
                        loadMfrCounts();
                        setLifecycles();
                        loadCommentsCount();
                        vm.lastLifecyclePhase = $rootScope.mfr.mfrType.lifecycle.phases[$rootScope.mfr.mfrType.lifecycle.phases.length - 1];
                        vm.firstLifecyclePhase = $rootScope.mfr.mfrType.lifecycle.phases[0];
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.loadMfrCounts = loadMfrCounts;
            function loadMfrCounts() {
                MfrService.getMfrCounts(vm.mfrId).then(
                    function (data) {
                        vm.mfrCounts = data;

                        var filesTab = document.getElementById("files");
                        var partsTab = document.getElementById("parts1");

                        partsTab.lastElementChild.innerHTML = vm.tabs.parts.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.mfrCounts.parts);
                        filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.mfrCounts.files);

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('MANUFACTURER', vm.mfrId).then(
                    function (data) {
                        $rootScope.showComments('MANUFACTURER', vm.mfrId, data);
                        $rootScope.showTags('MANUFACTURER', vm.mfrId, vm.mfr.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function downloadFilesAsZip() {
                $rootScope.showBusyIndicator($('.view-container'));
                var url = "{0}//{1}/api/plm/mfr/{2}/files/zip".
                    format(window.location.protocol, window.location.host, vm.mfrId);

                //launchUrl(url);
                window.open(url);
                $rootScope.hideBusyIndicator();
            }

            var confirmation = parsed.html($translate.instant("CONFIRMATION")).html();
            var doYouWantToPromoteMfr = parsed.html($translate.instant("DO_YOU_WANT_TO_PROMOTE_MFR_STATUS")).html();
            var doYouWantToDemoteMfr = parsed.html($translate.instant("DO_YOU_WANT_TO_DEMOTE_MFR_STATUS")).html();
            var yesTitle = parsed.html($translate.instant("YES")).html();
            var noTitle = parsed.html($translate.instant("NO")).html();
            var promoteSuccessMsg = parsed.html($translate.instant("MFR_PROMOTE_SUCCESS_MSG")).html();
            var demoteSuccessMsg = parsed.html($translate.instant("MFR_DEMOTE_SUCCESS_MSG")).html();

            $rootScope.promoteMfr = promoteMfr;
            function promoteMfr() {
                var options = {
                    title: confirmation,
                    message: doYouWantToPromoteMfr,
                    okButtonClass: 'btn-danger',
                    okButtonText: yesTitle,
                    cancelButtonText: noTitle
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        MfrService.promoteManufacturer(vm.mfrId, $rootScope.mfr).then(
                            function (data) {
                                loadMfr();
                                $rootScope.showSuccessMessage(promoteSuccessMsg);
                                $rootScope.hideBusyIndicator();
                                $rootScope.loadManufacturer();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            $rootScope.demoteMfr = demoteMfr;
            function demoteMfr() {
                var options = {
                    title: confirmation,
                    message: doYouWantToDemoteMfr,
                    okButtonClass: 'btn-danger',
                    okButtonText: yesTitle,
                    cancelButtonText: noTitle
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        MfrService.demoteManufacturer(vm.mfrId, $rootScope.mfr).then(
                            function (data) {
                                loadMfr();
                                $rootScope.showSuccessMessage(demoteSuccessMsg);
                                $rootScope.hideBusyIndicator();
                                $rootScope.loadManufacturer();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            function setLifecycles() {
                var phases = [];
                var currentPhase = $rootScope.mfr.lifeCyclePhase.phase;
                $rootScope.lifeCycleStatus = $rootScope.mfr.lifeCyclePhase.phase;
                var defs = $rootScope.mfr.mfrType.lifecycle.phases;
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

            var shareItem = parsed.html($translate.instant("SHARE_MFR")).html();
            var shareButton = parsed.html($translate.instant("SHARE")).html();

            vm.shareMfr = shareMfr;
            function shareMfr() {
                var options = {
                    title: shareItem,
                    template: 'app/desktop/modules/shared/share/shareView.jsp',
                    controller: 'ShareController as shareVm',
                    resolve: 'app/desktop/modules/shared/share/shareController',
                    width: 600,
                    showMask: true,
                    data: {
                        sharedItem: $rootScope.mfr,
                        itemsSharedType: 'itemSelection',
                        objectType: "MANUFACTURER"
                    },
                    buttons: [
                        {text: shareButton, broadcast: 'app.share.item'}
                    ],
                    callback: function (data) {
                        $rootScope.showSuccessMessage($rootScope.mfr.name + " : Shared successfully");
                    }
                };

                $rootScope.showSidePanel(options);
            }


            (function () {
                //if ($application.homeLoaded == true) {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedMfrTab"));
                }
                if ($window.localStorage.getItem("shared-permission") != undefined && $window.localStorage.getItem("shared-permission") != null) {
                    var sharedPermission = $window.localStorage.getItem("shared-permission");
                    if (sharedPermission != null && sharedPermission != "") {
                        $rootScope.sharedPermission = sharedPermission;
                    }
                }
                $window.localStorage.setItem("lastSelectedMfrTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    mfrDetailsTabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.mfr.tabactivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    mfrDetailsTabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.mfr.tabactivated', {tabId: tabId});
                    }, 1000)
                }
                loadMfr();
                //}
            })();
        }
    }
);
