<style>
    .view-content {
        position: relative;
    }

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
    }

    .view-content .table-footer {
        padding: 0 10px 0 10px;
        position: absolute;
        bottom: 0px !important;
        height: 40px;
        width: 100%;
        border-top: 1px solid #D3D7DB;
        display: table;
    }

    .view-content .table-footer > div {
        display: table-row;
        line-height: 30px;
    }

    .view-content .table-footer > div h5 {
        margin: 0;
    }

    .view-content .table-footer > div > div {
        display: table-cell;
        vertical-align: middle;
    }

    .view-content .table-footer > div > div > i {
        font-size: 16px;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    #freeTextSearchDirective {
        top: 7px !important;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 30px" translate></th>
                    <th style="width: 150px" translate>VARIANCE_NUMBER</th>
                    <th style="width: 150px" translate>VARIANCE_TYPE</th>
                    <th style="width: 150px" translate>TITLE</th>
                    <th class="col-width-200" translate>DESCRIPTION</th>
                    <th style="width: 150px" translate>EFFECTIVE_TYPE</th>
                    <th class="col-width-200" translate>REASON_FOR_VARIANCE</th>
                    <th class="col-width-200" translate>CURRENT_REQUIREMENT</th>
                    <th class="col-width-200" translate>REQUIREMENT_DEVIATION</th>
                    <th style="width: 150px" translate>STATUS</th>
                    <th style="width: 150px" translate>ORIGINATOR</th>
                    <th style="width: 150px" translate>CREATED_BY</th>
                    <th style="width: 150px" translate>CREATED_DATE</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="itemVarianceVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_VARIANCES</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="itemVarianceVm.loading == false && itemVarianceVm.variances.length == 0">
                    <td colspan="25" translate>NO_VARIANCES</td>
                </tr>

                <tr ng-repeat="variance in itemVarianceVm.variances">
                    <td style="width: 30px;">
                        <span ng-if="variance.recurring == true">
                        <i class="fa fa-repeat" aria-hidden="true"></i>
                        </span>
                    </td>
                    <td ng-hide="external.external == true">
                        <a href="" ng-click="itemVarianceVm.showVariance(variance)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="variance.varianceNumber | highlightText: freeTextQuery"></span>
                        </a>
                    </td>

                    <td ng-if="external.external == true">
                        <span>{{variance.varianceNumber}}</span>
                    </td>
                    <td>{{variance.varianceType}}</td>
                    <td>{{variance.title}}</td>
                    <td class="col-width-200">{{variance.description}}</td>
                    <td>{{variance.effectivityType}}</td>
                    <td class="col-width-200">{{variance.reasonForVariance}}</td>
                    <td class="col-width-200">{{variance.currentRequirement}}</td>
                    <td class="col-width-200">{{variance.requirementDeviation}}</td>
                    <td>
                        <workflow-status workflow="variance"></workflow-status>
                    </td>
                    <td>{{variance.originatorObject.fullName}}</td>
                    <td>{{variance.createdByObject.fullName}}</td>
                    <td>{{variance.createdDate}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
