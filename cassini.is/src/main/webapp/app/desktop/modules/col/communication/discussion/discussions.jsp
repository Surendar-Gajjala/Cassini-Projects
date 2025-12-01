<div>
    <%--<h4>Discussion Messages</h4>
    <hr>--%>
    <div>
        <button class="btn btn-xs btn-primary" ng-click="addDiscussion()">Add Discussion</button>
        <br><br ng-if="discussionError == false">
        <small style="color: red" ng-if="discussionError == true">{{discussionErrorMessage}}</small>
        <textarea class="form-control" name="discussion" rows="5"
                  style="resize: none; width: 100%"
                  ng-model="newDiscussionMessage.comment"></textarea>
    </div>
    <br>

    <div>
        <div class="media-list" style="max-height: 500px; overflow-y: auto;">
            <discussion discussion-id="c.id" ng-repeat="c in discussions"></discussion>
        </div>
    </div>
</div>