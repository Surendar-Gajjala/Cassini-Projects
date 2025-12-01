<div>
    <style scoped>
        #rightSidePanelContent {
            overflow-y: hidden !important;
        }

        .tab-content {
            padding: 0 !important;
        }

        .tab-content .tab-pane {
            overflow-y: auto !important;
        }

        .tab-pane {
            position: relative;
        }

        .preferences-view .nav-tabs li {
            cursor: pointer;
            border: 0 !important;
            min-width: 150px;
            text-align: center
        }

        .preferences-view .nav-tabs li:hover {
            background-color: inherit !important;
        }

        .popover {
            margin: -27px 0 0 10px !important;
        }

        .arrow {
            margin: 14px 0 0 0 !important;
        }

        .log-timeline {
            position: relative;
            width: 100%;
            margin: 0 auto;
            /*margin-top: 20px;*/
            padding: 1em 0;
            list-style-type: none;
            /*border-right: 1px solid lightgrey;*/
        }

        .log-timeline-container {
            width: 100%;
        }

        .log-timeline-container .log-timeline-panel {
            width: 100%;
            padding-top: 81px;;
        }

        .log-timeline-container .log-filter-panel {
            border-left: 1px solid #ddd !important;
            display: flex;
            position: fixed;
            z-index: 9998;
            background: white;
            padding-bottom: 10px;
            border-bottom: 1px solid lightgray;
        }

        .log-timeline-container .form-horizontal .form-group {
            width: 100%;
        }

        .session-timeline-container .session-timeline-panel {
            width: 100%;
            padding-top: 81px;;
        }

        .session-timeline-container .session-filter-panel {
            border-left: 1px solid #ddd !important;
            display: flex;
            position: fixed;
            z-index: 9998;
            background: white;
            padding-bottom: 10px;
            border-bottom: 1px solid lightgray;
        }

        .session-timeline-container .form-horizontal .form-group {
            width: 100%;
        }

        .log-timeline:before {
            position: absolute;
            left: 150px;
            top: 0;
            content: ' ';
            display: block;
            width: 1px;
            height: 100%;
            margin-left: -3px;
            background: #ddd;
            z-index: 5;
        }

        .log-timeline li {
            padding: .5em 0;
        }

        .log-timeline li:after {
            content: "";
            display: block;
            height: 0;
            clear: both;
            visibility: hidden;
        }

        .log-direction-l {
            position: relative;
            width: 130px;
            float: left;
            text-align: right;
        }

        .log-direction-r {
            position: relative;
            width: calc(100% - 170px);
            float: right;
        }

        .log-flag-wrapper {
            position: relative;
            display: inline-block;
            text-align: center;
        }

        .log-direction-l .log-flag {
            -webkit-box-shadow: -1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            -moz-box-shadow: -1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            box-shadow: -1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
        }

        .log-direction-r .log-flag {
            -webkit-box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            -moz-box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
        }

        .log-direction-l .log-date-flag {
            -webkit-box-shadow: -1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            -moz-box-shadow: -1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            box-shadow: -1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
        }

        .log-direction-r .log-date-flag {
            -webkit-box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            -moz-box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
        }

        .log-direction-r .session-flag {
            -webkit-box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            -moz-box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
        }

        .log-direction-l .log-flag:before,
        .log-direction-r .log-flag:before {
            position: absolute;
            top: 12px;
            right: -40px;
            content: ' ';
            display: block;
            width: 16px;
            height: 16px;
            margin-top: -7px;
            background-color: #36b2ef;
            border-radius: 50%;
            z-index: 10;
            border: 2px solid #fff;
        }

        .log-direction-l .log-date-flag:before,
        .log-direction-r .log-date-flag:before {
            position: absolute;
            top: 12px;
            right: -40px;
            content: ' ';
            display: block;
            width: 16px;
            height: 16px;
            border-radius: 50%;
            margin-top: -8px;
            background: #f32f2f;
            border: 2px solid #fff;
            z-index: 15;
        }

        .log-direction-l .session-flag:before,
        .log-direction-r .session-flag:before {
            position: absolute;
            top: 12px;
            right: -40px;
            content: ' ';
            display: block;
            width: 16px;
            height: 16px;
            margin-top: -8px;
            background: #2E9D44;
            border-radius: 50%;
            z-index: 15;
            border: 2px solid #fff;
        }

        .log-direction-r .log-flag:before {
            left: -30px;
        }

        .log-direction-r .log-date-flag:before {
            left: -30px;
        }

        .log-direction-r .session-flag:before {
            left: -30px;
        }

        .log-time-wrapper {
            display: inline;
            line-height: 1em;
            font-size: 0.66666em;
            /*color: #2a6fa8;*/
            vertical-align: middle;
            margin-top: 4px;
        }

        .log-direction-l .log-time-wrapper {
            float: left;
        }

        .log-direction-r .log-time-wrapper {
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

        .log-direction-r .desc {
            margin: 1em 0 0 0.75em;
        }

        .log-flag i {
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

        .log-type-login .log-direction-r .log-flag:before {
            border-color: red;
        }

        .log-type-item .log-direction-r .log-flag:before {
            border-color: #123dff;
        }

        .log-type-itemtype .log-direction-r .log-flag:before {
            border-color: #ffbf00;
        }

        .log-type-change .log-direction-r .log-flag:before {
            border-color: #F4A460;
        }

        .log-type-project .log-direction-r .log-flag:before {
            border-color: #3CB371;
        }

        .log-type-projecttask .log-direction-r .log-flag:before {
            border-color: #8B008B;
        }

        .log-type-projectactivity .log-direction-r .log-flag:before {
            border-color: #D2691E;
        }

        .log-type-projectmilestone .log-direction-r .log-flag:before {
            border-color: #FF1493;
        }

        .log-type-specification .log-direction-r .log-flag:before {
            border-color: #bf00ff;
        }

        .log-type-requirement .log-direction-r .log-flag:before {
            border-color: #BDB76B;
        }

        .log-type-specrequirement .log-direction-r .log-flag:before {
            border-color: #40E0D0;
        }

        .log-type-workflowtype .log-direction-r .log-flag:before {
            border-color: #F0E68C;
        }

        .log-type-workflow .log-direction-r .log-flag:before {
            border-color: #9ACD32;
        }

        .ui-select-container .ui-select-match .btn {
            /*height: 33px !important;*/
        }

        .ui-select-container .ui-select-match .btn .btn {
            /*height: 20px !important;*/
        }

        .ui-select-container .ui-select-match .btn .btn .glyphicon-remove {
            font-size: 10px !important;
            top: 0;
            color: #636e7b !important;
        }

        .selected-session {

        }

        .selected-session:before {

        }

        .preferences-view .nav-tabs {
            border: 0;
            border-bottom: 1px solid #eee;
        }
        .preferences-view .tab-content {
            border: 0;
        }

    </style>
    <div class="row row-eq-height" style="margin: 0;">
        <div class="col-sm-12" style="padding: 0;">
            <div class="preferences-view">
                <uib-tabset active="logHistoryVm.active">
                    <uib-tab heading="Activity Stream" active="logHistoryVm.tabs.log.active"
                             select="logHistoryVm.selectLogTab(logHistoryVm.tabs.log.id)">
                        <div ng-include="logHistoryVm.tabs.log.template"></div>
                    </uib-tab>

                    <uib-tab heading="Sessions" ng-show="hasPermission('sessions','all')"
                             active="logHistoryVm.tabs.session.active"
                             select="logHistoryVm.selectLogTab(logHistoryVm.tabs.session.id)">
                        <div ng-include="logHistoryVm.tabs.session.template"></div>
                    </uib-tab>
                </uib-tabset>
            </div>
        </div>
    </div>
</div>