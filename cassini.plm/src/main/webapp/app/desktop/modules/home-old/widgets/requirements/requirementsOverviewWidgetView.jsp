<div class="panel panel-default panel-alt widget-messaging">
    <div class="panel-heading" style="">

        <div class="col-sm-6" style="margin-top: 15px;margin-left: 15px;color: #002451;font-size: 16px;">
            <span style="font-weight: bold;" translate>REQUIREMENTS</span>
        </div>

        <div class="pull-right text-center" style="padding: 2px;">
             <span ng-if="requirementsOverviewWidgetVm.loading == false"><small>
                 {{requirementsOverviewWidgetVm.items.totalElements}}
                 <span translate>ITEMS_ALL_TITLE</span>
             </small></span>

            <div class="btn-group" style="">
                <button class="btn btn-xs btn-default"
                        ng-click="requirementsOverviewWidgetVm.previousPage()"
                        ng-disabled="requirementsOverviewWidgetVm.items.first">
                    <i class="fa fa-chevron-left"></i>
                </button>
                <button class="btn btn-xs btn-default"
                        ng-click="requirementsOverviewWidgetVm.nextPage()"
                        ng-disabled="requirementsOverviewWidgetVm.items.last">
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
                        <th style="width: 150px" translate>Requirement No</th>
                        <th class="col-width-250" translate>Name</th>
                        <th style="width: 150px" translate>Specification No</th>
                        <th style="width: 150px" translate>Status</th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr ng-if="requirementsOverviewWidgetVm.loading == false && requirementsOverviewWidgetVm.requirements.content.length == 0">
                        <td colspan="7"><span translate>NO_REQUIREMENTS</span></td>
                    </tr>
                    <tr ng-repeat="req in requirementsOverviewWidgetVm.requirements.content" style="font-size: 14px;">
                        <td style="width: 150px">
                            <a href="" ng-click="requirementsOverviewWidgetVm.showItem(item)">{{req.}}</a>
                        </td>
                        <td style="width: 150px">{{req.itemMasterObject.itemType.name}}</td>
                        <td class="col-width-250">{{req.itemMasterObject.itemName}}</td>
                        <td id="td">{{req.itemMasterObject.description}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>