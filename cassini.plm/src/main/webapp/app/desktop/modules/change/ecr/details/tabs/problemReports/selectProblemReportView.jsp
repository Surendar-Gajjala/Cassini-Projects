<div style="padding: 0px 10px;">
    <style scoped>
        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }
    </style>
    <form class="form-inline ng-pristine ng-valid" style="margin: 10px 0px;display: flex;flex-direction: row;">
        <div class="form-group" style="flex-grow: 1;margin-right: 0;">
            <div class="input-group input-group-sm" style="">
                <div class="input-group-btn dropdown" uib-dropdown="" style="">
                    <button uib-dropdown-toggle="" class="btn btn-default dropdown-toggle" type="button"
                            aria-haspopup="true" aria-expanded="false"
                            style="background-color: #fff;color: #333 !important;">
                        <span class="ng-scope" translate>SELECT</span> <span class="caret"
                                                                             style="margin-left: 4px;"></span>
                    </button>
                    <div class="dropdown-menu" role="menu">
                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                            <quality-type-tree on-select-type="selectProblemReportVm.onSelectType"
                                               quality-type="PROBLEMREPORTTYPE"></quality-type-tree>
                        </div>
                    </div>
                </div>
                <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                       placeholder="{{'SELECT_TYPE' | translate}}"
                       style="background-color: #fff;" ng-model="selectProblemReportVm.prType.type.name" readonly="">

            </div>
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
            <input type="text" ng-model="selectProblemReportVm.prFilter.prNumber"
                   ng-change="selectProblemReportVm.searchParts()"
                   style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;"
                   placeholder="{{'ENTER_NUMBER' | translate}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
            <input type="text" ng-model="selectProblemReportVm.prFilter.problem"
                   ng-change="selectProblemReportVm.searchParts()"
                   style="margin-left: -16px; height: 30px;width: 100%;margin-top: 0;"
                   placeholder="{{'ENTER_PROBLEM_PLACEHOLDER' | translate}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched">
        </div>
        <div style="margin-top: 0;flex-grow: 1;" class="">
            <button ng-click="selectProblemReportVm.clearFilter()" style="height: 29px;width: 100%"
                    class="btn btn-xs btn-danger ng-scope" ng-if="selectProblemReportVm.clear" translate>CLEAR
            </button>
        </div>
    </form>
    <div class="row">
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-4" style="padding-left: 0;padding-top: 10px;">
                <div style="">
                    <span style="" translate>SELECTED_PROBLEM_REPORTS</span>
                    <span class="badge badge-success">{{selectProblemReportVm.selectedParts.length}}</span>
                </div>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="selectProblemReportVm.problemReports.numberOfElements == 0">
                                    {{(selectProblemReportVm.pageable.page*selectProblemReportVm.pageable.size)}}
                                </span>
                                <span ng-if="selectProblemReportVm.problemReports.numberOfElements > 0">
                                    {{(selectProblemReportVm.pageable.page*selectProblemReportVm.pageable.size)+1}}
                                </span>-
                                <span ng-if="selectProblemReportVm.problemReports.last ==false">{{((selectProblemReportVm.pageable.page+1)*selectProblemReportVm.pageable.size)}}</span>
                                <span ng-if="selectProblemReportVm.problemReports.last == true">{{selectProblemReportVm.problemReports.totalElements}}</span>
                                <span translate> OF </span> {{selectProblemReportVm.problemReports.totalElements}}<span
                                        translate>AN</span></span>
                            </medium>
                        </span>
                        <span class="mr10">Page {{selectProblemReportVm.problemReports.totalElements != 0 ? selectProblemReportVm.problemReports.number+1:0}}
                            <span translate>OF</span> {{selectProblemReportVm.problemReports.totalPages}}
                        </span>
                        <a href="" ng-click="selectProblemReportVm.previousPage()"
                           ng-class="{'disabled': selectProblemReportVm.problemReports.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="selectProblemReportVm.nextPage()"
                           ng-class="{'disabled': selectProblemReportVm.problemReports.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-12 problem-reports-view" style="padding:0px; height: auto;overflow: auto;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="text-align: center;width: 20px !important;">
                        <input name="item" type="checkbox"
                               ng-if="selectProblemReportVm.problemReports.content.length > 0"
                               ng-model="selectProblemReportVm.selectAllCheck"
                               ng-click="selectProblemReportVm.checkAll(check);" ng-checked="check">
                    </th>
                    <th class="col-width-100" translate>NUMBER</th>
                    <th class="col-width-150" translate>TYPE</th>
                    <th class="col-width-200" translate>PRODUCT</th>
                    <th style="width: 100px;text-align: center" translate>REVISION</th>
                    <th class="col-width-200" translate>PROBLEM</th>
                    <th class="col-width-200" translate>DESCRIPTION</th>
                    <th class="col-width-100" translate>DEFECT_TYPE</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="selectProblemReportVm.problemReports.content.length == 0">
                    <td colspan="15" translate>NO_PROBLEM_REPORTS</td>
                </tr>
                <tr ng-if="selectProblemReportVm.problemReports.content.length > 0"
                    ng-repeat="problemReport in selectProblemReportVm.problemReports.content">
                    <td style="vertical-align: middle;text-align: center;width: 20px !important;">
                        <input name="problemReport" type="checkbox" ng-model="problemReport.selected"
                               ng-click="selectProblemReportVm.select(problemReport)">
                    </td>

                    <td class="col-width-100">
                        <span ng-bind-html="problemReport.prNumber | highlightText: selectProblemReportVm.prFilter.prNumber"></span>
                    </td>
                    <td class="col-width-150" title="{{problemReport.prType}}">
                        <span ng-bind-html="problemReport.prType  | highlightText: selectProblemReportVm.prFilter.type.name"></span>
                    </td>
                    <td class="col-width-200" title="{{problemReport.product}}">
                        {{problemReport.product}}
                    </td>
                    <td style="text-align: center;">
                        {{problemReport.revision}}
                    </td>
                    <td class="col-width-200" title="{{problemReport.problem}}" class="col-width-200">
                        <span ng-bind-html="problemReport.problem  | highlightText: selectProblemReportVm.prFilter.problem"></span>
                    </td>
                    <td title="{{problemReport.description}}" class="col-width-200">
                        <span ng-bind-html="problemReport.description"></span>
                    </td>
                    <td class="col-width-100" title="{{problemReport.failureType}}">
                        <span ng-bind-html="problemReport.failureType"></span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

