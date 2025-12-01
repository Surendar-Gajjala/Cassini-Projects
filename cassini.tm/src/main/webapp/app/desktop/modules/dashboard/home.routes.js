define(
    [
        'app/desktop/desktop.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'app.home': {
                    url: '/home',
                    templateUrl: 'app/desktop/modules/dashboard/homeView.jsp',
                    controller: 'HomeController as homeVm',
                    resolve: ['app/desktop/modules/dashboard/homeController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);