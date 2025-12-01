define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/compliance/declaration/details/tabs/basic/declarationBasicInfoController',
        'app/desktop/modules/compliance/declaration/details/tabs/attributes/declarationAttributesController',
        'app/desktop/modules/compliance/declaration/details/tabs/parts/declarationPartsController',
        'app/desktop/modules/compliance/declaration/details/tabs/specifications/declarationSpecificationsController',
        'app/desktop/modules/compliance/declaration/details/tabs/timeline/declarationTimelineController',
        'app/desktop/modules/compliance/declaration/details/tabs/files/declarationFilesController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/declarationService',
        'app/shared/services/core/supplierService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('DeclarationDetailsController', DeclarationDetailsController);

        function DeclarationDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                              ECOService, $translate, DeclarationService, CommentsService, DialogService, SupplierService) {

            $rootScope.viewInfo.icon = "fa fa-check-square-o";
            $rootScope.viewInfo.showDetails = true;

            var vm = this;
            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];


            vm.declarationId = $stateParams.declarationId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var attributesTitle = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var whereUsed = parsed.html($translate.instant("DETAILS_TAB_WHERE_USED")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            var submitDeclarationTitle = parsed.html($translate.instant("SUBMIT_DECLARATION")).html();
            var submitDeclarationMsg = parsed.html($translate.instant("SUBMIT_DECLARATION_MSG")).html();
            var declarationSubmitted = parsed.html($translate.instant("DECLARATION_SUBMITTED")).html();
            var declarationAccepted = parsed.html($translate.instant("DECLARATION_ACCEPTED")).html();
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            $rootScope.sharedPermission = null;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/compliance/declaration/details/tabs/basic/declarationBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: attributesTitle,
                 template: 'app/desktop/modules/compliance/declaration/details/tabs/attributes/declarationAttributesView.jsp',
                 index: 1,
                 active: true,
                 activated: true
                 }*/,
                parts: {
                    id: 'details.parts',
                    heading: 'Parts',
                    template: 'app/desktop/modules/compliance/declaration/details/tabs/parts/declarationPartsView.jsp',
                    index: 1,
                    active: true,
                    activated: true
                },
                specifications: {
                    id: 'details.specifications',
                    heading: 'Specifications',
                    template: 'app/desktop/modules/compliance/declaration/details/tabs/specifications/declarationSpecificationsView.jsp',
                    index: 2,
                    active: true,
                    activated: true
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/compliance/declaration/details/tabs/files/declarationFilesView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/compliance/declaration/details/tabs/timeline/declarationTimelineView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                }

            };


            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.compliance.declaration.details', {
                    declarationId: vm.declarationId,
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

            function refreshDetails() {
                $scope.$broadcast('app.declaration.tabActivated', {tabId: $rootScope.selectedTab.id});
            }


            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
                for (var t in vm.customTabs) {
                    vm.customTabs[t].active = (vm.customTabs[t].id == tab.id);
                }
            }

            function tabActivated(tabId) {
                $state.transitionTo('app.compliance.declaration.details', {
                    declarationId: vm.declarationId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.declaration.tabActivated', {tabId: tabId});

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

            vm.showExternalUserDeclarations = showExternalUserDeclarations;
            function showExternalUserDeclarations() {
                $state.go('app.home');
                $rootScope.showSharedDeclaration();
            }

            vm.back = back;
            function back() {
                window.history.back();
            }


            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedDeclarationTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            function loadDeclaration() {
                $rootScope.$broadcast("loadPgcObjectFilesCount", {
                    objectId: vm.declarationId,
                    objectType: "PGCOBJECT",
                    heading: vm.tabs.files.heading
                });
            }

            $rootScope.loadDeclarationTabCounts = loadDeclarationTabCounts;
            function loadDeclarationTabCounts() {
                DeclarationService.getDeclarationTabCount(vm.declarationId).then(
                    function (data) {
                        vm.tabCounts = data;
                        var declarationPartsTab = document.getElementById("declarationParts");
                        var declarationSpecificationsTab = document.getElementById("declarationSpecifications");
                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (declarationPartsTab != null) {
                            declarationPartsTab.lastElementChild.innerHTML = vm.tabs.parts.heading +
                                tmplStr.format(vm.tabCounts.parts);
                        }
                        if (declarationSpecificationsTab != null) {
                            declarationSpecificationsTab.lastElementChild.innerHTML = vm.tabs.specifications.heading +
                                tmplStr.format(vm.tabCounts.specifications);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
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
                    $scope.$broadcast('app.declaration.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.declaration.tabActivated', {tabId: 'details.files'});
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('PGCDECLARATION', vm.declarationId).then(
                    function (data) {
                        $rootScope.showComments('PGCDECLARATION', vm.declarationId, data);
                        $rootScope.showTags('PGCDECLARATION', vm.declarationId, vm.declaration.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.submitDeclaration = submitDeclaration;
            function submitDeclaration() {
                var options = {
                    title: submitDeclarationTitle,
                    message: submitDeclarationMsg,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        DeclarationService.submitDeclaration(vm.declarationId).then(
                            function (data) {
                                vm.declaration = data;
                                $rootScope.declaration = vm.declaration;
                                $rootScope.loadDeclarationBasicDetails();
                                $rootScope.showSuccessMessage(declarationSubmitted);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            function loadDeclarationDetails() {
                vm.loading = true;
                if (vm.declarationId != null && vm.declarationId != undefined) {
                    DeclarationService.getDeclaration(vm.declarationId).then(
                        function (data) {
                            vm.declaration = data;
                            $rootScope.declaration = vm.declaration;
                            $rootScope.viewInfo.title = "<div class='item-number'>" +
                                "{0}</div>".format(vm.declaration.number);
                            $rootScope.viewInfo.description = vm.declaration.name;
                            loadCommentsCount();
                            vm.loading = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            vm.generateReport = generateReport;
            function generateReport() {
                $rootScope.showBusyIndicator($('.view-container'));
                DeclarationService.generateComplianceReport(vm.declarationId).then(
                    function (data) {
                        vm.declaration = data;
                        $rootScope.declaration = vm.declaration;
                        $rootScope.loadDeclarationBasicDetails();
                        $rootScope.showSuccessMessage(declarationAccepted);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator()
                    }
                )
            }

            (function () {

                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedDeclarationTab"));
                }

                $window.localStorage.setItem("lastSelectedDeclarationTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.declaration.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.declaration.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                if ($window.localStorage.getItem("shared-permission") != undefined && $window.localStorage.getItem("shared-permission") != null) {
                    var sharedPermission = $window.localStorage.getItem("shared-permission");
                    if (sharedPermission != null && sharedPermission != "") {
                        $rootScope.sharedPermission = sharedPermission;
                    }
                }
                loadDeclarationDetails();
                loadDeclaration();
                loadDeclarationTabCounts();
            })();

        }
    }
);
