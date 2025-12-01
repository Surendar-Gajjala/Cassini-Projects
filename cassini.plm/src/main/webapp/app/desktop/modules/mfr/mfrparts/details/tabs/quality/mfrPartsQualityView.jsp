<div>
    <style scoped>
        table {
            table-layout: auto !important;
        }
    </style>
    <div class='responsive-table' style="padding: 10px;height: 90%;">
        <table class='table table-striped highlight-row' style="width: 100%;table-layout: fixed;">
            <thead>
            <tr>
                <th style="width: 150px" translate>QUALITY_NUMBER</th>
                <th style="width: 150px;" translate>QUALITY_TYPE</th>
                <th class="col-width-250" translate>TITLE</th>
                <th class="description-column" translate>DESCRIPTION</th>
                <th class="col-width-250" translate>CREATED_DATE</th>
                <th translate>CREATED_BY</th>
                <th style="width: 150px" translate>STATUS</th>
                <th style="width: 150px" translate>WORKFLOW</th>
                <th style="width: 150px" ng-if="mfrPartsChangesVm.collapseQcrs" translate>QCR_FOR</th>

            </tr>
            </thead>
            <tbody>
            <tr ng-if="mfrPartsChangesVm.loading == true">
                <td colspan="4"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                     class="mr5">
                    <span translate>LOADING_CHANGES</span></td>
            </tr>
            <tr ng-if="mfrPartsChangesVm.loading == false && mfrPartsChangesVm.qcrQuality.length == 0 && mfrPartsChangesVm.ncrQuality.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Quality.png" alt="" class="image">

                        <div class="message" translate>NO_QUALITY</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
                </td>

            </tr>
            <tr style="font-weight: bold;width: 50px;"
                ng-if="mfrPartsChangesVm.loading == false && mfrPartsChangesVm.ncrQuality.length > 0">
                <td colspan="12">
                <span>
                    <i class="fa fa-caret-right" ng-if="!mfrPartsChangesVm.collapseNcrs"
                       ng-click="mfrPartsChangesVm.expandAllNcrs()"></i>
                    <i class="fa fa-caret-down" ng-if="mfrPartsChangesVm.collapseNcrs"
                       ng-click="mfrPartsChangesVm.collapseAllNcrs()"></i>
                </span>
                    <span>{{mfrPartsChangesVm.pr}}</span>
                </td>
            </tr>
            <tr ng-if="mfrPartsChangesVm.ncrQuality.length > 0 && mfrPartsChangesVm.collapseNcrs"
                ng-repeat="quality in mfrPartsChangesVm.ncrQuality">
                <td style="width: 150px" title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                    ng-hide="external.external == true && quality.objectType == 'NCR'">
                    <a class="level1" href="" ng-click="mfrPartsChangesVm.showNcr(quality)"
                       ng-if="hasPermission('change','eco','view') || hasPermission('change','view')">{{quality.ncrNumber}}</a>

                    <span class="level1"
                          ng-if="!hasPermission('change','eco','view') || !hasPermission('change','view')">{{quality.ncrNumber}}</span>
                </td>
                <td ng-if="external.external == true && quality.objectType == 'NCR'">
                    <span class="level1"
                            >{{quality.ncrNumber}}</span>

                </td>
                <td>{{quality.ncrType.name}}</td>
                <td class="col-width-250">{{quality.title}}</td>
                <td class="description-column">{{quality.description}}</td>
                <td class="col-width-250">{{quality.createdDate}}</td>
                <td style="width: 150px">{{quality.createdByObject.fullName}}</td>
                <td style="width: 150px">
                    <workflow-status workflow="quality"></workflow-status>
                </td>
                <td style="width: 150px">{{quality.workflowObject.name}}</td>
            </tr>
            <tr style="font-weight: bold;width: 50px;"
                ng-if="mfrPartsChangesVm.loading == false && mfrPartsChangesVm.qcrQuality.length > 0">
                <td colspan="12">
                <span>
                    <i class="fa fa-caret-right" ng-if="!mfrPartsChangesVm.collapseQcrs"
                       ng-click="mfrPartsChangesVm.expandAllQcrs()"></i>
                    <i class="fa fa-caret-down" ng-if="mfrPartsChangesVm.collapseQcrs"
                       ng-click="mfrPartsChangesVm.collapseAllQcrs()"></i>
                </span>
                    <span>{{mfrPartsChangesVm.qcr}}</span>
                </td>
            </tr>
            <tr ng-if="mfrPartsChangesVm.qcrQuality.length > 0 && mfrPartsChangesVm.collapseQcrs"
                ng-repeat="quality in mfrPartsChangesVm.qcrQuality">
                <td style="width: 150px" ng-hide="external.external == true && quality.objectType == 'QCR'">
                    <a class="level1" ng-click="mfrPartsChangesVm.showQcr(quality)">{{quality.qcrNumber}}</a>

                    <span class="level1" ng-if="!hasPermission('change','eco','view') || !hasPermission('change','view')">{{quality.qcrNumber}}</span>
                </td>
                <td ng-if="external.external == true && quality.objectType == 'QCR'">
                    <span class="level1"
                            >{{quality.qcrNumber}}</span>

                </td>
                <td>{{quality.qcrType.name}}</td>
                <td class="col-width-250">{{quality.title}}</td>
                <td class="description-column">{{quality.description}}</td>
                <td class="col-width-250">{{quality.createdDate}}</td>
                <td style="width: 150px">{{quality.createdByObject.fullName}}</td>
                <td style="width: 150px">
                    <workflow-status workflow="quality"></workflow-status>
                </td>
                <td style="width: 150px">{{quality.workflowObject.name}}</td>
                <td style="width: 150px">{{quality.qcrFor}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>