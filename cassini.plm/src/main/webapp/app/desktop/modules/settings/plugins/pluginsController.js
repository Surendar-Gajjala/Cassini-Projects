define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/shared/services/core/pluginService'
    ],
    function (module) {
        module.controller('PluginsController', PluginsController);

        function PluginsController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies, $translate, $application,
                                   PluginService) {
            var vm = this;

            vm.loading = true;
            vm.plugins = [];
            vm.enablePlugin = enablePlugin;
            vm.disablePlugin = disablePlugin;

            function loadPlugins() {
                $rootScope.showBusyIndicator();
                vm.plugins = [];
                PluginService.getPlugins().then(
                    function(data) {
                        angular.forEach(data, function(plugin) {
                            if(plugin.accessPrivate) {
                                if(plugin.accessTenants.indexOf($application.session.tenantId) != -1) {
                                    vm.plugins.push(plugin);
                                }
                            }
                            else {
                                vm.plugins.push(plugin);
                            }
                        });

                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    },
                    function(error) {
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function enablePlugin(plugin) {
                PluginService.enablePlugin(plugin).then(
                    function(data) {
                        $rootScope.showSuccessMessage("Plugin '{0}' has been enabled. Please logout and log back in for this to take effect.".format(plugin.name));
                        plugin.enabled = true;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function disablePlugin(plugin) {
                PluginService.disablePlugin(plugin).then(
                    function(data) {
                        $rootScope.showSuccessMessage("Plugin '{0}' has been disabled.".format(plugin.name));
                        plugin.enabled = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadPlugins();
            })();
        }
    }
)
;