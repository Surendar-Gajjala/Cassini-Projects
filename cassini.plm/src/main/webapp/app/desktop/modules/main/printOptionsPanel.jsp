<div style="padding: 20px; height: auto;">
    <div class="row">
        <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 30px; text-align: center"></th>
                    <th translate></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="option in options">
                    <td style="width: 30px; text-align: center">
                        <input name="selectedOptions[]" type="checkbox" value="{{fruitName}}" ng-model="option.selected"
                                >
                    </td>
                    <td style="vertical-align: middle;">
                        {{option.label}}
                    </td>
                </tr>
                </tbody>
            </table>

            <%--<div class="form-group">
                <label ng-repeat="fruit in options">
                    <input type="checkbox" name="selectedOptions[]" value="{{fruitName}}" ng-model="fruit.selected">
                    {{fruit.name}}
                </label>
            </div>--%>
        </div>
    </div>
    <br>
    <br>
</div>
