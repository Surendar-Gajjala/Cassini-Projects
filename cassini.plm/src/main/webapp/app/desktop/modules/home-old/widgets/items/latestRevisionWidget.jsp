<div class="panel panel-default panel-alt widget-messaging">
    <div class="panel-heading" style="">

        <div class="col-sm-6" style="margin-top: 15px;margin-left: 15px;color: #002451;font-size: 16px;">
            <span style="font-weight: bold;" translate>LATEST_REVISION_UPDATE_ITEMS</span>
        </div>

        <div class="pull-right text-center" style="padding: 2px;">
             <span ng-if="itemWidgetVm.loading == false"><small>
                 {{itemWidgetVm.items.totalElements}}
                 <span translate>ITEMS_ALL_TITLE</span>
             </small></span>

            <div class="btn-group" style="">
                <button class="btn btn-xs btn-default"
                        ng-click="itemWidgetVm.previousPage()"
                        ng-disabled="itemWidgetVm.items.first">
                    <i class="fa fa-chevron-left"></i>
                </button>
                <button class="btn btn-xs btn-default"
                        ng-click="itemWidgetVm.nextPage()"
                        ng-disabled="itemWidgetVm.items.last">
                    <i class="fa fa-chevron-right"></i>
                </button>
            </div>

        </div>
    </div>

    <div class="panel-body">
        <div class="widget-panel" style="max-height: 400px; min-height: 400px;">

            <div class="responsive-table" style="padding: 10px;">
                <table class="table table-striped highlight-row">
                    <thead>
                    <tr style="font-size: 14px;">
                        <th style="width: 150px" translate>ITEM_NUMBER</th>
                        <th style="width: 150px" translate>ITEM_TYPE</th>
                        <th class="col-width-200" translate>ITEM_NAME</th>
                        <th style="width: 150px; text-align: center;" translate>REVISION</th>
                        <th style="width: 150px" translate>RELEASED_DATE</th>
                        <%--     <th style="width: 100px">Units</th>--%>
                    </tr>
                    </thead>

                    <tbody>
                    <tr ng-if="itemWidgetVm.loading == true">
                        <td colspan="7">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5"><span translate>LOADING_ITEMS</span>
                        </span>
                        </td>
                    </tr>

                    <tr ng-if="itemWidgetVm.loading == false && itemWidgetVm.items.content.length == 0">
                        <td colspan="7"><span translate>NO_ITEMS</span></td>
                    </tr>
                    <tr ng-repeat="item in itemWidgetVm.items.content" style="font-size: 14px;">
                        <td style="width: 150px">
                            <a href="" ng-click="itemWidgetVm.showItem(item)">{{item.itemMasterObject.itemNumber}}</a>
                        </td>
                        <td style="width: 150px">{{item.itemMasterObject.itemType.name}}</td>
                        <td class="col-width-200">{{item.itemMasterObject.itemName}}</td>
                        <td style="width: 150px; text-align: center;">{{item.revision}}</td>
                        <td style="width: 150px">
                            <span>{{item.releasedDate}}</span>
                        </td>
                        <%--<td style="width: 100px">{{item.itemMasterObject.units}}</td>--%>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>