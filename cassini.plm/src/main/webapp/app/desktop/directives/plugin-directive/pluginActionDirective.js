/**
 * Created by Nageshreddy on 25-06-2021.
 */
define(
    [
        'app/desktop/modules/pdm/pdm.module'
    ],
    function (module) {
        module.directive('pluginActions', pluginActionsDirective);

        function pluginActionsDirective($rootScope, $injector, $timeout) {

            return {
                templateUrl: 'app/desktop/directives/plugin-directive/pluginActionDirectiveView.jsp',
                replace: true,
                scope: {
                    objectValue: "=",
                    objectRevision: "=",
                    customActions: "=",
                    customActionGroups: "=",
                    tabCustomActions: "=",
                    tabCustomActionGroups: "=",
                    tabId: "="
                },
                link: function ($scope, element, attrs) {
                    $scope.performCustomAction = performCustomAction;

                    function loadActionsExtensions() {
                        $scope.customActions = [];
                        $scope.customActionGroups = [];
                        var context = {
                            object: $scope.objectValue,
                            objectRevision: $scope.objectRevision,
                            tabId: $scope.tabId
                        };
                        var plugins = $application.plugins;
                        angular.forEach(plugins, function (plugin) {
                            var extensions = plugin.extensions;
                            if (extensions != null && extensions !== undefined) {
                                var actions = extensions.objectDetails.actionGroups;
                                    if (actions != null && actions !== undefined && actions.length > 0) {
                                        angular.forEach(actions, function (action) {
                                            var show = true;
                                            if (action.filter != null && action.filter !== undefined) {
                                                show = jexl.evalSync(action.filter, context);
                                            }
                                            if (show) {
                                                $scope.customActionGroups.push(action);
                                            }
                                        })
                                    }
                                var actions1 = extensions.objectDetails.actions;
                                if (actions1 != null && actions1 !== undefined && actions1.length > 0) {
                                    angular.forEach(actions1, function (action) {
                                        var show = true;
                                        if (action.filter != null && action.filter !== undefined) {
                                            show = jexl.evalSync(action.filter, context);
                                        }
                                        if (show) {
                                            $scope.customActions.push(action);
                                        }
                                    })
                                }
                            }
                        });
                    }

                    function performCustomAction(action) {
                        var service = $injector.get(action.service);
                        if (service != null && service !== undefined) {
                            var method = service[action.method];
                            if (method != null && method !== undefined && typeof method === "function") {
                                if($scope.objectRevision != null && $scope.objectRevision != undefined) method($scope.objectValue, $scope.objectRevision);
                                else method($scope.objectValue);
                            }
                        }
                    }

                    (function () {
                        $timeout(function () {
                            loadActionsExtensions();
                        }, 2000);
                    })();
                }
            }

        }
    }
);