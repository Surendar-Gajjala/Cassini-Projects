<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }
</style>
<div style="padding: 0px 10px;">
    <form class="form-inline ng-pristine ng-valid" style="margin: 10px 0px;display: flex;flex-direction: row;">
        <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
            <input type="text" ng-model="projectsSelectionVm.filters.searchQuery"
                   ng-change="projectsSelectionVm.searchProjects()"
                   style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;" placeholder="Name"
                   class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
        </div>
        <div style="margin-top: 0;flex-grow: 1;" class="">

            <!-- ngIf: projectsSelectionVm.clear == true -->
            <button ng-click="projectsSelectionVm.clearFilter()" translate="" style="height: 29px;width: 100%"
                    class="btn btn-xs btn-danger ng-scope" ng-if="projectsSelectionVm.clear">Clear
            </button>
            <!-- end ngIf: projectsSelectionVm.clear == true -->
        </div>
    </form>
    <div class="row">
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-4" style="padding-left: 0;padding-top: 10px;">
                <div style="">
                    <span style="" translate>SELECTED_PROJECTS</span>
                    <span class="badge">{{projectsSelectionVm.selectedProjects.length}}</span>
                </div>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                             <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="projectsSelectionVm.projects.numberOfElements ==0">
                            {{(projectsSelectionVm.pageable.page*projectsSelectionVm.pageable.size)}}
                        </span>

                                    <span ng-if="projectsSelectionVm.projects.numberOfElements > 0">
                            {{(projectsSelectionVm.pageable.page*projectsSelectionVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="projectsSelectionVm.projects.last ==false">{{((projectsSelectionVm.pageable.page+1)*projectsSelectionVm.pageable.size)}}</span>
                                    <span ng-if="projectsSelectionVm.projects.last == true">{{projectsSelectionVm.projects.totalElements}}</span>

                                 <span translate> OF </span>
                                {{projectsSelectionVm.projects.totalElements}}<span
                                        translate>AN</span>
                                </span>
                             </medium>
                        </span>
                        <span class="mr10">  Page {{projectsSelectionVm.projects.totalElements != 0 ?
                        projectsSelectionVm.projects.number+1:0}} <span translate>OF</span> {{projectsSelectionVm.projects.totalPages}}</span>
                        <a href="" ng-click="projectsSelectionVm.previousPage()"
                           ng-class="{'disabled': projectsSelectionVm.projects.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="projectsSelectionVm.nextPage()"
                           ng-class="{'disabled': projectsSelectionVm.projects.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="text-align: center;width: 20px !important;">
                        <input name="item" type="checkbox" ng-model="projectsSelectionVm.selectAllCheck"
                               ng-if="projectsSelectionVm.projects.content.length != 0"
                               ng-click="projectsSelectionVm.selectAll(check);" ng-checked="check">
                    </th>
                    <th style="width: 180px;" translate>NAME</th>
                    <th style="width: 150px;" translate>DESCRIPTION</th>
                    <th style="width: 200px;" translate>PROJECT_MANAGER</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="projectsSelectionVm.projects.content.length == 0">
                    <td colspan="15" translate>NO_PROJECTS</td>
                </tr>
                <tr ng-if="projectsSelectionVm.projects.content.length > 0"
                    ng-repeat="project in projectsSelectionVm.projects.content">
                    <td style="vertical-align: middle;text-align: center;width: 20px !important;">
                        <input name="item" type="checkbox" ng-model="project.selected"
                               ng-click="projectsSelectionVm.selectCheck(project)">
                    </td>
                    <td style="vertical-align: middle;" title="{{}}">
                        {{project.name}}
                    </td>
                    <td style="vertical-align: middle;">
                        {{project.description}}
                    </td>
                    <td style="vertical-align: middle;">
                        {{project.managerFullName}}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>
