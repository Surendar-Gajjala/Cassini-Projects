<style>
    .panel-heading {
        height: 50px !important;
        background-color: white !important;
        border: 1px solid lightgrey !important;
        padding: 8px;
        color: #42526E !important;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .view-toolbar .btn {
        height: 30px !important;
        background-color: #0275d8 !important;
        color: #fff !important;
        border: 0 !important;
        font-weight: bold !important;
        font-size: 14px !important;
        border-color: #255625 !important;
    }

    .view-toolbar .btn:hover {
        background-color: #0275d8 !important;
        border-radius: 3px !important;
    }
</style>

<div class="panel panel-primary">
    <div class="view-toolbar">
        <div class="row" ng-if="hasPermission('admin','group','add')">
            <div class="col-sm-1" style="width: 40px;">
                <div class="btn-group">
                    <button class="btn btn-sm btn-success" ng-click="userVm.selectType('GROUP')"
                            title="{{newGroupTitle}}" translate>NEW_GROUP
                        <%--<i class="la la-plus" style="" aria-hidden="true"></i>--%>
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div id="admin-rightView-bar" class="panel-heading">
        <div class="col-md-12">
            <div class="col-md-4">
                <h3 style="margin: 0px;" translate>GROUPS</h3>
            </div>
            <div class="col-md-8" style="top: 5px;">
                <div class="pull-right text-center">
                    <div>
                        <span>
                           <medium>
                                <span><span translate>DISPLAYING</span>{{userVm.groups.numberOfElements}} of
                                            {{userVm.groups.totalElements}}
                                    </span>
                           </medium>
                        </span>
                        <span class="mr10"><span translate>PAGE</span> {{userVm.groups.totalElements != 0 ? userVm.groups.number+1:0}} of {{userVm.groups.totalPages}}</span>
                        <a href="" ng-click="userVm.previousGroupsPage()"
                           ng-class="{'disabled': userVm.groups.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="userVm.nextGroupsPage()"
                           ng-class="{'disabled': userVm.groups.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="admin-rightView" class="panel-body" style="font-size: 14px;">
        <div class="responsive-table" style="padding: 5px;">
            <table class="table table striped highlight-row">
                <thead>
                <tr>
                    <th translate>NAME</th>
                    <th translate>DESCRIPTION</th>
                    <th translate>IS_ACTIVE</th>
                    <th translate>EXTERNAL</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-if="userVm.loadingGroups == true">
                    <td colspan="6">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5"><span translate>LOADING_GROUPS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="userVm.loadingGroups == false && userVm.groups.content.length == 0">
                    <td colspan="6"><span translate>NO_GROUPS</span></td>
                </tr>

                <tr ng-repeat="personGroup in userVm.groups.content">
                    <td>{{personGroup.name}}</td>
                    <td>{{personGroup.description}}</td>
                    <td>
                        <input ng-disabled="true" ng-value="true" type="checkbox"
                               ng-model="personGroup.isActive">
                    </td>
                    <td>
                        <input ng-disabled="true" ng-value="true" type="checkbox"
                               ng-model="personGroup.external">
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>