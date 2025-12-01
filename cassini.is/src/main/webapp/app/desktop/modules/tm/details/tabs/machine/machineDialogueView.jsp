<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }
</style>
<div style="padding: 0px 20px;">
    <h4 class="section-title" style="margin: 0px;" translate>FILTERS</h4>

    <form class="form-inline" style="margin: 5px 0px;">
        <free-text-search style=" width: 300px; margin-right: 10px; position: absolute;top: 15px;right: 30px;"
                          on-clear="machineDialogueVm.resetPage"
                          on-search="machineDialogueVm.freeTextSearch"></free-text-search>

    </form>
    <hr style="margin: 0px;">
    <div class="row">
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-4">
                <div style="padding: 10px;">
                    <span style="color:#1877f2e6">Selected Items</span>
                    <span class="badge">{{machineDialogueVm.selectedMachines.length}}</span>
                </div>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                         <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{(machineDialogueVm.machines.numberOfElements * (machineDialogueVm.machines.page + 1))}} of
                                            {{machineDialogueVm.machines.totalElements}}
                                    </span>
                            </medium>
                        </span>
                        <span class="mr10">Page {{machineDialogueVm.machines.totalElements != 0 ? machineDialogueVm.machines.number+1:0}} of {{machineDialogueVm.machines.totalPages}}</span>
                        <a href="" ng-click="machineDialogueVm.previousPage()"
                           ng-class="{'disabled': machineDialogueVm.machines.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="machineDialogueVm.nextPage()"
                           ng-class="{'disabled': machineDialogueVm.machines.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="text-align: center;">
                        <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                               ng-model="machineDialogueVm.selectedAll"
                               ng-click="machineDialogueVm.checkAll()"
                               ng-if="machineDialogueVm.machines.content.length > 1">
                    </th>
                    <th style="vertical-align: middle;">Machine Number</th>
                    <th style="vertical-align: middle;">Machine Name</th>
                    <th style="vertical-align: middle;">Units</th>
                    <th style="vertical-align: middle;">Qty</th>
                    <th style="vertical-align: middle;">Group</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="machineDialogueVm.loading == true">
                    <td colspan="10">
                            <span style="font-size: 15px;">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading Machines..
                            </span>
                    </td>
                </tr>
                <tr ng-if="machineDialogueVm.machines.content.length == 0 && !machineDialogueVm.loading ">
                    <td colspan="11" style="padding-left: 30px;">No Machines are available to view</td>
                </tr>
                <tr ng-repeat="machine in machineDialogueVm.machines.content"
                    ng-if="machineDialogueVm.loading == false">
                    <th style="width: 50px; text-align: center">
                        <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                               ng-click="machineDialogueVm.select(machine)"
                               ng-model="machine.selected">
                    </th>

                    <td style="vertical-align: middle;"><span
                            ng-bind-html="machine.itemNumber | highlightText: freeTextQuery"></span></td>
                    <td style="vertical-align: middle;"><span
                            ng-bind-html="machine.itemName | highlightText: freeTextQuery"></span></td>
                    <td style="vertical-align: middle;"><span
                            ng-bind-html="machine.units | highlightText: freeTextQuery"></span></td>
                    <td style="vertical-align: middle;"><span
                            ng-bind-html="machine.quantity"></span></td>
                    <td style="vertical-align: middle;"><span
                            ng-bind-html="machine.boqName | highlightText: freeTextQuery"></span></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>