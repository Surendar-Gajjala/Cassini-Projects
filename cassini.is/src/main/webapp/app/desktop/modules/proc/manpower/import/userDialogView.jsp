<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row">
            <div>
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Manpower Type :<span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">

                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                                        Select <span class="caret" style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 380px;">
                                            <manpower-tree on-select-type="userDialogVm.onSelectType"></manpower-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="userDialogVm.selectedManpowerType.name"
                                       readonly>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <div class="row" style="margin: 0;">
            <div>
                <br>
                <table class="table table-striped">

                    <thead>
                    <th style="vertical-align: middle;">
                        <div class="ckbox ckbox-default" style="display: inline-block; margin-left: 22px">
                            <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                   ng-model="userDialogVm.selectedAll"
                                   ng-click="userDialogVm.checkAll(userDialogVm.selectedAll)"
                                   ng-if="userDialogVm.persons.length > 1">
                            <label for="item{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </th>
                    <%--<th style="vertical-align: middle;">Emp ID</th>--%>
                    <th style="vertical-align: middle;">Name</th>
                    <th style="vertical-align: middle;">Phone</th>
                    <th style="vertical-align: middle;">Email</th>
                    </thead>

                    <tbody>
                    <tr ng-if="userDialogVm.logins.length == 0 && userDialogVm.selectedManpowerType != null">
                        <td colspan="11" style="padding-left: 30px;">No Users</td>
                    </tr>
                    <tr ng-if="userDialogVm.persons.length == 0 && userDialogVm.selectedManpowerType == null">
                        <td colspan="11" style="padding-left: 30px;">Please select Manpower Type</td>
                    </tr>
                    <tr ng-if="userDialogVm.logins.length > 0 && userDialogVm.persons.length == 0 && userDialogVm.selectedManpowerType != null">
                        <td colspan="11" style="padding-left: 30px;">All users were added for the selected Manpower
                            Type
                        </td>
                    </tr>
                    <tr ng-repeat="person in userDialogVm.persons" ng-if="userDialogVm.selectedManpowerType != null">
                        <th style="width: 80px; text-align: center">
                            <div class="ckbox ckbox-default" style="display: inline-block;">
                                <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                       ng-click="userDialogVm.select(person)"
                                       ng-model="person.selected">
                                <label for="item{{$index}}" class="item-selection-checkbox"></label>
                            </div>
                        </th>
                        <%--<td style="vertical-align: middle;">{{person.itemNumber}}</td>--%>
                        <td style="vertical-align: middle;">{{person.person.fullName}}</td>
                        <td style="vertical-align: middle;">{{person.person.phoneMobile}}</td>
                        <td style="vertical-align: middle;">{{person.person.email}}</td>
                    </tr>
                    </tbody>
                </table>

                <br>
                <br>
            </div>
        </div>
    </div>
</div>