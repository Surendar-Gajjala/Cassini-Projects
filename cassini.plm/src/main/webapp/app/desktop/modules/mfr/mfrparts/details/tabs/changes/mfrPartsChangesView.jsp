<div>
    <style scoped>
        table {
            table-layout: auto !important;
        }
    </style>
    <div class='responsive-table' style="padding: 10px;height: 90%;">
        <table class='table table-striped highlight-row' style="width: 100%;table-layout: fixed;"
               ng-if="itemType != 'Document'">
            <thead>
            <tr>
                <th style="width: 150px" translate>CHANGE_NUMBER</th>
                <th style="width: 150px;" translate>CHANGE_TYPE</th>
                <th style="width: 150px" translate>STATUS</th>
                <th class="col-width-250" translate>TITLE</th>
                <th class="description-column" translate>DESCRIPTION</th>
                <th class="description-column" translate>REASON_FOR_CHANGE</th>
                <th style="width: 150px;" translate>RELEASED_OR_REJECTED_DATE</th>
                <th style="width: 150px" translate>WORKFLOW</th>
                <th translate>OWNER</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="mfrPartsChangesVm.loading == true">
                <td colspan="4"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                     class="mr5">
                    <span translate>LOADING_CHANGES</span></td>
            </tr>
            <tr ng-if="mfrPartsChangesVm.loading == false && mfrPartsChangesVm.mfrPartChanges.length == 0 && mfrPartsChangesVm.variances.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Changes.png" alt="" class="image">

                        <div class="message" translate>NO_CHANGES</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
                </td>
            </tr>
            <tr style="font-weight: bold;width: 50px;" ng-if="mfrPartsChangesVm.loading == false && mfrPartsChangesVm.mfrPartChanges.length > 0">
                <td colspan="12">
                <span>
                    <i class="fa fa-caret-right" ng-if="!mfrPartsChangesVm.collapseMcos"
                       ng-click="mfrPartsChangesVm.expandAllMcos()"></i>
                    <i class="fa fa-caret-down" ng-if="mfrPartsChangesVm.collapseMcos"
                       ng-click="mfrPartsChangesVm.collapseAllMcos()"></i>
                </span>
                    <span>{{mfrPartsChangesVm.mco}}</span>
                </td>
            </tr>
            <tr ng-if="mfrPartsChangesVm.mfrPartChanges.length > 0 && mfrPartsChangesVm.collapseMcos"
                ng-repeat="change in mfrPartsChangesVm.mfrPartChanges">
                <td style="width: 150px">
                    <a ng-if="external.external != true" class="level1" href="" ng-click="mfrPartsChangesVm.showMco(change)"
                       ng-if="change.changeType == 'MCO'">{{change.mcoNumber}}</a>
                       
                    <span ng-if="external.external == true" class="level1" 
                       ng-if="change.changeType == 'MCO'">{{change.mcoNumber}}</span>

                    <%--<span class="level1" ng-if="change.changeType == 'MCO' && !hasPermission('manufacturer','view')">{{change.mcoNumber}}</span>--%>
                </td>
                <td>{{change.changeType}}</td>
                <td style="width: 150px">
                    <workflow-status workflow="change"></workflow-status>
                </td>
                <td class="col-width-250">{{change.title}}</td>
                <td class="description-column">{{change.description}}</td>
                <td class="description-column">{{change.reasonForChange}}</td>
                <td style="width: 150px">{{change.releasedDate}}</td>
                <td style="width: 150px">{{change.workflowObject.name}}</td>
                <td style="width: 150px">{{change.changeAnalystObject.fullName}}</td>
            </tr>
            <tr style="font-weight: bold;width: 50px;" ng-if="mfrPartsChangesVm.loading == false && mfrPartsChangesVm.variances.length > 0">
                <td colspan="12">
                <span>
                    <i class="fa fa-caret-right" ng-if="!mfrPartsChangesVm.collapseVariances"
                       ng-click="mfrPartsChangesVm.expandAllVariances()"></i>
                    <i class="fa fa-caret-down" ng-if="mfrPartsChangesVm.collapseVariances"
                       ng-click="mfrPartsChangesVm.collapseAllVariances()"></i>
                </span>
                    <span>{{mfrPartsChangesVm.variance}}</span>
                </td>
            </tr>
            <tr ng-if="mfrPartsChangesVm.variances.length > 0 && mfrPartsChangesVm.collapseVariances"
                ng-repeat="variance in mfrPartsChangesVm.variances">
                <td style="width: 150px">
                    <a class="level1" ng-click="mfrPartsChangesVm.showVariance(variance)">{{variance.varianceNumber}}</a>

                    <span class="level1" ng-if="!hasPermission('change','variance','view')">{{variance.varianceNumber}}</span>
                </td>
                <td>{{variance.varianceType}}</td>
                <td style="width: 150px">
                    <workflow-status workflow="variance"></workflow-status>
                </td>
                <td class="col-width-250">{{variance.title}}</td>
                <td class="description-column">{{variance.description}}</td>
                <td class="description-column">{{variance.reasonForVariance}}</td>
                <td class="col-width-250">{{variance.modifiedDate}}</td>
                <td style="width: 150px">{{variance.workflowObject.name}}</td>
                <td style="width: 150px">{{variance.originatorObject.fullName}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>