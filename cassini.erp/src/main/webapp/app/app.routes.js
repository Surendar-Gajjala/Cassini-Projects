define([],
    function () {
        var viewsCss = {
                global: [{
                    href: 'app/assets/css/style.default.css',
                    persist: true
                }, {
                    href: 'app/assets/css/style.katniss.css',
                    persist: true
                }, {
                    href: 'app/assets/css/jquery-ui-1.10.3.css',
                    persist: true
                }, {
                    href: 'app/assets/css/animate.css',
                    persist: true
                }, {
                    href: 'app/assets/other/icon-fonts/icon-fonts.css',
                    persist: true
                }, {
                    href: 'app/assets/libs/angular-ui-select/select.css',
                    persist: true
                }, {
                    href: 'app/assets/css/scrollable-table.css',
                    persist: true
                }, {
                    href: 'app/assets/libs/loading/treasure-overlay-spinner.min.css',
                    persist: true
                }, {
                    href: 'app/assets/css/morris.css',
                    persist: true
                }, {
                    href: 'app/assets/libs/hopscotch/css/hopscotch.css',
                    persist: true
                }],
                app: [{
                    href: 'app/assets/css/app.css?bust=' + (new Date()).getTime(),
                    persist: true
                },
                    {
                        href: 'app/assets/css/bootstrap-timepicker.min.css',
                        persist: true
                    }, {
                        href: 'app/assets/libs/angular-xeditable/css/xeditable.css',
                        persist: true
                    }],
                login: [{
                    href: 'app/assets/css/app.css?bust=' + (new Date()).getTime(),
                    persist: true
                }, {
                    href: 'app/assets/css/login.css?bust=' + (new Date()).getTime(),
                    persist: true
                }],
                home: [{
                    href: 'app/assets/css/app.css?bust=' + (new Date()).getTime(),
                    persist: true
                }],
                crm: [{
                    href: 'app/assets/css/app.css?bust=' + (new Date()).getTime(),
                    persist: true
                }, {
                    href: 'app/assets/libs/angular-xeditable/css/xeditable.css',
                    persist: true
                }, {
                    href: 'app/assets/libs/daterangepicker/daterangepicker.css',
                    persist: true
                }, {
                    href: 'app/assets/css/timeline.css',
                    persist: true
                }, {
                    href: 'app/assets/css/bootstrap-wysihtml5.css',
                    persist: true
                }],
                hrm: [{
                    href: 'app/assets/css/app.css?bust=' + (new Date()).getTime(),
                    persist: true
                }, {
                    href: 'app/assets/libs/angular-xeditable/css/xeditable.css',
                    persist: true
                }],
                prod: [{
                    href: 'app/assets/css/app.css?bust=' + (new Date()).getTime(),
                    persist: true
                }, {
                    href: 'app/assets/libs/angular-xeditable/css/xeditable.css',
                    persist: true
                }, {
                    href: 'app/assets/libs/jquery/jquery-easyui/themes/bootstrap/easyui.css',
                    persist: true
                }, {
                    href: 'app/assets/libs/jquery/jquery-easyui/themes/icon.css',
                    persist: true
                },
                    {
                        href: 'app/assets/css/fullcalendar.css',
                        persist: true
                    }],
                admin: [{
                    href: 'app/assets/css/app.css?bust=' + (new Date()).getTime(),
                    persist: true
                }, {
                    href: 'app/assets/libs/angular-xeditable/css/xeditable.css',
                    persist: true
                }],
                security: [{
                    href: 'app/assets/css/app.css?bust=' + (new Date()).getTime(),
                    persist: true
                }, {
                    href: 'app/assets/libs/angular-xeditable/css/xeditable.css',
                    persist: true
                }, {
                    href: 'app/assets/libs/daterangepicker/daterangepicker.css',
                    persist: true
                }],
                common: [{
                    href: 'app/assets/css/app.css?bust=' + (new Date()).getTime(),
                    persist: true
                }, {
                    href: 'app/assets/libs/angular-xeditable/css/xeditable.css',
                    persist: true
                }],
                payroll: [{
                    href: 'app/assets/css/app.css?bust=' + (new Date()).getTime(),
                    persist: true
                }, {
                    href: 'app/assets/libs/angular-xeditable/css/xeditable.css',
                    persist: true
                }, {
                    href: 'app/assets/css/payroll.css',
                    persist: true
                }],
                attendance: [{
                    href: 'app/assets/css/app.css?bust=' + (new Date()).getTime(),
                    persist: true
                }, {
                    href: 'app/assets/libs/angular-xeditable/css/xeditable.css',
                    persist: true
                },
                    {
                        href: 'app/assets/css/fullcalendar.css',
                        persist: true
                    }],
                reporting: [
                    {
                        href: 'app/assets/libs/daterangepicker/daterangepicker.css',
                        persist: true
                    }
                ]
            },
            getViewCss = function (route) {
                var cssList = viewsCss.global;

                if (viewsCss[route] != null && route != null && route != 'global') {
                    cssList = cssList.concat(viewsCss[route]);
                }

                return cssList;
            };

        return {
            otherwise: '/app/home',
            routes: {
                'app': {
                    url: '/app',
                    templateUrl: 'app/components/main/mainView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'MainController',
                    resolve: ['app/components/main/mainController'],
                    css: getViewCss('app')
                },
                'login': {
                    url: '/login',
                    templateUrl: 'app/components/login/loginView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'LoginController',
                    resolve: ['app/components/login/loginController'],
                    css: getViewCss('login')
                },
                'app.home': {
                    url: '/home',
                    templateUrl: 'app/components/home/homeView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'HomeController',
                    resolve: ['app/components/home/homeController'],
                    css: getViewCss('home')
                },
                'app.crm': {
                    url: '/crm',
                    abstract: true,
                    templateUrl: 'app/components/crm/crmView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'CrmController',
                    resolve: ['app/components/crm/crmController'],
                    css: getViewCss('crm')
                },
                'app.crm.dashboard': {
                    url: '/dashboard',
                    templateUrl: 'app/components/crm/dashboard/crmDashboardView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'CrmDashboardController',
                    resolve: ['app/components/crm/dashboard/crmDashboardController'],
                    css: getViewCss('crm')
                },
                'app.crm.customers': {
                    url: '/customers?page',
                    templateUrl: 'app/components/crm/customer/customersView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'CustomersController',
                    resolve: ['app/components/crm/customer/customersController'],
                    css: getViewCss('crm')
                },

                'app.crm.shipments': {
                    url: '/shipments?page',
                    templateUrl: 'app/components/crm/consignment/new/newShipmentDialogue.jsp?bust=' + (new Date()).getTime(),
                    controller: 'NewShipmentDialogueController',
                    resolve: ['app/components/crm/consignment/new/newShipmentDialogueController'],
                    css: getViewCss('crm')
                },


                'app.crm.newcustomer': {
                    url: '/customers/new?source?salesRep?order',
                    templateUrl: 'app/components/crm/customer/new/newCustomerView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'NewCustomerController',
                    resolve: ['app/components/crm/customer/new/newCustomerController'],
                    css: getViewCss('crm')
                },
                /*'app.crm.pagedcustomers': {
                 url: '/customers/page/:page',
                 templateUrl: 'app/components/crm/customer/customersView.jsp?bust=' + (new Date()).getTime(),
                 controller: 'CustomersController',
                 resolve: ['app/components/crm/customer/customersController'],
                 css: getViewCss('crm')
                 },*/
                'app.crm.customer': {
                    url: '/customer/:customerId',
                    templateUrl: 'app/components/crm/customer/details/customerDetailsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'CustomerDetailsController',
                    resolve: ['app/components/crm/customer/details/customerDetailsController'],
                    css: getViewCss('crm')
                },
                'app.crm.orders': {
                    url: '/orders',
                    abstract: true,
                    templateUrl: 'app/components/crm/order/orderView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'OrderController',
                    resolve: ['app/components/crm/order/orderController'],
                    css: getViewCss('crm')
                },
                'app.crm.orders.all': {
                    url: '/all/:status',
                    templateUrl: 'app/components/crm/order/all/allOrdersView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'AllOrdersController',
                    resolve: ['app/components/crm/order/all/allOrdersController'],
                    css: getViewCss('crm')
                },
                'app.crm.orders.details': {
                    url: '/:orderId/details',
                    templateUrl: 'app/components/crm/order/details/orderDetailsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'OrderDetailsController',
                    resolve: ['app/components/crm/order/details/orderDetailsController'],
                    css: getViewCss('crm')
                },
                'app.crm.orders.dispatch': {
                    url: '/:orderId/dispatch',
                    templateUrl: 'app/components/crm/order/dispatch/orderDispatchView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'OrderDispatchController',
                    resolve: ['app/components/crm/order/dispatch/orderDispatchController'],
                    css: getViewCss('crm')
                },
                'app.crm.orders.verifications': {
                    url: '/verifications',
                    templateUrl: 'app/components/crm/order/verification/verificationsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'VerificationsController',
                    resolve: ['app/components/crm/order/verification/verificationsController'],
                    css: getViewCss('crm')
                },
                'app.crm.orders.cart': {
                    url: '/cart',
                    templateUrl: 'app/components/crm/order/cart/shoppingCartView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'ShoppingCartController',
                    resolve: ['app/components/crm/order/cart/shoppingCartController'],
                    css: getViewCss('crm')
                },
                'app.crm.salesregions': {
                    url: '/salesregion',
                    templateUrl: 'app/components/crm/salesregion/salesRegionsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'SalesRegionsController',
                    resolve: ['app/components/crm/salesregion/salesRegionsController'],
                    css: getViewCss('global')
                },
                'app.crm.salesreps': {
                    url: '/salesreps',
                    templateUrl: 'app/components/crm/salesrep/salesRepsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'SalesRepsController',
                    resolve: ['app/components/crm/salesrep/salesRepsController'],
                    css: getViewCss('crm')
                },
                'app.crm.salesrep': {
                    url: '/salesrep/:salesRepId?tab',
                    templateUrl: 'app/components/crm/salesrep/details/salesRepDetailsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'SalesRepDetailsController',
                    resolve: ['app/components/crm/salesrep/details/salesRepDetailsController'],
                    css: getViewCss('crm')
                },
                'app.crm.salesrep.basicinfo': {
                    url: '/basicinfo',
                    views: {
                        'basicinfo': {
                            templateUrl: 'app/components/crm/salesrep/details/tabs/basicInfoView.jsp?bust=' + (new Date()).getTime(),
                            controller: 'SalesRepBasicInfoController',
                            resolve: ['app/components/crm/salesrep/details/tabs/basicInfoController'],
                            css: getViewCss('crm')
                        }
                    }
                },
                'app.crm.salesrep.customers': {
                    url: '/customers',
                    views: {
                        'customers': {
                            templateUrl: 'app/components/crm/salesrep/details/tabs/customersView.jsp?bust=' + (new Date()).getTime(),
                            controller: 'SalesRepCustomersController',
                            resolve: ['app/components/crm/salesrep/details/tabs/customersController'],
                            css: getViewCss('crm')
                        }
                    }
                },
                'app.crm.salesrep.fieldreports': {
                    url: '/fieldreports?mode',
                    views: {
                        'fieldreports': {
                            templateUrl: 'app/components/crm/salesrep/details/tabs/fieldReportsView.jsp?bust=' + (new Date()).getTime(),
                            controller: 'SalesRepFieldReportsController',
                            resolve: ['app/components/crm/salesrep/details/tabs/fieldReportsController'],
                            css: getViewCss('crm')
                        }
                    }
                },
                'app.crm.salesrep.orders': {
                    url: '/orders',
                    views: {
                        "orders": {
                            templateUrl: 'app/components/crm/salesrep/details/tabs/ordersView.jsp?bust=' + (new Date()).getTime(),
                            controller: 'SalesRepOrdersController',
                            resolve: ['app/components/crm/salesrep/details/tabs/ordersController'],
                            css: getViewCss('crm')
                        }
                    }
                },
                'app.crm.salesrep.performance': {
                    url: '/performance',
                    views: {
                        'performance': {
                            templateUrl: 'app/components/crm/salesrep/details/tabs/performanceView.jsp?bust=' + (new Date()).getTime(),
                            controller: 'SalesRepPerformanceController',
                            resolve: ['app/components/crm/salesrep/details/tabs/performanceController'],
                            css: getViewCss('crm')
                        }
                    }
                },
                'app.crm.returns': {
                    url: '/returns',
                    abstract: true,
                    templateUrl: 'app/components/crm/return/returnsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'ReturnsController',
                    resolve: ['app/components/crm/return/returnsController'],
                    css: getViewCss('crm')
                },
                'app.crm.returns.all': {
                    url: '/all/:status',
                    templateUrl: 'app/components/crm/return/all/allReturnsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'AllReturnsController',
                    resolve: ['app/components/crm/return/all/allReturnsController'],
                    css: getViewCss('crm')
                },
                'app.crm.returns.details': {
                    url: '/:returnId',
                    templateUrl: 'app/components/crm/return/details/returnDetailsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'ReturnDetailsController',
                    resolve: ['app/components/crm/return/details/returnDetailsController'],
                    css: getViewCss('crm')
                },
                'app.crm.returns.new': {
                    url: '/new?source',
                    templateUrl: 'app/components/crm/return/new/newReturnView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'NewReturnController',
                    resolve: ['app/components/crm/return/new/newReturnController'],
                    css: getViewCss('crm')
                },
                'app.crm.shipping': {
                    url: '/shipping/home',
                    templateUrl: 'app/components/crm/consignment/consignmentsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'ConsignmentsController',
                    resolve: ['app/components/crm/consignment/consignmentsController'],
                    css: getViewCss('crm')
                },
                'app.crm.shippinghome': {
                    url: '/shipping',
                    templateUrl: 'app/components/crm/consignment/shippingHomeView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'ShippingHomeController',
                    resolve: ['app/components/crm/consignment/shippingHomeController'],
                    css: getViewCss('crm')
                },
                'app.crm.shippinghome.approvedorders': {
                    url: '/approvedorders',
                    templateUrl: 'app/components/home/roles/orderprocessing/orderProcessingHomeView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'OrderProcessingHomeController',
                    resolve: ['app/components/home/roles/orderprocessing/orderProcessingHomeController'],
                    css: getViewCss('crm')
                },
                'app.crm.shippinghome.pending': {
                    url: '/pending',
                    templateUrl: 'app/components/crm/consignment/pendingShipmentsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'PendingShipmentsController',
                    resolve: ['app/components/crm/consignment/pendingShipmentsController'],
                    css: getViewCss('crm')
                },
                'app.crm.shippinghome.shipped': {
                    url: '/shipped',
                    templateUrl: 'app/components/crm/consignment/shippedShipmentsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'ShippedShipmentsController',
                    resolve: ['app/components/crm/consignment/shippedShipmentsController'],
                    css: getViewCss('crm')
                },
                'app.crm.shippinghome.finished': {
                    url: '/finished',
                    templateUrl: 'app/components/crm/consignment/finishedShipmentsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'FinishedShipmentsController',
                    resolve: ['app/components/crm/consignment/finishedShipmentsController'],
                    css: getViewCss('crm')
                },
                'app.crm.consigndetails': {
                    url: '/consignments/:consignmentId',
                    templateUrl: 'app/components/crm/consignment/details/consignmentDetailsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'ConsignmentDetailsController',
                    resolve: ['app/components/crm/consignment/details/consignmentDetailsController'],
                    css: getViewCss('crm')
                },
                'app.crm.settings': {
                    url: '/settings',
                    abstract: true,
                    templateUrl: 'app/components/crm/settings/crmSettingsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'CRMSettingsController',
                    resolve: ['app/components/crm/settings/crmSettingsController'],
                    css: getViewCss('global')
                },
                'app.crm.settings.basic': {
                    url: '/basic',
                    templateUrl: 'app/components/crm/settings/basic/basicSettingsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'BasicSettingsController',
                    resolve: ['app/components/crm/settings/basic/basicSettingsController'],
                    css: getViewCss('global')
                },
                'app.hrm': {
                    url: '/hrm',
                    abstract: true,
                    templateUrl: 'app/components/hrm/hrmView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'HrmController',
                    resolve: ['app/components/hrm/hrmController'],
                    css: getViewCss('hrm')
                },
                'app.hrm.dashboard': {
                    url: '/dashboard',
                    abstract: true,
                    templateUrl: 'app/components/hrm/dashboard/hrmDashboardView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'HrmDashboardController',
                    resolve: ['app/components/hrm/dashboard/hrmDashboardController'],
                    css: getViewCss('hrm')
                },
                'app.hrm.employees': {
                    url: '/employees',
                    templateUrl: 'app/components/hrm/employee/employeesView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'EmployeesController',
                    resolve: ['app/components/hrm/employee/employeesController', 'app/shared/directives/datepicker'],
                    css: getViewCss('app')
                },
                'app.hrm.employee': {
                    url: '/employee/:employeeId',
                    templateUrl: 'app/components/hrm/employee/details/employeeDetailsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'EmployeeDetailsController',
                    resolve: ['app/components/hrm/employee/details/employeeDetailsController', 'app/shared/filters/dateConverterFilter',
                        'app/shared/filters/rupeeFilter', 'app/shared/directives/monthCalenderDirective'],
                    css: getViewCss('app')
                },
                'app.hrm.payroll': {
                    url: '/payroll',
                    templateUrl: 'app/components/hrm/payroll/payrollView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'PayrollController',
                    resolve: ['app/components/hrm/payroll/payrollController', 'app/shared/filters/rupeeFilter'],
                    css: getViewCss('payroll')
                },
                'app.hrm.attendance': {
                    url: '/attendance',
                    abstract: true,
                    templateUrl: 'app/components/hrm/attendance/attendance.jsp?bust=' + (new Date()).getTime()
                },
                'app.hrm.attendance.import': {
                    url: '/import',
                    templateUrl: 'app/components/hrm/attendance/attendanceView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'AttendanceController',
                    resolve: ['app/components/hrm/attendance/attendanceController', 'app/shared/directives/finishRenderDirective'],
                    css: getViewCss('attendance')
                },
                'app.hrm.timeoffs': {
                    url: '/timeoffs',
                    templateUrl: 'app/components/hrm/attendance/timeoffs/timeoffsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'TimeoffsController',
                    resolve: ['app/components/hrm/attendance/timeoffs/timeoffsController', 'app/shared/directives/datepicker'],
                    css: getViewCss('app')
                },
                'app.hrm.attendance.employee': {
                    url: '/employee/:empid/:month/:year',
                    templateUrl: 'app/components/hrm/attendance/employee/employeeAttendance.jsp?bust=' + (new Date()).getTime(),
                    controller: 'EmployeeAttendanceController',
                    resolve: ['app/components/hrm/attendance/employee/employeeAttendanceController', 'app/shared/filters/timeFilter'],
                    css: getViewCss('attendance')
                },
                'app.hrm.attendance.employees': {
                    url: '/employees/:month/:year',
                    templateUrl: 'app/components/hrm/attendance/employees/employeesAttendance.jsp?bust=' + (new Date()).getTime(),
                    controller: 'EmployeesAttendanceController',
                    resolve: ['app/components/hrm/attendance/employees/employeesAttendanceController', 'app/shared/filters/timeFilter'],
                    css: getViewCss('attendance')
                },
                'app.hrm.geotracker': {
                    url: '/geotracker',
                    templateUrl: 'app/components/hrm/geo/geoTrackerView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'GeoTrackerController',
                    resolve: ['app/components/hrm/geo/geoTrackerController'],
                    css: getViewCss('hrm')
                },
                'app.hrm.settings': {
                    url: '/settings',
                    templateUrl: 'app/components/hrm/settings/hrmSettingsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'HRMSettingsController',
                    resolve: ['app/components/hrm/settings/hrmSettingsController', 'app/shared/directives/datepicker'],
                    css: getViewCss('global')
                },
                'app.prod': {
                    url: '/prod',
                    abstract: true,
                    templateUrl: 'app/components/prod/prodView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'ProdController',
                    resolve: ['app/components/prod/prodController'],
                    css: getViewCss('prod')
                },
                'app.prod.dashboard': {
                    url: '/dashboard',
                    templateUrl: 'app/components/prod/dashboard/productionDashboardView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'ProductionDashboardController',
                    resolve: ['app/components/prod/dashboard/productionDashboardController'],
                    css: getViewCss('prod')
                },
                'app.prod.newproduct': {
                    url: '/products/new',
                    templateUrl: 'app/components/prod/product/new/newProductView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'NewProductController',
                    resolve: ['app/components/prod/product/new/newProductController'],
                    css: getViewCss('prod')
                },
                'app.prod.products': {
                    url: '/products?mode',
                    templateUrl: 'app/components/prod/product/productsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'ProductsController',
                    resolve: ['app/components/prod/product/productsController'],
                    css: getViewCss('prod')
                },
                'app.prod.materials': {
                    url: '/materials',
                    templateUrl: 'app/components/prod/material/materialsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'MaterialsController',
                    resolve: ['app/components/prod/material/materialsController'],
                    css: getViewCss('prod')
                },
                'app.prod.materialDetails': {
                    url: '/materials/detail?materialId',
                    templateUrl: 'app/components/prod/material/details/materialDetailsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'MaterialDetailsController',
                    resolve: ['app/components/prod/material/details/materialDetailsController'],
                    css: getViewCss('prod')
                },
                'app.prod.materialspo': {
                    url: '/materialspo',
                    templateUrl: 'app/components/prod/materialpo/materialsPOView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'MaterialsPOController',
                    resolve: ['app/components/prod/materialpo/materialsPOController'],
                    css: getViewCss('prod')
                },
                'app.prod.newmaterialspo': {
                    url: '/materialspo/new',
                    templateUrl: 'app/components/prod/materialpo/new/newMaterialPOView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'NewMaterialPOController',
                    resolve: ['app/components/prod/materialpo/new/newMaterialPOController'],
                    css: getViewCss('prod')
                },
                'app.prod.materialspoDetails': {
                    url: '/materialspo/detail?materialPoId',
                    templateUrl: 'app/components/prod/materialpo/new/newMaterialPOView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'NewMaterialPOController',
                    resolve: ['app/components/prod/materialpo/new/newMaterialPOController'],
                    css: getViewCss('prod')
                },
                'app.prod.productionorder': {
                    url: '/productionorders/new?createDisplay?orderId',
                    templateUrl: 'app/components/prod/production/order/productionOrderView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'ProductionOrderController',
                    resolve: ['app/components/prod/production/order/productionOrderController'],
                    css: getViewCss('crm')
                },

                'app.prod.suppliers': {
                    url: '/suppliers',
                    templateUrl: 'app/components/prod/supplier/suppliersView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'SuppliersController',
                    resolve: ['app/components/prod/supplier/suppliersController'],
                    css: getViewCss('prod')
                },
                'app.prod.newsupplier': {
                    url: '/suppliers/new?supplierId',
                    templateUrl: 'app/components/prod/supplier/new/newSupplierView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'NewSupplierController',
                    resolve: ['app/components/prod/supplier/new/newSupplierController'],
                    css: getViewCss('prod')
                },
                'app.prod.production': {
                    url: '/production',
                    templateUrl: 'app/components/prod/production/productionView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'ProductionController',
                    resolve: ['app/components/prod/production/productionController'],
                    css: getViewCss('prod')
                },
                'app.prod.settings': {
                    url: '/settings',
                    templateUrl: 'app/components/prod/settings/prodSettingsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'ProductionSettingsController',
                    resolve: ['app/components/prod/settings/prodSettingsController'],
                    css: getViewCss('global')
                },
                'app.store': {
                    url: '/store',
                    abstract: true,
                    templateUrl: 'app/components/store/storeMainView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'StoreMainController',
                    resolve: ['app/components/store/storeMainController'],
                    css: getViewCss('prod')
                },
                'app.store.all': {
                    url: '/all',
                    templateUrl: 'app/components/store/all/storesView.jsp',
                    controller: 'StoresController as allStoresVm',
                    resolve: ['app/components/store/all/storesController'],
                    css: getViewCss('prod')
                },
                'app.store.details': {
                    url: '/:storeId?mode',
                    templateUrl: 'app/components/store/details/storeDetailsView.jsp',
                    controller: 'StoreDetailController as storeDetailsVm',
                    resolve: ['app/components/store/details/storeDetailsController'],
                    css: getViewCss('prod')
                },
                'app.store.inventory': {
                    url: '/inventory',
                    templateUrl: 'app/components/store/inventory/inventoryView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'InventoryController',
                    resolve: ['app/components/store/inventory/inventoryController'],
                    css: getViewCss('prod')
                },
                'app.store.inventory.products': {
                    url: '/products',
                    views: {
                        'products': {
                            templateUrl: 'app/components/store/inventory/product/productInventoryView.jsp?bust=' + (new Date()).getTime(),
                            controller: 'ProductInventoryController',
                            resolve: ['app/components/store/inventory/product/productInventoryController'],
                            css: getViewCss('prod')
                        }
                    }
                },
                'app.store.inventory.material': {
                    url: '/material',
                    views: {
                        'material': {
                            templateUrl: 'app/components/store/inventory/material/materialControllerView.jsp?bust=' + (new Date()).getTime(),
                            controller: 'MaterialInventoryController',
                            resolve: ['app/components/store/inventory/material/materialInventoryController'],
                            css: getViewCss('prod')
                        }
                    }
                },
                'app.store.issue':{
                    url: '/issue/report',
                    templateUrl: 'app/components/store/issue/materialIssueReportView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'MaterialIssueReportController',
                    resolve: ['app/components/store/issue/materialIssueReportController'],
                    css: getViewCss('prod')
                },
                'app.admin': {
                    url: '/admin',
                    abstract: true,
                    templateUrl: 'app/components/admin/adminView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'AdminController',
                    resolve: ['app/components/admin/adminController'],
                    css: getViewCss('admin')
                },
                'app.admin.dashboard': {
                    url: '/dashboard',
                    abstract: true,
                    templateUrl: 'app/components/admin/dashboard/adminDashboardView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'AdminDashboardController',
                    resolve: ['app/components/admin/dashboard/adminDashboardController'],
                    css: getViewCss('global')
                },
                'app.admin.security': {
                    url: '/security',
                    abstract: true,
                    templateUrl: 'app/components/admin/security/securityView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'SecurityController',
                    resolve: ['app/components/admin/security/securityController'],
                    css: getViewCss('security')
                },
                'app.admin.security.logins': {
                    url: '/logins',
                    views: {
                        'logins': {
                            templateUrl: 'app/components/admin/security/login/loginsView.jsp?bust=' + (new Date()).getTime(),
                            controller: 'LoginsController',
                            resolve: ['app/components/admin/security/login/loginsController'],
                            css: getViewCss('security')
                        }
                    }
                },
                'app.admin.security.roles': {
                    url: '/roles',
                    views: {
                        'roles': {
                            templateUrl: 'app/components/admin/security/role/rolesView.jsp?bust=' + (new Date()).getTime(),
                            controller: 'RolesController',
                            resolve: ['app/components/admin/security/role/rolesController'],
                            css: getViewCss('security')
                        }
                    }
                },
                'app.admin.security.sessions': {
                    url: '/sessions',
                    views: {
                        'sessions': {
                            templateUrl: 'app/components/admin/security/session/sessionsView.jsp?bust=' + (new Date()).getTime(),
                            controller: 'SessionsController',
                            resolve: ['app/components/admin/security/session/sessionsController'],
                            css: getViewCss('security')
                        }
                    }
                },
                'app.admin.session': {
                    url: '/sessions/:sessionId',
                    templateUrl: 'app/components/admin/security/session/sessionDetailsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'SessionDetailsController',
                    resolve: ['app/components/admin/security/session/sessionDetailsController'],
                    css: getViewCss('security')
                },
                'app.admin.settings': {
                    url: '/security',
                    templateUrl: 'app/components/admin/settings/adminSettingsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'AdminSettingsController',
                    resolve: ['app/components/admin/settings/adminSettingsController'],
                    css: getViewCss('admin')
                },
                'app.common': {
                    url: '/common',
                    abstract: true,
                    templateUrl: 'app/components/common/commonView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'CommonController',
                    resolve: ['app/components/common/commonController'],
                    css: getViewCss('common')
                },
                'app.common.autonumbers': {
                    url: '/autonumbers',
                    templateUrl: 'app/components/common/autonumber/autonumbersView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'AutonumbersController',
                    resolve: ['app/components/common/autonumber/autonumbersController'],
                    css: getViewCss('common')
                },
                'app.reporting': {
                    url: '/reporting',
                    abstract: true,
                    templateUrl: 'app/components/reporting/reportingView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'ReportingController',
                    resolve: ['app/components/reporting/reportingController'],
                    css: getViewCss('reporting')
                },
                'app.reporting.reports': {
                    url: '/reports',
                    templateUrl: 'app/components/reporting/reports/reportsView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'ReportsController',
                    resolve: ['app/components/reporting/reports/reportsController'],
                    css: getViewCss('reporting')
                },
                'app.reporting.editor': {
                    url: '/editor',
                    templateUrl: 'app/components/reporting/editor/reportEditorView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'ReportEditorController',
                    resolve: ['app/components/reporting/editor/reportEditorController'],
                    css: getViewCss('reporting')
                },
                'app.help': {
                    url: '/help',
                    templateUrl: 'app/components/help/helpView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'HelpController',
                    resolve: ['app/components/help/helpController'],
                    css: getViewCss('common')
                },
                'app.feedback': {
                    url: '/feedback',
                    templateUrl: 'app/components/help/feedback/feedbackView.jsp?bust=' + (new Date()).getTime(),
                    controller: 'FeedbackController as feedbackVm',
                    resolve: ['app/components/help/feedback/feedbackController'],
                    css: getViewCss('common')
                }
            }
        };
    }
);