/**
 * Created by Nageshreddy on 25-06-2021.
 */
define(
    [
        'app/desktop/modules/pdm/pdm.module'
    ],
    function (module) {
        module.directive('pluginTableActions', pluginTableActionsDirective);

        function pluginTableActionsDirective($rootScope, $injector, $timeout) {

            return {
                templateUrl: 'app/desktop/directives/plugin-directive/pluginTableActionDirectiveView.jsp',
                replace: true,
                scope: {
                    objectValue: "=",
                    objectRevision: "=",
                    customTableActions: "=",
                    context: "@"
                },
                link: function ($scope, element, attrs) {
                    $rootScope.performCustomTableAction = performCustomTableAction;

                    function loadTableActionsExtensions() {
                        $scope.customTableActions = [];
                        var context = {
                            object: $scope.objectValue,
                            objectRevision: $scope.objectRevision
                        };
                        var plugins = $application.plugins;
                        angular.forEach(plugins, function (plugin) {
                            var extensions = plugin.extensions;
                            if (extensions != null && extensions !== undefined) {
                                var actions = extensions.objectDetails.tableActions;
                                if (actions != null && actions !== undefined && actions.length > 0) {
                                    angular.forEach(actions, function (action) {
                                        var show = true;
                                        if (action.context == $scope.context) {
                                            if (action.filter != null && action.filter !== undefined) show = jexl.evalSync(action.filter, context);
                                            if (show) {
                                                $scope.customTableActions.push(action);
                                                $rootScope.pluginTableObject = null;
                                                $rootScope.pluginTableObjectRevision = null;
                                                if($scope.context.includes("object.")) {
                                                    $rootScope.pluginTableObject = $scope.objectValue;
                                                    $rootScope.pluginTableObjectRevision = $scope.objectRevision;
                                                }
                                            }
                                        }
                                    })
                                }
                            }
                        });
                    }

                    function performCustomTableAction(action) {
                        var service = $injector.get(action.service);
                        if (service != null && service !== undefined) {
                            var method = service[action.method];
                            if (method != null && method !== undefined && typeof method === "function") {
                                if ($scope.objectRevision != null && $scope.objectRevision != undefined) method($scope.objectValue, $scope.objectRevision);
                                else method($scope.objectValue);
                            }
                        }
                    }

                    (function () {
                        loadTableActionsExtensions();
                    })();
                }
            }

        }
    }
);