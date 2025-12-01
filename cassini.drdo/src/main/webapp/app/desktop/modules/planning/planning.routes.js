/**
 * Created by Nageshreddy on 03-12-2018.
 */
define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.planning': {
                    url: '/planning',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/planning/planningMainView.jsp',
                    controller: 'PlanningMainController as planningMainVm',
                    resolve: ['app/desktop/modules/planning/planningMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.planning.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/planning/planningView.jsp',
                    controller: 'PlanningController as planningVm',
                    resolve: ['app/desktop/modules/planning/planningController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);