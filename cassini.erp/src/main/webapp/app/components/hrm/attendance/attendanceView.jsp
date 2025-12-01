<script type="text/ng-template" id="attendance-view-tb">
    <div class="row" ng-show="true">
        <div class="col-md-12 mb20">
            
            <button class="btn btn-sm btn-success" data-ng-click="navigateTimeOffs();">TimeOffs</button>
        </div>
    </div>
</script>

<div>
    <div id="attendance" style="text-align:center">
        <div class="row">
            <div class="col-md-12 col-lg-8 col-lg-offset-2 styled-panel">
                <div class="row">
                    <div class="pull-left"  style="vertical-align: middle;padding-top: 20px;text-align: right;">
                        <div class="btn-group" style="padding-left: 14px;">
                            <button class="btn btn-default" style="width:120px" ng-click="previousYear()">
                                <i class="fa fa-arrow-left mr10"></i>PREVIOUS
                            </button>
                        </div>
                    </div>
                    <div class="pull-right"  style="vertical-align: middle;padding-top: 20px;text-align: right;">
                        <div class="btn-group" style="padding-right: 14px;">
                            <button class="btn btn-default" style="width:120px" ng-disabled="isYearCurrent()" ng-click="nextYear()">
                                NEXT<i class="fa fa-arrow-right" style="margin-left:10px"></i>
                            </button>
                        </div>
                    </div>
                    <div style="padding-bottom: 20px;text-align: center;">
                        <h3 style="font-size: 40px; text-align: center;margin-left: 150px;">{{currentYear}}</h3>
                    </div>
                </div>

                <div ng-repeat="row in months">
                    <div class="row">
                        <div ng-repeat="month in row" on-finish-render="ngRepeatFinished">
                            <div class="col-md-4">
                                <div class="panel panel-default" style="height:230px">
                                    <div class="panel-heading" style="background-color: #E4E7EA;">
                                        <div class="panel-title" style="text-align: center">{{month.name}}</div>
                                    </div>
                                    <div style="text-align: center;margin: 20px;">
                                        <div ng-if="isMonthCurrent(month.index) && !isAttendanceImported(month.index) && isYearCurrent()">
                                            <div style="margin-bottom: 5px;">
                                                <span id="addAttendanceBtn" class="btn btn-xs btn-success fileinput-button">
                                                    <i class="glyphicon glyphicon-plus"></i>
                                                    <span>Add Attendance Files</span>
                                                </span>
                                                <button id="importAttendanceBtn" class="btn btn-sm btn-primary" ng-click="importAttendance();">
                                                    <i class="glyphicon glyphicon-import"></i>
                                                    <span>Import Attendance Files</span>
                                                </button>
                                            </div>

                                            <form onsubmit="return false;" action="" id="attachmentsForm" method="post" enctype="multipart/form-data" >
                                                <div id="attachmentsContainer" class="attachments-dropzone-container">
                                                    <div id="attachmentsDropZone" style="display: table; width: 100%;">
                                                        <div class="attachments-dropzone">
                                                            <div style="width: 100%; min-height: 50px; padding-top: 15px; display: inline-block; text-align: center">
                                                                <span ><i>You can drag and drop files here!</i></span>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div id="previews">
                                                        <div id="template">
                                                            <div class="row" style="border-bottom: 1px dotted lightgrey; margin-bottom: 10px;padding-bottom: 10px;">
                                                                <div class="col-sm-12">
                                                                    <p class="name" data-dz-name></p>
                                                                    <small class="error text-danger" data-dz-errormessage></small>
                                                                </div>
                                                                <div class="col-sm-12">
                                                                    <button data-dz-remove class="btn btn-sm btn-danger delete">
                                                                        <i class="glyphicon glyphicon-trash"></i>
                                                                    </button>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>
                                           </div>
                                        <button data-ng-click="viewAttendance(month.index);" class="btn btn-sm btn-default"ng-if="((!isMonthCurrent(month.index) && isMonthBeforeCurrentMonth(month.index)) || isAttendanceImported(month.index)) || !isYearCurrent()" style="margin-top: 40px;">View Attendance</button>
                                        <%--<button data-ng-click="importAttendance(month.index);" class="btn btn-sm btn-primary" ng-if="isMonthCurrent(month.index) && !isAttendanceImported(month.index) && isYearCurrent()" style="margin-top: 40px;">Import Attendance</button>--%>
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