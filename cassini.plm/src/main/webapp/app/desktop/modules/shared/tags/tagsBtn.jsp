<style>
    .tag-notification {
        text-decoration: none;
        padding: 15px 5px;
        position: relative;
        display: inline-block;
        border-radius: 2px;
    }

    .tag-notification .badge1 {
        position: absolute;
        top: 5px;
        left: 15px;
        border-radius: 50%;
        background-color: #337ab7;
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
<span class='tag-notification' title="{{'SHOW_TAG' | translate}}" ng-click='showTags()'>
    <span class="badge1 badge badge-primary" ng-if="tagsCount > 0"
          ng-class="{'padding1':tagsCount < 10,'padding2':tagsCount >= 10}">{{tagsCount}}</span>
    <i class="fa fa-tags" style="font-size: 18px;padding: 0 3px;cursor: pointer;border: none"></i>
</span>
