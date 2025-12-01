/**
 * Created by Suresh Cassini on 03-Jul-18.
 */
define(
    [
        'app/desktop/desktop.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'app.config': {
                    url: '/config',
                    templateUrl: 'app/desktop/modules/config/configView.jsp',
                    controller: 'ConfigController as configVm',
                    resolve: ['app/desktop/modules/config/configController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);