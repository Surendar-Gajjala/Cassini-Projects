<div ng-include="$parent.hasRole('Administrator') ? 'app/components/home/roles/admin/adminHomeView.jsp' : null"
     ng-controller="AdminHomeController"></div>

<div ng-include="$parent.hasRole('Sales Executive') ? 'app/components/home/roles/salesrep/salesRepHomeView.jsp' : null"
     ng-controller="SalesRepHomeController"></div>

<div ng-include="$parent.hasRole('Order Entry') ? 'app/components/home/roles/orderentry/orderEntryHomeView.jsp' : null"
     ng-controller="OrderEntryHomeController"></div>

<div ng-include="$parent.hasRole('Order Processing') ? 'app/components/home/roles/orderprocessing/orderProcessingHomeView.jsp' : null"
     ng-controller="OrderProcessingHomeController"></div>

<div ng-include="$parent.hasRole('Shipping Manager') ? 'app/components/home/roles/shipping/shippingManagerHomeView.jsp' : null"
     ng-controller="ShippingManagerHomeController"></div>

<div ng-include="$parent.hasRole('Warehouse Manager') ? 'app/components/home/roles/orderentry/warehouseManagerHomeView.jsp' : null"
     ng-controller="WarehouseManagerHomeController"></div>

<div ng-include="$parent.hasRole('HR Manager') ? 'app/components/home/roles/hr/hrManagerHomeView.jsp' : null"
     ng-controller="HrManagerHomeController"></div>