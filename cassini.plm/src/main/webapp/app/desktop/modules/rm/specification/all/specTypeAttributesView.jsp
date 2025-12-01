<div style="padding: 20px; height: auto;">
    <div class="row">
        <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="text-align: center">
                        <input name="item" type="checkbox" ng-model="specTypeAttributesVm.selectAllCheck"
                               ng-click="specTypeAttributesVm.selectAll(check);" ng-checked="check">
                    </th>
                    <th translate>ATTRIBUTE_NAME</th>
                    <th translate>OBJECT_TYPE</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="attribute in specTypeAttributesVm.customItemAttributes | orderBy: 'name'">
                    <th style="width: 80px; text-align: center">
                        <input ng-disabled="attribute.checked" name="attribute" type="checkbox" ng-model="attribute.checked"
                               ng-click="specTypeAttributesVm.selectCheck(attribute)">
                    </th>

                    <td style="vertical-align: middle;">
                        <span ng-bind-html="attribute.name"></span>
                    </td>
                    <td style="vertical-align: middle;">
                        {{attribute.objectType}}
                    </td>
                </tr>

                <%--<tr ng-repeat="attribute in specTypeAttributesVm.typeAttributes | orderBy: 'name'">
                    <th style="width: 80px; text-align: center">
                        <input name="attribute" type="checkbox" ng-model="attribute.checked"
                               ng-click="specTypeAttributesVm.selectCheck(attribute)">
                    </th>

                    <td style="vertical-align: middle;">
                        <span ng-bind-html="attribute.name"></span>
                    </td>
                    <td style="vertical-align: middle;">
                        {{attribute.objectType}}
                    </td>
                </tr>--%>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>
