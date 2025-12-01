<style>
    .just-box-shadow {
        box-shadow: 0 0 0 rgba(0, 0, 0, 0.3), 0px 0px 40px rgba(0, 0, 0, 0.1) inset;
    }

    .selected-report {
        border: 3px solid #679FC8 !important;
    }
</style>
<div fitcontent>
    <div class="row" style="height: 100%;padding-left: 7px; margin-right: -15px;">
        <div class="col-sm-3 styled-panel" style="height: 100%;padding-top: 10px;">
            <div class="row">
                <div class="form-group col-sm-12" style="padding: 0">
                    <label class="control-label">Module</label>
                    <ui-select ng-model="selectedModule" on-select="onSelectModule($item, $model)" theme="bootstrap"
                               style="width: 100%;" title="Select a module">
                        <ui-select-match placeholder="Select a module">{{$select.selected.name}}</ui-select-match>
                        <ui-select-choices repeat="report in reports | filter: $select.search">
                            <div ng-bind-html="report.name | highlight: $select.search"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>
            <div class="row reports-container">
                <div class="col-sm-12" style="padding-top: 10px;padding-bottom: 5px;">
                    <div class="just-box-shadow"
                         ng-repeat="report in selectedModule.reports"
                         ng-class="{'selected-report': report.selected}"
                         ng-click="selectReport(report)"
                         ng-if="report.viewType != 'none'"
                         style="background-color: #FFF; border: 1px solid #DDD; cursor: pointer;
                         margin-bottom: 10px; padding: 10px; padding-bottom: 0">
                        <div class="pull-left" style="line-height: 50px;font-size: 40px;">
                            <span class="text-info">
                                <i class="fa" ng-class="{'fa-bar-chart-o':report.viewType == 'chart.bar.vertical',
                                    'fa-pie-chart':report.viewType == 'chart.pie',
                                    'fa-table': report.viewType == 'custom'}"></i>
                            </span>
                        </div>
                        <div style="margin-left: 60px">
                            <h4 class="text-primary" style="margin: 0">{{report.name}}</h4>

                            <p>{{report.description}}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-sm-9" style="height: 100%; padding-left: 10px">
            <div class="styled-panel" style="height: 100%;">
                <div class="row reports-container"
                     style="overflow: hidden; margin-left: 10px; margin-right: 10px; top: 10px; cursor: default">
                    <vertical-bar-chart report="selectedReport"
                                        ng-if="selectedReport.viewType == 'chart.bar.vertical'"></vertical-bar-chart>
                    <pie-chart report="selectedReport" ng-if="selectedReport.viewType == 'chart.pie'"></pie-chart>

                    <div
                            ng-controller="UnfinishedTasksController"
                            ng-include="selectedReport.template">
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>