<div ng-if="purchaseOrderBasicDetailsVm.loading == true" style="padding: 30px;">
    <br/>
                <span style="font-size: 15px;">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">Loading...
                </span>
    <br/>
</div>
<div class="row row-eq-height" style="margin: 0;">
    <div class="item-details col-sm-10 col-sm-offset-1" style="padding: 30px;">

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>PO Number :<span class="asterisk">*</span></span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{purchaseOrderBasicDetailsVm.purchaseOrder.poNumber}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>PO Date :<span class="asterisk">*</span></span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{purchaseOrderBasicDetailsVm.purchaseOrder.poDate}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Supplier :<span class="asterisk">*</span></span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{purchaseOrderBasicDetailsVm.purchaseOrder.supplier}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Status : <span class="asterisk">*</span></span>
            </div>

            <div class="value col-xs-8 col-sm-9">
                <span style="color: white" class="label" ng-class="{
                                    'label-success': purchaseOrderBasicDetailsVm.purchaseOrder.status == 'NEW',
                                    'label-info': purchaseOrderBasicDetailsVm.purchaseOrder.status == 'APPROVED'}">{{purchaseOrderBasicDetailsVm.purchaseOrder.status}}
                        </span>
            </div>


        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Approved By:<span class="asterisk">*</span> </span>
            </div>

            <div class="value col-xs-8 col-sm-9">
                <a style="color:#428bca;" href="#"
                   ng-if="purchaseOrderBasicDetailsVm.purchaseOrder.status != 'APPROVED' && hasPermission('permission.purchaseOrders.editPurchaseOrder')"
                   editable-text="purchaseOrderBasicDetailsVm.purchaseOrder.approvedBy"
                   onaftersave="purchaseOrderBasicDetailsVm.update()">
                    {{purchaseOrderBasicDetailsVm.purchaseOrder.approvedBy || 'Click to enter ApprovedBy'}}</a>

                <p ng-if="purchaseOrderBasicDetailsVm.purchaseOrder.status == 'APPROVED' || !hasPermission('permission.purchaseOrders.editPurchaseOrder')">
                    {{purchaseOrderBasicDetailsVm.purchaseOrder.approvedBy}}</p>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Notes: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a style="color:#428bca;" href="#"
                   ng-if="purchaseOrderBasicDetailsVm.purchaseOrder.status != 'APPROVED' && hasPermission('permission.purchaseOrders.editPurchaseOrder')"
                   editable-text="indentBasicDetailsVm.purchaseOrder.notes"
                   onaftersave="purchaseOrderBasicDetailsVm.update()">
                    {{purchaseOrderBasicDetailsVm.purchaseOrder.notes || 'Click to enter Notes'}}</a>

                <p ng-if="purchaseOrderBasicDetailsVm.purchaseOrder.status == 'APPROVED' || !hasPermission('permission.purchaseOrders.editPurchaseOrder')">
                    {{purchaseOrderBasicDetailsVm.purchaseOrder.notes}}</p>
            </div>
        </div>

        <attributes-details-view attribute-id="purchaseOrderId" attribute-type="CUSTOM_PURCHASEORDER"
                                 has-permission="hasPermission('permission.purchaseOrders.editPurchaseOrder')"></attributes-details-view>

    </div>
</div>
