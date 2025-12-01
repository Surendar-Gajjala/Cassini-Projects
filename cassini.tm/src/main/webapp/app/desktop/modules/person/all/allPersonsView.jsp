<div class="view-container">
    <div class="view-toolbar">
        <button ng-if="hasRole('Administrator') == true || isAdmin() == true"
                class="btn btn-sm btn-success" ui-sref="app.person.new">Create Person</button>
        <free-text-search on-clear="allPersonsVm.resetPage"
                          on-search="allPersonsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content">
            <div style="padding: 10px;">
                <div class="pull-right text-center">
                <span ng-if="allPersonsVm.loading == false"><small>pages {{allPersonsVm.persons.number+1}} of
                    {{allPersonsVm.persons.totalPages}}
                </small></span>
                    <br>
                    <div class="btn-group" style="margin-bottom: 0">
                        <button class="btn btn-xs btn-default"
                                ng-click="allPersonsVm.previousPage()"
                                ng-disabled="allPersonsVm.persons.first">
                            <i class="fa fa-chevron-left"></i>
                        </button>
                        <button class="btn btn-xs btn-default"
                                ng-click="allPersonsVm.nextPage()"
                                ng-disabled="allPersonsVm.persons.last">
                            <i class="fa fa-chevron-right"></i>
                        </button>
                    </div>
                    <br>
                    <span ng-if="allPersonsVm.loading == false"><small>{{allPersonsVm.persons.totalElements}}
                        persons
                    </small></span>
                </div>
                <div>
                    <a href="" class="btn btn-xs btn-danger" ng-if="allPersonsVm.clear == true"
                       ng-click="allPersonsVm.clearFilter()">Clear Filters</a>
                </div>
            </div>
        <table class="table table-striped">
            <thead>
            <tr>
                <th style="width: 200px;">Name</th>
                <th style="width: 200px;">Parent Unit</th>
                <th style="width: 200px;">Role</th>
                <th style="width: 200px;">Department</th>
                <th style="width: 200px;">Designation</th>
                <th style="width: 200px;">Phone Number</th>
                <th style="width: 200px;">Login Name</th>
                <th style="width: 200px;">Reporting Officer</th>
                <th style="width: 200px;">ReportingOfficer Contact</th>
                <th style="width: 200px;">Emergency Contact</th>
             <%--   <th style="width: 150px;">Actions</th>--%>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="person in allPersonsVm.persons.content | orderBy : 'fullName':true">
                <td><a href="" ng-click="allPersonsVm.showPersonDetails(person)">{{person.firstName+" "+person.lastName}}</a></td>
                <td>{{person.otherInfo.parentUnit}}</td>
                <td>{{person.otherInfo.role}}</td>
                <td>{{person.department.name}}</td>
                <td>{{person.otherInfo.designation}}</td>
                <td>{{person.phoneMobile}}</td>
                <td>{{person.loginName}}</td>
                <td>{{person.otherInfo.controllingOfficer}}</td>
                <td>{{person.otherInfo.controllingOfficerContact}}</td>
                <td>{{person.emergencyContact.phoneMobile}}</td>
              <%--  <td>
                    <button title="Delete Person" class="btn btn-xs btn-danger"
                            ng-click="allPersonsVm.deletePerson(person)"><i class="fa fa-trash"></i></button>
                </td>--%>
            </tr>
            </tbody>
        </table>
    </div>
</div>