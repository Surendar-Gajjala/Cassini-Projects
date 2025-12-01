<div>
    <!--
    <div style="text-align: center">
        <ul class="quick-actions" style="padding-left: 0px;">
            <li><a ui-sref="app.crm.customers"><i class="icon-people-y"></i><span class="action-btn-label">Manage Customers</span></a></li>
            <li><a ui-sref="app.crm.orders.all"><i class="icon-product-sales"></i><span class="action-btn-label">Manage Orders</span></a></li>
            <li><a ui-sref="app.prod.products"><i class="icon-products"></i><span class="action-btn-label">Manage Products</span></a></li>
            <li><a href=""><i class="icon-production"></i><span class="action-btn-label">Manage Production</span></a></li>
            <li><a ui-sref="app.prod.inventory.products"><i class="icon-inventory"></i><span class="action-btn-label">Manage Inventory</span></a></li>
            <li><a ui-sref="app.crm.returns.all"><i class="icon-forward-b"></i><span class="action-btn-label">Manage Returns</span></a></li>
            <li><a ui-sref="app.crm.salesreps"><i class="icon-group"></i><span class="action-btn-label">Manage Sales Reps</span></a></li>
            <li><a ui-sref="app.crm.salesregions"><i class="icon-region"></i><span class="action-btn-label">Manage Regions</span></a></li>
            <li><a ui-sref="app.admin.security.logins"><i class="icon-lock"></i><span class="action-btn-label">Manage Security</span></a></li>
            <li><a ui-sref="app.hrm.payroll"><i class="icon-calendar-lb"></i><span class="action-btn-label">Manage Payroll</span></a></li>
            <li><a ui-sref="app.hrm.employees"><i class="icon-people-b"></i><span class="action-btn-label">Manage Employees</span></a></li>
        </ul>
    </div>
    -->

    <div class="row">
        <div class="col-md-6" ng-controller="OrdersWidgetController">
            <div ng-include="templates.ordersWidget"></div>
        </div>
        <div class="col-md-6" ng-controller="ReturnsWidgetController">
            <div ng-include="templates.returnsWidget"></div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6" ng-controller="FieldReportsWidgetController">
            <div ng-include="templates.customerReports"></div>
        </div>
        <div class="col-md-6" ng-controller="LowInventoryWidgetController">
            <div ng-include="templates.lowInventoryWidget"></div>
        </div>
    </div>





    <!--
    <div class="row">
        <div class="col-md-12" ng-controller="DailySalesReportController">
            <div ng-include="'app/components/home/widgets/reports/dailySalesReportView.jsp'"></div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6">
            <report-widget ng-init="barColors1 = ['#1CAF9A']; labels = ['Sales']" bar-colors="barColors1" labels="labels"
                           report="reports.crm.customers-per-salesrep"></report-widget>
        </div>
        <div class="col-md-6">
            <report-widget ng-init="barColors2 = ['#5BC0DE']; labels = ['Sales']" bar-colors="barColors2"  labels="labels"
                           report="reports.crm.orders-per-region"></report-widget>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6" ng-controller="ProductVsSampleSalesController">
            <div ng-include="'app/components/home/widgets/reports/productVsSampleSalesView.jsp'"></div>
        </div>
        <div class="col-md-6" ng-controller="BookSalesReportController">
            <div ng-include="'app/components/home/widgets/reports/bookSalesReportView.jsp'"></div>
        </div>
    </div>
    -->
</div>