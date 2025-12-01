define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.classification': {
                    url: '/classification',
                    templateUrl: 'app/desktop/modules/classification/classificationView.jsp',
                    controller: 'ClassificationController as classVm',
                    resolve: ['app/desktop/modules/classification/classificationController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.folderClassification': {
                    url: '/folderClassification',
                    templateUrl: 'app/desktop/modules/classification/folderView.jsp',
                    controller: 'FolderController as folderClassVm',
                    resolve: ['app/desktop/modules/classification/folderController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);