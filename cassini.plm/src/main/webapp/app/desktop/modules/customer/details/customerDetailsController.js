define(
    [
        'app/desktop/modules/customer/customer.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/customer/details/tabs/basic/customerBasicInfoController',
        'app/desktop/modules/customer/details/tabs/problemReport/customerProblemReportController',
        'app/desktop/modules/customer/details/tabs/timeline/customerTimelineController',
        'app/desktop/modules/customer/details/tabs/files/customerFilesController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/customerSupplierService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('CustomerDetailsController', CustomerDetailsController);

        function CustomerDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                           CustomerSupplierService, $translate, CommentsService) {
            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.customerId = $stateParams.customerId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var problemReportTitle = parsed.html($translate.instant("PROBLEMREPORTS")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            var lastSelectedTab = null;
            vm.active = 0;
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/customer/details/tabs/basic/customerBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                problemReport: {
                    id: 'details.problemReport',
                    heading: problemReportTitle,
                    template: 'app/desktop/modules/customer/details/tabs/problemReport/customerProblemReportView.jsp',
                    index: 1,
                    active: true,
                    activated: true
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/customer/details/tabs/files/customerFilesView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/customer/details/tabs/timeline/customerTimelineView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                }

            };
            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.customers.details', {
                    customerId: vm.customerId,
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

            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;

            function refreshDetails() {
                $scope.$broadcast('app.customer.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                    $scope.$broadcast('app.customer.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.customer.tabActivated', {tabId: 'details.files'});
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
                    JSON.parse($window.localStorage.getItem("lastSelectedCustomerTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function tabActivated(tabId) {
                $state.transitionTo('app.customers.details', {
                    customerId: vm.customerId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.customer.tabActivated', {tabId: tabId});

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

            function loadCustomer() {
                CustomerSupplierService.getCustomer(vm.customerId).then(
                    function (data) {
                        vm.customer = data;
                        $rootScope.customer = vm.customer;
                        $rootScope.viewInfo.title = parsed.html($translate.instant("CUSTOMER_DETAILS")).html();
                        $rootScope.viewInfo.description = vm.customer.name;
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('CUSTOMER', vm.customerId).then(
                    function (data) {
                        $rootScope.showComments('CUSTOMER', vm.customerId, data);
                        $rootScope.showTags('CUSTOMER', vm.customerId, vm.customer.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $rootScope.loadCustomerTabCounts = loadCustomerTabCounts;
            function loadCustomerTabCounts() {
                CustomerSupplierService.getCustomerTabCount(vm.customerId).then(
                    function (data) {
                        vm.tabCounts = data;
                        var customerPrTab = document.getElementById("prs");
                        var customerFileTab = document.getElementById("files");
                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (customerPrTab != null) {
                            customerPrTab.lastElementChild.innerHTML = vm.tabs.problemReport.heading +
                                tmplStr.format(vm.tabCounts.problemReports);
                        }
                        if (customerFileTab != null) {
                            customerFileTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                                tmplStr.format(vm.tabCounts.itemFiles);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedCustomerTab"));
                }

                $window.localStorage.setItem("lastSelectedCustomerTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.customer.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.customer.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadCustomer();
                loadCustomerTabCounts();
            })();

        }
    }
);
