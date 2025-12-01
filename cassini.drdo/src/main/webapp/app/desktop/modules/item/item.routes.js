define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.items': {
                    url: '/items',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/item/itemMainView.jsp',
                    controller: 'ItemMainController as itemMainVm',
                    resolve: ['app/desktop/modules/item/itemMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.items.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/item/all/itemsView.jsp',
                    controller: 'ItemsController as itemsVm',
                    resolve: ['app/desktop/modules/item/all/itemsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.items.new': {
                    url: '/new',
                    templateUrl: 'app/desktop/modules/item/new/newItemView.jsp',
                    controller: 'NewItemController as newItemVm',
                    resolve: ['app/desktop/modules/item/new/newItemController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.items.details': {
                    url: '/details/:itemId',
                    templateUrl: 'app/desktop/modules/item/details/itemDetailsView.jsp',
                    controller: 'ItemDetailsController as itemDetailsVm',
                    resolve: ['app/desktop/modules/item/details/itemDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);