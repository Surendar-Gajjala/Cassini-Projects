define(
    [
        'app/desktop/modules/mfr/mfr.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mfr/supplier/details/tabs/basic/supplierBasicInfoController',
        'app/desktop/modules/mfr/supplier/details/tabs/attributes/supplierAttributesController',
        'app/desktop/modules/mfr/supplier/details/tabs/contacts/supplierContactsController',
        'app/desktop/modules/mfr/supplier/details/tabs/mfrparts/supplierMfrPartsController',
        'app/desktop/modules/mfr/supplier/details/tabs/timeline/supplierTimelineController',
        'app/desktop/modules/mfr/supplier/details/tabs/files/supplierFilesController',
        'app/desktop/modules/mfr/supplier/details/tabs/ppap/supplierPPAPController',
        'app/desktop/modules/mfr/supplier/details/tabs/spr/supplierSPRController',
        'app/desktop/modules/mfr/supplier/details/tabs/cpi/supplierCPIController',
        'app/desktop/modules/mfr/supplier/details/tabs/fmChangeSupplier/supplier4mChangeController',
        'app/desktop/modules/mfr/supplier/details/tabs/supplierAudit/supplierAuditController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/supplierService',
        'app/shared/services/core/objectFileService',
        'app/shared/services/core/itemService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('SupplierDetailsController', SupplierDetailsController);

        function SupplierDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                           ECOService, $translate, CommentsService, ObjectFileService, SupplierService, DialogService, ItemService) {
            var vm = this;

            $rootScope.viewInfo.title = "";

            $rootScope.viewInfo.icon = "fa flaticon-office42";
            $rootScope.viewInfo.title = $translate.instant("SUPPLIER_DETAILS");
            $rootScope.viewInfo.showDetails = true;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];


            vm.supplierId = $stateParams.supplierId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var attributesTitle = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            var contactsHeading = parsed.html($translate.instant("CONTACTS")).html();
            var partsHeading = parsed.html($translate.instant("PARTS")).html();
            var lastSelectedTab = null;
            var subscribeMsg = parsed.html($translate.instant("SUBSCRIBE_MSG")).html();
            var subscribeTitle = parsed.html($translate.instant("SUBSCRIBE_TITLE")).html();
            var unsubscribeTitle = parsed.html($translate.instant("UN_SUBSCRIBE_TITLE")).html();
            var unsubscribeMsg = parsed.html($translate.instant("UN_SUBSCRIBE_MSG")).html();
            var supplierTitle = parsed.html($translate.instant("SUPPLIER")).html();
            vm.detailsShareTitle = parsed.html($translate.instant("SHARE")).html();
            vm.refreshTitle = parsed.html($translate.instant("CLICK_TO_REFRESH")).html();
            vm.shareSupplier = shareSupplier;
            vm.active = 0;
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            $rootScope.sharedPermission = null;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/mfr/supplier/details/tabs/basic/supplierBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: attributesTitle,
                 template: 'app/desktop/modules/mfr/supplier/details/tabs/attributes/supplierAttributesView.jsp',
                 index: 1,
                 active: true,
                 activated: true
                 }*/,
                contacts: {
                    id: 'details.contacts',
                    heading: contactsHeading,
                    template: 'app/desktop/modules/mfr/supplier/details/tabs/contacts/supplierContactsView.jsp',
                    index: 1,
                    active: true,
                    activated: true
                },
                parts: {
                    id: 'details.parts',
                    heading: partsHeading,
                    template: 'app/desktop/modules/mfr/supplier/details/tabs/mfrparts/supplierMfrPartsView.jsp',
                    index: 2,
                    active: true,
                    activated: true
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mfr/supplier/details/tabs/files/supplierFilesView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                ppap: {
                    id: 'details.ppap',
                    heading: "PPAP",
                    template: 'app/desktop/modules/mfr/supplier/details/tabs/ppap/supplierPPAPView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                },
                supplierAudit: {
                    id: 'details.supplierAudit',
                    heading: "Supplier Audit",
                    template: 'app/desktop/modules/mfr/supplier/details/tabs/supplierAudit/supplierAuditView.jsp',
                    index: 5,
                    active: false,
                    activated: false
                },
                spr: {
                    id: 'details.spr',
                    heading: "Supplier Performance Ratings",
                    template: 'app/desktop/modules/mfr/supplier/details/tabs/spr/supplierSPRView.jsp',
                    index: 6,
                    active: false,
                    activated: false
                },
                cpi: {
                    id: 'details.cpi',
                    heading: "CPI Form",
                    template: 'app/desktop/modules/mfr/supplier/details/tabs/cpi/supplierCPIView.jsp',
                    index: 7,
                    active: false,
                    activated: false
                },
                fmChangeSupplier: {
                    id: 'details.4mChangeSupplier',
                    heading: "4M Change",
                    template: 'app/desktop/modules/mfr/supplier/details/tabs/fmChangeSupplier/supplier4mChangeView.jsp',
                    index: 8,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mfr/supplier/details/tabs/timeline/supplierTimelineView.jsp',
                    index: 9,
                    active: false,
                    activated: false
                }
            };
            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mfr.supplier.details', {
                    supplierId: vm.supplierId,
                    tab: 'details.basic'
                }, {notify: false});
                vm.active = 0;
            }
            else {
                var tab = getTabById(tabId);
                if (tab != null) {
                    //vm.active = tab.index;
                }
            }
            vm.showExternalUserSuppliers = showExternalUserSuppliers;
            function showExternalUserSuppliers() {
                $state.go('app.home');
                $rootScope.sharedSupplier();

            }

            vm.tabActivated = tabActivated;

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
                    JSON.parse($window.localStorage.getItem("lastSelectedSupplierTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function tabActivated(tabId) {
                $state.transitionTo('app.mfr.supplier.details', {
                    supplierId: vm.supplierId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.supplier.tabActivated', {tabId: tabId});

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

            vm.back = back;
            function back() {
                window.history.back();
            }


            vm.supplierFileCounts = [];
            $rootScope.loadSupplierFileCounts = loadSupplierFileCounts;
            function loadSupplierFileCounts() {
                vm.supplierFileCounts = [];
                SupplierService.getSupplierFileCounts(vm.supplierId).then(
                    function (data) {
                        vm.supplierFileCounts = data;
                        var filesTab = document.getElementById("files");
                        var contactTab = document.getElementById("contacts");
                        var partsTab = document.getElementById("partsCount");
                        var ppapTab = document.getElementById("ppaps");
                        var supplierAuditTab = document.getElementById("supplierAudit1");
                        var supplierPerformanceRatings = document.getElementById("supplierPerformanceRatings");
                        var cpiForm = document.getElementById("cpiForm");
                        var fmChangeSupplier = document.getElementById("4mChangeSupplier");
                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (filesTab != null) {
                            filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                                tmplStr.format(vm.supplierFileCounts.files);
                        }
                        if (contactTab != null) {
                            contactTab.lastElementChild.innerHTML = vm.tabs.contacts.heading +
                                tmplStr.format(vm.supplierFileCounts.contacts);
                        }
                        if (partsTab != null) {
                            partsTab.lastElementChild.innerHTML = vm.tabs.parts.heading +
                                tmplStr.format(vm.supplierFileCounts.parts);
                        }
                        if (ppapTab != null) {
                            ppapTab.lastElementChild.innerHTML = vm.tabs.ppap.heading +
                                tmplStr.format(vm.supplierFileCounts.ppaps);
                        }
                        if (supplierAuditTab != null) {
                            supplierAuditTab.lastElementChild.innerHTML = vm.tabs.supplierAudit.heading +
                                tmplStr.format(vm.supplierFileCounts.supplierAudits);
                        }
                        if (supplierPerformanceRatings != null) {
                            supplierPerformanceRatings.lastElementChild.innerHTML = vm.tabs.spr.heading +
                                tmplStr.format(vm.supplierFileCounts.sprCount);
                        }
                        if (cpiForm != null) {
                            cpiForm.lastElementChild.innerHTML = vm.tabs.cpi.heading +
                                tmplStr.format(vm.supplierFileCounts.cpiCount);
                        }
                        if (fmChangeSupplier != null) {
                            fmChangeSupplier.lastElementChild.innerHTML = vm.tabs.fmChangeSupplier.heading +
                                tmplStr.format(vm.supplierFileCounts.fmChangeCount);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            vm.freeTextSearch = freeTextSearch;
            $rootScope.freeTextQuerys = null;
            function freeTextSearch(freeText) {
                $rootScope.freeTextQuerys = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    $scope.$broadcast('app.details.files.search', {name: freeText});
                }
                else {
                    $rootScope.freeTextQuerys = null;
                    $scope.$broadcast('app.supplier.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.supplier.tabActivated', {tabId: 'details.files'});
            }

            function loadSupplier() {
                SupplierService.getSupplier(vm.supplierId).then(
                    function (data) {
                        vm.supplier = data;
                        $rootScope.supplier = data;
                        setLifecycles();
                        loadCommentsCount();
                        $timeout(function () {
                            loadSubscribe();
                        }, 1000);
                        $rootScope.viewInfo.title = $translate.instant("SUPPLIER_DETAILS");
                        $rootScope.viewInfo.description = "Name: " + data.name;
                        vm.lastLifecyclePhase = $rootScope.supplier.supplierType.lifecycle.phases[$rootScope.supplier.supplierType.lifecycle.phases.length - 1];
                        vm.firstLifecyclePhase = $rootScope.supplier.supplierType.lifecycle.phases[0];
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadSubscribe() {
                ItemService.getSubscribeByPerson(vm.supplierId, $rootScope.loginPersonDetails.person.id).then(
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

            vm.subscribeSupplier = subscribeSupplier;

            function subscribeSupplier() {
                SupplierService.subscribeSupplier(vm.supplierId).then(
                    function (data) {
                        vm.subscribe = data;
                        if (vm.subscribe.subscribe) {
                            $rootScope.showSuccessMessage(subscribeMsg.format(supplierTitle));
                            $scope.subscribeButtonTitle = unsubscribeTitle;
                        } else {
                            $rootScope.showSuccessMessage(unsubscribeMsg.format(supplierTitle));
                            $scope.subscribeButtonTitle = subscribeTitle;
                        }

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            function setLifecycles() {
                var phases = [];
                var currentPhase = $rootScope.supplier.lifeCyclePhase.phase;
                $rootScope.lifeCycleStatus = $rootScope.supplier.lifeCyclePhase.phase;
                var defs = $rootScope.supplier.supplierType.lifecycle.phases;
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


            var confirmation = parsed.html($translate.instant("CONFIRMATION")).html();
            var doYouWantToPromotSupplier = parsed.html($translate.instant("DO_YOU_WANT_TO_PROMOTE_SUPPLIER_STATUS")).html();
            var doYouWantToDemoteSupplier = parsed.html($translate.instant("DO_YOU_WANT_TO_DEMOTE_SUPPLIER_STATUS")).html();
            var yesTitle = parsed.html($translate.instant("YES")).html();
            var noTitle = parsed.html($translate.instant("NO")).html();
            var promoteSuccessMsg = parsed.html($translate.instant("SUPPLIER_PROMOTE_SUCCESS_MSG")).html();
            var demoteSuccessMsg = parsed.html($translate.instant("SUPPLIER_DEMOTE_SUCCESS_MSG")).html();


            $rootScope.promoteSupplier = promoteSupplier;
            function promoteSupplier() {
                var options = {
                    title: confirmation,
                    message: doYouWantToPromotSupplier,
                    okButtonClass: 'btn-danger',
                    okButtonText: yesTitle,
                    cancelButtonText: noTitle
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        SupplierService.promoteSupplier(vm.supplierId, $rootScope.supplier).then(
                            function (data) {
                                loadSupplier();
                                $rootScope.showSuccessMessage(promoteSuccessMsg);
                                $rootScope.hideBusyIndicator();
                                $rootScope.loadSupplierBasicDetails();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            $rootScope.demoteSupplier = demoteSupplier;
            function demoteSupplier() {
                var options = {
                    title: confirmation,
                    message: doYouWantToDemoteSupplier,
                    okButtonClass: 'btn-danger',
                    okButtonText: yesTitle,
                    cancelButtonText: noTitle
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        SupplierService.demoteSupplier(vm.supplierId, $rootScope.supplier).then(
                            function (data) {
                                loadSupplier();
                                $rootScope.showSuccessMessage(demoteSuccessMsg);
                                $rootScope.hideBusyIndicator();
                                $rootScope.loadSupplierBasicDetails();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('MFRSUPPLIER', vm.supplierId).then(
                    function (data) {
                        $rootScope.showComments('MFRSUPPLIER', vm.supplierId, data);
                        $rootScope.showTags('MFRSUPPLIER', vm.supplierId, vm.supplier.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            var parse = angular.element("<div></div>");
            var shareButton = parse.html($translate.instant("SHARE")).html();
            var shareSupplierTitle = parse.html($translate.instant("SHARE_SUPPLIER")).html();

            function shareSupplier() {
                var options = {
                    title: shareSupplierTitle,
                    template: 'app/desktop/modules/shared/share/shareView.jsp',
                    controller: 'ShareController as shareVm',
                    resolve: 'app/desktop/modules/shared/share/shareController',
                    width: 600,
                    showMask: true,
                    data: {
                        sharedItem: $rootScope.supplier,
                        itemsSharedType: 'itemSelection',
                        objectType: "MFRSUPPLIER"
                    },
                    buttons: [
                        {text: shareButton, broadcast: 'app.share.item'}
                    ],
                    callback: function (data) {
                        $rootScope.showSuccessMessage($rootScope.supplier.name + " : Shared successfully");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.refreshDetails = refreshDetails;

            function refreshDetails() {
                $scope.$broadcast('app.supplier.tabActivated', {tabId: $rootScope.selectedTab.id});
            }

            (function () {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedSupplierTab"));

                }
                if ($window.localStorage.getItem("shared-permission") != undefined && $window.localStorage.getItem("shared-permission") != null) {
                    var sharedPermission = $window.localStorage.getItem("shared-permission");
                    if (sharedPermission != null && sharedPermission != "") {
                        $rootScope.sharedPermission = sharedPermission;
                    }
                }

                $window.localStorage.setItem("lastSelectedSupplierTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.supplier.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.supplier.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadSupplier();
                loadSupplierFileCounts();

            })();

        }
    }
);
