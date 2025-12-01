<div class="comment media" style="margin-left: 50px;">
    <div class="comment-body">
        <span class="pull-left"><i class="fa fa-user" style="font-size: 44px; color: lightgrey"></i></span>
        <div>
            <button class="btn btn-xs btn-primary pull-right" ng-click="showReply = true;replyText = ''">Reply</button>
            <div style="margin-left: 48px;">
                <div class="text-primary" style="font-size: 16px">{{comment.commentedBy.firstName}}</div>
                <div class="text-muted" style="font-size: 12px">{{comment.commentedDate}}</div>
                <div style="font-size: 15px" ng-bind-html="comment.comment"></div>
                <div ng-show="showReply" style="margin-top: 10px;padding: 5px;background-color: #ddd;">
                    <small style="color: red" ng-if="commentError">{{commentErrorMessage}}</small>
                    <div>
                        <div class="pull-right">
                            <button class="btn btn-xs btn-primary" ng-click="createReply()">Submit</button>
                            <br><br>
                            <button class="btn btn-xs btn-default" ng-click="showReply = false">Cancel</button>
                        </div>
                        <div style="padding-right: 65px;">
                            <textarea class="form-control" id="" rows="1" ng-model="replyText" style="resize: none;"></textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
