<style scoped>
    #freeTextSearchDirective {
        top: 7px !important;
    }

    .view-content {
        position: relative;
    }

    .responsive-table {
        width: 100%;
        margin-bottom: 0;
        padding-bottom: 20px;
        overflow-y: visible;
        overflow-x: visible;
    }

    .projectName {
        display: run-in;
        word-wrap: break-word;
        width: 150px !important;
        white-space: normal;
        text-align: left;
    }

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    .view-content .table-footer {
        padding: 0 10px 0 10px;
        position: absolute;
        bottom: 0;
        height: 40px;
        width: 100%;
        border-top: 1px solid #D3D7DB;
        display: table;
    }

    .view-content .table-footer > div {
        display: table-row;
        line-height: 30px;
    }

    .view-content .table-footer > div h5 {
        margin: 0;
    }

    .view-content .table-footer > div > div {
        display: table-cell;
        vertical-align: middle;
    }

    .view-content .table-footer > div > div > i {
        font-size: 16px;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .responsive-table {
        width: 100%;
        margin-bottom: 0;
        padding-bottom: 20px;
        overflow-y: visible;
        overflow-x: visible;
    }

    .view-content {
        position: relative;
    }

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    .view-content .responsive-table table thead {
        position: -webkit-sticky;
        position: sticky;
        top: -15px;
        background-color: #fff;
    }

</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-size: 20px;font-weight: bold;color: black;padding: 13px 10px 14px;vertical-align: middle;border-right: 1px solid lightgray;">{{viewInfo.title}}</span>
        <button class="width btn btn-sm btn-default" ng-click="itemStoreInventoryVm.back()">Back</button>
        <free-text-search on-clear="itemStoreInventoryVm.resetPage"
                          on-search="itemStoreInventoryVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;">
        <div class="responsive-table" style="padding:10px;">
            <table class="table table-striped table-bordered highlight-row">
                <thead>
                <tr ng-if="itemStoreInventoryVm.loading == false && itemStoreInventoryVm.inventories.length > 0">
                    <th style="vertical-align: middle; width: 50px;" rowspan="2"></th>
                    <th style="vertical-align: middle; width: 150px;" rowspan="2">Item Number</th>
                    <th style="vertical-align: middle; width: 150px;" rowspan="2">Item Name</th>
                    <%--<th style="vertical-align: middle; width: 150px;" rowspan="2">Item Description</th>--%>
                    <th style="vertical-align: middle; width: 150px;" rowspan="2">Item Type</th>
                    <th style="vertical-align: middle; width: 150px;" rowspan="2">Units</th>
                    <th style="text-align: center;width: {{itemStoreInventoryVm.colspan * 80}}px !important;"
                        colspan={{itemStoreInventoryVm.colspan}}>Inventory Qty
                    </th>
                    <th style="text-align: center;width: {{itemStoreInventoryVm.colspan * 80}}px !important;"
                        colspan={{itemStoreInventoryVm.colspan}}
                        ng-repeat="storeDetail in itemStoreInventoryVm.inventories[0].storeDetailsList">
                        Qty in {{storeDetail.storeName}}
                    </th>
                </tr>
                <tr ng-if="itemStoreInventoryVm.loading == false && itemStoreInventoryVm.inventories.length >= 0">
                    <th style="width: 150px; text-align: center">Total</th>
                    <th class="projectName"
                        ng-repeat="invProject in itemStoreInventoryVm.inventories[0].projectInventoryDetailsList"
                        title="{{invProject.projectName}}">
                        {{invProject.projectName | limitTo: 15}}{{invProject.projectName.length > 15 ? '...' : ''}}
                    </th>
                    <th class="projectName" title="{{invProject.projectName}}"
                        ng-repeat="invProject in itemStoreInventoryVm.inventories[0].storeDetails">
                        {{invProject.projectName | limitTo: 15}}{{invProject.projectName.length > 15 ? '...' : ''}}
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="itemStoreInventoryVm.loading == false && itemStoreInventoryVm.inventories.length == 0">
                    <td colspan="6">No Data available to view</td>
                </tr>
                <tr ng-if="itemStoreInventoryVm.loading == true">
                    <td colspan="10">
                            <span style="font-size: 15px;">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading Inventory..
                            </span>
                    </td>
                </tr>
                <tr ng-repeat="invStoreDetails in itemStoreInventoryVm.inventories"
                    ng-if="itemStoreInventoryVm.loading == false">
                    <td ng-if="invStoreDetails.resourceType == 'MATERIALTYPE' "
                        style="vertical-align: middle; width: 50px; text-align: center"><img
                            ng-src="app/assets/images/cart.png"
                            style="vertical-align: middle; height:16px; width:16px;"></td>
                    <td ng-if="invStoreDetails.resourceType == 'MACHINETYPE' " style="width: 50px; text-align: center">
                        <img
                                ng-src="app/assets/images/machine2.png" style="height:16px; width:16px;"></td>
                    <td style="vertical-align: middle; width: 150px;">{{invStoreDetails.itemNumber}}</td>
                    <td style="vertical-align: middle; width: 150px;max-width:150px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                        title="{{invStoreDetails.itemName}}">{{invStoreDetails.itemName}}
                    </td>
                    <%--<td ng-if="invStoreDetails.description != null" style="vertical-align: middle;width: 200px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"--%>
                    <%--title="{{invStoreDetails.description}}">{{invStoreDetails.description}}</td>--%>
                    <td style="vertical-align: middle; width: 150px;">{{invStoreDetails.itemType}}</td>
                    <td style="vertical-align: middle; width: 150px;">{{invStoreDetails.units}}</td>
                    <td style="vertical-align: middle; text-align: center">{{invStoreDetails.totalQuantity}}</td>
                    <td style="vertical-align: middle; text-align: center"
                        ng-repeat="invProject in invStoreDetails.projectInventoryDetailsList">{{invProject.quantity}}
                    </td>
                    <td style="vertical-align: middle; text-align: center"
                        ng-repeat="storeDetail in invStoreDetails.storeDetails">
                        {{storeDetail.quantity}}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <h5 style="font-weight: 700">Displaying {{itemStoreInventoryVm.inventories[0].numberOfElements}} of
                        {{itemStoreInventoryVm.inventories[0].totalElements}}</h5>
                </div>

                <div class="text-right">
                    <span class="mr10">Page {{itemStoreInventoryVm.pageable.page + 1}} of {{itemStoreInventoryVm.inventories[0].totalPages}}</span>
                    <a href="" ng-click="itemStoreInventoryVm.previousPage()"
                       ng-class="{'disabled': itemStoreInventoryVm.pageable.page == 0}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="itemStoreInventoryVm.nextPage()"
                       ng-class="{'disabled': itemStoreInventoryVm.inventories[0].last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>
    </div>
</div>
