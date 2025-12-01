define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.home': {
                    url: '/home',
                    templateUrl: 'app/desktop/modules/home/homeView.jsp',
                    controller: 'HomeController as homeVm',
                    resolve: ['app/desktop/modules/home/homeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.newhome': {
                    url: '/newhome',
                    templateUrl: 'app/desktop/modules/home/dashboard/homeDashboardView.jsp',
                    controller: 'HomeDashboardController as homeDashboardVm',
                    resolve: ['app/desktop/modules/home/dashboard/homeDashboardController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);