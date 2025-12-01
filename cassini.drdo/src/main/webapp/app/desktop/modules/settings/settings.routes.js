define(
    [
        'app/desktop/desktop.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'app.settings': {
                    url: '/settings',
                    templateUrl: 'app/desktop/modules/settings/settingsView.jsp',
                    controller: 'SettingsController as settingsVm',
                    resolve: ['app/desktop/modules/settings/settingsController.js'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);