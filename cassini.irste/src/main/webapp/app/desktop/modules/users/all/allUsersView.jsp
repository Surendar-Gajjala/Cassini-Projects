<style>
    .panel.panel-default .panel-heading {
        background-color: #FFF !important;
        padding: 0px;
        height: 50px;
        border: 1px solid #DDD;
    }
    #freeTextSearchDirective {
        top: 8px !important;
    }
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    a.activeView {
        text-decoration: underline;
        font-weight: bold;
        color: #121C25;
    }
</style>
<div class="view-container" applicationfitcontent>
    <div class="view-toolbar">
        <button class="btn btn-sm btn-default min-width" ng-click="allUsersVm.newResponder()"
        <%--ng-if="allUsersVm.usersView == 'Responder' && allUsersVm.usersView != 'Facilitator' && allUsersVm.usersView != 'Assistor'"--%>>
            New {{allUsersVm.usersView}}
        </button>
        <p class="blink" ng-if="allUsersVm.showSearchMode == true"
           style="color: blue;text-align: center;margin-top: -25px;font-size: 16px;">View is in search mode</p>
        <free-text-search on-clear="allUsersVm.resetPage" search-term="allUsersVm.searchText"
                          on-search="allUsersVm.freeTextSearch"></free-text-search>
      <%--  <p class="blink" ng-if="allUsersVm.showSearchMode == true"
           style="color: blue;text-align: center;margin-top: -25px;font-size: 16px;">View is in search mode</p>
        <free-text-search on-clear="allUsersVm.resetPage" on-search="allUsersVm.freeTextSearch"></free-text-search>--%>
        <%--<button class="btn btn-sm btn-default min-width" ng-click="allUsersVm.newResponder('Facilitator')"
                ng-if="allUsersVm.usersView != 'Responder' && allUsersVm.usersView == 'Facilitator' && allUsersVm.usersView != 'Assistor'">
            New Facilitator
        </button>
        <button class="btn btn-sm btn-default min-width" ng-click="allUsersVm.newResponder('Assistor')"
                ng-if="allUsersVm.usersView != 'Responder' && allUsersVm.usersView != 'Facilitator' && allUsersVm.usersView == 'Assistor'">
            New Assistor
        </button>--%>
        <form style="margin-top: -23px;margin-left: 160px;">
            <label class="radio-inline">
                <a ng-class="{'activeView':allUsersVm.usersView == 'Responder'}"
                   ng-click="allUsersVm.filterUsers('Responder')">Responders</a>
            </label>
            <label class="radio-inline">
                <a ng-class="{'activeView':allUsersVm.usersView == 'Assistor'}"
                   ng-click="allUsersVm.filterUsers('Assistor')">Assistors</a>
            </label>
            <label class="radio-inline">
                <a ng-class="{'activeView':allUsersVm.usersView == 'Facilitator'}"
                   ng-click="allUsersVm.filterUsers('Facilitator')">Facilitators</a>
            </label>


        </form>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div style="padding: 20px">
            <div class="panel panel-default panel-alt widget-messaging">
                <div class="panel-heading" style="">

                    <div ng-if="allUsersVm.usersView == 'Responder'"
                         style="margin-top: 15px;margin-left: 15px;color: #002451;font-weight: bold;font-size: 20px;">
                        <span>Responders</span>
                    </div>
                    <div ng-if="allUsersVm.usersView == 'Assistor'"
                         style="margin-top: 15px;margin-left: 15px;color: #002451;font-weight: bold;font-size: 20px;">
                        <span>Assistors</span>
                    </div>
                    <div ng-if="allUsersVm.usersView == 'Facilitator'"
                         style="margin-top: 15px;margin-left: 15px;color: #002451;font-weight: bold;font-size: 20px;">
                        <span>Facilitators</span>
                    </div>


                    <div class="pull-right text-center" style="padding: 10px;margin-top: -35px;">
                        <div>
                            <span ng-if="allUsersVm.loading == false">
                                <medium>
                                    <span style="margin-right: 10px;">
                                        Displaying {{allUsersVm.users.numberOfElements}} of
                                            {{allUsersVm.users.totalElements}}
                                    </span>
                                </medium>
                            </span>
                            <span class="mr10">Page {{allUsersVm.users.totalElements != 0 ? allUsersVm.users.number+1:0}} of {{allUsersVm.users.totalPages}}</span>
                            <a href="" ng-click="allUsersVm.previousPage()"
                               ng-class="{'disabled': allUsersVm.users.first}"><i
                                    class="fa fa-arrow-circle-left mr10"></i></a>
                            <a href="" ng-click="allUsersVm.nextPage()"
                               ng-class="{'disabled': allUsersVm.users.last}"><i
                                    class="fa fa-arrow-circle-right"></i></a>
                        </div>
                    </div>
                </div>
                <div class="widget-panel" style="overflow-y: auto">
                    <div class="responsive-table" style="padding: 10px;">
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr>
                                <th>Full Name</th>
                                <th>Designation</th>
                                <th>Phone Number</th>
                                <th>Email</th>
                                <th>Created Date</th>
                                <th>Login</th>
                                <th>Utilities</th>
                                <th align="center" style="width: 100px;text-align: center;">Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-if="allUsersVm.loading == true">
                                <td colspan="10">
                            <span style="font-size: 15px;">
                                <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                     class="mr5">Loading Records...
                            </span>
                                </td>
                            </tr>

                            <tr ng-if="allUsersVm.loading == false && allUsersVm.users.content.length == 0">
                                <td colspan="10" style="padding-left: 11px;">No Records....</td>
                            </tr>

                            <tr ng-repeat="user in allUsersVm.users.content">
                                <td>  <span
                                        ng-bind-html="user.fullName | highlightText: freeTextQuery"></span><%--{{user.fullName}}--%></td>
                                <td><span
                                        ng-bind-html="user.designation | highlightText: freeTextQuery"></span><%--{{user.designation}}--%></td>
                                <td><span
                                        ng-bind-html="user.phoneMobile | highlightText: freeTextQuery"></span><%--{{user.phoneMobile}}--%></td>
                                <td><span
                                        ng-bind-html="user.email | highlightText: freeTextQuery"></span><%--{{user.email}}--%></td>
                                <td><span
                                        ng-bind-html="user.createdDate | highlightText: freeTextQuery"></span><%--{{user.createdDate}}--%></td>
                                <td>
                                    <span
                                            ng-bind-html="user.login | highlightText: freeTextQuery"><%--{{user.login}}--%></span>
                                </td>
                               <%-- <td><span ng-repeat="utility in user.utilities">
                                    {{utility}}
                                </span></td>--%>
                                <td><span
                                        ng-bind-html="user.utilities.length > 30 ? user.utilities.trunc(30, true) : user.utilities | highlightText: freeTextQuery" title="{{user.utilities}}"><%--{{user.utilities | limitTo: 30 }}{{user.utilities.length > 30 ? '...' : ''}}--%></span>
                                </td>
                                <td align="center" style="width: 100px;">
                                   <%-- <button ng-enabled='loginPersonDetails.person.id == 1' title="Edit"
                                            class="btn btn-xs btn-warning"
                                            ng-click="allUsersVm.editResponder(user)">
                                        <i class="fa fa-edit"></i>
                                    </button>--%>
                                    <button ng-if='loginPersonDetails.person.id == 1'
                                            title="Delete" class="btn btn-xs btn-danger"
                                            ng-click="allUsersVm.deleteResponder(user)">
                                        <i class="fa fa-trash"></i>
                                    </button>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
