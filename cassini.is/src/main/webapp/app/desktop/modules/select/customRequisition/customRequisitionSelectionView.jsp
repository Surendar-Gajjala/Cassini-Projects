<style scoped>

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar selector" style="top: 30px; background-color: white">
        <free-text-search on-clear="customRequisitionSelectVm.resetPage"
                          on-search="customRequisitionSelectVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{customRequisitionSelectVm.customRequisitions.numberOfElements}} of
                                            {{customRequisitionSelectVm.customRequisitions.numberOfElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{customRequisitionSelectVm.customRequisitions.totalElements != 0 ? customRequisitionSelectVm.customRequisitions.number+1:0}} of {{customRequisitionSelectVm.customRequisitions.totalPages}}</span>
                <a href="" ng-click="customRequisitionSelectVm.previousPage()"
                   ng-class="{'disabled': customRequisitionSelectVm.customRequisitions.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="customRequisitionSelectVm.nextPage()"
                   ng-class="{'disabled': customRequisitionSelectVm.customRequisitions.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">

        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px;">Select</th>
                <th>Requisition Number</th>
                <th>Store</th>
                <th>Project</th>
                <th>Status</th>
                <th>Notes</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="customRequisitionSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>Loading Custom Requisitions..</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="customRequisitionSelectVm.loading == false && customRequisitionSelectVm.customRequisitions.content.length == 0">
                <td colspan="12">
                    <span translate>No Custom Requisitions are available to view</span>
                </td>
            </tr>

            <tr ng-repeat="customRequisition in customRequisitionSelectVm.customRequisitions.content | filter: search"
                ng-click="customRequisition.isChecked = !customRequisition.isChecked; customRequisitionSelectVm.radioChange(customRequisition, $event)"
                ng-dblClick="customRequisition.isChecked = !customRequisition.isChecked; customRequisitionSelectVm.selectRadioChange(customRequisition, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="customRequisition.isChecked" name="customRequisition"
                           value="customRequisition"
                           ng-dblClick="customRequisitionSelectVm.selectRadioChange(customRequisition, $event)"
                           ng-click="customRequisitionSelectVm.radioChange(customRequisition, $event)">
                </td>
                <td>{{customRequisition.requisitionNumber}}</td>
                <td>{{customRequisition.store.storeName}}</td>
                <td>{{customRequisition.project.name}}</td>
                <td style="vertical-align: middle;width: 150px;">
                        <span style="color: white;font-size: 12px;" class="label" ng-class="{
                                    'label-success': customRequisition.status == 'NEW',
                                    'label-warning': customRequisition.status == 'INPROGRESS',
                                    'label-info': customRequisition.status == 'APPROVED'}">{{customRequisition.status}}
                        </span>
                </td>

                <td title="{{customRequisition.notes}}">
                    {{customRequisition.notes | limitTo: 12}}{{customRequisition.notes.length > 12 ? '...' : ''}}
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>