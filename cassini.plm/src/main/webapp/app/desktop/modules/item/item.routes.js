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
                    url: '/:itemMode',
                    templateUrl: 'app/desktop/modules/item/all/itemsView.jsp',
                    controller: 'ItemsController as itemsVm',
                    resolve: ['app/desktop/modules/item/all/itemsController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.items.itemSearch': {
                    url: '/itemSearch',
                    templateUrl: 'app/desktop/modules/item/all/itemSearchDialogueView.jsp',
                    controller: 'ItemSearchDialogueController as itemSearchDialogueVm',
                    resolve: ['app/desktop/modules/item/all/itemSearchDialogueController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.items.advancedSearch': {
                    url: '/advancedSearch',
                    templateUrl: 'app/desktop/modules/item/all/advancedSearchView.jsp',
                    controller: 'AdvancedSearchController as advancedSearchVm',
                    resolve: ['app/desktop/modules/item/all/advancedSearchController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.items.new': {
                    url: '/new',
                    templateUrl: 'app/desktop/modules/item/new/newItemView.jsp',
                    controller: 'NewItemController as newItemVm',
                    resolve: ['app/desktop/modules/item/new/newItemController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.items.edit': {
                    url: '/:itemId/edit',
                    templateUrl: 'app/desktop/modules/item/edit/editItemView.jsp',
                    controller: 'EditItemController as itemVm',
                    resolve: ['app/desktop/modules/item/edit/editItemController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.items.details': {
                    url: '/revisions/:itemId?tab',
                    templateUrl: 'app/desktop/modules/item/details/itemDetailsView.jsp',
                    controller: 'ItemDetailsController as itemVm',
                    resolve: ['app/desktop/modules/item/details/itemDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);