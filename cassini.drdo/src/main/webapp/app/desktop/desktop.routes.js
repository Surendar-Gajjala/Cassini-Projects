define(
    [
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/login.routes',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.routes',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/help/help.routes',
        'app/desktop/modules/main/main.routes',
        'app/desktop/modules/home/home.routes',
        'app/desktop/modules/classification/classification.routes',
        'app/desktop/modules/item/item.routes',
        'app/desktop/modules/bom/bom.routes',
        'app/desktop/modules/storage/storage.routes',
        'app/desktop/modules/inventory/inventory.routes',
        'app/desktop/modules/gatePass/gatePass.routes',
        'app/desktop/modules/inward/inward.routes',
        'app/desktop/modules/request/request.routes',
        'app/desktop/modules/issue/issue.routes',
        'app/desktop/modules/dispatch/dispatch.routes',
        'app/desktop/modules/settings/settings.routes',
        'app/desktop/modules/planning/planning.routes',
        'app/desktop/modules/procurement/procurement.routes',
        'app/desktop/modules/reports/reports.routes',
        'app/desktop/modules/summary/summary.routes'
    ],
    function (loginRouteConfig,
              adminRouteConfig,
              helpRouteConfig,
              mainRouteConfig,
              homeRouteConfig,
              classificationRouteConfig,
              itemRouteConfig,
              bomRouteConfig,
              storageRouteConfig,
              inventoryRouteConfig,
              gatePassRouteConfig,
              inwardRouteConfig,
              requestRouteConfig,
              issueRouteConfig,
              dispatchRouteConfig,
              settingsRouteConfig,
              planningRouteConfig,
              procurementRouteConfig,
              reportsRouteConfig,
              summaryRouteConfig) {

        var defaultRouteConfig = {
            otherwise: '/app/home',
            routes: {}
        };

        return [
            defaultRouteConfig,
            loginRouteConfig,
            adminRouteConfig,
            helpRouteConfig,
            mainRouteConfig,
            homeRouteConfig,
            classificationRouteConfig,
            itemRouteConfig,
            bomRouteConfig,
            storageRouteConfig,
            inventoryRouteConfig,
            gatePassRouteConfig,
            inwardRouteConfig,
            requestRouteConfig,
            issueRouteConfig,
            dispatchRouteConfig,
            settingsRouteConfig,
            planningRouteConfig,
            procurementRouteConfig,
            reportsRouteConfig,
            summaryRouteConfig
        ];
    }
);