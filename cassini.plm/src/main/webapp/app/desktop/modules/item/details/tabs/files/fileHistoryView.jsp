<div style="padding: 10px">
    <style scoped>
        .timeline {
            width: 250px;
        }

        .timeline::before {
            left: -80px;
        }

        .timeline .flag {
            cursor: pointer;
        }

        .timeline .time-wrapper {
            margin-top: -20px;
            margin-left: 95px;
            width: 330px;
        }

        .timeline .time {
            font-size: 12px;
        }

        .comments-panel {
            padding: 10px;
            /*background-color: #eee;*/
            /*border-top: 1px solid #ddd;*/
            /*border-bottom: 1px solid #ddd;*/
            margin-bottom: 65px;
        }

        .comment-container {
            padding: 10px;
            border: 1px solid #ddd;
            background-color: #fff;
            margin-bottom: 10px;
            border-radius: 5px;
        }

        .comment-container:last-child {
            margin-bottom: 0;
        }

        .comment-user {

        }

        .comment-user h4 {
            margin: 0;
        }

        .comment-timestamp {

        }

        .comment-text {
            margin-top: 5px;
        }

        .comment-input {
            position: fixed;
            bottom: 30px;
            height: 65px;
            width: 500px;
            padding: 10px;
            background-color: #eee;
            border-top: 1px solid #ddd;
        }

        .input-group-btn > .btn {
            background: #028af3 !important;
            border-color: #0385ea !important;
            color: #fff !important;
        }

        .reviewers-container {
            display: flex;
            flex-direction: column;
            margin-top: 30px;
            margin-left: -50px;
            width: 390px;
        }

        .reviewers-container .reviewer-card {
            min-height: 75px;
            background-color: #fff;
            box-shadow: 0 0 30px 0 rgba(82, 63, 105, .05);
            border-radius: 5px;
            padding: 10px 10px 10px 20px;
            margin-bottom: 10px;
        }

        .reviewers-container .reviewer-card .reviewer-avatar {
            padding-top: 2px;
        }

        .reviewers-container .reviewer-card .reviewer-avatar .user-image,
        .reviewers-container .reviewer-card .reviewer-avatar .user-image img {
            width: 50px;
            height: 50px;
        }

        .reviewers-container .reviewer-card .reviewer-desc {
            flex: 1;
            padding-top: 5px;
        }

        .reviewers-container .reviewer-card .reviewer-desc .reviewer-name {
            font-size: 15px;
        }

        .reviewers-container .reviewer-card .reviewer-desc .reviewer-role {
            font-weight: 300;
        }

        .reviewers-container .reviewer-card .reviewer-approval {
            width: 100px;
            padding: 0 10px;
            text-align: center;
            padding-top: 2px;
        }

        .reviewers-container .reviewer-card .reviewer-approval input[switch] + label {
            margin-top: 0;
        }

        .reviewers-container .reviewer-card .reviewer-approval .remove-approver {
            float: right;
            margin-top: -67px;
            margin-right: -20px;
            display: none;
        }

        .reviewers-container .reviewer-card:hover .reviewer-approval .remove-approver {
            display: block;
            height: 20px;
            width: 20px;
            line-height: 18px;
            background-color: #ddd;
        }

        .reviewers-container .reviewer-card .reviewer-approval .remove-approver i {
            font-size: 12px;
            opacity: 0.6;
            cursor: pointer;
        }

        .reviewers-container .reviewer-card .reviewer-approval .remove-approver i:hover {
            opacity: 1;
        }
    </style>


    <div ng-if='fileHistoryVm.fileMode == "FileVersion"'>
        <h4>{{fileHistoryVm.fileName}}</h4>

        <p class="text-muted" ng-if="hasPermission('file', 'download')" style="font-style: italic">
            ({{clickOnVersionDownload}})</p>

        <div class="comments-panel">
            <ul class="timeline">
                <li ng-repeat="fileVersion in fileHistoryVm.fileVersions">
                    <div class="direction-r">
                        <div class="flag-wrapper" style="text-align: left !important;">
                            <span class="flag" style="" title="{{noPermission | translate}}"
                                  ng-if="!hasPermission('file', 'download')">
                                Version  <span
                                    ng-if="fileVersion.objectType == 'DOCUMENT' || fileVersion.objectType == 'MFRPARTINSPECTIONREPORT' || fileVersion.objectType == 'PPAPCHECKLIST'">
                                {{fileVersion.revision}}.{{fileVersion.version}}</span>
                                <span ng-if="fileVersion.objectType != 'DOCUMENT' && fileVersion.objectType != 'MFRPARTINSPECTIONREPORT' && fileVersion.objectType != 'PPAPCHECKLIST'">{{fileVersion.version}}</span>
                            </span>
                            <span class="flag" style="" ng-if="hasPermission('file', 'download')"
                                  ng-click="fileHistoryVm.downloadFile(fileVersion)">
                                Version <span
                                    ng-if="fileVersion.objectType == 'DOCUMENT' || fileVersion.objectType == 'MFRPARTINSPECTIONREPORT' || fileVersion.objectType == 'PPAPCHECKLIST'">
                                {{fileVersion.revision}}.{{fileVersion.version}}</span>
                                <span ng-if="fileVersion.objectType != 'DOCUMENT' && fileVersion.objectType != 'MFRPARTINSPECTIONREPORT' && fileVersion.objectType != 'PPAPCHECKLIST'">{{fileVersion.version}}</span>
                            </span>
                        <span class="time-wrapper">
                            <span class="time" style="font-size: 14px;">
                             {{fileVersion.createdDate}} ( <span style="font-style: italic;">{{fileVersion.createdByName}}</span> )
                            </span>
                            <span>
                            <i class="fa" ng-click="fileHistoryVm.showComment(fileVersion)"
                               ng-if="fileVersion.comments.length > 0"
                               title="{{(fileVersion.showComments ? 'HIDE_COMMENT' : 'SHOW_COMMENT') | translate}}"
                               ng-class="{'fa-comments-o':fileVersion.showComments == false,'fa-comments':fileVersion.showComments == true}"
                               style="cursor: pointer;font-size: 16px;"></i>
                        </span>
                        </span>
                        </div>
                    </div>
                    <br>
                    <br>

                    <div style="color: #2a6fa8">
                        <h6 style="width: 350px;margin-left: -50px;;font-size: 14px;">
                            FileName : <span>{{fileVersion.name}}</span></h6>
                    </div>
                    <div>
                        <h6 style="width: 350px;margin-left: -50px;;font-size: 12px;"><span>{{fileVersion.replaceFileName}}</span>
                        </h6>
                    </div>
                    <br>

                    <div ng-if="fileVersion.showComments == true" class="comment-container"
                         style="width: 350px;margin-left: -50px;"
                         ng-repeat="comment in fileVersion.comments">
                        <div class="comment-user"><h4>{{comment.commentedBy.fullName}}</h4></div>
                        <div class="comment-timestamp">
                            <small class="text-muted">{{comment.commentedDate}}</small>
                        </div>
                        <div class="comment-text">{{comment.comment}}</div>
                    </div>
                    <h6 ng-if="fileVersion.comments.length == 0" style="width: 300px;margin: 0;">
                        {{fileVersionHasNoComments}}</h6>

                    <div class="reviewers-container" ng-if="fileVersion.reviewers.length > 0">
                        <span>Sign Off</span>

                        <div class="reviewer-card" ng-repeat="reviewer in fileVersion.reviewers">
                            <div style="display: flex;">
                                <div class="reviewer-avatar">
                                    <person-avatar person-id="reviewer.reviewer" display="'normal'"></person-avatar>
                                </div>
                                <div class="reviewer-desc">
                                    <div class="reviewer-name">{{reviewer.reviewerName}}</div>
                                    <div class="reviewer-role">
                                        <span ng-if="reviewer.approver">Approver</span>
                                        <span ng-if="!reviewer.approver">Reviewer</span>
                                    </div>
                                </div>
                                <div class="reviewer-approval">
                                    <div class="text-light">Status</div>
                                    <div>
                                        <object-status object="reviewer"></object-status>
                                    </div>
                                </div>
                            </div>
                            <div style="display: flex;" ng-if="reviewer.status != 'NONE'">
                                <div class="reviewer-desc">
                                    <div class="reviewer-name">Notes</div>
                                    <div class="reviewer-role">{{reviewer.notes}}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
        <div class="comment-input"
             style="margin-left: -10px;padding: 5px 10px 10px !important;height: 50px !important;z-index: 10;">
            <div class="input-group">
                <textarea class="form-control" rows="1" style="resize: none"
                          placeholder="{{fileHistoryVm.enterComment}}"
                          ng-model="fileHistoryVm.newComment.comment" style="height: 42px;"></textarea>
            <span class="input-group-btn">
                <button type="button" class="btn btn-lightblue"
                        ng-click="fileHistoryVm.createComment()"><span translate>SEND</span>
                </button>
            </span>
            </div>
        </div>
    </div>

    <div ng-if='fileHistoryVm.fileMode == "FileDownloadHistory"'>
        <h4>{{fileHistoryVm.fileName}}</h4>

        <p class="text-muted" ng-if="hasPermission('file', 'download')" style="font-style: italic">
            ({{clickOnVersionDownload}})</p>

        <div class="comments-panel">
            <ul class="timeline">
                <li ng-repeat="fileVersion in fileHistoryVm.fileVersions">
                    <div class="direction-r">
                        <div class="flag-wrapper" style="text-align: left !important;">
                            <span class="flag" style="" title="{{noPermission | translate}}"
                                  ng-if="!hasPermission('file', 'download')">
                                Version <span
                                    ng-if="fileVersion.objectType == 'DOCUMENT' || fileVersion.objectType == 'MFRPARTINSPECTIONREPORT' || fileVersion.objectType == 'PPAPCHECKLIST'">{{fileVersion.revision}}.{{fileVersion.version}}</span>
                                <span ng-if="fileVersion.objectType != 'DOCUMENT' && fileVersion.objectType != 'MFRPARTINSPECTIONREPORT' && fileVersion.objectType != 'PPAPCHECKLIST'">{{fileVersion.version}}</span>
                            </span>
                            <span class="flag" style="" ng-if="hasPermission('file', 'download')"
                                  ng-click="fileHistoryVm.downloadFile(fileVersion)">Version <span
                                    ng-if="fileVersion.objectType == 'DOCUMENT' || fileVersion.objectType == 'MFRPARTINSPECTIONREPORT' || fileVersion.objectType == 'PPAPCHECKLIST'">{{fileVersion.revision}}.{{fileVersion.version}}</span>
                                <span ng-if="fileVersion.objectType != 'DOCUMENT' && fileVersion.objectType != 'MFRPARTINSPECTIONREPORT' && fileVersion.objectType != 'PPAPCHECKLIST'">{{fileVersion.version}}</span>
                            </span>
                        <span class="time-wrapper">
                            <span class="time" style="font-size: 14px;">
                             {{fileVersion.createdDate}} ( <span style="font-style: italic;">{{fileVersion.createdByName}}</span> )
                            </span>
                            <span>
                            <i class="fa fa-download" ng-if="fileVersion.downloadHistories.length > 0"
                               title="{{(fileVersion.showDownloads ? 'HIDE_DOWNLOADS' : 'SHOW_DOWNLOADS') | translate}}"
                               ng-click="fileHistoryVm.showDownload(fileVersion)"
                               style="cursor: pointer;font-size: 16px;" title="Click to show downloads"></i>
                        </span>
                        </span>
                        </div>
                    </div>
                    <br>
                    <br>

                    <div style="color: #2a6fa8">
                        <h6 style="width: 350px;margin-left: -50px;;font-size: 14px;">
                            FileName : <span>{{fileVersion.name}}</span></h6>
                    </div>
                    <div>
                        <h6 style="width: 350px;margin-left: -50px;;font-size: 12px;"><span>{{fileVersion.replaceFileName}}</span>
                        </h6>
                    </div>
                    <br>

                    <div ng-if="fileVersion.showDownloads == true" class="comment-container"
                         style="width: 350px;margin-left: -50px;"
                         ng-repeat="downloadHistory in fileVersion.downloadHistories">
                        <div class="comment-user"><h4>{{downloadHistory.person.fullName}}</h4></div>
                        <div class="comment-timestamp">
                            <small class="text-muted" style="font-size: 12px;">{{downloadHistory.downloadDate}}</small>
                        </div>
                    </div>
                    <h6 ng-if="fileVersion.downloadHistories.length == 0" style="width: 300px;margin: 0px;">
                        {{FileVersionHasNoDownloads}}
                    </h6>
                </li>
            </ul>
        </div>

        <p ng-if="fileHistoryVm.fileDownloadHistories.length == 0">{{NoDownloadHistory}}</p>
    </div>
</div>