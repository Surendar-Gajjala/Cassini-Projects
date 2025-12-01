<div style="position: relative;">
    <style scoped>

        .datetimepicker thead tr:first-child th .glyphicon-arrow-right::before {
            display: none !important;
        }

        .datetimepicker thead tr:first-child th .glyphicon-arrow-left::before {
            display: none !important;
        }

        .datetimepicker thead tr:first-child th:hover .glyphicon-arrow-right::before {
            display: none !important;
        }

        .datetimepicker thead tr:first-child th:hover .glyphicon-arrow-left::before {
            display: none !important;
        }

        .datetimepicker thead tr:first-child th:hover, .datetimepicker tfoot th:hover, .datetimepicker table tr td.day:hover {
            visibility: hidden !important;
        }

    </style>
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">

                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>


                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newShiftVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newShiftVm.newShift.number">
                            </div>
                        </div>

                        <%--<div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{'ENTER_NUMBER' | translate}}" name="title" ng-model="newShiftVm.newShift.number">
                        </div>--%>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{'ENTER_NAME' | translate}}"
                                   name="title" ng-model="newShiftVm.newShift.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newShiftVm.newShift.description"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>START_TIME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" id="startTime" shift-time-picker class="form-control" name="title"
                                   placeholder="{{'ENTER_START_TIME' | translate}}"
                                   ng-model="newShiftVm.newShift.localStartTime">


                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>END_TIME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" id="endTime" class="form-control" shift-time-picker name="title"
                                   placeholder="{{'ENTER_END_TIME' | translate}}"
                                   ng-model="newShiftVm.newShift.localEndTime">
                        </div>
                    </div>

                </form>

            </div>
        </div>
    </div>
</div>
