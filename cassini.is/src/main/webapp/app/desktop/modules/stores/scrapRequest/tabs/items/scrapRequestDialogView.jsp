<style>
    .table {
        border-spacing: 0;
        border-collapse: unset;
    }

</style>
<div style="position: relative;">
    <div style="overflow-y: auto;overflow-x: hidden;height: 100%;">

        <div style="margin: 0;">

            <div class="responsive-table" style="padding: 10px;margin-bottom: 50px !important;">
                <table class="table table-striped">
                    <thead>
                    <tr>

                        <th style="vertical-align: middle">Item Number</th>
                        <th style="vertical-align: middle">Item Name</th>
                        <th style="vertical-align: middle">Item Description</th>
                        <th style="vertical-align: middle">Scrap <br>Qty</th>

                    </tr>
                    </thead>
                    <tbody>

                    <tr ng-if="scrapRequestDialogVm.itemList.length == 0">
                        <td colspan="12">
                            <span>No Items are available to view</span>
                        </td>
                    </tr>

                    <tr ng-repeat="item in scrapRequestDialogVm.itemList">

                        <td style="vertical-align: middle;">
                            {{item.itemNumber }}
                        </td>
                        <td style="vertical-align: middle;width: 150px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                            title="{{item.itemName}}">
                            {{item.itemName }}
                        </td>

                        <td style="vertical-align: middle;width: 200px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                            title="{{item.description}}">
                            {{item.description }}
                        </td>

                        <td>
                            <input placeholder="Qty" class="form-control" type="text" style="width:50px;"
                                   data-ng-model="item.quantity">
                        </td>

                    </tr>
                    </tbody>
                </table>
            </div>

        </div>
    </div>
    <div id="appSidePanelButtonsPanel" class='buttons-panel' style="display: none">
        <button class="btn btn-sm btn-success" style="min-width: 80px"
                ng-click="scrapRequestDialogVm.create()" title="Click to select items">Select
        </button>
    </div>
</div>
