<div style="padding: 20px; height: auto;">
    <div class="row">
        <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="text-align: center">
                        <input name="item" type="checkbox" ng-model="projectTypeAttributesVm.selectAllCheck"
                               ng-click="projectTypeAttributesVm.selectAll(check);" ng-checked="check">
                    </th>
                    <th translate>ATTRIBUTE_NAME</th>
                    <th translate>OBJECT_TYPE</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="attribute in projectTypeAttributesVm.customItemAttributes">
                    <th style="width: 80px; text-align: center">
                        <input ng-checked="attribute.checked" name="attribute" type="checkbox"
                               ng-model="attribute.checked"
                               ng-click="projectTypeAttributesVm.selectCheck(attribute)">
                    </th>

                    <td style="vertical-align: middle;">
                        {{attribute.name}}
                    </td>
                    <td style="vertical-align: middle;">
                        {{attribute.objectType}}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>
