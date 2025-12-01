<%--
  Created by IntelliJ IDEA.
  User: Nageshreddy
  Date: 14-11-2018
  Time: 12:25
  To change this template use File | Settings | File Templates.
--%>
<style>
    .color1 {
        font-size: 16px;
        font-weight: bold;
        color: black !important;
    }

    .color2 {
        font-size: 16px;
        font-weight: bolder;
        width: 70px !important;
        color: orange !important;
    }

    .color3 {
        font-size: 16px;
        font-weight: bolder;
        width: 60px !important;
        color: blue !important;
    }

    .color4 {
        font-size: 16px;
        font-weight: bolder;
        width: 50px !important;
        color: green !important;
    }
</style>
<div class="styled-panel permissions-table" ng-show="ulsVm.showGrid">
    <table>
        <thead>
        <tr>
            <th class="skew">
                <div class="odd" style="width: auto;"><span>Groups</span></div>
            </th>
            <th class="skew">
                <div class="" style="width: auto;"><span>Locations</span></div>
            </th>
            <th class="skew" ng-repeat="utility in ulsVm.utilities">
                <div ng-class="{'odd': $index %2 != 1,'color1':group.level == 0,'color2':group.level == 1,
                                'color3':group.level == 2,'color4':group.level == 3}">
                    <span>{{utility}}</span>
                </div>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat-start="pg in ulsVm.groups" style="border-top: 5px solid #D3D3D3;"
            ng-init="location = pg.locations[0]">
            <td nowrap class="group odd"
                rowspan="{{pg.locations.length}}"
                style="white-space: nowrap; padding: 10px; text-align: left; font-weight: bold;"
                class="first"
                ng-class="{'first': ulsVm.first, 'last': ulsVm.last, 'indent': (pg.group.indexOf('.') != -1)}"
                ng-init="ulsVm.first = true; ulsVm.last = $last">
                <span>{{pg.name}}
                </span>
            </td>
            <td nowrap ng-class="{'first': ulsVm.first}" class="permission-name">{{location.name}}</td>
            <td align="center"
                ng-class="{'odd': $index %2 != 1,
                                'first': ulsVm.first}"
                ng-repeat="utility in ulsVm.utilityObjects">
                <input type="checkbox"
                       style="text-align:center"
                       89 ng-init="permIndex = ulsVm.getLocationIndexInGroup(location, utility);"
                       ng-model="location.utilities[permIndex].selected"
                       ng-click="ulsVm.toggleLocationUtilities(location, location.utilities[permIndex])"/>
            </td>
        </tr>

        <tr ng-repeat-end ng-repeat="location in pg.locations.slice(1)">
            <td nowrap class="permission-name"
                ng-init="ulsVm.first = false" ng-class="{'last': (ulsVm.last && $last)}">
                <span style="">{{location.name}}</span>
            </td>
            <td align="center"
                ng-class="{'odd': $index %2 != 1,
                                   'first': ulsVm.first, 'last':  (ulsVm.last && $parent.$last)}"
                ng-repeat="utility in ulsVm.utilityObjects">
                <input type="checkbox"
                       style="text-align:center"
                       ng-init="permIndex = ulsVm.getLocationIndexInGroup(location, utility);"
                       ng-model="location.utilities[permIndex].selected"
                       ng-click="ulsVm.toggleLocationUtilities(location, location.utilities[permIndex])"/>
            </td>
        </tr>
        </tbody>
    </table>
</div>