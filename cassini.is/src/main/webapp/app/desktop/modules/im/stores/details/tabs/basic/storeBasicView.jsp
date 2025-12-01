<div class="view-container">
    <div class="view-content">
        <div class="row row-eq-height" style="margin: 0">
            <div class="item-details col-sm-8" style="padding: 30px;">
                <h4 class="section-title" style="margin-left: -10px; margin-right: -10px;">Basic Information</h4>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Name: </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a ng-if="selectedProject.locked == false && hasPermission('permission.stores.edit') == true"
                           href="#" editable-text="storeBasicVm.store.name"
                           onaftersave="storeBasicVm.updateStore()">{{storeBasicVm.store.name}}</a>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Description: </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a ng-if="selectedProject.locked == false && hasPermission('permission.stores.edit') == true"
                           href="#"
                           editable-text="storeBasicVm.store.description"
                           onaftersave="storeBasicVm.updateStore()">
                            {{storeBasicVm.store.description || 'Click to enter description'}}
                        </a>
                    </div>
                </div>
                <attributes-details-view attribute-id="storeId" attribute-type="STORE"></attributes-details-view>
            </div>
        </div>
    </div>
</div>



