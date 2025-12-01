<div style="height: 8px;">
    <h5 style="padding: 8px 0;margin: 0;">
        <span style="padding-right: 5px" translate>DISPLAYING</span>
        <span ng-if="objects.totalElements ==0">
            {{(pageable.page*pageable.size)}}
        </span>
        <span ng-if="objects.totalElements > 0">
            {{(pageable.page*pageable.size)+1}}
        </span>
        -
        <span ng-if="objects.last == false">{{((pageable.page+1)*pageable.size)}}</span>
        <span ng-if="objects.last == true">{{objects.totalElements}}</span>
        <span translate>OF</span> {{objects.totalElements}}<span translate>AN</span>
        <span class="search-duration ng-binding ng-scope" ng-if="searchResponse != null && searchResponse !== undefined">({{searchResponse}})</span>
    </h5>
</div>
<div class="text-right" style="margin-top: -3px">
    <div class="dropup" uib-dropdown
         style="display: inline-block">
        <button uib-dropdown-toggle class="btn btn-sm btn-default dropdown-toggle"
                aria-haspopup="true" aria-expanded="false"
                style="background-color: #fff;padding: 3px 5px;">
            {{pageable.size}}<i class="caret" style="margin-left: 5px"></i>
        </button>
        <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
            style="">
            <li ng-repeat="page in pageNumbers"
                ng-click="setObjectPageSize(page)"><a href="">{{page}}</a></li>
        </ul>
    </div>
    <span class="mr10"><span translate>PAGE</span> {{objects.totalElements != 0 ? objects.number+1:0}} <span
            translate>OF</span> {{objects.totalPages}}</span>
    <a href="" ng-click="previousObjectPage()"
       ng-class="{'disabled': objects.first}"><i
            class="fa fa-arrow-circle-left mr10"></i></a>
    <a href="" ng-click="nextObjectPage()"
       ng-class="{'disabled': objects.last}"><i
            class="fa fa-arrow-circle-right"></i></a>
</div>