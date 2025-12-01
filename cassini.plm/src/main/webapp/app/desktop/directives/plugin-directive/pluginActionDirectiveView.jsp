<div class="btn-group">
    <div class="btn-group" ng-repeat="group in customActionGroups">
        <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                aria-haspopup="true" aria-expanded="false" title="{{group.tooltip}}">
            <span class="fa {{group.icon}}" style=""></span>
            <span class="caret" style="margin-left: 3px;margin-top: -3px;"></span>
        </button>
        <ul class="dropdown-menu" style="left: auto;right: 0;">
            <li ng-repeat="action in group.actions"
                ng-click="performCustomAction(action)"
                title="{{action.tooltip}}">
                <a href="">{{action.label}}</a>
            </li>
        </ul>
    </div>

    <button class="btn btn-sm btn-default"
            title="{{action.tooltip}}"
            ng-click="performCustomAction(action)"
            ng-repeat="action in customActions">
        <i class="fa {{action.icon}}" style=""></i>
    </button>

    <div class="btn-group" ng-repeat="tabGroup in tabCustomActionGroups">
        <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                aria-haspopup="true" aria-expanded="false" title="{{tabGroup.tooltip}}">
            <span class="fa {{tabGroup.icon}}" style=""></span>
            <span class="caret" style="margin-left: 3px;margin-top: -3px;"></span>
        </button>
        <ul class="dropdown-menu" style="left: auto;right: 0;">
            <li ng-repeat="tabAction in tabGroup.actions"
                ng-click="performCustomAction(tabAction)"
                title="{{tabAction.tooltip}}">
                <a href="">{{tabAction.label}}</a>
            </li>
        </ul>
    </div>

    <button class="btn btn-sm btn-default"
            title="{{tabAction.tooltip}}"
            ng-click="performCustomAction(tabAction)"
            ng-repeat="tabAction in tabCustomActions">
        <i class="fa {{tabAction.icon}}" style=""></i>
    </button>
</div>

