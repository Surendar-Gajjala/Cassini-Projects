<style scoped>

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar selector" style="top: 30px; background-color: white">
        <free-text-search on-clear="stockReturnSelectVm.resetPage"
                          on-search="stockReturnSelectVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{stockReturnSelectVm.stockReturns.numberOfElements}} of
                                            {{stockReturnSelectVm.stockReturns.numberOfElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{stockReturnSelectVm.stockReturns.totalElements != 0 ? stockReturnSelectVm.stockReturns.number+1:0}} of {{stockReturnSelectVm.stockReturns.totalPages}}</span>
                <a href="" ng-click="stockReturnSelectVm.previousPage()"
                   ng-class="{'disabled': stockReturnSelectVm.stockReturns.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="stockReturnSelectVm.nextPage()"
                   ng-class="{'disabled': stockReturnSelectVm.stockReturns.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">

        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px;">Select</th>
                <th>Return Number</th>
                <th>Project</th>
                <th>Store</th>
                <th>Status</th>
                <th>Notes</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="stockReturnSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>Loading Stock Returns</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="stockReturnSelectVm.loading == false && stockReturnSelectVm.stockReturns.content.length == 0">
                <td colspan="12">
                    <span translate>No Stock Returns</span>
                </td>
            </tr>

            <tr ng-repeat="stockReturn in stockReturnSelectVm.stockReturns.content | filter: search"
                ng-click="stockReturn.isChecked = !stockReturn.isChecked; stockReturnSelectVm.radioChange(stockReturn, $event)"
                ng-dblClick="stockReturn.isChecked = !stockReturn.isChecked; stockReturnSelectVm.selectRadioChange(stockReturn, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="stockReturn.isChecked" name="stockReturn" value="stockReturn"
                           ng-dblClick="stockReturnSelectVm.selectRadioChange(stockReturn, $event)"
                           ng-click="stockReturnSelectVm.radioChange(stockReturn, $event)">
                </td>
                <td>{{stockReturn.returnNumberSource}}</td>
                <td>{{stockReturn.projectObject.name}}</td>
                <td>{{stockReturn.storeObject.storeName}}</td>

                <td style="vertical-align: middle;width: 150px;">
                        <span style="color: white;font-size: 12px;" class="label" ng-class="{
                                    'label-success': stockReturn.status == 'NEW',
                                    'label-warning': stockReturn.status == 'INPROGRESS',
                                    'label-info': stockReturn.status == 'APPROVED'}">{{stockReturn.status}}
                        </span>
                </td>

                <td>{{stockReturn.approvedBy}}</td>
                <td title="{{stockReturn.notes}}">{{stockReturn.notes | limitTo: 20}}{{stockReturn.notes.length > 20 ?
                    '...' : ''}}
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>