<style scoped>
    .tabs {
        height: 50px;
        z-index: 1000;
        border-top: 1px solid #ddd;
        border-left: 1px solid #ddd;
        width: 100%;
        border-right: 1px solid #ddd;
        padding-left: 0;

    }

    .tabopen {
        border: 1px solid transparent !important;
        border-color: #ddd #ddd transparent #ddd !important;
        background-color: #fff;
        border-radius: 4px 4px 0 0 !important;
        border: 0 !important;
        border-color: #30a82a !important;
        border-bottom: 3px solid #2a6fa8 !important;
    }

    .tab {
        float: left;
        width: 75.9333px;
        height: 49.6px !important;
        padding-top: 15px;
        color: #636e7b;
        text-align: center;
        cursor: pointer;
        padding-right: 10px;
        padding-left: 10px;
        position: relative;
        margin-bottom: 0;

    }

    #communication-tab-content {
    }

    #communication-tab-content > div {
        height: 100%;
        padding: 10px;
    }

    .full-height {
        height: 100%;
    }

    .team-view {
        border-left: 1px solid #ddd;
        margin-left: 10px;
        margin-right: 10px;
        border-right: 1px solid #ddd;
        border-bottom: 1px solid #ddd;
        margin-bottom: 10px;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    span.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    /*.nav-tabs > .uib-tab.active > a, .nav-tabs > .uib-tab.active > a:focus, .nav-tabs > .uib-tab.active > a:hover {*/
    /*border: 0 !important;*/
    /*border-color: #30a82a !important;*/
    /*border-bottom: 3px solid #2a6fa8 !important;*/
    /*}*/
</style>


<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button ng-show="showFlag == true" class="btn btn-sm btn-default min-width"
                    ng-click="teamVm.back();">
                Back
            </button>
            <button ng-disabled="(selectedProject.locked == true) || !(hasPermission('permission.team.addPerson') || login.person.isProjectOwner) "
                    ng-if="teamVm.flag == false && showFlag == false" class="min-width btn btn-sm btn-success"
                    ng-click="teamVm.addPerson();">Add Person
            </button>
            <button ng-if="teamVm.flag == true && showFlag == false" class="min-width btn btn-sm btn-success"
                    ng-click="teamVm.newRole()"
                    ng-disabled="(selectedProject.locked == true) || !(login.person.isProjectOwner || hasPermission('permission.team.newRole'))">
                New Role
            </button>
        </div>
        <div class="pull-right text-center"
             style="padding: 10px;" <%--ng-if="teamVm.flag == false && showFlag == false"--%>>
            <div ng-if="teamVm.currentTab == 'Team' && teamVm.flag == false && showFlag == false">
                <span>
                    <medium>
                        <span style="margin-right: 10px;">
                              Displaying {{projectPersons.content.length}} of {{projectPersons.totalElements}}
                        </span>
                    </medium>
                </span>
                <span class="mr10">Page {{projectPersons.totalElements != 0 ? projectPersons.number+1:0}} of {{projectPersons.totalPages}}</span>
                <a href="" ng-click="teamVm.previousPage()"
                   ng-class="{'disabled': projectPersons.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="teamVm.nextPage()"
                   ng-class="{'disabled': projectPersons.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>

            <div ng-if="teamVm.currentTab != 'Team'">
                <span>
                    <medium>
                        <span style="margin-right: 10px;">
                              Displaying {{projectRoles.content.length}} of {{projectRoles.totalElements}}
                        </span>
                    </medium>
                </span>
                <span class="mr10">Page {{projectRoles.totalElements != 0 ? projectRoles.number+1:0}} of {{projectRoles.totalPages}}</span>
                <a href="" ng-click="teamVm.previousPage()"
                   ng-class="{'disabled': projectRoles.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="teamVm.nextPage()"
                   ng-class="{'disabled': projectRoles.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>

    </div>
    <div class="view-content no-padding" style="margin-top: 10px;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12">
                <div class="tabs" ng-if="showFlag == false">
                    <div class="tab" ng-repeat="tab in teamVm.tabs" id="{{tab.view}}"
                         ng-click="teamVm.setTabActive(tab)"
                         ng-class="{'tabopen':tab.view == teamVm.currentTab}" data="tab.state">{{tab.view}}
                    </div>
                </div>
            </div>
        </div>
        <div class="team-view" ui-view></div>
    </div>
</div>
