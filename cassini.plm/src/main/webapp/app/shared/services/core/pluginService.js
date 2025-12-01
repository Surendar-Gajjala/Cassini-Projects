define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('PluginService', PluginService);

        function PluginService(httpFactory) {
            return {
                getPlugins: getPlugins,
                enablePlugin: enablePlugin,
                disablePlugin: disablePlugin,
                loadExtensions: loadExtensions
            };

            function getPlugins() {
                var url = "api/plugins";
                return httpFactory.get(url);
            }

            function enablePlugin(plugin) {
                var url = "api/plugins/enable?id={0}".format(plugin.id);
                return httpFactory.put(url);
            }

            function disablePlugin(plugin) {
                var url = "api/plugins/disable?id={0}".format(plugin.id);
                return httpFactory.put(url);
            }

            function loadExtensions(plugin) {
                var url = "api/plugins/extensions?id={0}".format(plugin.id);
                return httpFactory.get(url);
            }
        }
    }
);