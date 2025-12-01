<div>
    <style scoped>
        table {
            table-layout: auto !important;
        }
    </style>
    <div class='responsive-table' style="padding: 10px;">
        <table class='table table-striped highlight-row' style="width: 100%;table-layout: fixed;">
            <thead>
            <tr>
                <th style="width: 150px" translate>CHANGE_NUMBER</th>
                <th style="width: 150px;" translate>CHANGE_TYPE</th>
                <th style="width: 150px" translate>STATUS</th>
                <th class="col-width-250" translate>TITLE</th>
                <th class="description-column" translate>DESCRIPTION</th>
                <th style="text-align: center;" translate>FROM_REVISION</th>
                <th style="text-align: center;" translate>TO_REVISION</th>
                <th class="description-column" translate>REASON_FOR_CHANGE</th>
                <th style="width: 150px;" translate>RELEASED_REJECTED_DATE</th>
                <th style="width: 150px" translate>WORKFLOW</th>
                <th translate>OWNER</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="mbomChangesVm.loading == true">
                <td colspan="15"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                     class="mr5">
                    <span translate>LOADING_CHANGES</span></td>
            </tr>
            <tr ng-if="mbomChangesVm.loading == false && mbomChangesVm.mcoChanges.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Changes.png" alt="" class="image">

                        <div class="message" translate>NO_CHANGES</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>
            <tr style="font-weight: bold;width: 50px;"
                ng-if="mbomChangesVm.loading == false && mbomChangesVm.mcoChanges.length > 0">
                <td colspan="12">
                    <span>
                        <i class="fa fa-caret-right" ng-if="!mbomChangesVm.collapseOrders"
                           ng-click="mbomChangesVm.expandAllChangeOrders()"></i>
                        <i class="fa fa-caret-down" ng-if="mbomChangesVm.collapseOrders"
                           ng-click="mbomChangesVm.collapseAllChangeOrders()"></i>
                    </span>
                    <span>{{mbomChangesVm.changeOrders}}</span>
                </td>
            </tr>
            <tr ng-if="mbomChangesVm.mcoChanges.length > 0 && mbomChangesVm.collapseOrders"
                ng-repeat="change in mbomChangesVm.mcoChanges">
                <td style="width: 150px">
                    <a class="level1" href="" ng-click="mbomChangesVm.showMco(change)"
                       ng-if="(changeMCOViewPermission || changeViewPermission)">{{change.mcoNumber}}</a>

                    <span class="level1"
                          ng-if="(!changeMCOViewPermission || !changeViewPermission)">{{change.mcoNumber}}</span>
                </td>
                <td>{{change.mcoType.name}}</td>
                <td style="width: 150px">
                    <workflow-status workflow="change"></workflow-status>
                </td>
                <td class="col-width-250">{{change.title}}</td>
                <td class="description-column">{{change.description}}</td>
                <td style="text-align: center;">{{change.fromRev}}</td>
                <td style="text-align: center;">{{change.toRev}}</td>
                <td class="description-column">{{change.reasonForChange}}</td>
                <td style="width: 150px">{{change.releasedDate}}</td>
                <td style="width: 150px">{{change.workflowObject.name}}</td>
                <td style="width: 150px">{{change.changeAnalystObject.fullName}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>