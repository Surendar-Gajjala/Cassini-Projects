define(
    [
        'app/desktop/desktop.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'app.commits': {
                    url: '/commits',
                    templateUrl: 'app/desktop/modules/commits/commitsView.jsp',
                    controller: 'CommitsController as commitsVm',
                    resolve: ['app/desktop/modules/commits/commitsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);