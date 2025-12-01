<div style="position: relative;">
    <style scoped>
        .comments-panel {
            padding: 10px;
            background-color: #eee;
            border-top: 1px solid #ddd;
            border-bottom: 1px solid #ddd;
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

    </style>

    <div style="padding: 20px;font-style: italic" ng-if="commentsVm.comments.length == 0">
        <span translate>NO_COMMENTS</span>
    </div>
    <div class="comments-panel" ng-if="commentsVm.comments.length > 0">
        <div class="comment-container" ng-repeat="comment in commentsVm.comments">
            <div class="comment-user"><h4>{{comment.commentedBy.fullName}}</h4></div>
            <div class="comment-timestamp">
                <small class="text-muted">{{comment.commentedDate}}</small>
            </div>
            <div class="comment-text">{{comment.comment}}</div>
        </div>
    </div>
    <div class="comment-input">
        <div class="input-group">
            <textarea class="form-control" rows="1" style="resize: none"
                      ng-model="commentsVm.newComment.comment" style="height: 42px;"></textarea>
            <span class="input-group-btn">
                <button type="button" class="btn btn-lightblue"
                        ng-click="commentsVm.createComment()"><span translate>SEND</span>
                </button>
            </span>
        </div>
    </div>
</div>
