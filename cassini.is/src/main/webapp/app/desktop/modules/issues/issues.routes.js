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
                    templateUrl: 'app/desktop/modules/issues/issuesMainView.jsp',
                    controller: 'IssuesMainController as issuesMainVm',
                    resolve: ['app/desktop/modules/issues/issuesMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.issues': {
                    url: '/issues',
                    templateUrl: 'app/desktop/modules/issues/all/issuesView.jsp',
                    controller: 'IssuesController as issuesVm',
                    resolve: ['app/desktop/modules/issues/all/issuesController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.newissue': {
                    url: '/issues/new',
                    templateUrl: 'app/desktop/modules/issues/new/newIssueDialog.jsp',
                    controller: 'NewIssueDialogController as newIssueDialogVm',
                    resolve: ['app/desktop/modules/issues/new/newIssueDialogController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.issuedetails': {
                    url: '/issues/:issueId/details',
                    templateUrl: 'app/desktop/modules/issues/details/issueDetailsView.jsp',
                    controller: 'IssueDetailsController as issueDetailsVm',
                    resolve: ['app/desktop/modules/issues/details/issueDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);