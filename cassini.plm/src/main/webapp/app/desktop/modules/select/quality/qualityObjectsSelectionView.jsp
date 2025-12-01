<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .tooltip {
        position: relative;
        display: inline-block;
        border-bottom: 1px dotted black; /* If you want dots under the hoverable text */
    }

    .pending-eco-tooltip {
        position: relative;
        display: inline-block;
    }

    .pending-eco-tooltip .pending-eco-tooltip-text {
        visibility: hidden;
        width: 300px;
        background-color: white;
        text-align: left;
        position: absolute;
        z-index: 1;
        top: -5px;
        bottom: auto;
        left: 100%;
        opacity: 0;
        transition: opacity 1s;
        padding: 2px;
        border: 1px solid black;
    }

    .pending-eco-tooltip:hover .pending-eco-tooltip-text {
        visibility: visible;
        opacity: 1;
    }

    .item-selection-table table th {
        position: -webkit-sticky;
        position: sticky;
        top: -1px !important;
        z-index: 5 !important;
    }

    .table-div {
        position: absolute;
        top: 142px !important;
        bottom: 50px !important;
        left: 0;
        right: 0;
    }

    .open > .dropdown-toggle.btn {
        color: #091007 !important;
    }

    .quality-node {
        background: transparent url("app/assets/images/quality-node.png") no-repeat !important;
        height: 16px;
    }
</style>
<div style="padding: 0 10px;height: 100%;">
    <div class="col-md-12" style="border: 1px solid lightgrey;">
        <style scoped>
            .ui-select-bootstrap > .ui-select-match > .btn {
                padding: 4px 5px;
            }
        </style>
        <div class="col-md-5" style="padding-left: 0;padding-top: 5px;">
            <div class="form-group" style="margin: 0;width: 100%;"
                 ng-if="qualityObjectSelectVm.selectAttributeDef.refSubType == null || qualityObjectSelectVm.selectAttributeDef.refSubType == ''">
                <ui-select ng-model="qualityObjectSelectVm.selectedQualityObjectType" theme="bootstrap"
                           on-select="qualityObjectSelectVm.onSelectQualityType($item)" style="width:100%">
                    <ui-select-match placeholder="Select Type">
                        {{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="type.value as type in qualityObjectSelectVm.qualityTypes | filter: $select.search">
                        <div ng-bind="type.label | highlight: $select.search"></div>
                    </ui-select-choices>
                </ui-select>
            </div>
        </div>
        <div class="col-md-7">
            <div class="pull-right text-center" style="padding: 10px;">
                <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="qualityObjectSelectVm.paginatedResults.numberOfElements ==0">
                            {{(qualityObjectSelectVm.pageable.page*qualityObjectSelectVm.pageable.size)}}
                        </span>

                                    <span ng-if="qualityObjectSelectVm.paginatedResults.numberOfElements > 0">
                            {{(qualityObjectSelectVm.pageable.page*qualityObjectSelectVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="qualityObjectSelectVm.paginatedResults.last ==false">{{((qualityObjectSelectVm.pageable.page+1)*qualityObjectSelectVm.pageable.size)}}</span>
                                    <span ng-if="qualityObjectSelectVm.paginatedResults.last == true">{{qualityObjectSelectVm.paginatedResults.totalElements}}</span>

                                 <span translate> OF </span>
                                {{qualityObjectSelectVm.paginatedResults.totalElements}}<span
                                        translate>AN</span>
                                </span>
                            </medium>
                        </span>
                        <span class="mr10">  Page {{qualityObjectSelectVm.paginatedResults.totalElements != 0 ?
                        qualityObjectSelectVm.paginatedResults.number+1:0}} <span translate>OF</span> {{qualityObjectSelectVm.paginatedResults.totalPages}}</span>
                    <a href="" ng-click="qualityObjectSelectVm.previousPage()"
                       ng-class="{'disabled': qualityObjectSelectVm.paginatedResults.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="qualityObjectSelectVm.nextPage()"
                       ng-class="{'disabled': qualityObjectSelectVm.paginatedResults.last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-12" style="margin-top: 10px" ng-if="qualityObjectSelectVm.selectedQualityObjectType == null">
        <span translate style="font-size: 14px;">Please select quality object type</span>
    </div>
    <div class=" col-md-12" style="padding: 0;">
        <form class="form-inline ng-pristine ng-valid" style="padding: 10px 0;display: flex;flex-direction: row;"
              ng-if="qualityObjectSelectVm.selectedQualityObjectType != null">
            <div class="form-group" style="flex-grow: 1;margin-right: 0;"
                 ng-if="qualityObjectSelectVm.selectAttributeDef.refSubType == null || qualityObjectSelectVm.selectAttributeDef.refSubType == ''">
                <div class="input-group input-group-sm" style="">
                    <div class="input-group-btn dropdown" uib-dropdown="" style=""
                         ng-disabled="qualityObjectSelectVm.selectedQualityObjectType == null">
                        <button uib-dropdown-toggle="" class="btn btn-default dropdown-toggle" type="button"
                                aria-haspopup="true" aria-expanded="false" style="background-color: #fff;">
                            <span translate="" class="ng-scope">Select</span> <span class="caret"
                                                                                    style="margin-left: 4px;"></span>
                        </button>
                        <div class="dropdown-menu" role="menu">
                            <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                                <div class="classification-pane">
                                    <ul id="quality-type-tree" class="easyui-tree"></ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                           style="background-color: #fff;" ng-model="qualityObjectSelectVm.selectedType.name"
                           readonly="">
                </div>
            </div>
            <div class="form-group" style="flex-grow: 2;margin-left: 5px;margin-right: 5px;text-align: center;">
                <input type="text" ng-model="qualityObjectSelectVm.number"
                       ng-change="qualityObjectSelectVm.searchFilterItem()"
                       ng-enter="qualityObjectSelectVm.searchFilterItem()"
                       ng-model-options="{ debounce: 500 }"
                       style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;"
                       placeholder="{{qualityObjectSelectVm.numberTitle}}"
                       ng-disabled="qualityObjectSelectVm.selectedQualityObjectType == null"
                       class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
            </div>
            <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
                <input type="text" ng-model="qualityObjectSelectVm.name"
                       ng-change="qualityObjectSelectVm.searchFilterItem()"
                       ng-enter="qualityObjectSelectVm.searchFilterItem()"
                       ng-model-options="{ debounce: 500 }"
                       style="margin-left: -16px; height: 30px;width: 100%;margin-top: 0;"
                       placeholder="{{qualityObjectSelectVm.nameTitle}}"
                       ng-disabled="qualityObjectSelectVm.selectedQualityObjectType == null"
                       class="input-sm form-control ng-pristine ng-valid ng-touched">
            </div>
            <div style="margin-top: 0;flex-grow: 1;" class="">

                <!-- ngIf: qualityObjectSelectVm.clear == true -->
                <button ng-click="qualityObjectSelectVm.clearFilter()" translate="" style="height: 29px;width: 100%"
                        class="btn btn-xs btn-danger ng-scope" ng-if="qualityObjectSelectVm.clear">Clear
                </button>
                <!-- end ngIf: qualityObjectSelectVm.clear == true -->
            </div>
        </form>
    </div>
    <div class="col-md-12" style="padding:0; height: calc(100% - 95px);overflow: auto;">
        <div class="responsive-table" style="height: 100%;">
            <table class="table table-striped highlight-row"
                   ng-if="qualityObjectSelectVm.selectedQualityObjectType === 'PRODUCTINSPECTIONPLANTYPE' || qualityObjectSelectVm.selectedQualityObjectType === 'MATERIALINSPECTIONPLANTYPE'">
                <thead>
                <tr>
                    <th style="width: 40px" translate>SELECT</th>
                    <th class="col-width-100" translate>NUMBER</th>
                    <th class="col-width-150" translate>TYPE</th>
                    <th class="col-width-150"
                        ng-if="qualityObjectSelectVm.selectedQualityObjectType === 'PRODUCTINSPECTIONPLANTYPE'"
                        translate>
                        PRODUCT
                    </th>
                    <th class="col-width-150"
                        ng-if="qualityObjectSelectVm.selectedQualityObjectType === 'MATERIALINSPECTIONPLANTYPE'"
                        translate>
                        MATERIAL
                    </th>
                    <th class="col-width-200" translate>NAME</th>
                    <th style="width: 150px;text-align: center" translate>REVISION</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="qualityObjectSelectVm.inspectionPlans.content.length == 0">
                    <td colspan="25" translate>NO_PLANS</td>
                </tr>

                <tr ng-repeat="inspectionPlan in qualityObjectSelectVm.inspectionPlans.content">
                    <td style="width: 40px;">
                        <input type="radio" ng-checked="inspectionPlan.checked"
                               ng-click="qualityObjectSelectVm.radioChange(inspectionPlan, $event)"
                               ng-dblclick="qualityObjectSelectVm.selectRadioChange(inspectionPlan, $event)">
                    </td>
                    <td class="col-width-100">
                        <a href="" ng-click="showPlanDetails(inspectionPlan)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="inspectionPlan.number | highlightText: qualityObjectSelectVm.number"></span>
                        </a>
                    </td>
                    <td class="col-width-150">
                        <span ng-bind-html="inspectionPlan.type | highlightText: qualityObjectSelectVm.searchText"></span>
                    </td>
                    <td class="col-width-150">
                                <span ng-if="qualityObjectSelectVm.active === productInspectionPlanTitle"
                                      ng-bind-html="inspectionPlan.productName | highlightText: qualityObjectSelectVm.searchText"></span>
                                <span ng-if="qualityObjectSelectVm.active === materialInspectionPlanTitle"
                                      ng-bind-html="inspectionPlan.materialName | highlightText: qualityObjectSelectVm.searchText"></span>
                    </td>
                    <td title="{{inspectionPlan.name}}" class="col-width-200">
                        <span ng-bind-html="inspectionPlan.name | highlightText: qualityObjectSelectVm.name"></span>
                    </td>
                    <td style="width: 150px;text-align: center;">{{inspectionPlan.revision}}</td>
                </tr>
                </tbody>
            </table>
            <table class="table table-striped highlight-row"
                   ng-if="qualityObjectSelectVm.selectedQualityObjectType === 'PRTYPE'">
                <thead>
                <tr>
                    <th style="width: 40px" translate>SELECT</th>
                    <th class="col-width-100" translate>PR_NUMBER</th>
                    <th class="col-width-100" translate>PRODUCT</th>
                    <th class="col-width-200" translate>PROBLEM</th>
                    <th class="col-width-100" translate>SEVERITY</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="qualityObjectSelectVm.problemReports.content.length == 0">
                    <td colspan="25" translate>NO_PRS</td>
                </tr>

                <tr ng-repeat="problemReport in qualityObjectSelectVm.problemReports.content">
                    <td style="width: 40px;">
                        <input type="radio" ng-checked="problemReport.checked"
                               ng-click="qualityObjectSelectVm.radioChange(problemReport, $event)"
                               ng-dblclick="qualityObjectSelectVm.selectRadioChange(problemReport, $event)">
                    </td>
                    <td class="col-width-100">
                        <a href=""
                           ng-click="showPrDetails(problemReport)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="problemReport.prNumber | highlightText: qualityObjectSelectVm.number"></span>
                        </a>
                    </td>
                    <td class="col-width-100">
                        <span ng-bind-html="problemReport.product | highlightText: qualityObjectSelectVm.searchText"></span>
                    </td>
                    <td class="col-width-200">
                        <span ng-bind-html="problemReport.problem  | highlightText: qualityObjectSelectVm.name"></span>
                    </td>
                    <td class="col-width-100">
                        <span ng-bind-html="problemReport.severity | highlightText: qualityObjectSelectVm.searchText"></span>
                    </td>
                </tr>
                </tbody>
            </table>
            <table class="table table-striped highlight-row"
                   ng-if="qualityObjectSelectVm.selectedQualityObjectType === 'NCRTYPE'">
                <thead>
                <tr>
                    <th style="width: 40px" translate>SELECT</th>
                    <th class="col-width-100" translate>NCR_NUMBER</th>
                    <th class="col-width-100" translate>NCR_TYPE</th>
                    <th class="col-width-200" translate>TITLE</th>
                    <th class="col-width-100" translate>SEVERITY</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="qualityObjectSelectVm.ncrs.content.length == 0">
                    <td colspan="25" translate>NO_NCRS</td>
                </tr>

                <tr ng-repeat="ncr in qualityObjectSelectVm.ncrs.content">
                    <td style="width: 40px;">
                        <input type="radio" ng-checked="ncr.checked"
                               ng-click="qualityObjectSelectVm.radioChange(ncr, $event)"
                               ng-dblclick="qualityObjectSelectVm.selectRadioChange(ncr, $event)">
                    </td>
                    <td class="col-width-100">
                        <a href=""
                           ng-click="showNcrDetails(ncr)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="ncr.ncrNumber | highlightText: qualityObjectSelectVm.number"></span>
                        </a>
                    </td>
                    <td class="col-width-100">
                        <span ng-bind-html="ncr.ncrType | highlightText: qualityObjectSelectVm.searchText"></span>
                    </td>
                    <td class="col-width-200" title="{{ncr.title}}">
                        <span ng-bind-html="ncr.title | highlightText: qualityObjectSelectVm.name"></span>
                    </td>
                    <td title="{{ncr.severity}}" class="col-width-100">
                        <span ng-bind-html="ncr.severity | highlightText: qualityObjectSelectVm.searchText"></span>
                    </td>
                </tr>
                </tbody>
            </table>
            <table class="table table-striped highlight-row"
                   ng-if="qualityObjectSelectVm.selectedQualityObjectType === 'QCRTYPE'">
                <thead>
                <tr>
                    <th style="width: 40px" translate>SELECT</th>
                    <th class="col-width-100" translate>QCR_NUMBER</th>
                    <th class="col-width-100" translate>QCR Type</th>
                    <th class="col-width-100" translate>QCR For</th>
                    <th class="col-width-200" translate>TITLE</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="qualityObjectSelectVm.qcrs.content.length == 0">
                    <td colspan="25" translate>NO_QCRS</td>
                </tr>

                <tr ng-repeat="qcr in qualityObjectSelectVm.qcrs.content">
                    <td style="width: 40px;">
                        <input type="radio" ng-checked="qcr.checked"
                               ng-click="qualityObjectSelectVm.radioChange(qcr, $event)"
                               ng-dblclick="qualityObjectSelectVm.selectRadioChange(qcr, $event)">
                    </td>
                    <td class="col-width-100">
                        <a href="" ng-click="showQcrDetails(qcr)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="qcr.qcrNumber | highlightText: qualityObjectSelectVm.number"></span>
                        </a>
                    </td>
                    <td class="col-width-100">
                        <span ng-bind-html="qcr.qcrType | highlightText: qualityObjectSelectVm.searchText"></span>
                    </td>
                    <td class="col-width-100">{{qcr.qcrFor}}</td>
                    <td class="col-width-200">
                        <span ng-bind-html="qcr.title  | highlightText: qualityObjectSelectVm.name"></span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

