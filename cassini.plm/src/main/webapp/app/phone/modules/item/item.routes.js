define(
    [
        'app/phone/phone.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'app.items': {
                    url: '/items',
                    templateUrl: 'app/phone/modules/item/all/itemsView.jsp',
                    controller: 'ItemsController as itemsVm',
                    resolve: ['app/phone/modules/item/all/itemsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);