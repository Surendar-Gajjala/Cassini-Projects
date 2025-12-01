<div class="view-container" fitcontent>
    <div class="view-content no-padding" style="overflow: auto">
        <div class="item-details" style="padding: 30px">
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span>Name :</span>
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <a href="" editable-text="personDetailsVm.person.fullName"
                       ng-if="selectedProject.locked == false && (hasPermission('permission.team.editTeam') || login.person.isProjectOwner)"
                       onaftersave="personDetailsVm.updatePerson()">{{personDetailsVm.person.fullName}}</a>

                    <p ng-if="selectedProject.locked == false && !(hasPermission('permission.team.editTeam') || login.person.isProjectOwner)">
                        {{personDetailsVm.person.fullName}}</p>

                    <p ng-if="selectedProject.locked == true">{{personDetailsVm.person.fullName}}</p>
                </div>
            </div>
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span>Phone : </span>
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <a href="" editable-text="personDetailsVm.person.phoneMobile"
                       ng-if="selectedProject.locked == false && (hasPermission('permission.team.editTeam') || login.person.isProjectOwner)"
                       onaftersave="personDetailsVm.updatePerson()"> {{personDetailsVm.person.phoneMobile || 'Click to
                        enter phone'}}</a>

                    <p ng-if="selectedProject.locked == false && !hasPermission('permission.team.editTeam') && !login.person.isProjectOwner">
                        {{personDetailsVm.person.phoneMobile}}</p>

                    <p ng-if="selectedProject.locked == true">{{personDetailsVm.person.phoneMobile}}</p>
                </div>
            </div>
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span>Email : </span>
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <a href="" editable-text="personDetailsVm.person.email"
                       ng-if="selectedProject.locked == false && (hasPermission('permission.team.editTeam') || login.person.isProjectOwner)"
                       onaftersave="personDetailsVm.updatePerson()">
                        {{personDetailsVm.person.email || 'Click to enter email'}}</a>

                    <p ng-if="selectedProject.locked == false && !hasPermission('permission.team.editTeam') && !login.person.isProjectOwn">
                        {{personDetailsVm.person.email}}</p>

                    <p ng-if="selectedProject.locked == true">{{personDetailsVm.person.email}}</p>
                </div>
            </div>

            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span>Roles : </span>
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <ul ng-repeat="personRole in personDetailsVm.personRoles">
                        <li>{{personRole.roleObject.role}}</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>