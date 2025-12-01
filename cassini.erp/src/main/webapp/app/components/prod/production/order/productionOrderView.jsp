
    <script type="text/ng-template" id="productionorders-view-tb">

         <button class="btn btn-primary mr5 btn-sm" data-ng-click="addProducts();" ng-if="((status=='NEW' || status=='ADDITEMS' || status=='CREATED') && (!onViewOrder || onEditOrder) && !saveMode)">
           <i class="glyphicon glyphicon-plus"></i> Add Products
         </button>

         <button class="btn btn-primary mr5 btn-sm" data-ng-click="editItems();" ng-if="onViewOrder && status=='CREATED'">Edit PO Items</button>

         <button class="btn btn-primary mr5 btn-sm" data-ng-click="addItems();" ng-if="(status=='ADDITEMS' || onEditOrder)">Add/Edit Items</button>
         <button ng-disabled="prodOrderItems.content.length==0" class="btn btn-primary mr5 btn-sm" data-ng-click="createProductionOrder();" ng-if="(status=='CREATORDER' && !onEditOrder)">Create Order</button>

         <button class="btn btn-primary mr5 btn-sm" data-ng-click="saveProductionOrder('CREATED');" ng-if="(status=='CREATED' && saveMode)">Save Order </button>

         <button class="btn btn-primary mr5 btn-sm" data-ng-click="saveProductionOrder('APPROVED');" ng-if="(status=='CREATED' && onViewOrder)">Approve</button>


         <button class="btn btn-primary mr5 btn-sm" data-ng-click="saveProductionOrder('INPRODUCTION');" ng-if="status=='APPROVED'">Start</button>


         <button class="btn btn-primary mr5 btn-sm" data-ng-click="saveProductionOrder('COMPLETED');" ng-if="status=='INPRODUCTION'">Finish</button>

        <div style="float: right;padding: 8px 12px;"><strong>Order Total: </strong>{{orderTotal}}</div>
    </script>
    <div ng-include="template"></div>


