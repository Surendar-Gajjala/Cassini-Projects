define([],
    function() {
        var viewsCss = {
                global: [
                    {
                        href: 'app/assets/libs/angular-material/angular-material.css',
                        persist: true
                    },
                    {
                        href: 'app/assets/libs/angular-material/angular-material.layouts.css',
                        persist: true
                    },
                    {
                        href: 'app/assets/css/animate.min.css',
                        persist: true
                    },
                    {
                        href: 'app/assets/css/app.mobile.css?bust=' + (new Date()).getTime(),
                        persist: true
                    }
                ]
            },
            getCss =  function(route) {
                var cssList = viewsCss.global;
                if(viewsCss[route] != null && route != null && route != 'global') {
                    cssList = cssList.concat(viewsCss[route]);
                }
                return cssList;
            };

        return {
            otherwise: '/app/home',
            routes: {
                'app': {
                    url: '/app',
                    abstract: true,
                    templateUrl: 'app/mobile/phone/modules/main/mainView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'MainController',
                    resolve: ['app/mobile/phone/modules/main/mainController'],
                    css: getCss('app')
                },
                'login': {
                    url: '/login?fromapp',
                    templateUrl: 'app/mobile/phone/modules/login/loginView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'LoginController',
                    resolve: ['app/mobile/phone/modules/login/loginController'],
                    css: getCss('app')
                },
                'app.home': {
                    url: '/home',
                    templateUrl: 'app/mobile/phone/modules/home/homeView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'HomeController',
                    resolve: ['app/mobile/phone/modules/home/homeController'],
                    css: getCss('app')
                },
                'app.crm': {
                    url: '/crm',
                    abstract: true,
                    templateUrl: 'app/mobile/phone/modules/crm/crmView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'CrmController',
                    resolve: ['app/mobile/phone/modules/crm/crmController'],
                    css: getCss('app')
                },
                'app.crm.orders': {
                    url: '/orders',
                    templateUrl: 'app/mobile/phone/modules/crm/order/ordersView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'OrdersController',
                    resolve: ['app/mobile/phone/modules/crm/order/ordersController'],
                    css: getCss('app')
                },
                'app.crm.order': {
                    url: '/orders/:orderId/details',
                    templateUrl: 'app/mobile/phone/modules/crm/order/orderView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'OrderController',
                    resolve: ['app/mobile/phone/modules/crm/order/orderController'],
                    css: getCss('app')
                },
                'app.crm.products': {
                    url: '/products',
                    templateUrl: 'app/mobile/phone/modules/crm/product/productsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'ProductsController',
                    resolve: ['app/mobile/phone/modules/crm/product/productsController'],
                    css: getCss('app')
                },
                'app.crm.customers': {
                    url: '/customers',
                    templateUrl: 'app/mobile/phone/modules/crm/customer/customersView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'CustomersController',
                    resolve: ['app/mobile/phone/modules/crm/customer/customersController'],
                    css: getCss('app')
                },
                'app.crm.customer': {
                    url: '/customer/:customerId',
                    templateUrl: 'app/mobile/phone/modules/crm/customer/customerView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'CustomerController',
                    resolve: ['app/mobile/phone/modules/crm/customer/customerController'],
                    css: getCss('app')
                }
            }
        };
    }
);