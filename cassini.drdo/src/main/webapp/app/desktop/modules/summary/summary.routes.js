define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.summary': {
                    url: '/summary',
                    templateUrl: 'app/desktop/modules/summary/summaryView.jsp',
                    controller: 'SummaryController as summaryVm',
                    resolve: ['app/desktop/modules/summary/summaryController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        }
    }
);