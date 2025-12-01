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
                <th style="width: 150px" ng-if="itemQualityVm.collapseQcrs" translate>QCR_FOR</th>

            </tr>
            </thead>
            <tbody>
            <tr ng-if="itemQualityVm.loading == true">
                <td colspan="4"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                     class="mr5">
                    <span translate>LOADING_QUALITYS</span></td>
            </tr>
            <tr ng-if="itemQualityVm.loading == false && itemQualityVm.prQuality.length == 0 && itemQualityVm.qcrQuality.length == 0 && itemQualityVm.productPrs.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Quality.png" alt="" class="image">

                        <div class="message" translate>NO_QUALITY</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
                </td>
            </tr>
            <tr style="font-weight: bold;width: 50px;"
                ng-if="itemQualityVm.loading == false && itemQualityVm.productPrs.length > 0 && item.itemType.itemClass == 'PRODUCT'">
                <td colspan="12">
                <span>
                    <i class="fa fa-caret-right" ng-if="!itemQualityVm.collapseProductPrs"
                       ng-click="itemQualityVm.expandAllProductPrs()"></i>
                    <i class="fa fa-caret-down" ng-if="itemQualityVm.collapseProductPrs"
                       ng-click="itemQualityVm.expandAllProductPrs()"></i>
                </span>
                    <span>{{itemQualityVm.problemReportTitle}}</span>
                </td>
            </tr>
            <tr ng-if="itemQualityVm.productPrs.length > 0 && itemQualityVm.collapseProductPrs && item.itemType.itemClass == 'PRODUCT'"
                ng-repeat="problemReport in itemQualityVm.productPrs">
                <td style="width: 150px">
                    <a class="level1" href="" ng-click="itemQualityVm.showPr(problemReport)"
                       ng-if="prViewPermission">{{problemReport.prNumber}}</a>

                    <span class="level1"
                          ng-if="!prViewPermission">{{problemReport.prNumber}}</span>
                </td>
                <td ng-if="external.external == true && quality.qualityType == 'PRTYPE'">{{problemReport.prNumber}}</td>
                <td>{{problemReport.prType.name}}</td>
                <td class="col-width-250">{{problemReport.problem}}</td>
                <td class="description-column">{{problemReport.description}}</td>
                <td class="col-width-250">{{problemReport.createdDate}}</td>
                <td style="width: 150px">{{problemReport.createdByObject.fullName}}</td>
                <td style="width: 150px">
                    <workflow-status workflow="problemReport"></workflow-status>
                </td>
                <td style="width: 150px">{{problemReport.workflowObject.name}}</td>
            </tr>
            <tr style="font-weight: bold;width: 50px;"
                ng-if="itemQualityVm.loading == false && itemQualityVm.prQuality.length > 0">
                <td colspan="12">
                <span>
                    <i class="fa fa-caret-right" ng-if="!itemQualityVm.collapsePrs"
                       ng-click="itemQualityVm.expandAllPrs()"></i>
                    <i class="fa fa-caret-down" ng-if="itemQualityVm.collapsePrs"
                       ng-click="itemQualityVm.collapseAllPrs()"></i>
                </span>
                    <span>{{itemQualityVm.pr}}</span>
                </td>
            </tr>
            <tr ng-if="itemQualityVm.prQuality.length > 0 && itemQualityVm.collapsePrs"
                ng-repeat="quality in itemQualityVm.prQuality">
                <td style="width: 150px">
                    <a class="level1" href="" ng-click="itemQualityVm.showPr(quality)"
                       ng-if="prViewPermission">{{quality.prNumber}}</a>

                    <span class="level1"
                          ng-if="!prViewPermission">{{quality.prNumber}}</span>
                </td>
                <td ng-if="external.external == true && quality.qualityType == 'PRTYPE'">{{quality.prNumber}}</td>
                <td>{{quality.prType.name}}</td>
                <td class="col-width-250">{{quality.problem}}</td>
                <td class="description-column">{{quality.description}}</td>
                <td class="col-width-250">{{quality.createdDate}}</td>
                <td style="width: 150px">{{quality.createdByObject.fullName}}</td>
                <td style="width: 150px">
                    <workflow-status workflow="quality"></workflow-status>
                </td>
                <td style="width: 150px">{{quality.workflowObject.name}}</td>
            </tr>
            <tr style="font-weight: bold;width: 50px;"
                ng-if="itemQualityVm.loading == false && itemQualityVm.qcrQuality.length > 0">
                <td colspan="12">
                <span>
                    <i class="fa fa-caret-right" ng-if="!itemQualityVm.collapseQcrs"
                       ng-click="itemQualityVm.expandAllQcrs()"></i>
                    <i class="fa fa-caret-down" ng-if="itemQualityVm.collapseQcrs"
                       ng-click="itemQualityVm.collapseAllQcrs()"></i>
                </span>
                    <span>{{itemQualityVm.qcr}}</span>
                </td>
            </tr>
            <tr ng-if="itemQualityVm.qcrQuality.length > 0 && itemQualityVm.collapseQcrs"
                ng-repeat="quality in itemQualityVm.qcrQuality">
                <td style="width: 150px">
                    <a class="level1" ng-click="itemQualityVm.showQcr(quality)"
                       ng-if="qcrViewPermission">{{quality.qcrNumber}}</a>

                    <span class="level1" ng-if="!qcrViewPermission">{{quality.qcrNumber}}</span>
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