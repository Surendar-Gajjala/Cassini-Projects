<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>
<div style="padding: 0px 20px;">
    <h4 class="section-title" style="margin: 0px;" translate>FILTERS</h4>

    <form class="form-inline" style="margin: 5px 0px;">

        <free-text-search style=" width: 300px; margin-right: 10px; position: absolute;top: 15px;right: 30px;"
                          on-clear="manpowerDialogueVm.resetPage"
                          on-search="manpowerDialogueVm.freeTextSearch"></free-text-search>

    </form>

    <div class="row">
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-4">
                <div style="padding: 10px;">
                    <span style="color:#1877f2e6">Selected Items</span>
                    <span ng-if="manpowerDialogueVm.personView == true"
                          class="badge">{{manpowerDialogueVm.selectedPersons.length}}</span>
                    <span ng-if="manpowerDialogueVm.personView == false"
                          class="badge">{{manpowerDialogueVm.selectedRoles.length}}</span>
                </div>
            </div>
            <div class="col-md-8" ng-if="manpowerDialogueVm.projectPersons !=''">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div ng-if="manpowerDialogueVm.personView == true">
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{manpowerDialogueVm.projectPersons.numberOfElements}} of
                                            {{manpowerDialogueVm.projectPersons.totalElements}}
                                    </span>
                            </medium>
                        </span>
                        <span class="mr10">Page {{manpowerDialogueVm.projectPersons.totalElements != 0 ? manpowerDialogueVm.projectPersons.number+1:0}} of {{manpowerDialogueVm.projectPersons.totalPages}}</span>
                        <a href="" ng-click="manpowerDialogueVm.previousPage()"
                           ng-class="{'disabled': manpowerDialogueVm.projectPersons.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="manpowerDialogueVm.nextPage()"
                           ng-class="{'disabled': manpowerDialogueVm.projectPersons.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                    <div ng-if="manpowerDialogueVm.personView == false">
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{manpowerDialogueVm.projectRoles.numberOfElements}} of
                                            {{manpowerDialogueVm.projectRoles.totalElements}}
                                    </span>
                            </medium>
                        </span>
                        <span class="mr10">Page {{manpowerDialogueVm.projectRoles.totalElements != 0 ? manpowerDialogueVm.projectRoles.number+1:0}} of {{manpowerDialogueVm.projectRoles.totalPages}}</span>
                        <a href="" ng-click="manpowerDialogueVm.previousPage()"
                           ng-class="{'disabled': manpowerDialogueVm.projectRoles.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="manpowerDialogueVm.nextPage()"
                           ng-class="{'disabled': manpowerDialogueVm.projectRoles.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>

        </div>

        <div style="background-color: #f9f9f9;border: 1px solid #ddd;width: 250px;margin-left: auto;margin-right: auto;margin-top: 59px;text-align: center;padding-top: 5px;border-radius: 20px;">
            <div class="rdio rdio-primary" style="display: inline-block;margin-right: 10px;">
                <input id="tasks" name="task" type="radio" value="manPower"
                       ng-click="manpowerDialogueVm.changeView('manPower')" checked>
                <label for="tasks">ManPower</label>
            </div>

            <div class="rdio rdio-primary" style="display: inline-block;margin-right: 10px;">
                <input id="mytasks" name="task" type="radio" value="roles"
                       ng-click="manpowerDialogueVm.changeView('roles')">
                <label for="mytasks">Roles</label>
            </div>
        </div>


        <div ng-show="manpowerDialogueVm.personView == true" class="col-md-12"
             style="padding:0px; height: auto;overflow: auto;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="text-align: center;">
                        <input id="item1{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                               ng-model="manpowerDialogueVm.selectedAll"
                               ng-click="manpowerDialogueVm.checkAll()"
                               ng-if="manpowerDialogueVm.projectPersons.content.length > 1">
                    </th>
                    <th style="vertical-align: middle;">Name</th>
                    <th style="vertical-align: middle;">Phone Number</th>
                    <th style="vertical-align: middle;">Email</th>
                </tr>
                </thead>
                <tbody>


                <tr ng-if="manpowerDialogueVm.projectPersons.content.length == 0  || manpowerDialogueVm.projectPersons == ''">
                    <td colspan="11" style="padding-left: 30px;">No Manpower is available to view</td>
                </tr>


                <tr ng-repeat="projectPerson in manpowerDialogueVm.projectPersons.content"
                    ng-if="!materialDialogueVm.loading">

                    <th style="width: 50px; text-align: center">
                        <input id="item2{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                               ng-click="manpowerDialogueVm.select(projectPerson)"
                               ng-model="projectPerson.selected">
                    </th>
                    <td style="vertical-align: middle;"><span
                            ng-bind-html="projectPerson.personObject.fullName | highlightText: freeTextQuery"></span>
                    </td>
                    <td style="vertical-align: middle;"><span
                            ng-bind-html="projectPerson.personObject.phoneMobile | highlightText: freeTextQuery"></span>
                    </td>
                    <td style="vertical-align: middle;"><span
                            ng-bind-html="projectPerson.personObject.email | highlightText: freeTextQuery"></span></td>
                </tr>
                </tbody>
            </table>
        </div>


        <div ng-show="manpowerDialogueVm.personRoles == true" class="col-md-12"
             style="padding:0px; height: auto;overflow: auto;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="text-align: center;">
                        <input id="item3{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                               ng-model="manpowerDialogueVm.selectedAll"
                               ng-click="manpowerDialogueVm.checkAllRoles()"
                               ng-if="manpowerDialogueVm.projectRoles.content.length > 1">
                    </th>
                    <th style="vertical-align: middle;">Role</th>
                    <th style="vertical-align: middle;">Description</th>
                </tr>
                </thead>
                <tbody>


                <tr ng-if="manpowerDialogueVm.projectRoles.content.length == 0  || manpowerDialogueVm.projectRoles == ''">
                    <td colspan="11" style="padding-left: 30px;">No Roles are available to view</td>
                </tr>


                <tr ng-repeat="projectRole in manpowerDialogueVm.projectRoles.content">

                    <th style="width: 50px; text-align: center">
                        <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                               ng-click="manpowerDialogueVm.selectRole(projectRole)"
                               ng-model="projectRole.selected">
                    </th>
                    <td style="vertical-align: middle;"><span
                            ng-bind-html="projectRole.role | highlightText: freeTextQuery"></span>
                    </td>
                    <td style="vertical-align: middle;"><span
                            ng-bind-html="projectRole.description | highlightText: freeTextQuery"></span>
                    </td>

                </tr>
                </tbody>
            </table>
        </div>

    </div>
    <br>
    <br>
</div>
