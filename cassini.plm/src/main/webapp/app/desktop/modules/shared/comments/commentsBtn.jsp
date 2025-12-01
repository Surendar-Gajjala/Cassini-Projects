<style>
    .comment-notification {
        text-decoration: none;
        padding: 15px 0px;
        position: relative;
        display: inline-block;
        border-radius: 2px;
    }

    .comment-notification .badge1 {
        position: absolute;
        top: 10px;
        left: 12px;
        border-radius: 50%;
        background-color: #f03e5f;
        color: white;
        font-size: 10px;
        cursor: pointer;
    }

    .fa-comments-o {
        border: none !important;
        font-size: 24px !important;
    }

    .padding1 {
        padding: 4px 6px !important;
    }

    .padding2 {
        padding: 4px 5px !important;
    }
</style>
<span class='comment-notification' title="{{'SHOW_COMMENT' | translate}}" ng-click='showComments()'>
    <span class="badge1 badge badge-primary" ng-if="commentCount > 0"
          ng-class="{'padding1':commentCount < 10,'padding2':commentCount >= 10}">{{commentCount}}</span>
    <i class="fa fa-comments-o" style="font-size: 18px;padding: 0 3px;cursor: pointer;"></i>
</span>
<%--
<div>
    <i ng-click="showComments()" title="{{addCommentTitle}}"
       class="fa fa-comments-o"
       style="border: 0; cursor: pointer"></i>
</div>--%>
