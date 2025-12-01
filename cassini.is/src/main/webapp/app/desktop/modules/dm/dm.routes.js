define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.dm': {
                    url: '/dm',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/dm/dmMainView.jsp',
                    controller: 'DmMainController as dmMainVm',
                    resolve: ['app/desktop/modules/dm/dmMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.documents': {
                    url: '/documents/:type',
                    templateUrl: 'app/desktop/modules/dm/documents/documentsView.jsp',
                    controller: 'DocumentsController as documentsVm',
                    resolve: ['app/desktop/modules/dm/documents/documentsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.documentdetails': {
                    url: '/documents/:folderId/:documentId',
                    templateUrl: 'app/desktop/modules/dm/documents/details/documentDetailsView.jsp',
                    controller: 'DocumentDetailsController as docDetailsVm',
                    resolve: ['app/desktop/modules/dm/documents/details/documentDetailsController'],
                    css: cssConfig.getViewCss('app')
                },

            }
        };
    }
);