<div>
    <h4>Comments</h4>
    <hr>
    <div>
        <button class="btn btn-xs btn-primary" ng-click="addComment()">Add Comment</button>
        <br><br ng-if="commentError == false">
        <small style="color: red" ng-if="commentError == true">{{commentErrorMessage}}</small>
        <textarea class="form-control" name="comment" rows="5"
                  style="resize: none; width: 100%"
                  ng-model="newComment.comment"></textarea>
    </div>
    <br>
    <div>
        <div class="media-list" style="max-height: 500px; overflow-y: auto;">
            <comment comment-id="c.id" ng-repeat="c in comments"></comment>
        </div>
    </div>
</div>