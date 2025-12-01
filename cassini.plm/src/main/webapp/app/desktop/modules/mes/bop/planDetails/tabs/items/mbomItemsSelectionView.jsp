<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    #rightSidePanelContent {
        overflow: hidden !important;
    }

    .item-selection-table table th {
        position: -webkit-sticky;
        position: sticky;
        top: -1px !important;
        z-index: 5 !important;
    }

    .table-div {
        position: absolute;
        top: 85px !important;
        bottom: 50px !important;
        left: 0;
        right: 0;
    }

    /*.open > .dropdown-toggle.btn {
        color: #091007 !important;
    }*/
</style>
<div>
    <div class="row">
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-5" style="padding:5px 0">
                <div style="">
                    <span style="" translate>SELECTED_PARTS</span>
                    <span class="badge">{{mbomItemsSelectionVm.selectedMbomItems.length}}</span>
                    <a href="" ng-click="mbomItemsSelectionVm.clearSelection()"
                       ng-if="mbomItemsSelectionVm.selectedMbomItems.length > 0">Clear</a>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="col-md-12 table-div" style="padding:0; height: auto;overflow: auto;">
    <div class="item-selection-table">
        <table class="table table-striped">
            <thead>
            <tr>
                <th style="text-align: center;width: 20px !important;">
                    <input name="mBomItem"
                           type="checkbox" ng-model="mbomItemsSelectionVm.selectAllCheck"
                           ng-click="mbomItemsSelectionVm.selectAll(check)" ng-checked="check">
                </th>
                <th style="width:1px !important;white-space: nowrap;text-align: left;" translate>ITEM_NUMBER</th>
                <th translate>ITEM_TYPE</th>
                <th translate>ITEM_NAME</th>
                <th class="col-width-100" style="text-align: center" translate>REVISION</th>
                <th class="col-width-75" style="text-align: center" translate>QUANTITY</th>
                <th class="col-width-75" style="text-align: center" translate>CONSUMED</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="mbomItemsSelectionVm.loading == true">
                <td colspan="25">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>LOADING_PARTS</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="mbomItemsSelectionVm.loading == false && mbomItemsSelectionVm.mBomItems.content.length == 0">
                <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/MBOM.png" alt="" class="image">

                        <div class="message" translate>NO_PARTS</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>
            <tr ng-repeat="mBomItem in mbomItemsSelectionVm.mBomItems">
                <td style="vertical-align: middle;text-align: center;width: 20px !important;">
                    <input name="mBomItem" type="checkbox" ng-if="!mBomItem.hasBom"
                           ng-model="mBomItem.selected"
                           title="{{(mBomItem.quantity - mBomItem.consumedQty) == 0 ? 'Part already consumed':'Item already exist'}}"
                           ng-disabled="mBomItem.alreadyExist || ((mBomItem.quantity - mBomItem.consumedQty) == 0)"
                           ng-click="mbomItemsSelectionVm.selectCheck(mBomItem)">
                </td>
                <td style="width:1px !important;white-space: nowrap;text-align: left;">
                        <span class="level{{mBomItem.level}}">
                            <i ng-if="mBomItem.hasBom" class="fa"
                               style="cursor: pointer;margin-right: 0;"
                               ng-class="{'fa-caret-right': (mBomItem.expanded == false || mBomItem.expanded == null || mBomItem.expanded == undefined),
                                          'fa-caret-down': mBomItem.expanded == true}"
                               ng-click="mbomItemsSelectionVm.toggleMBOMItemNode(mBomItem)"></i>
                            <span ng-if="mBomItem.type == 'NORMAL'" ng-class="{'ml9': mBomItem.hasBom == false}">{{mBomItem.itemNumber}}</span>
                            <span ng-if="mBomItem.type == 'PHANTOM'" ng-class="{'ml9': mBomItem.hasBom == false}">{{mBomItem.phantomNumber}}</span>
                        </span>
                </td>
                <td style="word-wrap: break-word;white-space: normal !important;">
                    <span ng-if="mBomItem.type == 'NORMAL'">{{mBomItem.itemTypeName}}</span>
                    <span ng-if="mBomItem.type == 'PHANTOM'">Phantom</span>
                </td>
                <td style="word-wrap: break-word;white-space: normal !important;">
                    <span ng-if="mBomItem.type == 'NORMAL'">{{mBomItem.itemName}}</span>
                    <span ng-if="mBomItem.type == 'PHANTOM'">{{mBomItem.phantomName}}</span>
                </td>
                <td class="col-width-75" style="text-align: center">{{mBomItem.revision}}</td>
                <td class="col-width-75" style="text-align: center">
                    {{mBomItem.quantity}}
                </td>
                <td class="col-width-75" style="text-align: center">
                    {{mBomItem.consumedQty}}
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
</div>