/**
 * Created by Nageshreddy on 25-06-2021.
 */
define(
    [
        'app/desktop/modules/pdm/pdm.module'
    ],
    function (module) {
        module.directive('pluginTabs', pluginTabsDirective);

        function pluginTabsDirective($rootScope, $translate, $timeout) {

            return {
                templateUrl: 'app/desktop/directives/plugin-directive/pluginTabsDirectiveView.jsp',
                //restrict: 'E',
                replace: true,
                scope: {
                    tabs: "=",
                    objectValue: "=",
                    objectRevision: "=",
                    tabActivate: "=",
                    tabId: "=",
                    active: "=",
                    customTabs: "="
                },

                link: function ($scope, element, attrs) {

                    function getLastTabIndexOfStandardTabs() {
                        var index = 0;
                        for (var t in $scope.tabs) {
                            if ($scope.tabs.hasOwnProperty(t)) {
                                index = $scope.tabs[t].index;
                            }
                        }

                        return index;
                    }

                    function getTabById(tabId) {
                        var tab = null;
                        for (var t in $scope.tabs) {
                            if ($scope.tabs.hasOwnProperty(t) && $scope.tabs[t].id == tabId) {
                                tab = $scope.tabs[t];
                            }
                        }

                        if (tab == null) {
                            angular.forEach($scope.customTabs, function (customTab) {
                                if (customTab.id === tabId) {
                                    tab = customTab;
                                }
                            });
                        }

                        return tab;
                    }

                    function loadDetailsExtensions() {
                        $scope.customTabs = [];
                        var context = {
                            object: $scope.objectValue,
                            objectRevision: $scope.objectRevision
                        };
                        var plugins = $application.plugins;
                        angular.forEach(plugins, function (plugin) {
                            var extensions = plugin.extensions;
                            if (extensions != null && extensions !== undefined) {
                                var objectDetails = extensions.objectDetails;
                                if (objectDetails != null && objectDetails !== undefined) {
                                    var tabs1 = objectDetails.tabs;
                                    if (tabs1 != null && tabs1 !== undefined && tabs1.length > 0) {
                                        var index = getLastTabIndexOfStandardTabs();
                                        angular.forEach(tabs1, function (tab) {
                                            var show = true;
                                            if (tab.filter != null && tab.filter !== undefined) {
                                                show = jexl.evalSync(tab.filter, context);
                                            }
                                            if (show) {
                                                index = index + 1;
                                                tab.index = index;
                                                tab.active = false;
                                                tab.activated = false;
                                                $scope.customTabs.push(tab);
                                                var actions = tab.tabActionGroups;
                                                if (actions != null && actions !== undefined && actions.length > 0) {
                                                    tab.tabCustomActionGroups = [];
                                                    angular.forEach(actions, function (action) {
                                                        var show = true;
                                                        if (action.filter != null && action.filter !== undefined) {
                                                            show = jexl.evalSync(action.filter, context);
                                                        }
                                                        if (show) {
                                                            tab.tabCustomActionGroups.push(action);
                                                        }
                                                    })
                                                }
                                                var actions1 = tab.tabActions;
                                                if (actions1 != null && actions1 !== undefined && actions1.length > 0) {
                                                    tab.tabCustomActions = [];
                                                    angular.forEach(actions1, function (action) {
                                                        var show = true;
                                                        if (action.filter != null && action.filter !== undefined) {
                                                            show = jexl.evalSync(action.filter, context);
                                                        }
                                                        if (show) {
                                                            tab.tabCustomActions.push(action);
                                                        }
                                                    })
                                                }
                                            }
                                        });

                                        var tab = getTabById($scope.tabId);
                                        if (tab !== null) {
                                            $scope.active = tab.index;
                                        }
                                        else {
                                            console.log("Tab not found: " + $scope.tabId);
                                        }
                                    }
                                }
                            }
                        });
                    }

                    (function () {
                        $scope.$on('app.object.plugins.tabActivated', function (event, data) {
                            loadDetailsExtensions();
                        });
                    })();
                }
            }

        }
    }
);