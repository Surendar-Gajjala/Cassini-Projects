<div>
    <style scoped>
        .home-content {
            padding: 10px !important;
        }

        .home-content .home-widget {
            border-radius: 5px;
        }

        .home-content .home-widget .home-widget-heading {
            height: 50px;
            line-height: 50px;
            vertical-align: middle;
            padding-left: 20px;
            border-bottom: 1px solid #eee;
            font-size: 18px;
        }

        .home-content .home-widget .home-widget-body {
            padding: 10px;
            min-height: 300px;
            max-height: 300px;
            overflow-y: auto;
        }

        .home-content > .home-content-row {
            display: -webkit-flex;
            display: flex;
            margin-bottom: 10px;
        }

        .home-content > .home-content-row > .home-content-col {
            -webkit-flex-grow: 1;
            flex-grow: 1;
            margin-left: 10px;
        }

        .home-content > .home-content-row > .home-content-col:first-child {
            margin-left: 0;
        }

        .panel.panel-default .panel-heading {
            background-color: #fff !important;
            padding: 0;
            height: 50px;
            border: 1px solid #dddddd;
        }

        .panel .panel-heading .pull-right .btn-group {
            display: inline-block;
            margin: 0 10px 0 0;
        }

        .panel .panel-heading .pull-right .btn-group .btn {
            border: 0;
            background-color: #fff;
        }

        .panel .panel-heading .pull-right {
            margin-top: 10px;
            padding: 2px;
        }

        .panel .panel-heading .pull-right small {
            color: #ccc;
        }

        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }

        .new {
            color: white;
            background:#3ccbbf !important;
        }

        .inprogress {
            color: white;
            background: #6be1f2 !important;
        }

        .completed {
            color: white;
            background:#68d3ab !important;
        }
    </style>
    <div class="view-container no-padding" style="overflow-y: auto;" applicationfitcontent>
        <div class="view-toolbar">
            <div style="text-align: center;margin-top: 15px;margin-left: 15px;color: #002451;font-weight: bold;font-size: 20px;">
                <span>Complaint Tracker</span>
            </div>
        </div>

        <div class="view-content no-padding" style="overflow-y: auto;">
            <div class="row" style="margin: 0;padding: 20px;">
                <div class="col-md-6 col-sm-8 col-md-offset-3 col-md-sm-4"
                     style="border: 1.5px solid grey;padding: 10px;border-radius: 5px;">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                <span><b>Complaint Number</b></span>
                                <span class="asterisk">*</span> :
                            </label>

                            <div class="col-sm-6">
                                <div class="input-group mb15">
                                    <input type="text" class="form-control" name="title"
                                           ng-model="dashboardVm.complaintNumber"
                                           ng-enter="dashboardVm.trackComplaint()">

                                    <div class="input-group-btn">
                                        <button class="btn btn-default" type="button" style="width: 85px"
                                                ng-click="dashboardVm.trackComplaint()">Search
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <h4 ng-if="dashboardVm.errorMessage != null" style="text-align: center;color: #ae0000;">
                            <b>{{dashboardVm.errorMessage}}</b>
                        </h4>

                        <div ng-if="dashboardVm.complaint != null">
                            <%--<div class="form-group">
                                <label class="col-sm-4 control-label">
                                    Complaint Number :
                                </label>

                                <div class="col-sm-6" style="margin-top: 10px;">
                                    <span>{{dashboardVm.complaint.complaintNumber}}</span>
                                </div>
                            </div>--%>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <b>Complainant :</b>
                                </label>

                                <div class="col-sm-6" style="margin-top: 10px;">
                                    <span>
                                {{dashboardVm.complaint.personObject.traineeId}}{{dashboardVm.complaint.personObject.designation}}</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <b>Status :</b>
                                </label>

                                <div class="col-sm-6" style="margin-top: 10px;">
                                    <span class="flag"
                                          ng-class="{'new':dashboardVm.complaint.status == 'NEW',
                                         'inprogress':dashboardVm.complaint.status == 'INPROGRESS',
                                         'completed':dashboardVm.complaint.status == 'COMPLETED'}">{{dashboardVm.complaint.status}}</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <b>Utility :</b>
                                </label>

                                <div class="col-sm-6" style="margin-top: 10px;">
                                    <span>{{dashboardVm.complaint.utility}}</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <b>Location :</b>
                                </label>

                                <div class="col-sm-6" style="margin-top: 10px;">
                                    <span>{{dashboardVm.complaint.location}}</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <b> Created Date :</b>
                                </label>

                                <div class="col-sm-6" style="margin-top: 10px;">
                                    <span>{{dashboardVm.complaint.createdDate}}</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <b> Details :</b>
                                </label>

                                <div class="col-sm-6" style="margin-top: 10px;">
                                    <span>{{dashboardVm.complaint.details}}</span>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>
