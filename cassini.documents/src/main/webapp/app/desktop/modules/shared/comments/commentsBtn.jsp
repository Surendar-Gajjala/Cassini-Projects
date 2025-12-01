<div>
    <style>
        .comments-count {
            margin-left: -20px;
            margin-top: 0;
            position: absolute;
            height: 16px;
            width: 16px;
            border-radius: 50%;
            background-color: #d9534f;
            color: #fff;
            font-size: 10px;
            line-height: 15px;
            border: 1px solid #d43f3a;
            vertical-align: middle;
            text-align: center;
            cursor: pointer;
        }
    </style>
    <i ng-click="showComments()"
       class="fa fa-comments-o"
       style="border: 0; cursor: pointer"></i>
    <span class="comments-count" ng-if="commentsCount > 0" ng-click="showComments()">{{commentsCount}}</span>
</div>