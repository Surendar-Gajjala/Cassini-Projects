<div class="panel panel-default panel-alt widget-messaging">
    <div class="panel-heading" style="">

        <div class="col-sm-8" style="margin-top: 15px;margin-left: 15px;color: #002451;font-size: 16px;">
            <span style="font-weight: bold;" translate>MOST_USED_MFR_PARTS</span>
        </div>

        <div class="pull-right text-center" style="padding: 2px;">
             <span ng-if="partsVm.loading == false"><small>
                 {{partsVm.items.totalElements}}
                 <span translate>MANUFACTURER_DETAILS_TAB_PARTS</span>
             </small></span>

            <div class="btn-group" style="">
                <button class="btn btn-xs btn-default"
                        ng-click="partsVm.previousPage()"
                        ng-disabled="partsVm.items.first">
                    <i class="fa fa-chevron-left"></i>
                </button>
                <button class="btn btn-xs btn-default"
                        ng-click="partsVm.nextPage()"
                        ng-disabled="partsVm.items.last">
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
                        <th style="width: 150px" translate>PART_NUMBER</th>
                        <th style="width: 150px" translate>MANUFACTURER_PART_TYPE</th>
                        <th class="col-width-200" translate>MANUFACTURER</th>
                        <th style="width: 100px" translate>USED_QUANTITY</th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr ng-if="partsVm.loading == true">
                        <td colspan="7">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5"><span translate>LOADING_MFR_PARTS</span>
                        </span>
                        </td>
                    </tr>

                    <tr ng-if="partsVm.loading == false && partsVm.items.content.length == 0">
                        <td colspan="7"><span translate>NO_MFR_PARTS</span></td>
                    </tr>
                    <tr ng-repeat="item in partsVm.items.content" style="font-size: 14px;">
                        <td style="width: 150px">
                            <a href="" ng-click="partsVm.showMfrpartDetails(item)">{{item.partNumber}}</a>
                        </td>
                        <td style="width: 150px">{{item.mfrPartType.name}}</td>
                        <td class="col-width-200">{{item.manufacturerObject.name}}</td>
                        <td style="width: 100px; text-align: center;">{{item.count}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>