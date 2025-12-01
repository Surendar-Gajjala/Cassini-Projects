<div class="view-container" fitcontent style="padding: 20px;background-color: #eef0f3">
    <style scoped>
        .view-content {
            padding: 0;
        }
        .tasks-container {
            width: 400px;
            height: 100%;
            border: 1px solid #ddd;
            background-color: #fff;
            overflow-y: auto;
            float: left;
        }
        .stats-container {
            padding: 10px;
            text-align: center;
        }
        .stats-container .btn-group {
            margin-bottom: 0;
        }
        .locations {
            border-top: 1px solid #ddd;
        }
        .location {
            padding: 10px;
            border-bottom: 1px solid #ddd;
        }
        .location .location-name {
            font-size: 18px;
            color: #626262;
            margin-bottom: 5px;
        }

        .location .status-counts .status-label {
            color: grey;
            font-size: 14px;
        }

        .location .status-counts .status-count {
            font-size: 18px;
            color: dodgerblue;
            cursor: pointer;
        }

        .main-container {
            margin-left: 420px;
            height: 100%;
            border: 1px solid #ddd;
            background-color: #fff;
            overflow-y: auto;
        }

        .main-container > .row {
            margin-left: 0 !important;
            margin-right: 0 !important;
        }

        .image-slider {
            margin: 20px;
        }

        .news-container {
            border: 1px solid #ddd;
            overflow-y: auto;
            height: 500px;
        }

        .news-container .news-item {
            padding: 10px;
            border-bottom: 1px solid #ddd;
        }

        .news-container .news-item:nth-child(odd) {
            background-color: #eee;
        }

        .news-container .news-item:last-child {
            border-bottom: 0;
        }

        .news-container .news-item .news-item-subject {
            font-weight: bold;
        }

        .news-container .news-item .news-item-content {

        }

    </style>

    <div class="view-content">
        <div class="tasks-container">
            <div class="stats-container">
                <div class="btn-group">
                    <button class='btn btn-sm btn-primary'
                            ng-click="homeVm.showTasksByStatus('all')">All ({{homeVm.totalCounts.total}})</button>
                    <button class='btn btn-sm btn-success'
                            ng-click="homeVm.showTasksByStatus('approved')">Completed ({{homeVm.totalCounts.approved}})</button>
                    <button class='btn btn-sm btn-warning'
                            ng-click="homeVm.showTasksByStatus('verified')">Verified ({{homeVm.totalCounts.verified}})</button>
                    <button class='btn btn-sm btn-danger'
                            ng-click="homeVm.showTasksByStatus('assigned')">Pending ({{homeVm.totalCounts.assigned}})</button>
                </div>
            </div>
            <div class="locations">
                <div class="location" ng-repeat="chunk in homeVm.chunks">
                    <div class="location-name">
                        {{chunk[0].name}}
                    </div>
                    <div class="row status-counts">
                        <div class="col-sm-4">
                            <span class="status-label">Pending</span><br>
                            <span class="status-count"
                                  ng-click="homeVm.showTasks(chunk[0].name, ['ASSIGNED'], 'Pending')">
                                {{chunk[0].counts.assigned}}
                            </span>
                        </div>
                        <div class="col-sm-4 text-center">
                            <span class="status-label">Verified</span><br>
                            <span class="status-count"
                                  ng-click="homeVm.showTasks(chunk[0].name, ['VERIFIED'], 'Verified')">
                                {{chunk[0].counts.verified}}
                            </span>
                        </div>
                        <div class="col-sm-4 text-right">
                            <span class="status-label">Completed</span><br>
                            <span class="status-count"
                                  ng-click="homeVm.showTasks(chunk[0].name, ['APPROVED'], 'Verified')">
                                {{chunk[0].counts.approved}}
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="main-container">
            <div class="row image-slider row-eq-height">
                <div class="col-sm-8">
                    <div id="myslider" style="height: 500px;">
                        <img height="100%" width="100%"
                             ng-repeat="image in homeVm.images"
                             ng-src="{{image}}" alt="">
                    </div>
                </div>
                <div class="col-sm-4">
                    <div class="news-container">
                        <div class="news-item" ng-repeat="news in homeVm.news">
                            <div class="news-item-subject">
                                {{news.subject}}
                            </div>
                            <div class="news-item-content">
                                {{news.content}}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-4">
                    <div>
                        <iframe width="100%" height="300" src="https://www.youtube.com/embed/1F5WRzgE8uI" frameborder="0" allowfullscreen></iframe>
                    </div>
                </div>
                <div class="col-sm-4">
                    <div>
                        <iframe width="100%" height="300" src="https://www.youtube.com/embed/8FtwFE0D8Y8" frameborder="0" allowfullscreen></iframe>
                    </div>
                </div>
                <div class="col-sm-4">
                    <div>
                        <iframe width="100%" height="300" src="https://www.youtube.com/embed/PC7gFMIEtOc" frameborder="0" allowfullscreen></iframe>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-4">
                    <div>
                        <iframe width="100%" height="300" src="https://www.youtube.com/embed/NJdspPgQb9A" frameborder="0" allowfullscreen></iframe>
                    </div>
                </div>
                <div class="col-sm-4">
                    <div>
                        <iframe width="100%" height="300" src="https://www.youtube.com/embed/XMlHxokh4vE" frameborder="0" allowfullscreen></iframe>
                    </div>
                </div>
                <div class="col-sm-4">
                    <div>
                        <iframe width="100%" height="300" src="https://www.youtube.com/embed/0V0abqTKbkA" frameborder="0" allowfullscreen></iframe>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
