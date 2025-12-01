<div>
    <style scoped>
        .item-timeline {
            position: relative;
            width: 100%;
            margin: 0 auto;
            /*margin-top: 20px;*/
            padding: 1em 0;
            list-style-type: none;
            /*border-right: 1px solid lightgrey;*/
        }

        .timeline-container {
            width: 100%;
            display: flex;
        }

        .timeline-container .timeline-panel {
            width: calc(100% - 300px);
        }

        .timeline-container .timeline-filter-panel {
            position: fixed;
            width: 300px;
            padding: 20px;
            bottom: 46px;
            top: 208px;
            border-left: 1px solid #ddd !important;
        }

        .timeline-container .form-horizontal .form-group {
            width: 250px;
            margin-left: auto;
            margin-right: auto;
        }

        .item-timeline:before {
            position: absolute;
            left: 250px;
            top: 0;
            content: ' ';
            display: block;
            width: 1px;
            height: 100%;
            margin-left: -3px;
            background: #ddd;
            z-index: 5;
        }

        .item-timeline li {
            padding: .5em 0;
        }

        .item-timeline li:after {
            content: "";
            display: block;
            height: 0;
            clear: both;
            visibility: hidden;
        }

        .item-direction-l {
            position: relative;
            width: 230px;
            float: left;
            text-align: right;
        }

        .item-direction-r {
            position: relative;
            width: calc(100% - 270px);
            float: right;
        }

        .item-flag-wrapper {
            position: relative;
            display: inline-block;
            padding-top: 2px;
            text-align: center;
        }

        .item-flag {

        }

        .item-date-flag {

        }

        .item-direction-l .item-flag {
            -webkit-box-shadow: -1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            -moz-box-shadow: -1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            box-shadow: -1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
        }

        .item-direction-r .item-flag {
            -webkit-box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            -moz-box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
        }

        .item-direction-l .item-date-flag {
            -webkit-box-shadow: -1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            -moz-box-shadow: -1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            box-shadow: -1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
        }

        .item-direction-r .item-date-flag {
            -webkit-box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            -moz-box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
        }

        .item-direction-l .item-flag:before,
        .item-direction-r .item-flag:before {
            position: absolute;
            top: 20px;
            right: -43px;
            content: ' ';
            display: block;
            width: 16px;
            height: 16px;
            margin-top: -18px;
            background-color: #36b2ef;
            border-radius: 50%;
            border: 2px solid #f6f8fa;
            z-index: 10;
        }

        .item-direction-l .item-date-flag:before,
        .item-direction-r .item-date-flag:before {
            position: absolute;
            top: 12px;
            right: -40px;
            content: ' ';
            display: block;
            width: 16px;
            height: 16px;
            margin-top: -10px;
            background: #f32f2f;
            border-radius: 10px;
            border: 2px solid #fff;
            z-index: 10;
        }

        .item-direction-r .item-flag:before {
            left: -30px;
        }

        .item-direction-r .item-date-flag:before {
            left: -30px;
        }

        .item-time-wrapper {
            display: inline;
            line-height: 1em;
            font-size: 0.66666em;
            vertical-align: middle;
            margin-top: 0;
        }

        .item-direction-l .item-time-wrapper {
            float: left;
        }

        .item-direction-r .item-time-wrapper {
            float: right;
        }

        .time {
            display: inline-block;
            padding: 4px 6px;
        }

        .desc {
            margin: 1em 0.75em 0 0;

            font-size: 0.77777em;
            font-style: italic;
            line-height: 1.5em;
        }

        .item-direction-r .desc {
            margin: 1em 0 0 0.75em;
        }

        .item-flag i {
            font-size: 16px;
            margin-left: -29px;
            position: absolute;
            z-index: 11;
            margin-top: 5px;
            color: white;
        }

        .highlight-color {
            color: #337ab7;
        }

        .italic-font {
            font-style: italic;
        }

        .timeline-container .no-timeline-container {
            position: absolute;
            top: 0;
            bottom: 0;
            left: 0;
            right: 300px;
        }

        .timeline-container .no-timeline-container .no-timeline {
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            text-align: center;
            position: absolute;
            width: 100%;
        }

        .timeline-container .no-timeline-container .no-timeline img {
            width: 400px;
        }

        .timeline-container .no-timeline-container .no-timeline .no-timeline-message {
            font-size: 20px;
            font-weight: 300 !important;
        }


    </style>

    <div class="timeline-container" style="">
        <style scoped>
            .timeline-container .popover {
                width: 250px !important;
                max-width: 250px !important;
                min-width: 250px !important;
            }

            .timeline-container .popover-content {
                padding: 0px;
            }
        </style>
        <div class="timeline-panel" style="">
            <div class="no-timeline-container"
                 ng-if="!loading && historyCount == 0">
                <div class="no-timeline">
                    <img src="app/assets/no_data_images/time.png" alt="">

                    <div class="no-timeline-message">
                        <span ng-hide="(activityStreamFilter.date != null && activityStreamFilter.date != '') || (selectedUser !=null && selectedUser !='')"
                              translate>NO_TIMELINE_MESSAGE</span>
                        <span ng-if="(activityStreamFilter.date != null && activityStreamFilter.date != '') || (selectedUser !=null && selectedUser !='')"
                              translate>NO_TIMELINE_FILTER_MESSAGE</span>
                    </div>

                </div>
            </div>
            <ul class="item-timeline" ng-if="historyCount > 0">
                <div ng-repeat="(key,histories) in objectHistory">
                    <li style="padding-bottom: 20px;">
                        <div class="item-direction-l"
                             style="border-bottom: 1px solid #ddd;margin-top: 10px;margin-right: -5px;">
                        </div>
                        <div class="item-direction-r">
                            <div class="item-flag-wrapper" style="text-align: left !important;">
                                <span class="item-date-flag" style=""></span>
                        <span class="item-time-wrapper">
                            <span class="time" style="font-size: 16px;font-weight: 300;">
                             <span ng-if="key == todayDate">TODAY</span>
                             <span ng-if="key != todayDate">{{key}}</span>
                            </span>
                        </span>
                            </div>
                            <hr style="margin-bottom: 0;margin-top: 10px;border-top: 1px solid #ddd;width: calc(100% - 100px);float: right;">
                        </div>
                    </li>
                    <li ng-repeat="history in histories">
                        <div class="item-direction-l">
                            <div class="item-flag-wrapper" style="text-align: left !important;">
                                <span class="item-time-wrapper">
                                    <span class="time" style="font-size: 14px;">
                                        {{history.time}}
                                    </span>
                                </span>
                            </div>
                        </div>
                        <div class="item-direction-r">
                            <div class="item-flag-wrapper" style="text-align: left !important;">
                                <span class="item-flag" style=""></span>

                                <div class="item-time-wrapper">
                                    <div class="time" style="font-size: 14px;">
                                        <p style="color: #2a6fa8;">
                                            <span>{{history.actor.fullName}}</span>
                                            <a class="icon fa fa-info-circle"
                                               style="padding-left: 5px; overflow-y: hidden !important;vertical-align: middle;"
                                               title="Session Info"
                                               ng-if="history.sessionObject != null && history.sessionObject != '' && hasPermission"
                                               uib-popover-template="sessionPopover.templateUrl"
                                               popover-append-to-body="true"
                                               popover-popup-delay="50"
                                               popover-placement="right"
                                               popover-trigger="'outsideClick'">
                                            </a>
                                        </p><br>

                                        <span style="line-height: 20px;" ng-bind-html="history.summary"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </li>
                </div>
                <li ng-if="lastPage == false" style="padding: 25px 0">
                    <div class="item-direction-r">
                        <div class="item-flag-wrapper" style="text-align: left !important;">
                            <a href="" ng-click="loadMoreHistory()" style="">&#x2193; Load
                                More</a>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
        <div>
            <form class="timeline-filter-panel form-horizontal" style="">
                <h4 class="text-center" translate>FILTERS</h4>

                <%--<div class="form-group">
                    <label class="control-label">
                        <span translate>REVISION</span> </label>
                    <br>

                    <div class="">
                        <ui-select ng-model="selectedRevision" theme="bootstrap"
                                   on-select="selectRevision($item)"
                                   on-remove="selectRevision()">
                            <ui-select-match allow-clear="true" placeholder="Select Revision">
                                {{$select.selected.revision}}
                            </ui-select-match>
                            <ui-select-choices repeat="itemRevision in itemRevisions">
                                <div ng-bind-html="itemRevision.revision"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>--%>
                <%--<div class="form-group">
                    <label class="control-label">
                        <span translate>TYPE</span> </label>
                    <br>

                    <div class="">
                        <ui-select ng-model="selectedType" theme="bootstrap"
                                   on-select="selectType($item)"
                                   on-remove="selectType()">
                            <ui-select-match allow-clear="true" placeholder="Select Type">
                                {{$select.selected.label}}
                            </ui-select-match>
                            <ui-select-choices repeat="type in types">
                                <div ng-bind-html="type.label"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>--%>
                <div class="form-group">
                    <label class="control-label" style="text-align: right;">
                        <span translate>DATE</span> </label>
                    <br>

                    <div class="">
                        <input type="text" class="form-control input-sm" ng-model="selectedDate.date"
                               placeholder="{{selectDate}}" inward-date-picker/>
                        <i class="fa fa-times" ng-if="activityStreamFilter.date != null"
                           style="margin-top: -25px;margin-right: 10px;float: right;cursor: pointer;"
                           ng-click="selectedDate.date = null;loadActivityStream()"></i>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">
                        <span translate>USER</span> </label>

                    <div class="">
                        <ui-select dropup ng-model="selectedUser" theme="bootstrap"
                                   on-select="selectUser($item)"
                                   on-remove="selectUser()">
                            <ui-select-match allow-clear="true" placeholder="Select User">
                                {{$select.selected.fullName}}
                            </ui-select-match>
                            <ui-select-choices position="up" repeat="person in persons">
                                <div ng-bind-html="person.fullName"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>
                <div class="form-group" ng-if="objectType == 'PROGRAM' || objectType == 'PROJECT' || objectType == 'PLANT' || objectType == 'ASSEMBLYLINE' || objectType == 'MACHINE' || objectType == 'EQUIPMENT' || objectType == 'SHIFT' || objectType == 'MATERIAL' || objectType == 'JIGFIXTURE' 
                || objectType == 'TOOL' || objectType == 'OPERATION' || objectType == 'MANPOWER' || objectType == 'INSTRUMENT'  || objectType == 'WORKCENTER' || objectType == 'MBOM' || objectType == 'BOP' || objectType == 'MANUFACTURER' || objectType == 'MANUFACTURERPART' || objectType == 'SUPPLIER'  
                || objectType == 'ASSET' || objectType == 'METER'  || objectType == 'SPAREPART'  || objectType == 'MAINTENANCEPLAN' || objectType == 'WORKORDER'  || objectType == 'WORKREQUEST' || objectType == 'PPAP' || objectType == 'SUPPLIERAUDIT' || objectType == 'QCR' || objectType == 'NCR' 
                || objectType == 'PROBLEMREPORT' || objectType == 'INSPECTION' || objectType == 'INSPECTIONPLAN' || objectType == 'ECR' || objectType == 'ECO' || objectType == 'DCR' || objectType == 'DCO' || objectType == 'MCO' || objectType == 'VARIANCE' || objectType == 'ITEM' || objectType == 'PLMNPR'">
                    <label class="control-label">
                        <span translate>ACTION</span> </label>

                    <div class="">
                        <ui-select dropup ng-model="selectedAction" theme="bootstrap"
                                   on-select="selectAction($item)"
                                   on-remove="selectAction()">
                            <ui-select-match allow-clear="true" placeholder="Select Action">
                                {{$select.selected.name}}
                            </ui-select-match>
                            <ui-select-choices position="up" repeat="action in objectTypeActions | orderBy:'name'">
                                <div ng-bind-html="action.name"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>