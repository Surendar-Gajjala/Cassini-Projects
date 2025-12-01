<style>
    .responsive-table .dropdown-content {
        display: none;
        position: absolute;
        background-color: #fff !important;
        min-width: 100px;
        box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
        z-index: 1 !important;
        border-radius: 5px;
        text-align: left;
        color: black !important;
        margin-top: -45px;

    }

    .responsive-table .dropdown-content a {
        text-decoration: none;
        display: block;
        color: black !important;
    }

    .responsive-table .dropdown-content a:hover {
        background-color: #0f3ff3;
        color: white !important;

    }

    .responsive-table .dropdown-content i:hover {
        background-color: #0f3ff3;
        color: white !important;

    }

    th.actions-column, td.actions-column {
        width: 150px;
        text-align: center;
    }

    .responsive-table .dropdown:hover .dropdown-content {
        display: block;
        color: black !important;
    }

    .responsive-table .dropdown-content {
        margin-left: -35px !important;
    }

    .ui-select-bootstrap > .ui-select-choices {
        max-height: 99px !important;
        overflow-x: hidden !important;
        /* background: blue; */
        /*margin-top: -20vh;*/
        z-index: 1 !important;
    }

    table .ui-select-choices {
        position: absolute !important;
    }

</style>

<div class="responsive-table" style="padding: 10px; overflow-y: auto;height: 100%;">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th>Name</th>
            <th>Phone</th>
            <th>Email</th>
            <th>Role</th>
            <th style="width: 150px;" class="actions-column">Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="personVm.loading == true">
            <td colspan="11">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading Persons...
                        </span>
            </td>
        </tr>

        <tr ng-if="personVm.loading == false && personVm.projectPersons.content.length == 0">
            <td colspan="11">No Persons are available to view</td>
        </tr>
        <tr ng-repeat="projectPerson in personVm.projectPersons.content">
            <td style="vertical-align: middle;cursor: pointer">
                <a href="" title="Edit this person"
                   ng-click="personVm.personDetails(projectPerson)">{{projectPerson.personObject.fullName}}</a>
            </td>
            <td style="vertical-align: middle;">{{projectPerson.personObject.phoneMobile}}</td>
            <td style="vertical-align: middle;">{{projectPerson.personObject.email}}</td>
            <td style="vertical-align: middle;width:20%">
                <div class="choices">
                    <ui-select multiple ng-model="projectPerson.roles" theme="bootstrap"
                               ng-disabled="!projectPerson.editMode"
                               on-remove="personVm.removeRole(projectPerson, $item)"
                               on-select="personVm.selectRole(projectPerson, $item)">
                        <ui-select-match placeholder="Select">
                            {{$item.roleObject.role}}
                        </ui-select-match>
                        <ui-select-choices repeat="role in projectPerson.roleList | filter: $select.search">
                            <div ng-bind="role.role"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>

            </td>
            <%--<td class="actions-column" style="text-align: center;">
                &lt;%&ndash;<div class="btn-group">
                    <button ng-if="(projectPerson.editMode == true && projectPerson.showValues == false)"
                            class="btn btn-xs btn-success"
                            ng-click="personVm.applyChanges(projectPerson)" title="Apply Changes"><i
                            class="fa fa-check"></i>
                    </button>
                    <button ng-if="(projectPerson.editMode == true && projectPerson.showValues == false)"
                            class="btn btn-xs btn-default"
                            ng-click="personVm.cancelChanges(projectPerson)" title="Cancel Changes"><i
                            class="fa fa-times"></i>
                    </button>

                    <button ng-if="(projectPerson.showValues == true || projectPerson.editMode == false)"
                            title="Edit this person"
                            class="btn btn-xs btn-warning"
                            ng-disabled="selectedProject.locked == true || !(hasPermission('permission.team.editTeam') || login.person.isProjectOwner)"
                            ng-click="personVm.editPerson(projectPerson)"><i class="fa fa-edit"></i>
                    </button>

                    <button ng-if="(projectPerson.showValues == true || projectPerson.editMode == false)"
                            title="Delete this person"
                            class="btn btn-xs btn-danger"
                            ng-disabled="selectedProject.locked == true || !(hasPermission('permission.team.editTeam') || login.person.isProjectOwner)"
                            ng-click="personVm.deleteProjectPerson(projectPerson)"><i class="fa fa-trash"></i>
                    </button>
                </div>&ndash;%&gt;



                <div class="dropdown">
                    <i class="fa fa-ellipsis-v" aria-hidden="true"></i>

                    <div class="dropdown-content"  style="color: black !important;padding: 5px;" >
                        <a ng-if="(projectPerson.editMode == true && projectPerson.showValues == false)"
                           style="color: black !important;padding: 5px;"
                           ng-click="personVm.applyChanges(projectPerson)" title="Apply Changes">
                            <i class="fa fa-check" style="color: black !important;margin: 2px 9px;"></i><span>Save </span>
                        </a>
                        <a  ng-if="(projectPerson.editMode == true && projectPerson.showValues == false)"
                           style="color: black !important;padding: 5px;"
                            ng-click="personVm.cancelChanges(projectPerson)" title="Cancel Changes">
                            <i class="fa fa-times" style="color: black !important;margin: 2px 9px"></i><span>Cancel </span>
                        </a>
                        <a ng-if="(projectPerson.showValues == true || projectPerson.editMode == false)"
                           style="color: black !important;padding: 5px;"
                           title="Edit this person"
                           ng-disabled="selectedProject.locked == true || !(hasPermission('permission.team.editTeam') || login.person.isProjectOwner)"
                           ng-click="personVm.editPerson(projectPerson)">
                            <i class="fa fa-edit" style="color: black !important;margin: 2px 9px;"></i><span>Edit </span>
                        </a>
                        <a ng-if="(projectPerson.showValues == true || projectPerson.editMode == false)"
                           style="color: black !important;padding: 5px;"
                           title="Delete this person"
                           ng-disabled="selectedProject.locked == true || !(hasPermission('permission.team.editTeam') || login.person.isProjectOwner)"
                           ng-click="personVm.deleteProjectPerson(projectPerson)">
                            <i class="fa fa-trash" style="color: black !important;margin: 2px 9px"></i><span>Delete </span>
                        </a>
                    </div>
                </div>

            </td>--%>


            <td class="text-center">
                                    <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                        style="z-index: 9999 !important;">
                                        <li ng-click="personVm.applyChanges(projectPerson)">
                                            <a class="dropdown-item" title="Apply Changes"
                                               ng-if="(projectPerson.editMode == true && projectPerson.showValues == false)">
                                                <span style="padding-left: 3px;">Save</span>
                                            </a></li>
                                        <li ng-click="personVm.cancelChanges(projectPerson)">
                                            <a class="dropdown-item" type="button"
                                               ng-if="(projectPerson.editMode == true && projectPerson.showValues == false)"
                                               title="Cancel Changes">
                                                <span style="padding-left: 3px;">Cancel</span>
                                            </a>
                                        </li>
                                        <li ng-click="personVm.editPerson(projectPerson)"
                                            ng-if="(projectPerson.showValues == true || projectPerson.editMode == false)">
                                            <a title="Edit this person" type="button"
                                               class="dropdown-item"
                                               ng-disabled="selectedProject.locked == true || !(hasPermission('permission.team.editTeam') || login.person.isProjectOwner)">
                                                <span style="padding-left: 3px;">Edit</span>
                                            </a>
                                        </li>
                                        <li ng-click="personVm.deleteProjectPerson(projectPerson)"
                                            ng-if="(projectPerson.showValues == true || projectPerson.editMode == false)">
                                            <a title="Delete this person"
                                               type="button"
                                               class="dropdown-item"
                                               ng-disabled="selectedProject.locked == true || !(hasPermission('permission.team.editTeam') || login.person.isProjectOwner)">
                                                <span style="padding-left: 3px;">Delete</span>
                                            </a>
                                        </li>
                                    </ul>
                                        </span>
            </td>


        </tr>
        </tbody>
    </table>
</div>