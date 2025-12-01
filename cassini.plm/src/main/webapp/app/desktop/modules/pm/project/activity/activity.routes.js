define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.pm.project.activity': {
                    url: '/activity',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/pm/project/activity/activityMainView.jsp',
                    controller: 'ActivityMainController as activityMainVm',
                    resolve: ['app/desktop/modules/pm/project/activity/activityMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.activity.details': {
                    url: '/details/:activityId?tab',
                    templateUrl: 'app/desktop/modules/pm/project/activity/details/activityDetailsView.jsp',
                    controller: 'ActivityDetailsController as activityDetailsVm',
                    resolve: ['app/desktop/modules/pm/project/activity/details/activityDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);