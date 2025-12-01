define(
    [
        'app/desktop/desktop.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'app.items': {
                    url: '/items',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/items/itemMainView.jsp',
                    controller: 'ItemMainController as itemMainVm',
                    resolve: ['app/desktop/modules/items/itemMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.items.all': {
                    url: '/all?mode',
                    templateUrl: 'app/desktop/modules/items/all/itemsView.jsp',
                    controller: 'ItemsController as itemsVm',
                    resolve: ['app/desktop/modules/items/all/itemsController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.items.new': {
                    url: '/new',
                    templateUrl: 'app/desktop/modules/items/new/newItemView.jsp',
                    controller: 'NewItemController as newItemVm',
                    resolve: ['app/desktop/modules/items/new/newItemController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.items.details': {
                    url: '/revisions/:itemId?tab',
                    templateUrl: 'app/desktop/modules/items/details/itemDetailsView.jsp',
                    controller: 'ItemDetailsController as itemVm',
                    resolve: ['app/desktop/modules/items/details/itemDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);