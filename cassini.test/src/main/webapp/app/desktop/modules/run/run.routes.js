/**
 * Created by SRAVAN on 7/30/2018.
 */
define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.run': {
                    url: '/run',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/run/runMainView.jsp',
                    controller: 'RunMainController as runMainVm',
                    resolve: ['app/desktop/modules/run/runMainController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.run.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/run/all/allRunsView.jsp',
                    controller: 'TestRunController as testRunVm',
                    resolve: ['app/desktop/modules/run/all/allRunsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.run.details': {
                    url: '/details/:testRunId',
                    templateUrl: 'app/desktop/modules/run/details/runDetailsView.jsp',
                    controller: 'RunDetailsController as runDetailsVm',
                    resolve: ['app/desktop/modules/run/details/runDetailsController'],
                    css: cssConfig.getViewCss('app')
                }

            }
        };
    }
);