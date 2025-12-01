<style scoped>

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar selector" style="top: 30px; background-color: white">
        <free-text-search on-clear="receiveSelectVm.resetPage"
                          on-search="receiveSelectVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{receiveSelectVm.stockReceives.numberOfElements}} of
                                            {{receiveSelectVm.stockReceives.numberOfElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{receiveSelectVm.stockReceives.totalElements != 0 ? receiveSelectVm.stockReceives.number+1:0}} of {{receiveSelectVm.stockReceives.totalPages}}</span>
                <a href="" ng-click="receiveSelectVm.previousPage()"
                   ng-class="{'disabled': receiveSelectVm.stockReceives.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="receiveSelectVm.nextPage()"
                   ng-class="{'disabled': receiveSelectVm.stockReceives.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">

        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px;">Select</th>
                <th>Receive Number</th>
                <th>Project</th>
                <th>Store</th>
                <%--<th>Purchase Order Number</th>--%>
                <%--<th>Supplier</th>--%>
                <th>Notes</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="receiveSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>Loading Stock Receives</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="receiveSelectVm.loading == false && receiveSelectVm.stockReceives.content.length == 0">
                <td colspan="12">
                    <span translate>No Stock Receives</span>
                </td>
            </tr>

            <tr ng-repeat="stockReceive in receiveSelectVm.stockReceives.content | filter: search"
                ng-click="stockReceive.isChecked = !stockReceive.isChecked; receiveSelectVm.radioChange(stockReceive, $event)"
                ng-dblClick="stockReceive.isChecked = !stockReceive.isChecked; receiveSelectVm.selectRadioChange(stockReceive, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="stockReceive.isChecked" name="stockReceive" value="stockReceive"
                           ng-dblClick="receiveSelectVm.selectRadioChange(stockReceive, $event)"
                           ng-click="receiveSelectVm.radioChange(stockReceive, $event)">
                </td>
                <td>{{stockReceive.receiveNumberSource}}</td>
                <td title="{{stockReceive.projectObject.name}}">{{stockReceive.projectObject.name | limitTo:
                    20}}{{stockReceive.projectObject.name.length > 20 ? '...' : ''}}
                </td>
                <td>{{stockReceive.storeObject.storeName}}</td>
                <%--<td>{{stockReceive.purchaseOrderNumberObject.poNumber}}</td>--%>
                <%--<td>{{stockReceive.supplierObject.name}}</td>--%>
                <td title="{{stockReceive.notes}}">{{stockReceive.notes | limitTo: 20}}{{stockReceive.notes.length > 20
                    ? '...' : ''}}
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>