<div>
    <style scoped>
        .adjust-height {
            height: calc(100% - 31px);
        }

    </style>
    <div class="form-group" style="margin: 0;padding-top: 10px;text-align: center;"
         ng-if="itemWhereUsedVm.bomItems.length > 0">
        <div class="ckbox ckbox-default" style="display: inline-block;">
            <input id="revisions" type="checkbox" ng-model="itemWhereUsedVm.showAllRevisions"
                   style="width: 25px;box-shadow: none;margin-top: 2px;"
                   ng-change="itemWhereUsedVm.loadAllRevisions(itemWhereUsedVm.showAllRevisions)">
            <label for="revisions" class="item-selection-checkbox" style="padding:0;margin: 0 !important;"
                   translate>SHOW_ALL_REVISIONS</label>
        </div>
    </div>
    <div class='responsive-table' id="whereUsedTable" style="top: 42px;">
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th style="width: 1% !important;white-space: nowrap;" translate>ITEM_NUMBER</th>
                <th class="col-width-250" translate>ITEM_NAME</th>
                <th translate>ITEM_TYPE</th>
                <th class="description-column" translate>DESCRIPTION</th>
                <th style="text-align: center" translate>REVISION</th>
                <th translate>LIFE_CYCLE_PHASE</th>
                <th style="text-align: center" translate>QUANTITY</th>
                <th style="text-align: center" translate>UNITS</th>
                <th translate>REF_DES</th>
                <th class="description-column" translate>NOTES</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="itemWhereUsedVm.loading == true">
                <td colspan="9">
                <span style="font-size: 15px;">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                         class="mr5"><span translate>LOADING_ITEMS</span>
                </span>
                </td>
            </tr>

            <tr ng-if="itemWhereUsedVm.loading == false && itemWhereUsedVm.bomItems.length == 0">
                <td colspan="11"><span translate>NO_ITEMS</span></td>
            </tr>

            </tbody>
        </table>
    </div>
</div>