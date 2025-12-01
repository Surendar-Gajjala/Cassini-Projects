<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="min-width btn btn-sm btn-default" ng-click="storeVm.back()">Back</button>
            <%--<button ng-show="storeVm.tabs.basic.active" class="min-width btn btn-sm btn-info"
                    ng-click="storeVm.updateStore()">Update
            </button>--%>
            <button ng-show="storeVm.tabs.basic.active" class="min-width btn btn-sm btn-success"
                    ng-click="storeVm.stockReceived()"
                    ng-disabled="selectedProject.locked == true || hasPermission('permission.stores.receive') == false">
                Receive Stock
            </button>
            <button ng-show="storeVm.tabs.basic.active" class="min-width btn btn-sm btn-warning"
                    ng-click="storeVm.stockIssued()"
                    ng-disabled="selectedProject.locked == true  || hasPermission('permission.stores.issue') == false">
                Issue Stock
            </button>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="storeVm.activeTab">
                        <uib-tab heading="{{storeVm.tabs.basic.heading}}"
                                 select="storeVm.storeDetailsTabActivated(storeVm.tabs.basic.id)">
                            <div ng-include="storeVm.tabs.basic.template"
                                 ng-controller="StoreBasicController as storeBasicVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{storeVm.tabs.inventory.heading}}"
                                 ng-if="hasPermission('permission.stores.inventory') == true"
                                 select="storeVm.storeDetailsTabActivated(storeVm.tabs.inventory.id)">
                            <div ng-include="storeVm.tabs.inventory.template"
                                 ng-controller="StoreInventoryController as inventoryVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{storeVm.tabs.stockMovement.heading}}"
                                 ng-if="hasPermission('permission.stores.stockMovement') == true"
                                 select="storeVm.storeDetailsTabActivated(storeVm.tabs.stockMovement.id)">
                            <div ng-include="storeVm.tabs.stockMovement.template"
                                 ng-controller="StockMovementController as stockMovVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
