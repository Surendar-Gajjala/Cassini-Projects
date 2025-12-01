<div style="position: relative;">
    <style scoped>
        .comments-panel {
            padding: 10px;
            background-color: #fff;
            border-top: 1px solid #ddd;
            border-bottom: 1px solid #ddd;
            margin-bottom: 65px;
        }

        .comment-container {
            padding: 0;
            margin-bottom: 10px;
        }

        .comment-container:last-child {
            margin-bottom: 0;
        }

        .comment-info {
            display: flex;
            flex-direction: row;
        }

        .comment-user {
            flex: 1;
        }

        .comment-user h5 {
            margin: 0;
            color: #0078D7;
        }

        .comment-timestamp {
            flex: 1;
            text-align: right;
        }

        .comment-text {
            margin-top: 0;
            white-space: pre-wrap;
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

        .comment-textarea {
            resize: none !important;
            min-height: 41px !important;
            position: absolute !important;
            bottom: 0px !important;
            left: 0px !important;
            margin-right: 41px !important;
            width: calc(100% - 62px) !important;
        }

    </style>


    <div style="padding: 20px;font-style: italic" ng-if="commentsVm.comments.length == 0">
        No comments
    </div>
    <div class="comments-panel" ng-if="commentsVm.comments.length > 0">
        <div class="comment-container" ng-repeat="comment in commentsVm.comments">
            <div class="comment-info">
                <div class="comment-user"><h5>{{comment.commentedBy.fullName}}</h5></div>
                <div class="comment-timestamp">
                    <small class="text-muted">{{comment.commentedDate}}</small>
                </div>
            </div>
            <div class="comment-text">{{comment.comment}}</div>
        </div>
    </div>
    <div class="comment-input">
        <div class="input-group">
            <textarea id="commentTextbox" class="form-control comment-textarea"
                      ng-keyup="commentsVm.onKeyUp();"
                      rows="1"
                      ng-model="commentsVm.newComment.comment"></textarea>
            <span class="input-group-btn">
                <button type="button" class="btn btn-lightblue"
                        ng-click="commentsVm.createComment();">Send
                </button>
            </span>
        </div>
    </div>
</div>

