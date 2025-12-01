define(
    [
        'app/desktop/desktop.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'app.help': {
                    url: '/help',
                    abstract: true,
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/help/helpView.jsp',
                    controller: 'HelpController as helpVm',
                    resolve: ['app/assets/bower_components/cassini-platform/app/desktop/modules/help/helpController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.help.main': {
                    url: '/main',
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/help/main/helpMainView.jsp',
                    controller: 'HelpMainController as helpMainVm',
                    resolve: ['app/assets/bower_components/cassini-platform/app/desktop/modules/help/main/helpMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.help.feedback': {
                    url: '/feedback',
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/help/feedback/feedbackView.jsp',
                    controller: 'FeedbackController as feedbackVm',
                    resolve: ['app/assets/bower_components/cassini-platform/app/desktop/modules/help/feedback/feedbackController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);