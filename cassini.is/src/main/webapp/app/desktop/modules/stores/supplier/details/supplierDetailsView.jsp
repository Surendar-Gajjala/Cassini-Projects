<style scoped>
    #store-container #store-content {
        padding: 10px;
        position: relative;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    .item-details > div.row.master-att:last-child,
    .item-details > div.row.revision-att:last-child {
        border-bottom: 0 !important;
    }
</style>
<div id="store-container" class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="min-width btn btn-sm btn-default" ng-click="supplierDetailsVm.back()">Back</button>
        </div>
    </div>
    <div id="store-content" class="view-content no-padding" style="overflow-y: auto;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="item-details col-sm-10 col-sm-offset-1" style="padding: 30px;">

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Name: </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="#" editable-text="supplierDetailsVm.supplier.name"
                           ng-if="hasPermission('permission.suppliers.edit')"
                           onaftersave="supplierDetailsVm.updateSupplier()">{{supplierDetailsVm.supplier.name}}</a>

                        <p ng-if="hasPermission('permission.suppliers.edit') == false">
                            {{supplierDetailsVm.supplier.name}}}}</p>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Description: </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="#" editable-text="supplierDetailsVm.supplier.description"
                           ng-if="hasPermission('permission.suppliers.edit')"
                           onaftersave="supplierDetailsVm.updateSupplier()">
                            {{supplierDetailsVm.supplier.description || 'Click to enter description'}}
                        </a>

                        <p ng-if="hasPermission('permission.suppliers.edit') == false">
                            {{supplierDetailsVm.supplier.description}}}}</p>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Contact Name: </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="#" editable-text="supplierDetailsVm.supplier.contactPerson"
                           ng-if="hasPermission('permission.suppliers.edit')"
                           onaftersave="supplierDetailsVm.updateSupplier()">{{supplierDetailsVm.supplier.contactPerson
                            || 'Click to enter name'}}</a>

                        <p ng-if="hasPermission('permission.suppliers.edit') == false">
                            {{supplierDetailsVm.supplier.contactPerson}}}}</p>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Contact Phone: </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="#" editable-text="supplierDetailsVm.supplier.contactPhone"
                           ng-if="hasPermission('permission.suppliers.edit')"
                           onaftersave="supplierDetailsVm.updateSupplier()">{{supplierDetailsVm.supplier.contactPhone ||
                            'Click to enter phone'}}</a>

                        <p ng-if="hasPermission('permission.suppliers.edit') == false">
                            {{supplierDetailsVm.supplier.contactPhone}}}}</p>
                    </div>
                </div>

                <attributes-details-view attribute-id="supplierId" attribute-type="SUPPLIER"
                                         has-permission="hasPermission('permission.suppliers.edit')"></attributes-details-view>

            </div>
        </div>
    </div>
</div>



