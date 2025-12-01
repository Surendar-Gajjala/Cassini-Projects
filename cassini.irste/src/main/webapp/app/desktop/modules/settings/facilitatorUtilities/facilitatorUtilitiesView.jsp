<%--
  Created by IntelliJ IDEA.
  User: SRAVAN
  Date: 12/11/2018
  Time: 11:42 AM
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
<div class="styled-panel permissions-table" ng-show="pusVm.showGrid">
  <table>
    <thead>
    <tr>
      <th class="skew">
        <div class="" style="width: auto;"><span>Facilitators</span></div>
      </th>
      <th class="skew" ng-repeat="utility in pusVm.utilities">
        <div ng-class="{'odd': $index %2 != 1,'color1':group.level == 0,'color2':group.level == 1,
                                'color3':group.level == 2,'color4':group.level == 3}">
          <span>{{utility}}</span>
        </div>
      </th>
    </tr>
    </thead>
    <tbody>
    <%--<tr ng-repeat-start="pg in pusVm.groups" style="border-top: 5px solid #D3D3D3;"
        ng-init="responder = pg.responders[0]">
        <td nowrap class="group odd"
            rowspan="{{pg.responders.length}}"
            style="white-space: nowrap; padding: 10px; text-align: left; font-weight: bold;"
            class="first"
            ng-class="{'first': pusVm.first, 'last': pusVm.last, 'indent': (pg.group.indexOf('.') != -1)}"
            ng-init="pusVm.first = true; pusVm.last = $last">
            <span>{{pg.name}}
            </span>
        </td>
        <td nowrap ng-class="{'first': pusVm.first}" class="permission-name">{{responder.name}}</td>
        <td align="center"
            ng-class="{'odd': $index %2 != 1,
                            'first': pusVm.first}"
            ng-repeat="utility in pusVm.utilityObjects">
            <input type="checkbox"
                   style="text-align:center"
                   89 ng-init="permIndex = pusVm.getResponderIndexInGroup(responder, utility);"
                   ng-model="responder.utilities[permIndex].selected"
                   ng-click="pusVm.toggleResponderUtilities(responder, responder.utilities[permIndex])"/>
        </td>
    </tr>--%>

    <%--<tr ng-repeat-end ng-repeat="responder in pg.responders.slice(1)">
        <td nowrap class="permission-name"
            ng-init="pusVm.first = false" ng-class="{'last': (pusVm.last && $last)}">
            <span style="">{{responder.name}}</span>
        </td>
        <td align="center"
            ng-class="{'odd': $index %2 != 1,
                               'first': pusVm.first, 'last':  (pusVm.last && $parent.$last)}"
            ng-repeat="utility in pusVm.utilityObjects">
            <input type="checkbox"
                   style="text-align:center"
                   ng-init="permIndex = pusVm.getResponderIndexInGroup(responder, utility);"
                   ng-model="responder.utilities[permIndex].selected"
                   ng-click="pusVm.toggleResponderUtilities(responder, responder.utilities[permIndex])"/>
        </td>
    </tr>--%>
    <tr ng-repeat="facilitator in pusVm.facilitators">
      <td nowrap class="permission-name"
          ng-init="pusVm.first = false" ng-class="{'last': (pusVm.last && $last)}">
        <span style="">{{facilitator.fullName}}</span>
      </td>
      <td align="center"
          ng-class="{'odd': $index %2 != 1,
                                   'first': pusVm.first, 'last':  (pusVm.last && $parent.$last)}"
          ng-repeat="utility in pusVm.utilityObjects">
        <input type="checkbox"
               style="text-align:center"
               ng-init="permIndex = pusVm.getResponderIndexInGroup(facilitator, utility);"
               ng-model="facilitator.utilities[permIndex].selected"
               ng-click="pusVm.toggleResponderUtilities(facilitator, facilitator.utilities[permIndex])"/>
      </td>
    </tr>
    </tbody>
  </table>
</div>

