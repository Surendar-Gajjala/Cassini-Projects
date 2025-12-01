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
                        <a href="" editable-text="storeBasicVm.store.storeName"
                           ng-if="hasPermission('permission.topStores.edit') == true"
                           onaftersave="storeBasicVm.updateStore()">
                            {{storeBasicVm.store.storeName}}</a>

                        <p ng-if="hasPermission('permission.topStores.edit') == false">
                            {{storeBasicVm.store.storeName}}</p>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Description: </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="" editable-text="storeBasicVm.store.description"
                           ng-if="hasPermission('permission.topStores.edit') == true"
                           onaftersave="storeBasicVm.updateStore()">
                            {{storeBasicVm.store.description || 'Click to enter description'}}</a>

                        <p ng-if="hasPermission('permission.topStores.edit') == false">
                            {{storeBasicVm.store.description}}</p>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Location : </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="" editable-text="storeBasicVm.store.locationName"
                           ng-if="hasPermission('permission.topStores.edit') == true"
                           onaftersave="storeBasicVm.updateStore()">
                            {{storeBasicVm.store.locationName || 'Click to enter location'}}</a>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Created By : </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{storeBasicVm.store.createdByPerson.firstName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Created On : </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{storeBasicVm.store.createdDate}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Modified By : </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{storeBasicVm.store.modifiedByPerson.firstName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Modified On : </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{storeBasicVm.store.modifiedDate}}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>



