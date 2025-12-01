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
                'app.home': {
                    url: '/home',
                    templateUrl: 'app/desktop/modules/home/homeView.jsp',
                    controller: 'HomeController as homeVm',
                    resolve: ['app/desktop/modules/home/homeController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);