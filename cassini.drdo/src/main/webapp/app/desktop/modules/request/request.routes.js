define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.requests': {
                    url: '/requests',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/request/requestMainView.jsp',
                    controller: 'RequestMainController as requestMainVm',
                    resolve: ['app/desktop/modules/request/requestMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.requests.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/request/all/requestsView.jsp',
                    controller: 'RequestsController as requestsVm',
                    resolve: ['app/desktop/modules/request/all/requestsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.requests.details': {
                    url: '/details/:requestId?mode',
                    templateUrl: 'app/desktop/modules/request/details/requestDetailsView.jsp',
                    controller: 'RequestDetailsController as requestDetailsVm',
                    resolve: ['app/desktop/modules/request/details/requestDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.requests.new': {
                    url: '/new',
                    templateUrl: 'app/desktop/modules/request/new/newRequestView.jsp',
                    controller: 'NewRequestController as newRequestVm',
                    resolve: ['app/desktop/modules/request/new/newRequestController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.requests.summary': {
                    url: '/summary/:requestId',
                    templateUrl: 'app/desktop/modules/request/details/requestSummaryView.jsp',
                    controller: 'RequestSummaryController as requestSummaryVm',
                    resolve: ['app/desktop/modules/request/details/requestSummaryController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);