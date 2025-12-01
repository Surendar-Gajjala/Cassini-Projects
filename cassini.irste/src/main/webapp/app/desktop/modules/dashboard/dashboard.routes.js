/**
 * Created by Nageshreddy on 08-11-2018.
 */
define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.dashboard': {
                    url: '/dashboard',
                    templateUrl: 'app/desktop/modules/dashboard/dashboardView.jsp',
                    controller: 'DashboardController as dashboardVm',
                    resolve: ['app/desktop/modules/dashboard/dashboardController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);