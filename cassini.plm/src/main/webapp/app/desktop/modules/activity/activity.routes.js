define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.activity': {
                    url: '/activity',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/activity/activityMainView.jsp',
                    controller: 'ActivityMainController as activityMainVm',
                    resolve: ['app/desktop/modules/activity/activityMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.activity.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/activity/all/allActivitiesView.jsp',
                    controller: 'AllActivitiesController as allActivitiesVm',
                    resolve: ['app/desktop/modules/activity/all/allActivitiesController'],
                    css: cssConfig.getViewCss('app')
                },
            }
        };
    }
);