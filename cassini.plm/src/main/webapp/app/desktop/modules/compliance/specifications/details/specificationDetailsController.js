define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/compliance/specifications/details/tabs/basic/specificationBasicInfoController',
        'app/desktop/modules/compliance/specifications/details/tabs/attributes/specificationAttributesController',
        'app/desktop/modules/compliance/specifications/details/tabs/timeline/specificationTimelineController',
        'app/desktop/modules/compliance/specifications/details/tabs/files/specificationFilesController',
        'app/desktop/modules/compliance/specifications/details/tabs/substance/specificationSubstancesController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/specificationService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('SpecificationDetailsController', SpecificationDetailsController);

        function SpecificationDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                                ECOService, $translate, SpecificationService, CommentsService) {

            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.icon = "fa fa-pencil";
            $rootScope.viewInfo.showDetails = true;
            var vm = this;
            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];


            vm.specificationId = $stateParams.specificationId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var attributesTitle = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var whereUsed = parsed.html($translate.instant("DETAILS_TAB_WHERE_USED")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/compliance/specifications/details/tabs/basic/specificationBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: attributesTitle,
                 template: 'app/desktop/modules/compliance/specifications/details/tabs/attributes/specificationAttributesView.jsp',
                 index: 1,
                 active: true,
                 activated: true
                 }*/,
                substances: {
                    id: 'details.substances',
                    heading: 'Substances',
                    template: 'app/desktop/modules/compliance/specifications/details/tabs/substance/specificationSubstancesView.jsp',
                    index: 1,
                    active: true,
                    activated: true
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/compliance/specifications/details/tabs/files/specificationFilesView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/compliance/specifications/details/tabs/timeline/specificationTimelineView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                }

            };


            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.compliance.specification.details', {
                    specificationId: vm.specificationId,
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
                $scope.$broadcast('app.specification.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                $state.transitionTo('app.compliance.specification.details', {
                    specificationId: vm.specificationId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.specification.tabActivated', {tabId: tabId});

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


            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedSpecificationTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            function loadSpecification() {
                $rootScope.$broadcast("loadPgcObjectFilesCount", {
                    objectId: vm.specificationId,
                    objectType: "PGCOBJECT",
                    heading: vm.tabs.files.heading
                });
                if (vm.specificationId != null && vm.specificationId != undefined) {
                    SpecificationService.getSpecification(vm.specificationId).then(
                        function (data) {
                            vm.specification = data;
                            $rootScope.specification = vm.specification;
                           // $rootScope.viewInfo.title = $translate.instant("SPECIFICATION_DETAILS");
                          //  $rootScope.viewInfo.description = vm.specification.number + " , " + vm.specification.name;
                          $rootScope.viewInfo.title = "<div class='item-number'>" +
                          "{0}</div>".
                              format(vm.specification.number);
                      $rootScope.viewInfo.description = vm.specificationId.name;
                            loadCommentsCount();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }

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
                    $scope.$broadcast('app.specification.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.specification.tabActivated', {tabId: 'details.files'});
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('PGCSPECIFICATION', vm.specificationId).then(
                    function (data) {
                        $rootScope.showComments('PGCSPECIFICATION', vm.specificationId, data);
                        $rootScope.showTags('PGCSPECIFICATION', vm.specificationId, vm.specification.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $rootScope.loadSpecificationTabCounts = loadSpecificationTabCounts;
            function loadSpecificationTabCounts() {
                SpecificationService.getSpecificationTabCount(vm.specificationId).then(
                    function (data) {
                        vm.tabCounts = data;
                        var substancesTab = document.getElementById("Substances");
                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (substancesTab != null) {
                            substancesTab.lastElementChild.innerHTML = vm.tabs.substances.heading +
                                tmplStr.format(vm.tabCounts.substances);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {

                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedSpecificationTab"));
                }

                $window.localStorage.setItem("lastSelectedSpecificationTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.specification.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.specification.tabActivated', {tabId: tabId});
                    }, 1000)
                }

                loadSpecificationTabCounts();
                loadSpecification();
            })();

        }
    }
);
