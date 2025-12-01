<div>
    <style scoped>

        .view-details {

        }

        .view-details .view-header {
            padding: 20px;
            border-bottom: 1px solid #eee;
        }

        .view-details .view-header .view-title {
            font-size: 16px;
            font-weight: 600;
        }

        .view-details .view-header .view-summary {
            color: #707d91;
            font-weight: 300;
        }

        .view-details .view-content {
            width: 100%;
        }

        ul.timeline {
            width: 100%;
            height: 100%;
            margin-left: 0px !important;
            margin-top: 0;
            overflow-y: auto;
            position: unset;
        }

        ul.timeline li {
            padding: 0 0 1em 0;
        }

        ul.timeline:before {
            left: 30px;
            width: 2px;
            background: #ddd;
            top: 82px;
            bottom: 0;
            height: unset;
        }

        ul.timeline .direction-r {
            padding-left: 54px !important;
            width: 100%;
        }

        ul.timeline .direction-r .timestamp {
            position: relative;
            display: inline;
        }

        ul.timeline .direction-r .desc {
            margin: 1em 0.75em 0 0;
            font-size: unset;
            font-style: unset;
        }

        ul.timeline .direction-r .activity-details {
            padding-right: 30px;
        }

        ul.timeline .direction-r .timestamp {
            color: #707d91;
        }

        ul.timeline .direction-r .timestamp:before {
            position: absolute;
            right: -40px;
            content: ' ';
            display: block;
            margin-top: -10px;
            background: #3699ff;
            border-radius: 10px;
            border: 2px solid #3699ff;
            z-index: 10;
            width: 10px;
            height: 10px;
            left: -31px;
            top: 14px;
        }

        .no-data .image {
            display: block;
            margin-left: auto;
            margin-right: auto;
            margin-top: 5%;
            width: 200px;
        }

        .no-data .message {
            font-size: 20px;
            font-weight: 300 !important;
            text-align: center;
        }

    </style>

    <div class="view-details">
        <div class="view-header">
            <div class="view-title" translate>USER_ACTIVITY</div>
            <div class="view-summary" translate>SHOW_ALL_USERS_ACTIVITY</div>
        </div>
        <div class="view-content">

         <div  ng-if="userActivityVm.activities.content.length == 0"  style="background-color: white  !important;color: unset !important;">
            <div class="no-data">
                <img src="app/assets/no_data_images/noUserActivity.png" alt="" class="image">

                <div class="message" translate>USER_HAS_NO_ACTIVITY</div>
                <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
            </div>

         </div>
            <ul class="timeline" ng-if="userActivityVm.activities.content.length > 0">
                <li ng-repeat="activity in userActivityVm.activities.content">
                    <div class="direction-r">
                        <div class="desc">
                            <span class="timestamp">{{activity.timestamp}}</span>

                            <div class="activity-details">
                                <span style="line-height: 20px;" ng-bind-html="activity.summary"></span>
                            </div>
                        </div>
                    </div>
                </li>
                <li style="text-align: center;margin-bottom: 20px;"
                    ng-if="userActivityVm.activities.content.length > 0 && !userActivityVm.pagenatedResults.last">
                    <a href="" ng-click="userActivityVm.loadMoreUserActivity()"><h5>&#x2193;Load more</h5></a>
                </li>
            </ul>
        </div>
    </div>

</div>