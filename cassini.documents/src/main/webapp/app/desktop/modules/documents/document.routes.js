define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.document': {
                    url: '/documents',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/documents/documentMainView.jsp',
                    controller: 'DocumentMainController as documentMainVm',
                    resolve: ['app/desktop/modules/documents/documentMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.document.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/documents/all/allDocumentsView.jsp',
                    controller: 'AllDocumentsController as allDocumentsVm',
                    resolve: ['app/desktop/modules/documents/all/allDocumentsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);