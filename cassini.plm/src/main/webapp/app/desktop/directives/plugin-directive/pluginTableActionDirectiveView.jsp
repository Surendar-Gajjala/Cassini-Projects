<li ng-repeat="action in customTableActions track by $index">
    <a ng-click="performCustomTableAction(action)" title="{{action.tooltip}}" href="">{{action.label}}</a>
</li>

