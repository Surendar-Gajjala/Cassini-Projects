define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.issues': {
                    url: '/issues',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/issue/issueMainView.jsp',
                    controller: 'IssueMainController as issueMainVm',
                    resolve: ['app/desktop/modules/issue/issueMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.issues.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/issue/all/issuesView.jsp',
                    controller: 'IssuesController as issuesVm',
                    resolve: ['app/desktop/modules/issue/all/issuesController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.issues.details': {
                    url: '/details/:issueId?mode',
                    templateUrl: 'app/desktop/modules/issue/details/issueDetailsView.jsp',
                    controller: 'IssueDetailsController as issueDetailsVm',
                    resolve: ['app/desktop/modules/issue/details/issueDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.issues.new': {
                    url: '/new?issueMode/?requestId',
                    templateUrl: 'app/desktop/modules/issue/new/newIssueView.jsp',
                    controller: 'NewIssueController as newIssueVm',
                    resolve: ['app/desktop/modules/issue/new/newIssueController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);