<div>
    <style scoped>
        table {
            table-layout: auto !important;
        }
    </style>
    <div class='responsive-table' style="padding: 10px;">
        <table class='table table-striped highlight-row' style="width: 100%;table-layout: fixed;"
               ng-if="itemType != 'DOCUMENT'">
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
                <th style="width: 150px;" translate>RELEASED_DATE</th>
                <th style="width: 150px" translate>WORKFLOW</th>
                <th translate>OWNER</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="itemChangesVm.loading == true">
                <td colspan="4"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                     class="mr5">
                    <span translate>LOADING_CHANGES</span></td>
            </tr>
            <tr ng-if="itemChangesVm.loading == false && itemChangesVm.ecoChanges.length == 0 && itemChangesVm.ecrChanges.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Changes.png" alt="" class="image">

                        <div class="message" translate>NO_CHANGES</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
                </td>
            </tr>
            <tr style="font-weight: bold;width: 50px;"
                ng-if="itemChangesVm.loading == false && itemChangesVm.ecoChanges.length > 0">
                <td colspan="12">
                <span>
                    <i class="fa fa-caret-right" ng-if="!itemChangesVm.collapseOrders"
                       ng-click="itemChangesVm.expandAllChangeOrders()"></i>
                    <i class="fa fa-caret-down" ng-if="itemChangesVm.collapseOrders"
                       ng-click="itemChangesVm.collapseAllChangeOrders()"></i>
                </span>
                    <span ng-if="itemType != 'Document'">{{itemChangesVm.changeOrders}}</span>
                </td>
            </tr>
            <tr ng-if="itemChangesVm.ecoChanges.length > 0 && itemChangesVm.collapseOrders"
                ng-repeat="change in itemChangesVm.ecoChanges">
                <td style="width: 150px" ng-if="external.external == false">
                    <a class="level1" href="" ng-click="itemChangesVm.showEco(change)"
                       ng-if="change.changeType == 'ECO' && (changeECOViewPermission || changeViewPermission)">{{change.ecoNumber}}</a>

                    <span class="level1"
                          ng-if="change.changeType == 'ECO' && (!changeECOViewPermission || !changeViewPermission)">{{change.ecoNumber}}</span>
                </td>
                <td ng-if="external.external == true && change.changeType == 'ECO'">{{change.ecoNumber}}</td>
                <td>{{change.ecoTypeObject.name}}</td>
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
                <td style="width: 150px">{{change.ecoOwnerObject.fullName}}</td>
            </tr>
            <tr style="font-weight: bold;width: 50px;"
                ng-if="itemChangesVm.loading == false && itemChangesVm.ecrChanges.length > 0">
                <td colspan="12">
                <span>
                    <i class="fa fa-caret-right" ng-if="!itemChangesVm.collapseRequests"
                       ng-click="itemChangesVm.expandAllChangeRequests()"></i>
                    <i class="fa fa-caret-down" ng-if="itemChangesVm.collapseRequests"
                       ng-click="itemChangesVm.collapseAllChangeRequests()"></i>
                </span>
                    <span ng-if="itemType != 'Document'">{{itemChangesVm.changeRequests}}</span>
                </td>
            </tr>
            <tr ng-if="itemChangesVm.ecrChanges.length > 0 && itemChangesVm.collapseRequests"
                ng-repeat="change in itemChangesVm.ecrChanges">
                <td style="width: 150px">
                    <a class="level1" ng-click="itemChangesVm.showEcr(change)"
                       ng-if="changeECOViewPermission || changeViewPermission">{{change.crNumber}}</a>

                    <span class="level1"
                          ng-if="!changeECOViewPermission || !changeViewPermission">{{change.crNumber}}</span>
                </td>
                <td>{{change.crTypeObject.name}}</td>
                <td style="width: 150px">
                    <workflow-status workflow="change"></workflow-status>
                </td>
                <td class="col-width-250">{{change.title}}</td>
                <td class="description-column">{{change.descriptionOfChange}}</td>
                <td></td>
                <td></td>
                <td class="description-column">{{change.reasonForChange}}</td>
                <td style="width: 150px">{{change.releasedDate}}</td>
                <td style="width: 150px">{{change.workflowObject.name}}</td>
                <td style="width: 150px">{{change.originatorObject.fullName}}</td>
            </tr>
            </tbody>
        </table>
        <table class='table table-striped highlight-row' ng-if="itemType == 'DOCUMENT'">
            <thead>
            <tr>
                <th style="width: 150px" translate>DOCUMENT_NUMBER</th>
                <th style="width: 150px;" translate>DOCUMENT_TYPE</th>
                <th style="width: 150px" translate>STATUS</th>
                <th class="col-width-250" translate>TITLE</th>
                <th class="description-column" translate>DESCRIPTION</th>
                <th style="text-align: center;" translate>FROM_REVISION</th>
                <th style="text-align: center;" translate>TO_REVISION</th>
                <th class="description-column" translate>REASON_FOR_CHANGE</th>
                <th style="width: 150px;" translate>RELEASED_DATE</th>
                <th style="width: 150px" translate>WORKFLOW</th>
                <th translate>OWNER</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="itemChangesVm.loading == true">
                <td colspan="4"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                     class="mr5">
                    <span translate>LOADING_CHANGES</span></td>
            </tr>
            <tr ng-if="itemChangesVm.loading == false && itemChangesVm.dcoChanges.length == 0 && itemChangesVm.dcrChanges.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Changes.png" alt="" class="image">

                        <div class="message" translate>NO_CHANGES</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
                </td>
            </tr>
            <tr ng-if="itemChangesVm.dcoChanges.length > 0"
                ng-repeat="change in itemChangesVm.dcoChanges">
                <td style="width: 150px" ng-if="external.external == false">
                    <a href="" ng-click="itemChangesVm.showDco(change)"
                       ng-if="change.changeType == 'DCO' && (changeECOViewPermission || changeViewPermission)">{{change.dcoNumber}}</a>

                    <span ng-if="change.changeType == 'DCO' && (!changeECOViewPermission || changeViewPermission)">{{change.dcoNumber}}</span>
                </td>
                <td ng-if="external.external == true && change.changeType == 'DCO'">{{change.dcoNumber}}</td>
                <td>{{change.dcoTypeObject.name}}</td>
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
                <td style="width: 150px">{{change.dcoOwnerObject.fullName}}</td>
            </tr>
            <tr ng-if="itemChangesVm.dcrChanges.length > 0"
                ng-repeat="change in itemChangesVm.dcrChanges">
                <td style="width: 150px" ng-if="external.external == false">
                    <a ng-click="itemChangesVm.showDcr(change)"
                       ng-if="change.changeType == 'DCR'  && (changeECOViewPermission || changeViewPermission)">{{change.crNumber}}</a>

                    <span ng-if="change.changeType == 'DCR'  && (!changeECOViewPermission || changeViewPermission)">{{change.crNumber}}</span>
                </td>
                <td ng-if="external.external == true && change.changeType == 'DCR'">{{change.crNumber}}</td>
                <td>{{change.dcrTypeObject.name}}</td>
                <td style="width: 150px">
                    <workflow-status workflow="change"></workflow-status>
                </td>
                <td class="col-width-250">{{change.title}}</td>
                <td class="description-column">{{change.description}}</td>
                <td>{{change.fromRev}}</td>
                <td>{{change.toRev}}</td>
                <td class="description-column">{{change.reasonForChange}}</td>
                <td style="width: 150px">{{change.releasedDate}}</td>
                <td style="width: 150px">{{change.workflowObject.name}}</td>
                <td style="width: 150px">{{change.dcrOwnerObject.fullName}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>