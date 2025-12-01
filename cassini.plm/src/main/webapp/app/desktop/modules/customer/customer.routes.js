define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.customers': {
                    url: '/customers',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/customer/customerMainView.jsp',
                    controller: 'CustomerMainController as customerMainVm',
                    resolve: ['app/desktop/modules/customer/customerMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.customers.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/customer/all/customersView.jsp',
                    controller: 'CustomersController as customersVm',
                    resolve: ['app/desktop/modules/customer/all/customersController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.customers.details': {
                    url: '/:customerId?tab',
                    templateUrl: 'app/desktop/modules/customer/details/customerDetailsView.jsp',
                    controller: 'CustomerDetailsController as customerDetailsVm',
                    resolve: ['app/desktop/modules/customer/details/customerDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);