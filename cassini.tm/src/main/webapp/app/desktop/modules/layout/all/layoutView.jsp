<div class="view-container">
    <div class="view-toolbar">
        <div class="col-sm-2">
            <button class="btn btn-sm btn-success">New Layout</button>
        </div>
        <div class="col-sm-3">
            <ui-select ng-model="layoutVm.project"
                       on-select="layoutVm.layoutProject(layoutVm.project)" theme="bootstrap"
                       style="width:200px;">
                <ui-select-match allow-clear="true" placeholder="All Projects">{{$select.selected.name}}
                </ui-select-match>
                <ui-select-choices
                        repeat="project in layoutVm.projects.content | filter: $select.search">
                    <div ng-bind="project.name"></div>
                </ui-select-choices>
            </ui-select>
        </div>
    </div>


    <div class="view-content">
        <div class="tab-pane active" id="payroll" style="text-align:center">
            <div class="row">
                <div class="col-md-12 col-lg-8 col-lg-offset-2 styled-panel"
                     style="border: 1px solid #dddddd; padding: 20px;">
                    <div class="row">
                        <div class="pull-left" style="vertical-align: middle;padding-top: 20px;text-align: right;">
                            <div class="btn-group" style="padding-left: 14px;">
                                <button ng-click="layoutVm.previousMonth()" class="btn btn-sm btn-default"
                                        style="width:120px">
                                    <i class="fa fa-arrow-left mr10"></i>PREVIOUS
                                </button>
                            </div>
                        </div>
                        <div class="pull-right" style="vertical-align: middle;padding-top: 20px;text-align: right;">
                            <div class="btn-group" style="padding-right: 14px;">
                                <button ng-click="layoutVm.nextMonth()" class="btn btn-sm btn-default"
                                        style="width:120px">
                                    NEXT<i class="fa fa-arrow-right" style="margin-left:10px"></i>
                                </button>
                            </div>
                        </div>
                        <div style="padding-bottom: 20px;text-align: center;">
                            <h3 style="font-size: 40px; text-align: center;margin-left: 150px;">
                                {{layoutVm.monthName}}/{{layoutVm.year}}</h3>
                        </div>
                    </div>

                    <div>
                        <%--<div class="row">--%>
                        <div class="col-md-2" ng-repeat="monthDay in layoutVm.currentMonthDays">
                            <div class="panel panel-default" style="height:230px">
                                <div class="panel-heading"
                                     style="background-color: #E4E7EA; border: 1px solid #dddddd;">
                                    <div class="panel-title" style="text-align: center">{{monthDay}}</div>
                                </div>
                                <div class="panel-body"
                                     style="text-align: center; height:100px; background: #ffffff;">
                                    <div ng-if="layoutVm.project != null">
                                        <button class="btn btn-sm btn-info"
                                                ng-if="monthDay < layoutVm.day && layoutVm.currentMonth == (layoutVm.month+1) &&
                                            layoutVm.currentYear == layoutVm.year"
                                                ng-click="layoutVm.downloadFile(monthDay)"
                                                style="margin-top: 25px;">View Drawing
                                        </button>
                                        <%--<button class="btn btn-sm btn-success fileinput-button"
                                                ng-if="monthDay >= layoutVm.day && layoutVm.currentMonth == (layoutVm.month+1) &&
                                                layoutVm.currentYear == layoutVm.year"
                                                ng-disabled="monthDay > layoutVm.day"
                                                style="margin-top: 25px;">Upload Drawing...
                                        </button>--%>
                                        <div class="input-group input-group-sm"
                                             ng-if="monthDay >= layoutVm.day && layoutVm.currentMonth == (layoutVm.month+1) &&
                                            layoutVm.currentYear == layoutVm.year">
                                            <input ng-hide="true" type="file" id="keyFile" file-model="layoutVm.keyFile"
                                            <%--style="width:0; height:0;display: inline-block;"--%>
                                                   onchange="angular.element(this).scope().addFile(this.files)"/>
                                             <span class="input-group-btn">
                                            <button ng-disabled="monthDay > layoutVm.day"
                                                    style="margin-top: 25px"
                                                    type="button" class="btn btn-success"
                                                    onclick="document.getElementById('keyFile').click();">Upload
                                                Drawing..
                                            </button>
                                            </span>
                                        </div>
                                        <button class="btn btn-sm btn-info"
                                                ng-click="layoutVm.downloadFile(monthDay)"
                                                ng-if="layoutVm.currentMonth > (layoutVm.month+1) && layoutVm.currentYear == layoutVm.year"
                                                style="margin-top: 25px;">View Drawing
                                        </button>
                                        <button class="btn btn-sm btn-success"
                                                ng-if="layoutVm.currentMonth < (layoutVm.month+1) && layoutVm.currentYear == layoutVm.year"
                                                ng-disabled="true"
                                                style="margin-top: 25px;">Upload Drawing
                                        </button>
                                        <button class="btn btn-sm btn-info"
                                                ng-click="layoutVm.downloadFile(monthDay)"
                                                ng-if="layoutVm.currentYear > layoutVm.year"
                                                style="margin-top: 25px;">View Drawing
                                        </button>
                                        <button class="btn btn-sm btn-success"
                                                ng-if="layoutVm.currentYear < layoutVm.year"
                                                ng-disabled="true"
                                                style="margin-top: 25px;">Upload Drawing
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>